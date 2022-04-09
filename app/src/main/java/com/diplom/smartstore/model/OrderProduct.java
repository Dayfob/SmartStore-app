package com.diplom.smartstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderProduct implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("item")
    @Expose
    private Product product;
    @SerializedName("itemAmount")
    @Expose
    private Integer itemAmount;

    public OrderProduct(Integer id, Product product, Integer itemAmount) {
        this.id = id;
        this.product = product;
        this.itemAmount = itemAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(Integer itemAmount) {
        this.itemAmount = itemAmount;
    }
}
