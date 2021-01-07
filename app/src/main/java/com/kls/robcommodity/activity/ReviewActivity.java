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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hsalf.smileyrating.SmileyRating;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.ItemDetailTransactionAdapter;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.TransactionItem;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.btnCancel)
    public Button btnCancel;
    @BindView(R.id.btnDoneReview)
    public Button btnDoneReview;
    @BindView(R.id.rvItemReview)
    public RecyclerView rvItemReview;
    @BindView(R.id.edtSubject)
    public EditText edtSubject;
    @BindView(R.id.edtComment)
    public EditText edtComment;
    @BindView(R.id.ratingBar)
    public SmileyRating ratingBar;


    private ItemDetailTransactionAdapter itemDetailTransactionAdapter;
    private SweetAlertDialog pDialog;
    private BottomSheetBehavior bs;
    private ArrayList<Map<String, Object>> reviewData = new ArrayList<>();
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        setUpBottomSheet();

        rvItemReview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        itemDetailTransactionAdapter = new ItemDetailTransactionAdapter(this);
        rvItemReview.setAdapter(itemDetailTransactionAdapter);
        itemDetailTransactionAdapter.notifyDataSetChanged();

        if (getIntent().getStringExtra("json") != null) {
            loadData(getIntent().getStringExtra("json"));
        }else {
            Toast.makeText(this, "JSON NULL", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpBottomSheet() {
        View bottom = findViewById(R.id.llbottomsheet);

        bs = BottomSheetBehavior.from(bottom);
        bs.setHideable(true);
    }

    private void loadData(String json) {
        List<TransactionItem> transactionItems = new ArrayList<>();

        Type type = new TypeToken<List<TransactionItem>>() {}.getType();
        transactionItems = new Gson().fromJson(json, type);

        if (transactionItems != null) {
            itemDetailTransactionAdapter.setTransactionItems(transactionItems);
            itemDetailTransactionAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "Transaction Item null", Toast.LENGTH_SHORT).show();
        }
        showLoading(false);
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.btnDoneReview)
    public void done() {
        if (!this.reviewData.isEmpty()){

            showLoading(true);

            for (Map<String, Object> data : this.reviewData) {

                Map<String, Object> bodyData = new HashMap<>();
                bodyData.put("subject", (String)data.get("subject"));
                bodyData.put("testimonial", (String)data.get("testimoni"));
                bodyData.put("stars", (Integer)data.get("rating"));

                NetworkHandler.getRetrofit().create(Api.class)
                        .reviewProduct((Integer) data.get("item_id"), bodyData)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse != null) {
                                    if (baseResponse.isSuccess()){
                                        System.out.println("Success " + (Integer) data.get("item_id"));
                                    }else {
                                        Toast.makeText(ReviewActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(ReviewActivity.this, "Base response null", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(ReviewActivity.this, "Failed Review " + (Integer) data.get("item_id"), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            showLoading(false);


        }
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void doReview(int item_id) {
        bs.setState(BottomSheetBehavior.STATE_EXPANDED);
        this.itemId = item_id;
        ratingBar.setRating(SmileyRating.Type.GREAT);
        System.out.println("ITEM ID " + this.itemId);

        if (!this.reviewData.isEmpty()){
            for (Map<String, Object> map : this.reviewData){
                if (item_id == (Integer) map.get("item_id")){
                    this.edtSubject.setText((String) map.get("subject"));
                    this.edtComment.setText((String) map.get("testimoni"));
                    this.ratingBar.setRating((Integer) map.get("rating"));

                    break;
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bs.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @OnClick(R.id.btnSendReview)
    public void addReview() {
        System.out.println("ADD REVIEW ITEM ID : "+ this.itemId);
        if (this.edtSubject.getText().toString().equals("") || this.edtComment.getText().toString().equals("")){
            Toast.makeText(this, "Data review cant empty", Toast.LENGTH_SHORT).show();
        }else {
            if (!reviewData.isEmpty()) {
                boolean isAvailable = false;

                for (int j = 0; j < reviewData.size(); j++) {
                    Map<String, Object> map = reviewData.get(j);
                    Integer id = (Integer) map.get("item_id");
                    if (this.itemId == id) {
                        map.put("subject", this.edtSubject.getText().toString());
                        map.put("testimoni", this.edtComment.getText().toString());
                        map.put("rating", this.ratingBar.getSelectedSmiley().getRating());

                        reviewData.set(j, map);
                        isAvailable = true;
                        break;
                    }
                }

                if (!isAvailable){
                    Map<String, Object> data = new HashMap<>();
                    data.put("item_id", this.itemId);
                    data.put("subject", this.edtSubject.getText().toString());
                    data.put("testimoni", this.edtComment.getText().toString());
                    data.put("rating", this.ratingBar.getSelectedSmiley().getRating());

                    reviewData.add(data);
                }

            } else {
                reviewData = new ArrayList<>();

                Map<String, Object> data = new HashMap<>();
                data.put("item_id", this.itemId);
                data.put("subject", this.edtSubject.getText().toString());
                data.put("testimoni", this.edtComment.getText().toString());
                data.put("rating", this.ratingBar.getSelectedSmiley().getRating());

                reviewData.add(data);
            }

            this.edtSubject.getText().clear();
            this.edtComment.getText().clear();

            for (Map<String, Object> map : this.reviewData){
                System.out.println("SIZE "+ this.reviewData.size() + "\n" + (Integer) map.get("item_id") + " " + (String) map.get("subject"));
            }

            bs.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }

    @OnClick(R.id.btnCancelSend)
    public void closeBS() {
        bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}