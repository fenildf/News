package com.official.nanorus.news.model.data.database.news;

public class NewsDatabaseContract {

    public final String TABLE_NAME_NEWS = "news";
    public final String COLUMN_NAME_NEWS_CATEGORY = "category";
    public final String COLUMN_NAME_NEWS_TITLE = "title";
    public final String COLUMN_NAME_NEWS_DESCRIPTION = "description";
    public final String COLUMN_NAME_NEWS_PUBLISHED_AT = "published_at";
    public final String COLUMN_NAME_NEWS_URL = "url";
    public final String COLUMN_NAME_NEWS_IMAGE_URL = "image_url";

    public final String COLUMN_NAME_ID = "_id";
    public final String COMMA = ",";

    public final String SQL_CREATE_TABLE_NEWS = "CREATE TABLE " + TABLE_NAME_NEWS + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
            COLUMN_NAME_NEWS_TITLE + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_DESCRIPTION + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_PUBLISHED_AT + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_URL + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_IMAGE_URL + " TEXT" +
            ")";

    public final String SQL_DELETE_TABLE_NEWS = "DROP TABLE IF EXISTS " + TABLE_NAME_NEWS;
}
