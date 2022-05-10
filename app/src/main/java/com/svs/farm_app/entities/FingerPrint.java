package com.svs.farm_app.entities;

public class FingerPrint {
	private String farmerID,genID, filePath, companyID;

	public FingerPrint(String farmerID, String genID, String filePath, String companyID) {
		this.farmerID = farmerID;
		this.genID = genID;
		this.filePath = filePath;
		this.companyID = companyID;
	}

	public FingerPrint() {
	}

	/**
	 * @return the farmerId
	 */
	public String getFarmerID() {
		return farmerID;
	}

	/**
	 * @param farmerID
	 *            the farmerId to set
	 */
	public void setFarmerID(String farmerID) {
		this.farmerID = farmerID;
	}

	/**
	 * @return the genID
	 */
	public String getGenID() {
		return genID;
	}

	/**
	 * @paramgenID
	 *            the genID to set
	 */
	public void setGenID(String genID) {
		this.genID = genID;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the companyID
	 */
	public String getCompanyID() {
		return companyID;
	}

	/**
	 * @param companyID
	 *            the companyID to set
	 */
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

}
