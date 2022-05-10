/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * @author Benson
 */
public class PlantingRains implements Serializable{
    @SerializedName("farm_id")
    private String farmId;
    @SerializedName("rain_date")
    private String rainDate;
    @SerializedName("collect_date")
    private String collectDate;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("company_id")
    private String companyID;

    public PlantingRains(String farmId, String rainDate, String collectDate, String remarks, String userId, String latitude, String longitude, String companyID) {

        this.farmId = farmId;
        this.remarks = remarks;
        this.rainDate = rainDate;
        this.collectDate = collectDate;
        this.userId = userId;
        this.remarks = remarks;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyID = companyID;

    }

    public PlantingRains() {

    }

    public String getFarmID() {
        return farmId;
    }

    public void setFarmID(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public String getUserID() {
        return userId;
    }

    public void setUserID(String userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setRainDate(String rainDate) {
        this.rainDate = rainDate;
    }

    public String getRainDate() {
        return rainDate;
    }

}
