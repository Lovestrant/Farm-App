package com.svs.farm_app.entities;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Benson on 3/29/2015.
 */
public class MappedFarm implements Serializable {
    @SerializedName("updated_by")
    private String userId;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("company_id")
    private String companyId;
    @SerializedName("points")
    private String points;
    @SerializedName("actual_farm_area")
    private String actualFarmArea;
    @SerializedName("farm_peri")
    private String perimeter;
    @SerializedName("farm_id")
    private String farmId;

    public MappedFarm(String farmId, String latitude, String longitude, String points, String actualFarmArea, String perimeter, String userId, String companyId) {
        this.farmId = farmId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.points = points;
        this.actualFarmArea = actualFarmArea;
        this.perimeter = perimeter;
        this.userId = userId;
        this.companyId = companyId;
    }

    public MappedFarm() {

    }


    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getActualFarmArea() {
        return actualFarmArea;
    }

    public void setActualFarmArea(String actualFarmArea) {
        this.actualFarmArea = actualFarmArea;
    }

    public String getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(String perimeter) {
        this.perimeter = perimeter;
    }


    public String forUpload() {
        class FLoad {
            @SerializedName("updated_by")
            private String userId;
            @SerializedName("latitude")
            private String latitude;
            @SerializedName("longitude")
            private String longitude;
            @SerializedName("company_id")
            private String companyId;
            @SerializedName("points")
            private List<LatLng> points;
            @SerializedName("actual_farm_area")
            private String actualFarmArea;
            @SerializedName("farm_peri")
            private String perimeter;
            @SerializedName("farm_id")
            private String farmId;


            public FLoad(MappedFarm t) {
                this.farmId = t.farmId;
                this.latitude = t.latitude;
                this.longitude = t.longitude;
                this.points = (new Gson()).fromJson(t.points, new TypeToken<List<LatLng>>() {
                }.getType());
                this.actualFarmArea = t.actualFarmArea;
                this.perimeter = t.perimeter;
                this.userId = t.userId;
                this.companyId = t.companyId;
            }

            @Override
            public String toString() {
                return "{" +
                        "\"user_id\":\"" + userId + "\"" +
                        ", \"latitude\":\"" + latitude + "\"" +
                        ", \"longitude\":\"" + longitude + "\"" +
                        ", \"company_id\":\"" + companyId + "\"" +
                        ", \"points\":" + getArray(points) +
                        ", \"actual_farm_area\":\"" + actualFarmArea + "\"" +
                        ", \"farm_peri\":\"" + perimeter + "\"" +
                        ", \"farm_id\":\"" + farmId + "\"" +
                        "}";
            }

            public String getArray(List<LatLng> l) {
                StringBuilder re = new StringBuilder("[");
                for (LatLng c:l
                     ) {
                    re.append("{\"latitude\":\"").append(c.latitude).append("\",");
                    re.append("\"longitude\":\"").append(c.longitude).append("\"},");

                }

                re.append("]");
                return re.toString().replaceFirst(",]","]");
            }
        }
        String res = new Gson().toJson(new FLoad(this).toString());
        Log.e("MappedFarm", "forUpload: " + res);
        Log.e("MappedFarm", "forUpload: " + (new FLoad(this)));
        return (new FLoad(this)).toString();
    }
}
