package com.hoanhph29102.assignment_api.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.annotations.SerializedName;
import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.User;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    EditText edNameUser, edEmailUser, edPhoneUser, edAddressUser;
    TextInputLayout inNameUser, inEmailUser;
    TextView btnUpdate;
    User user;
    SharedPreferences pref;
    @SerializedName("Name")
    private String newName;
    @SerializedName("Email")
    private String newEmail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edNameUser = view.findViewById(R.id.ed_name_user);
        edEmailUser = view.findViewById(R.id.ed_email_user);
        edPhoneUser = view.findViewById(R.id.ed_phone_user);
        edAddressUser = view.findViewById(R.id.ed_address_user);
        inEmailUser = view.findViewById(R.id.in_email_user);
        inNameUser = view.findViewById(R.id.in_name_user);

        btnUpdate = view.findViewById(R.id.btn_update);

        pref = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email = pref.getString("email", "");
        String id = pref.getString("userID","");

        edEmailUser.setText(email);
        edNameUser.setText(name);
        Log.d("UserFragment", "UserID: "+ id);
        btnUpdate.setOnClickListener(view1 -> updateUser(id));
    }

    private void updateUser(String id){
        newName = edNameUser.getText().toString();
        newEmail = edEmailUser.getText().toString();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.updateUser(id,new User(newName,newEmail));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                    Toast.makeText(getContext(), "cap nhat thanh cong", Toast.LENGTH_SHORT).show();

                    // save lại người dùng
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name", newName);
                    editor.putString("email", newEmail);
                    editor.apply();
                }
                else {
                    Toast.makeText(getContext(), "cap nhat that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Không kết nối được với máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("UserFragment", "Error update: "+ t.getMessage());
            }
        });
    }


}
