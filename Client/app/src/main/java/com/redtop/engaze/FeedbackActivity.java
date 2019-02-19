package com.redtop.engaze;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.redtop.engaze.app.VolleyAppController;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.EngazeLogReader;

public class FeedbackActivity extends BaseActivity {
	private static final String TAG = "FeedbackActivity";
	private EditText mFeedbacktext ;
	private JSONObject jobj;
	private boolean logcat = false;
	private CharSequence alertTitle;
	private CharSequence feedbackMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_feedback);
		mFeedbacktext = (EditText)findViewById(R.id.txt_feedback);
		Toolbar toolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
			getSupportActionBar().setTitle(R.string.title_feedback);
			//toolbar.setSubtitle(R.string.title_about);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override 
				public void onClick(View v) {
					onBackPressed();
				} 
			}); 


			toolbar.setOnTouchListener(new OnTouchListener() {
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
							logcat = true;
							SaveFeedback();
						}
					}	
					return true;
				}			
			});

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_feedback, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_feedback:
			AppUtility.hideKeyboard(mFeedbacktext, mContext);
			if (!mFeedbacktext.getText().toString().isEmpty()){	
				SaveFeedback();
			}
			else{
				Toast.makeText(getBaseContext(),							
						getResources().getString(R.string.event_invalid_input_message),
						Toast.LENGTH_LONG).show();
			}			
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	protected void SaveFeedback() {
		showProgressBar(getResources().getString(R.string.message_general_progressDialog));
		String JsonPostURL = Constants.MAP_API_URL + Constants.METHOD_SAVE_FEEDBACK;
		createFeedbackJson();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				JsonPostURL, jobj, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				onSaveResponse(response)	;							
			}


		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.toString());			
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.message_feedback_saveFailure), Toast.LENGTH_SHORT).show(); 	                   
				hideProgressBar();
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

	/**
	 * 
	 */
	private void createFeedbackJson() {
		jobj = new JSONObject();								
		try {
			jobj.put("RequestorId", AppUtility.getPref(Constants.LOGIN_ID, mContext));

			if(logcat ){
				jobj.put("Feedback", EngazeLogReader.getLog());
				jobj.put("FeedbackCategory", "Logcat");
				alertTitle = "Logcat Saved!";
				feedbackMessage = "Thanks for sharing your logcat!";
			}else{
				jobj.put("Feedback", mFeedbacktext.getText());
				jobj.put("FeedbackCategory", "General");
				alertTitle = "Feedback Saved!";
				feedbackMessage = getResources().getString(R.string.message_feedback_success);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void onSaveResponse(JSONObject response) {			
		AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(alertTitle);
		alertDialog.setMessage(feedbackMessage);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//dialog.dismiss();
				Intent intent = null;
				intent = new Intent(mContext, HomeActivity.class);					
				startActivity(intent);	
				finish();
			}
		});		
		alertDialog.show();		                   
		hideProgressBar();

	}
}
