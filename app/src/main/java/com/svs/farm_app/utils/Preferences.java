package com.svs.farm_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 21/07/16.
 */
public class Preferences {
    private static SharedPreferences settings ;
    private static SharedPreferences.Editor editor;

    public static final String PREFERNCE_NAME = "farmappsvs";

    public static  String USER_ID = "";
    public static  String ReturnStatus = "";
    public static  String USERNAME = "";
    public static  String PASSWORD = "";
    public static  String COMPANY_ID = "";

    public static  String DEVICE_MODEL = "";

    public  static  void loadPreferenceSettings(Context context){
        final  SharedPreferences settings = context.getSharedPreferences(PREFERNCE_NAME,0);

        USER_ID = settings.getString("USER_ID", USER_ID);
        ReturnStatus = settings.getString("ReturnStatus", ReturnStatus);
        USERNAME = settings.getString("USERNAME", USERNAME);
        PASSWORD = settings.getString("PASSWORD", PASSWORD);
        COMPANY_ID = settings.getString("COMPANY_ID", COMPANY_ID);
    }

    public static void savePrefenceSettings(Context context){
        settings = context.getSharedPreferences(PREFERNCE_NAME,0);
        editor = settings.edit();
        editor.putString("USER_ID", USER_ID);
        editor.putString("ReturnStatus", ReturnStatus);
        editor.putString("USERNAME", USERNAME);
        editor.putString("PASSWORD", PASSWORD);
        editor.putString("COMPANY_ID", COMPANY_ID);
        editor.commit();
    }
}
