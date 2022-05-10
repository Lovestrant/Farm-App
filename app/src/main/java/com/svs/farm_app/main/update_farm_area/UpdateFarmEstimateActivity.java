/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.main.update_farm_area;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.asynctask.uploads.UploadFarmAreaUpdate;
import com.svs.farm_app.entities.UpdateFarmArea;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author Benson
 */
public class UpdateFarmEstimateActivity extends Activity {

	private static final String TAG = UpdateFarmEstimateActivity.class.getSimpleName();
	private String farmID;
	private ConnectionDetector cd;
	private String newArea;
	private String userID;
    private String companyID;
	private DatabaseHandler db;
	private String currentArea;
	@BindView(R.id.etNewFarmArea)
	EditText etNewFarmArea;
	@BindView(R.id.bUpdateFarmArea)
	Button bUpdateFarmArea;
	@BindView(R.id.tvCurrentArea)
	TextView tvCurrentArea;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private UpdateFarmEstimateActivity mContext;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_farm);
		ButterKnife.bind(this);

		mContext = UpdateFarmEstimateActivity.this;

		initData();
		initListeners();
	}

	private void initData() {
		Intent intent = getIntent();
		farmID = intent.getStringExtra("farm_id");

		userID = Preferences.USER_ID;
        companyID = Preferences.COMPANY_ID;

		cd = new ConnectionDetector(getApplicationContext());
		db = new DatabaseHandler(getApplicationContext());

		currentArea = db.getFarmArea(farmID);

		try {
			tvCurrentArea.setText(currentArea);
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(),
					"Download Data Farm Data is", Toast.LENGTH_SHORT).show();
		}
	}

	private void initListeners() {

		etNewFarmArea.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				try {
					if (etNewFarmArea.getText().toString() == "") {
						etNewFarmArea.setText("0");
					}
					if (Float.parseFloat(etNewFarmArea.getText().toString()) > 20) {
						etNewFarmArea.setText("20");
					}
				} catch (Exception ex) {

				}
			}
		});

		bUpdateFarmArea.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				newArea = etNewFarmArea.getText().toString();

				if (newArea.length() > 0) {
					if (cd.isConnectingToInternet()) {

						new UploadFarmAreaUpdate(mContext,new UpdateFarmArea(farmID, newArea,userID,companyID)).execute();

					} else {

						db.addUpdateFarmArea(new UpdateFarmArea(farmID, newArea,userID,companyID));
						MyAlerts.backToDashboardDialog(mContext, R.string.saved_offline);
					}
				} else {
					
					MyAlerts.genericDialog(mContext,R.string.fill_in_all_details);
				}
			}
		});
	}
}
