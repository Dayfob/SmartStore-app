package com.diplom.smartstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("subcategory")
    @Expose
    private Subcategory subcategory;
    @SerializedName("amountCart")
    @Expose
    private int amountCart;

    @SerializedName("amountLeft")
    @Expose
    private int amountLeft;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes;

    public Product(Integer id, String name, String slug, String imgUrl, String description,
                   Brand brand, Category category, Subcategory subcategory,
                   int amountCart, int amountLeft, int price, List<Attribute> attributes) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.imgUrl = imgUrl;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.subcategory = subcategory;
        this.amountCart = amountCart;
        this.amountLeft = amountLeft;
        this.price = price;
        this.attributes = attributes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public int getAmountCart() {
        return amountCart;
    }

    public void setAmountCart(int amountCart) {
        this.amountCart = amountCart;
    }

    public int getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(int amountLeft) {
        this.amountLeft = amountLeft;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
