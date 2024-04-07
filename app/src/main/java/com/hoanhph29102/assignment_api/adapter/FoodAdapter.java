package com.hoanhph29102.assignment_api.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.model.Food;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.viewHolder>{

    Context context;
    List<Food> listFood;

    CartManager cartManager;
    public FoodAdapter(Context context, List<Food> listFood) {
        this.context = context;
        this.listFood = listFood;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.item_food,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Food food = listFood.get(position);


        holder.tvTen.setText(food.getName());
        holder.tvMota.setText(food.getDescription());
        holder.tvGia.setText(String.valueOf(food.getPrice()));
        //holder.icon.setImageResource(iconDAO.icon(list.get(position).getMaIcon()).getIcon());
        Picasso.get().load(food.getImage()).into(holder.imgFood);

        holder.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addToCart(food);

            }
        });
    }


    public void addToCart(Food food){
        if (CartManager.listCart.size() >0){
            int quantity = 1;
            boolean flag = false;
            for (int i = 0; i<CartManager.listCart.size(); i++){
                if (CartManager.listCart.get(i).getFood_id() == food.get_id()){
                    CartManager.listCart.get(i).setQuantity(quantity + CartManager.listCart.get(i).getQuantity());
                    double price = (food.getPrice() * CartManager.listCart.get(i).getQuantity());
                    CartManager.listCart.get(i).setPriceFood(price);
                    flag = true;
                }

            }
            if (flag == false){
                String id = food.get_id(); // Lấy ID của sản phẩm
                String nameFood = food.getName(); // Lấy tên của sản phẩm
                double priceFood = food.getPrice(); // Lấy giá của sản phẩm
                String imageFood = food.getImage();

                Cart cart = new Cart();
                cart.setFood_id(id);
                cart.setNameFood(nameFood);
                cart.setPriceFood(priceFood);
                cart.setQuantity(quantity);
                cart.setImageFood(imageFood);

                CartManager.listCart.add(cart);
            }
        } else {
            String id = food.get_id(); // Lấy ID của sản phẩm
            String nameFood = food.getName(); // Lấy tên của sản phẩm
            double priceFood = food.getPrice(); // Lấy giá của sản phẩm
            String imageFood = food.getImage();

            int quantity = 1; // Lấy số lượng của sản phẩm

            Cart cart = new Cart();
            cart.setFood_id(id);
            cart.setNameFood(nameFood);
            cart.setPriceFood(priceFood);
            cart.setQuantity(quantity);
            cart.setImageFood(imageFood);

            CartManager.listCart.add(cart);
        }


        Toast.makeText(context, "thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvMota, tvGia,tvID;
        ImageView imgFood;
        FloatingActionButton fabAdd;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tv_ten_food);
            tvMota = itemView.findViewById(R.id.tv_descr_food);
            tvGia = itemView.findViewById(R.id.tv_price_food);
            imgFood = itemView.findViewById(R.id.img_food);
            fabAdd = itemView.findViewById(R.id.fab_add_food);
        }
    }
}
