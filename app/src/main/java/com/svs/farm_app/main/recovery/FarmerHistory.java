package com.svs.farm_app.main.recovery;

/**
 * Created by Wamae on 12-Dec-17.
 */


public class FarmerHistory{
    int farmerId;
    float totalAmount = 0;
    float totalPaid = 0;
    int seasonId;
    String seasonName;

    public FarmerHistory(int farmerId, float totalAmount, float totalPaid, int seasonId, String seasonName) {
        this.farmerId = farmerId;
        this.totalAmount = totalAmount;
        this.totalPaid = totalPaid;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }

    public FarmerHistory(int farmerId, float totalAmount, int seasonId, String seasonName) {
        this.farmerId = farmerId;
        this.totalAmount = totalAmount;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }
}

