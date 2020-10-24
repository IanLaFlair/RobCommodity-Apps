package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HotItemModel  {

    @SerializedName("id")
    Integer id;

    @SerializedName("title")
    String title;

    @SerializedName("price")
    Integer price;

    @SerializedName("thumbnail_list")
    ThumbnailResponse thumbnailResponse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ThumbnailResponse getThumbnailResponse() {
        return thumbnailResponse;
    }

    public void setThumbnailResponse(ThumbnailResponse thumbnailResponse) {
        this.thumbnailResponse = thumbnailResponse;
    }
}
