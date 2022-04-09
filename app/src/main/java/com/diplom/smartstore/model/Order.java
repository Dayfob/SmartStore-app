package com.diplom.smartstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("isSent")
    @Expose
    private boolean isSent;
    @SerializedName("isPaid")
    @Expose
    private boolean isPaid;
    @SerializedName("paymentMethod")
    @Expose
    private String paymentMethod;
    @SerializedName("deliveryMethod")
    @Expose
    private String deliveryMethod;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("addInformation")
    @Expose
    private String addInformation;
    @SerializedName("deliveryPrice")
    @Expose
    private int deliveryPrice;

    public Order(Integer id, String status, User user, Integer totalPrice, boolean isSent,
                 boolean isPaid, String paymentMethod, String deliveryMethod, String address,
                 String addInformation, int deliveryPrice) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.totalPrice = totalPrice;
        this.isSent = isSent;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.address = address;
        this.addInformation = addInformation;
        this.deliveryPrice = deliveryPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddInformation() {
        return addInformation;
    }

    public void setAddInformation(String addInformation) {
        this.addInformation = addInformation;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
