package com.hoanhph29102.assignment_api.retrofit;

import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.Food;
import com.hoanhph29102.assignment_api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    String DOMAIN = "http://192.168.1.172:3001/";

    @GET("/")
    Call<List<Food>> getFood();

    @POST("user/register")

    Call<Void> registerUser(@Body User user);

    @POST("user/login")
    @FormUrlEncoded
    Call<User> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @PUT("user/{id}")
    Call<Void> updateUser(@Path("id") String userID,
                          @Body User user);

    @GET("user/profile")
    Call<User> getUserProfile();
}
