package com.redtop.engaze.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.R;
import com.redtop.engaze.adapter.ParticipantsWithNoCallSMSListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class EventParticipantListWithNoCallSMSFragment extends DialogFragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEventParticipantListWithNoCallSMSFragmentInteraction(LatLng endpoint);
    }

    private TextView tvHeader;
    private String action;
    private Context mContext;
    private HashMap<String, LatLng> mEndPoints = new HashMap<String, LatLng>();
    private ArrayList<String> mDisplayNameList = new ArrayList<String>();
    private ParticipantsWithNoCallSMSListAdapter mAdapter;

    /**
     * Called when the activity is first created.
     */
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = getActivity();
        final View rootView = inflater.inflate(R.layout.fragment_participants_list_nocallsms, container, false);
        tvHeader = (TextView) rootView.findViewById(R.id.ChooseCategoryHeader);
        action = getArguments().getParcelable("action");
        if (action.equals("loadroute")) {
            tvHeader.setText("Choose Route End point");
            mEndPoints = (HashMap<String, LatLng>) getArguments().getSerializable("endpoints");
            mDisplayNameList = new ArrayList<String>(mEndPoints.keySet());
        }
        ListView list = (ListView) rootView.findViewById(R.id.list_event_participants);
        mAdapter = new ParticipantsWithNoCallSMSListAdapter(getActivity(), R.layout.event_participant_simple_listitem, mDisplayNameList);

        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long arg3) {
                String endointName = (String) adapter.getItemAtPosition(position);
                mListener.onEventParticipantListWithNoCallSMSFragmentInteraction(mEndPoints.get(endointName));
            }
        });

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventParticipantListWithNoCallSMSFragment.OnFragmentInteractionListener) {
            mListener = (EventParticipantListWithNoCallSMSFragment.OnFragmentInteractionListener) context;
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