/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.grading_and_bailing;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
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
public class BailingActivity extends BaseClass implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Date date;
    private GPSTracker gpsTracker;
    private DatabaseHandler db;
    private ConnectionDetector cd;
    private Intent next;
    private double latitude;
    private double longitude;
    //private final static String FORM_TYPE_ID = "123";
    private String familyHours;
    private String hiredHours;
    private String moneyOut;
    private String remarks;
    private String collectDate;
    private Intent previous;
    private String[] remarksArray;
    @BindView(R.id.tvActualDate)
    TextView tvActualDate;
    @BindView(R.id.bDatePicker)
    Button bActivityDate;
    @BindView(R.id.etFamilyHours)
    EditText etFamilyHours;
    @BindView(R.id.etHiredHours)
    EditText etHiredHours;
    @BindView(R.id.etMoneyOut)
    EditText etMoneyOut;
    @BindView(R.id.spRemarks)
    Spinner spRemarks;
    @BindView(R.id.bBack)
    Button bBack;
    @BindView(R.id.bNext)
    Button bNext;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String farmID;
    private String companyID;
    private String userID;
    private SimpleDateFormat dateFormat;
    private String activityDate = "";
    private BailingActivity mContext;
    private String formTypeID;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_master_form);
        ButterKnife.bind(this);

        mContext = BailingActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initListeners();
        initData();
    }

    private void initView() {
        bBack.setText("Back");
        bNext.setText("Save");
    }

    private void initListeners() {
        bBack.setOnClickListener(this);
        bNext.setOnClickListener(this);
        bActivityDate.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == bBack) {
            startActivity(previous);
        } else if (v == bNext) {
            if (gpsTracker.canGetLocation) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }

            familyHours = etFamilyHours.getText().toString();
            hiredHours = etHiredHours.getText().toString();
            moneyOut = etMoneyOut.getText().toString();
            remarks = remarksArray[spRemarks.getSelectedItemPosition()];

            if (!"".equals(activityDate.trim())
                    && !"".equals(familyHours.trim())
                    && !"".equals(hiredHours.trim())
                    && !"".equals(moneyOut.trim())
                    && !"".equals(remarks.trim())) {
                db.addFarmAssMajor(new FarmAssFormsMajor(farmID, formTypeID,
                        "none", activityDate, familyHours, hiredHours,
                        moneyOut, remarks, userID, collectDate, String
                        .valueOf(latitude), String.valueOf(longitude), companyID));
                startActivity(next);
            } else {
                Toast.makeText(mContext,
                        "Fill in all the details", Toast.LENGTH_LONG).show();
            }
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
        Intent dcIntent = getIntent();
        formTypeID = dcIntent.getStringExtra("form_type_id");

        cd = new ConnectionDetector(BailingActivity.this);
        db = new DatabaseHandler(BailingActivity.this);

        farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
        userID = Preferences.USER_ID;
        companyID = Preferences.COMPANY_ID;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gpsTracker = new GPSTracker(mContext);

        previous = new Intent(mContext, BailingActivity.class);
        previous.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        next = new Intent(mContext, AllFarmAssessmentActivity.class);
        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        date = new Date();
        collectDate = dateFormat.format(date);
        remarksArray = new String[]{"5", "4", "3", "2", "1", "0"};
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = (monthOfYear + 1);
        activityDate = year + "-" + month + "-" + dayOfMonth;
        tvActualDate.setText(dayOfMonth + "-" + month + "-" + year);
    }
}
