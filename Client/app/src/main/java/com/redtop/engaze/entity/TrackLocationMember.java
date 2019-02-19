package com.redtop.engaze.entity;

import com.redtop.engaze.utils.Constants.AcceptanceStatus;

public class TrackLocationMember {
	private EventDetail event;
	private EventMember member;
	private AcceptanceStatus acceptance;


	public TrackLocationMember(EventDetail event, EventMember member, AcceptanceStatus acceptance) {
		super();
		this.event = event;
		this.member = member;
		this.acceptance = acceptance;
	}

	public EventDetail getEvent() {
		return event;
	}

	public void setEvent(EventDetail event) {
		this.event = event;
	}

	public void setMember(EventMember member) {
		this.member = member;
	}

	public EventMember getMember() {
		return this.member;
	}
	
	public void setAcceptance(AcceptanceStatus acceptance) {
		this.acceptance = acceptance;
	}

	public AcceptanceStatus getAcceptance() {
		return this.acceptance;
	}

}