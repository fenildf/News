package com.official.nanorus.contacts.model.data.database;

public class DatabaseContract {

    public final String TABLE_NAME_CONTACTS = "contacts";
    public final String COLUMN_NAME_CONTACTS_NAME = "name";
    public final String COLUMN_NAME_CONTACTS_SURNAME = "surname";
    public final String COLUMN_NAME_CONTACTS_PATRONYMIC = "patronymic";
    public final String COLUMN_NAME_CONTACTS_PHONE = "phone";
    public final String COLUMN_NAME_CONTACTS_EMAIL = "email";
    public final String COLUMN_NAME_CONTACTS_IMAGE = "image";

    public final String COLUMN_NAME_ID = "_id";
    public final String COMMA = ",";

    public final String SQL_CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
            COLUMN_NAME_CONTACTS_NAME + " TEXT" + COMMA +
            COLUMN_NAME_CONTACTS_SURNAME + " TEXT" + COMMA +
            COLUMN_NAME_CONTACTS_PATRONYMIC + " TEXT" + COMMA +
            COLUMN_NAME_CONTACTS_PHONE + " TEXT" + COMMA +
            COLUMN_NAME_CONTACTS_EMAIL + " TEXT" + COMMA +
            COLUMN_NAME_CONTACTS_IMAGE + " TEXT" +
            ")";

    public final String SQL_DELETE_TABLE_WEATHER = "DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS;


}
