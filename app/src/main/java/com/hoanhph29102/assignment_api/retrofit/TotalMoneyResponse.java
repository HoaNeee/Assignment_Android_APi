package com.hoanhph29102.assignment_api.retrofit;

import com.google.gson.annotations.SerializedName;

public class TotalMoneyResponse {
    @SerializedName("totalMoney")
    private double totalMoney;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
