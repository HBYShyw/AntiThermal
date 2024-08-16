package com.android.server;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Slog;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MountServiceIdler extends JobService {
    private static final String SUPER_POWERSAVE_MODE_STATE = "super_powersave_mode_state";
    private static final String TAG = "MountServiceIdler";
    private Runnable mFinishCallback = new Runnable() { // from class: com.android.server.MountServiceIdler.1
        @Override // java.lang.Runnable
        public void run() {
            Slog.i(MountServiceIdler.TAG, "Got mount service completion callback");
            synchronized (MountServiceIdler.this.mFinishCallback) {
                if (MountServiceIdler.this.mStarted) {
                    MountServiceIdler mountServiceIdler = MountServiceIdler.this;
                    mountServiceIdler.jobFinished(mountServiceIdler.mJobParams, false);
                    MountServiceIdler.this.mStarted = false;
                }
            }
            MountServiceIdler.scheduleIdlePass(MountServiceIdler.this);
        }
    };
    private JobParameters mJobParams;
    private boolean mStarted;
    private static ComponentName sIdleService = new ComponentName("android", MountServiceIdler.class.getName());
    private static int MOUNT_JOB_ID = 808;

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        try {
            ActivityManager.getService().performIdleMaintenance();
        } catch (RemoteException unused) {
        }
        this.mJobParams = jobParameters;
        StorageManagerService storageManagerService = StorageManagerService.sSelf;
        boolean z = Settings.System.getIntForUser(getApplicationContext().getContentResolver(), SUPER_POWERSAVE_MODE_STATE, 0, 0) == 1;
        if (storageManagerService != null && !z) {
            synchronized (this.mFinishCallback) {
                this.mStarted = true;
            }
            storageManagerService.runIdleMaint(this.mFinishCallback);
        }
        return storageManagerService != null;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        StorageManagerService storageManagerService = StorageManagerService.sSelf;
        if (storageManagerService != null) {
            storageManagerService.abortIdleMaint(this.mFinishCallback);
            synchronized (this.mFinishCallback) {
                this.mStarted = false;
            }
        }
        return false;
    }

    public static void scheduleIdlePass(Context context) {
        long currentTimeMillis;
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        long timeInMillis = offsetFromTodayMidnight(0, 3).getTimeInMillis();
        long timeInMillis2 = offsetFromTodayMidnight(0, 4).getTimeInMillis();
        long timeInMillis3 = offsetFromTodayMidnight(1, 3).getTimeInMillis();
        if (System.currentTimeMillis() > timeInMillis && System.currentTimeMillis() < timeInMillis2) {
            currentTimeMillis = TimeUnit.SECONDS.toMillis(10L);
        } else {
            currentTimeMillis = timeInMillis3 - System.currentTimeMillis();
        }
        JobInfo.Builder builder = new JobInfo.Builder(MOUNT_JOB_ID, sIdleService);
        builder.setRequiresDeviceIdle(true);
        builder.setRequiresBatteryNotLow(true);
        builder.setRequiresCharging(true);
        builder.setMinimumLatency(currentTimeMillis);
        jobScheduler.schedule(builder.build());
    }

    private static Calendar offsetFromTodayMidnight(int i, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, i2);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(5, i);
        return calendar;
    }
}
