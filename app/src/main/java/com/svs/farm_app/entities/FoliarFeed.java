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
public class FoliarFeed {
    @SerializedName("input_id")
    private String inputID;
    @SerializedName("input_type")
    private String inputType;

    public FoliarFeed(String _input_id, String _input_type) {
        this.inputID = _input_id;
        this.inputType = _input_type;
    }

    public FoliarFeed() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getInputID() {
        return inputID;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputID(String _input_id) {
        this.inputID = _input_id;
    }

    public void setInputType(String _input_type) {
        this.inputType = _input_type;
    }

}
