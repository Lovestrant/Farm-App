package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svs.farm_app.entities.Farmers;
import com.svs.farm_app.entities.ProductPurchase;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.AndroidMultiPartEntity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DataUtils;
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

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UploadUpdatedFarmers extends AsyncTask<Void, String, String> {

    private String responseString;
    private ConnectionDetector cd;
    private DatabaseHandler db;
    Context ctx;
    private List<RegisteredFarmer> farmer;
    private String TAG = "UPLOAD FARMERS";

    public UploadUpdatedFarmers(Context ctx, List<RegisteredFarmer> registeredFarmers, DatabaseHandler db) {
        this.ctx = ctx;
        this.db = db;
        cd = new ConnectionDetector(ctx);
        this.farmer = registeredFarmers;
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


            responseString = null;


            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(Config.FARMER_UPDATE_URL).openConnection();
                connection.setRequestProperty("X-API-KEY", Config.API_KEY);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
//                connection.setRequestProperty("Content-Type", "application/json");
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String urlParameters = "data=" + gson.toJson(RegisteredFarmer.toJsonStringForUpdate(farmer)).toString();

                byte[] input = RegisteredFarmer.toJsonStringForUpdate(farmer).toString().getBytes("utf-8");
//                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
//                dos.writeBytes(urlParameters);
                OutputStream dos = connection.getOutputStream();
                dos.write(input);
//                dos.writeBytes(input);
                dos.flush();
                dos.close();
                int responseCode = connection.getResponseCode();
                InputStream inputStream = null;
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    inputStream = new BufferedInputStream(connection.getErrorStream());
                } else {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                }

//                outputStream.write(input, 0, input.length);

                if (inputStream != null) {
                    responseString = DataUtils.convertInputStreamToString(inputStream);
                }

                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: " + e.getLocalizedMessage());
                e.printStackTrace();
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
                if (object.getBoolean("res")) {
                    db.clearTable(DatabaseHandler.TABLE_UPDATED_FARMERS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

            }
        }

    }

}
