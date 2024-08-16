package com.android.server.pm;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManagerInternal;
import android.os.Process;
import android.util.EventLog;
import android.util.Log;
import com.android.server.LocalManagerRegistry;
import com.android.server.LocalServices;
import com.android.server.art.DexUseManagerLocal;
import com.android.server.art.model.DexContainerFileUseInfo;
import com.android.server.pm.PackageManagerLocal;
import com.android.server.pm.dex.DynamicCodeLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.util.HexEncoding;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DynamicCodeLoggingService extends JobService {
    private static final int AUDIT_AVC = 1400;
    private static final int AUDIT_WATCHING_JOB_ID = 203142925;
    private static final String AVC_PREFIX = "type=1400 ";
    private static final boolean DEBUG = false;
    private static final int IDLE_LOGGING_JOB_ID = 2030028;
    private static final String TAG = DynamicCodeLoggingService.class.getName();
    private static final long IDLE_LOGGING_PERIOD_MILLIS = TimeUnit.DAYS.toMillis(1);
    private static final long AUDIT_WATCHING_PERIOD_MILLIS = TimeUnit.HOURS.toMillis(2);
    private static final Pattern EXECUTE_NATIVE_AUDIT_PATTERN = Pattern.compile(".*\\bavc: +granted +\\{ execute(?:_no_trans|) \\} .*\\bpath=(?:\"([^\" ]*)\"|([0-9A-F]+)) .*\\bscontext=u:r:untrusted_app(?:_25|_27)?:.*\\btcontext=u:object_r:app_data_file:.*\\btclass=file\\b.*");
    private volatile boolean mIdleLoggingStopRequested = false;
    private volatile boolean mAuditWatchingStopRequested = false;

    /* renamed from: -$$Nest$smgetDynamicCodeLogger, reason: not valid java name */
    static /* bridge */ /* synthetic */ DynamicCodeLogger m1934$$Nest$smgetDynamicCodeLogger() {
        return getDynamicCodeLogger();
    }

    public static void schedule(Context context) {
        ComponentName componentName = new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, DynamicCodeLoggingService.class.getName());
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        jobScheduler.schedule(new JobInfo.Builder(IDLE_LOGGING_JOB_ID, componentName).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(IDLE_LOGGING_PERIOD_MILLIS).build());
        jobScheduler.schedule(new JobInfo.Builder(AUDIT_WATCHING_JOB_ID, componentName).setRequiresDeviceIdle(true).setRequiresBatteryNotLow(true).setPeriodic(AUDIT_WATCHING_PERIOD_MILLIS).build());
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        int jobId = jobParameters.getJobId();
        if (jobId == IDLE_LOGGING_JOB_ID) {
            this.mIdleLoggingStopRequested = false;
            new IdleLoggingThread(jobParameters).start();
            return true;
        }
        if (jobId != AUDIT_WATCHING_JOB_ID) {
            return false;
        }
        this.mAuditWatchingStopRequested = false;
        new AuditWatchingThread(jobParameters).start();
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        int jobId = jobParameters.getJobId();
        if (jobId == IDLE_LOGGING_JOB_ID) {
            this.mIdleLoggingStopRequested = true;
            return true;
        }
        if (jobId != AUDIT_WATCHING_JOB_ID) {
            return false;
        }
        this.mAuditWatchingStopRequested = true;
        return true;
    }

    private static DynamicCodeLogger getDynamicCodeLogger() {
        return ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getDynamicCodeLogger();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void syncDataFromArtService(DynamicCodeLogger dynamicCodeLogger) {
        DexUseManagerLocal dexUseManagerLocal = DexOptHelper.getDexUseManagerLocal();
        if (dexUseManagerLocal == null) {
            return;
        }
        PackageManagerLocal packageManagerLocal = (PackageManagerLocal) LocalManagerRegistry.getManager(PackageManagerLocal.class);
        Objects.requireNonNull(packageManagerLocal);
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            for (String str : withUnfilteredSnapshot.getPackageStates().keySet()) {
                for (DexContainerFileUseInfo dexContainerFileUseInfo : dexUseManagerLocal.getSecondaryDexContainerFileUseInfo(str)) {
                    Iterator it = dexContainerFileUseInfo.getLoadingPackages().iterator();
                    while (it.hasNext()) {
                        dynamicCodeLogger.recordDex(dexContainerFileUseInfo.getUserHandle().getIdentifier(), dexContainerFileUseInfo.getDexContainerFile(), str, (String) it.next());
                    }
                }
            }
            withUnfilteredSnapshot.close();
        } catch (Throwable th) {
            if (withUnfilteredSnapshot != null) {
                try {
                    withUnfilteredSnapshot.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class IdleLoggingThread extends Thread {
        private final JobParameters mParams;

        IdleLoggingThread(JobParameters jobParameters) {
            super("DynamicCodeLoggingService_IdleLoggingJob");
            this.mParams = jobParameters;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            DynamicCodeLogger m1934$$Nest$smgetDynamicCodeLogger = DynamicCodeLoggingService.m1934$$Nest$smgetDynamicCodeLogger();
            DynamicCodeLoggingService.syncDataFromArtService(m1934$$Nest$smgetDynamicCodeLogger);
            for (String str : m1934$$Nest$smgetDynamicCodeLogger.getAllPackagesWithDynamicCodeLoading()) {
                if (DynamicCodeLoggingService.this.mIdleLoggingStopRequested) {
                    Log.w(DynamicCodeLoggingService.TAG, "Stopping IdleLoggingJob run at scheduler request");
                    return;
                }
                m1934$$Nest$smgetDynamicCodeLogger.logDynamicCodeLoading(str);
            }
            DynamicCodeLoggingService.this.jobFinished(this.mParams, false);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AuditWatchingThread extends Thread {
        private final JobParameters mParams;

        AuditWatchingThread(JobParameters jobParameters) {
            super("DynamicCodeLoggingService_AuditWatchingJob");
            this.mParams = jobParameters;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (processAuditEvents()) {
                DynamicCodeLoggingService.this.jobFinished(this.mParams, false);
            }
        }

        private boolean processAuditEvents() {
            try {
                int tagCode = EventLog.getTagCode("auditd");
                int[] iArr = {tagCode};
                if (tagCode == -1) {
                    return true;
                }
                DynamicCodeLogger m1934$$Nest$smgetDynamicCodeLogger = DynamicCodeLoggingService.m1934$$Nest$smgetDynamicCodeLogger();
                ArrayList arrayList = new ArrayList();
                EventLog.readEvents(iArr, arrayList);
                Matcher matcher = DynamicCodeLoggingService.EXECUTE_NATIVE_AUDIT_PATTERN.matcher("");
                for (int i = 0; i < arrayList.size(); i++) {
                    if (DynamicCodeLoggingService.this.mAuditWatchingStopRequested) {
                        Log.w(DynamicCodeLoggingService.TAG, "Stopping AuditWatchingJob run at scheduler request");
                        return false;
                    }
                    EventLog.Event event = (EventLog.Event) arrayList.get(i);
                    int uid = event.getUid();
                    if (Process.isApplicationUid(uid)) {
                        Object data = event.getData();
                        if (data instanceof String) {
                            String str = (String) data;
                            if (str.startsWith(DynamicCodeLoggingService.AVC_PREFIX)) {
                                matcher.reset(str);
                                if (matcher.matches()) {
                                    String group = matcher.group(1);
                                    if (group == null) {
                                        group = DynamicCodeLoggingService.unhex(matcher.group(2));
                                    }
                                    m1934$$Nest$smgetDynamicCodeLogger.recordNative(uid, group);
                                }
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                Log.e(DynamicCodeLoggingService.TAG, "AuditWatchingJob failed", e);
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String unhex(String str) {
        return (str == null || str.length() == 0) ? "" : new String(HexEncoding.decode(str, false));
    }
}
