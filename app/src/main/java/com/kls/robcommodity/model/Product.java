package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class Product{

	@SerializedName("short_description")
	private String shortDescription;

	@SerializedName("sold")
	private int sold;

	@SerializedName("sub_category")
	private Object subCategory;

	@SerializedName("discount_price")
	private Double discountPrice;

	@SerializedName("rating")
	private int rating;

	@SerializedName("description")
	private String description;

	@SerializedName("discount")
	private int discount;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("title")
	private String title;

	@SerializedName("sale")
	private int sale;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("price")
	private int price;

	@SerializedName("sub_category_id")
	private Object subCategoryId;

	@SerializedName("thumbnail_list")
	private ThumbnailList thumbnailList;

	@SerializedName("id")
	private int id;

	@SerializedName("categories")
	private Categories categories;

	@SerializedName("sku")
	private String sku;

	@SerializedName("stock")
	private int stock;

	@SerializedName("status")
	private String status;

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setSold(int sold){
		this.sold = sold;
	}

	public int getSold(){
		return sold;
	}

	public void setSubCategory(Object subCategory){
		this.subCategory = subCategory;
	}

	public Object getSubCategory(){
		return subCategory;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setDiscount(int discount){
		this.discount = discount;
	}

	public int getDiscount(){
		return discount;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setSale(int sale){
		this.sale = sale;
	}

	public int getSale(){
		return sale;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setSubCategoryId(Object subCategoryId){
		this.subCategoryId = subCategoryId;
	}

	public Object getSubCategoryId(){
		return subCategoryId;
	}

	public void setThumbnailList(ThumbnailList thumbnailList){
		this.thumbnailList = thumbnailList;
	}

	public ThumbnailList getThumbnailList(){
		return thumbnailList;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCategories(Categories categories){
		this.categories = categories;
	}

	public Categories getCategories(){
		return categories;
	}

	public void setSku(String sku){
		this.sku = sku;
	}

	public String getSku(){
		return sku;
	}

	public void setStock(int stock){
		this.stock = stock;
	}

	public int getStock(){
		return stock;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}