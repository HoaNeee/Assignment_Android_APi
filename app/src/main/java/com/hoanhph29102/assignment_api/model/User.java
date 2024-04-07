package com.hoanhph29102.assignment_api.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    private String _id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Email")
    private String email;
    @SerializedName("Password")
    private String password;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public User(String _id, String name, String email, String password) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
