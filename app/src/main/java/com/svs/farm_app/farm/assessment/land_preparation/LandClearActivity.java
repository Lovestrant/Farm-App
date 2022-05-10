/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.land_preparation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.farm.assessment.utils.FormTypes;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 
 * @author Benson
 */
public class LandClearActivity extends BaseClass implements View.OnClickListener , DatePickerDialog.OnDateSetListener {
	private Date date;
	private GPSTracker gpsTracker;
	private DatabaseHandler db;
	private Intent next;
	private double latitude;
	private double longitude;
	private String familyHours;
	private String hiredHours;
	private String moneyOut;
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
	private LandClearActivity mContext;
	private String farmID;
	private String companyID;
	private String userID;
	private SimpleDateFormat dateFormat;
	private String activityDate ="";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_master_form);
		ButterKnife.bind(this);

		mContext = LandClearActivity.this;

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initView();
		initListeners();
		initData();
	}

	private void initView() {
		bBack.setText("Back");
		bNext.setText("Next");
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
            List<FarmAssFormsMajor> forms = db.getAllFormsMajor();
            for (FarmAssFormsMajor cn : forms) {
				Log.i("FarmAssFormsMajor Next:",
                        "" + cn.getFarmId() + " " + cn.getFormTypeId());
            }

			familyHours = etFamilyHours.getText().toString();
			hiredHours = etHiredHours.getText().toString();
			moneyOut = etMoneyOut.getText().toString();
			String remarks = remarksArray[spRemarks.getSelectedItemPosition()];

			if (!"".equals(activityDate.trim())
					&& !"".equals(familyHours.trim())
					&& !"".equals(hiredHours.trim())
					&& !"".equals(moneyOut.trim())
					&& !"".equals(remarks.trim())) {
				db.addFarmAssMajor(new FarmAssFormsMajor(farmID, FormTypes.LAND_CLEARING,
						"none", activityDate, familyHours, hiredHours,
						moneyOut, remarks, userID, collectDate, String
								.valueOf(latitude), String.valueOf(longitude), companyID));
				startActivity(next);
				//MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);
			} else {
				Toast.makeText(mContext,R.string.fill_in_all_details, Toast.LENGTH_LONG).show();
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
		db = new DatabaseHandler(mContext);

		farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		previous = new Intent(mContext,
				AllFarmAssessmentActivity.class);
		previous.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		next = new Intent(mContext, PloughingActivity.class);
		
		date = new Date();
		collectDate = dateFormat.format(date);

		if (gpsTracker.canGetLocation) {
			latitude = gpsTracker.getLatitude();
			longitude = gpsTracker.getLongitude();
		}
		remarksArray = new String[]{"5","4","3","2","1","0"};
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		int month = (monthOfYear + 1);
		activityDate = year + "-" + month + "-" + dayOfMonth;
		tvActualDate.setText( dayOfMonth+ "-" + month + "-" + year);
	}
}
