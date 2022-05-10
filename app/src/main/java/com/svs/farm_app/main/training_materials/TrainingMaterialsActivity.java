package com.svs.farm_app.main.training_materials;

import android.app.Activity;
import android.os.Bundle;

import com.svs.farm_app.R;

import droidninja.filepicker.FilePickerBuilder;

/**
 * Created by user on 1/29/2015.
 */
public class TrainingMaterialsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmat);
        FilePickerBuilder.getInstance().setMaxCount(10)
                //.setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickDocument(this);
    }
}
