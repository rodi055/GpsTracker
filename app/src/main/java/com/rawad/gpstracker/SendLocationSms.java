package com.rawad.gpstracker;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;

import static java.util.Collections.singleton;

public class SendLocationSms extends IntentService {

    private static final String TAG = SendLocationSms.class.getSimpleName();

    public SendLocationSms() {
        super("SendLocationSms");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: %%%%%%%%");
        Context context = getApplicationContext();
        PermissionManager permissionManager = PermissionManager.getInstance(context);
        permissionManager.checkPermissions(singleton(Manifest.permission.SEND_SMS), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("123456", null, "sdfsdfsdfsd", null, null);
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

}