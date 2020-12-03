package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kls.robcommodity.R;
import com.kls.robcommodity.model.LoginResponse;
import com.kls.robcommodity.model.RegisterResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigninActivity extends AppCompatActivity {

    @BindView(R.id.edt_email_signin)
    EditText edt_email;
    @BindView(R.id.edt_password_signin)
    EditText edt_password;
    SweetAlertDialog pDialog;
    @BindView(R.id.btn_signin_de)
    Button btn_sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //@TODO : unremark logintask terus remark startActivity
//                startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                loginTask(edt_email.getText().toString(), edt_password.getText().toString());
            }
        });
    }

    private void loginTask(String email, String password){
        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Map<String, String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        Call<LoginResponse> call = api.postLogin(params);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse != null){
                    try(SharedPreferenceManager sp = SharedPreferenceManager.getInstance()) {
                        sp.begin();
                        sp.put(SharedPreferenceKey.TOKEN, loginResponse.getToken());
                        sp.commit();
                    }
                    pDialog.dismiss();
                    startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                pDialog.dismiss();
            }
        });
    }

}