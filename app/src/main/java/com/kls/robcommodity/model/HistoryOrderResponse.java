package com.kls.robcommodity.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HistoryOrderResponse extends BaseResponse {

	@SerializedName("data")
	private List<HistoryOrderModel> data;

	public void setData(List<HistoryOrderModel> data){
		this.data = data;
	}

	public List<HistoryOrderModel> getData(){
		return data;
	}
}