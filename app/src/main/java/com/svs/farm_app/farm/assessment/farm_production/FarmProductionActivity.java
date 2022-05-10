/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.farm_production;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmProduction;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyAlerts;
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
public class FarmProductionActivity extends BaseClass implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private DatabaseHandler db;
    private GPSTracker gpsTracker;
    private Intent newf;
    private Date date;
    private String collectDate;
    private double latitude;
    private double longitude;
    private String pickingCount;
    @BindView(R.id.tvActualDate)
    TextView tvActualDate;
    @BindView(R.id.bDatePicker)
    Button bActivityDate;
    @BindView(R.id.etGradeA)
    EditText etGradeA;
    @BindView(R.id.etGradeB)
    EditText etGradeB;
    @BindView(R.id.etGradeC)
    EditText etGradeC;
    @BindView(R.id.bBack)
    Button bBack;
    @BindView(R.id.bNext)
    Button bNext;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FarmProductionActivity mContext;
    private String farmId;
    private String companyId;
    private String userId;
    private SimpleDateFormat dateFormat;
    private String activityDate = "";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_picking);
        ButterKnife.bind(this);

        mContext = FarmProductionActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
        setToolbarTitle();
        initListeners();
    }

    private void setToolbarTitle() {
        ActionBar actionBar = getSupportActionBar();
        switch (pickingCount) {
            case AllFarmAssessmentActivity.FIRST_PICKING:
                actionBar.setTitle(R.string.farm_production_one);
                break;
            case AllFarmAssessmentActivity.SECOND_PICKING:
                actionBar.setTitle(R.string.farm_production_two);
                break;
            case AllFarmAssessmentActivity.THIRD_PICKING:
                actionBar.setTitle(R.string.farm_production_three);
                break;
            case AllFarmAssessmentActivity.FOURTH_PICKING:
                actionBar.setTitle(R.string.farm_production_four);
                break;
        }
    }

    private void initView() {
        bBack.setText("Back");
        bNext.setText("Save");
    }

    private void initData() {
        Intent pcIntent = getIntent();
        pickingCount = pcIntent.getStringExtra("picking_count");
        db = new DatabaseHandler(FarmProductionActivity.this);

        farmId = MyPrefrences.getPrefrence(mContext, "farm_id");
        userId = Preferences.USER_ID;
        companyId = Preferences.COMPANY_ID;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gpsTracker = new GPSTracker(mContext);

        newf = new Intent(mContext, AllFarmAssessmentActivity.class);
        newf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        date = new Date();
        collectDate = dateFormat.format(date);

    }

    private void initListeners() {
        bBack.setOnClickListener(this);
        bNext.setOnClickListener(this);
        bActivityDate.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == bNext) {
            if (gpsTracker.canGetLocation) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }

            String gradeA = etGradeA.getText().toString();
            String gradeB = etGradeB.getText().toString();
            String gradeC = etGradeC.getText().toString();

            if (!"".equals(activityDate.trim()) && !pickingCount.trim().isEmpty()
                    && !collectDate.trim().isEmpty()
                    && !gradeA.trim().isEmpty() && !gradeB.trim().isEmpty()
                    && !gradeC.trim().isEmpty()) {
                db.addFarmProduction(new FarmProduction(farmId, pickingCount, gradeA, gradeB, gradeC,
                        activityDate, collectDate, String.valueOf(latitude), String
                        .valueOf(longitude), userId, companyId));

                MyAlerts.backToAllFarmAssessmentsDialog(mContext, R.string.saved_offline);

            } else {
                Toast.makeText(mContext,
                        R.string.fill_in_all_details, Toast.LENGTH_SHORT).show();
            }
        } else if (v == bBack) {
            startActivity(new Intent(FarmProductionActivity.this,
                    AllFarmAssessmentActivity.class));
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = (monthOfYear + 1);
        activityDate = year + "-" + month + "-" + dayOfMonth;
        tvActualDate.setText(dayOfMonth + "-" + month + "-" + year);
    }
}
