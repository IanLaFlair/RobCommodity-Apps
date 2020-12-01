package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse extends BaseResponse {

    @SerializedName("data")
    public ArrayList<Categories> categories;

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }
}
