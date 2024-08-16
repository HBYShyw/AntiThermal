package com.android.server.people.data;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.CancellationSignal;
import com.android.server.LocalServices;
import com.android.server.people.PeopleServiceInternal;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DataMaintenanceService extends JobService {
    private static final int BASE_JOB_ID = 204561367;
    private static final long JOB_RUN_INTERVAL = TimeUnit.HOURS.toMillis(24);
    private CancellationSignal mSignal;

    private static int getJobId(int i) {
        return i + BASE_JOB_ID;
    }

    private static int getUserId(int i) {
        return i - BASE_JOB_ID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void scheduleJob(Context context, int i) {
        int jobId = getJobId(i);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        if (jobScheduler.getPendingJob(jobId) == null) {
            jobScheduler.schedule(new JobInfo.Builder(jobId, new ComponentName(context, (Class<?>) DataMaintenanceService.class)).setRequiresDeviceIdle(true).setPeriodic(JOB_RUN_INTERVAL).build());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cancelJob(Context context, int i) {
        ((JobScheduler) context.getSystemService(JobScheduler.class)).cancel(getJobId(i));
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        final int userId = getUserId(jobParameters.getJobId());
        this.mSignal = new CancellationSignal();
        new Thread(new Runnable() { // from class: com.android.server.people.data.DataMaintenanceService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DataMaintenanceService.this.lambda$onStartJob$0(userId, jobParameters);
            }
        }).start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(int i, JobParameters jobParameters) {
        ((PeopleServiceInternal) LocalServices.getService(PeopleServiceInternal.class)).pruneDataForUser(i, this.mSignal);
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
