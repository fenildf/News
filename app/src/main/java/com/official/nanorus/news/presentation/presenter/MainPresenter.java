package com.official.nanorus.news.presentation.presenter;

import android.util.Log;

import com.official.nanorus.news.entity.data.ip.Ip;
import com.official.nanorus.news.model.data.ResourceManager;
import com.official.nanorus.news.model.data.TextUtils;
import com.official.nanorus.news.model.domain.CategoriesInteractor;
import com.official.nanorus.news.model.domain.LaunchInteractor;
import com.official.nanorus.news.model.domain.MainInteractor;
import com.official.nanorus.news.model.domain.NewsInteractor;
import com.official.nanorus.news.model.domain.SettingsInteractor;
import com.official.nanorus.news.navigation.Router;
import com.official.nanorus.news.presentation.view.main.IMainView;
import com.official.nanorus.news.presentation.view.main.MainActivity;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    private final String TAG = this.getClass().getSimpleName();

    private IMainView view;
    private MainInteractor mainInteractor;
    private NewsInteractor newsInteractor;
    private SettingsInteractor settingsInteractor;
    private CategoriesInteractor categoriesInteractor;
    private ResourceManager resourceManager;
    private LaunchInteractor launchInteractor;
    private Router router;

    private Disposable menuCategoriesDisposable;
    private Disposable newsCategoryDisposable;
    private Disposable launchDisposable;

    private static final boolean FIRST_LAUNCH_TEST = false;

    public MainPresenter() {
        mainInteractor = new MainInteractor();
        newsInteractor = new NewsInteractor();
        settingsInteractor = new SettingsInteractor();
        categoriesInteractor = new CategoriesInteractor();
        launchInteractor = new LaunchInteractor();
        resourceManager = new ResourceManager();
        router = Router.getInstance();
    }

    public void bindView(IMainView view) {
        this.view = view;
    }

    public void startWork(int selectedMenuItem, int selectedCategory) {
        launchInteractor.setAppFirstStarted(FIRST_LAUNCH_TEST);
        if (launchInteractor.isAppFirstStarted()) {
            categoriesInteractor.clearCategories();
            newsInteractor.clearCountries();
            Completable categoriesCompletable = categoriesInteractor.insertDefaultCategories();
            Completable countriesCompletable = newsInteractor.insertDefaultCountries();
            Single<Ip> ipSingle = launchInteractor.getIp();
            launchDisposable = categoriesCompletable.andThen(countriesCompletable)
                    .andThen(ipSingle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(ip -> {
                        String countryCode = ip.getCountryCode().toLowerCase();
                        return launchInteractor.getCountry(countryCode);
                    })
                    .subscribe(country -> {
                        launchInteractor.setAppFirstStarted(false);
                        newsInteractor.setCountry(country.getAbbreviation());
                        start(selectedMenuItem, selectedCategory);
                    }, throwable -> {
                        Log.d(TAG, throwable.getMessage());
                        launchInteractor.setAppFirstStarted(false);
                        newsInteractor.setCountry(resourceManager.getStringDefaultCountry());
                        start(selectedMenuItem, selectedCategory);
                    });
        } else {
            start(selectedMenuItem, selectedCategory);
        }
    }

    private void start(int selectedMenuItem, int selectedCategory) {
        setupNavigationMenu();
        if (selectedMenuItem == MainActivity.MENU_ITEM_CATEGORIES) {
            onNewsCategoryMenuItemClicked(selectedCategory);
        } else
            onCategoriesMenuItemClicked();
    }

    public void onNewsMenuItemClicked() {
        newsInteractor.setQuery("");
        view.showNews();
    }

    public void onCategoriesMenuItemClicked() {
        newsInteractor.deleteNewsCategory();
        newsInteractor.setQuery("");
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
