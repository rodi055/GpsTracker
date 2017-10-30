package com.rawad.gpstracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.intentfilter.androidpermissions.PermissionManager;

import java.util.Arrays;

/**
 * Created by Rawad on 31-Oct-17.
 */

public class CurrentLocation implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DESTINATION_ADDRESS = "0000000000";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager manager;
    private LocationRequest mLocationRequest;
    private double latitude;
    private double longitude;

    public CurrentLocation(Context mContext) {
        manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        PermissionManager permissionManager = PermissionManager.getInstance(mContext);
        permissionManager.checkPermissions(Arrays.asList(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                createLocationRequest();
            }

            @Override
            public void onPermissionDenied() {

            }
        });

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        Log.d(TAG, "Main UI code is running!");
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            startLocationUpdates();
        } else {
            while (latitude == 0 || longitude == 0) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                if (latitude != 0 && longitude != 0) {
                    stopLocationUpdates();

                    StringBuffer smsBody = new StringBuffer();
                    smsBody.append("http://maps.google.com?q=");
                    smsBody.append(mLastLocation.getLatitude());
                    smsBody.append(",");
                    smsBody.append(mLastLocation.getLongitude());
                    Util.sendLocationViaSms(DESTINATION_ADDRESS, smsBody.toString());
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("", "LocatinChngListner, loc: " + location.getLatitude() + "," + location.getLongitude());
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            } else if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
    }


    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(1 * 1000);
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }
}
