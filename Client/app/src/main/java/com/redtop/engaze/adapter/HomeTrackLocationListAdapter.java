package com.redtop.engaze.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.fragment.SnoozeOffsetFragment;
import com.redtop.engaze.customeviews.CircularImageView;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.TrackLocationMember;
import com.redtop.engaze.fragment.TrackingOffsetFragment;
import com.redtop.engaze.interfaces.OnActionCompleteListner;
import com.redtop.engaze.interfaces.OnActionFailedListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.Constants.Action;
import com.redtop.engaze.utils.Constants.TrackingType;
import com.redtop.engaze.utils.DateUtil;
import com.redtop.engaze.utils.EventHelper;
import com.redtop.engaze.utils.EventManager;
import com.redtop.engaze.utils.JsonSerializer;

public class HomeTrackLocationListAdapter extends ArrayAdapter<TrackLocationMember> {

	protected static final int SNOOZING_REQUEST_CODE = 1;
	public List<TrackLocationMember> items;	
	private Context mContext;
	private TrackLocationAdapterCallback callback;	
	private TrackingType trackingType;

	public HomeTrackLocationListAdapter(Context context, int resourceId,
			List<TrackLocationMember> items, TrackingType trackingType ) {
		super(context, resourceId, items);		
		this.mContext = context;	
		this.items = items;
		this.trackingType = trackingType;
		this.callback = ((TrackLocationAdapterCallback) context);
	}

	/*private view holder class*/
	private class ViewHolder {	
		CircularImageView imageView;
		TextView txtName;
		TextView txtView;	
		TextView txtPoke;	
		TextView txtExtend;
		TextView txtStop;
		TextView txtTimeInfo;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final TrackLocationMember rowItem = getItem(position);
		final EventDetail ed = rowItem.getEvent();
		final ContactOrGroup cg = rowItem.getMember().getContact();
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_home_track_location_list, null);
			holder = new ViewHolder();

