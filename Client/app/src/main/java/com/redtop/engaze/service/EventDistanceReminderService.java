package com.redtop.engaze.service;

import java.util.ArrayList;

import org.json.JSONObject;
import org.w3c.dom.Document;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.EventMember;
import com.redtop.engaze.interfaces.OnAPICallCompleteListner;
import com.redtop.engaze.utils.APICaller;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EventNotificationManager;
import com.redtop.engaze.utils.GoogleDirection;
import com.redtop.engaze.utils.InternalCaching;
import com.redtop.engaze.utils.Constants.ReminderFrom;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class EventDistanceReminderService extends IntentService implements GoogleApiClient.ConnectionCallbacks, 
GoogleApiClient.OnConnectionFailedListener, LocationListener {

	private Context mContext;
	private EventDetail mEvent;
	private EventMember mMember;
	private LatLng reminderStartLatLng;
	private LatLng reminderEndLatLng;
	private String mReminderId;		
	private Handler mDistancetCheckHandler;
	private Runnable mDistancetCheckRunnable;
	private Boolean mReceivedLocation = false;
	private GoogleApiClient mGoogleApiClient;
	private static int durationTillIntervalSetHalf = 60;//seconds
	private static int intervalPostdurationTillIntervalSetHalf = 15;

	public static final String TAG = EventDistanceReminderService.class.getName();
	private LocationRequest mLocationRequest;

	public EventDistanceReminderService() {		
		super(TAG);	
		mDistancetCheckHandler = new Handler();
		mDistancetCheckRunnable = new Runnable() {			
			@Override
			public void run() {
				if(!checkValidityOfReminder()){
					return ;
				}
				else{					
					getParticipantLocationsFromServer();
				}								
			}
		};
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			mContext = this;	
			mEvent = InternalCaching.getEventFromCache(intent.getStringExtra("EventId"), mContext) ;
			mMember = mEvent.getMember(intent.getStringExtra("MemberId"));
			mReminderId = mMember.getDistanceReminderId();
			mDistancetCheckHandler.post(mDistancetCheckRunnable);
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();
		}
	}

	private void createGoogleAPIClientAndLocationRequest(){
		Log.v(TAG, "Creating Google Api Client");
		mGoogleApiClient = 
				new GoogleApiClient.Builder(mContext)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(LocationServices.API)
		.addApi( Places.GEO_DATA_API )
		.addApi( Places.PLACE_DETECTION_API ).build();	
		mGoogleApiClient.connect();	

		Log.v(TAG, "Creating Location Request");
		mLocationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(Constants.LOCATION_REFRESH_INTERVAL_FAST)        // 10 seconds, in milliseconds
				.setFastestInterval(Constants.LOCATION_REFRESH_INTERVAL_FAST);
	}

	@Override
	public void onLocationChanged(Location location) {
		synchronized (this)
		{
			if(mReceivedLocation){
				return; 
			}
			mReceivedLocation = true;
		}		
		
		if (mGoogleApiClient.isConnected()){			
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			mGoogleApiClient.disconnect();
		}

		reminderEndLatLng = new LatLng(location.getLatitude(), location.getLongitude()); 
		getDistanceForReminderDistanceCalculation();				
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "Connection failed with code " + connectionResult.getErrorCode());
	}	

	@Override
	public void onConnected(Bundle arg0) {
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);	
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		Log.i(TAG, "Location services connected.");
		if(location!=null){
			mReceivedLocation = true;
		}
		reminderEndLatLng = new LatLng(location.getLatitude(), location.getLongitude()); 
		getDistanceForReminderDistanceCalculation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.i(TAG, "Location services suspended. Please reconnect.");
	}

	private void getParticipantLocationsFromServer(){
		APICaller.getUserLocationFromServer(mContext, mMember.getUserId(), mEvent.getEventId(), new OnAPICallCompleteListner() {

			@Override
			public void apiCallComplete(JSONObject response) {
				Log.d(TAG, response.toString());
				String Status ="";
				try {
					Status = (String)response.getString("Status");

					if (Status == "true")
					{
						JSONObject c = response.getJSONArray("ListOfUserLocation").getJSONObject(0);
						reminderStartLatLng = new LatLng( Double.parseDouble( c.getString("Latitude")), Double.parseDouble(c.getString("Longitude")));
						if(mMember.getReminderFrom()== ReminderFrom.SELF){
							createGoogleAPIClientAndLocationRequest();						
						}
						else{
							if(mMember.getReminderFrom()== ReminderFrom.DESTINATION){
								reminderEndLatLng = new LatLng( Double.parseDouble(mEvent.getDestinationLatitude()), Double.parseDouble(mEvent.getDestinationLongitude())); 
								getDistanceForReminderDistanceCalculation();
							}
							else{
								Log.v(TAG,"This option is yet not implemented");
							}
						}
					}
					else
					{	
						Log.v(TAG, "Unable to get the location of participant from server.");
						Log.v(TAG,"Service will retry after 15 seconds");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new OnAPICallCompleteListner() {

			@Override
			public void apiCallComplete(JSONObject response) {
				Log.v(TAG, "Unable to get the location of participant from server.");
				Log.v(TAG,"Service will retry after 15 seconds");

			}
		});
	}

	private void getDistanceForReminderDistanceCalculation(){
		GoogleDirection mGd = new GoogleDirection(mContext);			
		mGd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {

			@Override
			public void onResponse(String status, Document doc,
					GoogleDirection gd) {

				checkReminder(gd, doc);
			}
		});
		mGd.request(reminderStartLatLng, reminderEndLatLng, GoogleDirection.MODE_DRIVING);

	}

	private void checkReminder(GoogleDirection gd, Document doc){
		if (gd.getTotalDistanceValue(doc) <= mMember.getDistanceReminderDistance()){
			int actual = gd.getTotalDistanceValue(doc)/1000;
			String notificationMessage = "";
			if(actual > 1) {
				notificationMessage  = mMember.getProfileName() +" is just " + actual + " Kms away!" ;
			}
			else {
				notificationMessage = mMember.getProfileName() +" is just " + gd.getTotalDistanceValue(doc) + " mtrs away!" ;
			}
			EventNotificationManager.approachingAlertNotification(mContext, mEvent, notificationMessage);

			mEvent.getReminderEnabledMembers().remove(mEvent.getReminderEnabledMembers().indexOf(mMember));			
			InternalCaching.saveEventToCache(mEvent, this);
		}
		else{
			int duration = gd.getTotalDurationValue(doc);
			int postDelayTime = intervalPostdurationTillIntervalSetHalf;
			if(duration > durationTillIntervalSetHalf){
				postDelayTime = duration/2;
			}
			//mDistancetCheckHandler.postDelayed(mDistancetCheckRunnable, 10*1000);
			mDistancetCheckHandler.postDelayed(mDistancetCheckRunnable, postDelayTime*1000);			
		}
	}

	private Boolean checkValidityOfReminder(){	

		mEvent =  InternalCaching.getEventFromCache(mEvent.getEventId(), mContext);
		if(mEvent == null){
			return false;
		}
		mMember = mEvent.getMember(mMember.getUserId());
		if(mMember == null){
			return false;
		}

		ArrayList<EventMember> reminderEnabledMem = mEvent.getReminderEnabledMembers();
		if(!(reminderEnabledMem!=null && reminderEnabledMem.size()>0
				&& reminderEnabledMem.contains(mMember))){
			return false;
		}

		if(!mReminderId.equalsIgnoreCase(mMember.getDistanceReminderId())){
			return false;
		}
		String destLat = mEvent.getDestinationLatitude();
		if(mMember.getReminderFrom()== ReminderFrom.DESTINATION &&
				(destLat==null || destLat =="")){
			return false;
		}
		return true;		
	}

	//	public static List<EventDetail> getDistanceReminderEnabledEvents(Context context){
	//		List<EventDetail> events = InternalCaching.getEventListFromCache(context);
	//		List<EventDetail> reminderEvents = new ArrayList<EventDetail>(); 
	//		if(events==null){
	//			return null;
	//		}
	//
	//		for(EventDetail ed : events){
	//			if(ed.getState().equals(Constants.TRACKING_ON) && ed.isDistanceReminderSet){
	//				reminderEvents.add(ed);
	//			}
	//		}		
	//
	//		return reminderEvents;
	//	}

}

