package com.hoanhph29102.assignment_api.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.annotations.SerializedName;
import com.hoanhph29102.assignment_api.R;
import com.hoanhph29102.assignment_api.model.User;
import com.hoanhph29102.assignment_api.retrofit.ApiService;
import com.hoanhph29102.assignment_api.retrofit.RealPathUtil;
import com.hoanhph29102.assignment_api.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    EditText edNameUser, edEmailUser, edPhoneUser, edAddressUser;
    TextInputLayout inNameUser, inEmailUser;
    TextView btnUpdate;
    User user1;
    SharedPreferences pref;
    ImageView imgUser;
    @SerializedName("Name")
    private String newName;
    @SerializedName("Email")
    private String newEmail;
    @SerializedName("Address")
    private String newAddress;
    @SerializedName("Phone")
    private String newPhone;
    @SerializedName("ImageUser")
    private String newImage;

    private String name,email,phone,address,image,id;
    private AlertDialog progressDialog;
    Uri mUri;

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                    //selectedImageBitmap = bitmap;
                    imgUser.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showDialogProgress();
        View view = inflater.inflate(R.layout.fragment_user, container, false);



        pref = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);

        name = pref.getString("name", "");
        email = pref.getString("email", "");
        address = pref.getString("address", "");
        phone = pref.getString("phone", "");


        image = pref.getString("avt", "");

        id = pref.getString("userID","");

       //String imagePath = "http://192.168.1.172:3001/uploads/avt-1713424897007.png";
       //Glide.with(this).load("http://192.168.1.172:3001/uploads/" + image).into(imgUser);



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
        imgUser = view.findViewById(R.id.img_user_update);
        btnUpdate = view.findViewById(R.id.btn_update);

        edEmailUser.setText(email);

        edNameUser.setText(name);
        edAddressUser.setText(address);
        edPhoneUser.setText(phone);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress();
                updateUser(id);
            }
        });
        imgUser.setOnClickListener(view1 -> openAlbum());
        Picasso.get().load(ApiService.DOMAIN + "uploads/" + image).into(imgUser, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Log.d("UserFragment", "image: " + image);
                dismissDialogProgress();
            }

            @Override
            public void onError(Exception e) {
                Log.d("UserFragment", "image: " + image);
                showDialogProgress();
            }
        });

    }
    private void updateUser(String userId){
        newName = edNameUser.getText().toString();
        newEmail = edEmailUser.getText().toString();
        newAddress = edAddressUser.getText().toString();
        newPhone = edPhoneUser.getText().toString();

        //Log.e("UserFragment", "Name: "+ newName );

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        if (mUri != null){

            RequestBody requestBodyName = RequestBody.create(MediaType.parse("mutilpath/from-data"), newName);
            RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("mutilpath/from-data"), newEmail);
            RequestBody requestBodyPhone = RequestBody.create(MediaType.parse("mutilpath/from-data"), newPhone+"");
            RequestBody requestBodyAddress = RequestBody.create(MediaType.parse("mutilpath/from-data"), newAddress+"");

            String strRealPath = RealPathUtil.getRealPath(getContext(),mUri);
            File file = new File(strRealPath);
            RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("mutilpath/from-data"), file);
            MultipartBody.Part mutilPathAvt = MultipartBody.Part.createFormData("avt", file.getName(),requestBodyAvt);

            Call<User> call = apiService.updateUserPro(userId,requestBodyName,requestBodyEmail,requestBodyPhone,requestBodyAddress,mutilPathAvt);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        user1 = response.body();

                        if (user1 != null) {

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("name", user1.getName());
                            editor.putString("email", user1.getEmail());
                            editor.putString("phone", user1.getPhone());
                            editor.putString("address", user1.getAddress());
                            newImage = user1.getImage();
                            editor.putString("avt", newImage);
                            Log.e("UserFragment Image", "updateUser: " + user1.getName());
                            editor.apply();
                            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                            dismissDialogProgress();
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "cap nhat that bai", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), "Không kết nối được với máy chủ", Toast.LENGTH_SHORT).show();
                    Log.e("UserFragment", "Error update: "+ t.getMessage());
                }
            });
        }
        //Call<Void> call = apiService.updateUser(id,new User(newName,newEmail,newPhone,newAddress));
        else {

            Call<User> call = apiService.updateUserProWithoutImage(userId, new User(newName,newEmail,newAddress,newPhone));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        user1 = response.body();

                        if (user1 != null) {

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("name", user1.getName());
                            editor.putString("email", user1.getEmail());
                            editor.putString("phone", user1.getPhone());
                            editor.putString("address", user1.getAddress());
                            Log.e("UserFragment", "onResponse successful: "+newName);
                            editor.apply();
                            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                            dismissDialogProgress();
                        }
                    }else {
                        Log.e("UserFragment", "onResponse failure: "+newName);
                        Log.e("UserFragment", "onResponse failure: "+newEmail);
                        Toast.makeText(getContext(), "cap nhat thong tin user not image that bai", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), "Không kết nối được với máy chủ", Toast.LENGTH_SHORT).show();
                    Log.e("UserFragment", "Error update: "+ t.getMessage());
                }
            });
        }
    }

    private void openAlbum(){
        if (getContext() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }

    private void showDialogProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
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
