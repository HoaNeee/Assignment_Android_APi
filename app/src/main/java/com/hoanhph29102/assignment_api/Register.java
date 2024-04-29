package com.hoanhph29102.assignment_api;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.annotations.SerializedName;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RealPathUtil;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText edEmailRegis, edPassRegis, edRePass, edNameUser;
    TextInputLayout inEmail, inPass,inRePass, inName;
    TextView btnRegister, btnToLogin;

    ImageView imgUser;
    Uri mUri;
    private AlertDialog progressDialog;

    private static final int PICK_IMAGE_REQUEST = 1;

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK){
                Intent data = o.getData();
                if (data == null){
                    return;
                }
                Uri uri = data.getData();
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    //selectedImageBitmap = bitmap;
                    imgUser.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    });

    @SerializedName("Email")
    String email;
    @SerializedName("Password")
    String password;
    @SerializedName("Name")
    String name;
    @SerializedName("Phone")
    String phone;
    @SerializedName("Address")
    String address;


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
        imgUser = findViewById(R.id.img_user);

        btnRegister = findViewById(R.id.btn_register);
        btnToLogin = findViewById(R.id.btn_to_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                RegisterUser();
            }
        });


        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                startActivity(new Intent(Register.this, Login.class));

            }
        });

        //imgUser.setOnClickListener(view -> uploadImage());
        imgUser.setOnClickListener(view -> onClickRequestPermission());
    }

    private void RegisterUser() {
        email = edEmailRegis.getText().toString();
        name = edNameUser.getText().toString();
        password = edPassRegis.getText().toString();
        String rePass = edRePass.getText().toString();

        String strRealPath = RealPathUtil.getRealPath(this,mUri);
        Log.e("Register", "strRealPath: "+ strRealPath );
        File file = new File(strRealPath);
        Log.e("Register", "File: "+ file );
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("mutilpath/from-data"), name);
        RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("mutilpath/from-data"), email);
        RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("mutilpath/from-data"), password);
        RequestBody requestBodyPhone = RequestBody.create(MediaType.parse("mutilpath/from-data"), "Vui lòng cập nhật");
        RequestBody requestBodyAddress = RequestBody.create(MediaType.parse("mutilpath/from-data"), "Vui lòng cập nhật");
        RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("mutilpath/from-data"), file);
        MultipartBody.Part mutilPathAvt = MultipartBody.Part.createFormData("avt", file.getName(),requestBodyAvt);


        if (!password.equals(rePass)){
            Toast.makeText(this, "mật khẩu xác nhận không đúng", Toast.LENGTH_SHORT).show();
            dismissDialogProgress();
            return;
        } else if (edNameUser.getText().toString().isEmpty() || edNameUser.getText().toString().equals("") || edEmailRegis.getText().toString().isEmpty() || edEmailRegis.getText().toString().equals("") ||
            edRePass.getText().toString().isEmpty() || edRePass.getText().toString().equals("") || edPassRegis.getText().toString().isEmpty() || edPassRegis.getText().toString().equals("")
        ){
            Toast.makeText(this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            dismissDialogProgress();
            return;
        }


            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            Call<Void> call = apiService.registerUser(requestBodyName,requestBodyEmail,requestBodyPassword,requestBodyPhone,requestBodyAddress,mutilPathAvt);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Register.this, "dang ky thanh cong", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        dismissDialogProgress();
                    } else {
                        try {

                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.getString("message");
                            Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        showDialogProgress();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(Register.this, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                    Log.e("Register", t.getMessage());
                    showDialogProgress();
                }
            });
    }
    private void onClickRequestPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openAlbum();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openAlbum();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }
        }

    }

    private void openAlbum(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        mLauncher.launch(Intent.createChooser(intent,"Select Picture"));
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