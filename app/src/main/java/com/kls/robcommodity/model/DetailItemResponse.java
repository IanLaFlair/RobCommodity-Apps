package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class DetailItemResponse {

    @SerializedName("data")
    DetailItemModel detailItemModel;

    public DetailItemModel getDetailItemModel() {
        return detailItemModel;
    }

    public void setDetailItemModel(DetailItemModel detailItemModel) {
        this.detailItemModel = detailItemModel;
    }
}
