package com.svs.farm_app.main.training_attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmerTime;
import com.svs.farm_app.utils.AndroidMultiPartEntity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class TransparentActivity extends BaseClass {

	private String genID;
	private Bitmap famerPhoto;
	private String time2;
	private String farmerID;

	private String TAG = TransparentActivity.class.getSimpleName();
	private String trainCatID;
	private String userID;
	private String companyID;
	private String farmerTimeIn;
	private String farmerTimeOut;
	private String extTrainID;
	private GPSTracker gps;
	private TransparentActivity mContext;
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transparent);

		mContext = TransparentActivity.this;

		initData();
		initView();
	}

	private void initData() {
		Intent intent = getIntent();
		genID = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);
		time2 = intent.getStringExtra("time");
		farmerTimeIn = intent.getStringExtra("farmer_time_in");
		farmerTimeOut = intent.getStringExtra("farmer_time_out");
		farmerID = intent.getStringExtra("farmer_id");

		trainCatID = intent.getStringExtra("train_cat_id");
		extTrainID = intent.getStringExtra("ext_train_id");

		db = new DatabaseHandler(mContext);



		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		Log.i(TAG, genID + " " + time2 + " " + farmerTimeIn + " "
				+ farmerTimeOut + " " + farmerID);
		try {
			famerPhoto = BitmapFactory.decodeFile(Config.SD_CARD_PATH +"/farmer_details/"
					+ genID);
		} catch (Exception ex) {
			Log.e("Image Ex: ", ex.getMessage());
		}
		gps = new GPSTracker(getApplicationContext());
	}

	private void initView() {

		AlertDialog.Builder alertadd = new AlertDialog.Builder(
				TransparentActivity.this);
		alertadd.setTitle("Attendance");

		LayoutInflater factory = LayoutInflater.from(TransparentActivity.this);
		final View view = factory.inflate(R.layout.dialog_main, null);
		try {
			ImageView image = (ImageView) view.findViewById(R.id.imageView);
			image.setImageBitmap(famerPhoto);
		} catch (Exception ex) {
			Log.e("Image Ex2: ", ex.getMessage());
		}
		TextView text = (TextView) view.findViewById(R.id.tvFID);
		text.setText("Farmer ID: " + genID);
		TextView text2 = (TextView) view.findViewById(R.id.tvTime);
		text2.setText(time2);

		alertadd.setView(view);
		alertadd.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				if ("0".equals(farmerTimeIn)) {

					db.updateFarmerTimes(farmerID,extTrainID, trainCatID, farmerTimeOut,
							userID, companyID);

				} else if ("0".equals(farmerTimeOut)) {

					String latitude="";
					String longitude = "";
					latitude = String.valueOf(gps.getLatitude());
					longitude = String.valueOf(gps.getLongitude());
					db.addFarmerTimes(new FarmerTime(farmerID,extTrainID, trainCatID,latitude,longitude,
							farmerTimeIn, farmerTimeOut, userID, companyID));
				}
				finish();
			}
		});
		alertadd.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {

						finish();
					}
				});
		alertadd.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_transparent, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class uploadFarmerTimes extends AsyncTask {

		private String responseString;
		String _fid, f_date_time;

		public uploadFarmerTimes(String _fid, String f_date_time) {
			this._fid = _fid;
			this.f_date_time = f_date_time;

		}

		@Override
		protected Object doInBackground(Object[] params) {
			Log.e("Uploding farmer times", "");
			// check if database is empty
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			ConnectionDetector cd = new ConnectionDetector(
					getApplicationContext());
			if (cd.isConnectingToInternet() == true) {
				// if (count > 0) {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Config.FARMER_TIME_UPLOAD_URL);

				try {
					AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
							new AndroidMultiPartEntity.ProgressListener() {

								@Override
								public void transferred(long num) {
									// publishProgress((int) ((num / (float)
									// totalSize) * 100));
								}
							});


					entity.addPart("farmer_id", new StringBody(_fid));
					entity.addPart("f_date_time", new StringBody(f_date_time));
					entity.addPart(
							"userID",
							new StringBody(getPrefrence(
									getApplicationContext(), "userID")));
					entity.addPart(
							"company_id",
							new StringBody(getPrefrence(
									getApplicationContext(), "company_id")));
					httppost.setEntity(entity);

					HttpResponse response = httpclient.execute(httppost);
					HttpEntity r_entity = response.getEntity();

					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						responseString = EntityUtils.toString(r_entity);
					} else {
						responseString = EntityUtils.toString(r_entity);
					}

				} catch (Exception e) {
				}

				try {
					Log.e("Server response:", responseString + f_date_time
							+ genID);
				} catch (Exception ex) {
				}

			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),
								"No Internet connection!", Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
			return null;

		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Server response: " + responseString,
							Toast.LENGTH_LONG).show();
				}
			});

		}
	}

	public static String getPrefrence(Context ctx, String key) {
		SharedPreferences pref = ctx.getSharedPreferences("mypref",
				MODE_PRIVATE);
		String result = pref.getString(key, null);
		return result;
	}
}
