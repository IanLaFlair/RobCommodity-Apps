package com.kls.robcommodity.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyNowModel implements Parcelable {

    private Integer productId;
    private Long total;
    private String note;
    private Integer quantity;
    private String productName;
    private Long priceProduct;
    private String img;

    public BuyNowModel() {
    }


    public BuyNowModel(Parcel in) {
        if (in.readByte() == 0) {
            productId = null;
        } else {
            productId = in.readInt();
        }
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readLong();
        }
        note = in.readString();
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        productName = in.readString();
        if (in.readByte() == 0) {
            priceProduct = null;
        } else {
            priceProduct = in.readLong();
        }
        img = in.readString();
    }

    public static final Creator<BuyNowModel> CREATOR = new Creator<BuyNowModel>() {
        @Override
        public BuyNowModel createFromParcel(Parcel in) {
            return new BuyNowModel(in);
        }

        @Override
        public BuyNowModel[] newArray(int size) {
            return new BuyNowModel[size];
        }
    };

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(Long priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (productId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(productId);
        }
        if (total == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(total);
        }
        parcel.writeString(note);
        if (quantity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(quantity);
        }
        parcel.writeString(productName);
        if (priceProduct == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(priceProduct);
        }
        parcel.writeString(img);
    }
}
