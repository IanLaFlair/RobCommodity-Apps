package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserModel implements Parcelable {
    @SerializedName("id")
    public String userId;
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("role")
    public String role;
    @SerializedName("no_telp")
    public String numberPhone;
    @SerializedName("photo")
    public String picture;

    public UserModel() {
    }

    public UserModel(Parcel in) {
        userId = in.readString();
        username = in.readString();
        email = in.readString();
        role = in.readString();
        numberPhone = in.readString();
        picture = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(role);
        parcel.writeString(numberPhone);
        parcel.writeString(picture);
    }
}
