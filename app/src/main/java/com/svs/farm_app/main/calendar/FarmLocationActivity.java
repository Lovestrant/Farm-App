package com.svs.farm_app.main.calendar;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;

import com.authentication.utils.ToastUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svs.farm_app.R;

public class FarmLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = FarmLocationActivity.class.getSimpleName();
    private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_farm_location);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		Intent intent = getIntent();

		try {
			double latitude = Double.valueOf(intent.getStringExtra("latitude"));
			double longitude = Double.valueOf(intent.getStringExtra("longitude"));


		String lastName =intent.getStringExtra("lname");
		String firstName =intent.getStringExtra("fname");

        String details = firstName+" "+lastName;
        Log.i(TAG,details);

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
				.title("Demo farm").snippet(details).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

		marker.showInfoWindow();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 14.0f) );

		}catch (NullPointerException ex){
			ex.printStackTrace();
			ToastUtil.showToast(getApplicationContext(),R.string.farm_not_mapped);
		}
	}
}
