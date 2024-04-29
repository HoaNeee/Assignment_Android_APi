package com.hoanhph29102.assignment_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Order {
    private String _id;
    @SerializedName("UserID")
    private String userId;
    @SerializedName("CartID")
    private String cartId;
    @SerializedName("nameUser")
    private String nameUser;
    @SerializedName("phoneUser")
    private String phoneUser;
    @SerializedName("addressUser")
    private String addressUser;
    @SerializedName("Date")
    private String date;
    @SerializedName("totalMoney")
    private double totalMoney;
    @SerializedName("items")
    private List<OrderItem> items;
    @SerializedName("status")
    private int status;

    public Order() {
    }

    public Order(String _id, String userId, String cartId, String nameUser, String phoneUser, String addressUser, String date, double totalMoney, List<OrderItem> items, int status) {
        this._id = _id;
        this.userId = userId;
        this.cartId = cartId;
        this.nameUser = nameUser;
        this.phoneUser = phoneUser;
        this.addressUser = addressUser;
        this.date = date;
        this.totalMoney = totalMoney;
        this.items = items;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(String addressUser) {
        this.addressUser = addressUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class OrderItem{
        private String nameFood;
        private int quantity;

        public OrderItem(String nameFood, int quantity) {
            this.nameFood = nameFood;
            this.quantity = quantity;
        }

        public String getNameFood() {
            return nameFood;
        }

        public void setNameFood(String nameFood) {
            this.nameFood = nameFood;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
