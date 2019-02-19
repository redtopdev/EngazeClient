package com.redtop.engaze.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.redtop.engaze.R;
import com.redtop.engaze.adapter.NameImageAdapter;
import com.redtop.engaze.entity.NameImageItem;

import java.util.ArrayList;

public class EventTypeListFragment extends DialogFragment implements OnItemClickListener {

	/** Called when the activity is first created. */
	
	ListView listView;
	ArrayList<NameImageItem> rowItems;
	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onEventTypeListFragmentInteraction(NameImageItem nameImageItem);
	}
	
	//public TypedArray images ;

	@Override
	public  View onCreateView(LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState)  {
	    super.onCreate(savedInstanceState);
	    
	    String[] eventTypeName =  getResources().getStringArray(R.array.event_type_name);
	    TypedArray images = getResources().obtainTypedArray(R.array.event_type_image);
		final View rootView = inflater.inflate(R.layout.fragment_event_type_list, container, false);

	    rowItems = new ArrayList<NameImageItem>();
		for (int i = 0; i < eventTypeName.length; i++) {
			NameImageItem item = new NameImageItem(images.getResourceId(i, -1), eventTypeName[i], i);
			rowItems.add(item);
		}

		listView = (ListView) rootView.findViewById(R.id.eve_type_list);
		NameImageAdapter adapter = new NameImageAdapter(getActivity(),
				R.layout.item_name_image_row, rowItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mListener.onEventTypeListFragmentInteraction(rowItems.get(position));
		this.dismiss();
		
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof EventTypeListFragment.OnFragmentInteractionListener) {
			mListener = (EventTypeListFragment.OnFragmentInteractionListener) context;
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
