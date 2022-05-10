package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

public class Pesticides {
    @SerializedName("input_id")
    public String inputID;
    @SerializedName("input_type")
    public String inputType;

    public Pesticides(String inputId, String inputType) {
        this.inputID = inputId;
        this.inputType = inputType;
    }

    public Pesticides() {
    }

    public String getInputID() {
        return inputID;
    }

    public void setInputID(String inputID) {
        this.inputID = inputID;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

}
