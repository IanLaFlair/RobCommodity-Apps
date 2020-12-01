package com.kls.robcommodity.viewmodel;

import com.kls.robcommodity.model.CartItemResponse;
import com.kls.robcommodity.model.CategoryProductResponse;
import com.kls.robcommodity.utils.Api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryProductViewModel extends ViewModel {

    private MutableLiveData<CategoryProductResponse> categoryProductResponses = new MutableLiveData<>();

    public void setCategoryProductResponses(Integer categoryID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(Api.class)
                .getCategoryProduct(categoryID)
                .enqueue(new Callback<CategoryProductResponse>() {
                    @Override
                    public void onResponse(Call<CategoryProductResponse> call, Response<CategoryProductResponse> response) {
                        CategoryProductResponse categoryProductResponse = response.body();
                        if (categoryProductResponse != null){
                            categoryProductResponses.postValue(categoryProductResponse);
                        }else {
                            categoryProductResponses.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryProductResponse> call, Throwable t) {
                        t.printStackTrace();
                        categoryProductResponses.postValue(null);
                    }
                });
    }

    public LiveData<CategoryProductResponse> getCategoryProductResponse() {
        return categoryProductResponses;
    }

}
