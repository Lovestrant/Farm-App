package com.svs.farm_app.asynctask.downloads;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.authentication.utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.svs.farm_app.entities.UserVillage;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadUserVillages extends Request<String> {

	private static final String TAG = DownloadUserVillages.class.getSimpleName();
	private int method;
	private String url;
	private ErrorListener mListener;
	private DatabaseHandler db;
	private Context mContext;

	public DownloadUserVillages(int method, String url, DatabaseHandler db,Context ctx, ErrorListener listener) {
		super(method, url, listener);
		this.method = method;
		this.url = url;
		this.db = db;
		this.mContext = ctx;
		mListener = listener;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		db.clearTable(db.TABLE_USER_VILLAGE);
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

		List<UserVillage> usersVillages = db.getVillageIdByUserId(Preferences.USER_ID);
		String CSVUserVillageIds = db.userVillageIdsToCsv(usersVillages,",");
		DownloadAssignedInputs assignedInputs = new DownloadAssignedInputs(Request.Method.GET, Config.DOWNLOAD_ASSIGNED_INPUTS +"?village_ids="+CSVUserVillageIds+"&company_id="+Preferences.COMPANY_ID, db,mContext, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(DownloadAssignedInputs.TAG, ""+error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		DownloadRegisteredFarmers registeredFarmers = new DownloadRegisteredFarmers(Request.Method.GET, Config.DOWNLOAD_REGISTERED_FARMERS +"?village_ids="+CSVUserVillageIds+"&company_id="+Preferences.COMPANY_ID, db,mContext, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(DownloadRegisteredFarmers.TAG, ""+error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		DownloadRegisteredFarms farms = new DownloadRegisteredFarms(Request.Method.GET, Config.DOWNLOAD_FARM_LIST +"?village_ids="+CSVUserVillageIds+"&company_id="+Preferences.COMPANY_ID, db,mContext, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(DownloadRegisteredFarms.TAG, ""+error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		DownloadCottonDeductions deductions = new DownloadCottonDeductions(Request.Method.GET, Config.DOWNLOAD_COTTON_DEDUCTIONS + "?company_id=" + Preferences.COMPANY_ID+"&village_ids="+CSVUserVillageIds, db, mContext, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("COTTON KEY_DEDUCTIONS: ", "" + error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		DownloadCollectedOrders collectedOrders = new DownloadCollectedOrders(Request.Method.GET, Config.DOWNLOAD_COLLECTED_ORDERS + "?company_id=" + Preferences.COMPANY_ID+"&village_ids="+CSVUserVillageIds, db, mContext, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("COLLECTED ORDERS: ", "" + error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		DownloadRecoveredCash recoveredCash = new DownloadRecoveredCash(Request.Method.GET, Config.DOWNLOAD_RECOVERED_CASH + "?company_id=" + Preferences.COMPANY_ID+"&village_ids="+CSVUserVillageIds, db, mContext, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("COLLECTED CASH: ", "" + error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		DownloadProductGrades cottonPrices = new DownloadProductGrades(Request.Method.GET, Config.DOWNLOAD_COTTON_PRICES + "?company_id=" + Preferences.COMPANY_ID+"&village_ids="+CSVUserVillageIds, db, mContext, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("COTTON PRICES: ", "" + error.getMessage());
				DashBoardActivity.downloadCount++;
			}
		});

		RequestQueue queue = VolleySingleton.getInstance(mContext).getRequestQueue();
		queue.add(assignedInputs);
		queue.add(registeredFarmers);
		queue.add(farms);
		queue.add(deductions);
		queue.add(collectedOrders);
		queue.add(recoveredCash);
		queue.add(cottonPrices);
	}

	/**
	 * @param response
	 */
	private void addData(String response) {

		try {

			Gson gson = new GsonBuilder().create();

			List<UserVillage> list = gson.fromJson(response, new TypeToken<List<UserVillage>>() {
			}.getType());

			for (int i = 0; i < list.size(); i++) {
				db.addUserVillage(list.get(i));
			}


		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
