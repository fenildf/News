package com.official.nanorus.contacts.model.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.official.nanorus.contacts.app.App;

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

}
