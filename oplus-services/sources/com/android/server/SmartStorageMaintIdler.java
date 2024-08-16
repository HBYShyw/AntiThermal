package com.android.server;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Slog;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SmartStorageMaintIdler extends JobService {
    private static final int SMART_MAINT_JOB_ID = 2808;
    private static final ComponentName SMART_STORAGE_MAINT_SERVICE = new ComponentName("android", SmartStorageMaintIdler.class.getName());
    private static final String TAG = "SmartStorageMaintIdler";
    private final Runnable mFinishCallback = new Runnable() { // from class: com.android.server.SmartStorageMaintIdler.1
        @Override // java.lang.Runnable
        public void run() {
            Slog.i(SmartStorageMaintIdler.TAG, "Got smart storage maintenance service completion callback");
            if (SmartStorageMaintIdler.this.mStarted) {
                SmartStorageMaintIdler smartStorageMaintIdler = SmartStorageMaintIdler.this;
                smartStorageMaintIdler.jobFinished(smartStorageMaintIdler.mJobParams, false);
                SmartStorageMaintIdler.this.mStarted = false;
            }
            SmartStorageMaintIdler.scheduleSmartIdlePass(SmartStorageMaintIdler.this, StorageManagerService.sSmartIdleMaintPeriod);
        }
    };
    private JobParameters mJobParams;
    private boolean mStarted;

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        this.mJobParams = jobParameters;
        StorageManagerService storageManagerService = StorageManagerService.sSelf;
        if (storageManagerService != null) {
            this.mStarted = true;
            storageManagerService.runSmartIdleMaint(this.mFinishCallback);
        }
        return storageManagerService != null;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        this.mStarted = false;
        return false;
    }

    public static void scheduleSmartIdlePass(Context context, int i) {
        StorageManagerService storageManagerService = StorageManagerService.sSelf;
        if (storageManagerService == null || storageManagerService.isPassedLifetimeThresh()) {
            return;
        }
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        long millis = TimeUnit.MINUTES.toMillis(i);
        JobInfo.Builder builder = new JobInfo.Builder(2808, SMART_STORAGE_MAINT_SERVICE);
        builder.setMinimumLatency(millis);
        jobScheduler.schedule(builder.build());
    }
}
