package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Scouting implements Serializable {
    @SerializedName("farm_id")
    public String farmId;
    @SerializedName("activity_date")
    public String activityDate;
    @SerializedName("family_hours")
    public String familyHours;
    @SerializedName("hired_hours")
    public String hiredHours;
    @SerializedName("money_out")
    public String moneyOut;
    @SerializedName("boll_worm")
    public String bollWorm;
    @SerializedName("jassid")
    public String jassid;
    @SerializedName("stainer")
    public String stainer;
    @SerializedName("aphid")
    public String aphid;
    @SerializedName("beneficial")
    public String beneficialInsects;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("collect_date")
    public transient String collectDate;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("company_id")
    public String companyId;
    @SerializedName("spray")
    public String sprayDecision;

    public Scouting(String farmId, String activityDate, String familyHours,
                    String hiredHours, String moneyOut, String bollWorm,
                    String jassid, String stainer, String aphid, String beneficialInsects,
                    String sprayDecision, String userId, String collectDate,
                    String latitude, String longitude, String companyId) {
        this.farmId = farmId;
        this.activityDate = activityDate;
        this.familyHours = familyHours;
        this.hiredHours = hiredHours;
        this.moneyOut = moneyOut;
        this.bollWorm = bollWorm;
        this.jassid = jassid;
        this.stainer = stainer;
        this.aphid = aphid;
        this.beneficialInsects = beneficialInsects;
        this.sprayDecision = sprayDecision;
        this.userId = userId;
        this.collectDate = collectDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyId = companyId;
    }

    public Scouting() {
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getFamilyHours() {
        return familyHours;
    }

    public void setFamilyHours(String familyHours) {
        this.familyHours = familyHours;
    }

    public String getHiredHours() {
        return hiredHours;
    }

    public void setHiredHours(String hiredHours) {
        this.hiredHours = hiredHours;
    }

    public String getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(String moneyOut) {
        this.moneyOut = moneyOut;
    }

    public String getBollWorm() {
        return bollWorm;
    }

    public void setBollWorm(String ballWorm) {
        this.bollWorm = ballWorm;
    }

    public String getJassid() {
        return jassid;
    }

    public void setJassid(String jassid) {
        this.jassid = jassid;
    }

    public String getStainer() {
        return stainer;
    }

    public void setStainer(String stainer) {
        this.stainer = stainer;
    }

    public String getAphid() {
        return aphid;
    }

    public void setAphid(String aphid) {
        this.aphid = aphid;
    }

    public String getBeneficialInsects() {
        return beneficialInsects;
    }

    public void setBeneficialInsects(String beneficialInsects) {
        this.beneficialInsects = beneficialInsects;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSprayDecision() {
        return sprayDecision;
    }

    public void setSprayDecision(String sprayDecision) {
        this.sprayDecision = sprayDecision;
    }

}
