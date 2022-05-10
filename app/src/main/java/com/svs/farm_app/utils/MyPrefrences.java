package com.svs.farm_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefrences {
	public static void savePrefrence(Context ctx, String Key, String value) {
		ctx.getSharedPreferences("mypref", Context.MODE_PRIVATE).edit()
				.putString(Key, value).commit();
	}

	public static String getPrefrence(Context ctx, String key) {
		SharedPreferences pref = ctx.getSharedPreferences("mypref",
				Context.MODE_PRIVATE);
		String result = pref.getString(key, null);
		return result;
	}

	public static void removePrefrence(Context ctx, String key) {
		SharedPreferences preferences = ctx.getSharedPreferences("mypref", Context.MODE_PRIVATE);
		preferences.edit().remove(key).commit();
	}
	
}
