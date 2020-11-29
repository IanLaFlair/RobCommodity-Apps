package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ShippingAddressDetail extends BaseResponse {

    @SerializedName("data")
    public ShippingAddressModel shippingAddressModel;

    public ShippingAddressModel getShippingAddressModel() {
        return shippingAddressModel;
    }

    public void setShippingAddressModel(ShippingAddressModel shippingAddressModel) {
        this.shippingAddressModel = shippingAddressModel;
    }
}
