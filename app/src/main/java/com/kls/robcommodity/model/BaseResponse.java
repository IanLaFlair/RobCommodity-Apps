package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("success")
    boolean success;
    @SerializedName("status")
    int status;
    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
