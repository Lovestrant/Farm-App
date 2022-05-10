package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FarmIncome implements Serializable {
	@SerializedName("farm_id")
	private String farmId;
	@SerializedName("grade_a")
	private String gradeA;
	@SerializedName("grade_b")
	private String gradeB;
	@SerializedName("grade_c")
	private String gradeC;
	@SerializedName("delivery_date")
	private String deliveryDate;
	private transient String collectDate;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	@SerializedName("user_id")
	private String userId;
	@SerializedName("company_id")
	private String companyId;
	@SerializedName("delivery_count")
	private String deliveryCount;

	public FarmIncome(String farmId, String deliveryCount, String gradeA, String gradeB,
	                  String gradeC, String deliveryDate, String collectDate, String latitude,
	                  String longitude, String userId, String companyId) {
		this.farmId = farmId;
		this.deliveryCount = deliveryCount;
		this.gradeA = gradeA;
		this.gradeB = gradeB;
		this.gradeC = gradeC;
		this.deliveryDate = deliveryDate;
		this.collectDate = collectDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.userId = userId;
		this.companyId = companyId;
	}

	public FarmIncome() {
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}
	
	public String getDeliveryCount() {
		return deliveryCount;
	}

	public void setDeliveryCount(String deliveryCount) {
		this.deliveryCount = deliveryCount;
	}

	public String getGradeA() {
		return gradeA;
	}

	public void setGradeA(String gradeA) {
		this.gradeA = gradeA;
	}

	public String getGradeB() {
		return gradeB;
	}

	public void setGradeB(String gradeB) {
		this.gradeB = gradeB;
	}

	public String getGradeC() {
		return gradeC;
	}

	public void setGradeC(String gradeC) {
		this.gradeC = gradeC;
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

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
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