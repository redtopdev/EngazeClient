/**
 * 
 */
package com.redtop.engaze.entity;

import java.util.List;

/**
 * @author sandeep2.kumar
 *
 *         05-Aug-2015 11:03:04 pm
 */
public class ListOfEvents {
	private String eventId;
	private String name;
	private String eventTypeId;
	private String description;
	private String startTime;
	private String endTime;
	private String duration;
	private List<EventMember> userList;
	private String initiatorId;
	private String eventStateId;
	private String trackingStateId;
	private String destinationLatitude;
	private String destinationLongitude;
	private String destinationName;
	private String isTrackingRequired;
	private String reminderOffset;
	private String trackingStartOffset;
	private String trackingStopTime;
	private String requestorId;
	private String hasRequestorAcceptedEvent;
	private String hasRequestorAcceptedTracking;
	private String newEventIdHolder;
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEventTypeId() {
		return eventTypeId;
	}
	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public List<EventMember> getUserList() {
		return userList;
	}
	public void setUserList(List<EventMember> userList) {
		this.userList = userList;
	}
	public String getInitiatorId() {
		return initiatorId;
	}
	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}
	public String getEventStateId() {
		return eventStateId;
	}
	public void setEventStateId(String eventStateId) {
		this.eventStateId = eventStateId;
	}
	public String getTrackingStateId() {
		return trackingStateId;
	}
	public void setTrackingStateId(String trackingStateId) {
		this.trackingStateId = trackingStateId;
	}
	public String getDestinationLatitude() {
		return destinationLatitude;
	}
	public void setDestinationLatitude(String destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}
	public String getDestinationLongitude() {
		return destinationLongitude;
	}
	public void setDestinationLongitude(String destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getIsTrackingRequired() {
		return isTrackingRequired;
	}
	public void setIsTrackingRequired(String isTrackingRequired) {
		this.isTrackingRequired = isTrackingRequired;
	}
	public String getReminderOffset() {
		return reminderOffset;
	}
	public void setReminderOffset(String reminderOffset) {
		this.reminderOffset = reminderOffset;
	}
	public String getTrackingStartOffset() {
		return trackingStartOffset;
	}
	public void setTrackingStartOffset(String trackingStartOffset) {
		this.trackingStartOffset = trackingStartOffset;
	}
	public String getTrackingStopTime() {
		return trackingStopTime;
	}
	public void setTrackingStopTime(String trackingStopTime) {
		this.trackingStopTime = trackingStopTime;
	}
	public String getRequestorId() {
		return requestorId;
	}
	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}
	public String getHasRequestorAcceptedEvent() {
		return hasRequestorAcceptedEvent;
	}
	public void setHasRequestorAcceptedEvent(String hasRequestorAcceptedEvent) {
		this.hasRequestorAcceptedEvent = hasRequestorAcceptedEvent;
	}
	public String getHasRequestorAcceptedTracking() {
		return hasRequestorAcceptedTracking;
	}
	public void setHasRequestorAcceptedTracking(String hasRequestorAcceptedTracking) {
		this.hasRequestorAcceptedTracking = hasRequestorAcceptedTracking;
	}
	public String getNewEventIdHolder() {
		return newEventIdHolder;
	}
	public void setNewEventIdHolder(String newEventIdHolder) {
		this.newEventIdHolder = newEventIdHolder;
	}
	@Override
	public String toString() {
		return "ListOfEvents [eventId=" + eventId + ", name=" + name
				+ ", eventTypeId=" + eventTypeId + ", description="
				+ description + ", startTime=" + startTime + ", endTime="
				+ endTime + ", duration=" + duration + ", userList=" + userList.toString()
				+ ", initiatorId=" + initiatorId + ", eventStateId="
				+ eventStateId + ", trackingStateId=" + trackingStateId
				+ ", destinationLatitude=" + destinationLatitude
				+ ", destinationLongitude=" + destinationLongitude
				+ ", destinationName=" + destinationName
				+ ", isTrackingRequired=" + isTrackingRequired
				+ ", reminderOffset=" + reminderOffset
				+ ", trackingStartOffset=" + trackingStartOffset
				+ ", trackingStopTime=" + trackingStopTime + ", requestorId="
				+ requestorId + ", hasRequestorAcceptedEvent="
				+ hasRequestorAcceptedEvent + ", hasRequestorAcceptedTracking="
				+ hasRequestorAcceptedTracking + ", newEventIdHolder="
				+ newEventIdHolder + "]";
	}
	
	
}
