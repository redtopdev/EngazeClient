package com.redtop.engaze.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.redtop.engaze.AddContactsActivity;
import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.ShowContactsActivity;
import com.redtop.engaze.adapter.MemberAdapter;
import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.localbroadcastmanager.LocalBroadcastManager;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.ContactAndGroupListManager;

import java.util.ArrayList;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public ListView mListView;
    public ArrayList<ContactOrGroup> mAllMembers;
    public MemberAdapter mAdapter;
    private View mRootView;
    private Context mContext;
    private LinearLayout mNoContactsLayout;
    public String[] images;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private ContactListBroadCastManager mContactListBroadcastManager;
    private ContactsFragmentActionListener mListener;

    public interface ContactsFragmentActionListener {
        void onContactListItemClicked(ContactOrGroup contact, int position);
    }

    /**
     * Called when the activity is first created.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        if(mContext instanceof ShowContactsActivity){
            mListener = (ShowContactsActivity)mContext;
        }
        else if(getActivity() instanceof AddContactsActivity){
            mListener = (AddContactsActivity)mContext;
        }
        mRootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        mNoContactsLayout = (LinearLayout) mRootView.findViewById(R.id.ll_contacts_help_text);
        mContactListBroadcastManager = new ContactListBroadCastManager(mContext);
        android.support.v4.content.LocalBroadcastManager.getInstance(mContext).registerReceiver(mContactListBroadcastManager, mContactListBroadcastManager.getFilter());
        mListView = (ListView) mRootView.findViewById(R.id.friend_list);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                mListener.onContactListItemClicked((ContactOrGroup) adapter.getItemAtPosition(position), position);
            }
        });




        if (BaseActivity.isFirstTime) {

            if (AppUtility.getPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZED, mContext)) {
                BaseActivity.isFirstTime = false;
                loadMemberList();
                if (!AppUtility.getPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZED, mContext)) {
                    if (AppUtility.getPrefBoolean(Constants.IS_REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED, mContext)) {
                        ContactAndGroupListManager.initializedRegisteredUser(mContext);
                    }
                }
            } else {
                if(null == mSwipeRefreshLayout)
                {
                    mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout_friendlist);
                    mSwipeRefreshLayout.setOnRefreshListener(this);
                }
                mSwipeRefreshLayout.setRefreshing(true);
                if (AppUtility.getPrefBoolean(Constants.IS_CONTACT_LIST_INITIALIZATION_FAILED, mContext)) {
                    ContactAndGroupListManager.cacheContactAndGroupList(mContext);
                }
            }

        } else {
            loadMemberList();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout_friendlist);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return mRootView;
    }

    @Override
    public void onDestroy() {
        android.support.v4.content.LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mContactListBroadcastManager);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        //android.support.v4.content.LocalBroadcastManager.getInstance(mContext).registerReceiver(mContactListBroadcastManager, mContactListBroadcastManager.getFilter());
        super.onResume();
    }

    @Override
    public void onPause() {
        //android.support.v4.content.LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mContactListBroadcastManager);
        super.onPause();
    }


    private void loadMemberList() {
        mAllMembers = ContactAndGroupListManager.getAllContacts(mContext);
        if (mAllMembers == null || mAllMembers.size() == 0) {
            mListView.setVisibility(View.GONE);
            mNoContactsLayout.setVisibility(View.VISIBLE);

        } else {
            mNoContactsLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter = new MemberAdapter(mContext, R.layout.member_list_item, mAllMembers);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void refreshContactList(){
        if(AppUtility.isNetworkAvailable(mContext))
        {

            Thread thread= new Thread(){
                @Override
                public void run(){
                    ContactAndGroupListManager.cacheContactAndGroupList(mContext);
                }
            };
            thread.start();
        }
        else
        {

            Toast.makeText(mContext,
                    getResources().getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        refreshContactList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactsFragmentActionListener) {
            mListener = (ContactsFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ContactsFragmentActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class ContactListBroadCastManager extends LocalBroadcastManager {

        public ContactListBroadCastManager(Context context) {
            super(context);
            initializeFilter();
        }

        private void initializeFilter() {
            mFilter = new IntentFilter();
            mFilter.addAction(Constants.REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED);
            mFilter.addAction(Constants.REGISTERED_CONTACT_LIST_INITIALIZATION_SUCCESS);
            mFilter.addAction(Constants.CONTACT_LIST_INITIALIZATION_SUCCESS);
            mFilter.addAction(Constants.CONTACT_LIST_INITIALIZATION_FAILED);

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String message = intent.getStringExtra("message");
            switch (intent.getAction()) {
                case Constants.REGISTERED_CONTACT_LIST_INITIALIZATION_FAILED:
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (message != null && !message.equals("")) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(mContext,
                            getResources().getString(R.string.message_contacts_errorRetrieveData), Toast.LENGTH_SHORT).show();


                    break;

                case Constants.REGISTERED_CONTACT_LIST_INITIALIZATION_SUCCESS:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case Constants.CONTACT_LIST_INITIALIZATION_SUCCESS:
                    break;
                case Constants.CONTACT_LIST_INITIALIZATION_FAILED:
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (message != null && !message.equals("")) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(mContext,
                            getResources().getString(R.string.message_contacts_errorRetrieveData), Toast.LENGTH_SHORT).show();

                    break;

                default:
                    break;
            }
        }

        public IntentFilter getFilter() {
            return mFilter;
        }

    }
}
