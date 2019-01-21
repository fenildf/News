package com.official.nanorus.news.model.data.api;

import com.official.nanorus.news.model.data.api.services.IpService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class IpRetroClient {

    private static IpRetroClient instance;
    private static Retrofit retrofit;

    public static IpRetroClient getInstance() {
        if (instance == null)
            instance = new IpRetroClient();
        return instance;
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://ip-api.com/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public IpService getIpService() {
        return getRetrofit().create(IpService.class);
    }

}
