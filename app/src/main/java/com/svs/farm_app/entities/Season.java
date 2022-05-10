package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

public class Season {
    @SerializedName("season_id")
    public int seasonId;

    @SerializedName("season_name")
    public String seasonName;

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
