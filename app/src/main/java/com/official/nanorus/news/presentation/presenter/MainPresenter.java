package com.official.nanorus.news.presentation.presenter;

import android.util.Log;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.data.ResourceManager;
import com.official.nanorus.news.model.data.TextUtils;
import com.official.nanorus.news.model.domain.CategoriesInteractor;
import com.official.nanorus.news.model.domain.MainInteractor;
import com.official.nanorus.news.model.domain.NewsInteractor;
import com.official.nanorus.news.presentation.view.main.IMainView;
import com.official.nanorus.news.presentation.view.main.MainActivity;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    private final String TAG = this.getClass().getSimpleName();

    private IMainView view;
    private NewsInteractor newsInteractor;
    private MainInteractor mainInteractor;
    private ResourceManager resourceManager;
    private CategoriesInteractor categoriesInteractor;

    private Disposable menuCategoriesDisposable;
    private Disposable newsCategoryDisposable;

    public MainPresenter() {
        newsInteractor = new NewsInteractor();
        resourceManager = new ResourceManager();
        mainInteractor = new MainInteractor();
        categoriesInteractor = new CategoriesInteractor();
    }

    public void bindView(IMainView view) {
        this.view = view;
        onCategoriesMenuItemClicked();
    }

    public void onNewsMenuItemClicked() {
        view.showNews();
    }

    public void onCategoriesMenuItemClicked() {
        view.setTitle("News");
        view.showCategories();
    }

    public void saveMenuState(int item) {
        //   contactsInteractor.setLastMenuItem(item);
    }


    public void onNewsCategoryMenuItemClicked(int category) {
        newsInteractor.setQuery("");
        newsInteractor.setCategory(category);
        onNewsMenuItemClicked();

        newsCategoryDisposable = newsInteractor.getCategory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(category1 ->
                        {
                            view.setTitle(TextUtils.uppercaseFirstCharacter(category1.getName()));
                            view.getNews();
                        }, throwable -> Log.d(TAG, throwable.getMessage())

                );
    }

    public void onNewsCategoriesMenuCreate() {
        Single<List<Category>> categoriesSingle = categoriesInteractor.getCategories();
        menuCategoriesDisposable = categoriesSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories1 -> view.setMenuNewsCategories(categories1),
                        throwable -> Log.d(TAG, throwable.getMessage()));

    }

    public void releasePresenter() {
        if (menuCategoriesDisposable != null && !menuCategoriesDisposable.isDisposed())
            menuCategoriesDisposable.dispose();
        if (newsCategoryDisposable != null && !newsCategoryDisposable.isDisposed())
            newsCategoryDisposable.dispose();
        view = null;
    }

    public void onMainMenuItemClicked() {
        onCategoriesMenuItemClicked();
    }
}
