package com.redtop.engaze.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.redtop.engaze.R;
import com.redtop.engaze.adapter.CachedLocationAdapter;
import com.redtop.engaze.adapter.NewSuggestedLocationAdapter;
import com.redtop.engaze.entity.AutoCompletePlace;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.interfaces.OnSelectLocationCompleteListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.DestinationCacher;
import com.redtop.engaze.utils.EngazeApp;
import com.redtop.engaze.utils.LocationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchLocationFragment.SearchLocationFragmentActionListener} interface
 * to handle interaction events.
 */
public class SearchLocationFragment extends Fragment  implements
        View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, View.OnFocusChangeListener, View.OnTouchListener {

    protected int mSearchLocationTextLength;
    public EditText mTxtSearchLocation;
    public ListView mLocationListView;
    public ListView mFavouriteLocationListView;
    public RelativeLayout mLocationSearchResultView;
    public RelativeLayout mLocationSearchView;
    public ImageView mIconSearchClear ;
    public ImageView mTxtSelectLocationBack;
    public int mFontSize;
    private Fragment currentFrament;
    private LocationHelper mLh;
    private LatLng mLatlong ;
    private ArrayList<AutoCompletePlace> mAutoCompletePlaces = new ArrayList<AutoCompletePlace>();
    private NewSuggestedLocationAdapter mSuggestedLocationAdapter;

    private SearchLocationFragmentActionListener mListener;

    public SearchLocationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_location, container, false);
        currentFrament = this;
        initializeElements(rootView);
        setClickListener();
        mLh = new LocationHelper(getActivity(), getActivity());
        mLatlong = getArguments().getParcelable("LatLang");
        mSuggestedLocationAdapter = new NewSuggestedLocationAdapter(getActivity(), R.layout.item_suggested_location_list, mAutoCompletePlaces);
        mLocationListView.setAdapter(mSuggestedLocationAdapter);
        mFavouriteLocationListView.setVisibility(View.GONE);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<EventPlace> favouriteEventPlaces = DestinationCacher.getDestinationsFromCache(getActivity());
                CachedLocationAdapter cachedLocationAdapter = new CachedLocationAdapter(getActivity(), R.layout.item_cached_location_list, favouriteEventPlaces);
                mFavouriteLocationListView.setAdapter(cachedLocationAdapter);
                if(favouriteEventPlaces.size()>0){
                    mFavouriteLocationListView.setVisibility(View.VISIBLE);
                }
            }
        });
        return rootView;
    }

    protected void initializeElements(View rootView){
        mSearchLocationTextLength = Constants.HOME_ACTIVITY_LOCATION_TEXT_LENGTH;
        mLocationSearchView = (RelativeLayout)rootView.findViewById(R.id.rl_location_search_view);
        mLocationSearchResultView = (RelativeLayout)rootView.findViewById(R.id.rl_location_search_result);
        mLocationListView = (ListView)rootView.findViewById(R.id.location_list);
        mFavouriteLocationListView = (ListView)rootView.findViewById(R.id.favourite_location_list);
        mTxtSelectLocationBack = (ImageView)rootView.findViewById(R.id.img_select_location_back);
        mTxtSearchLocation = (EditText)rootView.findViewById(R.id.txt_search_location);
        mFontSize = (int)rootView.getResources().getDimension(R.dimen.small_text_size);
        mIconSearchClear = (ImageView)rootView.findViewById(R.id.icon_search_clear);
        mIconSearchClear.setVisibility(View.GONE);

    }

    protected void setClickListener(){

        mTxtSelectLocationBack.setOnClickListener(this);
        mIconSearchClear.setOnClickListener(this);
        mLocationListView.setOnItemClickListener(this);
        mTxtSearchLocation.addTextChangedListener(this);
        mTxtSearchLocation.setOnFocusChangeListener(this);
        mTxtSearchLocation.setOnTouchListener(this);
        mFavouriteLocationListView.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchLocationFragmentActionListener) {
            mListener = (SearchLocationFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchLocationFragmentActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.img_select_location_back:
                mListener.onSearchLocationBackButtonPressed();
                getActivity().getSupportFragmentManager().beginTransaction().remove(currentFrament).commit();
                break;

            case R.id.icon_search_clear:
                mTxtSearchLocation.setText("");
                mTxtSearchLocation.setHint( getActivity().getResources().getString(R.string.location_search_bar_hint));
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence query, int start, int before, int count) {

    }

    @Override
    public void onTextChanged(CharSequence query, int start, int before, int count) {
        getAutoCompletePlacePridictions(query);
        if(mTxtSearchLocation.getText().toString().equals("")){
            mIconSearchClear.setVisibility(View.GONE);
            if(mFavouriteLocationListView.getAdapter()!=null && mFavouriteLocationListView.getAdapter().getCount() !=0){
                mFavouriteLocationListView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            mIconSearchClear.setVisibility(View.VISIBLE);
            mFavouriteLocationListView.setVisibility(View.GONE);
            mLocationListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AutoCompletePlace item = null;
        switch(parent.getId()){
            case R.id.location_list:
                item = (AutoCompletePlace) parent.getItemAtPosition(position);
                onListItemClicked(item);
                break;
            case R.id.favourite_location_list:
                EventPlace ep = (EventPlace) parent.getItemAtPosition(position);
                mListener.onPlaceSelected(ep);
                getActivity().getSupportFragmentManager().beginTransaction().remove(currentFrament).commit();
                break;
        }
    }

    public void onListItemClicked(AutoCompletePlace item) {
        mLh.findPlaceById(item.getPlaceId(), EngazeApp.getGoogleApiClient(), new OnSelectLocationCompleteListner() {
            @Override
            public void OnSelectLocationComplete(Place place) {
                EventPlace ev = new EventPlace(place.getName().toString(),
                        place.getAddress().toString(), place.getLatLng());
                AppUtility.hideKeyboard(mTxtSearchLocation, getActivity());
                mListener.onPlaceSelected(ev);
                getActivity().getSupportFragmentManager().beginTransaction().remove(currentFrament).commit();
            }
        });
    }



    private void getAutoCompletePlacePridictions(CharSequence query) {
        if (!AppUtility.isNetworkAvailable(getActivity())) {
            return;
        }
        String newQuery = query.toString();
        Location location = new Location("");
        location.setLatitude(mLatlong.latitude);
        location.setLongitude(mLatlong.longitude);
        LatLngBounds bounds = mLh.getLatLongBounds(location);
        List<Integer> filterTypes = new ArrayList<Integer>();
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .build();
        Places.GeoDataApi.getAutocompletePredictions(EngazeApp.getGoogleApiClient(), newQuery, bounds, filter)
                .setResultCallback(
                        new ResultCallback<AutocompletePredictionBuffer>() {
                            @Override
                            public void onResult(AutocompletePredictionBuffer buffer) {
                                OnAutocomleteSuccess(buffer);
                            }
                        }, 60, TimeUnit.SECONDS);

    }

    private void OnAutocomleteSuccess(AutocompletePredictionBuffer buffer) {
        if (buffer == null)
            return;
        mAutoCompletePlaces.clear();

        if (buffer.getStatus().isSuccess()) {
            for (AutocompletePrediction prediction : buffer) {
                //Add as a new item to avoid IllegalArgumentsException when buffer is released
                mAutoCompletePlaces.add(new AutoCompletePlace(prediction.getPlaceId(), prediction.getFullText(null).toString()));
            }
        }

        //Prevent memory leak by releasing buffer
        buffer.release();
        mSuggestedLocationAdapter.mItems = mAutoCompletePlaces;
        mSuggestedLocationAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface SearchLocationFragmentActionListener {
        // TODO: Update argument type and name
        void onPlaceSelected(EventPlace eventPlace);
        void onSearchLocationBackButtonPressed();
    }
}
