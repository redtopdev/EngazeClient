package com.redtop.engaze.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redtop.engaze.R;
import com.redtop.engaze.RunningEventActivity;
import com.redtop.engaze.customeviews.CircularImageView;
import com.redtop.engaze.entity.EventMember;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.EventHelper;

@SuppressLint("SimpleDateFormat")
public class CustomParticipantsInfoList extends BaseAdapter{

	private Context context;	
	private final ArrayList<EventMember> eventMembers;
	private String initiatorID;
	private String source;
	private String eventId;
	//private final Integer[] imageId;
	private static LayoutInflater inflater=null;
	public CustomParticipantsInfoList(Context context,
			ArrayList<EventMember> arrayList, String initiatorID, String eventId, String source) {
		//super(context, R.layout.event_participant_listitem, arrayList);
		this.context = context;
		this.eventMembers = arrayList;
		this.initiatorID = initiatorID;
		this.source = source;
		this.eventId = eventId;		
		inflater = ( LayoutInflater )context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public class Holder
	{
		TextView tv;
		CircularImageView img_profile;
		ImageView img_call;
		//ImageView img_sms;
		RelativeLayout rl_parent;		
		ImageView img_status;
		ImageView img_poke;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		Holder holder=new Holder();
		View rowView; 

		final EventMember member = eventMembers.get(position);
		final String mobileno = member.getMobileNumber();


		rowView = inflater.inflate(R.layout.item_event_participant_list, null);
		holder.tv=(TextView) rowView.findViewById(R.id.txt_participant);
		holder.img_profile=(CircularImageView) rowView.findViewById(R.id.img_participant);       
		holder.rl_parent = (RelativeLayout)rowView.findViewById(R.id.rl_participant);
		//holder.img.setImageResource(imageId[position]);  
		holder.img_call = (ImageView) rowView.findViewById(R.id.img_call);
		//holder.img_sms = (ImageView) rowView.findViewById(R.id.img_sms);		
		holder.img_status = (ImageView)rowView.findViewById(R.id.img_status); 
		holder.img_poke = (ImageView)rowView.findViewById(R.id.img_poke); 

		if(source != null && source.equals(RunningEventActivity.class.getName())){			
			holder.img_profile.setBackground(member.getContact().getImageDrawable(context));
			holder.img_status.setVisibility(View.GONE);
		}
		else{
			holder.img_profile.setVisibility(View.GONE);
			
			holder.img_status.setVisibility(View.VISIBLE);
			if(member.getAcceptanceStatus()==AcceptanceStatus.ACCEPTED)
			{
				AppUtility.setRippleDrawable(holder.img_status,context, R.drawable.ripple_lightgreen);				
				//holder.img_status.setBackground(context.getResources().getDrawable(R.drawable.ic_check_green_48));
			}
			else if(member.getAcceptanceStatus()==AcceptanceStatus.DECLINED)
			{
				AppUtility.setRippleDrawable(holder.img_status,context, R.drawable.ripple_red);
				//holder.img_status.setBackground(context.getResources().getDrawable(R.drawable.ic_decline_red_48));
			}
			else
			{
				AppUtility.setRippleDrawable(holder.img_status,context, R.drawable.ripple_amber);
				//holder.ll_status.setBackgroundColor(context.getResources().getColor(R.color.event_pending));
				//holder.img_status.setBackground(context.getResources().getDrawable(R.drawable.ic_exclam));
			}
		}
		if(AppUtility.isParticipantCurrentUser(member.getUserId(), context))
		{			
			holder.img_call.setVisibility(View.GONE);
			//holder.img_sms.setVisibility(holder.img_sms.GONE);
			holder.tv.setText("You");
			holder.img_poke.setVisibility(View.GONE);
		}
		else
		{ 
			final String participanName = member.getProfileName();			

			holder.tv.setText(participanName);			

			if(member.getAcceptanceStatus()!=AcceptanceStatus.ACCEPTED){
				holder.img_poke.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						EventHelper.pokeParticipant(member.getUserId(),member.getProfileName(), eventId, context);
					}
				});
			}
			else{
				holder.img_poke.setVisibility(View.GONE);
			}			

			holder.img_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + mobileno));
					context.startActivity(callIntent);
				}
			});
		}

		return rowView;
	}

	protected void sendSMSMessage(String mobileno, Editable editable) {
		// TODO Auto-generated method stub
		//Log.i("Send SMS", "");

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(mobileno, null, editable.toString(), null, null);
			Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
		} 

		catch (Exception e) {
			Toast.makeText(context, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	protected void showSMSInputDialog(final String mobileno) {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View promptView = layoutInflater.inflate(R.layout.input_sms_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder.setCancelable(false)
		.setPositiveButton("Send", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				sendSMSMessage(mobileno, editText.getText());
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventMembers.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}