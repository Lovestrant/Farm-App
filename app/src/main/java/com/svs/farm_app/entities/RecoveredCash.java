package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Collected cash per season
 * Created by wamae on 12/12/2017.
 */
public class RecoveredCash {
    @SerializedName("times")
    private int times;
    @SerializedName("fid")
    private int farmerId;
    @SerializedName("total")
    private float totalAmount;
    @SerializedName("season_id")
    private int seasonId;
    @SerializedName("season_name")
    private String seasonName;

    public RecoveredCash(int times, int farmerId, float totalAmount, int seasonId, String seasonName) {
        this.times = times;
        this.farmerId = farmerId;
        this.totalAmount = totalAmount;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }

    public RecoveredCash() {

    }

    public int getTimes() {
        return times;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public void setTotalAmount(float ballWeight) {
        this.totalAmount = ballWeight;
    }

    public float getTotalAmount(){
        return totalAmount;
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
