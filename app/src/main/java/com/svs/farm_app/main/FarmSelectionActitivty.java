package com.svs.farm_app.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.main.mapping.MappingActivity;
import com.svs.farm_app.main.update_farm_area.UpdateFarmEstimateActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DataUtils;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.MyArrayAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FarmSelectionActitivty extends BaseClass {
    private final String TAG = FarmSelectionActitivty.class.getSimpleName();
    private DatabaseHandler db;
    private MyData[] farmData;
    private MyArrayAdapter farmsAdapter;
    private String farmerId;
    private FarmSelectionActitivty mContext;
    private String toWhich;
    private String genId;
    private String farmID;

    @BindView(R.id.spFarms)
    Spinner spFarms;
    @BindView(R.id.bNext)
    Button next;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_selection);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        mContext = FarmSelectionActitivty.this;
        initData();
        initListeners();
        if(farmerId!=null)
            loadFarms(farmerId);
    }

    /**
     * Load farms in spinner
     *
     * @param farmerID
     */
    private void loadFarms(String farmerID) {

        List<Farm> farmsList = db.getFarmsByFarmer(farmerID);

        if (farmsList.size() > 0) {
            farmsAdapter = new MyArrayAdapter(mContext, android.R.layout.simple_spinner_item);

            farmData = new MyData[farmsList.size()];

            int i = 0;
            for (Farm f : farmsList) {
                farmData[i] = new MyData(f.getFarmID(), f.getEstimatedFarmArea());

                String log = "Farm ID: " + f.getFarmID() + " ,Farm Name: " + f.getFarmName() + " ,Farm Size: " + f.getEstimatedFarmArea();
                Log.e(TAG, "Farms List: " + log);
                i++;
            }

            farmsAdapter.addAll(farmData);
            farmsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFarms.setAdapter(farmsAdapter);

            next.setEnabled(true);
        } else {
            MyAlerts.genericDialog(mContext, R.string.farms_not_found);
            next.setEnabled(false);
        }
    }

    private void initListeners() {

        spFarms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.e(TAG, "onItemSelected: "+parent.getAdapter().getItem(position).getClass() );

//                MyData data = farmData[position];
                MyData data = (MyData) parent.getAdapter().getItem(position);
                farmID = data.getFarmID().toString();
                Log.i(TAG, "farmID: " + farmID);
                DataUtils.copyToClipBoard("farm_id", farmID, mContext);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toWhich.equals("farm_update")) {

                    Intent updateFarmEst = new Intent(mContext, UpdateFarmEstimateActivity.class);
                    updateFarmEst.putExtra("farm_id", farmID);
                    startActivity(updateFarmEst);

                } else if (toWhich.equals("mapping")) {

                    // db.addMappedFarm(new MappedFarm(farmID, Preferences.USER_ID, Preferences.COMPANY_ID));

//                    List<MappedFarm> kmlmetadata = db.getMappedFarms();
//                    for (MappedFarm cn : kmlmetadata) {
//                        String log = "FID: " + cn.getFID() + " ,USERID: "
//                                + cn.getUserId() + " ,Farm_id: "
//                                + cn.getFarmId();
//                        Log.i(TAG, "Farm Data: " + log);
//                    }
//                    Intent planimeter = getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    "com.vistechprojects.planimeter");

                    Intent mapping = new Intent(mContext, MappingActivity.class);
                    mapping.putExtra("farm_id", farmID);

                    // try {
                    startActivity(mapping);
//                    } catch (Exception ex) {
//                        MyAlerts.genericDialog(mContext, R.string.install_planimeter);
//                    }
                } else {
                    Intent forms = new Intent(mContext, AllFarmAssessmentActivity.class);
                    MyPrefrences.savePrefrence(mContext, "farm_id", farmID);
                    startActivity(forms);
                }
            }
        });
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        Intent intent = getIntent();
        toWhich = intent.getStringExtra("to_which");
        farmerId = intent.getStringExtra("farmer_id");
        genId = intent.getStringExtra("gen_id");
        Log.i(TAG, "savedInstanceState == null");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("to_which", toWhich);
        outState.putString("farmer_id", farmerId);
        outState.putString("gen_id", genId);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume  " + (farmerId == null));
        if (farmerId == null) {
            farmerId = MyPrefrences.getPrefrence(mContext, "select_farmer_id");
            toWhich = MyPrefrences.getPrefrence(mContext, "select_to_which");
            genId = MyPrefrences.getPrefrence(mContext, "select_gen_id");

            Log.i(TAG, "onResume  " + farmerId);

            loadFarms(farmerId);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        MyPrefrences.savePrefrence(mContext, "select_farmer_id", farmerId);
        MyPrefrences.savePrefrence(mContext, "select_to_which", toWhich);
        MyPrefrences.savePrefrence(mContext, "select_gen_id", genId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        toWhich = savedInstanceState.getString("to_which");
        farmerId = savedInstanceState.getString("farmer_id");
        genId = savedInstanceState.getString("gen_id");
        Log.i(TAG, "onSaveInstanceState " + savedInstanceState.getString("farmer_id"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    /**
     * Class to map farm data in spinner
     */
    private class MyData {

        private String farmID, farmSize;

        public MyData(String farmID, String farmSize) {
            this.farmID = farmID;
            this.farmSize = farmSize;
        }

        public String getFarmID() {
            return farmID;
        }

        @Override
        public String toString() {
            return farmSize + " Hectares";
        }
    }

}
