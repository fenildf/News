package com.official.nanorus.contacts.model.domain;

import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.model.repository.NewsRepository;

import io.reactivex.Observable;


public class NewsInteractor {

    private NewsRepository repository;

    public NewsInteractor(){
        repository = new NewsRepository();
    }

    public Observable<News> getRefreshedNews(String query){
       return repository.getRefreshedNews(query);
    }

}
