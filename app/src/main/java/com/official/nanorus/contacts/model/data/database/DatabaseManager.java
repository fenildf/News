package com.official.nanorus.contacts.model.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.official.nanorus.contacts.entity.contact.Contact;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;


public class DatabaseManager {
    private DatabaseHelper databaseHelper;
    private DatabaseContract databaseContract;

    private final String TAG = this.getClass().getName();

    public DatabaseManager() {
        databaseHelper = DatabaseHelper.getInstance();
        databaseContract = new DatabaseContract();
    }

    public Observable<Contact> getContacts() {
        Log.d(TAG, "getContacts()");
        return Observable.create(contactEmitter -> {
            SQLiteDatabase db = databaseHelper.getReadableDB();
            Cursor cursor = db.rawQuery("SELECT * FROM " + databaseContract.TABLE_NAME_CONTACTS, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact(
                            cursor.getInt(cursor.getColumnIndex(databaseContract.COLUMN_NAME_ID)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_NAME)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_SURNAME)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_PATRONYMIC)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_PHONE)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_EMAIL)),
                            null
                    );
                    contactEmitter.onNext(contact);
                } while (cursor.moveToNext());
            }
            contactEmitter.onCompleted();
            cursor.close();
            databaseHelper.closeDB();
        }, Emitter.BackpressureMode.BUFFER);
    }

    public void putContact(Contact contact) {
        Log.d(TAG, "putContact()");
        SQLiteDatabase db = databaseHelper.getWritableDB();
        ContentValues cv = new ContentValues();
        cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getName());
        cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getSurname());
        cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getPatronymic());
        cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getPhone());
        cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getEmail());
        db.insert(databaseContract.TABLE_NAME_CONTACTS, null, cv);
        databaseHelper.closeDB();
        cv.clear();

    }

}
