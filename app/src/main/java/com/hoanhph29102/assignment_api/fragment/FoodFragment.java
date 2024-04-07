package com.hoanhph29102.assignment_api.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hoanhph29102.assignment_api.MainActivity;
import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.adapter.CartAdapter;
import com.hoanhph29102.assignment_api.adapter.FoodAdapter;
import com.hoanhph29102.assignment_api.adapter.TestAdapter;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.model.Food;
import com.hoanhph29102.assignment_api.model.TestModel;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodFragment extends Fragment {
    RecyclerView rcFood;
    TestAdapter testAdapter;

    FoodAdapter foodAdapter;
    MainActivity mainActivity;
    private BottomNavigationView bottomNavigationView;

    private boolean isBottomNavigationVisible = true;

    // Khai báo biến để lưu vị trí cuộn của RecyclerView
    private int lastVisibleItemPosition = 0;

    // Hằng số để xác định hướng cuộn
    private static final int DIRECTION_UP = -1;
    private static final int DIRECTION_DOWN = 1;

    List<Food> listFood;
    List<Cart> listCart;
    private CartManager cartManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcFood = view.findViewById(R.id.rc_food);
        mainActivity = (MainActivity)getActivity();

        //ẩn bottom menu
        bottomNavigationView = ((MainActivity) getActivity()).findViewById(R.id.bottom_navigation);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rcFood.setLayoutManager(gridLayoutManager);

        rcFood.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int direction = dy > 0 ? DIRECTION_UP : DIRECTION_DOWN;
                // Lấy vị trí cuối cùng của item hiển thị
                lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                // Nếu hướng cuộn là lên và thanh toolbar hoặc bottom navigation đang hiện thì ẩn chúng đi
                if (direction == DIRECTION_UP && isBottomNavigationVisible) {
                    hideBottomNavi();
                } else if (direction == DIRECTION_DOWN && !isBottomNavigationVisible) {
                    showBottomNavi();
                }
            }
        });


        fetchFood();
    }

    private void fetchFood(){

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Food>> call = apiService.getFood();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()){
                    listFood = response.body();
                    foodAdapter = new FoodAdapter(getContext(),listFood);
                    rcFood.setAdapter(foodAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("foodFragment", t.getMessage());
            }
        });
    }

    private void hideBottomNavi() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavi();
            isBottomNavigationVisible = false;
        }
    }

    // Hàm hiện thanh bottom navigation
    private void showBottomNavi() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showBottomNavi();
            isBottomNavigationVisible = true;
        }
    }
}
