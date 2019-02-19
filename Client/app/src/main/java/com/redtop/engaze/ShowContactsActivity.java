package com.redtop.engaze;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.redtop.engaze.adapter.MemberAdapter;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.fragment.ContactsFragment;
public class ShowContactsActivity extends BaseActivity implements
        ContactsFragment.ContactsFragmentActionListener {

    ListView listView;
    public MemberAdapter mAdapter;
    public String[] images;
    private ContactsFragment mContactsFragment;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_show_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.friend_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setTitle(R.string.title_friend_list);
            //toolbar.setSubtitle(R.string.title_event);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        mContactsFragment = new ContactsFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.contact_fragment_container, mContactsFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_member, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_member);
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
                mAdapter.filter(searchQuery.toString().trim());
                listView.invalidate();
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
        int id = item.getItemId();
        switch (id) {
            //		case R.id.action_add_member:
            //			inviteFriend();
            //			break;
            case R.id.action_refresh_contactlist:
                mContactsFragment.mSwipeRefreshLayout.setRefreshing(true);
                mContactsFragment.refreshContactList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContactListItemClicked(final ContactOrGroup contact, int position) {
        if (contact.getUserId() != null) {

            AlertDialog.Builder adb = null;
            adb = new AlertDialog.Builder(mContext);

            adb.setTitle("Meet " + contact.getName());
            adb.setMessage(getResources().getString(R.string.message_meetnow_memberlistactivity));
            adb.setIcon(android.R.drawable.ic_dialog_alert);

            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(mContext, TrackLocationActivity.class);
                    i.putExtra("EventTypeId", 6);
                    i.putExtra("meetNowUserID", contact.getUserId());
                    startActivity(i);
                }
            });

            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            adb.show();
        }
    }
}
