package com.android.server;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.FileUtils;
import android.os.SystemProperties;
import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ZramWriteback extends JobService {
    private static final String BDEV_SYS = "/sys/block/zram%d/backing_dev";
    private static final boolean DEBUG = false;
    private static final String FIRST_WB_DELAY_PROP = "ro.zram.first_wb_delay_mins";
    private static final String FORCE_WRITEBACK_PROP = "zram.force_writeback";
    private static final String IDLE_SYS = "/sys/block/zram%d/idle";
    private static final String IDLE_SYS_ALL_PAGES = "all";
    private static final String MARK_IDLE_DELAY_PROP = "ro.zram.mark_idle_delay_mins";
    private static final int MARK_IDLE_JOB_ID = 811;
    private static final int MAX_ZRAM_DEVICES = 256;
    private static final String PERIODIC_WB_DELAY_PROP = "ro.zram.periodic_wb_delay_hours";
    private static final String TAG = "ZramWriteback";
    private static final int WB_STATS_MAX_FILE_SIZE = 128;
    private static final String WB_STATS_SYS = "/sys/block/zram%d/bd_stat";
    private static final String WB_SYS = "/sys/block/zram%d/writeback";
    private static final String WB_SYS_IDLE_PAGES = "idle";
    private static final int WRITEBACK_IDLE_JOB_ID = 812;
    private static final ComponentName sZramWriteback = new ComponentName("android", ZramWriteback.class.getName());
    private static int sZramDeviceId = 0;

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void markPagesAsIdle() {
        String format = String.format(IDLE_SYS, Integer.valueOf(sZramDeviceId));
        try {
            FileUtils.stringToFile(new File(format), IDLE_SYS_ALL_PAGES);
        } catch (IOException unused) {
            Slog.e(TAG, "Failed to write to " + format);
        }
    }

    private void flushIdlePages() {
        String format = String.format(WB_SYS, Integer.valueOf(sZramDeviceId));
        try {
            FileUtils.stringToFile(new File(format), WB_SYS_IDLE_PAGES);
        } catch (IOException unused) {
            Slog.e(TAG, "Failed to write to " + format);
        }
    }

    private int getWrittenPageCount() {
        String format = String.format(WB_STATS_SYS, Integer.valueOf(sZramDeviceId));
        try {
            return Integer.parseInt(FileUtils.readTextFile(new File(format), 128, "").trim().split("\\s+")[2], 10);
        } catch (IOException unused) {
            Slog.e(TAG, "Failed to read writeback stats from " + format);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markAndFlushPages() {
        int writtenPageCount = getWrittenPageCount();
        flushIdlePages();
        markPagesAsIdle();
        if (writtenPageCount != -1) {
            Slog.i(TAG, "Total pages written to disk is " + (getWrittenPageCount() - writtenPageCount));
        }
    }

    private static boolean isWritebackEnabled() {
        try {
        } catch (IOException unused) {
            Slog.w(TAG, "Writeback is not enabled on zram");
        }
        if (!"none".equals(FileUtils.readTextFile(new File(String.format(BDEV_SYS, Integer.valueOf(sZramDeviceId))), 128, "").trim())) {
            return true;
        }
        Slog.w(TAG, "Writeback device is not set");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void schedNextWriteback(Context context) {
        ((JobScheduler) context.getSystemService("jobscheduler")).schedule(new JobInfo.Builder(WRITEBACK_IDLE_JOB_ID, sZramWriteback).setMinimumLatency(TimeUnit.HOURS.toMillis(SystemProperties.getInt(PERIODIC_WB_DELAY_PROP, 24))).setRequiresDeviceIdle(!SystemProperties.getBoolean(FORCE_WRITEBACK_PROP, false)).build());
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        if (!isWritebackEnabled()) {
            jobFinished(jobParameters, false);
            return false;
        }
        if (jobParameters.getJobId() == MARK_IDLE_JOB_ID) {
            markPagesAsIdle();
            jobFinished(jobParameters, false);
            return false;
        }
        new Thread("ZramWriteback_WritebackIdlePages") { // from class: com.android.server.ZramWriteback.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                ZramWriteback.this.markAndFlushPages();
                ZramWriteback.schedNextWriteback(ZramWriteback.this);
                ZramWriteback.this.jobFinished(jobParameters, false);
            }
        }.start();
        return true;
    }

    public static void scheduleZramWriteback(Context context) {
        int i = SystemProperties.getInt(MARK_IDLE_DELAY_PROP, 20);
        int i2 = SystemProperties.getInt(FIRST_WB_DELAY_PROP, FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_ACCEPTED);
        boolean z = SystemProperties.getBoolean(FORCE_WRITEBACK_PROP, false);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        ComponentName componentName = sZramWriteback;
        JobInfo.Builder builder = new JobInfo.Builder(MARK_IDLE_JOB_ID, componentName);
        TimeUnit timeUnit = TimeUnit.MINUTES;
        long j = i;
        jobScheduler.schedule(builder.setMinimumLatency(timeUnit.toMillis(j)).setOverrideDeadline(timeUnit.toMillis(j)).build());
        jobScheduler.schedule(new JobInfo.Builder(WRITEBACK_IDLE_JOB_ID, componentName).setMinimumLatency(timeUnit.toMillis(i2)).setRequiresDeviceIdle(!z).build());
    }
}
