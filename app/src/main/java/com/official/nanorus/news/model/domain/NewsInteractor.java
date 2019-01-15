package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.model.repository.NewsRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class NewsInteractor {

    private NewsRepository repository;

    public NewsInteractor() {
        repository = new NewsRepository();
    }

    public Observable<News> getRefreshedNews(String query, boolean withCategory) {
        Observable<News> refreshedNews;
        if (withCategory) {
            Single<Category> categorySingle = repository.getCategory();
            refreshedNews = categorySingle.subscribeOn(Schedulers.io())
                    .flatMapObservable(category -> repository.getRefreshedNews(repository.getCountry(), category, query));
        } else {
            refreshedNews = repository.getRefreshedNews(repository.getCountry(), query);
        }
        return refreshedNews;
    }

    public Observable<News> getNews(Category category) {
        return repository.getCachedNews(category);
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

    public Single<Category> getCategory() {
        return repository.getCategory();
    }

    public void setCategory(int category) {
        repository.setCategory(category);
    }

    public String getCountry() {
        return repository.getCountry();
    }

    public void setCountry(String country) {
        repository.setCountry(country);
    }

}
