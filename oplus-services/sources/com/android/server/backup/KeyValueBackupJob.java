package com.android.server.backup;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseLongArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Random;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class KeyValueBackupJob extends JobService {
    private static final long MAX_DEFERRAL = 86400000;

    @VisibleForTesting
    public static final int MAX_JOB_ID = 52418896;

    @VisibleForTesting
    public static final int MIN_JOB_ID = 52417896;
    private static final String TAG = "KeyValueBackupJob";
    private static final String USER_ID_EXTRA_KEY = "userId";
    private static ComponentName sKeyValueJobService = new ComponentName("android", KeyValueBackupJob.class.getName());

    @GuardedBy({"KeyValueBackupJob.class"})
    private static final SparseBooleanArray sScheduledForUserId = new SparseBooleanArray();

    @GuardedBy({"KeyValueBackupJob.class"})
    private static final SparseLongArray sNextScheduledForUserId = new SparseLongArray();

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public static void schedule(int i, Context context, UserBackupManagerService userBackupManagerService) {
        schedule(i, context, 0L, userBackupManagerService);
    }

    public static void schedule(int i, Context context, long j, UserBackupManagerService userBackupManagerService) {
        long keyValueBackupIntervalMilliseconds;
        long keyValueBackupFuzzMilliseconds;
        int keyValueBackupRequiredNetworkType;
        boolean keyValueBackupRequireCharging;
        synchronized (KeyValueBackupJob.class) {
            SparseBooleanArray sparseBooleanArray = sScheduledForUserId;
            if (!sparseBooleanArray.get(i) && userBackupManagerService.isFrameworkSchedulingEnabled()) {
                BackupManagerConstants constants = userBackupManagerService.getConstants();
                synchronized (constants) {
                    keyValueBackupIntervalMilliseconds = constants.getKeyValueBackupIntervalMilliseconds();
                    keyValueBackupFuzzMilliseconds = constants.getKeyValueBackupFuzzMilliseconds();
                    keyValueBackupRequiredNetworkType = constants.getKeyValueBackupRequiredNetworkType();
                    keyValueBackupRequireCharging = constants.getKeyValueBackupRequireCharging();
                }
                if (j <= 0) {
                    j = new Random().nextInt((int) keyValueBackupFuzzMilliseconds) + keyValueBackupIntervalMilliseconds;
                }
                Slog.v(TAG, "Scheduling k/v pass in " + ((j / 1000) / 60) + " minutes");
                JobInfo.Builder overrideDeadline = new JobInfo.Builder(getJobIdForUserId(i), sKeyValueJobService).setMinimumLatency(j).setRequiredNetworkType(keyValueBackupRequiredNetworkType).setRequiresCharging(keyValueBackupRequireCharging).setOverrideDeadline(86400000L);
                Bundle bundle = new Bundle();
                bundle.putInt(USER_ID_EXTRA_KEY, i);
                overrideDeadline.setTransientExtras(bundle);
                ((JobScheduler) context.getSystemService("jobscheduler")).schedule(overrideDeadline.build());
                sparseBooleanArray.put(i, true);
                sNextScheduledForUserId.put(i, System.currentTimeMillis() + j);
            }
        }
    }

    public static void cancel(int i, Context context) {
        synchronized (KeyValueBackupJob.class) {
            ((JobScheduler) context.getSystemService("jobscheduler")).cancel(getJobIdForUserId(i));
            clearScheduledForUserId(i);
        }
    }

    public static long nextScheduled(int i) {
        long j;
        synchronized (KeyValueBackupJob.class) {
            j = sNextScheduledForUserId.get(i);
        }
        return j;
    }

    @VisibleForTesting
    public static boolean isScheduled(int i) {
        boolean z;
        synchronized (KeyValueBackupJob.class) {
            z = sScheduledForUserId.get(i);
        }
        return z;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        int i = jobParameters.getTransientExtras().getInt(USER_ID_EXTRA_KEY);
        synchronized (KeyValueBackupJob.class) {
            clearScheduledForUserId(i);
        }
        try {
            BackupManagerService.getInstance().backupNowForUser(i);
            return false;
        } catch (RemoteException unused) {
            return false;
        }
    }

    @GuardedBy({"KeyValueBackupJob.class"})
    private static void clearScheduledForUserId(int i) {
        sScheduledForUserId.delete(i);
        sNextScheduledForUserId.delete(i);
    }

    private static int getJobIdForUserId(int i) {
        return JobIdManager.getJobIdForUserId(MIN_JOB_ID, 52418896, i);
    }
}
