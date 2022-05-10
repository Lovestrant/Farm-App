package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class YieldEstimate implements Serializable{
	@SerializedName("farm_id")
	private String farmId;
	@SerializedName("num_of_plants")
	private String numOfPlants;
	@SerializedName("num_of_balls")
	private String numOfBalls;
	@SerializedName("dis_to_left")
	private String distanceToLeft;
	@SerializedName("dis_to_right")
	private String distanceToRight;
	private transient String collectDate;
	@SerializedName("user_id")
	private String userId;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	@SerializedName("company_id")
	private String companyId;
	@SerializedName("sampling_station")
	private String samplingStation;

	public YieldEstimate(String farmId, String samplingStation, String numOfPlants, String numOfBalls,
	                     String distanceToLeft, String distanceToRight, String collectDate, String userId,
	                     String latitude, String longitude, String companyId) {
		this.farmId = farmId;
		this.samplingStation = samplingStation;
		this.numOfPlants = numOfPlants;
		this.numOfBalls = numOfBalls;
		this.distanceToLeft = distanceToLeft;
		this.distanceToRight = distanceToRight;
		this.collectDate = collectDate;
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.companyId = companyId;
	}

	public YieldEstimate() {
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getNumOfPlants() {
		return numOfPlants;
	}

	public void setNumOfPlants(String numOfPlants) {
		this.numOfPlants = numOfPlants;
	}

	public String getNumOfBolls() {
		return numOfBalls;
	}

	public void setNumOfBolls(String numOfBalls) {
		this.numOfBalls = numOfBalls;
	}

	public String getDistanceToLeft() {
		return distanceToLeft;
	}

	public void setDistanceToLeft(String distanceToLeft) {
		this.distanceToLeft = distanceToLeft;
	}

	public String getDistanceToRight() {
		return distanceToRight;
	}

	public void setDistanceToRight(String distanceToRight) {
		this.distanceToRight = distanceToRight;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSamplingStation() {
		return samplingStation;
	}

	public void setSamplingStation(String samplingStation) {
		this.samplingStation = samplingStation;		
	}

}
