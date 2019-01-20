package com.official.nanorus.news.model.repository;

import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.model.data.AppPreferencesManager;
import com.official.nanorus.news.model.data.database.news.NewsDatabaseManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SettingsRepository {

    private AppPreferencesManager appPreferencesManager;
    private NewsDatabaseManager newsDatabaseManager;

    public SettingsRepository() {
        this.appPreferencesManager = AppPreferencesManager.getInstance();
        this.newsDatabaseManager = NewsDatabaseManager.getInstance();
    }

    public void saveCountry(String countryAbbreviation) {
        appPreferencesManager.setNewsCountry(countryAbbreviation);
    }

    public Single<Country> getCountry() {
        return newsDatabaseManager.getCountry(appPreferencesManager.getNewsCountry());
    }

    public Observable<List<Country>> getCountries() {
        return newsDatabaseManager.getCountries();
    }
}
