package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Collected villageId per season
 * Created by wamae on 12/12/2017.
 */
public class ProductGrade {
    @SerializedName("village_id")
    private int villageId;
    @SerializedName("product_grade_id")
    private int productGradeId;
    @SerializedName("grade_name")
    private String gradeName;
    @SerializedName("price")
    private float price;
    @SerializedName("price_date")
    private String priceDate;

    public ProductGrade(int villageId, int productGradeId,String gradeName, float price, String priceDate) {
        this.villageId = villageId;
        this.productGradeId = productGradeId;
        this.gradeName = gradeName;
        this.price = price;
        this.priceDate = priceDate;
    }

    public ProductGrade() {

    }

    public int getVillageId() {
        return villageId;
    }

    public int getProductGradeId() {
        return productGradeId;
    }
    public String getGradeName() {
        return gradeName;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public void setProductGradeId(int productGradeId) {
        this.productGradeId = productGradeId;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public void setPrice(float ballWeight) {
        this.price = ballWeight;
    }

    public float getPrice(){
        return price;
    }

    public String getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    @Override
    public String toString() {
        return gradeName;
    }
}
