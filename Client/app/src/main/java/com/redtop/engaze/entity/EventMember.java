/**
 * 
 */
package com.redtop.engaze.entity;

import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.Constants.ReminderFrom;

/**
 * @author Vijay.kumar
 *
 *         05-Aug-2015 10:55:06 pm
 */
public class EventMember implements DataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8550528718275826735L;
	private String eventId;
	private String userId;
	private String profileName;
	private String contactName;
	private String mobileNumber;
	private String gCMClientId;
	private Boolean isEventAccepted;
	private Boolean isTrackingAccepted;
	private String trackingStartTime;
	private String trackingEndTime;
	private String trackingEndReason;
	private String isTrackingActive;
	private String userEventEndTime;
	private AcceptanceStatus acceptanceStatus;
	private int distanceReminderDistance;
	private String distanceReminderId;
	private ReminderFrom reminderFrom ;
	public Boolean isUserLocationShared;	
	public ContactOrGroup contactOrGroup;	
	
	public EventMember(String userId, String profileName, int distanceReminder, ReminderFrom  distanceReminderFrom){
		this.userId = userId;
		this.profileName = profileName;	
		this.distanceReminderDistance = distanceReminder;
		this.reminderFrom = distanceReminderFrom;//0 destination and 1 from current user
	}
	
	public EventMember(String userId, String profileName, String mobileNumber,
			AcceptanceStatus eventAcceptanceState){
		this.userId = userId;
		this.profileName = profileName;
		this.mobileNumber = mobileNumber;		
		this.acceptanceStatus = eventAcceptanceState;				
	}
	
	

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String cName) {
		this.contactName = cName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getgCMClientId() {
		return gCMClientId;
	}

	public void setgCMClientId(String gCMClientId) {
		this.gCMClientId = gCMClientId;
	}		

	public Boolean getIsTrackingAccepted() {
		return isTrackingAccepted;
	}

	public void setIsTrackingAccepted(Boolean isTrackingAccepted) {
		this.isTrackingAccepted = isTrackingAccepted;
	}

	public String getTrackingStartTime() {
		return trackingStartTime;
	}

	public void setTrackingStartTime(String trackingStartTime) {
		this.trackingStartTime = trackingStartTime;
	}

	public String getTrackingEndTime() {
		return trackingEndTime;
	}

	public void setTrackingEndTime(String trackingEndTime) {
		this.trackingEndTime = trackingEndTime;
	}

	public String getTrackingEndReason() {
		return trackingEndReason;
	}

	public void setTrackingEndReason(String trackingEndReason) {
		this.trackingEndReason = trackingEndReason;
	}

	public String getIsTrackingActive() {
		return isTrackingActive;
	}

	public void setIsTrackingActive(String isTrackingActive) {
		this.isTrackingActive = isTrackingActive;
	}

	public String getUserEventEndTime() {
		return userEventEndTime;
	}

	public void setUserEventEndTime(String userEventEndTime) {
		this.userEventEndTime = userEventEndTime;
	}
	
	public AcceptanceStatus getAcceptanceStatus() {
		return acceptanceStatus;
	}

	public void setAcceptanceStatus(AcceptanceStatus acceptanceStatus) {
		this.acceptanceStatus = acceptanceStatus;
	}
	
	public String getDistanceReminderId() {
		return distanceReminderId;
	}

	public void setDistanceReminderId(String distanceReminderId) {
		this.distanceReminderId = distanceReminderId;
	}
	
	public int getDistanceReminderDistance() {
		return distanceReminderDistance;
	}

	public void setDistanceReminderDistance(int distanceReminderDistance) {
		this.distanceReminderDistance = distanceReminderDistance;
	}
	
	public ReminderFrom getReminderFrom() {
		return reminderFrom;
	}

	public void setReminderFrom(ReminderFrom distanceReminderFrom) {
		this.reminderFrom = distanceReminderFrom;
	}
	public void setContact(ContactOrGroup cg){
		this.contactOrGroup = cg;
	}
	public ContactOrGroup getContact(){
		return this.contactOrGroup;
	}

	@Override
	public String toString() {
		return "UserList [eventId=" + eventId + ", userId=" + userId
				+ ", mobileNumber=" + mobileNumber + ", gCMClientId="
				+ gCMClientId + ", isEventAccepted=" + isEventAccepted
				+ ", isTrackingAccepted=" + isTrackingAccepted
				+ ", trackingStartTime=" + trackingStartTime
				+ ", trackingEndTime=" + trackingEndTime
				+ ", trackingEndReason=" + trackingEndReason
				+ ", isTrackingActive=" + isTrackingActive
				+ ", userEventEndTime=" + userEventEndTime + "]";
	}	
}
