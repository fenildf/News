package com.official.nanorus.news.model.domain;

import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.repository.CategoriesRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CategoriesInteractor {

    private CategoriesRepository repository;

    public CategoriesInteractor() {
       repository = new CategoriesRepository();
    }

    public Single<List<Category>> getCategories() {
        return repository.getCategories();
    }

    private Single<List<Category>> refreshCategories() {
       return repository.getCategories();
    }

    public Completable insertDefaultCategories() {
        return repository.insertDefaultCategories();
    }

    public void clearCategories(){
        repository.clearCategories();
    }
}
