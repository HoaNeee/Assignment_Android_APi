package com.hoanhph29102.assignment_api.viewM;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoanhph29102.assignment_api.model.Order;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private MutableLiveData<List<Order>> orderList;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    private ApiService apiService;

    public OrderViewModel() {
        orderList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<List<Order>> getOrderList() {
        return orderList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void getOrderWithUser(String userId) {
        isLoading.setValue(true);
        Call<List<Order>> call = apiService.getOrderWithUser(userId);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    orderList.setValue(response.body());
                } else {
                    errorMessage.setValue("Failed to load orders.");
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error. Please check your connection.");
            }
        });
    }

    public void confirmOrder(String orderId) {
        isLoading.setValue(true);
        Call<Void> call = apiService.confirmOrder(orderId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.setValue(false);
                if (!response.isSuccessful()) {
                    errorMessage.setValue("Failed to confirm order.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error. Please check your connection.");
            }
        });
    }
}
