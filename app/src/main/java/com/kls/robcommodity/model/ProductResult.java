package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductResult extends BaseResponse {

    @SerializedName("current_page")
    int currentPage;
    @SerializedName("data")
    ArrayList<Product> products;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
