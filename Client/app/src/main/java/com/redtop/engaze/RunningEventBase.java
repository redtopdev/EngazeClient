package com.redtop.engaze;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.redtop.engaze.adapter.EventDetailsOnMapAdapter;
import com.redtop.engaze.adapter.EventUserLocationAdapter;
import com.redtop.engaze.adapter.NameImageAdapter;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.entity.UsersLocationDetail;
import com.redtop.engaze.fragment.SnoozeOffsetFragment;
import com.redtop.engaze.localbroadcastmanager.RunningEventBroadcastManager;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Comparer;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.ContactAndGroupListManager;
import com.redtop.engaze.utils.DateUtil;
import com.redtop.engaze.utils.FBShareHelper;
import com.redtop.engaze.utils.GoogleDirection;
import com.redtop.engaze.utils.InternalCaching;
import com.redtop.engaze.viewmanager.RunningEventViewManager;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat" })
public class RunningEventBase  extends BaseLocationActivity  {
	protected boolean mIsInfoWindowOpen = false;
	public Boolean canRefreshUserLocation=true;
	protected RunningEventBroadcastManager mRunningEventBroadcastManager = null;
	public RunningEventViewManager viewManager = null;
	static LatLng currentLocation = new LatLng(0, 0);
	protected static final String TAG = RunningEventBase.class.getName();
	protected ArrayList<UsersLocationDetail> mUsersLocationDetailList;
	protected List<UsersLocationDetail> mRunningEventDetailList;
	protected String mEventId;
	protected int mEventTypeId;
	public EventDetail mEvent;
	protected ArrayList<Marker> mMarkers;
	protected ArrayList<Marker> mETADistanceMarkers;
	protected LatLngBounds mBounds;
	protected static final int SNOOZING_REQUEST_CODE = 1;
	protected static final int RUNNING_EVENT_MENU_CODE = 2;	
	protected static final int ADDREMOVE_INVITEES_REQUEST_CODE =3;
	protected static final int UPDATE_LOCATION_REQUEST_CODE = 4;
	protected EventDetailsOnMapAdapter mEventDetailAdapter;
	protected NameImageAdapter mUserLocationItemMenuAdapter;
	protected Boolean mEnableAutoCameraAdjust = true;	
	protected int mLocationRefreshTime = Constants.LOCATION_REFRESH_INTERVAL_FAST;

	protected HashMap<Marker, UsersLocationDetail> markerUserLocation = new HashMap<Marker, UsersLocationDetail>();

	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	

	protected Comparer mComparer;
	protected Marker mDestinationMarker;
	protected EventUserLocationAdapter mUserLocationDetailAdapter;	
	protected LatLng mDestinationlatlang =null;
	protected Marker mCurrentMarker;
	
	protected UsersLocationDetail currentUld;
	protected Polyline mPreviousPolyline = null;

	protected GoogleDirection mGd;	

	protected Boolean mIsTrafficOn = true;

	protected Boolean mIsETAOn = true;
	protected static Boolean isActivityRunning;

	protected String mUserId;
	protected String mEventStartTimeForUI;
	protected Duration mSnooze;
	protected int snoozeFlag = 0;

	protected Boolean mAutoCameraMoved = true;

	protected boolean shouldExecuteOnResume;
	protected ArrayList<ContactOrGroup> mContactsAndgroups;
	protected EventPlace mDestinationPlace;
	protected Handler locationhandler ;
	protected Runnable locationRunnable;
	protected FBShareHelper fbHelper;
	public Boolean mIsActivityPauseForDialog = false;
	public Boolean mShowRouteLoadedView;
	public UsersLocationDetail mRouteStartUd;
	public UsersLocationDetail mRouteEndUd;
	protected View mClickedUserLocationView;
	protected int mNormalMapTopPadding = 8 ;
	protected int mMarkerCenterMapTopPadding = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}

	protected void initialize(Bundle savedInstanceState){			
		mComparer = new Comparer();
		fbHelper = new FBShareHelper(this);
		fbHelper.initializeFacebookInstance();
		mRunningEventBroadcastManager = new RunningEventBroadcastManager(mContext);
		mEventId = this.getIntent().getStringExtra("EventId");
		mEventTypeId = this.getIntent().getIntExtra("EventTypeId", 0);
		mEvent = InternalCaching.getEventFromCache(mEventId, mContext);
		if(mEvent!=null){
			String eventTitle;
			if(AppUtility.IsEventTrackBuddyEventForCurrentuser(mEvent, mContext)){
				eventTitle =  mContext.getResources().getString(R.string.title_running_event_track_buddies);
			}
			else{
				eventTitle = mEvent.getName();
			}
			viewManager = new RunningEventViewManager(mContext, savedInstanceState, eventTitle);
			mUserId =  AppUtility.getPref(Constants.LOGIN_ID, mContext);
			mGd = new GoogleDirection(mContext);		
			initializeEventStartTimeForUI();
			ContactAndGroupListManager.assignContactsToEventMembers(mEvent.getMembers(), mContext);
		}
	}


	private void initializeEventStartTimeForUI(){	

		Calendar calendar = Calendar.getInstance();
		try {						
			Date startParsedDate =  sdf.parse(mEvent.getStartTime());			
			calendar.setTime(startParsedDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		mEventStartTimeForUI = DateUtil.getTime(calendar);		
	}	

	public void gotoPreviousPage(){		
		super.onBackPressed();					
		finish();		
	}

	public void actBasedOnTimeLeft(){
		if(mEvent==null){
			return;
		}
		switch (getTimeLeft()){
		case "5 MINS" :
			if(snoozeFlag != 1 && AppUtility.isCurrentUserInitiator(mEvent.getInitiatorId(), mContext)){
				snoozeFlag  = 1;
				FragmentManager fm = ((BaseActivity)mContext).getSupportFragmentManager();
				SnoozeOffsetFragment dialogFragment = new SnoozeOffsetFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("FromHomeLayout", false);
				dialogFragment.setArguments(bundle);
				dialogFragment.show(fm, "snooze fragment");
			}
			break;		
		}		
	}

	public String getTimeLeft(){		
		Calendar eventEnd = Calendar.getInstance();
		try {
			eventEnd.setTime(sdf.parse(mEvent.getEndTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long diffMinutes  = 1+ (eventEnd.getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/60000;
		return DateUtil.getDurationText(diffMinutes);
	}	

	protected void arrangeListinAvailabilityOrder() {
		if(mEventTypeId < 100){
			Collections.sort(mUsersLocationDetailList, mComparer);
		}
	}

	protected void runningEventAlertDialog(String title, String message, final Boolean dismissFlag){
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(dismissFlag){
					alertDialog.setCanceledOnTouchOutside(true);
					dialog.dismiss();
				}else{
					gotoPreviousPage();
				}
			}
		});		
		alertDialog.show();
	}
}
