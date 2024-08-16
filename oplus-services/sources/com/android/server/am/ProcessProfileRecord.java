package com.android.server.am;

import android.app.IApplicationThread;
import android.app.ProcessMemoryState;
import android.content.pm.ApplicationInfo;
import android.os.Debug;
import android.os.SystemClock;
import android.util.DebugUtils;
import android.util.TimeUtils;
import com.android.internal.annotations.CompositeRWLock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.procstats.ProcessState;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.am.ProcessList;
import com.android.server.power.stats.BatteryStatsImpl;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProcessProfileRecord {
    final ProcessRecord mApp;

    @GuardedBy({"mService.mProcessStats.mLock"})
    private ProcessState mBaseProcessTracker;
    private BatteryStatsImpl.Uid.Proc mCurProcBatteryStats;

    @GuardedBy({"mProfilerLock"})
    private int mCurRawAdj;

    @GuardedBy({"mProfilerLock"})
    private long mInitialIdlePss;

    @GuardedBy({"mProfilerLock"})
    private long mLastCachedPss;

    @GuardedBy({"mProfilerLock"})
    private long mLastCachedSwapPss;

    @CompositeRWLock({"mService", "mProfilerLock"})
    private long mLastLowMemory;

    @GuardedBy({"mProfilerLock"})
    private Debug.MemoryInfo mLastMemInfo;

    @GuardedBy({"mProfilerLock"})
    private long mLastMemInfoTime;

    @GuardedBy({"mProfilerLock"})
    private long mLastPss;

    @GuardedBy({"mProfilerLock"})
    private long mLastPssTime;

    @GuardedBy({"mProfilerLock"})
    private long mLastRequestedGc;

    @GuardedBy({"mProfilerLock"})
    private long mLastRss;

    @GuardedBy({"mProfilerLock"})
    private long mLastStateTime;

    @GuardedBy({"mProfilerLock"})
    private long mLastSwapPss;

    @GuardedBy({"mProfilerLock"})
    private long mNextPssTime;

    @GuardedBy({"mProcLock"})
    private boolean mPendingUiClean;

    @GuardedBy({"mProfilerLock"})
    private int mPid;
    private final ActivityManagerGlobalLock mProcLock;
    final Object mProfilerLock;

    @GuardedBy({"mProfilerLock"})
    private int mPssStatType;

    @GuardedBy({"mProfilerLock"})
    private boolean mReportLowMemory;
    private final ActivityManagerService mService;

    @GuardedBy({"mProfilerLock"})
    private int mSetAdj;

    @GuardedBy({"mProfilerLock"})
    private int mSetProcState;

    @GuardedBy({"mProfilerLock"})
    private IApplicationThread mThread;

    @CompositeRWLock({"mService", "mProcLock"})
    private int mTrimMemoryLevel;

    @GuardedBy({"mProfilerLock"})
    private final ProcessList.ProcStateMemTracker mProcStateMemTracker = new ProcessList.ProcStateMemTracker();

    @GuardedBy({"mProfilerLock"})
    private int mPssProcState = 20;
    final AtomicLong mLastCpuTime = new AtomicLong(0);
    final AtomicLong mCurCpuTime = new AtomicLong(0);
    private AtomicInteger mCurrentHostingComponentTypes = new AtomicInteger(0);
    private AtomicInteger mHistoricalHostingComponentTypes = new AtomicInteger(0);

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessProfileRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        ActivityManagerService activityManagerService = processRecord.mService;
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        this.mProfilerLock = activityManagerService.mAppProfiler.mProfilerLock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(long j) {
        this.mNextPssTime = j;
        this.mLastPssTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService.mProcessStats.mLock"})
    public ProcessState getBaseProcessTracker() {
        return this.mBaseProcessTracker;
    }

    @GuardedBy({"mService.mProcessStats.mLock"})
    void setBaseProcessTracker(ProcessState processState) {
        this.mBaseProcessTracker = processState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onProcessActive(IApplicationThread iApplicationThread, final ProcessStatsService processStatsService) {
        if (this.mThread == null) {
            synchronized (this.mProfilerLock) {
                synchronized (processStatsService.mLock) {
                    final ProcessState baseProcessTracker = getBaseProcessTracker();
                    PackageList pkgList = this.mApp.getPkgList();
                    if (baseProcessTracker != null) {
                        synchronized (pkgList) {
                            baseProcessTracker.setState(-1, processStatsService.getMemFactorLocked(), SystemClock.uptimeMillis(), pkgList.getPackageListLocked());
                        }
                        baseProcessTracker.makeInactive();
                    }
                    ApplicationInfo applicationInfo = this.mApp.info;
                    final ProcessState processStateLocked = processStatsService.getProcessStateLocked(applicationInfo.packageName, applicationInfo.uid, applicationInfo.longVersionCode, this.mApp.processName);
                    setBaseProcessTracker(processStateLocked);
                    processStateLocked.makeActive();
                    pkgList.forEachPackage(new BiConsumer() { // from class: com.android.server.am.ProcessProfileRecord$$ExternalSyntheticLambda0
                        @Override // java.util.function.BiConsumer
                        public final void accept(Object obj, Object obj2) {
                            ProcessProfileRecord.this.lambda$onProcessActive$0(baseProcessTracker, processStatsService, processStateLocked, (String) obj, (ProcessStats.ProcessStateHolder) obj2);
                        }
                    });
                    this.mThread = iApplicationThread;
                }
            }
            return;
        }
        synchronized (this.mProfilerLock) {
            this.mThread = iApplicationThread;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onProcessActive$0(ProcessState processState, ProcessStatsService processStatsService, ProcessState processState2, String str, ProcessStats.ProcessStateHolder processStateHolder) {
        ProcessState processState3 = processStateHolder.state;
        if (processState3 != null && processState3 != processState) {
            processState3.makeInactive();
        }
        processStatsService.updateProcessStateHolderLocked(processStateHolder, str, this.mApp.info.uid, this.mApp.info.longVersionCode, this.mApp.processName);
        ProcessState processState4 = processStateHolder.state;
        if (processState4 != processState2) {
            processState4.makeActive();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onProcessInactive(ProcessStatsService processStatsService) {
        synchronized (this.mProfilerLock) {
            synchronized (processStatsService.mLock) {
                final ProcessState baseProcessTracker = getBaseProcessTracker();
                if (baseProcessTracker != null) {
                    PackageList pkgList = this.mApp.getPkgList();
                    synchronized (pkgList) {
                        baseProcessTracker.setState(-1, processStatsService.getMemFactorLocked(), SystemClock.uptimeMillis(), pkgList.getPackageListLocked());
                    }
                    baseProcessTracker.makeInactive();
                    setBaseProcessTracker(null);
                    pkgList.forEachPackageProcessStats(new Consumer() { // from class: com.android.server.am.ProcessProfileRecord$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ProcessProfileRecord.lambda$onProcessInactive$1(baseProcessTracker, (ProcessStats.ProcessStateHolder) obj);
                        }
                    });
                }
                this.mThread = null;
            }
        }
        this.mCurrentHostingComponentTypes.set(0);
        this.mHistoricalHostingComponentTypes.set(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onProcessInactive$1(ProcessState processState, ProcessStats.ProcessStateHolder processStateHolder) {
        ProcessState processState2 = processStateHolder.state;
        if (processState2 != null && processState2 != processState) {
            processState2.makeInactive();
        }
        processStateHolder.pkg = null;
        processStateHolder.state = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastPssTime() {
        return this.mLastPssTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastPssTime(long j) {
        this.mLastPssTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getNextPssTime() {
        return this.mNextPssTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setNextPssTime(long j) {
        this.mNextPssTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getInitialIdlePss() {
        return this.mInitialIdlePss;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setInitialIdlePss(long j) {
        this.mInitialIdlePss = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastPss() {
        return this.mLastPss;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastPss(long j) {
        this.mLastPss = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastCachedPss() {
        return this.mLastCachedPss;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastCachedPss(long j) {
        this.mLastCachedPss = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastSwapPss() {
        return this.mLastSwapPss;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastSwapPss(long j) {
        this.mLastSwapPss = j;
    }

    @GuardedBy({"mProfilerLock"})
    long getLastCachedSwapPss() {
        return this.mLastCachedSwapPss;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastCachedSwapPss(long j) {
        this.mLastCachedSwapPss = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastRss() {
        return this.mLastRss;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastRss(long j) {
        this.mLastRss = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public Debug.MemoryInfo getLastMemInfo() {
        return this.mLastMemInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastMemInfo(Debug.MemoryInfo memoryInfo) {
        this.mLastMemInfo = memoryInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastMemInfoTime() {
        return this.mLastMemInfoTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastMemInfoTime(long j) {
        this.mLastMemInfoTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public int getPssProcState() {
        return this.mPssProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setPssProcState(int i) {
        this.mPssProcState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public int getPssStatType() {
        return this.mPssStatType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setPssStatType(int i) {
        this.mPssStatType = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProcLock"})
    public int getTrimMemoryLevel() {
        return this.mTrimMemoryLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void setTrimMemoryLevel(int i) {
        this.mTrimMemoryLevel = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasPendingUiClean() {
        return this.mPendingUiClean;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setPendingUiClean(boolean z) {
        this.mPendingUiClean = z;
        this.mApp.getWindowProcessController().setPendingUiClean(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BatteryStatsImpl.Uid.Proc getCurProcBatteryStats() {
        return this.mCurProcBatteryStats;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurProcBatteryStats(BatteryStatsImpl.Uid.Proc proc) {
        this.mCurProcBatteryStats = proc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastRequestedGc() {
        return this.mLastRequestedGc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setLastRequestedGc(long j) {
        this.mLastRequestedGc = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy(anyOf = {"mService", "mProfilerLock"})
    public long getLastLowMemory() {
        return this.mLastLowMemory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProfilerLock"})
    public void setLastLowMemory(long j) {
        this.mLastLowMemory = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public boolean getReportLowMemory() {
        return this.mReportLowMemory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setReportLowMemory(boolean z) {
        this.mReportLowMemory = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPss(long j, long j2, long j3, boolean z, int i, long j4) {
        synchronized (this.mService.mProcessStats.mLock) {
            ProcessState processState = this.mBaseProcessTracker;
            if (processState != null) {
                PackageList pkgList = this.mApp.getPkgList();
                synchronized (pkgList) {
                    processState.addPss(j, j2, j3, z, i, j4, pkgList.getPackageListLocked());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportExcessiveCpu() {
        synchronized (this.mService.mProcessStats.mLock) {
            ProcessState processState = this.mBaseProcessTracker;
            if (processState != null) {
                PackageList pkgList = this.mApp.getPkgList();
                synchronized (pkgList) {
                    processState.reportExcessiveCpu(pkgList.getPackageListLocked());
                }
            }
        }
    }

    void reportCachedKill() {
        synchronized (this.mService.mProcessStats.mLock) {
            ProcessState processState = this.mBaseProcessTracker;
            if (processState != null) {
                PackageList pkgList = this.mApp.getPkgList();
                synchronized (pkgList) {
                    processState.reportCachedKill(pkgList.getPackageListLocked(), this.mLastCachedPss);
                    pkgList.forEachPackageProcessStats(new Consumer() { // from class: com.android.server.am.ProcessProfileRecord$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ProcessProfileRecord.this.lambda$reportCachedKill$2((ProcessStats.ProcessStateHolder) obj);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportCachedKill$2(ProcessStats.ProcessStateHolder processStateHolder) {
        FrameworkStatsLog.write(17, this.mApp.info.uid, processStateHolder.state.getName(), processStateHolder.state.getPackage(), this.mLastCachedPss, processStateHolder.appVersion);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProcessTrackerState(int i, int i2) {
        synchronized (this.mService.mProcessStats.mLock) {
            ProcessState processState = this.mBaseProcessTracker;
            if (processState != null && i != 20) {
                PackageList pkgList = this.mApp.getPkgList();
                long uptimeMillis = SystemClock.uptimeMillis();
                synchronized (pkgList) {
                    processState.setState(i, i2, uptimeMillis, pkgList.getPackageListLocked());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void commitNextPssTime() {
        commitNextPssTime(this.mProcStateMemTracker);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void abortNextPssTime() {
        abortNextPssTime(this.mProcStateMemTracker);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long computeNextPssTime(int i, boolean z, boolean z2, long j) {
        return ProcessList.computeNextPssTime(i, this.mProcStateMemTracker, z, z2, j);
    }

    private static void commitNextPssTime(ProcessList.ProcStateMemTracker procStateMemTracker) {
        int i = procStateMemTracker.mPendingMemState;
        if (i >= 0) {
            int[] iArr = procStateMemTracker.mHighestMem;
            int i2 = procStateMemTracker.mPendingHighestMemState;
            iArr[i] = i2;
            procStateMemTracker.mScalingFactor[i] = procStateMemTracker.mPendingScalingFactor;
            procStateMemTracker.mTotalHighestMem = i2;
            procStateMemTracker.mPendingMemState = -1;
        }
    }

    private static void abortNextPssTime(ProcessList.ProcStateMemTracker procStateMemTracker) {
        procStateMemTracker.mPendingMemState = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public int getPid() {
        return this.mPid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setPid(int i) {
        this.mPid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public IApplicationThread getThread() {
        return this.mThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public int getSetProcState() {
        return this.mSetProcState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public int getSetAdj() {
        return this.mSetAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public int getCurRawAdj() {
        return this.mCurRawAdj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public long getLastStateTime() {
        return this.mLastStateTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProfilerLock"})
    public void updateProcState(ProcessStateRecord processStateRecord) {
        this.mSetProcState = processStateRecord.getCurProcState();
        this.mSetAdj = processStateRecord.getCurAdj();
        this.mCurRawAdj = processStateRecord.getCurRawAdj();
        this.mLastStateTime = processStateRecord.getLastStateTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addHostingComponentType(@ProcessMemoryState.HostingComponentType int i) {
        AtomicInteger atomicInteger = this.mCurrentHostingComponentTypes;
        atomicInteger.set(atomicInteger.get() | i);
        AtomicInteger atomicInteger2 = this.mHistoricalHostingComponentTypes;
        atomicInteger2.set(i | atomicInteger2.get());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearHostingComponentType(@ProcessMemoryState.HostingComponentType int i) {
        AtomicInteger atomicInteger = this.mCurrentHostingComponentTypes;
        atomicInteger.set((~i) & atomicInteger.get());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ProcessMemoryState.HostingComponentType
    public int getCurrentHostingComponentTypes() {
        return this.mCurrentHostingComponentTypes.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ProcessMemoryState.HostingComponentType
    public int getHistoricalHostingComponentTypes() {
        return this.mHistoricalHostingComponentTypes.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpPss(PrintWriter printWriter, String str, long j) {
        synchronized (this.mProfilerLock) {
            printWriter.print(str);
            printWriter.print("lastPssTime=");
            TimeUtils.formatDuration(this.mLastPssTime, j, printWriter);
            printWriter.print(" pssProcState=");
            printWriter.print(this.mPssProcState);
            printWriter.print(" pssStatType=");
            printWriter.print(this.mPssStatType);
            printWriter.print(" nextPssTime=");
            TimeUtils.formatDuration(this.mNextPssTime, j, printWriter);
            printWriter.println();
            printWriter.print(str);
            printWriter.print("lastPss=");
            DebugUtils.printSizeValue(printWriter, this.mLastPss * 1024);
            printWriter.print(" lastSwapPss=");
            DebugUtils.printSizeValue(printWriter, this.mLastSwapPss * 1024);
            printWriter.print(" lastCachedPss=");
            DebugUtils.printSizeValue(printWriter, this.mLastCachedPss * 1024);
            printWriter.print(" lastCachedSwapPss=");
            DebugUtils.printSizeValue(printWriter, this.mLastCachedSwapPss * 1024);
            printWriter.print(" lastRss=");
            DebugUtils.printSizeValue(printWriter, this.mLastRss * 1024);
            printWriter.println();
            printWriter.print(str);
            printWriter.print("trimMemoryLevel=");
            printWriter.println(this.mTrimMemoryLevel);
            printWriter.print(str);
            printWriter.print("procStateMemTracker: ");
            this.mProcStateMemTracker.dumpLine(printWriter);
            printWriter.print(str);
            printWriter.print("lastRequestedGc=");
            TimeUtils.formatDuration(this.mLastRequestedGc, j, printWriter);
            printWriter.print(" lastLowMemory=");
            TimeUtils.formatDuration(this.mLastLowMemory, j, printWriter);
            printWriter.print(" reportLowMemory=");
            printWriter.println(this.mReportLowMemory);
        }
        printWriter.print(str);
        printWriter.print("currentHostingComponentTypes=0x");
        printWriter.print(Integer.toHexString(getCurrentHostingComponentTypes()));
        printWriter.print(" historicalHostingComponentTypes=0x");
        printWriter.println(Integer.toHexString(getHistoricalHostingComponentTypes()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpCputime(PrintWriter printWriter, String str) {
        long j = this.mLastCpuTime.get();
        printWriter.print(str);
        printWriter.print("lastCpuTime=");
        printWriter.print(j);
        if (j > 0) {
            printWriter.print(" timeUsed=");
            TimeUtils.formatDuration(this.mCurCpuTime.get() - j, printWriter);
        }
        printWriter.println();
    }
}
