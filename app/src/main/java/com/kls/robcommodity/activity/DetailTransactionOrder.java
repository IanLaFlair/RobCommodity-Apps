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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.adapter.ItemDetailTransactionAdapter;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.DetailTransactionHistoryResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;
import java.util.Map;

public class DetailTransactionOrder extends AppCompatActivity {
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
    @BindView(R.id.llDetailTs)
    public LinearLayout llDetailTs;
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

        btnTsPay.setVisibility(View.INVISIBLE); //Kalo dipake dirubah aja

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
        if (!edtCancelNote.getText().toString().equals("")){
            this.cancelPayment(detailTransactionHistoryResponse.getHistoryOrderModel().getPaymentToken(),
                    edtCancelNote.getText().toString());
        }else {
            Toast.makeText(this, "Cancel note cant be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelPayment(String token, String cancelNote) {
        showLoading(true);
        Map<String, Object> map = new HashMap<>();
        map.put("cancellation_note", cancelNote);

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
                                recreate();

                            }else {
                                showLoading(false);
                                bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                Toast.makeText(DetailTransactionOrder.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                            txtTransactionCode.setText("Transaction Code : \n" + detailTransactionHistoryResponse.getHistoryOrderModel().getOrderId());
                            txtPaymentCode.setText("Payment Code : \n"+ detailTransactionHistoryResponse.getTransactionPayment().get(0).getPaymentCode());
                            txtDetailOrderDate.setText("Order Date : \n" + detailTransactionHistoryResponse.getHistoryOrderModel().getOrderDate());
                            txtPaymentStatus.setText("Payment Status : \n" +detailTransactionHistoryResponse.getHistoryOrderModel().getPaymentStatus());
                            txtTotalDetail.setText("Total : \n" + Helper.formatToDollarCurrency(Helper.rupiahToDollar(Double.valueOf(detailTransactionHistoryResponse.getHistoryOrderModel().getGrandTotal()))));
                            if (detailTransactionHistoryResponse.getHistoryOrderModel().getStatus().equals("paid")){
                                btnCancel.setVisibility(View.VISIBLE);
                            }else {
                                btnCancel.setVisibility(View.INVISIBLE);
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
}