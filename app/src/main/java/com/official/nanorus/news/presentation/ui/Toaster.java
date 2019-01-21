package com.official.nanorus.news.presentation.ui;

import android.widget.Toast;

import com.official.nanorus.news.app.App;

public class Toaster {

    public static void shortToast(String text) {
        Toast.makeText(App.getApp(), text, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(int resTextId) {
        Toast.makeText(App.getApp(), App.getApp().getText(resTextId), Toast.LENGTH_SHORT).show();
    }

    public static void longToast(int resTextId) {
        Toast.makeText(App.getApp(), App.getApp().getText(resTextId), Toast.LENGTH_LONG).show();
    }

    public static void longToast(String text) {
        Toast.makeText(App.getApp(), text, Toast.LENGTH_LONG).show();
    }
}
