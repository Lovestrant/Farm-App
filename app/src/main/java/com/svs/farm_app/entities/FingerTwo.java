package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benson on 3/1/2015.
 */
public class FingerTwo implements Serializable{
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("correct_seed")
    private String correctSeedPlanting;
    @SerializedName("row_spacing")
    private String rowSpacing;
    @SerializedName("seed_per_stat")
    private String seedsPerStation;
    @SerializedName("planting_time")
    private String plantingTime;
    @SerializedName("farm_id")
    private String farmId;
    private transient String collectDate;

    public FingerTwo(String collectDate, String companyId, String userId, String correctSeedPlanting, String rowSpacing, String seedsPerStation, String plantingTime, String farmId) {
       this.companyId = companyId;
        this.collectDate = collectDate;
        this.userId = userId;
        this.correctSeedPlanting = correctSeedPlanting;
        this.rowSpacing = rowSpacing;
        this.seedsPerStation = seedsPerStation;
        this.plantingTime = plantingTime;
        this.farmId = farmId;
    }

    public FingerTwo() {

    }

    public String getCompanyId() {
        return companyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCorrectSeedPlanting() {
        return correctSeedPlanting;
    }

    public String getRowSpacing() {
        return rowSpacing;
    }

    public String getSeedsPerStation() {
        return seedsPerStation;
    }

    public String getPlantingTime() {
        return plantingTime;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCorrectSeedPlanting(String correctSeed) {
        this.correctSeedPlanting = correctSeed;
    }

    public void setRowSpacing(String rowSpacing) {
        this.rowSpacing = rowSpacing;
    }

    public void setSeedsPerStation(String seedsPerStation) {
        this.seedsPerStation = seedsPerStation;
    }

    public void setPlantingTime(String plantingTime) {
        this.plantingTime = plantingTime;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectionDate() {
        return collectDate;
    }
}
