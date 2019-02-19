package com.redtop.engaze.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.fragment.EventParticipantsInfoFragment;
import com.redtop.engaze.fragment.EventRecurrenceInfoFragment;
import com.redtop.engaze.EventsActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.ShowLocationActivity;
import com.redtop.engaze.customeviews.CircularImageView;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.fontawesome.TextFont;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.ContactAndGroupListManager;
import com.redtop.engaze.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventReCycleViewAdapter extends RecyclerView.Adapter<EventReCycleViewAdapter.EventViewHolder> {
    public List<EventDetail> mEventList;
    private static Context mContext;
    private static ProgressDialog pDialog;

    public EventReCycleViewAdapter(
            List<EventDetail> items, Context context) {
        mContext = context;
        this.mEventList = items;

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Saving Response...");
        pDialog.setCancelable(false);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder viewHolder, final int i) {
        if (mEventList == null || mEventList.size() == 0) {
            return;
        }
        final EventDetail ed = mEventList.get(i);
        viewHolder.eventDetail = ed;
        ContactOrGroup cg = ContactAndGroupListManager.getContact(mContext, ed.getInitiatorId());
        if (cg != null) {
            viewHolder.profileImage.setBackground(cg.getIconImageDrawable(mContext));
        } else {
            viewHolder.profileImage.setBackground(ContactOrGroup.getAppUserIconDrawable(mContext));
        }
        if (AppUtility.isCurrentUserInitiator(ed.getInitiatorId(), mContext)) {
            viewHolder.txtInitiator.setText("You");
        } else {
            viewHolder.txtInitiator.setText(ed.GetInitiatorName());
        }

        //viewHolder.txtEventID.setText(ed.getEventId());
        if (ed.getDestinationName() == null || ed.getDestinationName().equals("")) {
            viewHolder.rlLocationSection.setVisibility(View.GONE);
        } else {
            viewHolder.rlLocationSection.setVisibility(View.VISIBLE);

            viewHolder.txtLocation.setText(AppUtility.createTextForDisplay(ed.getDestinationName(), Constants.EVENTS_ACTIVITY_LOCATION_TEXT_LENGTH));
        }
        String title = ed.getName();
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        viewHolder.txtEventTile.setText(title);


        viewHolder.txtEventParticipant.setText(Integer.toString(ed.getMemberCount()));
        viewHolder.imgEventTypeImage.setBackgroundResource(((EventsActivity) mContext).mEventTypeImages.getResourceId(Integer.parseInt(ed.getEventTypeId()), -1));

        setDescriptionLayout(ed, viewHolder);

        SimpleDateFormat originalformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {

            Date startDate = originalformat.parse(ed.getStartTime());
            Date endDate = originalformat.parse(ed.getEndTime());
            Date currentDate = Calendar.getInstance().getTime();

            if (endDate.getTime() >= currentDate.getTime() && currentDate.getTime() > startDate.getTime()) {
                viewHolder.runningStatus = true;
            } else {
                viewHolder.runningStatus = false;
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);

            viewHolder.eventStartDayOfWeek = DateUtil.getDayOfWeek(cal);
            viewHolder.eventStartDayOfMonth = DateUtil.getDayOfMonth(cal);
            viewHolder.eventStartMonth = DateUtil.getShortMonth(cal);
            viewHolder.eventStartYear = DateUtil.getYear(cal);
            viewHolder.eventStartTime = DateUtil.getTime(cal);

            viewHolder.txtEventStartDayOfWeek.setText(viewHolder.eventStartDayOfWeek);
            viewHolder.txtEventStartDayOfMonth.setText(viewHolder.eventStartDayOfMonth);
            viewHolder.txtEventStartMonth.setText(viewHolder.eventStartMonth);
            viewHolder.txtEventStartYear.setText(viewHolder.eventStartYear);
            viewHolder.txtEventStartTime.setText(viewHolder.eventStartTime);

            viewHolder.txtEventTimeToStart.setText(setTimeToStartText(cal));

            cal.add(Calendar.MINUTE, Integer.parseInt(ed.getTrackingStartOffset()) * -1);


            if (ed.getCurrentMember().getAcceptanceStatus() == AcceptanceStatus.ACCEPTED && cal.getTime().getTime() - currentDate.getTime() < 0) {
                viewHolder.trackingStatus = true;
                viewHolder.imgEventTrackingOn.setVisibility(View.VISIBLE);
            } else {
                viewHolder.trackingStatus = false;
                viewHolder.imgEventTrackingOn.setVisibility(View.GONE);
            }

            Calendar calEndDate = Calendar.getInstance();
            calEndDate.setTime(endDate);

            viewHolder.eventEndDayOfWeek = DateUtil.getDayOfWeek(calEndDate);
            viewHolder.eventEndDayOfMonth = DateUtil.getDayOfMonth(calEndDate);
            viewHolder.eventEndMonth = DateUtil.getShortMonth(calEndDate);
            viewHolder.eventEndYear = DateUtil.getYear(calEndDate);
            viewHolder.eventEndTime = DateUtil.getTime(calEndDate);
            if (!(viewHolder.eventEndDayOfMonth.equals(viewHolder.eventStartDayOfMonth)
                    && viewHolder.eventEndMonth.equals(viewHolder.eventStartMonth)
                    && viewHolder.eventEndYear.equals(viewHolder.eventStartYear))) {
                String dateToAppend = viewHolder.eventEndMonth + " " + viewHolder.eventEndDayOfMonth + " " + viewHolder.eventEndYear;
                viewHolder.txtEventEndTime.setText(viewHolder.eventEndTime + ", " + dateToAppend);
            } else {
                viewHolder.txtEventEndTime.setText(viewHolder.eventEndTime);
            }


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (ed.getIsRecurrence().equals("true")) {
            viewHolder.btnRecurrence.setVisibility(View.VISIBLE);
            viewHolder.btnRecurrence.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = ((BaseActivity)mContext).getSupportFragmentManager();
                    EventRecurrenceInfoFragment dialogFragment = new EventRecurrenceInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("RecurrenceType", ed.getRecurrenceType());
                    bundle.putString("NumberOfOccurences", ed.getNumberOfOccurencesLeft());// ed.getNumberOfOccurences());
                    bundle.putString("FrequencyOfOcuurence", ed.getFrequencyOfOccurence());
                    bundle.putSerializable("Recurrencedays", ed.getRecurrenceDays());
                    bundle.putString("RecurrenceDayOfMonth", viewHolder.eventStartDayOfMonth);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(fragmentManager, "EventRecurrenceInfo fragment");
                }
            });
        } else {
            viewHolder.btnRecurrence.setVisibility(View.GONE);
        }

        viewHolder.rlLocationSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowLocationActivity.class);
                intent.putExtra("DestinatonLocation", ed.getDestinationName());
                intent.putExtra("DestinatonAddress", ed.getDestinationAddress());
                intent.putExtra("DestinatonLatitude", ed.getDestinationLatitude());
                intent.putExtra("DestinatonLongitude", ed.getDestinationLongitude());
                mContext.startActivity(intent);

                if (((EventsActivity) mContext).mActionMode != null) {
                    ((EventsActivity) mContext).mActionMode.finish();
                }
            }
        });

        viewHolder.llParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed.getMembers() != null) {
                    Intent intent = new Intent(mContext, EventParticipantsInfoFragment.class);
                    intent.putExtra("EventMembers", ed.getMembers());
                    intent.putExtra("InitiatorID", ed.getInitiatorId());
                    intent.putExtra("EventId", ed.getEventId());
                    mContext.startActivity(intent);

                    if (((EventsActivity) mContext).mActionMode != null) {
                        ((EventsActivity) mContext).mActionMode.finish();

                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable originalDrawable = viewHolder.imgEventTime.getBackground();
            Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
            DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.icon));
            viewHolder.imgEventTime.setBackground(wrappedDrawable);

            originalDrawable = viewHolder.imgParticipants.getBackground();
            wrappedDrawable = DrawableCompat.wrap(originalDrawable);
            DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.icon));
            viewHolder.imgParticipants.setBackground(wrappedDrawable);

            originalDrawable = viewHolder.imgEventLocation.getBackground();
            wrappedDrawable = DrawableCompat.wrap(originalDrawable);
            DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.icon));
            viewHolder.imgEventLocation.setBackground(wrappedDrawable);

            originalDrawable = viewHolder.imgEventTypeImage.getBackground();
            wrappedDrawable = DrawableCompat.wrap(originalDrawable);
            DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.icon));
            viewHolder.imgEventTypeImage.setBackground(wrappedDrawable);

        }
    }

    private void setDescriptionLayout(EventDetail ed, EventViewHolder viewHolder) {
        if (ed.getDescription().isEmpty()) {
            viewHolder.llEventDescription.setVisibility(View.GONE);
        } else {
            viewHolder.llEventDescription.setVisibility(View.VISIBLE);
            final String description = ed.getDescription();
            viewHolder.txtEventDesc.setText(description);
        }
    }

    private String setTimeToStartText(Calendar startCal) {

        String durationText = DateUtil.getDurationText((startCal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 60000);
        if (durationText.equals("0") || durationText.equals("")) {
            durationText = "RUNNING";
        }

        return durationText;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_event_list, viewGroup, false);
        return new EventViewHolder(itemView);
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        public EventViewHolder(final View itemView) {
            super(itemView);

            this.rlDateSection = (RelativeLayout) itemView.findViewById(R.id.event_datesection);
            this.rlLocationSection = (RelativeLayout) itemView.findViewById(R.id.rl_event_location);
            this.txtEventTile = (TextView) itemView.findViewById(R.id.txt_event_title);
            this.txtEventDesc = (TextView) itemView.findViewById(R.id.txt_event_Desc);
            this.txtLocation = (TextView) itemView.findViewById(R.id.txt_location);
            this.txtEventEndTime = (TextView) itemView.findViewById(R.id.txt_event_end_time);
            this.txtInitiator = (TextView) itemView.findViewById(R.id.txt_initiator_value);

            this.txtEventStartDayOfWeek = (TextView) itemView.findViewById(R.id.txt_event_day);
            this.txtEventStartDayOfMonth = (TextView) itemView.findViewById(R.id.txt_event_date);
            this.txtEventStartMonth = (TextView) itemView.findViewById(R.id.txt_event_month);
            this.txtEventStartYear = (TextView) itemView.findViewById(R.id.txt_event_year);
            this.txtEventStartTime = (TextView) itemView.findViewById(R.id.txt_event_start_time);
            this.txtEventParticipant = (TextView) itemView.findViewById(R.id.txt_event_participant);
            this.llParticipants = (RelativeLayout) itemView.findViewById(R.id.ll_participants);
            this.txtEventTimeToStart = (TextView) itemView.findViewById(R.id.txt_event_timeToStart);
            //this.txtEventAcceptanceStatus = (TextView)itemView.findViewById(R.id.txt_event_acceptance_status);
            this.imgEventTime = (ImageView) itemView.findViewById(R.id.ic_event_time);
            this.imgEventLocation = (ImageView) itemView.findViewById(R.id.ic_event_location);

            this.llEventDescription = (RelativeLayout) itemView.findViewById(R.id.ll_event_description);
            this.imgParticipants = (ImageView) itemView.findViewById(R.id.ic_participant);
            this.imgEventTypeImage = (ImageView) itemView.findViewById(R.id.img_event_type);
            this.profileImage = (CircularImageView) itemView.findViewById(R.id.host_contact_icon);
            this.imgEventTrackingOn = (ImageView) itemView.findViewById(R.id.ic_event_tracking_on);
            this.llDetailRectangle = (LinearLayout) itemView.findViewById(R.id.ll_detail_rectangle);
            this.llDetailRectangle.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_home_buttton));
            this.llDetailRectangle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (
                            eventDetail.getCurrentMember().getAcceptanceStatus() == Constants.AcceptanceStatus.ACCEPTED &&

                                    trackingStatus) {
                        Intent intent = new Intent(mContext, RunningEventActivity.class);
                        intent.putExtra("EventId", eventDetail.getEventId());
                        mContext.startActivity(intent);
                        if (((EventsActivity) mContext).mActionMode != null) {
                            ((EventsActivity) mContext).mActionMode.finish();
                        }
                    }
                }
            });

            llDetailRectangle.setOnLongClickListener(new View.OnLongClickListener() {
                // Called when the user long-clicks on someView
                public boolean onLongClick(View view) {

                    EventsActivity activity = ((EventsActivity) mContext);
                    if (activity.mActionMode != null) {
                        return false;
                    }
                    eventOptions();
                    view.setSelected(true);
                    return true;
                }
            });

            this.btnRecurrence = (TextFont) itemView.findViewById(R.id.btn_recurrence);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CardView view = (CardView) itemView;
                view.setCardBackgroundColor(Color.TRANSPARENT);
                //view.setCardElevation(0);
                view.setRadius(0);
                view.setMaxCardElevation(0);
                view.setPreventCornerOverlap(false);

            } else {
                itemView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            }
            this.rlDateSection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    eventOptions();
                    v.setSelected(true);
                }
            });
        }

        protected void enableDisableContextMenuItems(Menu menu) {
            MenuItem itemMuteUnmute = menu.findItem(R.id.context_action_mute_unmute);
            MenuItem itemAccept = menu.findItem(R.id.context_action_accept);
            MenuItem itemDeclined = menu.findItem(R.id.context_action_decline);
            MenuItem itemEdit = menu.findItem(R.id.context_action_edit);
            MenuItem itemDelete = menu.findItem(R.id.context_action_delete);
            Drawable dr = null;
            if (eventDetail.isMute != null && eventDetail.isMute) {
                dr = ((EventsActivity) mContext).getResources().getDrawable(R.drawable.event_mute);
            } else {
                dr = ((EventsActivity) mContext).getResources().getDrawable(R.drawable.event_unmute);
            }

            itemMuteUnmute.setIcon(dr);

            if (this.eventDetail.getCurrentMember().getUserId().equalsIgnoreCase(this.eventDetail.getInitiatorId())) {
                itemAccept.setVisible(false);
                itemDeclined.setVisible(false);
                if (this.runningStatus || this.trackingStatus) {
                    itemEdit.setVisible(false);
                    itemDelete.setVisible(false);
                } else {
                    itemEdit.setVisible(true);
                    itemDelete.setVisible(true);
                }
            } else {
                itemDelete.setVisible(false);
                itemEdit.setVisible(false);
//				if(this.runningStatus || this.trackingStatus)
//				{
//					itemDelete.setVisible(false);
//				}
//				else
//				{
//					itemDelete.setVisible(true);
//				}
                if (this.eventDetail.getCurrentMember().getAcceptanceStatus() == AcceptanceStatus.ACCEPTED) {
                    itemAccept.setVisible(false);
                    itemDeclined.setVisible(true);
                } else if (this.eventDetail.getCurrentMember().getAcceptanceStatus() == AcceptanceStatus.PENDING) {
                    itemAccept.setVisible(true);
                    itemDeclined.setVisible(true);
                } else {
                    itemAccept.setVisible(true);
                    itemDeclined.setVisible(false);
                }
            }
        }

        public void eventOptions() {
            EventsActivity activity = ((EventsActivity) mContext);
            llDetailRectangle.setBackground(activity.getResources().getDrawable(R.drawable.event_detail_rectangle_long_pressed));

            // Start the CAB using the ActionMode.Callback defined above
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.event_list_toolbar);
            activity.mActionMode = toolbar.startActionMode(activity.mActionModeCallback);
            activity.mActionMode.setTag(eventDetail);
            activity.mActionMode.setTitle("");
            activity.mActionMode.setTitleOptionalHint(false);
            activity.mCurrentItem = itemView;
            Menu menu = activity.mActionMode.getMenu();
            enableDisableContextMenuItems(menu);
        }


        public TextView txtEventTile;
        public TextView txtEventDesc;
        public TextView txtInitiator;
        public TextView txtLocation;
        public TextView txtEventEndTime;
        public TextView txtEventStartDayOfWeek;
        public TextView txtEventStartDayOfMonth;
        public TextView txtEventStartMonth;
        public TextView txtEventStartYear;
        public TextView txtEventStartTime;
        public TextView txtEventParticipant;
        public TextView txtEventTimeToStart;
        public RelativeLayout rlLocationSection;
        public LinearLayout llDetailRectangle;
        public RelativeLayout rlDateSection;

        public RelativeLayout llEventDescription;
        public EventDetail eventDetail;
        public ImageView imgParticipants;
        public ImageView imgEventTypeImage;
        public ImageView imgEventTrackingOn;

        public ImageView imgEventTime;
        public ImageView imgEventLocation;

        public RelativeLayout llParticipants;
        public Boolean trackingStatus;
        public Boolean runningStatus;
        public CircularImageView profileImage;

        public String eventStartDayOfWeek;
        public String eventStartDayOfMonth;
        public String eventStartMonth;
        public String eventStartYear;
        public String eventStartTime;

        public String eventEndDayOfWeek;
        public String eventEndDayOfMonth;
        public String eventEndMonth;
        public String eventEndYear;
        public String eventEndTime;
        public TextFont btnRecurrence;

    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mEventList.size();
    }
}