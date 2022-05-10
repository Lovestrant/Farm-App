package com.svs.farm_app.entities;

/**
 * Created by user on 1/20/2015.
 */
public class Countries {
    private String countryId;
    private String countryName;

    public Countries(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public Countries() {

    }

    public String getCountryID() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryID(String countryID) {
        this.countryId = countryID;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
