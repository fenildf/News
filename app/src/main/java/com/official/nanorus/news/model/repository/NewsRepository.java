package com.official.nanorus.news.model.repository;


import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.entity.data.news.api.NewsArticle;
import com.official.nanorus.news.entity.data.news.api.NewsRequest;
import com.official.nanorus.news.model.data.AppPreferencesManager;
import com.official.nanorus.news.model.data.Utils;
import com.official.nanorus.news.model.data.api.NewsRetroClient;
import com.official.nanorus.news.model.data.database.categories.CategoriesDatabaseManager;
import com.official.nanorus.news.model.data.database.news.NewsDatabaseManager;

import java.util.List;

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

    public Observable<News> getCachedNews(int category) {
        return newsDatabaseManager.getNews(category).concatMapIterable(newsList -> newsList);
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

    private Observable<News> getApiNews(String country, Category category, String query) {
        return retroClient.getNewsService().getNewsFeed(country, category.getName(), query)
                .toObservable()
                .map(NewsRequest::getNewsArticles)
                .flatMap((Function<List<NewsArticle>, Observable<NewsArticle>>) Observable::fromIterable)
                .map(newsArticle ->
                        new News(newsArticle.getTitle(), newsArticle.getDescription(),
                                newsArticle.getUrl(), newsArticle.getUrlToImage(),
                                Utils.mapDate(newsArticle.getPublishedAt()), category.getId()
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

    public String getCountry() {
        return preferencesManager.getNewsCountry();
    }

    public void setCountry(String country) {
        preferencesManager.setNewsCountry(country);
    }

}
