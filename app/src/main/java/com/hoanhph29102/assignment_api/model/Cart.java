package com.hoanhph29102.assignment_api.model;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("_id")
    private String _id;
    @SerializedName("foodId")
    private String Food_id;
    @SerializedName("userId")
    private String UserID;
    @SerializedName("quantity")
    private int quantity;
    private String nameFood;
    private double priceFood;
    private String imageFood;

    public Cart(String _id, String food_id, int quantity, String nameFood, double priceFood,String imageFood) {
        this._id = _id;
        this.Food_id = food_id;
        this.imageFood = imageFood;
        this.quantity = quantity;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
    }

    public Cart(String food_id, int quantity, String nameFood, double priceFood, String imageFood) {
        this.Food_id = food_id;
        this.quantity = quantity;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.imageFood = imageFood;
    }

    public Cart(){
    }
    public Cart(String food_id, int quantity,String userID, String nameFood, double priceFood, String imageFood) {
        this.Food_id = food_id;
        this.quantity = quantity;
        this.UserID = userID;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.imageFood = imageFood;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

//    public String getUser_id() {
//        return User_id;
//    }

//    public void setUser_id(String user_id) {
//        User_id = user_id;
//    }

    public String getFood_id() {
        return Food_id;
    }

    public void setFood_id(String food_id) {
        Food_id = food_id;
    }

    public String getImageFood() {
        return imageFood;
    }

    public void setImageFood(String imageFood) {
        this.imageFood = imageFood;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public double getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(double priceFood) {
        this.priceFood = priceFood;
    }
}
