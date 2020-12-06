package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ChargeModel {
    @SerializedName("redirect_url")
    private String redirectUrl;

    @SerializedName("token")
    private String token;

    public void setRedirectUrl(String redirectUrl){
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl(){
        return redirectUrl;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
