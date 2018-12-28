package com.official.nanorus.news.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.categories.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoriesRecyclerViewHolder> {

    public final String TAG = this.getClass().getSimpleName();

    private List<Category> dataList;
    private Context context;

    public CategoriesRecyclerViewAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
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

        Button categoryButton = holder.categoryButton;
        categoryButton.setText(data.getName());
        categoryButton.setBackground(holder.itemView.getContext().getResources().getDrawable(data.getImage()));
        categoryButton.setOnClickListener(view -> {
            try {
                ((CategoryListListener) context).onCategoryClicked(data);
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

        Button categoryButton;

        public CategoriesRecyclerViewHolder(View itemView) {
            super(itemView);
            categoryButton = itemView.findViewById(R.id.btn_category);
        }
    }
}
