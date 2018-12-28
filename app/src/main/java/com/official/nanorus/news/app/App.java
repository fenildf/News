package com.official.nanorus.news.app;

import android.app.Application;

public class App extends Application {

    private static App instance;

    public App() {
        instance = this;
    }

    public static App getApp() {
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        instance = null;
        super.finalize();
    }
}
