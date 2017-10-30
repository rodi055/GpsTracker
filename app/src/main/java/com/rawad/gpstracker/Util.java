package com.rawad.gpstracker;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Rawad on 30-Oct-17.
 */

public class Util {

    private static final String TAG = Util.class.getSimpleName();
    private void scheduleJob(Context context, JobScheduler jobScheduler) {
        ComponentName serviceComponent = new ComponentName(context, SendGpsJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(3 * 1000); // wait at least
        builder.setOverrideDeadline(5 * 1000); // maximum delay
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
    public static int scheduleJob(Context context) {
        Log.d(TAG, "scheduleJob: ");
        ComponentName serviceComponent = new ComponentName(context, SendGpsJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency( 1*60 * 60 * 1000); // wait at least
        builder.setOverrideDeadline(1* 60 * 60 * 1000 +10000); // maximum delay
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        JobInfo jobInfo = builder.build();
        jobScheduler.schedule(jobInfo);
        return jobInfo.getId();
    }

    public static void sendLocationViaSms(String destination, String smsBody) {
        Log.d(TAG, "sendLocationViaSms $$$$$$$$$$$$ ");
        //ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.SEND_SMS}, 1);
        SmsManager smsManager = SmsManager.getDefault();

        //smsManager.sendTextMessage(destination, null, smsBody.toString(), null, null);
    }
}
