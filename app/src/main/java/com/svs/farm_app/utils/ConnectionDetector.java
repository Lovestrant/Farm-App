package com.svs.farm_app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by user on 12/27/2014.
 */
public class ConnectionDetector {

    private Context context;
    private boolean isReachable = false;
    private String TAG = ConnectionDetector.class.getSimpleName();

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            try {
                Log.e(TAG, Config.BASE_URL);
                URL myUrl = new URL(Config.BASE_URL);
                HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();
                connection.setConnectTimeout(3000);
                connection.connect();

                isReachable = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                isReachable = false;
            }
        }

        Log.i(TAG, String.valueOf(isReachable));
        return isReachable;

    }
}
