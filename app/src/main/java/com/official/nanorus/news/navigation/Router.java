package com.official.nanorus.news.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.official.nanorus.news.presentation.view.settings.SettingsActivity;

public class Router {

    private String TAG = this.getClass().getName();

    private static Router instance;

    public static Router getInstance() {
        if (instance == null)
            instance = new Router();
        return instance;
    }

    public void finishActivity(Activity activity) {
        activity.finish();
    }

    public void openUrlInBrowser(String url, Context context) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void openSettingsView(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public void onBackPressed(Activity activity){
        activity.onBackPressed();
    }

}
