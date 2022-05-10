/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment.land_preparation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.farm.assessment.utils.FormTypes;
import com.svs.farm_app.utils.ConnectionDetector;
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
 * @author Wamae
 */
public class RippingFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ConnectionDetector cd;
    private GPSTracker gpsTracker;
    private double latitude;
    private double longitude;
    private String hiredHours;
    private String moneyOut;
    private String remarks;
    private String collectDate;
    private String rippingMethod;
    private String[] remarksArray;
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
    @BindView(R.id.spRemarks)
    Spinner spRemarks;
    @BindView(R.id.bBack)
    Button bBack;
    @BindView(R.id.bNext)
    Button bNext;
    @BindView(R.id.rbOxen)
    RadioButton rbOxen;
    @BindView(R.id.rg)
    RadioGroup rgRadioGroup;
    @BindView(R.id.rbTractor)
    RadioButton rbTractor;
    private Activity mContext;
    private String farmID;
    private String companyID;
    private String userID;
    private SimpleDateFormat dateFormat;
    private String activityDate ="";
    private Date date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        View rootView = inflater.inflate(R.layout.activity_master_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initListeners();
    }

    private void initView() {
        ((LandPreparationActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_ripping);
        rgRadioGroup.setVisibility(View.VISIBLE);
        bBack.setText(R.string.back);
        bNext.setText(R.string.next);
    }

    private void initListeners() {
        rbTractor.setOnClickListener(this);
        rbOxen.setOnClickListener(this);
        bBack.setOnClickListener(this);
        bNext.setOnClickListener(this);
        bActivityDate.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v == rbTractor) {
            if (rbTractor.isChecked()) {
                rippingMethod = "tractor";
            } else {
                rippingMethod = "";
            }
        } else if (v == rbOxen) {
            if (rbOxen.isChecked()) {
                rippingMethod = "oxen";
            } else {
                rippingMethod = "";
            }
        } else if (v == bBack) {
            ((LandPreparationActivity) mContext).majorFormStack.pop();
            getActivity().getFragmentManager().popBackStack();
        } else if (v == bNext) {
            String familyHours = etFamilyHours.getText().toString();
            hiredHours = etHiredHours.getText().toString();
            moneyOut = etMoneyOut.getText().toString();
            remarks = remarksArray[spRemarks.getSelectedItemPosition()];

            if (gpsTracker.canGetLocation) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }

            if (!"".equals(activityDate.trim())
                    && !"".equals(familyHours.trim())
                    && !"".equals(hiredHours.trim())
                    && !"".equals(moneyOut.trim())
                    && !"".equals(remarks.trim())) {

                ((LandPreparationActivity) mContext).majorFormStack.push(new FarmAssFormsMajor(farmID, FormTypes.RIPPING,
                        rippingMethod, activityDate, familyHours,
                        hiredHours, moneyOut, remarks, userID, collectDate,
                        String.valueOf(latitude), String.valueOf(longitude), companyID));

                PotholingFragment potholingFragment = new PotholingFragment();
                insertFragment(R.id.fragment_container, potholingFragment, "potholing");


            } else {
                Toast.makeText(mContext,
                        R.string.fill_in_all_details, Toast.LENGTH_LONG).show();
            }
        } else if (v == bActivityDate) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.setMaxDate(now);
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        }
    }

    private void initData() {

        farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
        userID = Preferences.USER_ID;
        companyID = Preferences.COMPANY_ID;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gpsTracker = new GPSTracker(mContext);

        date = new Date();
        collectDate = dateFormat.format(date);

        rippingMethod = "tractor";

        remarksArray = new String[]{"5", "4", "3", "2", "1", "0"};
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = (monthOfYear + 1);
        activityDate = year + "-" + month + "-" + dayOfMonth;
        tvActualDate.setText(dayOfMonth + "-" + month + "-" + year);
    }

    private void insertFragment(int layout, Fragment frag, String tag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, frag, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
