/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.farm.assessment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.FarmDataColleted;
import com.svs.farm_app.farm.assessment.farm_income.FarmIncomeActivity;
import com.svs.farm_app.farm.assessment.farm_production.FarmProductionActivity;
import com.svs.farm_app.farm.assessment.five_fingers.FingerFiveActivity;
import com.svs.farm_app.farm.assessment.five_fingers.FingerFourActivity;
import com.svs.farm_app.farm.assessment.five_fingers.FingerOneActivity;
import com.svs.farm_app.farm.assessment.five_fingers.FingerThreeActivity;
import com.svs.farm_app.farm.assessment.five_fingers.FingerTwoActivity;
import com.svs.farm_app.farm.assessment.germination.GerminationActivity;
import com.svs.farm_app.farm.assessment.grading_and_bailing.GradingActivity;
import com.svs.farm_app.farm.assessment.harvesting.HarvestingActivity;
import com.svs.farm_app.farm.assessment.herbicide_application.HerbApplicationActivity;
import com.svs.farm_app.farm.assessment.land_preparation.LandPreparationActivity;
import com.svs.farm_app.farm.assessment.molasses.MolassesActivity;
import com.svs.farm_app.farm.assessment.planting.RowPlantingActivity;
import com.svs.farm_app.farm.assessment.post_harvest.StalkDestructionActivity;
import com.svs.farm_app.farm.assessment.replanting_gap_filling_thinning.ReplantingActivity;
import com.svs.farm_app.farm.assessment.scouting_pest_control.ScoutingActivity;
import com.svs.farm_app.farm.assessment.soil_fertility.SoilFertilityActivity;
import com.svs.farm_app.farm.assessment.trans_hse_market.TransportHseToMarketActivity;
import com.svs.farm_app.farm.assessment.utils.FormTypes;
import com.svs.farm_app.farm.assessment.weeding.WeedingActivity;
import com.svs.farm_app.farm.assessment.yield_estimate.YieldActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyPrefrences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllFarmAssessmentActivity extends BaseClass {

    public static final String TAG = AllFarmAssessmentActivity.class.getSimpleName();
    public static final String FIRST_PICKING = "1";
    public static final String SECOND_PICKING = "2";
    public static final String THIRD_PICKING = "3";
    public static final String FOURTH_PICKING = "4";

    private final String TRANSPORT_ONE = "1";
    private final String TRANSPORT_TWO = "2";
    private final String TRANSPORT_THREE = "3";
    private final String TRANSPORT_FOUR = "4";

    private final String DELIVERY_ONE = "1";
    private final String DELIVERY_TWO = "2";
    private final String DELIVERY_THREE = "3";
    private final String DELIVERY_FOUR = "4";

    private List<FarmDataColleted> tempFormTypes;

    private MySimpleArrayAdapter myAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listview)
    ListView lvDataCollected;
    private AllFarmAssessmentActivity mContext;
    private DatabaseHandler db;
    private String farmId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farm_assessment_list_view);
        ButterKnife.bind(this);

        mContext = AllFarmAssessmentActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initListeners();
    }

    private void initListeners() {
        lvDataCollected.setOnItemClickListener(new OnItemClickListener() {
            private Intent next;

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        next = new Intent(mContext, GoodRainsActivity.class);
                        startActivity(next);
                        break;
                    case 1:
                        next = new Intent(mContext, OtherCropsActivity.class);
                        startActivity(next);
                        break;
                    case 2:
                        next = new Intent(mContext, LandPreparationActivity.class);
                        startActivity(next);
                        break;
                    case 3:
                        next = new Intent(mContext, RowPlantingActivity.class);
                        startActivity(next);
                        break;
                    case 4:
                        next = new Intent(mContext, HerbApplicationActivity.class).putExtra("form_type_id", FormTypes.HERBICIDE_APPLICATION_ONE);
                        startActivity(next);
                        break;
                    case 5:
                        next = new Intent(mContext, HerbApplicationActivity.class).putExtra("form_type_id", FormTypes.HERBICIDE_APPLICATION_TWO);
                        startActivity(next);
                        break;
                    case 6:
                        next = new Intent(mContext, GerminationActivity.class);
                        startActivity(next);
                        break;
                    case 7:
                        next = new Intent(mContext, ReplantingActivity.class);
                        startActivity(next);
                        break;
                    case 8:
                        next = new Intent(mContext, SoilFertilityActivity.class);
                        startActivity(next);
                        break;
                    case 9:
                        next = new Intent(mContext, WeedingActivity.class).putExtra("form_type_id", FormTypes.WEEDING_ONE);
                        startActivity(next);
                        break;
                    case 10:
                        next = new Intent(mContext, WeedingActivity.class).putExtra("form_type_id", FormTypes.WEEDING_TWO);
                        startActivity(next);
                        break;
                    case 11:
                        next = new Intent(mContext, WeedingActivity.class).putExtra("form_type_id", FormTypes.WEEDING_THREE);
                        startActivity(next);
                        break;
                    case 12:
                        next = new Intent(mContext, WeedingActivity.class).putExtra("form_type_id", FormTypes.WEEDING_FOUR);
                        startActivity(next);
                        break;
                    case 13:
                        next = new Intent(mContext, FingerOneActivity.class);
                        startActivity(next);
                        break;
                    case 14:
                        next = new Intent(mContext, FingerTwoActivity.class);
                        startActivity(next);
                        break;
                    case 15:
                        next = new Intent(mContext, FingerThreeActivity.class);
                        startActivity(next);
                        break;
                    case 16:
                        next = new Intent(mContext, FingerFourActivity.class);
                        startActivity(next);
                        break;
                    case 17:
                        next = new Intent(mContext, MolassesActivity.class);
                        startActivity(next);
                        break;
                    case 18:
                        next = new Intent(mContext, ScoutingActivity.class);
                        startActivity(next);
                        break;
                    case 19:
                        next = new Intent(mContext, FingerFiveActivity.class);
                        startActivity(next);
                        break;
                    case 20:
                        next = new Intent(mContext, YieldActivity.class);
                        startActivity(next);
                        break;
                    case 21:
                        next = new Intent(mContext, HarvestingActivity.class)
                                .putExtra("form_type_id", FormTypes.FIRST_HARVEST)
                                .putExtra("form_type_id2", FormTypes.FIRST_TRANSPORT_FIELD_TO_HOUSE);
                        startActivity(next);
                        break;
                    case 22:
                        next = new Intent(mContext, HarvestingActivity.class)
                                .putExtra("form_type_id", FormTypes.SECOND_HARVEST)
                                .putExtra("form_type_id2", FormTypes.SECOND_TRANSPORT_FIELD_TO_HOUSE);
                        startActivity(next);
                        break;
                    case 23:
                        next = new Intent(mContext, HarvestingActivity.class)
                                .putExtra("form_type_id", FormTypes.THIRD_HARVEST)
                                .putExtra("form_type_id2", FormTypes.THIRD_TRANSPORT_FIELD_TO_HOUSE);
                        startActivity(next);
                        break;
                    case 24:
                        next = new Intent(mContext, HarvestingActivity.class)
                                .putExtra("form_type_id", FormTypes.FOURTH_HARVEST)
                                .putExtra("form_type_id2", FormTypes.FOUR_TRANSPORT_FIELD_TO_HOUSE);
                        startActivity(next);
                        break;
                    case 25:
                        next = new Intent(mContext, GradingActivity.class)
                                .putExtra("form_type_id", FormTypes.FIRST_GRADING)
                                .putExtra("form_type_id2", FormTypes.FIRST_BAILING);
                        startActivity(next);
                        break;
                    case 26:
                        next = new Intent(mContext, GradingActivity.class)
                                .putExtra("form_type_id", FormTypes.SECOND_GRADING)
                                .putExtra("form_type_id2", FormTypes.SECOND_BAILING);
                        startActivity(next);
                        break;
                    case 27:
                        next = new Intent(mContext, GradingActivity.class)
                                .putExtra("form_type_id", FormTypes.THIRD_GRADING)
                                .putExtra("form_type_id2", FormTypes.THIRD_BAILING);
                        startActivity(next);
                        break;
                    case 28:
                        next = new Intent(mContext, GradingActivity.class)
                                .putExtra("form_type_id", FormTypes.FOURTH_GRADING)
                                .putExtra("form_type_id2", FormTypes.FOURTH_BAILING);
                        startActivity(next);
                        break;
                    case 29:
                        next = new Intent(mContext, FarmProductionActivity.class).putExtra("picking_count", FIRST_PICKING);
                        startActivity(next);
                        break;
                    case 30:
                        next = new Intent(mContext, FarmProductionActivity.class).putExtra("picking_count", SECOND_PICKING);
                        startActivity(next);
                        break;
                    case 31:
                        next = new Intent(mContext, FarmProductionActivity.class).putExtra("picking_count", THIRD_PICKING);
                        startActivity(next);
                        break;
                    case 32:
                        next = new Intent(mContext, FarmProductionActivity.class).putExtra("picking_count", FOURTH_PICKING);
                        startActivity(next);
                        break;
                    case 33:
                        next = new Intent(mContext, TransportHseToMarketActivity.class).putExtra("transport_count", TRANSPORT_ONE);
                        startActivity(next);
                        break;
                    case 34:
                        next = new Intent(mContext, TransportHseToMarketActivity.class).putExtra("transport_count", TRANSPORT_TWO);
                        startActivity(next);
                        break;
                    case 35:
                        next = new Intent(mContext, TransportHseToMarketActivity.class).putExtra("transport_count", TRANSPORT_THREE);
                        startActivity(next);
                        break;
                    case 36:
                        next = new Intent(mContext, TransportHseToMarketActivity.class).putExtra("transport_count", TRANSPORT_FOUR);
                        startActivity(next);
                        break;
                    case 37:
                        next = new Intent(mContext, FarmIncomeActivity.class).putExtra("delivery_count", DELIVERY_ONE);
                        startActivity(next);
                        break;
                    case 38:
                        next = new Intent(mContext, FarmIncomeActivity.class).putExtra("delivery_count", DELIVERY_TWO);
                        startActivity(next);
                        break;
                    case 39:
                        next = new Intent(mContext, FarmIncomeActivity.class).putExtra("delivery_count", DELIVERY_THREE);
                        startActivity(next);
                        break;
                    case 40:
                        next = new Intent(mContext, FarmIncomeActivity.class).putExtra("delivery_count", DELIVERY_FOUR);
                        startActivity(next);
                        break;
                    case 41:
                        next = new Intent(mContext, StalkDestructionActivity.class).putExtra("form_type_id", FormTypes.STALK_DESTRUCTION_ONE);
                        startActivity(next);
                        break;
                    case 42:
                        next = new Intent(mContext, StalkDestructionActivity.class).putExtra("form_type_id", FormTypes.STALK_DESTRUCTION_TWO);
                        startActivity(next);
                        break;
                }
            }
        });
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        farmId = MyPrefrences.getPrefrence(mContext, "farm_id");

        tempFormTypes = db.getAllFarmDataCollectedByFarmId(farmId);

        String[] formNames = getResources().getStringArray(R.array.farm_assesments);

        myAdapter = new MySimpleArrayAdapter(this, formNames);
        lvDataCollected.setAdapter(myAdapter);
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<String> {

        private String[] formNames;

        private MySimpleArrayAdapter(Context context, String[] formNames) {
            super(context, 0, formNames);
            this.formNames = formNames;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //if (convertView == null) {
            convertView = inflater.inflate(R.layout.farm_assesment_list_item, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
            Log.i(TAG, "convertView is NULL");
            /*} else {
                Log.i(TAG,"convertView NOT NULL");
                holder = (ViewHolder) convertView.getTag();
            }*/

            holder.tvAssessment.setText(formNames[position]);

            String firstLetter = String.valueOf(formNames[position].charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL;

            int color = generator.getColor(getItem(position));

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color);

            holder.ivAssessmentIcon.setImageDrawable(drawable);

            if (tempFormTypes.size() > 0) {
                String[] formTypes = TextUtils.split(tempFormTypes.get(0).getFormTypeID(), ",");

                if (formTypes.length > 0) {
                    for (String cn : formTypes) {
                        try {
                            if (Integer.parseInt(cn) == (position + 1)) {
                                Log.i(TAG, "POSITION: " + position + " holder position: " + position);
                                holder.tick.setVisibility(View.VISIBLE);
                            }
                        } catch (NumberFormatException ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            private ImageView ivAssessmentIcon;
            private TextView tvAssessment;
            private ImageView tick;

            public ViewHolder(View v) {
                ivAssessmentIcon = (ImageView) v.findViewById(R.id.ivAssessmentIcon);
                tvAssessment = (TextView) v.findViewById(R.id.tvAssessment);
                tick = (ImageView) v.findViewById(R.id.tick);
            }
        }

    }

}
