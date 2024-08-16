package com.android.server.notification;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.CancellationSignal;
import android.util.Slog;
import com.android.server.LocalServices;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationHistoryJobService extends JobService {
    static final int BASE_JOB_ID = 237039804;
    private static final long JOB_RUN_INTERVAL = TimeUnit.MINUTES.toMillis(20);
    private static final String TAG = "NotificationHistoryJob";
    private CancellationSignal mSignal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void scheduleJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        if (jobScheduler.getPendingJob(BASE_JOB_ID) != null || jobScheduler.schedule(new JobInfo.Builder(BASE_JOB_ID, new ComponentName(context, (Class<?>) NotificationHistoryJobService.class)).setRequiresDeviceIdle(false).setPeriodic(JOB_RUN_INTERVAL).build()) == 1) {
            return;
        }
        Slog.w(TAG, "Failed to schedule history cleanup job");
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        this.mSignal = new CancellationSignal();
        new Thread(new Runnable() { // from class: com.android.server.notification.NotificationHistoryJobService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NotificationHistoryJobService.this.lambda$onStartJob$0(jobParameters);
            }
        }).start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(JobParameters jobParameters) {
        ((NotificationManagerInternal) LocalServices.getService(NotificationManagerInternal.class)).cleanupHistoryFiles();
        jobFinished(jobParameters, this.mSignal.isCanceled());
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        CancellationSignal cancellationSignal = this.mSignal;
        if (cancellationSignal == null) {
            return false;
        }
        cancellationSignal.cancel();
        return false;
    }
}
