package com.official.nanorus.contacts.model.repository;


import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.entity.data.news.api.NewsArticle;
import com.official.nanorus.contacts.entity.data.news.api.NewsRequest;
import com.official.nanorus.contacts.model.data.api.NewsRetroClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private NewsRetroClient retroClient;

    public NewsRepository() {
        retroClient = NewsRetroClient.getInstance();
    }

    public Observable<News> getRefreshedNews(String query) {
        return retroClient.getNewsService().getNewsFeed(query)
                .toObservable()
                .map(NewsRequest::getNewsArticles)
                .flatMap((Function<List<NewsArticle>, Observable<NewsArticle>>) Observable::fromIterable)
                .map(newsArticle -> new News(newsArticle.getTitle(), newsArticle.getDescription(),
                        newsArticle.getUrl(), newsArticle.getUrlToImage(), newsArticle.getPublishedAt()))
                .subscribeOn(Schedulers.io());
    }

}
