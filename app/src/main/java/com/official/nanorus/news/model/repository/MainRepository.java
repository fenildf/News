package com.official.nanorus.news.model.repository;

import com.official.nanorus.news.model.data.AppPreferencesManager;

public class MainRepository {

    private AppPreferencesManager preferencesManager;

    public MainRepository() {
        preferencesManager = AppPreferencesManager.getInstance();
    }

    public int getSelectedMenuItem() {
        return preferencesManager.getSelectedMenuItem();
    }

    public void setSelectedMenuItem(int item) {
        preferencesManager.setSelectedMenuItem(item);
    }

}
