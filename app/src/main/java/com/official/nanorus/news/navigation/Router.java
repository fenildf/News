package com.official.nanorus.news.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Router {

    public void openUrlIntBrowser(String url, Context context) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}
