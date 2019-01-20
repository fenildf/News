package com.official.nanorus.news.presentation.view.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.presentation.presenter.NewsPresenter;
import com.official.nanorus.news.presentation.ui.adapters.NewsRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements INewsView {

    public final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.rv_news)
    RecyclerView newsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewsRecyclerViewAdapter adapter;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_no_news)
    TextView noNewsTextView;
    @BindView(R.id.sv_search)
    SearchView searchView;

    private NewsPresenter presenter;

    public interface NewsListener {
        void setTitle(String title);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, view);

        initNewsList();
        presenter = new NewsPresenter();
        presenter.bindView(this);

        initViews();
        return view;
    }

    private void initViews() {
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onSwipeRefresh());
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_blue_light));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                presenter.onQueryEntered(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(view -> {
            searchView.setQuery("", false);
            presenter.onQueryEntered("");
        });
    }

    public void initNewsList() {
        newsRecyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(newsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new NewsRecyclerViewAdapter();
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void clearNewsList() {
        adapter.clearList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateNewsList(List<News> news) {
        adapter.updateList(news);
        adapter.notifyDataSetChanged();
    }

    public void getNews() {
        if (presenter != null)
            presenter.getRefreshedNews("");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.releasePresenter();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void scrollToTop() {
        newsRecyclerView.scrollToPosition(0);
    }

    @Override
    public void showLoading(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
        if (show) {
            setTitle(getString(R.string.loading));
        }
    }

    @Override
    public void setTitle(String title) {
        if (getActivity() instanceof NewsListener) {
            ((NewsListener) getActivity()).setTitle(title);
        }
    }

    @Override
    public void showNoNews(boolean show) {
        if (show)
            noNewsTextView.setVisibility(View.VISIBLE);
        else
            noNewsTextView.setVisibility(View.INVISIBLE);
    }
}
