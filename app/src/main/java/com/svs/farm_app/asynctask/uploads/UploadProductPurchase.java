package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.ProductPurchase;
import com.svs.farm_app.farmersearch.FarmerSearchActivity;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DataUtils;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.svs.farm_app.utils.MyAlerts.backToDashboardDialog;

public class UploadProductPurchase extends AsyncTask<Void, Void, String> {

	private static final String TAG = UploadProductPurchase.class.getSimpleName();
	private final List<ProductPurchase> productPurchases;
	private final DatabaseHandler db;
	Context mContext;
	private String response = null;
	private boolean isOnline = false;

	public UploadProductPurchase(Context ctx, List<ProductPurchase> productPurchases,DatabaseHandler db) {
		this.mContext = ctx;
		this.productPurchases = productPurchases;
		this.db = db;
		DashBoardActivity.isUploadFinished(mContext);
	}

	public UploadProductPurchase(Context ctx, ProductPurchase productPurchase,DatabaseHandler db) {
		this.mContext = ctx;
		this.productPurchases = DataUtils.makeSingleList(productPurchase);
		this.db = db;
		isOnline = true;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(Config.UPLOAD_PRODUCT_PURCHASE).openConnection();
			connection.setRequestProperty("X-API-KEY", Config.API_KEY);
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);

			String urlParameters = "data=" + new Gson().toJson(productPurchases).toString();

			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			dos.writeBytes(urlParameters);
			dos.flush();
			dos.close();

			InputStream inputStream = new BufferedInputStream(connection.getInputStream());

			if (inputStream != null) {
				response = DataUtils.convertInputStreamToString(inputStream);
			}

			inputStream.close();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Handler handler = new Handler(mContext.getMainLooper());
			handler.post(() -> Toast.makeText(mContext, "Issue at " + Config.UPLOAD_PRODUCT_PURCHASE, Toast.LENGTH_LONG).show());

		}

		return response;
	}


	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);

		JSONObject object;

		try {
			Log.i(TAG,""+response);

			object = new JSONObject(response);
			if ((object.get("message").equals("ok"))) {
				if (isOnline) {
					MyAlerts.toActivityDialog(mContext,R.string.saved_online,new Intent(mContext, FarmerSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Config.TO_ACTIVITY, "recovery"));

				} else {
					Log.i(TAG,"clearing");
					db.clearTable(db.TABLE_PRODUCT_PURCHASES);
				}
			} else {
				Toast.makeText(mContext, "Message is " + object.get("message").toString()+ "at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
				if (isOnline) {
					db.addProductPurchase(productPurchases.get(0));
					MyAlerts.backToDashboardDialog(mContext,R.string.try_save_offline);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			Toast.makeText(mContext, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

		}
	}

}

