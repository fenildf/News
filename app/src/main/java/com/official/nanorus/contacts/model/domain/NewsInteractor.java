package com.official.nanorus.contacts.model.domain;

import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.model.repository.NewsRepository;

import java.util.List;

import io.reactivex.Observable;


public class NewsInteractor {

    private NewsRepository repository;

    public NewsInteractor() {
        repository = new NewsRepository();
    }

    public Observable<News> getRefreshedNews(String query, boolean fromCache) {
        return repository.getRefreshedNews(query, fromCache);
    }

    public Observable<News> getNews() {
        return repository.getCachedNews();
    }

    public String getQuery() {
        return repository.getQuery();
    }

    public void setQuery(String query) {
        repository.setQuery(query);
    }

    public void saveNews(List<News> newsList) {
        repository.saveNewsToCache(newsList);
    }
}
