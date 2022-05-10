package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Collected orders per season
 * Created by wamae on 12/12/2017.
 */
public class CollectedOrder {
    @SerializedName("orders")
    private int orders;
    @SerializedName("fid")
    private int farmerId;
    @SerializedName("total")
    private float totalAmount;
    @SerializedName("season_id")
    private int seasonId;
    @SerializedName("season_name")
    private String seasonName;

    public CollectedOrder(int orders, int farmerId, float totalAmount, int seasonId, String seasonName) {
        this.orders = orders;
        this.farmerId = farmerId;
        this.totalAmount = totalAmount;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }

    public CollectedOrder() {

    }

    public int getOrders() {
        return orders;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setOrders(int orders) {
        this.orders = orders;
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
