package com.official.nanorus.news.model.data.database.categories;

public class CategoriesDatabaseContract {

    public final String TABLE_NAME_CATEGORIES = "categories";
    public final String COLUMN_NAME_CATEGORY_NAME = "name";
    public final String COLUMN_NAME_CATEGORY_DEFAULT_NAME = "default_name";
    public final String COLUMN_NAME_CATEGORY_IMAGE = "image";
    public final String COLUMN_NAME_CATEGORY_LANG = "lang";


    public final String COLUMN_NAME_ID = "_id";
    public final String COMMA = ",";

    public final String SQL_CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_NAME_CATEGORIES + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
            COLUMN_NAME_CATEGORY_NAME + " TEXT" + COMMA +
            COLUMN_NAME_CATEGORY_DEFAULT_NAME + " TEXT" + COMMA +
            COLUMN_NAME_CATEGORY_IMAGE + " TEXT" + COMMA +
            COLUMN_NAME_CATEGORY_LANG + " TEXT" +
            ")";

    public final String SQL_FIRST_INSERT_CATEGORIES = "INSERT INTO " + TABLE_NAME_CATEGORIES + " (" +
            COLUMN_NAME_CATEGORY_NAME + ", " + COLUMN_NAME_CATEGORY_DEFAULT_NAME + ", " + COLUMN_NAME_CATEGORY_IMAGE +
            ", " + COLUMN_NAME_CATEGORY_LANG + ")" +
            "VALUES('business','business', 'business', 'en')," +
            "('entertainment','entertainment', 'entertainment', 'en')," +
            "('health', 'health', 'health', 'en')," +
            "('science','science', 'science', 'en')," +
            "('sports','sports', 'sports', 'en')," +
            "('technology','technology', 'technology', 'en')," +

            "('бизнес','business', 'business', 'ru')," +
            "('развлечения','entertainment', 'entertainment', 'ru')," +
            "('здоровье', 'health', 'health', 'ru')," +
            "('наука','science', 'science', 'ru')," +
            "('спорт','sports', 'sports', 'ru')," +
            "('технологии','technology', 'technology', 'ru')," +

            "('бiзнес','business', 'business', 'ua')," +
            "('розваги','entertainment', 'entertainment', 'ua')," +
            "('здоров`я', 'health', 'health', 'ua')," +
            "('наука','science', 'science', 'ua')," +
            "('спорт','sports', 'sports', 'ua')," +
            "('технологiї','technology', 'technology', 'ua');";

    public final String SQL_DELETE_TABLE_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES;
}
