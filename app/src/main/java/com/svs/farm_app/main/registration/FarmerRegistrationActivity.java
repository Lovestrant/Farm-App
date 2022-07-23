package com.svs.farm_app.main.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.OtherCrops;
import com.svs.farm_app.entities.SubVillage;
import com.svs.farm_app.entities.Village;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyPrefrences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 12/19/2014.
 */
public class FarmerRegistrationActivity extends BaseClass {

    private static final String TAG = FarmerRegistrationActivity.class.getSimpleName();
    @BindView(R.id.etFName)
    androidx.appcompat.widget.AppCompatEditText etFName;
    @BindView(R.id.etMName)
    androidx.appcompat.widget.AppCompatEditText etMName;
    @BindView(R.id.etLName)
    androidx.appcompat.widget.AppCompatEditText etLName;
    @BindView(R.id.etIDNum)
    androidx.appcompat.widget.AppCompatEditText etIDNum;
    @BindView(R.id.etPhone)
    androidx.appcompat.widget.AppCompatEditText etPhone;
    @BindView(R.id.etEmail)
    androidx.appcompat.widget.AppCompatEditText etEmail;
    @BindView(R.id.spGender)
    androidx.appcompat.widget.AppCompatSpinner spGender;
    @BindView(R.id.etPostalAddress)
    androidx.appcompat.widget.AppCompatEditText etPostalAddress;

    @BindView(R.id.spVillage)
    androidx.appcompat.widget.AppCompatSpinner spVillage;
    @BindView(R.id.spSubVillage)
    androidx.appcompat.widget.AppCompatSpinner spSubVillage;
    @BindView(R.id.spOtherCropsOne)
    androidx.appcompat.widget.AppCompatSpinner spOtherCropsOne;
    @BindView(R.id.spOtherCropsTwo)
    androidx.appcompat.widget.AppCompatSpinner spOtherCropsTwo;
    @BindView(R.id.spOtherCropsThree)
    androidx.appcompat.widget.AppCompatSpinner spOtherCropsThree;
    @BindView(R.id.etFarmAreaOne)
    androidx.appcompat.widget.AppCompatEditText etFarmAreaOne;
    @BindView(R.id.etFarmAreaTwo)
    androidx.appcompat.widget.AppCompatEditText etFarmAreaTwo;
    @BindView(R.id.etFarmAreaThree)
    androidx.appcompat.widget.AppCompatEditText etFarmAreaThree;
    @BindView(R.id.etFarmAreaFour)
    androidx.appcompat.widget.AppCompatEditText etFarmAreaFour;
    @BindView(R.id.cbShowIntent)
    androidx.appcompat.widget.AppCompatCheckBox cbShowIntent;
    @BindView(R.id.spFarmVillageOne)
    androidx.appcompat.widget.AppCompatSpinner spFarmVillageOne;
    @BindView(R.id.spFarmVillageTwo)
    androidx.appcompat.widget.AppCompatSpinner spFarmVillageTwo;
    @BindView(R.id.spFarmVillageThree)
    androidx.appcompat.widget.AppCompatSpinner spFarmVillageThree;
    @BindView(R.id.spFarmVillageFour)
    androidx.appcompat.widget.AppCompatSpinner spFarmVillageFour;
    @BindView(R.id.bRegister)
    FloatingActionButton bRegister;
    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;

    String firstName;
    String middleName;
    String lastName;
    String gender;
    String subVillageName;
    String email;
    String villageName;
    String postalAddress;
    String IDNumber, phone, estimateFarmAreaOne;
    ConnectionDetector cd;
    DatabaseHandler db;
    private String fingerPath;
    private String villageID, subVillageID;
    private String farmerPic;
    private ArrayAdapter<MyData> villagesAdapter;
    private ArrayAdapter<MyData> subVillagesDataAdapter;

    private MyData[] farmVillageTwoData;
    private MyData[] farmVillageThreeData;
    private MyData[] farmVillageFourData;
    private MyData[] villagesData;
    private MyData[] subVillagesData;
    private double longitude, latitude;
    private GPSTracker gpsTracker;
    private Date date;
    private SimpleDateFormat sdf;

    private String estimateFarmAreaTwo;
    private String estimateFarmAreaThree;
    private String estimateFarmAreaFour;

    private String showIntent;

    protected String farmVillageNameOneThree;
    protected String farmVillageNameFour;
    protected String farmVillageNameTwo;
    protected String farmVillageNameOne;
    protected String farmVillageIDOne, farmVillageIDTwo, farmVillageIDThree, farmVillageIDFour;
    private MyData[] farmVillageOneData;
    private ArrayAdapter<MyData> farmVillageOneAdapter;
    private ArrayAdapter<MyData> farmVillageTwoAdapter;
    private ArrayAdapter<MyData> farmVillageThreeAdapter;
    private ArrayAdapter<MyData> farmVillageFourAdapter;

