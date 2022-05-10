package com.svs.farm_app.farm.assessment.soil_fertility;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.entities.FarmAssFormsMedium;
import com.svs.farm_app.entities.FoliarFeed;
import com.svs.farm_app.farm.assessment.utils.FormTypes;
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
 * Created by ADMIN on 07-Mar-17.
 */

public class FoliarFeedFragment extends Fragment implements View.OnClickListener  , DatePickerDialog.OnDateSetListener{
    private Date date;
    private GPSTracker gpsTracker;
    private double latitude;
    private double longitude;
    private String familyHours;
    private String hiredHours;
    private String moneyOut;
    private String remarks;
    private String collectDate;
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
    @BindView(R.id.layout_remarks)
    LinearLayout layoutRemarks;
    @BindView(R.id.layout_application_mode)
    LinearLayout layoutApplication;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.spType)
    Spinner spFoliarFeedType;
    @BindView(R.id.etQuantity)
    EditText etQuantity;
    @BindView(R.id.rbKnapsack)
    RadioButton rbKnapsack;
    @BindView(R.id.rbUlva)
    RadioButton rbUlva;
    @BindView(R.id.bBack)
    Button bBack;
    @BindView(R.id.bNext)
    Button bNext;
    private String farmID;
    private String companyID;
    private String userID;
    private SimpleDateFormat dateFormat;
    private String activityDate ="";
    private Activity mContext;
    private DatabaseHandler db;
    private String formTypeID;
    private ArrayAdapter<MyData> foliarFeedAdapter;
    private MyData[] foliarFeedData;
    private String inputQuantity;
    private String inputID;
    private String sprayMethod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        View rootView =  inflater.inflate(R.layout.activity_master_fragment, container,false);
        ButterKnife.bind(this,rootView);
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
        layoutRemarks.setVisibility(View.GONE);
        layoutApplication.setVisibility(View.VISIBLE);
        tvType.setText("Foliar Feed Type");
        bBack.setText("back");
        bNext.setText("Next");
    }

    private void initListeners() {
        rbKnapsack.setOnClickListener(this);
        rbUlva.setOnClickListener(this);
        bBack.setOnClickListener(this);
        bNext.setOnClickListener(this);
        bActivityDate.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == rbKnapsack) {
            sprayMethod = "knapsack";
        } else if (v == rbUlva) {
            sprayMethod = "ulva+";
        }else if (v == bBack) {
            ((SoilFertilityActivity)mContext).majorFormStack.pop();
            getActivity().getFragmentManager().popBackStack();
        } else if (v == bNext) {
            if (gpsTracker.canGetLocation) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }

            familyHours = etFamilyHours.getText().toString();
            hiredHours = etHiredHours.getText().toString();
            moneyOut = etMoneyOut.getText().toString();
            inputID = String.valueOf(foliarFeedData[spFoliarFeedType.getSelectedItemPosition()].getValue());
            inputQuantity = etQuantity.getText().toString();

            if (!"".equals(activityDate.trim())
                    && !"".equals(familyHours.trim())
                    && !"".equals(hiredHours.trim())
                    && !"".equals(moneyOut.trim())) {

                /*db.addFarmAssMajor(new FarmAssFormsMajor(farmID, formTypeID,
                        "none", activityDate, familyHours, hiredHours,
                        moneyOut, remarks, userID, collectDate, String
                        .valueOf(latitude), String.valueOf(longitude), companyID));*/

                ((SoilFertilityActivity)mContext).mediumFormStack.push(new FarmAssFormsMedium(farmID, formTypeID,
                        "none", activityDate, familyHours, hiredHours,
                        moneyOut, inputID, inputQuantity, sprayMethod,
                        userID, collectDate, String.valueOf(latitude), String
                        .valueOf(longitude), companyID));

                /*FoliarFeedFragment foliarFeedFragment = new FoliarFeedFragment();
                insertFragment(R.id.fragment_container, foliarFeedFragment, "foliar_feed_one");*/

                if (formTypeID == FormTypes.FOLIAR_FEED_ONE) {

                    FoliarFeedFragment fertilizerFragment = new FoliarFeedFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("form_type_id", FormTypes.FOLIAR_FEED_TWO);
                    fertilizerFragment.setArguments(bundle);

                    insertFragment(R.id.fragment_container, fertilizerFragment, FormTypes.FOLIAR_FEED_TWO);

                    ((SoilFertilityActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_foliar_app_two);

                }else if (formTypeID == FormTypes.FOLIAR_FEED_TWO) {

                    FoliarFeedFragment fertilizerFragment = new FoliarFeedFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("form_type_id", FormTypes.FOLIAR_FEED_THREE);
                    fertilizerFragment.setArguments(bundle);

                    insertFragment(R.id.fragment_container, fertilizerFragment, FormTypes.FOLIAR_FEED_THREE);

                    ((SoilFertilityActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_foliar_app_three);

                }
                else if (formTypeID == FormTypes.FOLIAR_FEED_THREE) {

                    saveForms();

                    MyAlerts.backToAllFarmAssessmentsDialog(mContext,R.string.saved_offline);
                }


            } else {
                Toast.makeText(mContext,R.string.fill_in_all_details, Toast.LENGTH_LONG).show();
            }
        }else if (v == bActivityDate) {
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

    private void saveForms() {
        for(Object form: ((SoilFertilityActivity)mContext).mediumFormStack){
            db.addFarmAssMedium((FarmAssFormsMedium) form);
        }

        for(Object form: ((SoilFertilityActivity)mContext).majorFormStack){
            db.addFarmAssMajor((FarmAssFormsMajor) form);
        }
    }

    private void initData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            formTypeID = bundle.getString("form_type_id");
        }

        db = new DatabaseHandler(mContext);

        farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
        userID = Preferences.USER_ID;
        companyID = Preferences.COMPANY_ID;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gpsTracker = new GPSTracker(mContext);

        date = new Date();
        collectDate = dateFormat.format(date);

        remarksArray = new String[]{"5","4","3","2","1","0"};

        loadFoliarFeed();
    }

    private void loadFoliarFeed() {
        db = new DatabaseHandler(mContext);
        List<FoliarFeed> herbicidesList = db.getAllFoliarFeed();
        foliarFeedAdapter = new ArrayAdapter<MyData>(mContext,
                android.R.layout.simple_spinner_item);
        foliarFeedData = new MyData[herbicidesList.size()];
        int i = 0;
        for (FoliarFeed r : herbicidesList) {
            if (i < herbicidesList.size()) {
                foliarFeedData[i] = new MyData(r.getInputType(), r.getInputID());
                Log.i("FOLIARMydata:", foliarFeedData[i].getValue());
            }

            i++;
        }

        foliarFeedAdapter.addAll(foliarFeedData);
        foliarFeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFoliarFeedType.setAdapter(foliarFeedAdapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = (monthOfYear + 1);
        activityDate = year + "-" + month + "-" + dayOfMonth;
        tvActualDate.setText( dayOfMonth+ "-" + month + "-" + year);
    }

    private void insertFragment(int layout, Fragment frag, String tag){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, frag, tag);
        transaction.addToBackStack(null);
        transaction.commit();
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

