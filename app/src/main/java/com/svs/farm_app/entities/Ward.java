package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 1/20/2015.
 */
public class Ward extends Object{
    @SerializedName("ward_id")
    private String wardID;
    @SerializedName("ward_name")
    private String wardName;
    @SerializedName("boll_weight")
	private String bollWeight;
	
    public Ward(String wardId, String wardName, String bollWeight) {
        this.wardID = wardId;
        this.wardName = wardName;
        this.bollWeight = bollWeight;
    }

    public Ward() {

    }

    public String getWardID() {
        return wardID;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

	public void setBollWeight(String ballWeight) {
		this.bollWeight = ballWeight;
	}
	
	public String getBollWeight(){
		return bollWeight;
	}
}
