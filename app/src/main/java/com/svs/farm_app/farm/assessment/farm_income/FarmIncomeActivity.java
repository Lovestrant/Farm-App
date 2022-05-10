/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.farm_income;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmIncome;
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
 * 
 * @author Benson
 */
public class FarmIncomeActivity extends BaseClass implements
		View.OnClickListener , DatePickerDialog.OnDateSetListener{

	private DatabaseHandler db;
	private String farmID;
	private String userID;
	private String companyID;
	private SimpleDateFormat dateFormat;
	private GPSTracker gpsTracker;
	private Intent farmAssessment;
	private Date date;
	private String collectDate;
	private String activityDate = "";
	private double latitude;
	private double longitude;
	private String deliveryCount;
	private FarmIncomeActivity mContext;
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

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_picking);
		ButterKnife.bind(this);

		mContext = FarmIncomeActivity.this;

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

	private void initData() {
		Intent dcIntent = getIntent();
		deliveryCount = dcIntent.getStringExtra("delivery_count");
		db = new DatabaseHandler(mContext);

		farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		farmAssessment = new Intent(mContext,AllFarmAssessmentActivity.class);
		farmAssessment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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
			if (!"".equals(activityDate.trim())
					&& !activityDate.trim().isEmpty()
					&& !collectDate.trim().isEmpty()
					&& !gradeA.trim().isEmpty() && !gradeB.trim().isEmpty()
					&& !gradeC.trim().isEmpty()) {
				db.addFarmIncome(new FarmIncome(farmID,deliveryCount, gradeA, gradeB, gradeC,
						activityDate, collectDate, String.valueOf(latitude), String
								.valueOf(longitude), userID, companyID));

				MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);

			} else {
				Toast.makeText(mContext,
						R.string.fill_in_all_details, Toast.LENGTH_SHORT).show();
			}
		} else if (v == bBack) {
			startActivity(new Intent(mContext,
					AllFarmAssessmentActivity.class));
		}else if (v == bActivityDate) {
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
		tvActualDate.setText( dayOfMonth+ "-" + month + "-" + year);
	}

}
