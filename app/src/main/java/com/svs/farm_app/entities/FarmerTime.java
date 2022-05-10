package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Benson on 2/20/2015.
 */
public class FarmerTime {
    @SerializedName("fid")
    private String farmerID;
    @SerializedName("company_id")
    private String companyID;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("farmer_time_in")
    private String farmerTimeIn;
    @SerializedName("farmer_time_out")
    private String farmerTimeOut;
    @SerializedName("train_type_id")
    private String trainCatID;
    @SerializedName("ext_train_id")
    private String extTrainID;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

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

    public FarmerTime(String farmerID, String extTrainID, String trainCatID, String latitude, String longitude, String farmerTimeIn, String farmerTimeOut, String userID, String companyID) {
        this.farmerID = farmerID;
        this.extTrainID = extTrainID;
        this.trainCatID = trainCatID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.farmerTimeIn = farmerTimeIn;
        this.farmerTimeOut = farmerTimeOut;
        this.userID = userID;
        this.companyID = companyID;
    }

    public FarmerTime() {

    }


    public String getExtTrainID() {
        return extTrainID;
    }

    public void setExTrainId(String extTrainId) {
        this.extTrainID = extTrainId;
    }

    public String getFarmerID() {
        return farmerID;
    }


    public String getTrainCatID() {
        return trainCatID;
    }

    public String getFarmerTimeIn() {
        return farmerTimeIn;
    }

    public String getFarmerTimeOut() {
        return farmerTimeOut;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getUserID() {
        return userID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }


    public void setUserId(String userID) {
        this.userID = userID;
    }

    public void setFarmerTimeIn(String farmerTimeIn) {
        this.farmerTimeIn = farmerTimeIn;
    }

    public void setFarmerTimeOut(String farmerTimeOut) {
        this.farmerTimeOut = farmerTimeOut;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setTrainCatID(String trainCatID) {
        this.trainCatID = trainCatID;
    }
}
