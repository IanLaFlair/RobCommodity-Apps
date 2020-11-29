package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ExchangeRateResponse {

    @SerializedName("rates")
    public ExchangeRate exchangeRate;

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
