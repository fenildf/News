package com.official.nanorus.news.model.data;

import android.content.Context;

import com.official.nanorus.news.R;
import com.official.nanorus.news.app.App;

import java.lang.reflect.Field;

public class ResourceManager {
    private Context context;

    public ResourceManager() {
        this.context = App.getApp().getApplicationContext();
    }

    public String getStringNews() {
        return context.getString(R.string.news);
    }

    public String getStringAppName() {
        return context.getString(R.string.app_name);
    }

    public String getStringNoInternet() {
        return context.getString(R.string.no_internet);
    }

    public int getNewsCategoryImage(String category) {
        return getResId(category, R.drawable.class);
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getStringDefaultCountry() {
        return context.getString(R.string.defaultCountry);
    }
}
