package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

public class ShowIntent {
    @SerializedName("updated_by")
    private String userID;
    @SerializedName("company_id")
    private String companyID;
    @SerializedName("fid")
    private String farmerID;

    public ShowIntent(String farmerID, String userID, String companyID) {
        this.farmerID = farmerID;
        this.userID = userID;
        this.companyID = companyID;
    }

    public ShowIntent() {
    }

    public String getFarmerId() {
        return farmerID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public String getUserId() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCompanyId() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
}
