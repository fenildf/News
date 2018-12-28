package com.official.nanorus.news.presentation.view.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.official.nanorus.news.R;
import com.official.nanorus.news.presentation.presenter.CategoriesPresenter;
import com.official.nanorus.news.presentation.ui.adapters.CategoriesRecyclerViewAdapter;
import com.official.nanorus.news.presentation.ui.adapters.NewsRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoriesFragment extends Fragment implements ICategoriesView {

    @BindView(R.id.rv_categories)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.rv_all_news)
    RecyclerView newsRecyclerView;
    private RecyclerView.LayoutManager categoriesLayoutManager;
    private CategoriesRecyclerViewAdapter categoriesAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private NewsRecyclerViewAdapter newsAdapter;

    private CategoriesPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, null);
        ButterKnife.bind(this, view);

        initCategoriesList();
        initNewsList();
        presenter = new CategoriesPresenter();
        presenter.bindView(this);
        return view;
    }

    private void initCategoriesList() {
        categoriesLayoutManager = new LinearLayoutManager(categoriesRecyclerView.getContext(), GridLayoutManager.VERTICAL, false);
        categoriesAdapter = new CategoriesRecyclerViewAdapter(this.getActivity());
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
    }

    private void initNewsList() {
        newsLayoutManager = new LinearLayoutManager(newsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        newsAdapter = new NewsRecyclerViewAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
    }




}
