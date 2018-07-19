package com.official.nanorus.contacts.presentation.ui;

import android.widget.Toast;

import com.official.nanorus.contacts.app.App;

public class Toaster {

    public static void shortToast(String text) {
        Toast.makeText(App.getApp(), text, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(int resTextId) {
        Toast.makeText(App.getApp(), App.getApp().getText(resTextId), Toast.LENGTH_SHORT).show();
    }

}
