package com.rawad.gpstracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import com.intentfilter.androidpermissions.PermissionManager;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rawad on 23-Oct-17.
 */

public class SendLocationTile extends TileService {
    private static final String TAG = SendLocationTile.class.getSimpleName();
    private String PHONE_NUMBER = "00000";

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Context context = getApplicationContext();
        PermissionManager permissionManager = PermissionManager.getInstance(context);
        permissionManager.checkPermissions(Arrays.asList(new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION})
                , new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        Tile tile = getQsTile();
                        tile.setState(Tile.STATE_INACTIVE);
                        tile.updateTile();
                    }

                    @Override
                    public void onPermissionDenied() {
                        Tile tile = getQsTile();
                        tile.setState(Tile.STATE_UNAVAILABLE);
                        tile.updateTile();
                    }
                });
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        if (tile.getState() == Tile.STATE_INACTIVE) {
            Log.d(TAG, "onClick: tile activated");
            scheduleAlarm();
            getQsTile().setState(Tile.STATE_ACTIVE);
        } else {
            Log.d(TAG, "onClick: tile inactive");
            cancelAlarm();
            getQsTile().setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

    // Setup a recurring alarm every hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), GpsSmsAlarmReceiver.class);
        intent.putExtra("phoneNumber", PHONE_NUMBER);
        Log.d(TAG, "scheduleAlarm: phone number: " + PHONE_NUMBER);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GpsSmsAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                TimeUnit.HOURS.toMillis(1), pIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), GpsSmsAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GpsSmsAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
