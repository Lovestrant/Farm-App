package com.svs.farm_app.entities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.svs.farm_app.utils.CardUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class RegisteredFarmer implements Serializable {
    @SerializedName("fid")
    private String farmerId;
    @SerializedName("gen_id")
    private String genId;
    @SerializedName("card_no")
    private String cardNo;
    @SerializedName("fname")
    private String firstName;
    @SerializedName("middle_name")
    private String middleName;
    @SerializedName("lname")
    private String lastName;
    @SerializedName("is_contracted")
    private String contractStatus;
    @SerializedName("on_credit")
    private String creditStatus;
    @SerializedName("phone")
    private String phone;
    @SerializedName("gender")
    private String gender;
    @SerializedName("contract_number")
    private String contract_number;
    @SerializedName("village_id")
    private int villageId;
    @SerializedName("company_id")
    private String companyId;

    public RegisteredFarmer(String farmerId,String middleName, String genId, String cardNo, String firstName, String lastName, String contractStatus,
                            String creditStatus, int villageId, String companyID) {
        this.farmerId = farmerId;
        this.genId = genId;
        this.cardNo = cardNo;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contractStatus = contractStatus;
        this.creditStatus = creditStatus;
        this.villageId = villageId;
        this.companyId = companyID;
    }

    public RegisteredFarmer() {
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String fId) {
        this.farmerId = fId;
    }

    public String getGenId() {
        return genId;
    }

    public void setGenId(String genId) {
        this.genId = genId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    @Override
    public String toString() {
        String readableCardNo = null;
        if (this.cardNo != null) {
            readableCardNo = CardUtils.hyphenate(this.cardNo);
        } else {
            readableCardNo = this.cardNo;
        }


        return "Name: " + this.firstName + " " + this.lastName + " \nCard No: " + readableCardNo;
    }


    public String toStringx() {

        String readableCardNo = null;
        if (this.cardNo != null) {
            readableCardNo = CardUtils.hyphenate(this.cardNo);
        } else {
            readableCardNo = this.cardNo;
        }
        return "RegisteredFarmer{" +
                "farmerId='" + farmerId + '\'' +
                ", genId='" + genId + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contractStatus='" + contractStatus + '\'' +
                ", creditStatus='" + creditStatus + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + ((gender == null) ? "" : gender) + '\'' +
                ", villageId=" + villageId +
                ", companyId='" + companyId + '\'' +
                '}';
    }

    public static JSONObject toJsonStringForUpdate(List<RegisteredFarmer> registeredFarmers) {
        JSONObject o1 = new JSONObject();
        JSONArray a = new JSONArray();
        for (RegisteredFarmer r : registeredFarmers
        ) {

            JSONObject o = null;
            try {
                o = new JSONObject(
                        "{" +
                                "fid='" + r.farmerId + '\'' +
                                ", fname='" + r.firstName + '\'' +
                                ", lname='" + r.lastName + '\'' +
                                ", gender='" + ((r.gender == null) ? "M" : r.gender) + '\'' +
                                ", phone='" + r.phone + '\'' +
                                ", contract_number='" + r.contract_number + '\'' +
                                '}');
            } catch (JSONException e) {
                Log.e(TAG, "toJsonStringForUpdate: " + e.getMessage());
                e.printStackTrace();
                o = new JSONObject();
            }
            a.put(o);
        }
        try {
            o1.put("farmers", a);
        } catch (JSONException e) {
            Log.e(TAG, "toJsonStringForUpdate1: " + e.getMessage());
            e.printStackTrace();
        }
        Log.e(TAG, "toJsonStringForUpdate: " + new Gson().toJson(o1));
        return o1;
    }

    private static final String TAG = "RegisteredFarmer";

    public String toSearchable() {
        return farmerId + ' ' +
                genId + ' ' +
                cardNo + ' ' +
                firstName + ' ' +
                lastName + ' ' +
                contractStatus + ' ' +
                contract_number + ' ' +
                creditStatus + ' ' +
                villageId +
                companyId + ' ';
    }
}
