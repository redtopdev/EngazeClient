package com.redtop.engaze.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

import com.redtop.engaze.R;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.utils.AppUtility;

public class TrackingOffsetFragment extends DialogFragment  {

	private ArrayList<TextView> periods;
	private Context mContext;

	private Duration tracking = null;
	private OnFragmentInteractionListener mListener;
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onTrackingOffsetFragmentInteraction(Duration duration);
	}

	/** Called when the fragment is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View rootView = inflater.inflate(R.layout.fragment_tracking_start_offset, container, false);
		mContext =  getActivity();
		tracking = getArguments().getParcelable("Tracking");

		Button save = (Button)rootView.findViewById(R.id.save_event_track);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText intervalEditText = (EditText)rootView.findViewById(R.id.TrackingValue);
				if (!intervalEditText.getText().toString().isEmpty()){	
					try{
						int userInput = Integer.parseInt(intervalEditText.getText().toString());				
						tracking.setTimeInterval(userInput);
						if(AppUtility.validateTrackingInput(tracking, mContext)){
							mListener.onTrackingOffsetFragmentInteraction(tracking);
							dismiss();
						}
					}catch(NumberFormatException e){
						Toast.makeText(mContext,
								getResources().getString(R.string.message_createEvent_trackingStartMaxAlert),
								Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(mContext,
							getResources().getString(R.string.event_invalid_input_message),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		EditText text = (EditText)rootView.findViewById(R.id.TrackingValue);
		text.setText(Integer.toString(tracking.getTimeInterval()));

		periods = new ArrayList<TextView>();
		TextView period = null;

		period = (TextView)rootView.findViewById(R.id.Minutes);
		setDefaultTrackingPeriod(period);	
		periods.add(period);

		period = (TextView)rootView.findViewById(R.id.Hours);
		setDefaultTrackingPeriod(period);	
		periods.add(period);
		for(int i=0;i<periods.size();i++) {
			TextView pr = periods.get(i);

			pr.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for (int i = 0; i < periods.size(); i++) {
						TextView dv = periods.get(i);
						dv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
						String duration = dv.getText().toString();
						if (duration.contains(getResources().getString(R.string.before))) {
							dv.setText(duration.substring(0, duration.indexOf(getResources().getString(R.string.before))));
							dv.setTextColor(getResources().getColorStateList(R.color.primary_text));
						}

					}
					// TODO Auto-generated method stub
					TextView dur = ((TextView) v);
					Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
					dur.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
					dur.setTextColor(getResources().getColorStateList(R.color.primary));
					dur.setText(dur.getText().toString().concat(getResources().getString(R.string.before)));
					tracking.setPeriod(dur.getTag().toString());
				}
			});
		}
		return  rootView;
	}

	private void setDefaultTrackingPeriod(TextView period) {
		if(period.getTag().equals(tracking.getPeriod()))
		{		   
			period.setText(period.getText().toString().concat(getResources().getString(R.string.before)));
			period.setTextColor(getResources().getColorStateList(R.color.primary));
			Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
			period.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
		}	
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof TrackingOffsetFragment.OnFragmentInteractionListener) {
			mListener = (TrackingOffsetFragment.OnFragmentInteractionListener) context;
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
