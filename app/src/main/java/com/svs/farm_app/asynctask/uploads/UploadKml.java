package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.svs.farm_app.entities.MappedFarm;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.AndroidMultiPartEntity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UploadKml extends AsyncTask<Void, Void, Void> {

	private Exception exception;
	private ConnectionDetector cd;
	private DatabaseHandler db;
	Context ctx;
	private long totalSize;
	List<MappedFarm> kml_meta;
	
	public UploadKml(Context ctx) {
		this.ctx = ctx;
		db = new DatabaseHandler(ctx);
		cd = new ConnectionDetector(ctx);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 
	 */
	private void sanitizeData(List<MappedFarm> kml_meta) {
		if(kml_meta.size()>0){
			String kmlPath = Config.SD_CARD_PATH + "/com.vistechprojects.planimeter/data/";
			for (MappedFarm cn : kml_meta) {
				String farmId = cn.getFarmId();
			String kmlFullPath = kmlPath+farmId+".kml";
			File kmlFile = new File(kmlFullPath);
			Log.i("KML:","del One");
			if(!kmlFile.exists()){
				Log.i("KML:","del Two"+farmId);
				//delete record
				//String query = "delete from "+DatabaseHandler.TABLE_MAPPED_FARMS+" where "+DatabaseHandler.KEY_FARM_ID+" = "+farmId;
				//db.deleteFromTable(farmId);
			}
			//
		}
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (cd.isConnectingToInternet()) {
			//sanitize data remove orphan data without kml
			kml_meta = db.getAllMappedFarms();
			sanitizeData(kml_meta);
			kml_meta = db.getAllMappedFarms();
			// db = new DatabaseHandler(getApplicationContext());
			// upload to server
			String responseString2 = "";

			HttpClient httpclient = new DefaultHttpClient();
			Log.e("", Config.UPLOAD_MAPPED_FARMS);
			HttpPost httppost = new HttpPost(Config.UPLOAD_MAPPED_FARMS);
			httppost.addHeader("X-API-KEY", Config.API_KEY);

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new AndroidMultiPartEntity.ProgressListener() {

							@Override
							public void transferred(long num) {
								// publishProgress((int) ((num / (float)
								// totalSize) * 100));
							}
						});
				// get all file in a directory
				String name = null, length = null;
				File sdCardRoot = Environment.getExternalStorageDirectory();
				File yourDir = new File(Config.SD_CARD_PATH+"/com.vistechprojects.planimeter/data/");
				File[] contents = yourDir.listFiles();
				if (contents.length > 0) {
					
					Log.i("KMLMeta", "" + kml_meta.size());
					for (MappedFarm cn : kml_meta) {
						String filePath2 = Config.SD_CARD_PATH + "/com.vistechprojects.planimeter/data/"
								+ cn.getFarmId() + ".kml";
						Log.i("Kml path:", filePath2.toString());
						File sourceFile = new File(filePath2);
						if (kml_meta.size() > 0) {
							String ext = FilenameUtils.getExtension(sourceFile.getName());
							Log.e("Ext:", ext);

							entity.addPart("kml", new FileBody(sourceFile));
							entity.addPart("company_id", new StringBody(cn.getCompanyId()));
							entity.addPart(DatabaseHandler.KEY_USER_ID, new StringBody(cn.getUserId()));
							entity.addPart("farm_id", new StringBody(cn.getFarmId()));
//							entity.addPart("village_id", new StringBody(cn.getVillageId()));
							Date date = new Date();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							entity.addPart("map_date", new StringBody(dateFormat.format(date)));

							totalSize = entity.getContentLength();
							httppost.setEntity(entity);

							// Making server call
							HttpResponse response = httpclient.execute(httppost);
							HttpEntity r_entity = response.getEntity();

							int statusCode = response.getStatusLine().getStatusCode();
							if (statusCode == 200) {
								// Server response
								responseString2 = EntityUtils.toString(r_entity);

							} else {
								responseString2 = EntityUtils.toString(r_entity);
							}
							Log.e("KML Server Response:", responseString2);
						}
					}
					if(responseString2.contains("ok")){
					//db.clearTable(db.TABLE_MAPPED_FARMS);
					//clean kml directory
					try {
						FileUtils.cleanDirectory(new File(Config.SD_CARD_PATH + "/com.vistechprojects.planimeter/data/"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					}else{
						Toast.makeText(ctx, "Message is " + responseString2.toString()+ "at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

					}
					// }
				} else if (contents == null) {
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							Toast.makeText(getApplicationContext(), "No Farms to Sync", Toast.LENGTH_SHORT).show();
//						}
//					});
				} else {

//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							Toast.makeText(getApplicationContext(), "No Farms to Sync", Toast.LENGTH_SHORT).show();
//						}
//					});
				}
			} catch (Exception e) {
				// Log.e("KMLException:", e.getMessage());
			}
			finally {
				Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

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

		return null;

	}

	@Override
	protected void onPostExecute(Void aVoid) {
		super.onPostExecute(aVoid);
		DashBoardActivity.isUploadFinished(ctx);
	}
}
