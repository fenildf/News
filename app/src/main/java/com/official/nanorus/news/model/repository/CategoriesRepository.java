package com.official.nanorus.news.model.repository;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.data.ResourceManager;
import com.official.nanorus.news.model.data.database.categories.CategoriesDatabaseManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class CategoriesRepository {

    private CategoriesDatabaseManager databaseManager;
    private ResourceManager resourceManager;

    public CategoriesRepository() {
        databaseManager = CategoriesDatabaseManager.getInstance();
        resourceManager = new ResourceManager();
    }

    public Single<List<Category>> getCategories() {
        return databaseManager.getCategoriesList();
    }

    private void putCategories(List<Category> categories) {
        databaseManager.clearCategories();
        databaseManager.putCategories(categories);
    }

    public Single<List<Category>> refreshCategories() {

        Single<List<Category>> sourceListSingle = Single.create(emitter -> {
            List<Category> categories = new ArrayList<>();
            categories.add(new Category(1, "business", resourceManager.getNewsCategoryImage("business")));
            categories.add(new Category(2, "entertainment", resourceManager.getNewsCategoryImage("entertainment")));
            categories.add(new Category(3, "health", resourceManager.getNewsCategoryImage("health")));
            categories.add(new Category(4, "science", resourceManager.getNewsCategoryImage("science")));
            categories.add(new Category(5, "sports", resourceManager.getNewsCategoryImage("sports")));
            categories.add(new Category(6, "technology", resourceManager.getNewsCategoryImage("technology")));
            emitter.onSuccess(categories);
        });

        Single<List<Category>> refreshedListSingle = sourceListSingle.map(categories -> {
            putCategories(categories);
            return categories;
        });
        return refreshedListSingle;
    }
}
