package com.rawad.gpstracker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Rawad on 3-Nov-17.
 */
public class SendLocationSms extends IntentService implements LocationProvider.LocationCallback {

    private static final String TAG = SendLocationSms.class.getSimpleName();
    private LocationProvider mLocationProvider;
    private Location lastLocation;

    public SendLocationSms() {
        super("SendLocationSms");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        Context mContext = getApplicationContext();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        Log.d(TAG, "Phone number to send: " + phoneNumber);
        mLocationProvider = new LocationProvider(mContext, this);
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
        Log.d(TAG, "Sending the sms: " + smsBody + " to: " + phoneNumber);
        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
        mLocationProvider.disconnect();
    }

    @Override
    public void handleNewLocation(Location location) {
        lastLocation = location;
    }
}