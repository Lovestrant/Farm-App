package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wamae on 1/20/2015.
 */
public class CottonDeduction {
    @SerializedName("deliveries")
    private int deliveries;
    @SerializedName("fid")
    private int farmerId;
    @SerializedName("deductions")
    private float deductions;
    @SerializedName("season_id")
    private int seasonId;
    @SerializedName("season_name")
    private String seasonName;

    public CottonDeduction(int deliveries, int farmerId, float deductions, int seasonId, String seasonName) {
        this.deliveries = deliveries;
        this.farmerId = farmerId;
        this.deductions = deductions;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }

    public CottonDeduction() {

    }

    public int getDeliveries() {
        return deliveries;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setDeliveries(int deliveries) {
        this.deliveries = deliveries;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public void setDeductions(float ballWeight) {
        this.deductions = ballWeight;
    }

    public float getDeductions(){
        return deductions;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
