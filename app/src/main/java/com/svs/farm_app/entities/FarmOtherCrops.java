/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * @author Benson
 */
public class FarmOtherCrops implements Serializable {
    @SerializedName("farm_id")
    private String farmId;
    @SerializedName("crop_id")
    private String cropIdOne;
    @SerializedName("crop_id1")
    private String cropIdTwo;
    @SerializedName("crop_id2")
    private String cropIdThree;
    @SerializedName("user_id")
    private String userId;
    private transient String collectDate;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("company_id")
    private String companyId;

    public FarmOtherCrops(String farmId, String cropIdOne, String cropIdTwo, String cropIdThree, String userId,
                          String collectDate, String latitude, String longitude, String companyId) {
		this.farmId = farmId;
		this.cropIdOne = cropIdOne;
		this.cropIdTwo = cropIdTwo;
		this.cropIdThree = cropIdThree;
        this.userId = userId;
        this.collectDate = collectDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.companyId = companyId;

	}

	public FarmOtherCrops() {

	}

	public String getFarmID() {
		return farmId;
	}

	public String getCropIdOne() {
		return cropIdOne;
	}

	public String getCropIdTwo() {
		return cropIdTwo;
	}

	public String getCropIdThree() {
		return cropIdThree;
	}

    public String getUserId() {
        return userId;
    }

	public String getCollectDate() {
		return collectDate;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setFarmID(String farmId) {
		this.farmId = farmId;
	}

	public void setCropIdOne(String cropIdOne) {
		this.cropIdOne = cropIdOne;
	}

	public void setCropIdTwo(String cropIdTwo) {
		this.cropIdTwo = cropIdTwo;
	}

	public void setCropIdThree(String cropIdThree) {
		this.cropIdThree = cropIdThree;
	}

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
