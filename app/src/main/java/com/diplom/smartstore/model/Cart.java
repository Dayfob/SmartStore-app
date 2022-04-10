package com.diplom.smartstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {

    @SerializedName("products")
    @Expose
    private List<Product> products;
    @SerializedName("sumPrice")
    @Expose
    private int sumPrice;

    public Cart(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getSumPrice() {
        int sumPrice = 0;

        for (Product product : products) {
            sumPrice += product.getPrice() * product.getAmountCart();
        }
        return sumPrice;
    }

    public void setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
    }
}
