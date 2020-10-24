package com.kls.robcommodity.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kls.robcommodity.model.HotItemModel;
import com.kls.robcommodity.model.HotItemResponse;
import com.kls.robcommodity.utils.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotItemViewModel extends ViewModel {

    private MutableLiveData<ArrayList<HotItemModel>> hotItemModel = new MutableLiveData<>();

    public void setHotItemModel(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<HotItemResponse> call = api.getHotItem();
        call.enqueue(new Callback<HotItemResponse>() {
            @Override
            public void onResponse(Call<HotItemResponse> call, Response<HotItemResponse> response) {
                HotItemResponse hotItemResponse = response.body();
                if (hotItemResponse != null){
                    ArrayList<HotItemModel> resultList = hotItemResponse.getHotItemModels();
                    hotItemModel.postValue(resultList);
                }
            }

            @Override
            public void onFailure(Call<HotItemResponse> call, Throwable t) {
                hotItemModel.postValue(null);
            }
        });
    }

    public LiveData<ArrayList<HotItemModel>> getHotItemModel() {
        return hotItemModel;
    }

}
