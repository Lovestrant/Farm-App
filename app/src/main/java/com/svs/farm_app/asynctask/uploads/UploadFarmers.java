package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.svs.farm_app.entities.Farmers;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class UploadFarmers extends AsyncTask<Void, String, String> {

    private String responseString;
    private ConnectionDetector cd;
    private DatabaseHandler db;
    Context ctx;
    private Farmers farmer;
    private String TAG = "UPLOAD FARMERS";

    public UploadFarmers(Context ctx, Farmers farmer) {
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

    protected String doInBackground(Void... params) {
        int i = 0;
        if (cd.isConnectingToInternet() == true) {

            // for (Farmers farmer : farmer) {
            i = farmer.getRowID();

            String log = "Id: " + farmer.getRowID() + " ,FName: " + farmer.getFName() + " ,LName: " + farmer.getLName()
                    + " ,Gender: " + farmer.getGender() + " ,ID number: " + farmer.getIDNumber() + " ,Phone: "
                    + farmer.getPhoneNumber() + " ,Email: " + farmer.getEmail() + " ,Post: " + farmer.getPostAddress()
                    + " ,Village: " + farmer.getVillageID() + " ,Subvillage: " + farmer.getSubVillageID() + " ,ImagePath: "
                    + farmer.getFarmerPicPath() + ",User ID" + farmer.getUserID() + ",Company ID" + farmer.getCompanyID();

            Log.d("FARMERS:", log);

            responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            Log.e("", Config.FARMER_UPLOAD_URL);
            HttpPost httppost = new HttpPost(Config.FARMER_UPLOAD_URL);
            httppost.addHeader("X-API-KEY", Config.API_KEY);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            }
                        });

                imagePath = farmer.getFarmerPicPath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    Log.d("ImagePath:", imagePath.toString());
                    farmerPic = new File(imagePath);
                } else {
                    farmerPic = File.createTempFile("farmerPic" + System.currentTimeMillis(), "temp", ctx.getCacheDir());
                }
                if (farmer.getLeftThumb() != null && !farmer.getLeftThumb().isEmpty()) {
                    leftThumbFile = new File(farmer.getLeftThumb());
                } else {
                    leftThumbFile = farmerPic;
                }
                if (farmer.getRightThumb() != null && !farmer.getRightThumb().isEmpty()) {
                    rightThumbFile = new File(farmer.getRightThumb());
                } else {
                    rightThumbFile = farmerPic;
                }

                // Adding file data to http body
                entity.addPart("image", new FileBody(farmerPic));
                entity.addPart("left_thumb", new FileBody(leftThumbFile));
                entity.addPart("right_thumb", new FileBody(rightThumbFile));
                // Extra parameters if you want to pass to server
                entity.addPart("fname", new StringBody(farmer.getFName()));
                entity.addPart("lname", new StringBody(farmer.getLName()));
                entity.addPart("gender", new StringBody(farmer.getGender()));
                entity.addPart("id_no", new StringBody(String.valueOf(farmer.getIDNumber())));
                entity.addPart("phone", new StringBody(farmer.getPhoneNumber()));
                entity.addPart("email", new StringBody(farmer.getEmail()));
                entity.addPart("post", new StringBody(farmer.getPostAddress()));
                entity.addPart("village_id", new StringBody(farmer.getVillageID()));
                entity.addPart("subvillage_id", new StringBody(farmer.getSubVillageID()));
                entity.addPart("est_farm_area_one", new StringBody(farmer.getEstimatedFarmArea()));
                entity.addPart("contract_status", new StringBody(farmer.getShowIntent()));
                entity.addPart("contract_number", new StringBody(farmer.getContractNo()));
                entity.addPart("farm_village_id_one", new StringBody(farmer.getFarmVidOne()));
                entity.addPart("other_crops_id_one", new StringBody(farmer.getOtherCropsOne()));
                if (farmer.getOtherCropsTwo() != null)
                    entity.addPart("other_crops_id_two", new StringBody(farmer.getOtherCropsTwo()));
                if (farmer.getOtherCropsThree() != null)
                    entity.addPart("other_crops_id_three", new StringBody(farmer.getOtherCropsThree()));
                if(farmer.getFarmVidTwo() != null){
                    entity.addPart("farm_village_id_two", new StringBody(farmer.getFarmVidTwo()));
                    entity.addPart("est_farm_area_two", new StringBody(farmer.getEstimatedFarmAreaTwo()));
                }if(farmer.getFarmVidThree() != null){
                    entity.addPart("farm_village_id_three", new StringBody(farmer.getFarmVidThree()));
                    entity.addPart("est_farm_area_three", new StringBody(farmer.getEstimatedFarmAreaThree()));
                }if(farmer.getFarmVidFour() != null){
                    entity.addPart("farm_village_id_four", new StringBody(farmer.getFarmVidFour()));
                    entity.addPart("est_farm_area_four", new StringBody(farmer.getEstimatedFarmAreaFour()));
                }

                entity.addPart("latitude", new StringBody(farmer.getLatitude()));
                entity.addPart("longitude", new StringBody(farmer.getLongitude()));
                //entity.addPart("reg_date", new StringBody(farmer.getRegistrationDate()));
                entity.addPart("company_id", new StringBody(farmer.getCompanyID()));
                entity.addPart(DatabaseHandler.KEY_USER_ID, new StringBody(farmer.getUserID()));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);

                    //upload farmer's farm histories
                    Handler mainHandler = new Handler(ctx.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            new UploadFarmHistory(ctx, farmer).execute();
                        }
                    };
                    mainHandler.post(myRunnable);


                } else {
                    responseString = EntityUtils.toString(r_entity);
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            Log.i("Server response:", responseString);

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
                    db.removeItemFromTable(String.valueOf(farmer.getRowID()), db.TABLE_FARMERS);
                    farmerPic.delete();
                }
                else if (object.get("message").toString().contains("DUPLI")) {
                    db.removeItemFromTable(String.valueOf(farmer.getRowID()), db.TABLE_FARMERS);
                    farmerPic.delete();
                }
                else{
                    Toast.makeText(ctx, "Message is " + object.get("message").toString()+ "at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

            }
        }

    }

}
