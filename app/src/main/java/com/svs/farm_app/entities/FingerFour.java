package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benson on 3/1/2015.
 */
public class FingerFour implements Serializable{
    @SerializedName("first_branch")
    private String firstBranching;
    @SerializedName("foliar")
    private String foliar;
    @SerializedName("weeds")
    private String weeds;
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("farm_id")
    private String farmId;
    private transient String colletionDate;

    public FingerFour(String companyId, String userId, String branchesValue, String foliarValue, String weedsValue, String farmId) {
       
        this.companyId = companyId;
        this.userId = userId;
        this.firstBranching = branchesValue;
        this.foliar = foliarValue;
        this.weeds = weedsValue;
        this.farmId = farmId;
    }

    public FingerFour() {

    }


    public String getUserId() {
        return userId;
    }
    public String getFirstBranch() {
        return firstBranching;
    }

    public String getFoliar() {
        return foliar;
    }

    public String getWeeds() {
        return weeds;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

    public void setFirstBranching(String firstBranching) {
        this.firstBranching = firstBranching;
    }

    public void setFoliar(String foliar) {
        this.foliar = foliar;
    }

    public void setWeeds(String weeds) {
        this.weeds = weeds;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectionDate() {
        return colletionDate;
    }

    public String getCompanyId() {
     return companyId;
    }
}
