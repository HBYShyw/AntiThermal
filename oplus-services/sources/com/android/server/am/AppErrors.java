package com.android.server.am;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AnrController;
import android.app.ApplicationErrorReport;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.VersionedPackage;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.ProcessMap;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.PackageWatchdog;
import com.android.server.am.AppErrorDialog;
import com.android.server.am.AppNotRespondingDialog;
import com.android.server.usage.AppStandbyInternal;
import com.android.server.wm.WindowProcessController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppErrors {
    private static final String TAG = "ActivityManager";
    private static final String UPLOAD_VALUE_TOW_MINUTE_LIMIT = "TwoMinuteLimit";

    @GuardedBy({"mBadProcessLock"})
    private ArraySet<String> mAppsNotReportingCrashes;
    private final Context mContext;
    private final PackageWatchdog mPackageWatchdog;
    private final ActivityManagerGlobalLock mProcLock;
    private final ActivityManagerService mService;

    @GuardedBy({"mBadProcessLock"})
    private final ProcessMap<Long> mProcessCrashTimes = new ProcessMap<>();

    @GuardedBy({"mBadProcessLock"})
    private final ProcessMap<Long> mProcessCrashTimesPersistent = new ProcessMap<>();

    @GuardedBy({"mBadProcessLock"})
    private final ProcessMap<Long> mProcessCrashShowDialogTimes = new ProcessMap<>();

    @GuardedBy({"mBadProcessLock"})
    private final ProcessMap<Pair<Long, Integer>> mProcessCrashCounts = new ProcessMap<>();
    private volatile ProcessMap<BadProcessInfo> mBadProcesses = new ProcessMap<>();
    private final Object mBadProcessLock = new Object();
    public IAppErrorsExt mAppErrorsExt = (IAppErrorsExt) ExtLoader.type(IAppErrorsExt.class).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppErrors(Context context, ActivityManagerService activityManagerService, PackageWatchdog packageWatchdog) {
        context.assertRuntimeOverlayThemable();
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        this.mContext = context;
        this.mPackageWatchdog = packageWatchdog;
    }

    public void resetState() {
        Slog.i("ActivityManager", "Resetting AppErrors");
        synchronized (this.mBadProcessLock) {
            this.mAppsNotReportingCrashes.clear();
            this.mProcessCrashTimes.clear();
            this.mProcessCrashTimesPersistent.clear();
            this.mProcessCrashShowDialogTimes.clear();
            this.mProcessCrashCounts.clear();
            this.mBadProcesses = new ProcessMap<>();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void dumpDebugLPr(ProtoOutputStream protoOutputStream, long j, String str) {
        ArrayMap arrayMap;
        int i;
        String str2;
        SparseArray sparseArray;
        long j2;
        String str3;
        SparseArray sparseArray2;
        ArrayMap arrayMap2;
        int i2;
        ProcessMap<BadProcessInfo> processMap = this.mBadProcesses;
        if (this.mProcessCrashTimes.getMap().isEmpty() && processMap.getMap().isEmpty()) {
            return;
        }
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1112396529665L, SystemClock.uptimeMillis());
        long j3 = 1138166333441L;
        if (!processMap.getMap().isEmpty()) {
            ArrayMap map = processMap.getMap();
            int size = map.size();
            int i3 = 0;
            while (i3 < size) {
                long start2 = protoOutputStream.start(2246267895811L);
                String str4 = (String) map.keyAt(i3);
                SparseArray sparseArray3 = (SparseArray) map.valueAt(i3);
                int size2 = sparseArray3.size();
                protoOutputStream.write(j3, str4);
                int i4 = 0;
                while (i4 < size2) {
                    int keyAt = sparseArray3.keyAt(i4);
                    ProcessRecord processRecord = (ProcessRecord) this.mService.getProcessNamesLOSP().get(str4, keyAt);
                    if (str == null || (processRecord != null && processRecord.getPkgList().containsKey(str))) {
                        BadProcessInfo badProcessInfo = (BadProcessInfo) sparseArray3.valueAt(i4);
                        j2 = start;
                        str3 = str4;
                        sparseArray2 = sparseArray3;
                        ArrayMap arrayMap3 = map;
                        long start3 = protoOutputStream.start(2246267895810L);
                        protoOutputStream.write(1120986464257L, keyAt);
                        arrayMap2 = arrayMap3;
                        i2 = size;
                        protoOutputStream.write(1112396529666L, badProcessInfo.time);
                        protoOutputStream.write(1138166333443L, badProcessInfo.shortMsg);
                        protoOutputStream.write(1138166333444L, badProcessInfo.longMsg);
                        protoOutputStream.write(1138166333445L, badProcessInfo.stack);
                        protoOutputStream.end(start3);
                    } else {
                        arrayMap2 = map;
                        j2 = start;
                        i2 = size;
                        str3 = str4;
                        sparseArray2 = sparseArray3;
                    }
                    i4++;
                    str4 = str3;
                    size = i2;
                    sparseArray3 = sparseArray2;
                    start = j2;
                    map = arrayMap2;
                }
                protoOutputStream.end(start2);
                i3++;
                j3 = 1138166333441L;
            }
        }
        long j4 = start;
        synchronized (this.mBadProcessLock) {
            if (!this.mProcessCrashTimes.getMap().isEmpty()) {
                ArrayMap map2 = this.mProcessCrashTimes.getMap();
                int size3 = map2.size();
                int i5 = 0;
                while (i5 < size3) {
                    long start4 = protoOutputStream.start(2246267895810L);
                    String str5 = (String) map2.keyAt(i5);
                    SparseArray sparseArray4 = (SparseArray) map2.valueAt(i5);
                    int size4 = sparseArray4.size();
                    protoOutputStream.write(1138166333441L, str5);
                    int i6 = 0;
                    while (i6 < size4) {
                        int keyAt2 = sparseArray4.keyAt(i6);
                        ProcessRecord processRecord2 = (ProcessRecord) this.mService.getProcessNamesLOSP().get(str5, keyAt2);
                        if (str == null || (processRecord2 != null && processRecord2.getPkgList().containsKey(str))) {
                            arrayMap = map2;
                            i = size3;
                            long start5 = protoOutputStream.start(2246267895810L);
                            protoOutputStream.write(1120986464257L, keyAt2);
                            str2 = str5;
                            sparseArray = sparseArray4;
                            protoOutputStream.write(1112396529666L, ((Long) sparseArray4.valueAt(i6)).longValue());
                            protoOutputStream.end(start5);
                        } else {
                            arrayMap = map2;
                            i = size3;
                            str2 = str5;
                            sparseArray = sparseArray4;
                        }
                        i6++;
                        map2 = arrayMap;
                        str5 = str2;
                        size3 = i;
                        sparseArray4 = sparseArray;
                    }
                    protoOutputStream.end(start4);
                    i5++;
                    map2 = map2;
                    size3 = size3;
                }
            }
        }
        protoOutputStream.end(j4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean dumpLPr(FileDescriptor fileDescriptor, PrintWriter printWriter, boolean z, String str) {
        boolean z2;
        int i;
        int i2;
        AppErrors appErrors = this;
        long uptimeMillis = SystemClock.uptimeMillis();
        synchronized (appErrors.mBadProcessLock) {
            if (appErrors.mProcessCrashTimes.getMap().isEmpty()) {
                z2 = z;
            } else {
                ArrayMap map = appErrors.mProcessCrashTimes.getMap();
                int size = map.size();
                z2 = z;
                int i3 = 0;
                boolean z3 = false;
                while (i3 < size) {
                    String str2 = (String) map.keyAt(i3);
                    SparseArray sparseArray = (SparseArray) map.valueAt(i3);
                    int size2 = sparseArray.size();
                    int i4 = 0;
                    while (i4 < size2) {
                        int keyAt = sparseArray.keyAt(i4);
                        ArrayMap arrayMap = map;
                        ProcessRecord processRecord = (ProcessRecord) appErrors.mService.getProcessNamesLOSP().get(str2, keyAt);
                        if (str == null || (processRecord != null && processRecord.getPkgList().containsKey(str))) {
                            if (!z3) {
                                if (z2) {
                                    printWriter.println();
                                }
                                printWriter.println("  Time since processes crashed:");
                                z2 = true;
                                z3 = true;
                            }
                            printWriter.print("    Process ");
                            printWriter.print(str2);
                            printWriter.print(" uid ");
                            printWriter.print(keyAt);
                            printWriter.print(": last crashed ");
                            i2 = size;
                            TimeUtils.formatDuration(uptimeMillis - ((Long) sparseArray.valueAt(i4)).longValue(), printWriter);
                            printWriter.println(" ago");
                        } else {
                            i2 = size;
                        }
                        i4++;
                        size = i2;
                        map = arrayMap;
                    }
                    i3++;
                    map = map;
                }
            }
            if (!appErrors.mProcessCrashCounts.getMap().isEmpty()) {
                ArrayMap map2 = appErrors.mProcessCrashCounts.getMap();
                int size3 = map2.size();
                boolean z4 = false;
                for (int i5 = 0; i5 < size3; i5++) {
                    String str3 = (String) map2.keyAt(i5);
                    SparseArray sparseArray2 = (SparseArray) map2.valueAt(i5);
                    int size4 = sparseArray2.size();
                    int i6 = 0;
                    while (i6 < size4) {
                        int keyAt2 = sparseArray2.keyAt(i6);
                        ArrayMap arrayMap2 = map2;
                        ProcessRecord processRecord2 = (ProcessRecord) appErrors.mService.getProcessNamesLOSP().get(str3, keyAt2);
                        if (str == null || (processRecord2 != null && processRecord2.getPkgList().containsKey(str))) {
                            if (!z4) {
                                if (z2) {
                                    printWriter.println();
                                }
                                printWriter.println("  First time processes crashed and counts:");
                                z4 = true;
                                z2 = true;
                            }
                            printWriter.print("    Process ");
                            printWriter.print(str3);
                            printWriter.print(" uid ");
                            printWriter.print(keyAt2);
                            printWriter.print(": first crashed ");
                            i = size3;
                            TimeUtils.formatDuration(uptimeMillis - ((Long) ((Pair) sparseArray2.valueAt(i6)).first).longValue(), printWriter);
                            printWriter.print(" ago; crashes since then: ");
                            printWriter.println(((Pair) sparseArray2.valueAt(i6)).second);
                        } else {
                            i = size3;
                        }
                        i6++;
                        map2 = arrayMap2;
                        size3 = i;
                    }
                }
            }
        }
        ProcessMap<BadProcessInfo> processMap = appErrors.mBadProcesses;
        if (!processMap.getMap().isEmpty()) {
            ArrayMap map3 = processMap.getMap();
            int size5 = map3.size();
            int i7 = 0;
            boolean z5 = false;
            while (i7 < size5) {
                String str4 = (String) map3.keyAt(i7);
                SparseArray sparseArray3 = (SparseArray) map3.valueAt(i7);
                int size6 = sparseArray3.size();
                int i8 = 0;
                while (i8 < size6) {
                    int keyAt3 = sparseArray3.keyAt(i8);
                    ProcessRecord processRecord3 = (ProcessRecord) appErrors.mService.getProcessNamesLOSP().get(str4, keyAt3);
                    if (str == null || (processRecord3 != null && processRecord3.getPkgList().containsKey(str))) {
                        if (!z5) {
                            if (z2) {
                                printWriter.println();
                            }
                            printWriter.println("  Bad processes:");
                            z5 = true;
                            z2 = true;
                        }
                        BadProcessInfo badProcessInfo = (BadProcessInfo) sparseArray3.valueAt(i8);
                        printWriter.print("    Bad process ");
                        printWriter.print(str4);
                        printWriter.print(" uid ");
                        printWriter.print(keyAt3);
                        printWriter.print(": crashed at time ");
                        printWriter.println(badProcessInfo.time);
                        if (badProcessInfo.shortMsg != null) {
                            printWriter.print("      Short msg: ");
                            printWriter.println(badProcessInfo.shortMsg);
                        }
                        if (badProcessInfo.longMsg != null) {
                            printWriter.print("      Long msg: ");
                            printWriter.println(badProcessInfo.longMsg);
                        }
                        if (badProcessInfo.stack != null) {
                            printWriter.println("      Stack:");
                            int i9 = 0;
                            for (int i10 = 0; i10 < badProcessInfo.stack.length(); i10++) {
                                if (badProcessInfo.stack.charAt(i10) == '\n') {
                                    printWriter.print("        ");
                                    printWriter.write(badProcessInfo.stack, i9, i10 - i9);
                                    printWriter.println();
                                    i9 = i10 + 1;
                                }
                            }
                            if (i9 < badProcessInfo.stack.length()) {
                                printWriter.print("        ");
                                String str5 = badProcessInfo.stack;
                                printWriter.write(str5, i9, str5.length() - i9);
                                printWriter.println();
                            }
                        }
                    }
                    i8++;
                    appErrors = this;
                }
                i7++;
                appErrors = this;
            }
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBadProcess(String str, int i) {
        return this.mBadProcesses.get(str, i) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearBadProcess(String str, int i) {
        synchronized (this.mBadProcessLock) {
            ProcessMap<BadProcessInfo> processMap = new ProcessMap<>();
            processMap.putAll(this.mBadProcesses);
            processMap.remove(str, i);
            this.mBadProcesses = processMap;
        }
    }

    void markBadProcess(String str, int i, BadProcessInfo badProcessInfo) {
        synchronized (this.mBadProcessLock) {
            ProcessMap<BadProcessInfo> processMap = new ProcessMap<>();
            processMap.putAll(this.mBadProcesses);
            processMap.put(str, i, badProcessInfo);
            this.mBadProcesses = processMap;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetProcessCrashTime(String str, int i) {
        synchronized (this.mBadProcessLock) {
            this.mProcessCrashTimes.remove(str, i);
            this.mProcessCrashCounts.remove(str, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetProcessCrashTime(boolean z, int i, int i2) {
        synchronized (this.mBadProcessLock) {
            ArrayMap map = this.mProcessCrashTimes.getMap();
            for (int size = map.size() - 1; size >= 0; size--) {
                SparseArray<?> sparseArray = (SparseArray) map.valueAt(size);
                resetProcessCrashMapLBp(sparseArray, z, i, i2);
                if (sparseArray.size() == 0) {
                    map.removeAt(size);
                }
            }
            ArrayMap map2 = this.mProcessCrashCounts.getMap();
            for (int size2 = map2.size() - 1; size2 >= 0; size2--) {
                SparseArray<?> sparseArray2 = (SparseArray) map2.valueAt(size2);
                resetProcessCrashMapLBp(sparseArray2, z, i, i2);
                if (sparseArray2.size() == 0) {
                    map2.removeAt(size2);
                }
            }
        }
    }

    @GuardedBy({"mBadProcessLock"})
    private void resetProcessCrashMapLBp(SparseArray<?> sparseArray, boolean z, int i, int i2) {
        for (int size = sparseArray.size() - 1; size >= 0; size--) {
            int keyAt = sparseArray.keyAt(size);
            if (z ? UserHandle.getUserId(keyAt) == i2 : !(i2 != -1 ? keyAt != UserHandle.getUid(i2, i) : UserHandle.getAppId(keyAt) != i)) {
                sparseArray.removeAt(size);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadAppsNotReportingCrashesFromConfig(String str) {
        if (str != null) {
            String[] split = str.split(",");
            if (split.length > 0) {
                synchronized (this.mBadProcessLock) {
                    ArraySet<String> arraySet = new ArraySet<>();
                    this.mAppsNotReportingCrashes = arraySet;
                    Collections.addAll(arraySet, split);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void killAppAtUserRequestLocked(ProcessRecord processRecord) {
        int i;
        int i2;
        ErrorDialogController dialogController = processRecord.mErrorState.getDialogController();
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (dialogController.hasDebugWaitingDialog()) {
                    i = 13;
                    i2 = 1;
                } else {
                    i = 6;
                    i2 = 0;
                }
                dialogController.clearAllErrorDialogs();
                killAppImmediateLSP(processRecord, i, i2, "user-terminated", "user request after error" + this.mAppErrorsExt.handleAnrAnnotation(processRecord));
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    @GuardedBy({"mService", "mProcLock"})
    private void killAppImmediateLSP(ProcessRecord processRecord, int i, int i2, String str, String str2) {
        ProcessErrorStateRecord processErrorStateRecord = processRecord.mErrorState;
        processErrorStateRecord.setCrashing(false);
        processErrorStateRecord.setCrashingReport(null);
        processErrorStateRecord.setNotResponding(false);
        processErrorStateRecord.setNotRespondingReport(null);
        int pid = processErrorStateRecord.mApp.getPid();
        if (pid <= 0 || pid == ActivityManagerService.MY_PID) {
            return;
        }
        synchronized (this.mBadProcessLock) {
            handleAppCrashLSPB(processRecord, str, null, null, null, null);
        }
        processRecord.killLocked(str2, i, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleAppCrashLocked(int i, int i2, String str, int i3, String str2, boolean z, int i4, Bundle bundle) {
        int i5;
        final ProcessRecord processRecord;
        synchronized (this.mService.mPidsSelfLocked) {
            processRecord = null;
            int i6 = 0;
            while (true) {
                if (i6 >= this.mService.mPidsSelfLocked.size()) {
                    break;
                }
                ProcessRecord valueAt = this.mService.mPidsSelfLocked.valueAt(i6);
                if (i < 0 || valueAt.uid == i) {
                    if (valueAt.getPid() == i2) {
                        processRecord = valueAt;
                        break;
                    } else if (valueAt.getPkgList().containsKey(str) && (i3 < 0 || valueAt.userId == i3)) {
                        processRecord = valueAt;
                    }
                }
                i6++;
            }
        }
        if (processRecord == null) {
            Slog.w("ActivityManager", "crashApplication: nothing for uid=" + i + " initialPid=" + i2 + " packageName=" + str + " userId=" + i3);
            return;
        }
        if (i4 == 5) {
            String[] packageList = processRecord.getPackageList();
            for (i5 = 0; i5 < packageList.length; i5++) {
                if (this.mService.mPackageManagerInt.isPackageStateProtected(packageList[i5], processRecord.userId)) {
                    Slog.w("ActivityManager", "crashApplication: Can not crash protected package " + packageList[i5]);
                    return;
                }
            }
        }
        this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeProcess(i2, 12);
        processRecord.scheduleCrashLocked(str2, i4, bundle);
        if (z) {
            this.mService.mHandler.postDelayed(new Runnable() { // from class: com.android.server.am.AppErrors$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AppErrors.this.lambda$scheduleAppCrashLocked$0(processRecord);
                }
            }, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAppCrashLocked$0(ProcessRecord processRecord) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        killAppImmediateLSP(processRecord, 13, 14, "forced", "killed for invalid state");
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (Throwable th2) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendRecoverableCrashToAppExitInfo(ProcessRecord processRecord, ApplicationErrorReport.CrashInfo crashInfo) {
        if (processRecord == null || crashInfo == null || !"Native crash".equals(crashInfo.exceptionClassName)) {
            return;
        }
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mProcessList.noteAppRecoverableCrash(processRecord);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void crashApplication(ProcessRecord processRecord, ApplicationErrorReport.CrashInfo crashInfo) {
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            crashApplicationInner(processRecord, crashInfo, callingPid, callingUid);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0229 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void crashApplicationInner(ProcessRecord processRecord, ApplicationErrorReport.CrashInfo crashInfo, int i, int i2) {
        String str;
        long j;
        boolean z;
        ActivityManagerService activityManagerService;
        Intent intent;
        long currentTimeMillis = System.currentTimeMillis();
        String str2 = crashInfo.exceptionClassName;
        String str3 = crashInfo.exceptionMessage;
        String str4 = crashInfo.stackTrace;
        if (str2 != null && str3 != null) {
            str3 = str2 + ": " + str3;
        } else if (str2 != null) {
            str = str2;
            long j2 = 0;
            if (processRecord == null) {
                this.mPackageWatchdog.onPackageFailure(processRecord.getPackageListWithVersionCode(), 3);
                ActivityManagerService activityManagerService2 = this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService2) {
                    try {
                        this.mService.mProcessList.noteAppKill(processRecord, "Native crash".equals(crashInfo.exceptionClassName) ? 5 : 4, 0, "crash");
                    } finally {
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                if (str2 != null && str != null && str4 != null && "Native crash".equals(str2)) {
                    j2 = this.mAppErrorsExt.getVmSize(processRecord.getPid(), processRecord.processName);
                }
                j = j2;
                z = this.mAppErrorsExt.isAppForeground(processRecord.processName);
            } else {
                j = 0;
                z = false;
            }
            int computeRelaunchReason = processRecord == null ? processRecord.getWindowProcessController().computeRelaunchReason() : 0;
            AppErrorResult appErrorResult = new AppErrorResult();
            activityManagerService = this.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                int i3 = computeRelaunchReason;
                boolean z2 = z;
                long j3 = j;
                try {
                    if (handleAppCrashInActivityController(processRecord, crashInfo, str2, str, str4, currentTimeMillis, i, i2)) {
                        return;
                    }
                    if (i3 == 2) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (processRecord != null && processRecord.getActiveInstrumentation() != null) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (processRecord != null) {
                        this.mService.mBatteryStatsService.noteProcessCrash(processRecord.processName, processRecord.uid);
                    }
                    AppErrorDialog.Data data = new AppErrorDialog.Data();
                    data.result = appErrorResult;
                    data.proc = processRecord;
                    data.crashInfo = crashInfo;
                    data.vmSize = j3;
                    data.isForeground = z2;
                    if (processRecord != null && makeAppCrashingLocked(processRecord, str2, str, str4, data)) {
                        Message obtain = Message.obtain();
                        obtain.what = 1;
                        int i4 = data.taskId;
                        obtain.obj = data;
                        this.mService.mUiHandler.sendMessage(obtain);
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        int i5 = appErrorResult.get();
                        MetricsLogger.action(this.mContext, FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_SHELL, i5);
                        if (i5 == 6 || i5 == 7) {
                            i5 = 1;
                        }
                        if (i5 == 1) {
                            long clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                this.mService.mAtmInternal.onHandleAppCrash(processRecord.getWindowProcessController());
                                if (!processRecord.isPersistent()) {
                                    ActivityManagerService activityManagerService3 = this.mService;
                                    ActivityManagerService.boostPriorityForLockedSection();
                                    synchronized (activityManagerService3) {
                                        try {
                                            this.mService.mProcessList.removeProcessLocked(processRecord, false, false, 4, "crash");
                                        } finally {
                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                        }
                                    }
                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                    this.mService.mAtmInternal.resumeTopActivities(false);
                                }
                            } finally {
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                            }
                        } else {
                            if (i5 == 2) {
                                ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                                ActivityManagerService.boostPriorityForProcLockedSection();
                                synchronized (activityManagerGlobalLock) {
                                    try {
                                        intent = createAppErrorIntentLOSP(processRecord, currentTimeMillis, crashInfo);
                                    } catch (Throwable th) {
                                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                                        throw th;
                                    }
                                }
                                ActivityManagerService.resetPriorityAfterProcLockedSection();
                            } else if (i5 == 3) {
                                ActivityManagerService activityManagerService4 = this.mService;
                                ActivityManagerService.boostPriorityForLockedSection();
                                synchronized (activityManagerService4) {
                                    try {
                                        this.mService.mProcessList.removeProcessLocked(processRecord, false, true, 4, "crash");
                                    } finally {
                                        ActivityManagerService.resetPriorityAfterLockedSection();
                                    }
                                }
                                ActivityManagerService.resetPriorityAfterLockedSection();
                                if (i4 != -1) {
                                    try {
                                        this.mService.startActivityFromRecents(i4, ActivityOptions.makeBasic().toBundle());
                                    } catch (IllegalArgumentException e) {
                                        Slog.e("ActivityManager", "Could not restart taskId=" + i4, e);
                                    }
                                }
                            } else if (i5 == 5) {
                                synchronized (this.mBadProcessLock) {
                                    stopReportingCrashesLBp(processRecord);
                                }
                            } else if (i5 == 8) {
                                intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.parse("package:" + processRecord.info.packageName));
                                intent.addFlags(AudioFormat.EVRC);
                            }
                            if (intent != null) {
                                try {
                                    this.mContext.startActivityAsUser(intent, new UserHandle(processRecord.userId));
                                } catch (ActivityNotFoundException e2) {
                                    Slog.w("ActivityManager", "bug report receiver dissappeared", e2);
                                }
                            }
                            this.mAppErrorsExt.doErrorsStatistics(this.mContext, processRecord, crashInfo);
                            return;
                        }
                        intent = null;
                        if (intent != null) {
                        }
                        this.mAppErrorsExt.doErrorsStatistics(this.mContext, processRecord, crashInfo);
                        return;
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                } finally {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }
        }
        str = str3;
        long j22 = 0;
        if (processRecord == null) {
        }
        if (processRecord == null) {
        }
        AppErrorResult appErrorResult2 = new AppErrorResult();
        activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
        }
    }

    @GuardedBy({"mService"})
    private boolean handleAppCrashInActivityController(final ProcessRecord processRecord, final ApplicationErrorReport.CrashInfo crashInfo, final String str, final String str2, final String str3, long j, int i, int i2) {
        AppErrors appErrors;
        final int i3;
        final String str4 = processRecord != null ? processRecord.processName : null;
        int pid = processRecord != null ? processRecord.getPid() : i;
        if (processRecord != null) {
            appErrors = this;
            i3 = processRecord.info.uid;
        } else {
            appErrors = this;
            i3 = i2;
        }
        final int i4 = pid;
        return appErrors.mService.mAtmInternal.handleAppCrashInActivityController(str4, pid, str, str2, j, crashInfo.stackTrace, new Runnable() { // from class: com.android.server.am.AppErrors$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppErrors.this.lambda$handleAppCrashInActivityController$1(crashInfo, str4, i4, processRecord, str, str2, str3, i3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleAppCrashInActivityController$1(ApplicationErrorReport.CrashInfo crashInfo, String str, int i, ProcessRecord processRecord, String str2, String str3, String str4, int i2) {
        if (Build.IS_DEBUGGABLE && "Native crash".equals(crashInfo.exceptionClassName)) {
            Slog.w("ActivityManager", "Skip killing native crashed app " + str + "(" + i + ") during testing");
            return;
        }
        Slog.w("ActivityManager", "Force-killing crashed app " + str + " at watcher's request");
        if (processRecord != null) {
            if (makeAppCrashingLocked(processRecord, str2, str3, str4, null)) {
                return;
            }
            processRecord.killLocked("crash", 4, true);
        } else {
            if (this.mAppErrorsExt.isThreadGroupLeader("ActivityManager", i)) {
                return;
            }
            Process.killProcess(i);
            ProcessList.killProcessGroup(i2, i);
            this.mService.mProcessList.noteAppKill(i, i2, 4, 0, "crash");
        }
    }

    @GuardedBy({"mService"})
    private boolean makeAppCrashingLocked(ProcessRecord processRecord, String str, String str2, String str3, AppErrorDialog.Data data) {
        boolean handleAppCrashLSPB;
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                ProcessErrorStateRecord processErrorStateRecord = processRecord.mErrorState;
                processErrorStateRecord.setCrashing(true);
                processErrorStateRecord.setCrashingReport(generateProcessError(processRecord, 1, null, str, str2, str3));
                processErrorStateRecord.startAppProblemLSP();
                processRecord.getWindowProcessController().stopFreezingActivities();
                synchronized (this.mBadProcessLock) {
                    handleAppCrashLSPB = handleAppCrashLSPB(processRecord, "force-crash", str, str2, str3, data);
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        return handleAppCrashLSPB;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManager.ProcessErrorStateInfo generateProcessError(ProcessRecord processRecord, int i, String str, String str2, String str3, String str4) {
        ActivityManager.ProcessErrorStateInfo processErrorStateInfo = new ActivityManager.ProcessErrorStateInfo();
        processErrorStateInfo.condition = i;
        processErrorStateInfo.processName = processRecord.processName;
        processErrorStateInfo.pid = processRecord.getPid();
        processErrorStateInfo.uid = processRecord.info.uid;
        processErrorStateInfo.tag = str;
        processErrorStateInfo.shortMsg = str2;
        processErrorStateInfo.longMsg = str3;
        processErrorStateInfo.stackTrace = str4;
        return processErrorStateInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public Intent createAppErrorIntentLOSP(ProcessRecord processRecord, long j, ApplicationErrorReport.CrashInfo crashInfo) {
        ApplicationErrorReport createAppErrorReportLOSP = createAppErrorReportLOSP(processRecord, j, crashInfo);
        if (createAppErrorReportLOSP == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.APP_ERROR");
        intent.setComponent(processRecord.mErrorState.getErrorReportReceiver());
        intent.putExtra("android.intent.extra.BUG_REPORT", createAppErrorReportLOSP);
        intent.addFlags(AudioFormat.EVRC);
        return intent;
    }

    @GuardedBy(anyOf = {"mService", "mProcLock"})
    private ApplicationErrorReport createAppErrorReportLOSP(ProcessRecord processRecord, long j, ApplicationErrorReport.CrashInfo crashInfo) {
        ProcessErrorStateRecord processErrorStateRecord = processRecord.mErrorState;
        if (processErrorStateRecord.getErrorReportReceiver() == null) {
            return null;
        }
        if (!processErrorStateRecord.isCrashing() && !processErrorStateRecord.isNotResponding() && !processErrorStateRecord.isForceCrashReport()) {
            return null;
        }
        ApplicationErrorReport applicationErrorReport = new ApplicationErrorReport();
        applicationErrorReport.packageName = processRecord.info.packageName;
        applicationErrorReport.installerPackageName = processErrorStateRecord.getErrorReportReceiver().getPackageName();
        applicationErrorReport.processName = processRecord.processName;
        applicationErrorReport.time = j;
        applicationErrorReport.systemApp = (processRecord.info.flags & 1) != 0;
        if (processErrorStateRecord.isCrashing() || processErrorStateRecord.isForceCrashReport()) {
            applicationErrorReport.type = 1;
            applicationErrorReport.crashInfo = crashInfo;
        } else if (processErrorStateRecord.isNotResponding()) {
            ActivityManager.ProcessErrorStateInfo notRespondingReport = processErrorStateRecord.getNotRespondingReport();
            if (notRespondingReport == null) {
                return null;
            }
            applicationErrorReport.type = 2;
            ApplicationErrorReport.AnrInfo anrInfo = new ApplicationErrorReport.AnrInfo();
            applicationErrorReport.anrInfo = anrInfo;
            anrInfo.activity = notRespondingReport.tag;
            anrInfo.cause = notRespondingReport.shortMsg;
            anrInfo.info = notRespondingReport.longMsg;
        }
        return applicationErrorReport;
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00c2  */
    @GuardedBy({"mService", "mProcLock", "mBadProcessLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleAppCrashLSPB(ProcessRecord processRecord, String str, String str2, String str3, String str4, AppErrorDialog.Data data) {
        Long l;
        Long l2;
        ProcessErrorStateRecord processErrorStateRecord;
        boolean z;
        boolean z2;
        boolean z3;
        long j;
        int i;
        ProcessErrorStateRecord processErrorStateRecord2;
        boolean z4;
        WindowProcessController windowProcessController;
        int i2;
        int i3;
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z5 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "anr_show_background", 0, this.mService.mUserController.getCurrentUserId()) != 0;
        this.mAppErrorsExt.hookHandleAppCrashBegin(processRecord);
        String str5 = processRecord.processName;
        int i4 = processRecord.uid;
        int i5 = processRecord.userId;
        boolean z6 = processRecord.isolated;
        boolean isPersistent = processRecord.isPersistent();
        WindowProcessController windowProcessController2 = processRecord.getWindowProcessController();
        ProcessErrorStateRecord processErrorStateRecord3 = processRecord.mErrorState;
        if (processRecord.isolated) {
            l = null;
            l2 = null;
        } else {
            l = (Long) this.mProcessCrashTimes.get(str5, i4);
            l2 = (Long) this.mProcessCrashTimesPersistent.get(str5, i4);
        }
        boolean incServiceCrashCountLocked = processRecord.mServices.incServiceCrashCountLocked(uptimeMillis);
        if (l != null) {
            processErrorStateRecord = processErrorStateRecord3;
            if (uptimeMillis < l.longValue() + ActivityManagerConstants.MIN_CRASH_INTERVAL) {
                z = true;
                if (!z || isProcOverCrashLimitLBp(processRecord, uptimeMillis)) {
                    z2 = incServiceCrashCountLocked;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Process ");
                    sb.append(str5);
                    sb.append(" has crashed too many times, killing! Reason: ");
                    sb.append(!z ? "crashed quickly" : "over process crash limit");
                    Slog.w("ActivityManager", sb.toString());
                    EventLog.writeEvent(EventLogTags.AM_PROCESS_CRASHED_TOO_MUCH, Integer.valueOf(i5), str5, Integer.valueOf(i4));
                    this.mService.mAtmInternal.onHandleAppCrash(windowProcessController2);
                    if (isPersistent) {
                        EventLog.writeEvent(EventLogTags.AM_PROC_BAD, Integer.valueOf(i5), Integer.valueOf(i4), str5);
                        if (z6) {
                            z3 = z6;
                            j = uptimeMillis;
                            i = i4;
                            processErrorStateRecord2 = processErrorStateRecord;
                            windowProcessController = windowProcessController2;
                            i2 = i5;
                        } else {
                            i = i4;
                            processErrorStateRecord2 = processErrorStateRecord;
                            windowProcessController = windowProcessController2;
                            z3 = z6;
                            j = uptimeMillis;
                            i2 = i5;
                            markBadProcess(str5, processRecord.uid, new BadProcessInfo(uptimeMillis, str2, str3, str4));
                            this.mProcessCrashTimes.remove(str5, processRecord.uid);
                            this.mProcessCrashCounts.remove(str5, processRecord.uid);
                        }
                        processErrorStateRecord2.setBad(true);
                        processRecord.setRemoved(true);
                        AppStandbyInternal appStandbyInternal = (AppStandbyInternal) LocalServices.getService(AppStandbyInternal.class);
                        if (appStandbyInternal != null) {
                            appStandbyInternal.restrictApp(processRecord.info != null ? processRecord.info.packageName : str5, i2, 4);
                        }
                        this.mService.mProcessList.removeProcessLocked(processRecord, false, z2, 4, "crash");
                        z4 = false;
                        this.mService.mAtmInternal.resumeTopActivities(false);
                        if (!z5) {
                            return false;
                        }
                    } else {
                        z3 = z6;
                        j = uptimeMillis;
                        i = i4;
                        processErrorStateRecord2 = processErrorStateRecord;
                        z4 = false;
                        windowProcessController = windowProcessController2;
                    }
                    this.mService.mAtmInternal.resumeTopActivities(z4);
                } else {
                    int finishTopCrashedActivities = this.mService.mAtmInternal.finishTopCrashedActivities(windowProcessController2, str);
                    if (data != null) {
                        data.taskId = finishTopCrashedActivities;
                    }
                    if (data == null || l2 == null) {
                        z2 = incServiceCrashCountLocked;
                    } else {
                        z2 = incServiceCrashCountLocked;
                        if (uptimeMillis < l2.longValue() + ActivityManagerConstants.MIN_CRASH_INTERVAL) {
                            data.repeating = true;
                        }
                    }
                    z3 = z6;
                    j = uptimeMillis;
                    i = i4;
                    processErrorStateRecord2 = processErrorStateRecord;
                    windowProcessController = windowProcessController2;
                }
                if (data == null && z2) {
                    i3 = 1;
                    data.isRestartableForService = true;
                } else {
                    i3 = 1;
                }
                if (windowProcessController.isHomeProcess() && windowProcessController.hasActivities() && (processRecord.info.flags & i3) == 0) {
                    windowProcessController.clearPackagePreferredForHomeActivities();
                }
                if (!z3) {
                    int i6 = i;
                    this.mProcessCrashTimes.put(str5, i6, Long.valueOf(j));
                    this.mProcessCrashTimesPersistent.put(str5, i6, Long.valueOf(j));
                    updateProcessCrashCountLBp(str5, i6, j);
                }
                if (processErrorStateRecord2.getCrashHandler() != null) {
                    return true;
                }
                this.mService.mHandler.post(processErrorStateRecord2.getCrashHandler());
                return true;
            }
        } else {
            processErrorStateRecord = processErrorStateRecord3;
        }
        z = false;
        if (!z) {
        }
        z2 = incServiceCrashCountLocked;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Process ");
        sb2.append(str5);
        sb2.append(" has crashed too many times, killing! Reason: ");
        sb2.append(!z ? "crashed quickly" : "over process crash limit");
        Slog.w("ActivityManager", sb2.toString());
        EventLog.writeEvent(EventLogTags.AM_PROCESS_CRASHED_TOO_MUCH, Integer.valueOf(i5), str5, Integer.valueOf(i4));
        this.mService.mAtmInternal.onHandleAppCrash(windowProcessController2);
        if (isPersistent) {
        }
        this.mService.mAtmInternal.resumeTopActivities(z4);
        if (data == null) {
        }
        i3 = 1;
        if (windowProcessController.isHomeProcess()) {
            windowProcessController.clearPackagePreferredForHomeActivities();
        }
        if (!z3) {
        }
        if (processErrorStateRecord2.getCrashHandler() != null) {
        }
    }

    @GuardedBy({"mBadProcessLock"})
    private void updateProcessCrashCountLBp(String str, int i, long j) {
        Pair pair;
        Pair pair2 = (Pair) this.mProcessCrashCounts.get(str, i);
        if (pair2 == null || ((Long) pair2.first).longValue() + ActivityManagerConstants.PROCESS_CRASH_COUNT_RESET_INTERVAL < j) {
            pair = new Pair(Long.valueOf(j), 1);
        } else {
            pair = new Pair((Long) pair2.first, Integer.valueOf(((Integer) pair2.second).intValue() + 1));
        }
        this.mProcessCrashCounts.put(str, i, pair);
    }

    @GuardedBy({"mBadProcessLock"})
    private boolean isProcOverCrashLimitLBp(ProcessRecord processRecord, long j) {
        Pair pair = (Pair) this.mProcessCrashCounts.get(processRecord.processName, processRecord.uid);
        return !processRecord.isolated && pair != null && j < ((Long) pair.first).longValue() + ActivityManagerConstants.PROCESS_CRASH_COUNT_RESET_INTERVAL && ((Integer) pair.second).intValue() >= ActivityManagerConstants.PROCESS_CRASH_COUNT_LIMIT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013f A[Catch: all -> 0x01db, TRY_LEAVE, TryCatch #2 {all -> 0x01db, blocks: (B:49:0x00c5, B:51:0x00c9, B:52:0x00d7, B:55:0x00e9, B:58:0x00f4, B:61:0x010e, B:63:0x0116, B:66:0x0123, B:68:0x0129, B:72:0x013f), top: B:48:0x00c5 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01a9 A[Catch: all -> 0x01e0, TryCatch #0 {all -> 0x01e0, blocks: (B:74:0x014d, B:75:0x0161, B:78:0x01b7, B:80:0x01d0, B:81:0x01d5, B:89:0x0175, B:91:0x0179, B:93:0x01a9, B:104:0x01de), top: B:46:0x00c4 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0159  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleShowAppErrorUi(Message message) {
        ProcessRecord processRecord;
        boolean z;
        Object obj;
        long j;
        ProcessRecord processRecord2;
        int i;
        AppErrorDialog.Data data = (AppErrorDialog.Data) message.obj;
        boolean z2 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "anr_show_background", 0, this.mService.mUserController.getCurrentUserId()) != 0;
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                ProcessRecord processRecord3 = data.proc;
                AppErrorResult appErrorResult = data.result;
                if (processRecord3 == null) {
                    Slog.e("ActivityManager", "handleShowAppErrorUi: proc is null");
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                ProcessErrorStateRecord processErrorStateRecord = processRecord3.mErrorState;
                int i2 = processRecord3.userId;
                if (processErrorStateRecord.getDialogController().hasCrashDialogs()) {
                    Slog.e("ActivityManager", "App already has crash dialog: " + processRecord3);
                    if (appErrorResult != null) {
                        appErrorResult.set(AppErrorDialog.ALREADY_SHOWING);
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                boolean z3 = UserHandle.getAppId(processRecord3.uid) >= 10000 && processRecord3.getPid() != ActivityManagerService.MY_PID;
                for (int i3 : this.mService.mUserController.getCurrentProfileIds()) {
                    z3 &= i2 != i3;
                }
                if (z3 && !z2) {
                    Slog.w("ActivityManager", "Skipping crash dialog of " + processRecord3 + ": background");
                    if (appErrorResult != null) {
                        appErrorResult.set(AppErrorDialog.BACKGROUND_USER);
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                Object obj2 = this.mBadProcessLock;
                try {
                    synchronized (obj2) {
                        try {
                            Long l = !processRecord3.isolated ? (Long) this.mProcessCrashShowDialogTimes.get(processRecord3.processName, processRecord3.uid) : null;
                            boolean z4 = Settings.Global.getInt(this.mContext.getContentResolver(), "show_first_crash_dialog", 0) != 0;
                            if (this.mService.mActivityManagerServiceExt.isChinaModel()) {
                                z4 = true;
                            }
                            boolean z5 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "show_first_crash_dialog_dev_option", 0, this.mService.mUserController.getCurrentUserId()) != 0;
                            String str = processRecord3.info.packageName;
                            ArraySet<String> arraySet = this.mAppsNotReportingCrashes;
                            boolean z6 = arraySet != null && arraySet.contains(processRecord3.info.packageName);
                            long uptimeMillis = SystemClock.uptimeMillis();
                            if (l != null) {
                                processRecord = processRecord3;
                                if (uptimeMillis < l.longValue() + ActivityManagerConstants.MIN_CRASH_INTERVAL) {
                                    z = true;
                                    if (z) {
                                        obj = obj2;
                                        j = uptimeMillis;
                                        processRecord2 = processRecord;
                                        i = i2;
                                    } else {
                                        j = uptimeMillis;
                                        processRecord2 = processRecord;
                                        i = i2;
                                        obj = obj2;
                                        this.mAppErrorsExt.doUpload(this.mContext, str, String.valueOf(processRecord2.info.longVersionCode), UPLOAD_VALUE_TOW_MINUTE_LIMIT);
                                    }
                                    if ((!this.mService.mAtmInternal.canShowErrorDialogs() || z2) && !z6 && !z && (z4 || z5 || data.repeating)) {
                                        this.mAppErrorsExt.hookHandleShowAppErrorUi(processErrorStateRecord, appErrorResult, data);
                                        this.mService.mActivityManagerServiceExt.hookHandleApplicationCrashDialog(processRecord2, data);
                                        Slog.i("ActivityManager", "Showing crash dialog for package " + str + " u" + i);
                                        if (!processRecord2.isolated) {
                                            this.mProcessCrashShowDialogTimes.put(processRecord2.processName, processRecord2.uid, Long.valueOf(j));
                                        }
                                    } else {
                                        Slog.i("ActivityManager", "hookHandleApplicationCrashBeforeInner do not showErrorDialog shouldThottle= " + z);
                                        if (appErrorResult != null) {
                                            appErrorResult.set(AppErrorDialog.CANT_SHOW);
                                        }
                                    }
                                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                                    return;
                                }
                            } else {
                                processRecord = processRecord3;
                            }
                            z = false;
                            if (z) {
                            }
                            if (this.mService.mAtmInternal.canShowErrorDialogs()) {
                            }
                            this.mAppErrorsExt.hookHandleShowAppErrorUi(processErrorStateRecord, appErrorResult, data);
                            this.mService.mActivityManagerServiceExt.hookHandleApplicationCrashDialog(processRecord2, data);
                            Slog.i("ActivityManager", "Showing crash dialog for package " + str + " u" + i);
                            if (!processRecord2.isolated) {
                            }
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            return;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                throw th;
            } catch (Throwable th3) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th3;
            }
        }
    }

    @GuardedBy({"mBadProcessLock"})
    private void stopReportingCrashesLBp(ProcessRecord processRecord) {
        if (this.mAppsNotReportingCrashes == null) {
            this.mAppsNotReportingCrashes = new ArraySet<>();
        }
        this.mAppsNotReportingCrashes.add(processRecord.info.packageName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleShowAnrUi(Message message) {
        AppNotRespondingDialog.Data data = (AppNotRespondingDialog.Data) message.obj;
        ProcessRecord processRecord = data.proc;
        if (processRecord == null) {
            Slog.e("ActivityManager", "handleShowAnrUi: proc is null");
            return;
        }
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                ProcessErrorStateRecord processErrorStateRecord = processRecord.mErrorState;
                processErrorStateRecord.setAnrData(data);
                List<VersionedPackage> packageListWithVersionCode = !processRecord.isPersistent() ? processRecord.getPackageListWithVersionCode() : null;
                if (processErrorStateRecord.getDialogController().hasAnrDialogs()) {
                    Slog.e("ActivityManager", "App already has anr dialog: " + processRecord);
                    MetricsLogger.action(this.mContext, FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_SESSION_CALLBACK, -2);
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                }
                boolean z = false;
                boolean z2 = true;
                boolean z3 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "anr_show_background", 0, this.mService.mUserController.getCurrentUserId()) != 0;
                if (!this.mService.mAtmInternal.canShowErrorDialogs() && !z3) {
                    MetricsLogger.action(this.mContext, FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_SESSION_CALLBACK, -1);
                    if (processErrorStateRecord.mProcessErrorStateRecordExt.isTheiaAnrTestApp(processRecord.processName)) {
                        z = z2;
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    if (z) {
                        this.mService.killAppAtUsersRequest(processRecord);
                    }
                    if (packageListWithVersionCode == null) {
                        this.mPackageWatchdog.onPackageFailure(packageListWithVersionCode, 4);
                        return;
                    }
                    return;
                }
                AnrController anrController = processErrorStateRecord.getDialogController().getAnrController();
                boolean isShowOriAnrDialog = this.mAppErrorsExt.isShowOriAnrDialog(processRecord);
                if (anrController == null) {
                    if (isShowOriAnrDialog) {
                        processErrorStateRecord.getDialogController().showAnrDialogs(data);
                        processRecord.mErrorState.mProcessErrorStateRecordExt.notifyTheiaAnrFinished(processRecord.mPid, processRecord.uid, processRecord.processName, "dialog");
                    } else if (!this.mAppErrorsExt.isPersistProcessRestarting(processRecord, this.mService)) {
                    }
                    z2 = false;
                } else {
                    String str = processRecord.info.packageName;
                    if (!anrController.onAnrDelayCompleted(str, processRecord.info.uid)) {
                        Slog.d("ActivityManager", "ANR delay completed. Cancelling ANR dialog for package: " + str);
                        processErrorStateRecord.setNotResponding(false);
                        processErrorStateRecord.setNotRespondingReport(null);
                        processErrorStateRecord.getDialogController().clearAnrDialogs();
                    } else if (isShowOriAnrDialog) {
                        Slog.d("ActivityManager", "ANR delay completed. Showing ANR dialog for package: " + str);
                        processErrorStateRecord.getDialogController().showAnrDialogs(data);
                    } else if (!this.mAppErrorsExt.isPersistProcessRestarting(processRecord, this.mService)) {
                    }
                    z2 = false;
                }
                if (processErrorStateRecord.mProcessErrorStateRecordExt.isTheiaAnrTestApp(processRecord.processName)) {
                }
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                if (z) {
                }
                if (packageListWithVersionCode == null) {
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleDismissAnrDialogs(ProcessRecord processRecord) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                ProcessErrorStateRecord processErrorStateRecord = processRecord.mErrorState;
                this.mService.mUiHandler.removeMessages(2, processErrorStateRecord.getAnrData());
                if (processErrorStateRecord.getDialogController().hasAnrDialogs()) {
                    processErrorStateRecord.setNotResponding(false);
                    processErrorStateRecord.setNotRespondingReport(null);
                    processErrorStateRecord.getDialogController().clearAnrDialogs();
                }
                processRecord.mErrorState.setAnrData(null);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class BadProcessInfo {
        final String longMsg;
        final String shortMsg;
        final String stack;
        final long time;

        BadProcessInfo(long j, String str, String str2, String str3) {
            this.time = j;
            this.shortMsg = str;
            this.longMsg = str2;
            this.stack = str3;
        }
    }
}
