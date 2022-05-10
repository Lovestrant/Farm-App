package com.svs.farm_app.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.entities.UserVillage;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.main.update_farm_area.UpdateFarmEstimateActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyData;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.svs.farm_app.searchable.SearchableSpinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FarmAssPlotSelectionActitivty extends BaseClass {

    private final String TAG = FarmAssPlotSelectionActitivty.class.getSimpleName();
    private DatabaseHandler db;
    private MyData[] farmData;
    private ArrayAdapter<String> farmsAdapter;
    private String genID;
    private String farmID;
    private Intent to_which;
    private String whereTo;
    private FarmAssPlotSelectionActitivty mContext;
    @BindView(R.id.spFarms)
    SearchableSpinner sPlots;
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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = FarmAssPlotSelectionActitivty.this;
        initData();
        initListeners();
        loadFarms();
    }

    private void loadFarms() {
        List<UserVillage> userVillages = db.getVillageIdByUserId(Preferences.USER_ID);

        String[] villagesArray = new String[userVillages.size()];

        int j = 0;
        for (UserVillage cn : userVillages) {
            villagesArray[j] = cn.getVillageId();
            j++;
        }
        String villages = TextUtils.join(",", villagesArray);

        List<Farm> farmsList = db.getAllFarmsForAss(villages);

        farmsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item);

        farmData = new MyData[farmsList.size()];

        int i = 0;
        for (Farm r : farmsList) {
            farmData[i] = new MyData(r.getFarmName(), r.getFarmID(),
                    r.getEstimatedFarmArea());

            String farmersName = db.getFarmerName(r.getFarmName());
            farmsAdapter.add(farmersName + "\n Area: "+ farmData[i].getFarmSize());

            String log = "Farm Id: " + r.getFarmID() + " ,Farm name: "
                    + r.getFarmAss() + " ,Farm Size: "
                    + r.getEstimatedFarmArea();
            Log.e(TAG, log);
            i++;
        }

        farmsAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        farmsAdapter.notifyDataSetChanged();
        sPlots.setAdapter(farmsAdapter);
        next.setEnabled(true);
    }

    private void initListeners() {
        sPlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                MyData d = farmData[position];
                farmID = d.getFarmID().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whereTo.equals("farm_update")) {
                    Intent update_farm_est = new Intent(mContext, UpdateFarmEstimateActivity.class);

                    MyPrefrences.savePrefrence(mContext, "farm_id", farmID);
                    startActivity(update_farm_est);
                } else if (whereTo.equals("planimeter")) {

                    Intent planimeter = getPackageManager().getLaunchIntentForPackage("com.vistechprojects.planimeter");
                    startActivity(planimeter);

                } else if (whereTo.equals("farm_assesment")) {
                    Intent forms = new Intent(mContext, AllFarmAssessmentActivity.class);

                    MyPrefrences.savePrefrence(mContext, "farm_id", farmID);
                    startActivity(forms);
                }
            }
        });
    }

    private void initData() {
        db = new DatabaseHandler(mContext);
        genID = MyPrefrences.getPrefrence(mContext, "val_gen_id");

        whereTo = MyPrefrences.getPrefrence(mContext, "to_which2");
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plot_selection_actitivty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
