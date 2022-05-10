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
public class UserVillage {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("village_id")
    private String villageId;

    public UserVillage(String userId, String villageId) {
      this.userId = userId;
      this.villageId = villageId;
    }

    public UserVillage() {
       
    }

    public String getUserId() {
        return userId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public void setVillageId(String villageId) {
       this.villageId = villageId;
    }

}
