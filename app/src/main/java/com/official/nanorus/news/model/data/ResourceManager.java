package com.official.nanorus.news.model.data;

import android.content.Context;

import com.official.nanorus.news.R;
import com.official.nanorus.news.app.App;

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

    public int getNewsCategoryImage(String business) {
        return 0;
    }
}
