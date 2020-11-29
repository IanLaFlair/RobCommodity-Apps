package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class Categories{

	@SerializedName("image")
	private Object image;

	@SerializedName("logo")
	private Object logo;

	@SerializedName("id")
	private int id;

	@SerializedName("category")
	private String category;

	public void setImage(Object image){
		this.image = image;
	}

	public Object getImage(){
		return image;
	}

	public void setLogo(Object logo){
		this.logo = logo;
	}

	public Object getLogo(){
		return logo;
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