			holder.txtName = (TextView) convertView.findViewById(R.id.track_location_contact_name);
			holder.imageView = (CircularImageView) convertView.findViewById(R.id.track_location_contact_icon);
			holder.txtTimeInfo = (TextView) convertView.findViewById(R.id.track_location_time_info);
			holder.txtView = (TextView) convertView.findViewById(R.id.track_location_contact_view);
			holder.txtPoke = (TextView) convertView.findViewById(R.id.track_location_contact_poke);
			holder.txtExtend = (TextView) convertView.findViewById(R.id.track_location_contact_extend);
			holder.txtStop = (TextView) convertView.findViewById(R.id.track_location_contact_stop);
			convertView.setTag(holder);

		} else 
			holder = (ViewHolder) convertView.getTag();
		
		if(trackingType == TrackingType.SELF){
			holder.txtView.setVisibility(View.GONE);			
		}		
		
		//holder.txtName.setText(cg.getName());
		holder.txtName.setText(cg.getName());
		holder.imageView.setBackground(cg.getImageDrawable(mContext));
		holder.txtTimeInfo.setText(getStartTimeAndTimeLeftText(ed,rowItem.getAcceptance()));
		if(rowItem.getMember().getAcceptanceStatus()==AcceptanceStatus.ACCEPTED){
			holder.txtPoke.setVisibility(View.GONE);
		}
		holder.txtPoke.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				EventHelper.pokeParticipant(rowItem.getMember().getUserId(),cg.getName() ,ed.getEventId(), mContext);				
			}
		});

		if(AppUtility.isCurrentUserInitiator(ed.getInitiatorId(), mContext)){
			holder.txtExtend.setVisibility(View.VISIBLE);
		}

		holder.txtExtend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((BaseActivity)mContext).notificationselectedEvent = ed;
				FragmentManager fm = ((BaseActivity)mContext).getSupportFragmentManager();
				SnoozeOffsetFragment dialogFragment = new SnoozeOffsetFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("FromHomeLayout", true);
				dialogFragment.setArguments(bundle);
				dialogFragment.show(fm, "snooze fragment");
			}
		});
		holder.txtStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				((BaseActivity)mContext).showProgressBar("Please wait");
				if(AppUtility.isCurrentUserInitiator(ed.getInitiatorId(), mContext)){
					//Current user is initiator...so he can either end the event or remove the member.
					if(ed.getMemberCount() <= 2){
						//End the event since it is a 1 to 1 event
						((BaseActivity)mContext).showProgressBar("Please wait");
						EventManager.endEvent(mContext, ed, new OnActionCompleteListner() {
							@Override
							public void actionComplete(Action action) {
								if(callback!=null){
									callback.refreshTrackingEvents();
								}
								((BaseActivity)mContext).hideProgressBar();						
							}
						}, new OnActionFailedListner() {
							
							@Override
							public void actionFailed(String msg, Action action) {
								EventManager.refreshEventList(mContext, null, null);
								((BaseActivity)mContext).actionFailed(msg, action);
							}
						});
						
					}else{
						//remove the row item member alone since there are still other members in the event.
						((BaseActivity)mContext).showProgressBar("Please wait");
						ed.getContactOrGroup().remove(ed.getCurrentMember().getContact());
												
						JSONObject jObj= JsonSerializer.createUpdateParticipantsJSON(mContext, ed.getContactOrGroup(), ed.getEventId());
						EventManager.addRemoveParticipants(jObj, mContext, new OnActionCompleteListner() {
							@Override
							public void actionComplete(Action action) {	
								//updateRecyclerViews();						
								if(callback!=null){
									callback.refreshTrackingEvents();
								}							
								//locationhandler.post(locationRunnable);
								((BaseActivity)mContext).hideProgressBar();	
							}
						}, new OnActionFailedListner() {
							
							@Override
							public void actionFailed(String msg, Action action) {
								EventManager.refreshEventList(mContext, null, null);
								((BaseActivity)mContext).actionFailed(msg, action);								
							}
						});
					}
					
				}else{
					//Current user is just a participant, so he can only leave the event.
					
						((BaseActivity)mContext).showProgressBar("Please wait");
						EventManager.leaveEvent(mContext, ed, new OnActionCompleteListner() {

							@Override
							public void actionComplete(Action action) {
								rowItem.getMember().setAcceptanceStatus(AcceptanceStatus.DECLINED);
								if(callback!=null){
									callback.refreshTrackingEvents();
								}
								((BaseActivity)mContext).hideProgressBar();
							}
						}, new OnActionFailedListner() {
							
							@Override
							public void actionFailed(String msg, Action action) {
								EventManager.refreshEventList(mContext, null, null);
								((BaseActivity)mContext).actionFailed(msg, action);
								
							}
						});					
				
				}				
			}
		});
		
		holder.txtView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((BaseActivity)mContext).showProgressBar("Please wait");
				Intent intent = new Intent(mContext, RunningEventActivity.class);
				intent.putExtra("EventId", ed.getEventId());
				intent.putExtra("EventTypeId", Integer.parseInt(ed.getEventTypeId()));
				mContext.startActivity(intent);
				((BaseActivity)mContext).hideProgressBar();	
			}
		});
		return convertView;
	}

	public interface TrackLocationAdapterCallback {
		void refreshTrackingEvents();			
	}
	
	@SuppressWarnings("static-access")
	private String getStartTimeAndTimeLeftText(EventDetail event, AcceptanceStatus acceptanceStatus){	
		String timeInfoTxt = "";
		if(acceptanceStatus == acceptanceStatus.ACCEPTED){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	
			Calendar calendar = Calendar.getInstance();		
			try {
				calendar.setTime(sdf.parse(event.getEndTime()));
				long diffMinutes  = (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/60000;
				String timeLeft = DateUtil.getDurationText(diffMinutes);

				calendar = Calendar.getInstance();

				Date startParsedDate =  sdf.parse(event.getStartTime());			
				calendar.setTime(startParsedDate);
				String startTime = DateUtil.getTime(calendar);

				timeInfoTxt = String.format(mContext.getResources().getString(R.string.track_location_timeinfo_text), 
						startTime, timeLeft);

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		}
		else{
			if (Integer.parseInt(event.getEventTypeId()) ==  100){ // Share my Location
				timeInfoTxt = "Awaiting response to your Share My Location Request";	
			}
			else{
				timeInfoTxt = "Awaiting response to your Track Buddy Request";
			}
			
		}
		return timeInfoTxt;
	}	
}