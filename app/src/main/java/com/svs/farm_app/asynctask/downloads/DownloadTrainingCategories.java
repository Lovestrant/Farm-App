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
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DownloadTrainingCategories extends Request<String> {
	private static final String TAG = DownloadTrainingCategories.class.getSimpleName();
	private int method;
	private String url;
	private ErrorListener mListener;
	private DatabaseHandler db;
	private Context mContext;

	public DownloadTrainingCategories(int method, String url, DatabaseHandler db, Context ctx, ErrorListener listener) {
		super(method, url, listener);
		this.method = method;
		this.url = url;
		this.db = db;
		this.mContext = ctx;
		mListener = listener;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		DashBoardActivity.downloadCount++;
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new VolleyError(e));
		}
	}

	@Override
	protected void deliverResponse(String response) {
		db.clearTable(db.TABLE_TRAIN_CATEGORIES);
		try {

			JSONArray JA = new JSONArray(response);
			JSONObject json;
			String[] train_cat_id = new String[JA.length()];
			String[] ext_train_id = new String[JA.length()];
			String[] train_cat = new String[JA.length()];

			for (int i = 0; i < JA.length(); i++) {
				json = JA.getJSONObject(i);
				train_cat_id[i] = json.getString("train_cat_id");
				ext_train_id[i] = json.getString("ext_train_id");
				train_cat[i] = json.getString("train_cat");

			}

			for (int i = 0; i < JA.length(); i++) {
				//db.addTrainingCat(new AssignedTrainings(train_cat_id[i], ext_train_id[i], train_cat[i]));
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
