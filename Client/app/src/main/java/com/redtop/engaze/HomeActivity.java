package com.redtop.engaze;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.adapter.HomeRunningEventListAdapter.RunningEventAdapterCallback;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.fragment.NavDrawerFragment;
import com.redtop.engaze.fragment.SearchLocationFragment;
import com.redtop.engaze.fragment.ShowMapFragment;
import com.redtop.engaze.fragment.ShowPendingEventsFragment;
import com.redtop.engaze.fragment.ShowRunningEventsFragment;
import com.redtop.engaze.fragment.ShowShareMyLocationListFragment;
import com.redtop.engaze.fragment.ShowTrackBuddyListFragment;
import com.redtop.engaze.fragment.SnoozeOffsetFragment;
import com.redtop.engaze.interfaces.OnActionCompleteListner;
import com.redtop.engaze.interfaces.OnRefreshEventListCompleteListner;
import com.redtop.engaze.localbroadcastmanager.HomeBroadcastManager;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.Constants.Action;
import com.redtop.engaze.utils.EventManager;
import java.util.List;

public class HomeActivity extends BaseActivity implements RunningEventAdapterCallback,
        NavDrawerFragment.FragmentDrawerListener,
        OnRefreshEventListCompleteListner, SnoozeOffsetFragment.OnFragmentInteractionListener,
        ShowTrackBuddyListFragment.OnFragmentInteractionListener,
        ShowShareMyLocationListFragment.OnFragmentInteractionListener,
        ShowMapFragment.ShowMapFragmentActionListener,
        View.OnClickListener,
        SearchLocationFragment.SearchLocationFragmentActionListener{

    // region elements declaration
    public RelativeLayout mPendingImageButtonLayout;
    public TextView mTxtPendingEventListItemCount;
    public RelativeLayout mRunningImageButtonLayout;

    public TextView mTxtRunningEventListItemCount;
    public ImageButton mImgBtnMeetNow;
    public ImageButton mImgBtnMeetLater;
    public ImageButton mImgBtnMeetTrackBuddy;
    public ImageButton mImgBtnMeetShareMyLoc;
    public int mSearchLocationTextLength;
    private Boolean isGPSEnableThreadRun = false;
    private Duration mSnooze;
    private RelativeLayout mTrackBuddyImageButtonLayout;
    private ImageButton mCurrentTrackBuddyListButton;
    private TextView mTxtTrackBuddyEventListItemCount;
    private RelativeLayout mShareMyLocationImageButtonLayout;
    private ImageButton mCurrentShareMyLocationListButton;
    private TextView mTxtShareMyLocationEventListItemCount;
    private ImageButton mCurrentPendingEventListButton;
    private ImageButton mCurrentRunningEventListButton;
    private SearchLocationFragment mSearchFragment;
    private ShowMapFragment mMapFragment;
    public EventPlace mEventPlace;
    //endregion

    //region Elements Initialization
    private void initializeElements(){
        mTrackBuddyImageButtonLayout = (RelativeLayout)findViewById(R.id.rl_hn_track_buddy_events);
        mCurrentTrackBuddyListButton = (ImageButton)findViewById(R.id.img_hn_buddy_tracking);
        mTxtTrackBuddyEventListItemCount = (TextView)findViewById(R.id.txt_track_buddy_events );

        mShareMyLocationImageButtonLayout = (RelativeLayout)findViewById(R.id.rl_hn_share_location_events);
        mCurrentShareMyLocationListButton = (ImageButton)findViewById(R.id.img_hn_location_sharing);
        mTxtShareMyLocationEventListItemCount = (TextView)findViewById(R.id.txt_sharing_location_events );

        mPendingImageButtonLayout = (RelativeLayout)findViewById(R.id.rl_hn_pending_events);
        mCurrentPendingEventListButton = (ImageButton)findViewById(R.id.img_hn_pending_events);
        mTxtPendingEventListItemCount = (TextView)findViewById(R.id.txt_unread_events );

        mRunningImageButtonLayout = (RelativeLayout)findViewById(R.id.rl_hn_running_events);
        mCurrentRunningEventListButton = (ImageButton)findViewById(R.id.img_hn_running_events);
        mTxtRunningEventListItemCount = (TextView)findViewById(R.id.txt_running_events );

        mTrackBuddyImageButtonLayout.setOnClickListener(this);
        mCurrentTrackBuddyListButton.setOnClickListener(this);
        mShareMyLocationImageButtonLayout.setOnClickListener(this);
        mCurrentShareMyLocationListButton.setOnClickListener(this);
        mCurrentPendingEventListButton.setOnClickListener(this);
        mPendingImageButtonLayout.setOnClickListener(this);
        mCurrentRunningEventListButton.setOnClickListener(this);
        mRunningImageButtonLayout.setOnClickListener(this);

        mImgBtnMeetNow = (ImageButton)findViewById(R.id.img_meet_now);
        mImgBtnMeetLater =  (ImageButton)findViewById(R.id.img_meet_later);
        mImgBtnMeetTrackBuddy = (ImageButton)findViewById(R.id.img_track_buddy);
        mImgBtnMeetShareMyLoc = (ImageButton)findViewById(R.id.img_share_mylocation);
        mImgBtnMeetNow.setOnClickListener(this);
        mImgBtnMeetLater.setOnClickListener(this);
        mImgBtnMeetTrackBuddy.setOnClickListener(this);
        mImgBtnMeetShareMyLoc.setOnClickListener(this);

        mSearchLocationTextLength = Constants.HOME_ACTIVITY_LOCATION_TEXT_LENGTH;

        mMapFragment = new ShowMapFragment();
        mSearchFragment = new SearchLocationFragment();


        if(AppUtility.deviceDensity <320){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mImgBtnMeetTrackBuddy.getLayoutParams();
            params.setMargins(10, 0, 10, 2); //substitute parameters for left, top, right, bottom
            mImgBtnMeetTrackBuddy.setLayoutParams(params);
            RelativeLayout.LayoutParams paramsml = (RelativeLayout.LayoutParams)mImgBtnMeetLater.getLayoutParams();
            paramsml.setMargins(10, 0, 10, 2); //substitute parameters for left, top, right, bottom
            mImgBtnMeetLater.setLayoutParams(paramsml);
            RelativeLayout.LayoutParams paramsh = (RelativeLayout.LayoutParams)mImgBtnMeetShareMyLoc.getLayoutParams();
            paramsh.setMargins(10, 0, 10, 2); //substitute parameters for left, top, right, bottom
            mImgBtnMeetShareMyLoc.setLayoutParams(paramsh);
            TextView txtTB = (TextView)findViewById(R.id.txt_track_buddy);
            txtTB.setTextSize(11);
            txtTB = (TextView)findViewById(R.id.txt_share_my_location);
            txtTB.setTextSize(11);
            txtTB = (TextView)findViewById(R.id.txt_meet_later);
            txtTB.setTextSize(11);
            txtTB = (TextView)findViewById(R.id.txt_meet_now);
            txtTB.setTextSize(11);
        }
    }
    //endregion

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastManager);
        super.onPause();
    }

    @Override
    protected void onResume() {
        turnOnOfInternetAvailabilityMessage(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastManager,
                mBroadcastManager.getFilter());
        displayNotifications();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        TAG = HomeActivity.class.getName();
        setContentView(R.layout.activity_home);
        initializeElements();
        Toolbar toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            NavDrawerFragment drawerFragment = (NavDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
            drawerFragment.setDrawerListener(this);
            toolbar.setOnTouchListener(new View.OnTouchListener() {
                Handler handler = new Handler();

                int numberOfTaps = 0;
                long lastTapTimeMs = 0;
                long touchDownMs = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchDownMs = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.removeCallbacksAndMessages(null);

                            if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                                //it was not a tap

                                numberOfTaps = 0;
                                lastTapTimeMs = 0;
                                break;
                            }

                            if (numberOfTaps > 0
                                    && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                                numberOfTaps += 1;
                            } else {
                                numberOfTaps = 1;
                            }

                            lastTapTimeMs = System.currentTimeMillis();

                            if (numberOfTaps == 5) {
                                //handle triple tap
                                if(Constants.DEBUG){
                                    Constants.DEBUG = false;
                                    Toast.makeText(mContext, "DEBUG mode Disabled!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Constants.DEBUG = true;
                                    Toast.makeText(mContext, "DEBUG mode Enabled!", Toast.LENGTH_SHORT).show();

                                }
                            }
                    }

                    return true;
                }
            });
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.map_fragment_container, mMapFragment).commit();
        mBroadcastManager = new HomeBroadcastManager(mContext);
        Log.i(TAG, "density: " + AppUtility.deviceDensity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            //		case R.id.action_myevents:
            //			if(mInternetStatus){
            //				intent = new Intent(this, EventsActivity.class);
            //				startActivity(intent);
            //			}
            //			break;

            case R.id.action_refresh:
                showProgressBar(getResources().getString(R.string.message_general_progressDialog));
                EventManager.refreshEventList(this, this, this);
                if (Constants.DEBUG) {
                    Log.d(TAG, "Refresh Clicked in Home Layout!");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void saveEventState(String eventId, AcceptanceStatus status) {
        showProgressBar("Please wait");
        EventManager.saveUserResponse(status, mContext, eventId, this, this);
    }

    private void displayNotifications() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                updatePendingEventListButtonView();
                updateRunningEventListButtonView();
                updateShareMyLocationListButtonView();
                updateRefreshTrackBuddyListButtonView(EventManager.getListOfTrackingMembers(mContext, "locationsIn").size());
            }
        });
    }

    @Override
    public void onEventEndClicked() {
        updateRunningEventListButtonView();
    }

    @Override
    public void onEventLeaveClicked() {
        updateRunningEventListButtonView();
    }

    @Override
    public void actionComplete(Action action) {
        updateShareMyLocationListButtonView();
        updateRefreshTrackBuddyListButtonView(EventManager.getListOfTrackingMembers(mContext, "locationsIn").size());
        updateRunningEventListButtonView();
        updatePendingEventListButtonView();
        super.actionComplete(action);
    }

    @Override
    public void RefreshEventListComplete(List<EventDetail> eventDetailList) {
        updateShareMyLocationListButtonView();
        updateRefreshTrackBuddyListButtonView(EventManager.getListOfTrackingMembers(mContext, "locationsIn").size());
        updateRunningEventListButtonView();
        updatePendingEventListButtonView();
        hideProgressBar();
    }

    @Override
    public void onSnoozeOffsetFragmentInteraction(Duration snoozeDuration) {

        showProgressBar(getResources().getString(R.string.message_general_progressDialog));
        //update server and cache with new Event end time
        mSnooze = snoozeDuration;
        EventManager.extendEventEndTime(mSnooze.getTimeInterval(), mContext, notificationselectedEvent, new OnActionCompleteListner() {
            @Override
            public void actionComplete(Action action) {
                updateShareMyLocationListButtonView();
                updateRefreshTrackBuddyListButtonView();
                hideProgressBar();
            }
        }, this);
    }

    //region click events Definitions
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_meet_now:
                if (mInternetStatus) {
                    Intent intent = new Intent(mContext, TrackLocationActivity.class);
                    intent.putExtra("DestinatonLocation", (Parcelable) ((HomeActivity) mContext).mEventPlace);
                    intent.putExtra("caller", HomeActivity.class.toString());
                    intent.putExtra("EventTypeId", 6);
                    startActivity(intent);
                }
                break;

            case R.id.img_meet_later:
                if (mInternetStatus) {
                    Intent intent = new Intent(mContext, CreateEditEventActivity.class);
                    intent.putExtra("DestinatonLocation", (Parcelable) ((HomeActivity) mContext).mEventPlace);
                    startActivity(intent);
                }
                break;

            case R.id.img_share_mylocation:
                if (mInternetStatus) {
                    Intent intent = new Intent(mContext, TrackLocationActivity.class);
                    intent.putExtra("EventTypeId", 100);//EventType share my location
                    startActivity(intent);
                }
                break;

            case R.id.img_track_buddy:
                if (mInternetStatus) {
                    Intent intent = new Intent(mContext, TrackLocationActivity.class);
                    intent.putExtra("EventTypeId", 200);//EventType track buddy
                    startActivity(intent);
                }
                break;

            case R.id.img_hn_pending_events:
                showPendingEventListFragment();
                break;

            case R.id.img_hn_running_events:
                showRunningEventListFragment();
                break;

            case R.id.img_hn_location_sharing:
                showShareMyLocationListFragment();
                break;
            case R.id.img_hn_buddy_tracking:
                ShowTrackBuddyListFragment();
                break;

            case R.id.rl_hn_running_events:
                showRunningEventListFragment();
                break;

            case R.id.rl_hn_track_buddy_events:
                ShowTrackBuddyListFragment();
                break;

            case R.id.rl_hn_share_location_events:
                showShareMyLocationListFragment();
                break;

            case R.id.rl_hn_pending_events:
                showPendingEventListFragment();
                break;
        }
    }
