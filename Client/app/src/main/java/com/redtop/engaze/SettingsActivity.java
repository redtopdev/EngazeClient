package com.redtop.engaze;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatActivity{

	ListView listView;
	public  String[] settingNames ;
	public String[]images ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
			getSupportActionBar().setTitle(R.string.title_settings);
			//toolbar.setSubtitle(R.string.title_event);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
		
		findViewById(R.id.txt_event_settings).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingsActivity.this, EventSettingsActivity.class));
			}
		});




		// TODO Auto-generated method stub
	}

}
