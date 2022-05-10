/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Benson
 */
public class Herbicides {
    @SerializedName("input_id")
    private String inputID;
    @SerializedName("input_type")
    private String inputType;

    public Herbicides(String inputID, String inputType) {
       this.inputID = inputID;
       this.inputType = inputType;
    }

    public Herbicides() {

    }

    public String getInputID() {
        return inputID;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputID(String inputID) {
        this.inputID = inputID;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

}
