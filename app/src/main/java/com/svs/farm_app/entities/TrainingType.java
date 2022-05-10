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
public class TrainingType {
    @SerializedName("train_type_id")
    private String trainTypeID;
    @SerializedName("train_type")
    private String trainType;
    @SerializedName("train_cat_id")
    private String trainCatID;

    public TrainingType(String _train_cat_id,String _train_type_id, String _train_type) {
        this.trainCatID = _train_cat_id;
        this.trainTypeID = _train_type_id;
        this.trainType = _train_type;
    }

    public TrainingType() {
        
    }

    public void setTID(String _tid) {
        this.trainTypeID = _tid;
    }

    public void setTType(String _train_type) {
        this.trainType = _train_type;
    }

    public String getTID() {
        return trainTypeID;
    }

    public String getTType() {
        return trainType;
    }

    public String getTcatID() {
       return trainCatID;
    }

    public void setTrainCatID(String _train_cat_id) {
       this.trainCatID = _train_cat_id;
    }

}
