package com.svs.farm_app.farm.assessment.five_fingers;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FingerTwo;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FingerTwoActivity extends BaseClass {
	//TODO: Possibly change radio button to sliders
	public ConnectionDetector cd;
	private String companyId;
	private String userId;
	private String plantingTimeValue = "0";
	private String rowSpacingValue = "0";
	private String seedPerStation = "0";
	private String correctSeedPlantingValue = "0";
	private DatabaseHandler db;
	private String farmId;
	private FingerTwoActivity mContext;
	@BindView(R.id.rgCorrectSeedPlant)
	RadioGroup rgCorrectSeedPlant;
	@BindView(R.id.rgSeedPerStat)
	RadioGroup rgSeedPerStat;
	@BindView(R.id.rgRowSpacing)
	RadioGroup rgRowSpacing;
	@BindView(R.id.rgPlantingTime)
	RadioGroup rgPlantingTime;
	@BindView(R.id.bSave)
	Button bSave;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finger_two);
		ButterKnife.bind(this);

		mContext = FingerTwoActivity.this;

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initData();
		initListeners();
	}

	private void initListeners() {
		bSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDateFormat timeFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = new Date();

				db.addFingerTwo(new FingerTwo(timeFormat.format(date), companyId,
						userId, correctSeedPlantingValue, rowSpacingValue, seedPerStation,
						plantingTimeValue, farmId));

				MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);
			}
		});
	}

	private void initData() {
		cd = new ConnectionDetector(mContext);

		farmId = MyPrefrences.getPrefrence(mContext, "farm_id");
		userId = Preferences.USER_ID;
		companyId = Preferences.COMPANY_ID;

		db = new DatabaseHandler(mContext);
	}

	public void onCorrectSeedPlantClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbCorrectSeed1:
			if (checked) {
				correctSeedPlantingValue = "0";
			}
			break;
		case R.id.rbCorrectSeed2:
			if (checked) {
				correctSeedPlantingValue = "2";
			}
			break;
		}

	}

	public void onSeedPerStatClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbSeedPerStat1:
			if (checked) {
				seedPerStation = "0";
			}
			break;
		case R.id.rbSeedPerStat2:
			if (checked) {
				seedPerStation = "3";
			}
			break;
		}

	}

	public void onRowSpacingClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbRowSpacing1:
			if (checked) {
				rowSpacingValue = "0";
			}
			break;
		case R.id.rbRowSpacing2:
			if (checked) {
				rowSpacingValue = "5";
			}
			break;
		}

	}

	public void onPlantingTimeClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbPlantingTime1:
			if (checked) {
				plantingTimeValue = "0";
			}
			break;
		case R.id.rbPlantingTime2:
			if (checked) {
				plantingTimeValue = "1";
			}
			break;
		case R.id.rbPlantingTime3:
			if (checked) {
				plantingTimeValue = "2";
			}
			break;
		case R.id.rbPlantingTime4:
			if (checked) {
				plantingTimeValue = "3";
			}
			break;
		case R.id.rbPlantingTime5:
			if (checked) {
				plantingTimeValue = "4";
			}
			break;
		case R.id.rbPlantingTime6:
			if (checked) {
				plantingTimeValue = "5";
			}
			break;
		}

	}
}