package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Benson on 3/1/2015.
 */
public class Farm {
    @SerializedName("farm_id")
    private String farmID;
    @SerializedName("fid")
    private String farmerID;
    @SerializedName("farm_name")
    private String farmName;
    @SerializedName("farm_peri")
    private String farmPerimeter;
    @SerializedName("company_id")
    private String companyID;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("farm_ass")
    private String isForAsessment;
    @SerializedName("estimated_farm_area")
    private String estimatedFarmArea;
    @SerializedName("actual_farm_area")
    private String actualFarmArea;
    @SerializedName("village_id")
    private String villageID;

    public Farm(String farmID, String farmerID, String farmName, String estimatedFarmArea, String actualFarmArea, String villageID,
                String farmPerimeter, String isForAsessment, String latitude, String longitude, String companyID) {
        this.farmID = farmID;
        this.farmerID = farmerID;
        this.farmName = farmName;
        this.estimatedFarmArea = estimatedFarmArea;
        this.actualFarmArea = actualFarmArea;
        this.villageID = villageID;
        this.farmPerimeter = farmPerimeter;
        this.isForAsessment = isForAsessment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyID = companyID;
    }

    public Farm() {

    }

    public String getEstimatedFarmArea() {
        return estimatedFarmArea;
    }

    public void setEstimatedFarmArea(String estimatedFarmArea) {
        this.estimatedFarmArea = estimatedFarmArea;
    }

    public String getActualFarmArea() {
        return actualFarmArea;
    }

    public void setActualFarmArea(String actualFarmArea) {
        this.actualFarmArea = actualFarmArea;
    }

    public String getFarmID() {
        return farmID;
    }

    public String getFarmName() {
        return farmName;
    }

    public String getFarmPerimeter() {
        return farmPerimeter;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setFarmID(String farmID) {
        this.farmID = farmID;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public void setFarmPeri(String farmPerimeter) {
        this.farmPerimeter = farmPerimeter;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getFarmAss() {
        return isForAsessment;
    }

    public void setIsForAss(String farmAss) {
        this.isForAsessment = farmAss;
    }

    public String getVillageID() {
        return villageID;
    }

    public void setVillageID(String villageID) {
        this.villageID = villageID;
    }


    public String getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }
}
