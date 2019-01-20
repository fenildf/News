package com.official.nanorus.news.model.data.database.news;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.model.data.Utils;
import com.official.nanorus.news.model.data.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

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

    public int putNews(List<News> newsList) {
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

            if (news.getPublishedAt() != null) {
                cv.put(databaseContract.COLUMN_NAME_NEWS_CATEGORY, news.getCategory());
            }

            if (cv.size() > 0) {
                db.insert(databaseContract.TABLE_NAME_NEWS, null, cv);
                cv.clear();
            }
        }
        databaseHelper.closeDB();
        return 0;
    }

    public int clearNews(int category) {
        SQLiteDatabase db = databaseHelper.getWritableDB();
        db.delete(databaseContract.TABLE_NAME_NEWS, databaseContract.COLUMN_NAME_NEWS_CATEGORY + " = " + category, null);
        databaseHelper.closeDB();
        return 0;
    }

    public Observable<List<News>> getNews(int category) {
        Log.d(TAG, "getContacts()");
        return Observable.create(emitter -> {
            SQLiteDatabase db = databaseHelper.getReadableDB();
            Cursor cursor = db.rawQuery("SELECT * FROM " + databaseContract.TABLE_NAME_NEWS +
                    " WHERE " + databaseContract.COLUMN_NAME_NEWS_CATEGORY + " = " + category, null);
            List<News> newsList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    newsList.add(new News(
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_TITLE)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_URL)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_IMAGE_URL)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_PUBLISHED_AT)),
                            cursor.getInt(cursor.getColumnIndex(databaseContract.COLUMN_NAME_NEWS_CATEGORY))
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


    public Single<Country> getCountry(String abbreviation) {
        Log.d(TAG, "getCountry()");
        SQLiteDatabase db = databaseHelper.getReadableDB();
        return Single.create(emitter -> {
            Cursor cursor = db.rawQuery("SELECT * FROM " + databaseContract.TABLE_NAME_COUNTRIES +
                            " WHERE " + databaseContract.COLUMN_NAME_COUNTRIES_ABBREVIATION + " = " + "'" + abbreviation + "'"
                            + " AND " + databaseContract.COLUMN_NAME_COUNTRIES_LANG + " = " + "'" + Utils.getAppLanguage() + "'"
                    , null);
            if (cursor.moveToFirst()) {
                Country country = new Country(
                        cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_COUNTRIES_NAME)),
                        cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_COUNTRIES_ABBREVIATION)));
                emitter.onSuccess(country);
            } else {
                emitter.onError(new Throwable("No country with abbreviation " + abbreviation));
            }
            cursor.close();
            databaseHelper.closeDB();
        });
    }


    public Observable<List<Country>> getCountries() {
        Log.d(TAG, "getCountries()");
        return Observable.create(emitter -> {
            SQLiteDatabase db = databaseHelper.getReadableDB();
            Cursor cursor = db.rawQuery("SELECT * FROM " + databaseContract.TABLE_NAME_COUNTRIES +
                    " WHERE " + databaseContract.COLUMN_NAME_COUNTRIES_LANG + " = '" + Utils.getAppLanguage() + "'", null);
            List<Country> countryList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    countryList.add(new Country(
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_COUNTRIES_NAME)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_COUNTRIES_ABBREVIATION))));
                    if (countryList.size() >= 15) {
                        emitter.onNext(countryList);
                        countryList = new ArrayList<>();
                    }
                } while (cursor.moveToNext());
                if (countryList.size() != 0) {
                    emitter.onNext(countryList);
                }
            }

            emitter.onComplete();
            cursor.close();
            databaseHelper.closeDB();
        });

    }

    public void insertDefaultCountries() {
        SQLiteDatabase db = databaseHelper.getWritableDB();
        db.execSQL(databaseContract.SQL_FIRST_INSERT_COUNTRIES);
        databaseHelper.closeDB();
    }

    public void clearCountries() {
        SQLiteDatabase db = databaseHelper.getWritableDB();
        db.delete(databaseContract.TABLE_NAME_COUNTRIES, null, null);
        databaseHelper.closeDB();
    }
}
