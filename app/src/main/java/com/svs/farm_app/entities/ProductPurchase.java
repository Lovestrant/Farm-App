package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * Created by wamae on 12/12/2017.
 */
public class ProductPurchase implements Serializable {
    @SerializedName("fid")
    int farmerId;
    @SerializedName("cotton_grade_id")
    int gradeId;
    @SerializedName("cotton_weight")
    float weight;
    @SerializedName("cotton_price")
    float price;
    @SerializedName("receipt_no")
    String receiptNumber;
    @SerializedName("deductions")
    float deductions;
    @SerializedName("user_id")
    int userId;
    @SerializedName("company_id")
    int companyId;

    public ProductPurchase(int farmerId, int gradeId, float weight, float price, float deductions, String receiptNumber, int userId,int companyId) {
        this.farmerId = farmerId;
        this.gradeId = gradeId;
        this.weight = weight;
        this.price = price;
        this.deductions = deductions;
        this.receiptNumber = receiptNumber;
        this.userId = userId;
        this.companyId = companyId;
    }

    public ProductPurchase() {

    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public float getDeductions() {
        return deductions;
    }

    public void setDeductions(float deductions) {
        this.deductions = deductions;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