    private ArrayAdapter<MyData> otherCropsOneAdapter, otherCropsTwoAdapter, otherCropsThreeAdapter;
    private MyData[] otherCropsOneData, otherCropsTwoData, otherCropsThreeData;
    protected String otherCropsNameThree;
    protected String otherCropsIDThree;
    protected String otherCropsIDTwo;
    protected String otherCropsNameTwo;
    protected String otherCropsIDOne;
    protected String otherCropsNameOne;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_farmer2);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = FarmerRegistrationActivity.this;

        db = new DatabaseHandler(mContext);

        initListeners();
        loadVillages();
        loadCrops();
    }

    private void loadVillages() {
        List<Village> villageList = db.getVillages();
        villagesAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        villagesData = new MyData[villageList.size()];
        farmVillageOneAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        farmVillageOneData = new MyData[villageList.size()];
        farmVillageTwoAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        farmVillageTwoData = new MyData[villageList.size()];
        farmVillageThreeAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        farmVillageThreeData = new MyData[villageList.size()];
        farmVillageFourAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        farmVillageFourData = new MyData[villageList.size()];

        int i = 0;
        for (Village r : villageList) {
            farmVillageOneData[i] = new MyData(r.getVillageName(), r.getVillageID());
            farmVillageTwoData[i] = new MyData(r.getVillageName(), r.getVillageID());
            farmVillageThreeData[i] = new MyData(r.getVillageName(), r.getVillageID());
            farmVillageFourData[i] = new MyData(r.getVillageName(), r.getVillageID());
            villagesData[i] = new MyData(r.getVillageName(), r.getVillageID());

            Log.e(TAG, villagesData[i].getValue());

            String log = "Village ID: " + r.getVillageID() + " ,Village Name: "
                    + r.getVillageName();
            Log.e(TAG, log);
            i++;
        }

        villagesAdapter.addAll(villagesData);
        farmVillageOneAdapter.addAll(farmVillageOneData);
        farmVillageTwoAdapter.addAll(farmVillageTwoData);
        farmVillageThreeAdapter.addAll(farmVillageThreeData);
        farmVillageFourAdapter.addAll(farmVillageFourData);
        villagesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villagesAdapter.notifyDataSetChanged();
        farmVillageOneAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        farmVillageOneAdapter.notifyDataSetChanged();
        farmVillageTwoAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        farmVillageTwoAdapter.notifyDataSetChanged();

        farmVillageThreeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        farmVillageThreeAdapter.notifyDataSetChanged();
        farmVillageFourAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        farmVillageFourAdapter.notifyDataSetChanged();

        spVillage.setAdapter(villagesAdapter);
        spFarmVillageOne.setAdapter(farmVillageOneAdapter);
        spFarmVillageTwo.setAdapter(farmVillageTwoAdapter);
        spFarmVillageThree.setAdapter(farmVillageThreeAdapter);
        spFarmVillageFour.setAdapter(farmVillageFourAdapter);
    }

    private void loadSubVillages(String d) {
        List<SubVillage> subVillageList = db.getDynamicSubVillages(d);

        subVillagesDataAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        subVillagesData = new MyData[subVillageList.size()];

        int i = 0;
        for (SubVillage r : subVillageList) {

            subVillagesData[i] = new MyData(r.getSubVillageName(),
                    r.getSubVillageID());
            Log.e(TAG, subVillagesData[i].getValue());

            String log = "SubVillage ID: " + r.getSubVillageID()
                    + " ,SubVillage name: " + r.getSubVillageName();
            Log.e(TAG, log);
            i++;
        }

        subVillagesDataAdapter.addAll(subVillagesData);
        subVillagesDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subVillagesDataAdapter.notifyDataSetChanged();
        spSubVillage.setAdapter(subVillagesDataAdapter);
    }

    private void loadCrops() {
        List<OtherCrops> cropsList = db.allOtherCrops();

        otherCropsOneAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        otherCropsTwoAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        otherCropsThreeAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);

        otherCropsOneData = new MyData[cropsList.size()];
        otherCropsTwoData = new MyData[cropsList.size()];
        otherCropsThreeData = new MyData[cropsList.size()];

        int i = 0;
        for (OtherCrops r : cropsList) {
            if (i < cropsList.size()) {
                otherCropsOneData[i] = new MyData(r.getCropName(), r.getCropID());
                otherCropsTwoData[i] = new MyData(r.getCropName(), r.getCropID());
                otherCropsThreeData[i] = new MyData(r.getCropName(), r.getCropID());
            }

            i++;
        }

        otherCropsOneAdapter.addAll(otherCropsOneData);
        otherCropsOneAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        otherCropsTwoAdapter.addAll(otherCropsTwoData);
        otherCropsTwoAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        otherCropsThreeAdapter.addAll(otherCropsThreeData);
        otherCropsThreeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        otherCropsOneAdapter.notifyDataSetChanged();
        spOtherCropsOne.setAdapter(otherCropsOneAdapter);
        spOtherCropsTwo.setAdapter(otherCropsTwoAdapter);
        spOtherCropsThree.setAdapter(otherCropsThreeAdapter);
    }

    public void initListeners() {
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                gender = spGender.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                villageName = spVillage.getItemAtPosition(position).toString();
                MyData d = villagesData[position];
                villageID = d.getValue();
                Log.i(TAG, "" + d);

                loadSubVillages(villageID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSubVillage
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        subVillageName = spSubVillage.getItemAtPosition(position)
                                .toString();
                        MyData d = subVillagesData[position];
                        subVillageID = d.getValue();
                        Log.i(TAG, "" + d);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spFarmVillageOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                farmVillageNameOne = spFarmVillageOne.getItemAtPosition(position).toString();
                MyData d = farmVillageOneData[position];
                farmVillageIDOne = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spFarmVillageTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                farmVillageNameTwo = spFarmVillageTwo.getItemAtPosition(position).toString();
                MyData d = farmVillageTwoData[position];
                farmVillageIDTwo = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spFarmVillageThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                farmVillageNameOneThree = spFarmVillageThree.getItemAtPosition(position).toString();
                MyData d = farmVillageThreeData[position];
                farmVillageIDThree = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spFarmVillageFour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                farmVillageNameFour = spFarmVillageFour.getItemAtPosition(position).toString();
                MyData d = farmVillageFourData[position];
                farmVillageIDFour = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOtherCropsOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                otherCropsNameOne = spOtherCropsOne.getItemAtPosition(position).toString();
                MyData d = otherCropsOneData[position];
                otherCropsIDOne = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spOtherCropsTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                otherCropsNameTwo = spOtherCropsTwo.getItemAtPosition(position).toString();
                MyData d = otherCropsTwoData[position];
                otherCropsIDTwo = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spOtherCropsThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                otherCropsNameThree = spOtherCropsThree.getItemAtPosition(position).toString();
                MyData d = otherCropsThreeData[position];
                otherCropsIDThree = d.getValue();
                Log.i(TAG, "" + d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // if (cd.isConnectingToInternet() == true) {
                if (cbShowIntent.isChecked()) {
                    showIntent = "shows intent";
                } else {
                    showIntent = "not contracted";
                }
                firstName = etFName.getText().toString();
                middleName =etMName.getText().toString();
                lastName = etLName.getText().toString();
                gender = spGender.getSelectedItem().toString();
                IDNumber = etIDNum.getText().toString();
                phone = etPhone.getText().toString();
                email = etEmail.getText().toString();
                postalAddress = etPostalAddress.getText().toString();
                estimateFarmAreaOne = etFarmAreaOne.getText().toString();
                estimateFarmAreaTwo = etFarmAreaTwo.getText().toString();
                estimateFarmAreaThree = etFarmAreaThree.getText().toString();
                estimateFarmAreaFour = etFarmAreaFour.getText().toString();

                if (firstName.trim().length() == 0 || lastName.trim().length() == 0
                        || estimateFarmAreaOne.trim().length() == 0 || Double.parseDouble(estimateFarmAreaOne) <= 0) {

                    new MaterialStyledDialog.Builder(FarmerRegistrationActivity.this)
                            .setTitle(R.string.app_name)
                            .setStyle(Style.HEADER_WITH_TITLE)
                            .setDescription(R.string.fill_in_all_details)
                            .setCancelable(true)
                            .setPositiveText(R.string.ok)
                            .show();
                } else {
                    launchUploadActivity();
                }
            }
        });

        // set min and max farm area
        etFarmAreaOne.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    if (etFarmAreaOne.getText().toString() == "") {
                        etFarmAreaOne.setText("0");
                    }
                    if (Float.parseFloat(etFarmAreaOne.getText().toString()) > 20) {
                        etFarmAreaOne.setText("20");
                    }
                } catch (Exception ex) {

                }
            }
        });

        etFarmAreaTwo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    if (etFarmAreaTwo.getText().toString() == "") {
                        etFarmAreaTwo.setText("0");
                    }
                    if (Float.parseFloat(etFarmAreaTwo.getText().toString()) > 20) {
                        etFarmAreaTwo.setText("20");
                    }
                } catch (Exception ex) {

                }
            }
        });

        etFarmAreaThree.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    if (etFarmAreaThree.getText().toString() == "") {
                        etFarmAreaThree.setText("0");
                    }
                    if (Float.parseFloat(etFarmAreaThree.getText().toString()) > 20) {
                        etFarmAreaThree.setText("20");
                    }
                } catch (Exception ex) {

                }
            }
        });

        etFarmAreaFour.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    if (etFarmAreaFour.getText().toString() == "") {
                        etFarmAreaFour.setText("0");
                    }
                    if (Float.parseFloat(etFarmAreaFour.getText().toString()) > 20) {
                        etFarmAreaFour.setText("20");
                    }
                } catch (Exception ex) {

                }
            }
        });

        bRegister = findViewById(R.id.bRegister);

        cd = new ConnectionDetector(mContext);
        gpsTracker = new GPSTracker(mContext);
        // isInternetPresent = cd.isConnectingToInternet();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();

    }

    private void launchUploadActivity() {
        if (gpsTracker.canGetLocation) {


            Intent i = new Intent(mContext, UploadActivity.class);

            i = attachFarmerDetails(i);

            Log.e("latitude: ", String.valueOf(latitude));
            Log.e("long: ", String.valueOf(longitude));
            gpsTracker.stopUsingGPS();
            startActivity(i);
        } else {
            // turn on gps
            gpsTracker.showSettingsAlert(FarmerRegistrationActivity.this);

            Intent i = new Intent(mContext, UploadActivity.class);

            i = attachFarmerDetails(i);

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();


            Log.e("latitude: ", String.valueOf(latitude));
            Log.e("long: ", String.valueOf(longitude));
            gpsTracker.stopUsingGPS();
//            Intent r = new Intent("android.location.GPS_ENABLED_CHANGE");
//            r.putExtra("enabled", false);
//            sendBroadcast(r);
            startActivity(i);
        }
    }

    private Intent attachFarmerDetails(Intent i) {

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        fingerPath = MyPrefrences.getPrefrence(mContext, "finger_path");

        farmerPic = MyPrefrences.getPrefrence(mContext, "farmer_pic");

        i.putExtra("finger_path", fingerPath);
        i.putExtra("farmer_pic", farmerPic);
        i.putExtra("fname", firstName);
        i.putExtra("lname", middleName);
        i.putExtra("lname", lastName);
        i.putExtra("gender", gender);
        i.putExtra("id_no", IDNumber);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        i.putExtra("postal_address", postalAddress);
        i.putExtra("village_name", villageName);
        i.putExtra("village_id", villageID);
        i.putExtra("sub_village_name", subVillageName);
        i.putExtra("sub_village_id", subVillageID);
        i.putExtra("farm_village_name_one", farmVillageNameOne);
        i.putExtra("farm_village_id_one", farmVillageIDOne);
        i.putExtra("farm_village_name_two", farmVillageNameTwo);
        i.putExtra("farm_village_id_two", farmVillageIDTwo);
        i.putExtra("farm_village_name_three", farmVillageNameOneThree);
        i.putExtra("farm_village_id_three", farmVillageIDThree);
        i.putExtra("farm_village_name_four", farmVillageNameFour);
        i.putExtra("farm_village_id_four", farmVillageIDFour);
        i.putExtra("other_crops_name_one", otherCropsNameOne);
        i.putExtra("other_crops_id_one", otherCropsIDOne);
        i.putExtra("other_crops_name_two", otherCropsNameTwo);
        i.putExtra("other_crops_id_two", otherCropsIDTwo);
        i.putExtra("other_crops_name_three", otherCropsNameThree);
        i.putExtra("other_crops_id_three", otherCropsIDThree);
        i.putExtra("show_intent", showIntent);
        i.putExtra("farm_area_one", estimateFarmAreaOne);
        i.putExtra("farm_area_two", estimateFarmAreaTwo);
        i.putExtra("farm_area_three", estimateFarmAreaThree);
        i.putExtra("farm_area_four", estimateFarmAreaFour);
        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        i.putExtra("reg_date", sdf.format(date));

        return i;
    }

    private class MyData {

        String spinnerText;
        String value;

        public MyData(String spinnerText, String value) {
            this.spinnerText = spinnerText;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return spinnerText;
        }
    }
}
