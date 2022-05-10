package com.svs.farm_app.asynctask.uploads;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.authentication.utils.VolleySingleton;
import com.svs.farm_app.asynctask.downloads.DownloadJustRegisteredFarmer;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.entities.Farmers;
import com.svs.farm_app.entities.OtherCrops;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.main.registration.register.FarmHistory;
import com.svs.farm_app.main.registration.register.FarmHistoryItem;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadFarmHistory extends AsyncTask<Void, String, String> implements DownloadJustRegisteredFarmer.OnFarmerFetched {

    private ConnectionDetector cd;
    private DatabaseHandler db;
    private Context ctx;
    private Farmers farmer;
    private String TAG = "UPLOAD FARM HISTORY";
    private ArrayList<FarmHistory> farmHistoriesForVillage = new ArrayList<>();

    public UploadFarmHistory(Context ctx, Farmers farmer) {
        this.ctx = ctx;
        db = new DatabaseHandler(ctx);
        cd = new ConnectionDetector(ctx);
        this.farmer = farmer;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(Void... params) {
        if (cd.isConnectingToInternet()) {

            //get All farm histories
            List<FarmHistory> farmHistories = db.getAllFarmHistories();

            farmHistoriesForVillage = new ArrayList<>();
            for (FarmHistory farmHistory : farmHistories) {
                if (farmHistory.getFarmerId().equals(farmer.getID()))
                    farmHistoriesForVillage.add(farmHistory);
            }

            //farm histories for village
            if (!farmHistoriesForVillage.isEmpty()) {
                DownloadJustRegisteredFarmer registeredFarmers = new DownloadJustRegisteredFarmer(Request.Method.GET, Config.DOWNLOAD_REGISTERED_FARMERS + "?village_ids=" + farmer.getVillageID() + "&company_id=" + farmer.getCompanyID(), db, ctx, this, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(UploadFarmHistory.this.getClass().getSimpleName(), "" + error.getMessage());
                        DashBoardActivity.downloadCount++;
                    }
                });
                VolleySingleton.getInstance(ctx).getRequestQueue().add(registeredFarmers);
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            Log.i(TAG, result);
        }
    }

    @Override
    public void farmerFetched(RegisteredFarmer registeredFarmer) {
        if (registeredFarmer != null) {
            try {
                List<OtherCrops> crops = db.allOtherCrops();
                List<FarmHistoryItem> historyItems = db.getAllFarmHistoryItems();

                HashMap<String, Object> map = new HashMap<>();
                map.put("fid", registeredFarmer.getFarmerId());
                map.put("user_id", Preferences.USER_ID);
                map.put("company_id", registeredFarmer.getCompanyId());

                List<HashMap<String, Object>> farmHistoryMaps = new ArrayList<>();
                HashMap<String, Object> farmHistoryMap = new HashMap<>();
                for (FarmHistory farmHistory : farmHistoriesForVillage) {
                    //get farm history items for farm history
                    List<FarmHistoryItem> farmHistoryItems = new ArrayList<>();
                    for (FarmHistoryItem farmHistoryItem : historyItems) {
                        if (farmHistoryItem.getFarmHistoryId().equals(farmHistory.getId()))
                            farmHistoryItems.add(farmHistoryItem);
                    }
                    List<HashMap<String, Object>> farmHistoryItemMaps = new ArrayList<>();
                    HashMap<String, Object> farmHistoryItemMap = new HashMap<>();
                    for (FarmHistoryItem farmHistoryItem : farmHistoryItems) {
                        for (OtherCrops crop : crops) {
                            if (crop.getCropID().equals(farmHistoryItem.getCropId())) ;
                            farmHistoryItem.setCrop(crop);
                        }
                        farmHistoryItemMap.put("product_name", farmHistoryItem.getCrop().getCropName());
                        farmHistoryItemMap.put("farm_size", farmHistoryItem.getAcres());
                        farmHistoryItemMap.put("produce_weight", farmHistoryItem.getWeight());
                       // farmHistoryItemMap.put("land_owned", farmHistoryItem.getLandOwned());
                        farmHistoryItemMaps.add(farmHistoryItemMap);
                    }
                    //TODO: Add season selector
                    farmHistoryMap.put("season_id", 8);
                    farmHistoryMap.put("season_name", "2020 - 2021");
                    farmHistoryMap.put("total_land_holding_size", farmHistory.getTotalLandHoldingSize());
                    farmHistoryMap.put("products", farmHistoryItemMaps);
                }
                farmHistoryMaps.add(farmHistoryMap);
                map.put("farm_history", farmHistoryMaps);
                postData(Config.FARMER_UPDATE, map);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }


    }

    public void postData(String url, HashMap data) {
        RequestQueue requstQueue = VolleySingleton.getInstance(ctx).getRequestQueue();

        Log.d(TAG,"Payload : "+ new JSONObject(data));

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        db.clearTable(db.TABLE_FARM_HISTORY_ITEMS);
                        db.clearTable(db.TABLE_FARM_HISTORY);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ctx, "Message is " + " at class"+this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

                        try {
                            Log.e(TAG, error.getLocalizedMessage());
                        }catch (Exception e){
                            e.printStackTrace();
                        } finally {
                            Toast.makeText(ctx, "Issue at " + this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        ) {
            //here I want to post data to sever
        };
        requstQueue.add(jsonobj);

    }


}
