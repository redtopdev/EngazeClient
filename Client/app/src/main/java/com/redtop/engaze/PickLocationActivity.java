package com.redtop.engaze;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.fragment.SearchLocationFragment;
import com.redtop.engaze.fragment.ShowMapFragment;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;

@SuppressLint("ResourceAsColor")
public class PickLocationActivity extends BaseActivity
implements  View.OnClickListener, ShowMapFragment.ShowMapFragmentActionListener,
        SearchLocationFragment.SearchLocationFragmentActionListener{
    static LatLng currentLocation = new LatLng(12.9667, 77.5667);
    private static final String TAG = PickLocationActivity.class.getName();
    // region elements declaration

    public TextView mSelectedLocationNameText;
    public TextView mSelectedLocationAddressText;
    int mSearchLocationTextLength;
    private SearchLocationFragment mSearchFragment;
    private ShowMapFragment mMapFragment;
    private EventPlace mEventPlace;
    //endregion

    //region Elements Initialization
    private void initializeElements() {
        mSelectedLocationAddressText = (TextView) findViewById(R.id.txt_selected_location_address);
        mSelectedLocationNameText = (TextView) findViewById(R.id.txt_selected_location_name);
        mSearchLocationTextLength = Constants.PICK_LOCATION_ACTIVITY_LOCATION_TEXT_LENGTH;
        mMapFragment = new ShowMapFragment();
        mSearchFragment = new SearchLocationFragment();
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_pick_location);
        mEventPlace = (EventPlace) this.getIntent().getParcelableExtra("DestinatonLocation");
        initializeElements();
        if(mEventPlace!=null) {
            mSelectedLocationNameText.setText(AppUtility.createTextForDisplay(mEventPlace.getName(), mSearchLocationTextLength + 2));
            mSelectedLocationAddressText.setText(mEventPlace.getAddress());
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.map_fragment_container, mMapFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_location:
                Intent intent = new Intent();
                intent.putExtra("DestinatonPlace", (Parcelable) mEventPlace);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onLocationTextClicked(LatLng latLang) {
        findViewById(R.id.rl_map_view).setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putParcelable("LatLang", latLang);
        mSearchFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.search_location_container, mSearchFragment).commit();
    }

    @Override
    public void onShowMapBackButtonPressed() {
        onBackPressed();
        finish();
    }

    @Override
    public void onPlaceSelected(EventPlace eventPlace) {
        mEventPlace = eventPlace;
        mSelectedLocationNameText.setText(AppUtility.createTextForDisplay(mEventPlace.getName(), mSearchLocationTextLength + 2));
        mSelectedLocationAddressText.setText(mEventPlace.getAddress());
        findViewById(R.id.rl_map_view).setVisibility(View.VISIBLE);
        mMapFragment.moveToSelectedLocation(eventPlace);
    }

    @Override
    public void onSearchLocationBackButtonPressed() {
        findViewById(R.id.rl_map_view).setVisibility(View.VISIBLE);
    }
}