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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmOtherCrops;
import com.svs.farm_app.entities.OtherCrops;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Benson
 */
public class OtherCropsActivity extends BaseClass implements OnClickListener {
	private DatabaseHandler db;
	private ConnectionDetector cd;
	private String userId;
	private String companyId;
	private String farmId;
	private String cropOne, cropTwo, cropThree;
	private SimpleDateFormat dateFormat;
	private Date date;
	private GPSTracker gpsTracker;
	private double latitude;
	private double longitude;
	private Intent farmAssessment;
	private ArrayAdapter<MyData> otherCropsAdapter;
	private MyData[] cropData;
	@BindView(R.id.bCancel)
	Button bCancel;
	@BindView(R.id.bSave)
	Button bSave;
	@BindView(R.id.spThirdCrop)
	Spinner spThirdCrop;
	@BindView(R.id.spSecondCrop)
	Spinner spSecondCrop;
	@BindView(R.id.spFirstCrop)
	Spinner spFirstCrop;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private OtherCropsActivity mContext;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_other_crops_1);
		ButterKnife.bind(this);

		mContext = OtherCropsActivity.this;

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initListeners();
		initData();
	}

	private void loadCrops() {

		List<OtherCrops> cropsList = db.allOtherCrops();

		otherCropsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item);

		cropData = new MyData[cropsList.size()];
		int i = 0;
		for (OtherCrops r : cropsList) {
			if (i < cropsList.size()) {
				cropData[i] = new MyData(r.getCropName(), r.getCropID());
				Log.e("Mydata:", cropData[i].getValue());
			}

			i++;
		}

		otherCropsAdapter.addAll(cropData);
		otherCropsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		otherCropsAdapter.notifyDataSetChanged();
		spFirstCrop.setAdapter(otherCropsAdapter);
		spSecondCrop.setAdapter(otherCropsAdapter);
		spThirdCrop.setAdapter(otherCropsAdapter);
	}

	private void initListeners() {
		bSave.setOnClickListener(this);
		bCancel.setOnClickListener(this);
		spFirstCrop
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						MyData d = cropData[position];
						cropOne = d.getValue();

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						throw new UnsupportedOperationException(
								"Not supported yet.");
					}
				});
		spSecondCrop
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						MyData d = cropData[position];
						cropTwo = d.getValue();

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						throw new UnsupportedOperationException(
								"Not supported yet.");
					}
				});
		spThirdCrop
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						MyData d = cropData[position];
						cropThree = d.getValue();

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						throw new UnsupportedOperationException(
								"Not supported yet.");
					}
				});
	}

	public void onClick(View v) {

		if (v == bSave) {
			date = new Date();
			String collectDate = dateFormat.format(date);

			if (gpsTracker.canGetLocation) {
				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
			}

			db.addFarmOtherCrops(new FarmOtherCrops(farmId, cropOne, cropTwo,
					cropThree, userId, collectDate, String.valueOf(latitude),
					String.valueOf(longitude), companyId));
			Log.d("OTHER CROPS: ", farmId);
			startActivity(farmAssessment);
		} else if (v == bCancel) {

			startActivity(farmAssessment);
		}
	}

	private void initData() {
		cd = new ConnectionDetector(OtherCropsActivity.this);
		db = new DatabaseHandler(OtherCropsActivity.this);
		farmId = MyPrefrences.getPrefrence(mContext, "farm_id");
		userId = Preferences.USER_ID;
		companyId = Preferences.COMPANY_ID;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		farmAssessment = new Intent(mContext,AllFarmAssessmentActivity.class);
		farmAssessment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		loadCrops();
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
}
