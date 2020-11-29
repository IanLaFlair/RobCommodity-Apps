package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HotItemModel implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("title")
    String title;

    @SerializedName("discount_price")
    Double price;

    @SerializedName("thumbnail_list")
    ThumbnailResponse thumbnailResponse;

    public HotItemModel() {
    }

    public HotItemModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
    }

    public static final Creator<HotItemModel> CREATOR = new Creator<HotItemModel>() {
        @Override
        public HotItemModel createFromParcel(Parcel in) {
            return new HotItemModel(in);
        }

        @Override
        public HotItemModel[] newArray(int size) {
            return new HotItemModel[size];
        }
    };

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ThumbnailResponse getThumbnailResponse() {
        return thumbnailResponse;
    }

    public void setThumbnailResponse(ThumbnailResponse thumbnailResponse) {
        this.thumbnailResponse = thumbnailResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(title);
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(price);
        }
    }
}
