package com.redtop.engaze;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.fragment.ContactsFragment;
import com.redtop.engaze.utils.AppUtility;

public class AddContactsActivity extends BaseActivity implements
		ContactsFragment.ContactsFragmentActionListener {
	private SearchView mSearchView;

	private ViewGroup mFlowContainer;
	private RelativeLayout mInviteeSection;
	private ImageButton mAddInvitees;
	ArrayList<ContactOrGroup>mAddedMembers;
	ArrayList<Integer>contactGroupPositions = new ArrayList<Integer>();
	private ContactsFragment mContactsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		mContext = this;				
		setContentView(R.layout.activity_add_contacts);
		Toolbar toolbar = (Toolbar) findViewById(R.id.search_contact_toolbar);
		String caller  = getIntent().getStringExtra("caller");
		if(caller!=null && caller.equals(HomeActivity.class.toString())){
			AppUtility.setPrefArrayList("Invitees", null, mContext);
		}
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
			getSupportActionBar().setTitle(R.string.title_select_friends);							
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
		initializeElements();

		mAddedMembers = AppUtility.getPrefArrayList("Invitees", this);
		if(mAddedMembers!=null){
			mInviteeSection.setVisibility(View.VISIBLE);
			mAddInvitees.setVisibility(View.VISIBLE);
			for(ContactOrGroup cg : mAddedMembers){
				createContactLayoutItem(cg);
			}
		}
		else{
			mInviteeSection.setVisibility(View.GONE);
			mAddInvitees.setVisibility(View.GONE);
			mAddedMembers = new ArrayList<ContactOrGroup>();			
		}

		initializeClickEvents();

		mContactsFragment = new ContactsFragment();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.add(R.id.contact_fragment_container, mContactsFragment).commit();
	}

	private void initializeClickEvents() {

		mAddInvitees.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtility.setPrefArrayList("Invitees", mAddedMembers, mContext);				
				Intent intent = new Intent();

				setResult(RESULT_OK, intent);        
				finish();					

			}
		});
	}


	private void initializeElements() {
		mAddInvitees = (ImageButton)findViewById(R.id.img_add_invitees);
		mFlowContainer = (ViewGroup) findViewById(R.id.participant_layout);
		mInviteeSection = (RelativeLayout)findViewById(R.id.invitee_section);
		mAddInvitees = (ImageButton)findViewById(R.id.img_add_invitees);
	}

	private void createContactLayoutItem(ContactOrGroup cg){		
		int childrenCount= mFlowContainer.getChildCount();
		LinearLayout contactLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.contact_item_layout_template, null);

		TextView lblname = (TextView)contactLayout.getChildAt(0);
		lblname.setText(cg.getName());
		lblname.setTag(cg);

		contactLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {			
				mFlowContainer.removeView(v);				
				if(mFlowContainer.getChildCount()==0){
					mInviteeSection.setVisibility(View.GONE);
					mAddInvitees.setVisibility(View.GONE);
				}				
				mAddedMembers.remove((ContactOrGroup)((LinearLayout)v).getChildAt(0).getTag()); 
			}
		});		

		mFlowContainer.addView(contactLayout, childrenCount-1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		getMenuInflater().inflate(R.menu.menu_search, menu);
	
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		//*** setOnQueryTextFocusChangeListener ***
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String searchQuery) {
				mContactsFragment.mAdapter.filter(searchQuery.toString().trim());
				mContactsFragment.mListView.invalidate();
				return true;
			}
		});

		MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Do something when collapsed
				return true;  // Return true to collapse action view
			}

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Do something when expanded
				return true;  // Return true to expand action view
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){	        	
		case R.id.action_add_member_contactlist:
			inviteFriend();
			break;			
		}       
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onContactListItemClicked(ContactOrGroup contact, int position) {
		if(mAddedMembers.size() < 10) {

			if(mAddedMembers.size()==0){
				//removeHintText();
				mAddInvitees.setVisibility(View.VISIBLE);
				mInviteeSection.setVisibility(View.VISIBLE);
			}

			Boolean alreadyAdded = false;
			for (ContactOrGroup cg  : mAddedMembers ){
				if(cg.getUserId().equals(contact.getUserId())){
					alreadyAdded = true;
					break;
				}
			}
			if(!alreadyAdded){
				mAddedMembers.add(contact);
				createContactLayoutItem(contact);
				contactGroupPositions.add(position);
			}
			else
			{
				Toast.makeText(mContext,
						"User is already added", Toast.LENGTH_SHORT).show();
			}


			mSearchView.setQuery("", false);
			mSearchView.clearFocus();
			mContactsFragment.mListView.clearTextFilter();
			mContactsFragment.mAdapter.getFilter().filter("");
		}
		else{
			Toast.makeText(mContext,
					"You have reached maximum limit of participants!", Toast.LENGTH_SHORT).show();
		}
	}
}
