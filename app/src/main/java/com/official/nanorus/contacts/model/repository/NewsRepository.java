package com.official.nanorus.contacts.model.repository;


import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.entity.data.news.api.NewsArticle;
import com.official.nanorus.contacts.entity.data.news.api.NewsRequest;
import com.official.nanorus.contacts.model.data.AppPreferencesManager;
import com.official.nanorus.contacts.model.data.Utils;
import com.official.nanorus.contacts.model.data.api.NewsRetroClient;
import com.official.nanorus.contacts.model.data.database.news.NewsDatabaseManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private NewsRetroClient retroClient;
    private NewsDatabaseManager databaseManager;
    private AppPreferencesManager preferencesManager;

    public NewsRepository() {
        retroClient = NewsRetroClient.getInstance();
        preferencesManager = AppPreferencesManager.getInstance();
        databaseManager = NewsDatabaseManager.getInstance();
    }

    public Observable<News> getCachedNews() {
        return databaseManager.getNews().concatMapIterable(newsList -> newsList);
    }

    public void saveNewsToCache(List<News> newsList) {
        databaseManager.clearNews();
        databaseManager.putNews(newsList);
    }

    public Observable<News> getRefreshedNews(String query, boolean fromCache) {
        return (Observable<News>) retroClient.getPreparedObservable(
                getApiNews(query),
                News.class, true, fromCache
        );
    }

    private Observable<News> getApiNews(String query) {
        return retroClient.getNewsService().getNewsFeed(query)
                .toObservable()
                .map(NewsRequest::getNewsArticles)
                .flatMap((Function<List<NewsArticle>, Observable<NewsArticle>>) Observable::fromIterable)
                .map(newsArticle ->
                        new News(newsArticle.getTitle(), newsArticle.getDescription(),
                                newsArticle.getUrl(), newsArticle.getUrlToImage(), Utils.mapDate(newsArticle.getPublishedAt())
                        ))
                .subscribeOn(Schedulers.io());
    }

    public String getQuery() {
        return preferencesManager.getNewsQuery();
    }

    public void setQuery(String query) {
        preferencesManager.setNewsQuery(query);
    }

}
