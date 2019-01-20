package com.official.nanorus.news.model.repository;

import com.official.nanorus.news.model.data.AppPreferencesManager;

public class LaunchRepository {
    private AppPreferencesManager preferencesManager;

    public LaunchRepository() {
        preferencesManager = AppPreferencesManager.getInstance();
    }

    public void setAppFirstStarted(boolean appFirstStarted) {
        preferencesManager.setAppFirstStarted(appFirstStarted);
    }

    public boolean getAppFirstStarted() {
        return preferencesManager.getAppFirstStarted();
    }

}
