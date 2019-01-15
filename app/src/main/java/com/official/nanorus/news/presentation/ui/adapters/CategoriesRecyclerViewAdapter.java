package com.official.nanorus.news.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.data.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoriesRecyclerViewHolder> {

    public final String TAG = this.getClass().getSimpleName();

    private List<Category> dataList;
    private Context context;
    ResourceManager resourceManager;
    CategoryListListener categoryListListener;

    public CategoriesRecyclerViewAdapter(Context context, CategoryListListener categoryListListener) {
        this.context = context;
        dataList = new ArrayList<>();
        resourceManager = new ResourceManager();
        this.categoryListListener = categoryListListener;
    }

    public interface CategoryListListener {
        void onCategoryClicked(Category category);
    }

    @NonNull
    @Override
    public CategoriesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_list_item, parent, false);
        return new CategoriesRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRecyclerViewHolder holder, int position) {
        Category data = dataList.get(position);
        holder.categoryTextView.setText(data.getName());
        ImageButton categoryButton = holder.categoryButton;
        Glide.with(context).load(resourceManager.getNewsCategoryImage(data.getImage()))
                .into(categoryButton);

        categoryButton.setOnClickListener(view -> {
            try {
                categoryListListener.onCategoryClicked(data);
            } catch (ClassCastException cce) {
                Log.d(TAG, cce.getMessage());
            }
        });
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

    public void updateList(List<Category> list) {
        dataList.addAll(0, list);
    }


    public class CategoriesRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageButton categoryButton;
        TextView categoryTextView;

        public CategoriesRecyclerViewHolder(View itemView) {
            super(itemView);
            categoryButton = itemView.findViewById(R.id.btn_category);
            categoryTextView = itemView.findViewById(R.id.tv_category);
        }
    }
}
