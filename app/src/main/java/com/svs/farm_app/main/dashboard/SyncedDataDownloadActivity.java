package com.svs.farm_app.main.dashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncedDataDownloadActivity extends BaseClass {
    private static String TAG = SyncedDataDownloadActivity.class.getSimpleName();
    private SyncedArray syncedArray;
    private DatabaseHandler db;
    ProgressDialog progress;
    List<SyncedArray> list;
    ListView listView;
    ServerData serverData = new ServerData();
    CustomListAdapter adapter;
    private ProgressDialog dialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SyncedDataDownloadActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_data_download);
        ButterKnife.bind(this);

        mContext = SyncedDataDownloadActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();

        dialog = new ProgressDialog(SyncedDataDownloadActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.checking_downloads));
        dialog.show();

        new DownloadCounts().execute(new String[]{Config.DOWNLOAD_DOWNLOAD_COUNTS});

    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        list = new ArrayList<>();

        adapter = new CustomListAdapter(this, list);
    }

    @SuppressWarnings("static-access")
    public void CreateList() {
        CreateItem("Assigned  Inputs", serverData.getAssigned_inputs_count(),
                db.getTableCount(db.TABLE_ASSIGNED_INPUTS));
        CreateItem("Farms", serverData.getFarms_count(), db.getTableCount(db.TABLE_FARMS));
        CreateItem("Foliar Feed", serverData.getFoliar_feed_count(), db.getTableCount(db.TABLE_FOLIAR_FEED));
        CreateItem("Herbicides", serverData.getHerbicide_count(), db.getTableCount(db.TABLE_HERBICIDES));
        CreateItem("My Trainings", serverData.getMy_trainings_count(),
                db.getTableCount(db.TABLE_EXT_OFFICER_TRAINING));
        CreateItem("Other Crops", serverData.getOther_crops_count(),
                db.getTableCount(DatabaseHandler.TABLE_OTHER_CROPS));
        CreateItem("Pesticide", serverData.getPesticide_count(), db.getTableCount(db.TABLE_PESTICIDES));
        CreateItem("Farmers", serverData.getRegistered_farmers_count(),
                db.getTableCount(db.TABLE_REGISTERED_FARMERS));
        CreateItem("Sub Villages", serverData.getSub_villages_count(),
                db.getTableCount(db.TABLE_SUBVILLAGES));
        /*CreateItem("Train categories", serverData.getTraining_categories_count(),
                db.getTableCount(db.TABLE_TRAIN_CATEGORIES));*/
        // CreateItem("AssignedTrainings
        // materials",serverData.getTraining_mat_names_count(),
        // db.getTableCount(tableName));
        CreateItem("AssignedTrainings Types", serverData.getTraining_types_count(), db.getTableCount(db.TABLE_TRAIN_TYPES));
        CreateItem("User Villages", serverData.getUser_village_count(),
                db.getTableCount(db.TABLE_USER_VILLAGE));
        CreateItem("Villages", serverData.getVillage_count(), db.getTableCount(db.TABLE_VILLAGES));
        CreateItem("Assesments Done", serverData.getFarmDataCollected(),
                db.getTableCount(db.TABLE_FARM_DATA_COLLECTED));
        CreateItem("Updated Farmers", 0,
                db.getTableCount(db.TABLE_UPDATED_FARMERS));
        /*
         * CreateItem("Wack Farmers", serverData.getVillage_count(),
         * db.getTableCount(DatabaseHandler.TABLE_WACK_FARMERS));
         */

    }

    public void CreateItem(String name, int serverCount, int deviceCount) {
        Log.e("Data", name + " " + serverCount + " " + deviceCount);
        syncedArray = new SyncedArray();
        syncedArray.setServerCount(serverCount);
        syncedArray.setDeviceCount(deviceCount);
        syncedArray.setName(name);
        list.add(syncedArray);
    }

    public class DownloadCounts extends AsyncTask<String, String, String> {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            Log.i(TAG, url[0] + "?company_id=" + Preferences.COMPANY_ID + "&user_id=" + Preferences.USER_ID);
            String result = null;

            result = addParams(url[0] + "?company_id=" + Preferences.COMPANY_ID + "&user_id=" + Preferences.USER_ID);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Log.i(TAG, result);

            JSONObject json;
            try {
                json = new JSONObject(result);
                if (json.has("message")) {
                    new MaterialStyledDialog.Builder(SyncedDataDownloadActivity.this)
                            .setTitle(R.string.app_name)
                            .setDescription(json.getString("message"))
                            .setStyle(Style.HEADER_WITH_TITLE)
                            .setCancelable(true)
                            .setPositiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivity(new Intent(mContext,
                                            DashBoardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                            }).show();
                } else {
                    serverData.setAssigned_inputs_count(json.getInt("assigned_inputs_count"));
                    serverData.setFarms_count(json.getInt("farms_count"));
                    serverData.setFoliar_feed_count(json.getInt("foliar_feed_count"));
                    serverData.setMy_trainings_count(json.getInt("my_trainings_count"));
                    serverData.setHerbicide_count(json.getInt("herbicide_count"));
                    serverData.setOther_crops_count(json.getInt("other_crops_count"));
                    serverData.setPesticide_count(json.getInt("pesticide_count"));
                    serverData.setRegistered_farmers_count(json.getInt("registered_farmers_count"));
                    serverData.setSub_villages_count(json.getInt("sub_villages_count"));
                    serverData.setTraining_categories_count(json.getInt("training_categories_count"));
                    serverData.setTraining_mat_names_count(json.getInt("training_mat_names_count"));
                    serverData.setTraining_types_count(json.getInt("training_types_count"));
                    serverData.setUser_village_count(json.getInt("user_village_count"));
                    serverData.setVillage_count(json.getInt("village_count"));
                    serverData.setFarmDataCollected(json.getInt("farm_data_collected_count"));

                    Log.e("Server Data: ", " tester: " + serverData.toString());
                    CreateList();
                    listView = (ListView) findViewById(R.id.list);
                    (listView).setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class ServerData {
        int user_village_count;
        int village_count;
        int sub_villages_count;
        int herbicide_count;
        int foliar_feed_count;
        int pesticide_count;
        int other_crops_count;
        int my_trainings_count;
        int training_categories_count;
        int training_types_count;
        int training_mat_names_count;
        int registered_farmers_count;
        int farms_count;
        int assigned_inputs_count;
        int farm_data_collected;

        public int getUser_village_count() {
            return user_village_count;
        }

        public void setUser_village_count(int user_villgae_count) {
            this.user_village_count = user_villgae_count;
        }

        public int getVillage_count() {
            return village_count;
        }

        public void setVillage_count(int village_count) {
            this.village_count = village_count;
        }

        public int getSub_villages_count() {
            return sub_villages_count;
        }

        public void setSub_villages_count(int sub_villages_count) {
            this.sub_villages_count = sub_villages_count;
        }

        public int getHerbicide_count() {
            return herbicide_count;
        }

        public void setHerbicide_count(int herbicide_count) {
            this.herbicide_count = herbicide_count;
        }

        public int getFoliar_feed_count() {
            return foliar_feed_count;
        }

        public void setFoliar_feed_count(int foliar_feed_count) {
            this.foliar_feed_count = foliar_feed_count;
        }

        public int getPesticide_count() {
            return pesticide_count;
        }

        public void setPesticide_count(int pesticide_count) {
            this.pesticide_count = pesticide_count;
        }

        public int getOther_crops_count() {
            return other_crops_count;
        }

        public void setOther_crops_count(int other_crops_count) {
            this.other_crops_count = other_crops_count;
        }

        public int getMy_trainings_count() {
            return my_trainings_count;
        }

        public void setMy_trainings_count(int my_trainings_count) {
            this.my_trainings_count = my_trainings_count;
        }

        public int getTraining_categories_count() {
            return training_categories_count;
        }

        public void setTraining_categories_count(int training_categories_count) {
            this.training_categories_count = training_categories_count;
        }

        public int getTraining_types_count() {
            return training_types_count;
        }

        public void setTraining_types_count(int training_types_count) {
            this.training_types_count = training_types_count;
        }

        public int getTraining_mat_names_count() {
            return training_mat_names_count;
        }

        public void setTraining_mat_names_count(int training_mat_names_count) {
            this.training_mat_names_count = training_mat_names_count;
        }

        public int getRegistered_farmers_count() {
            return registered_farmers_count;
        }

        public void setRegistered_farmers_count(int registered_farmers_count) {
            this.registered_farmers_count = registered_farmers_count;
        }

        public int getFarms_count() {
            return farms_count;
        }

        public void setFarms_count(int farms_count) {
            this.farms_count = farms_count;
        }

        public int getAssigned_inputs_count() {
            return assigned_inputs_count;
        }

        public void setAssigned_inputs_count(int assigned_inputs_count) {
            this.assigned_inputs_count = assigned_inputs_count;
        }

        public int getFarmDataCollected() {
            return farm_data_collected;
        }

        public void setFarmDataCollected(int farm_data_collected) {
            this.farm_data_collected = farm_data_collected;
        }

        public String toString() {
            return "user_village_count: " + user_village_count + "\nvillage_count: " + village_count
                    + "\nsub_villages_count: " + sub_villages_count + "\nherbicide_count: " + herbicide_count
                    + "\nfoliar_feed_count: " + foliar_feed_count + "\npesticide_count: " + pesticide_count
                    + "\nother_crops_count: " + other_crops_count + "\nmy_trainings_count: " + my_trainings_count
                    + "\ntraining_categories_count: " + training_categories_count + "\ntraining_types_count: "
                    + training_categories_count + "\ntraining_mat_names_count: " + training_mat_names_count
                    + "\nregistered_farmers_count: " + registered_farmers_count + "\nfarms_count: " + farms_count
                    + "\nassigned_inputs_count: " + assigned_inputs_count + "\nfarm_data_collected: "
                    + farm_data_collected;
        }
    }

    private static String convertInputStreamToString(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        in.close();
        return result.toString();
    }

    private class SyncedArray {
        String Name;
        int ServerCount, DeviceCount;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getServerCount() {
            return ServerCount;
        }

        public void setServerCount(int count) {
            ServerCount = count;
        }

        public int getDeviceCount() {
            return DeviceCount;
        }

        public void setDeviceCount(int count) {
            DeviceCount = count;
        }
    }

    public static String addParams(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("X-API-KEY", Config.API_KEY);
        String result = "";

        try {

            HttpResponse response = httpclient.execute(httpget);


            InputStream inputStream = response.getEntity().getContent();

            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Didn't work";
            }

            Log.i(TAG, "result: " + response.toString());

            return result;

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        return result;
    }

    public class CustomListAdapter extends ArrayAdapter<SyncedArray> {

        private final Activity context;
        private final List<SyncedArray> items;

        public CustomListAdapter(Activity context, List<SyncedArray> items) {
            super(context, R.layout.synced_data_list, items);
            this.context = context;
            this.items = items;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.synced_data_download, null, true);

            TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
            TextView tvServerCount = (TextView) rowView.findViewById(R.id.tvServerCount);
            TextView tvDeviceCount = (TextView) rowView.findViewById(R.id.tvDeviceCount);

            tvName.setText(items.get(position).getName());
            tvServerCount.setText(items.get(position).getServerCount() + "");
            tvDeviceCount.setText(items.get(position).getDeviceCount() + "");
            return rowView;

        }
    }
}
