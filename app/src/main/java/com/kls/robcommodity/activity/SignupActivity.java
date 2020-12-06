package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.model.RegisterResponse;
import com.kls.robcommodity.utils.Api;
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

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edt_email_reg)
    EditText edt_emailreg;
    @BindView(R.id.edt_password_reg)
    EditText edt_password_reg;
    @BindView(R.id.edt_nama)
    EditText edt_name;
    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.txt_login)
    TextView txt_login;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerTask(edt_name.getText().toString(),edt_emailreg.getText().toString(),edt_password_reg.getText().toString());
            }
        });
    }

    private void registerTask(String name, String email, String password){
        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Map<String, String> params = new HashMap<>();
        params.put("username",name);
        params.put("email",email);
        params.put("password",password);
        Call<RegisterResponse> call = api.postRegister(params);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                pDialog.dismiss();
                if (registerResponse != null){
                    Integer status = registerResponse.getStatus();
                    String message = registerResponse.getMessage();
                    if (status == 201){
//                        try(SharedPreferenceManager sp = SharedPreferenceManager.getInstance()) {
//                            sp.begin();
//                            sp.put(SharedPreferenceKey.TOKEN, registerResponse.getToken());
//                            sp.commit();
//                        }
                        startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                    }else {
                        Toast.makeText(SignupActivity.this, "Register Gagal "+message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(SignupActivity.this, "Register Gagal "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}