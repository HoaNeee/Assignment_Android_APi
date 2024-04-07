package com.hoanhph29102.assignment_api.adapter;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.Cart;
import com.hoanhph29102.assignment_api.model.CartManager;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        Picasso.get().load(cart.getImageFood()).into(holder.imgItem);

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCart, tvPriceCart, tvQuantity;
        ImageView imgItem, imgDel;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameCart = itemView.findViewById(R.id.tv_name_item_cart);
            tvPriceCart = itemView.findViewById(R.id.tv_price_cart);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            imgItem = itemView.findViewById(R.id.img_item_cart);
            imgDel = itemView.findViewById(R.id.img_del);
        }
    }
}
