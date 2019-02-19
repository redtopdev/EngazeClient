package com.redtop.engaze.fragment;

import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.HomeActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.entity.EventPlace;
import com.redtop.engaze.interfaces.OnGpsSetOnListner;
import com.redtop.engaze.utils.AppUtility;
import com.redtop.engaze.utils.Constants;
import com.redtop.engaze.utils.LocationHelper;
import com.redtop.engaze.utils.MarkerHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowMapFragment.ShowMapFragmentActionListener} interface
 * to handle interaction events.
 */
public class ShowMapFragment extends LocationFragment implements
        OnMapReadyCallback, View.OnClickListener, LocationListener {
    private GoogleMap mMap;
    private ShowMapFragmentActionListener mListener;
    private Context mContext;
    private LatLng mLatlong;
    private ImageButton myLocationButton;
    public ImageView mPin;
    public EventPlace mEventPlace;
    public TextView mEventLocationTextViem;
    protected LocationHelper mLh;
    protected OnGpsSetOnListner gpsOnListner;
    public Boolean isGPSOn = false;
    public LocationManager mLm;
    private View mRootView;
    private ImageView mBackButton;
    private Boolean findAddressOnCameraChange = true;
    public Boolean needLocation = true;
    public Boolean isCameraMovedToMyLocation = false;
    public Boolean findLatLangOnCameraChange = true;
    private Boolean isGPSEnableThreadRun = false;

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)) {
            if (mLatlong == mMyCoordinates) {
                myLocationButton.setImageResource(R.drawable.pointer_on_gps_on);
            } else {
                myLocationButton.setImageResource(R.drawable.gps_on);

            }
            isGPSOn = true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)) {
            isGPSOn = false;
            myLocationButton.setImageResource(R.drawable.gps_off);
        }
    }

    public interface ShowMapFragmentActionListener {
        void onLocationTextClicked(LatLng latLang);
        void onShowMapBackButtonPressed();
    }

    public ShowMapFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_show_map, container, false);
        mBackButton = (ImageView) mRootView.findViewById(R.id.img_show_location_back);
        myLocationButton = (ImageButton) mRootView.findViewById(R.id.img_my_location);
        myLocationButton.setOnClickListener(this);
        mEventLocationTextViem = (TextView) mRootView.findViewById(R.id.txt_location);
        mEventLocationTextViem.setOnClickListener(this);
        mPin = (ImageView) mRootView.findViewById(R.id.img_center_pin);
        mPin.setBackground(MarkerHelper.CreateLocatonPinDrawable(mContext));
        mPin.setVisibility(View.GONE);
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_map);
        mLatlong = new LatLng(Double.longBitsToDouble(AppUtility.getPrefLong("lat", mContext)),
                Double.longBitsToDouble(AppUtility.getPrefLong("long", mContext)));
        fragment.getMapAsync(this);
        mLh = new LocationHelper(mContext, getActivity());
        mLm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        gpsOnListner = null;
        if (getActivity().getClass().getName().equals(HomeActivity.class.getName())) {
            mBackButton.setVisibility(View.GONE);
        }

        return mRootView;
    }

    public void moveToSelectedLocation(EventPlace ep) {
        mLatlong = ep.getLatLang();
        findAddressOnCameraChange = false;
        findLatLangOnCameraChange = false;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatlong, Constants.ZOOM_VALUE));
        mEventLocationTextViem.setText(mEventPlace.getName());
    }

    @Override
    public void onResume() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            mLm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, (android.location.LocationListener) this);
            if (mLm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGPSOn = true;
            } else {
                isGPSOn = false;
            }
        }
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowMapFragmentActionListener) {
            mListener = (ShowMapFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShowMapFragmentActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_show_location_back:
                mListener.onShowMapBackButtonPressed();
                break;

            case R.id.img_my_location:
                if (!AppUtility.isNetworkAvailable(mContext)) {
                    return;
                }

                LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    bringPinToMyLocation();
                } else {
                    checkAndEnableGPS();
                }
                break;
            case R.id.txt_location:
                mListener.onLocationTextClicked(mLatlong);
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0, AppUtility.dpToPx(64, mContext), 0, 0);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // hideAllListViewLayout();
            }
        });

        if (AppUtility.isNetworkAvailable(mContext)) {
            runGPSEnableThread();
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatlong, Constants.ZOOM_VALUE));
        }

        findLatLangOnCameraChange = false;
        initializeMapCameraChangeListner();
        mPin.setVisibility(View.VISIBLE);
    }

    protected void initializeMapCameraChangeListner() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {

                if (findLatLangOnCameraChange) {
                    mLatlong = mMap.getCameraPosition().target;
                } else {
                    findLatLangOnCameraChange = true;
                }
                if (isGPSOn) {
                    if (mMyCoordinates == mLatlong && mMyCoordinates != null && mLatlong != null) {
                        myLocationButton.setImageResource(R.drawable.pointer_on_gps_on);
                        AppUtility.setPrefLong("lat", Double.doubleToLongBits(mLatlong.latitude), mContext);
                        AppUtility.setPrefLong("long", Double.doubleToLongBits(mLatlong.longitude), mContext);
                    } else {
                        myLocationButton.setImageResource(R.drawable.gps_on);
                    }
                }

                try {
                    if (findAddressOnCameraChange) {
                        new CameraChangeGetPlace().execute();
                    } else {
                        findAddressOnCameraChange = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void runGPSEnableThread() {
        isGPSEnableThreadRun = false;
        Thread thread = new Thread() {
            @Override
            public void run() {
                checkAndEnableGPS();
            }
        };
        thread.start();
    }

    protected void bringPinToMyLocation() {
        try {
            //myImageButton.setVisibility(View.GONE);
            Location location = mLh.getMyLocation2(mGoogleApiClient);
            if (location != null) {
                mLatlong = new LatLng(location.getLatitude(), location.getLongitude());
                mMyCoordinates = mLatlong;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatlong, Constants.ZOOM_VALUE));
                findLatLangOnCameraChange = false;
            } else {
                mLatlong = null;
                isCameraMovedToMyLocation = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void checkAndEnableGPS() {

        LocationRequest locReqHighPriority = LocationRequest.create();
        locReqHighPriority.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locReqHighPriority).setAlwaysShow(true);


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //not doing anything because onMyLocationFound will be called and move marker to my location;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(
                                    (BaseActivity) mContext,
                                    CHECK_SETTINGS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
    }

    @Override
    protected void onMyLocationFound(Location location) {

        if (mMap == null) {
            return;//this may call before map is loaded
        }
        synchronized (this) {
            if (!needLocation) {
                return;
            } else {
                needLocation = false;
            }
        }

        mMyCoordinates = new LatLng(location.getLatitude(), location.getLongitude());

        mLatlong = mMyCoordinates;
        if (!isCameraMovedToMyLocation) {
            isCameraMovedToMyLocation = true;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatlong, Constants.ZOOM_VALUE));
            findLatLangOnCameraChange = false;
        }
    }

    protected void createEventPlace() {
        Place place = mLh.getPlaceFromLatLang(mLatlong);
        mEventPlace = null;
        if (place != null) {
            mEventPlace = new EventPlace(place.getName().toString(),
                    place.getAddress().toString(), place.getLatLng());
        }
    }

    private class CameraChangeGetPlace extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                createEventPlace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (mEventPlace == null) {//when network is slow, or google service is down

                turnOnOfLocationAvailabilityMessage(mContext, false);
                // hideProgressBar();
                return;
            }
            turnOnOfLocationAvailabilityMessage(mContext, true);
            mEventLocationTextViem.setText(AppUtility.createTextForDisplay(mEventPlace.getName(), Constants.HOME_ACTIVITY_LOCATION_TEXT_LENGTH));
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }

    protected void turnOnOfLocationAvailabilityMessage(Context context, Boolean locationAvailable) {
        // using the same Internet status layout to display the location unavailability message.
        View v = mRootView.findViewById(R.id.internet_status);
        if (v != null) {

            LinearLayout locationStatusLayout = (LinearLayout) v;
            if (locationAvailable) {
                if (locationStatusLayout != null) {
                    locationStatusLayout.setVisibility(View.GONE);
                }
            } else {
                if (locationStatusLayout != null) {
                    TextView locationAvailabilityTxt = (TextView) mRootView.findViewById(R.id.txt_internet_unavailable_message);
                    locationAvailabilityTxt.setText(getResources().getString(R.string.unable_locate_address));
                    locationStatusLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
