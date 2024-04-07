package com.hoanhph29102.assignment_api.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.TestModel;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.viewHolder>{

    Context context;
    List<TestModel> listTest;

    public TestAdapter(Context context, List<TestModel> list) {
        this.context = context;
        this.listTest = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.item_test,parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TestModel testModel = listTest.get(position);
        if (testModel == null){
            return;
        }
        holder.tvImg.setText(testModel.getTvImg());
        holder.tvName.setText(testModel.getTvName());
    }

    @Override
    public int getItemCount() {
        return listTest.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tvImg, tvName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvImg = itemView.findViewById(R.id.tv_img);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
