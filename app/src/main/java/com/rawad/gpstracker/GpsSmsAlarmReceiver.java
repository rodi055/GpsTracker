package com.rawad.gpstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Rawad on 31-Oct-17.
 */

public class GpsSmsAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 95412;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SendLocationSms.class);
        context.startService(i);
    }
}