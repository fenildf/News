package com.official.nanorus.contacts.presentation.presenter;

import android.util.Log;

import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.model.domain.NewsInteractor;
import com.official.nanorus.contacts.presentation.view.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class NewsPresenter {

    public final String TAG = this.getClass().getSimpleName();

    private NewsInteractor interactor;
    private NewsFragment view;
    private Observable<News> newsObservable;
    private Disposable newsDisponsable;

    public NewsPresenter() {
        interactor = new NewsInteractor();
    }

    public void bindView(NewsFragment view) {
        this.view = view;
        getRefreshedNews("bitcoin");
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
        newsObservable = interactor.getRefreshedNews(query);
        List<News> newsList = new ArrayList<>();

        newsDisponsable = newsObservable.subscribe(news -> {
                    Log.d(TAG, news.getTitle());
                    view.clearNewsList();
                    newsList.add(news);
                },
                throwable -> Log.d(TAG, throwable.getMessage()),
                () -> view.updateNewsList(newsList)
        );
    }

    public void getNews() {
        Log.d(TAG, "getNews()");
        newsObservable = interactor.getNews();
        if (newsDisponsable != null && !newsDisponsable.isDisposed()) {
            newsDisponsable.dispose();
        }
        List<News> newsList = new ArrayList<>();
        newsDisponsable = newsObservable.subscribe(news -> {
                    Log.d(TAG, news.getTitle());
                    view.clearNewsList();
                    newsList.add(news);
                },
                throwable -> Log.d(TAG, throwable.getMessage()),
                () -> view.updateNewsList(newsList)
        );
    }

}
