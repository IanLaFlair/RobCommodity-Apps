package com.kls.robcommodity.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.ShippingAddressDetail;
import com.kls.robcommodity.model.ShippingAddressModel;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;

public class AddShipmentFragment extends Fragment {
    @BindView(R.id.edt_recipient_name)
    EditText edtRecipientName;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_country)
    EditText edtCountry;
    @BindView(R.id.edt_address_line_1)
    EditText edtAddress1;
    @BindView(R.id.edt_address_line_2)
    EditText edtAddress2;
    @BindView(R.id.edt_city)
    EditText edtCity;
    @BindView(R.id.edt_state)
    EditText edtState;
    @BindView(R.id.edt_postal_code)
    EditText edtPostalCode;
    @BindView(R.id.btnSave)
    Button btnSave;

    private SweetAlertDialog pDialog;
    private NavController navController;

    public AddShipmentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_shipment, container, false);
        ButterKnife.bind(this, view);

        this.navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_cart);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);

        if (getArguments() != null){
            this.setData(getArguments().getInt("shipment_id"));
        }

        return view;
    }

    private void setData(Integer shipment_id) {
        showLoading(true);
        System.out.println(shipment_id);
        NetworkHandler.getRetrofit().create(Api.class)
                .getShippingAddressDetail(shipment_id)
                .enqueue(new Callback<ShippingAddressDetail>() {
                    @Override
                    public void onResponse(Call<ShippingAddressDetail> call, Response<ShippingAddressDetail> response) {
                        ShippingAddressDetail shippingAddressDetail = response.body();
                        if (shippingAddressDetail != null){
                            ShippingAddressModel shippingAddressModel = shippingAddressDetail.shippingAddressModel;

                            edtRecipientName.setText(shippingAddressModel.getRecipientName());
                            edtPhone.setText(shippingAddressModel.getPhone());
                            edtCountry.setText(shippingAddressModel.getCountry());
                            edtAddress1.setText(shippingAddressModel.getAddress1());
                            if (shippingAddressModel.getAddress2() != null){
                                edtAddress2.setText(shippingAddressModel.getAddress2());
                            }
                            edtCity.setText(shippingAddressModel.getCity());
                            edtState.setText(shippingAddressModel.getState());
                            edtPostalCode.setText(shippingAddressModel.getPostalCode());

                            showLoading(false);
                        }else {
                            showLoading(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ShippingAddressDetail> call, Throwable t) {

                    }
                });
    }

    @OnClick(R.id.btnSave)
    public void save() {
        showLoading(true);

        if(validate()){
            ShippingAddressModel shippingAddressModel = new ShippingAddressModel();
            shippingAddressModel.setRecipientName(edtRecipientName.getText().toString());
            shippingAddressModel.setPhone(edtPhone.getText().toString());
            shippingAddressModel.setCountry(edtCountry.getText().toString());
            shippingAddressModel.setAddress1(edtAddress1.getText().toString());
            shippingAddressModel.setAddress2(edtAddress2.getText().toString());

            shippingAddressModel.setCity(edtCity.getText().toString());
            shippingAddressModel.setState(edtState.getText().toString());
            shippingAddressModel.setPostalCode(edtPostalCode.getText().toString());

            Api api = NetworkHandler.getRetrofit().create(Api.class);
            Call<BaseResponse> responseCall = null;
            if (getArguments() != null){
                responseCall = api.updateShipmentAddress(getArguments().getInt("shipment_id"), shippingAddressModel);
            }else {
                responseCall = api.postShipmentAddress(shippingAddressModel);
            }

            responseCall.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()){
                        BaseResponse baseResponse = response.body();
                        if (baseResponse != null){
                            if (baseResponse.isSuccess()){
                                showLoading(false);
                                navController.popBackStack();
                            }
                        }else {
                            showLoading(false);
                            Toast.makeText(getActivity(), "Base Response null", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        try {
                            showLoading(false);
                            Toast.makeText(getActivity(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            });
        }else {
            showLoading(false);
        }
    }

    private boolean validate() {


        if (edtRecipientName.getText() == null || edtRecipientName.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Recipient name can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        if (edtPhone.getText() == null || edtPhone.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Phone number can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        if (edtCountry.getText() == null || edtCountry.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Country can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        if (edtAddress1.getText() == null || edtAddress1.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Address 1 can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        if (edtCity.getText() == null || edtCity.getText().toString().equals("")){
            Toast.makeText(getActivity(), "City can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        if (edtState.getText() == null || edtState.getText().toString().equals("")){
            Toast.makeText(getActivity(), "State can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        if (edtPostalCode.getText() == null || edtPostalCode.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Postal Code can't empty", Toast.LENGTH_SHORT).show();
            showLoading(false);

            return false;
        }

        return true;
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }
}