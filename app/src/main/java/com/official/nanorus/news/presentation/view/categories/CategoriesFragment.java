package com.official.nanorus.news.presentation.view.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.model.data.TextUtils;
import com.official.nanorus.news.presentation.presenter.CategoriesPresenter;
import com.official.nanorus.news.presentation.ui.adapters.CategoriesRecyclerViewAdapter;
import com.official.nanorus.news.presentation.ui.adapters.NewsRecyclerViewAdapter;
import com.official.nanorus.news.presentation.view.main.IMainView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoriesFragment extends Fragment implements ICategoriesView, CategoriesRecyclerViewAdapter.CategoryListListener {
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.rv_categories)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.rv_all_news)
    RecyclerView newsRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager categoriesLayoutManager;
    private CategoriesRecyclerViewAdapter categoriesAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private NewsRecyclerViewAdapter newsAdapter;

    private CategoriesPresenter presenter;

    public interface CategoriesListener {
        void setTitle(String title);

        void setSelectedFragment(Category category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, null);
        ButterKnife.bind(this, view);

        initCategoriesList();
        initNewsList();
        presenter = new CategoriesPresenter();
        presenter.bindView(this);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());

        return view;
    }

    private void initCategoriesList() {
        categoriesLayoutManager = new GridLayoutManager(categoriesRecyclerView.getContext(), 2, GridLayoutManager.VERTICAL, false);
        categoriesAdapter = new CategoriesRecyclerViewAdapter(this.getActivity(), this);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
    }

    private void initNewsList() {
        newsLayoutManager = new LinearLayoutManager(newsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        newsAdapter = new NewsRecyclerViewAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
    }


    @Override
    public void setCategories(List<Category> categories) {
        categoriesAdapter.updateList(categories);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearNewsList() {
        Log.d(TAG, "clearNewsList()");
        newsAdapter.clearList();
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateNewsList(List<News> newsList) {
        Log.d(TAG, "updateNewsList()");
        newsAdapter.updateList(newsList);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoNews(boolean empty) {

    }

    @Override
    public void showLoading(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
        if (show)
            setTitle(getString(R.string.loading));
        else
            setTitle(getString(R.string.app_name));
    }

    @Override
    public void scrollToTop() {

    }

    @Override
    public void showNewsPage(Category category) {
        if (getActivity() instanceof IMainView) {
            IMainView mainView = (IMainView) getActivity();
            mainView.setTitle(TextUtils.uppercaseFirstCharacter(category.getName()));
            mainView.showNews();
        }

        if (getActivity() instanceof CategoriesListener){
            CategoriesListener categoriesListener = (CategoriesListener) getActivity();
            categoriesListener.setSelectedFragment(category);
        }
    }

    @Override
    public void setTitle(String title) {
        if (getActivity() instanceof CategoriesListener) {
            ((CategoriesListener) getActivity()).setTitle(title);
        }
    }

    @Override
    public void onCategoryClicked(Category category) {
        presenter.onCategoryClicked(category);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.releasePresenter();
    }
}
