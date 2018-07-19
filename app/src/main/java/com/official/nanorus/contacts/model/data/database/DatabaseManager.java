package com.official.nanorus.contacts.model.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.official.nanorus.contacts.entity.contact.Contact;

import rx.Emitter;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseHelper databaseHelper;
    private DatabaseContract databaseContract;

    private final String TAG = this.getClass().getName();

    public interface AddContactListener {

        void onSuccess();

        void onFail();

    }

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

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

    public void putContact(Contact contact, AddContactListener addContactListener) {
        Log.d(TAG, "putContact()");
        SQLiteDatabase db = databaseHelper.getWritableDB();
        ContentValues cv = new ContentValues();
        if (contact.getName() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getName());
        if (contact.getSurname() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getSurname());
        if (contact.getPatronymic() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getPatronymic());
        if (contact.getPhone() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getPhone());
        if (contact.getEmail() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getEmail());
        if (cv.size() > 0) {
            long result = db.insert(databaseContract.TABLE_NAME_CONTACTS, null, cv);
            if (result == -1)
                addContactListener.onFail();
            else
                addContactListener.onSuccess();

        }
        databaseHelper.closeDB();
        cv.clear();

    }

}
