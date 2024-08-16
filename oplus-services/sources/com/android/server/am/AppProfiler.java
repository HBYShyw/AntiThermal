package com.android.server.am;

import android.app.ActivityThread;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.FeatureFlagUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.StatsEvent;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.ProcessMap;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.IProcessCpuTrackerExt;
import com.android.internal.os.ProcessCpuTracker;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.MemInfoReader;
import com.android.internal.util.QuickSelect;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.AppProfiler;
import com.android.server.clipboard.ClipboardService;
import com.android.server.power.stats.BatteryStatsImpl;
import com.android.server.power.stats.IBatteryStatsImplExt;
import com.android.server.utils.PriorityDump;
import com.android.server.wm.ActivityTaskManagerDebugConfig;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppProfiler {
    private static final String ACTION_HEAP_DUMP_FINISHED = "com.android.internal.intent.action.HEAP_DUMP_FINISHED";
    static final String ACTIVITY_START_PSS_DEFER_CONFIG = "activity_start_pss_defer";
    static final long BATTERY_STATS_TIME = 1800000;
    private static final String EXTRA_HEAP_DUMP_IS_USER_INITIATED = "com.android.internal.extra.heap_dump.IS_USER_INITIATED";
    private static final String EXTRA_HEAP_DUMP_PROCESS_NAME = "com.android.internal.extra.heap_dump.PROCESS_NAME";
    private static final String EXTRA_HEAP_DUMP_REPORT_PACKAGE = "com.android.internal.extra.heap_dump.REPORT_PACKAGE";
    private static final String EXTRA_HEAP_DUMP_SIZE_BYTES = "com.android.internal.extra.heap_dump.SIZE_BYTES";
    static final long MONITOR_CPU_MAX_TIME = 268435455;
    static final long MONITOR_CPU_MIN_TIME = 5000;
    static final boolean MONITOR_CPU_USAGE = true;
    static final boolean MONITOR_THREAD_CPU_USAGE = false;
    private static final String TAG = "ActivityManager";
    public IAppProfilerExt mAppProfilerExt;
    public IBatteryStatsImplExt mBatteryStatsImplExt;
    private final Handler mBgHandler;
    final CachedAppsWatermarkData mCachedAppsWatermarkData;

    @GuardedBy({"mService"})
    boolean mHasHomeProcess;

    @GuardedBy({"mService"})
    boolean mHasPreviousProcess;
    private final AtomicLong mLastCpuTime;

    @GuardedBy({"mService"})
    private int mLastNumProcesses;
    private volatile long mLastWriteTime;
    private final LowMemDetector mLowMemDetector;

    @GuardedBy({"mProfilerLock"})
    private int mMemWatchDumpPid;

    @GuardedBy({"mProfilerLock"})
    private String mMemWatchDumpProcName;

    @GuardedBy({"mProfilerLock"})
    private int mMemWatchDumpUid;

    @GuardedBy({"mProfilerLock"})
    private Uri mMemWatchDumpUri;

    @GuardedBy({"mProfilerLock"})
    private boolean mMemWatchIsUserInitiated;
    final ActivityManagerGlobalLock mProcLock;
    private final CountDownLatch mProcessCpuInitLatch;
    private final AtomicBoolean mProcessCpuMutexFree;
    private final Thread mProcessCpuThread;
    private final ProcessCpuTracker mProcessCpuTracker;
    public IProcessCpuTrackerExt mProcessCpuTrackerExt;
    final Object mProfilerLock;
    private final DeviceConfig.OnPropertiesChangedListener mPssDelayConfigListener;
    private final ActivityManagerService mService;
    static final String TAG_PSS = "ActivityManager" + ActivityManagerDebugConfig.POSTFIX_PSS;
    static final String TAG_OOM_ADJ = ActivityManagerService.TAG_OOM_ADJ;
    private static final boolean IS_AGING_VERSION = "1".equals(SystemProperties.get("persist.sys.agingtest", ""));
    private static final boolean IS_HIGHTEMP_VERSION = "hightempaging".equals(SystemProperties.get("ro.oplus.image.my_engineering.type", ""));
    private volatile long mPssDeferralTime = 0;

    @GuardedBy({"mProfilerLock"})
    private final ArrayList<ProcessProfileRecord> mPendingPssProfiles = new ArrayList<>();
    private final AtomicInteger mActivityStartingNesting = new AtomicInteger(0);

    @GuardedBy({"mProfilerLock"})
    private long mLastFullPssTime = SystemClock.uptimeMillis();

    @GuardedBy({"mProfilerLock"})
    private boolean mFullPssPending = false;
    private volatile boolean mTestPssMode = false;

    @GuardedBy({"mService"})
    private boolean mAllowLowerMemLevel = false;

    @GuardedBy({"mService"})
    private int mLastMemoryLevel = 0;

    @GuardedBy({"mService"})
    private int mMemFactorOverride = -1;

    @GuardedBy({"mProcLock"})
    private long mLowRamTimeSinceLastIdle = 0;

    @GuardedBy({"mProcLock"})
    private long mLowRamStartTime = 0;

    @GuardedBy({"mService"})
    private long mLastMemUsageReportTime = 0;

    @GuardedBy({"mProfilerLock"})
    private final ArrayList<ProcessRecord> mProcessesToGc = new ArrayList<>();

    @GuardedBy({"mProfilerLock"})
    private Map<String, String> mAppAgentMap = null;

    @GuardedBy({"mProfilerLock"})
    private int mProfileType = 0;

    @GuardedBy({"mProfilerLock"})
    private final ProfileData mProfileData = new ProfileData();

    @GuardedBy({"mProfilerLock"})
    private final ProcessMap<Pair<Long, String>> mMemWatchProcesses = new ProcessMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ProfileData {
        private String mProfileApp;
        private ProcessRecord mProfileProc;
        private ProfilerInfo mProfilerInfo;

        private ProfileData() {
            this.mProfileApp = null;
            this.mProfileProc = null;
            this.mProfilerInfo = null;
        }

        void setProfileApp(String str) {
            this.mProfileApp = str;
            if (AppProfiler.this.mService.mAtmInternal != null) {
                AppProfiler.this.mService.mAtmInternal.setProfileApp(str);
            }
        }

        String getProfileApp() {
            return this.mProfileApp;
        }

        void setProfileProc(ProcessRecord processRecord) {
            this.mProfileProc = processRecord;
            if (AppProfiler.this.mService.mAtmInternal != null) {
                AppProfiler.this.mService.mAtmInternal.setProfileProc(processRecord == null ? null : processRecord.getWindowProcessController());
            }
        }

        ProcessRecord getProfileProc() {
            return this.mProfileProc;
        }

        void setProfilerInfo(ProfilerInfo profilerInfo) {
            this.mProfilerInfo = profilerInfo;
            if (AppProfiler.this.mService.mAtmInternal != null) {
                AppProfiler.this.mService.mAtmInternal.setProfilerInfo(profilerInfo);
            }
        }

        ProfilerInfo getProfilerInfo() {
            return this.mProfilerInfo;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class CachedAppsWatermarkData {

        @GuardedBy({"mProcLock"})
        int mAverageFrozenTimeInSeconds;

        @GuardedBy({"mProcLock"})
        int mBinderProxySnapshot;

        @GuardedBy({"mProcLock"})
        private long[] mCachedAppFrozenDurations;

        @GuardedBy({"mProcLock"})
        int mCachedAppHighWatermark;

        @GuardedBy({"mProcLock"})
        int mCachedInKb;

        @GuardedBy({"mProcLock"})
        private long mEarliestFrozenTimestamp;

        @GuardedBy({"mProcLock"})
        int mFreeInKb;

        @GuardedBy({"mProcLock"})
        int mKernelInKb;

        @GuardedBy({"mProcLock"})
        private long mLatestFrozenTimestamp;

        @GuardedBy({"mProcLock"})
        int mLongestFrozenTimeInSeconds;

        @GuardedBy({"mProcLock"})
        int mMeanFrozenTimeInSeconds;

        @GuardedBy({"mProcLock"})
        int mNumOfFrozenApps;

        @GuardedBy({"mProcLock"})
        int mShortestFrozenTimeInSeconds;

        @GuardedBy({"mProcLock"})
        private long mTotalFrozenDurations;

        @GuardedBy({"mProcLock"})
        int mUptimeInSeconds;

        @GuardedBy({"mProcLock"})
        int mZramInKb;

        CachedAppsWatermarkData() {
        }

        @GuardedBy({"mProcLock"})
        void updateCachedAppsHighWatermarkIfNecessaryLocked(int i, long j) {
            if (i > this.mCachedAppHighWatermark) {
                this.mCachedAppHighWatermark = i;
                this.mUptimeInSeconds = (int) (j / 1000);
                AppProfiler.this.mService.mHandler.removeMessages(79);
                AppProfiler.this.mService.mHandler.obtainMessage(79, Long.valueOf(j)).sendToTarget();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void updateCachedAppsSnapshot(final long j) {
            ActivityManagerGlobalLock activityManagerGlobalLock = AppProfiler.this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    this.mEarliestFrozenTimestamp = j;
                    this.mLatestFrozenTimestamp = 0L;
                    this.mTotalFrozenDurations = 0L;
                    this.mNumOfFrozenApps = 0;
                    int lruSizeLOSP = AppProfiler.this.mService.mProcessList.getLruSizeLOSP();
                    long[] jArr = this.mCachedAppFrozenDurations;
                    if (jArr == null || jArr.length < lruSizeLOSP) {
                        this.mCachedAppFrozenDurations = new long[Math.max(lruSizeLOSP, AppProfiler.this.mService.mConstants.CUR_MAX_CACHED_PROCESSES)];
                    }
                    AppProfiler.this.mService.mProcessList.forEachLruProcessesLOSP(true, new Consumer() { // from class: com.android.server.am.AppProfiler$CachedAppsWatermarkData$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            AppProfiler.CachedAppsWatermarkData.this.lambda$updateCachedAppsSnapshot$0(j, (ProcessRecord) obj);
                        }
                    });
                    int i = this.mNumOfFrozenApps;
                    if (i > 0) {
                        this.mLongestFrozenTimeInSeconds = (int) ((j - this.mEarliestFrozenTimestamp) / 1000);
                        this.mShortestFrozenTimeInSeconds = (int) ((j - this.mLatestFrozenTimestamp) / 1000);
                        this.mAverageFrozenTimeInSeconds = (int) ((this.mTotalFrozenDurations / i) / 1000);
                        this.mMeanFrozenTimeInSeconds = (int) (QuickSelect.select(this.mCachedAppFrozenDurations, 0, i, i / 2) / 1000);
                    }
                    this.mBinderProxySnapshot = 0;
                    SparseIntArray nGetBinderProxyPerUidCounts = BinderInternal.nGetBinderProxyPerUidCounts();
                    if (nGetBinderProxyPerUidCounts != null) {
                        int size = nGetBinderProxyPerUidCounts.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            if (AppProfiler.this.mService.mProcessList.getUidRecordLOSP(nGetBinderProxyPerUidCounts.keyAt(i2)) != null) {
                                this.mBinderProxySnapshot += nGetBinderProxyPerUidCounts.valueAt(i2);
                            }
                        }
                    }
                    MemInfoReader memInfoReader = new MemInfoReader();
                    memInfoReader.readMemInfo();
                    this.mFreeInKb = (int) memInfoReader.getFreeSizeKb();
                    this.mCachedInKb = (int) memInfoReader.getCachedSizeKb();
                    this.mZramInKb = (int) memInfoReader.getZramTotalSizeKb();
                    this.mKernelInKb = (int) memInfoReader.getKernelUsedSizeKb();
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateCachedAppsSnapshot$0(long j, ProcessRecord processRecord) {
            if (processRecord.mOptRecord.isFrozen()) {
                long freezeUnfreezeTime = processRecord.mOptRecord.getFreezeUnfreezeTime();
                if (freezeUnfreezeTime < this.mEarliestFrozenTimestamp) {
                    this.mEarliestFrozenTimestamp = freezeUnfreezeTime;
                }
                if (freezeUnfreezeTime > this.mLatestFrozenTimestamp) {
                    this.mLatestFrozenTimestamp = freezeUnfreezeTime;
                }
                long j2 = j - freezeUnfreezeTime;
                this.mTotalFrozenDurations += j2;
                long[] jArr = this.mCachedAppFrozenDurations;
                int i = this.mNumOfFrozenApps;
                this.mNumOfFrozenApps = i + 1;
                jArr[i] = j2;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public StatsEvent getCachedAppsHighWatermarkStats(int i, boolean z) {
            StatsEvent buildStatsEvent;
            ActivityManagerGlobalLock activityManagerGlobalLock = AppProfiler.this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    buildStatsEvent = FrameworkStatsLog.buildStatsEvent(i, this.mCachedAppHighWatermark, this.mUptimeInSeconds, this.mBinderProxySnapshot, this.mFreeInKb, this.mCachedInKb, this.mZramInKb, this.mKernelInKb, this.mNumOfFrozenApps, this.mLongestFrozenTimeInSeconds, this.mShortestFrozenTimeInSeconds, this.mMeanFrozenTimeInSeconds, this.mAverageFrozenTimeInSeconds);
                    if (z) {
                        this.mCachedAppHighWatermark = 0;
                        this.mUptimeInSeconds = 0;
                        this.mBinderProxySnapshot = 0;
                        this.mFreeInKb = 0;
                        this.mCachedInKb = 0;
                        this.mZramInKb = 0;
                        this.mKernelInKb = 0;
                        this.mNumOfFrozenApps = 0;
                        this.mLongestFrozenTimeInSeconds = 0;
                        this.mShortestFrozenTimeInSeconds = 0;
                        this.mMeanFrozenTimeInSeconds = 0;
                        this.mAverageFrozenTimeInSeconds = 0;
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
            return buildStatsEvent;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BgHandler extends Handler {
        static final int COLLECT_PSS_BG_MSG = 1;
        static final int DEFER_PSS_MSG = 2;
        static final int MEMORY_PRESSURE_CHANGED = 4;
        static final int STOP_DEFERRING_PSS_MSG = 3;

        BgHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AppProfiler appProfiler = AppProfiler.this;
                if (appProfiler.mAppProfilerExt.isNeedSkipDumpPss(appProfiler.mBgHandler)) {
                    return;
                }
                AppProfiler.this.collectPssInBackground();
                return;
            }
            if (i == 2) {
                AppProfiler.this.deferPssForActivityStart();
                return;
            }
            if (i == 3) {
                AppProfiler.this.stopDeferPss();
                return;
            }
            if (i == 4) {
                ActivityManagerService activityManagerService = AppProfiler.this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        AppProfiler.this.handleMemoryPressureChangedLocked(message.arg1, message.arg2);
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                return;
            }
            AppProfiler.this.mProcessCpuTracker.mProcessCpuTrackerExt.handleMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:138:? -> B:135:0x02d4). Please report as a decompilation issue!!! */
    public void collectPssInBackground() {
        int i;
        long[] jArr;
        MemInfoReader memInfoReader;
        ProcessProfileRecord remove;
        int pssProcState;
        int pssStatType;
        long lastPssTime;
        int i2;
        long[] jArr2;
        Object obj;
        int i3;
        Object obj2;
        List stats;
        AppProfiler appProfiler = this;
        long uptimeMillis = SystemClock.uptimeMillis();
        synchronized (appProfiler.mProfilerLock) {
            i = 0;
            jArr = 0;
            if (appProfiler.mFullPssPending) {
                appProfiler.mFullPssPending = false;
                memInfoReader = new MemInfoReader();
            } else {
                memInfoReader = null;
            }
        }
        if (memInfoReader != null) {
            updateCpuStatsNow();
            synchronized (appProfiler.mProcessCpuTracker) {
                stats = appProfiler.mProcessCpuTracker.getStats(new ProcessCpuTracker.FilterStats() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda0
                    public final boolean needed(ProcessCpuTracker.Stats stats2) {
                        boolean lambda$collectPssInBackground$0;
                        lambda$collectPssInBackground$0 = AppProfiler.lambda$collectPssInBackground$0(stats2);
                        return lambda$collectPssInBackground$0;
                    }
                });
            }
            int size = stats.size();
            long j = 0;
            for (int i4 = 0; i4 < size; i4++) {
                synchronized (appProfiler.mService.mPidsSelfLocked) {
                    if (appProfiler.mService.mPidsSelfLocked.indexOfKey(((ProcessCpuTracker.Stats) stats.get(i4)).pid) < 0) {
                        if (!appProfiler.mAppProfilerExt.filterNativeProcessGetPss((ProcessCpuTracker.Stats) stats.get(i4))) {
                            j += Debug.getPss(((ProcessCpuTracker.Stats) stats.get(i4)).pid, null, null);
                        }
                    }
                }
            }
            try {
                memInfoReader.readMemInfo();
            } catch (RuntimeException e) {
                Slog.e("ActivityManager", "readMemInfo :" + e);
            }
            synchronized (appProfiler.mService.mProcessStats.mLock) {
                if (ActivityManagerDebugConfig.DEBUG_PSS) {
                    Slog.d(TAG_PSS, "Collected native and kernel memory in " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms");
                }
                long cachedSizeKb = memInfoReader.getCachedSizeKb();
                long freeSizeKb = memInfoReader.getFreeSizeKb();
                long zramTotalSizeKb = memInfoReader.getZramTotalSizeKb();
                long kernelUsedSizeKb = memInfoReader.getKernelUsedSizeKb();
                EventLogTags.writeAmMeminfo(cachedSizeKb * 1024, freeSizeKb * 1024, zramTotalSizeKb * 1024, kernelUsedSizeKb * 1024, j * 1024);
                appProfiler.mService.mProcessStats.addSysMemUsageLocked(cachedSizeKb, freeSizeKb, zramTotalSizeKb, kernelUsedSizeKb, j);
            }
        }
        boolean isGameScene = appProfiler.mAppProfilerExt.isGameScene();
        if (ActivityManagerDebugConfig.DEBUG_PSS) {
            Slog.d(TAG_PSS, "gameScene =  " + isGameScene);
        }
        long[] jArr3 = new long[3];
        int i5 = 0;
        while (true) {
            synchronized (appProfiler.mProfilerLock) {
                if (appProfiler.mPendingPssProfiles.size() <= 0) {
                    break;
                }
                remove = appProfiler.mPendingPssProfiles.remove(i);
                pssProcState = remove.getPssProcState();
                pssStatType = remove.getPssStatType();
                lastPssTime = remove.getLastPssTime();
                long uptimeMillis2 = SystemClock.uptimeMillis();
                if (remove.getThread() != null && pssProcState == remove.getSetProcState() && lastPssTime + 1000 < uptimeMillis2) {
                    i2 = remove.getPid();
                } else {
                    remove.abortNextPssTime();
                    if (ActivityManagerDebugConfig.DEBUG_PSS) {
                        Slog.d(TAG_PSS, "Skipped pss collection of -1: still need " + ((1000 + lastPssTime) - uptimeMillis2) + "ms until safe");
                    }
                    remove = jArr;
                    i2 = 0;
                }
            }
            if (remove != null) {
                long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
                ProcessCachedOptimizerRecord processCachedOptimizerRecord = remove.mApp.mOptRecord;
                boolean z = (processCachedOptimizerRecord != null && processCachedOptimizerRecord.skipPSSCollectionBecauseFrozen()) || appProfiler.mService.isCameraActiveForUid(remove.mApp.uid);
                appProfiler.mAppProfilerExt.boost(remove, -2);
                long pss = (z || isGameScene) ? 0L : Debug.getPss(i2, jArr3, jArr);
                appProfiler.mAppProfilerExt.reset(remove);
                long currentThreadTimeMillis2 = SystemClock.currentThreadTimeMillis();
                Object obj3 = appProfiler.mProfilerLock;
                synchronized (obj3) {
                    if (pss != 0) {
                        try {
                            if (remove.getThread() != null && remove.getSetProcState() == pssProcState && remove.getPid() == i2 && remove.getLastPssTime() == lastPssTime) {
                                int i6 = i5 + 1;
                                remove.commitNextPssTime();
                                long j2 = pss;
                                jArr2 = jArr3;
                                obj = jArr;
                                i3 = 0;
                                obj2 = obj3;
                                try {
                                    recordPssSampleLPf(remove, pssProcState, j2, jArr3[0], jArr3[1], jArr3[2], pssStatType, currentThreadTimeMillis2 - currentThreadTimeMillis, SystemClock.uptimeMillis());
                                    i5 = i6;
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            obj2 = obj3;
                            throw th;
                        }
                    }
                    jArr2 = jArr3;
                    obj2 = obj3;
                    obj = jArr;
                    i3 = 0;
                    remove.abortNextPssTime();
                    if (ActivityManagerDebugConfig.DEBUG_PSS) {
                        String str = TAG_PSS;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Skipped pss collection of ");
                        sb.append(i2);
                        sb.append(": ");
                        sb.append(remove.getThread() == null ? "NO_THREAD " : "");
                        sb.append(z ? "SKIP_PSS_COLLECTION " : "");
                        sb.append(isGameScene ? "GAME" : "");
                        sb.append(remove.getPid() != i2 ? "PID_CHANGED " : "");
                        sb.append(" initState=");
                        sb.append(pssProcState);
                        sb.append(" curState=");
                        sb.append(remove.getSetProcState());
                        sb.append(" ");
                        sb.append(remove.getLastPssTime() != lastPssTime ? "TIME_CHANGED" : "");
                        Slog.d(str, sb.toString());
                    }
                }
            } else {
                jArr2 = jArr3;
                obj = jArr;
                i3 = 0;
            }
            appProfiler = this;
            jArr3 = jArr2;
            i = i3;
            jArr = obj;
        }
        if (appProfiler.mTestPssMode || ActivityManagerDebugConfig.DEBUG_PSS) {
            Slog.d(TAG_PSS, "Collected pss of " + i5 + " processes in " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms");
        }
        appProfiler.mPendingPssProfiles.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$collectPssInBackground$0(ProcessCpuTracker.Stats stats) {
        return stats.vsize > 0 && stats.uid < 10000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void updateNextPssTimeLPf(int i, ProcessProfileRecord processProfileRecord, long j, boolean z) {
        if (z || ((j > processProfileRecord.getNextPssTime() || j > Math.max(processProfileRecord.getLastPssTime() + ClipboardService.DEFAULT_CLIPBOARD_TIMEOUT_MILLIS, processProfileRecord.getLastStateTime() + ProcessList.minTimeFromStateChange(this.mTestPssMode))) && requestPssLPf(processProfileRecord, i))) {
            processProfileRecord.setNextPssTime(processProfileRecord.computeNextPssTime(i, this.mTestPssMode, this.mService.mAtmInternal.isSleeping(), j));
        }
    }

    @GuardedBy({"mProfilerLock"})
    private void recordPssSampleLPf(final ProcessProfileRecord processProfileRecord, int i, final long j, final long j2, long j3, final long j4, final int i2, final long j5, long j6) {
        ProcessProfileRecord processProfileRecord2;
        Long l;
        final ProcessRecord processRecord = processProfileRecord.mApp;
        this.mAppProfilerExt.recordPssStats(processRecord.info.packageName, processRecord.processName, processRecord.info.uid, processRecord.getPid(), i, j, j2, j3, j4);
        long j7 = j * 1024;
        EventLogTags.writeAmPss(processProfileRecord.getPid(), processRecord.uid, processRecord.processName, j7, j2 * 1024, j3 * 1024, j4 * 1024, i2, i, j5);
        processProfileRecord.setLastPssTime(j6);
        processProfileRecord.addPss(j, j2, j4, true, i2, j5);
        processRecord.getPkgList().forEachPackageProcessStats(new Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AppProfiler.lambda$recordPssSampleLPf$1(ProcessRecord.this, j, j2, j4, i2, j5, processProfileRecord, (ProcessStats.ProcessStateHolder) obj);
            }
        });
        if (ActivityManagerDebugConfig.DEBUG_PSS) {
            Slog.d(TAG_PSS, "pss of " + processRecord.toShortString() + ": " + j + " lastPss=" + processProfileRecord.getLastPss() + " state=" + ProcessList.makeProcStateString(i));
        }
        if (processProfileRecord.getInitialIdlePss() == 0) {
            processProfileRecord2 = processProfileRecord;
            processProfileRecord2.setInitialIdlePss(j);
        } else {
            processProfileRecord2 = processProfileRecord;
        }
        processProfileRecord2.setLastPss(j);
        processProfileRecord2.setLastSwapPss(j3);
        if (i >= 14) {
            processProfileRecord2.setLastCachedPss(j);
            processProfileRecord2.setLastCachedSwapPss(j3);
        }
        processProfileRecord2.setLastRss(j4);
        SparseArray sparseArray = (SparseArray) this.mMemWatchProcesses.getMap().get(processRecord.processName);
        if (sparseArray != null) {
            Pair pair = (Pair) sparseArray.get(processRecord.uid);
            if (pair == null) {
                pair = (Pair) sparseArray.get(0);
            }
            if (pair != null) {
                l = (Long) pair.first;
                if (l != null || j7 < l.longValue() || processProfileRecord.getThread() == null || this.mMemWatchDumpProcName != null) {
                    return;
                }
                if (Build.IS_DEBUGGABLE || processRecord.isDebuggable()) {
                    Slog.w("ActivityManager", "Process " + processRecord + " exceeded pss limit " + l + "; reporting");
                    startHeapDumpLPf(processProfileRecord2, false);
                    return;
                }
                Slog.w("ActivityManager", "Process " + processRecord + " exceeded pss limit " + l + ", but debugging not enabled");
                return;
            }
        }
        l = null;
        if (l != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$recordPssSampleLPf$1(ProcessRecord processRecord, long j, long j2, long j3, int i, long j4, ProcessProfileRecord processProfileRecord, ProcessStats.ProcessStateHolder processStateHolder) {
        FrameworkStatsLog.write(18, processRecord.info.uid, processStateHolder.state.getName(), processStateHolder.state.getPackage(), j, j2, j3, i, j4, processStateHolder.appVersion, processProfileRecord.getCurrentHostingComponentTypes(), processProfileRecord.getHistoricalHostingComponentTypes());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class RecordPssRunnable implements Runnable {
        private final ContentResolver mContentResolver;
        private final Uri mDumpUri;
        private final ProcessProfileRecord mProfile;

        RecordPssRunnable(ProcessProfileRecord processProfileRecord, Uri uri, ContentResolver contentResolver) {
            this.mProfile = processProfileRecord;
            this.mDumpUri = uri;
            this.mContentResolver = contentResolver;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ParcelFileDescriptor openFileDescriptor = this.mContentResolver.openFileDescriptor(this.mDumpUri, "rw");
                try {
                    IApplicationThread thread = this.mProfile.getThread();
                    if (thread != null) {
                        try {
                            if (ActivityManagerDebugConfig.DEBUG_PSS) {
                                Slog.d(AppProfiler.TAG_PSS, "Requesting dump heap from " + this.mProfile.mApp + " to " + this.mDumpUri.getPath());
                            }
                            thread.dumpHeap(true, false, false, this.mDumpUri.getPath(), openFileDescriptor, (RemoteCallback) null);
                        } catch (RemoteException unused) {
                        }
                    }
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                } finally {
                }
            } catch (IOException e) {
                Slog.e("ActivityManager", "Failed to dump heap", e);
                AppProfiler.this.abortHeapDump(this.mProfile.mApp.processName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void startHeapDumpLPf(ProcessProfileRecord processProfileRecord, boolean z) {
        ProcessRecord processRecord = processProfileRecord.mApp;
        String str = processRecord.processName;
        this.mMemWatchDumpProcName = str;
        this.mMemWatchDumpUri = makeHeapDumpUri(str);
        this.mMemWatchDumpPid = processProfileRecord.getPid();
        int i = processRecord.uid;
        this.mMemWatchDumpUid = i;
        this.mMemWatchIsUserInitiated = z;
        try {
            BackgroundThread.getHandler().post(new RecordPssRunnable(processProfileRecord, this.mMemWatchDumpUri, this.mService.mContext.createPackageContextAsUser("android", 0, UserHandle.getUserHandleForUid(i)).getContentResolver()));
        } catch (PackageManager.NameNotFoundException unused) {
            throw new RuntimeException("android package not found.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpHeapFinished(String str, int i) {
        synchronized (this.mProfilerLock) {
            if (i != this.mMemWatchDumpPid) {
                Slog.w("ActivityManager", "dumpHeapFinished: Calling pid " + Binder.getCallingPid() + " does not match last pid " + this.mMemWatchDumpPid);
                return;
            }
            Uri uri = this.mMemWatchDumpUri;
            if (uri != null && uri.getPath().equals(str)) {
                if (ActivityManagerDebugConfig.DEBUG_PSS) {
                    Slog.d(TAG_PSS, "Dump heap finished for " + str);
                }
                this.mService.mHandler.sendEmptyMessage(50);
                Runtime.getRuntime().gc();
                return;
            }
            Slog.w("ActivityManager", "dumpHeapFinished: Calling path " + str + " does not match last path " + this.mMemWatchDumpUri);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handlePostDumpHeapNotification() {
        int i;
        String str;
        long j;
        String str2;
        boolean z;
        synchronized (this.mProfilerLock) {
            i = this.mMemWatchDumpUid;
            str = this.mMemWatchDumpProcName;
            Pair pair = (Pair) this.mMemWatchProcesses.get(str, i);
            if (pair == null) {
                pair = (Pair) this.mMemWatchProcesses.get(str, 0);
            }
            if (pair != null) {
                j = ((Long) pair.first).longValue();
                str2 = (String) pair.second;
            } else {
                j = 0;
                str2 = null;
            }
            z = this.mMemWatchIsUserInitiated;
            this.mMemWatchDumpUri = null;
            this.mMemWatchDumpProcName = null;
            this.mMemWatchDumpPid = -1;
            this.mMemWatchDumpUid = -1;
        }
        if (str == null) {
            return;
        }
        if (ActivityManagerDebugConfig.DEBUG_PSS) {
            Slog.d(TAG_PSS, "Showing dump heap notification from " + str + "/" + i);
        }
        Intent intent = new Intent(ACTION_HEAP_DUMP_FINISHED);
        intent.setPackage("com.android.shell");
        intent.putExtra("android.intent.extra.UID", i);
        intent.putExtra(EXTRA_HEAP_DUMP_IS_USER_INITIATED, z);
        intent.putExtra(EXTRA_HEAP_DUMP_SIZE_BYTES, j);
        intent.putExtra(EXTRA_HEAP_DUMP_REPORT_PACKAGE, str2);
        intent.putExtra(EXTRA_HEAP_DUMP_PROCESS_NAME, str);
        this.mService.mContext.sendBroadcastAsUser(intent, UserHandle.getUserHandleForUid(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDumpHeapDebugLimit(String str, int i, long j, String str2) {
        synchronized (this.mProfilerLock) {
            try {
                if (j > 0) {
                    this.mMemWatchProcesses.put(str, i, new Pair(Long.valueOf(j), str2));
                } else if (i != 0) {
                    this.mMemWatchProcesses.remove(str, i);
                } else {
                    this.mMemWatchProcesses.getMap().remove(str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abortHeapDump(String str) {
        Message obtainMessage = this.mService.mHandler.obtainMessage(51);
        obtainMessage.obj = str;
        this.mService.mHandler.sendMessage(obtainMessage);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleAbortDumpHeap(String str) {
        if (str != null) {
            synchronized (this.mProfilerLock) {
                if (str.equals(this.mMemWatchDumpProcName)) {
                    this.mMemWatchDumpProcName = null;
                    this.mMemWatchDumpUri = null;
                    this.mMemWatchDumpPid = -1;
                    this.mMemWatchDumpUid = -1;
                }
            }
        }
    }

    private static Uri makeHeapDumpUri(String str) {
        return Uri.parse("content://com.android.shell.heapdump/" + str + "_javaheap.bin");
    }

    @GuardedBy({"mProfilerLock"})
    private boolean requestPssLPf(ProcessProfileRecord processProfileRecord, int i) {
        if (this.mPendingPssProfiles.contains(processProfileRecord)) {
            return false;
        }
        if (this.mPendingPssProfiles.size() == 0) {
            long j = (this.mPssDeferralTime <= 0 || this.mActivityStartingNesting.get() <= 0) ? 0L : this.mPssDeferralTime;
            if (ActivityManagerDebugConfig.DEBUG_PSS && j > 0) {
                Slog.d(TAG_PSS, "requestPssLPf() deferring PSS request by " + j + " ms");
            }
            this.mBgHandler.sendEmptyMessageDelayed(1, j);
        }
        if (ActivityManagerDebugConfig.DEBUG_PSS) {
            Slog.d(TAG_PSS, "Requesting pss of: " + processProfileRecord.mApp);
        }
        processProfileRecord.setPssProcState(i);
        processProfileRecord.setPssStatType(0);
        this.mPendingPssProfiles.add(processProfileRecord);
        return true;
    }

    @GuardedBy({"mProfilerLock"})
    private void deferPssIfNeededLPf() {
        if (this.mPendingPssProfiles.size() > 0) {
            this.mBgHandler.removeMessages(1);
            this.mBgHandler.sendEmptyMessageDelayed(1, this.mPssDeferralTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deferPssForActivityStart() {
        if (this.mPssDeferralTime > 0) {
            if (ActivityManagerDebugConfig.DEBUG_PSS) {
                Slog.d(TAG_PSS, "Deferring PSS collection for activity start");
            }
            synchronized (this.mProfilerLock) {
                deferPssIfNeededLPf();
            }
            this.mActivityStartingNesting.getAndIncrement();
            this.mBgHandler.sendEmptyMessageDelayed(3, this.mPssDeferralTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDeferPss() {
        int decrementAndGet = this.mActivityStartingNesting.decrementAndGet();
        if (decrementAndGet <= 0) {
            if (ActivityManagerDebugConfig.DEBUG_PSS) {
                Slog.d(TAG_PSS, "PSS activity start deferral interval ended; now " + decrementAndGet);
            }
            if (decrementAndGet < 0) {
                Slog.wtf("ActivityManager", "Activity start nesting undercount!");
                this.mActivityStartingNesting.incrementAndGet();
                return;
            }
            return;
        }
        if (ActivityManagerDebugConfig.DEBUG_PSS) {
            Slog.d(TAG_PSS, "Still deferring PSS, nesting=" + decrementAndGet);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void requestPssAllProcsLPr(final long j, final boolean z, final boolean z2) {
        long j2;
        synchronized (this.mProfilerLock) {
            if (!z) {
                long j3 = this.mLastFullPssTime;
                if (z2) {
                    j2 = this.mService.mConstants.FULL_PSS_LOWERED_INTERVAL;
                } else {
                    j2 = this.mService.mConstants.FULL_PSS_MIN_INTERVAL;
                }
                if (j < j3 + j2) {
                    return;
                }
            }
            if (this.mAppProfilerExt.checkCPUBusy()) {
                return;
            }
            if (ActivityManagerDebugConfig.DEBUG_PSS) {
                Slog.d(TAG_PSS, "Requesting pss of all procs!  memLowered=" + z2);
            }
            this.mLastFullPssTime = j;
            this.mFullPssPending = true;
            for (int size = this.mPendingPssProfiles.size() - 1; size >= 0; size--) {
                this.mPendingPssProfiles.get(size).abortNextPssTime();
            }
            this.mPendingPssProfiles.ensureCapacity(this.mService.mProcessList.getLruSizeLOSP());
            this.mPendingPssProfiles.clear();
            this.mService.mProcessList.forEachLruProcessesLOSP(false, new Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppProfiler.this.lambda$requestPssAllProcsLPr$2(z2, z, j, (ProcessRecord) obj);
                }
            });
            if (!this.mBgHandler.hasMessages(1)) {
                this.mBgHandler.sendEmptyMessage(1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestPssAllProcsLPr$2(boolean z, boolean z2, long j, ProcessRecord processRecord) {
        ProcessProfileRecord processProfileRecord = processRecord.mProfile;
        if (processProfileRecord.getThread() == null || processProfileRecord.getSetProcState() == 20) {
            return;
        }
        long lastStateTime = processProfileRecord.getLastStateTime();
        if (z || ((z2 && j > 1000 + lastStateTime) || j > lastStateTime + 1200000)) {
            processProfileRecord.setPssProcState(processProfileRecord.getSetProcState());
            processProfileRecord.setPssStatType(z2 ? 2 : 1);
            updateNextPssTimeLPf(processProfileRecord.getSetProcState(), processProfileRecord, j, true);
            this.mPendingPssProfiles.add(processProfileRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestPssMode(boolean z) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mTestPssMode = z;
                if (z) {
                    requestPssAllProcsLPr(SystemClock.uptimeMillis(), true, true);
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getTestPssMode() {
        return this.mTestPssMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public int getLastMemoryLevelLocked() {
        int i = this.mMemFactorOverride;
        return i != -1 ? i : this.mLastMemoryLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean isLastMemoryLevelNormal() {
        int i = this.mMemFactorOverride;
        return i != -1 ? i <= 0 : this.mLastMemoryLevel <= 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void updateLowRamTimestampLPr(long j) {
        this.mLowRamTimeSinceLastIdle = 0L;
        if (this.mLowRamStartTime != 0) {
            this.mLowRamStartTime = j;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setAllowLowerMemLevelLocked(boolean z) {
        this.mAllowLowerMemLevel = z;
    }

    @GuardedBy({"mService"})
    boolean allowLowerMemLevelLocked() {
        return this.mAllowLowerMemLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void setMemFactorOverrideLocked(int i) {
        this.mMemFactorOverride = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public boolean updateLowMemStateLSP(int i, int i2, int i3, long j) {
        int i4;
        boolean z;
        final boolean memFactorLocked;
        final int memFactorLocked2;
        LowMemDetector lowMemDetector = this.mLowMemDetector;
        if (lowMemDetector != null && lowMemDetector.isAvailable()) {
            i4 = this.mLowMemDetector.getMemFactor();
        } else {
            ActivityManagerConstants activityManagerConstants = this.mService.mConstants;
            if (i > activityManagerConstants.CUR_TRIM_CACHED_PROCESSES || i2 > activityManagerConstants.CUR_TRIM_EMPTY_PROCESSES) {
                i4 = 0;
            } else {
                int i5 = i + i2;
                i4 = i5 <= 3 ? 3 : i5 <= 5 ? 2 : 1;
            }
        }
        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            Slog.d(TAG_OOM_ADJ, "oom: memFactor=" + i4 + " override=" + this.mMemFactorOverride + " last=" + this.mLastMemoryLevel + " allowLow=" + this.mAllowLowerMemLevel + " numProcs=" + this.mService.mProcessList.getLruSizeLOSP() + " last=" + this.mLastNumProcesses);
        }
        int i6 = this.mMemFactorOverride;
        boolean z2 = i6 != -1;
        if (z2) {
            i4 = i6;
        }
        if (i4 > this.mLastMemoryLevel && !z2 && (!this.mAllowLowerMemLevel || this.mService.mProcessList.getLruSizeLOSP() >= this.mLastNumProcesses)) {
            i4 = this.mLastMemoryLevel;
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                Slog.d(TAG_OOM_ADJ, "Keeping last mem factor!");
            }
        }
        int i7 = this.mLastMemoryLevel;
        int i8 = 15;
        if (i4 != i7) {
            EventLogTags.writeAmMemFactor(i4, i7);
            FrameworkStatsLog.write(15, i4);
            this.mBgHandler.obtainMessage(4, this.mLastMemoryLevel, i4).sendToTarget();
        }
        this.mCachedAppsWatermarkData.updateCachedAppsHighWatermarkIfNecessaryLocked(i + i2, j);
        synchronized (this.mService.mProcessStats.mLock) {
            ActivityManagerService activityManagerService = this.mService;
            ProcessStatsService processStatsService = activityManagerService.mProcessStats;
            ActivityTaskManagerInternal activityTaskManagerInternal = activityManagerService.mAtmInternal;
            if (activityTaskManagerInternal != null && activityTaskManagerInternal.isSleeping()) {
                z = false;
                memFactorLocked = processStatsService.setMemFactorLocked(i4, z, SystemClock.uptimeMillis());
                memFactorLocked2 = this.mService.mProcessStats.getMemFactorLocked();
            }
            z = true;
            memFactorLocked = processStatsService.setMemFactorLocked(i4, z, SystemClock.uptimeMillis());
            memFactorLocked2 = this.mService.mProcessStats.getMemFactorLocked();
        }
        ActivityManagerService activityManagerService2 = this.mService;
        if (activityManagerService2.mConstants.USE_MODERN_TRIM) {
            activityManagerService2.mProcessList.forEachLruProcessesLOSP(true, new Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppProfiler.lambda$updateLowMemStateLSP$3((ProcessRecord) obj);
                }
            });
            return false;
        }
        this.mLastMemoryLevel = i4;
        this.mLastNumProcesses = activityManagerService2.mProcessList.getLruSizeLOSP();
        if (i4 != 0) {
            if (this.mLowRamStartTime == 0) {
                this.mLowRamStartTime = j;
            }
            if (i4 == 2) {
                i8 = 10;
            } else if (i4 != 3) {
                i8 = 5;
            }
            int i9 = i3 / 3;
            final int i10 = this.mHasHomeProcess ? 3 : 2;
            if (this.mHasPreviousProcess) {
                i10++;
            }
            if (i9 >= i10) {
                i10 = i9;
            }
            final int[] iArr = {0};
            final int[] iArr2 = {80};
            final int i11 = i8;
            this.mService.mProcessList.forEachLruProcessesLOSP(true, new Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppProfiler.this.lambda$updateLowMemStateLSP$4(memFactorLocked, memFactorLocked2, iArr2, iArr, i10, i11, (ProcessRecord) obj);
                }
            });
        } else {
            long j2 = this.mLowRamStartTime;
            if (j2 != 0) {
                this.mLowRamTimeSinceLastIdle += j - j2;
                this.mLowRamStartTime = 0L;
            }
            this.mService.mProcessList.forEachLruProcessesLOSP(true, new Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppProfiler.this.lambda$updateLowMemStateLSP$5(memFactorLocked, memFactorLocked2, (ProcessRecord) obj);
                }
            });
        }
        return memFactorLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateLowMemStateLSP$3(ProcessRecord processRecord) {
        IApplicationThread thread;
        ProcessProfileRecord processProfileRecord = processRecord.mProfile;
        ProcessStateRecord processStateRecord = processRecord.mState;
        if (processStateRecord.hasProcStateChanged()) {
            processStateRecord.setProcStateChanged(false);
        }
        int curProcState = processRecord.mState.getCurProcState();
        if (((curProcState < 7 || curProcState >= 16) && !processRecord.mState.isSystemNoUi()) || !processRecord.mProfile.hasPendingUiClean() || (thread = processRecord.getThread()) == null) {
            return;
        }
        try {
            thread.scheduleTrimMemory(20);
            processRecord.mProfile.setPendingUiClean(false);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLowMemStateLSP$4(boolean z, int i, int[] iArr, int[] iArr2, int i2, int i3, ProcessRecord processRecord) {
        ProcessProfileRecord processProfileRecord = processRecord.mProfile;
        processProfileRecord.getTrimMemoryLevel();
        ProcessStateRecord processStateRecord = processRecord.mState;
        int curProcState = processStateRecord.getCurProcState();
        if (z || processStateRecord.hasProcStateChanged()) {
            this.mService.setProcessTrackerStateLOSP(processRecord, i);
            processStateRecord.setProcStateChanged(false);
        }
        if (this.mAppProfilerExt.isSkipTrimMemoryForQuickBootScene(processRecord.processName)) {
            return;
        }
        trimMemoryUiHiddenIfNecessaryLSP(processRecord);
        if (curProcState >= 14 && !processRecord.isKilledByAm()) {
            scheduleTrimMemoryLSP(processRecord, iArr[0], "Trimming memory of ");
            processProfileRecord.setTrimMemoryLevel(iArr[0]);
            int i4 = iArr2[0] + 1;
            iArr2[0] = i4;
            if (i4 >= i2) {
                iArr2[0] = 0;
                int i5 = iArr[0];
                if (i5 == 60) {
                    iArr[0] = 40;
                    return;
                } else {
                    if (i5 != 80) {
                        return;
                    }
                    iArr[0] = 60;
                    return;
                }
            }
            return;
        }
        if (curProcState == 13 && !processRecord.isKilledByAm()) {
            scheduleTrimMemoryLSP(processRecord, 40, "Trimming memory of heavy-weight ");
            processProfileRecord.setTrimMemoryLevel(40);
        } else {
            scheduleTrimMemoryLSP(processRecord, i3, "Trimming memory of fg ");
            processProfileRecord.setTrimMemoryLevel(i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLowMemStateLSP$5(boolean z, int i, ProcessRecord processRecord) {
        ProcessProfileRecord processProfileRecord = processRecord.mProfile;
        ProcessStateRecord processStateRecord = processRecord.mState;
        if (z || processStateRecord.hasProcStateChanged()) {
            this.mService.setProcessTrackerStateLOSP(processRecord, i);
            processStateRecord.setProcStateChanged(false);
        }
        trimMemoryUiHiddenIfNecessaryLSP(processRecord);
        processProfileRecord.setTrimMemoryLevel(0);
    }

    @GuardedBy({"mService", "mProcLock"})
    private void trimMemoryUiHiddenIfNecessaryLSP(ProcessRecord processRecord) {
        if ((processRecord.mState.getCurProcState() >= 7 || processRecord.mState.isSystemNoUi()) && processRecord.mProfile.hasPendingUiClean()) {
            scheduleTrimMemoryLSP(processRecord, 20, "Trimming memory of bg-ui ");
            processRecord.mProfile.setPendingUiClean(false);
        }
    }

    @GuardedBy({"mService", "mProcLock"})
    private void scheduleTrimMemoryLSP(ProcessRecord processRecord, int i, String str) {
        IApplicationThread thread;
        if (processRecord.mProfile.getTrimMemoryLevel() >= i || (thread = processRecord.getThread()) == null) {
            return;
        }
        try {
            if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH || ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                Slog.v(TAG_OOM_ADJ, str + processRecord.processName + " to " + i);
            }
            this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeTemporarily(processRecord, 13);
            thread.scheduleTrimMemory(i);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public long getLowRamTimeSinceIdleLPr(long j) {
        long j2 = this.mLowRamTimeSinceLastIdle;
        long j3 = this.mLowRamStartTime;
        return j2 + (j3 > 0 ? j - j3 : 0L);
    }

    @GuardedBy({"mProfilerLock"})
    private void performAppGcLPf(ProcessRecord processRecord) {
        try {
            ProcessProfileRecord processProfileRecord = processRecord.mProfile;
            processProfileRecord.setLastRequestedGc(SystemClock.uptimeMillis());
            IApplicationThread thread = processProfileRecord.getThread();
            if (thread != null) {
                if (processProfileRecord.getReportLowMemory()) {
                    processProfileRecord.setReportLowMemory(false);
                    thread.scheduleLowMemory();
                } else {
                    thread.processInBackground();
                }
            }
        } catch (Exception unused) {
        }
    }

    @GuardedBy({"mProfilerLock"})
    private void performAppGcsLPf() {
        if (this.mProcessesToGc.size() <= 0) {
            return;
        }
        while (this.mProcessesToGc.size() > 0) {
            ProcessRecord remove = this.mProcessesToGc.remove(0);
            ProcessProfileRecord processProfileRecord = remove.mProfile;
            if (processProfileRecord.getCurRawAdj() > 200 || processProfileRecord.getReportLowMemory()) {
                if (processProfileRecord.getLastRequestedGc() + this.mService.mConstants.GC_MIN_INTERVAL <= SystemClock.uptimeMillis()) {
                    performAppGcLPf(remove);
                    scheduleAppGcsLPf();
                    return;
                } else {
                    addProcessToGcListLPf(remove);
                    scheduleAppGcsLPf();
                }
            }
        }
        scheduleAppGcsLPf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public final void performAppGcsIfAppropriateLocked() {
        synchronized (this.mProfilerLock) {
            if (this.mService.canGcNowLocked()) {
                performAppGcsLPf();
            } else {
                scheduleAppGcsLPf();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public final void scheduleAppGcsLPf() {
        this.mService.mHandler.removeMessages(5);
        if (this.mProcessesToGc.size() > 0) {
            ProcessRecord processRecord = this.mProcessesToGc.get(0);
            Message obtainMessage = this.mService.mHandler.obtainMessage(5);
            long lastRequestedGc = processRecord.mProfile.getLastRequestedGc() + this.mService.mConstants.GC_MIN_INTERVAL;
            long uptimeMillis = SystemClock.uptimeMillis();
            ActivityManagerService activityManagerService = this.mService;
            long j = activityManagerService.mConstants.GC_TIMEOUT;
            if (lastRequestedGc < uptimeMillis + j) {
                lastRequestedGc = uptimeMillis + j;
            }
            activityManagerService.mHandler.sendMessageAtTime(obtainMessage, lastRequestedGc);
        }
    }

    @GuardedBy({"mProfilerLock"})
    private void addProcessToGcListLPf(ProcessRecord processRecord) {
        boolean z = true;
        int size = this.mProcessesToGc.size() - 1;
        while (true) {
            if (size < 0) {
                z = false;
                break;
            } else {
                if (this.mProcessesToGc.get(size).mProfile.getLastRequestedGc() < processRecord.mProfile.getLastRequestedGc()) {
                    this.mProcessesToGc.add(size + 1, processRecord);
                    break;
                }
                size--;
            }
        }
        if (z) {
            return;
        }
        this.mProcessesToGc.add(0, processRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public final void doLowMemReportIfNeededLocked(final ProcessRecord processRecord) {
        if (!this.mService.mProcessList.haveBackgroundProcessLOSP()) {
            boolean z = Build.IS_DEBUGGABLE;
            final long uptimeMillis = SystemClock.uptimeMillis();
            if (z) {
                if (uptimeMillis < this.mLastMemUsageReportTime + 300000) {
                    z = false;
                } else {
                    this.mLastMemUsageReportTime = uptimeMillis;
                }
            }
            int lruSizeLOSP = this.mService.mProcessList.getLruSizeLOSP();
            final ArrayList arrayList = z ? new ArrayList(lruSizeLOSP) : null;
            EventLogTags.writeAmLowMemory(lruSizeLOSP);
            this.mService.mProcessList.forEachLruProcessesLOSP(false, new Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppProfiler.this.lambda$doLowMemReportIfNeededLocked$6(processRecord, arrayList, uptimeMillis, (ProcessRecord) obj);
                }
            });
            if (z) {
                this.mService.mHandler.sendMessage(this.mService.mHandler.obtainMessage(33, arrayList));
            }
        }
        synchronized (this.mProfilerLock) {
            scheduleAppGcsLPf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doLowMemReportIfNeededLocked$6(ProcessRecord processRecord, ArrayList arrayList, long j, ProcessRecord processRecord2) {
        if (processRecord2 == processRecord || processRecord2.getThread() == null) {
            return;
        }
        ProcessStateRecord processStateRecord = processRecord2.mState;
        if (arrayList != null) {
            arrayList.add(new ProcessMemInfo(processRecord2.processName, processRecord2.getPid(), processStateRecord.getSetAdj(), processStateRecord.getSetProcState(), processStateRecord.getAdjType(), processStateRecord.makeAdjReason()));
        }
        ProcessProfileRecord processProfileRecord = processRecord2.mProfile;
        if (processProfileRecord.getLastLowMemory() + this.mService.mConstants.GC_MIN_INTERVAL <= j) {
            synchronized (this.mProfilerLock) {
                if (processStateRecord.getSetAdj() <= 400) {
                    processProfileRecord.setLastRequestedGc(0L);
                } else {
                    processProfileRecord.setLastRequestedGc(processProfileRecord.getLastLowMemory());
                }
                processProfileRecord.setReportLowMemory(true);
                processProfileRecord.setLastLowMemory(j);
                this.mProcessesToGc.remove(processRecord2);
                addProcessToGcListLPf(processRecord2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportMemUsage(ArrayList<ProcessMemInfo> arrayList) {
        AppProfiler appProfiler;
        long j;
        long j2;
        int i;
        long j3;
        long j4;
        List<ProcessCpuTracker.Stats> list;
        int i2;
        SparseArray sparseArray;
        ArrayList<ProcessMemInfo> arrayList2 = arrayList;
        SparseArray sparseArray2 = new SparseArray(arrayList.size());
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            ProcessMemInfo processMemInfo = arrayList2.get(i3);
            sparseArray2.put(processMemInfo.pid, processMemInfo);
        }
        updateCpuStatsNow();
        long[] jArr = new long[4];
        long[] jArr2 = new long[2];
        List<ProcessCpuTracker.Stats> cpuStats = getCpuStats(new Predicate() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$reportMemUsage$7;
                lambda$reportMemUsage$7 = AppProfiler.lambda$reportMemUsage$7((ProcessCpuTracker.Stats) obj);
                return lambda$reportMemUsage$7;
            }
        });
        int size2 = cpuStats.size();
        long j5 = 0;
        long j6 = 0;
        long j7 = 0;
        int i4 = 0;
        while (i4 < size2) {
            ProcessCpuTracker.Stats stats = cpuStats.get(i4);
            long pss = Debug.getPss(stats.pid, jArr2, jArr);
            if (pss <= j5 || sparseArray2.indexOfKey(stats.pid) >= 0) {
                list = cpuStats;
                i2 = size2;
                sparseArray = sparseArray2;
            } else {
                ProcessMemInfo processMemInfo2 = new ProcessMemInfo(stats.name, stats.pid, -1000, -1, "native", null);
                processMemInfo2.pss = pss;
                list = cpuStats;
                i2 = size2;
                processMemInfo2.swapPss = jArr2[1];
                sparseArray = sparseArray2;
                processMemInfo2.memtrack = jArr[0];
                j6 += jArr[1];
                j7 += jArr[2];
                arrayList2.add(processMemInfo2);
            }
            i4++;
            cpuStats = list;
            sparseArray2 = sparseArray;
            j5 = 0;
            size2 = i2;
        }
        int size3 = arrayList.size();
        int i5 = 0;
        long j8 = 0;
        long j9 = 0;
        long j10 = 0;
        while (i5 < size3) {
            ProcessMemInfo processMemInfo3 = arrayList2.get(i5);
            int i6 = size3;
            if (processMemInfo3.pss == 0) {
                processMemInfo3.pss = Debug.getPss(processMemInfo3.pid, jArr2, jArr);
                j3 = j9;
                processMemInfo3.swapPss = jArr2[1];
                j4 = j10;
                processMemInfo3.memtrack = jArr[0];
                j6 += jArr[1];
                j7 += jArr[2];
            } else {
                j3 = j9;
                j4 = j10;
            }
            j8 += processMemInfo3.pss;
            j10 = j4 + processMemInfo3.swapPss;
            j9 = j3 + processMemInfo3.memtrack;
            i5++;
            arrayList2 = arrayList;
            size3 = i6;
        }
        long j11 = j9;
        long j12 = j10;
        Collections.sort(arrayList, new Comparator<ProcessMemInfo>() { // from class: com.android.server.am.AppProfiler.2
            @Override // java.util.Comparator
            public int compare(ProcessMemInfo processMemInfo4, ProcessMemInfo processMemInfo5) {
                int i7 = processMemInfo4.oomAdj;
                int i8 = processMemInfo5.oomAdj;
                if (i7 != i8) {
                    return i7 < i8 ? -1 : 1;
                }
                long j13 = processMemInfo4.pss;
                long j14 = processMemInfo5.pss;
                if (j13 != j14) {
                    return j13 < j14 ? 1 : -1;
                }
                return 0;
            }
        });
        StringBuilder sb = new StringBuilder(128);
        StringBuilder sb2 = new StringBuilder(128);
        sb.append("Low on memory -- ");
        ActivityManagerService.appendMemBucket(sb, j8, "total", false);
        ActivityManagerService.appendMemBucket(sb2, j8, "total", true);
        StringBuilder sb3 = new StringBuilder(1024);
        StringBuilder sb4 = new StringBuilder(1024);
        StringBuilder sb5 = new StringBuilder(1024);
        int size4 = arrayList.size();
        int i7 = Integer.MIN_VALUE;
        int i8 = 0;
        boolean z = true;
        long j13 = 0;
        long j14 = 0;
        long j15 = 0;
        while (i8 < size4) {
            ProcessMemInfo processMemInfo4 = arrayList.get(i8);
            int i9 = processMemInfo4.oomAdj;
            long j16 = j7;
            if (i9 >= 900) {
                j = j6;
                j13 += processMemInfo4.pss;
            } else {
                j = j6;
            }
            long j17 = j13;
            if (i9 == -1000 || !(i9 < 500 || i9 == 600 || i9 == 700)) {
                j2 = j;
            } else {
                if (i7 != i9) {
                    if (i9 <= 0) {
                        sb.append(" / ");
                    }
                    if (processMemInfo4.oomAdj >= 0) {
                        if (z) {
                            sb2.append(":");
                            z = false;
                        }
                        sb2.append("\n\t at ");
                    } else {
                        sb2.append("$");
                    }
                    i7 = i9;
                } else {
                    sb.append(" ");
                    sb2.append("$");
                }
                if (processMemInfo4.oomAdj <= 0) {
                    j2 = j;
                    ActivityManagerService.appendMemBucket(sb, processMemInfo4.pss, processMemInfo4.name, false);
                } else {
                    j2 = j;
                }
                ActivityManagerService.appendMemBucket(sb2, processMemInfo4.pss, processMemInfo4.name, true);
                if (processMemInfo4.oomAdj >= 0 && ((i = i8 + 1) >= size4 || arrayList.get(i).oomAdj != i7)) {
                    sb2.append("(");
                    int i10 = 0;
                    while (true) {
                        int[] iArr = ActivityManagerService.DUMP_MEM_OOM_ADJ;
                        if (i10 >= iArr.length) {
                            break;
                        }
                        if (iArr[i10] == processMemInfo4.oomAdj) {
                            sb2.append(ActivityManagerService.DUMP_MEM_OOM_LABEL[i10]);
                            sb2.append(":");
                            sb2.append(iArr[i10]);
                        }
                        i10++;
                    }
                    sb2.append(")");
                }
            }
            boolean z2 = z;
            ActivityManagerService.appendMemInfo(sb3, processMemInfo4);
            if (processMemInfo4.oomAdj == -1000) {
                long j18 = processMemInfo4.pss;
                if (j18 >= 512) {
                    ActivityManagerService.appendMemInfo(sb4, processMemInfo4);
                } else {
                    j14 += j18;
                    j15 += processMemInfo4.memtrack;
                }
            } else {
                if (j14 > 0) {
                    ActivityManagerService.appendBasicMemEntry(sb4, -1000, -1, j14, j15, "(Other native)");
                    sb4.append('\n');
                    j14 = 0;
                }
                ActivityManagerService.appendMemInfo(sb5, processMemInfo4);
            }
            i8++;
            z = z2;
            j7 = j16;
            j13 = j17;
            j6 = j2;
        }
        long j19 = j6;
        long j20 = j7;
        sb5.append("           ");
        ProcessList.appendRamKb(sb5, j8);
        sb5.append(": TOTAL");
        if (j11 > 0) {
            sb5.append(" (");
            sb5.append(ActivityManagerService.stringifyKBSize(j11));
            sb5.append(" memtrack)");
        }
        sb5.append("\n");
        MemInfoReader memInfoReader = new MemInfoReader();
        try {
            memInfoReader.readMemInfo();
        } catch (RuntimeException e) {
            Slog.e("ActivityManager", "readMemInfo :" + e);
        }
        long[] rawInfo = memInfoReader.getRawInfo();
        StringBuilder sb6 = new StringBuilder(1024);
        Debug.getMemInfo(rawInfo);
        sb6.append("  MemInfo: ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[5]));
        sb6.append(" slab, ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[4]));
        sb6.append(" shmem, ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[12]));
        sb6.append(" vm alloc, ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[13]));
        sb6.append(" page tables ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[14]));
        sb6.append(" kernel stack\n");
        sb6.append("           ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[2]));
        sb6.append(" buffers, ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[3]));
        sb6.append(" cached, ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[11]));
        sb6.append(" mapped, ");
        sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[1]));
        sb6.append(" free\n");
        if (rawInfo[10] != 0) {
            sb6.append("  ZRAM: ");
            sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[10]));
            sb6.append(" RAM, ");
            sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[8]));
            sb6.append(" swap total, ");
            sb6.append(ActivityManagerService.stringifyKBSize(rawInfo[9]));
            sb6.append(" swap free\n");
        }
        long[] ksmInfo = ActivityManagerService.getKsmInfo();
        if (ksmInfo[1] != 0 || ksmInfo[0] != 0 || ksmInfo[2] != 0 || ksmInfo[3] != 0) {
            sb6.append("  KSM: ");
            sb6.append(ActivityManagerService.stringifyKBSize(ksmInfo[1]));
            sb6.append(" saved from shared ");
            sb6.append(ActivityManagerService.stringifyKBSize(ksmInfo[0]));
            sb6.append("\n       ");
            sb6.append(ActivityManagerService.stringifyKBSize(ksmInfo[2]));
            sb6.append(" unshared; ");
            sb6.append(ActivityManagerService.stringifyKBSize(ksmInfo[3]));
            sb6.append(" volatile\n");
        }
        sb6.append("  Free RAM: ");
        sb6.append(ActivityManagerService.stringifyKBSize(j13 + memInfoReader.getCachedSizeKb() + memInfoReader.getFreeSizeKb()));
        sb6.append("\n");
        long kernelUsedSizeKb = memInfoReader.getKernelUsedSizeKb();
        long ionHeapsSizeKb = Debug.getIonHeapsSizeKb();
        long ionPoolsSizeKb = Debug.getIonPoolsSizeKb();
        long dmabufMappedSizeKb = Debug.getDmabufMappedSizeKb();
        if (ionHeapsSizeKb >= 0 && ionPoolsSizeKb >= 0) {
            sb6.append("       ION: ");
            sb6.append(ActivityManagerService.stringifyKBSize(ionHeapsSizeKb + ionPoolsSizeKb));
            sb6.append("\n");
            kernelUsedSizeKb += ionHeapsSizeKb - dmabufMappedSizeKb;
            j8 = (j8 - j19) + dmabufMappedSizeKb;
        } else {
            long dmabufTotalExportedKb = Debug.getDmabufTotalExportedKb();
            if (dmabufTotalExportedKb >= 0) {
                sb6.append("DMA-BUF: ");
                sb6.append(ActivityManagerService.stringifyKBSize(dmabufTotalExportedKb));
                sb6.append("\n");
                kernelUsedSizeKb += dmabufTotalExportedKb - dmabufMappedSizeKb;
                j8 = (j8 - j19) + dmabufMappedSizeKb;
            }
            long dmabufHeapTotalExportedKb = Debug.getDmabufHeapTotalExportedKb();
            if (dmabufHeapTotalExportedKb >= 0) {
                sb6.append("DMA-BUF Heap: ");
                sb6.append(ActivityManagerService.stringifyKBSize(dmabufHeapTotalExportedKb));
                sb6.append("\n");
            }
            long dmabufHeapPoolsSizeKb = Debug.getDmabufHeapPoolsSizeKb();
            if (dmabufHeapPoolsSizeKb >= 0) {
                sb6.append("DMA-BUF Heaps pool: ");
                sb6.append(ActivityManagerService.stringifyKBSize(dmabufHeapPoolsSizeKb));
                sb6.append("\n");
            }
        }
        long gpuTotalUsageKb = Debug.getGpuTotalUsageKb();
        if (gpuTotalUsageKb >= 0) {
            long gpuPrivateMemoryKb = Debug.getGpuPrivateMemoryKb();
            if (gpuPrivateMemoryKb >= 0) {
                sb6.append("      GPU: ");
                sb6.append(ActivityManagerService.stringifyKBSize(gpuTotalUsageKb));
                sb6.append(" (");
                sb6.append(ActivityManagerService.stringifyKBSize(gpuTotalUsageKb - gpuPrivateMemoryKb));
                sb6.append(" dmabuf + ");
                sb6.append(ActivityManagerService.stringifyKBSize(gpuPrivateMemoryKb));
                sb6.append(" private)\n");
                j8 -= j20;
                kernelUsedSizeKb += gpuPrivateMemoryKb;
            } else {
                sb6.append("       GPU: ");
                sb6.append(ActivityManagerService.stringifyKBSize(gpuTotalUsageKb));
                sb6.append("\n");
            }
        }
        sb6.append("  Used RAM: ");
        sb6.append(ActivityManagerService.stringifyKBSize((j8 - j13) + kernelUsedSizeKb));
        sb6.append("\n");
        sb6.append("  Lost RAM: ");
        sb6.append(ActivityManagerService.stringifyKBSize(((((memInfoReader.getTotalSizeKb() - (j8 - j12)) - memInfoReader.getFreeSizeKb()) - memInfoReader.getCachedSizeKb()) - kernelUsedSizeKb) - memInfoReader.getZramTotalSizeKb()));
        sb6.append("\n");
        Slog.i("ActivityManager", "Low on memory:");
        Slog.i("ActivityManager", sb4.toString());
        Slog.i("ActivityManager", sb5.toString());
        Slog.i("ActivityManager", sb6.toString());
        StringBuilder sb7 = new StringBuilder(1024);
        sb7.append("Low on memory:");
        sb7.append((CharSequence) sb2);
        sb7.append('\n');
        sb7.append((CharSequence) sb3);
        sb7.append((CharSequence) sb5);
        sb7.append('\n');
        sb7.append((CharSequence) sb6);
        sb7.append('\n');
        if (IS_HIGHTEMP_VERSION || IS_AGING_VERSION) {
            appProfiler = this;
        } else {
            StringWriter stringWriter = new StringWriter();
            appProfiler = this;
            ActivityManagerService activityManagerService = appProfiler.mService;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    PrintWriter fastPrintWriter = new FastPrintWriter(stringWriter, false, 256);
                    String[] strArr = new String[0];
                    fastPrintWriter.println();
                    ActivityManagerGlobalLock activityManagerGlobalLock = appProfiler.mProcLock;
                    ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            appProfiler.mService.mProcessList.dumpProcessesLSP(null, fastPrintWriter, strArr, 0, false, null, -1);
                        } catch (Throwable th) {
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    fastPrintWriter.println();
                    appProfiler.mService.mServices.newServiceDumperLocked(null, fastPrintWriter, strArr, 0, false, null).dumpLocked();
                    fastPrintWriter.println();
                    appProfiler.mService.mAtmInternal.dump("activities", (FileDescriptor) null, fastPrintWriter, strArr, 0, false, false, (String) null, -1);
                    fastPrintWriter.flush();
                } finally {
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
            sb7.append(stringWriter.toString());
        }
        FrameworkStatsLog.write(81);
        appProfiler.mService.addErrorToDropBox("lowmem", null, "system_server", null, null, null, sb.toString(), sb7.toString(), null, null, null, null, null);
        ActivityManagerService activityManagerService2 = appProfiler.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService2) {
            try {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (appProfiler.mLastMemUsageReportTime < uptimeMillis) {
                    appProfiler.mLastMemUsageReportTime = uptimeMillis;
                }
            } finally {
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$reportMemUsage$7(ProcessCpuTracker.Stats stats) {
        return stats.vsize > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mService"})
    public void handleMemoryPressureChangedLocked(int i, int i2) {
        this.mService.mServices.rescheduleServiceRestartOnMemoryPressureIfNeededLocked(i, i2, "mem-pressure-event", SystemClock.uptimeMillis());
    }

    @GuardedBy({"mProfilerLock"})
    private void stopProfilerLPf(ProcessRecord processRecord, int i) {
        IApplicationThread thread;
        if (processRecord == null || processRecord == this.mProfileData.getProfileProc()) {
            processRecord = this.mProfileData.getProfileProc();
            i = this.mProfileType;
            clearProfilerLPf();
        }
        if (processRecord == null || (thread = processRecord.mProfile.getThread()) == null) {
            return;
        }
        try {
            thread.profilerControl(false, (ProfilerInfo) null, i);
        } catch (RemoteException unused) {
            throw new IllegalStateException("Process disappeared");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void clearProfilerLPf() {
        if (this.mProfileData.getProfilerInfo() != null && this.mProfileData.getProfilerInfo().profileFd != null) {
            try {
                this.mProfileData.getProfilerInfo().profileFd.close();
            } catch (IOException unused) {
            }
        }
        this.mProfileData.setProfileApp(null);
        this.mProfileData.setProfileProc(null);
        this.mProfileData.setProfilerInfo(null);
    }

    @GuardedBy({"mProfilerLock"})
    void clearProfilerLPf(ProcessRecord processRecord) {
        if (this.mProfileData.getProfileProc() == null || this.mProfileData.getProfilerInfo() == null || this.mProfileData.getProfileProc() != processRecord) {
            return;
        }
        clearProfilerLPf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public boolean profileControlLPf(ProcessRecord processRecord, boolean z, ProfilerInfo profilerInfo, int i) {
        ParcelFileDescriptor parcelFileDescriptor;
        ParcelFileDescriptor parcelFileDescriptor2;
        ParcelFileDescriptor parcelFileDescriptor3;
        try {
            try {
                if (z) {
                    stopProfilerLPf(null, 0);
                    this.mService.setProfileApp(processRecord.info, processRecord.processName, profilerInfo, processRecord.isSdkSandbox ? processRecord.getClientInfoForSdkSandbox() : null);
                    this.mProfileData.setProfileProc(processRecord);
                    this.mProfileType = i;
                    try {
                        parcelFileDescriptor3 = profilerInfo.profileFd.dup();
                    } catch (IOException unused) {
                        parcelFileDescriptor3 = null;
                    }
                    profilerInfo.profileFd = parcelFileDescriptor3;
                    processRecord.mProfile.getThread().profilerControl(z, profilerInfo, i);
                    try {
                        this.mProfileData.getProfilerInfo().profileFd.close();
                    } catch (IOException unused2) {
                    }
                    this.mProfileData.getProfilerInfo().profileFd = null;
                    if (processRecord.getPid() == ActivityManagerService.MY_PID) {
                        profilerInfo = null;
                    }
                } else {
                    stopProfilerLPf(processRecord, i);
                    if (profilerInfo != null && (parcelFileDescriptor2 = profilerInfo.profileFd) != null) {
                        try {
                            parcelFileDescriptor2.close();
                        } catch (IOException unused3) {
                        }
                    }
                }
                if (profilerInfo == null || r5 == null) {
                    return true;
                }
                try {
                    profilerInfo.profileFd.close();
                    return true;
                } catch (IOException unused4) {
                    return true;
                }
            } catch (RemoteException unused5) {
                throw new IllegalStateException("Process disappeared");
            }
        } finally {
            if (profilerInfo != null && (parcelFileDescriptor = profilerInfo.profileFd) != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException unused6) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setProfileAppLPf(String str, ProfilerInfo profilerInfo) {
        this.mProfileData.setProfileApp(str);
        if (this.mProfileData.getProfilerInfo() != null && this.mProfileData.getProfilerInfo().profileFd != null) {
            try {
                this.mProfileData.getProfilerInfo().profileFd.close();
            } catch (IOException unused) {
            }
        }
        this.mProfileData.setProfilerInfo(new ProfilerInfo(profilerInfo));
        this.mProfileType = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setProfileProcLPf(ProcessRecord processRecord) {
        this.mProfileData.setProfileProc(processRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void setAgentAppLPf(String str, String str2) {
        if (str2 == null) {
            Map<String, String> map = this.mAppAgentMap;
            if (map != null) {
                map.remove(str);
                if (this.mAppAgentMap.isEmpty()) {
                    this.mAppAgentMap = null;
                    return;
                }
                return;
            }
            return;
        }
        if (this.mAppAgentMap == null) {
            this.mAppAgentMap = new HashMap();
        }
        if (this.mAppAgentMap.size() >= 100) {
            Slog.e("ActivityManager", "App agent map has too many entries, cannot add " + str + "/" + str2);
            return;
        }
        this.mAppAgentMap.put(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateCpuStats() {
        if (this.mLastCpuTime.get() < SystemClock.uptimeMillis() - MONITOR_CPU_MIN_TIME && this.mProcessCpuMutexFree.compareAndSet(true, false)) {
            synchronized (this.mProcessCpuThread) {
                this.mProcessCpuThread.notify();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateCpuStatsNow() {
        BatteryStatsImpl activeStatistics;
        long j;
        ActivityManagerService.PidMap pidMap;
        int i;
        long j2;
        ProcessProfileRecord processProfileRecord;
        ProcessCpuTracker.Stats stats;
        boolean z = true;
        int i2 = 0;
        boolean z2 = this.mService.mSystemReady && FeatureFlagUtils.isEnabled(this.mService.mContext, "settings_enable_monitor_phantom_procs");
        synchronized (this.mProcessCpuTracker) {
            this.mProcessCpuMutexFree.set(false);
            long uptimeMillis = SystemClock.uptimeMillis();
            if (this.mLastCpuTime.get() < uptimeMillis - MONITOR_CPU_MIN_TIME) {
                this.mLastCpuTime.set(uptimeMillis);
                this.mProcessCpuTracker.update();
                this.mProcessCpuTracker.mProcessCpuTrackerExt.collectAnbormalStats();
                this.mProcessCpuTracker.mProcessCpuTrackerExt.setContext(this.mService.mContext);
                if (this.mProcessCpuTracker.hasGoodLastStats()) {
                    if ("true".equals(SystemProperties.get("events.cpu"))) {
                        int lastUserTime = this.mProcessCpuTracker.getLastUserTime();
                        int lastSystemTime = this.mProcessCpuTracker.getLastSystemTime();
                        int lastIoWaitTime = this.mProcessCpuTracker.getLastIoWaitTime();
                        int lastIrqTime = this.mProcessCpuTracker.getLastIrqTime();
                        int lastSoftIrqTime = this.mProcessCpuTracker.getLastSoftIrqTime();
                        int i3 = lastUserTime + lastSystemTime + lastIoWaitTime + lastIrqTime + lastSoftIrqTime;
                        int lastIdleTime = this.mProcessCpuTracker.getLastIdleTime() + i3;
                        if (lastIdleTime == 0) {
                            lastIdleTime = 1;
                        }
                        EventLogTags.writeCpu((i3 * 100) / lastIdleTime, (lastUserTime * 100) / lastIdleTime, (lastSystemTime * 100) / lastIdleTime, (lastIoWaitTime * 100) / lastIdleTime, (lastIrqTime * 100) / lastIdleTime, (lastSoftIrqTime * 100) / lastIdleTime);
                    }
                    if (z2 && z) {
                        this.mService.mPhantomProcessList.updateProcessCpuStatesLocked(this.mProcessCpuTracker);
                    }
                    activeStatistics = this.mService.mBatteryStatsService.getActiveStatistics();
                    this.mBatteryStatsImplExt = activeStatistics.mBatteryStatsImplExt;
                    this.mProcessCpuTrackerExt = this.mProcessCpuTracker.mProcessCpuTrackerExt;
                    synchronized (activeStatistics) {
                        if (z) {
                            if (activeStatistics.startAddingCpuStatsLocked()) {
                                int countStats = this.mProcessCpuTracker.countStats();
                                long elapsedRealtime = SystemClock.elapsedRealtime();
                                long uptimeMillis2 = SystemClock.uptimeMillis();
                                ActivityManagerService.PidMap pidMap2 = this.mService.mPidsSelfLocked;
                                synchronized (pidMap2) {
                                    int i4 = 0;
                                    int i5 = 0;
                                    while (i2 < countStats) {
                                        try {
                                            ProcessCpuTracker.Stats stats2 = this.mProcessCpuTracker.getStats(i2);
                                            if (stats2.working) {
                                                ProcessRecord processRecord = this.mService.mPidsSelfLocked.get(stats2.pid);
                                                int i6 = i4 + stats2.rel_utime;
                                                int i7 = i5 + stats2.rel_stime;
                                                if (processRecord != null) {
                                                    ProcessProfileRecord processProfileRecord2 = processRecord.mProfile;
                                                    BatteryStatsImpl.Uid.Proc curProcBatteryStats = processProfileRecord2.getCurProcBatteryStats();
                                                    if (curProcBatteryStats != null && curProcBatteryStats.isActive()) {
                                                        i = countStats;
                                                        j2 = uptimeMillis;
                                                        processProfileRecord = processProfileRecord2;
                                                        pidMap = pidMap2;
                                                        stats = stats2;
                                                        curProcBatteryStats.addCpuTimeLocked(stats.rel_utime, stats.rel_stime);
                                                        processProfileRecord.mLastCpuTime.compareAndSet(0L, processProfileRecord.mCurCpuTime.addAndGet(stats.rel_utime + stats.rel_stime));
                                                    }
                                                    i = countStats;
                                                    processProfileRecord = processProfileRecord2;
                                                    j2 = uptimeMillis;
                                                    pidMap = pidMap2;
                                                    stats = stats2;
                                                    curProcBatteryStats = activeStatistics.getProcessStatsLocked(processRecord.info.uid, processRecord.processName, elapsedRealtime, uptimeMillis2);
                                                    processProfileRecord.setCurProcBatteryStats(curProcBatteryStats);
                                                    curProcBatteryStats.addCpuTimeLocked(stats.rel_utime, stats.rel_stime);
                                                    processProfileRecord.mLastCpuTime.compareAndSet(0L, processProfileRecord.mCurCpuTime.addAndGet(stats.rel_utime + stats.rel_stime));
                                                } else {
                                                    i = countStats;
                                                    j2 = uptimeMillis;
                                                    pidMap = pidMap2;
                                                    BatteryStatsImpl.Uid.Proc proc = stats2.batteryStats;
                                                    if (proc == null || !proc.isActive()) {
                                                        proc = activeStatistics.getProcessStatsLocked(stats2.uid, stats2.name, elapsedRealtime, uptimeMillis2);
                                                        stats2.batteryStats = proc;
                                                    }
                                                    proc.addCpuTimeLocked(stats2.rel_utime, stats2.rel_stime);
                                                }
                                                i4 = i6;
                                                i5 = i7;
                                            } else {
                                                i = countStats;
                                                j2 = uptimeMillis;
                                                pidMap = pidMap2;
                                            }
                                            try {
                                                i2++;
                                                countStats = i;
                                                pidMap2 = pidMap;
                                                uptimeMillis = j2;
                                            } catch (Throwable th) {
                                                th = th;
                                                throw th;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            pidMap = pidMap2;
                                        }
                                    }
                                    j = uptimeMillis;
                                    activeStatistics.addCpuStatsLocked(i4, i5, this.mProcessCpuTracker.getLastUserTime(), this.mProcessCpuTracker.getLastSystemTime(), this.mProcessCpuTracker.getLastIoWaitTime(), this.mProcessCpuTracker.getLastIrqTime(), this.mProcessCpuTracker.getLastSoftIrqTime(), this.mProcessCpuTracker.getLastIdleTime());
                                }
                            } else {
                                j = uptimeMillis;
                            }
                            activeStatistics.finishAddingCpuStatsLocked();
                        } else {
                            j = uptimeMillis;
                        }
                        ArrayMap simpleTopProcessesSnapShot = this.mProcessCpuTrackerExt.getSimpleTopProcessesSnapShot();
                        this.mBatteryStatsImplExt.setThermalCpuLoading(this.mProcessCpuTrackerExt.getLoad1(), this.mProcessCpuTrackerExt.getLoad5(), this.mProcessCpuTrackerExt.getLoad15(), (int) (this.mProcessCpuTracker.getTotalCpuPercent() * 10.0f), this.mProcessCpuTrackerExt.getMaxCpuThousandths(), this.mProcessCpuTrackerExt.getMaxCpuProName(), simpleTopProcessesSnapShot != null ? simpleTopProcessesSnapShot.toString() : "NULL");
                        if (this.mLastWriteTime < j - 1800000) {
                            this.mLastWriteTime = j;
                            this.mService.mBatteryStatsService.scheduleWriteToDisk();
                        }
                    }
                }
            }
            z = false;
            if (z2) {
                this.mService.mPhantomProcessList.updateProcessCpuStatesLocked(this.mProcessCpuTracker);
            }
            activeStatistics = this.mService.mBatteryStatsService.getActiveStatistics();
            this.mBatteryStatsImplExt = activeStatistics.mBatteryStatsImplExt;
            this.mProcessCpuTrackerExt = this.mProcessCpuTracker.mProcessCpuTrackerExt;
            synchronized (activeStatistics) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCpuTimeForPid(int i) {
        return this.mProcessCpuTracker.getCpuTimeForPid(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCpuDelayTimeForPid(int i) {
        return this.mProcessCpuTracker.getCpuDelayTimeForPid(i);
    }

    List<ProcessCpuTracker.Stats> getCpuStats(final Predicate<ProcessCpuTracker.Stats> predicate) {
        List<ProcessCpuTracker.Stats> stats;
        synchronized (this.mProcessCpuTracker) {
            stats = this.mProcessCpuTracker.getStats(new ProcessCpuTracker.FilterStats() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda7
                public final boolean needed(ProcessCpuTracker.Stats stats2) {
                    boolean test;
                    test = predicate.test(stats2);
                    return test;
                }
            });
        }
        return stats;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forAllCpuStats(Consumer<ProcessCpuTracker.Stats> consumer) {
        synchronized (this.mProcessCpuTracker) {
            int countStats = this.mProcessCpuTracker.countStats();
            for (int i = 0; i < countStats; i++) {
                consumer.accept(this.mProcessCpuTracker.getStats(i));
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class ProcessCpuThread extends Thread {
        ProcessCpuThread(String str) {
            super(str);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (AppProfiler.this.mProcessCpuTracker) {
                AppProfiler.this.mProcessCpuInitLatch.countDown();
                AppProfiler.this.mProcessCpuTracker.init();
            }
            while (true) {
                try {
                    try {
                        synchronized (this) {
                            long uptimeMillis = SystemClock.uptimeMillis();
                            long j = (AppProfiler.this.mLastCpuTime.get() + AppProfiler.MONITOR_CPU_MAX_TIME) - uptimeMillis;
                            long j2 = (AppProfiler.this.mLastWriteTime + 1800000) - uptimeMillis;
                            if (j2 < j) {
                                j = j2;
                            }
                            if (j > 0) {
                                AppProfiler.this.mProcessCpuMutexFree.set(true);
                                wait(j);
                            }
                        }
                    } catch (InterruptedException unused) {
                    }
                    AppProfiler.this.updateCpuStatsNow();
                } catch (Exception e) {
                    Slog.e("ActivityManager", "Unexpected exception collecting process stats", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class CpuBinder extends Binder {
        private final PriorityDump.PriorityDumper mPriorityDumper = new PriorityDump.PriorityDumper() { // from class: com.android.server.am.AppProfiler.CpuBinder.1
            public void dumpCritical(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
                if (DumpUtils.checkDumpAndUsageStatsPermission(AppProfiler.this.mService.mContext, "cpuinfo", printWriter)) {
                    synchronized (AppProfiler.this.mProcessCpuTracker) {
                        if (z) {
                            AppProfiler.this.mProcessCpuTracker.dumpProto(fileDescriptor);
                            return;
                        }
                        printWriter.print(AppProfiler.this.mProcessCpuTracker.printCurrentLoad());
                        printWriter.print(AppProfiler.this.mProcessCpuTracker.printCurrentState(SystemClock.uptimeMillis()));
                        AppProfiler.this.mProcessCpuTracker.mProcessCpuTrackerExt.printCpuTrack(printWriter);
                    }
                }
            }
        };

        CpuBinder() {
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            PriorityDump.dump(this.mPriorityDumper, fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCpuInfoService() {
        ServiceManager.addService("cpuinfo", new CpuBinder(), false, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppProfiler(ActivityManagerService activityManagerService, Looper looper, LowMemDetector lowMemDetector) {
        ProcessCpuTracker processCpuTracker = new ProcessCpuTracker(false);
        this.mProcessCpuTracker = processCpuTracker;
        this.mLastCpuTime = new AtomicLong(0L);
        this.mProcessCpuMutexFree = new AtomicBoolean(true);
        this.mProcessCpuInitLatch = new CountDownLatch(1);
        this.mLastWriteTime = 0L;
        this.mCachedAppsWatermarkData = new CachedAppsWatermarkData();
        this.mProfilerLock = new Object();
        this.mAppProfilerExt = (IAppProfilerExt) ExtLoader.type(IAppProfilerExt.class).create();
        this.mPssDelayConfigListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.AppProfiler.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains(AppProfiler.ACTIVITY_START_PSS_DEFER_CONFIG)) {
                    AppProfiler.this.mPssDeferralTime = properties.getLong(AppProfiler.ACTIVITY_START_PSS_DEFER_CONFIG, 0L);
                    if (ActivityManagerDebugConfig.DEBUG_PSS) {
                        Slog.d(AppProfiler.TAG_PSS, "Activity-start PSS delay now " + AppProfiler.this.mPssDeferralTime + " ms");
                    }
                }
            }
        };
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        BgHandler bgHandler = new BgHandler(looper);
        this.mBgHandler = bgHandler;
        this.mLowMemDetector = lowMemDetector;
        this.mProcessCpuThread = new ProcessCpuThread("CpuTracker");
        processCpuTracker.mProcessCpuTrackerExt.initHandler(bgHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void retrieveSettings() {
        long j = DeviceConfig.getLong("activity_manager", ACTIVITY_START_PSS_DEFER_CONFIG, 0L);
        DeviceConfig.addOnPropertiesChangedListener("activity_manager", ActivityThread.currentApplication().getMainExecutor(), this.mPssDelayConfigListener);
        this.mPssDeferralTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityManagerInternalAdded() {
        this.mProcessCpuThread.start();
        try {
            this.mProcessCpuInitLatch.await();
        } catch (InterruptedException e) {
            Slog.wtf("ActivityManager", "Interrupted wait during start", e);
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted wait during start");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityLaunched() {
        if (this.mPssDeferralTime > 0) {
            this.mBgHandler.sendMessageAtFrontOfQueue(this.mBgHandler.obtainMessage(2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x004b A[Catch: all -> 0x0183, TryCatch #0 {, blocks: (B:4:0x000d, B:6:0x0018, B:8:0x0024, B:10:0x0031, B:12:0x003b, B:17:0x004b, B:18:0x0058, B:20:0x0062, B:22:0x008a, B:24:0x008e, B:26:0x0094, B:28:0x009a, B:30:0x00a4, B:31:0x00bf, B:33:0x00c3, B:35:0x00d1, B:37:0x00d5, B:39:0x00e7, B:41:0x00ef, B:42:0x00f2, B:95:0x006d, B:97:0x0071), top: B:3:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0062 A[Catch: all -> 0x0183, TryCatch #0 {, blocks: (B:4:0x000d, B:6:0x0018, B:8:0x0024, B:10:0x0031, B:12:0x003b, B:17:0x004b, B:18:0x0058, B:20:0x0062, B:22:0x008a, B:24:0x008e, B:26:0x0094, B:28:0x009a, B:30:0x00a4, B:31:0x00bf, B:33:0x00c3, B:35:0x00d1, B:37:0x00d5, B:39:0x00e7, B:41:0x00ef, B:42:0x00f2, B:95:0x006d, B:97:0x0071), top: B:3:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0057  */
    @GuardedBy({"mService"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ProfilerInfo setupProfilerInfoLocked(IApplicationThread iApplicationThread, ProcessRecord processRecord, ActiveInstrumentation activeInstrumentation) throws IOException, RemoteException {
        ProfilerInfo profilerInfo;
        ProfilerInfo profilerInfo2;
        ProfilerInfo profilerInfo3;
        ParcelFileDescriptor parcelFileDescriptor;
        boolean z;
        String str = processRecord.processName;
        synchronized (this.mProfilerLock) {
            profilerInfo = null;
            if (this.mProfileData.getProfileApp() != null && this.mProfileData.getProfileApp().equals(str)) {
                this.mProfileData.setProfileProc(processRecord);
                if (this.mProfileData.getProfilerInfo() != null) {
                    if (this.mProfileData.getProfilerInfo().profileFile == null && !this.mProfileData.getProfilerInfo().attachAgentDuringBind) {
                        z = false;
                        profilerInfo2 = !z ? new ProfilerInfo(this.mProfileData.getProfilerInfo()) : null;
                        if (this.mProfileData.getProfilerInfo().agent != null) {
                            profilerInfo = this.mProfileData.getProfilerInfo().agent;
                        }
                        ProfilerInfo profilerInfo4 = profilerInfo;
                        profilerInfo = profilerInfo2;
                        profilerInfo3 = profilerInfo4;
                    }
                    z = true;
                    if (!z) {
                    }
                    if (this.mProfileData.getProfilerInfo().agent != null) {
                    }
                    ProfilerInfo profilerInfo42 = profilerInfo;
                    profilerInfo = profilerInfo2;
                    profilerInfo3 = profilerInfo42;
                }
                profilerInfo3 = null;
            } else {
                if (activeInstrumentation != null && activeInstrumentation.mProfileFile != null) {
                    profilerInfo2 = new ProfilerInfo(activeInstrumentation.mProfileFile, (ParcelFileDescriptor) null, 0, false, false, (String) null, false, 0);
                    ProfilerInfo profilerInfo422 = profilerInfo;
                    profilerInfo = profilerInfo2;
                    profilerInfo3 = profilerInfo422;
                }
                profilerInfo3 = null;
            }
            Map<String, String> map = this.mAppAgentMap;
            if (map != null && map.containsKey(str) && processRecord.isDebuggable()) {
                this.mAppAgentMap.get(str);
                if (profilerInfo == null) {
                    profilerInfo = new ProfilerInfo((String) null, (ParcelFileDescriptor) null, 0, false, false, this.mAppAgentMap.get(str), true, 0);
                } else if (profilerInfo.agent == null) {
                    profilerInfo = profilerInfo.setAgent(this.mAppAgentMap.get(str), true);
                }
            }
            if (profilerInfo != null && (parcelFileDescriptor = profilerInfo.profileFd) != null) {
                profilerInfo.profileFd = parcelFileDescriptor.dup();
                if (TextUtils.equals(this.mProfileData.getProfileApp(), str) && this.mProfileData.getProfilerInfo() != null) {
                    clearProfilerLPf();
                }
            }
        }
        if (this.mService.mActiveInstrumentation.size() > 0 && activeInstrumentation == null) {
            for (int size = this.mService.mActiveInstrumentation.size() - 1; size >= 0 && processRecord.getActiveInstrumentation() == null; size--) {
                ActiveInstrumentation activeInstrumentation2 = this.mService.mActiveInstrumentation.get(size);
                if (!activeInstrumentation2.mFinished && activeInstrumentation2.mTargetInfo.uid == processRecord.uid) {
                    ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            String[] strArr = activeInstrumentation2.mTargetProcesses;
                            if (strArr.length == 0) {
                                if (activeInstrumentation2.mTargetInfo.packageName.equals(processRecord.info.packageName)) {
                                    processRecord.setActiveInstrumentation(activeInstrumentation2);
                                    activeInstrumentation2.mRunningProcesses.add(processRecord);
                                }
                            } else {
                                int length = strArr.length;
                                int i = 0;
                                while (true) {
                                    if (i >= length) {
                                        break;
                                    }
                                    if (strArr[i].equals(processRecord.processName)) {
                                        processRecord.setActiveInstrumentation(activeInstrumentation2);
                                        activeInstrumentation2.mRunningProcesses.add(processRecord);
                                        break;
                                    }
                                    i++;
                                }
                            }
                        } catch (Throwable th) {
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            }
        }
        if (profilerInfo3 != null) {
            iApplicationThread.attachAgent(profilerInfo3);
        }
        if (processRecord.isDebuggable()) {
            iApplicationThread.attachStartupAgents(processRecord.info.dataDir);
        }
        return profilerInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void onCleanupApplicationRecordLocked(ProcessRecord processRecord) {
        synchronized (this.mProfilerLock) {
            ProcessProfileRecord processProfileRecord = processRecord.mProfile;
            this.mProcessesToGc.remove(processRecord);
            this.mPendingPssProfiles.remove(processProfileRecord);
            processProfileRecord.abortNextPssTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void onAppDiedLocked(ProcessRecord processRecord) {
        synchronized (this.mProfilerLock) {
            if (this.mProfileData.getProfileProc() == processRecord) {
                clearProfilerLPf();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public boolean dumpMemWatchProcessesLPf(PrintWriter printWriter, boolean z) {
        if (this.mMemWatchProcesses.getMap().size() > 0) {
            printWriter.println("  Mem watch processes:");
            ArrayMap map = this.mMemWatchProcesses.getMap();
            for (int size = map.size() - 1; size >= 0; size--) {
                String str = (String) map.keyAt(size);
                SparseArray sparseArray = (SparseArray) map.valueAt(size);
                for (int size2 = sparseArray.size() - 1; size2 >= 0; size2--) {
                    if (z) {
                        printWriter.println();
                        z = false;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("    ");
                    sb.append(str);
                    sb.append('/');
                    UserHandle.formatUid(sb, sparseArray.keyAt(size2));
                    Pair pair = (Pair) sparseArray.valueAt(size2);
                    sb.append(": ");
                    DebugUtils.sizeValueToString(((Long) pair.first).longValue(), sb);
                    if (pair.second != null) {
                        sb.append(", report to ");
                        sb.append((String) pair.second);
                    }
                    printWriter.println(sb.toString());
                }
            }
            printWriter.print("  mMemWatchDumpProcName=");
            printWriter.println(this.mMemWatchDumpProcName);
            printWriter.print("  mMemWatchDumpUri=");
            printWriter.println(this.mMemWatchDumpUri);
            printWriter.print("  mMemWatchDumpPid=");
            printWriter.println(this.mMemWatchDumpPid);
            printWriter.print("  mMemWatchDumpUid=");
            printWriter.println(this.mMemWatchDumpUid);
            printWriter.print("  mMemWatchIsUserInitiated=");
            printWriter.println(this.mMemWatchIsUserInitiated);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean dumpProfileDataLocked(PrintWriter printWriter, String str, boolean z) {
        if ((this.mProfileData.getProfileApp() != null || this.mProfileData.getProfileProc() != null || (this.mProfileData.getProfilerInfo() != null && (this.mProfileData.getProfilerInfo().profileFile != null || this.mProfileData.getProfilerInfo().profileFd != null))) && (str == null || str.equals(this.mProfileData.getProfileApp()))) {
            if (z) {
                printWriter.println();
                z = false;
            }
            printWriter.println("  mProfileApp=" + this.mProfileData.getProfileApp() + " mProfileProc=" + this.mProfileData.getProfileProc());
            if (this.mProfileData.getProfilerInfo() != null) {
                printWriter.println("  mProfileFile=" + this.mProfileData.getProfilerInfo().profileFile + " mProfileFd=" + this.mProfileData.getProfilerInfo().profileFd);
                printWriter.println("  mSamplingInterval=" + this.mProfileData.getProfilerInfo().samplingInterval + " mAutoStopProfiler=" + this.mProfileData.getProfilerInfo().autoStopProfiler + " mStreamingOutput=" + this.mProfileData.getProfilerInfo().streamingOutput + " mClockType=" + this.mProfileData.getProfilerInfo().clockType);
                StringBuilder sb = new StringBuilder();
                sb.append("  mProfileType=");
                sb.append(this.mProfileType);
                printWriter.println(sb.toString());
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpLastMemoryLevelLocked(PrintWriter printWriter) {
        int i = this.mLastMemoryLevel;
        if (i == 0) {
            printWriter.println("normal)");
            return;
        }
        if (i == 1) {
            printWriter.println("moderate)");
            return;
        }
        if (i == 2) {
            printWriter.println("low)");
        } else if (i == 3) {
            printWriter.println("critical)");
        } else {
            printWriter.print(i);
            printWriter.println(")");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void dumpMemoryLevelsLocked(PrintWriter printWriter) {
        printWriter.println("  mAllowLowerMemLevel=" + this.mAllowLowerMemLevel + " mLastMemoryLevel=" + this.mLastMemoryLevel + " mLastNumProcesses=" + this.mLastNumProcesses);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void writeMemWatchProcessToProtoLPf(ProtoOutputStream protoOutputStream) {
        if (this.mMemWatchProcesses.getMap().size() > 0) {
            long start = protoOutputStream.start(1146756268064L);
            ArrayMap map = this.mMemWatchProcesses.getMap();
            for (int i = 0; i < map.size(); i++) {
                String str = (String) map.keyAt(i);
                SparseArray sparseArray = (SparseArray) map.valueAt(i);
                long start2 = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1138166333441L, str);
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    long start3 = protoOutputStream.start(2246267895810L);
                    Pair pair = (Pair) sparseArray.valueAt(size);
                    protoOutputStream.write(1120986464257L, sparseArray.keyAt(size));
                    protoOutputStream.write(1138166333442L, DebugUtils.sizeValueToString(((Long) pair.first).longValue(), new StringBuilder()));
                    protoOutputStream.write(1138166333443L, (String) pair.second);
                    protoOutputStream.end(start3);
                }
                protoOutputStream.end(start2);
            }
            long start4 = protoOutputStream.start(1146756268034L);
            protoOutputStream.write(1138166333441L, this.mMemWatchDumpProcName);
            protoOutputStream.write(1138166333446L, this.mMemWatchDumpUri.toString());
            protoOutputStream.write(1120986464259L, this.mMemWatchDumpPid);
            protoOutputStream.write(1120986464260L, this.mMemWatchDumpUid);
            protoOutputStream.write(1133871366149L, this.mMemWatchIsUserInitiated);
            protoOutputStream.end(start4);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void writeProfileDataToProtoLocked(ProtoOutputStream protoOutputStream, String str) {
        if (this.mProfileData.getProfileApp() == null && this.mProfileData.getProfileProc() == null) {
            if (this.mProfileData.getProfilerInfo() == null) {
                return;
            }
            if (this.mProfileData.getProfilerInfo().profileFile == null && this.mProfileData.getProfilerInfo().profileFd == null) {
                return;
            }
        }
        if (str == null || str.equals(this.mProfileData.getProfileApp())) {
            long start = protoOutputStream.start(1146756268066L);
            protoOutputStream.write(1138166333441L, this.mProfileData.getProfileApp());
            this.mProfileData.getProfileProc().dumpDebug(protoOutputStream, 1146756268034L);
            if (this.mProfileData.getProfilerInfo() != null) {
                this.mProfileData.getProfilerInfo().dumpDebug(protoOutputStream, 1146756268035L);
                protoOutputStream.write(1120986464260L, this.mProfileType);
            }
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void writeMemoryLevelsToProtoLocked(ProtoOutputStream protoOutputStream) {
        protoOutputStream.write(1133871366199L, this.mAllowLowerMemLevel);
        protoOutputStream.write(1120986464312L, this.mLastMemoryLevel);
        protoOutputStream.write(1120986464313L, this.mLastNumProcesses);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void printCurrentCpuState(StringBuilder sb, long j) {
        synchronized (this.mProcessCpuTracker) {
            sb.append(this.mProcessCpuTracker.printCurrentState(j, 10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<String, String> getAppProfileStatsForDebugging(long j, int i) {
        String printCurrentLoad;
        String printCurrentState;
        synchronized (this.mProcessCpuTracker) {
            updateCpuStatsNow();
            printCurrentLoad = this.mProcessCpuTracker.printCurrentLoad();
            printCurrentState = this.mProcessCpuTracker.printCurrentState(j);
        }
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 > i) {
                break;
            }
            int indexOf = printCurrentState.indexOf(10, i3);
            if (indexOf == -1) {
                i3 = printCurrentState.length();
                break;
            }
            i3 = indexOf + 1;
            i2++;
        }
        return new Pair<>(printCurrentLoad, printCurrentState.substring(0, i3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public void writeProcessesToGcToProto(ProtoOutputStream protoOutputStream, long j, String str) {
        if (this.mProcessesToGc.size() > 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            int size = this.mProcessesToGc.size();
            for (int i = 0; i < size; i++) {
                ProcessRecord processRecord = this.mProcessesToGc.get(i);
                if (str == null || str.equals(processRecord.info.packageName)) {
                    long start = protoOutputStream.start(j);
                    ProcessProfileRecord processProfileRecord = processRecord.mProfile;
                    processRecord.dumpDebug(protoOutputStream, 1146756268033L);
                    protoOutputStream.write(1133871366146L, processProfileRecord.getReportLowMemory());
                    protoOutputStream.write(1112396529667L, uptimeMillis);
                    protoOutputStream.write(1112396529668L, processProfileRecord.getLastRequestedGc());
                    protoOutputStream.write(1112396529669L, processProfileRecord.getLastLowMemory());
                    protoOutputStream.end(start);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProfilerLock"})
    public boolean dumpProcessesToGc(PrintWriter printWriter, boolean z, String str) {
        if (this.mProcessesToGc.size() > 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            int size = this.mProcessesToGc.size();
            boolean z2 = false;
            for (int i = 0; i < size; i++) {
                ProcessRecord processRecord = this.mProcessesToGc.get(i);
                if (str == null || str.equals(processRecord.info.packageName)) {
                    if (!z2) {
                        if (z) {
                            printWriter.println();
                        }
                        printWriter.println("  Processes that are waiting to GC:");
                        z2 = true;
                        z = true;
                    }
                    printWriter.print("    Process ");
                    printWriter.println(processRecord);
                    ProcessProfileRecord processProfileRecord = processRecord.mProfile;
                    printWriter.print("      lowMem=");
                    printWriter.print(processProfileRecord.getReportLowMemory());
                    printWriter.print(", last gced=");
                    printWriter.print(uptimeMillis - processProfileRecord.getLastRequestedGc());
                    printWriter.print(" ms ago, last lowMem=");
                    printWriter.print(uptimeMillis - processProfileRecord.getLastLowMemory());
                    printWriter.println(" ms ago");
                }
            }
        }
        return z;
    }
}
