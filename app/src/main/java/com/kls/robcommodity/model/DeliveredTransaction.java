package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class DeliveredTransaction{

	@SerializedName("transaction_id")
	private int transactionId;

	@SerializedName("shipping_service_name")
	private String shippingServiceName;

	@SerializedName("shipping_cost")
	private String shippingCost;

	@SerializedName("shipment_address")
	private ShippingAddressModel shipmentAddress;

	@SerializedName("total_qty")
	private int totalQty;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("shipment_date")
	private String shipmentDate;

	@SerializedName("total_weight")
	private Object totalWeight;

	@SerializedName("receipt_number")
	private String receiptNumber;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("received_date")
	private Object receivedDate;

	@SerializedName("id")
	private int id;

	@SerializedName("shipment_address_id")
	private int shipmentAddressId;

	@SerializedName("shipped_by")
	private String shippedBy;

	@SerializedName("shipment_type")
	private String shipmentType;

	@SerializedName("shipment_status")
	private String shipmentStatus;

	public void setTransactionId(int transactionId){
		this.transactionId = transactionId;
	}

	public int getTransactionId(){
		return transactionId;
	}

	public void setShippingServiceName(String shippingServiceName){
		this.shippingServiceName = shippingServiceName;
	}

	public String getShippingServiceName(){
		return shippingServiceName;
	}

	public void setShippingCost(String shippingCost){
		this.shippingCost = shippingCost;
	}

	public String getShippingCost(){
		return shippingCost;
	}

	public void setShipmentAddress(ShippingAddressModel shipmentAddress){
		this.shipmentAddress = shipmentAddress;
	}

	public ShippingAddressModel getShipmentAddress(){
		return shipmentAddress;
	}

	public void setTotalQty(int totalQty){
		this.totalQty = totalQty;
	}

	public int getTotalQty(){
		return totalQty;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setShipmentDate(String shipmentDate){
		this.shipmentDate = shipmentDate;
	}

	public String getShipmentDate(){
		return shipmentDate;
	}

	public void setTotalWeight(Object totalWeight){
		this.totalWeight = totalWeight;
	}

	public Object getTotalWeight(){
		return totalWeight;
	}

	public void setReceiptNumber(String receiptNumber){
		this.receiptNumber = receiptNumber;
	}

	public String getReceiptNumber(){
		return receiptNumber;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setReceivedDate(Object receivedDate){
		this.receivedDate = receivedDate;
	}

	public Object getReceivedDate(){
		return receivedDate;
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

	public void setShippedBy(String shippedBy){
		this.shippedBy = shippedBy;
	}

	public String getShippedBy(){
		return shippedBy;
	}

	public void setShipmentType(String shipmentType){
		this.shipmentType = shipmentType;
	}

	public String getShipmentType(){
		return shipmentType;
	}

	public void setShipmentStatus(String shipmentStatus){
		this.shipmentStatus = shipmentStatus;
	}

	public String getShipmentStatus(){
		return shipmentStatus;
	}
}