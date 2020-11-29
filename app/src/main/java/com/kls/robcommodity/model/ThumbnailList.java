package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ThumbnailList{

	@SerializedName("product_id")
	private int productId;

	@SerializedName("id")
	private int id;

	@SerializedName("url_image")
	private String urlImage;

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUrlImage(String urlImage){
		this.urlImage = urlImage;
	}

	public String getUrlImage(){
		return urlImage;
	}
}