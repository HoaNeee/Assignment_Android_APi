package com.hoanhph29102.assignment_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.annotations.SerializedName;
import com.hoanhph29102.assignment_api.model.User;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText edEmailRegis, edPassRegis, edRePass, edNameUser;
    TextInputLayout inEmail, inPass,inRePass, inName;
    TextView btnRegister, btnToLogin;
    @SerializedName("Email")
    String email;
    @SerializedName("Password")
    String password;
    @SerializedName("Name")
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edPassRegis = findViewById(R.id.ed_pass_dk);
        edEmailRegis = findViewById(R.id.ed_email);
        edRePass = findViewById(R.id.ed_re_pass);
        inEmail = findViewById(R.id.in_email);
        inPass = findViewById(R.id.in_pass);
        inRePass = findViewById(R.id.in_re_pass);
        edNameUser = findViewById(R.id.ed_name_user);
        inName = findViewById(R.id.in_name);

        btnRegister = findViewById(R.id.btn_register);
        btnToLogin = findViewById(R.id.btn_to_login);

        btnRegister.setOnClickListener(view -> RegisterUser());
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    private void RegisterUser() {
        email = edEmailRegis.getText().toString();
        name = edNameUser.getText().toString();
        password = edPassRegis.getText().toString();
        String rePass = edRePass.getText().toString();

        User userRegis = new User();
        userRegis.setName(name);
        userRegis.setEmail(email);
        userRegis.setPassword(password);

        if (!password.equals(rePass)){
            Toast.makeText(this, "mật khẩu xác nhận không đúng", Toast.LENGTH_SHORT).show();
            return;
        }


            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            Call<Void> call = apiService.registerUser(userRegis);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Register.this, "dang ky thanh cong", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.getString("message");
                            Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(Register.this, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                    Log.e("Register", t.getMessage());
                }
            });

    }
}