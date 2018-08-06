package com.official.nanorus.contacts.presentation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.data.news.News;
import com.official.nanorus.contacts.presentation.presenter.NewsPresenter;
import com.official.nanorus.contacts.presentation.ui.adapters.NewsRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.rv_news)
    RecyclerView newsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
   private NewsRecyclerViewAdapter adapter;

    private NewsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, view);

        initNewsList();
        presenter = new NewsPresenter();
        presenter.bindView(this);

        return view;
    }

    public void initNewsList() {
        layoutManager = new LinearLayoutManager(newsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
       adapter = new NewsRecyclerViewAdapter();
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.setLayoutManager(layoutManager);
    }

    public void clearNewsList() {
        adapter.clearList();
        adapter.notifyDataSetChanged();
    }

    public void updateNewsList(List<News> news) {
        adapter.updateList(news);
        adapter.notifyDataSetChanged();
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
}
