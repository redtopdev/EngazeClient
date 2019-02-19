package com.redtop.engaze.utils;
//package com.redtop.engaze.utils;
//
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResult;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.redtop.engaze.BaseActivity;
//import com.redtop.engaze.LocationActivity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentSender.SendIntentException;
//import android.location.LocationManager;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//public class GPSHelper {
//
//	protected void checkAndEnableGPS() {
//
//		LocationRequest locReqHighPriority =  LocationRequest.create();
//		locReqHighPriority.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);		
//
//		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//		.addLocationRequest(locReqHighPriority).setAlwaysShow(true);
//
//
//		PendingResult<LocationSettingsResult> result =
//				LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//
//		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//			@Override
//			public void onResult(LocationSettingsResult result) {
//				final Status status = result.getStatus();		       
//				switch (status.getStatusCode()) {
//				case LocationSettingsStatusCodes.SUCCESS:
//					doActionOnGPSOn();
//					break;
//				case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//					// Location settings are not satisfied. But could be fixed by showing the user
//					// a dialog.
//					try {
//						// Show the dialog by calling startResolutionForResult(),
//						// and check the result in onActivityResult().
//						status.startResolutionForResult(
//								(BaseActivity)mContext,
//								CHECK_SETTINGS_REQUEST_CODE);
//					} catch (SendIntentException e) {
//						// Ignore the error.
//					}
//					break;
//				case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//
//					break;
//				}
//			}
//		});
//	}
//	
//	public void checkGpsAndBringPinToMyLocation() {
//		if(!mInternetStatus){
//			return;
//		}					
//
//		LocationManager manager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE );
//		if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
//			bringPinToMyLocation();
//		}
//		else{
//			((LocationActivity)mContext).needLocation = true;
//			checkAndEnableGPS();					
//		}		
//	}
//	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case CHECK_SETTINGS_REQUEST_CODE:
//			switch (resultCode) {
//			case Activity.RESULT_OK:
//				doActionOnGPSOnThroughMyLocationButton();
//				break;
//			case Activity.RESULT_CANCELED:				
//				break;
//			default:
//				break;
//			}
//			break;
//		}
//	}	
//}
