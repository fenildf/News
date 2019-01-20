package com.official.nanorus.news.model.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.official.nanorus.news.app.App;

public class AppPreferencesManager {
    private final String TAG = this.getClass().getSimpleName();

    private SharedPreferences preferences;

    private static AppPreferencesManager instance;

    public static AppPreferencesManager getInstance() {
        if (instance == null)
            instance = new AppPreferencesManager();
        return instance;
    }

    public AppPreferencesManager() {
        preferences = App.getApp().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
    }

    private void putString(String name, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(name, value);
        editor.apply();
    }

    private void putInt(String name, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(name, value);
        editor.apply();
    }

    private void putBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public void setSelectedMenuItem(int item) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt("menu_item", item);
        editor.apply();
    }

    public int getSelectedMenuItem() {
        return getPreferences().getInt("menu_item", 0);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public String getNewsQuery() {
        return getPreferences().getString("news_query", null);
    }

    public void setNewsQuery(String query) {
        putString("news_query", query);
    }

    public int getNewsCategory() {
        int category;
        try {
            category = getPreferences().getInt("news_category", 1);
        } catch (ClassCastException e) {
            category = 1;
        }
        return category;
    }

    public void setNewsCategory(int category) {
        putInt("news_category", category);
    }

    public void setAppFirstStarted(boolean appFirstStarted) {
        putBoolean("app_first_started", appFirstStarted);
    }

    public boolean getAppFirstStarted() {
        return getPreferences().getBoolean("app_first_started", true);
    }

    public String getNewsCountry() {
        return getPreferences().getString("news_country", null);
    }

    public void setNewsCountry(String country) {
        putString("news_country", country);
    }
}
