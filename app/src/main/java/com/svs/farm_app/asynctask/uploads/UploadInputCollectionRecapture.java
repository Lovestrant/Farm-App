package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.svs.farm_app.entities.ManualInputFingerprintRecapture;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.AndroidMultiPartEntity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;

import org.apache.commons.io.FileUtils;
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

public class UploadInputCollectionRecapture extends AsyncTask<Void, Void, Void> {

    private String responseString;
    private ConnectionDetector cd;
    private DatabaseHandler db;
    Context ctx;

    public UploadInputCollectionRecapture(Context ctx) {
        this.ctx = ctx;
        db = new DatabaseHandler(ctx);
        cd = new ConnectionDetector(ctx);
    }


    private String imagePath;
    private long totalSize;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @SuppressWarnings("deprecation")
    @Override
    protected Void doInBackground(Void... params) {

        // check if database is empty
        int count = db.getTableCount(db.TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE);
        Log.i("Farmer Count: ", String.valueOf(count));
        int i = 0;
        if (cd.isConnectingToInternet() == true) {
            if (count > 0) {
                List<ManualInputFingerprintRecapture> farmers = db.getManualInputFingerprintRecapture();
                HttpPost httppost = null;
                for (ManualInputFingerprintRecapture cn : farmers) {
                    //i=cn.getRowID();
                    // sample farmers
                    String log = " ,FID: " + cn.getfId() + " ,GEN_ID: " + cn.getGenId() + " ,Left Thumb: " + cn.getLeftThumb() + " ,Right Thumb: "
                            + cn.getRightThumb();
                    // Writing Farmers to log
                    Log.d("MAN Input collection:", log);
                    // upload to server
                    responseString = null;

                    HttpClient httpclient = new DefaultHttpClient();
                    Log.e("", Config.RE_REGISTER_URL);


                    httppost = new HttpPost(Config.INPUT_COLLECTION_ERROR_URL);
                    httppost.addHeader("X-API-KEY", Config.API_KEY);

                    try {
                        AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                                new AndroidMultiPartEntity.ProgressListener() {

                                    @Override
                                    public void transferred(long num) {
                                        // publishProgress((int) ((num /
                                        // (float) totalSize) * 100));
                                    }
                                });

                        File leftThumbFile = new File(cn.getLeftThumb());
                        File rightThumbFile = new File(cn.getRightThumb());

                        // Adding file data to http body
                        entity.addPart("left_thumb", new FileBody(leftThumbFile));
                        entity.addPart("right_thumb", new FileBody(rightThumbFile));
                        // Extra parameters if you want to pass to server
                        entity.addPart("farmer_id", new StringBody(cn.getfId()));
                        entity.addPart(DatabaseHandler.KEY_GEN_ID, new StringBody(cn.getGenId()));

                        totalSize = entity.getContentLength();
                        httppost.setEntity(entity);

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
                        Handler handler = new Handler(ctx.getMainLooper());
                        handler.post(() -> Toast.makeText(ctx, "Issue at RE_REGISTER_URL + INPUT_COLLECTION_ERROR_URL" + Config.RE_REGISTER_URL, Toast.LENGTH_LONG).show());
                            Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();


                    }

                    // return responseString;

                }
                if (responseString.contains("ok")) {
                    Log.i("server", "ok");
                    db.clearTable(db.TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE);
                    try {
                        FileUtils.cleanDirectory(new File(Config.SD_CARD_PATH + "/" + Config.IMAGE_DIRECTORY_NAME2));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(ctx, "Message is " + responseString.toString()+ "at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

                }
                // clear table farmers

                // delete all info from tables

                Log.i("Data synced!", "");
                Log.i("Server response:", responseString);

            } else {
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(getApplicationContext(), "No farmer data to sync!", Toast.LENGTH_SHORT)
//								.show();
//					}
//				});
            }
        } else {
//			mContext.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					Toast.makeText(mContext, "Connection Timed out!", Toast.LENGTH_SHORT)
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

