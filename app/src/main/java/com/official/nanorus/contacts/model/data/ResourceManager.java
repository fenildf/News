package com.official.nanorus.contacts.model.data;

import android.content.Context;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.app.App;

public class ResourceManager {
    private Context context;

    public ResourceManager() {
        this.context = App.getApp().getApplicationContext();
    }

    public String getStringAddContactSuccess() {
        return context.getString(R.string.contact_added);
    }
    public String getStringAddContactFail() {
        return context.getString(R.string.contact_did_not_add);
    }

}
