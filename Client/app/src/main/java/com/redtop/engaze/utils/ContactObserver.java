package com.redtop.engaze.utils;

import android.content.Context;
import android.database.ContentObserver;

public class ContactObserver extends ContentObserver{
	private Context mContext;
	public ContactObserver( Context context) {
        super(null);
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
    	super.onChange(selfChange);    	
    	//ContactAndGroupListManager.cacheContactList(mContext);       
    }

}
