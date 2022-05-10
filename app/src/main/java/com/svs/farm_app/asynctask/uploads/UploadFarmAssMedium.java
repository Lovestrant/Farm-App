package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmAssFormsMedium;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DataUtils;
import com.svs.farm_app.utils.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.svs.farm_app.utils.MyAlerts.backToDashboardDialog;

public class UploadFarmAssMedium extends AsyncTask<Void, Void, String> {
    //TODO: Check why "+" is being removed on serialization of data
    private static final String TAG = UploadAssignedTrainings.class.getSimpleName();
    private final List<FarmAssFormsMedium> formsMedium;
    Context mContext;
    private String response = null;
    private boolean isOnline = false;

    public UploadFarmAssMedium(Context ctx, List<FarmAssFormsMedium> formsMedium) {
        this.mContext = ctx;
        this.formsMedium = formsMedium;
        DashBoardActivity.isUploadFinished(mContext);
    }

    public UploadFarmAssMedium(Context ctx, FarmAssFormsMedium formsMedium) {
        this.mContext = ctx;
        this.formsMedium = DataUtils.makeSingleList(formsMedium);
        isOnline = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Config.UPLOAD_FARM_ASS_FORM_MEDIUM).openConnection();
            connection.setRequestProperty("X-API-KEY", Config.API_KEY);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            String urlParameters = "data=" + gson.toJson(formsMedium).toString();

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
            handler.post(() -> Toast.makeText(mContext, "Issue at " + Config.UPLOAD_FARM_ASS_FORM_MEDIUM, Toast.LENGTH_LONG).show());
        }

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        JSONObject object;
        DatabaseHandler db = new DatabaseHandler(mContext);

        try {
            Log.i(TAG, "" + response);
            object = new JSONObject(response);
            if ((object.get("message").equals("ok"))) {
                if (isOnline) {
                    backToDashboardDialog(mContext, R.string.saved_online);
                } else {
                    Log.i(TAG, "clearing");
                    db.clearTable(db.TABLE_FARM_ASS_MEDIUM);
                }
            } else {
                Toast.makeText(mContext, "Message is " + object.get("message").toString()+ "at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                if (isOnline) {
                    db.addFarmAssMedium(formsMedium.get(0));
                    backToDashboardDialog(mContext, R.string.try_save_offline);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(mContext, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

        }
    }
}

