package com.redtop.engaze.entity;

public class EventDetailListItem {
	private String userIcon;
	private String userName;
	private String userLastLocation;
	private String userDistance;
	private String userTimeRemaining;

	public EventDetailListItem(String userIcon, String userName,
			String userLastLocation, String userDistance,
			String userTimeRemaining) {
		super();
		this.userIcon = userIcon;
		this.userName = userName;
		this.userLastLocation = userLastLocation;
		this.userDistance = userDistance;
		this.userTimeRemaining = userTimeRemaining;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLastLocation() {
		return userLastLocation;
	}

	public void setUserLastLocation(String userLastLocation) {
		this.userLastLocation = userLastLocation;
	}

	public String getUserDistance() {
		return userDistance;
	}

	public void setUserDistance(String userDistance) {
		this.userDistance = userDistance;
	}

	public String getUserTimeRemaining() {
		return userTimeRemaining;
	}

	public void setUserTimeRemaining(String userTimeRemaining) {
		this.userTimeRemaining = userTimeRemaining;
	}

}
