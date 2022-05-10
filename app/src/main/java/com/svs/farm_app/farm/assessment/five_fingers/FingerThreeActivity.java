package com.svs.farm_app.farm.assessment.five_fingers;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FingerThree;
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

public class FingerThreeActivity extends BaseClass {
    private String gapFillValue = "0", gapFillOnEmergenceValue = "0", thinNumValue = "0",
            thinAfterEmergenceValue = "0";
    public ConnectionDetector cd;
    private String companyId;
    private String userId;
    private DatabaseHandler db;
    private String farmId;
    private FingerThreeActivity mContext;
    @BindView(R.id.rgGapfill)
    RadioGroup rgGapfill;
    @BindView(R.id.rgGapFillOnEmer)
    RadioGroup rgGapFillOnEmer;
    @BindView(R.id.rgThinNum)
    RadioGroup rgThinNum;
    @BindView(R.id.rgThinAfterEmer)
    RadioGroup rgThinAfterEmer;
    @BindView(R.id.bSave)
    Button bSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_three);
        ButterKnife.bind(this);

        mContext = FingerThreeActivity.this;

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

                db.addFingerThree(new FingerThree(timeFormat.format(date), companyId,
                        userId, gapFillValue, gapFillOnEmergenceValue, thinNumValue,
                        thinAfterEmergenceValue, farmId));

                MyAlerts.backToAllFarmAssessmentsDialog(mContext, R.string.saved_offline);
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

    public void onGapFillClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbGapFill1:
                if (checked) {
                    gapFillValue = "0";
                }
                break;
            case R.id.rbGapFill2:
                if (checked) {
                    gapFillValue = "5";
                }
                break;
        }
    }

    public void onGapFillAfterEmerClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbGapFillOnEmer1:
                if (checked) {
                    gapFillOnEmergenceValue = "0";
                }
                break;
            case R.id.rbGapFillOnEmer2:
                if (checked) {
                    gapFillOnEmergenceValue = "5";
                }
                break;
        }
    }

    public void onThinNumClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbThinNum1:
                if (checked) {
                    thinNumValue = "0";
                }
                break;
            case R.id.rbThinNum2:
                if (checked) {
                    thinNumValue = "5";
                }
                break;
        }
    }

    public void onThinAfterEmerClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbThinAfterEmer1:
                if (checked) {
                    thinAfterEmergenceValue = "0";
                }
                break;
            case R.id.rbThinAfterEmer2:
                if (checked) {
                    thinAfterEmergenceValue = "5";
                }
                break;
        }
    }
}