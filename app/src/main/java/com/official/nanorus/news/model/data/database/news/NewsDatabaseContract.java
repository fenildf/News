package com.official.nanorus.news.model.data.database.news;

import com.official.nanorus.news.R;
import com.official.nanorus.news.app.App;

public class NewsDatabaseContract {

    public final String TABLE_NAME_NEWS = "news";
    public final String COLUMN_NAME_NEWS_CATEGORY = "category";
    public final String COLUMN_NAME_NEWS_TITLE = "title";
    public final String COLUMN_NAME_NEWS_DESCRIPTION = "description";
    public final String COLUMN_NAME_NEWS_PUBLISHED_AT = "published_at";
    public final String COLUMN_NAME_NEWS_URL = "url";
    public final String COLUMN_NAME_NEWS_IMAGE_URL = "image_url";


    public final String TABLE_NAME_COUNTRIES = "countries";
    public final String COLUMN_NAME_COUNTRIES_NAME = "name";
    public final String COLUMN_NAME_COUNTRIES_ABBREVIATION = "abbreviation";
    public final String COLUMN_NAME_COUNTRIES_LANG = "lang";

    public final String COLUMN_NAME_ID = "_id";
    public final String COMMA = ",";

    public final String SQL_CREATE_TABLE_NEWS = "CREATE TABLE " + TABLE_NAME_NEWS + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
            COLUMN_NAME_NEWS_TITLE + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_DESCRIPTION + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_PUBLISHED_AT + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_URL + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_IMAGE_URL + " TEXT" + COMMA +
            COLUMN_NAME_NEWS_CATEGORY + " INTEGER" +
            ")";

    public final String SQL_CREATE_TABLE_COUNTRIES = "CREATE TABLE " + TABLE_NAME_COUNTRIES + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
            COLUMN_NAME_COUNTRIES_NAME + " TEXT" + COMMA +
            COLUMN_NAME_COUNTRIES_ABBREVIATION + " TEXT" + COMMA +
            COLUMN_NAME_COUNTRIES_LANG + " TEXT" +
            ")";


    public final String SQL_FIRST_INSERT_COUNTRIES = "INSERT INTO " + TABLE_NAME_COUNTRIES + " (" +
            COLUMN_NAME_COUNTRIES_NAME + ", " + COLUMN_NAME_COUNTRIES_ABBREVIATION + ", " + COLUMN_NAME_COUNTRIES_LANG + ")" +
            "VALUES('Russian Federation', 'ru', 'en')," +
            "('Ukraine', 'ua', 'en')," +
            "('United States', 'us', 'en')," +
            "('Japan', 'jp', 'en')," +
            "('Germany', 'de', 'en')," +

            "('Российская Федерация', 'ru', 'ru')," +
            "('Украина', 'ua', 'ru')," +
            "('США', 'us', 'ru')," +
            "('Япония', 'jp', 'ru')," +
            "('Германия', 'de', 'ru')," +

            "('Російська Федерація', 'ru', 'ua')," +
            "('Україна', 'ua', 'ua')," +
            "('США', 'us', 'ua')," +
            "('Японія', 'jp', 'ua')," +
            "('Німеччина', 'de', 'ua');";


    public final String SQL_DELETE_TABLE_NEWS = "DROP TABLE IF EXISTS " + TABLE_NAME_NEWS;
}
