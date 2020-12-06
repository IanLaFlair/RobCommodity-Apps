package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class ChargeResponse extends BaseResponse{

	@SerializedName("data")
	ChargeModel chargeModel;

	public ChargeModel getChargeModel() {
		return chargeModel;
	}

	public void setChargeModel(ChargeModel chargeModel) {
		this.chargeModel = chargeModel;
	}
}