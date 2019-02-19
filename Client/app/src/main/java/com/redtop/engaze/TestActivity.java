package com.redtop.engaze;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.redtop.engaze.utils.Constants;

public class TestActivity extends Activity {
	protected int progressStatus = 0;
	protected Handler handler = new Handler();
	protected int refreshDuration ;
	protected ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		// Get the widgets reference from XML layout
		final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
		final Button btn = (Button) findViewById(R.id.btn);

		pb = (ProgressBar) findViewById(R.id.pb);		
		refreshDuration = Constants.LOCATION_REFRESH_INTERVAL_FAST;

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startProgressBar();
			}
		});
	}

	
	private void startProgressBar(){
		final int sleepTime = (refreshDuration)/100;
		progressStatus = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(progressStatus < 100){
					// Update the progress status
					progressStatus +=1;

					// Try to sleep the thread for 20 milliseconds
					try{
						Thread.sleep(sleepTime);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					pb.setProgress(progressStatus);					
				}
			}
		}).start(); // Start the operation
	}
}
