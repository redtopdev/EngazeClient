package com.redtop.engaze.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redtop.engaze.R;

@SuppressWarnings("deprecation")
public class EventRecurrenceInfoFragment extends DialogFragment {
	private LinearLayout mLlDailySettings, mLlWeekySettings, mLlMonthlySettings;	
	private Hashtable<Integer, String> mWeekDays;
	private TextView mPattern, mDays;
	protected String mRecurrenceType;
	protected String mNumberOfOccurences;
	protected String mFrequencyOfOcuurence;
	protected String mRecurrenceDayOfMonth;
	protected ArrayList<Integer>mRecurrencedays;
	private Context mContext;
	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View rootView = inflater.inflate(R.layout.fragment_recurrenceinfo, container, false);
		mRecurrencedays = (ArrayList<Integer>) getArguments().getSerializable("Recurrencedays");
		mRecurrenceType = getArguments().getString("RecurrenceType");
		mNumberOfOccurences = getArguments().getString("NumberOfOccurences");
		mFrequencyOfOcuurence = getArguments().getString("FrequencyOfOcuurence");
		mRecurrenceDayOfMonth = getArguments().getString("RecurrenceDayOfMonth");
		mLlDailySettings = (LinearLayout)getActivity().findViewById(R.id.ll_daily_settings);
		mLlWeekySettings = (LinearLayout)getActivity().findViewById(R.id.ll_weekly_settings);
		mLlMonthlySettings = (LinearLayout)getActivity().findViewById(R.id.ll_monthly_settings);
		mPattern = (TextView)getActivity().findViewById(R.id.pattern);
		mDays = (TextView)getActivity().findViewById(R.id.txtDays);
		populateEventRecurrenceData();
		return  rootView;
	}

	private void populateEventRecurrenceData() {		
		mWeekDays = new Hashtable<Integer, String>();
		mWeekDays.put(1, "Sun");
		mWeekDays.put(2, "Mon");
		mWeekDays.put(3, "Tues");
		mWeekDays.put(4, "Wednes");
		mWeekDays.put(5, "Thurs");
		mWeekDays.put(6, "Fri");
		mWeekDays.put(7, "Sat");

		if(mRecurrenceType.equals("1")){			
			mPattern.setText(getResources().getString(R.string.label_daily));			
			String dailyText = String.format(getResources().getString(R.string.label_daily_occurrences), mFrequencyOfOcuurence);
			((TextView)getActivity().findViewById(R.id.day_frequency_input)).setText(Html.fromHtml(dailyText));
			setDailyLayoutVisible();
		}
		else if(mRecurrenceType.equals("2")){			
			mPattern.setText(getResources().getString(R.string.label_weekly));				
			String weeklyText = String.format(getResources().getString(R.string.label_weekly_occurrences), mFrequencyOfOcuurence);
			((TextView)getActivity().findViewById(R.id.week_frequency_input)).setText(Html.fromHtml(weeklyText));
			setWeeklyLayoutVisible();

			String daysText ="";
			Collections.sort(mRecurrencedays);
			for(int day : mRecurrencedays){				
				daysText =  daysText + ", " + mWeekDays.get(day);		
			}

			daysText = daysText.substring(1);
			String weekDays = String.format(getResources().getString(R.string.label_weekly_days), daysText); 
			mDays.setText(Html.fromHtml(weekDays));
		}
		else{			
			mPattern.setText(getResources().getString(R.string.label_monthly));

			String monthlyText = String.format(getResources().getString(R.string.label_monthly_occurrences), mRecurrenceDayOfMonth, mFrequencyOfOcuurence);
			((TextView)getActivity().findViewById(R.id.month_frequency_input)).setText(Html.fromHtml(monthlyText));
			setMonthlyLayoutVisible();
		}	

		String endAfterOccurrences = String.format(getResources().getString(R.string.label_end_after_occurrence), mNumberOfOccurences);
		((TextView)getActivity().findViewById(R.id.occurece_input)).setText(Html.fromHtml(endAfterOccurrences));
	}

	private void setDailyLayoutVisible(){
		mLlDailySettings.setVisibility(View.VISIBLE);
		mLlWeekySettings.setVisibility(View.GONE);
		mLlMonthlySettings.setVisibility(View.GONE);
	}

	private void setWeeklyLayoutVisible(){
		mLlDailySettings.setVisibility(View.GONE);
		mLlWeekySettings.setVisibility(View.VISIBLE);
		mLlMonthlySettings.setVisibility(View.GONE);
	}

	private void setMonthlyLayoutVisible(){
		mLlDailySettings.setVisibility(View.GONE);
		mLlWeekySettings.setVisibility(View.GONE);
		mLlMonthlySettings.setVisibility(View.VISIBLE);
	}
}
