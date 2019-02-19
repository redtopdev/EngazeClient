package com.redtop.engaze;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.entity.NameImageItem;
import com.redtop.engaze.entity.Reminder;
import com.redtop.engaze.fragment.CustomReminderFragment;
import com.redtop.engaze.fragment.DurationOffsetFragment;
import com.redtop.engaze.fragment.EventTypeListFragment;
import com.redtop.engaze.fragment.SnoozeOffsetFragment;
import com.redtop.engaze.fragment.TrackingOffsetFragment;
import com.redtop.engaze.interfaces.OnEventSaveCompleteListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.DateUtil;
import com.redtop.engaze.utils.DestinationCacher;
import com.redtop.engaze.utils.EventManager;
import com.redtop.engaze.utils.LocationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public abstract class BaseEventActivity extends BaseActivity implements DurationOffsetFragment.OnFragmentInteractionListener,
        TrackingOffsetFragment.OnFragmentInteractionListener,
        EventTypeListFragment.OnFragmentInteractionListener,
        CustomReminderFragment.OnFragmentInteractionListener{
    protected int mDurationTime = 0;
    protected int mDurationOffset;
    protected TextView mQuickEventName;

    protected EventDetail mEventDetailData;
    protected TextView mEventLocationTextView;
    protected NameImageItem mEventTypeItem;
    protected Reminder mReminder;
    protected Duration mTracking;
    protected Duration mDuration;
    protected TextView mDurationtext;
    protected EventPlace mDestinationPlace;
    protected LocationHelper mLh;
    protected ImageView mEventTypeView;
    protected ArrayList<ContactOrGroup> mContactsAndgroups;
    protected String TAG;
    protected AlertDialog mAlertDialog;
    protected long mTrackingOffset = 0;
    protected long mReminderOffset = 0;
    protected Date mStartDate;
    protected String mEventId = null;
    protected String mEventName;

    protected String mEventDescription;
    protected String mCreateUpdateSuccessfulMessage;
    protected String mCreateUpdateUrl;
    protected String mIsQuickEvent;
    protected JSONObject mEventJobj;
    protected Boolean mFromEventsActivity = true;
    //For Recurrence
    protected String mIsRecurrence = "false";
    protected String mRecurrenceType;
    protected String mNumberOfOccurences;
    protected String mFrequencyOfOcuurence;
    protected ArrayList<Integer> mRecurrencedays;
    protected int mEventTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLh = new LocationHelper(this, this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case LOCATION_REQUEST_CODE:
                    mDestinationPlace = data.getParcelableExtra("DestinatonPlace");
                    mLh.displayPlace(mDestinationPlace, mEventLocationTextView);

                    break;

            }
        }
    }

    protected void SetDurationText(Duration duration) {
        if (duration != null) {
            mDurationTime = duration.getTimeInterval();
            String holder = "";
            switch (duration.getPeriod()) {
                case "minute":
                    mDurationOffset = duration.getTimeInterval();
                    break;
                case "hour":
                    mDurationOffset = duration.getTimeInterval() * 60;
                    break;
                case "day":
                    mDurationOffset = duration.getTimeInterval() * 60 * 24;
                    break;
                case "week":
                    mDurationOffset = duration.getTimeInterval() * 60 * 24 * 7;
                    break;
            }
            holder = DateUtil.getDurationText(mDurationOffset).toLowerCase();
            if (holder.equals("0")) {
                holder = "0 minute";

            } else if (!(holder.contains("minutes") || holder.contains("minute"))) {
                holder = holder.replace("min", "minute");
                holder = holder.replace("mins", "minutes");
            }

            mDurationtext.setText(holder);

        } else {
            Log.d(TAG, "inside else");
        }

    }

    protected void SetReminderText(Reminder reminder) {
        TextView reminderOffsettext = (TextView) findViewById(R.id.ReminderOffeset);

        if (reminder != null) {
            String reminderText = "";
            switch (reminder.getPeriod()) {
                case "minute":
                    mReminderOffset = reminder.getTimeInterval();
                    break;
                case "hour":
                    mReminderOffset = reminder.getTimeInterval() * 60;
                    break;
                case "day":
                    mReminderOffset = reminder.getTimeInterval() * 60 * 24;
                    break;
                case "week":
                    mReminderOffset = reminder.getTimeInterval() * 60 * 24 * 7;
                    break;
            }

            reminderText = DateUtil.getDurationText(mReminderOffset).toLowerCase();
            if (reminderText.equals("0")) {
                reminderText = "0 minute";

            } else if (!(reminderText.contains("minutes") || reminderText.contains("minute"))) {
                reminderText = reminderText.replace("min", "minute");
                reminderText = reminderText.replace("mins", "minutes");
            }
            reminderText += " before through " + reminder.getNotificationType();

            reminderOffsettext.setText(reminderText);

        } else {
            Log.d(TAG, "insdie else");
        }

    }

    protected void SetTrackingText(Duration tracking) {
        TextView trackingOffsettext = (TextView) findViewById(R.id.TrackingStartOffeset);

        if (tracking != null) {
            String trackingText = "";
            switch (tracking.getPeriod()) {
                case "minute":
                    mTrackingOffset = tracking.getTimeInterval();
                    break;
                case "hour":
                    mTrackingOffset = tracking.getTimeInterval() * 60;
                    break;
                case "day":
                    mTrackingOffset = tracking.getTimeInterval() * 60 * 24;
                    break;
                case "week":
                    mTrackingOffset = tracking.getTimeInterval() * 60 * 24 * 7;
                    break;
            }

            trackingText = DateUtil.getDurationText(mTrackingOffset).toLowerCase();
            if (trackingText.equals("0")) {
                trackingText = "0 minute";

            } else if (!(trackingText.contains("minutes") || trackingText.contains("minute"))) {
                trackingText = trackingText.replace("min", "minute");
                trackingText = trackingText.replace("mins", "minutes");
            }

            trackingText += " before";
            trackingOffsettext.setText(trackingText);

        } else {
            Log.d(TAG, "inside else");
        }
    }

    protected void setAlertDialog(String Title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);
        // set title
        alertDialogBuilder.setTitle(Title);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });

        mAlertDialog = alertDialogBuilder.create();
    }

    protected JSONObject createEventJson() {
        String isUserLocationShared = "true";
        if (mEventTypeId == 100) {
            isUserLocationShared = "false";
        }
        JSONObject jobj = new JSONObject();
        JSONObject userListJobj;
        JSONArray jsonarr = new JSONArray();
        Date endDate = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        calendar.add(Calendar.MINUTE, mDurationOffset);
        endDate = calendar.getTime();

        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String start = DateUtil.convertToUtcDateTime(parseFormat.format(mStartDate), parseFormat); //parseFormat.format(mStartDate);
        String end = DateUtil.convertToUtcDateTime(parseFormat.format(endDate), parseFormat);//parseFormat.format(endDate);
        try {
            String userId;
            if (mContactsAndgroups != null) {
                for (ContactOrGroup cg : mContactsAndgroups) {
                    userId = cg.getUserId();
                    userListJobj = new JSONObject();
                    if (userId != null && !userId.isEmpty()) {
                        userListJobj.put("UserId", userId);
                        userListJobj.put("IsUserLocationShared", isUserLocationShared);
                    } else {
                        userListJobj.put("MobileNumber", cg.getMobileNumber());
                    }
                    jsonarr.put(userListJobj);
                }
            }

            jobj.put("Name", mEventName);
            jobj.put("Description", mEventDescription);
            if (mEventId != null) {
                jobj.put("EventId", mEventId);
            }
            jobj.put("UserList", jsonarr);
            jobj.put("Duration", mDurationOffset);
            jobj.put("InitiatorId", AppUtility.getPref(Constants.LOGIN_ID, this));
            jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, this));
            jobj.put("EventStateId", "1");
            jobj.put("TrackingStateId", "1");
            jobj.put("IsTrackingRequired", "True");
            jobj.put("StartTime", start);
            jobj.put("EndTime", end);

            if (mDestinationPlace != null) {
                jobj.put("DestinationLatitude", mDestinationPlace.getLatLang().latitude);
                jobj.put("DestinationLongitude", mDestinationPlace.getLatLang().longitude);
                jobj.put("DestinationAddress", mDestinationPlace.getAddress());
                //jobj.put("DestinationName", mDestinationPlace.getName());
                jobj.put("DestinationName", mEventLocationTextView.getText());
            } else {
                jobj.put("DestinationLatitude", "");
                jobj.put("DestinationLongitude", "");
                jobj.put("DestinationAddress", "");
                jobj.put("DestinationName", "");
            }

            setReminderOffset();
            if (mReminder != null) {
                jobj.put("ReminderType", mReminder.getNotificationType());
            }
            jobj.put("ReminderOffset", "" + mReminderOffset + "");
            jobj.put("EventTypeId", "" + mEventTypeItem.getImageIndex());
            jobj.put("TrackingStopTime", "");
            setTrackingOffset();
            jobj.put("TrackingStartOffset", "" + mTrackingOffset + "");
            jobj.put("IsQuickEvent", mIsQuickEvent);
            if (mIsRecurrence.equals("true")) {
                jobj.put("IsRecurring", true);
                jobj.put("RecurrenceCount", mNumberOfOccurences);
                jobj.put("RecurrenceFrequency", mFrequencyOfOcuurence);
                jobj.put("RecurrenceFrequencyTypeId", mRecurrenceType);
                if (mRecurrenceType.equals("2")) {
                    String days = "";
                    for (int day : mRecurrencedays) {
                        days += "," + Integer.toString(day);
                    }
                    days = days.substring(1);
                    jobj.put("RecurrenceDaysOfWeek", days);
                }
            } else {
                jobj.put("IsRecurring", false);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jobj;
    }

    private void setTrackingOffset() {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(mStartDate);
        long diffMinutes = (startCal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 60000;

        if (mTrackingOffset > diffMinutes) {
            mTrackingOffset = diffMinutes;
        }
    }

    private void setReminderOffset() {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(mStartDate);
        long diffMinutes = (startCal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 60000;
        if (mReminderOffset > diffMinutes) {
            mReminderOffset = diffMinutes;
            mReminder = null;
        }
    }

    protected void saveEvent(final Boolean isMeetNow) {

        showProgressBar(getResources().getString(R.string.message_general_progressDialog));

        EventManager.saveEvent(mContext, mEventJobj, isMeetNow, mReminder, new OnEventSaveCompleteListner() {

            @Override
            public void eventSaveComplete(EventDetail eventDetail) {
                Toast.makeText(getApplicationContext(),
                        mCreateUpdateSuccessfulMessage,
                        Toast.LENGTH_LONG).show();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDestinationPlace != null) {//when event is created without destination
                            DestinationCacher.cacheDestination(mDestinationPlace, mContext);
                        }
                    }
                });
                try {
                    if (mEventJobj.getInt("EventTypeId") == 100) {
                        gotoHomePage();
                    } else if (mEventJobj.getInt("EventTypeId") == 200) {
                        gotoTrackingPage(eventDetail.getEventId());
                    } else if (isMeetNow) {
                        gotoTrackingPage(eventDetail.getEventId());
                    } else {
                        gotoEventsPage();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, this);

    }

    protected void gotoHomePage() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                hideProgressBar();
                finish();
            }
        });

    }

    private void gotoTrackingPage(final String eventid) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, RunningEventActivity.class);
                intent.putExtra("EventId", eventid);
                startActivity(intent);
                hideProgressBar();
                finish();
                //}
            }
        });
    }

    private void gotoEventsPage() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, EventsActivity.class);
                startActivity(intent);
                hideProgressBar();
                finish();
            }
        });

    }

    @Override
    public void onDurationOffsetFragmentInteraction(Duration duration) {
        mDuration = duration;
        SetDurationText(mDuration);
    }

    @Override
    public void onTrackingOffsetFragmentInteraction(Duration tracking) {
        mTracking = tracking;
        SetTrackingText(mTracking);
    }

    @Override
    public void onCustomReminderFragmentInteraction(Reminder reminder) {
        mReminder = reminder;
        SetReminderText(mReminder);
    }

    @Override
    public void onEventTypeListFragmentInteraction(NameImageItem nameImageItem) {
        mEventTypeItem = nameImageItem;
        if (mEventTypeItem != null) {
            Drawable originalDrawable = getResources().getDrawable(mEventTypeItem.getImageId());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
                DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.icon));
                mEventTypeView.setBackground(wrappedDrawable);
            } else {
                mEventTypeView.setBackground(getResources().getDrawable(mEventTypeItem.getImageId()));
            }

            //eventTypeView.setText(eventTypeItem.getName());
            Log.d(TAG, "insdie if " + mEventTypeItem.getName());
            Log.d(TAG, "insdie if " + mEventTypeItem.getImageId());
        } else {
            Log.d(TAG, "insdie else");
        }
    }
}