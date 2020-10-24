package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.SliderAdapter;
import com.kls.robcommodity.model.DetailItemResponse;
import com.kls.robcommodity.model.DetailSliderModel;
import com.kls.robcommodity.model.ThumbnailResponse;
import com.kls.robcommodity.utils.Api;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailItemActivtiy extends AppCompatActivity {

    @BindView(R.id.imageSlider)
    SliderView sliderView;
    private SliderAdapter adapter;
    @BindView(R.id.textView5)
    TextView txt_name;
    @BindView(R.id.txt_price)
    TextView txt_price;

    @BindView(R.id.backButton)
    ImageView back;
    @BindView(R.id.txt_Stock)
    TextView txt_stock;
    @BindView(R.id.txt_sold)
    TextView txt_sold;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item_activtiy);
        ButterKnife.bind(this);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent i = getIntent();
        Integer id = i.getIntExtra("ID",0);
        getDetailTask(id);
        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        renewItems(sliderView);
    }

    private void getDetailTask(Integer id){
        List<DetailSliderModel> sliderItemList = new ArrayList<>();
        showLoading(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<DetailItemResponse> call = api.getDetail(id);
        call.enqueue(new Callback<DetailItemResponse>() {
            @Override
            public void onResponse(Call<DetailItemResponse> call, Response<DetailItemResponse> response) {
                showLoading(false);
                DetailItemResponse detailItemResponse = response.body();
                if (detailItemResponse != null){
                    txt_name.setText(detailItemResponse.getDetailItemModel().getTitle());
                    txt_price.setText("$ "+detailItemResponse.getDetailItemModel().getPrice());
                    txt_stock.setText(String.valueOf(detailItemResponse.getDetailItemModel().getStock()));
                    txt_sold.setText(String.valueOf(detailItemResponse.getDetailItemModel().getSold()));
                    txt_desc.setText(String.valueOf(detailItemResponse.getDetailItemModel().getDescription()));
                    ArrayList<ThumbnailResponse> thumbnailResponses = detailItemResponse.getDetailItemModel().getThumbnailResponses();

                    //dummy data
                    for (int i = 0; i < thumbnailResponses.size(); i++) {
                        DetailSliderModel sliderItem = new DetailSliderModel();
                        String url = detailItemResponse.getDetailItemModel().getThumbnailResponses().get(i).getImage();

                        sliderItem.setImageUrl(url);
                        sliderItemList.add(sliderItem);
                    }
                    adapter.renewItems(sliderItemList);
                }
            }

            @Override
            public void onFailure(Call<DetailItemResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(DetailItemActivtiy.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void renewItems(View view) {

    }

    private void showLoading(Boolean state) {
        if (state) {
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

}