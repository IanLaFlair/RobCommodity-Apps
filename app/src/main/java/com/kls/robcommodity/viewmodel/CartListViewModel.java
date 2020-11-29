package com.kls.robcommodity.viewmodel;

import com.kls.robcommodity.model.CartItemModel;
import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartListViewModel extends ViewModel {

    private MutableLiveData<CartItemResponse> cartItemResponses = new MutableLiveData<>();

    public void setCartItemData() {
        NetworkHandler.getRetrofit().create(Api.class)
                .getListCart()
                .enqueue(new Callback<CartItemResponse>() {
                    @Override
                    public void onResponse(Call<CartItemResponse> call, Response<CartItemResponse> response) {
                        CartItemResponse cartItemResponse = response.body();
                        if (cartItemResponse != null) {
//                            ArrayList<CartItemModel> results = cartItemResponse.getCartItemModels();
                            cartItemResponses.postValue(cartItemResponse);
                        }else {
                            cartItemResponses.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<CartItemResponse> call, Throwable t) {
                        cartItemResponses.postValue(null);
                    }
                });
    }

    public LiveData<CartItemResponse> getCartItemResponse() {
        return cartItemResponses;
    }

}
