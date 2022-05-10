package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 1/20/2015.
 */
public class Village {
    @SerializedName("ward_id")
    private String wardID;
    @SerializedName("village_id")
    private String villageID;
    @SerializedName("village_name")
    private String villageName;

    public Village(String _village_id, String _ward_id, String _village_name) {
        this.villageID = _village_id;
        this.wardID = _ward_id;
        this.villageName = _village_name;
    }

    public Village() {
        
    }

    public String getVillageID() {
        return villageID;
    }
    public String getWardID() {
        return wardID;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageID(String villageID) {
        this.villageID = villageID;
    }
    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
