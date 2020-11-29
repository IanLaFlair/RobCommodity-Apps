package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class PaymentTransaction{

	@SerializedName("transaction_id")
	private int transactionId;

	@SerializedName("amount")
	private Double amount;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("method")
	private String method;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("token")
	private String token;

	@SerializedName("payment_code")
	private String paymentCode;

	@SerializedName("status")
	private String status;

	public void setTransactionId(int transactionId){
		this.transactionId = transactionId;
	}

	public int getTransactionId(){
		return transactionId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setPaymentType(String paymentType){
		this.paymentType = paymentType;
	}

	public String getPaymentType(){
		return paymentType;
	}

	public void setMethod(String method){
		this.method = method;
	}

	public String getMethod(){
		return method;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setPaymentCode(String paymentCode){
		this.paymentCode = paymentCode;
	}

	public String getPaymentCode(){
		return paymentCode;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}