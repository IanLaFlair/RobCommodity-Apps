package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class Categories{

	@SerializedName("image")
	private String image;

	@SerializedName("logo")
	private String logo;

	@SerializedName("id")
	private int id;

	@SerializedName("category")
	private String category;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}
}