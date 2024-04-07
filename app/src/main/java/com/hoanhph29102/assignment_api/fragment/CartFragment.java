package com.hoanhph29102.assignment_api.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.adapter.CartAdapter;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private RecyclerView rcCart;
    private CartAdapter cartAdapter;

    CartManager cartManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcCart = view.findViewById(R.id.rc_cart);

        rcCart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcCart.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(getContext(),CartManager.listCart);
        rcCart.setAdapter(cartAdapter);

    }

    @Override
    public void onResume() {
        cartAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
