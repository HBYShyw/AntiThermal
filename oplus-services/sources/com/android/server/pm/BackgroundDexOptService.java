package com.android.server.pm;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.BatteryManagerInternal;
import android.os.Binder;
import android.os.Environment;
import android.os.IThermalService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.LocalServices;
import com.android.server.PinnerService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.Installer;
import com.android.server.pm.dex.ArtStatsLogUtils;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.dex.DexoptOptions;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import com.android.server.utils.TimingsTraceAndSlog;
import dalvik.system.DexFile;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class BackgroundDexOptService {
    private static final long CANCELLATION_WAIT_CHECK_INTERVAL_MS = 200;
    private static final int IDLE_RECORD_VERSION = 1;

    @VisibleForTesting
    static final int JOB_IDLE_OPTIMIZE = 800;

    @VisibleForTesting
    static final int JOB_POST_BOOT_UPDATE = 801;
    private static final int LOW_THRESHOLD_MULTIPLIER_FOR_DOWNGRADE = 2;
    public static final int STATUS_ABORT_BATTERY = 4;
    public static final int STATUS_ABORT_BY_CANCELLATION = 1;
    public static final int STATUS_ABORT_NO_SPACE_LEFT = 2;
    public static final int STATUS_ABORT_THERMAL = 3;
    public static final int STATUS_DEX_OPT_FAILED = 5;
    public static final int STATUS_FATAL_ERROR = 6;
    public static final int STATUS_OK = 0;
    public static final int STATUS_UNSPECIFIED = -1;
    private static final int THERMAL_CUTOFF_DEFAULT = 4;
    public IBackgroundDexOptServiceExt mBackgroundDexOptServiceExt;

    @GuardedBy({"mLock"})
    private Thread mDexOptCancellingThread;
    private final DexOptHelper mDexOptHelper;

    @GuardedBy({"mLock"})
    private Thread mDexOptThread;

    @GuardedBy({"mLock"})
    private boolean mDisableJobSchedulerJobs;
    private final long mDowngradeUnusedAppsThresholdInMillis;

    @GuardedBy({"mLock"})
    private final ArraySet<String> mFailedPackageNamesPrimary;

    @GuardedBy({"mLock"})
    private final ArraySet<String> mFailedPackageNamesSecondary;

    @GuardedBy({"mLock"})
    private boolean mFinishedPostBootUpdate;
    private final Injector mInjector;

    @GuardedBy({"mLock"})
    private final ArraySet<String> mLastCancelledPackages;

    @GuardedBy({"mLock"})
    private long mLastExecutionDurationMs;

    @GuardedBy({"mLock"})
    private long mLastExecutionStartUptimeMs;

    @GuardedBy({"mLock"})
    private int mLastExecutionStatus;
    private final Object mLock;
    private List<PackagesUpdatedListener> mPackagesUpdatedListeners;
    private final ArtStatsLogUtils.BackgroundDexoptJobStatsLogger mStatsLogger;
    private int mThermalStatusCutoff;
    private static final String TAG = "BackgroundDexOptService";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final long IDLE_OPTIMIZATION_PERIOD = TimeUnit.DAYS.toMillis(1);
    private static ComponentName sDexoptServiceName = new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, BackgroundDexOptJobService.class.getName());

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PackagesUpdatedListener {
        void onPackagesUpdated(ArraySet<String> arraySet);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface Status {
    }

    public BackgroundDexOptService(Context context, DexManager dexManager, PackageManagerService packageManagerService) throws Installer.LegacyDexoptDisabledException {
        this(new Injector(context, dexManager, packageManagerService));
    }

    @VisibleForTesting
    public BackgroundDexOptService(Injector injector) throws Installer.LegacyDexoptDisabledException {
        this.mStatsLogger = new ArtStatsLogUtils.BackgroundDexoptJobStatsLogger();
        this.mLock = new Object();
        this.mLastExecutionStatus = -1;
        this.mLastCancelledPackages = new ArraySet<>();
        this.mFailedPackageNamesPrimary = new ArraySet<>();
        this.mFailedPackageNamesSecondary = new ArraySet<>();
        this.mPackagesUpdatedListeners = new ArrayList();
        this.mThermalStatusCutoff = 4;
        this.mBackgroundDexOptServiceExt = (IBackgroundDexOptServiceExt) ExtLoader.type(IBackgroundDexOptServiceExt.class).base(this).create();
        Installer.checkLegacyDexoptDisabled();
        this.mInjector = injector;
        this.mDexOptHelper = injector.getDexOptHelper();
        LocalServices.addService(BackgroundDexOptService.class, this);
        this.mDowngradeUnusedAppsThresholdInMillis = injector.getDowngradeUnusedAppsThresholdInMillis();
    }

    public void systemReady() throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        if (this.mInjector.isBackgroundDexOptDisabled()) {
            return;
        }
        this.mBackgroundDexOptServiceExt.beforeScheduleJob(this.mInjector.getContext(), this.mInjector.getPackageManagerService());
        this.mInjector.getContext().registerReceiver(new BroadcastReceiver() { // from class: com.android.server.pm.BackgroundDexOptService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                BackgroundDexOptService.this.mInjector.getContext().unregisterReceiver(this);
                BackgroundDexOptService.this.scheduleAJob(BackgroundDexOptService.JOB_POST_BOOT_UPDATE);
                if (BackgroundDexOptService.this.mBackgroundDexOptServiceExt.isEnableFastIdle()) {
                    return;
                }
                BackgroundDexOptService.this.scheduleAJob(800);
                if (BackgroundDexOptService.DEBUG) {
                    Slog.d(BackgroundDexOptService.TAG, "BootBgDexopt scheduled");
                }
            }
        }, new IntentFilter("android.intent.action.BOOT_COMPLETED"));
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        boolean isBackgroundDexOptDisabled = this.mInjector.isBackgroundDexOptDisabled();
        indentingPrintWriter.print("enabled:");
        indentingPrintWriter.println(!isBackgroundDexOptDisabled);
        if (isBackgroundDexOptDisabled) {
            return;
        }
        synchronized (this.mLock) {
            indentingPrintWriter.print("mDexOptThread:");
            indentingPrintWriter.println(this.mDexOptThread);
            indentingPrintWriter.print("mDexOptCancellingThread:");
            indentingPrintWriter.println(this.mDexOptCancellingThread);
            indentingPrintWriter.print("mFinishedPostBootUpdate:");
            indentingPrintWriter.println(this.mFinishedPostBootUpdate);
            indentingPrintWriter.print("mDisableJobSchedulerJobs:");
            indentingPrintWriter.println(this.mDisableJobSchedulerJobs);
            indentingPrintWriter.print("mLastExecutionStatus:");
            indentingPrintWriter.println(this.mLastExecutionStatus);
            indentingPrintWriter.print("mLastExecutionStartUptimeMs:");
            indentingPrintWriter.println(this.mLastExecutionStartUptimeMs);
            indentingPrintWriter.print("mLastExecutionDurationMs:");
            indentingPrintWriter.println(this.mLastExecutionDurationMs);
            indentingPrintWriter.print("now:");
            indentingPrintWriter.println(SystemClock.elapsedRealtime());
            indentingPrintWriter.print("mLastCancelledPackages:");
            indentingPrintWriter.println(String.join(",", this.mLastCancelledPackages));
            indentingPrintWriter.print("mFailedPackageNamesPrimary:");
            indentingPrintWriter.println(String.join(",", this.mFailedPackageNamesPrimary));
            indentingPrintWriter.print("mFailedPackageNamesSecondary:");
            indentingPrintWriter.println(String.join(",", this.mFailedPackageNamesSecondary));
        }
    }

    public static BackgroundDexOptService getService() {
        return (BackgroundDexOptService) LocalServices.getService(BackgroundDexOptService.class);
    }

    public boolean runBackgroundDexoptJob(List<String> list) throws Installer.LegacyDexoptDisabledException {
        enforceRootOrShell();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                waitForDexOptThreadToFinishLocked();
                resetStatesForNewDexOptRunLocked(Thread.currentThread());
            }
            PackageManagerService packageManagerService = this.mInjector.getPackageManagerService();
            if (list == null) {
                list = this.mDexOptHelper.getOptimizablePackages(packageManagerService.snapshotComputer());
            }
            return runIdleOptimization(packageManagerService, list, false);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            markDexOptCompleted();
        }
    }

    public void cancelBackgroundDexoptJob() throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        enforceRootOrShell();
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.BackgroundDexOptService$$ExternalSyntheticLambda0
            public final void runOrThrow() {
                BackgroundDexOptService.this.lambda$cancelBackgroundDexoptJob$0();
            }
        });
    }

    public void setDisableJobSchedulerJobs(boolean z) throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        enforceRootOrShell();
        synchronized (this.mLock) {
            this.mDisableJobSchedulerJobs = z;
        }
    }

    public void addPackagesUpdatedListener(PackagesUpdatedListener packagesUpdatedListener) throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        synchronized (this.mLock) {
            this.mPackagesUpdatedListeners.add(packagesUpdatedListener);
        }
    }

    public void removePackagesUpdatedListener(PackagesUpdatedListener packagesUpdatedListener) throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        synchronized (this.mLock) {
            this.mPackagesUpdatedListeners.remove(packagesUpdatedListener);
        }
    }

    public void notifyPackageChanged(String str) throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        synchronized (this.mLock) {
            this.mFailedPackageNamesPrimary.remove(str);
            this.mFailedPackageNamesSecondary.remove(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onStartJob(final BackgroundDexOptJobService backgroundDexOptJobService, final JobParameters jobParameters) {
        Slog.i(TAG, "onStartJob:" + jobParameters.getJobId());
        if (this.mBackgroundDexOptServiceExt.skipOnStartJob()) {
            return false;
        }
        boolean z = jobParameters.getJobId() == JOB_POST_BOOT_UPDATE;
        final PackageManagerService packageManagerService = this.mInjector.getPackageManagerService();
        if (packageManagerService.isStorageLow()) {
            Slog.w(TAG, "Low storage, skipping this run");
            markPostBootUpdateCompleted(jobParameters);
            return false;
        }
        final List<String> optimizablePackages = this.mDexOptHelper.getOptimizablePackages(packageManagerService.snapshotComputer());
        if (optimizablePackages.isEmpty()) {
            Slog.i(TAG, "No packages to optimize");
            markPostBootUpdateCompleted(jobParameters);
            return false;
        }
        this.mThermalStatusCutoff = this.mInjector.getDexOptThermalCutoff();
        synchronized (this.mLock) {
            if (this.mDisableJobSchedulerJobs) {
                Slog.i(TAG, "JobScheduler invocations disabled");
                return false;
            }
            Thread thread = this.mDexOptThread;
            if (thread != null && thread.isAlive()) {
                Slog.i(TAG, "onStartJob:mDexOptThread.isAlive return true");
                backgroundDexOptJobService.jobFinished(jobParameters, true);
                return true;
            }
            if (!z && !this.mFinishedPostBootUpdate) {
                Slog.i(TAG, "onStartJob:Post boot job not finished yet,return true");
                backgroundDexOptJobService.jobFinished(jobParameters, true);
                return true;
            }
            if (!z && this.mBackgroundDexOptServiceExt.needSkipIdleOptimization()) {
                backgroundDexOptJobService.jobFinished(jobParameters, true);
                return true;
            }
            try {
                Injector injector = this.mInjector;
                StringBuilder sb = new StringBuilder();
                sb.append("BackgroundDexOptService_");
                sb.append(z ? "PostBoot" : "Idle");
                resetStatesForNewDexOptRunLocked(injector.createAndStartThread(sb.toString(), new Runnable() { // from class: com.android.server.pm.BackgroundDexOptService$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        BackgroundDexOptService.this.lambda$onStartJob$1(packageManagerService, optimizablePackages, jobParameters, backgroundDexOptJobService);
                    }
                }));
            } catch (Installer.LegacyDexoptDisabledException e) {
                Slog.wtf(TAG, e);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6, types: [int] */
    public /* synthetic */ void lambda$onStartJob$1(PackageManagerService packageManagerService, List list, JobParameters jobParameters, BackgroundDexOptJobService backgroundDexOptJobService) {
        boolean z;
        String str;
        String str2;
        boolean runIdleOptimization;
        String str3;
        boolean z2;
        String str4 = " completed:";
        ?? r1 = "dexopt finishing. jobid:";
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG, 16384L);
        timingsTraceAndSlog.traceBegin("jobExecution");
        boolean z3 = true;
        try {
            try {
                runIdleOptimization = runIdleOptimization(packageManagerService, list, jobParameters.getJobId() == JOB_POST_BOOT_UPDATE);
                timingsTraceAndSlog.traceEnd();
                StringBuilder sb = new StringBuilder();
                sb.append("dexopt finishing. jobid:");
                r1 = jobParameters.getJobId();
                sb.append((int) r1);
                sb.append(" completed:");
                sb.append(runIdleOptimization);
                Slog.i(TAG, sb.toString());
                writeStatsLog(jobParameters);
                if (jobParameters.getJobId() == JOB_POST_BOOT_UPDATE && runIdleOptimization) {
                    markPostBootUpdateCompleted(jobParameters);
                }
                str3 = str4;
            } catch (Installer.LegacyDexoptDisabledException e) {
                Slog.wtf(TAG, e);
                timingsTraceAndSlog.traceEnd();
                Slog.i(TAG, "dexopt finishing. jobid:" + jobParameters.getJobId() + " completed:false");
                writeStatsLog(jobParameters);
                jobParameters.getJobId();
                if (jobParameters.getJobId() != JOB_POST_BOOT_UPDATE) {
                    this.mBackgroundDexOptServiceExt.parseResultAfterIdleOptimization(this.mLastExecutionStatus);
                }
            } catch (RuntimeException e2) {
                try {
                    throw e2;
                } catch (Throwable th) {
                    th = th;
                    z = true;
                    str2 = str4;
                    str = r1;
                    timingsTraceAndSlog.traceEnd();
                    Slog.i(TAG, str + jobParameters.getJobId() + str2 + false);
                    writeStatsLog(jobParameters);
                    jobParameters.getJobId();
                    boolean z4 = (jobParameters.getJobId() == JOB_POST_BOOT_UPDATE && this.mBackgroundDexOptServiceExt.parseResultAfterIdleOptimization(this.mLastExecutionStatus)) ? z3 : false;
                    if (z && !z4) {
                        z3 = false;
                    }
                    backgroundDexOptJobService.jobFinished(jobParameters, z3);
                    markDexOptCompleted();
                    throw th;
                }
            }
            if (jobParameters.getJobId() != JOB_POST_BOOT_UPDATE) {
                IBackgroundDexOptServiceExt iBackgroundDexOptServiceExt = this.mBackgroundDexOptServiceExt;
                int i = this.mLastExecutionStatus;
                boolean parseResultAfterIdleOptimization = iBackgroundDexOptServiceExt.parseResultAfterIdleOptimization(i);
                str3 = i;
                if (parseResultAfterIdleOptimization) {
                    z2 = true;
                    str4 = i;
                    if (runIdleOptimization && !z2) {
                        z3 = false;
                    }
                    backgroundDexOptJobService.jobFinished(jobParameters, z3);
                    markDexOptCompleted();
                }
            }
            z2 = false;
            str4 = str3;
            if (runIdleOptimization) {
                z3 = false;
            }
            backgroundDexOptJobService.jobFinished(jobParameters, z3);
            markDexOptCompleted();
        } catch (Throwable th2) {
            th = th2;
            z = false;
            str2 = str4;
            str = r1;
            timingsTraceAndSlog.traceEnd();
            Slog.i(TAG, str + jobParameters.getJobId() + str2 + false);
            writeStatsLog(jobParameters);
            jobParameters.getJobId();
            if (jobParameters.getJobId() == JOB_POST_BOOT_UPDATE) {
            }
            if (z) {
                z3 = false;
            }
            backgroundDexOptJobService.jobFinished(jobParameters, z3);
            markDexOptCompleted();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onStopJob(BackgroundDexOptJobService backgroundDexOptJobService, JobParameters jobParameters) {
        Slog.i(TAG, "onStopJob:" + jobParameters.getJobId());
        this.mInjector.createAndStartThread("DexOptCancel", new Runnable() { // from class: com.android.server.pm.BackgroundDexOptService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BackgroundDexOptService.this.lambda$onStopJob$2();
            }
        });
        if (JOB_POST_BOOT_UPDATE != jobParameters.getJobId()) {
            return true;
        }
        this.mBackgroundDexOptServiceExt.notifyTriggerFastIdle();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStopJob$2() {
        try {
            lambda$cancelBackgroundDexoptJob$0();
        } catch (Installer.LegacyDexoptDisabledException e) {
            Slog.wtf(TAG, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: cancelDexOptAndWaitForCompletion, reason: merged with bridge method [inline-methods] */
    public void lambda$cancelBackgroundDexoptJob$0() throws Installer.LegacyDexoptDisabledException {
        synchronized (this.mLock) {
            if (this.mDexOptThread == null) {
                return;
            }
            Thread thread = this.mDexOptCancellingThread;
            if (thread != null && thread.isAlive()) {
                waitForDexOptThreadToFinishLocked();
                return;
            }
            this.mDexOptCancellingThread = Thread.currentThread();
            try {
                controlDexOptBlockingLocked(true);
                waitForDexOptThreadToFinishLocked();
            } finally {
                this.mDexOptCancellingThread = null;
                this.mDexOptThread = null;
                controlDexOptBlockingLocked(false);
                this.mLock.notifyAll();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void waitForDexOptThreadToFinishLocked() {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG, 262144L);
        timingsTraceAndSlog.traceBegin("waitForDexOptThreadToFinishLocked");
        while (true) {
            try {
                Thread thread = this.mDexOptThread;
                if (thread == null || !thread.isAlive()) {
                    break;
                } else {
                    this.mLock.wait(CANCELLATION_WAIT_CHECK_INTERVAL_MS);
                }
            } catch (InterruptedException unused) {
                Slog.w(TAG, "Interrupted while waiting for dexopt thread");
                Thread.currentThread().interrupt();
            }
        }
        timingsTraceAndSlog.traceEnd();
    }

    private void markDexOptCompleted() {
        synchronized (this.mLock) {
            if (this.mDexOptThread != Thread.currentThread()) {
                throw new IllegalStateException("Only mDexOptThread can mark completion, mDexOptThread:" + this.mDexOptThread + " current:" + Thread.currentThread());
            }
            this.mDexOptThread = null;
            this.mLock.notifyAll();
        }
    }

    @GuardedBy({"mLock"})
    private void resetStatesForNewDexOptRunLocked(Thread thread) throws Installer.LegacyDexoptDisabledException {
        this.mDexOptThread = thread;
        this.mLastCancelledPackages.clear();
        controlDexOptBlockingLocked(false);
    }

    private void enforceRootOrShell() {
        int callingUid = this.mInjector.getCallingUid();
        if (callingUid != 0 && callingUid != 2000) {
            throw new SecurityException("Should be shell or root user");
        }
    }

    @GuardedBy({"mLock"})
    private void controlDexOptBlockingLocked(boolean z) throws Installer.LegacyDexoptDisabledException {
        this.mInjector.getPackageManagerService();
        this.mDexOptHelper.controlDexOptBlocking(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleAJob(int i) {
        JobScheduler jobScheduler = this.mInjector.getJobScheduler();
        JobInfo.Builder requiresDeviceIdle = new JobInfo.Builder(i, sDexoptServiceName).setRequiresDeviceIdle(true);
        if (i == 800) {
            requiresDeviceIdle.setRequiresCharging(true).setPeriodic(IDLE_OPTIMIZATION_PERIOD);
        }
        jobScheduler.schedule(requiresDeviceIdle.build());
    }

    private long getLowStorageThreshold() {
        long dataDirStorageLowBytes = this.mInjector.getDataDirStorageLowBytes();
        if (dataDirStorageLowBytes == 0) {
            Slog.e(TAG, "Invalid low storage threshold");
        }
        return dataDirStorageLowBytes;
    }

    private void logStatus(int i) {
        if (i == 0) {
            Slog.i(TAG, "Idle optimizations completed.");
            return;
        }
        if (i == 1) {
            Slog.w(TAG, "Idle optimizations aborted by cancellation.");
            return;
        }
        if (i == 2) {
            Slog.w(TAG, "Idle optimizations aborted because of space constraints.");
            return;
        }
        if (i == 3) {
            Slog.w(TAG, "Idle optimizations aborted by thermal throttling.");
            return;
        }
        if (i == 4) {
            Slog.w(TAG, "Idle optimizations aborted by low battery.");
            return;
        }
        if (i == 5) {
            Slog.w(TAG, "Idle optimizations failed from dexopt.");
            return;
        }
        Slog.w(TAG, "Idle optimizations ended with unexpected code: " + i);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0066 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean runIdleOptimization(PackageManagerService packageManagerService, List<String> list, boolean z) throws Installer.LegacyDexoptDisabledException {
        int i;
        synchronized (this.mLock) {
            i = -1;
            this.mLastExecutionStatus = -1;
            this.mLastExecutionStartUptimeMs = SystemClock.uptimeMillis();
            this.mLastExecutionDurationMs = -1L;
        }
        this.mBackgroundDexOptServiceExt.beforeOptInIdleOptimization();
        try {
            i = idleOptimizePackages(packageManagerService, list, getLowStorageThreshold(), z);
            logStatus(i);
            this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("re", String.valueOf(i));
            boolean z2 = true;
            this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("ver", String.valueOf(1));
            this.mBackgroundDexOptServiceExt.afterOptInIdleOptimization();
            if (i != 0 && i != 5) {
                z2 = false;
            }
            synchronized (this.mLock) {
                this.mLastExecutionStatus = i;
                this.mLastExecutionDurationMs = SystemClock.uptimeMillis() - this.mLastExecutionStartUptimeMs;
            }
            return z2;
        } catch (RuntimeException e) {
            try {
                throw e;
            } catch (Throwable th) {
                th = th;
                i = 6;
                synchronized (this.mLock) {
                    this.mLastExecutionStatus = i;
                    this.mLastExecutionDurationMs = SystemClock.uptimeMillis() - this.mLastExecutionStartUptimeMs;
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            synchronized (this.mLock) {
            }
        }
    }

    private long getDirectorySize(File file) {
        if (file.isDirectory()) {
            long j = 0;
            for (File file2 : file.listFiles()) {
                j += getDirectorySize(file2);
            }
            return j;
        }
        return file.length();
    }

    private long getPackageSize(Computer computer, String str) {
        ApplicationInfo applicationInfo;
        long j = 0;
        PackageInfo packageInfo = computer.getPackageInfo(str, 0L, 0);
        if (packageInfo != null && (applicationInfo = packageInfo.applicationInfo) != null) {
            File file = Paths.get(applicationInfo.sourceDir, new String[0]).toFile();
            if (file.isFile()) {
                file = file.getParentFile();
            }
            j = 0 + getDirectorySize(file);
            if (!ArrayUtils.isEmpty(packageInfo.applicationInfo.splitSourceDirs)) {
                for (String str2 : packageInfo.applicationInfo.splitSourceDirs) {
                    File file2 = Paths.get(str2, new String[0]).toFile();
                    if (file2.isFile()) {
                        file2 = file2.getParentFile();
                    }
                    if (!file.getAbsolutePath().equals(file2.getAbsolutePath())) {
                        j += getDirectorySize(file2);
                    }
                }
            }
        }
        return j;
    }

    private int idleOptimizePackages(PackageManagerService packageManagerService, List<String> list, long j, boolean z) throws Installer.LegacyDexoptDisabledException {
        List<String> list2;
        int abortIdleOptimizations;
        ArraySet<String> arraySet = new ArraySet<>();
        try {
            boolean supportSecondaryDex = this.mInjector.supportSecondaryDex();
            if (supportSecondaryDex && (abortIdleOptimizations = reconcileSecondaryDexFiles()) != 0) {
                this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("sP", "rc");
            } else {
                boolean shouldDowngrade = shouldDowngrade(2 * j);
                boolean z2 = DEBUG;
                if (z2) {
                    Slog.d(TAG, "Should Downgrade " + shouldDowngrade);
                }
                if (shouldDowngrade) {
                    Computer snapshotComputer = packageManagerService.snapshotComputer();
                    Set<String> unusedPackages = snapshotComputer.getUnusedPackages(this.mDowngradeUnusedAppsThresholdInMillis);
                    if (z2) {
                        Slog.d(TAG, "Unsused Packages " + String.join(",", unusedPackages));
                    }
                    if (!unusedPackages.isEmpty()) {
                        for (String str : unusedPackages) {
                            abortIdleOptimizations = abortIdleOptimizations(-1L);
                            if (abortIdleOptimizations == 0) {
                                int downgradePackage = downgradePackage(snapshotComputer, packageManagerService, str, true, z);
                                if (downgradePackage == 1) {
                                    arraySet.add(str);
                                }
                                abortIdleOptimizations = convertPackageDexOptimizerStatusToInternal(downgradePackage);
                                if (abortIdleOptimizations == 0 && (!supportSecondaryDex || (abortIdleOptimizations = convertPackageDexOptimizerStatusToInternal(downgradePackage(snapshotComputer, packageManagerService, str, false, z))) == 0)) {
                                }
                            }
                        }
                        ArrayList arrayList = new ArrayList(list);
                        arrayList.removeAll(unusedPackages);
                        list2 = arrayList;
                        return optimizePackages(list2, j, arraySet, z);
                    }
                }
                list2 = list;
                return optimizePackages(list2, j, arraySet, z);
            }
            return abortIdleOptimizations;
        } finally {
            notifyPinService(arraySet);
            notifyPackagesUpdated(arraySet);
        }
    }

    private int optimizePackages(List<String> list, long j, ArraySet<String> arraySet, boolean z) throws Installer.LegacyDexoptDisabledException {
        boolean supportSecondaryDex = this.mInjector.supportSecondaryDex();
        if (!z) {
            this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("pT", String.valueOf(list.size()));
        }
        int i = 0;
        for (String str : this.mBackgroundDexOptServiceExt.adjustPkgOrderInOptimizePackages(list)) {
            int abortIdleOptimizations = abortIdleOptimizations(j);
            if (abortIdleOptimizations != 0) {
                return abortIdleOptimizations;
            }
            int breakAndReturnInOptimizePackages = this.mBackgroundDexOptServiceExt.breakAndReturnInOptimizePackages(z);
            if (breakAndReturnInOptimizePackages != 0) {
                return breakAndReturnInOptimizePackages;
            }
            int optimizePackage = optimizePackage(str, true, z);
            if (optimizePackage == 2) {
                return 1;
            }
            if (optimizePackage == 1) {
                arraySet.add(str);
            } else if (optimizePackage == -1) {
                if (!z) {
                    this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("pD", String.valueOf(arraySet.size()));
                }
                i = convertPackageDexOptimizerStatusToInternal(optimizePackage);
            }
            if (supportSecondaryDex) {
                int optimizePackage2 = optimizePackage(str, false, z);
                if (optimizePackage2 == 2) {
                    return 1;
                }
                if (optimizePackage2 == -1) {
                    if (!z) {
                        this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("sT", String.valueOf(arraySet.size()));
                    }
                    i = convertPackageDexOptimizerStatusToInternal(optimizePackage2);
                }
            }
        }
        if (!z) {
            this.mBackgroundDexOptServiceExt.updateIdleOptimizeRecord("pD", String.valueOf(arraySet.size()));
        }
        return i;
    }

    private int downgradePackage(Computer computer, PackageManagerService packageManagerService, String str, boolean z, boolean z2) throws Installer.LegacyDexoptDisabledException {
        int performDexOptPrimary;
        if (DEBUG) {
            Slog.d(TAG, "Downgrading " + str);
        }
        if (isCancelling()) {
            return 2;
        }
        String compilerFilterForReason = PackageManagerServiceCompilerMapping.getCompilerFilterForReason(11);
        int i = DexFile.isProfileGuidedCompilerFilter(compilerFilterForReason) ? 37 : 36;
        if (!z2) {
            i |= 512;
        }
        long packageSize = getPackageSize(computer, str);
        if (z || PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str)) {
            if (!packageManagerService.canHaveOatDir(computer, str)) {
                packageManagerService.deleteOatArtifactsOfPackage(computer, str);
                performDexOptPrimary = 0;
            } else {
                performDexOptPrimary = performDexOptPrimary(str, 11, compilerFilterForReason, i);
            }
        } else {
            performDexOptPrimary = performDexOptSecondary(str, 11, compilerFilterForReason, i);
        }
        if (performDexOptPrimary == 1) {
            FrameworkStatsLog.write(128, str, packageSize, getPackageSize(packageManagerService.snapshotComputer(), str), false);
        }
        return performDexOptPrimary;
    }

    private int reconcileSecondaryDexFiles() throws Installer.LegacyDexoptDisabledException {
        for (String str : this.mInjector.getDexManager().getAllPackagesWithSecondaryDexFiles()) {
            if (isCancelling()) {
                return 1;
            }
            this.mInjector.getDexManager().reconcileSecondaryDexFiles(str);
        }
        return 0;
    }

    private int optimizePackage(String str, boolean z, boolean z2) throws Installer.LegacyDexoptDisabledException {
        int i = z2 ? 2 : 9;
        String compilerFilterForReason = PackageManagerServiceCompilerMapping.getCompilerFilterForReason(i);
        int i2 = !z2 ? UsbTerminalTypes.TERMINAL_IN_MIC_ARRAY : 4;
        if (DexFile.isProfileGuidedCompilerFilter(compilerFilterForReason)) {
            i2 |= 1;
        }
        int adjustDexoptFlagsInOptimizePackage = this.mBackgroundDexOptServiceExt.adjustDexoptFlagsInOptimizePackage(i2, str);
        if (z || PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str)) {
            return performDexOptPrimary(str, i, compilerFilterForReason, adjustDexoptFlagsInOptimizePackage);
        }
        return performDexOptSecondary(str, i, compilerFilterForReason, adjustDexoptFlagsInOptimizePackage);
    }

    private int performDexOptPrimary(String str, int i, String str2, int i2) throws Installer.LegacyDexoptDisabledException {
        final DexoptOptions dexoptOptions = new DexoptOptions(str, i, str2, null, i2);
        return trackPerformDexOpt(str, true, new FunctionalUtils.ThrowingCheckedSupplier() { // from class: com.android.server.pm.BackgroundDexOptService$$ExternalSyntheticLambda3
            public final Object get() {
                Integer lambda$performDexOptPrimary$3;
                lambda$performDexOptPrimary$3 = BackgroundDexOptService.this.lambda$performDexOptPrimary$3(dexoptOptions);
                return lambda$performDexOptPrimary$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$performDexOptPrimary$3(DexoptOptions dexoptOptions) throws Installer.LegacyDexoptDisabledException {
        return Integer.valueOf(this.mDexOptHelper.performDexOptWithStatus(dexoptOptions));
    }

    private int performDexOptSecondary(String str, int i, String str2, int i2) throws Installer.LegacyDexoptDisabledException {
        final DexoptOptions dexoptOptions = new DexoptOptions(str, i, str2, null, i2 | 8);
        return trackPerformDexOpt(str, false, new FunctionalUtils.ThrowingCheckedSupplier() { // from class: com.android.server.pm.BackgroundDexOptService$$ExternalSyntheticLambda4
            public final Object get() {
                Integer lambda$performDexOptSecondary$4;
                lambda$performDexOptSecondary$4 = BackgroundDexOptService.this.lambda$performDexOptSecondary$4(dexoptOptions);
                return lambda$performDexOptSecondary$4;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$performDexOptSecondary$4(DexoptOptions dexoptOptions) throws Installer.LegacyDexoptDisabledException {
        return Integer.valueOf(this.mDexOptHelper.performDexOpt(dexoptOptions) ? 1 : -1);
    }

    private int trackPerformDexOpt(String str, boolean z, FunctionalUtils.ThrowingCheckedSupplier<Integer, Installer.LegacyDexoptDisabledException> throwingCheckedSupplier) throws Installer.LegacyDexoptDisabledException {
        synchronized (this.mLock) {
            ArraySet<String> arraySet = z ? this.mFailedPackageNamesPrimary : this.mFailedPackageNamesSecondary;
            if (arraySet.contains(str)) {
                return 0;
            }
            int intValue = ((Integer) throwingCheckedSupplier.get()).intValue();
            if (intValue == -1) {
                synchronized (this.mLock) {
                    arraySet.add(str);
                }
            } else if (intValue == 2) {
                synchronized (this.mLock) {
                    this.mLastCancelledPackages.add(str);
                }
            }
            return intValue;
        }
    }

    private int convertPackageDexOptimizerStatusToInternal(int i) {
        if (i == -1) {
            return 5;
        }
        if (i == 0 || i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        Slog.e(TAG, "Unkknown error code from PackageDexOptimizer:" + i, new RuntimeException());
        return 5;
    }

    private int abortIdleOptimizations(long j) {
        if (isCancelling()) {
            return 1;
        }
        int currentThermalStatus = this.mInjector.getCurrentThermalStatus();
        if (DEBUG) {
            Log.d(TAG, "Thermal throttling status during bgdexopt: " + currentThermalStatus);
        }
        if (currentThermalStatus >= this.mThermalStatusCutoff) {
            return 3;
        }
        if (this.mInjector.isBatteryLevelLow()) {
            return 4;
        }
        long dataDirUsableSpace = this.mInjector.getDataDirUsableSpace();
        if (dataDirUsableSpace >= j) {
            return 0;
        }
        Slog.w(TAG, "Aborting background dex opt job due to low storage: " + dataDirUsableSpace);
        return 2;
    }

    private boolean shouldDowngrade(long j) {
        return this.mInjector.getDataDirUsableSpace() < j;
    }

    private boolean isCancelling() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDexOptCancellingThread != null;
        }
        return z;
    }

    private void markPostBootUpdateCompleted(JobParameters jobParameters) {
        if (jobParameters.getJobId() != JOB_POST_BOOT_UPDATE) {
            return;
        }
        synchronized (this.mLock) {
            if (!this.mFinishedPostBootUpdate) {
                this.mFinishedPostBootUpdate = true;
            }
        }
        this.mInjector.getJobScheduler().cancel(JOB_POST_BOOT_UPDATE);
    }

    private void notifyPinService(ArraySet<String> arraySet) {
        PinnerService pinnerService = this.mInjector.getPinnerService();
        if (pinnerService != null) {
            Slog.i(TAG, "Pinning optimized code " + arraySet);
            pinnerService.update(arraySet, false);
        }
    }

    private void notifyPackagesUpdated(ArraySet<String> arraySet) {
        synchronized (this.mLock) {
            Iterator<PackagesUpdatedListener> it = this.mPackagesUpdatedListeners.iterator();
            while (it.hasNext()) {
                it.next().onPackagesUpdated(arraySet);
            }
        }
    }

    private void writeStatsLog(JobParameters jobParameters) {
        int i;
        long j;
        synchronized (this.mLock) {
            i = this.mLastExecutionStatus;
            j = this.mLastExecutionDurationMs;
        }
        this.mStatsLogger.write(i, jobParameters.getStopReason(), j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Injector {
        private final Context mContext;
        private final File mDataDir = Environment.getDataDirectory();
        private final DexManager mDexManager;
        private final PackageManagerService mPackageManagerService;

        Injector(Context context, DexManager dexManager, PackageManagerService packageManagerService) {
            this.mContext = context;
            this.mDexManager = dexManager;
            this.mPackageManagerService = packageManagerService;
        }

        int getCallingUid() {
            return Binder.getCallingUid();
        }

        Context getContext() {
            return this.mContext;
        }

        PackageManagerService getPackageManagerService() {
            return this.mPackageManagerService;
        }

        DexOptHelper getDexOptHelper() {
            return new DexOptHelper(getPackageManagerService());
        }

        JobScheduler getJobScheduler() {
            return (JobScheduler) this.mContext.getSystemService(JobScheduler.class);
        }

        DexManager getDexManager() {
            return this.mDexManager;
        }

        PinnerService getPinnerService() {
            return (PinnerService) LocalServices.getService(PinnerService.class);
        }

        boolean isBackgroundDexOptDisabled() {
            return SystemProperties.getBoolean("pm.dexopt.disable_bg_dexopt", false);
        }

        boolean isBatteryLevelLow() {
            return ((BatteryManagerInternal) LocalServices.getService(BatteryManagerInternal.class)).getBatteryLevelLow();
        }

        long getDowngradeUnusedAppsThresholdInMillis() {
            String str = SystemProperties.get("pm.dexopt.downgrade_after_inactive_days");
            if (str == null || str.isEmpty()) {
                Slog.w(BackgroundDexOptService.TAG, "SysProp pm.dexopt.downgrade_after_inactive_days not set");
                return JobStatus.NO_LATEST_RUNTIME;
            }
            return TimeUnit.DAYS.toMillis(Long.parseLong(str));
        }

        boolean supportSecondaryDex() {
            return SystemProperties.getBoolean("dalvik.vm.dexopt.secondary", false);
        }

        long getDataDirUsableSpace() {
            return this.mDataDir.getUsableSpace();
        }

        long getDataDirStorageLowBytes() {
            return ((StorageManager) this.mContext.getSystemService(StorageManager.class)).getStorageLowBytes(this.mDataDir);
        }

        int getCurrentThermalStatus() {
            try {
                return IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice")).getCurrentThermalStatus();
            } catch (RemoteException unused) {
                return 3;
            }
        }

        int getDexOptThermalCutoff() {
            return SystemProperties.getInt("dalvik.vm.dexopt.thermal-cutoff", 4);
        }

        Thread createAndStartThread(String str, Runnable runnable) {
            Thread thread = new Thread(runnable, str);
            Slog.i(BackgroundDexOptService.TAG, "Starting thread:" + str);
            thread.start();
            return thread;
        }
    }
}
