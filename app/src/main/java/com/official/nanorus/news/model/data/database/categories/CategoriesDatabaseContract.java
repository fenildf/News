package com.official.nanorus.news.model.data.database.categories;

public class CategoriesDatabaseContract {

    public final String TABLE_NAME_CATEGORIES = "categories";
    public final String COLUMN_NAME_CATEGORY_NAME = "name";
    public final String COLUMN_NAME_CATEGORY_IMAGE = "image";


    public final String COLUMN_NAME_ID = "_id";
    public final String COMMA = ",";

    public final String SQL_CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_NAME_CATEGORIES + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
            COLUMN_NAME_CATEGORY_NAME + " TEXT" + COMMA +
            COLUMN_NAME_CATEGORY_IMAGE + " TEXT" +
            ")";

    public final String SQL_DELETE_TABLE_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES;
}
