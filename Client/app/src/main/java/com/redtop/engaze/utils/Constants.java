package com.redtop.engaze.utils;

public class Constants {	
	public static boolean DEBUG = false;
	public static final int MAX_DESTINATION_CACHE_COUNT = 5;
	public static final int MAX_SMS_MESSAGE_LENGTH = 20;
	public static final int VALID_MOBILE_NUMBER = 21;
	public static final int INVALID_MOBILE_NUMBER = 22;
	public static final int OPERATION_TIME_OUT = 23;
	public static final int ERROR_VERIFY_MOBILE_NUMBER = 24;
	public static final String SMS_PORT = "8901";
	public static final int DEFAULT_SHORT_TIME_TIMEOUT = 20000;//millisecond
	public static final int DEFAULT_MEDIUM_TIME_TIMEOUT = 40000;//millisecond
	public static final int DEFAULT_LONG_TIME_TIMEOUT = 120000;//millisecond
	public static final int LOCATION_RETRIVAL_INTERVAL = 25000;//millisecond
	public static final int EVENTS_REFRESH_INTERVAL = 60000;
	public static final int RUNNING_EVENT_CHECK_INTERVAL = 120000;

	public static final int SMS_TIMEOUT_PERIOD = 120000;
	public static final int SMS_INTERVAL_MILISECOND = 1000;
	public static final int LOCATION_REFRESH_INTERVAL_FAST= 15000;//milliseconds
	public static final int LOCATION_REFRESH_INTERVAL_NORMAL= 20000;//milliseconds
	public static final int LOCATION_REFRESH_SLOWER_NORMAL= 60000;//milliseconds

	public static final int SNOOZING_REQUEST_CODE = 1;
	//public static final String MAP_API_URL = "http://watchus-001-site1.smarterasp.net/Api/";
	//public static final String MAP_API_URL = "http://watchus-001-site1.htempurl.com/Api/";
	//public static final String MAP_API_URL = "http://redtopdev.com/CoordifyAPI/api/";
	public static final String MAP_API_URL = "https://redtopdev.azurewebsites.net/CoordifyAPI/api/";
	//public static final String EMAIL_EXCEPTION_URL = "http://redtopdev.com/server.php/";
	public static final String EMAIL_EXCEPTION_URL = "https://redtopdev.azurewebsites.net/server.php/";
	public static final String METHOD_GET_REGISTERED_CONTACTS = "Contacts/GetRegisteredContacts";
	public static final String METHOD_COUNTRY_CODES = "CountryCodes";
	public static final String METHOD_EVENT_DETAIL = "Event/Get";
	public static final String METHOD_USER_LOCATION = "Location/Get";
	public static final String METHOD_USER_LOCATION_UPLOAD="Location/Upload";	
	public static final String METHOD_ACCOUNT_REGISTER = "Account/Register";
	public static final String METHOD_RESPOND_INVITE = "Event/RespondToInvite";
	public static final String METHOD_POKEALL_CONTACTS = "Contacts/RemindContact";
	public static final String METHOD_CREATE_EVENT = "Event/CreateEvent";
	public static final String METHOD_UPDATE_EVENT = "Event/Update";
	public static final String METHOD_SMS_GATEWAY = "Contacts/SendSMSOTP";
	public static final String METHOD_EXTEND_EVENT = "Event/ExtendEvent";
	public static final String METHOD_END_EVENT = "Event/EndEvent";
	public static final String METHOD_LEAVE_EVENT = "Event/LeaveEvent";
	public static final String METHOD_DELETE_EVENT = "Event/DeleteEvent";  
	public static final String METHOD_UPDATE_PARTICIPANTS = "Event/UpdateParticipants";  
	public static final String METHOD_UPDATE_DESTINATION = "Event/UpdateEventLocation";  
	public static final String METHOD_SAVE_FEEDBACK = "Account/SaveFeedback";
	public static final String TAG_ID_COUNTRY = "Code";
	public static final String TAG_NAME = "Name";
	public static final String MOBILE_NUMBER = "MobileNumber";
	public static final String DEVICE_ID = "DeviceId";
	public static final String LOGIN_ID = "LoginID";
	public static final String LOGIN_NAME = "LoginName";
	public static final String COUNTRY_CODE = "countryCode";

	// preference file key	
	public static final String USER_AUTH_TOKEN = "user_auth_token";

	public static final String SUBFOLDER_JARVIS = "/eventtracker";
	public static final int READ_DATA_MODEL_OBJECT_FROM_FILE = 111;
	public static final int WRITE_DATA_MODEL_OBJECT_TO_FILE = 112;

