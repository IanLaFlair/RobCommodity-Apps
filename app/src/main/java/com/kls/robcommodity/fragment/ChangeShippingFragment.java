package com.kls.robcommodity.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.adapter.ShippingAddressAdapter;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.ShippingAddressModel;
import com.kls.robcommodity.model.ShippingAddressResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.viewmodel.CartListViewModel;
import com.kls.robcommodity.viewmodel.ShippingAddressViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

public class ChangeShippingFragment extends Fragment {

    @BindView(R.id.rv_shipping)
    RecyclerView rvShipping;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.txtHelpShipment)
    TextView txtHelpShipment;

    private ShippingAddressAdapter shippingAddressAdapter;
    private ShippingAddressViewModel shippingAddressViewModel;
    private SweetAlertDialog pDialog;
    private NavController navController;

    public ChangeShippingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_change_shipping, container, false);
        ButterKnife.bind(this, view);

        this.navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_cart);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);


        this.rvShipping.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        this.shippingAddressAdapter = new ShippingAddressAdapter(getActivity(), this);
        this.rvShipping.setAdapter(shippingAddressAdapter);
        this.shippingAddressAdapter.notifyDataSetChanged();

        this.loadData();

        this.rvShipping.setHasFixedSize(true);

        return view;
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    private void loadData() {
        this.shippingAddressViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ShippingAddressViewModel.class);

        this.shippingAddressViewModel.getShippingAddressResponse().observe(getActivity(), new Observer<ShippingAddressResponse>() {
            @Override
            public void onChanged(ShippingAddressResponse shippingAddressResponse) {
                if (shippingAddressResponse != null){
                    shippingAddressAdapter.setData(shippingAddressResponse.getShippingAddressModels());
                    shippingAddressAdapter.notifyDataSetChanged();
                    checkShipping(shippingAddressResponse.getShippingAddressModels());

                    showLoading(false);
                }
            }
        });

        this.shippingAddressViewModel.setShippingAddress();
    }

    @OnClick(R.id.fab)
    public void addShipment() {
        this.navController.navigate(R.id.add_shipment);
    }

    private void checkShipping(ArrayList<ShippingAddressModel> shippingAddressModels) {

        ArrayList<Integer> selected = new ArrayList<>();

        for (ShippingAddressModel model : shippingAddressModels){
            selected.add(model.getSelected());
        }

        if (selected.contains(1)){
            txtHelpShipment.setVisibility(View.GONE);
        }else {
            txtHelpShipment.setVisibility(View.VISIBLE);
        }
    }

    public void itemOnClick(ShippingAddressModel shippingAddressModel) {
        showLoading(true);
        NetworkHandler.getRetrofit().create(Api.class)
                .getPickShipmentAddress(shippingAddressModel.getId())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        shippingAddressViewModel.setShippingAddress();
                        shippingAddressAdapter.notifyDataSetChanged();
                        showLoading(false);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        showLoading(false);
                    }
                });
    }

    public void deleteShipment(int adapterPosition) {
        showLoading(true);

        ShippingAddressModel shippingAddressModel = this.shippingAddressViewModel
                .getShippingAddressResponse()
                .getValue()
                .getShippingAddressModels()
                .get(adapterPosition);

        if (shippingAddressModel != null) {
            NetworkHandler.getRetrofit().create(Api.class)
                    .getDeleteShipment(shippingAddressModel.getId())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            BaseResponse baseResponse = response.body();

                            if (baseResponse != null){
                                if (baseResponse.isSuccess()){
                                    shippingAddressViewModel.setShippingAddress();
                                    shippingAddressAdapter.notifyDataSetChanged();
                                    showLoading(false);
                                }else {
                                    Toast.makeText(getActivity(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("Not success", baseResponse.getMessage());
                                    showLoading(false);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            t.printStackTrace();
                            showLoading(false);
                        }
                    });
        }
    }

    public void nextDetail(Integer id) {
        Bundle bundle = new Bundle();
        bundle.putInt("shipment_id", id);
        this.navController.navigate(R.id.add_shipment, bundle);
    }
}