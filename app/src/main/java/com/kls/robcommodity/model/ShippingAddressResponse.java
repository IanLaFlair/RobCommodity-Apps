package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShippingAddressResponse extends BaseResponse{

    @SerializedName("data")
    private ArrayList<ShippingAddressModel> shippingAddressModels;

    public ArrayList<ShippingAddressModel> getShippingAddressModels() {
        return shippingAddressModels;
    }

    public void setShippingAddressModels(ArrayList<ShippingAddressModel> shippingAddressModels) {
        this.shippingAddressModels = shippingAddressModels;
    }
}
