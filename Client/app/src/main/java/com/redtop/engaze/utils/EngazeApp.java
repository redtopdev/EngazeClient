package com.redtop.engaze.utils;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.redtop.engaze.app.VolleyAppController;

/**
 * Created by atulsaga on 2/6/2017.
 */

public class EngazeApp extends VolleyAppController
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static GoogleApiClient mGoogleApiClient;
    public static Context mContext;
    public static EngazeApp app;
    protected String TAG = EngazeApp.class.getName();

    public static GoogleApiClient getGoogleApiClient(){
       if(mGoogleApiClient==null) {
           mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                   .addConnectionCallbacks(app)
                   .addOnConnectionFailedListener(app)
                   .addApi(LocationServices.API)
                   .addApi(Places.GEO_DATA_API)
                   .addApi(Places.PLACE_DETECTION_API).build();
           mGoogleApiClient.connect();
       }
       return  mGoogleApiClient;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
        app = this;
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
