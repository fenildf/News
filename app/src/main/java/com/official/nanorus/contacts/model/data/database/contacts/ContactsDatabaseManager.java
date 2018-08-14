package com.official.nanorus.contacts.model.data.database.contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.official.nanorus.contacts.entity.data.contact.Contact;
import com.official.nanorus.contacts.model.data.SuccessListener;
import com.official.nanorus.contacts.model.data.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class ContactsDatabaseManager {
    private static ContactsDatabaseManager instance;
    private DatabaseHelper databaseHelper;
    private ContactsDatabaseContract databaseContract;

    private final String TAG = this.getClass().getSimpleName();

    public void deleteContact(int id, SuccessListener successListener) {
        int result = databaseHelper.getWritableDB().delete(databaseContract.TABLE_NAME_CONTACTS,
                databaseContract.COLUMN_NAME_ID + " = " + id, null);
        if (result > 0) {
            successListener.onSuccess();
        } else {
            successListener.onFail();
        }
        databaseHelper.closeDB();
    }

    public void clearContacts(SuccessListener successListener) {
        int result = databaseHelper.getWritableDB().delete(databaseContract.TABLE_NAME_CONTACTS, "1", null);
        if (result > 0) {
            successListener.onSuccess();
        } else {
            successListener.onFail();
        }
        databaseHelper.closeDB();
    }

    public static ContactsDatabaseManager getInstance() {
        if (instance == null)
            instance = new ContactsDatabaseManager();
        return instance;
    }

    public ContactsDatabaseManager() {
        databaseHelper = DatabaseHelper.getInstance();
        databaseContract = new ContactsDatabaseContract();
    }

    public Observable<List<Contact>> getContacts() {
        Log.d(TAG, "getContacts()");
        return Observable.create(emitter -> {
            SQLiteDatabase db = databaseHelper.getReadableDB();
            Cursor cursor = db.rawQuery("SELECT * FROM " + databaseContract.TABLE_NAME_CONTACTS, null);
            List<Contact> contactList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    contactList.add(new Contact(
                            cursor.getInt(cursor.getColumnIndex(databaseContract.COLUMN_NAME_ID)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_NAME)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_SURNAME)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_PATRONYMIC)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_PHONE)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(databaseContract.COLUMN_NAME_CONTACTS_IMAGE))
                    ));
                    if (contactList.size() >= 15) {
                        emitter.onNext(contactList);
                        contactList = new ArrayList<>();
                    }
                } while (cursor.moveToNext());
                if (contactList.size() != 0) {
                    emitter.onNext(contactList);
                }
            }

            emitter.onComplete();
            cursor.close();
            databaseHelper.closeDB();
        });
    }

    public void putContact(Contact contact, SuccessListener successListener) {
        Log.d(TAG, "putContact()");
        SQLiteDatabase db = databaseHelper.getWritableDB();
        ContentValues cv = new ContentValues();
        if (contact.getName() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_NAME, contact.getName());
        if (contact.getSurname() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_SURNAME, contact.getSurname());
        if (contact.getPatronymic() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_PATRONYMIC, contact.getPatronymic());
        if (contact.getPhone() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_PHONE, contact.getPhone());
        if (contact.getEmail() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_EMAIL, contact.getEmail());
        if (contact.getImage() != null)
            cv.put(databaseContract.COLUMN_NAME_CONTACTS_IMAGE, contact.getImage());
        if (cv.size() > 0) {
            long result = db.insert(databaseContract.TABLE_NAME_CONTACTS, null, cv);
            if (result == -1)
                successListener.onFail();
            else
                successListener.onSuccess();

        }
        databaseHelper.closeDB();
        cv.clear();

    }

}
