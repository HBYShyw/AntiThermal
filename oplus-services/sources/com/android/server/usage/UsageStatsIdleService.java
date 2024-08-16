package com.android.server.usage;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.usage.UsageStatsManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import com.android.server.LocalServices;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsageStatsIdleService extends JobService {
    private static final String PRUNE_JOB_NS = "usagestats_prune";
    private static final String UPDATE_MAPPINGS_JOB_NS = "usagestats_mapping";
    private static final String USER_ID_KEY = "user_id";

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void schedulePruneJob(Context context, int i) {
        ComponentName componentName = new ComponentName(context.getPackageName(), UsageStatsIdleService.class.getName());
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt(USER_ID_KEY, i);
        scheduleJobInternal(context, new JobInfo.Builder(i, componentName).setRequiresDeviceIdle(true).setExtras(persistableBundle).setPersisted(true).build(), PRUNE_JOB_NS, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void scheduleUpdateMappingsJob(Context context, int i) {
        ComponentName componentName = new ComponentName(context.getPackageName(), UsageStatsIdleService.class.getName());
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt(USER_ID_KEY, i);
        JobInfo.Builder persisted = new JobInfo.Builder(i, componentName).setPersisted(true);
        TimeUnit timeUnit = TimeUnit.DAYS;
        scheduleJobInternal(context, persisted.setMinimumLatency(timeUnit.toMillis(1L)).setOverrideDeadline(timeUnit.toMillis(2L)).setExtras(persistableBundle).build(), UPDATE_MAPPINGS_JOB_NS, i);
    }

    private static void scheduleJobInternal(Context context, JobInfo jobInfo, String str, int i) {
        JobScheduler forNamespace = ((JobScheduler) context.getSystemService(JobScheduler.class)).forNamespace(str);
        if (jobInfo.equals(forNamespace.getPendingJob(i))) {
            return;
        }
        forNamespace.cancel(i);
        forNamespace.schedule(jobInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cancelPruneJob(Context context, int i) {
        cancelJobInternal(context, PRUNE_JOB_NS, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cancelUpdateMappingsJob(Context context, int i) {
        cancelJobInternal(context, UPDATE_MAPPINGS_JOB_NS, i);
    }

    private static void cancelJobInternal(Context context, String str, int i) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        if (jobScheduler != null) {
            jobScheduler.forNamespace(str).cancel(i);
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        final int i = jobParameters.getExtras().getInt(USER_ID_KEY, -1);
        if (i == -1) {
            return false;
        }
        AsyncTask.execute(new Runnable() { // from class: com.android.server.usage.UsageStatsIdleService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UsageStatsIdleService.this.lambda$onStartJob$0(jobParameters, i);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(JobParameters jobParameters, int i) {
        UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        if (UPDATE_MAPPINGS_JOB_NS.equals(jobParameters.getJobNamespace())) {
            jobFinished(jobParameters, !usageStatsManagerInternal.updatePackageMappingsData(i));
        } else {
            jobFinished(jobParameters, !usageStatsManagerInternal.pruneUninstalledPackagesData(i));
        }
    }
}
