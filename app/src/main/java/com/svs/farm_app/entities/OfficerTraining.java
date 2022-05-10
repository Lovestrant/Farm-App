package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Benson Wamae on 10-Feb-17.
 */
public class OfficerTraining {
    @SerializedName("ext_train_id")
    private String extTrainID;
    @SerializedName("train_cat_id")
    private String trainCatID;
    @SerializedName("train_cat")
    private String trainCat;
    @SerializedName("ext_train_date")
    private String trainDate;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("farm_id")
    private String farmID;
    @SerializedName("village_id")
    private String villageID;
    @SerializedName("fid")
    private String farmerID;

    public OfficerTraining(String extTrainID, String trainCatID, String trainCat, String trainDate,
                           String userID, String farmID, String villageID, String farmerID) {
        this.extTrainID = extTrainID;
        this.trainCatID = trainCatID;
        this.trainCat = trainCat;
        this.trainDate = trainDate;
        this.userID = userID;
        this.farmID = farmID;
        this.villageID = villageID;
        this.farmerID = farmerID;
    }

    public OfficerTraining() {

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

    public String getTrainCat() {
        return trainCat;
    }

    public void setTrainCat(String trainCat) {
        this.trainCat = trainCat;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFarmID() {
        return farmID;
    }

    public void setFarmID(String farmID) {
        this.farmID = farmID;
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
