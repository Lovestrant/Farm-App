package com.svs.farm_app.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.legacy.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;

public class BaseClass extends AppCompatActivity {
    public static final String TAG = BaseClass.class.getSimpleName();
    private GPSTracker gps;

    @Override
    protected void onResume() {
        super.onResume();
        //checkPermission();
        Preferences.loadPreferenceSettings(BaseClass.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preferences.savePrefenceSettings(BaseClass.this);
        turnOffGPS();
    }

    //	@Override
//	protected void onStop() {
//		super.onDestroy();
//		turnOffGPS();
//	}
    @Override
    protected void onPause() {
        super.onPause();
        Preferences.savePrefenceSettings(BaseClass.this);
        turnOffGPS();
    }

    public void turnOffGPS() {
        gps = new GPSTracker(BaseClass.this);
        if (gps.isGPSEnabled) {
            gps.stopUsingGPS();
        }

    }

/*
    public void checkPermission(){
		gps = new GPSTracker(BaseClass.this);
		if (!gps.isGPSEnabled) {
			Toast.makeText(BaseClass.this, "GPS must be on to use Farm App", Toast.LENGTH_LONG).show();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gps.goToLocationSettings();
				}
			});

		}else{
			gps.getLocation();
		}
	}
*/

    /**
     * Make sure the Permission is on depending on the version of android running on the device.
     */
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, ">= lollipop");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED)) {

            MaterialStyledDialog.Builder GPSDialog = new MaterialStyledDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setStyle(Style.HEADER_WITH_TITLE)
                    .setDescription(R.string.app_permissions)
                    .setPositiveText(R.string.ok)

                    .onPositive(new MaterialDialog.SingleButtonCallback() {

                        public static final int PERMISSION_REQUEST_CODE = 1;

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.NFC},
                                        PERMISSION_REQUEST_CODE);
                                checkGPS();
                            }
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog
                                                    dialog, @NonNull DialogAction which) {

                            finish();
                        }
                    });

            GPSDialog.show();

            } else {
                Log.i(TAG, "update GPS Location");
                gps = new GPSTracker(BaseClass.this);
                gps.getLocation();
            }
        } else {
            Log.i(TAG, "<lollipop");

            checkGPS();
        }
    }

    public void checkGPS() {
        gps = new GPSTracker(BaseClass.this);
        if (!gps.isGPSEnabled) {

            showGPSDisabledAlertToUser();

        } else {
            gps.getLocation();
        }

    }

    /**
     * Show GPS dialog when GPS is disabled
     */
    private void showGPSDisabledAlertToUser() {

        new MaterialStyledDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(R.string.gps_disabled)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        gps.goToLocationSettings();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        onPause();
                    }
                })
                .show();
    }


}
