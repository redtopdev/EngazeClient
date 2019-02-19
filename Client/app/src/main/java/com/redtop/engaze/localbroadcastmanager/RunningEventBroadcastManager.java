package com.redtop.engaze.localbroadcastmanager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.utils.Constants;

// this can be a person from contact list or can be a group which will be resolved to actual contact at server
public class RunningEventBroadcastManager  extends LocalBroadcastManager{

	public RunningEventActivity activity;
	public IntentFilter mFilterEventNotExist;

	public RunningEventBroadcastManager(Context context) {
		super(context);
		activity = (RunningEventActivity)mContext;		
		initializeFilter();
	}

	private void initializeFilter() {
		mFilter = new IntentFilter();
		mFilter.addAction(Constants.PARTICIPANT_LEFT_EVENT);		
		mFilter.addAction(Constants.EVENT_USER_RESPONSE);
		mFilter.addAction(Constants.EVENT_OVER); 
		mFilter.addAction(Constants.EVENT_ENDED_BY_INITIATOR);
		mFilter.addAction(Constants.EVENT_DESTINATION_UPDATED_BY_INITIATOR);		
		mFilter.addAction(Constants.EVENT_PARTICIPANTS_UPDATED_BY_INITIATOR);
		mFilter.addAction(Constants.EVENT_DESTINATION_UPDATED);
		mFilter.addAction(Constants.REMOVED_FROM_EVENT_BY_INITIATOR);
		mFilter.addAction(Constants.EVENT_EXTENDED_BY_INITIATOR);

		mFilterEventNotExist = new IntentFilter();
		mFilterEventNotExist.addAction(Constants.EVENT_OVER); 
		mFilterEventNotExist.addAction(Constants.EVENT_ENDED_BY_INITIATOR);
		mFilterEventNotExist.addAction(Constants.REMOVED_FROM_EVENT_BY_INITIATOR);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String receivedEventId = intent.getStringExtra("eventId");
		if(!(receivedEventId!=null && activity.mEvent!=null && receivedEventId.equals(activity.mEvent.getEventId()))){
			return;
		}
		switch (intent.getAction()){

		case Constants.PARTICIPANT_LEFT_EVENT:
			String eventResponderName  = intent.getStringExtra("EventResponderName");
			activity.onParticipantLeft(eventResponderName);
			break;
		case Constants.EVENT_USER_RESPONSE:	
			int eventAcceptanceStateId = intent.getIntExtra("eventAcceptanceStateId", -1); 
			String EventResponderName  = intent.getStringExtra("EventResponderName"); 
			activity.onUserResponse(eventAcceptanceStateId, EventResponderName);

			break;

		case Constants.EVENT_EXTENDED_BY_INITIATOR:	
			String ExtendEventDuration  = intent.getStringExtra("com.redtop.engaze.service.ExtendEventDuration");
			activity.onEventExtendedByInitiator(ExtendEventDuration);
			break;	

		case Constants.EVENT_OVER:
			activity.onEventOver();	

			break;
		case Constants.EVENT_ENDED_BY_INITIATOR:
			activity.onEventEndedByInitiator();			
			break;

		case Constants.EVENT_DESTINATION_UPDATED_BY_INITIATOR:
			String changedDestination  = intent.getStringExtra("com.redtop.engaze.service.UpdatedDestination");											
			activity.onEventDestinationUpdatedByInitiator(changedDestination);
			break;

		case Constants.REMOVED_FROM_EVENT_BY_INITIATOR:	
			activity.onUserRemovedFromEventByInitiator();

			break;
		case Constants.EVENT_PARTICIPANTS_UPDATED_BY_INITIATOR:
			activity.onEventParticipantUpdatedByInitiator();
			break;
		}				

	}
	public IntentFilter getFilter(){
		return mFilter;
	}
}
