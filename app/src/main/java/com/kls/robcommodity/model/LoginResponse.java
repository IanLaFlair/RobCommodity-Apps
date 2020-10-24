package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    Integer status;
    @SerializedName("message")
    String message;
    @SerializedName("token")
    String token;

}
