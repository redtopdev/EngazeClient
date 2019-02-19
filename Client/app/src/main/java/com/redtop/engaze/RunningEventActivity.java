package com.redtop.engaze;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EventManager;
import com.redtop.engaze.utils.InternalCaching;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat" })
public class RunningEventActivity extends RunningEventActions implements OnMapReadyCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		mContext = this;					
		setContentView(R.layout.activity_running_event);
		this.initialize(savedInstanceState);
	}

	@Override
	protected void initialize(Bundle savedInstanceState){
		super.initialize(savedInstanceState);
		shouldExecuteOnResume = false;
		showProgressBar(getResources().getString(R.string.message_general_progressDialog));		

		if(mEvent==null){
			Toast.makeText(mContext, 
					mContext.getResources().getString(R.string.message_general_event_null_error), 
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		turnOnOfInternetAvailabilityMessage(mContext);	
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		fragment.getMapAsync(this);
	}

	private boolean isEventPast() {
		SimpleDateFormat  originalformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Date endDate  = originalformat.parse(mEvent.getEndTime());
			if(endDate.getTime()- Calendar.getInstance().getTimeInMillis() <0){
				return true;
			}
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onBackPressed() {

		if(!viewManager.isRecenterButtonHidden()){
			viewManager.clickRecenterButton();	
		}
		else{			
			canRefreshUserLocation = true;
			super.onBackPressed();
		}

		return;
	}

	@Override
	protected void onResume() {
		if(!mIsActivityPauseForDialog){
			isActivityRunning = true;
			if(mEvent==null){
				runningEventAlertDialog("Event Over","This event is already over !!", false);
			}
			else
			{
				LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mRunningEventBroadcastManager);
				LocalBroadcastManager.getInstance(mContext).registerReceiver(mRunningEventBroadcastManager,
						mRunningEventBroadcastManager.mFilter);
				canRefreshUserLocation = true;
				if(shouldExecuteOnResume && !((mUsersLocationDetailList==null || mUsersLocationDetailList.size()==0)) ){	
					mEvent = InternalCaching.getEventFromCache(mEventId, mContext);
					super.updateRecyclerViews();	
					super.locationhandler.post(super.locationRunnable);
				}
				else{
					shouldExecuteOnResume = true;
				}

				if(isEventPast()){					
					EventManager.eventOver(mContext, mEventId);
					Intent eventRemoved = new Intent(Constants.EVENT_OVER);
					eventRemoved.putExtra("eventId", mEventId);						
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(eventRemoved);
				}
			}
		}
		else{
			mIsActivityPauseForDialog = false;
		}

		super.onResume();				
	}

	@Override
	protected void onPause() {
		if(!mIsActivityPauseForDialog){
			isActivityRunning = false;
			mEvent = InternalCaching.getEventFromCache(mEventId, mContext);
			locationhandler.removeCallbacks(locationRunnable);
			EventManager.saveUsersLocationDetailList(mContext,mEvent,mUsersLocationDetailList);
			LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mRunningEventBroadcastManager);
			LocalBroadcastManager.getInstance(mContext).registerReceiver(mRunningEventBroadcastManager,
					mRunningEventBroadcastManager.mFilterEventNotExist);
			if(mMyCoordinates !=null){			
				AppUtility.setPrefLong("lat", Double.doubleToLongBits(mMyCoordinates.latitude), mContext);
				AppUtility.setPrefLong("long", Double.doubleToLongBits(mMyCoordinates.longitude), mContext);
			}
			//hideProgressBar();
		}
		super.onPause();		
	}	

	@Override
	public void onMapReady(GoogleMap map) {	
		mMap = map;
		mMap.setPadding(0,AppUtility.dpToPx(mNormalMapTopPadding, mContext),0,20);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowCloseListener(new OnInfoWindowCloseListener() {

			@Override
			public void onInfoWindowClose(Marker arg0) {
				viewManager.hideGoogleNavigatioButton();			
			}
		});		

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				if(!mAutoCameraMoved){
					mEnableAutoCameraAdjust = false;
					if(mMarkers!=null && mMarkers.size() >0 ){
						viewManager.showReCenterButton();
					}
				}
				else
				{
					mAutoCameraMoved = false;
				}
			}
		});			

		mMap.setPadding(0, AppUtility.dpToPx(64, mContext), 0, 0);
		mMap.getUiSettings().setMapToolbarEnabled(false);		
		mMap.setMyLocationEnabled(false);		
		mMap.getUiSettings().setMyLocationButtonEnabled(true);	
		mMap.getUiSettings().setRotateGesturesEnabled(true);
		mMap.getUiSettings().setScrollGesturesEnabled(true);
		mMap.getUiSettings().setTiltGesturesEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.setTrafficEnabled(true);
		mUsersLocationDetailList = mEvent.getUsersLocationDetailList();

		if (mUsersLocationDetailList==null || mUsersLocationDetailList.size()==0){			
			super.createUserLocationList();			
		}
		else{
			super.updateUserLocationList();
		}

		super.BindLocationListToAdapter();
		super.createDestinationMarker();
		super.refreshRunningEvent();
		super.locationhandler.post(super.locationRunnable);
		hideProgressBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mIsActivityPauseForDialog = true;
		switch(item.getItemId()){
		case R.id.action_end:
			super.onEventEndClicked();
			break;

		case R.id.action_extend:
			super.onEventExtendedClicked();
			break;

		case R.id.action_edit_participants:
			super.onEditParticipantsClicked();
			break;

		case R.id.action_change_destination:
			super.onChangeEventDestinationClicked();				
			break;			

		case R.id.action_leave:
			super.onLeaveEventClicked();			

			break;
		case R.id.action_poke_all:
			super.onPokeAllParticipantsClicked();
			break;

		case R.id.action_share:
			super.onShareOnFaceBookClicked();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu
		if(mEvent!=null){
			if (AppUtility.isCurrentUserInitiator(mEvent.getInitiatorId(), mContext)){
				getMenuInflater().inflate(R.menu.menu_running_event_initiator, menu);			
				if((mEvent.getMembersbyStatus(AcceptanceStatus.getStatus(1))).size() > 1){			
					menu.removeItem(R.id.action_poke_all);			
				}
			}
			else{
				getMenuInflater().inflate(R.menu.menu_running_event_participant, menu);
			}
		}
		return true;
	}	
}
