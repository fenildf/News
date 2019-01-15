package com.official.nanorus.news.presentation.view.categories;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.News;

import java.util.List;

public interface ICategoriesView {

    void setCategories(List<Category> categories);

    void clearNewsList();

    void updateNewsList(List<News> newsList);

    void showNoNews(boolean empty);

    void showLoading(boolean b);

    void scrollToTop();

    void showNewsPage(Category category);
}
