package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ShippingAddressModel {

    @SerializedName("id")
    Integer id;
    @SerializedName("user_id")
    Integer userId;
    @SerializedName("selected")
    Integer selected;
    @SerializedName("recipient_name")
    String recipientName;
    @SerializedName("phone")
    String phone;
    @SerializedName("country")
    String country;
    @SerializedName("address_line_1")
    String address1;
    @SerializedName("address_line_2")
    String address2;
    @SerializedName("city")
    String city;
    @SerializedName("state")
    String state;
    @SerializedName("postal_code")
    String postalCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
