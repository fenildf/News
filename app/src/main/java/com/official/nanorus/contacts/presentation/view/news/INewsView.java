package com.official.nanorus.contacts.presentation.view.news;

import com.official.nanorus.contacts.entity.data.news.News;

import java.util.List;

public interface INewsView {
    void showLoading(boolean b);

    void clearNewsList();

    void updateNewsList(List<News> newsList);

    void showNoNews(boolean empty);

    void setTitle(String s);

    void showEnterQuery();
}
