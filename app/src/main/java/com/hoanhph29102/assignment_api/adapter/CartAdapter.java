package com.hoanhph29102.assignment_api.adapter;



import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.QuantityUpdate;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder>{

    Context context;
    List<Cart> listCart;

    public CartAdapter(Context context, List<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.item_cart,parent,false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Cart cart = listCart.get(position);

        holder.tvNameCart.setText(cart.getNameFood());
        holder.tvPriceCart.setText(cart.getPriceFood()+"");
        holder.tvQuantity.setText(cart.getQuantity()+"");

        //Log.d("IDCART", "onBindViewHolder: "+ cart.get_id() );


        Picasso.get().load(cart.getImageFood()).into(holder.imgItem);

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteItemCart(cart.get_id());
            }
        });


        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = cart.getQuantity() + 1;
                QuantityUpdate quantityUpdate = new QuantityUpdate(newQuantity);
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                updateQuantity(cart.get_id(), quantityUpdate);
            }
        });
        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = cart.getQuantity() - 1;
                if (newQuantity > 0){
                    QuantityUpdate quantityUpdate = new QuantityUpdate(newQuantity);
                    holder.tvQuantity.setText(String.valueOf(newQuantity));
                    updateQuantity(cart.get_id(), quantityUpdate);
                    holder.imgMinus.setEnabled(true);
                } else {
                    Toast.makeText(context, "Số lượng đã đạt tối thiểu", Toast.LENGTH_SHORT).show();
                    holder.imgMinus.setEnabled(false);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCart, tvPriceCart, tvQuantity,tvID;
        ImageView imgItem, imgDel,imgMinus,imgPlus;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameCart = itemView.findViewById(R.id.tv_name_item_cart);
            tvPriceCart = itemView.findViewById(R.id.tv_price_cart);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            imgItem = itemView.findViewById(R.id.img_item_cart);
            imgDel = itemView.findViewById(R.id.img_del);
            imgMinus = itemView.findViewById(R.id.img_minus);
            imgPlus = itemView.findViewById(R.id.img_plus);

        }
    }
    private void updateQuantity(String id, QuantityUpdate quantity){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.updateQuantity(id,quantity);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "khong thanh cong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Không kết nối được đến server", Toast.LENGTH_SHORT).show();
                Log.e("CartAdapter", "UPDATE QUANTITY"+t.getMessage());
            }
        });
    }

    private void DeleteItemCart(String id){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteItemCart(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Không kết nối được đến server", Toast.LENGTH_SHORT).show();
                Log.e("CartAdapter", "DELETE"+t.getMessage());
            }
        });
    }
}
