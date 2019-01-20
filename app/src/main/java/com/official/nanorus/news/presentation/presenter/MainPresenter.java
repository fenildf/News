package com.official.nanorus.news.presentation.presenter;

import android.util.Log;

import com.official.nanorus.news.model.data.ResourceManager;
import com.official.nanorus.news.model.data.TextUtils;
import com.official.nanorus.news.model.domain.CategoriesInteractor;
import com.official.nanorus.news.model.domain.LaunchInteractor;
import com.official.nanorus.news.model.domain.MainInteractor;
import com.official.nanorus.news.model.domain.NewsInteractor;
import com.official.nanorus.news.navigation.Router;
import com.official.nanorus.news.presentation.view.main.IMainView;
import com.official.nanorus.news.presentation.view.main.MainActivity;

import io.reactivex.Completable;
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
    private Router router;
    private LaunchInteractor launchInteractor;

    private Disposable menuCategoriesDisposable;
    private Disposable newsCategoryDisposable;
    private Disposable launchDisposable;

    public MainPresenter() {
        newsInteractor = new NewsInteractor();
        resourceManager = new ResourceManager();
        mainInteractor = new MainInteractor();
        categoriesInteractor = new CategoriesInteractor();
        router = Router.getInstance();
        launchInteractor = new LaunchInteractor();
    }

    public void bindView(IMainView view) {
        this.view = view;
    }

    public void startWork(int selectedMenuItem, int selectedCategory) {
        //launchInteractor.setAppFirstStarted(true);
        if (launchInteractor.isAppFirstStarted()) {
            categoriesInteractor.clearCategories();
            newsInteractor.clearCountries();
            Completable categoriesCompletable = categoriesInteractor.insertDefaultCategories();
            Completable countriesCompletable = newsInteractor.insertDefaultCountries();
            launchDisposable = categoriesCompletable.andThen(countriesCompletable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        launchInteractor.setAppFirstStarted(false);
                        start(selectedMenuItem, selectedCategory);
                    });
        } else {
            start(selectedMenuItem, selectedCategory);
        }
    }

    private void start(int selectedMenuItem, int selectedCategory){
        setupNavigationMenu();
        if (selectedMenuItem == MainActivity.MENU_ITEM_CATEGORIES) {
            onNewsCategoryMenuItemClicked(selectedCategory);
        } else
            onCategoriesMenuItemClicked();
    }

    public void onNewsMenuItemClicked() {
        view.showNews();
    }

    public void onCategoriesMenuItemClicked() {
        view.setTitle(resourceManager.getStringAppName());
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

    public void setupNavigationMenu() {
        menuCategoriesDisposable = categoriesInteractor.getCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories1 -> view.setupNavigationMenu(categories1),
                        throwable -> Log.d(TAG, throwable.getMessage()));

    }

    public void releasePresenter() {
        if (menuCategoriesDisposable != null && !menuCategoriesDisposable.isDisposed())
            menuCategoriesDisposable.dispose();
        if (newsCategoryDisposable != null && !newsCategoryDisposable.isDisposed())
            newsCategoryDisposable.dispose();
        if (launchDisposable != null && !launchDisposable.isDisposed())
            launchDisposable.dispose();
        view = null;
    }

    public void onMainMenuItemClicked() {
        onCategoriesMenuItemClicked();
    }

    public void onSettingsMenuItemClicked() {
        router.openSettingsView(view.getActivity());
    }
}
