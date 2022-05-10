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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.svs.farm_app.entities.Village;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadVillages extends Request<String> {
	protected static final String TAG = DownloadVillages.class.getSimpleName();
	private int method;
	private String url;
	private ErrorListener mListener;
	private DatabaseHandler db;
	private Context mContext;

	public DownloadVillages(int method, String url, DatabaseHandler db, Context ctx, ErrorListener listener) {
		super(method, url, listener);
		this.method = method;
		this.url = url;
		this.db = db;
		this.mContext = ctx;
		mListener = listener;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		db.clearTable(db.TABLE_VILLAGES);
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.i(TAG,json);
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

			List<Village> list = gson.fromJson(response, new TypeToken<List<Village>>() {
			}.getType());

			for (int i = 0; i < list.size(); i++) {
				db.addVillages(list.get(i));
			}


		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
