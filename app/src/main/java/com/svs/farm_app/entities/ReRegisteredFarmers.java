package com.svs.farm_app.entities;

import java.io.Serializable;

public class ReRegisteredFarmers implements Serializable{

    private String companyId = null;
    String farmerId;
	String genId;

	String farmerPic;
	String leftThumb;
	String rightThumb;
	private String userId;
	private String _id;

	public ReRegisteredFarmers(String companyId, String userId, String farmerId, String genId, String farmerPic, String leftThumb, String rightThumb) {
        this.companyId = companyId;
		this.userId = userId;
		this.farmerId = farmerId;
		this.genId = genId;
		this.farmerPic = farmerPic;
		this.leftThumb = leftThumb;
		this.rightThumb = rightThumb;
	}

	public ReRegisteredFarmers() {
	}

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getGenId() {
		return genId;
	}

	public void setGenId(String genId) {
		this.genId = genId;
	}

	public String getFarmerPic() {
		return farmerPic;
	}

	public void setFarmerPic(String farmerPic) {
		this.farmerPic = farmerPic;
	}

	public String getLeftThumb() {
		return leftThumb;
	}

	public void setLeftThumb(String leftThumb) {
		this.leftThumb = leftThumb;
	}

	public String getRightThumb() {
		return rightThumb;
	}

	public void setRightThumb(String rightThumb) {
		this.rightThumb = rightThumb;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public String getRowId() {
		return _id;
	}

	public void setRowId(String _id) {
		this._id = _id;
	}

}
