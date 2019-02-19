package com.redtop.engaze.service;

import java.util.Hashtable;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.redtop.engaze.entity.ContactOrGroup;
import com.redtop.engaze.interfaces.OnRefreshMemberListCompleteListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.ContactAndGroupListManager;
import com.redtop.engaze.utils.EventHelper;
import com.redtop.engaze.utils.InternalCaching;

public class FirstTimeInitializationService extends IntentService {

    private static final String TAG = "RCRefreshService";
    private Context mContext;

    public FirstTimeInitializationService() {
        super(TAG);
        Log.i(TAG, "Constructor RegisteredContactsRefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = this;
        EventHelper.setLocationServiceCheckAlarm(mContext);
        initializeContactList();
    }

    private void initializeContactList() {
        try {
            InternalCaching.initializeCache(mContext);
            ContactAndGroupListManager.cacheContactAndGroupList(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
