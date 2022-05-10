/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.molasses;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.MolassesTrapCatch;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.ConnectionDetector;
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
public class MolassesActivity extends BaseClass implements OnClickListener , DatePickerDialog.OnDateSetListener{

	private Date date;
	private GPSTracker gpsTracker;
	private DatabaseHandler db;
	private ConnectionDetector cd;
	private String farmID;
	private String userID;
	private String companyID;
	private double latitude;
	private double longitude;
	private String activityDate;
	private String collectDate;
	private Intent previous;
	private String trapOne;
	private String trapTwo;
	private String action;
	private SimpleDateFormat dateFormat;
	private MolassesActivity mContext;
	@BindView(R.id.tvActualDate)
	TextView tvActualDate;
	@BindView(R.id.bDatePicker)
	Button bActivityDate;
	@BindView(R.id.etTrapOne)
	EditText etTrapOne;
	@BindView(R.id.etTrapTwo)
	EditText etTrapTwo;
	@BindView(R.id.rbSprayed)
	RadioButton rbSprayed;
	@BindView(R.id.rbNoAction)
	RadioButton rbNoAction;
	@BindView(R.id.bBack)
	Button bBack;
	@BindView(R.id.bNext)
	Button bNext;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_molasses);
		ButterKnife.bind(this);

		mContext = MolassesActivity.this;

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
		rbSprayed.setOnClickListener(this);
		rbNoAction.setOnClickListener(this);
		bBack.setOnClickListener(this);
		bNext.setOnClickListener(this);
		bActivityDate.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == rbSprayed) {
			action = "sprayed";
			Log.d("MOLASSES:", "" + action);
		} else if (v == rbNoAction) {
			action = "no action";
			Log.d("MOLASSES:", "" + action);

		} else if (v == bBack) {
			startActivity(previous);
		} else if (v == bNext) {

			if (gpsTracker.canGetLocation) {
				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
			}

			trapOne = etTrapOne.getText().toString();
			trapTwo = etTrapTwo.getText().toString();

			if (!activityDate.trim().isEmpty() && !trapOne.trim().isEmpty() && !trapTwo.trim().isEmpty()
					&& !"".equals(action.trim())) {
				db.addMolassesTrapCatch(new MolassesTrapCatch(farmID, activityDate, trapOne, trapTwo, action, userID,
						collectDate, String.valueOf(latitude), String.valueOf(longitude), companyID));

				MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);

			} else {
				Toast.makeText(mContext, "Fill in all the details", Toast.LENGTH_LONG).show();
			}
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

	private void initData() {
		cd = new ConnectionDetector(mContext);
		db = new DatabaseHandler(mContext);

		farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		previous = new Intent(mContext, AllFarmAssessmentActivity.class);
		previous.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		date = new Date();
		collectDate = dateFormat.format(date);

	}


	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		int month = (monthOfYear + 1);
		activityDate = year + "-" + month + "-" + dayOfMonth;
		tvActualDate.setText( dayOfMonth+ "-" + month + "-" + year);
	}
}
