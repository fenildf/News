package com.official.nanorus.news.presentation.view.settings;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.news.Country;
import com.official.nanorus.news.presentation.presenter.SettingsPresenter;
import com.official.nanorus.news.presentation.ui.Toaster;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity implements ISettingsView {

    private SettingsPresenter presenter;

    @BindView(R.id.tv_country)
    TextView countryTextView;
    @BindView(R.id.tv_language)
    TextView languageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        presenter = new SettingsPresenter();
        presenter.bindView(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.settings);
    }

    @OnClick({R.id.tv_country_title, R.id.tv_country})
    void onCountryClick() {
        Toaster.shortToast("country");
        presenter.onCountryClicked();
    }

    @OnClick({R.id.tv_language_title, R.id.tv_language})
    void onLanguageClick() {
        Toaster.shortToast("language");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setCountry(String country) {
        countryTextView.setText(country);
    }

    @Override
    public void setLanguage(String language) {
        languageTextView.setText(language);
    }

    @Override
    public void openCountryChoosingDialog(List<Country> countries) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(R.string.select_country_of_news);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for (Country country:countries){
            arrayAdapter.add(country.getName());
        }

        builderSingle.setNegativeButton(R.string.close, (dialog, which) -> dialog.dismiss());
        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
          String countryName = arrayAdapter.getItem(which);
            for (Country country : countries) {
                if (country.getName().equals(countryName)) {
                    presenter.onCountrySelected(country);
                }
            }

        });
        builderSingle.show();
    }

    @Override
    public void openLanguageChoosingDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.releasePresenter();
    }
}
