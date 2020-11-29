package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ThumbnailResponse implements Parcelable {

    @SerializedName("url_image")
    String image;

    public ThumbnailResponse() {
    }

    public ThumbnailResponse(Parcel in) {
        image = in.readString();
    }

    public static final Creator<ThumbnailResponse> CREATOR = new Creator<ThumbnailResponse>() {
        @Override
        public ThumbnailResponse createFromParcel(Parcel in) {
            return new ThumbnailResponse(in);
        }

        @Override
        public ThumbnailResponse[] newArray(int size) {
            return new ThumbnailResponse[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
    }
}
