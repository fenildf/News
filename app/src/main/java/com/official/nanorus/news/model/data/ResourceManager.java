package com.official.nanorus.news.model.data;

import android.content.Context;
import android.net.Uri;

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

}
