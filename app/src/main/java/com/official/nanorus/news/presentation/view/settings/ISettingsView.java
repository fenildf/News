package com.official.nanorus.news.presentation.view.settings;

import android.app.Activity;

import com.official.nanorus.news.entity.data.news.Country;

import java.util.List;

public interface ISettingsView {
    Activity getActivity();

    void setCountry(String country);

    void openCountryChoosingDialog(List<Country> countries);

}
