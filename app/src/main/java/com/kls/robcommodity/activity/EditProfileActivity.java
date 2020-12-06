package com.kls.robcommodity.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kls.robcommodity.R;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.UserModel;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.cvProfileImage)
    public CircleImageView cvProfileImage;
    @BindView(R.id.edtUsername)
    public EditText edtUserName;
    @BindView(R.id.edtEmail)
    public EditText edtEmail;
    @BindView(R.id.edtPhoneNumber)
    public EditText edtPhoneNumber;
    @BindView(R.id.btnSaveProfile)
    public Button btnSave;

    private SweetAlertDialog pDialog;
    private UserModel userModel;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        this.pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        this.pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        this.pDialog.setTitleText("Mohon Tunggu..");
        this.pDialog.setCancelable(false);

        this.edtEmail.setVisibility(View.GONE);

        showLoading(true);
        this.userModel = getIntent().getParcelableExtra("user");
        if (this.userModel != null){
            setData();
        }

    }

    private void setData() {
        Glide.with(this)
                .load(this.userModel.getPicture())
                .into(cvProfileImage);

        this.edtUserName.setText(this.userModel.getUsername());
//        this.edtEmail.setText(this.userModel.getEmail());
        this.edtPhoneNumber.setText(this.userModel.getNumberPhone() != null ? this.userModel.getNumberPhone() : "-");
        showLoading(false);

    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.cvProfileImage)
    public void changePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            cvProfileImage.setImageURI(imageUri);
        }

    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @OnClick(R.id.btnSaveProfile)
    public void save() {
        showLoading(true);

        MediaType contentType = MediaType.parse("multipart/form-data");

        MultipartBody.Part body = null;
        if (this.imageUri != null && !this.imageUri.getPath().isEmpty()) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                File picture = this.createTempFile(bitmap);
                RequestBody requestBody = RequestBody.create(
                        MediaType.parse("image/*"),
                        picture
                );
                body = MultipartBody.Part.createFormData("photo", picture.getName(), requestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (!this.edtUserName.getText().toString().isEmpty()){
            RequestBody userName = RequestBody.create(contentType, this.edtUserName.getText().toString());
//            RequestBody email = RequestBody.create(contentType, this.edtEmail.getText().toString());
            RequestBody phoneNumber = RequestBody.create(contentType, this.edtPhoneNumber.getText().toString());

            Api api = NetworkHandler.getRetrofit().create(Api.class);
            Call<BaseResponse> call;
            if (body != null){
                call = api.updateProfile(userName, phoneNumber, body);
            }else {
                call = api.updateProfile(userName, phoneNumber);
            }
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null){
                        if (baseResponse.isSuccess()){
                            EditProfileActivity.this.setResult(RESULT_OK);
                            EditProfileActivity.this.finish();

                        }else {
                            Toast.makeText(EditProfileActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        try {
                            System.out.println(response.errorBody().string());
                            Toast.makeText(EditProfileActivity.this, "Base Response Null", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    showLoading(false);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                    showLoading(false);
                }
            });

        }else {
            Toast.makeText(this, "Username Cant be empty", Toast.LENGTH_SHORT).show();
            showLoading(false);
        }
    }
}