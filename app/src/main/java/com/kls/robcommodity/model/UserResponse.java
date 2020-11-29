package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse extends BaseResponse {

    @SerializedName("data")
    public UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
