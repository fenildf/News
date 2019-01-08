package com.official.nanorus.news.model.data.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.official.nanorus.news.app.App;
import com.official.nanorus.news.model.data.database.categories.CategoriesDatabaseContract;
import com.official.nanorus.news.model.data.database.news.NewsDatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    private NewsDatabaseContract newsDatabaseContract;
    private CategoriesDatabaseContract categoriesDatabaseContract;
    private static final String DB_NAME = "Database.db";
    private static final int DB_VERSION = 6;
    public static int DBConnectionsCount = 0;


    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public DatabaseHelper() {
        super(App.getApp().getApplicationContext(), DB_NAME, null, DB_VERSION);
        this.newsDatabaseContract = new NewsDatabaseContract();
        this.categoriesDatabaseContract = new CategoriesDatabaseContract();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(newsDatabaseContract.SQL_CREATE_TABLE_NEWS);
        sqLiteDatabase.execSQL(categoriesDatabaseContract.SQL_CREATE_TABLE_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion <= 2)
            sqLiteDatabase.execSQL(newsDatabaseContract.SQL_CREATE_TABLE_NEWS);
        if (oldVersion < 4)
            sqLiteDatabase.execSQL(categoriesDatabaseContract.SQL_CREATE_TABLE_CATEGORIES);
        if (oldVersion < 5)
            sqLiteDatabase.execSQL("ALTER TABLE " + newsDatabaseContract.TABLE_NAME_NEWS + " ADD " + newsDatabaseContract.COLUMN_NAME_NEWS_CATEGORY);
    }

    public SQLiteDatabase getReadableDB() {
        DBConnectionsCount++;
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDB() {
        DBConnectionsCount++;
        return this.getWritableDatabase();
    }

    public void closeDB() {
        DBConnectionsCount--;
        if (DBConnectionsCount == 0)
            this.close();
    }

}
