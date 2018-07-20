package com.official.nanorus.contacts.model.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.official.nanorus.contacts.app.App;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    private DatabaseContract databaseContract;
    private static final String DB_NAME = "Database.db";
    private static final int DB_VERSION = 2;
    public static int DBConnectionsCount = 0;


    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public DatabaseHelper() {
        super(App.getApp().getApplicationContext(), DB_NAME, null, DB_VERSION);
        this.databaseContract = new DatabaseContract();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(databaseContract.SQL_CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i == 1 && i1 == 2) {
            sqLiteDatabase.execSQL("ALTER TABLE " + databaseContract.TABLE_NAME_CONTACTS + " ADD COLUMN " + databaseContract.COLUMN_NAME_CONTACTS_IMAGE + " TEXT");
        }
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
