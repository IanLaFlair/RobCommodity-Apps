package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailTransactionHistoryResponse extends BaseResponse{

    @SerializedName("data")
    HistoryOrderModel historyOrderModel;

    @SerializedName("user")
    UserModel userModel;

    @SerializedName("shipment_address")
    ShippingAddressModel shippingAddressModel;

    @SerializedName("transaction_item")
    List<TransactionItem> transactionItem;

    @SerializedName("transaction_payment")
    List<PaymentTransaction> transactionPayment;

    @SerializedName("delivered_transaction")
    DeliveredTransaction deliveredTransactions;

    {
        this.transactionItem = new ArrayList<>();
        this.transactionPayment = new ArrayList<>();
    }

    public HistoryOrderModel getHistoryOrderModel() {
        return historyOrderModel;
    }

    public void setHistoryOrderModel(HistoryOrderModel historyOrderModel) {
        this.historyOrderModel = historyOrderModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ShippingAddressModel getShippingAddressModel() {
        return shippingAddressModel;
    }

    public void setShippingAddressModel(ShippingAddressModel shippingAddressModel) {
        this.shippingAddressModel = shippingAddressModel;
    }

    public List<TransactionItem> getTransactionItem() {
        return transactionItem;
    }

    public void setTransactionItem(List<TransactionItem> transactionItem) {
        this.transactionItem = transactionItem;
    }

    public List<PaymentTransaction> getTransactionPayment() {
        return transactionPayment;
    }

    public void setTransactionPayment(List<PaymentTransaction> transactionPayment) {
        this.transactionPayment = transactionPayment;
    }

    public DeliveredTransaction getDeliveredTransactions() {
        return deliveredTransactions;
    }

    public void setDeliveredTransactions(DeliveredTransaction deliveredTransactions) {
        this.deliveredTransactions = deliveredTransactions;
    }
}
