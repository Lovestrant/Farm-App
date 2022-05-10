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
public class OtherCrops {
    @SerializedName("crop_id")
    private String cropID;
    @SerializedName("crop_name")
    private String cropName;

    public OtherCrops(){
    }

    public OtherCrops(String _crop_id, String _crop_name) {
       this.cropID = _crop_id;
       this.cropName = _crop_name;
    }
    
    public String getCropID() {
       return cropID;
    }

    public String getCropName() {
       return cropName;
    }

    public void setCropID(String _crop_id) {
       this.cropID = _crop_id;
    }

    public void setCropName(String _crop_name) {
       this.cropName = _crop_name;
    }

    
    
}
