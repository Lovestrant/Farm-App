/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.PlantingRains;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Benson
 */
public class GoodRainsActivity extends BaseClass implements OnClickListener, DatePickerDialog.OnDateSetListener {
    private String userID;
    private String companyID;
    private String farmID;
    private SimpleDateFormat dateFormat;
    private Date d1;
    private GPSTracker gpsTracker;
    private double latitude;
    private double longitude;
    private DatabaseHandler db;
    private ConnectionDetector cd;
    private Intent farmAssessment;
    private String[] remarksArray;
    private GoodRainsActivity mContext;
    @BindView(R.id.layout_middle)
    LinearLayout layout;
    @BindView(R.id.tvActualDate)
    TextView tvActualDate;
    @BindView(R.id.bDatePicker)
    Button bActivityDate;
    @BindView(R.id.spRemarks)
    Spinner spRemarks;
    @BindView(R.id.bBack)
    Button bSave;
    @BindView(R.id.bNext)
    Button bCancel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String activityDate = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_master_form);
        ButterKnife.bind(this);

        mContext = GoodRainsActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initListeners();
        initData();
    }

    private void initView() {
        layout.setVisibility(View.GONE);
        bSave.setText("Save");
        bCancel.setText("Cancel");
    }

    private void initListeners() {
        bSave.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        bActivityDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bSave) {
            d1 = new Date();
            String collectionDate = dateFormat.format(d1);

            if (gpsTracker.canGetLocation) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }
            String remarks = remarksArray[spRemarks.getSelectedItemPosition()];

            if (!remarks.trim().isEmpty() && !activityDate.trim().isEmpty()) {
                db.addPlantingRains(new PlantingRains(farmID, activityDate,
                        collectionDate, remarks, userID, String.valueOf(latitude),
                        String.valueOf(longitude), companyID));
                Log.e("save", "remarks " + remarks + " date " + activityDate);
                startActivity(farmAssessment);
            } else {
                Toast.makeText(mContext,
                        "Fill in all the fields", Toast.LENGTH_SHORT).show();
            }
        } else if (v == bCancel) {
            startActivity(farmAssessment);
        } else if (v == bActivityDate) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    mContext,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.setMaxDate(now);
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        }
    }

    private void initData() {
        cd = new ConnectionDetector(mContext);

        db = new DatabaseHandler(mContext);

        farmID = MyPrefrences.getPrefrence(mContext, "farm_id");

        userID = Preferences.USER_ID;

        companyID = Preferences.COMPANY_ID;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gpsTracker = new GPSTracker(mContext);

        farmAssessment = new Intent(mContext,AllFarmAssessmentActivity.class);
        farmAssessment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        remarksArray = new String[]{"5", "4", "3", "2", "1", "0"};
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = (monthOfYear + 1);
        activityDate = year + "-" + month + "-" + dayOfMonth;
        tvActualDate.setText( dayOfMonth+ "-" + month + "-" + year);
    }
}
