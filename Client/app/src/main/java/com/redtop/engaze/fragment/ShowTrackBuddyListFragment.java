package com.redtop.engaze.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.redtop.engaze.HomeActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.adapter.HomeRunningEventListAdapter;
import com.redtop.engaze.adapter.HomeTrackLocationListAdapter;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.TrackLocationMember;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EventManager;

import java.util.List;

public class ShowTrackBuddyListFragment extends DialogFragment implements HomeTrackLocationListAdapter.TrackLocationAdapterCallback {
    public ListView mHomeTrackBuddyListView;
    private HomeTrackLocationListAdapter mTrackBuddyListAdapter;
    private List<TrackLocationMember> mTrackBuddyList;
    private OnFragmentInteractionListener mListener;

    @Override
    public void refreshTrackingEvents() {
        mTrackBuddyList = EventManager.getListOfTrackingMembers(getActivity(), "locationsIn");
        mTrackBuddyListAdapter.items = mTrackBuddyList;
        mTrackBuddyListAdapter.notifyDataSetChanged();
        mListener.onTrackBuddyListFragmentInteraction(mTrackBuddyList.size());
    }

    public interface OnFragmentInteractionListener {
        void onTrackBuddyListFragmentInteraction(int listCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_show_running_events, container, false);
        if ( getActivity() instanceof  HomeActivity){
            mListener = (HomeActivity)getActivity();
        }
        mTrackBuddyList = EventManager.getListOfTrackingMembers(getActivity(), "locationsIn");
        mHomeTrackBuddyListView = (ListView)rootView.findViewById(R.id.home_running_event_list);
        mTrackBuddyListAdapter = new HomeTrackLocationListAdapter(getActivity(), R.layout.item_home_running_event_list, mTrackBuddyList, Constants.TrackingType.BUDDY);
        mHomeTrackBuddyListView.setAdapter(mTrackBuddyListAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
