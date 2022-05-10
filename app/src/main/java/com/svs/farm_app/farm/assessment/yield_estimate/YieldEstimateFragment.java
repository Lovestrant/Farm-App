package com.svs.farm_app.farm.assessment.yield_estimate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.YieldEstimate;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.farm.assessment.utils.FormTypes;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ADMIN on 07-Mar-17.
 */

public class YieldEstimateFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = YieldEstimateFragment.class.getSimpleName();
    private DatabaseHandler db;
    private String farmID;
    private String userID;
    private String companyID;
    private SimpleDateFormat dateFormat;
    private GPSTracker gpsTracker;
    private Date date;
    private double latitude;
    private double longitude;
    private Activity mContext;
    @BindView(R.id.ertNumofPlants)
    EditText etNumofPlants;
    @BindView(R.id.etNumofBalls)
    EditText etNumofBalls;
    @BindView(R.id.etLeftRow)
    EditText etLeftRow;
    @BindView(R.id.etRightRow)
    EditText etRightRow;
    @BindView(R.id.btnYieldPrevious)
    Button bBack;
    @BindView(R.id.btnYieldNext)
    Button bNext;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String formTypeID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        View rootView = inflater.inflate(R.layout.yield_estimate_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initListeners();
    }

    private void initView() {
        setToolbarTitle();
        bBack.setText(R.string.back);
        bNext.setText(R.string.next);

        if (formTypeID == FormTypes.SAMPLING_STATION_FIVE) {

            bNext.setText(R.string.save);

        }
    }

    private void setToolbarTitle() {
        //ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (formTypeID == FormTypes.SAMPLING_STATION_ONE) {

            toolbar.setTitle(R.string.sampling_station_one);

        } else if (formTypeID == FormTypes.SAMPLING_STATION_TWO) {

            toolbar.setTitle(R.string.sampling_station_two);

        } else if (formTypeID == FormTypes.SAMPLING_STATION_THREE) {

            toolbar.setTitle(R.string.sampling_station_three);

        } else if (formTypeID == FormTypes.SAMPLING_STATION_FOUR) {

            toolbar.setTitle(R.string.sampling_station_four);

        } else if (formTypeID == FormTypes.SAMPLING_STATION_FIVE) {

            toolbar.setTitle(R.string.sampling_station_five);

        }
    }

    private void initListeners() {
        bBack.setOnClickListener(this);
        bNext.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == bBack) {
            ((YieldActivity) mContext).yieldEstimateStack.pop();
            getActivity().getFragmentManager().popBackStack();
            getActivity().finish();
        } else if (v == bNext) {
            date = new Date();

            String collectDate = dateFormat.format(date);

            if (gpsTracker.canGetLocation) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
            }

            String numOfPlants = etNumofPlants.getText().toString().trim();
            String numOfBalls = etNumofBalls.getText().toString().trim();
            String leftRow = etLeftRow.getText().toString().trim();
            String rightRow = etRightRow.getText().toString().trim();

            if (!numOfPlants.trim().isEmpty() && !numOfBalls.trim().isEmpty()
                    && !leftRow.trim().isEmpty() && !rightRow.trim().isEmpty()) {

                ((YieldActivity) mContext).yieldEstimateStack.push(new YieldEstimate(farmID, formTypeID, numOfPlants,
                        numOfBalls, leftRow, rightRow, collectDate, userID,
                        String.valueOf(latitude), String.valueOf(longitude), companyID));

                if (formTypeID == FormTypes.SAMPLING_STATION_ONE) {
                    Log.i(TAG, FormTypes.SAMPLING_STATION_ONE);
                    YieldEstimateFragment yieldFragment = new YieldEstimateFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("form_type_id", FormTypes.SAMPLING_STATION_TWO);
                    yieldFragment.setArguments(bundle);

                    insertFragment(R.id.fragment_container, yieldFragment, FormTypes.SAMPLING_STATION_TWO);

                } else if (formTypeID == FormTypes.SAMPLING_STATION_TWO) {
                    Log.i(TAG, FormTypes.SAMPLING_STATION_TWO);
                    YieldEstimateFragment yieldFragment = new YieldEstimateFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("form_type_id", FormTypes.SAMPLING_STATION_THREE);
                    yieldFragment.setArguments(bundle);

                    insertFragment(R.id.fragment_container, yieldFragment, FormTypes.SAMPLING_STATION_THREE);

                } else if (formTypeID == FormTypes.SAMPLING_STATION_THREE) {
                    Log.i(TAG, FormTypes.SAMPLING_STATION_THREE);
                    YieldEstimateFragment yieldFragment = new YieldEstimateFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("form_type_id", FormTypes.SAMPLING_STATION_FOUR);
                    yieldFragment.setArguments(bundle);

                    insertFragment(R.id.fragment_container, yieldFragment, FormTypes.SAMPLING_STATION_FOUR);

                } else if (formTypeID == FormTypes.SAMPLING_STATION_FOUR) {
                    Log.i(TAG, FormTypes.SAMPLING_STATION_FOUR);
                    YieldEstimateFragment yieldFragment = new YieldEstimateFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("form_type_id", FormTypes.SAMPLING_STATION_FIVE);
                    yieldFragment.setArguments(bundle);

                    insertFragment(R.id.fragment_container, yieldFragment, FormTypes.SAMPLING_STATION_FIVE);

                } else if (formTypeID == FormTypes.SAMPLING_STATION_FIVE) {
                    Log.i(TAG, FormTypes.SAMPLING_STATION_FIVE);
                    saveForms();

                    double yieldEstimate = calculateYieldEstimate(farmID);

                    Intent intent = new Intent(mContext, AllFarmAssessmentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MyAlerts.toActivityDialog(mContext,"Yield Estimate is " + yieldEstimate + " Kgs.\nData saved offline",intent);

                }

            } else {
                Toast.makeText(mContext,
                        R.string.fill_in_all_details, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveForms() {
        for (Object form : ((YieldActivity) mContext).yieldEstimateStack) {
            db.addYieldEstimate((YieldEstimate) form);
        }
    }

    private void initData() {
        db = new DatabaseHandler(mContext);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            formTypeID = bundle.getString("form_type_id");
        }

        farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
        userID = Preferences.USER_ID;
        companyID = Preferences.COMPANY_ID;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gpsTracker = new GPSTracker(mContext);

        date = new Date();
    }

    private void insertFragment(int layout, Fragment frag, String tag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, frag, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private double calculateYieldEstimate(String farmId) {
        List<YieldEstimate> estimates = db.getAllYieldEstimatesForFarm(farmId);
        /*To be gotten from ward data*/
        double avgBollWeight;
        try {
            avgBollWeight = db.getBollWeightForFarm(farmId);
        } catch (NullPointerException ex) {
            avgBollWeight = 3;
        }

        int plantsTotal = 0;
        int bollsTotal = 0;
        double disLeftTotal = 0;
        double disRightTotal = 0;
        /*get farm area*/
        double tempFarmArea = Double.valueOf(db.getFarmArea(farmId));
        double farmArea = tempFarmArea / 2.4711;

        for (YieldEstimate cn : estimates) {
            plantsTotal += Integer.parseInt(cn.getNumOfPlants());
            bollsTotal += Integer.parseInt(cn.getNumOfBolls());
            disLeftTotal += Double.parseDouble(cn.getDistanceToLeft());
            disRightTotal += Double.parseDouble(cn.getDistanceToRight());

        }

        double avgPlantsPerMeter = Math.round(plantsTotal / 15);
        double avgBollsPerPlant = Math.round(bollsTotal / plantsTotal);
        double avgRowWidth = Math.round((disLeftTotal + disRightTotal) / 1000);

        double production = farmArea * (10000 / avgRowWidth) * (avgPlantsPerMeter) * (avgBollsPerPlant) * (avgBollWeight / 1000);
        Log.i("YIELD ESTIMATE: ", "Farm Area: " + farmArea + " Plants Total: " + plantsTotal + " Bolls Total: " + bollsTotal + " Left: " + disLeftTotal
                + " Right: " + disRightTotal + " AVG Plants/m: " + avgPlantsPerMeter
                + " AVG Bolls/Plant: " + avgBollsPerPlant + " AVG Row Width: " + avgRowWidth + " production: " + production);
        return Math.round(production);
    }
}

