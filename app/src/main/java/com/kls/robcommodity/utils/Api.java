package com.kls.robcommodity.utils;

import com.kls.robcommodity.model.DetailItemResponse;
import com.kls.robcommodity.model.HotItemResponse;
import com.kls.robcommodity.model.LoginResponse;
import com.kls.robcommodity.model.RegisterResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://robcommodity.com/api/";

    @POST("register")
    @FormUrlEncoded
    Call<RegisterResponse> postRegister(@FieldMap Map<String,String> params);

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> postLogin(@FieldMap Map<String, String> params);

    @GET("categories/1/products")
    Call<HotItemResponse> getHotItem();

    @GET("products/{id}")
    Call<DetailItemResponse> getDetail(@Path("id") Integer  id);

}
