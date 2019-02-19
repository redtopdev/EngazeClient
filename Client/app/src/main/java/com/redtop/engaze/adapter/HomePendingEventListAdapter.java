package com.redtop.engaze.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.EventsActivity;
import com.redtop.engaze.HomeActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.DateUtil;

public class HomePendingEventListAdapter extends ArrayAdapter<EventDetail> {
	public List<EventDetail> items;	
	private Context mContext;


	public HomePendingEventListAdapter(Context context, int resourceId,
			List<EventDetail> items) {
		super(context, resourceId, items);		
		this.mContext = context;	
		this.items = items;
	}

	/*private view holder class*/
	private class ViewHolder {	
		TextView txtEventName;
		TextView txtEventStartTime;
		TextView txtInitiator;
		TextView txtAccept;
		TextView txtReject;
		TextView txtView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		EventDetail rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_home_pending_event_list, null);
			holder = new ViewHolder();
			holder.txtEventName = (TextView) convertView.findViewById(R.id.event_name);//  rowItem.getName()
			holder.txtEventStartTime = (TextView)convertView.findViewById(R.id.event_time);
			holder.txtInitiator = (TextView)convertView.findViewById(R.id.event_initiator);
			holder.txtAccept = (TextView)convertView.findViewById(R.id.event_accept);
			holder.txtReject = (TextView)convertView.findViewById(R.id.event_reject);
			holder.txtView = (TextView)convertView.findViewById(R.id.event_view);
			convertView.setTag(holder);

		} else 
			holder = (ViewHolder) convertView.getTag();
		final String eventId = rowItem.getEventId();
		SimpleDateFormat  originalformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date startDate = null;
		try {
			startDate = originalformat.parse(rowItem.getStartTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		String startTime = String.format("%s %s %s %s %s", 
				DateUtil.getDayOfWeek(cal), DateUtil.getDayOfMonth(cal),
				DateUtil.getShortMonth(cal), DateUtil.getYear(cal),
				DateUtil.getTime(cal));
		
		holder.txtEventName.setText(rowItem.getName());
		holder.txtEventStartTime.setText(startTime);
		holder.txtInitiator.setText("from "+ rowItem.GetInitiatorName());
		holder.txtAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomeActivity)mContext).saveEventState(eventId, AcceptanceStatus.ACCEPTED);
			}
		});
		holder.txtReject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomeActivity)mContext).saveEventState(eventId, AcceptanceStatus.DECLINED);				
			}
		});
		if(AppUtility.IsEventShareMyLocationEventForCurrentuser(rowItem, mContext)){
			holder.txtView.setVisibility(View.GONE);
			holder.txtEventName.setText(mContext.getResources().getString(R.string.share_my_location_notification));
		}
		else if(AppUtility.IsEventTrackBuddyEventForCurrentuser(rowItem, mContext)){
			holder.txtView.setVisibility(View.GONE);
			holder.txtEventName.setText(mContext.getResources().getString(R.string.track_my_buddy_text_notification)); 
		}
		else{
			holder.txtView.setVisibility(View.VISIBLE);
			holder.txtView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((BaseActivity)mContext).showProgressBar("Please wait");
					Intent intent = new Intent(mContext, EventsActivity.class);
					intent.putExtra("defaultTab", 1);
					mContext.startActivity(intent);
					((BaseActivity)mContext).hideProgressBar();						
				}
			});			
		}
		return convertView;
	}			
}