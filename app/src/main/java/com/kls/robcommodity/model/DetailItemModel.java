package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailItemModel {

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("price")
    Integer price;

    @SerializedName("stock")
    Integer stock;

    @SerializedName("sold")
    Integer sold;

    @SerializedName("thumbnail")
    ArrayList<ThumbnailResponse> thumbnailResponses;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public ArrayList<ThumbnailResponse> getThumbnailResponses() {
        return thumbnailResponses;
    }

    public void setThumbnailResponses(ArrayList<ThumbnailResponse> thumbnailResponses) {
        this.thumbnailResponses = thumbnailResponses;
    }
}
