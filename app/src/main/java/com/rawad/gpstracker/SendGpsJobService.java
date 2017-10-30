package com.rawad.gpstracker;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class SendGpsJobService extends JobService {

    private static final String TAG = SendGpsJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: start service ");
        Util.sendLocationViaSms("0542881420", "hii");
       // Util.scheduleJob(getBaseContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return true;
    }
}
