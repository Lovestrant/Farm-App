package com.svs.farm_app.main.calendar;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.entities.OfficerTraining;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTrainingsActivity extends BaseClass {
    private final String TAG = MyTrainingsActivity.class.getSimpleName();

    private String trainDate;
    private DatabaseHandler db;
    private ArrayAdapter<MyData> dataAdapter;
    private MyData[] trainingData;

    @BindView(R.id.lvTrainings)
    ListView lvTrainings;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MyTrainingsActivity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trainings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = MyTrainingsActivity.this;

        initData();
    }

    private void initData() {
        Intent intent = getIntent();

        db = new DatabaseHandler(mContext);
        trainDate = intent.getStringExtra("date");

        Log.i(TAG, "DATE: " + trainDate);

        List<OfficerTraining> trainingList = db.getUserTrainingsByDate(trainDate, Preferences.USER_ID);

        Log.i(TAG,"SIZE: "+trainingList.size());

        trainingData = new MyData[trainingList.size()];

        int i = 0;
        for (OfficerTraining training : trainingList) {
            trainingData[i] = new MyData(training.getTrainCat(), training.getFarmID(), training.getFarmerID());
            i++;
        }

        try {
            dataAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.list_item_my_training);
            dataAdapter.addAll(trainingData);

            lvTrainings.setAdapter(dataAdapter);

            lvTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                    try {
                        Farm farm = db.getFarmsLatLng(trainingData[position].getFarmID());

                        RegisteredFarmer farmer = db.getFarmerDetails(trainingData[position].getFarmerID());

                        String latitude = farm.getLatitude();
                        String longitude = farm.getLongitude();
                        String firstName = farmer.getFirstName();
                        String lastName = farmer.getLastName();

                        Log.i(TAG, "Farm ID: "+trainingData[position].getFarmID());

                        Intent farmLocation = new Intent(MyTrainingsActivity.this, FarmLocationActivity.class);
                        farmLocation.putExtra("latitude", latitude);
                        farmLocation.putExtra("longitude", longitude);
                        farmLocation.putExtra("lname", lastName);
                        farmLocation.putExtra("fname", firstName);

                        startActivity(farmLocation);
                    } catch (Exception ex) {
                        Log.e(TAG, "" + ex.getMessage());
                    }

                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class MyData {

        String trainCat;
        String farmID;
        String farmerID;

        public MyData(String trainCat, String farmID, String farmerID) {
            this.trainCat = trainCat;
            this.farmID = farmID;
            this.farmerID = farmerID;
        }

        public String getTrainCat() {
            return trainCat;
        }

        public String getFarmID() {
            return farmID;
        }

        public String getFarmerID() {
            return farmerID;
        }
        @Override
        public String toString() {
            return trainCat;
        }
    }
}
