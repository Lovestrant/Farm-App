package com.svs.farm_app.farm.assessment.scouting_pest_control;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.Scouting;
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
 *
 * @author Benson
 */
public class ScoutingActivity extends BaseClass implements View.OnClickListener , DatePickerDialog.OnDateSetListener{

	private static final String TAG = ScoutingActivity.class.getSimpleName();
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
	private String decision;
	private String ballWorm;
	private String jassid;
	private String stainer;
	private String aphid;
	private String beneficial;
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
	@BindView(R.id.etBallWorm)
	EditText etBallWorm;
	@BindView(R.id.etJassid)
	EditText etJassid;
	@BindView(R.id.etStainer)
	EditText etStainer;
	@BindView(R.id.etAphid)
	EditText etAphid;
	@BindView(R.id.etBeneficial)
	EditText etBeneficial;
	@BindView(R.id.rbYes)
	RadioButton rbYes;
	@BindView(R.id.rbNo)
	RadioButton rbNo;
	@BindView(R.id.bBack)
	Button bBack;
	@BindView(R.id.bNext)
	Button bNext;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private ScoutingActivity mContext;
	private String farmID;
	private String companyID;
	private String userID;
	private SimpleDateFormat dateFormat;
	private String activityDate ="";
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_scouting);
		ButterKnife.bind(this);

		mContext = ScoutingActivity.this;

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initView();
		initListeners();
		initData();
	}

	private void initView() {
		bNext.setText("Next");
		bBack.setText("Back");
	}

	private void initListeners() {
		bBack.setOnClickListener(this);
		bNext.setOnClickListener(this);
		rbYes.setOnClickListener(this);
		rbNo.setOnClickListener(this);
		bActivityDate.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == rbYes) {
			if (rbYes.isChecked()) {
				decision = "yes";
				next = new Intent(mContext,
						PestControlActivity.class);
			} else {
				decision = "";
			}

		} else if (v == rbNo) {
			if (rbNo.isChecked()) {
				decision = "no";
				Log.d(TAG, decision);
				next = new Intent(mContext,
						AllFarmAssessmentActivity.class);
				next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			} else {
				decision = "";
			}
		} else if (v == bBack) {
			finish();
		} else if (v == bNext) {

			if (gpsTracker.canGetLocation) {
				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
			}

			familyHours = etFamilyHours.getText().toString();
			hiredHours = etHiredHours.getText().toString();
			moneyOut = etMoneyOut.getText().toString();
			ballWorm = etBallWorm.getText().toString();
			jassid = etJassid.getText().toString();
			stainer = etStainer.getText().toString();
			aphid = etAphid.getText().toString();
			beneficial = etBeneficial.getText().toString();
			Log.d(TAG, decision);

			if (!activityDate.trim().isEmpty()
					&& !familyHours.trim().isEmpty()
					&& !hiredHours.trim().isEmpty()
					&& !moneyOut.trim().isEmpty()
					&& !decision.trim().isEmpty()) {
				db.addScouting(new Scouting(farmID, activityDate, familyHours,
						hiredHours, moneyOut, ballWorm, jassid, stainer,
						aphid, beneficial, decision, userID, collectDate,
						String.valueOf(latitude), String.valueOf(longitude), companyID));

				startActivity(next);
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

	private void initData() {
		new ConnectionDetector(ScoutingActivity.this);
		db = new DatabaseHandler(ScoutingActivity.this);

		farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		gpsTracker = new GPSTracker(mContext);

		previous = new Intent(mContext,AllFarmAssessmentActivity.class);
		previous.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		next = new Intent(mContext,AllFarmAssessmentActivity.class);

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
