package com.official.nanorus.news.presentation.presenter;

import android.util.Log;

import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.model.domain.SettingsInteractor;
import com.official.nanorus.news.navigation.Router;
import com.official.nanorus.news.presentation.ui.Toaster;
import com.official.nanorus.news.presentation.view.settings.ISettingsView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private SettingsInteractor interactor;
    private Router router;
    private ISettingsView view;
    private Observable<List<Country>> countriesObservable;
    private Disposable countriesDisposable;
    private Disposable countryDisposable;

    public SettingsPresenter() {
        interactor = new SettingsInteractor();
        router = Router.getInstance();
    }

    public void bindView(ISettingsView view) {
        this.view = view;
        view.setLanguage("Russian");
        view.setCountry("Russian Federation");

        countryDisposable = interactor.getCountry().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(country -> view.setCountry(country.getName()), throwable -> {
                    Log.d(TAG, throwable.getMessage());
                    Toaster.shortToast(throwable.getMessage());
                });
    }

    public void onBackPressed() {
        router.onBackPressed(view.getActivity());
    }

    public void onCountryClicked() {
        List<Country> countries = new ArrayList<>();
        countriesObservable = interactor.getCountries();
        countriesDisposable = countriesObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries::addAll,
                        throwable -> {
                            Log.d(TAG, throwable.getMessage());
                            Toaster.shortToast(throwable.getMessage());
                        },
                        () -> view.openCountryChoosingDialog(countries)
                );
    }

    public void onCountrySelected(Country country) {
        Toaster.shortToast("Country selected: " + country.getName());
        interactor.saveCountry(country);
        view.setCountry(country.getName());
    }

    public void onLanguageSelected(String language) {

    }

    public void releasePresenter() {
        if (countriesDisposable != null && !countriesDisposable.isDisposed())
            countriesDisposable.dispose();
        countriesDisposable = null;
        countriesObservable = null;
        view = null;
        router = null;
        interactor = null;
    }
}
