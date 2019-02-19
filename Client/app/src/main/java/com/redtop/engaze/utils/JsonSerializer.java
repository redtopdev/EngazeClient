package com.redtop.engaze.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.EventDetail;

public class JsonSerializer {	


	public static JSONObject createUpdateParticipantsJSON(Context context, ArrayList<ContactOrGroup> contactsAndgroups, String eventId){
		JSONObject userListJobj;
		JSONObject jobj = new JSONObject();
		JSONArray jsonarr = new JSONArray();

		String userId;
		try{
			if(contactsAndgroups!=null){
				for(ContactOrGroup cg : contactsAndgroups){
					userId = cg.getUserId();
					userListJobj = new JSONObject();
					if(userId != null && !userId.isEmpty()){
						userListJobj.put("UserId", userId);

					}
					else{
						userListJobj.put("MobileNumber", cg.getMobileNumbers().get(0));
					}	
					jsonarr.put(userListJobj);
				}
			}

			jobj.put("EventId", eventId);
			jobj.put("UserList", jsonarr);	
			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return jobj;
	}

	public static JSONObject createPokeAllContactsJSON( Context context, EventDetail ed){
		JSONObject jobj = new JSONObject();		

		try {
			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));
			jobj.put("EventId", ed.getEventId());
			jobj.put("RequestorName", AppUtility.getPref(Constants.LOGIN_NAME, context));
			jobj.put("EventName", ed.getName());
			jobj.put("EventId", ed.getEventId());
			//			jobj.put("ContactNumbersForRemind", conactsArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return jobj;
	}
}
