package com.rawad.gpstracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DESTINATION_ADDRESS = "0000000000";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private LocationManager manager;
    private LocationRequest mLocationRequest;
    private double latitude;
    private double longitude;
    private AlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText = (TextView) findViewById(R.id.LatitudeText);
        mLongitudeText = (TextView) findViewById(R.id.LongitudeText);
        pDialog = new AlertDialog.Builder(MainActivity.this).create();
        latitude = 0;
        longitude = 0;
        Context mContext = this;

        manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        checkPermission();
        createLocationRequest();
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        Log.d(TAG, "Main UI code is running!");

    }

    @Override
    protected void onStart() {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } else {
            // Showyourmesg();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } else {
            // Showyourmesg();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            startLocationUpdates();
        } else {
            while (latitude == 0 || longitude == 0) {
                pDialog.setMessage("Getting Location");
                pDialog.show();
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                if (latitude != 0 && longitude != 0) {
                    stopLocationUpdates();
                    pDialog.dismiss();
                    mLatitudeText.setText(String.valueOf(latitude));
                    mLongitudeText.setText(String.valueOf(longitude));
                    Toast.makeText(this, latitude + " " + longitude, Toast.LENGTH_LONG).show();
                    sendLocationViaSms();
                }
            }
        }
    }

    private void sendLocationViaSms() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("http://maps.google.com?q=");
        smsBody.append(mLastLocation.getLatitude());
        smsBody.append(",");
        smsBody.append(mLastLocation.getLongitude());
        smsManager.sendTextMessage(DESTINATION_ADDRESS, null, smsBody.toString(), null, null);
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
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