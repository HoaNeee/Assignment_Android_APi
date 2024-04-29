package com.hoanhph29102.assignment_api.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new DataUpdateInterceptor(listener))
//                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
