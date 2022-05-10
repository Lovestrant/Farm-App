package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WackFarmer implements Serializable{
	@SerializedName("fid")
	private String farmerID;
	@SerializedName("gen_id")
	private String genID;
	@SerializedName("wack_status")
	private String status;
	@SerializedName("fname")
	private String firstName;
	@SerializedName("lname")
	private String lastName;
	@SerializedName("village_id")
	private String villageId;

	public WackFarmer(String farmerId, String fName, String lName, String genId, String villageId, String status) {
		this.farmerID = farmerId;
		this.firstName = fName;
		this.lastName = lName;
		this.genID = genId;
		this.villageId = villageId;
		this.status = status;
	}

	public WackFarmer() {
	}

	public String getFarmerId() {
		return farmerID;
	}

	public void setFarmerID(String farmerId) {
		this.farmerID = farmerId;
	}

	public String getGenId() {
		return genID;
	}

	public void setGenID(String genID) {
		this.genID = genID;
	}

	public void setWackStatus(String status) {
		this.status= status;
		
	}

	public String getWackStatus() {
		return status;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getVillageId() {
		return villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
		
	}
	

}
