package com.official.nanorus.news.entity.data.categories;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private String defaultName;
    private String image;

    private static List<Category> categories = new ArrayList<>();

    public static List<Category> getCategories() {
        return categories;
    }

    public Category(int id, String name, String defaultName, String image) {
        this.id = id;
        this.name = name;
        this.defaultName = defaultName;
        this.image = image;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
