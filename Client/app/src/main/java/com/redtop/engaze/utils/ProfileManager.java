package com.redtop.engaze.utils;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.redtop.engaze.R;
import com.redtop.engaze.interfaces.OnAPICallCompleteListner;
import com.redtop.engaze.interfaces.OnActionFailedListner;
import com.redtop.engaze.utils.Constants.Action;


public class ProfileManager {
	private final static String TAG = ProfileManager.class.getName();

	public static void saveProfile(final Context context,final JSONObject jRequestobj,
			final OnAPICallCompleteListner listnerOnSuccess, 
			final OnActionFailedListner listnerOnFailure){

		if(!AppUtility.isNetworkAvailable(context))
		{
			String message = context.getResources().getString(R.string.message_general_no_internet_responseFail);
			Log.d(TAG, message);
			listnerOnFailure.actionFailed(message, Action.SAVEPROFILE);
			return ;

		}

		APICaller.saveProfile(context, jRequestobj, new OnAPICallCompleteListner() {

			@Override
			public void apiCallComplete(JSONObject response) {
				Log.d(TAG, "EventResponse:" + response.toString());

				try {								
					String Status = (String)response.getString("Status");

					if (Status == "true")
					{
						String loginID = (String)response.getString("Id");
						// save the loginid to preferences  
						AppUtility.setPref(Constants.LOGIN_ID, loginID, context);	
						AppUtility.setPref(Constants.LOGIN_NAME, jRequestobj.getString("ProfileName"), context);
						listnerOnSuccess.apiCallComplete(response);
					}
					else{
						listnerOnFailure.actionFailed(null, Action.SAVEPROFILE);						
					}

				} catch (Exception ex) {
					Log.d(TAG, ex.toString());
					ex.printStackTrace();
					listnerOnFailure.actionFailed(null, Action.SAVEPROFILE);
				}		

			}
		}, new OnAPICallCompleteListner() {

			@Override
			public void apiCallComplete(JSONObject response) {
				listnerOnFailure.actionFailed(null, Action.SAVEPROFILE);				
			}
		});
	}

}


