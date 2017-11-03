package com.rawad.gpstracker;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;

import java.util.Arrays;

public class SendLocationSms extends IntentService implements LocationProvider.LocationCallback {

    private static final String TAG = SendLocationSms.class.getSimpleName();
    private LocationProvider mLocationProvider;
    private Location lastLocation;

    public SendLocationSms() {
        super("SendLocationSms");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: %%%%%%%%");
        Context mContext = getApplicationContext();
        final String phoneNumber = intent.getStringExtra("phoneNumber");
        mLocationProvider = new LocationProvider(mContext, this);
        PermissionManager permissionManager = PermissionManager.getInstance(mContext);
        permissionManager.checkPermissions(Arrays.asList(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}),
                new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        mLocationProvider.connect();
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!mLocationProvider.isConnected()) {
                            Log.d(TAG, "Not connected to location provider");
                            return;
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        String smsBody = "Location: lat - " + lastLocation.getLatitude() + " long - " + lastLocation.getLongitude();
                        Log.d(TAG, "Sending the sms: " + smsBody);
                        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
                        mLocationProvider.disconnect();
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void handleNewLocation(Location location) {
        lastLocation = location;
    }
}