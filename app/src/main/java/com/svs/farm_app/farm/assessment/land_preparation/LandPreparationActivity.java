package com.svs.farm_app.farm.assessment.land_preparation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.svs.farm_app.R;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandPreparationActivity extends BaseClass {

    private LandPreparationActivity mContext;
    public String farmID;
    public String companyID;
    public String userID;
    private DatabaseHandler db;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FragmentTransaction transaction;
    public Stack majorFormStack;
    public Stack mediumFormStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_fragment_holder);
        ButterKnife.bind(this);

        if (fragmentContainer != null) {
            if (savedInstanceState != null) {
                return;
            }

            LandClearingFragment landClearingFragment = new LandClearingFragment();

            insertFragment(R.id.fragment_container, landClearingFragment, "land_clearing");
        }

        mContext = LandPreparationActivity.this;

        initView();
        initData();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        farmID = MyPrefrences.getPrefrence(mContext, "farm_id");
        userID = Preferences.USER_ID;
        companyID = Preferences.COMPANY_ID;

        majorFormStack = new Stack();
        mediumFormStack = new Stack();
    }

    private void insertFragment(int layout, Fragment frag, String tag) {
        FragmentManager manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(layout, frag, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

            if (getFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    if (getFragmentManager().getBackStackEntryCount() == 1) {
                        finish();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
