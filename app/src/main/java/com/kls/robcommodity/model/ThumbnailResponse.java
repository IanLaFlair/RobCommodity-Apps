package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ThumbnailResponse {

    @SerializedName("url_image")
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
