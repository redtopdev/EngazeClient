package com.redtop.engaze;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.redtop.engaze.interfaces.OnActionCancelledListner;
import com.redtop.engaze.interfaces.OnActionCompleteListner;
import com.redtop.engaze.interfaces.OnActionFailedListner;
import com.redtop.engaze.utils.UserMessageHandler;
import com.redtop.engaze.utils.Constants.Action;


@SuppressWarnings("deprecation")
@SuppressLint("ResourceAsColor")
public class ActionSuccessFailMessageHandler  extends AppCompatActivity implements OnActionFailedListner, OnActionCompleteListner, OnActionCancelledListner   {
	private ProgressDialog mDialog;
	public Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mDialog ==null){
			mDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		}
	}
	@Override
	public void actionFailed(String msg, Action action) {
		if(msg ==null){ 
			msg=UserMessageHandler.getFailureMessage(action, mContext);
		}

		hideProgressBar();
		Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
	}

	public void showProgressBar(String message ){
		showProgressBar("",message);
	}

	public void showProgressBar(String title, String message ){

		if(!(title==null || title.equals(""))){
			mDialog.setTitle(title);
		}
		mDialog.setMessage(message);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setIndeterminate(true);
		mDialog.show();
	}	

	public void hideProgressBar(){
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
	}

	@Override
	public void actionComplete(Action action) {
		String msg=UserMessageHandler.getSuccessMessage(action, mContext);
		hideProgressBar();
		Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void actionCancelled(Action action){
	}
}
