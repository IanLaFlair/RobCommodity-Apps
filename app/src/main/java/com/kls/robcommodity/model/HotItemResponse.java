package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HotItemResponse {

    @SerializedName("data")
    private ArrayList<HotItemModel> hotItemModels;

    public ArrayList<HotItemModel> getHotItemModels() {
        return hotItemModels;
    }

    public void setHotItemModels(ArrayList<HotItemModel> hotItemModels) {
        this.hotItemModels = hotItemModels;
    }
}
