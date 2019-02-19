package com.redtop.engaze.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.app.VolleyAppController;
import com.redtop.engaze.customeviews.CircularImageView;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;

public class ContactOrGroupListAdapter extends ArrayAdapter<ContactOrGroup> {

	private Context mContext;
	List<ContactOrGroup> rowItems;
	List<ContactOrGroup> list ;
	private JSONObject mInviteJasonObj;
	public ContactOrGroupListAdapter(Context context, int resource,
			List<ContactOrGroup> data) {
		super(context, resource, data);
		this.mContext = context;
		this.rowItems = data;
		list =  new ArrayList<ContactOrGroup>();
		list.addAll(rowItems);
	}

	/*private view holder class*/
	private class ViewHolder {
		CircularImageView imageView;
		TextView txtName;
		TextView btnInvite;

	}

	public void setSelected(int position) {
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final ContactOrGroup rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_contact_group_list, null);
			holder = new ViewHolder();

			holder.txtName = (TextView) convertView.findViewById(R.id.contact_name);
			holder.imageView = (CircularImageView) convertView.findViewById(R.id.contact_icon);
			holder.btnInvite = (TextView)convertView.findViewById(R.id.invite_member);
			convertView.setTag(holder);

		} else 
			holder = (ViewHolder) convertView.getTag();

		holder.txtName.setText(rowItem.getName());
		holder.imageView.setBackground(rowItem.getImageDrawable(mContext));
		holder.btnInvite.setVisibility(View.VISIBLE);

		if((rowItem.getUserId()!= null && rowItem.getUserId()!="") || rowItem.getGroupId()!=0)
		{
			holder.btnInvite.setVisibility(View.GONE);	
		}				

		holder.btnInvite.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				((BaseActivity)mContext).showProgressBar(mContext.getResources().getString(R.string.message_general_progressDialog));
				String num = rowItem.getMobileNumbers().get(0);
				mInviteJasonObj = createInvitationJson(num.toString());
				SendInvite();

			}
		});

		//holder.imageView.setImageResource(rowItem.getGroupId()());			

		return convertView;
	}

	private JSONObject createInvitationJson(String numberForInvite)
	{
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("ContactNumberForInvite", numberForInvite);
			jobj.put("RequestorId",AppUtility.getPref(Constants.LOGIN_ID, mContext));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobj;
	}


	private void SendInvite() {			 

		//showProgressBar();

		String JsonPostURL = Constants.MAP_API_URL + "Contacts/InviteContact";		

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				JsonPostURL, mInviteJasonObj, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {				
				((BaseActivity)mContext).hideProgressBar();

				Toast.makeText(mContext,
						"Invitation Sent",
						Toast.LENGTH_SHORT).show();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				((BaseActivity)mContext).hideProgressBar();
				Toast.makeText(mContext,
						"Failed to send invite. Please try agan later.", Toast.LENGTH_SHORT).show(); 	                   
				//hideProgressBar();
			}
		})
		{
			@Override
			public String getBodyContentType() {
				return "application/json; charset=utf-8";
			}
		};
		jsonObjReq.setRetryPolicy((RetryPolicy) new DefaultRetryPolicy(Constants.DEFAULT_SHORT_TIME_TIMEOUT, 
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		VolleyAppController.getInstance().addToRequestQueue(jsonObjReq); 
	}

	public void filter(String charText) {

		charText = charText.toLowerCase(Locale.getDefault());

		rowItems.clear();
		if (charText.length() == 0) {
			rowItems.addAll(list);

		} else {
			for (ContactOrGroup postDetail : list) {
				if (charText.length() != 0 && postDetail.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
					rowItems.add(postDetail);
				} 
			}
		}
		notifyDataSetChanged();
	}
}