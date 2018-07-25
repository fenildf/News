package com.official.nanorus.contacts.model.data.api.services;

import com.official.nanorus.contacts.entity.data.news.api.NewsRequest;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("everything?apiKey=90b7ce1f745a43dbb8bfa2a1fe130e72")
    Single<NewsRequest> getNewsFeed(@Query("q") String query);

}
