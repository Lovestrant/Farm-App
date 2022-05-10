package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benson on 3/1/2015.
 */
public class FingerOne implements Serializable{
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("soil_type")
    private String soilType;
    @SerializedName("water_log_risk")
    private String WaterLogRisk;
    @SerializedName("crop_rotation")
    private String cropRotation;
    @SerializedName("ratoon")
    private String ratoon;
    @SerializedName("crop_residues")
    private String cropResidues;
    @SerializedName("manure")
    private String manure;
    @SerializedName("land_prep")
    private String landPreparation;
    @SerializedName("seed_bed_prep")
    private String seedBedPreparation;
    @SerializedName("erosion_prev")
    private String erosionPrevention;
    @SerializedName("farm_id")
    private String farmId;
    private transient String collectionDate;

    public FingerOne(String collectionDate, String companyId, String userId, String soilTypeValue, String waterLogRiskValue, String erosionPrevValue, String cropRotationValue, String ratoonValue, String cropResValue, String manureValue, String landPrepValue, String seedBedPrepValue, String farmId) {
        
        this.collectionDate = collectionDate;
        this.companyId = companyId;
        this.userId = userId;
        this.soilType = soilTypeValue;
        this.WaterLogRisk = waterLogRiskValue;
        this.erosionPrevention = erosionPrevValue;
        this.cropRotation = cropRotationValue;
        this.ratoon = ratoonValue;
        this.cropResidues = cropResValue;
        this.manure = manureValue;
        this.landPreparation = landPrepValue;
        this.seedBedPreparation = seedBedPrepValue;
        this.farmId = farmId;
    }

    public FingerOne() {

    }


    public String getCompanyId() {
        return companyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSoilType() {
        return soilType;
    }

    public String getWaterLogRisk() {
        return WaterLogRisk;
    }


    public String getErosionPrevention() {
        return erosionPrevention;
    }

    public String getCropRotation() {
        return cropRotation;
    }

    public String getRatoon() {
        return ratoon;
    }

    public String getCropResidues() {
        return cropResidues;
    }

    public String getManure() {
        return manure;
    }

    public String getLandPreparation() {
        return landPreparation;
    }

    public String getSeedBedPreparation() {
        return seedBedPreparation;
    }


    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public void setWaterLogRisk(String WaterLogRisk) {
        this.WaterLogRisk = WaterLogRisk;
    }

    public void setErosionPrevention(String erosionPrevention) {
        this.erosionPrevention = erosionPrevention;
    }

    public void setCropRotation(String cropRotation) {
        this.cropRotation = cropRotation;
    }

    public void setRatoon(String ratoon) {
        this.ratoon = ratoon;
    }

    public void setCropResidues(String cropResidues) {
        this.cropResidues = cropResidues;
    }

    public void setManure(String manure) {
        this.manure = manure;
    }

    public void setLandPrep(String landPreparation) {
        this.landPreparation = landPreparation;
    }

    public void setSeeBedPreparation(String seedBedPreparation) {
        this.seedBedPreparation = seedBedPreparation;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectionDate() {
        return collectionDate;
    }
}
