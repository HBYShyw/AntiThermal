package com.android.server.cpu;

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.IntArray;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.Preconditions;
import com.android.server.ServiceThread;
import com.android.server.SystemService;
import com.android.server.Watchdog;
import com.android.server.cpu.CpuInfoReader;
import com.android.server.cpu.CpuMonitorInternal;
import com.android.server.cpu.CpuMonitorService;
import com.android.server.utils.PriorityDump;
import com.android.server.utils.Slogf;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CpuMonitorService extends SystemService {
    private static final long CACHE_DURATION_MILLISECONDS;
    static final boolean DEBUG = Log.isLoggable(CpuMonitorService.class.getSimpleName(), 3);
    private static final long DEBUG_MONITORING_INTERVAL_MILLISECONDS;
    static final long DEFAULT_MONITORING_INTERVAL_MILLISECONDS = -1;
    private static final long LATEST_AVAILABILITY_DURATION_MILLISECONDS;
    private static final long NORMAL_MONITORING_INTERVAL_MILLISECONDS;
    static final String TAG = "CpuMonitorService";

    @GuardedBy({"mLock"})
    private final SparseArrayMap<CpuMonitorInternal.CpuAvailabilityCallback, CpuAvailabilityCallbackInfo> mAvailabilityCallbackInfosByCallbacksByCpuset;
    private final Context mContext;
    private final CpuInfoReader mCpuInfoReader;

    @GuardedBy({"mLock"})
    private final SparseArray<CpusetInfo> mCpusetInfosByCpuset;

    @GuardedBy({"mLock"})
    private long mCurrentMonitoringIntervalMillis;
    private final long mDebugMonitoringIntervalMillis;
    private Handler mHandler;
    private final HandlerThread mHandlerThread;
    private final long mLatestAvailabilityDurationMillis;
    private final CpuMonitorInternal mLocalService;
    private final Object mLock;
    private final Runnable mMonitorCpuStats;
    private final long mNormalMonitoringIntervalMillis;
    private final boolean mShouldDebugMonitor;

    static {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        NORMAL_MONITORING_INTERVAL_MILLISECONDS = timeUnit.toMillis(5L);
        TimeUnit timeUnit2 = TimeUnit.MINUTES;
        DEBUG_MONITORING_INTERVAL_MILLISECONDS = timeUnit2.toMillis(1L);
        CACHE_DURATION_MILLISECONDS = (Build.IS_USERDEBUG || Build.IS_ENG) ? timeUnit2.toMillis(30L) : timeUnit2.toMillis(10L);
        LATEST_AVAILABILITY_DURATION_MILLISECONDS = timeUnit.toMillis(30L);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CpuMonitorService(Context context) {
        this(context, r2, r3, r4, NORMAL_MONITORING_INTERVAL_MILLISECONDS, DEBUG_MONITORING_INTERVAL_MILLISECONDS, LATEST_AVAILABILITY_DURATION_MILLISECONDS);
        CpuInfoReader cpuInfoReader = new CpuInfoReader();
        boolean z = true;
        ServiceThread serviceThread = new ServiceThread(TAG, 10, true);
        if (!Build.IS_USERDEBUG && !Build.IS_ENG) {
            z = false;
        }
    }

    @VisibleForTesting
    CpuMonitorService(Context context, CpuInfoReader cpuInfoReader, HandlerThread handlerThread, boolean z, long j, long j2, long j3) {
        super(context);
        this.mLock = new Object();
        this.mMonitorCpuStats = new Runnable() { // from class: com.android.server.cpu.CpuMonitorService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CpuMonitorService.this.monitorCpuStats();
            }
        };
        this.mCurrentMonitoringIntervalMillis = -1L;
        this.mLocalService = new CpuMonitorInternal() { // from class: com.android.server.cpu.CpuMonitorService.1
            @Override // com.android.server.cpu.CpuMonitorInternal
            public void addCpuAvailabilityCallback(Executor executor, CpuAvailabilityMonitoringConfig cpuAvailabilityMonitoringConfig, CpuMonitorInternal.CpuAvailabilityCallback cpuAvailabilityCallback) {
                CpuAvailabilityCallbackInfo newCallbackInfoLocked;
                Objects.requireNonNull(cpuAvailabilityCallback, "Callback must be non-null");
                Objects.requireNonNull(cpuAvailabilityMonitoringConfig, "Config must be non-null");
                synchronized (CpuMonitorService.this.mLock) {
                    for (int i = 0; i < CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
                        CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo = (CpuAvailabilityCallbackInfo) CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.delete(CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.keyAt(i), cpuAvailabilityCallback);
                        if (cpuAvailabilityCallbackInfo != null) {
                            Slogf.i(CpuMonitorService.TAG, "Overwriting the existing %s", new Object[]{cpuAvailabilityCallbackInfo});
                        }
                    }
                    newCallbackInfoLocked = CpuMonitorService.this.newCallbackInfoLocked(cpuAvailabilityMonitoringConfig, cpuAvailabilityCallback, executor);
                }
                CpuMonitorService.this.asyncNotifyMonitoringIntervalChangeToClient(newCallbackInfoLocked);
                if (CpuMonitorService.DEBUG) {
                    Slogf.d(CpuMonitorService.TAG, "Successfully added %s", new Object[]{newCallbackInfoLocked});
                }
            }

            @Override // com.android.server.cpu.CpuMonitorInternal
            public void removeCpuAvailabilityCallback(CpuMonitorInternal.CpuAvailabilityCallback cpuAvailabilityCallback) {
                synchronized (CpuMonitorService.this.mLock) {
                    for (int i = 0; i < CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
                        CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo = (CpuAvailabilityCallbackInfo) CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.delete(CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.keyAt(i), cpuAvailabilityCallback);
                        if (cpuAvailabilityCallbackInfo != null) {
                            if (CpuMonitorService.DEBUG) {
                                Slogf.d(CpuMonitorService.TAG, "Successfully removed %s", new Object[]{cpuAvailabilityCallbackInfo});
                            }
                            CpuMonitorService.this.checkAndStopMonitoringLocked();
                            return;
                        }
                    }
                    Slogf.w(CpuMonitorService.TAG, "CpuAvailabilityCallback was not previously added. Ignoring the remove request");
                }
            }
        };
        this.mContext = context;
        this.mHandlerThread = handlerThread;
        this.mShouldDebugMonitor = z;
        this.mNormalMonitoringIntervalMillis = j;
        this.mDebugMonitoringIntervalMillis = j2;
        this.mLatestAvailabilityDurationMillis = j3;
        this.mCpuInfoReader = cpuInfoReader;
        SparseArray<CpusetInfo> sparseArray = new SparseArray<>(2);
        this.mCpusetInfosByCpuset = sparseArray;
        sparseArray.append(1, new CpusetInfo(1));
        sparseArray.append(2, new CpusetInfo(2));
        this.mAvailabilityCallbackInfosByCallbacksByCpuset = new SparseArrayMap<>();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        if (!this.mCpuInfoReader.init() || this.mCpuInfoReader.readCpuInfos() == null) {
            Slogf.wtf(TAG, "Failed to initialize CPU info reader. This happens when the CPU frequency stats are not available or the sysfs interface has changed in the Kernel. Cannot monitor CPU without these stats. Terminating CPU monitor service");
            return;
        }
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
        publishLocalService(CpuMonitorInternal.class, this.mLocalService);
        publishBinderService("cpu_monitor", new CpuMonitorBinder(), false, 1);
        Watchdog.getInstance().addThread(this.mHandler);
        synchronized (this.mLock) {
            if (this.mShouldDebugMonitor && !this.mHandler.hasCallbacks(this.mMonitorCpuStats)) {
                this.mCurrentMonitoringIntervalMillis = this.mDebugMonitoringIntervalMillis;
                Slogf.i(TAG, "Starting debug monitoring");
                this.mHandler.post(this.mMonitorCpuStats);
            }
        }
    }

    @VisibleForTesting
    long getCurrentMonitoringIntervalMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mCurrentMonitoringIntervalMillis;
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDump(final IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.printf("*%s*\n", new Object[]{CpuMonitorService.class.getSimpleName()});
        indentingPrintWriter.increaseIndent();
        this.mCpuInfoReader.dump(indentingPrintWriter);
        Object[] objArr = new Object[1];
        objArr[0] = this.mShouldDebugMonitor ? "Yes" : "No";
        indentingPrintWriter.printf("mShouldDebugMonitor = %s\n", objArr);
        indentingPrintWriter.printf("mNormalMonitoringIntervalMillis = %d\n", new Object[]{Long.valueOf(this.mNormalMonitoringIntervalMillis)});
        indentingPrintWriter.printf("mDebugMonitoringIntervalMillis = %d\n", new Object[]{Long.valueOf(this.mDebugMonitoringIntervalMillis)});
        indentingPrintWriter.printf("mLatestAvailabilityDurationMillis = %d\n", new Object[]{Long.valueOf(this.mLatestAvailabilityDurationMillis)});
        synchronized (this.mLock) {
            indentingPrintWriter.printf("mCurrentMonitoringIntervalMillis = %d\n", new Object[]{Long.valueOf(this.mCurrentMonitoringIntervalMillis)});
            if (hasClientCallbacksLocked()) {
                indentingPrintWriter.println("CPU availability change callbacks:");
                indentingPrintWriter.increaseIndent();
                this.mAvailabilityCallbackInfosByCallbacksByCpuset.forEach(new Consumer() { // from class: com.android.server.cpu.CpuMonitorService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        CpuMonitorService.lambda$doDump$0(indentingPrintWriter, (CpuMonitorService.CpuAvailabilityCallbackInfo) obj);
                    }
                });
                indentingPrintWriter.decreaseIndent();
            }
            if (this.mCpusetInfosByCpuset.size() > 0) {
                indentingPrintWriter.println("Cpuset infos:");
                indentingPrintWriter.increaseIndent();
                for (int i = 0; i < this.mCpusetInfosByCpuset.size(); i++) {
                    indentingPrintWriter.printf("%s\n", new Object[]{this.mCpusetInfosByCpuset.valueAt(i)});
                }
                indentingPrintWriter.decreaseIndent();
            }
        }
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$doDump$0(IndentingPrintWriter indentingPrintWriter, CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo) {
        indentingPrintWriter.printf("%s\n", new Object[]{cpuAvailabilityCallbackInfo});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void monitorCpuStats() {
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mHandler.removeCallbacks(this.mMonitorCpuStats);
        SparseArray<CpuInfoReader.CpuInfo> readCpuInfos = this.mCpuInfoReader.readCpuInfos();
        if (readCpuInfos == null) {
            Slogf.wtf(TAG, "Failed to read CPU info from device");
            synchronized (this.mLock) {
                stopMonitoringCpuStatsLocked();
            }
            return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < readCpuInfos.size(); i++) {
                CpuInfoReader.CpuInfo valueAt = readCpuInfos.valueAt(i);
                for (int i2 = 0; i2 < this.mCpusetInfosByCpuset.size(); i2++) {
                    this.mCpusetInfosByCpuset.valueAt(i2).appendCpuInfo(uptimeMillis, valueAt);
                }
            }
            for (int i3 = 0; i3 < this.mCpusetInfosByCpuset.size(); i3++) {
                CpusetInfo valueAt2 = this.mCpusetInfosByCpuset.valueAt(i3);
                valueAt2.populateLatestCpuAvailabilityInfo(uptimeMillis, this.mLatestAvailabilityDurationMillis);
                checkClientThresholdsAndNotifyLocked(valueAt2);
            }
            if (this.mCurrentMonitoringIntervalMillis > 0 && (hasClientCallbacksLocked() || this.mShouldDebugMonitor)) {
                this.mHandler.postAtTime(this.mMonitorCpuStats, uptimeMillis + this.mCurrentMonitoringIntervalMillis);
            } else {
                stopMonitoringCpuStatsLocked();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void checkClientThresholdsAndNotifyLocked(CpusetInfo cpusetInfo) {
        int prevCpuAvailabilityPercent = cpusetInfo.getPrevCpuAvailabilityPercent();
        CpuAvailabilityInfo latestCpuAvailabilityInfo = cpusetInfo.getLatestCpuAvailabilityInfo();
        if (latestCpuAvailabilityInfo == null || prevCpuAvailabilityPercent < 0 || this.mAvailabilityCallbackInfosByCallbacksByCpuset.numElementsForKey(cpusetInfo.cpuset) == 0) {
            return;
        }
        for (int i = 0; i < this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
            for (int i2 = 0; i2 < this.mAvailabilityCallbackInfosByCallbacksByCpuset.numElementsForKeyAt(i); i2++) {
                CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo = (CpuAvailabilityCallbackInfo) this.mAvailabilityCallbackInfosByCallbacksByCpuset.valueAt(i, i2);
                CpuAvailabilityMonitoringConfig cpuAvailabilityMonitoringConfig = cpuAvailabilityCallbackInfo.config;
                if (cpuAvailabilityMonitoringConfig.cpuset == cpusetInfo.cpuset && didCrossAnyThreshold(prevCpuAvailabilityPercent, latestCpuAvailabilityInfo.latestAvgAvailabilityPercent, cpuAvailabilityMonitoringConfig.getThresholds())) {
                    asyncNotifyCpuAvailabilityToClient(latestCpuAvailabilityInfo, cpuAvailabilityCallbackInfo);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void asyncNotifyMonitoringIntervalChangeToClient(CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo) {
        Executor executor = cpuAvailabilityCallbackInfo.executor;
        if (executor == null) {
            this.mHandler.post(cpuAvailabilityCallbackInfo.notifyMonitoringIntervalChangeRunnable);
        } else {
            executor.execute(cpuAvailabilityCallbackInfo.notifyMonitoringIntervalChangeRunnable);
        }
    }

    private void asyncNotifyCpuAvailabilityToClient(CpuAvailabilityInfo cpuAvailabilityInfo, CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo) {
        cpuAvailabilityCallbackInfo.notifyCpuAvailabilityChangeRunnable.prepare(cpuAvailabilityInfo);
        Executor executor = cpuAvailabilityCallbackInfo.executor;
        if (executor == null) {
            this.mHandler.post(cpuAvailabilityCallbackInfo.notifyCpuAvailabilityChangeRunnable);
        } else {
            executor.execute(cpuAvailabilityCallbackInfo.notifyCpuAvailabilityChangeRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public CpuAvailabilityCallbackInfo newCallbackInfoLocked(CpuAvailabilityMonitoringConfig cpuAvailabilityMonitoringConfig, CpuMonitorInternal.CpuAvailabilityCallback cpuAvailabilityCallback, Executor executor) {
        CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo = new CpuAvailabilityCallbackInfo(this, cpuAvailabilityMonitoringConfig, cpuAvailabilityCallback, executor);
        String cpusetString = CpuAvailabilityMonitoringConfig.toCpusetString(cpuAvailabilityCallbackInfo.config.cpuset);
        CpusetInfo cpusetInfo = this.mCpusetInfosByCpuset.get(cpuAvailabilityCallbackInfo.config.cpuset);
        Preconditions.checkState(cpusetInfo != null, "Missing cpuset info for cpuset %s", new Object[]{cpusetString});
        boolean hasClientCallbacksLocked = hasClientCallbacksLocked();
        this.mAvailabilityCallbackInfosByCallbacksByCpuset.add(cpuAvailabilityCallbackInfo.config.cpuset, cpuAvailabilityCallbackInfo.callback, cpuAvailabilityCallbackInfo);
        if (DEBUG) {
            Slogf.d(TAG, "Added a CPU availability callback: %s", new Object[]{cpuAvailabilityCallbackInfo});
        }
        CpuAvailabilityInfo latestCpuAvailabilityInfo = cpusetInfo.getLatestCpuAvailabilityInfo();
        if (latestCpuAvailabilityInfo != null) {
            asyncNotifyCpuAvailabilityToClient(latestCpuAvailabilityInfo, cpuAvailabilityCallbackInfo);
        }
        if (hasClientCallbacksLocked && this.mHandler.hasCallbacks(this.mMonitorCpuStats)) {
            return cpuAvailabilityCallbackInfo;
        }
        this.mHandler.removeCallbacks(this.mMonitorCpuStats);
        this.mCurrentMonitoringIntervalMillis = this.mNormalMonitoringIntervalMillis;
        this.mHandler.post(this.mMonitorCpuStats);
        return cpuAvailabilityCallbackInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void checkAndStopMonitoringLocked() {
        if (hasClientCallbacksLocked()) {
            return;
        }
        if (this.mShouldDebugMonitor) {
            if (DEBUG) {
                Slogf.e(TAG, "Switching to debug monitoring");
            }
            this.mCurrentMonitoringIntervalMillis = this.mDebugMonitoringIntervalMillis;
            return;
        }
        stopMonitoringCpuStatsLocked();
    }

    @GuardedBy({"mLock"})
    private boolean hasClientCallbacksLocked() {
        for (int i = 0; i < this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
            if (this.mAvailabilityCallbackInfosByCallbacksByCpuset.numElementsForKeyAt(i) > 0) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private void stopMonitoringCpuStatsLocked() {
        this.mHandler.removeCallbacks(this.mMonitorCpuStats);
        this.mCurrentMonitoringIntervalMillis = -1L;
        for (int i = 0; i < this.mCpusetInfosByCpuset.size(); i++) {
            this.mCpusetInfosByCpuset.valueAt(i).clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean containsCpuset(int i, int i2) {
        if (i2 == 1) {
            return (i & 1) != 0;
        }
        if (i2 == 2) {
            return (i & 2) != 0;
        }
        Slogf.wtf(TAG, "Provided invalid expectedCpuset %d", new Object[]{Integer.valueOf(i2)});
        return false;
    }

    private static boolean didCrossAnyThreshold(int i, int i2, IntArray intArray) {
        if (i == i2) {
            return false;
        }
        for (int i3 = 0; i3 < intArray.size(); i3++) {
            int i4 = intArray.get(i3);
            if (i < i4 && i2 >= i4) {
                return true;
            }
            if (i >= i4 && i2 < i4) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class CpuAvailabilityCallbackInfo {
        public final CpuMonitorInternal.CpuAvailabilityCallback callback;
        public final CpuAvailabilityMonitoringConfig config;
        public final Executor executor;
        public final CpuMonitorService service;
        public final Runnable notifyMonitoringIntervalChangeRunnable = new Runnable() { // from class: com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.1
            @Override // java.lang.Runnable
            public void run() {
                CpuAvailabilityCallbackInfo cpuAvailabilityCallbackInfo = CpuAvailabilityCallbackInfo.this;
                cpuAvailabilityCallbackInfo.callback.onMonitoringIntervalChanged(cpuAvailabilityCallbackInfo.service.getCurrentMonitoringIntervalMillis());
            }
        };
        public final NotifyCpuAvailabilityChangeRunnable notifyCpuAvailabilityChangeRunnable = new NotifyCpuAvailabilityChangeRunnable();

        CpuAvailabilityCallbackInfo(CpuMonitorService cpuMonitorService, CpuAvailabilityMonitoringConfig cpuAvailabilityMonitoringConfig, CpuMonitorInternal.CpuAvailabilityCallback cpuAvailabilityCallback, Executor executor) {
            this.service = cpuMonitorService;
            this.config = cpuAvailabilityMonitoringConfig;
            this.callback = cpuAvailabilityCallback;
            this.executor = executor;
        }

        public String toString() {
            return "CpuAvailabilityCallbackInfo{config = " + this.config + ", callback = " + this.callback + ", mExecutor = " + this.executor + '}';
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public final class NotifyCpuAvailabilityChangeRunnable implements Runnable {

            @GuardedBy({"mLock"})
            private CpuAvailabilityInfo mCpuAvailabilityInfo;
            private final Object mLock;

            private NotifyCpuAvailabilityChangeRunnable() {
                this.mLock = new Object();
            }

            public void prepare(CpuAvailabilityInfo cpuAvailabilityInfo) {
                synchronized (this.mLock) {
                    this.mCpuAvailabilityInfo = cpuAvailabilityInfo;
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (this.mLock) {
                    CpuAvailabilityCallbackInfo.this.callback.onAvailabilityChanged(this.mCpuAvailabilityInfo);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class CpuMonitorBinder extends Binder {
        private final PriorityDump.PriorityDumper mPriorityDumper;

        private CpuMonitorBinder() {
            this.mPriorityDumper = new PriorityDump.PriorityDumper() { // from class: com.android.server.cpu.CpuMonitorService.CpuMonitorBinder.1
                public void dumpCritical(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
                    if (!DumpUtils.checkDumpAndUsageStatsPermission(CpuMonitorService.this.mContext, CpuMonitorService.TAG, printWriter) || z) {
                        return;
                    }
                    IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
                    try {
                        CpuMonitorService.this.doDump(indentingPrintWriter);
                        indentingPrintWriter.close();
                    } catch (Throwable th) {
                        try {
                            indentingPrintWriter.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            };
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            PriorityDump.dump(this.mPriorityDumper, fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class CpusetInfo {
        public final int cpuset;
        private CpuAvailabilityInfo mLatestCpuAvailabilityInfo;
        private final LongSparseArray<Snapshot> mSnapshotsByUptime = new LongSparseArray<>();

        CpusetInfo(int i) {
            this.cpuset = i;
        }

        public void appendCpuInfo(long j, CpuInfoReader.CpuInfo cpuInfo) {
            if (CpuMonitorService.containsCpuset(cpuInfo.cpusetCategories, this.cpuset)) {
                Snapshot snapshot = this.mSnapshotsByUptime.get(j);
                if (snapshot == null) {
                    snapshot = new Snapshot(j);
                    this.mSnapshotsByUptime.append(j, snapshot);
                    if (this.mSnapshotsByUptime.size() > 0 && j - this.mSnapshotsByUptime.valueAt(0).uptimeMillis > CpuMonitorService.CACHE_DURATION_MILLISECONDS) {
                        this.mSnapshotsByUptime.removeAt(0);
                    }
                }
                snapshot.appendCpuInfo(cpuInfo);
            }
        }

        public CpuAvailabilityInfo getLatestCpuAvailabilityInfo() {
            return this.mLatestCpuAvailabilityInfo;
        }

        public void populateLatestCpuAvailabilityInfo(long j, long j2) {
            int size = this.mSnapshotsByUptime.size();
            if (size == 0) {
                this.mLatestCpuAvailabilityInfo = null;
                return;
            }
            Snapshot valueAt = this.mSnapshotsByUptime.valueAt(size - 1);
            long j3 = valueAt.uptimeMillis;
            if (j3 != j) {
                if (CpuMonitorService.DEBUG) {
                    Slogf.d(CpuMonitorService.TAG, "Skipping stale CPU availability information for cpuset %s", new Object[]{CpuAvailabilityMonitoringConfig.toCpusetString(this.cpuset)});
                }
                this.mLatestCpuAvailabilityInfo = null;
            } else {
                CpuAvailabilityInfo cpuAvailabilityInfo = this.mLatestCpuAvailabilityInfo;
                if (cpuAvailabilityInfo == null || cpuAvailabilityInfo.dataTimestampUptimeMillis != j3) {
                    this.mLatestCpuAvailabilityInfo = new CpuAvailabilityInfo(this.cpuset, j3, valueAt.getAverageAvailableCpuFreqPercent(), getCumulativeAvgAvailabilityPercent(j - j2), j2);
                }
            }
        }

        public int getPrevCpuAvailabilityPercent() {
            int size = this.mSnapshotsByUptime.size();
            if (size < 2) {
                return -1;
            }
            return this.mSnapshotsByUptime.valueAt(size - 2).getAverageAvailableCpuFreqPercent();
        }

        private int getCumulativeAvgAvailabilityPercent(long j) {
            int size = this.mSnapshotsByUptime.size() - 1;
            long j2 = 0;
            long j3 = Long.MAX_VALUE;
            int i = 0;
            long j4 = 0;
            while (true) {
                if (size < 0) {
                    break;
                }
                Snapshot valueAt = this.mSnapshotsByUptime.valueAt(size);
                long j5 = valueAt.uptimeMillis;
                if (j5 <= j) {
                    j3 = j5;
                    break;
                }
                i++;
                j2 += valueAt.totalNormalizedAvailableCpuFreqKHz;
                j4 += valueAt.totalOnlineMaxCpuFreqKHz;
                size--;
                j3 = j5;
            }
            if (j3 > j || i < 2) {
                return -1;
            }
            return (int) ((j2 * 100.0d) / j4);
        }

        public void clear() {
            this.mLatestCpuAvailabilityInfo = null;
            this.mSnapshotsByUptime.clear();
        }

        public String toString() {
            return "CpusetInfo{cpuset = " + CpuAvailabilityMonitoringConfig.toCpusetString(this.cpuset) + ", mSnapshotsByUptime = " + this.mSnapshotsByUptime + ", mLatestCpuAvailabilityInfo = " + this.mLatestCpuAvailabilityInfo + '}';
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public static final class Snapshot {
            public long totalNormalizedAvailableCpuFreqKHz;
            public int totalOfflineCpus;
            public long totalOfflineMaxCpuFreqKHz;
            public int totalOnlineCpus;
            public long totalOnlineMaxCpuFreqKHz;
            public final long uptimeMillis;

            Snapshot(long j) {
                this.uptimeMillis = j;
            }

            public void appendCpuInfo(CpuInfoReader.CpuInfo cpuInfo) {
                if (!cpuInfo.isOnline) {
                    this.totalOfflineCpus++;
                    this.totalOfflineMaxCpuFreqKHz += cpuInfo.maxCpuFreqKHz;
                } else {
                    this.totalOnlineCpus++;
                    this.totalNormalizedAvailableCpuFreqKHz += cpuInfo.getNormalizedAvailableCpuFreqKHz();
                    this.totalOnlineMaxCpuFreqKHz += cpuInfo.maxCpuFreqKHz;
                }
            }

            public int getAverageAvailableCpuFreqPercent() {
                int i = (int) ((this.totalNormalizedAvailableCpuFreqKHz * 100.0d) / this.totalOnlineMaxCpuFreqKHz);
                if (i >= 0) {
                    return i;
                }
                Slogf.wtf(CpuMonitorService.TAG, "Computed negative CPU availability percent(%d) for %s ", new Object[]{Integer.valueOf(i), toString()});
                return 0;
            }

            public String toString() {
                return "Snapshot{uptimeMillis = " + this.uptimeMillis + ", totalOnlineCpus = " + this.totalOnlineCpus + ", totalOfflineCpus = " + this.totalOfflineCpus + ", totalNormalizedAvailableCpuFreqKHz = " + this.totalNormalizedAvailableCpuFreqKHz + ", totalOnlineMaxCpuFreqKHz = " + this.totalOnlineMaxCpuFreqKHz + ", totalOfflineMaxCpuFreqKHz = " + this.totalOfflineMaxCpuFreqKHz + '}';
            }
        }
    }
}
