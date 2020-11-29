package com.kls.robcommodity.viewmodel;

import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.HistoryOrderResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryOrderViewModel extends ViewModel {

    private MutableLiveData<HistoryOrderResponse> historyOrderResponses = new MutableLiveData<>();

    public void setHistoryOrderData() {
        NetworkHandler.getRetrofit().create(Api.class)
                .getHistoryOrder()
                .enqueue(new Callback<HistoryOrderResponse>() {
                    @Override
                    public void onResponse(Call<HistoryOrderResponse> call, Response<HistoryOrderResponse> response) {
                        HistoryOrderResponse historyOrderResponse = response.body();
                        if (historyOrderResponse != null){
                            historyOrderResponses.postValue(historyOrderResponse);
                        }else {
                            historyOrderResponses.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryOrderResponse> call, Throwable t) {
                        t.printStackTrace();
                        historyOrderResponses.postValue(null);
                    }
                });
    }

    public LiveData<HistoryOrderResponse> getHistoryOrderResponse() {
        return historyOrderResponses;
    }
}
