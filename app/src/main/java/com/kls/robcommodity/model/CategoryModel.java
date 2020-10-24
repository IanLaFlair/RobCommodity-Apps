package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {

    String name;
    Integer Icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcon() {
        return Icon;
    }

    public void setIcon(Integer icon) {
        Icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeValue(this.Icon);
    }

    public CategoryModel() {
    }

    protected CategoryModel(Parcel in) {
        this.name = in.readString();
        this.Icon = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryModel> CREATOR = new Parcelable.Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel source) {
            return new CategoryModel(source);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}


