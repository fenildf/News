package com.official.nanorus.contacts.model.data.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.official.nanorus.contacts.app.App;
import com.official.nanorus.contacts.model.data.database.contacts.ContactsDatabaseContract;
import com.official.nanorus.contacts.model.data.database.news.NewsDatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    private ContactsDatabaseContract contactsDatabaseContract;
    private NewsDatabaseContract newsDatabaseContract;
    private static final String DB_NAME = "Database.db";
    private static final int DB_VERSION = 3;
    public static int DBConnectionsCount = 0;


    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public DatabaseHelper() {
        super(App.getApp().getApplicationContext(), DB_NAME, null, DB_VERSION);
        this.contactsDatabaseContract = new ContactsDatabaseContract();
        this.newsDatabaseContract = new NewsDatabaseContract();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(contactsDatabaseContract.SQL_CREATE_TABLE_CONTACTS);
        sqLiteDatabase.execSQL(newsDatabaseContract.SQL_CREATE_TABLE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            sqLiteDatabase.execSQL("ALTER TABLE " + contactsDatabaseContract.TABLE_NAME_CONTACTS + " ADD COLUMN " + contactsDatabaseContract.COLUMN_NAME_CONTACTS_IMAGE + " TEXT");
            sqLiteDatabase.execSQL(newsDatabaseContract.SQL_CREATE_TABLE_NEWS);
        }
        if (oldVersion == 2 && newVersion == 3) {
            sqLiteDatabase.execSQL(newsDatabaseContract.SQL_CREATE_TABLE_NEWS);
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
