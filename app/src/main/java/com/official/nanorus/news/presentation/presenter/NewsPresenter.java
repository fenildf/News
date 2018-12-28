package com.official.nanorus.news.presentation.presenter;

import android.util.Log;

import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.model.data.ResourceManager;
import com.official.nanorus.news.model.data.Utils;
import com.official.nanorus.news.model.domain.NewsInteractor;
import com.official.nanorus.news.presentation.ui.Toaster;
import com.official.nanorus.news.presentation.view.news.INewsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenter {

    public final String TAG = this.getClass().getSimpleName();

    private NewsInteractor interactor;
    private INewsView view;
    private Observable<News> newsObservable;
    private Disposable newsDisponsable;
    private ResourceManager resourceManager;

    public NewsPresenter() {
        interactor = new NewsInteractor();
        resourceManager = new ResourceManager();
    }

    public void bindView(INewsView view) {
        this.view = view;
        getNews();
    }

    public void releasePresenter() {
        view = null;
        interactor = null;
        if (newsDisponsable != null && !newsDisponsable.isDisposed()) {
            newsDisponsable.dispose();
        }
    }


    public void getRefreshedNews(String query) {
        Log.d(TAG, "getRefreshedNews()");
        view.showLoading(true);
        newsObservable = interactor.getRefreshedNews(query);
        List<News> newsList = new ArrayList<>();
        newsDisponsable = newsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                            view.scrollToTop();
                            Log.d(TAG, news.getTitle());
                            newsList.add(news);
                        },
                        throwable -> {
                            Log.d(TAG, throwable.getMessage());
                            view.showLoading(false);
                            if (Utils.checkNetWorkError(throwable)) {
                                Toaster.shortToast(resourceManager.getStringNoInternet());
                                getNews();
                            } else {
                                Toaster.shortToast(throwable.getMessage());
                            }
                        },
                        () -> {
                            view.clearNewsList();
                            view.updateNewsList(newsList);
                            view.showNoNews(newsList.isEmpty());
                            view.showLoading(false);
                            interactor.setQuery(query);
                            interactor.saveNews(newsList);
                        }
                );
    }

    public void getNews() {
        Log.d(TAG, "getNews()");
        view.showLoading(true);

        newsObservable = interactor.getCategory().flatMapObservable(category -> interactor.getNews(category.getId()));
        if (newsDisponsable != null && !newsDisponsable.isDisposed()) {
            newsDisponsable.dispose();
        }
        List<News> newsList = new ArrayList<>();
        newsDisponsable = newsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                news -> {
                    Log.d(TAG, news.getTitle());
                    newsList.add(news);
                },
                throwable -> {
                    Log.d(TAG, throwable.getMessage());
                    view.showLoading(false);
                },
                () -> {
                    view.clearNewsList();
                    view.showLoading(false);
                    if (!newsList.isEmpty()) {
                        view.updateNewsList(newsList);
                    } else {
                        getRefreshedNews("");
                    }
                }
        );
    }

    public void onQueryEntered(String query) {
        interactor.setCountry("ru");
        //interactor.setCategory("sport");
        getRefreshedNews(query);
    }

    public void onSwipeRefresh() {
        getRefreshedNews(interactor.getQuery());
    }

    public void onViewResume() {
        String query = interactor.getQuery();
       /* if (query != null && !query.equals(""))
            view.setTitle(resourceManager.getStringNews() + ": " + interactor.getQuery());*/
    }
}
