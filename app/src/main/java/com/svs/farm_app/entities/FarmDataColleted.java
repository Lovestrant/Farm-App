package com.svs.farm_app.entities;

public class FarmDataColleted {
	private String farmId,formTypeId;

	public FarmDataColleted(String farmId, String formTypeId) {
		this.farmId = farmId;
		this.formTypeId = formTypeId;
	}

	public FarmDataColleted() {
	}

	public String getFarmID() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getFormTypeID() {
		return formTypeId;
	}

	public void setFormTypeId(String formTypeId) {
		this.formTypeId = formTypeId;
	}
	

}
