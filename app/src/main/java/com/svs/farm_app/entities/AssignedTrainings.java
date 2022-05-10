package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 1/26/2015.
 */
public class AssignedTrainings {
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("train_cat_id")
    private String trainCatID;
    @SerializedName("company_id")
    private String companyID;
    @SerializedName("train_start_time")
    private String trainStartTime;
    @SerializedName("train_stop_time")
    private String trainStopTime;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("ext_train_id")
    private String extTrainID;

    public AssignedTrainings(String extTrainID, String trainCatID, String trainStartTime, String trainStopTime, String latitude, String longitude, String userID, String companyID) {
        this.extTrainID = extTrainID;
        this.trainCatID = trainCatID;
        this.trainStartTime = trainStartTime;
        this.trainStopTime = trainStopTime;
        this.companyID = companyID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userID = userID;
    }

    public AssignedTrainings() {

    }

    public String getExtTrainID() {
        return extTrainID;
    }

    public void setExtTrainID(String extTrainID) {
        this.extTrainID = extTrainID;
    }

    public String getTrainCatID() {
        return trainCatID;
    }

    public void setTrainCatID(String trainCatID) {
        this.trainCatID = trainCatID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getTStartTime() {
        return trainStartTime;
    }

    public void setTStartTime(String TStartTime) {
        this.trainStartTime = TStartTime;
    }

    public String getTStopTime() {
        return trainStopTime;
    }

    public void setTStopTime(String TStopTime) {
        this.trainStopTime = TStopTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
