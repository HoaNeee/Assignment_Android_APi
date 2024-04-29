package com.hoanhph29102.assignment_api;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

public class Login extends AppCompatActivity {

    EditText edEmail, edPass;
    TextInputLayout inUser, inPass;
    TextView btnLogin, btnToRegis;
    CheckBox checkBox;
    User user;
    private AlertDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.ed_email_login);
        edPass = findViewById(R.id.ed_dn_pass);
        btnLogin = findViewById(R.id.btn_login);
        checkBox = findViewById(R.id.remember_me);
        btnToRegis = findViewById(R.id.btn_to_regis);
        inPass = findViewById(R.id.in_Pass);
        inUser = findViewById(R.id.in_email_login);

        SharedPreferences pref = getSharedPreferences("User_File", MODE_PRIVATE);
        edEmail.setText(pref.getString("Username", ""));
        edPass.setText(pref.getString("Password", ""));
        checkBox.setChecked(pref.getBoolean("Remember", false));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                LoginUser();
            }
        });
        btnToRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                startActivity(new Intent(Login.this, Register.class));
            }
        });


    }

    private void LoginUser(){
        String email = edEmail.getText().toString();
        String pass = edPass.getText().toString();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<User> call = apiService.loginUser(email, pass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    //dữ liê trả về
                    user = response.body();

                    rememberUser(email, pass, checkBox.isChecked());

                    saveUserToSharedPreferences(user);
                    startActivity(new Intent(Login.this,MainActivity.class));
                    finish();
                    Toast.makeText(Login.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();
                }
                else {
                    Toast.makeText(Login.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    dismissDialogProgress();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Login.this, "Lỗi khi đăng Nhập", Toast.LENGTH_SHORT).show();
                Log.e("Login", t.getMessage());
                showDialogProgress();
            }
        });
    }

    private void rememberUser(String u, String p, boolean status) {
        SharedPreferences pref = getSharedPreferences("User_File", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if (!status) {
            //xóa trắng dữ liệu trước đó
            edit.clear();
        } else {
            //lưu dữ liệu
            edit.putString("Username", u);
            edit.putString("Password", p);
            edit.putBoolean("Remember", status);
        }
        //lưu lại toàn bộ
        edit.commit();
    }

    private void saveUserToSharedPreferences(User user) {

        SharedPreferences pref = getSharedPreferences("User_Info", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userID", user.get_id());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("address", user.getAddress());
        editor.putString("avt", user.getImage());
        // Lưu các thông tin người dùng khác nếu cần
        editor.apply();
    }

    private void showDialogProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
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