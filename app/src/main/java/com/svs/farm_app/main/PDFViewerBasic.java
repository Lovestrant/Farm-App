package com.svs.farm_app.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.svs.farm_app.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PDFViewerBasic extends AppCompatActivity {

    private static final String TAG = PDFViewerBasic.class.getSimpleName();
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String PDFPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Training Materials");

        Intent intent = getIntent();
        PDFPath = intent.getStringExtra("pdf_path");
        Log.i(TAG,"pdf path: "+PDFPath+" Exists: "+new File(PDFPath).exists());

        pdfView.fromFile(new File(PDFPath)).load();
    }

}
