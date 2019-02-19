package com.redtop.engaze.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.adapter.NameImageAdapter;
import com.redtop.engaze.entity.EventDetail;
import com.redtop.engaze.entity.EventMember;
import com.redtop.engaze.entity.NameImageItem;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants.AcceptanceStatus;
import com.redtop.engaze.utils.EtaDistanceAlertHelper;
import com.redtop.engaze.utils.EventHelper;
import com.redtop.engaze.utils.InternalCaching;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class RunningEventMenuOptionsFragment extends DialogFragment implements OnItemClickListener {

    protected ArrayList<NameImageItem> mUserMenuItems;
    private String mEventId;
    private String mUserName;
    private String mUserId;
    private String mobileno;
    private EventDetail mEvent;
    private EventMember member;
    private Context mContext;

    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_running_event_menu_options, container, false);
        mContext = getActivity();
        mUserName = getArguments().getString("UserName");
        mUserId = getArguments().getString("UserId");
        mEventId = getArguments().getString("EventId");

        mEvent = InternalCaching.getEventFromCache(mEventId, mContext);
        member = mEvent.getMember(mUserId);
        mobileno = member.getMobileNumber();
        Integer acceptanceStatusId = getArguments().getInt("AcceptanceStatus", 0);
        AcceptanceStatus status = AcceptanceStatus.getStatus(acceptanceStatusId);
        mUserMenuItems = new ArrayList<NameImageItem>();

        String[] userOptions = getResources().getStringArray(R.array.running_event_user_options);
        TypedArray images = getResources().obtainTypedArray(R.array.running_event_user_options_image);
        for (int i = 0; i < userOptions.length; i++) {
            NameImageItem item = new NameImageItem(images.getResourceId(i, -1), userOptions[i], i);
            mUserMenuItems.add(item);
        }
        if (status == AcceptanceStatus.DECLINED || status == AcceptanceStatus.PENDING) {
            mUserMenuItems.remove(1);
        } else {
            mUserMenuItems.remove(0);
        }

        NameImageAdapter adapter = new NameImageAdapter(mContext,
                R.layout.item_name_image_row, mUserMenuItems);
        ListView listView = (ListView) rootView.findViewById(R.id.user_menu_options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        View view = ((BaseActivity) mContext).getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        lp.y = AppUtility.dpToPx(70, mContext);
        ((BaseActivity) mContext).getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        NameImageItem item = (NameImageItem) arg0.getItemAtPosition(position);
        switch (item.getImageIndex()) {
            case 0:
                onUserLocationItemMenuItemPokeClicked();
                break;

            case 1:
                onUserLocationItemMenuItemAlertClicked();
                break;
            case 2:
                onUserLocationItemMenuItemWhatsappClicked();
                break;

            case 3:
                onUserLocationItemMenuItemCallClicked();
                break;
            default:
                break;
        }
    }

    private void onUserLocationItemMenuItemWhatsappClicked() {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {
            Uri uri = Uri.parse("smsto:" + mobileno);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.putExtra("sms_body", "Your text here!");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } else {
            Toast.makeText(mContext, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    private void onUserLocationItemMenuItemCallClicked() {
        // TODO Auto-generated method stub
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobileno));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mContext.startActivity(callIntent);
    }

    public void onUserLocationItemMenuItemAlertClicked() {

        EtaDistanceAlertHelper etaHelper = new EtaDistanceAlertHelper(mContext, mEventId, mUserName, mUserId);
        etaHelper.showSetAlertDialog();
    }

    public void onUserLocationItemMenuItemPokeClicked() {

        EventHelper.pokeParticipant(mUserId, mUserName, mEventId, mContext);
    }
}
