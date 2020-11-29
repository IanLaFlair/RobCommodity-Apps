package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class TransactionItem {

	@SerializedName("transaction_id")
	private int transactionId;

	@SerializedName("note")
	private String note;

	@SerializedName("product")
	private Product product;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("qty")
	private int qty;

	@SerializedName("id")
	private int id;

	public void setTransactionId(int transactionId){
		this.transactionId = transactionId;
	}

	public int getTransactionId(){
		return transactionId;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setProduct(Product product){
		this.product = product;
	}

	public Product getProduct(){
		return product;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setQty(int qty){
		this.qty = qty;
	}

	public int getQty(){
		return qty;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}