//endregion

    //region toolbar notification buttons activities
    public void updateRefreshTrackBuddyListButtonView() {
        updateRefreshTrackBuddyListButtonView(
                EventManager.getListOfTrackingMembers(mContext, "LocationsIn").size());
    }

    public void updateRefreshTrackBuddyListButtonView(int trackBuddyListCount) {

        if (trackBuddyListCount == 0) {
            mTrackBuddyImageButtonLayout.setVisibility(View.GONE);
        } else {
            mTrackBuddyImageButtonLayout.setVisibility(View.VISIBLE);
            mTxtTrackBuddyEventListItemCount.setText(Integer.toString(trackBuddyListCount));
        }

    }
    public void updateShareMyLocationListButtonView(){
        updateShareMyLocationListButtonView(
                EventManager.getListOfTrackingMembers(mContext, "LocationsOut").size());
    }

    public void updateShareMyLocationListButtonView(int shareMyLocationListCount) {
        if (shareMyLocationListCount == 0) {
            mShareMyLocationImageButtonLayout.setVisibility(View.GONE);
        } else {
            mShareMyLocationImageButtonLayout.setVisibility(View.VISIBLE);
            mTxtShareMyLocationEventListItemCount.setText(Integer.toString(shareMyLocationListCount));
        }
    }

    public void updateRunningEventListButtonView() {
        int runningEventListCount = EventManager.getRunningEventList(mContext).size();
        if (runningEventListCount == 0) {
            mRunningImageButtonLayout.setVisibility(View.GONE);
        } else {
            mRunningImageButtonLayout.setVisibility(View.VISIBLE);
            mTxtRunningEventListItemCount.setText(Integer.toString(runningEventListCount));
        }
    }

    public void updatePendingEventListButtonView() {
        int pendingEventListCount = EventManager.getPendingEventList(mContext).size();
        if (pendingEventListCount == 0) {
            mPendingImageButtonLayout.setVisibility(View.GONE);
        } else {
            mPendingImageButtonLayout.setVisibility(View.VISIBLE);
            mTxtPendingEventListItemCount.setText(Integer.toString(pendingEventListCount));
        }
    }

    private void showRunningEventListFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ShowRunningEventsFragment dialogFragment = new ShowRunningEventsFragment();
        dialogFragment.show(fm, "ShowRunningEvents fragment");
    }

    private void showPendingEventListFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ShowPendingEventsFragment dialogFragment = new ShowPendingEventsFragment();
        dialogFragment.show(fm, "ShowPending fragment");
    }

    private void showShareMyLocationListFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ShowShareMyLocationListFragment dialogFragment = new ShowShareMyLocationListFragment();
        dialogFragment.show(fm, "ShowShareMyLocation fragment");
    }

    private void ShowTrackBuddyListFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ShowTrackBuddyListFragment dialogFragment = new ShowTrackBuddyListFragment();
        dialogFragment.show(fm, "ShowTrackBuddy fragment");
    }

    private void toggleListView(final RelativeLayout listViewLayout, ImageButton listViewShowHideButton, int arrowId ){
        if(listViewLayout.getVisibility()== View.GONE){
            listViewShowHideButton.setSelected(true);
            //hideAllListViewExceptThis(listViewLayout);
            int[] loc = new int[2];
            listViewShowHideButton.getLocationInWindow(loc);
            findViewById(arrowId).setX(loc[0] + 10);
            //mCurrentRunningEventListButton.setBackground(activity.getResources().getDrawable(R.drawable.ripple_gray));
            listViewLayout.setVisibility(View.VISIBLE);
            listViewLayout.setAlpha(0.0f);
            listViewLayout.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            listViewLayout.setVisibility(View.VISIBLE);
                        }
                    });
        }
        else{
            listViewShowHideButton.setSelected(false);
            //mCurrentRunningEventListButton.setBackground(activity.getResources().getDrawable(R.drawable.ripple_lightgray));
            listViewLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTrackBuddyListFragmentInteraction(int listCount) {
        updateRefreshTrackBuddyListButtonView(listCount);
    }

    @Override
    public void onShowShareMyLocationListFragmentInteraction(int listCount) {
        updateShareMyLocationListButtonView(listCount);
    }

    @Override
    public void onLocationTextClicked(LatLng latLng) {
        findViewById(R.id.rl_map_view).setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putParcelable("LatLang", latLng);

        mSearchFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        //fm.beginTransaction().detach(mMapFragment).commit();
        fm.beginTransaction().add(R.id.search_location_container, mSearchFragment).commit();
    }

    @Override
    public void onShowMapBackButtonPressed() {
        //nothing to do as this button is not visible in home screen
    }

    @Override
    public void onPlaceSelected(EventPlace eventPlace) {
        findViewById(R.id.rl_map_view).setVisibility(View.VISIBLE);
        mEventPlace = eventPlace;
        mMapFragment.moveToSelectedLocation(eventPlace);
    }

    @Override
    public void onSearchLocationBackButtonPressed() {
        findViewById(R.id.rl_map_view).setVisibility(View.VISIBLE);
    }

    //endregion
}
