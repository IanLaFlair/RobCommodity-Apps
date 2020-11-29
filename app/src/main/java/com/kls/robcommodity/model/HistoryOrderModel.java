package com.kls.robcommodity.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HistoryOrderModel {

	@SerializedName("cancelled_at")
	private Object cancelledAt;

	@SerializedName("payment_url")
	private String paymentUrl;

	@SerializedName("base_total_price")
	private String baseTotalPrice;

	@SerializedName("cancelled_by")
	private Object cancelledBy;

	@SerializedName("cancellation_note")
	private Object cancellationNote;

	@SerializedName("shipment_address")
	private ShippingAddressModel shipmentAddress;

	@SerializedName("payment_transaction")
	private PaymentTransaction paymentTransaction;

	@SerializedName("transaction_item")
	private List<TransactionItem> transactionItem;

	@SerializedName("payment_status")
	private String paymentStatus;

	@SerializedName("payment_token")
	private String paymentToken;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("order_date")
	private String orderDate;

	@SerializedName("tax_percent")
	private String taxPercent;

	@SerializedName("approved_by")
	private String approvedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("approved_at")
	private String approvedAt;

	@SerializedName("delivered_transaction")
	private DeliveredTransaction deliveredTransaction;

	@SerializedName("id")
	private int id;

	@SerializedName("shipment_address_id")
	private int shipmentAddressId;

	@SerializedName("grand_total")
	private String grandTotal;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("user")
	private UserModel user;

	@SerializedName("status")
	private String status;

	public void setCancelledAt(Object cancelledAt){
		this.cancelledAt = cancelledAt;
	}

	public Object getCancelledAt(){
		return cancelledAt;
	}

	public void setPaymentUrl(String paymentUrl){
		this.paymentUrl = paymentUrl;
	}

	public String getPaymentUrl(){
		return paymentUrl;
	}

	public void setBaseTotalPrice(String baseTotalPrice){
		this.baseTotalPrice = baseTotalPrice;
	}

	public String getBaseTotalPrice(){
		return baseTotalPrice;
	}

	public void setCancelledBy(Object cancelledBy){
		this.cancelledBy = cancelledBy;
	}

	public Object getCancelledBy(){
		return cancelledBy;
	}

	public void setCancellationNote(Object cancellationNote){
		this.cancellationNote = cancellationNote;
	}

	public Object getCancellationNote(){
		return cancellationNote;
	}

	public void setShipmentAddress(ShippingAddressModel shipmentAddress){
		this.shipmentAddress = shipmentAddress;
	}

	public ShippingAddressModel getShipmentAddress(){
		return shipmentAddress;
	}

	public void setPaymentTransaction(PaymentTransaction paymentTransaction){
		this.paymentTransaction = paymentTransaction;
	}

	public PaymentTransaction getPaymentTransaction(){
		return paymentTransaction;
	}

	public void setTransactionItem(List<TransactionItem> transactionItem){
		this.transactionItem = transactionItem;
	}

	public List<TransactionItem> getTransactionItem(){
		return transactionItem;
	}

	public void setPaymentStatus(String paymentStatus){
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentStatus(){
		return paymentStatus;
	}

	public void setPaymentToken(String paymentToken){
		this.paymentToken = paymentToken;
	}

	public String getPaymentToken(){
		return paymentToken;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setTaxPercent(String taxPercent){
		this.taxPercent = taxPercent;
	}

	public String getTaxPercent(){
		return taxPercent;
	}

	public void setApprovedBy(String approvedBy){
		this.approvedBy = approvedBy;
	}

	public String getApprovedBy(){
		return approvedBy;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setApprovedAt(String approvedAt){
		this.approvedAt = approvedAt;
	}

	public String getApprovedAt(){
		return approvedAt;
	}

	public void setDeliveredTransaction(DeliveredTransaction deliveredTransaction){
		this.deliveredTransaction = deliveredTransaction;
	}

	public DeliveredTransaction getDeliveredTransaction(){
		return deliveredTransaction;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShipmentAddressId(int shipmentAddressId){
		this.shipmentAddressId = shipmentAddressId;
	}

	public int getShipmentAddressId(){
		return shipmentAddressId;
	}

	public void setGrandTotal(String grandTotal){
		this.grandTotal = grandTotal;
	}

	public String getGrandTotal(){
		return grandTotal;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setUser(UserModel user){
		this.user = user;
	}

	public UserModel getUser(){
		return user;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}