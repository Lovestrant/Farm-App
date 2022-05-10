package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.svs.farm_app.entities.FingerPrint;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.AndroidMultiPartEntity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadFingerprints extends AsyncTask<Void, Void, Void> {

	private String responseString;
	private File imageFile;
	private long totalSize;
	private ConnectionDetector cd;
	private DatabaseHandler db;
	Context ctx;
	
	public UploadFingerprints(Context ctx) {
		this.ctx = ctx;
		db = new DatabaseHandler(ctx);
		cd = new ConnectionDetector(ctx);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute(); // To change body of generated methods, choose
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Void doInBackground(Void... params) {

		// check if database is empty
		int count = db.getTableCount(db.TABLE_FINGERPRINTS);
		Log.i("Fingerprint Count: ", String.valueOf(count));
		if (cd.isConnectingToInternet() == true) {
			if (count > 0) {
				List<FingerPrint> fingerprint = db.getAllFingerprints();

				for (FingerPrint cn : fingerprint) {
					// sample farmers
					String log = "Id: " + cn.getGenID() + " ,Filepath: " + cn.getFilePath() + " ,companyID: "
							+ cn.getCompanyID();
					// Writing Farmers to log
					Log.d("FINGERPRINTS:", log);
					// upload to server
					responseString = null;

					HttpClient httpclient = new DefaultHttpClient();
					Log.e("", Config.FINGERPRINT_UPLOAD_URL);
					HttpPost httppost = new HttpPost(Config.FINGERPRINT_UPLOAD_URL);

					try {
						AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
								new AndroidMultiPartEntity.ProgressListener() {

									@Override
									public void transferred(long num) {
										// publishProgress((int) ((num /
										// (float) totalSize) * 100));
									}
								});
						String imagePath = cn.getFilePath();
						// fingerPath = cn.getFingerPath();
						File file = new File(imagePath);
						Log.d("Fingerprint Path:", imagePath.toString() + "|" + file.exists());
						// Log.d("FingerPath:", fingerPath.toString());
						imageFile = new File(imagePath);
						// File fingerFile = new File(fingerPath);
						try {
							// Adding file data to http body
							entity.addPart("fingerprint", new FileBody(imageFile));
						} catch (Exception ex) {

						}  finally {
							Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

						}
						// entity.addPart("image2", new
						// FileBody(fingerFile));

						// Extra parameters if you want to pass to server
						entity.addPart(DatabaseHandler.KEY_GEN_ID, new StringBody(cn.getFarmerID()));
						entity.addPart("farmer_id", new StringBody(cn.getGenID()));
						entity.addPart("company_id", new StringBody(cn.getCompanyID()));

						totalSize = entity.getContentLength();
						httppost.setEntity(entity);
						httppost.addHeader("X-API-KEY", Config.API_KEY);

						// Making server call
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity r_entity = response.getEntity();

						int statusCode = response.getStatusLine().getStatusCode();
						if (statusCode == 200) {
							// Server response
							responseString = EntityUtils.toString(r_entity);
						} else {
							responseString = EntityUtils.toString(r_entity);
						}

					} catch (ClientProtocolException e) {
						responseString = e.toString();
					} catch (IOException e) {
						responseString = e.toString();
					} finally {
						Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

					}

					// return responseString;
					if (imageFile.exists()) {
						imageFile.delete();
					}
				}
				// clear table fingerprints
				if(responseString.contains("ok")){
				db.clearTable(db.TABLE_FINGERPRINTS);
				}

				Log.i("Data synced!", "");
				Log.i("SERVER:", responseString);

			} else {
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(getApplicationContext(), "No fingerprint data to sync!", Toast.LENGTH_SHORT)
//								.show();
//					}
//				});
			}
		} else {
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					Toast.makeText(getApplicationContext(), "Connection Timed out!", Toast.LENGTH_SHORT)
//							.show();
//				}
//			});
		}

		// db.close();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result); // To change body of generated methods,
										// choose Tools | Templates.
		DashBoardActivity.isUploadFinished(ctx);
		db.close();
	}

}
