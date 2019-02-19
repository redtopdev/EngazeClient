package com.redtop.engaze.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.redtop.engaze.R;
import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.adapter.CustomParticipantsInfoList;
import com.redtop.engaze.entity.EventMember;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;

@SuppressWarnings("deprecation")
public class EventParticipantsInfoFragment extends DialogFragment {
	private TextView tvHeader;
	private Context mContext;
	private ArrayList<EventMember> eventMembers;

	/**
	 * Called when the activity is first created.
	 */
	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = getActivity();
		final View rootView = inflater.inflate(R.layout.fragment_participantsinfo, container, false);
		tvHeader = (TextView) rootView.findViewById(R.id.ChooseCategoryHeader);
		eventMembers = (ArrayList<EventMember>) getArguments().getSerializable("EventMembers");

		String source = getArguments().getString("source");
		String initiatorID = getArguments().getString("InitiatorID");
		String eventId = getArguments().getString("EventId");
		ListView list = (ListView) rootView.findViewById(R.id.list_event_participants);
		CustomParticipantsInfoList adapter = new CustomParticipantsInfoList(mContext, eventMembers, initiatorID, eventId, source);
		if (source != null && source.equals(RunningEventActivity.class.getName())) {
			if (eventMembers.size() > 0) {
				if (eventMembers.get(0).getAcceptanceStatus() == AcceptanceStatus.ACCEPTED) {
					tvHeader.setText(getResources().getString(R.string.accepted_members_header));
				} else if (eventMembers.get(0).getAcceptanceStatus() == AcceptanceStatus.PENDING) {
					tvHeader.setText(getResources().getString(R.string.pending_members_header));
				} else if (eventMembers.get(0).getAcceptanceStatus() == AcceptanceStatus.DECLINED) {
					tvHeader.setText(getResources().getString(R.string.declined_members_header));
				}
			}
		}

		list.setAdapter(adapter);
		return rootView;
	}
}
	
	/*@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();
	    View view = getWindow().getDecorView();
	    WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
	    lp.gravity = Gravity.TOP;	   
	    lp.y = AppUtility.dpToPx(98, this);	  
	    getWindowManager().updateViewLayout(view, lp);
	}*/

