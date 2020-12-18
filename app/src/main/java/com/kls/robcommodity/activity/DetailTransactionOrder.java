package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.adapter.ItemDetailTransactionAdapter;
import com.kls.robcommodity.constant.Constant;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.ChargeResponse;
import com.kls.robcommodity.model.DetailTransactionHistoryResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailTransactionOrder extends AppCompatActivity implements TransactionFinishedCallback {
    @BindView(R.id.rvItemOrder)
    public RecyclerView rvItemOrder;
    @BindView(R.id.txtTsCode)
    public TextView txtTransactionCode;
    @BindView(R.id.txtPaymentCode)
    public TextView txtPaymentCode;
    @BindView(R.id.txtDetailOrderDate)
    public TextView txtDetailOrderDate;
    @BindView(R.id.txtPaymentStatus)
    public TextView txtPaymentStatus;
    @BindView(R.id.txtTotalTsDetail)
    public TextView txtTotalDetail;
    @BindView(R.id.txtReceiptNumber)
    public TextView txtReceiptNumber;
    @BindView(R.id.txtTsStatus)
    public TextView txtTsStatus;
    @BindView(R.id.llDetailTs)
    public RelativeLayout llDetailTs;
    @BindView(R.id.btnTsPay)
    public Button btnTsPay;
    @BindView(R.id.btnDetailTsCancel)
    public Button btnCancel;
    @BindView(R.id.edtCancelNote)
    public EditText edtCancelNote;

    private BottomSheetBehavior bs;
    private DetailTransactionHistoryResponse detailTransactionHistoryResponse;
    private ItemDetailTransactionAdapter itemDetailTransactionAdapter;
    private SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction_order);
        ButterKnife.bind(this);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        initPayment();

        rvItemOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        itemDetailTransactionAdapter = new ItemDetailTransactionAdapter(this);
        rvItemOrder.setAdapter(itemDetailTransactionAdapter);
        itemDetailTransactionAdapter.notifyDataSetChanged();
        if (getIntent().getIntExtra("transaction_id", -1) != -1){
            loadData(getIntent().getIntExtra("transaction_id", -1));
        }else {
            showLoading(false);
            Toast.makeText(this, "ID NULL", Toast.LENGTH_SHORT).show();
        }
        setUpBottomSheet();
        rvItemOrder.setHasFixedSize(true);
    }

    private void initPayment() {
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(Api.BASE_URL)
                .setClientKey(Constant.CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
//                .setColorTheme(new CustomColorTheme(
//                        "#DF0000",
//                        "#D00303",
//                        "#03DAC5"
//                ))
                .buildSDK();
    }

    private void setUpBottomSheet() {
        View bottom = findViewById(R.id.llbottomsheet);

        bs = BottomSheetBehavior.from(bottom);
        bs.setHideable(true);
    }

    @OnClick(R.id.btnDetailTsCancel)
    public void showBS() {
        bs.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.btnCancelSend)
    public void closeBS() {
        bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick({R.id.btnSendCancel})
    public void cancelTransaction() {
        if (!edtCancelNote.getText().toString().isEmpty()){
            this.cancelPayment(detailTransactionHistoryResponse.getHistoryOrderModel().getPaymentToken(),
                    edtCancelNote.getText().toString(), false);
        }else {
            Toast.makeText(this, "Cancel note cant be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnTsPay)
    public void pay() {
        String token = SharedPreferenceManager.get(SharedPreferenceKey.TOKEN, String.class);
        if (token != null) {

            UIKitCustomSetting setting = MidtransSDK.getInstance().getUIKitCustomSetting();
            setting.setSkipCustomerDetailsPages(true);
            MidtransSDK.getInstance().setUIKitCustomSetting(setting);

            MidtransSDK.getInstance().setTransactionRequest(transactionRequest());
            System.out.println("NEW TOKEN = "+ detailTransactionHistoryResponse.getHistoryOrderModel().getPaymentToken());
            MidtransSDK.getInstance().startPaymentUiFlow(DetailTransactionOrder.this, detailTransactionHistoryResponse.getHistoryOrderModel().getPaymentToken());
//            NetworkHandler.getRetrofit().create(Api.class)
//                    .postCharge("midtrans", token)
//                    .enqueue(new Callback<ChargeResponse>() {
//                        @Override
//                        public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
//                            ChargeResponse chargeResponse = response.body();
//                            if (chargeResponse != null) {
//                                if (!chargeResponse.isSuccess() && chargeResponse.getStatus() == 200) {
////
//
//
//                                } else {
//                                    Toast.makeText(DetailTransactionOrder.this, chargeResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                try {
//                                    System.out.println(response.errorBody().string() + "\n token" + MidtransSDK.getInstance().getTransaction().getToken());
//                                    Toast.makeText(DetailTransactionOrder.this, "Please complete transaction first, check in your transaction", Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ChargeResponse> call, Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
        }
    }

    private TransactionRequest transactionRequest() {
        TransactionRequest request = new TransactionRequest("ORDER-"+String.valueOf(System.currentTimeMillis()), 1000);

        request.setCustomerDetails(customerDetails());
        
        return request;
    }

    private CustomerDetails customerDetails() {
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName("NAMAMU");
        cd.setLastName("YourName");
        cd.setEmail("email@gmail.com");
        cd.setPhone("Nope");

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("ssss");
        shippingAddress.setCity("dada");
        shippingAddress.setCountryCode("2432");
        shippingAddress.setFirstName("jl");
        shippingAddress.setLastName("kot");
        shippingAddress.setPostalCode("12313");
        shippingAddress.setPhone("328498");

        cd.setShippingAddress(shippingAddress);
        return cd;
    }

    private void cancelPayment(String token, String cancelNote, boolean fromPayment) {
        showLoading(true);
        Map<String, Object> map = new HashMap<>();
        if (fromPayment){
            map.put("cancellation_note", " ");
        }else {
            map.put("cancellation_note", cancelNote);
        }
        NetworkHandler.getRetrofit().create(Api.class)
                .postCancelTransaction(token, map)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (response.body().isSuccess()){
                                showLoading(false);
                                bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                Toast.makeText(DetailTransactionOrder.this, "Transaction Cancel", Toast.LENGTH_SHORT).show();
                                if (fromPayment){
                                    DetailTransactionOrder.this.finish();
                                }else {
                                    recreate();
                                }

                            }else {
                                showLoading(false);
                                bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                Toast.makeText(DetailTransactionOrder.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            showLoading(false);
                            try {
                                System.out.println(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
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

    private void loadData(int transaction_id) {
        NetworkHandler.getRetrofit().create(Api.class)
                .getDetailTransaction(transaction_id)
                .enqueue(new Callback<DetailTransactionHistoryResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<DetailTransactionHistoryResponse> call, Response<DetailTransactionHistoryResponse> response) {
                        detailTransactionHistoryResponse = response.body();
                        if (detailTransactionHistoryResponse != null){
                            if (!detailTransactionHistoryResponse.getTransactionItem().isEmpty()){
                                itemDetailTransactionAdapter.setTransactionItems(detailTransactionHistoryResponse.getTransactionItem());
                                itemDetailTransactionAdapter.notifyDataSetChanged();
                            }

                            txtTsStatus.setText("Transaction Status : \n"+ detailTransactionHistoryResponse.getHistoryOrderModel().getStatus().toUpperCase());

                            txtTransactionCode.setText("Transaction Code : \n" + detailTransactionHistoryResponse.getHistoryOrderModel().getOrderId());
                            if (!detailTransactionHistoryResponse.getTransactionPayment().isEmpty()){
                                txtPaymentCode.setText("Payment Code : \n"+ detailTransactionHistoryResponse.getTransactionPayment().get(0).getPaymentCode());
                            } else {
                                txtPaymentCode.setText("Payment Code : \n -");
                            }
                            txtDetailOrderDate.setText("Order Date : \n" + detailTransactionHistoryResponse.getHistoryOrderModel().getOrderDate());
                            txtPaymentStatus.setText("Payment Status : \n" +detailTransactionHistoryResponse.getHistoryOrderModel().getPaymentStatus());

                            if (detailTransactionHistoryResponse.getDeliveredTransactions() != null && detailTransactionHistoryResponse.getDeliveredTransactions().getReceiptNumber() != null){
                                txtReceiptNumber.setText("Receipt Number : \n" + detailTransactionHistoryResponse.getDeliveredTransactions().getReceiptNumber());
                            }else if (detailTransactionHistoryResponse.getHistoryOrderModel().getStatus().equals("cancelled")){
                                txtReceiptNumber.setText("Receipt Number : \n" + detailTransactionHistoryResponse.getHistoryOrderModel().getStatus().toUpperCase());
                            }else {
                                txtReceiptNumber.setText("Receipt Number : \nON PROGRESS");
                            }

                            txtTotalDetail.setText("Total : \n" + Helper.formatToDollarCurrency(Helper.rupiahToDollar(Double.valueOf(detailTransactionHistoryResponse.getHistoryOrderModel().getGrandTotal()))));


                            if (detailTransactionHistoryResponse.getHistoryOrderModel().getStatus().equals("created")){
                                btnTsPay.setVisibility(View.VISIBLE);
                                btnCancel.setVisibility(View.VISIBLE);
                            }else if (detailTransactionHistoryResponse.getHistoryOrderModel().getStatus().equals("paid")){
                                btnCancel.setVisibility(View.VISIBLE);
                                btnTsPay.setVisibility(View.INVISIBLE);
                            }else {
                                btnCancel.setVisibility(View.INVISIBLE);
                                btnTsPay.setVisibility(View.INVISIBLE);
                            }

                            showLoading(false);
                        }else {
                            showLoading(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailTransactionHistoryResponse> call, Throwable t) {
                        t.printStackTrace();
                        showLoading(false);
                    }
                });
    }

    private void showLoading(boolean state) {
        if (state){
            llDetailTs.setVisibility(View.INVISIBLE);
            pDialog.show();
        }else {
            llDetailTs.setVisibility(View.VISIBLE);
            pDialog.dismiss();
        }
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();

                    execCompletedPayment(result);
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();

//                    cancelPayment(MidtransSDK.getInstance().getTransaction().getToken());
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();

//                    cancelPayment(MidtransSDK.getInstance().getTransaction().getToken());
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();

            cancelPayment(MidtransSDK.getInstance().getTransaction().getToken(), "", true);
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
//                cancelPayment(MidtransSDK.getInstance().getTransaction().getToken());
//                getActivity().finish();
            }else{
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
//                cancelPayment(MidtransSDK.getInstance().getTransaction().getToken());
//                getActivity().finish();
            }
        }
    }

    private void execCompletedPayment(TransactionResult result) {
        showLoading(true);
        TransactionResponse response = result.getResponse();

        System.out.println();

        Api api = NetworkHandler.getRetrofit().create(Api.class);

        Call<BaseResponse> callResponse =  api.getSuccessMidtransResponse(response.getOrderId(), response.getStatusCode(), response.getTransactionStatus());;

//        if (this.cartItemResponse != null){
//            callResponse = api.getSuccessMidtransResponse(
//                    response.getOrderId(), response.getStatusCode(), response.getTransactionStatus(),
//                    this.cartItemResponse.getCartItemModels().get(0).getQuantity(), this.cartItemResponse.getCartItemModels().get(0).getNote(),
//                    this.cartItemResponse.getCartItemModels().get(0).getHotItemModel().getId());
//        }else {
//        }


        callResponse.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null){
                    if (response.body().isSuccess()){
                        showLoading(false);
                        finish();
                    }else {
                        Toast.makeText(DetailTransactionOrder.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                }else {
                    Toast.makeText(DetailTransactionOrder.this, "Response Null", Toast.LENGTH_SHORT).show();
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