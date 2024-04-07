package com.hoanhph29102.assignment_api.model;

public class TestModel {
    private String tvImg, tvName;

    public TestModel(String tvImg, String tvName) {
        this.tvImg = tvImg;
        this.tvName = tvName;
    }

    public String getTvImg() {
        return tvImg;
    }

    public void setTvImg(String tvImg) {
        this.tvImg = tvImg;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }
}
