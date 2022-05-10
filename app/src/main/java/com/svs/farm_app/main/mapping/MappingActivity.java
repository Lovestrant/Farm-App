package com.svs.farm_app.main.mapping;

/**
 * Created by ADMIN on 22-Aug-17.
 */


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.authentication.activity.BaseActivity;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.svs.farm_app.R;
import com.svs.farm_app.asynctask.uploads.UploadMappedFarms;
import com.svs.farm_app.databinding.ActivityMappingBinding;
import com.svs.farm_app.entities.MappedFarm;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MapUtils;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.Preferences;

import java.util.ArrayList;

import br.com.safety.locationlistenerhelper.core.LocationTracker;
import br.com.safety.locationlistenerhelper.core.SettingsLocationTracker;

public class MappingActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = MappingActivity.class.getSimpleName();
    private GoogleMap mMap;
    Button bStart;
    Button bFinish;
    TextView tvArea;
    TextView tvPerimeter;
    TextView tvAccuracy;
    Toolbar toolbar;
    private MappingActivity mContext;
    private LocationTracker locationTracker;
    private ArrayList<LatLng> points;
    private String farmId;
    private ConnectionDetector cd;
    private double area, perimeter;
    private DatabaseHandler db;
    boolean canClickToAddPoint = false;
    boolean shouldFollowLocation = true;
    LatLng lastKnownPoint;

    public MappingActivity() {
    }
ActivityMappingBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_mapping);

        initView();

        points = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initListeners();

        initData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bStart = (Button) findViewById(R.id.bStartMapping);
        bFinish = (Button) findViewById(R.id.bFinishMapping);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvPerimeter = (TextView) findViewById(R.id.tvPerimeter);
        tvAccuracy = (TextView) findViewById(R.id.tvAccuracy);

        setSupportActionBar(b.myToolbar.toolbar);

        ((AppCompatActivity)this).getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bFinish.setEnabled(false);
        b.bAddPointMapping.setEnabled(false);
        b.undoIv.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initData() {
        mContext = MappingActivity.this;

        cd = new ConnectionDetector(mContext);
        db = new DatabaseHandler(mContext);

        Intent intent = getIntent();
        farmId = intent.getStringExtra("farm_id");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initListeners() {

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (points.size() > 0) {
                    //TODO: ask user to confirm to clear points
                }

                b.saveLinearLayout.setVisibility(View.GONE);

                bStart.setEnabled(false);
                bFinish.setEnabled(true);
                b.bAddPointMapping.setEnabled(true);
                mMap.clear();
                points.clear();
                canClickToAddPoint = true;
            }
        });
        b.bAddPointMapping.setOnClickListener(v -> {
          if (points.size()>0&&(points.get(points.size()-1))==lastKnownPoint) {
              Toast.makeText(mContext, "Wait to detect new point", Toast.LENGTH_SHORT).show();
              Log.e(TAG, "initListeners: Wait to detect new point" );

          }else{
              LatLng point = lastKnownPoint;
             addPoint(point);
          }
        });
        b.undoIv.setOnClickListener(v->{
            points.remove(points.size()-1);
            if (points.size()==0)
                b.undoIv.setVisibility(View.GONE);

            mMap.clear();

            PolygonOptions polygonOptions = new PolygonOptions().strokeColor(ContextCompat.getColor(mContext, R.color.colorPrimary)).fillColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                    .clickable(true).addAll(points);

            mMap.addPolygon(polygonOptions);
            Toast.makeText(mContext, "Point removed successfully", Toast.LENGTH_SHORT).show();
        });
        b.pauseIv.setOnClickListener(v->{
            shouldFollowLocation=!shouldFollowLocation;
        });

        bFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canClickToAddPoint=false;
                locationTracker.stopLocationService(MappingActivity.this);

                bStart.setEnabled(true);
                bFinish.setEnabled(false);
                b.bAddPointMapping.setEnabled(false);

                LatLng farmLatLng = null;
                String latitude = null, longitude = null;

                try {
                    farmLatLng = points.get(0);
                    latitude = Double.toString(farmLatLng.latitude);
                    longitude = Double.toString(farmLatLng.longitude);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }

                String allPoints = new Gson().toJson(points);
                String actualFarmArea = Double.toString(area);
                String farmPeri = Double.toString(perimeter);

                MappedFarm mappedFarm = new MappedFarm(farmId, latitude, longitude, allPoints, actualFarmArea, farmPeri, Preferences.USER_ID, Preferences.COMPANY_ID);

