package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.repository.CategoriesRepository;

import java.util.List;

import io.reactivex.Single;

public class CategoriesInteractor {

    private CategoriesRepository repository;

    public CategoriesInteractor() {
       repository = new CategoriesRepository();
    }

    public Single<List<Category>> getCategories() {
        return repository.getCategories();
    }

    public Single<List<Category>> refreshCategories() {
       return repository.refreshCategories();
    }
}
