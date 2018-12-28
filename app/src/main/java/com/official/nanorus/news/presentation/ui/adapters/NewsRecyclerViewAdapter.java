package com.official.nanorus.news.presentation.ui.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.news.News;
import com.official.nanorus.news.navigation.Router;

import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsRecyclerViewHolder> {

    public final String TAG = this.getClass().getSimpleName();

    private List<News> dataList;
    private Router router;

    public NewsRecyclerViewAdapter() {
        dataList = new ArrayList<>();
        router = new Router();
    }

    @NonNull
    @Override
    public NewsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewHolder holder, int position) {
        News data = dataList.get(position);
        holder.titleTextView.setText(data.getTitle());
        holder.descriptionTextView.setText(data.getDescription());
        holder.dateTextView.setText(data.getPublishedAt());
        holder.photoImageView.setVisibility(View.GONE);
        holder.moreButton.setOnClickListener(view -> router.openUrlIntBrowser(data.getUrl(), holder.itemView.getContext()));
        if (data.getUrlToImage() != null) {
            holder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(data.getUrlToImage())
                    .apply(new RequestOptions().fitCenter())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d(TAG, position + ". " + data.getTitle() + ": load failed");
                            holder.photoImageView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.photoImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    public void clearList() {
        if (dataList != null)
            dataList.clear();
    }

    public void updateList(List<News> list) {
        dataList.addAll(0, list);
    }


    public class NewsRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        ImageView photoImageView;
        Button moreButton;

        public NewsRecyclerViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title);
            dateTextView = itemView.findViewById(R.id.tv_date);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            photoImageView = itemView.findViewById(R.id.iv_photo);
            moreButton = itemView.findViewById(R.id.btn_more);
        }
    }
}
