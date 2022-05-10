package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

public class UpdateFarmArea {
	@SerializedName("farm_id")
	String farmID;
	@SerializedName("estimated_farm_area")
	String newArea;
	@SerializedName("updated_by")
	String userID;
	@SerializedName("company_id")
	String companyID;

	public UpdateFarmArea(String farmID, String newArea, String userID, String companyID) {
		this.farmID = farmID;
		this.newArea = newArea;
		this.userID = userID;
		this.companyID = companyID;
	}

	public UpdateFarmArea() {
	}

	public String getFarmID() {
		return farmID;
	}

	public void setFarmID(String farmID) {
		this.farmID = farmID;
	}

	public String getNewArea() {
		return newArea;
	}

	public void setNewArea(String newArea) {
		this.newArea = newArea;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
}
