package com.redtop.engaze.fragment;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.redtop.engaze.R;
import com.redtop.engaze.entity.Reminder;
import com.redtop.engaze.utils.AppUtility;

public class CustomReminderFragment extends DialogFragment {

	private OnFragmentInteractionListener mListener;
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onCustomReminderFragmentInteraction(Reminder reminder );
	}

	private ArrayList<TextView> periods;
	private ArrayList<TextView>notificationTypes;
	private Reminder reminder = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View rootView = inflater.inflate(R.layout.fragment_custom_reminder, container, false);
		reminder =  getArguments().getParcelable("Reminder");
		Button save = (Button)rootView.findViewById(R.id.save_event_reminder);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText intervalEditText = (EditText)rootView.findViewById(R.id.Value);
				if (!intervalEditText.getText().toString().isEmpty()){		
					try{
						int userInput = Integer.parseInt(intervalEditText.getText().toString());
						reminder.setTimeInterval(userInput);
						if(AppUtility.validateReminderInput(reminder, getActivity())){
							mListener.onCustomReminderFragmentInteraction(reminder);
							dismiss();
						}
					}catch(NumberFormatException e){
						Toast.makeText(getActivity(),
								getResources().getString(R.string.message_createEvent_reminderMaxAlert),
								Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getActivity(),
							getResources().getString(R.string.event_invalid_input_message),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		EditText text = (EditText)rootView.findViewById(R.id.Value);
		text.setText(Integer.toString(reminder.getTimeInterval()));
		TextView notificationType = null;	    
		notificationTypes= new ArrayList<TextView>();

		notificationType = (TextView)rootView.findViewById(R.id.Alarm);
		setDefaultNotificationType(notificationType);	    
		notificationTypes.add(notificationType);

		notificationType = (TextView)rootView.findViewById(R.id.Notification);
		setDefaultNotificationType(notificationType);	    
		notificationTypes.add(notificationType);   


		for(int i=0;i<notificationTypes.size();i++){
			TextView nt = notificationTypes.get(i);

			nt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for(int i=0;i<notificationTypes.size();i++){
						TextView nt = notificationTypes.get(i);
						nt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);	
						nt.setTextColor(getResources().getColorStateList(R.color.primary_text));
					}
					// TODO Auto-generated method stub
					TextView  nt = ((TextView)v);
					reminder.setNotificationType(nt.getTag().toString());
					Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
					nt.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
					nt.setTextColor(getResources().getColorStateList(R.color.primary));					 					
				}
			});

		}


		TextView period = null;
		periods = new ArrayList<TextView>();

		period = (TextView)rootView.findViewById(R.id.Minutes);
		setDefaultPeriod(period);	    
		periods.add(period);

		period = (TextView)rootView.findViewById(R.id.Hours);
		setDefaultPeriod(period);	    
		periods.add(period);

		for(int i=0;i<periods.size();i++){
			TextView per = periods.get(i);

			per.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for(int i=0;i<periods.size();i++){
						TextView dv = periods.get(i);
						dv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
						String duration = dv.getText().toString();
						if(duration.contains(getResources().getString(R.string.before)))
						{
							dv.setText(duration.substring(0, duration.indexOf(getResources().getString(R.string.before))));
							dv.setTextColor(getResources().getColorStateList(R.color.primary_text));
						}						
					}

					// TODO Auto-generated method stub
					TextView  dur = ((TextView)v);
					Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
					dur.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
					dur.setTextColor(getResources().getColorStateList(R.color.primary));
					dur.setText(dur.getText().toString().concat(getResources().getString(R.string.before)));
					reminder.setPeriod(dur.getTag().toString());					
				}
			});					    
		}
		return  rootView;
	}

	private void setDefaultPeriod(TextView period) {
		if(period.getTag().equals(reminder.getPeriod()))
		{		   
			period.setText(period.getText().toString().concat(getResources().getString(R.string.before)));
			period.setTextColor(getResources().getColorStateList(R.color.primary));
			Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
			period.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
		}	
	}

	private void setDefaultNotificationType(TextView notificationType) {
		if(notificationType.getTag().equals(reminder.getNotificationType()))
		{
			// TODO Auto-generated method stub
			notificationType.setTextColor(getResources().getColorStateList(R.color.primary));
			Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
			notificationType.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof CustomReminderFragment.OnFragmentInteractionListener) {
			mListener = (CustomReminderFragment.OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
}
