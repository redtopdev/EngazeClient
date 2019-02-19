package com.redtop.engaze.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.string;
import android.widget.TextView;


public class EventData {
	public String EventId;
	public String Name;
	public int EventType;
	public String Description;
	public String StartTime;
	public String EndTime;
	public double Duration;
	public String InitiatorId;
	public int StateId;
	public int TrackingStateId;
	public String TrackingStopTime;
	public double DestinationLatitude;
	public double DestinationLongitude;
	public Object DestinationName;
	public boolean IsTrackingRequired;
	public double ReminderOffset;
	public double TrackingStartOffset;
	public String AdminList;

	private Calendar cal;


	public void setName(String name){
		this.Name = name;
	}
	public String getName(){
		return Name;
	}

	public void setEventType(int EventType){
		this.EventType = EventType;
	}

	public void setStartTime(TextView dateView) {
		SimpleDateFormat parseFormat = new SimpleDateFormat("E MMMM dd,yyyy");
		dateView.setText(parseFormat.format(cal.getTime()));
		this.StartTime = dateView.getText().toString(); 
	}

	public void setEndTime(TextView dateView) {
		SimpleDateFormat parseFormat = new SimpleDateFormat("E MMMM dd,yyyy");
		dateView.setText(parseFormat.format(cal.getTime()));
		this.EndTime = dateView.getText().toString(); 
	}

	public long getDuration(){
		Date S, E = null;
		long diffHours = 0;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		try{
			S = format.parse(this.StartTime);	
			E = format.parse(this.EndTime);

			long diff = S.getTime() -  E.getTime();		
			diffHours = diff/(60 * 60 * 1000) % 24;		
		}
		catch (Exception e){
			e.printStackTrace();
		}	
		return diffHours;	
	}
}
