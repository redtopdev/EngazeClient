package com.redtop.engaze;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.MarkerHelper;

@SuppressLint("ResourceAsColor")
public class ShowLocationActivity extends BaseActivity implements OnMapReadyCallback {

	static LatLng mLatlong = new LatLng(0, 0);
	private String mLocation;
	private String mDestinatonAddress;
	protected GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_location);
		mContext = this;
		mLocation = this.getIntent().getStringExtra("DestinatonLocation");
		mDestinatonAddress = this.getIntent().getStringExtra("DestinatonAddress");
		TextView selectedLocationNameText = (TextView) findViewById(R.id.txt_selected_location_name);
		TextView selectedLocationAddressText = (TextView) findViewById(R.id.txt_selected_location_address);
		selectedLocationNameText.setText(mLocation);
		selectedLocationAddressText.setText(mDestinatonAddress);
		mLatlong = new LatLng(Double.parseDouble(this.getIntent().getStringExtra("DestinatonLatitude")),
				Double.parseDouble(this.getIntent().getStringExtra("DestinatonLongitude")));

		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_show_location);

		fragment.getMapAsync(this);
		Toolbar toolbar = (Toolbar) findViewById(R.id.show_location_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
			getSupportActionBar().setTitle(mLocation);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		//unregisterReceiver(locationReceiver);
		super.onPause();
	}

	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;
		mMap.getUiSettings().setMyLocationButtonEnabled(false);
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mMap.setMyLocationEnabled(false);
		mMap.getUiSettings().setMapToolbarEnabled(false);
		//mMap.setPadding(0, AppUtility.dpToPx(64, mContext), 0, 0);
		MarkerHelper.drawPinMarker(mMap, mLatlong, mContext);
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(mLatlong).zoom(Constants.ZOOM_VALUE).bearing(0).tilt(90).build();
		mMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}
}


