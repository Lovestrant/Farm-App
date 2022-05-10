package com.svs.farm_app.entities;

public class ManualInputFingerprintRecapture {
	String fId,
	genId,
	leftThumb,
	rightThumb;

	public ManualInputFingerprintRecapture(String fId, String genId, String leftThumb, String rightThumb) {
		this.fId = fId;
		this.genId = genId;
		this.leftThumb = leftThumb;
		this.rightThumb = rightThumb;
	}

	public ManualInputFingerprintRecapture() {
	}

	public String getfId() {
		return fId;
	}

	public void setFarmerID(String fId) {
		this.fId = fId;
	}

	public String getGenId() {
		return genId;
	}

	public void setGenID(String genId) {
		this.genId = genId;
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
	


}
