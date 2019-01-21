package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.entity.data.ip.Ip;
import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.model.repository.LaunchRepository;

import io.reactivex.Single;

public class LaunchInteractor {

    private LaunchRepository repository;

    public LaunchInteractor(){
        repository = new LaunchRepository();
    }

    public void setAppFirstStarted(boolean appFirstStarted) {
        repository.setAppFirstStarted(appFirstStarted);
    }

    public boolean isAppFirstStarted() {
        return repository.getAppFirstStarted();
    }

    public Single<Ip> getIp() {
        return repository.getIp();
    }

    public Single<Country> getCountry(String abbreviation) {
       return repository.getCountry(abbreviation);
    }
}
