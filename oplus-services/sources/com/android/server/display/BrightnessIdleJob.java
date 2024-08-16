package com.android.server.display;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.display.DisplayManagerInternal;
import android.util.Slog;
import com.android.server.LocalServices;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BrightnessIdleJob extends JobService {
    private static final int JOB_ID = 3923512;

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public static void scheduleJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        JobInfo pendingJob = jobScheduler.getPendingJob(JOB_ID);
        JobInfo build = new JobInfo.Builder(JOB_ID, new ComponentName(context, (Class<?>) BrightnessIdleJob.class)).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(TimeUnit.HOURS.toMillis(24L)).build();
        if (pendingJob != null && !pendingJob.equals(build)) {
            jobScheduler.cancel(JOB_ID);
            pendingJob = null;
        }
        if (pendingJob == null) {
            jobScheduler.schedule(build);
        }
    }

    public static void cancelJob(Context context) {
        ((JobScheduler) context.getSystemService(JobScheduler.class)).cancel(JOB_ID);
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        if (BrightnessTracker.DEBUG) {
            Slog.d("BrightnessTracker", "Scheduled write of brightness events");
        }
        ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class)).persistBrightnessTrackerState();
        return false;
    }
}
