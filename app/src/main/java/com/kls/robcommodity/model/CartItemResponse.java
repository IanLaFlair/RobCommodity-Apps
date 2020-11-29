package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CartItemResponse extends BaseResponse implements Parcelable {

    @SerializedName("total")
    private BigDecimal total;

    @SerializedName("data")
    private ArrayList<CartItemModel> cartItemModels;

    public CartItemResponse() {
    }

    public CartItemResponse(Parcel in) {
        cartItemModels = in.createTypedArrayList(CartItemModel.CREATOR);
    }

    public static final Creator<CartItemResponse> CREATOR = new Creator<CartItemResponse>() {
        @Override
        public CartItemResponse createFromParcel(Parcel in) {
            return new CartItemResponse(in);
        }

        @Override
        public CartItemResponse[] newArray(int size) {
            return new CartItemResponse[size];
        }
    };

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ArrayList<CartItemModel> getCartItemModels() {
        return cartItemModels;
    }

    public void setCartItemModels(ArrayList<CartItemModel> cartItemModels) {
        this.cartItemModels = cartItemModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(cartItemModels);
    }
}
