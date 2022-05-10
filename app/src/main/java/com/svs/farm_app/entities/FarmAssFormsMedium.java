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
public class FarmAssFormsMedium implements Serializable {

	@SerializedName("farm_id")
	private String farmId;
	@SerializedName("form_type_id")
	private String formTypeId;
	@SerializedName("activity_date")
	private String activityDate;
	@SerializedName("family_hours")
	private String familyHours;
	@SerializedName("hired_hours")
	private String hiredHours;
	private transient String collectDate;
	@SerializedName("money_out")
	private String moneyOut;
	@SerializedName("user_id")
	private String userID;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	@SerializedName("company_id")
	private String companyId;
	@SerializedName("activity_method")
	private String activityMethod;
	@SerializedName("input_id")
	private String inputId;
	@SerializedName("input_quantity")
	private String inputQuantity;
	@SerializedName("spray_method")
	private String sprayType;

	/**
	 * Constructor
	 * 
	 * @param farmId
	 * @param formTypeId
	 * @param activityMethod
	 * @param activityDate
	 * @param familyHours
	 * @param hiredHours
	 * @param moneyOut
	 * @param inputId
	 * @param inputQuantity
	 * @param sprayType
	 * @param userId
	 * @param collectDate
	 * @param latitude
	 * @param longitude
	 * @param companyId
	 */
	public FarmAssFormsMedium(String farmId, String formTypeId,
	                          String activityMethod, String activityDate,
	                          String familyHours, String hiredHours, String moneyOut,
	                          String inputId, String inputQuantity, String sprayType,
	                          String userId, String collectDate, String latitude, String longitude,
	                          String companyId) {
		this.farmId = farmId;
		this.formTypeId = formTypeId;
		this.activityMethod = activityMethod;
		this.activityDate = activityDate;
		this.familyHours = familyHours;
		this.hiredHours = hiredHours;
		this.collectDate = collectDate;
		this.moneyOut = moneyOut;
		this.inputId = inputId;
		this.inputQuantity = inputQuantity;
		this.sprayType = sprayType;
		this.userID = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.companyId = companyId;

	}

	public FarmAssFormsMedium() {
	}

	public String getFarmId() {
		return farmId;
	}

	public String getFormTypeId() {
		return formTypeId;
	}

	public String getActivityDate() {
		return activityDate;
	}

	public String getFamilyHours() {
		return familyHours;
	}

	public String getHiredHours() {
		return hiredHours;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public String getMoneyOut() {
		return moneyOut;
	}

	public String getUserId() {
		return userID;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public void setFormTypeId(String formTypeId) {
		this.formTypeId = formTypeId;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public void setFamilyHours(String familyHours) {
		this.familyHours = familyHours;
	}

	public void setHiredHours(String hiredHours) {
		this.hiredHours = hiredHours;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public void setMoneyOut(String moneyOut) {
		this.moneyOut = moneyOut;
	}

	public void setUserId(String userID) {
		this.userID = userID;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getActivityMethod() {
		return activityMethod;
	}

	public void setActivityMethodId(String activityMethod) {
		this.activityMethod = activityMethod;
	}

	public String getInputId() {
		return inputId;
	}

	public String getInputQuantity() {
		return inputQuantity;
	}

	public String getSprayType() {
		return sprayType;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	public void setInputQuantity(String inputQuantity) {
		this.inputQuantity = inputQuantity;
	}

	public void setSprayType(String sprayMethod) {
		this.sprayType = sprayMethod;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
		
}
