package com.rawad.gpstracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SendLocationSms extends Service {
    LocationAlarmSender alarm = new LocationAlarmSender();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
