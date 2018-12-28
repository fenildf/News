package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.model.repository.MainRepository;

public class MainInteractor {

    private MainRepository repository;

    public MainInteractor() {
        repository = new MainRepository();
    }

    public int getSelectedMenuItem() {
        return repository.getSelectedMenuItem();
    }

    public void setSelectedMenuItem(int item) {
        repository.setSelectedMenuItem(item);
    }
}
