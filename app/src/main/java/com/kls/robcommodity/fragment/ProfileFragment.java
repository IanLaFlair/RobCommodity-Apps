package com.kls.robcommodity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.CartActivity;
import com.kls.robcommodity.activity.ContacsUsActivity;
import com.kls.robcommodity.activity.EditProfileActivity;
import com.kls.robcommodity.activity.HistoryOrderActivity;
import com.kls.robcommodity.activity.SigninActivity;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.UserModel;
import com.kls.robcommodity.model.UserResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.security.PublicKey;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    @BindView(R.id.nameProfile)
    public TextView txtProfileName;
    @BindView(R.id.emailProfile)
    public TextView txtEmailProfile;
    @BindView(R.id.imgProfile)
    public CircleImageView imgProfile;
    @BindView(R.id.txtNumberPhone)
    public TextView txtNumberPhone;

    private SweetAlertDialog pDialog;
    private UserModel userModel;

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
                            userModel = userResponse.getUserModel();
                            if (userModel != null){
                                txtProfileName.setText(userModel.getUsername());
                                txtEmailProfile.setText(userModel.getEmail());
                                txtNumberPhone.setText(userModel.getNumberPhone() != null ? userModel.getNumberPhone() : " - ");

                                Glide.with(getActivity())
                                        .load(userModel.getPicture())
                                        .into(imgProfile);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                loadUserProfile();
            }
        }
    }

    @OnClick(R.id.txtEdit)
    public void editProfile() {
        Intent i = new Intent(getActivity(), EditProfileActivity.class);
        i.putExtra("user", this.userModel);
        startActivityForResult(i, 1);
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

    @OnClick(R.id.txtShareApp)
    public void shareApp() {
        //@TODO : shareApp
    }

    @OnClick(R.id.txtUpdateApp)
    public void updateApp() {
        //@TODO : updateApp
    }

    @OnClick(R.id.txtLogout)
    public void logout() {
        try(SharedPreferenceManager sp = SharedPreferenceManager.getInstance()) {
            sp.begin();
            sp.remove(SharedPreferenceKey.TOKEN);
            sp.commit();
        }
        startActivity(new Intent(getActivity(), SigninActivity.class));
        getActivity().finish();
    }
}