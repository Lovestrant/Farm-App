package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/2/2015.
 */
public class Companies extends Object{
    @SerializedName("company_id")
    private String companyID;
    @SerializedName("company_name")
    private String companyName;

    public Companies(String companyID, String companyName) {
        this.companyID = companyID;
        this.companyName = companyName;
    }

    public Companies() {

    }

    public String getCompanyID() {
        return companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
