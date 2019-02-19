package com.redtop.engaze.utils;

import com.redtop.engaze.service.EventTrackerLocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NetworkUpdateReceiver extends BroadcastReceiver{

	public static final String TAG = NetworkUpdateReceiver.class.getName();
    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {
    	Boolean isNetAvail = AppUtility.isNetworkAvailable(context);
		AppUtility.setPref("NetworkStaus", isNetAvail.toString(), context);
		Intent networkStatusUpdate = new Intent(Constants.NETWORK_STATUS_UPDATE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStatusUpdate);
        Boolean internetStatus = AppUtility.isNetworkAvailable(context);
		
		if(internetStatus){
			Log.v(TAG, "Performing start/stop operation of Location service as network is back");
			EventTrackerLocationService.peroformSartStop(context);
		}
		else{
			Log.v(TAG, "Stopping Location service as network is not available back");
			EventTrackerLocationService.peroformStop(context);
		}

    }    
}