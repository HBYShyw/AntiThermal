package com.android.server.blob;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Slog;
import com.android.server.LocalServices;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BlobStoreIdleJobService extends JobService {
    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        AsyncTask.execute(new Runnable() { // from class: com.android.server.blob.BlobStoreIdleJobService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BlobStoreIdleJobService.this.lambda$onStartJob$0(jobParameters);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(JobParameters jobParameters) {
        ((BlobStoreManagerInternal) LocalServices.getService(BlobStoreManagerInternal.class)).onIdleMaintenance();
        jobFinished(jobParameters, false);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        Slog.d(BlobStoreConfig.TAG, "Idle maintenance job is stopped; id=" + jobParameters.getJobId() + ", reason=" + JobParameters.getInternalReasonCodeDescription(jobParameters.getInternalStopReasonCode()));
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void schedule(Context context) {
        ((JobScheduler) context.getSystemService("jobscheduler")).schedule(new JobInfo.Builder(BlobStoreConfig.IDLE_JOB_ID, new ComponentName(context, (Class<?>) BlobStoreIdleJobService.class)).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(BlobStoreConfig.getIdleJobPeriodMs()).build());
        if (BlobStoreConfig.LOGV) {
            Slog.v(BlobStoreConfig.TAG, "Scheduling the idle maintenance job");
        }
    }
}
