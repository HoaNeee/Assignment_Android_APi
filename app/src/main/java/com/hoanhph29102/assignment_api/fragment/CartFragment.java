package com.hoanhph29102.assignment_api.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.adapter.CartAdapter;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.retrofit.ApiService;

import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import com.hoanhph29102.assignment_api.retrofit.TotalMoneyResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartFragment extends Fragment {
    private RecyclerView rcCart;
    private CartAdapter cartAdapter;

    private CartManager cartManager;

    private SwipeRefreshLayout refreshLayout;
    private AlertDialog progressDialog;
    private TextView tvEmptyCart, btnThanhToan, tvTongGia;

    private List<Cart> listCart;

    private SharedPreferences pref;
    private String userId;
    private ApiService apiService;
    private Double totalMoney;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showDialogProgress();
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initData(view);
        refreshLayout.setOnRefreshListener(this::refreshData);
        Retrofit retrofit = RetrofitClient.getClient();
        apiService = retrofit.create(ApiService.class);

        fetchCart();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcCart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcCart.setLayoutManager(linearLayoutManager);

        pref = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        userId = pref.getString("userID", "");
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                createOrder(userId);
            }
        });


    }

    @Override
    public void onResume() {
        fetchCart();
        super.onResume();
    }
    public void refreshData() {
        refreshLayout.setRefreshing(true);
        fetchCart();
    }

//    public void onDataUpdated(){
//        fetchCart();
//    }
    private void initData(View view){

        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        tvEmptyCart = view.findViewById(R.id.tv_empty_cart);

        rcCart = view.findViewById(R.id.rc_cart);
        btnThanhToan = view.findViewById(R.id.btn_thanh_toan);
        tvTongGia = view.findViewById(R.id.tv_tong_gia);
    }

    public void fetchCart(){
        pref = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String id = pref.getString("userID", "");

        //Log.d("CartFrag", "fetchCart: "+id);
        //ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Cart>> call = apiService.getCartWithUser(id);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()){
                    listCart = response.body();
                    if (listCart != null && !listCart.isEmpty()) {
                        // Danh sách không rỗng, cập nhật giao diện để hiển thị danh sách sản phẩm trong giỏ hàng
                        cartAdapter = new CartAdapter(getContext(), listCart);
                        rcCart.setAdapter(cartAdapter);
                        dismissDialogProgress();
                        tvEmptyCart.setVisibility(View.GONE);
                        getTotalMoney(id);
                    } else {
                        Toast.makeText(getContext(), "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                        tvEmptyCart.setVisibility(View.VISIBLE);
                        dismissDialogProgress();
                    }

                    refreshLayout.setRefreshing(false);
                } else {
                    // Xử lý phản hồi không thành công nếu cần
                    Toast.makeText(getContext(), "GET thất bại", Toast.LENGTH_SHORT).show();
                    showDialogProgress();
                }
                    //layoutTotalCart.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Toast.makeText(getContext(), "khong thẻ kết nối đến server", Toast.LENGTH_SHORT).show();
                Log.e("CartAdapter","GET cart"+t.getMessage());
            }
        });

    }
    private void createOrder(String id){
        //ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.createOrder(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    clearCartWithUser(id);
                    Toast.makeText(getContext(), "Tao đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();
                }else {
                    Toast.makeText(getContext(), "Tao don hang that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Khong thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("CartFragment","Create Order"+t.getMessage());
            }
        });
    }
    private void clearCartWithUser(String id){
        //ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.clearCart(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    fetchCart();
                    Toast.makeText(getContext(), "Da clear gio hang", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();
                } else {
                    Toast.makeText(getContext(), "clear gio hang that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Khong thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("CartFragment","Clear Cart"+t.getMessage());
            }
        });
    }
    private void getTotalMoney(String id){
        Call<TotalMoneyResponse> call = apiService.getTotalMoney(id);
        call.enqueue(new Callback<TotalMoneyResponse>() {
            @Override
            public void onResponse(Call<TotalMoneyResponse> call, Response<TotalMoneyResponse> response) {
                if (response.isSuccessful()) {
                    TotalMoneyResponse totalMoneyResponse = response.body();
                    if (totalMoneyResponse != null) {
                        totalMoney = totalMoneyResponse.getTotalMoney();

                        tvTongGia.setText(totalMoney+"");
                        //Toast.makeText(getContext(), "Tổng tiền trong giỏ hàng: " + totalMoney, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "GET totalMoney thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TotalMoneyResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("CartFragment", "GET totalMoney" + t.getMessage());
            }
        });
    }
    private void showDialogProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
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
}
