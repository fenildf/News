package com.official.nanorus.news.presentation.view.news;

import com.official.nanorus.news.entity.data.news.News;

import java.util.List;

public interface INewsView {
    void showLoading(boolean b);

    void clearNewsList();

    void updateNewsList(List<News> newsList);

    void showNoNews(boolean empty);

    void setTitle(String s);

    void scrollToTop();
}
