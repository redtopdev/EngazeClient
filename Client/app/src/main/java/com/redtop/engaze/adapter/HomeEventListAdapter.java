package com.redtop.engaze.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redtop.engaze.R;
import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.entity.EventDetail;

public class HomeEventListAdapter extends RecyclerView.Adapter<HomeEventListAdapter.EventNameHolder> {
	public List<EventDetail> items;	
	private static Context mContext;
	private static String TAG = HomeEventListAdapter.class.getName();  

	public HomeEventListAdapter(
			List<EventDetail> items, Context context) {
		mContext = context;
		this.items = items;
	}

	@Override
	public void onBindViewHolder(final EventNameHolder viewHolder, final int i) {
		final EventDetail ed = items.get(i);
		String title = ed.getName();
		title = title.substring(0,1).toUpperCase() + title.substring(1);
		viewHolder.txtEventTile.setText(title); 
		viewHolder.ll_cv_user_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, RunningEventActivity.class);
				intent.putExtra("EventId", ed.getEventId());
				mContext.startActivity(intent);

			}
		});
	}

	@Override
	public EventNameHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		View itemView = LayoutInflater.
				from(viewGroup.getContext()).
				inflate(R.layout.item_home_event_list, viewGroup, false);		
		return new EventNameHolder(itemView);
	}

	public class EventNameHolder extends RecyclerView.ViewHolder {
		public EventNameHolder(View itemView) {
			super(itemView);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				CardView view = (CardView) itemView;
				view.setCardBackgroundColor(Color.TRANSPARENT);
				view.setCardElevation(0);
			} else {
				itemView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
			}

			this.txtEventTile = (TextView) itemView.findViewById(R.id.txt_event_name);
			this.ll_cv_user_location = (LinearLayout)itemView.findViewById(R.id.ll_cv_user_location);
		}

		public TextView txtEventTile;
		public LinearLayout  ll_cv_user_location;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return items.size();
	}		
}