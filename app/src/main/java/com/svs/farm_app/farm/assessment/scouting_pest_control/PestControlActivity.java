package com.svs.farm_app.farm.assessment.scouting_pest_control;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmAssFormsMedium;
import com.svs.farm_app.entities.Pesticides;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.farm.assessment.utils.FormTypes;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 
 * @author Benson
 */
public class PestControlActivity extends BaseClass implements OnClickListener , DatePickerDialog.OnDateSetListener{

	private String sprayMethod = "ulva+";
	private DatabaseHandler db;
	private GPSTracker gpsTracker;
	private Intent farmAssessment;
	private Date date;
	private double latitude;
	private double longitude;
	private String hiredHours;
	private String moneyOut;
	private String remarks;
	private String collectDate;
	private String inputID;
	private String inputQuantity;
	private ArrayAdapter<MyData> pesticideAdapter;
	private MyData[] pesticideData;
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
	@BindView(R.id.layout_remarks)
	LinearLayout layoutRemarks;
	@BindView(R.id.layout_application_mode)
	LinearLayout layoutApplication;
	@BindView(R.id.tvType)
	TextView tvType;
	@BindView(R.id.spType)
	Spinner spPesticideType;
	@BindView(R.id.etQuantity)
	EditText etPesticideQuantity;
	@BindView(R.id.rbKnapsack)
	RadioButton rbKnapsack;
	@BindView(R.id.rbUlva)
	RadioButton rbUlva;
	@BindView(R.id.bBack)
	Button bBack;
	@BindView(R.id.bNext)
	Button bNext;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private PestControlActivity mContext;
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

		mContext = PestControlActivity.this;

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initView();
		initListeners();
		initData();
	}

	private void initView() {
		layoutRemarks.setVisibility(View.GONE);
		layoutApplication.setVisibility(View.VISIBLE);
		tvType.setText("Herbicide Type");

		bBack.setText("Back");
		bNext.setText("Next");
	}

	private void initData() {
		new ConnectionDetector(mContext);
		db = new DatabaseHandler(mContext);

		farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		farmAssessment = new Intent(mContext,
				AllFarmAssessmentActivity.class);
		farmAssessment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		date = new Date();
		collectDate = dateFormat.format(date);
		loadPesticides();

	}

	private void loadPesticides() {
		List<Pesticides> pesticideList = db.getAllPesticides();
		pesticideAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item);
		pesticideData = new MyData[pesticideList.size()];
		
		int i = 0;
		for (Pesticides r : pesticideList) {
			if (i < pesticideList.size()) {
				pesticideData[i] = new MyData(r.getInputType(), r.getInputID());
				Log.e("Mydata:", pesticideData[i].getValue());
			}
			i++;
		}

		pesticideAdapter.addAll(pesticideData);
		pesticideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pesticideAdapter.notifyDataSetChanged();
		spPesticideType.setAdapter(pesticideAdapter);
	}

	private void initListeners() {
		rbKnapsack.setOnClickListener(this);
		rbUlva.setOnClickListener(this);
		bBack.setOnClickListener(this);
		bNext.setOnClickListener(this);
		bActivityDate.setOnClickListener(this);
		spPesticideType
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						MyData d = pesticideData[position];
						inputID = d.getValue();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

	public void onClick(View v) {
		if (v == rbKnapsack) {
			sprayMethod = "knapsack";
		} else if (v == rbUlva) {
			sprayMethod = "ulva+";
		} else if (v == bBack) {
			finish();
		} else if (v == bNext) {
			if (gpsTracker.canGetLocation) {
				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
			}

			String familyHours = etFamilyHours.getText().toString();
			hiredHours = etHiredHours.getText().toString();
			moneyOut = etMoneyOut.getText().toString();
			remarks = "";

			inputQuantity = etPesticideQuantity.getText().toString();

			if (!"".equals(activityDate.trim())
					&& !"".equals(familyHours.trim())
					&& !"".equals(hiredHours.trim())
					&& !"".equals(moneyOut.trim())
					&& "".equals(remarks.trim())
					&& !"".equals(sprayMethod.trim()) && inputID != null
					&& !"".equals(inputQuantity.trim())) {
				db.addFarmAssMedium(new FarmAssFormsMedium(farmID, FormTypes.PEST_CONTROL,
						"none", activityDate, familyHours, hiredHours,
						moneyOut, inputID, inputQuantity, sprayMethod,
						userID, collectDate, String.valueOf(latitude), String
								.valueOf(longitude), companyID));

				MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);
			} else {
				Toast.makeText(mContext,
						"Fill in all the details", Toast.LENGTH_LONG).show();
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

	private class MyData {

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
