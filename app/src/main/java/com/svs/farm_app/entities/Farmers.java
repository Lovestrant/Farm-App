package com.svs.farm_app.entities;

/**
 * Created by user on 12/29/2014.
 */

public class Farmers {

	private String companyID;
	String latitude, longitude;

	int _id;
	String IDNO;
	String fName,mName, lName, gender;
	String phoneNumber;
	String email, postAddress, villageId, countryId, districtId, farmerPhotoPath;
	private String wardId;
	private String subVillageId;
	private String estimatedFarmArea;
	private String estimatedFarmAreaTwo;
	private String estimatedFarmAreaThree;
	private String estimatedFarmAreaFour;
	private String showIntent, farmVidOne, farmVidTwo, farmVidThree, farmVidFour;
	private String leftThumb, rightThumb;
	private String otherCropsOne;
	private String otherCropsTwo;
	private String otherCropsThree;
	private String userID;
	private String contractNo;

	// Empty constructor
	public Farmers() {

	}

	// constructor
	public Farmers(String fName,String mName, String lName, String gender, String IDNO, String phoneNumber, String email,
			String postAddress, String villageId, String subVillageId, String farmerPhotoPath,String leftThumb,String rightThumb, String latitude, String longitude,
			String showIntent, String estimatedFarmArea, String farmVidOne, String estfarmarea2, String farmVidTwo,
			String estfarmarea3, String farmVidThree, String estfarmarea4, String farmVidFour,String otherCropsOne,String otherCropsTwo,String otherCropsThree,
			String companyID,String userID,String contractNo) {
		this.IDNO = IDNO;
		this.fName = fName;
		this.mName = mName;
		this.lName = lName;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.postAddress = postAddress;
		this.villageId = villageId;
		this.subVillageId = subVillageId;
		this.farmerPhotoPath = farmerPhotoPath;
		this.leftThumb = leftThumb;
		this.rightThumb = rightThumb;
		this.latitude = latitude;
		this.longitude = longitude;
		this.showIntent = showIntent;
		this.estimatedFarmArea = estimatedFarmArea;
		this.estimatedFarmAreaTwo = estfarmarea2;
		this.estimatedFarmAreaThree = estfarmarea3;
		this.estimatedFarmAreaFour = estfarmarea4;
		this.farmVidOne = farmVidOne;
		this.farmVidTwo = farmVidTwo;
		this.farmVidThree = farmVidThree;
		this.farmVidFour = farmVidFour;
		this.otherCropsOne = otherCropsOne;
		this.otherCropsTwo = otherCropsTwo;
		this.otherCropsThree = otherCropsThree;
		this.companyID = companyID;
		this.userID = userID;
		this.contractNo=contractNo;
	}

	public String getShowIntent() {
		return showIntent;
	}

	public void setShowIntent(String showIntent) {
		this.showIntent = showIntent;
	}

	// getting ID
	public int getRowID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public int getID() {
		return _id;
	}

	public String getIDNumber() {
		return this.IDNO;
	}

	public void setIDNO(String id_no) {
		this.IDNO = id_no;
	}

	// getting fname
	public String getFName() {
		return this.fName;
	}
	// getting fname


	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	// setting fname
	public void setFName(String name) {
		this.fName = name;
	}

	// getting lname
	public String getLName() {
		return this.lName;
	}

	// setting lname
	public void setLName(String name) {
		this.lName = name;
	}

	// getting gender
	public String getGender() {
		return this.gender;
	}

	// setting gender
	public void setGender(String gender) {
		this.gender = gender;
	}

	// getting phone number
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	// setting phone number
	public void setPhoneNumber(String phone_number) {
		this.phoneNumber = phone_number;
	}

	public String getEmail() {
		return this.email;
	}

	// setting email
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostAddress() {
		return this.postAddress;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	// setting postAddress
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	public String getVillageID() {
		return this.villageId;
	}

	// setting villageId
	public void setVillageID(String villageId) {
		this.villageId = villageId;
	}

	// getting countryId
	public String getCountryId() {
		return this.countryId;
	}

	// setting countryId
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getDistrictId() {
		return this.districtId;
	}

	// setting districtId
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getFarmerPicPath() {
		return this.farmerPhotoPath;
	}

	// setting farmer pic path
	public void setFarmerPhotoPath(String farmerPhotoPath) {
		this.farmerPhotoPath = farmerPhotoPath;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public void setSubVillage(String subVillageId) {
		this.subVillageId = subVillageId;
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

	public void setCompanyID(String companyId) {
		this.companyID = companyId;
	}

	public String getCompanyID() {
		return companyID;
	}

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getWardId() {
		return wardId;
	}

	public String getSubVillageID() {
		return subVillageId;
	}

	public String getEstimatedFarmArea() {
		return estimatedFarmArea;
	}

	public void setEstimatedFarmArea(String estimatedFarmArea) {
		this.estimatedFarmArea = estimatedFarmArea;
	}

	public String getEstimatedFarmAreaTwo() {
		return estimatedFarmAreaTwo;
	}

	public void setEstimatedFarmAreaTwo(String estimatedFarmAreaTwo) {
		this.estimatedFarmAreaTwo = estimatedFarmAreaTwo;
	}

	public String getEstimatedFarmAreaThree() {
		return estimatedFarmAreaThree;
	}

	public void setEstimatedFarmAreaThree(String estimatedFarmAreaThree) {
		this.estimatedFarmAreaThree = estimatedFarmAreaThree;
	}

	public String getEstimatedFarmAreaFour() {
		return estimatedFarmAreaFour;
	}

	public String getFarmVidOne() {
		return farmVidOne;
	}

	public void setFarmVidOne(String farmVidOne) {
		this.farmVidOne = farmVidOne;
	}

	public String getFarmVidTwo() {
		return farmVidTwo;
	}

	public void setFarmVidTwo(String farmVidTwo) {
		this.farmVidTwo = farmVidTwo;
	}

	public String getFarmVidThree() {
		return farmVidThree;
	}

	public void setFarmVidThree(String farmVidThree) {
		this.farmVidThree = farmVidThree;
	}

	public String getFarmVidFour() {
		return farmVidFour;
	}

	public void setFarmVidFour(String farmVidFour) {
		this.farmVidFour = farmVidFour;
	}

	public void setEstimatedFarmAreaFour(String estimatedFarmAreaFour) {
		this.estimatedFarmAreaFour = estimatedFarmAreaFour;
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

	public String getOtherCropsOne() {
		return otherCropsOne;
	}

	public void setOtherCropsOne(String otherCropsOne) {
		this.otherCropsOne = otherCropsOne;
	}

	public String getOtherCropsTwo() {
		return otherCropsTwo;
	}

	public void setOtherCropsTwo(String otherCropsTwo) {
		this.otherCropsTwo = otherCropsTwo;
	}

	public String getOtherCropsThree() {
		return otherCropsThree;
	}

	public void setOtherCropsThree(String otherCropsThree) {
		this.otherCropsThree = otherCropsThree;
	}
	

}
