package com.hoanhph29102.assignment_api.model;

public class Cart {
    private String _id;
    private String Food_id;
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

    public Cart(int quantity, String nameFood, double priceFood,String imageFood) {
        this.imageFood = imageFood;
        this.quantity = quantity;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
    }
    public Cart(){

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
