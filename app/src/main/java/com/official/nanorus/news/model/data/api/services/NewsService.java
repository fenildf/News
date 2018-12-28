package com.official.nanorus.news.model.data.api.services;

import com.official.nanorus.news.entity.data.news.api.NewsRequest;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("top-headlines?apiKey=90b7ce1f745a43dbb8bfa2a1fe130e72")
    Single<NewsRequest> getNewsFeed(@Query("country") String country, @Query("category") String category, @Query("q") String query);

}
