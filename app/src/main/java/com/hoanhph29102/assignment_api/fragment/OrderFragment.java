package com.hoanhph29102.assignment_api.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hoanhph29102.assignment_api.MainActivity;
import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.adapter.OrderAdapter;
import com.hoanhph29102.assignment_api.model.Order;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private List<Order> listOrder;
    private OrderAdapter orderAdapter;
    private RecyclerView rc_order;
    private SharedPreferences pref;
    private TextView tvEmptyOrder;
    private AlertDialog progressDialog;
    private ApiService apiService;


    private boolean isBottomNavigationVisible = true;

    // Khai báo biến để lưu vị trí cuộn của RecyclerView
    private int lastVisibleItemPosition = 0;

    // Hằng số để xác định hướng cuộn
    private static final int DIRECTION_UP = -1;
    private static final int DIRECTION_DOWN = 1;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showDialogProgress();
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initData(view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        fetchOrder();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = ((MainActivity) getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        rc_order.setLayoutManager(linearLayoutManager);

//        rc_order.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int direction = dy > 0 ? DIRECTION_UP : DIRECTION_DOWN;
//                // Lấy vị trí cuối cùng của item hiển thị
//                lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                // Nếu hướng cuộn là lên và thanh toolbar hoặc bottom navigation đang hiện thì ẩn chúng đi
//                if (direction == DIRECTION_UP && isBottomNavigationVisible) {
//                    hideBottomNavi();
//                } else if (direction == DIRECTION_DOWN && !isBottomNavigationVisible) {
//                    showBottomNavi();
//                }
//            }
//        });
    }

    @Override
    public void onResume() {
        fetchOrder();
        super.onResume();
    }
    private void initData(View view){
        rc_order = view.findViewById(R.id.rc_order);
        tvEmptyOrder = view.findViewById(R.id.tv_empty_order);
    }

    private void fetchOrder(){

        pref = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String id = pref.getString("userID", "");

        Call<List<Order>> call = apiService.getOrderWithUser(id);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()){
                    listOrder = response.body();
                    if (listOrder != null && !listOrder.isEmpty()){
                        orderAdapter = new OrderAdapter(getContext(), listOrder);
                        rc_order.setAdapter(orderAdapter);
                        dismissDialogProgress();
                        tvEmptyOrder.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), "Đơn hàng đang trống", Toast.LENGTH_SHORT).show();
                        dismissDialogProgress();
                        tvEmptyOrder.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(getContext(), "GET thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Không kết nối được đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("OrerFragment","GET"+t.getMessage());
                showDialogProgress();
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
