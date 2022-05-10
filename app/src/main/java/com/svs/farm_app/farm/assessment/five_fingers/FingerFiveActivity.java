package com.svs.farm_app.farm.assessment.five_fingers;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FingerFive;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FingerFiveActivity extends BaseClass {

	public ConnectionDetector cd;
	private String companyId;
	private String userId;
	private String safeUsageCansValue = "0";
	private String scoutSignValue = "0";
	private String pestDamageValue = "0";
	private String pestLevelValue = "0";
	private String sprayTimeValue = "0";
	private String scoutMethodValue = "0";
	private String correctPesticideValue = "0";
	private String emptyCansValue = "0";
	private String pestAbsDurationValue = "0";
	private String pegBoardValue = "0";
	private DatabaseHandler db;
	private String farmId;
	@BindView(R.id.rgPestLevel)
	RadioGroup rgPestLevel;
	@BindView(R.id.rgPestDamage)
	RadioGroup rgPestDamage;
	@BindView(R.id.rgScoutSign)
	RadioGroup rgScoutSign;
	@BindView(R.id.rgEmptyCans)
	RadioGroup rgEmptyCans;
	@BindView(R.id.rgPegBoard)
	RadioGroup rgPegBoard;
	@BindView(R.id.rgScoutMethod)
	RadioGroup rgScoutMethod;
	@BindView(R.id.rgSprayingTime)
	RadioGroup rgSprayingTime;
	@BindView(R.id.rgPestAbsDuration)
	RadioGroup rgPestAbsDuration;
	@BindView(R.id.rgCorrectPesticide)
	RadioGroup rgCorrectPesticide;
	@BindView(R.id.rgSafeUsageCans)
	RadioGroup rgSafeUsageCans;
	@BindView(R.id.bSave)
	Button bSave;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private FingerFiveActivity mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finger_five);
		ButterKnife.bind(this);

		mContext = FingerFiveActivity.this;

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
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();

				db.addFingerFive(new FingerFive(timeFormat.format(date), companyId,
						userId, pestLevelValue, pestDamageValue, scoutSignValue,
						emptyCansValue, pegBoardValue, scoutMethodValue,
						sprayTimeValue, pestAbsDurationValue, correctPesticideValue,
						safeUsageCansValue, farmId));

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

	public void onPestLevelClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbPestLevel1:
			if (checked) {
				pestLevelValue = "0";
			}
			break;
		case R.id.rbPestLevel2:
			if (checked) {
				pestLevelValue = "2";
			}
			break;
		}

	}

	public void onPestDamageClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbPestDamage1:
			if (checked) {
				pestDamageValue = "0";
			}
			break;
		case R.id.rbPestDamage2:
			if (checked) {
				pestDamageValue = "2";
			}
			break;
		}

	}

	public void onLastScoutClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbScoutSign1:
			if (checked) {
				scoutSignValue = "0";
			}
			break;
		case R.id.rbScoutSign2:
			if (checked) {
				scoutSignValue = "2";
			}
			break;
		}

	}

	public void onEmptyPestCansClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbEmptyCans1:
			if (checked) {
				emptyCansValue = "0";
			}
			break;
		case R.id.rbEmptyCans2:
			if (checked) {
				emptyCansValue = "2";
			}
			break;
		}

	}

	public void onPegBoardClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbPegBoard1:
			if (checked) {
				pegBoardValue = "0";
			}
			break;
		case R.id.rbPegBoard2:
			if (checked) {
				pegBoardValue = "2";
			}
			break;
		}

	}

	public void onScoutMethodClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbScoutMethod1:
			if (checked) {
				scoutMethodValue = "0";
			}
			break;
		case R.id.rbScoutMethod2:
			if (checked) {
				scoutMethodValue = "2";
			}
			break;
		}

	}

	public void onSprayTimeClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbSprayingTime1:
			if (checked) {
				sprayTimeValue = "0";
			}
			break;
		case R.id.rbSprayingTime2:
			if (checked) {
				sprayTimeValue = "2";
			}
			break;
		}

	}

	public void onPestAbsClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbPestAbsDuration1:
			if (checked) {
				pestAbsDurationValue = "0";
			}
			break;
		case R.id.rbPestAbsDuration2:
			if (checked) {
				pestAbsDurationValue = "2";
			}
			break;
		}

	}

	public void onCorrectPestUseClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbCorrectPesticide1:
			if (checked) {
				correctPesticideValue = "0";
			}
			break;
		case R.id.rbCorrectPesticide2:
			if (checked) {
				correctPesticideValue = "2";
			}
			break;
		}

	}

	public void onSafePestUseClicked(View view) {

		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbSafeUsageCans1:
			if (checked) {
				safeUsageCansValue = "0";
			}
			break;
		case R.id.rbSafeUsageCans2:
			if (checked) {
				safeUsageCansValue = "2";
			}
			break;
		}

	}

	private class uploadFingerFiveForm extends AsyncTask {
		private String responseString1;

		@Override
		protected Object doInBackground(Object[] params) {
			if (cd.isConnectingToInternet()) {

				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							Config.UPLOAD_FINGER_FIVE_FORM);

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							9);
					nameValuePairs.add(new BasicNameValuePair("company_id", companyId));
					nameValuePairs.add(new BasicNameValuePair(DatabaseHandler.KEY_USER_ID,
							userId));
					nameValuePairs.add(new BasicNameValuePair("farm_id", farmId));
					nameValuePairs.add(new BasicNameValuePair("pest_level",
							pestLevelValue));
					nameValuePairs.add(new BasicNameValuePair("pest_damage",
							pestDamageValue));
					nameValuePairs.add(new BasicNameValuePair("last_scout",
							scoutSignValue));
					nameValuePairs.add(new BasicNameValuePair("empty_cans",
							emptyCansValue));
					nameValuePairs.add(new BasicNameValuePair(
							"peg_board_avail", pegBoardValue));
					nameValuePairs.add(new BasicNameValuePair("scout_method",
							scoutMethodValue));
					nameValuePairs.add(new BasicNameValuePair("spray_time",
							sprayTimeValue));
					nameValuePairs.add(new BasicNameValuePair(
							"pest_abs_duration", pestAbsDurationValue));
					nameValuePairs.add(new BasicNameValuePair(
							"correct_use_pesticide", correctPesticideValue));
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");

					Date date = new Date();
					nameValuePairs.add(new BasicNameValuePair("form_date",
							timeFormat.format(date)));
					nameValuePairs.add(new BasicNameValuePair(
							"safe_usage_cans", safeUsageCansValue));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						responseString1 = EntityUtils.toString(entity);
					} else {
						responseString1 = "Error occurred! Http Status Code: "
								+ statusCode;
					}
				} catch (Exception e) {
					Log.e("Exception: ", e.toString());
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext,
								"No Internet connection!", Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
			return null;
		}
	}


}