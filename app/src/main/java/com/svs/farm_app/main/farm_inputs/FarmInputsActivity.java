package com.svs.farm_app.main.farm_inputs;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.svs.farm_app.R;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 12/31/2014.
 */
public class FarmInputsActivity extends BaseClass {

    private DatabaseHandler db;
    private MyCursorAdapter dataAdapter;
    private String orderID;
    private String genID;
    private String collectDate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lvFarmInputs)
    ListView lvFarmInputs;
    @BindView(R.id.etFarmerFilter)
    EditText etFarmerFilter;
    private FarmInputsActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_inputs_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = FarmInputsActivity.this;
        initView();
        initData();
        initListeners();
    }

    private void initListeners() {
        lvFarmInputs.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                orderID = cursor.getString(cursor.getColumnIndexOrThrow(db.KEY_ORDER_ID));

                genID = cursor.getString(cursor.getColumnIndexOrThrow(db.KEY_GEN_ID));
                String farmerID = cursor.getString(cursor.getColumnIndexOrThrow(db.KEY_FARMER_ID));

                MyPrefrences.savePrefrence(mContext, "wack_fid", farmerID);
                MyPrefrences.savePrefrence(mContext, "wack_gen_id", genID);

                Log.i("GEN_ID: ", genID);

                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                collectDate = dateFormat.format(date);


                String collectDate = dateFormat.format(date);
                db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
                        Preferences.USER_ID, "Auto"));
//						Preferences.USER_ID,Config.FINGERPRINT));

                db.deleteAssignedInput(orderID);

                initData();
//				Intent verifyFarmer = null;
//
//				if (Preferences.DEVICE_MODEL.equals(Config.TPS350)) {
//
//					verifyFarmer = new Intent(FarmInputsActivity.this, TPS350FingerprintVerificationActivity.class);
//
//					Log.e(TAG, "onItemClick-testItem: "+ (verifyFarmer==null));
//					verifyFarmer.putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_VERIFICATION);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_GEN_ID, genID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_ORDER_ID, orderID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_COLLECT_DATE, collectDate);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID);
//
//				}else if (Preferences.DEVICE_MODEL.equals(Config.TPS900)) {
//
//					verifyFarmer = new Intent(FarmInputsActivity.this, TPS900FingerprintVerificationActivity.class);
//
//					Log.e(TAG, "onItemClick-testItem: "+ (verifyFarmer==null));
//					verifyFarmer.putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_VERIFICATION);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_GEN_ID, genID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_ORDER_ID, orderID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_COLLECT_DATE, collectDate);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID);
//
//				} else if (Preferences.DEVICE_MODEL.equals(Config.FP05)) {
//
//					verifyFarmer = new Intent(FarmInputsActivity.this, FP05FingerprintVerificationActivity.class);
//
//					Log.e(TAG, "onItemClick-testItem: "+ (verifyFarmer==null));verifyFarmer.putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_VERIFICATION);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_GEN_ID, genID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_ORDER_ID, orderID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_COLLECT_DATE, collectDate);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID);
//
//				}else if(Preferences.DEVICE_MODEL.equals(Config.COREWISE_V0)){
//
//					verifyFarmer = new Intent(FarmInputsActivity.this, FingerprintActivityLeft.class);
//
//					Log.e(TAG, "onItemClick-testItem: "+ (verifyFarmer==null));
//					verifyFarmer.putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_VERIFICATION);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_GEN_ID, genID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_ORDER_ID, orderID);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_COLLECT_DATE, collectDate);
//					verifyFarmer.putExtra(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID);
//				}
//
//				Log.e(TAG, "onItemClick-testItem: "+Config.FINGERPRINT_VERIFICATION );
//				Log.e(TAG, "onItemClick-testItem: "+genID );
//				Log.e(TAG, "onItemClick-testItem: "+orderID );
//				Log.e(TAG, "onItemClick-testItem: "+ collectDate);
//				Log.e(TAG, "onItemClick-testItem: "+ Preferences.USER_ID);
//				Log.e(TAG, "onItemClick-testItem: "+ (verifyFarmer==null));
//
//				startActivity(verifyFarmer);

            }
        });

        etFarmerFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                dataAdapter.getFilter().filter(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void initView() {
    }

    private void initData() {

        db = new DatabaseHandler(this);

        Cursor cursor = db.getAllAssignedInputsCursor();

        String[] columns = new String[]{DatabaseHandler.KEY_GEN_ID, DatabaseHandler.KEY_INPUT_TYPE, DatabaseHandler.KEY_INPUT_QUANTITY, DatabaseHandler.KEY_INPUT_TOTAL,
                DatabaseHandler.KEY_ORDER_NUM, DatabaseHandler.KEY_FNAME, DatabaseHandler.KEY_LNAME};

        int[] to = new int[]{R.id.tvGenID, R.id.tvInputType, R.id.tvInputQuantity, R.id.tvInputTotal,
                R.id.tvOrderNum};

        dataAdapter = new MyCursorAdapter(this, R.layout.activity_farm_inputs_details, cursor, columns, to, 0);

        lvFarmInputs.setAdapter(dataAdapter);

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                Log.e(TAG, "runQuery: ");
                return db.fetchInputsByCardNo(constraint.toString());
            }
        });

    }

    private class MyCursorAdapter extends SimpleCursorAdapter {
        String[] from;
        int[] to;

        public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.from = from;
            this.to = to;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvGenID = (TextView) view.findViewById(R.id.tvGenID);
            tvGenID.setText(cursor.getString(cursor.getColumnIndex(from[0])));

            TextView tvInputType = (TextView) view.findViewById(R.id.tvInputType);
            tvInputType.setText("");

            TextView tvInputQuantity = (TextView) view.findViewById(R.id.tvInputQuantity);
            tvInputQuantity.setText("");

            String temp = cursor.getString(cursor.getColumnIndex(from[1]));

            String[] parseInputTypes = temp.split(",");
            for (int i = 0; i < parseInputTypes.length; i++) {
                tvInputType.append(parseInputTypes[i].trim() + "\n");
            }

            String tempInputQuantity = cursor.getString(cursor.getColumnIndex(from[2]));
            String[] parseInputQuantity = tempInputQuantity.split(",");
            for (int i = 0; i < parseInputQuantity.length; i++) {
                tvInputQuantity.append(parseInputQuantity[i].trim() + "\n");
            }

            TextView tvInputTotal = (TextView) view.findViewById(R.id.tvInputTotal);
            tvInputTotal.setText(cursor.getString(cursor.getColumnIndex(from[3])));

            TextView tvOrderNum = (TextView) view.findViewById(R.id.tvOrderNum);
            tvOrderNum.setText(cursor.getString(cursor.getColumnIndex(from[4])));

            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            tvName.setText(cursor.getString(cursor.getColumnIndex(from[6])) + " " + cursor.getString(cursor.getColumnIndex(from[5])));
        }

        @Override
        public Filter getFilter() {
            return super.getFilter();
        }
    }
}
