package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.svs.farm_app.entities.ReRegisteredFarmers;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.shouheng.compress.Compress;
import me.shouheng.compress.listener.CompressListener;

public class UploadReRegisteredFarmers extends AsyncTask<Void, String, String> {

    private static final String TAG = UploadReRegisteredFarmers.class.getSimpleName();
    private final ReRegisteredFarmers farmer;
    private String responseString;
    private ConnectionDetector cd;
    private DatabaseHandler db;
    Context ctx;

    public UploadReRegisteredFarmers(Context ctx, ReRegisteredFarmers farmer) {
        this.ctx = ctx;
        db = new DatabaseHandler(ctx);
        cd = new ConnectionDetector(ctx);
        this.farmer = farmer;
    }


    private String imagePath;
    private long totalSize;
    private File farmerPic;
    private File leftThumbFile;
    private File rightThumbFile;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {

        int count = db.getTableCount(db.TABLE_RE_REGISTERED_FARMERS);
        Log.i("Farmer Count: ", String.valueOf(count));

        if (cd.isConnectingToInternet() == true) {
            if (count > 0) {
                HttpPost httppost = null;

                String log = " ,FID: " + farmer.getFarmerId() + " ,GEN_ID: " + farmer.getGenId()
                        + " ,FARMERPic : " + farmer.getFarmerPic() + " ,Left Thumb: " + farmer.getLeftThumb() + " ,Right Thumb: "
                        + farmer.getRightThumb();

                Log.d("FARMERS:", log);

                responseString = null;

                HttpClient httpclient = new DefaultHttpClient();
                Log.e("", Config.RE_REGISTER_URL);

                imagePath = farmer.getFarmerPic();

                httppost = new HttpPost(Config.RE_REGISTER_URL);
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

                    leftThumbFile = new File(farmer.getLeftThumb());
                    rightThumbFile = new File(farmer.getRightThumb());

                    Log.d("ImagePath:", imagePath.toString());
                    Compress.Companion.with(ctx, new File(imagePath)).setQuality(10).setQuality(40).setCompressListener(new CompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            farmerPic = file;
                        }

                        @Override
                        public void onError(@NonNull Throwable throwable) {

                        }
                    });

                    entity.addPart("image", new FileBody(farmerPic));
                    entity.addPart("left_thumb", new FileBody(leftThumbFile));
                    entity.addPart("right_thumb", new FileBody(rightThumbFile));

                    entity.addPart("farmer_id", new StringBody(farmer.getFarmerId()));
                    entity.addPart(DatabaseHandler.KEY_GEN_ID, new StringBody(farmer.getGenId()));
                    entity.addPart(DatabaseHandler.KEY_USER_ID, new StringBody(farmer.getUserId()));
                    entity.addPart("company_id", new StringBody(farmer.getCompanyId()));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
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
                    handler.post(() -> Toast.makeText(ctx, "Issue at " + Config.RE_REGISTER_URL, Toast.LENGTH_LONG).show());
                }

            }
            Log.i("Server response:", responseString);

        } else {

        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null) {
            JSONObject object;
            try {
                Log.i(TAG, result);
                object = new JSONObject(result);
                if (object.get("message").equals("ok")) {
                    db.removeItemFromTable(String.valueOf(farmer.getRowId()), db.TABLE_RE_REGISTERED_FARMERS);
                    farmerPic.delete();
                    leftThumbFile.delete();
                    rightThumbFile.delete();
                }else{
                    Toast.makeText(ctx, "Message is " + object.get("message").toString()+ "at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

            }
        }

        DashBoardActivity.isUploadFinished(ctx);
    }

}

