package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.model.repository.NewsRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public class NewsInteractor {

    private NewsRepository repository;

    public NewsInteractor() {
        repository = new NewsRepository();
    }

    public Observable<News> getRefreshedNews(String query, boolean withCategory) {
        Observable<News> refreshedNews;
        if (withCategory) {
            Single<Category> categorySingle = repository.getCategory();
            Single<Country> countrySingle = repository.getCountry();

            refreshedNews = Single.zip(categorySingle, countrySingle, (category, country) ->
                    repository.getRefreshedNews(country.getAbbreviation(), category, query))
                    .flatMapObservable(newsObservable -> newsObservable);

        } else {
            refreshedNews = repository.getCountry()
                    .flatMapObservable(country -> repository.getRefreshedNews(country.getAbbreviation(), query));
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

    public Single<Country> getCountry() {
        return repository.getCountry();
    }

    public void setCountry(String country) {
        repository.setCountry(country);
    }

    public Completable insertDefaultCountries() {
        return repository.insertDefaultCountries();
    }

    public void clearCountries() {
        repository.clearCountries();
    }
}
