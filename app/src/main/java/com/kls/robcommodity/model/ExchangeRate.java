package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ExchangeRate {

    @SerializedName("IDR")
    public Double idr;

    public Double getIdr() {
        return idr;
    }

    public void setIdr(Double idr) {
        this.idr = idr;
    }
}
