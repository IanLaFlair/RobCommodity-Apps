package com.kls.robcommodity.viewmodel;

import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.ShippingAddressResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingAddressViewModel extends ViewModel {

    private MutableLiveData<ShippingAddressResponse> shippingAddressResponses = new MutableLiveData<>();

    public void setShippingAddress() {
        NetworkHandler.getRetrofit().create(Api.class)
                .getShippingAddress()
                .enqueue(new Callback<ShippingAddressResponse>() {
                    @Override
                    public void onResponse(Call<ShippingAddressResponse> call, Response<ShippingAddressResponse> response) {
                        ShippingAddressResponse shippingAddressResponse = response.body();
                        if (shippingAddressResponse != null){
                            if (shippingAddressResponse.isSuccess()){
                                shippingAddressResponses.postValue(shippingAddressResponse);
                            }
                        }else {
                            shippingAddressResponses.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ShippingAddressResponse> call, Throwable t) {
                        shippingAddressResponses.postValue(null);
                    }
                });

    }

    public LiveData<ShippingAddressResponse> getShippingAddressResponse() {
        return shippingAddressResponses;
    }

}
