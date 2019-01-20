package com.official.nanorus.news.presentation.view.main;

import android.app.Activity;
import android.content.Context;

import com.official.nanorus.news.entity.data.categories.Category;

import java.util.List;

public interface IMainView {
    void setSelectedMenuItem(int lastMenuItem);

    void showCategories();

    void showNews();

    void getNews();

    void setTitle(String category);

    void setupNavigationMenu(List<Category> categories);

    void setToolbarButtonHamburger();

    void setToolbarButtonArray();

    Activity getActivity();
}
