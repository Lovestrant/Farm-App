package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 1/20/2015.
 */
public class SubVillage {
    @SerializedName("subvillage_id")
    private String subVillageID;
    @SerializedName("village_id")
    private String villageID;
    @SerializedName("subvillage_name")
    private String subvillageName;

    public SubVillage(String _subvillage_id, String _village_id, String _subvillage_name) {
        this.subVillageID =_subvillage_id;
        this.villageID = _village_id;
        this.subvillageName = _subvillage_name;
    }

    public SubVillage() {

    }

    public String getSubVillageID() {
        return subVillageID;
    }
    public String getVillageID() {
        return villageID;
    }

    public String getSubVillageName() {
        return subvillageName;
    }

    public void setSubVillageID(String subVillageID) {
        this.subVillageID = subVillageID;
    }
    public void setVillageID(String villageID) {
        this.villageID = villageID;
    }

    public void setSubVillageName(String subVillageName) {
        this.subvillageName = subVillageName;
    }
}
