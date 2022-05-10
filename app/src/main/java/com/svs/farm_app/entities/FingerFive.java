package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benson on 3/1/2015.
 */
public class FingerFive implements Serializable{
    @SerializedName("pest_level")
    private String pestLevel;
    @SerializedName("pest_damage")
    private String pestDamage;
    @SerializedName("last_scout")
    private String lastScout;
    @SerializedName("empty_cans")
    private String emptyCans;
    @SerializedName("peg_board_avail")
    private String pegBoardAvailable;
    @SerializedName("scout_method")
    private String scoutMethod;
    @SerializedName("spray_time")
    private String sprayTime;
    @SerializedName("correct_use_pesticide")
    private String correctPesticideUse;
    @SerializedName("safe_usage_cans")
    private String safeCanUsage;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("pest_abs_duration")
    private String pestAbsenceDuration;
    @SerializedName("farm_id")
    private String farmId;
    private transient String collectionDate;

    public FingerFive(String collectionDate, String companyId, String userId, String pestLevelValue, String pestDamageValue, String scoutSignValue, String emptyCansValue, String pegBoardValue, String scoutMethodValue, String sprayTimeValue, String pestAbsDurrationValue, String correctPesticideValue, String safeUsageCansValue, String farmId) {
        
        this.collectionDate = collectionDate;
        this.companyId = companyId;
        this.userId = userId;
        this.pestLevel = pestLevelValue;
        this.pestDamage = pestDamageValue;
        this.lastScout = scoutSignValue;
        this.emptyCans = emptyCansValue;
        this.pegBoardAvailable = pegBoardValue;
        this.scoutMethod = scoutMethodValue;
        this.sprayTime = sprayTimeValue;
        this.pestAbsenceDuration = pestAbsDurrationValue;
        this.correctPesticideUse = correctPesticideValue;
        this.safeCanUsage = safeUsageCansValue;
        this.farmId = farmId;
    }

    public FingerFive() {

    }

    public String getPestLevel() {
        return pestLevel;
    }

    public String getPestDamage() {
        return pestDamage;
    }

    public String getLastScout() {
        return lastScout;
    }

    public String getIfEmptyCansOnFarm() {
        return emptyCans;
    }

    public String getPegBoardAvailability() {
        return pegBoardAvailable;
    }

    public String getScoutMethod() {
        return scoutMethod;
    }

    public String getSprayTime() {
        return sprayTime;
    }

    public String getPestAbsenceDuration() {
        return pestAbsenceDuration;
    }

    public String getCorrectPesticideUse() {
        return correctPesticideUse;
    }

    public String getSafeUsageCans() {
        return safeCanUsage;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPestLevel(String pestLevel) {
        this.pestLevel = pestLevel;
    }

    public void setPestDamage(String pestDamage) {
        this.pestDamage = pestDamage;
    }

    public void setLastScout(String lastScout) {
        this.lastScout = lastScout;
    }

    public void setEmptyCans(String emptyCans) {
        this.emptyCans = emptyCans;
    }

    public void setPegBoardAvail(String pegBoardAvailable) {
        this.pegBoardAvailable = pegBoardAvailable;
    }

    public void setScoutMethod(String scoutMethod) {
        this.scoutMethod = scoutMethod;
    }

    public void setSprayTime(String sprayTime) {
        this.sprayTime = sprayTime;
    }

    public void setPestAbsenceDuration(String pestAbsenceDuration) {
        this.pestAbsenceDuration = pestAbsenceDuration;
    }

    public void setCorrectPesticideUsage(String correctPesticideUse) {
        this.correctPesticideUse = correctPesticideUse;
    }

    public void setSafeCanUsage(String safeCanUsage) {
        this.safeCanUsage = safeCanUsage;
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
