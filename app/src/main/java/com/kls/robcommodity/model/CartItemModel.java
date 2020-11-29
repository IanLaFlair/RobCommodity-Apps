package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CartItemModel implements Parcelable {

    @SerializedName("id")
    int id;

    @SerializedName("qty")
    int quantity;

    @SerializedName("note")
    String note;

    @SerializedName("product")
    HotItemModel hotItemModel;

    boolean selected = false;

    public CartItemModel() {
    }

    public CartItemModel(Parcel in) {
        id = in.readInt();
        quantity = in.readInt();
        note = in.readString();
    }

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel in) {
            return new CartItemModel(in);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public HotItemModel getHotItemModel() {
        return hotItemModel;
    }

    public void setHotItemModel(HotItemModel hotItemModel) {
        this.hotItemModel = hotItemModel;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(quantity);
        parcel.writeString(note);
    }
}
