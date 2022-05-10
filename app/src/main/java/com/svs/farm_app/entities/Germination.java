package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Germination implements Serializable{

	@SerializedName("farm_id")
	private String farmId;
	@SerializedName("germ_date")
	private String germinationDate;
	private transient String collectDate;
	@SerializedName("user_id")
	private String userId;
	@SerializedName("remarks")
	private String remarks;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	@SerializedName("company_id")
	private String companyId;

	public Germination(String farmId, String germinationDate,
	                   String collectDate, String remarks, String userId,
	                   String latitude, String longitude, String companyId) {
		this.farmId = farmId;
		this.remarks = remarks;
		this.germinationDate = germinationDate;
		this.collectDate = collectDate;
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.companyId = companyId;

	}

	public Germination() {

	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String _remarks) {
		this.remarks = _remarks;
	}

	public String getFarmId() {
		return farmId;
	}

	public String getGerminationDate() {
		return germinationDate;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public String getUserId() {
		return userId;
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

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public void setGerminationDate(String germinationDate) {
		this.germinationDate = germinationDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongt(String longitude) {
		this.longitude = longitude;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
