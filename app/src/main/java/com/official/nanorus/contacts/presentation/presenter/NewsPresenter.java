package com.official.nanorus.contacts.presentation.presenter;

import android.util.Log;

import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.Utils;
import com.official.nanorus.contacts.model.domain.NewsInteractor;
import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.news.INewsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

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


    public void getRefreshedNews(String query, boolean fromCache) {
        Log.d(TAG, "getRefreshedNews()");
        view.showLoading(true);
        newsObservable = interactor.getRefreshedNews(query, fromCache);
        List<News> newsList = new ArrayList<>();

        newsDisponsable = newsObservable.subscribe(news -> {
                    Log.d(TAG, news.getTitle());
                    newsList.add(news);
                },
                throwable -> {
                    Log.d(TAG, throwable.getMessage());
                    if (Utils.checkNetWorkError(throwable)) {
                        Toaster.shortToast(resourceManager.getStringNoInternet());
                    } else {
                        Toaster.shortToast(throwable.getMessage());
                    }
                    view.showLoading(false);
                },
                () -> {
                    view.clearNewsList();
                    view.updateNewsList(newsList);
                    view.showNoNews(newsList.isEmpty());
                    view.showLoading(false);
                    interactor.setQuery(query);
                    view.setTitle(resourceManager.getStringNews() + ": " + query);
                    interactor.saveNews(newsList);
                }
        );
    }

    public void getNews() {
        Log.d(TAG, "getNews()");
        view.showLoading(true);
        newsObservable = interactor.getNews();
        if (newsDisponsable != null && !newsDisponsable.isDisposed()) {
            newsDisponsable.dispose();
        }
        List<News> newsList = new ArrayList<>();
        newsDisponsable = newsObservable.subscribe(
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
                        view.setTitle(resourceManager.getStringNews() + ": " + interactor.getQuery());
                    } else {
                        String query = interactor.getQuery();
                        if (query != null && !query.equals("")) {
                            getRefreshedNews(query, true);
                        } else {
                            view.showEnterQuery();
                        }
                    }
                }
        );
    }

    public void onQueryEntered(String query) {
        getRefreshedNews(query, false);
    }

    public void onSwipeRefresh() {
        getRefreshedNews(interactor.getQuery(), false);
    }

    public void onViewResume() {
        String query = interactor.getQuery();
        if (query != null && !query.equals(""))
            view.setTitle(resourceManager.getStringNews() + ": " + interactor.getQuery());
    }
}
