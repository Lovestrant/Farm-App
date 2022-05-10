package com.svs.farm_app.asynctask.downloads;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.DatabaseHandler;

import java.util.List;

public class DownloadJustRegisteredFarmer extends Request<String> {
    protected static final String TAG = DownloadJustRegisteredFarmer.class.getSimpleName();
    private DatabaseHandler db;
    private Context mContext;
    private OnFarmerFetched onFarmerFetched;

    public DownloadJustRegisteredFarmer(int method, String url, DatabaseHandler db, Context ctx, OnFarmerFetched onFarmerFetched, ErrorListener listener) {
        super(method, url, listener);
        this.db = db;
        this.mContext = ctx;
        this.onFarmerFetched = onFarmerFetched;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        db.clearTable(db.TABLE_REGISTERED_FARMERS);
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.i(TAG, json);
            addData(json);
            return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        DashBoardActivity.downloadCount++;
    }

    /**
     * @param response
     */
    private void addData(String response) {

        try {
            Gson gson = new GsonBuilder().create();

            List<RegisteredFarmer> list = gson.fromJson(response, new TypeToken<List<RegisteredFarmer>>() {
            }.getType());

            if (!list.isEmpty()) {
                RegisteredFarmer farmer = list.get(list.size() - 1);
                onFarmerFetched.farmerFetched(farmer);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public interface OnFarmerFetched {
        void farmerFetched(RegisteredFarmer farmer);
    }

}
