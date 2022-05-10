package com.svs.farm_app.asynctask.downloads;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.svs.farm_app.entities.FarmDataColleted;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DownloadDataCollected extends Request<String> {
	private static final String TAG = DownloadDataCollected.class.getSimpleName();
	private int method;
	private String url;
	private ErrorListener mListener;
	private DatabaseHandler db;
	private Context mContext;

	public DownloadDataCollected(int method, String url, DatabaseHandler db, Context ctx, ErrorListener listener) {
		super(method, url, listener);
		this.method = method;
		this.url = url;
		this.db = db;
		this.mContext = ctx;
		mListener = listener;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		db.clearTable(db.TABLE_FARM_DATA_COLLECTED);
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
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

			JSONArray JA = new JSONArray(response);
			JSONObject json;
			String[] farm_id = new String[JA.length()];
			String[] form_type_id = new String[JA.length()];

			for (int i = 0; i < JA.length(); i++) {
				json = JA.getJSONObject(i);
				farm_id[i] = json.getString("farm_id");
				form_type_id[i] = json.getString("form_type_id");

			}
			for (int i = 0; i < JA.length(); i++) {
				db.addFarmDataCollected(new FarmDataColleted(farm_id[i], form_type_id[i]));
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
