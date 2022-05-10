package com.svs.farm_app.main.recovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.authentication.activity.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.CollectedOrder;
import com.svs.farm_app.entities.CottonDeduction;
import com.svs.farm_app.entities.RecoveredCash;
import com.svs.farm_app.main.recovery.product_purchase.ProductPurchaseActivity;

import java.util.ArrayList;
import java.util.List;

public class FarmerHistoryActivity extends BaseActivity {

    private int farmerId;
    private List<CottonDeduction> farmersDeductions;
    private List<CollectedOrder> farmersCollectedOrders;
    private List<RecoveredCash> farmersCashRecoveries;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<FarmerHistory> farmerHistory;
    private FarmerHistoryAdapter mAdapter;
    private final String TAG = FarmerHistoryActivity.class.getSimpleName();
    private Context mContext;
    private Toolbar toolbar;
    private Button bNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_history);
        initData();
        initView();
        initListeners();
    }

    private void initListeners() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FarmerHistory history = farmerHistory.get(position);
                //Toast.makeText(getApplicationContext(), history.totalAmount + " is selected!"+history.seasonName, Toast.LENGTH_SHORT).show();
                //TODO: More details on orders
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cottonPurchase = new Intent(mContext,ProductPurchaseActivity.class);
                cottonPurchase.putExtra("farmer_id",farmerId);
                Log.i(TAG,"Farmer ID;"+farmerId);
                startActivity(cottonPurchase);
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvFarmerHistory);
        bNext = (Button) findViewById(R.id.bNext);

        mAdapter = new FarmerHistoryAdapter(farmerHistory);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mContext = getApplicationContext();

        Intent intent = getIntent();
        try {
            farmerId = Integer.parseInt(intent.getStringExtra("farmer_id"));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        /*Get all farmers deductions for each season*/
        Gson gson = new GsonBuilder().create();

       // String cottonDeductionsJSON = MyPrefrences.getPrefrence(mContext, Config.COTTON_DEDUCTIONS);
        //Log.i(TAG, cottonDeductionsJSON);

        List<CottonDeduction> allDeductions = db.getAllCottonDeductions();

        farmersDeductions = getFarmersCottonDeductions(allDeductions, farmerId);
        /*Get all cash recoveries*/
        //String recoveredCashJSON = MyPrefrences.getPrefrence(mContext, Config.RECOVERED_CASH);
        //Log.i(TAG, recoveredCashJSON);

        List<RecoveredCash> recoveredCash = db.getAllRecoveredCash();

        farmersCashRecoveries = getFarmersCashRecoveries(recoveredCash, farmerId);

        /*Get all farmers collected orders for each season*/
        //String collectedOrdersJSON = MyPrefrences.getPrefrence(mContext, Config.COLLECTED_ORDERS);
        //Log.i(TAG, collectedOrdersJSON);

        List<CollectedOrder> allCollectedOrders = db.getAllCollectedOrders();

        farmersCollectedOrders = getFarmersCollectedOrders(allCollectedOrders, farmerId);

        /*Get overall difference*/
        //TODO: Green - Paid, Yellow - Partially paid, Red - Not paid
        //totalAmountCollected = getTotalAmountCollected(farmerId,farmersCollectedOrders);
        farmerHistory = getFarmerHistory(farmersCollectedOrders, farmersDeductions, farmersCashRecoveries);
        Log.i(TAG, "farmer history: " + farmerHistory.size());
    }

    /**
     * Creates a history from collected orders, cotton delivery deductions and cash collections
     *
     * @param farmersCollectedOrders
     * @param farmersDeductions
     * @param farmersCashRecoveries
     * @return
     */
    private List<FarmerHistory> getFarmerHistory(List<CollectedOrder> farmersCollectedOrders, List<CottonDeduction> farmersDeductions, List<RecoveredCash> farmersCashRecoveries) {
        List<FarmerHistory> farmerHistory = new ArrayList<>();

        for (CollectedOrder collectedOrder : farmersCollectedOrders) {
            FarmerHistory history = new FarmerHistory(collectedOrder.getFarmerId(), collectedOrder.getTotalAmount(), collectedOrder.getSeasonId(), collectedOrder.getSeasonName());
            for (CottonDeduction cottonDeduction : farmersDeductions) {
                if (collectedOrder.getSeasonId() == cottonDeduction.getSeasonId()) {
                    history.totalPaid += cottonDeduction.getDeductions();
                }
            }

            for (RecoveredCash cash : farmersCashRecoveries) {
                if (collectedOrder.getSeasonId() == cash.getSeasonId()) {
                    history.totalPaid += cash.getTotalAmount();
                }
            }

            farmerHistory.add(history);
        }

        return farmerHistory;
    }


    /**
     * Get the total amount
     *
     * @param farmerId
     * @param farmersCollectedOrders
     * @return
     */
    private float getTotalAmountCollected(int farmerId, List<CollectedOrder> farmersCollectedOrders) {
        float totalAmount = 0;
        for (CollectedOrder collectedOrder : farmersCollectedOrders) {
            if (farmerId == collectedOrder.getFarmerId()) {
                totalAmount += collectedOrder.getTotalAmount();
            }
        }

        return totalAmount;
    }

    private List<RecoveredCash> getFarmersCashRecoveries(List<RecoveredCash> allRecoveredCash, int farmerId) {
        List<RecoveredCash> recoveredCash = new ArrayList<>();

        for (RecoveredCash cash : allRecoveredCash) {
            if (farmerId == cash.getFarmerId()) {
                recoveredCash.add(cash);
            }
        }

        return recoveredCash;

    }

    private List<CollectedOrder> getFarmersCollectedOrders(List<CollectedOrder> allCollectedOrders, int farmerId) {
        List<CollectedOrder> collectedOrders = new ArrayList<>();

        for (CollectedOrder collectedOrder : allCollectedOrders) {
            if (farmerId == collectedOrder.getFarmerId()) {
                collectedOrders.add(collectedOrder);
            }
        }

        return collectedOrders;

    }

    private List<CottonDeduction> getFarmersCottonDeductions(List<CottonDeduction> allDeductions, int farmerId) {
        List<CottonDeduction> deductions = new ArrayList<>();

        for (CottonDeduction cottonDeduction : allDeductions) {
            if (farmerId == cottonDeduction.getFarmerId()) {
                deductions.add(cottonDeduction);
            }
        }

        return deductions;

    }
}
