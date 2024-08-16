package com.android.server.am;

import android.app.ActivityManager;
import android.app.AnrController;
import android.app.ApplicationErrorReport;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IncrementalStatesInfo;
import android.content.pm.PackageManagerInternal;
import android.os.IBinder;
import android.os.ITheiaManagerExt;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.incremental.IIncrementalService;
import android.os.incremental.IncrementalManager;
import android.os.incremental.IncrementalMetrics;
import android.provider.Settings;
import android.util.EventLog;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.CompositeRWLock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.ProcessCpuTracker;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.os.anr.AnrLatencyTracker;
import com.android.internal.util.FrameworkStatsLog;
import com.android.modules.expresslog.Counter;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.ResourcePressureUtil;
import com.android.server.Watchdog;
import com.android.server.am.AppNotRespondingDialog;
import com.android.server.am.trace.SmartTraceUtils;
import com.android.server.criticalevents.CriticalEventLog;
import com.android.server.stats.pull.ProcfsMemoryUtil;
import com.android.server.wm.WindowProcessController;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProcessErrorStateRecord {

    @GuardedBy({"mService"})
    private String mAnrAnnotation;

    @CompositeRWLock({"mService", "mProcLock"})
    private AppNotRespondingDialog.Data mAnrData;
    public final ProcessRecord mApp;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mBad;

    @CompositeRWLock({"mService", "mProcLock"})
    private Runnable mCrashHandler;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mCrashing;

    @CompositeRWLock({"mService", "mProcLock"})
    private ActivityManager.ProcessErrorStateInfo mCrashingReport;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mDefered;

    @CompositeRWLock({"mService", "mProcLock"})
    private final ErrorDialogController mDialogController;

    @CompositeRWLock({"mService", "mProcLock"})
    private ComponentName mErrorReportReceiver;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mForceCrashReport;

    @CompositeRWLock({"mService", "mProcLock"})
    private boolean mNotResponding;

    @CompositeRWLock({"mService", "mProcLock"})
    private ActivityManager.ProcessErrorStateInfo mNotRespondingReport;
    private final ActivityManagerGlobalLock mProcLock;
    private final ActivityManagerService mService;
    public IProcessErrorStateRecordExt mProcessErrorStateRecordExt = (IProcessErrorStateRecordExt) ExtLoader.type(IProcessErrorStateRecordExt.class).create();
    public IProcessErrorStateRecordSocExt mSocExt = (IProcessErrorStateRecordSocExt) ExtLoader.type(IProcessErrorStateRecordSocExt.class).base(this).create();
    private ITheiaManagerExt mTheiaManagerExt = (ITheiaManagerExt) ExtLoader.type(ITheiaManagerExt.class).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isBad() {
        return this.mBad;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setBad(boolean z) {
        this.mBad = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isCrashing() {
        return this.mCrashing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCrashing(boolean z) {
        this.mCrashing = z;
        this.mApp.getWindowProcessController().setCrashing(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isForceCrashReport() {
        return this.mForceCrashReport;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setForceCrashReport(boolean z) {
        this.mForceCrashReport = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public boolean isNotResponding() {
        return this.mNotResponding;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setNotResponding(boolean z) {
        this.mNotResponding = z;
        this.mApp.getWindowProcessController().setNotResponding(z);
    }

    @GuardedBy(anyOf = {"mService", "mProcLock"})
    boolean isDefered() {
        return this.mDefered;
    }

    @GuardedBy({"mService", "mProcLock"})
    void setDefered(boolean z) {
        this.mDefered = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public Runnable getCrashHandler() {
        return this.mCrashHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCrashHandler(Runnable runnable) {
        this.mCrashHandler = runnable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public ActivityManager.ProcessErrorStateInfo getCrashingReport() {
        return this.mCrashingReport;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setCrashingReport(ActivityManager.ProcessErrorStateInfo processErrorStateInfo) {
        this.mCrashingReport = processErrorStateInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public String getAnrAnnotation() {
        return this.mAnrAnnotation;
    }

    @GuardedBy({"mService"})
    void setAnrAnnotation(String str) {
        this.mAnrAnnotation = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public ActivityManager.ProcessErrorStateInfo getNotRespondingReport() {
        return this.mNotRespondingReport;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setNotRespondingReport(ActivityManager.ProcessErrorStateInfo processErrorStateInfo) {
        this.mNotRespondingReport = processErrorStateInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public ComponentName getErrorReportReceiver() {
        return this.mErrorReportReceiver;
    }

    @GuardedBy({"mService", "mProcLock"})
    void setErrorReportReceiver(ComponentName componentName) {
        this.mErrorReportReceiver = componentName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public ErrorDialogController getDialogController() {
        return this.mDialogController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setAnrData(AppNotRespondingDialog.Data data) {
        this.mAnrData = data;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public AppNotRespondingDialog.Data getAnrData() {
        return this.mAnrData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessErrorStateRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        ActivityManagerService activityManagerService = processRecord.mService;
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        this.mDialogController = new ErrorDialogController(processRecord);
    }

    void appNotResponding(String str, ApplicationInfo applicationInfo, String str2, WindowProcessController windowProcessController, boolean z, TimeoutRecord timeoutRecord, ExecutorService executorService, boolean z2, boolean z3, Future<File> future) {
        appNotResponding(str, applicationInfo, str2, windowProcessController, z, timeoutRecord, executorService, z2, z3, future, UUID.randomUUID().toString());
    }

    @GuardedBy({"mService"})
    boolean skipAnrLocked(String str) {
        if (this.mService.mAtmInternal.isShuttingDown()) {
            Slog.i(IActivityManagerServiceExt.TAG, "During shutdown skipping ANR: " + this + " " + str);
            return true;
        }
        if (isNotResponding()) {
            Slog.i(IActivityManagerServiceExt.TAG, "Skipping duplicate ANR: " + this + " " + str);
            return true;
        }
        if (isCrashing()) {
            Slog.i(IActivityManagerServiceExt.TAG, "Crashing app skipping ANR: " + this + " " + str);
            return true;
        }
        if (this.mApp.isKilledByAm()) {
            Slog.i(IActivityManagerServiceExt.TAG, "App already killed by AM skipping ANR: " + this + " " + str);
            return true;
        }
        if (!this.mApp.isKilled()) {
            return false;
        }
        Slog.i(IActivityManagerServiceExt.TAG, "Skipping died app ANR: " + this + " " + str);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0473 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:10:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x04c7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x050a  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x008f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x05e0  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x063d  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x068c  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x06a2  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x06b7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x05e5  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x05d1  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x04d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:363:0x007a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void appNotResponding(String str, ApplicationInfo applicationInfo, String str2, WindowProcessController windowProcessController, boolean z, TimeoutRecord timeoutRecord, ExecutorService executorService, boolean z2, boolean z3, Future<File> future, String str3) {
        boolean z4;
        boolean z5;
        ActivityManagerService activityManagerService;
        UUID uuid;
        boolean z6;
        int i;
        final ArrayList<Integer> arrayList;
        long j;
        final SparseBooleanArray sparseBooleanArray;
        long j2;
        boolean isDefered;
        ArrayList<Integer> arrayList2;
        long j3;
        StringBuilder sb;
        StringBuilder sb2;
        ProcessErrorStateRecord processErrorStateRecord;
        int i2;
        IncrementalMetrics incrementalMetrics;
        int i3;
        PackageManagerInternal packageManagerInternal;
        float f;
        float f2;
        IncrementalMetrics incrementalMetrics2;
        ActivityManagerService activityManagerService2;
        ProcessErrorStateRecord processErrorStateRecord2;
        IncrementalMetrics incrementalMetrics3;
        String str4;
        String codePath;
        IBinder service;
        String str5;
        final String str6 = timeoutRecord.mReason;
        final AnrLatencyTracker anrLatencyTracker = timeoutRecord.mLatencyTracker;
        if (this.mProcessErrorStateRecordExt.isOnlyDumpSelf(this.mApp.info) || this.mProcessErrorStateRecordExt.isDumpRestart(this.mApp.info)) {
            z4 = true;
        } else if (this.mProcessErrorStateRecordExt.isDumpMiddle(this.mApp.info)) {
            z4 = z2;
            z5 = true;
            ArrayList<Integer> arrayList3 = new ArrayList<>(5);
            SparseBooleanArray sparseBooleanArray2 = new SparseBooleanArray(20);
            this.mProcessErrorStateRecordExt.initForAnrStackDump();
            this.mApp.getWindowProcessController().appEarlyNotResponding(str6, new Runnable() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ProcessErrorStateRecord.this.lambda$appNotResponding$0(anrLatencyTracker, str6);
                }
            });
            long uptimeMillis = SystemClock.uptimeMillis();
            Future<?> submit = !isMonitorCpuUsage() ? executorService.submit(new Runnable() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ProcessErrorStateRecord.this.lambda$appNotResponding$1(anrLatencyTracker);
                }
            }) : null;
            final int pid = this.mApp.getPid();
            anrLatencyTracker.waitingOnAMSLockStarted();
            final boolean isSilentAnr = isSilentAnr();
            activityManagerService = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    anrLatencyTracker.waitingOnAMSLockEnded();
                    setAnrAnnotation(str6);
                    Counter.logIncrement("stability_anr.value_total_anrs");
                    if (skipAnrLocked(str6)) {
                        anrLatencyTracker.anrSkippedProcessErrorStateRecordAppNotResponding();
                        Counter.logIncrement("stability_anr.value_skipped_anrs");
                        return;
                    }
                    boolean z7 = z4;
                    this.mProcessErrorStateRecordExt.dumpSystraceWhenAnr(this.mService);
                    anrLatencyTracker.waitingOnProcLockStarted();
                    ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            anrLatencyTracker.waitingOnProcLockEnded();
                            setNotResponding(true);
                        } finally {
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    ProcessRecord processRecord = this.mApp;
                    EventLog.writeEvent(EventLogTags.AM_ANR, Integer.valueOf(this.mApp.userId), Integer.valueOf(pid), processRecord.processName, Integer.valueOf(processRecord.info.flags), str6);
                    TraceErrorLogger traceErrorLogger = this.mService.mTraceErrorLogger;
                    if (traceErrorLogger == null || !traceErrorLogger.isAddErrorIdEnabled()) {
                        uuid = null;
                    } else {
                        UUID generateErrorId = this.mService.mTraceErrorLogger.generateErrorId();
                        this.mService.mTraceErrorLogger.addProcessInfoAndErrorIdToTrace(this.mApp.processName, pid, generateErrorId);
                        this.mService.mTraceErrorLogger.addSubjectToTrace(str6, generateErrorId);
                        uuid = generateErrorId;
                    }
                    FrameworkStatsLog.write(FrameworkStatsLog.ANR_OCCURRED_PROCESSING_STARTED, this.mApp.processName);
                    if (applicationInfo != null) {
                        this.mProcessErrorStateRecordExt.hookANRInfo(applicationInfo.uid, pid, applicationInfo.packageName);
                    }
                    arrayList3.add(Integer.valueOf(pid));
                    if (z5) {
                        int pid2 = (windowProcessController == null || windowProcessController.getPid() <= 0) ? pid : windowProcessController.getPid();
                        if (pid2 != pid) {
                            arrayList3.add(Integer.valueOf(pid2));
                        }
                        int i4 = ActivityManagerService.MY_PID;
                        if (i4 != pid && i4 != pid2) {
                            arrayList3.add(Integer.valueOf(i4));
                        }
                        z6 = true;
                    } else {
                        z6 = z7;
                    }
                    this.mProcessErrorStateRecordExt.moveAnrTaskToBackIfNeed(this.mService, this.mApp, isSilentAnr, z);
                    if (isSilentAnr || z6) {
                        i = pid;
                        arrayList = arrayList3;
                        j = uptimeMillis;
                        sparseBooleanArray = sparseBooleanArray2;
                    } else {
                        int pid3 = (windowProcessController == null || windowProcessController.getPid() <= 0) ? pid : windowProcessController.getPid();
                        if (pid3 != pid) {
                            arrayList3.add(Integer.valueOf(pid3));
                        }
                        this.mProcessErrorStateRecordExt.hookAddAnrAppProcNames(ActivityManagerService.MY_PID, pid, pid3, arrayList3);
                        i = pid;
                        arrayList = arrayList3;
                        final int i5 = pid3;
                        j = uptimeMillis;
                        sparseBooleanArray = sparseBooleanArray2;
                        this.mService.mProcessList.forEachLruProcessesLOSP(false, new Consumer() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ProcessErrorStateRecord.this.lambda$appNotResponding$2(pid, i5, arrayList, sparseBooleanArray, (ProcessRecord) obj);
                            }
                        });
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    int i6 = i;
                    String buildMemoryHeadersFor = buildMemoryHeadersFor(i6);
                    anrLatencyTracker.criticalEventLogStarted();
                    CriticalEventLog criticalEventLog = CriticalEventLog.getInstance();
                    int processClassEnum = this.mApp.getProcessClassEnum();
                    ProcessRecord processRecord2 = this.mApp;
                    String logLinesForTraceFile = criticalEventLog.logLinesForTraceFile(processClassEnum, processRecord2.processName, processRecord2.uid);
                    anrLatencyTracker.criticalEventLogEnded();
                    CriticalEventLog criticalEventLog2 = CriticalEventLog.getInstance();
                    int processClassEnum2 = this.mApp.getProcessClassEnum();
                    ProcessRecord processRecord3 = this.mApp;
                    criticalEventLog2.logAnr(str6, processClassEnum2, processRecord3.processName, processRecord3.uid, processRecord3.mPid);
                    this.mProcessErrorStateRecordExt.resetProcNumDumpStackPids();
                    if (!isSilentAnr && this.mApp.info.packageName != null) {
                        long theiaEventTypeWhenANR = this.mTheiaManagerExt.getTheiaEventTypeWhenANR(this.mApp.info.packageName);
                        long uptimeMillis2 = SystemClock.uptimeMillis();
                        if (this.mService.mContext == null || this.mApp.info.packageName == null) {
                            Slog.w(IActivityManagerServiceExt.TAG, "packageVersion is empty for a null context or null packageName");
                            str5 = "empty";
                        } else {
                            str5 = getVersionName(this.mService.mContext, this.mApp.info.packageName, this.mApp.userId);
                        }
                        String str7 = "processName:" + this.mApp.info.packageName + "|packageVersion:" + str5;
                        ITheiaManagerExt iTheiaManagerExt = this.mTheiaManagerExt;
                        ProcessRecord processRecord4 = this.mApp;
                        iTheiaManagerExt.sendEvent(theiaEventTypeWhenANR, uptimeMillis2, processRecord4.mPid, processRecord4.uid, 268439555L, str7);
                        Slog.d(IActivityManagerServiceExt.TAG, "TheiaManager sendEvent:" + this.mApp.info.packageName + " ANR happen");
                    }
                    if (this.mSocExt.startAnrDump(this.mService, this, str, applicationInfo, str2, windowProcessController != null ? (ProcessRecord) windowProcessController.mOwner : null, z, str6, isSilentAnr, j, z6, uuid, logLinesForTraceFile, executorService, anrLatencyTracker, buildMemoryHeadersFor, submit, z3, future)) {
                        return;
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.setLength(0);
                    sb3.append("ANR in ");
                    sb3.append(this.mApp.processName);
                    if (str != null) {
                        sb3.append(" (");
                        sb3.append(str);
                        sb3.append(")");
                    }
                    sb3.append("\n");
                    sb3.append("PID: ");
                    sb3.append(i6);
                    sb3.append("\n");
                    if (str6 != null) {
                        sb3.append("Reason: ");
                        sb3.append(str6);
                        sb3.append("\n");
                    }
                    if (str2 != null && str2.equals(str)) {
                        sb3.append("Parent: ");
                        sb3.append(str2);
                        sb3.append("\n");
                    }
                    if (uuid != null) {
                        sb3.append("ErrorId: ");
                        sb3.append(uuid.toString());
                        sb3.append("\n");
                    }
                    sb3.append("Frozen: ");
                    sb3.append(this.mApp.mOptRecord.isFrozen());
                    sb3.append("\n");
                    AnrController anrController = this.mService.mActivityTaskManager.getAnrController(applicationInfo);
                    if (anrController != null) {
                        String str8 = applicationInfo.packageName;
                        int i7 = applicationInfo.uid;
                        long anrDelayMillis = anrController.getAnrDelayMillis(str8, i7);
                        anrController.onAnrDelayStarted(str8, i7);
                        Slog.i(IActivityManagerServiceExt.TAG, "ANR delay of " + anrDelayMillis + "ms started for " + str8);
                        j2 = anrDelayMillis;
                    } else {
                        j2 = 0;
                    }
                    StringBuilder sb4 = new StringBuilder();
                    anrLatencyTracker.currentPsiStateCalled();
                    String currentPsiState = ResourcePressureUtil.currentPsiState();
                    anrLatencyTracker.currentPsiStateReturned();
                    sb4.append(currentPsiState);
                    ProcessCpuTracker processCpuTracker = new ProcessCpuTracker(true);
                    final boolean isSmartTraceEnabled = isSmartTraceEnabled(isSilentAnr);
                    ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
                    ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock2) {
                        try {
                            isDefered = isDefered();
                        } finally {
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    long j4 = j2;
                    final boolean z8 = z6;
                    Future submit2 = executorService.submit(new Callable() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda3
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            ArrayList lambda$appNotResponding$3;
                            lambda$appNotResponding$3 = ProcessErrorStateRecord.this.lambda$appNotResponding$3(anrLatencyTracker, isSilentAnr, z8, isSmartTraceEnabled);
                            return lambda$appNotResponding$3;
                        }
                    });
                    StringWriter stringWriter = new StringWriter();
                    AtomicLong atomicLong = new AtomicLong(-1L);
                    ProcessCpuTracker processCpuTracker2 = (isSilentAnr || IProcessErrorStateRecordExt.isAgingVersion) ? null : processCpuTracker;
                    if (isSilentAnr) {
                        sparseBooleanArray = null;
                    }
                    long j5 = -1;
                    File dumpStackTraces = StackTracesDumpHelper.dumpStackTraces(arrayList, processCpuTracker2, sparseBooleanArray, submit2, stringWriter, atomicLong, str6, logLinesForTraceFile, buildMemoryHeadersFor, executorService, future, anrLatencyTracker);
                    if (dumpStackTraces != null) {
                        try {
                            arrayList2 = arrayList;
                            try {
                                this.mProcessErrorStateRecordExt.dumpStackTraces(i6, arrayList2, (ArrayList) submit2.get(), dumpStackTraces);
                            } catch (InterruptedException e) {
                                e = e;
                                Slog.w(IActivityManagerServiceExt.TAG, "Failed to get native pids", e);
                                long uptimeMillis3 = IDeviceIdleControllerExt.ADVANCE_TIME + SystemClock.uptimeMillis();
                                if (isSmartTraceEnabled) {
                                    long uptimeMillis4 = SystemClock.uptimeMillis();
                                    try {
                                        SmartTraceUtils.dumpStackTraces(i6, arrayList2, (ArrayList) submit2.get(), dumpStackTraces);
                                        Slog.i(IActivityManagerServiceExt.TAG, this.mApp.processName + " hit anr, dumpStackTraces cost " + (SystemClock.uptimeMillis() - uptimeMillis4) + "  ms");
                                    } catch (InterruptedException e2) {
                                        Slog.w(IActivityManagerServiceExt.TAG, "Failed to get native pids", e2);
                                    } catch (ExecutionException e3) {
                                        Slog.w(IActivityManagerServiceExt.TAG, "Failed to get native pids", e3.getCause());
                                    }
                                }
                                if (isPerfettoDumpEnabled(isSilentAnr)) {
                                    SmartTraceUtils.traceStart();
                                }
                                if (isMonitorCpuUsage()) {
                                }
                                sb.append(stringWriter.getBuffer());
                                sb2.append(processCpuTracker.printCurrentState(j3));
                                if (!shouldDeferAppNotResponding(isSilentAnr)) {
                                }
                                if (dumpStackTraces == null) {
                                }
                                processErrorStateRecord.mProcessErrorStateRecordExt.hookAssertANRInfo(dumpStackTraces, i6);
                                packageManagerInternal = processErrorStateRecord.mService.getPackageManagerInternal();
                                if (processErrorStateRecord.mApp.info != null) {
                                    IncrementalStatesInfo incrementalStatesInfo = packageManagerInternal.getIncrementalStatesInfo(processErrorStateRecord.mApp.info.packageName, 1000, processErrorStateRecord.mApp.userId);
                                    if (incrementalStatesInfo != null) {
                                    }
                                    codePath = processErrorStateRecord.mApp.info.getCodePath();
                                    if (codePath != null) {
                                        Slog.e(IActivityManagerServiceExt.TAG, "App ANR on incremental package " + processErrorStateRecord.mApp.info.packageName + " which is " + ((int) (f * 100.0f)) + "% loaded.");
                                        service = ServiceManager.getService("incremental");
                                        if (service != null) {
                                        }
                                    }
                                }
                                f2 = f;
                                incrementalMetrics2 = incrementalMetrics;
                                if (incrementalMetrics2 != null) {
                                }
                                activityManagerService2 = processErrorStateRecord.mService;
                                ActivityManagerService.boostPriorityForLockedSection();
                                synchronized (activityManagerService2) {
                                }
                            } catch (ExecutionException e4) {
                                e = e4;
                                Slog.w(IActivityManagerServiceExt.TAG, "Failed to get native pids", e.getCause());
                                long uptimeMillis32 = IDeviceIdleControllerExt.ADVANCE_TIME + SystemClock.uptimeMillis();
                                if (isSmartTraceEnabled) {
                                }
                                if (isPerfettoDumpEnabled(isSilentAnr)) {
                                }
                                if (isMonitorCpuUsage()) {
                                }
                                sb.append(stringWriter.getBuffer());
                                sb2.append(processCpuTracker.printCurrentState(j3));
                                if (!shouldDeferAppNotResponding(isSilentAnr)) {
                                }
                                if (dumpStackTraces == null) {
                                }
                                processErrorStateRecord.mProcessErrorStateRecordExt.hookAssertANRInfo(dumpStackTraces, i6);
                                packageManagerInternal = processErrorStateRecord.mService.getPackageManagerInternal();
                                if (processErrorStateRecord.mApp.info != null) {
                                }
                                f2 = f;
                                incrementalMetrics2 = incrementalMetrics;
                                if (incrementalMetrics2 != null) {
                                }
                                activityManagerService2 = processErrorStateRecord.mService;
                                ActivityManagerService.boostPriorityForLockedSection();
                                synchronized (activityManagerService2) {
                                }
                            }
                        } catch (InterruptedException e5) {
                            e = e5;
                            arrayList2 = arrayList;
                        } catch (ExecutionException e6) {
                            e = e6;
                            arrayList2 = arrayList;
                        }
                    } else {
                        arrayList2 = arrayList;
                    }
                    long uptimeMillis322 = IDeviceIdleControllerExt.ADVANCE_TIME + SystemClock.uptimeMillis();
                    if (isSmartTraceEnabled && dumpStackTraces != null) {
                        long uptimeMillis42 = SystemClock.uptimeMillis();
                        SmartTraceUtils.dumpStackTraces(i6, arrayList2, (ArrayList) submit2.get(), dumpStackTraces);
                        Slog.i(IActivityManagerServiceExt.TAG, this.mApp.processName + " hit anr, dumpStackTraces cost " + (SystemClock.uptimeMillis() - uptimeMillis42) + "  ms");
                    }
                    if (isPerfettoDumpEnabled(isSilentAnr) && !isDefered) {
                        SmartTraceUtils.traceStart();
                    }
                    if (isMonitorCpuUsage()) {
                        try {
                            submit.get();
                        } catch (InterruptedException e7) {
                            Slog.w(IActivityManagerServiceExt.TAG, "Interrupted while updating the CPU stats", e7);
                        } catch (ExecutionException e8) {
                            Slog.w(IActivityManagerServiceExt.TAG, "Failed to update the CPU stats", e8.getCause());
                        }
                        this.mService.updateCpuStatsNow();
                        j3 = j;
                        sb = sb4;
                        this.mService.mAppProfiler.printCurrentCpuState(sb, j3);
                        sb2 = sb3;
                        sb2.append(processCpuTracker.printCurrentLoad());
                        sb2.append((CharSequence) sb);
                    } else {
                        sb2 = sb3;
                        j3 = j;
                        sb = sb4;
                    }
                    sb.append(stringWriter.getBuffer());
                    sb2.append(processCpuTracker.printCurrentState(j3));
                    if (!shouldDeferAppNotResponding(isSilentAnr)) {
                        processErrorStateRecord = this;
                        i2 = 0;
                        incrementalMetrics = null;
                        i3 = 1;
                        Slog.e(IActivityManagerServiceExt.TAG, sb2.toString());
                    } else {
                        if (!isDefered) {
                            Slog.e(IActivityManagerServiceExt.TAG, sb2.toString());
                            long uptimeMillis5 = SystemClock.uptimeMillis();
                            long j6 = uptimeMillis322 < uptimeMillis5 ? 2000L : uptimeMillis322 - uptimeMillis5;
                            Slog.i(IActivityManagerServiceExt.TAG, "Defer to handle " + this.mApp.processName + " ANR, delay " + j6 + " ms  ");
                            ProcessRecord processRecord5 = this.mApp;
                            processRecord5.mService.mAnrHelper.deferAppNotResponding(processRecord5, str, applicationInfo, str2, windowProcessController, z, executorService, timeoutRecord, j6, z3, future);
                            ActivityManagerGlobalLock activityManagerGlobalLock3 = this.mProcLock;
                            ActivityManagerService.boostPriorityForProcLockedSection();
                            synchronized (activityManagerGlobalLock3) {
                                try {
                                    setDefered(true);
                                    setNotResponding(false);
                                    setNotRespondingReport(null);
                                } finally {
                                }
                            }
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            return;
                        }
                        processErrorStateRecord = this;
                        incrementalMetrics = null;
                        i3 = 1;
                        ActivityManagerGlobalLock activityManagerGlobalLock4 = processErrorStateRecord.mProcLock;
                        ActivityManagerService.boostPriorityForProcLockedSection();
                        synchronized (activityManagerGlobalLock4) {
                            i2 = 0;
                            try {
                                processErrorStateRecord.setDefered(false);
                            } finally {
                            }
                        }
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                        Slog.d(IActivityManagerServiceExt.TAG, processErrorStateRecord.mApp.processName + " has been defered, handle anr right now  ");
                    }
                    if (dumpStackTraces == null) {
                        Process.sendSignal(i6, 3);
                    } else if (atomicLong.get() > 0) {
                        long j7 = atomicLong.get();
                        AppExitInfoTracker appExitInfoTracker = processErrorStateRecord.mService.mProcessList.mAppExitInfoTracker;
                        ProcessRecord processRecord6 = processErrorStateRecord.mApp;
                        appExitInfoTracker.scheduleLogAnrTrace(i6, processRecord6.uid, processRecord6.getPackageList(), dumpStackTraces, 0L, j7);
                    }
                    processErrorStateRecord.mProcessErrorStateRecordExt.hookAssertANRInfo(dumpStackTraces, i6);
                    packageManagerInternal = processErrorStateRecord.mService.getPackageManagerInternal();
                    if (processErrorStateRecord.mApp.info != null && processErrorStateRecord.mApp.info.packageName != null && packageManagerInternal != null) {
                        IncrementalStatesInfo incrementalStatesInfo2 = packageManagerInternal.getIncrementalStatesInfo(processErrorStateRecord.mApp.info.packageName, 1000, processErrorStateRecord.mApp.userId);
                        f = incrementalStatesInfo2 != null ? incrementalStatesInfo2.getProgress() : 1.0f;
                        codePath = processErrorStateRecord.mApp.info.getCodePath();
                        if (codePath != null && !codePath.isEmpty() && IncrementalManager.isIncrementalPath(codePath)) {
                            Slog.e(IActivityManagerServiceExt.TAG, "App ANR on incremental package " + processErrorStateRecord.mApp.info.packageName + " which is " + ((int) (f * 100.0f)) + "% loaded.");
                            service = ServiceManager.getService("incremental");
                            if (service != null) {
                                incrementalMetrics2 = new IncrementalManager(IIncrementalService.Stub.asInterface(service)).getMetrics(codePath);
                                f2 = f;
                                if (incrementalMetrics2 != null) {
                                    sb2.append("Package is ");
                                    sb2.append((int) (100.0f * f2));
                                    sb2.append("% loaded.\n");
                                }
                                activityManagerService2 = processErrorStateRecord.mService;
                                ActivityManagerService.boostPriorityForLockedSection();
                                synchronized (activityManagerService2) {
                                    try {
                                        if (processErrorStateRecord.mApp.mThread != null) {
                                            try {
                                                ActivityManagerGlobalLock activityManagerGlobalLock5 = processErrorStateRecord.mProcLock;
                                                ActivityManagerService.boostPriorityForProcLockedSection();
                                                synchronized (activityManagerGlobalLock5) {
                                                    try {
                                                        processErrorStateRecord.mApp.mThread.dumpMainLooperTrackedMsg();
                                                    } finally {
                                                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                                                    }
                                                }
                                                ActivityManagerService.resetPriorityAfterProcLockedSection();
                                            } catch (RemoteException unused) {
                                                Slog.w(IActivityManagerServiceExt.TAG, "Process is already dead, Skip dump main looper traced message.");
                                            }
                                        } else {
                                            Slog.w(IActivityManagerServiceExt.TAG, "Process's IApplicationThread is null, skip dump main looper traced message.");
                                        }
                                    } finally {
                                        ActivityManagerService.resetPriorityAfterLockedSection();
                                    }
                                }
                                ActivityManagerService.resetPriorityAfterLockedSection();
                                ProcessRecord processRecord7 = processErrorStateRecord.mApp;
                                int i8 = processRecord7.uid;
                                String str9 = processRecord7.processName;
                                String str10 = str == null ? "unknown" : str;
                                int i9 = processRecord7.info != null ? processErrorStateRecord.mApp.info.isInstantApp() ? 2 : i3 : i2;
                                int i10 = processErrorStateRecord.mApp.isInterestingToUserLocked() ? 2 : i3;
                                int processClassEnum3 = processErrorStateRecord.mApp.getProcessClassEnum();
                                String str11 = processErrorStateRecord.mApp.info != null ? processErrorStateRecord.mApp.info.packageName : "";
                                int i11 = incrementalMetrics2 != null ? i3 : 0;
                                long millisSinceOldestPendingRead = incrementalMetrics2 != null ? incrementalMetrics2.getMillisSinceOldestPendingRead() : -1L;
                                int storageHealthStatusCode = incrementalMetrics2 != null ? incrementalMetrics2.getStorageHealthStatusCode() : -1;
                                int dataLoaderStatusCode = incrementalMetrics2 != null ? incrementalMetrics2.getDataLoaderStatusCode() : -1;
                                int i12 = (incrementalMetrics2 == null || !incrementalMetrics2.getReadLogsEnabled()) ? 0 : i3;
                                long millisSinceLastDataLoaderBind = incrementalMetrics2 != null ? incrementalMetrics2.getMillisSinceLastDataLoaderBind() : -1L;
                                long dataLoaderBindDelayMillis = incrementalMetrics2 != null ? incrementalMetrics2.getDataLoaderBindDelayMillis() : -1L;
                                int totalDelayedReads = incrementalMetrics2 != null ? incrementalMetrics2.getTotalDelayedReads() : -1;
                                int totalFailedReads = incrementalMetrics2 != null ? incrementalMetrics2.getTotalFailedReads() : -1;
                                int lastReadErrorUid = incrementalMetrics2 != null ? incrementalMetrics2.getLastReadErrorUid() : -1;
                                long millisSinceLastReadError = incrementalMetrics2 != null ? incrementalMetrics2.getMillisSinceLastReadError() : -1L;
                                int lastReadErrorNumber = incrementalMetrics2 != null ? incrementalMetrics2.getLastReadErrorNumber() : 0;
                                if (incrementalMetrics2 != null) {
                                    j5 = incrementalMetrics2.getTotalDelayedReadsDurationMillis();
                                }
                                IncrementalMetrics incrementalMetrics4 = incrementalMetrics;
                                StringBuilder sb5 = sb2;
                                StringBuilder sb6 = sb;
                                float f3 = f2;
                                FrameworkStatsLog.write(79, i8, str9, str10, str6, i9, i10, processClassEnum3, str11, (boolean) i11, f2, millisSinceOldestPendingRead, storageHealthStatusCode, dataLoaderStatusCode, (boolean) i12, millisSinceLastDataLoaderBind, dataLoaderBindDelayMillis, totalDelayedReads, totalFailedReads, lastReadErrorUid, millisSinceLastReadError, lastReadErrorNumber, j5);
                                if (windowProcessController != null) {
                                    processErrorStateRecord2 = this;
                                    incrementalMetrics3 = (ProcessRecord) windowProcessController.mOwner;
                                } else {
                                    processErrorStateRecord2 = this;
                                    incrementalMetrics3 = incrementalMetrics4;
                                }
                                ActivityManagerService activityManagerService3 = processErrorStateRecord2.mService;
                                ProcessRecord processRecord8 = processErrorStateRecord2.mApp;
                                activityManagerService3.addErrorToDropBox("anr", processRecord8, processRecord8.processName, str, str2, incrementalMetrics3, null, sb6.toString(), dumpStackTraces, null, new Float(f3), incrementalMetrics2, uuid, str3);
                                if (this.mApp.getWindowProcessController().appNotResponding(sb5.toString(), new Runnable() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda4
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        ProcessErrorStateRecord.this.lambda$appNotResponding$4();
                                    }
                                }, new Runnable() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda5
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        ProcessErrorStateRecord.this.lambda$appNotResponding$5();
                                    }
                                })) {
                                    return;
                                }
                                ActivityManagerService activityManagerService4 = this.mService;
                                ActivityManagerService.boostPriorityForLockedSection();
                                synchronized (activityManagerService4) {
                                    try {
                                        BatteryStatsService batteryStatsService = this.mService.mBatteryStatsService;
                                        if (batteryStatsService != null) {
                                            ProcessRecord processRecord9 = this.mApp;
                                            batteryStatsService.noteProcessAnr(processRecord9.processName, processRecord9.uid);
                                        }
                                        if (isSilentAnr() && !this.mApp.isDebugging()) {
                                            if (this.mProcessErrorStateRecordExt.isTheiaAnrTestApp(this.mApp.processName)) {
                                                ActivityManagerService.resetPriorityAfterLockedSection();
                                                return;
                                            } else {
                                                this.mApp.killLocked("bg anr", 6, true);
                                                ActivityManagerService.resetPriorityAfterLockedSection();
                                                return;
                                            }
                                        }
                                        ActivityManagerGlobalLock activityManagerGlobalLock6 = this.mProcLock;
                                        ActivityManagerService.boostPriorityForProcLockedSection();
                                        synchronized (activityManagerGlobalLock6) {
                                            if (str6 != null) {
                                                try {
                                                    str4 = "ANR " + str6;
                                                } finally {
                                                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                                                }
                                            } else {
                                                str4 = "ANR";
                                            }
                                            makeAppNotRespondingLSP(str, str4, sb5.toString());
                                            this.mDialogController.setAnrController(anrController);
                                            this.mApp.getWrapper().getExtImpl().setAnrAnnotation(str6);
                                        }
                                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                                        if (this.mService.mUiHandler != null) {
                                            Message obtain = Message.obtain();
                                            obtain.what = 2;
                                            obtain.obj = new AppNotRespondingDialog.Data(this.mApp, applicationInfo, z, z3);
                                            this.mService.mUiHandler.sendMessageDelayed(obtain, j4);
                                        }
                                        this.mProcessErrorStateRecordExt.hookSendApplicationStop(this.mService, this.mApp);
                                        ActivityManagerService.resetPriorityAfterLockedSection();
                                        return;
                                    } finally {
                                        ActivityManagerService.resetPriorityAfterLockedSection();
                                    }
                                }
                            }
                        }
                    }
                    f2 = f;
                    incrementalMetrics2 = incrementalMetrics;
                    if (incrementalMetrics2 != null) {
                    }
                    activityManagerService2 = processErrorStateRecord.mService;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } else {
            z4 = z2;
        }
        z5 = false;
        ArrayList<Integer> arrayList32 = new ArrayList<>(5);
        SparseBooleanArray sparseBooleanArray22 = new SparseBooleanArray(20);
        this.mProcessErrorStateRecordExt.initForAnrStackDump();
        this.mApp.getWindowProcessController().appEarlyNotResponding(str6, new Runnable() { // from class: com.android.server.am.ProcessErrorStateRecord$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ProcessErrorStateRecord.this.lambda$appNotResponding$0(anrLatencyTracker, str6);
            }
        });
        long uptimeMillis6 = SystemClock.uptimeMillis();
        if (!isMonitorCpuUsage()) {
        }
        final int pid4 = this.mApp.getPid();
        anrLatencyTracker.waitingOnAMSLockStarted();
        final boolean isSilentAnr2 = isSilentAnr();
        activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$0(AnrLatencyTracker anrLatencyTracker, String str) {
        anrLatencyTracker.waitingOnAMSLockStarted();
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                anrLatencyTracker.waitingOnAMSLockEnded();
                setAnrAnnotation(str);
                this.mApp.killLocked("anr", 6, true);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$1(AnrLatencyTracker anrLatencyTracker) {
        anrLatencyTracker.updateCpuStatsNowCalled();
        this.mService.updateCpuStatsNow();
        anrLatencyTracker.updateCpuStatsNowReturned();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$2(int i, int i2, ArrayList arrayList, SparseBooleanArray sparseBooleanArray, ProcessRecord processRecord) {
        if (processRecord == null || processRecord.getThread() == null) {
            return;
        }
        int pid = processRecord.getPid();
        if (Process.getUidForPid(processRecord.getPid()) != processRecord.uid) {
            Slog.w(IActivityManagerServiceExt.TAG, "Process " + processRecord.getPid() + " does not match uid " + processRecord.uid + ", skip dump its java trace");
            return;
        }
        if (pid <= 0 || pid == i || pid == i2 || pid == ActivityManagerService.MY_PID) {
            return;
        }
        boolean hookReturnIsInterestProc = this.mProcessErrorStateRecordExt.hookReturnIsInterestProc(processRecord);
        this.mProcessErrorStateRecordExt.hookAddFirstPids(processRecord.processName, arrayList, pid);
        if (processRecord.isPersistent()) {
            if (this.mProcessErrorStateRecordExt.hookAddPersistentProc(hookReturnIsInterestProc)) {
                arrayList.add(Integer.valueOf(pid));
                if (ActivityManagerDebugConfig.DEBUG_ANR) {
                    Slog.i(IActivityManagerServiceExt.TAG, "Adding persistent proc: " + processRecord);
                    return;
                }
                return;
            }
            return;
        }
        if (processRecord.mServices.isTreatedLikeActivity()) {
            if (this.mProcessErrorStateRecordExt.hookAddLikelyIME()) {
                arrayList.add(Integer.valueOf(pid));
                if (ActivityManagerDebugConfig.DEBUG_ANR) {
                    Slog.i(IActivityManagerServiceExt.TAG, "Adding likely IME: " + processRecord);
                    return;
                }
                return;
            }
            return;
        }
        if (this.mProcessErrorStateRecordExt.hookAddANRProc(hookReturnIsInterestProc)) {
            sparseBooleanArray.put(pid, true);
            if (ActivityManagerDebugConfig.DEBUG_ANR) {
                Slog.i(IActivityManagerServiceExt.TAG, "Adding ANR proc: " + processRecord);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ArrayList lambda$appNotResponding$3(AnrLatencyTracker anrLatencyTracker, boolean z, boolean z2, boolean z3) throws Exception {
        String[] strArr;
        anrLatencyTracker.nativePidCollectionStarted();
        ArrayList<Integer> arrayList = null;
        if (!(this.mApp.info.isSystemApp() || this.mApp.info.isSystemExt()) || z || z2) {
            int i = 0;
            while (true) {
                String[] strArr2 = Watchdog.NATIVE_STACKS_OF_INTEREST;
                if (i >= strArr2.length) {
                    strArr = null;
                    break;
                }
                if (strArr2[i].equals(this.mApp.processName)) {
                    strArr = new String[]{this.mApp.processName};
                    break;
                }
                i++;
            }
            int[] pidsForCommands = strArr == null ? null : Process.getPidsForCommands(strArr);
            if (pidsForCommands != null) {
                ArrayList<Integer> arrayList2 = new ArrayList<>(pidsForCommands.length);
                for (int i2 : pidsForCommands) {
                    arrayList2.add(Integer.valueOf(i2));
                }
                arrayList = arrayList2;
            }
        } else if (!z3 || SmartTraceUtils.isDumpPredefinedPidsEnabled()) {
            Watchdog.getInstance();
            arrayList = Watchdog.getInterestingNativePids();
        }
        anrLatencyTracker.nativePidCollectionEnded();
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$4() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mApp.killLocked("anr", 6, true);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$5() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mServices.scheduleServiceTimeoutLocked(this.mApp);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @GuardedBy({"mService", "mProcLock"})
    private void makeAppNotRespondingLSP(String str, String str2, String str3) {
        setNotResponding(true);
        AppErrors appErrors = this.mService.mAppErrors;
        if (appErrors != null) {
            this.mNotRespondingReport = appErrors.generateProcessError(this.mApp, 2, str, str2, str3, null);
        }
        startAppProblemLSP();
        this.mApp.getWindowProcessController().stopFreezingActivities();
    }

    private boolean isSmartTraceEnabled(boolean z) {
        return SmartTraceUtils.isSmartTraceEnabled() && (!z || (z && SmartTraceUtils.isSmartTraceEnabledOnBgApp()));
    }

    private boolean isPerfettoDumpEnabled(boolean z) {
        return SmartTraceUtils.isPerfettoDumpEnabled() && (!z || (z && SmartTraceUtils.isPerfettoDumpEnabledOnBgApp()));
    }

    private boolean shouldDeferAppNotResponding(boolean z) {
        return isSmartTraceEnabled(z) || isPerfettoDumpEnabled(z);
    }

    private String getVersionName(Context context, String str, int i) {
        try {
            return context.getPackageManager().getPackageInfoAsUser(str, 0, i).versionName;
        } catch (Exception e) {
            Slog.e(IActivityManagerServiceExt.TAG, "Failed to get versionName: " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void startAppProblemLSP() {
        this.mErrorReportReceiver = null;
        for (int i : this.mService.mUserController.getCurrentProfileIds()) {
            ProcessRecord processRecord = this.mApp;
            if (processRecord.userId == i) {
                this.mErrorReportReceiver = ApplicationErrorReport.getErrorReportReceiver(this.mService.mContext, processRecord.info.packageName, this.mApp.info.flags);
            }
        }
        for (BroadcastQueue broadcastQueue : this.mService.mBroadcastQueues) {
            broadcastQueue.onApplicationProblemLocked(this.mApp);
        }
    }

    @GuardedBy({"mService"})
    private boolean isInterestingForBackgroundTraces() {
        if (this.mApp.getPid() == ActivityManagerService.MY_PID || this.mApp.isInterestingToUserLocked()) {
            return true;
        }
        return (this.mApp.info != null && "com.android.systemui".equals(this.mApp.info.packageName)) || this.mApp.mState.hasTopUi() || this.mApp.mState.hasOverlayUi();
    }

    private boolean getShowBackground() {
        ContentResolver contentResolver = this.mService.mContext.getContentResolver();
        return Settings.Secure.getIntForUser(contentResolver, "anr_show_background", 0, contentResolver.getUserId()) != 0;
    }

    private String buildMemoryHeadersFor(int i) {
        if (i <= 0) {
            Slog.i(IActivityManagerServiceExt.TAG, "Memory header requested with invalid pid: " + i);
            return null;
        }
        ProcfsMemoryUtil.MemorySnapshot readMemorySnapshotFromProcfs = ProcfsMemoryUtil.readMemorySnapshotFromProcfs(i);
        if (readMemorySnapshotFromProcfs == null) {
            Slog.i(IActivityManagerServiceExt.TAG, "Failed to get memory snapshot for pid:" + i);
            return null;
        }
        return "RssHwmKb: " + readMemorySnapshotFromProcfs.rssHighWaterMarkInKilobytes + "\nRssKb: " + readMemorySnapshotFromProcfs.rssInKilobytes + "\nRssAnonKb: " + readMemorySnapshotFromProcfs.anonRssInKilobytes + "\nRssShmemKb: " + readMemorySnapshotFromProcfs.rssShmemKilobytes + "\nVmSwapKb: " + readMemorySnapshotFromProcfs.swapInKilobytes + "\n";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    @VisibleForTesting
    public boolean isSilentAnr() {
        return (getShowBackground() || isInterestingForBackgroundTraces()) ? false : true;
    }

    @VisibleForTesting
    boolean isMonitorCpuUsage() {
        AppProfiler appProfiler = this.mService.mAppProfiler;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void onCleanupApplicationRecordLSP() {
        IProcessErrorStateRecordExt iProcessErrorStateRecordExt = this.mProcessErrorStateRecordExt;
        ProcessRecord processRecord = this.mApp;
        iProcessErrorStateRecordExt.notifyTheiaAnrFinished(processRecord.mPid, processRecord.uid, processRecord.processName, "end");
        getDialogController().clearAllErrorDialogs();
        setCrashing(false);
        setNotResponding(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, long j) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (this.mCrashing || this.mDialogController.hasCrashDialogs() || this.mNotResponding || this.mDialogController.hasAnrDialogs() || this.mBad) {
                    printWriter.print(str);
                    printWriter.print(" mCrashing=" + this.mCrashing);
                    printWriter.print(" " + this.mDialogController.getCrashDialogs());
                    printWriter.print(" mNotResponding=" + this.mNotResponding);
                    printWriter.print(" " + this.mDialogController.getAnrDialogs());
                    printWriter.print(" bad=" + this.mBad);
                    if (this.mErrorReportReceiver != null) {
                        printWriter.print(" errorReportReceiver=");
                        printWriter.print(this.mErrorReportReceiver.flattenToShortString());
                    }
                    printWriter.println();
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }
}
