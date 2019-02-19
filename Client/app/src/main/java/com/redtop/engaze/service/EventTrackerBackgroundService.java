package com.redtop.engaze.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.utils.ContactObserver;
import com.redtop.engaze.utils.EventHelper;
import com.redtop.engaze.utils.InternalCaching;

public class EventTrackerBackgroundService extends Service {

	private static final String TAG = "EventTrackerBackgroundService";
	private Context mContext;
	private ContactObserver mContactObserver;	

	public synchronized static void perofomrSartStop(Context context, String action){
		if(action.equals("stop")){

			context.stopService(new Intent(context, EventTrackerBackgroundService.class));
		}

		if(action.equals("start")){

			context.startService(new Intent(context, EventTrackerBackgroundService.class));

		}		
	}


	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "EventTracker Background Service stopped");	
		getContentResolver().unregisterContentObserver(mContactObserver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		
		onStart(intent, startId);
		return Service.START_STICKY;

	}

	@Override
	public void onCreate() {
		super.onCreate(); 
		mContext = this;

	}    

	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		mContext = getApplicationContext();
		//		Toast.makeText(mContext,"EventTracker Background Service Started",
		//				Toast.LENGTH_LONG).show();
//		mContactObserver =  new ContactObserver(mContext);
//		getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI ,true, 
//				mContactObserver);
		removePastEvents();

		Log.v(TAG, "Background Service started");
	}

	private void removePastEvents() {
		// TODO Auto-generated method stub
		List<EventDetail> list= InternalCaching.getEventListFromCache(this);
		if(list!=null){
			ArrayList<String> deletedEventidlist = new ArrayList<String>();
			for(EventDetail ev : list)
			{

				if(EventHelper.isEventPast(mContext, ev)){
					deletedEventidlist.add(ev.getEventId());
				}
			}
			InternalCaching.removeEventsFromCache(deletedEventidlist, this);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
