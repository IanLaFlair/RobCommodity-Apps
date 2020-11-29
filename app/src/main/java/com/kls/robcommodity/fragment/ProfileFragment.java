package com.kls.robcommodity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.CartActivity;
import com.kls.robcommodity.activity.ContacsUsActivity;
import com.kls.robcommodity.activity.HistoryOrderActivity;
import com.kls.robcommodity.model.UserModel;
import com.kls.robcommodity.model.UserResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.security.PublicKey;

public class ProfileFragment extends Fragment {
    @BindView(R.id.nameProfile)
    public TextView txtProfileName;
    @BindView(R.id.emailProfile)
    public TextView txtEmailProfile;

    private SweetAlertDialog pDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        this.pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        this.pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        this.pDialog.setTitleText("Mohon Tunggu..");
        this.pDialog.setCancelable(true);

        this.loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        showLoading(true);

        NetworkHandler.getRetrofit().create(Api.class)
                .getUser()
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse userResponse = response.body();

                        if (userResponse != null){
                            UserModel userModel = userResponse.getUserModel();
                            if (userModel != null){
                                txtProfileName.setText(userModel.getUsername());
                                txtEmailProfile.setText(userModel.getEmail());
                                showLoading(false);
                            }
                        }else {
                            Toast.makeText(getActivity(), "Response Null", Toast.LENGTH_SHORT).show();
                            showLoading(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        t.printStackTrace();
                        showLoading(false);
                    }
                });
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.txtHistoryOrder)
    public void historyOrder(){
        startActivity(new Intent(getActivity(), HistoryOrderActivity.class));
    }

    @OnClick(R.id.txtCart)
    public void cart(){
        startActivity(new Intent(getActivity(), CartActivity.class));
    }

    @OnClick(R.id.txtVisitWebsite)
    public void website(){
        String url = "http://robcommodity.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.txtContactUs)
    public void contactUs(){
        startActivity(new Intent(getActivity(), ContacsUsActivity.class));
    }
}