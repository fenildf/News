package com.official.nanorus.contacts.presentation.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsPresenter presenter;

    public interface NewsListener {
        void setTitle(String title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, view);

        initNewsList();
        presenter = new NewsPresenter();
        presenter.bindView(this);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onSwipeRefresh());
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

    public void showEnterQuery() {
        showQueryDialog();
    }

    public void showLoading(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    public void showQueryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.enter_news_query);
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.search, (dialog, which) -> presenter.onQueryEntered(input.getText().toString()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void setTitle(String title) {
        if (getActivity() instanceof NewsListener) {
            ((NewsListener) getActivity()).setTitle(title);
        }
    }
}
