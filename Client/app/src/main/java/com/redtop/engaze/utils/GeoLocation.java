package com.redtop.engaze.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.redtop.engaze.interfaces.OnGetCurrentLocationCompleteListner;

public class GeoLocation implements OnConnectionFailedListener, ConnectionCallbacks {


	private static final String TAG = GeoLocation.class.getName();


//	public static void getAddressFromLocation(final Double latitude,final Double longitude,
//			final Context context, final Handler handler) {
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//				String result = null;
//				Log.d(TAG, "latitude : "+latitude);
//				Log.d(TAG, "longitude : "+longitude);
//				try {
//					List addressList = geocoder.getFromLocation(latitude, longitude, 1);
//					if (addressList != null && addressList.size() > 0) {
//						Address address = (Address) addressList.get(0);
//						StringBuilder sb = new StringBuilder();
//						sb.append(address.getAddressLine(0)).append(",");
//						sb.append(address.getAdminArea()).append(",");
//						sb.append(address.getCountryCode()).append(",");
//						sb.append(address.getCountryName()).append(",");
//						sb.append(address.getLocality()).append(",");
//						sb.append(address.getPostalCode()).append(",");
//						sb.append(address.getSubAdminArea()).append(",");
//						sb.append(address.getSubLocality());
//						result = sb.toString();
//					}
//				} catch (IOException e) {
//					Log.e(TAG, "Unable to connect to Geocoder", e);
//					e.printStackTrace();
//				} finally {
//					Message message = Message.obtain();
//					message.setTarget(handler);
//					if (result != null) {
//						message.what = 1;
//						Bundle bundle = new Bundle();
//
//						bundle.putString("address", result);
//						message.setData(bundle);
//					} else {
//						message.what = 1;
//						Bundle bundle = new Bundle();
//						result = "Address: "
//								+ "\n Unable to get Latitude and Longitude for this address location.";
//						bundle.putString("address", result);
//						message.setData(bundle);
//					}
//					message.sendToTarget();
//				}
//			}
//		};
//		thread.start();
//	}
	
	public static String getCurrentPlace(FragmentActivity activity, final OnGetCurrentLocationCompleteListner listner) {
		final String content ="";
		try
		{
			GeoLocation gl = new GeoLocation();
			GoogleApiClient googleApiClient = new GoogleApiClient
					.Builder( activity )
			.enableAutoManage(activity, 0, gl )
			.addApi( Places.GEO_DATA_API )
			.addApi( Places.PLACE_DETECTION_API )
			.addConnectionCallbacks( gl )
			.addOnConnectionFailedListener( gl )
			.build();
			PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace( googleApiClient, null );
			result.setResultCallback( new ResultCallback<PlaceLikelihoodBuffer>() {
				@Override
				public void onResult( PlaceLikelihoodBuffer likelyPlaces ) {
					PlaceLikelihood currentPlaceLikelihood = null;
					if(null != likelyPlaces) {
						for (PlaceLikelihood placeLikelihood : likelyPlaces) {
							if(currentPlaceLikelihood == null)
							{
								currentPlaceLikelihood = placeLikelihood;
							}
							else
							{
								if (placeLikelihood.getLikelihood()> currentPlaceLikelihood.getLikelihood())
								{
									currentPlaceLikelihood = placeLikelihood;
								}
							}
						}


						if( currentPlaceLikelihood != null && currentPlaceLikelihood.getPlace() != null && !TextUtils.isEmpty( currentPlaceLikelihood.getPlace().getName() ) )
						{
							listner.OnGetCurrentLocationComplete(currentPlaceLikelihood.getPlace());	
						}
						likelyPlaces.release();
					}
				}
			});
		}
		catch(Exception e)
		{
			Log.d( TAG, "GooglePlayServicesNotAvailableException thrown"  + e.toString());
		}

		return content;
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(TAG, "Connection error : "+ arg0.toString());

	}
}
