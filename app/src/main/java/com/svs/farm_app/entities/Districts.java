package com.svs.farm_app.entities;

/**
 * Created by user on 1/20/2015.
 */
public class Districts {
    private String regionId;
    private String districtId;
    private String districtName;

    public Districts(String districtId,String regionId, String districtName) {
        this.districtId = districtId;
        this.regionId = regionId;
        this.districtName = districtName;
    }

    public Districts() {

    }

    public String getDistrictID() {
        return districtId;
    }
    public String getRegionID() {
        return regionId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictID(String districtId) {
        this.districtId = districtId;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
