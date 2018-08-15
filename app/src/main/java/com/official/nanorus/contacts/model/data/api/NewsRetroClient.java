package com.official.nanorus.contacts.model.data.api;

import android.support.v4.util.LruCache;

import com.official.nanorus.contacts.model.data.api.services.NewsService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetroClient {

    private static NewsRetroClient instance;
    private static Retrofit retrofit;

    private LruCache<Class<?>, Observable<?>> apiObservables = new LruCache<>(10);

    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable, Class<?> clazz, boolean cacheObservable, boolean useCache) {

        Observable<?> preparedObservable = null;

        if (useCache)
            preparedObservable = apiObservables.get(clazz);

        if (preparedObservable != null)
            return preparedObservable;


        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if (cacheObservable) {
            preparedObservable = preparedObservable.cache();
            apiObservables.put(clazz, preparedObservable);
        }

        return preparedObservable;
    }


    public static NewsRetroClient getInstance() {
        if (instance == null)
            instance = new NewsRetroClient();
        return instance;
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public NewsService getNewsService() {
        return getRetrofit().create(NewsService.class);
    }
}
