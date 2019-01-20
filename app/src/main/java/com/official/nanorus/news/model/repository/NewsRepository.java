package com.official.nanorus.news.model.repository;


import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.entity.data.news.api.NewsArticle;
import com.official.nanorus.news.entity.data.news.api.NewsRequest;
import com.official.nanorus.news.model.data.AppPreferencesManager;
import com.official.nanorus.news.model.data.Utils;
import com.official.nanorus.news.model.data.api.NewsRetroClient;
import com.official.nanorus.news.model.data.database.categories.CategoriesDatabaseManager;
import com.official.nanorus.news.model.data.database.news.NewsDatabaseManager;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private NewsRetroClient retroClient;
    private NewsDatabaseManager newsDatabaseManager;
    private CategoriesDatabaseManager categoriesDatabaseManager;
    private AppPreferencesManager preferencesManager;

    public NewsRepository() {
        retroClient = NewsRetroClient.getInstance();
        preferencesManager = AppPreferencesManager.getInstance();
        newsDatabaseManager = NewsDatabaseManager.getInstance();
        categoriesDatabaseManager = CategoriesDatabaseManager.getInstance();
    }

    public Observable<News> getCachedNews(Category category) {
        int categoryId = 0;
        if (category != null)
            categoryId = category.getId();
        return newsDatabaseManager.getNews(categoryId).concatMapIterable(newsList -> newsList);
    }

    public void saveNewsToCache(List<News> newsList) {
        int category = newsList.get(0).getCategory();
        Observable.just(newsDatabaseManager.clearNews(category),
                newsDatabaseManager.putNews(newsList))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Observable<News> getRefreshedNews(String country, Category category, String query) {
        return getApiNews(country, category, query);
    }

    public Observable<News> getRefreshedNews(String country, String query) {
        return getApiNews(country, query);
    }

    private Observable<News> getApiNews(String country, Category category, String query) {
        return retroClient.getNewsService().getNewsFeed(country, category.getDefaultName(), query)
                .toObservable()
                .map(NewsRequest::getNewsArticles)
                .flatMap((Function<List<NewsArticle>, Observable<NewsArticle>>) Observable::fromIterable)
                .map(newsArticle ->
                        new News(newsArticle.getTitle(), newsArticle.getDescription(),
                                newsArticle.getUrl(), newsArticle.getUrlToImage(),
                                Utils.mapDateFromApi(newsArticle.getPublishedAt()), category.getId()
                        ))
                .subscribeOn(Schedulers.io());
    }

    private Observable<News> getApiNews(String country, String query) {
        return retroClient.getNewsService().getNewsFeed(country, query)
                .toObservable()
                .map(NewsRequest::getNewsArticles)
                .flatMap((Function<List<NewsArticle>, Observable<NewsArticle>>) Observable::fromIterable)
                .map(newsArticle ->
                        new News(newsArticle.getTitle(), newsArticle.getDescription(),
                                newsArticle.getUrl(), newsArticle.getUrlToImage(),
                                Utils.mapDateFromApi(newsArticle.getPublishedAt()), 0
                        ))
                .subscribeOn(Schedulers.io());
    }


    public String getQuery() {
        return preferencesManager.getNewsQuery();
    }

    public void setQuery(String query) {
        preferencesManager.setNewsQuery(query);
    }

    public Single<Category> getCategory() {
        int categoryId = preferencesManager.getNewsCategory();
        return categoriesDatabaseManager.getCategory(categoryId);
    }

    public void setCategory(int category) {
        preferencesManager.setNewsCategory(category);
    }

    public Single<Country> getCountry() {
        return newsDatabaseManager.getCountry(preferencesManager.getNewsCountry());
    }

    public void setCountry(String country) {
        preferencesManager.setNewsCountry(country);
    }

    public Completable insertDefaultCountries() {
       return Completable.create(emitter -> {
            newsDatabaseManager.insertDefaultCountries();
            emitter.onComplete();
        });
    }

    public void clearCountries(){
        newsDatabaseManager.clearCountries();
    }
}
