package com.redtop.engaze.service;

import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EventHelper;
import com.redtop.engaze.utils.EventManager;
import com.redtop.engaze.utils.EventNotificationManager;
import com.redtop.engaze.utils.InternalCaching;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class EventTrackerAlarmReceiverService extends BroadcastReceiver
{	private static final String TAG = "EventTrackerAlarmReceiverService";
	@Override
	public void onReceive(Context context, Intent intent)
	{		
		final String eventId   = intent.getStringExtra("EventId");

		switch(intent.getStringExtra("AlarmType"))
		{
		case Constants.EVENT_START:	
			EventManager.startEvent(context, eventId);
			break;
		case Constants.EVENT_OVER:
			EventManager.eventOver(context, eventId);
			Intent eventRemoved = new Intent(Constants.EVENT_OVER);
			eventRemoved.putExtra("eventId", eventId);						
			LocalBroadcastManager.getInstance(context).sendBroadcast(eventRemoved);

			break;
		case Constants.EVENT_REMINDER:	
			EventDetail eventDetailData = InternalCaching.getEventFromCache(eventId, context);
			if(eventDetailData!=null){
				String reminderType = intent.getStringExtra("ReminderType");
				if(reminderType.equals("alarm")){				
					EventNotificationManager.ringAlarm(context);				
				}
				else if(reminderType.equals("notification")){
					EventNotificationManager.showReminderNotification(context, eventDetailData);
				}
			}
			break;
		case Constants.TRACKING_STARTED:
			EventManager.eventTrackingStart(context, eventId);
			Intent trackingStarted = new Intent(Constants.TRACKING_STARTED);
			trackingStarted.putExtra("eventId", eventId);
			LocalBroadcastManager.getInstance(context).sendBroadcast(trackingStarted);
			break;
		case Constants.CHECK_LOCATION_SERVICE:
			 Log.i(TAG, "Alarm received to check location service");
			EventTrackerLocationService.peroformSartStop(context.getApplicationContext());
			EventHelper.setLocationServiceCheckAlarm(context);
			break;
		default :
			break;
		}
	}
}