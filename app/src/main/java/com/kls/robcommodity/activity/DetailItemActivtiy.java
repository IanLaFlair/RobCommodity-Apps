package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.SliderAdapter;
import com.kls.robcommodity.constant.Constant;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.BuyNowModel;
import com.kls.robcommodity.model.CartItemModel;
import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.DetailItemResponse;
import com.kls.robcommodity.model.DetailSliderModel;
import com.kls.robcommodity.model.HotItemModel;
import com.kls.robcommodity.model.ThumbnailResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.Authentication;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
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

    @BindView(R.id.btn_buynow)
    Button btnBuyNow;

    @BindView(R.id.btn_cart)
    Button btnCart;

    @BindView(R.id.backButton)
    ImageView back;
    @BindView(R.id.txt_Stock)
    TextView txt_stock;
    @BindView(R.id.txt_sold)
    TextView txt_sold;
    @BindView(R.id.txt_desc)
    TextView txt_desc;

    @BindView(R.id.nbQty)
    NumberPicker nbQty;

    SweetAlertDialog pDialog;
    Integer id;
    private CartItemResponse cartItemResponse;
    private HotItemModel hotItemModel;
    private ThumbnailResponse thumbnailResponse;
    private BottomSheetBehavior bs;

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
        id = i.getIntExtra("ID",0);

        getDetailTask(id);
        setUpBottomSheet();

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

    private void setUpBottomSheet() {
        View bottom = findViewById(R.id.llbottomsheet);

        bs = BottomSheetBehavior.from(bottom);
        bs.setHideable(true);
    }

    private void getDetailTask(Integer id){
        List<DetailSliderModel> sliderItemList = new ArrayList<>();
        showLoading(true);
        NetworkHandler.getRetrofit().create(Api.class)
                .getDetail(id)
                .enqueue(new Callback<DetailItemResponse>() {
                    @Override
                    public void onResponse(Call<DetailItemResponse> call, Response<DetailItemResponse> response) {
                        showLoading(false);
                        DetailItemResponse detailItemResponse = response.body();
                        if (detailItemResponse != null){

                            setDataToSend(detailItemResponse);

                            txt_name.setText(detailItemResponse.getDetailItemModel().getTitle());
                            txt_price.setText(Helper.formatToDollarCurrency(Helper.rupiahToDollar(detailItemResponse.getDetailItemModel().getPrice())));
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

    private void setDataToSend(DetailItemResponse detailItemResponse) {
        cartItemResponse = new CartItemResponse();

        ArrayList<CartItemModel> cartItemModels = new ArrayList<>();
        CartItemModel cartItemModel = new CartItemModel();
        cartItemModel.setId(id);

        hotItemModel = new HotItemModel();
        hotItemModel.setId(id);
        hotItemModel.setPrice(detailItemResponse.getDetailItemModel().getPrice());
        hotItemModel.setTitle(detailItemResponse.getDetailItemModel().getTitle());

        thumbnailResponse = new ThumbnailResponse();
        thumbnailResponse.setImage(detailItemResponse.getDetailItemModel().getThumbnailResponses().get(0).getImage());

        hotItemModel.setThumbnailResponse(thumbnailResponse);

        cartItemModel.setHotItemModel(hotItemModel);

        cartItemModels.add(cartItemModel);

        cartItemResponse.setCartItemModels(cartItemModels);
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.btn_cart)
    public void addToCart() {
        this.buyAndAddCart(true);
    }

    @OnClick(R.id.btn_bs_cancel)
    public void cancel(){
        nbQty.setValue(1);
        bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.btn_bs_buy)
    public void buyBS() {
        BuyNowModel buyNowModel = new BuyNowModel();
        buyNowModel.setProductId(this.hotItemModel.getId());
        buyNowModel.setPriceProduct(this.hotItemModel.getPrice().longValue());
        buyNowModel.setProductName(this.hotItemModel.getTitle());
        buyNowModel.setImg(this.thumbnailResponse.getImage());
        buyNowModel.setQuantity(nbQty.getValue());


        Intent intent = new Intent(DetailItemActivtiy.this, CartActivity.class);
        intent.putExtra("buyNow", buyNowModel);

        bs.setState(BottomSheetBehavior.STATE_COLLAPSED);
        startActivity(intent);
    }

    @OnClick(R.id.btn_buynow)
    public void buyNow() {

        bs.setState(BottomSheetBehavior.STATE_EXPANDED);


    }

    public void buyAndAddCart(boolean isAddToCart) {
        Map<String, Integer> params = new HashMap<>();
        params.put("qty", 1);
        params.put("product_id", this.id);

        NetworkHandler.getRetrofit().create(Api.class)
                .postItemToCart(params)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        BaseResponse baseResponse = response.body();
                        if (baseResponse != null) {
                            if (baseResponse.isSuccess()){
                                if (isAddToCart){
                                    Toast.makeText(DetailItemActivtiy.this, "Success add to cart", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(DetailItemActivtiy.this, "Failed add to cart "+baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        t.printStackTrace();
                        System.out.println("THROWABLE "+t.getMessage());
                    }
                });
    }

    public void renewItems(View view) {

    }

}