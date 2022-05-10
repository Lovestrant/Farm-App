package com.authentication.activity;

import android.os.Bundle;
import android.os.HandlerThread;

import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import android_serialport_api.SerialPortManager;

public class BaseActivity extends BaseClass {
    protected MyApplication application;
    protected HandlerThread handlerThread;
    protected DatabaseHandler db;
    protected ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) getApplicationContext();
        db = new DatabaseHandler(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Preferences.DEVICE_MODEL.equals(Config.COREWISE_V0)) {
            if (!SerialPortManager.getInstance().isOpen()) {
                SerialPortManager.getInstance().openSerialPort();
            }
        }

        handlerThread = application.getHandlerThread();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Preferences.DEVICE_MODEL.equals(Config.COREWISE_V0)) {
            if (SerialPortManager.getInstance().isOpen()) {
                SerialPortManager.getInstance().closeSerialPort();
            }
        }
        handlerThread = null;
    }


}
