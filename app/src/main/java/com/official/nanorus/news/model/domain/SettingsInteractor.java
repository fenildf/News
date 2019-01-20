package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.model.data.Utils;
import com.official.nanorus.news.model.repository.SettingsRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SettingsInteractor {

    private SettingsRepository repository;

    public SettingsInteractor(){
        repository = new SettingsRepository();
    }

    public void saveCountry(Country country) {
        repository.saveCountry(country.getAbbreviation());
    }

    public Single<Country> getCountry() {
        return repository.getCountry();
    }

    public Observable<List<Country>> getCountries() {
        return repository.getCountries();
    }
}
