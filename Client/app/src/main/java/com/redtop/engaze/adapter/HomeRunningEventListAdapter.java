package com.redtop.engaze.adapter;

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
import com.redtop.engaze.R;
import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.interfaces.OnActionCompleteListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.Constants.Action;
import com.redtop.engaze.utils.EventManager;

public class HomeRunningEventListAdapter extends ArrayAdapter<EventDetail> {
	public List<EventDetail> items;	
	private Context mContext;
	private RunningEventAdapterCallback callback;


	public HomeRunningEventListAdapter(Context context, int resourceId,
			List<EventDetail> items) {
		super(context, resourceId, items);		
		this.mContext = context;	
		this.items = items;
		this.callback = ((RunningEventAdapterCallback) context);
	}

	/*private view holder class*/
	private class ViewHolder {	
		TextView txtEventName;		
		TextView txtInitiator;
		TextView txtLeave;
		TextView txtEnd;
		TextView txtView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		final EventDetail rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_home_running_event_list, null);
			holder = new ViewHolder();
			holder.txtEventName = (TextView) convertView.findViewById(R.id.event_name);//  rowItem.getName()		
			holder.txtInitiator = (TextView)convertView.findViewById(R.id.event_initiator);
			holder.txtLeave = (TextView)convertView.findViewById(R.id.event_leave);
			holder.txtEnd = (TextView)convertView.findViewById(R.id.event_end);
			holder.txtView = (TextView)convertView.findViewById(R.id.event_view);
			convertView.setTag(holder);

		} else 
			holder = (ViewHolder) convertView.getTag();
		rowItem.getEventId();

		if(AppUtility.isCurrentUserInitiator(rowItem.getInitiatorId(), mContext)){
			holder.txtLeave.setVisibility(View.GONE);
			holder.txtEnd.setVisibility(View.VISIBLE);
		}
		else{
			holder.txtEnd.setVisibility(View.GONE);
			holder.txtLeave.setVisibility(View.VISIBLE);
		}

		holder.txtEventName.setText(rowItem.getName());		
		holder.txtInitiator.setText("from "+ rowItem.GetInitiatorName());

		holder.txtLeave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((BaseActivity)mContext).showProgressBar("Please wait");
				EventManager.leaveEvent(mContext, rowItem, new OnActionCompleteListner() {

					@Override
					public void actionComplete(Action action) {
						rowItem.getCurrentMember().setAcceptanceStatus(AcceptanceStatus.DECLINED);
						if(callback!=null){
							callback.onEventLeaveClicked();
						}
						((BaseActivity)mContext).hideProgressBar();
					}
				}, (BaseActivity)mContext);					
			}
		});

		holder.txtEnd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((BaseActivity)mContext).showProgressBar("Please wait");
				EventManager.endEvent(mContext, rowItem, new OnActionCompleteListner() {
					@Override
					public void actionComplete(Action action) {
						if(callback!=null){
							callback.onEventEndClicked();
						}
						((BaseActivity)mContext).hideProgressBar();						
					}
				},(BaseActivity)mContext);				
			}
		});

		holder.txtView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((BaseActivity)mContext).showProgressBar("Please wait");
				Intent intent = new Intent(mContext, RunningEventActivity.class);
				intent.putExtra("EventId", rowItem.getEventId());
				mContext.startActivity(intent);
				((BaseActivity)mContext).hideProgressBar();	
			}
		});
		return convertView;
	}

	public static interface RunningEventAdapterCallback {
		void onEventEndClicked();
		void onEventLeaveClicked();
	}
}