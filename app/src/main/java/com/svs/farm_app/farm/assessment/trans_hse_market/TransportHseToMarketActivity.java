/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.trans_hse_market;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.TransportHseToMarket;
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
public class TransportHseToMarketActivity extends BaseClass implements View.OnClickListener , DatePickerDialog.OnDateSetListener {

	private DatabaseHandler db;
	private GPSTracker gpsTracker;
	private Intent newf;
	private Intent previous;
	private Date date;
	private String collectDate;
	private double latitude;
	private double longitude;
	private String transportCount;
	@BindView(R.id.tvActualDate)
	TextView tvActualDate;
	@BindView(R.id.bDatePicker)
	Button bActivityDate;
	@BindView(R.id.etFamilyHours)
	EditText etFamilyHours;
	@BindView(R.id.tvHiredHours)
	TextView tvHiredHours;
    @BindView(R.id.tvFamilyHours)
    TextView tvFamilyHours;
    @BindView(R.id.etHiredHours)
    EditText etHiredHours;
	@BindView(R.id.etMoneyOut)
	EditText etMoneyOut;
	@BindView(R.id.bBack)
	Button bBack;
	@BindView(R.id.bNext)
	Button bNext;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private TransportHseToMarketActivity mContext;
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

		mContext = TransportHseToMarketActivity.this;

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
		etFamilyHours.setVisibility(View.GONE);
		etHiredHours.setVisibility(View.GONE);
        tvFamilyHours.setVisibility(View.GONE);
        tvHiredHours.setVisibility(View.GONE);
	}

	private void initListeners() {
		bNext.setOnClickListener(this);
		bBack.setOnClickListener(this);
		bActivityDate.setOnClickListener(this);
		/*spTransMode
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// svillage =
						// village.getItemAtPosition(position).toString();
						MyData d = transportModeData[position];
						transModeId = d.getGradeId();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});*/
	}

	private void initData() {
		Intent dcIntent = getIntent();
		transportCount = dcIntent.getStringExtra("transport_count");
		new ConnectionDetector(mContext);
		db = new DatabaseHandler(mContext);

		farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		newf = new Intent(mContext,
				AllFarmAssessmentActivity.class);
		newf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		previous = new Intent(mContext,
				AllFarmAssessmentActivity.class);
		previous.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		date = new Date();
		collectDate = dateFormat.format(date);

		//loadTransportModes();

	}
/*
	private void loadTransportModes() {

		List<TransportModes> transModeList = db.getAllTransportModes();
		dataAdapter5 = new ArrayAdapter<MyData>(getBaseContext(),
				android.R.layout.simple_spinner_item);
		transportModeData = new MyData[transModeList.size()];

		dataAdapter5.addAll(transportModeData);
		dataAdapter5
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter5.notifyDataSetChanged();
		spTransMode.setAdapter(dataAdapter5);
	}*/

	public void onClick(View v) {
		if (v == bBack) {
			finish();
		} else if (v == bNext) {

			if (gpsTracker.canGetLocation) {
				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
			}

			String moneyOut = etMoneyOut.getText().toString();

			if (!"".equals(activityDate.trim()) && !"".equals(moneyOut.trim())
					&& !collectDate.trim().isEmpty()) {
				db.addTransportHseToMarket(new TransportHseToMarket(farmID,transportCount, activityDate, collectDate,
						moneyOut, String.valueOf(latitude), String
								.valueOf(longitude), userID, companyID));
				MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);
			} else {
				Toast.makeText(mContext,R.string.fill_in_all_details, Toast.LENGTH_SHORT).show();
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

	class MyData {

		String spinnerText;
		String value;

		public MyData(String spinnerText, String value) {
			this.spinnerText = spinnerText;
			this.value = value;
		}

		public String getSpinnerText() {
			return spinnerText;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return spinnerText;
		}
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		int month = (monthOfYear + 1);
		activityDate = year + "-" + month + "-" + dayOfMonth;
		tvActualDate.setText( dayOfMonth+ "-" + month + "-" + year);
	}
}
