package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Benson on 3/17/2015.
 */
public class SignedDoc{
    @SerializedName("fid")
    private String farmerId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("company_id")
    private String companyId;

    public SignedDoc(String farmerId, String userId, String companyId) {
        this.farmerId = farmerId;
        this.userId = userId;
        this.companyId = companyId;
    }

    public SignedDoc() {

    }

    public String getFarmerId() {
        return farmerId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyId(String CompanyId) {
        this.companyId = CompanyId;
    }
}
