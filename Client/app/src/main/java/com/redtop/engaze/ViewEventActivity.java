package com.redtop.engaze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity ;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ViewEventActivity extends AppCompatActivity  {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	

	    setContentView(R.layout.activity_view_event);
		Toolbar toolbar = (Toolbar) findViewById(R.id.view_event_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
			getSupportActionBar().setTitle(R.string.title_view_event);
			//toolbar.setSubtitle(R.string.title_event);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

}
