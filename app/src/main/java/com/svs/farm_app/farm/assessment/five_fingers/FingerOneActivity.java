package com.svs.farm_app.farm.assessment.five_fingers;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FingerOne;
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

public class FingerOneActivity extends BaseClass {

    private String soilTypeValue = "0";
    private String seedBedPrepValue = "0", landPrepValue = "0", manureValue = "0", cropResValue = "0",
            ratoonValue = "0", cropRotationValue = "0", erosionPrevValue = "0",
            waterLogRiskValue = "0";
    public ConnectionDetector cd;
    private String companyId;
    private String userId;
    private DatabaseHandler db;
    private String farmId;
    @BindView(R.id.rgWaterLogRisk)
    RadioGroup rgWaterLogRisk;
    @BindView(R.id.rgErosionPrev)
    RadioGroup rgErosionPrev;
    @BindView(R.id.rgCropRotation)
    RadioGroup rgCropRotation;
    @BindView(R.id.rgRatoon)
    RadioGroup rgRatoon;
    @BindView(R.id.rgCropRes)
    RadioGroup rgCropRes;
    @BindView(R.id.rgManure)
    RadioGroup rgManure;
    @BindView(R.id.rgLandPrep)
    RadioGroup rgLandPrep;
    @BindView(R.id.rgSeedBedPrep)
    RadioGroup rgSeedBedPrep;
    @BindView(R.id.rgSoilType)
    RadioGroup rgSoilType;
    @BindView(R.id.bSave)
    Button bSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FingerOneActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_one);
        ButterKnife.bind(this);

        mContext = FingerOneActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
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

                db.addFingerOne(new FingerOne(timeFormat.format(date), companyId,
                        userId, soilTypeValue, waterLogRiskValue, erosionPrevValue,
                        cropRotationValue, ratoonValue, cropResValue, manureValue,
                        landPrepValue, seedBedPrepValue, farmId));

                MyAlerts.backToAllFarmAssessmentsDialog(mContext, R.string.saved_offline);
            }
        });
    }

    private void initView() {
        rgSoilType.check(R.id.rbSoilType1);
        rgWaterLogRisk.check(R.id.rbRisk1);
        rgErosionPrev.check(R.id.rbErPrev1);
        rgCropRotation.check(R.id.rbCropRot1);
        rgRatoon.check(R.id.rbRatoon1);
        rgCropRes.check(R.id.rbCropRes1);
        rgManure.check(R.id.rbManure1);
        rgLandPrep.check(R.id.rbLandPrep1);
        rgSeedBedPrep.check(R.id.rbSeedBedPrep1);
    }

    private void initData() {
        cd = new ConnectionDetector(mContext);

        farmId = MyPrefrences.getPrefrence(mContext, "farm_id");
        userId = Preferences.USER_ID;
        companyId = Preferences.COMPANY_ID;

        db = new DatabaseHandler(mContext);
    }

    public void onSoilTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbSoilType1:
                if (checked) {
                    soilTypeValue = "0";
                }
                break;
            case R.id.rbSoilType2:
                if (checked) {
                    soilTypeValue = "1";
                }
                break;
            case R.id.rbSoilType3:
                if (checked) {
                    soilTypeValue = "2";
                }
                break;
        }

    }

    public void onWaterLogClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbRisk1:
                if (checked) {
                    waterLogRiskValue = "0";
                }
                break;
            case R.id.rbRisk2:
                if (checked) {

                    waterLogRiskValue = "2";
                }
                break;
        }

    }

    public void onErosionPrevClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbErPrev1:
                if (checked) {
                    erosionPrevValue = "0";
                }
                break;
            case R.id.rbErPrev2:
                if (checked) {
                    erosionPrevValue = "3";
                }
                break;
        }

    }

    public void onCropRotationClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbCropRot1:
                if (checked) {
                    cropRotationValue = "0";
                }
                break;
            case R.id.rbCropRot2:
                if (checked) {
                    cropRotationValue = "3";
                }
                break;
        }

    }

    public void onRatoonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbRatoon1:
                if (checked) {
                    ratoonValue = "0";
                }
                break;
            case R.id.rbRatoon2:
                if (checked) {
                    ratoonValue = "2";
                }
                break;
        }

    }

    public void onCropResClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbCropRes1:
                if (checked) {
                    cropResValue = "0";
                }
                break;
            case R.id.rbCropRes2:
                if (checked) {
                    cropResValue = "1";
                }
                break;
            case R.id.rbCropRes3:
                if (checked) {
                    cropResValue = "2";
                }
                break;
        }

    }

    public void onManureClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbManure1:
                if (checked) {
                    manureValue = "0";
                }
                break;
            case R.id.rbManure2:
                if (checked) {
                    manureValue = "2";
                }
                break;
        }

    }

    public void onLandPrepClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbLandPrep1:
                if (checked) {
                    landPrepValue = "0";
                }
                break;
            case R.id.rbLandPrep2:
                if (checked) {
                    landPrepValue = "2";
                }
                break;
        }

    }

    public void onSeedBedPrepClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbSeedBedPrep1:
                if (checked) {
                    seedBedPrepValue = "0";
                }
                break;
            case R.id.rbSeedBedPrep2:
                if (checked) {
                    seedBedPrepValue = "2";
                }
                break;
        }

    }

}
