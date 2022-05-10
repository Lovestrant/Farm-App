package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

public class CollectedInputs {
	@SerializedName("order_id")
	private String orderId;
	@SerializedName("collection_date")
	private String collectDate;
	@SerializedName("user_id")
	private String userID;
	@SerializedName("collection_method")
	private String collectionMethod;

	public CollectedInputs(String orderId, String collectDate, String userID,String method) {
		this.orderId = orderId;
		this.collectDate = collectDate;
		this.userID = userID;
		this.collectionMethod = method;
	}

	public CollectedInputs() {
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the collectDate
	 */
	public String getCollectDate() {
		return collectDate;
	}

	/**
	 * @param collectDate
	 *            the collectDate to set
	 */
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	/**
	 * @return the userID
	 */
	public String getUserId() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserId(String userID) {
		this.userID = userID;
	}

	public String getCollectionMethod() {
		return collectionMethod;
	}

	public void setCollectionMethod(String collectionMethod) {
		this.collectionMethod = collectionMethod;
	}

}
