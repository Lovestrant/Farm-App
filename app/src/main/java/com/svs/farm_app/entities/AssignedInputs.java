package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 1/21/2015.
 */
public class AssignedInputs {
    @SerializedName("input_id")
    private String inputId;
    @SerializedName("input_type")
    private String inputType;
    @SerializedName("card_no")
    private String cardNo;
    @SerializedName("order_num")
    private String orderNum;
    @SerializedName("fid")
    private String farmerId;
    @SerializedName("input_total")
    private String inputTotal;
    @SerializedName("assfinputs_id")
    private String assFInputsId;
//    @SerializedName("fid")
//    private String inputUnit;
    @SerializedName("input_quantity")
	private String InputQuantity;
    @SerializedName("order_id")
	private String orderId;

    public AssignedInputs(String orderId, String assFInputsId, String inputId, String inputTotal, String InputQuantity, String inputIype, String farmerId, String cardNo, String orderNum) {
        this.orderId = orderId;
    	this.assFInputsId = assFInputsId;
        this.inputId = inputId;
        this.inputType = inputIype;
        this.InputQuantity = InputQuantity;
        this.inputTotal = inputTotal;
        this.farmerId = farmerId;
        this.cardNo = cardNo;
        this.orderNum = orderNum;
    }

    public AssignedInputs() {

    }

    public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getInputQuantity() {
		return InputQuantity;
	}

	public void setInputQuantity(String inputQuantity) {
		InputQuantity = inputQuantity;
	}

	public String getInputId() {
        return inputId;
    }

    public String getInputType() {
        return inputType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getInputTotal() {
        return inputTotal;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setInputTotal(String inputTotal) {
        this.inputTotal = inputTotal;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setAssInputId(String assfInputsId) {
        this.assFInputsId = assfInputsId;
    }

    public String getInputAssInputId() {
        return assFInputsId;
    }

//    public String getInputUnit() {
//        return inputUnit;
//    }
//
//    public void setInputUnit(String inputUnit) {
//        this.inputUnit = inputUnit;
//    }
}