	public static final String FILE_HEADER_EVENT_DETAIL = "event_detail";

	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
	public static final String NETWORK_STATUS_UPDATE = "networkStatusUpdate";
	public static final String GCM_REGISTRATION_TOKEN = "gcmregistrationToken";
	public static final String CURRENT_LATITUDE ="latitude";
	public static final String CURRENT_LONGITUDE ="longitude";
	public static final int ReminderBroadcastId = 1;
	public static final int TrackingStartBroadcastId = 2;
	public static final int TrackingStopBroadcastId = 3;
	public static final int EventEndBroadcastId = 4;
	public static final int EventStartBroadcastId = 5;
	public static final int LocationServiceCheckBroadcastId = 6;

	public static final String REGISTERED_CONTACTS = "rContacts";
	public static final String CONTACTS = "contacts";
	public static final String IS_REGISTERED_CONTACT_LIST_INITIALIZED= "isrcli";
	public static final String IS_CONTACT_LIST_INITIALIZED = "iscli";
	public static final String IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED = "isrclif";
	public static final String IS_CONTACT_LIST_INITIALIZATION_FAILED = "isclif";
	public static final String REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED = "rclif";
	public static final String REGISTERED_CONTACT_LIST_INITIALIZATION_SUCCESS = "rclis";
	public static final String CONTACT_LIST_INITIALIZATION_SUCCESS = "clis";
	public static final String CONTACT_LIST_INITIALIZATION_FAILED = "clif";
	public static final String EVENT_RECEIVED ="eventReceived";
	public static final String EVENT_USER_RESPONSE = "eventUserResponse";
	public static final String EVENT_OVER = "eventOver";
	public static final String EVENT_ENDED = "eventEnded";	
	public static final String EVENT_LEFT = "eventLeft";
	public static final String EVENT_REMINDER = "eventReminder";
	public static final String EVENT_START = "eventStart";
	public static final String EVENTS_REFRESHED = "eventsRefreshed";
	public static final String TRACKING_STARTED = "trackingStarted"; 
	public static final String CHECK_LOCATION_SERVICE = "checkLocationService";
	public static final String EVENT_DESTINATION_UPDATED = "eventDestinationUpdated"; 
	public static final String EVENT_DESTINATION_UPDATED_BY_INITIATOR = "eventDestinationUpdatedByInitiator";
	public static final String EVENT_PARTICIPANTS_UPDATED_BY_INITIATOR = "eventParticipantsUpdatedByInitiator";
	public static final String REMOVED_FROM_EVENT_BY_INITIATOR = "removedFromEventByInitiator";
	public static final String EVENT_ENDED_BY_INITIATOR = "eventEndedByInitiator";
	public static final String EVENT_DELETE_BY_INITIATOR = "eventDeletedByInitiator";
	public static final String EVENT_UPDATED_BY_INITIATOR= "eventUpdatedByInitiator";
	public static final String EVENT_EXTENDED_BY_INITIATOR= "eventExtendedByInitiator";
	public static final String PARTICIPANT_LEFT_EVENT = "participantLeftEvent";

	public static final String PARTICIPANTS_LOCATION_UPDATE_INTENT_ACTION = "com.redtop.engaze.PARTICIPANTS_LOCATION_UPDATE";

	public static final String CACHE_EVENTS ="events";
	public static final String CACHE_TRACK_EVENTS ="trackevents";
	public static final String CACHE_CONTACTS ="contacts";
	public static final String CACHE_REGISTERED_CONTACTS ="registeredcontacts";
	public static final String CACHE_DESTINATIONS ="destinations";

	public static final int HOME_ACTIVITY_LOCATION_TEXT_LENGTH = 44;
	public static final int PICK_LOCATION_ACTIVITY_LOCATION_TEXT_LENGTH = 36;
	public static final int EVENTS_ACTIVITY_LOCATION_TEXT_LENGTH = 24;
	public static final int EDIT_ACTIVITY_LOCATION_TEXT_LENGTH = 34;
	public static final int MEMBER_NAME_TEXT_LENGTH = 20;
	public static final int ZOOM_VALUE= 16;
	public static final String TRACKING_ON = "1";
	public static final String TRACKING_OFF = "2";
	public static final String EVENT_END = "3";	
	public static final String EVENT_OPEN = "4";
	public static final String EMAIL_ACCOUNT = "emailAccount";
	public static final float DESTINATION_RADIUS = 30;
	public static final long POKE_INTERVAL = 15;
	public static enum Action {
		POKEALL(1), LEAVEEVENT(2), 
		ENDEVENT(3)	, EXTENDEVENTENDTIME(4), 
		ADDREMOVEPARTICIPANTS(5), CHANGEDESTINATION(6), 
		SAVEUSERRESPONSE(7), REFRESHEVENTLIST(8), 
		DELETEEVENT(9),	SAVEEVENT(10), 
		SAVEPROFILE(11), SAVEEVENTSHAREMYLOCATION(12),
		SAVEEVENTTRACKBUDDY(13), UPDATEEVENTWITHPARTICIPANTRESPONSE(14), 
		UPDATEEVENTWITHPARTICIPANTLEFT(15),	EVENTENDEDBYINITIATOR(16),
		EVENTEXTENDEDBYINITIATOR(17),EVENTDELETEDBYINITIATOR(18), 
		PARTICIPANTSUPDATEDBYINITIATOR(19), CURRENTPARTICIPANTREMOVEDBYINITIATOR(20),
		EVENTDESTINATIONCHANGEDBYINITIATOR(21), GETEVENTDATAFROMSERVER(22),
		REMOVEBUDDYFROMSHARING(23), POKEPARTICIPANT(24), SETTIMEBASEDALERT(25);
		private final int actionId;
		private Action(int actionId){
			this.actionId = actionId;
		}

