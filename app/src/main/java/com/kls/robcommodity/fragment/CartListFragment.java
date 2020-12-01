package com.kls.robcommodity.fragment;

import android.graphics.Color;
import android.os.Bundle;

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

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.CartItemModel;
import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.viewmodel.CartListViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.travijuu.numberpicker.library.NumberPicker;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartListFragment extends Fragment {

    @BindView(R.id.rv_cart)
    RecyclerView rvCart;

    @BindView(R.id.btn_buy_cart)
    Button btnCart;

    @BindView(R.id.txt_total_cart)
    TextView txtTotalCart;

    @BindView(R.id.pb)
    ProgressBar pb;

    @BindView(R.id.ll_total)
    LinearLayout llTotal;

    @BindView(R.id.emptyView)
    LinearLayout llEmptyView;

    public CartListViewModel cartListViewModel;
    private CartListAdapter cartListAdapter;
    public CountDownTimer countDownTimer;
    private  NavController navController;
    private SweetAlertDialog pDialog;
    public int[] isFirst = {0};

    public CartListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);

        this.navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_cart);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        this.pb.setVisibility(View.VISIBLE);
        this.llTotal.setVisibility(View.INVISIBLE);

        rvCart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cartListAdapter = new CartListAdapter(getActivity(), this);
        rvCart.setAdapter(cartListAdapter);
        cartListAdapter.notifyDataSetChanged();

        loadData();

        rvCart.setHasFixedSize(true);

        return view;
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.btn_buy_cart)
    public void buy(){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("itemModels",cartListViewModel.getCartItemResponse().getValue().getCartItemModels());
        this.navController.navigate(R.id.shipment, bundle);
    }

    private void loadData() {
            cartListViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CartListViewModel.class);

            cartListViewModel.getCartItemResponse().observe(getActivity(), new Observer<CartItemResponse>() {
                @Override
                public void onChanged(CartItemResponse cartItemResponse) {
                    if (cartItemResponse != null){
                        if (cartItemResponse.getCartItemModels() != null && !cartItemResponse.getCartItemModels().isEmpty()) {
                            cartListAdapter.setData(cartItemResponse.getCartItemModels());
                            cartListAdapter.notifyDataSetChanged();

                            grandTotal();

                            pb.setVisibility(View.GONE);
                            llTotal.setVisibility(View.VISIBLE);
                            llEmptyView.setVisibility(View.GONE);
                        }else {
                            llEmptyView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        llEmptyView.setVisibility(View.VISIBLE);
                    }
                    showLoading(false);

                }
            });

            cartListViewModel.setCartItemData();

    }


    public void delete(int adapterPosition) {
        showLoading(true);
        CartItemModel cartItemModel = this.cartListViewModel.getCartItemResponse()
                .getValue()
                .getCartItemModels()
                .get(adapterPosition);

        if (cartItemModel != null){

            NetworkHandler.getRetrofit().create(Api.class)
                    .deleteCart(cartItemModel.getId())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            BaseResponse baseResponse = response.body();

                            if (baseResponse != null){
                                if (baseResponse.isSuccess()){
                                    cartListViewModel.getCartItemResponse().getValue().getCartItemModels().remove(adapterPosition);
                                    cartListViewModel.setCartItemData();
                                    cartListAdapter.notifyDataSetChanged();
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

    public void changeQuantity(int value, int adapterPosition, NumberPicker numberPicker) {
        if (this.countDownTimer != null){
            this.countDownTimer.cancel();
        }

        pb.setVisibility(View.VISIBLE);
        llTotal.setVisibility(View.INVISIBLE);

        this.countDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                System.out.println("OnFINISH");
                CartItemModel cartItemModel = cartListViewModel.getCartItemResponse()
                        .getValue()
                        .getCartItemModels()
                        .get(adapterPosition);

                if (cartItemModel != null){
                    NetworkHandler.getRetrofit().create(Api.class)
                            .postSetQuantity(cartItemModel.getId(), value)
                            .enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    BaseResponse baseResponse = response.body();
                                    if (baseResponse != null){
                                        if (baseResponse.isSuccess()){
                                            cartListViewModel.setCartItemData();
                                            cartListAdapter.notifyDataSetChanged();
                                            numberPicker.setValue(value);
                                            System.out.println("SUCCESS");
                                            pb.setVisibility(View.GONE);
                                            llTotal.setVisibility(View.VISIBLE);
                                        }else {
                                            Toast.makeText(getActivity(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            System.out.println("NOT SUCCESS");
                                            pb.setVisibility(View.GONE);
                                            llTotal.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    t.printStackTrace();
                                    pb.setVisibility(View.GONE);
                                    llTotal.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        };

        this.countDownTimer.start();
    }

    private void grandTotal() {
        String total = Helper.formatToDollarCurrency(Helper.rupiahToDollar(this.cartListViewModel.getCartItemResponse().getValue().getTotal().doubleValue()));
        this.txtTotalCart.setText(total);
    }

}