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
import com.redtop.engaze.adapter.HomeTrackLocationListAdapter;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.TrackLocationMember;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EventManager;

import java.util.List;

public class ShowShareMyLocationListFragment extends DialogFragment
        implements HomeTrackLocationListAdapter.TrackLocationAdapterCallback{
    public ListView mHomeShareMyLocationListView;
    private HomeTrackLocationListAdapter mShareMyLocationtAdapter;
    private List<TrackLocationMember> mShareMyLocationList;
    private OnFragmentInteractionListener mListener;

    @Override
    public void refreshTrackingEvents() {
        mShareMyLocationList = EventManager.getListOfTrackingMembers(getActivity(), "locationsOut");
        mShareMyLocationtAdapter.items = mShareMyLocationList;
        mShareMyLocationtAdapter.notifyDataSetChanged();
        mListener.onShowShareMyLocationListFragmentInteraction(mShareMyLocationList.size());
    }

    public interface OnFragmentInteractionListener {
        void onShowShareMyLocationListFragmentInteraction(int listCount);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_show_running_events, container, false);
        if ( getActivity() instanceof HomeActivity){
            mListener = (HomeActivity)getActivity();
        }
        mShareMyLocationList = EventManager.getListOfTrackingMembers(getActivity(), "LocationsOut");
        mHomeShareMyLocationListView = (ListView)rootView.findViewById(R.id.home_running_event_list);
        mShareMyLocationtAdapter = new HomeTrackLocationListAdapter(getActivity(), R.layout.item_home_running_event_list, mShareMyLocationList, Constants.TrackingType.SELF);
        mHomeShareMyLocationListView.setAdapter(mShareMyLocationtAdapter);
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
