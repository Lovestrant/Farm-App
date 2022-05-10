package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FarmProduction implements Serializable{
    @SerializedName("farm_id")
    private String farmId;
    @SerializedName("grade_a")
    private String gradeA;
    @SerializedName("grade_b")
    private String gradeB;
    @SerializedName("grade_c")
    private String gradeC;
    @SerializedName("picking_date")
    private String pickingDate;
    private transient String collectDate;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("picking_count")
    private String pickingCount;

    public FarmProduction(String farmId, String pickingCount, String gradeA, String gradeB, String gradeC,
                          String pickingDate, String collectDate, String latitude, String longitude,
                          String userId, String companyId) {
        this.farmId = farmId;
        this.pickingCount = pickingCount;
        this.gradeA = gradeA;
        this.gradeB = gradeB;
        this.gradeC = gradeC;
        this.pickingDate = pickingDate;
        this.collectDate = collectDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.companyId = companyId;
    }

    public FarmProduction() {
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getPickingCount() {
        return pickingCount;
    }

    public void setPickingCount(String pickingCount) {
        this.pickingCount = pickingCount;
    }

    public String getGradeA() {
        return gradeA;
    }

    public void setGradeA(String gradeA) {
        this.gradeA = gradeA;
    }

    public String getGradeB() {
        return gradeB;
    }

    public void setGradeB(String gradeB) {
        this.gradeB = gradeB;
    }

    public String getGradeC() {
        return gradeC;
    }

    public void setGradeC(String gradeC) {
        this.gradeC = gradeC;
    }

    public String getPickingDate() {
        return pickingDate;
    }

    public void setPickingDate(String pickingDate) {
        this.pickingDate = pickingDate;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String lat) {
        this.latitude = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
