package com.official.nanorus.news.model.data.api.services;

import com.official.nanorus.news.entity.data.ip.Ip;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IpService {
    @GET("json")
    Single<Ip> getIp(@Query("fields")String fields);
}
