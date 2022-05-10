package com.svs.farm_app.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/2/2015.
 */
public class Users {
    @SerializedName("user_id")
    private String userID;
    @SerializedName("username")
    private String userName;
    @SerializedName("password")
    private String password;
    @SerializedName("company_id")
    private String companyID;

    public Users(String _user_id, String _user_name, String _user_pwd,String _co_id) {
        this.userID = _user_id;
        this.userName = _user_name;
        this.password = _user_pwd;
        this.companyID = _co_id;
    }

    public Users() {

    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPwd(String userPwd) {
        this.password = userPwd;
    }

    public void setCompanyID(String coID) {
        this.companyID = coID;
    }

}
