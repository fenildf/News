package com.official.nanorus.news.model.repository;

import com.official.nanorus.news.entity.data.ip.Ip;
import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.model.data.AppPreferencesManager;
import com.official.nanorus.news.model.data.api.IpRetroClient;
import com.official.nanorus.news.model.data.database.news.NewsDatabaseManager;

import io.reactivex.Single;

public class LaunchRepository {
    private AppPreferencesManager preferencesManager;
    private NewsDatabaseManager newsDatabaseManager;

    public LaunchRepository() {
        preferencesManager = AppPreferencesManager.getInstance();
        newsDatabaseManager = NewsDatabaseManager.getInstance();
    }

    public void setAppFirstStarted(boolean appFirstStarted) {
        preferencesManager.setAppFirstStarted(appFirstStarted);
    }

    public boolean getAppFirstStarted() {
        return preferencesManager.getAppFirstStarted();
    }

    public Single<Ip> getIp() {
        return IpRetroClient.getInstance().getIpService().getIp("49155");
    }

    public Single<Country> getCountry(String abbreviation) {
        return newsDatabaseManager.getCountry(abbreviation);
    }
}
