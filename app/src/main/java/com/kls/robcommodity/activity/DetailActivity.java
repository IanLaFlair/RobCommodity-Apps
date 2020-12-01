package com.kls.robcommodity.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.adapter.CategoryOrSearchAdapter;
import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.CategoryProductResponse;
import com.kls.robcommodity.model.DetailItemResponse;
import com.kls.robcommodity.model.ProductResult;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.viewmodel.CategoryProductViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.txtTittleCategory)
    public TextView txtTittleCategory;
    @BindView(R.id.edtSearch)
    public EditText edtSearch;
    @BindView(R.id.rvDetail)
    public RecyclerView rvDetail;
    @BindView(R.id.emptyView)
    public LinearLayout llEmptyView;
    @BindView(R.id.pb)
    public ProgressBar pb;

    private SweetAlertDialog pDialog;
    private CategoryOrSearchAdapter categoryOrSearchAdapter;
    private CategoryProductViewModel categoryProductViewModel;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);

        this.init();
        this.extract();

    }

    @OnClick(R.id.btnCart)
    public void toCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    private void init() {
        rvDetail.setLayoutManager(new GridLayoutManager(this, 2));
        categoryOrSearchAdapter = new CategoryOrSearchAdapter(this);
        rvDetail.setAdapter(categoryOrSearchAdapter);
        categoryOrSearchAdapter.notifyDataSetChanged();
    }

    private void showLoading(boolean state) {
        if (state){
            this.pDialog.show();
        }else {
            this.pDialog.dismiss();
        }
    }

    private void extract() {
        String categoryName = getIntent().getStringExtra("NAME");
        int categoryID = getIntent().getIntExtra("ID", -1);
        if (categoryName != null && categoryID != -1){
            showLoading(true);

            this.txtTittleCategory.setText(categoryName);

            this.loadDataCategory(categoryID);

            txtTittleCategory.setVisibility(View.VISIBLE);
            edtSearch.setVisibility(View.GONE);
        }else {
            txtTittleCategory.setVisibility(View.GONE);
            edtSearch.setVisibility(View.VISIBLE);

            this.search();
        }
    }

    private void search() {
        llEmptyView.setVisibility(View.VISIBLE);

        this.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onSearch() {
        pb.setVisibility(View.VISIBLE);
        llEmptyView.setVisibility(View.GONE);
        rvDetail.setVisibility(View.GONE);

        if (this.countDownTimer != null){
            this.countDownTimer.cancel();
        }

        this.countDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                callSearch(edtSearch.getText().toString());
            }
        };

        this.countDownTimer.start();
    }

    private void callSearch(String keyword) {
        new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .searchProduct(keyword)
                .enqueue(new Callback<ProductResult>() {
                    @Override
                    public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                        ProductResult productResult = response.body();
                        if (productResult != null){
                            if (productResult.isSuccess()){
                                if (productResult.getProducts() != null && !productResult.getProducts().isEmpty()){
                                    categoryOrSearchAdapter.setProducts(productResult.getProducts());
                                    llEmptyView.setVisibility(View.GONE);
                                    rvDetail.setVisibility(View.VISIBLE);
                                }else {
                                    llEmptyView.setVisibility(View.VISIBLE);
                                }
                            }else {
                                Toast.makeText(DetailActivity.this, productResult.getMessage(), Toast.LENGTH_SHORT).show();
                                llEmptyView.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Toast.makeText(DetailActivity.this, "Product Result Null", Toast.LENGTH_SHORT).show();
                            llEmptyView.setVisibility(View.VISIBLE);
                        }

                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ProductResult> call, Throwable t) {
                        t.printStackTrace();
                        llEmptyView.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    }
                });
    }

    private void loadDataCategory(int categoryID) {
        this.categoryProductViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CategoryProductViewModel.class);

        this.categoryProductViewModel.getCategoryProductResponse().observe(this, new Observer<CategoryProductResponse>() {
            @Override
            public void onChanged(CategoryProductResponse categoryProductResponse) {
                if (categoryProductResponse != null){
                    if (categoryProductResponse.getProductResult().getProducts() != null && !categoryProductResponse.getProductResult().getProducts().isEmpty()){
                        categoryOrSearchAdapter.setProducts(categoryProductResponse.getProductResult().getProducts());
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

        this.categoryProductViewModel.setCategoryProductResponses(categoryID);
    }

}