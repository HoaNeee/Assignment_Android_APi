package com.hoanhph29102.assignment_api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hoanhph29102.assignment_api.Animation.DepthPageTransformer;
import com.hoanhph29102.assignment_api.adapter.CartAdapter;
import com.hoanhph29102.assignment_api.adapter.ViewPagerAdapter;
import com.hoanhph29102.assignment_api.fragment.CartFragment;
import com.hoanhph29102.assignment_api.fragment.FoodFragment;
import com.hoanhph29102.assignment_api.fragment.OrderFragment;
import com.hoanhph29102.assignment_api.fragment.UserFragment;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.model.Food;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    UserFragment userFragment;
    CartFragment cartFragment;
    FoodFragment foodFragment;
    OrderFragment orderFragment;
    ImageView iconSearch, iconLogout;
    Toolbar toolbar;
    FragmentContainerView fragContainer;

    //dung de luu tru tam thoi
    CartAdapter cartAdapter;
    private AlertDialog progressDialog;
    private RelativeLayout lnBottomNavi,lnArrUp;

    private boolean isBottomNavigationVisible = true;
    Animation slideAnimation;
    MenuItem itemArrDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //viewPager2 = findViewById(R.id.view_pager);

        toolbar = findViewById(R.id.toolbar_main);
        iconLogout = findViewById(R.id.icon_logout);
        iconSearch = findViewById(R.id.icon_search);
        lnBottomNavi = findViewById(R.id.ln_bottom_navi);
        lnArrUp = findViewById(R.id.ln_arr_up);

         slideAnimation = AnimationUtils.loadAnimation(this,R.anim.move_down);


        fragContainer = findViewById(R.id.fragment_container_main);

        //cartAdapter = new CartAdapter(this, CartManager.listCart);

//        if (CartManager.listCart == null){
//            CartManager.listCart = new ArrayList<>();
//        }


        iconLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        lnArrUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation slideUpAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.move_up);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.setEnabled(true);
                bottomNavigationView.startAnimation(slideUpAnimation);
                slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        showBottomNavigationWithButton();
                        hideArrUp();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lnArrUp.setVisibility(View.GONE);
                        lnArrUp.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        foodFragment = new FoodFragment();
        cartFragment = new CartFragment();
        orderFragment = new OrderFragment();
        userFragment = new UserFragment();

        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,foodFragment,cartFragment,orderFragment,userFragment);

//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,foodFragment,cartFragment,orderFragment,userFragment);
//        viewPager2.setAdapter(viewPagerAdapter);
//        viewPager2.setUserInputEnabled(false);
//        viewPager2.setPageTransformer(new DepthPageTransformer());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_main, foodFragment)
                .commit();

        itemArrDown = bottomNavigationView.getMenu().findItem(R.id.nav_arr_down);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();




                if (id == R.id.nav_home){

                    //viewPager2.setCurrentItem(0);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_main, foodFragment)
                            .commit();

                    itemArrDown.setEnabled(true);

                    return true;
                }else if (id == R.id.nav_cart){

                    //viewPager2.setCurrentItem(1);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_main, cartFragment)
                            .commit();

                    itemArrDown.setEnabled(false);

                    return true;

                }
                else if (id == R.id.nav_order){

                    //viewPager2.setCurrentItem(2);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_main, orderFragment)
                            .commit();

                    itemArrDown.setEnabled(true);

                    return true;

                }
                else if (id == R.id.nav_arr_down){
                    //hideBottomNavigationWithButton();
                    lnArrUp.setVisibility(View.VISIBLE);
                    lnArrUp.setEnabled(true);
                    bottomNavigationView.startAnimation(slideAnimation);
                    slideAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            hideBottomNavigationWithButton();
                            showArrUp();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            bottomNavigationView.setVisibility(View.GONE);
                            bottomNavigationView.setEnabled(false);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //return true;
                }


                return false;
            }
        });

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position){
//                    case 0:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
//                        break;
//                    case 1:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
//                        break;
//                    case 2:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_order).setChecked(true);
//                        break;
////                    case 3:
//////                        bottomNavigationView.getMenu().findItem(R.id.nav_user).setChecked(false);
//////                        break;
//
//                }
//            }
//        });
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.menu_hello) {

                    return true;
                } else if (id == R.id.menu_user_info) {

                    //viewPager2.setCurrentItem(3);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_main, userFragment)
                            .commit();
                    itemArrDown.setEnabled(true);
                    return true;
                } else if (id == R.id.menu_logout) {
                    showDiaglogLogout();
                    return true;
                }
                itemArrDown.setEnabled(true);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_toolbar);

        SharedPreferences preferences = getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String userName = preferences.getString("name", "User");

        MenuItem menuItemName = popupMenu.getMenu().findItem(R.id.menu_hello);
        menuItemName.setTitle("Xin chào, "+userName);
        menuItemName.setEnabled(false);

        popupMenu.show();
    }

    public void hideBottomNavi() {
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }
    public void showBottomNavi() {

        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private void hideBottomNavigationWithButton() {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, lnBottomNavi.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        lnBottomNavi.startAnimation(animate);

    }

    private void showBottomNavigationWithButton() {
        TranslateAnimation animate = new TranslateAnimation(0, 0, lnBottomNavi.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        lnBottomNavi.startAnimation(animate);
    }
    private void showArrUp(){
        TranslateAnimation animate = new TranslateAnimation(0, 0, lnBottomNavi.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        lnArrUp.startAnimation(animate);
    }
    private void hideArrUp(){
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, lnBottomNavi.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        lnArrUp.startAnimation(animate);
    }

    private void showDialogProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);

        builder.setView(dialogView);
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }
    private void dismissDialogProgress(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void showDiaglogLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc muốn đăng xuất không?");
        builder.setIcon(R.drawable.logout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDialogProgress();
                Logout();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Logout(){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.logoutUser();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Khong thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity","Log out"+t.getMessage());
                showDialogProgress();
            }
        });
    }

}