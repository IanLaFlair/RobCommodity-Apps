package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("status")
    Integer status;
    @SerializedName("message")
    String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
