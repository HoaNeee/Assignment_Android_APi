package com.hoanhph29102.assignment_api.retrofit;

import com.google.gson.annotations.SerializedName;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.Food;
import com.hoanhph29102.assignment_api.model.Order;
import com.hoanhph29102.assignment_api.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    String DOMAIN = "http://192.168.1.5:3001/";

    //GET
    @GET("/")
    Call<List<Food>> getFood();

    @GET("/cart")
    Call<List<Cart>> getCart();

    @GET("/cart")
    Call<List<Cart>> getCartWithUser(@Header("userId") String userId);
    @GET("/cart/total-money")
    Call<TotalMoneyResponse> getTotalMoney(@Header("userId") String userId);

    @GET("/order")
    Call<List<Order>> getOrder();

    @GET("/order")
    Call<List<Order>> getOrderWithUser(@Header("userId") String userId);

    //POST
    @POST("/cart")
    Call<Void> addToCart(@Body Cart cart);
    @POST("/cart")
    Call<Void> addToCartWithUser(@Body Cart cart,
                                 @Header("userId") String userId);

//    @POST("user/register")
//    Call<Void> registerUser(@Body User user);

    @Multipart
    @POST("user/register")
    Call<Void> registerUser(
            @Part("Name") RequestBody name,
            @Part("Email") RequestBody email,
            @Part("Password") RequestBody password,
            @Part("Phone") RequestBody phone,
            @Part("Address") RequestBody address,
            @Part MultipartBody.Part avt
    );


    @POST("user/login")
    @FormUrlEncoded
    Call<User> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );
    @POST("user/logout")
    Call<Void> logoutUser();

    @POST("/order")
    Call<Void> createOrder(@Header("userId") String userId);
    @POST("order/clear-cart")
    Call<Void> clearCart(@Header("userId") String userId);
    @POST("/order/confirm/{id}")
    Call<Void> confirmOrder(@Path("id") String id);
    @POST("/order/cancel/{id}")
    Call<Void> cancelOrder(@Path("id") String id);

    //PUT
    @PUT("/cart/quantity/{id}")
    Call<Void> updateQuantity(@Path("id") String id, @Body QuantityUpdate quantity);

    @PUT("user/{id}")
    Call<Void> updateUser(@Path("id") String userID,
                          @Body User user);

    @Multipart
    @PUT("user/{id}")
    Call<User> updateUserPro(
            @Path("id") String userId,
            @Part("Name") RequestBody name,
            @Part("Email") RequestBody email,
            @Part("Phone") RequestBody phone,
            @Part("Address") RequestBody address,
            @Part MultipartBody.Part avt
    );
    @PUT("user/without-image/{id}")
    Call<User> updateUserProWithoutImage(
            @Path("id") String userId,
            @Body User user
    );

    //DELETE
    @DELETE("cart/{id}")
    Call<Void> deleteItemCart(@Path("id") String id);
}
