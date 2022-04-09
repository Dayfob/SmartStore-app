package com.diplom.smartstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class App implements Serializable {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("products")
    @Expose
    private List<Product> products;
    @SerializedName("categories")
    @Expose
    private List<Category> categories;
    @SerializedName("subcategories")
    @Expose
    private List<Subcategory> subcategories;
    @SerializedName("brands")
    @Expose
    private List<Brand> brands;
    @SerializedName("news")
    @Expose
    private List<News> news;

    public App(User user, List<Product> products, List<Category> categories, List<Subcategory> subcategories, List<Brand> brands, List<News> news) {
        this.user = user;
        this.products = products;
        this.categories = categories;
        this.subcategories = subcategories;
        this.brands = brands;
        this.news = news;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
