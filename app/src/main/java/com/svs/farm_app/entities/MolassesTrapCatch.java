/**
 * 
 */
package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Benson
 *
 */
public class MolassesTrapCatch implements Serializable{

	@SerializedName("farm_id")
	private String farmId;
	@SerializedName("rec_date")
	private String activityDate;
	@SerializedName("trap_one")
	private String trapOne;
	@SerializedName("trap_two")
	private String trapTwo;
	@SerializedName("action_taken")
	private String action;
	@SerializedName("user_id")
	private String userId;
	@SerializedName("collect_date")
	private transient String collectDate;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	@SerializedName("company_id")
	private String companyId;

	/**
	 * 
	 */
	public MolassesTrapCatch() {

	}

	public MolassesTrapCatch(String farmId, String activityDate,
	                         String trapOne, String trapTwo, String action, String userId,
	                         String collectDate, String latitude, String longitude, String companyId) {
		this.farmId = farmId;
		this.activityDate = activityDate;
		this.trapOne = trapOne;
		this.trapTwo = trapTwo;
		this.action = action;
		this.userId = userId;
		this.collectDate = collectDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.companyId = companyId;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getTrapDate() {
		return activityDate;
	}

	public void setTrapDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getTrapOne() {
		return trapOne;
	}

	public void setTrapOne(String trapOne) {
		this.trapOne = trapOne;
	}

	public String getTrapTwo() {
		return trapTwo;
	}

	public void setTrapTwo(String trapTwo) {
		this.trapTwo = trapTwo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
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

}
