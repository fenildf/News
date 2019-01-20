package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.model.repository.LaunchRepository;

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


}
