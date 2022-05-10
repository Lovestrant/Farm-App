/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Class to interact with database handler (farm assessment forms major table).
 * The manor forms are a grouping of forms that are same in structure.Refer to demo farmer note book
 * @author Benson
 */
public class FarmAssFormsMajor{
    @SerializedName("farm_id")
    private String farmId;
    @SerializedName("form_type_id")
    private String formTypeId;
    @SerializedName("activity_date")
    private String activityDate;
    @SerializedName("family_hours")
    private String familyHours;
    @SerializedName("hired_hours")
    private String hiredHours;
    private transient String collectDate;
    @SerializedName("money_out")
    private String moneyOut;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("activity_method")
    private String activityMethod;

    public FarmAssFormsMajor(String farmId, String formTypeId, String activityMethod, String activityDate, String familyHours, String hiredHours, String moneyOut, String remarks, String userId, String collectDate, String latitude, String longitude, String companyId) {
        this.farmId = farmId;
        this.formTypeId = formTypeId;
        this.activityMethod = activityMethod;
        this.activityDate = activityDate;
        this.familyHours = familyHours;
        this.hiredHours = hiredHours;
        this.collectDate = collectDate;
        this.moneyOut = moneyOut;
        this.remarks = remarks;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyId = companyId;

    }

    public FarmAssFormsMajor() {
    }

    public String getFarmId() {
        return farmId;
    }

    public String getFormTypeId() {
        return formTypeId;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public String getFamilyHours() {
        return familyHours;
    }

    public String getHiredHours() {
        return hiredHours;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public String getMoneyOut() {
        return moneyOut;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public void setFormTypeId(String formTypeId) {
        this.formTypeId = formTypeId;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public void setFamilyHours(String familyHours) {
        this.familyHours = familyHours;
    }

    public void setHiredHours(String hiredHours) {
        this.hiredHours = hiredHours;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public void setMoneyOut(String moneyOut) {
        this.moneyOut = moneyOut;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getActivityMethod() {
        return activityMethod;
    }

    public void setActivityMethod(String activityMethod) {
        this.activityMethod = activityMethod;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    public void setLatitude(String latitude){
    	this.latitude = latitude;
    }
    
    public void setLongitude(String longitude){
    	this.longitude = longitude;
    }

}
