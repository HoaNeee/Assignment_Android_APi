package com.hoanhph29102.assignment_api.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.Order;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder>{
    private Context context;
    private List<Order> listOrder;
    private AlertDialog progressDialog;
    private String strStatus = "";


    public OrderAdapter(Context context, List<Order> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_order,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Order order = listOrder.get(position);

        holder.tvId.setText(order.get_id());
        holder.tvNameUser.setText(order.getNameUser());
        holder.tvSdtUser.setText(order.getPhoneUser());
        holder.tvAddressUser.setText(order.getAddressUser());
        holder.tvNgayTao.setText(order.getDate().toString());
        holder.tvTongTien.setText(String.valueOf(order.getTotalMoney()));
        if (order.getStatus() == 0){
            strStatus = "Chưa thanh toán";
            holder.tvStatus.setText(strStatus);
            holder.tvStatus.setTextColor(Color.RED);
            holder.lnConfirm.setVisibility(View.VISIBLE);
        } else if (order.getStatus() == 1){
            strStatus = "Đã thanh toán";
            holder.tvStatus.setText(strStatus);
            holder.tvStatus.setTextColor(Color.GREEN);
            holder.lnConfirm.setVisibility(View.GONE);
        }else if (order.getStatus() == 2){
            strStatus = "Đã hủy";
            holder.tvStatus.setText(strStatus);
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.gray));
            holder.lnConfirm.setVisibility(View.GONE);
            holder.itemView.setBackgroundResource(R.drawable.background_cancel_item);
        }

        //kieu list
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(context, order.getItems());

        //kieu text
        StringBuilder productListBuilder = new StringBuilder();
        for (Order.OrderItem item : order.getItems()) {
            productListBuilder.append(item.getNameFood()).append(" - ").append(item.getQuantity()).append("\n");
        }
        String productList = productListBuilder.toString();

        holder.lvDanhSach.setText(productList);

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                confirmOrder(order.get_id());
                strStatus = "Đã thanh toán";
                order.setStatus(1);
                holder.tvStatus.setTextColor(Color.GREEN);
                holder.tvStatus.setText(strStatus);
                holder.lnConfirm.setVisibility(View.GONE);

            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                cancelOrder(order.get_id());
                strStatus = "Đã hủy";
                order.setStatus(2);
                holder.tvStatus.setTextColor(Color.GRAY);
                holder.tvStatus.setText(strStatus);
                holder.lnConfirm.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNameUser, tvSdtUser, tvAddressUser, tvNgayTao, tvTongTien, tvStatus;
        TextView lvDanhSach;
        TextView btnCancel, btnConfirm;
        LinearLayout lnConfirm;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id_order);
            tvNameUser = itemView.findViewById(R.id.tv_ten_user);
            tvSdtUser = itemView.findViewById(R.id.tv_sdt_user);
            tvAddressUser = itemView.findViewById(R.id.tv_address_user);
            tvNgayTao = itemView.findViewById(R.id.tv_ngay_tao);
            tvTongTien = itemView.findViewById(R.id.tv_tong_tien);
            tvStatus = itemView.findViewById(R.id.tv_status);
            lvDanhSach = itemView.findViewById(R.id.lv_danh_sach_sp);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
            lnConfirm = itemView.findViewById(R.id.ln_confirm);
        }
    }
    private void confirmOrder(String id){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.confirmOrder(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    strStatus = "Đã thanh toán";

                    Toast.makeText(context, "confirm thanh cong", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();

                } else {
                    Toast.makeText(context, "confirm that bai ", Toast.LENGTH_SHORT).show();
                    showDialogProgress();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Không kết nối được đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("OrderAdapter","POST CONFIRM"+t.getMessage());
                showDialogProgress();
            }
        });
    }
    private void cancelOrder(String id){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.cancelOrder(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    strStatus = "Đã hủy";
                    Toast.makeText(context, "cancel thanh cong", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "cancel that bai ", Toast.LENGTH_SHORT).show();
                    showDialogProgress();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Không kết nối được đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("OrderAdapter","POST CONFIRM"+t.getMessage());
                showDialogProgress();

            }
        });
    }
    private void showDialogProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
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
