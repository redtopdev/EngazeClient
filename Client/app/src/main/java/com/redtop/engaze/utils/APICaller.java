package com.redtop.engaze.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.redtop.engaze.R;
import com.redtop.engaze.app.VolleyAppController;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.interfaces.OnAPICallCompleteListner;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;

public class APICaller {

	private final static String TAG = APICaller.class.getName();

	public static void callSMSGateway(final Context context, JSONObject smsGatewayObj){
		try{
			String JsonPostURL =  Constants.MAP_API_URL + Constants.METHOD_SMS_GATEWAY;

			Log.d(TAG, "Calling URL:" + JsonPostURL);

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, smsGatewayObj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					String Status;
					try {
						Status = (String)response.getString("Status");
						if (Status == "true")
						{
							Log.d(TAG, "SMS Gateway Call Success: " + response);
//							Toast.makeText(context,
//									context.getResources().getString(R.string.message_smsGateway_success),
//									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					Toast.makeText(context,
							context.getResources().getString(R.string.message_smsGateway_error),
							Toast.LENGTH_LONG).show();
				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq); 
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();		
			Toast.makeText(context,
					context.getResources().getString(R.string.message_smsGateway_error),
					Toast.LENGTH_LONG).show();
		}

	}

	public static void CreateEvent(Context context, JSONObject mEventJobj, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {			 
		try{
			String JsonPostURL = "";
			if(mEventJobj.has("EventId")){
				JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_UPDATE_EVENT;
			}else{				
				JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_CREATE_EVENT;
			}
			Log.d(TAG, "Calling URL:" + JsonPostURL);

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, mEventJobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);						
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);
				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq); 
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);

		}
	}

	public static void endEvent(final Context context, final String eventID, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {
		try{

			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_END_EVENT;
			// making json object request
			JSONObject jobj = new JSONObject();
			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));					            		
			jobj.put("EventId", eventID);			

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);				
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);
				}
			})

			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};

			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);

		}
	}

	public static void leaveEvent(final Context context, final String eventID, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {
		try{

			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_LEAVE_EVENT;
			// making json object request
			JSONObject jobj = new JSONObject();
			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));					            		
			jobj.put("EventId", eventID);			

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);				
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);
				}
			})

			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};

			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);

		}
	}

	public static void saveUserResponse(final AcceptanceStatus userAcceptanceResponse, final Context context, final String eventid,  final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {
		try {
			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_RESPOND_INVITE;
			// making json object request
			JSONObject jobj = new JSONObject();

			jobj.put("EventId", eventid);					            		
			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));
			jobj.put("EventAcceptanceStateId", userAcceptanceResponse.getStatus());
			jobj.put("TrackingAccepted", "true");					

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);		


				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);

				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	public static void getEventDetail(Context context,  String eventid, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure)
	{

		try {
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("EventId", eventid);
				jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.MAP_API_URL+Constants.METHOD_EVENT_DETAIL, jobj, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);																	
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);
				}
			}){

				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("userid", "userid");		 
					return params;
				}

			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue			
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq); 
		}
		catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	public static void deleteEvent(final Context context, final String eventid, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {

		try {
			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_DELETE_EVENT;
			// making json object request
			JSONObject jobj = new JSONObject();

			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));					            		
			jobj.put("EventId", eventid);			

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());	
					//temp(error.getMessage(), context);
					listnerOnFailure.apiCallComplete(null);
				}
			})

			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};

			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	public static void addRemoveParticipants(JSONObject addRemoveContactsJSON, final Context mContext, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {
		try {
			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_UPDATE_PARTICIPANTS;

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, addRemoveContactsJSON, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);	}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());	
					//temp(error.getMessage(), mContext);
					listnerOnFailure.apiCallComplete(null);
				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};

			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	public static void changeDestination(final EventPlace destinationPlace, final Context context,final String eventId, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {
		try {
			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_UPDATE_DESTINATION;

			JSONObject jobj = new JSONObject();

			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));		

			if(destinationPlace!=null)
			{
				jobj.put("DestinationLatitude", destinationPlace.getLatLang().latitude);
				jobj.put("DestinationLongitude", destinationPlace.getLatLang().longitude);
				jobj.put("DestinationAddress", destinationPlace.getAddress());
				jobj.put("DestinationName", destinationPlace.getName());
				//jobj.put( "DestinationName", mEventLocationTextView.getText());
			}
			else
			{
				jobj.put("DestinationLatitude", "");
				jobj.put("DestinationLongitude", "");
				jobj.put("DestinationAddress", "");
				jobj.put("DestinationName", "");
			}

			jobj.put("EventId", eventId);			


			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);	
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());	
					//temp(error.getMessage(), mContext);
					listnerOnFailure.apiCallComplete(null);
				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};

			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	public static void extendEventEndTime(final int i, final Context context, final String eventID, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {
		try {
			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_EXTEND_EVENT;
			// making json object request
			JSONObject jobj = new JSONObject();

			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));					            		
			jobj.put("EventId", eventID);
			jobj.put("ExtendEventDuration", i);

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);				
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());	
					//temp(error.getMessage(), context);
					listnerOnFailure.apiCallComplete(null);
				}
			})

			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};

			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	public static void RefreshEventListFromServer(final Context context, final OnAPICallCompleteListner listnerOnSuccess, final OnAPICallCompleteListner listnerOnFailure) {				
		try {
			String tag_json_obj = "json_obj_get_event_detail";		
			String apiUrl = Constants.MAP_API_URL + Constants.METHOD_EVENT_DETAIL;

			JSONObject jobj = new JSONObject();								

			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					apiUrl , jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {					
					listnerOnSuccess.apiCallComplete(response);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);		
				}
			}){
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}


	public static void saveProfile(Context context, JSONObject jRequestobj,
			final OnAPICallCompleteListner listnerOnSuccess,
			final OnAPICallCompleteListner listnerOnFailure) {
		try
		{
			String apiUrl = Constants.MAP_API_URL + Constants.METHOD_ACCOUNT_REGISTER;

			Log.d(TAG, "Calling URL:" + apiUrl);

			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					apiUrl , jRequestobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);																
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);
				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_MEDIUM_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}

	}

	public static void AssignUserIdToRegisteredUser(Context context, final ArrayList<ContactOrGroup> contactsAndgroups,
			final OnAPICallCompleteListner listnerOnSuccess,
			final OnAPICallCompleteListner listnerOnFailure){
		try
		{
			JSONObject jsnobj = createContactsJSON(context,contactsAndgroups);			
			String apiUrl = Constants.MAP_API_URL + Constants.METHOD_GET_REGISTERED_CONTACTS;
			Log.d(TAG, "Calling URL:" + apiUrl);
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					apiUrl, jsnobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);	
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
					if(listnerOnFailure!=null){
						listnerOnFailure.apiCallComplete(null);
					}
				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_LONG_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq); 
		}
		catch(Exception ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}

	private static JSONObject createContactsJSON(Context context, ArrayList<ContactOrGroup> contactsAndGroups) throws JSONException{
		// making json object request
		JSONObject jobj = new JSONObject();
		JSONArray jsonarr = new JSONArray();
        for (ContactOrGroup cg : contactsAndGroups){
            for(String mobileNumber : cg.getMobileNumbers()){

                jsonarr.put(mobileNumber.replaceAll("\\s",""));
            }
        }

		// Construct the selected Users json object			
		jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));
		jobj.put("ContactList", jsonarr);

		return jobj;
	}

	public static void pokeParticipants(Context context, JSONObject pokeAllContactsJSON,
			final OnAPICallCompleteListner listnerOnSuccess,
			final OnAPICallCompleteListner listnerOnFailure) {
		try {
			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_POKEALL_CONTACTS;
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, pokeAllContactsJSON, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);		


				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);

				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}

	}

	public static void getLocationsFromServer(Context context, String userId, String eventId,
			final OnAPICallCompleteListner listnerOnSuccess,
			final OnAPICallCompleteListner listnerOnFailure)
	{
		try {

			JSONObject jobj = new JSONObject();								

			jobj.put("RequestorId", userId);
			jobj.put("EventId", eventId);

			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_USER_LOCATION;
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);		


				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);

				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}
	
	public static void getUserLocationFromServer(Context context, String userId, String eventId,
			final OnAPICallCompleteListner listnerOnSuccess,
			final OnAPICallCompleteListner listnerOnFailure)
	{
		try {
			JSONArray jsonarr = new JSONArray();
			jsonarr.put(userId);

			JSONObject jobj = new JSONObject();								

			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, context));
			jobj.put("EventId", eventId);
			jobj.put("UserIds", jsonarr);

			String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_USER_LOCATION;
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					JsonPostURL, jobj, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					listnerOnSuccess.apiCallComplete(response);		


				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(TAG, "Volley Error: " + error.getAlertMessage());
					listnerOnFailure.apiCallComplete(null);

				}
			})
			{
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}
			};
			jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// Adding request to request queue
			VolleyAppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		catch (Exception ex) {
			Log.d(TAG, ex.toString());
			ex.printStackTrace();			
			listnerOnFailure.apiCallComplete(null);
		}
	}
}

