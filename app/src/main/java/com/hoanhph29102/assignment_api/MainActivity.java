package com.hoanhph29102.assignment_api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hoanhph29102.assignment_api.Animation.DepthPageTransformer;
import com.hoanhph29102.assignment_api.adapter.CartAdapter;
import com.hoanhph29102.assignment_api.adapter.ViewPagerAdapter;
import com.hoanhph29102.assignment_api.fragment.CartFragment;
import com.hoanhph29102.assignment_api.fragment.FoodFragment;
import com.hoanhph29102.assignment_api.fragment.UserFragment;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.model.Food;
import com.hoanhph29102.assignment_api.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    UserFragment userFragment;
    CartFragment cartFragment;
    FoodFragment foodFragment;
    ImageView iconSearch, iconLogout;
    Toolbar toolbar;

    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        viewPager2 = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar_main);
        iconLogout = findViewById(R.id.icon_logout);
        iconSearch = findViewById(R.id.icon_search);

        cartAdapter = new CartAdapter(this, CartManager.listCart);

        if (CartManager.listCart == null){
            CartManager.listCart = new ArrayList<>();
        }


        iconLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logout nè", Toast.LENGTH_SHORT).show();
            }
        });

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Search nè", Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setPageTransformer(new DepthPageTransformer());


        foodFragment = new FoodFragment();
        userFragment = new UserFragment();
        cartFragment = new CartFragment();




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home){
                    viewPager2.setCurrentItem(0);
                }else if (id == R.id.nav_cart){
                    cartAdapter.notifyDataSetChanged();
                    viewPager2.setCurrentItem(1);
                }
                else if (id == R.id.nav_order){
                    viewPager2.setCurrentItem(2);
                }else if (id == R.id.nav_user){
                    viewPager2.setCurrentItem(3);
                }

                return true;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_order).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                        break;

                }
            }
        });
    }

    public void hideBottomNavi() {

        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }
    public void showBottomNavi() {

        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

}