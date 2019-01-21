package com.official.nanorus.news.presentation.presenter;

import android.util.Log;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.model.data.ResourceManager;
import com.official.nanorus.news.model.data.TextUtils;
import com.official.nanorus.news.model.data.Utils;
import com.official.nanorus.news.model.domain.CategoriesInteractor;
import com.official.nanorus.news.model.domain.NewsInteractor;
import com.official.nanorus.news.presentation.ui.Toaster;
import com.official.nanorus.news.presentation.view.categories.ICategoriesView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoriesPresenter {
    private final String TAG = this.getClass().getSimpleName();
    private ICategoriesView view;
    private CategoriesInteractor categoriesInteractor;
    private NewsInteractor newsInteractor;
    private Disposable categoriesDisposable;
    private Observable<News> newsObservable;
    private Disposable newsDisponsable;
    private ResourceManager resourceManager;
    private Disposable setCategoryTitleDisposable;

    public CategoriesPresenter() {
        newsInteractor = new NewsInteractor();
        resourceManager = new ResourceManager();
        categoriesInteractor = new CategoriesInteractor();
    }

    public void bindView(ICategoriesView view) {
        this.view = view;
        getCategories();
        getNews();
    }

    public void getCategories() {
        Log.d(TAG, "getCategories()");
        categoriesDisposable = categoriesInteractor.getCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::setCategories,
                        throwable -> Log.d(TAG, throwable.getMessage()));
    }

    public void getRefreshedNews(String query) {
        Log.d(TAG, "getRefreshedNews()");
        view.showLoading(true);
        newsObservable = newsInteractor.getRefreshedNews(query, false);
        List<News> newsList = new ArrayList<>();
        newsDisponsable = newsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                            Log.d(TAG, news.getTitle());
                            newsList.add(news);
                        },
                        throwable -> {
                            Log.d(TAG, throwable.getMessage());
                            view.showLoading(false);
                            if (Utils.checkNetworkError(throwable)) {
                                Toaster.shortToast(resourceManager.getStringNoInternet());
                                getNews();
                            } else {
                                Toaster.shortToast(throwable.getMessage());
                            }
                        },
                        () -> {
                            view.scrollToTop();
                            view.clearNewsList();
                            view.updateNewsList(newsList);
                            view.showNoNews(newsList.isEmpty());
                            view.showLoading(false);
                            newsInteractor.setQuery(query);
                            newsInteractor.saveNews(newsList);
                        }
                );
    }

    public void getNews() {
        Log.d(TAG, "getNews()");
        showLoading(true);

        newsObservable = newsInteractor.getNews(null);
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
                    showLoading(false);
                    if (!newsList.isEmpty()) {
                        view.updateNewsList(newsList);
                    } else {
                        getRefreshedNews("");
                    }
                }
        );
    }

    public void setCategoryTitle() {
        setCategoryTitleDisposable = newsInteractor.getCategory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(category -> view.setTitle(TextUtils.uppercaseFirstCharacter(category.getName())), throwable -> Log.d(TAG, throwable.getMessage()));
    }

    public void showLoading(boolean show){
        view.showLoading(show);
        if (show){
        } else {
            setCategoryTitle();
        }
    }

    public void onCategoryClicked(Category category) {
        newsInteractor.setCategory(category.getId());
        newsInteractor.setQuery("");
        view.showNewsPage(category);
    }


    public void releasePresenter() {
        if (categoriesDisposable != null && !categoriesDisposable.isDisposed())
            categoriesDisposable.dispose();
        if (newsDisponsable != null && !newsDisponsable.isDisposed())
            newsDisponsable.dispose();
        view = null;
    }

    public void onRefresh() {
        getRefreshedNews("");
    }
}
