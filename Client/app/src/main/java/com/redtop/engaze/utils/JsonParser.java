package com.redtop.engaze.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.EventMember;
import com.redtop.engaze.entity.UsersLocationDetail;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;

public class JsonParser {
	private List<EventDetail> eventDetailList = new ArrayList<EventDetail>();


	public ArrayList<EventMember>parseMemberList(Context context, JSONArray jsonStr){
		EventMember mem = null;
		ArrayList<EventMember> list = new ArrayList<EventMember>();
		try {
			for (int i = 0; i < jsonStr.length(); i++) {
				JSONObject c = jsonStr.getJSONObject(i);

				mem = new EventMember(								
						AppUtility.checkNull(c.getString("UserId")), 
						AppUtility.checkNull(c.getString("ProfileName")),
						AppUtility.checkNull(c.getString("MobileNumber")),
						AcceptanceStatus.getStatus(c.getInt("EventAcceptanceStateId"))
						);
				ContactOrGroup cg = ContactAndGroupListManager.getContact(context, c.getString("UserId"));
				if(cg!=null){
					mem.setProfileName(cg.getName());
					mem.setContact(cg);
				}
				else{
					mem.setProfileName("~" + mem.getProfileName());
				}
				mem.isUserLocationShared = c.getBoolean("IsTrackingAccepted");
				list.add(mem);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public List<EventDetail> parseEventDetailList(JSONArray jsonStr, Context context) {
		JSONArray eventDetailJsonArray = jsonStr;
		String loginUser = AppUtility.getPref(Constants.LOGIN_ID, context);
		try {
			for (int i = 0; i < eventDetailJsonArray.length(); i++) {
				JSONObject c = eventDetailJsonArray.getJSONObject(i);	
				EventDetail dt = new EventDetail(	
						parseMemberList(context, c.getJSONArray("UserList")),
						AppUtility.checkNull(c.getString("EventId")), 
						AppUtility.checkNull(c.getString("Name")), 
						AppUtility.checkNull(c.getString("EventTypeId")),
						AppUtility.checkNull(c.getString("Description")),
						AppUtility.checkNull(DateUtil.convertUtcToLocalDateTime(c.getString("StartTime"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))), 
						AppUtility.checkNull(DateUtil.convertUtcToLocalDateTime(c.getString("EndTime"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))),
						AppUtility.checkNull(c.getString("Duration")),
						AppUtility.checkNull(c.getString("InitiatorId")),
						AppUtility.checkNull(c.getString("InitiatorName")),
						AppUtility.checkNull(c.getString("EventStateId")), 
						AppUtility.checkNull(c.getString("TrackingStateId")),
						c.getString("DestinationLatitude"), 
						c.getString("DestinationLongitude"),	
						c.getString("DestinationName"), 
						c.getString("DestinationAddress"), 
						c.getString("IsTrackingRequired"),
						c.getString("ReminderOffset"),
						//AppUtility.checkNull(c.getString("IsTrackingRequired")),
						//AppUtility.checkNull(c.getString("ReminderOffset")),
						//c.getString("ReminderType"),
						"notification",
						c.getString("TrackingStartOffset"),
						c.getString("IsQuickEvent"));
				dt.setCurrentMember(dt.getMember(loginUser));				
				dt.setIsRecurrence(c.getString("IsRecurring"));
				if(c.getString("IsRecurring").equals("true")){
					dt.setNumberOfOccurencesLeft(c.getString("RecurrenceRemaining"));
					dt.setNumberOfOccurences(c.getString("RecurrenceCount"));
					dt.setFrequencyOfOcuurence(c.getString("RecurrenceFrequency"));
					dt.setRecurrenceType(c.getString("RecurrenceFrequencyTypeId"));

					if(c.getString("RecurrenceFrequencyTypeId").equals("2")){

						ArrayList<String>strRecurrencedays = new ArrayList<String>(Arrays.asList(c.getString("RecurrenceDaysOfWeek")
								.split(",")));							  
						ArrayList<Integer>recurrencedays = new ArrayList<Integer>();
						for (String strDay : strRecurrencedays){
							recurrencedays.add(Integer.parseInt(strDay));
						}
						dt.setRecurrenceDays(recurrencedays);
					}
					dt.setRecurrenceActualStartTime(DateUtil.convertUtcToLocalDateTime(c.getString("StartTime"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))); 
				}

				eventDetailList.add(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventDetailList;
	}

	public List<UsersLocationDetail> parseUserLocation(JSONArray jsonStr) {
		JSONArray eventDetailJsonArray = jsonStr;
		List<UsersLocationDetail> usersLocationDetailList = new ArrayList<UsersLocationDetail>();
		try {
			for (int i = 0; i < eventDetailJsonArray.length(); i++) {
				JSONObject c = eventDetailJsonArray.getJSONObject(i);
				usersLocationDetailList.add(new UsersLocationDetail(AppUtility
						.checkNull(c.getString("UserId")), AppUtility
						.checkNull(c.getString("Latitude")), AppUtility
						.checkNull(c.getString("Longitude")), AppUtility
						.checkNull(c.getString("IsDeleted")), AppUtility
						.checkNull(c.getString("CreatedOn")), AppUtility
						.checkNull(c.getString("ETA")), AppUtility.checkNull(c
								.getString("ArrivalStatus")),AppUtility.checkNull(c.getString("ProfileName"))));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usersLocationDetailList;
	}

	public void updateUserListWithLocation(JSONArray jsonStr, List<UsersLocationDetail>userLocationList, LocationHelper lh, LatLng destinationLatLang){		
		Location destLoc = null;
		if(destinationLatLang!=null){
			destLoc = new Location("");
			destLoc.setLatitude(destinationLatLang.latitude);//your coords of course
			destLoc.setLongitude(destinationLatLang.longitude);			
		}

		JSONArray eventDetailJsonArray = jsonStr;
		try {
			for (int i = 0; i < eventDetailJsonArray.length(); i++) {
				JSONObject c = eventDetailJsonArray.getJSONObject(i);
				for (UsersLocationDetail ud :  userLocationList){
					if(ud.getUserId().equalsIgnoreCase(c.getString("UserId"))){
						ud.setLatitude(c.getString("Latitude"));
						ud.setLongitude(c.getString("Longitude"));
						ud.setCreatedOn(DateUtil.convertUtcToLocalDateTime(c.getString("CreatedOn"),new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")));
						ud.setEta(c.getString("ETA"));
						ud.setArrivalStatus(c.getString("ArrivalStatus"));
						if(c.has("LocationAddress") && c.getString("LocationAddress")!=null){
							ud.setCurrentAddress(c.getString("LocationAddress"));
							ud.setCurrentDisplayAddress(buildCurrentDisplayAddress(c.getString("LocationAddress")));
							if(destLoc!=null){
								Location loc = new Location("");//provider name is unecessary
								loc.setLatitude(Double.parseDouble(c.getString("Latitude")));//your coords of course
								loc.setLongitude(Double.parseDouble(c.getString("Longitude")));
								if(loc.distanceTo(destLoc)<= Constants.DESTINATION_RADIUS){
									ud.setCurrentAddress("at destination");
									ud.setCurrentAddress("at destination");
									ud.setCurrentDisplayAddress("at destination");
								}							
							}
						}

						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String buildCurrentDisplayAddress(String currentAddress){

		String currentDisplayAddress ="";
		if(currentAddress ==null || currentAddress.equals("")){
			return currentDisplayAddress;
		}

		String[] arrAddress = currentAddress.split(",");

		if(arrAddress.length > 1){

			List<String> addressLines = new ArrayList<String>(Arrays.asList(arrAddress));
			addressLines.remove(0);
			StringBuilder builder = new StringBuilder();
			builder.append(addressLines.get(0));
			addressLines.remove(0);
			for(String addressLine : addressLines) {
				builder.append(", " + addressLine);
			}
			currentDisplayAddress =  builder.toString();			
		}
		else{
			currentDisplayAddress =  currentAddress;
		}

		return currentDisplayAddress;
	}
}
