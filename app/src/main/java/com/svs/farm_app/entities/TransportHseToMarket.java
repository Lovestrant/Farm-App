package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransportHseToMarket implements Serializable{

    @SerializedName("farm_id")
    private String farmId;
    @SerializedName("trans_date")
    private String deliveryDate;
    @SerializedName("collect_date")
    private transient String collectDate;
    @SerializedName("money_out")
    private String moneyOut;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("transport_count")
    private String transportCount;

    public TransportHseToMarket(String farmId, String transportCount, String deliveryDate, String collectDate,
                                String moneyOut, String latitude, String longitude,
                                String userId, String companyId) {
        this.farmId = farmId;
        this.transportCount = transportCount;
        this.deliveryDate = deliveryDate;
        this.collectDate = collectDate;
        this.moneyOut = moneyOut;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.companyId = companyId;
    }

    public TransportHseToMarket() {
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getTransportCount() {
        return transportCount;
    }

    public void setTransportCount(String transportCount) {
        this.transportCount = transportCount;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public String getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(String moneyOut) {
        this.moneyOut = moneyOut;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
