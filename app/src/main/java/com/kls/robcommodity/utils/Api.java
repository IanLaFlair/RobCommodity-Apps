package com.kls.robcommodity.utils;

import com.google.gson.annotations.SerializedName;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.Categories;
import com.kls.robcommodity.model.CategoryProductResponse;
import com.kls.robcommodity.model.CategoryResponse;
import com.kls.robcommodity.model.ChargeResponse;
import com.kls.robcommodity.model.DetailItemResponse;
import com.kls.robcommodity.model.DetailTransactionHistoryResponse;
import com.kls.robcommodity.model.ExchangeRateResponse;
import com.kls.robcommodity.model.HistoryOrderResponse;
import com.kls.robcommodity.model.HotItemResponse;
import com.kls.robcommodity.model.LoginResponse;
import com.kls.robcommodity.model.ProductResult;
import com.kls.robcommodity.model.RegisterResponse;
import com.kls.robcommodity.model.ShippingAddressDetail;
import com.kls.robcommodity.model.ShippingAddressModel;
import com.kls.robcommodity.model.ShippingAddressResponse;
import com.kls.robcommodity.model.UserResponse;

import java.io.File;
import java.util.Map;

import androidx.annotation.Nullable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://robcommodity.com/api/";

    @POST("register")
    @FormUrlEncoded
    Call<RegisterResponse> postRegister(@FieldMap Map<String,String> params);

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> postLogin(@FieldMap Map<String, String> params);

    @GET("products/all/hot  ")
    Call<HotItemResponse> getHotItem();

    @GET("categories")
    Call<CategoryResponse> getCategories();

    @GET("categories/{category_id}/products")
    Call<CategoryProductResponse> getCategoryProduct(@Path("category_id") Integer categoryID);

    @GET("products/{id}")
    Call<DetailItemResponse> getDetail(@Path("id") Integer  id);

    @POST("cart/create")
    @FormUrlEncoded
    Call<BaseResponse> postItemToCart(@FieldMap Map<String, Integer> params);

    @GET("cart")
    Call<CartItemResponse> getListCart();

    @GET("cart/{cart_id}/delete")
    Call<BaseResponse> deleteCart(@Path("cart_id") Integer cartId);

    @POST("cart/qty/set/{cart_id}")
    Call<BaseResponse> postSetQuantity(@Path("cart_id") Integer cartId, @Query("qty") Integer quantity);

    @GET("shipping/address/user")
    Call<ShippingAddressResponse> getShippingAddress();

    @GET("shipping/address/{id}/pick")
    Call<BaseResponse> getPickShipmentAddress(@Path("id") Integer shipmentId);

    @POST("shipping/address/create")
    Call<BaseResponse> postShipmentAddress(@Body ShippingAddressModel shippingAddressModel);

    @POST("shipping/address/{shipment_id}/update")
    Call<BaseResponse> updateShipmentAddress(@Path("shipment_id") Integer shipmentID ,@Body ShippingAddressModel shippingAddressModel);

    @POST("cart/note/set/{cart_id}")
    Call<BaseResponse> postNoteOrder(@Path("cart_id") Integer cartId, @Query("note") String note);

    @GET("payment/completed/midtrans")
    Call<BaseResponse> getSuccessMidtransResponse(
            @Query("order_id") String orderID,
            @Query("status_code") String statusCode,
            @Query("transaction_status") String transactionStatus
    );

    @GET("payment/completed/now/midtrans")
    Call<BaseResponse> getSuccessMidtransResponse(
            @Query("order_id") String orderID,
            @Query("status_code") String statusCode,
            @Query("transaction_status") String transactionStatus,
            @Nullable @Query("qty") Integer quantity,
            @Nullable @Query("note") String note,
            @Nullable @Query("product_id") Integer productID
    );

    @GET("shipping/address/{shipment_id}/delete")
    Call<BaseResponse> getDeleteShipment(@Path("shipment_id") Integer shipmentID);

    @GET("shipping/address/{id}")
    Call<ShippingAddressDetail> getShippingAddressDetail(@Path("id") Integer shipmentID);

    @GET("profile/my")
    Call<UserResponse> getUser();

    @GET("transactions/completed/user")
    Call<HistoryOrderResponse> getHistoryOrder();

    @GET("user/transactions")
    Call<HistoryOrderResponse> getUserTransaction();

    @GET("transactions/{transaction_id}")
    Call<DetailTransactionHistoryResponse> getDetailTransaction(@Path("transaction_id") Integer transactionID);

    @POST("payment/cancel")
    @FormUrlEncoded
    Call<BaseResponse> postCancelTransaction(@Query("token") String token, @FieldMap Map<String, Object> cancelNote);

    @POST("payment/{payment_type}/{token}/charge")
    Call<ChargeResponse> postCharge(@Path("payment_type") String paymentType, @Path("token") String token);

    @POST("payment/{payment_type}/{token}/now/{product_id}/{qty}/charge")
    Call<ChargeResponse> postChargeNow(
            @Path("payment_type") String paymentType,
            @Path("token") String token,
            @Path("product_id") Integer productID,
            @Path("qty") Integer quantity
    );

    @GET("logout")
    Call<BaseResponse> logOut();

    @Multipart
    @POST("profile/update")
    Call<BaseResponse> updateProfile(
            @Part("username") RequestBody username,
            @Part("no_telp") RequestBody numberPhone,
            @Part MultipartBody.Part photo
    );

    @Multipart
    @POST("profile/update")
    Call<BaseResponse> updateProfile(
            @Part("username") RequestBody username,
            @Part("no_telp") RequestBody numberPhone
    );

    @POST("products/search")
    Call<ProductResult> searchProduct(@Query("keyword") String keyword);

    @GET("latest")
    Call<ExchangeRateResponse> getExchangeRate(@Query("base") String base);

    @POST("payment/paypal/{token}/charge")
    Call<BaseResponse> successPaypal(@Path("token") String token, @Query("paypalId") String paypalID);

    @POST("payment/paypal/{token}/now/{product_id}/{qty}/charge")
    Call<BaseResponse> successPaypal(
            @Path("token") String token,
            @Path("product_id") Integer productID,
            @Path("qty") Integer qty,
            @Query("paypalId") String paypalID);

    @GET("shipping/transactions/{transaction_id}/arrived")
    Call<BaseResponse> completeShipment(
      @Path("transaction_id") Integer transactionID
    );

    @POST("products/{transaction_item_id}/review")
    @FormUrlEncoded
    Call<BaseResponse> reviewProduct(@Path("transaction_item_id") Integer itemID, @FieldMap Map<String, Object> data);

    @GET("dollar/latest")
    Call<Double> getExchangeCurrency();
}
