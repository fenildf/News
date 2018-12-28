package com.official.nanorus.news.model.data;

public class TextUtils {

    public static String uppercaseFirstCharacter(String str) {
        str = str.toLowerCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
