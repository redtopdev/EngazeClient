package com.redtop.engaze.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.redtop.engaze.R;
import com.redtop.engaze.adapter.HomeRunningEventListAdapter;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.utils.EventManager;

import java.util.List;

public class ShowRunningEventsFragment extends DialogFragment {
    public ListView mHomeRunningEventListView;
    private HomeRunningEventListAdapter mRunningEventAdapter;
    private List<EventDetail> mRunningEventDetailList;
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
        final View rootView = inflater.inflate(R.layout.fragment_show_running_events, container, false);
        mRunningEventDetailList = EventManager.getRunningEventList(getActivity());
        mHomeRunningEventListView = (ListView)rootView.findViewById(R.id.home_running_event_list);
        mRunningEventAdapter = new HomeRunningEventListAdapter(getActivity(), R.layout.item_home_running_event_list, mRunningEventDetailList);
        mHomeRunningEventListView.setAdapter(mRunningEventAdapter);
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
