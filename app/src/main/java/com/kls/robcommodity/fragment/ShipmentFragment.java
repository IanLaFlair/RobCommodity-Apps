package com.kls.robcommodity.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.HomeActivity;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.constant.Constant;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.BuyNowModel;
import com.kls.robcommodity.model.CartItemModel;
import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.ChargeResponse;
import com.kls.robcommodity.model.HotItemModel;
import com.kls.robcommodity.model.ShippingAddressModel;
import com.kls.robcommodity.model.ShippingAddressResponse;
import com.kls.robcommodity.model.ThumbnailResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.kls.robcommodity.viewmodel.CartListViewModel;
import com.kls.robcommodity.viewmodel.ShippingAddressViewModel;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Authentication;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShipmentFragment extends Fragment implements TransactionFinishedCallback {

    @BindView(R.id.txt_change)
    TextView txtChangeAddress;
    @BindView(R.id.rv_checkout_item)
    RecyclerView rvCheckoutCart;
    @BindView(R.id.txt_total_cart)
    TextView txtTotalCart;
    @BindView(R.id.btn_buy_cart)
    Button btnPayCheckout;
    @BindView(R.id.txt_recipient_name)
    TextView txtRecipientName;
    @BindView(R.id.txt_shipment_address)
    TextView txtShipmentAddress;

    private CartListAdapter cartListAdapter;
    private CartListViewModel cartListViewModel;
    private ShippingAddressViewModel shippingAddressViewModel;
    private SweetAlertDialog pDialog;
    private CountDownTimer countDownTimer;
    private ArrayList<Map<String, Object>> noteList = new ArrayList<>();
    private ArrayList<ShippingAddressModel> shippingAddressModels = new ArrayList<>();
    private NavController navController;

    private CartItemResponse cartItemResponse;

    public ShipmentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_shipment, container, false);
        ButterKnife.bind(this, view);
        SharedPreferenceManager.getInstance().setSharedPreferences(getActivity().getSharedPreferences(SharedPreferenceManager.NAME, Context.MODE_PRIVATE));


        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        this.navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_cart);

        this.rvCheckoutCart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        this.cartListAdapter = new CartListAdapter(getActivity(), this);
        this.rvCheckoutCart.setAdapter(cartListAdapter);
        this.cartListAdapter.notifyDataSetChanged();

        if (getArguments().getParcelable("bundle") != null){
            getData();
        }else {
            loadData();
        }

        loadShippingAddress();


        initPayment(getArguments().getParcelable("bundle") != null);

        this.rvCheckoutCart.setHasFixedSize(false);

        return view;
    }

    private void loadShippingAddress() {
        this.shippingAddressViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ShippingAddressViewModel.class);

        this.shippingAddressViewModel.getShippingAddressResponse().observe(getActivity(), new Observer<ShippingAddressResponse>() {
            @Override
            public void onChanged(ShippingAddressResponse shippingAddressResponse) {
                if (shippingAddressResponse != null){
                    shippingAddressModels = shippingAddressResponse.getShippingAddressModels();
                    setAddress(shippingAddressModels);
                    showLoading(false);
                }
            }
        });

        this.shippingAddressViewModel.setShippingAddress();
    }

    private void getData() {
        BuyNowModel buyNowModel = getArguments().getParcelable("bundle");
        this.cartItemResponse = new CartItemResponse();

        HotItemModel hotItemModel = new HotItemModel();
        hotItemModel.setTitle(buyNowModel.getProductName());
        hotItemModel.setPrice(buyNowModel.getPriceProduct().doubleValue());
        hotItemModel.setId(buyNowModel.getProductId());

        ThumbnailResponse thumbnailResponse = new ThumbnailResponse();
        thumbnailResponse.setImage(buyNowModel.getImg());

        hotItemModel.setThumbnailResponse(thumbnailResponse);

        CartItemModel cartItemModel = new CartItemModel();
        cartItemModel.setHotItemModel(hotItemModel);
        cartItemModel.setQuantity(buyNowModel.getQuantity());

        ArrayList<CartItemModel> cartItemModels = new ArrayList<>();
        cartItemModels.add(cartItemModel);

        this.cartItemResponse.setCartItemModels(cartItemModels);

        cartListAdapter.setData(cartItemResponse.getCartItemModels());
        cartListAdapter.notifyDataSetChanged();
        grandTotal();
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    private void loadData() {
        cartListViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CartListViewModel.class);

        cartListViewModel.getCartItemResponse().observe(getActivity(), new Observer<CartItemResponse>() {
            @Override
            public void onChanged(CartItemResponse cartItemResponse) {
                if (cartItemResponse != null) {
                    cartListAdapter.setData(cartItemResponse.getCartItemModels());
                    cartListAdapter.notifyDataSetChanged();
                    grandTotal();
                }
            }

        });
        cartListViewModel.setCartItemData();
    }

    private void setAddress(ArrayList<ShippingAddressModel> shippingAddressModels) {

        ArrayList<Integer> selected = new ArrayList<>();

        for (ShippingAddressModel shippingAddressModel : shippingAddressModels){

            selected.add(shippingAddressModel.getSelected());


            if (shippingAddressModel.getSelected() == 1){
                StringBuilder sb = new StringBuilder();
                sb.append(shippingAddressModel.getPhone())
                        .append("\n")
                        .append(shippingAddressModel.getAddress1())
                        .append("\n");

                if (shippingAddressModel.getAddress2() != null && !shippingAddressModel.getAddress2().equals("")){
                    sb.append(shippingAddressModel.getAddress2())
                            .append("\n");
                }

                sb.append(shippingAddressModel.getCity())
                        .append(" - ")
                        .append(shippingAddressModel.getState())
                        .append("\n")
                        .append(shippingAddressModel.getCountry())
                        .append(" - ")
                        .append(shippingAddressModel.getPostalCode());

                txtRecipientName.setText(shippingAddressModel.getRecipientName());
                txtShipmentAddress.setText(sb.toString());
            }
        }

        if (!selected.contains(1)){
            txtShipmentAddress.setText("Please select one address");
        }
    }

    @OnClick(R.id.btn_buy_cart)
    public void pay(){
        showLoading(true);
        String token = SharedPreferenceManager.get(SharedPreferenceKey.TOKEN, String.class);
        if (this.cartItemResponse == null){
            for (int i = 0; i < this.noteList.size(); i++){
                Map<String, Object> data = this.noteList.get(i);
                Integer itemId = (Integer) data.get("item_id");
                String note = (String) data.get("note");

                System.out.println("POSITION : "+ i + ", ITEM_ID : "+ itemId +", NOTE : "+note);

                NetworkHandler.getRetrofit().create(Api.class)
                        .postNoteOrder(itemId, note)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                System.out.println("SET NOTE : " + response.body().isSuccess());
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }

            NetworkHandler.getRetrofit().create(Api.class)
                    .postCharge("midtrans", token)
                    .enqueue(new Callback<ChargeResponse>() {
                        @Override
                        public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                            if (response.body() != null){
                                MidtransSDK.getInstance().setTransactionRequest(transactionRequest());

                                MidtransSDK.getInstance().startPaymentUiFlow(getActivity(), PaymentMethod.CREDIT_CARD, response.body().getToken());
                            }
                        }

                        @Override
                        public void onFailure(Call<ChargeResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

        }else {
            NetworkHandler.getRetrofit().create(Api.class)
                    .postChargeNow("midtrans", token,
                            this.cartItemResponse.getCartItemModels().get(0).getHotItemModel().getId(),
                            this.cartItemResponse.getCartItemModels().get(0).getQuantity())
                    .enqueue(new Callback<ChargeResponse>() {
                        @Override
                        public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                            if (response.body() != null){
                                MidtransSDK.getInstance().setTransactionRequest(transactionRequest());

                                MidtransSDK.getInstance().startPaymentUiFlow(getActivity(), PaymentMethod.CREDIT_CARD, response.body().getToken());
                            }
                        }

                        @Override
                        public void onFailure(Call<ChargeResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }



        showLoading(false);
    }

    private TransactionRequest transactionRequest() {
        TransactionRequest request = new TransactionRequest("ORDER-"+String.valueOf(System.currentTimeMillis()), 1000);

        return request;
    }

    private void initPayment(boolean bundle) {
//        String baseUrl;
//        if (bundle){
//            baseUrl = Constant.MERCHAN_BASE_URL_MIDTRANS_BUY_NOW + "wqMpKxroIwT4RvcxldXZGluiwTR6vN/now/" //token ntar ganti diambil dari sharedpreferences
//                    + this.cartItemResponse.getCartItemModels().get(0).getHotItemModel().getId()
//                    +"/" +this.cartItemResponse.getCartItemModels().get(0).getQuantity()+"/";
//        }else {
//            baseUrl = Constant.MERCHANT_BASE_URL_MIDTRANS +"wqMpKxroIwT4RvcxldXZGluiwTR6vN/";
//        }


        SdkUIFlowBuilder.init()
                .setContext(getActivity())
                .setMerchantBaseUrl(Api.BASE_URL)
                .setClientKey(Constant.CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme(
                        "#DF0000",
                        "#D00303",
                        "#03DAC5"
                ))
                .buildSDK();
    }

    @OnClick(R.id.txt_change)
    public void changeAddress(){
        this.navController.navigate(R.id.change_shipment);
    }

    public void addNote(int itemID, String note) {
        if (cartItemResponse != null){
            cartItemResponse.getCartItemModels().get(0).setNote(note);
        }else {
            if (!noteList.isEmpty()){
                for (int i = 0; i < noteList.size(); i++){
                    Map<String, Object> map = noteList.get(i);
                    Integer id = (Integer) map.get("item_id");
                    if (itemID == id){

                        map.put("note", note);
                        noteList.set(i, map);

                    }else {
                        if (i != noteList.size()-1){
                            continue;
                        }else {
                            Map<String, Object> data = new HashMap<>();
                            data.put("item_id", itemID);
                            data.put("note", note);

                            noteList.add(data);
                        }
                    }
                }
            }else {
                noteList = new ArrayList<>();
                Map<String, Object> data = new HashMap<>();
                data.put("item_id", itemID);
                data.put("note", note);

                noteList.add(data);
            }
        }

    }

    private void grandTotal() {
        String total;
        if (cartListViewModel != null){
            total = Helper.formatToDollarCurrency(Helper.rupiahToDollar(this.cartListViewModel.getCartItemResponse().getValue().getTotal().doubleValue()));
        }else {
            HotItemModel hotItemModel = this.cartItemResponse.getCartItemModels().get(0).getHotItemModel();
            total = Helper.formatToDollarCurrency(Helper.rupiahToDollar(hotItemModel.getPrice().doubleValue() * this.cartItemResponse.getCartItemModels().get(0).getQuantity()));
        }
//        String total = Helper.formatToDollarCurrency(new BigDecimal(20000));
        this.txtTotalCart.setText(total);
    }



    @Override
    public void onTransactionFinished(TransactionResult result) {
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(getActivity(), "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();

                    execCompletedPayment(result);
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(getActivity(), "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();

                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(getActivity(), "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();

                    cancelPayment(MidtransSDK.getInstance().getTransaction().getToken());
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(getActivity(), "Transaction Failed", Toast.LENGTH_LONG).show();

            cancelPayment(MidtransSDK.getInstance().getTransaction().getToken());
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(getActivity(), "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }else{
                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    private void cancelPayment(String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("cancellation_note", "");

        NetworkHandler.getRetrofit().create(Api.class)
                .postCancelTransaction(token, map)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (response.body().isSuccess()){
                                Toast.makeText(getActivity(), "Transaction Cancel", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void execCompletedPayment(TransactionResult result) {
        showLoading(true);
        TransactionResponse response = result.getResponse();

        System.out.println();

        Api api = NetworkHandler.getRetrofit().create(Api.class);

        Call<BaseResponse> callResponse;

        if (this.cartItemResponse != null){
            callResponse = api.getSuccessMidtransResponse(
                    response.getOrderId(), response.getStatusCode(), response.getTransactionStatus(),
                    this.cartItemResponse.getCartItemModels().get(0).getQuantity(), this.cartItemResponse.getCartItemModels().get(0).getNote(),
                    this.cartItemResponse.getCartItemModels().get(0).getHotItemModel().getId());
        }else {
            callResponse = api.getSuccessMidtransResponse(response.getOrderId(), response.getStatusCode(), response.getTransactionStatus());
        }

        callResponse.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null){
                    if (response.body().isSuccess()){
                        showLoading(false);
                        getActivity().finish();
                    }else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                }else {
                    Toast.makeText(getActivity(), "Response Null", Toast.LENGTH_SHORT).show();
                    showLoading(false);
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