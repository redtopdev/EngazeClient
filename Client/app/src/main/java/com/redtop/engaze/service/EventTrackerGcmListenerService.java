package com.redtop.engaze.service;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.interfaces.OnActionCompleteListner;
import com.redtop.engaze.interfaces.OnActionFailedListner;
import com.redtop.engaze.interfaces.OnRefreshEventListCompleteListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EventManager;
import com.redtop.engaze.utils.EventNotificationManager;
import com.redtop.engaze.utils.InternalCaching;
import com.redtop.engaze.utils.Constants.Action;

public class EventTrackerGcmListenerService extends GcmListenerService implements OnActionFailedListner {

	private static final String TAG = "MyGcmListenerService";
	private Context mContext;
	private String mEventId;
	// [START receive_message]
	@Override
	public void onMessageReceived(String from, final Bundle data) {
		mContext = this;		
		final String message = data.getString("Type");		
		Log.d(TAG, "From: " + from);
		Log.d(TAG, "Message: " + message);		
		mEventId = data.getString("EventId");		
		if(mEventId!=null){	
			if(message.equals("EventEnd") ||message.equals("EventDelete")||message.equals("RemovedFromEvent")){
				InternalCaching.getEventFromCache(mEventId, mContext);
				actionsBasedOnGCMMessageTypes(message, data);
			}
			else
			{
				EventManager.refreshEventList(this, new OnRefreshEventListCompleteListner() {			
					@Override
					public void RefreshEventListComplete(List<EventDetail> eventDetailList) {
						InternalCaching.getEventFromCache(mEventId, mContext);
						actionsBasedOnGCMMessageTypes(message, data);
					}
				}, this);	
			}
		}
	}

	private void actionsBasedOnGCMMessageTypes(String message, final Bundle data){
		Intent intent = null;
		switch(message)
		{
		case "EventUpdate":
			intent = new Intent(Constants.EVENT_UPDATED_BY_INITIATOR);
			intent.putExtra("eventId", mEventId);
			LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
			break;

		case "EventResponse":
		{
			final String userId = data.getString("EventResponderId");
			final String userName = data.getString("EventResponderName");
			final int eventAcceptanceStateId = Integer.parseInt(data.getString("EventAcceptanceStateId"));
			EventManager.updateEventWithParticipantResponse(mContext, mEventId, userId, userName,eventAcceptanceStateId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					Intent intent = new Intent(Constants.EVENT_USER_RESPONSE);
					intent.putExtra("eventId", mEventId);
					intent.putExtra("userId", userId);
					intent.putExtra("eventAcceptanceStateId", eventAcceptanceStateId);
					intent.putExtra("EventResponderName", userName);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

				}
			}, this);

			break;
		}

		case "EventLeave":
			final String userId = data.getString("EventResponderId");
			final String userName = data.getString("EventResponderName");
			EventManager.updateEventWithParticipantLeft(mContext, mEventId, userId, userName, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					Intent intent = new Intent(Constants.PARTICIPANT_LEFT_EVENT);
					intent.putExtra("eventId", mEventId);
					intent.putExtra("userId", userId);					
					intent.putExtra("EventResponderName", userName);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

				}
			}, this);

			break;

		case "EventInvite":	
			getEventDetail(data);

			break;

		case "RegisteredUserUpdate":
			//code against that
			break;
		case "EventEnd":
			EventManager.eventEndedByInitiator(mContext, mEventId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					Intent intent = new Intent(Constants.EVENT_ENDED_BY_INITIATOR);
					intent.putExtra("eventId", mEventId);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
				}
			}, this);
			break;

		case "EventExtend":

			EventManager.eventExtendedByInitiator(mContext, mEventId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					//LocalBroadCast
					Intent intent = new Intent(Constants.EVENT_EXTENDED_BY_INITIATOR);
					intent.putExtra("eventId", mEventId);
					intent.putExtra("com.redtop.engaze.service.ExtendEventDuration", data.getString("ExtendEventDuration"));
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

				}
			}, this);

			break;		

		case "EventDelete":

			EventManager.eventDeletedByInitiator(mContext, mEventId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					//LocalBroadCast
					Intent intent = new Intent(Constants.EVENT_DELETE_BY_INITIATOR);
					intent.putExtra("eventId", mEventId);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

				}
			}, this);					

			break;

		case "EventUpdateLocation":	

			EventManager.eventDestinationChangedByInitiator(mContext, mEventId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					//LocalBroadCast
					Intent intent = new Intent(Constants.EVENT_DESTINATION_UPDATED_BY_INITIATOR);
					intent.putExtra("com.redtop.engaze.service.UpdatedDestination", data.getString("DestinationName"));
					intent.putExtra("eventId", mEventId);					
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
				}
			}, this);	

			break;
		case "RemovedFromEvent" :
			EventManager.currentparticipantRemovedByInitiator(mContext, mEventId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					//LocalBroadCast
					Intent intent = new Intent(Constants.REMOVED_FROM_EVENT_BY_INITIATOR);
					intent.putExtra("eventId", mEventId);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
				}
			}, this);				
			break;

		case "EventUpdateParticipants" :

			EventManager.participantsUpdatedByInitiator(mContext, mEventId, new OnActionCompleteListner() {

				@Override
				public void actionComplete(Action action) {
					//LocalBroadCast
					Intent intent = new Intent(Constants.EVENT_PARTICIPANTS_UPDATED_BY_INITIATOR);	
					intent.putExtra("eventId", mEventId);
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
				}
			}, this);	

			break;

		case "RemindContact":
			EventDetail event = InternalCaching.getEventFromCache(mEventId, mContext);
			if(AppUtility.isNotifyUser(event)){
				EventNotificationManager.pokeNotification(mContext, mEventId);
			}
			break;
		}
	}

	private void getEventDetail(Bundle data)
	{
		EventManager.getEventDataFromServer(mContext, data.getString("EventId"), new OnActionCompleteListner() {

			@Override
			public void actionComplete(Action action) {
				Intent eventReceived = new Intent(Constants.EVENT_RECEIVED);				
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(eventReceived);	
			}
		}, this);		
	}	

	@Override
	public void actionFailed(String msg, Action action) {
//		try{
//			if(msg==null){
//				msg = UserMessageHandler.getFailureMessage(action, mContext);						
//			}
//			Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}		
	}
}
