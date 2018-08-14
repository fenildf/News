package com.official.nanorus.contacts.model.data.database.news;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.model.data.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class NewsDatabaseManager {
    private final String TAG = this.getClass().getSimpleName();

    private static NewsDatabaseManager instance;
    private DatabaseHelper databaseHelper;
    private NewsDatabaseContract databaseContract;

    public static NewsDatabaseManager getInstance() {
        if (instance == null)
            instance = new NewsDatabaseManager();
        return instance;
    }

    public NewsDatabaseManager() {
        databaseHelper = DatabaseHelper.getInstance();
        databaseContract = new NewsDatabaseContract();
    }

    public void putNews(List<News> newsList) {
        SQLiteDatabase db = databaseHelper.getWritableDB();
        ContentValues cv = new ContentValues();

        for (News news : newsList) {
            if (news.getTitle() != null) {
                cv.put(databaseContract.COLUMN_NAME_NEWS_TITLE, news.getTitle());
            }
            if (news.getDescription() != null) {
                cv.put(databaseContract.COLUMN_NAME_NEWS_DESCRIPTION, news.getDescription());
            }
            if (news.getUrl() != null) {
                cv.put(databaseContract.COLUMN_NAME_NEWS_URL, news.getUrl());
            }
            if (news.getUrlToImage() != null) {
                cv.put(databaseContract.COLUMN_NAME_NEWS_IMAGE_URL, news.getUrlToImage());
            }
            if (news.getPublishedAt() != null) {
                cv.put(databaseContract.COLUMN_NAME_NEWS_PUBLISHED_AT, news.getPublishedAt());
            }

            if (cv.size() > 0) {
                db.insert(databaseContract.TABLE_NAME_NEWS, null, cv);
                cv.clear();
            }
        }
        databaseHelper.closeDB();
    }

    public void clearNews(){
        SQLiteDatabase db = databaseHelper.getWritableDB();
        db.delete(databaseContract.TABLE_NAME_NEWS, null, null);

    }
    public Observable<List<News>> getNews() {
        Log.d(TAG, "getContacts()");
        return Observable.create(emitter -> {
            SQLiteDatabase db = databaseHelper.getReadableDB();
            Cursor cursor = db.rawQuery("SELECT * FROM " + databaseContract.TABLE_NAME_NEWS, null);
            List<News> newsList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    newsList.add(new News(
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_TITLE)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_URL)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_IMAGE_URL)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_PUBLISHED_AT))
                    ));
                    if (newsList.size() >= 15) {
                        emitter.onNext(newsList);
                        newsList = new ArrayList<>();
                    }
                } while (cursor.moveToNext());
                if (newsList.size() != 0) {
                    emitter.onNext(newsList);
                }
            }

            emitter.onComplete();
            cursor.close();
            databaseHelper.closeDB();
        });
    }

}
