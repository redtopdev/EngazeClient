package com.redtop.engaze;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.redtop.engaze.service.EventRefreshService;
import com.redtop.engaze.service.FirstTimeInitializationService;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;

public class SplashActivity extends BaseActivity {

	private ProgressDialog mProgress;
    private static Boolean iSUpgrade = true;
	
	@Override
	public void onBackPressed() {
		finish();
	}
	@Override	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		mContext = this;
		AppUtility.setApplicationContext(this.getApplicationContext());
		AppUtility.deviceDensity = getResources().getDisplayMetrics().densityDpi;
		setContentView(R.layout.activity_splash);			
		String loginValue = AppUtility.getPref(Constants.LOGIN_ID, this);
		Intent intent = null;

		if(loginValue != null){				
			String firstTimeUse = AppUtility.getPref("firstTime",  mContext);
			if( firstTimeUse != null && firstTimeUse.equals("true")){
				mProgress = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
				mProgress.setMessage(getResources().getString(R.string.message_home_initialize));
				mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				mProgress.setCancelable(false);
				mProgress.setCanceledOnTouchOutside(false);
				mProgress.setIndeterminate(true);
				mProgress.show();
				AppUtility.setPref("firstTime", "false", mContext);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mProgress.hide();
						//Intent intent = new Intent(getApplicationContext(), Recurrence.class);
						Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
						startActivity(intent);
					}
				}, 3000);

			}
			else
			{
				Boolean test = false;
				test= false;
				if(test){
					intent = new Intent(this, TestActivity.class);
					//intent = new Intent(getApplicationContext(), Recurrence.class);
					startActivity(intent);
				}
				else{
                    if(iSUpgrade){
                        iSUpgrade = false;
                        BaseActivity.isFirstTime = true;
                        Intent registeredContactsRefreshServiceIntent = new Intent(mContext, FirstTimeInitializationService.class);
                        startService(registeredContactsRefreshServiceIntent);
                    }
				
				Intent refreshServiceIntent = new Intent(this, EventRefreshService.class);
				startService(refreshServiceIntent);

				intent = new Intent(this, HomeActivity.class);
				//intent = new Intent(getApplicationContext(), Recurrence.class);
				startActivity(intent);
				}
			}
		}
		else
		{
			String authToken = null;
			authToken = AppUtility.getPref(Constants.USER_AUTH_TOKEN, mContext);

			if(authToken!=null && authToken.equals("1"))
			{
				intent = new Intent(this, ProfileActivity.class);
			}
			else
			{
				intent = new Intent(this, MobileNumberVerificationActivity.class);
			}
			startActivity(intent);

		}
	}
}
