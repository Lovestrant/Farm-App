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
import com.svs.farm_app.entities.FingerFour;
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

public class FingerFourActivity extends BaseClass {
	//TODO:Change radio buttons to sliders
	private String branchesValue = "0", foliarValue = "0", weedsValue = "0";
	public ConnectionDetector cd;
	private String companyId;
	private String userId;
	private DatabaseHandler db;
	private String farmId;
	private FingerFourActivity mContext;
	@BindView(R.id.rgFirstBranch)
	RadioGroup rgFirstBranch;
	@BindView(R.id.rgFoliar)
	RadioGroup rgFoliar;
	@BindView(R.id.rgWeeds)
	RadioGroup rgWeeds;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.bSave)
	Button bSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finger_four);
		ButterKnife.bind(this);

		mContext = FingerFourActivity.this;

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
				db.addFingerFour(new FingerFour(companyId,
						userId, branchesValue, foliarValue, weedsValue, farmId));

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

	public void onFirstBranchClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbFirstBranch1:
			if (checked) {
				branchesValue = "0";
			}
			break;
		case R.id.rbFirstBranch2:
			if (checked) {
				branchesValue = "10";
			}
			break;
		}
	}

	public void onFoliarClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbFoliar1:
			if (checked) {
				foliarValue = "0";
			}
			break;
		case R.id.rbFoliar2:
			if (checked) {
				foliarValue = "5";
			}
			break;
		}

	}

	public void onWeedsClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbWeeds1:
			if (checked) {
				weedsValue = "0";
			}
			break;
		case R.id.rbWeeds2:
			if (checked) {
				weedsValue = "5";
			}
			break;
		}

	}

	private class uploadFingerFourForm extends AsyncTask {
		private String responseString1;

		@Override
		protected Object doInBackground(Object[] params) {
			if (cd.isConnectingToInternet()) {

				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							Config.UPLOAD_FINGER_FOUR_FORM);

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							8);
					nameValuePairs.add(new BasicNameValuePair("company_id", companyId));
					nameValuePairs.add(new BasicNameValuePair("first_branch",
							branchesValue));
					nameValuePairs.add(new BasicNameValuePair("foliar",
							foliarValue));
					nameValuePairs.add(new BasicNameValuePair("weeds",
							weedsValue));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							userId));
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					nameValuePairs.add(new BasicNameValuePair("form_date",
							timeFormat.format(date)));
					nameValuePairs.add(new BasicNameValuePair("farm_id", farmId));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						responseString1 = EntityUtils.toString(entity);
					} else {
						responseString1 = EntityUtils.toString(entity);
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