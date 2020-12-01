package com.kls.robcommodity.model;

import com.google.gson.annotations.SerializedName;

public class CategoryProductResponse extends BaseResponse{

    @SerializedName("result")
    ProductResult productResult;

    public ProductResult getProductResult() {
        return productResult;
    }

    public void setProductResult(ProductResult productResult) {
        this.productResult = productResult;
    }
}
