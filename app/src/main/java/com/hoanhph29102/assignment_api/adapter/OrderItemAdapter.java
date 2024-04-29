package com.hoanhph29102.assignment_api.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.Order;

import java.util.List;

public class OrderItemAdapter extends BaseAdapter {
    private Context context;
    private List<Order.OrderItem> listOrderItem;

    public OrderItemAdapter(Context context, List<Order.OrderItem> listOrderItem) {
        this.context = context;
        this.listOrderItem = listOrderItem;
    }

    @Override
    public int getCount() {
        return listOrderItem.size();
    }

    @Override
    public Object getItem(int i) {
        return listOrderItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view1 = inflater.inflate(R.layout.item_list_view_order, null);

        TextView nameFoodTextView = view1.findViewById(R.id.tv_name_food_order);
        TextView quantityTextView = view1.findViewById(R.id.tv_quantity_order);

        Order.OrderItem orderItem = listOrderItem.get(i);
        nameFoodTextView.setText(orderItem.getNameFood());
        quantityTextView.setText(String.valueOf(orderItem.getQuantity()));

        Log.e("OrderItem","OrderItem" + listOrderItem.size());

        return view1;

    }
}
