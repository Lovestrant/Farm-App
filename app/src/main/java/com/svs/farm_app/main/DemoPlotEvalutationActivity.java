package com.svs.farm_app.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.svs.farm_app.R;
import com.svs.farm_app.utils.BaseClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DemoPlotEvalutationActivity extends BaseClass {
    private Spinner spinnerForms;
    String[] formsArray;
    private ArrayAdapter<String> formsAdapter;
    private String[] landActivityArray;
    private ArrayAdapter<String> landActivityAdapter;
    private Spinner spinnerActivities;
    private String[] labourTypeArray;
    private String[] genderArray;
    private Spinner spinnerLabourType;
    private Spinner spinnerGender;
    private ArrayAdapter<String> genderAdapter;
    private TextView tvActivities;
    private TextView tvStopTime;
    private TextView tvTotalTime;
    private TextView tvLabourType;
    private EditText etStartTime;
    private TextView tvStartTime;
    private EditText etStopTime;
    private TextView tvTotalTime2;
    private TextView tvMales;
    private EditText etMales;
    private TextView tvFemales;
    private EditText etFemales;
    private TextView tvTotalCost;
    private EditText etTotalCost;
    private TextView tvHiredLabour;
    private TextView tvMales2;
    private EditText etMales2;
    private TextView tvFemales2;
    private EditText etFemales2;
    private String[] plantinArray;
    private String[] plantingArray;
    private String[] herbicideArray;
    private String[] replantingArray;
    private String[] soilFertilityArray;
    private String[] weedingArray;
    private String[] scoutingPestControlArray;
    private String[] harvestArray;
    private String[] gradingArray;
    private String[] bailingArray;
    private String[] transportationToMarket;
    private String[] transportToMarketArray;
    private String[] postHarvestArray;
    private String[] transportFieldToHouseArray;
    private TimePicker tpStartTime;
    private TimePicker tpStopTime;
    private TextView tvDate;
    private DatePicker dpDate;
    private SimpleDateFormat timeFormat;
    private Integer startMin;
    private Integer startHour;
    private Integer stopHour;
    private Date startTime;
    private Integer stopMin;
    private Date stopTime;
    private String stopTime2;
    private String startTime2;
    private long diff;
    private Date diffDate;
    private String sDiffDate;
    private Button bSubmitForm;
    private Button bDate;
    private EditText etDate;
    private Button bStartTime;
    private Button bStopTime;
    Calendar c;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog dpd;
    private int mHour;
    private int mMinute;
    private int mHour2;
    private int mMinute2;
    private TimePickerDialog tpdStartTime;
    private TimePickerDialog tpdStopTime;
    String sForm;
    String sActivity;
    String sDate;
    String sStartTime;
    String sStopTime;
    String sOwnMales;
    String sOwnFemales;
    String sHiredMales;
    String sHiredFemales;
    String sTotalCost;
    private String farmer_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdemo_plot);
    }
}