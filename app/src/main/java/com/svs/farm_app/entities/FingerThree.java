package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benson on 3/1/2015.
 */
public class FingerThree implements Serializable{
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("gap_fill")
    private String gapFilling;
    @SerializedName("gap_fill_emer")
    private String fillingAfterEmergence;
    @SerializedName("thin_num")
    private String recommendedPlantsPerStation;
    @SerializedName("thin_num_emer")
    private String thinningAfterEmergence;
    @SerializedName("farm_id")
    private String farmId;
    private transient String formDate;

    public FingerThree(String formDate, String companyId, String userId, String gapFilling, String fillingAfterEmergence, String recommendedPlantsPerStation, String thinningAfterEmergence, String farmId) {
       
        this.companyId = companyId;
        this.formDate = formDate;
        this.userId = userId;
        this.gapFilling = gapFilling;
        this.fillingAfterEmergence = fillingAfterEmergence;
        this.recommendedPlantsPerStation = recommendedPlantsPerStation;
        this.thinningAfterEmergence = thinningAfterEmergence;
        this.farmId = farmId;
    }

    public FingerThree() {

    }

    public String getCompanyId() {
        return companyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGapFilling() {
        return gapFilling;
    }

    public String getFillAfterEmergence() {
        return fillingAfterEmergence;
    }

    public String getRecommendedPlantsPerStation() {
        return recommendedPlantsPerStation;
    }

    public String getThinningAfterEmergence() {
        return thinningAfterEmergence;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGapFilling(String gapFilling) {
        this.gapFilling = gapFilling;
    }

    public void setFillingAfterEmergence(String fillingAfterEmergence) {
        this.fillingAfterEmergence = fillingAfterEmergence;
    }

    public void setRecommendedPlantsPerStation(String recommendedPlantsPerStation) {
        this.recommendedPlantsPerStation = recommendedPlantsPerStation;
    }

    public void setThinningAfterEmergence(String thinningAfterEmergence) {
        this.thinningAfterEmergence = thinningAfterEmergence;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getFormDate() {
        return formDate;
    }
}