//                confirmSaving(mContext, R.string.kindly_confirm, mappedFarm);
                b.saveLinearLayout.setVisibility(View.VISIBLE);
                String toDisplay=getResources().getString(R.string.kindly_confirm) + " \nArea: " + area + " acres\nPerimeter: " + perimeter;
                b.areaTv.setText(toDisplay);
                b.submitBtn.setOnClickListener(v->{
                    if (area == 0) {
                        //MyAlerts.genericDialog(mContext,R.string.area_cannot_be_zero);
                        Toast.makeText(mContext, R.string.area_cannot_be_zero, Toast.LENGTH_LONG).show();

                    } else {
                        if (cd.isConnectingToInternet()) {
                            new UploadMappedFarms(mContext, mappedFarm).execute();
                        } else {
                            db.addMappedFarm(mappedFarm);
                            MyAlerts.toActivityDialog(mContext, R.string.saved_offline, MappingActivity.this);
                        }

                        bFinish.setEnabled(true);
                    }
                });
            }
        });
    }

    public void addPoint(LatLng point){
        if (!canClickToAddPoint)
            return;

        points.add(point);
        b.undoIv.setVisibility(View.VISIBLE);

        area = MapUtils.computeSignedArea(points, MapUtils.EARTH_RADIUS);
        perimeter = MapUtils.computeLength(points);

        new Handler().post(() -> {
            mMap.clear();

            PolygonOptions polygonOptions = new PolygonOptions().strokeColor(ContextCompat.getColor(mContext, R.color.colorPrimary)).fillColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                    .clickable(true).addAll(points);

            mMap.addPolygon(polygonOptions);

            tvArea.setText(area + " hectares");
            tvPerimeter.setText(perimeter + " m");

            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
        });
    }

    public void confirmSaving(final Context ctx, int dialogMessage, final MappedFarm mappedFarm) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(getResources().getString(dialogMessage) + " \nArea: " + area + " acres\nPerimeter: " + perimeter)
                .setCancelable(true)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (area == 0) {
                            //MyAlerts.genericDialog(mContext,R.string.area_cannot_be_zero);
                            Toast.makeText(mContext, R.string.area_cannot_be_zero, Toast.LENGTH_LONG).show();

                        } else {
                            if (cd.isConnectingToInternet()) {
                                new UploadMappedFarms(mContext, mappedFarm).execute();
                            } else {
                                db.addMappedFarm(mappedFarm);
                                MyAlerts.toActivityDialog(mContext, R.string.saved_offline, MappingActivity.this);
                            }

                            bFinish.setEnabled(true);
                        }

                    }
                }).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationTracker = new LocationTracker("my.action")
                .setInterval(3000)
                .setGps(true)
                .setNetWork(false)
                .start(getBaseContext(), MappingActivity.this);

        MyLocationReceiver receiver = new MyLocationReceiver(new Handler()); // Create the receiver
        registerReceiver(receiver, new IntentFilter("my.action")); // Register receiver
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        mMap.setOnMapClickListener(this::addPoint);

    }

    public class MyLocationReceiver extends BroadcastReceiver {


        private Handler handler = null;

        public MyLocationReceiver(Handler handler) {
            this.handler = handler;
        }

        public MyLocationReceiver() {

        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (null != intent && intent.getAction().equals("my.action")) {
                final Location locationData = (Location) intent.getParcelableExtra(SettingsLocationTracker.LOCATION_MESSAGE);
                Log.i("Location: ", "Latitude: " + locationData.getLatitude() + "Longitude:" + locationData.getLongitude());
                final double latitude = locationData.getLatitude();
                final double longitude = locationData.getLongitude();
                Log.i(TAG, latitude + "," + longitude);

                if (locationData != null) {
                    if (lastKnownPoint==null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(locationData.getLatitude(), locationData.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
//                        points.add(new LatLng(locationData.getLatitude(), locationData.getLongitude()));
                    }
                    lastKnownPoint = new LatLng(locationData.getLatitude(), locationData.getLongitude());


                    tvAccuracy.setText(Math.round(locationData.getAccuracy()) + " m");

                    if (shouldFollowLocation) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownPoint));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
                    }
                }
            }
        }

    }

}

