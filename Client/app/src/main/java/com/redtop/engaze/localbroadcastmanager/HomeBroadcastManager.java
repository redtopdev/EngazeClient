package com.redtop.engaze.localbroadcastmanager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.redtop.engaze.HomeActivity;
import com.redtop.engaze.utils.Constants;

public class HomeBroadcastManager  extends LocalBroadcastManager{

	public HomeActivity activity;

	public HomeBroadcastManager(Context context) {
		super(context);
		activity = (HomeActivity)mContext;		
		initializeFilter();
	}

	private void initializeFilter() {
		mFilter = new IntentFilter();
		mFilter.addAction(Constants.TRACKING_STARTED);
		mFilter.addAction(Constants.EVENT_OVER);
		mFilter.addAction(Constants.EVENT_ENDED);
		mFilter.addAction(Constants.EVENT_ENDED_BY_INITIATOR);	
		mFilter.addAction(Constants.REMOVED_FROM_EVENT_BY_INITIATOR);	
		mFilter.addAction(Constants.EVENT_LEFT);
		mFilter.addAction(Constants.EVENT_RECEIVED);		
		mFilter.addAction(Constants.REMOVED_FROM_EVENT_BY_INITIATOR);
		mFilter.addAction(Constants.EVENT_USER_RESPONSE);
		mFilter.addAction(Constants.EVENTS_REFRESHED);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		switch (intent.getAction()) {
		case Constants.EVENT_USER_RESPONSE:
			activity.updateShareMyLocationListButtonView();
			activity.updateRefreshTrackBuddyListButtonView();
			activity.updateRunningEventListButtonView();
			activity.updatePendingEventListButtonView();
			break;
		
		case Constants.TRACKING_STARTED:
			activity.updateRunningEventListButtonView();
			break;
		case Constants.EVENT_OVER:
			activity.updateShareMyLocationListButtonView();
			activity.updateRefreshTrackBuddyListButtonView();
			activity.updateRunningEventListButtonView();
			activity.updatePendingEventListButtonView();
			break;
		case Constants.EVENT_ENDED:	
			activity.updateRunningEventListButtonView();
			activity.updateShareMyLocationListButtonView();
			activity.updateRefreshTrackBuddyListButtonView();
			break;
		case Constants.EVENT_ENDED_BY_INITIATOR:
			activity.updateShareMyLocationListButtonView();
			activity.updateRefreshTrackBuddyListButtonView();
			activity.updateRunningEventListButtonView();
			activity.updatePendingEventListButtonView();
			break;
		case Constants.REMOVED_FROM_EVENT_BY_INITIATOR:
			activity.updateShareMyLocationListButtonView();
			activity.updateRefreshTrackBuddyListButtonView();
			activity.updateRunningEventListButtonView();
			activity.updatePendingEventListButtonView();
			break;
		case Constants.EVENTS_REFRESHED:
			activity.updateShareMyLocationListButtonView();
			activity.updateRefreshTrackBuddyListButtonView();
			activity.updateRunningEventListButtonView();
			activity.updatePendingEventListButtonView();
			break;	
		case Constants.EVENT_RECEIVED:
			activity.updatePendingEventListButtonView();
			activity.updateRunningEventListButtonView();
			
			break;
			

		default:
			break;
		}		
	}
	public IntentFilter getFilter(){
		return mFilter;
	}



}
