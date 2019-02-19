package com.redtop.engaze;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.localbroadcastmanager.HomeBroadcastManager;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.LinearLayout;

@SuppressWarnings("deprecation")
public abstract class BaseActivity extends ActionSuccessFailMessageHandler {
    protected HomeBroadcastManager mBroadcastManager = null;

    protected BroadcastReceiver mNetworkUpdateBroadcastReceiver;
    protected static Boolean mInternetStatus;
    protected static final int START_DATE_DIALOG_ID = 1;
    protected static final int START_TIME_DIALOG_ID = 2;
    protected static final int LOCATION_REQUEST_CODE = 7;
    protected static final int REQUEST_CODE_EMAIL = 10;
    protected String TAG;
    public EventDetail notificationselectedEvent;
    public static Boolean isFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInternetStatus = AppUtility.isNetworkAvailable(this);

        mNetworkUpdateBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.NETWORK_STATUS_UPDATE)) {
                    mInternetStatus = AppUtility.isNetworkAvailable(context);
                    turnOnOfInternetAvailabilityMessage(context);
                    if (mInternetStatus) {
                        onInternetConnectionResume();
                    } else {
                        onInternetConnectionLost();
                    }
                }

            }
        };


    }

    protected void turnOnOfInternetAvailabilityMessage(Context context) {
        View v = findViewById(R.id.internet_status);
        if (v != null) {

            LinearLayout networkStatusLayout = (LinearLayout) v;
            if (mInternetStatus) {
                if (networkStatusLayout != null) {
                    networkStatusLayout.setVisibility(View.GONE);
                }
            } else {
                if (networkStatusLayout != null) {
                    networkStatusLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNetworkUpdateBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNetworkUpdateBroadcastReceiver,
                new IntentFilter(Constants.NETWORK_STATUS_UPDATE));
        turnOnOfInternetAvailabilityMessage(this);
    }

    protected void onInternetConnectionResume() {
    }

    protected void onInternetConnectionLost() {

    }


    public void inviteFriend() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.message_invitation_success));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.message_invitation_body));
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.label_invitation_inviteUsing)));
    }

    protected void displayView(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                if (TAG.equals(EventsActivity.class.getName())) {
                    finish();
                } else if (TAG.equals(HomeActivity.class.getName())) {
                    //do nothing
                }
                break;
            case 1:
                if (TAG.equals(HomeActivity.class.getName())) {
                    intent = new Intent(this, EventsActivity.class);
                } else if (TAG.equals(EventsActivity.class.getName())) {
                    //do nothing
                }
                break;

            case 2:
                inviteFriend();
                break;

            case 3:
                intent = new Intent(this, ShowContactsActivity.class);
                break;
            case 4:
                intent = new Intent(this, EventSettingsActivity.class);
                break;

            case 5:
                intent = new Intent(this, FeedbackActivity.class);
                break;

            case 6:
                intent = new Intent(this, AboutActivity.class);
                break;

            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}