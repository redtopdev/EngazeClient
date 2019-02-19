package com.redtop.engaze.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.redtop.engaze.R;
import com.redtop.engaze.adapter.HomePendingEventListAdapter;
import com.redtop.engaze.adapter.HomeRunningEventListAdapter;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.utils.EventManager;

import java.util.List;

public class ShowPendingEventsFragment extends DialogFragment {
    public ListView mHomePendingEventListView;
    private HomePendingEventListAdapter mPendingEventAdapter;
    private List<EventDetail> mPendingEventDetailList;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSnoozeOffsetFragmentInteraction(Duration duration);
    }

    /**
     * Called when the fragment is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_show_pending_events, container, false);
        mPendingEventDetailList = EventManager.getPendingEventList(getActivity());
        mHomePendingEventListView = (ListView)rootView.findViewById(R.id.home_pending_event_list);
        mPendingEventAdapter = new HomePendingEventListAdapter(getActivity(), R.layout.item_home_running_event_list, mPendingEventDetailList);
        mHomePendingEventListView.setAdapter(mPendingEventAdapter);
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