		public int getAction(){
			return this.actionId;
		}

		public static Action getAction(int actionId){
			switch(actionId){
			case 1:
				return Action.POKEALL;
			case 2:
				return Action.LEAVEEVENT;
			case 3:
				return Action.ENDEVENT;
			case 4:
				return Action.EXTENDEVENTENDTIME;
			case 5:
				return Action.ADDREMOVEPARTICIPANTS;
			case 6:
				return Action.CHANGEDESTINATION;
			case 7:
				return Action.SAVEUSERRESPONSE;
			case 8:
				return Action.REFRESHEVENTLIST;
			case 9:
				return Action.DELETEEVENT;
			case 10:
				return Action.SAVEEVENT;
			case 11:
				return Action.SAVEPROFILE;
			case 12:
				return Action.SAVEEVENTSHAREMYLOCATION;
			case 13:
				return Action.SAVEEVENTTRACKBUDDY;
			case 14:
				return Action.UPDATEEVENTWITHPARTICIPANTRESPONSE;
			case 15:
				return Action.UPDATEEVENTWITHPARTICIPANTLEFT;
			case 16:
				return Action.EVENTENDEDBYINITIATOR;
			case 17:
				return Action.EVENTEXTENDEDBYINITIATOR;
			case 18:
				return Action.EVENTDELETEDBYINITIATOR;
			case 19:
				return Action.PARTICIPANTSUPDATEDBYINITIATOR;
			case 20:
				return Action.CURRENTPARTICIPANTREMOVEDBYINITIATOR;
			case 21:
				return Action.EVENTDESTINATIONCHANGEDBYINITIATOR;
			case 22:
				return Action.GETEVENTDATAFROMSERVER;
			case 23:
				return Action.REMOVEBUDDYFROMSHARING;
			case 24:
				return Action.POKEPARTICIPANT;
			case 25:
				return Action.SETTIMEBASEDALERT;
			default :
				return Action.POKEALL;							
			}
		}
	}

	public static enum TrackingType{
		SELF(1),BUDDY(0);
		private final int trackingType;
		private TrackingType(int trackingType) {
			this.trackingType = trackingType;
		}
		public int getTrackingType() {
			return this.trackingType;
		}
		public static TrackingType getTrackingType(int trackingTypeId)
		{
			switch(trackingTypeId)
			{
			case 0 :
				return TrackingType.BUDDY;						
			case 1:
				return TrackingType.SELF;
			default :
				return TrackingType.SELF;
			}
		}
	}


	public static enum AcceptanceStatus{
		ACCEPTED(1), DECLINED(0),PENDING(-1);

		private final int status;

		private AcceptanceStatus(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public static AcceptanceStatus getStatus(int statusId)
		{
			switch(statusId)
			{

			case 0 :
				return AcceptanceStatus.DECLINED;				

			case -1:
				return AcceptanceStatus.PENDING;			

			case 1:
				return AcceptanceStatus.ACCEPTED;

			default :
				return AcceptanceStatus.PENDING;

			}
		}
	}

	public static enum ReminderFrom{
		NONE(-1), SELF(0), DESTINATION(1),OTHER(2);

		private final int reminderFrom;

		private ReminderFrom(int reminderFrom) {
			this.reminderFrom = reminderFrom;
		}

		public int getDistanceReminderFrom() {
			return reminderFrom;
		}

		public static ReminderFrom getDistanceReminderFrom(int reminderFrom)
		{
			switch(reminderFrom)
			{
			case -1 :
				return ReminderFrom.NONE;

			case 0 :
				return ReminderFrom.SELF;				

			case 1:
				return ReminderFrom.DESTINATION;		

			default :
				return ReminderFrom.NONE;

			}
		}
	}
}


