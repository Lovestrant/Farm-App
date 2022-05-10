package com.svs.farm_app.main.show_intent;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;
import com.svs.farm_app.farmersearch.FarmerSearchActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Benson on 3/16/2015.
 */
public class ShowIntentActivity extends BaseClass {
    @BindView(R.id.cConductCode)
    CheckBox conduct;
    @BindView(R.id.cFarmersContract)
    CheckBox contract;
    @BindView(R.id.bSign)
    Button sign;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    final private String TAG = ShowIntentActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_doc);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initListeners();
    }

    private void initListeners() {
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showIntent = new Intent(getApplicationContext(),
                        FarmerSearchActivity.class);

                showIntent.putExtra(Config.TO_ACTIVITY, "show_intent");
                if (conduct.isChecked() && contract.isChecked()) {
                    startActivity(showIntent);
                } else {
                    new MaterialStyledDialog.Builder(ShowIntentActivity.this)
                            .setTitle(R.string.app_name)
                            .setStyle(Style.HEADER_WITH_TITLE)
                            .setDescription(R.string.check_all_boxes)
                            .setCancelable(true)
                            .setPositiveText(R.string.ok)
                            .show();
                }
            }
        });
    }

}
