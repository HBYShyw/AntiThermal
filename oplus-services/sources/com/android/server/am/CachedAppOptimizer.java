package com.android.server.am;

import android.app.ActivityThread;
import android.app.IApplicationThread;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.IntArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.ProcLocksReader;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.ServiceThread;
import com.android.server.am.CachedAppOptimizer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CachedAppOptimizer {
    static final int ASYNC_RECEIVED_WHILE_FROZEN = 2;
    private static final String ATRACE_COMPACTION_TRACK = "Compaction";
    private static final String ATRACE_FREEZER_TRACK = "Freezer";
    private static final int COMPACT_ACTION_ANON_FLAG = 2;
    private static final int COMPACT_ACTION_FILE_FLAG = 1;
    static final double COMPACT_DOWNGRADE_FREE_SWAP_THRESHOLD = 0.2d;
    static final int COMPACT_NATIVE_MSG = 5;
    static final int COMPACT_PROCESS_MSG = 1;
    static final int COMPACT_SYSTEM_MSG = 2;
    static final int DEADLOCK_WATCHDOG_MSG = 7;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB = 8000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB = 12000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_1 = 5000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_2 = 10000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_3 = 500;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_4 = 10000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_5 = 600000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_6 = 600000;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ = 999;

    @VisibleForTesting
    static final long DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ = 900;

    @VisibleForTesting
    static final long DEFAULT_FREEZER_BINDER_DIVISOR = 4;

    @VisibleForTesting
    static final boolean DEFAULT_FREEZER_BINDER_ENABLED = true;

    @VisibleForTesting
    static final int DEFAULT_FREEZER_BINDER_OFFSET = 500;

    @VisibleForTesting
    static final long DEFAULT_FREEZER_BINDER_THRESHOLD = 1000;

    @VisibleForTesting
    static final long DEFAULT_FREEZER_DEBOUNCE_TIMEOUT = 10000;

    @VisibleForTesting
    static final boolean DEFAULT_FREEZER_EXEMPT_INST_PKG = true;

    @VisibleForTesting
    static final float DEFAULT_STATSD_SAMPLE_RATE = 0.1f;

    @VisibleForTesting
    static final boolean DEFAULT_USE_COMPACTION = false;

    @VisibleForTesting
    static final boolean DEFAULT_USE_FREEZER = true;
    static final int DO_FREEZE = 1;

    @VisibleForTesting
    static final boolean ENABLE_FILE_COMPACT = false;
    private static final int FREEZE_BINDER_TIMEOUT_MS = 100;
    private static final int FREEZE_DEADLOCK_TIMEOUT_MS = 1000;

    @VisibleForTesting
    static final String KEY_COMPACT_FULL_DELTA_RSS_THROTTLE_KB = "compact_full_delta_rss_throttle_kb";

    @VisibleForTesting
    static final String KEY_COMPACT_FULL_RSS_THROTTLE_KB = "compact_full_rss_throttle_kb";

    @VisibleForTesting
    static final String KEY_COMPACT_PROC_STATE_THROTTLE = "compact_proc_state_throttle";

    @VisibleForTesting
    static final String KEY_COMPACT_STATSD_SAMPLE_RATE = "compact_statsd_sample_rate";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_1 = "compact_throttle_1";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_2 = "compact_throttle_2";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_3 = "compact_throttle_3";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_4 = "compact_throttle_4";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_5 = "compact_throttle_5";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_6 = "compact_throttle_6";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_MAX_OOM_ADJ = "compact_throttle_max_oom_adj";

    @VisibleForTesting
    static final String KEY_COMPACT_THROTTLE_MIN_OOM_ADJ = "compact_throttle_min_oom_adj";

    @VisibleForTesting
    static final String KEY_FREEZER_BINDER_DIVISOR = "freeze_binder_divisor";

    @VisibleForTesting
    static final String KEY_FREEZER_BINDER_ENABLED = "freeze_binder_enabled";

    @VisibleForTesting
    static final String KEY_FREEZER_BINDER_OFFSET = "freeze_binder_offset";

    @VisibleForTesting
    static final String KEY_FREEZER_BINDER_THRESHOLD = "freeze_binder_threshold";

    @VisibleForTesting
    static final String KEY_FREEZER_DEBOUNCE_TIMEOUT = "freeze_debounce_timeout";

    @VisibleForTesting
    static final String KEY_FREEZER_EXEMPT_INST_PKG = "freeze_exempt_inst_pkg";

    @VisibleForTesting
    static final String KEY_FREEZER_STATSD_SAMPLE_RATE = "freeze_statsd_sample_rate";

    @VisibleForTesting
    static final String KEY_USE_COMPACTION = "use_compaction";

    @VisibleForTesting
    static final String KEY_USE_FREEZER = "use_freezer";
    static final int LAST_COMPACTED_ANY_PROCESS_STATS_HISTORY_SIZE = 20;
    static final int LAST_COMPACTION_FOR_PROCESS_STATS_SIZE = 256;
    static final int REPORT_UNFREEZE = 2;
    static final int REPORT_UNFREEZE_MSG = 4;
    private static final int RSS_ANON_INDEX = 2;
    private static final int RSS_FILE_INDEX = 1;
    private static final int RSS_SWAP_INDEX = 3;
    private static final int RSS_TOTAL_INDEX = 0;
    static final int SET_FROZEN_PROCESS_MSG = 3;
    static final int SYNC_RECEIVED_WHILE_FROZEN = 1;
    static final int TXNS_PENDING_WHILE_FROZEN = 4;
    static final int UID_FROZEN_STATE_CHANGED_MSG = 6;
    static final int UNFREEZE_REASON_ACTIVITY = 1;
    static final int UNFREEZE_REASON_ALLOWLIST = 10;
    static final int UNFREEZE_REASON_BACKUP = 22;
    static final int UNFREEZE_REASON_BINDER_TXNS = 18;
    static final int UNFREEZE_REASON_BIND_SERVICE = 4;
    static final int UNFREEZE_REASON_COMPONENT_DISABLED = 29;
    static final int UNFREEZE_REASON_EXECUTING_SERVICE = 27;
    static final int UNFREEZE_REASON_FEATURE_FLAGS = 19;
    static final int UNFREEZE_REASON_FILE_LOCKS = 16;
    static final int UNFREEZE_REASON_FILE_LOCK_CHECK_FAILURE = 17;
    static final int UNFREEZE_REASON_FINISH_RECEIVER = 2;
    static final int UNFREEZE_REASON_GET_PROVIDER = 7;
    static final int UNFREEZE_REASON_NONE = 0;
    static final int UNFREEZE_REASON_PING = 15;
    static final int UNFREEZE_REASON_PROCESS_BEGIN = 11;
    static final int UNFREEZE_REASON_PROCESS_END = 12;
    static final int UNFREEZE_REASON_REMOVE_PROVIDER = 8;
    static final int UNFREEZE_REASON_REMOVE_TASK = 24;
    static final int UNFREEZE_REASON_RESTRICTION_CHANGE = 28;
    static final int UNFREEZE_REASON_SHELL = 23;
    static final int UNFREEZE_REASON_SHORT_FGS_TIMEOUT = 20;
    static final int UNFREEZE_REASON_START_RECEIVER = 3;
    static final int UNFREEZE_REASON_START_SERVICE = 6;
    static final int UNFREEZE_REASON_STOP_SERVICE = 26;
    static final int UNFREEZE_REASON_SYSTEM_INIT = 21;
    static final int UNFREEZE_REASON_TRIM_MEMORY = 13;
    static final int UNFREEZE_REASON_UID_IDLE = 25;
    static final int UNFREEZE_REASON_UI_VISIBILITY = 9;
    static final int UNFREEZE_REASON_UNBIND_SERVICE = 5;
    private final ActivityManagerService mAm;
    public ICachedAppOptimizerExt mCachedAppOptimizerExt;
    final ServiceThread mCachedAppOptimizerThread;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile float mCompactStatsdSampleRate;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mCompactThrottleFullFull;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mCompactThrottleFullSome;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mCompactThrottleMaxOomAdj;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mCompactThrottleMinOomAdj;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mCompactThrottleSomeFull;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mCompactThrottleSomeSome;

    @VisibleForTesting
    Handler mCompactionHandler;
    LinkedList<SingleCompactionStats> mCompactionStatsHistory;
    private Handler mFreezeHandler;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mFreezerBinderDivisor;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile boolean mFreezerBinderEnabled;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile int mFreezerBinderOffset;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mFreezerBinderThreshold;

    @VisibleForTesting
    volatile long mFreezerDebounceTimeout;

    @GuardedBy({"this"})
    private int mFreezerDisableCount;

    @VisibleForTesting
    volatile boolean mFreezerExemptInstPkg;
    public final Object mFreezerLock;

    @GuardedBy({"mProcLock"})
    private boolean mFreezerOverride;

    @VisibleForTesting
    volatile float mFreezerStatsdSampleRate;

    @GuardedBy({"mProcLock"})
    private final SparseArray<ProcessRecord> mFrozenProcesses;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mFullAnonRssThrottleKb;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    volatile long mFullDeltaRssThrottleKb;

    @GuardedBy({"mProcLock"})
    @VisibleForTesting
    LinkedHashMap<Integer, SingleCompactionStats> mLastCompactionStats;
    private final DeviceConfig.OnPropertiesChangedListener mOnFlagsChangedListener;
    private final DeviceConfig.OnPropertiesChangedListener mOnNativeBootFlagsChangedListener;

    @GuardedBy({"mProcLock"})
    private final ArrayList<ProcessRecord> mPendingCompactionProcesses;
    private final LinkedHashMap<String, AggregatedProcessCompactionStats> mPerProcessCompactStats;
    private final EnumMap<CompactSource, AggregatedSourceCompactionStats> mPerSourceCompactStats;

    @VisibleForTesting
    final Object mPhenotypeFlagLock;
    private final ActivityManagerGlobalLock mProcLock;
    private final ProcLocksReader mProcLocksReader;

    @GuardedBy({"mPhenotypeFlagLock"})
    @VisibleForTesting
    final Set<Integer> mProcStateThrottle;
    private final ProcessDependencies mProcessDependencies;
    private final Random mRandom;
    private final SettingsContentObserver mSettingsObserver;
    private long mSystemCompactionsPerformed;
    private long mSystemTotalMemFreed;
    private PropertyChangedCallbackForTest mTestCallback;
    private long mTotalCompactionDowngrades;
    private EnumMap<CancelCompactReason, Integer> mTotalCompactionsCancelled;

    @GuardedBy({"mPhenotypeFlagLock"})
    private volatile boolean mUseCompaction;
    private volatile boolean mUseFreezer;
    private CachedAppOptimizerWrapper mWrapper;

    @VisibleForTesting
    static final String DEFAULT_COMPACT_PROC_STATE_THROTTLE = String.valueOf(11);

    @VisibleForTesting
    static final Uri CACHED_APP_FREEZER_ENABLED_URI = Settings.Global.getUriFor("cached_apps_freezer");

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum CancelCompactReason {
        SCREEN_ON,
        OOM_IMPROVEMENT
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum CompactProfile {
        NONE,
        SOME,
        ANON,
        FULL
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum CompactSource {
        APP,
        SHELL
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface ProcessDependencies {
        long[] getRss(int i);

        void performCompaction(CompactProfile compactProfile, int i) throws IOException;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface PropertyChangedCallbackForTest {
        void onPropertyChanged();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface UnfreezeReason {
    }

    /* renamed from: -$$Nest$smgetMemoryFreedCompaction, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m1431$$Nest$smgetMemoryFreedCompaction() {
        return getMemoryFreedCompaction();
    }

    /* renamed from: -$$Nest$smgetUsedZramMemory, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m1432$$Nest$smgetUsedZramMemory() {
        return getUsedZramMemory();
    }

    /* renamed from: -$$Nest$smthreadCpuTimeNs, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m1433$$Nest$smthreadCpuTimeNs() {
        return threadCpuTimeNs();
    }

    private static native void cancelCompaction();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void compactProcess(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void compactSystem();

    public static native int freezeBinder(int i, boolean z, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getBinderFreezeInfo(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native double getFreeSwapPercent();

    private static native String getFreezerCheckPath();

    private static native long getMemoryFreedCompaction();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnfreezeReasonCodeFromOomAdjReason(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 20;
            case 14:
                return 21;
            case 15:
                return 22;
            case 16:
                return 23;
            case 17:
                return 24;
            case 18:
                return 25;
            case 19:
                return 26;
            case 20:
                return 27;
            case 21:
                return 28;
            case 22:
                return 29;
            default:
                return 0;
        }
    }

    private static native long getUsedZramMemory();

    private static native boolean isFreezerProfileValid();

    private static native long threadCpuTimeNs();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SettingsContentObserver extends ContentObserver {
        SettingsContentObserver() {
            super(CachedAppOptimizer.this.mAm.mHandler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (CachedAppOptimizer.CACHED_APP_FREEZER_ENABLED_URI.equals(uri)) {
                synchronized (CachedAppOptimizer.this.mPhenotypeFlagLock) {
                    CachedAppOptimizer.this.updateUseFreezer();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AggregatedCompactionStats {
        public long mFullCompactPerformed;
        public long mFullCompactRequested;
        public double mMaxCompactEfficiency;
        public long mProcCompactionsMiscThrottled;
        public long mProcCompactionsNoPidThrottled;
        public long mProcCompactionsOomAdjThrottled;
        public long mProcCompactionsRSSThrottled;
        public long mProcCompactionsTimeThrottled;
        public long mSomeCompactPerformed;
        public long mSomeCompactRequested;
        public long mSumOrigAnonRss;
        public long mTotalAnonMemFreedKBs;
        public long mTotalCpuTimeMillis;
        public long mTotalDeltaAnonRssKBs;
        public long mTotalZramConsumedKBs;

        AggregatedCompactionStats() {
        }

        public long getThrottledSome() {
            return this.mSomeCompactRequested - this.mSomeCompactPerformed;
        }

        public long getThrottledFull() {
            return this.mFullCompactRequested - this.mFullCompactPerformed;
        }

        public void addMemStats(long j, long j2, long j3, long j4, long j5) {
            double d = j3 / j4;
            if (d > this.mMaxCompactEfficiency) {
                this.mMaxCompactEfficiency = d;
            }
            this.mTotalDeltaAnonRssKBs += j;
            this.mTotalZramConsumedKBs += j2;
            this.mTotalAnonMemFreedKBs += j3;
            this.mSumOrigAnonRss += j4;
            this.mTotalCpuTimeMillis += j5;
        }

        public void dump(PrintWriter printWriter) {
            long j = this.mSomeCompactRequested + this.mFullCompactRequested;
            long j2 = this.mSomeCompactPerformed + this.mFullCompactPerformed;
            printWriter.println("    Performed / Requested:");
            printWriter.println("      Some: (" + this.mSomeCompactPerformed + "/" + this.mSomeCompactRequested + ")");
            printWriter.println("      Full: (" + this.mFullCompactPerformed + "/" + this.mFullCompactRequested + ")");
            long throttledSome = getThrottledSome();
            long throttledFull = getThrottledFull();
            if (throttledSome > 0 || throttledFull > 0) {
                printWriter.println("    Throttled:");
                printWriter.println("       Some: " + throttledSome + " Full: " + throttledFull);
                printWriter.println("    Throttled by Type:");
                long j3 = j - j2;
                printWriter.println("       NoPid: " + this.mProcCompactionsNoPidThrottled + " OomAdj: " + this.mProcCompactionsOomAdjThrottled + " Time: " + this.mProcCompactionsTimeThrottled + " RSS: " + this.mProcCompactionsRSSThrottled + " Misc: " + this.mProcCompactionsMiscThrottled + " Unaccounted: " + (((((j3 - this.mProcCompactionsNoPidThrottled) - this.mProcCompactionsOomAdjThrottled) - this.mProcCompactionsTimeThrottled) - this.mProcCompactionsRSSThrottled) - this.mProcCompactionsMiscThrottled));
                double d = (((double) j3) / ((double) j)) * 100.0d;
                StringBuilder sb = new StringBuilder();
                sb.append("    Throttle Percentage: ");
                sb.append(d);
                printWriter.println(sb.toString());
            }
            if (this.mFullCompactPerformed > 0) {
                printWriter.println("    -----Memory Stats----");
                printWriter.println("    Total Delta Anon RSS (KB) : " + this.mTotalDeltaAnonRssKBs);
                printWriter.println("    Total Physical ZRAM Consumed (KB): " + this.mTotalZramConsumedKBs);
                printWriter.println("    Total Anon Memory Freed (KB): " + this.mTotalAnonMemFreedKBs);
                printWriter.println("    Avg Compaction Efficiency (Anon Freed/Anon RSS): " + (((double) this.mTotalAnonMemFreedKBs) / ((double) this.mSumOrigAnonRss)));
                printWriter.println("    Max Compaction Efficiency: " + this.mMaxCompactEfficiency);
                printWriter.println("    Avg Compression Ratio (1 - ZRAM Consumed/DeltaAnonRSS): " + (1.0d - (((double) this.mTotalZramConsumedKBs) / ((double) this.mTotalDeltaAnonRssKBs))));
                long j4 = this.mFullCompactPerformed;
                printWriter.println("    Avg Anon Mem Freed/Compaction (KB) : " + (j4 > 0 ? this.mTotalAnonMemFreedKBs / j4 : 0L));
                printWriter.println("    Compaction Cost (ms/MB): " + (((double) this.mTotalCpuTimeMillis) / (((double) this.mTotalAnonMemFreedKBs) / 1024.0d)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AggregatedProcessCompactionStats extends AggregatedCompactionStats {
        public final String processName;

        AggregatedProcessCompactionStats(String str) {
            super();
            this.processName = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AggregatedSourceCompactionStats extends AggregatedCompactionStats {
        public final CompactSource sourceType;

        AggregatedSourceCompactionStats(CompactSource compactSource) {
            super();
            this.sourceType = compactSource;
        }
    }

    public CachedAppOptimizer(ActivityManagerService activityManagerService) {
        this(activityManagerService, null, new DefaultProcessDependencies());
    }

    @VisibleForTesting
    CachedAppOptimizer(ActivityManagerService activityManagerService, PropertyChangedCallbackForTest propertyChangedCallbackForTest, ProcessDependencies processDependencies) {
        this.mPendingCompactionProcesses = new ArrayList<>();
        this.mFrozenProcesses = new SparseArray<>();
        this.mFreezerLock = new Object();
        this.mCachedAppOptimizerExt = (ICachedAppOptimizerExt) ExtLoader.type(ICachedAppOptimizerExt.class).create();
        this.mOnFlagsChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CachedAppOptimizer.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                synchronized (CachedAppOptimizer.this.mPhenotypeFlagLock) {
                    for (String str : properties.getKeyset()) {
                        if (CachedAppOptimizer.KEY_USE_COMPACTION.equals(str)) {
                            CachedAppOptimizer.this.updateUseCompaction();
                        } else {
                            if (!CachedAppOptimizer.KEY_COMPACT_THROTTLE_1.equals(str) && !CachedAppOptimizer.KEY_COMPACT_THROTTLE_2.equals(str) && !CachedAppOptimizer.KEY_COMPACT_THROTTLE_3.equals(str) && !CachedAppOptimizer.KEY_COMPACT_THROTTLE_4.equals(str) && !CachedAppOptimizer.KEY_COMPACT_THROTTLE_5.equals(str) && !CachedAppOptimizer.KEY_COMPACT_THROTTLE_6.equals(str)) {
                                if (CachedAppOptimizer.KEY_COMPACT_STATSD_SAMPLE_RATE.equals(str)) {
                                    CachedAppOptimizer.this.updateCompactStatsdSampleRate();
                                } else if (CachedAppOptimizer.KEY_FREEZER_STATSD_SAMPLE_RATE.equals(str)) {
                                    CachedAppOptimizer.this.updateFreezerStatsdSampleRate();
                                } else if (CachedAppOptimizer.KEY_COMPACT_FULL_RSS_THROTTLE_KB.equals(str)) {
                                    CachedAppOptimizer.this.updateFullRssThrottle();
                                } else if (CachedAppOptimizer.KEY_COMPACT_FULL_DELTA_RSS_THROTTLE_KB.equals(str)) {
                                    CachedAppOptimizer.this.updateFullDeltaRssThrottle();
                                } else if (CachedAppOptimizer.KEY_COMPACT_PROC_STATE_THROTTLE.equals(str)) {
                                    CachedAppOptimizer.this.updateProcStateThrottle();
                                } else if (CachedAppOptimizer.KEY_COMPACT_THROTTLE_MIN_OOM_ADJ.equals(str)) {
                                    CachedAppOptimizer.this.updateMinOomAdjThrottle();
                                } else if (CachedAppOptimizer.KEY_COMPACT_THROTTLE_MAX_OOM_ADJ.equals(str)) {
                                    CachedAppOptimizer.this.updateMaxOomAdjThrottle();
                                }
                            }
                            CachedAppOptimizer.this.updateCompactionThrottles();
                        }
                    }
                }
                if (CachedAppOptimizer.this.mTestCallback != null) {
                    CachedAppOptimizer.this.mTestCallback.onPropertyChanged();
                }
            }
        };
        this.mOnNativeBootFlagsChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CachedAppOptimizer.2
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                synchronized (CachedAppOptimizer.this.mPhenotypeFlagLock) {
                    for (String str : properties.getKeyset()) {
                        if (CachedAppOptimizer.KEY_FREEZER_DEBOUNCE_TIMEOUT.equals(str)) {
                            CachedAppOptimizer.this.updateFreezerDebounceTimeout();
                        } else if (CachedAppOptimizer.KEY_FREEZER_EXEMPT_INST_PKG.equals(str)) {
                            CachedAppOptimizer.this.updateFreezerExemptInstPkg();
                        } else if (CachedAppOptimizer.KEY_FREEZER_BINDER_ENABLED.equals(str) || CachedAppOptimizer.KEY_FREEZER_BINDER_DIVISOR.equals(str) || CachedAppOptimizer.KEY_FREEZER_BINDER_THRESHOLD.equals(str) || CachedAppOptimizer.KEY_FREEZER_BINDER_OFFSET.equals(str)) {
                            CachedAppOptimizer.this.updateFreezerBinderState();
                        }
                    }
                }
                if (CachedAppOptimizer.this.mTestCallback != null) {
                    CachedAppOptimizer.this.mTestCallback.onPropertyChanged();
                }
            }
        };
        this.mPhenotypeFlagLock = new Object();
        this.mCompactThrottleSomeSome = DEFAULT_COMPACT_THROTTLE_1;
        this.mCompactThrottleSomeFull = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.mCompactThrottleFullSome = DEFAULT_COMPACT_THROTTLE_3;
        this.mCompactThrottleFullFull = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.mCompactThrottleMinOomAdj = DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ;
        this.mCompactThrottleMaxOomAdj = DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ;
        this.mUseCompaction = false;
        this.mUseFreezer = false;
        this.mFreezerDisableCount = 1;
        this.mRandom = new Random();
        this.mCompactStatsdSampleRate = DEFAULT_STATSD_SAMPLE_RATE;
        this.mFreezerStatsdSampleRate = DEFAULT_STATSD_SAMPLE_RATE;
        this.mFullAnonRssThrottleKb = DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB;
        this.mFullDeltaRssThrottleKb = DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB;
        this.mFreezerBinderEnabled = true;
        this.mFreezerBinderDivisor = DEFAULT_FREEZER_BINDER_DIVISOR;
        this.mFreezerBinderOffset = 500;
        this.mFreezerBinderThreshold = DEFAULT_FREEZER_BINDER_THRESHOLD;
        this.mFreezerOverride = false;
        this.mFreezerDebounceTimeout = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.mFreezerExemptInstPkg = true;
        this.mLastCompactionStats = new LinkedHashMap<Integer, SingleCompactionStats>() { // from class: com.android.server.am.CachedAppOptimizer.3
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<Integer, SingleCompactionStats> entry) {
                return size() > 256;
            }
        };
        this.mCompactionStatsHistory = new LinkedList<SingleCompactionStats>() { // from class: com.android.server.am.CachedAppOptimizer.4
            @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
            public boolean add(SingleCompactionStats singleCompactionStats) {
                if (size() >= 20) {
                    remove();
                }
                return super.add((AnonymousClass4) singleCompactionStats);
            }
        };
        this.mPerProcessCompactStats = new LinkedHashMap<>(256);
        this.mPerSourceCompactStats = new EnumMap<>(CompactSource.class);
        this.mTotalCompactionsCancelled = new EnumMap<>(CancelCompactReason.class);
        this.mWrapper = new CachedAppOptimizerWrapper();
        this.mAm = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        this.mCachedAppOptimizerThread = new ServiceThread("CachedAppOptimizerThread", 2, true);
        this.mProcStateThrottle = new HashSet();
        this.mProcessDependencies = processDependencies;
        this.mTestCallback = propertyChangedCallbackForTest;
        this.mSettingsObserver = new SettingsContentObserver();
        this.mProcLocksReader = new ProcLocksReader();
    }

    public void init() {
        DeviceConfig.addOnPropertiesChangedListener("activity_manager", ActivityThread.currentApplication().getMainExecutor(), this.mOnFlagsChangedListener);
        DeviceConfig.addOnPropertiesChangedListener("activity_manager_native_boot", ActivityThread.currentApplication().getMainExecutor(), this.mOnNativeBootFlagsChangedListener);
        this.mAm.mContext.getContentResolver().registerContentObserver(CACHED_APP_FREEZER_ENABLED_URI, false, this.mSettingsObserver);
        synchronized (this.mPhenotypeFlagLock) {
            updateUseCompaction();
            updateCompactionThrottles();
            updateCompactStatsdSampleRate();
            updateFreezerStatsdSampleRate();
            updateFullRssThrottle();
            updateFullDeltaRssThrottle();
            updateProcStateThrottle();
            updateUseFreezer();
            updateMinOomAdjThrottle();
            updateMaxOomAdjThrottle();
        }
    }

    public boolean useCompaction() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseCompaction;
        }
        return z;
    }

    public boolean useFreezer() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseFreezer;
        }
        return z;
    }

    public boolean freezerExemptInstPkg() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseFreezer && this.mFreezerExemptInstPkg;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void dump(PrintWriter printWriter) {
        printWriter.println("CachedAppOptimizer settings");
        synchronized (this.mPhenotypeFlagLock) {
            printWriter.println("  use_compaction=" + this.mUseCompaction);
            printWriter.println("  compact_throttle_1=" + this.mCompactThrottleSomeSome);
            printWriter.println("  compact_throttle_2=" + this.mCompactThrottleSomeFull);
            printWriter.println("  compact_throttle_3=" + this.mCompactThrottleFullSome);
            printWriter.println("  compact_throttle_4=" + this.mCompactThrottleFullFull);
            printWriter.println("  compact_throttle_min_oom_adj=" + this.mCompactThrottleMinOomAdj);
            printWriter.println("  compact_throttle_max_oom_adj=" + this.mCompactThrottleMaxOomAdj);
            printWriter.println("  compact_statsd_sample_rate=" + this.mCompactStatsdSampleRate);
            printWriter.println("  compact_full_rss_throttle_kb=" + this.mFullAnonRssThrottleKb);
            printWriter.println("  compact_full_delta_rss_throttle_kb=" + this.mFullDeltaRssThrottleKb);
            StringBuilder sb = new StringBuilder();
            sb.append("  compact_proc_state_throttle=");
            sb.append(Arrays.toString(this.mProcStateThrottle.toArray(new Integer[0])));
            printWriter.println(sb.toString());
            printWriter.println(" Per-Process Compaction Stats");
            long j = 0;
            long j2 = 0;
            for (AggregatedProcessCompactionStats aggregatedProcessCompactionStats : this.mPerProcessCompactStats.values()) {
                printWriter.println("-----" + aggregatedProcessCompactionStats.processName + "-----");
                j += aggregatedProcessCompactionStats.mSomeCompactPerformed;
                j2 += aggregatedProcessCompactionStats.mFullCompactPerformed;
                aggregatedProcessCompactionStats.dump(printWriter);
                printWriter.println();
            }
            printWriter.println();
            printWriter.println(" Per-Source Compaction Stats");
            for (AggregatedSourceCompactionStats aggregatedSourceCompactionStats : this.mPerSourceCompactStats.values()) {
                printWriter.println("-----" + aggregatedSourceCompactionStats.sourceType + "-----");
                aggregatedSourceCompactionStats.dump(printWriter);
                printWriter.println();
            }
            printWriter.println();
            printWriter.println("Total Compactions Performed by profile: " + j + " some, " + j2 + " full");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Total compactions downgraded: ");
            sb2.append(this.mTotalCompactionDowngrades);
            printWriter.println(sb2.toString());
            printWriter.println("Total compactions cancelled by reason: ");
            for (CancelCompactReason cancelCompactReason : this.mTotalCompactionsCancelled.keySet()) {
                printWriter.println("    " + cancelCompactReason + ": " + this.mTotalCompactionsCancelled.get(cancelCompactReason));
            }
            printWriter.println();
            printWriter.println(" System Compaction Memory Stats");
            printWriter.println("    Compactions Performed: " + this.mSystemCompactionsPerformed);
            printWriter.println("    Total Memory Freed (KB): " + this.mSystemTotalMemFreed);
            printWriter.println("    Avg Mem Freed per Compact (KB): " + (this.mSystemCompactionsPerformed > 0 ? this.mSystemTotalMemFreed / r1 : 0.0d));
            printWriter.println();
            printWriter.println("  Tracking last compaction stats for " + this.mLastCompactionStats.size() + " processes.");
            printWriter.println("Last Compaction per process stats:");
            printWriter.println("    (ProcessName,Source,DeltaAnonRssKBs,ZramConsumedKBs,AnonMemFreedKBs,CompactEfficiency,CompactCost(ms/MB),procState,oomAdj,oomAdjReason)");
            Iterator<Map.Entry<Integer, SingleCompactionStats>> it = this.mLastCompactionStats.entrySet().iterator();
            while (it.hasNext()) {
                it.next().getValue().dump(printWriter);
            }
            printWriter.println();
            printWriter.println("Last 20 Compactions Stats:");
            printWriter.println("    (ProcessName,Source,DeltaAnonRssKBs,ZramConsumedKBs,AnonMemFreedKBs,CompactEfficiency,CompactCost(ms/MB),procState,oomAdj,oomAdjReason)");
            Iterator<SingleCompactionStats> it2 = this.mCompactionStatsHistory.iterator();
            while (it2.hasNext()) {
                it2.next().dump(printWriter);
            }
            printWriter.println();
            printWriter.println("  use_freezer=" + this.mUseFreezer);
            printWriter.println("  freeze_statsd_sample_rate=" + this.mFreezerStatsdSampleRate);
            printWriter.println("  freeze_debounce_timeout=" + this.mFreezerDebounceTimeout);
            printWriter.println("  freeze_exempt_inst_pkg=" + this.mFreezerExemptInstPkg);
            printWriter.println("  freeze_binder_enabled=" + this.mFreezerBinderEnabled);
            printWriter.println("  freeze_binder_threshold=" + this.mFreezerBinderThreshold);
            printWriter.println("  freeze_binder_divisor=" + this.mFreezerBinderDivisor);
            printWriter.println("  freeze_binder_offset=" + this.mFreezerBinderOffset);
            ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    int size = this.mFrozenProcesses.size();
                    printWriter.println("  Apps frozen: " + size);
                    for (int i = 0; i < size; i++) {
                        ProcessRecord valueAt = this.mFrozenProcesses.valueAt(i);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("    ");
                        sb3.append(valueAt.mOptRecord.getFreezeUnfreezeTime());
                        sb3.append(": ");
                        sb3.append(valueAt.getPid());
                        sb3.append(" ");
                        sb3.append(valueAt.processName);
                        sb3.append(valueAt.mOptRecord.isFreezeSticky() ? " (sticky)" : "");
                        printWriter.println(sb3.toString());
                    }
                    if (!this.mPendingCompactionProcesses.isEmpty()) {
                        printWriter.println("  Pending compactions:");
                        int size2 = this.mPendingCompactionProcesses.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            ProcessRecord processRecord = this.mPendingCompactionProcesses.get(i2);
                            printWriter.println("    pid: " + processRecord.getPid() + ". name: " + processRecord.processName + ". hasPendingCompact: " + processRecord.mOptRecord.hasPendingCompact());
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

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean compactApp(ProcessRecord processRecord, CompactProfile compactProfile, CompactSource compactSource, boolean z) {
        processRecord.mOptRecord.setReqCompactSource(compactSource);
        processRecord.mOptRecord.setReqCompactProfile(compactProfile);
        AggregatedSourceCompactionStats perSourceAggregatedCompactStat = getPerSourceAggregatedCompactStat(compactSource);
        AggregatedProcessCompactionStats perProcessAggregatedCompactStat = getPerProcessAggregatedCompactStat(processRecord.processName);
        int i = AnonymousClass5.$SwitchMap$com$android$server$am$CachedAppOptimizer$CompactProfile[compactProfile.ordinal()];
        if (i == 1) {
            perProcessAggregatedCompactStat.mSomeCompactRequested++;
            perSourceAggregatedCompactStat.mSomeCompactRequested++;
        } else if (i == 2) {
            perProcessAggregatedCompactStat.mFullCompactRequested++;
            perSourceAggregatedCompactStat.mFullCompactRequested++;
        } else {
            Slog.e(IActivityManagerServiceExt.TAG, "Unimplemented compaction type, consider adding it.");
            return false;
        }
        if (!processRecord.mOptRecord.hasPendingCompact()) {
            String str = processRecord.processName;
            if (str == null) {
                str = "";
            }
            if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                Slog.d(IActivityManagerServiceExt.TAG, "compactApp " + processRecord.mOptRecord.getReqCompactSource().name() + " " + processRecord.mOptRecord.getReqCompactProfile().name() + " " + str);
            }
            processRecord.mOptRecord.setHasPendingCompact(true);
            processRecord.mOptRecord.setForceCompact(z);
            this.mPendingCompactionProcesses.add(processRecord);
            Handler handler = this.mCompactionHandler;
            handler.sendMessage(handler.obtainMessage(1, processRecord.mState.getCurAdj(), processRecord.mState.getSetProcState()));
            return true;
        }
        if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
            Slog.d(IActivityManagerServiceExt.TAG, " compactApp Skipped for " + processRecord.processName + " pendingCompact= " + processRecord.mOptRecord.hasPendingCompact() + ". Requested compact profile: " + processRecord.mOptRecord.getReqCompactProfile().name() + ". Compact source " + processRecord.mOptRecord.getReqCompactSource().name());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.am.CachedAppOptimizer$5, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$am$CachedAppOptimizer$CompactProfile;

        static {
            int[] iArr = new int[CompactProfile.values().length];
            $SwitchMap$com$android$server$am$CachedAppOptimizer$CompactProfile = iArr;
            try {
                iArr[CompactProfile.SOME.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$server$am$CachedAppOptimizer$CompactProfile[CompactProfile.FULL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void compactNative(CompactProfile compactProfile, int i) {
        Handler handler = this.mCompactionHandler;
        handler.sendMessage(handler.obtainMessage(5, i, compactProfile.ordinal()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AggregatedProcessCompactionStats getPerProcessAggregatedCompactStat(String str) {
        if (str == null) {
            str = "";
        }
        AggregatedProcessCompactionStats aggregatedProcessCompactionStats = this.mPerProcessCompactStats.get(str);
        if (aggregatedProcessCompactionStats != null) {
            return aggregatedProcessCompactionStats;
        }
        AggregatedProcessCompactionStats aggregatedProcessCompactionStats2 = new AggregatedProcessCompactionStats(str);
        this.mPerProcessCompactStats.put(str, aggregatedProcessCompactionStats2);
        return aggregatedProcessCompactionStats2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AggregatedSourceCompactionStats getPerSourceAggregatedCompactStat(CompactSource compactSource) {
        AggregatedSourceCompactionStats aggregatedSourceCompactionStats = this.mPerSourceCompactStats.get(compactSource);
        if (aggregatedSourceCompactionStats != null) {
            return aggregatedSourceCompactionStats;
        }
        AggregatedSourceCompactionStats aggregatedSourceCompactionStats2 = new AggregatedSourceCompactionStats(compactSource);
        this.mPerSourceCompactStats.put((EnumMap<CompactSource, AggregatedSourceCompactionStats>) compactSource, (CompactSource) aggregatedSourceCompactionStats2);
        return aggregatedSourceCompactionStats2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void compactAllSystem() {
        if (useCompaction()) {
            if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                Slog.d(IActivityManagerServiceExt.TAG, "compactAllSystem");
            }
            Trace.instantForTrack(64L, ATRACE_COMPACTION_TRACK, "compactAllSystem");
            Handler handler = this.mCompactionHandler;
            handler.sendMessage(handler.obtainMessage(2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateUseCompaction() {
        this.mUseCompaction = DeviceConfig.getBoolean("activity_manager", KEY_USE_COMPACTION, false);
        if (this.mUseCompaction && this.mCompactionHandler == null) {
            if (!this.mCachedAppOptimizerThread.isAlive()) {
                this.mCachedAppOptimizerThread.start();
            }
            this.mCompactionHandler = new MemCompactionHandler();
        }
        Process.setThreadGroupAndCpuset(this.mCachedAppOptimizerThread.getThreadId(), 2);
    }

    public synchronized boolean enableFreezer(final boolean z) {
        if (!this.mUseFreezer) {
            return false;
        }
        if (z) {
            int i = this.mFreezerDisableCount - 1;
            this.mFreezerDisableCount = i;
            if (i > 0) {
                return true;
            }
            if (i < 0) {
                Slog.e(IActivityManagerServiceExt.TAG, "unbalanced call to enableFreezer, ignoring");
                this.mFreezerDisableCount = 0;
                return false;
            }
        } else {
            int i2 = this.mFreezerDisableCount + 1;
            this.mFreezerDisableCount = i2;
            if (i2 > 1) {
                return true;
            }
        }
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        this.mFreezerOverride = z ? false : true;
                        Slog.d(IActivityManagerServiceExt.TAG, "freezer override set to " + this.mFreezerOverride);
                        this.mAm.mProcessList.forEachLruProcessesLOSP(true, new Consumer() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda1
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                CachedAppOptimizer.this.lambda$enableFreezer$0(z, (ProcessRecord) obj);
                            }
                        });
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
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableFreezer$0(boolean z, ProcessRecord processRecord) {
        if (processRecord == null) {
            return;
        }
        ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
        if (z && processCachedOptimizerRecord.hasFreezerOverride()) {
            freezeAppAsyncLSP(processRecord);
            processCachedOptimizerRecord.setFreezerOverride(false);
        }
        if (z || !processCachedOptimizerRecord.isFrozen()) {
            return;
        }
        unfreezeAppLSP(processRecord, 19);
        processCachedOptimizerRecord.setFreezerOverride(true);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean isFreezerSupported() {
        FileReader fileReader;
        Exception e;
        char read;
        boolean z = false;
        FileReader fileReader2 = null;
        try {
            String freezerCheckPath = getFreezerCheckPath();
            Slog.d(IActivityManagerServiceExt.TAG, "Checking cgroup freezer: " + freezerCheckPath);
            fileReader = new FileReader(freezerCheckPath);
        } catch (FileNotFoundException unused) {
        } catch (RuntimeException unused2) {
        } catch (Exception e2) {
            fileReader = null;
            e = e2;
        }
        try {
            read = (char) fileReader.read();
        } catch (FileNotFoundException unused3) {
            fileReader2 = fileReader;
            Slog.w(IActivityManagerServiceExt.TAG, "File cgroup.freeze not present");
            fileReader = fileReader2;
            if (fileReader != null) {
            }
            Slog.d(IActivityManagerServiceExt.TAG, "Freezer supported: " + z);
            return z;
        } catch (RuntimeException unused4) {
            fileReader2 = fileReader;
            Slog.w(IActivityManagerServiceExt.TAG, "Unable to read freezer info");
            fileReader = fileReader2;
            if (fileReader != null) {
            }
            Slog.d(IActivityManagerServiceExt.TAG, "Freezer supported: " + z);
            return z;
        } catch (Exception e3) {
            e = e3;
            Slog.w(IActivityManagerServiceExt.TAG, "Unable to read cgroup.freeze: " + e.toString());
            if (fileReader != null) {
            }
            Slog.d(IActivityManagerServiceExt.TAG, "Freezer supported: " + z);
            return z;
        }
        if (read != '1' && read != '0') {
            Slog.e(IActivityManagerServiceExt.TAG, "Unexpected value in cgroup.freeze");
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e4) {
                    Slog.e(IActivityManagerServiceExt.TAG, "Exception closing cgroup.freeze: " + e4.toString());
                }
            }
            Slog.d(IActivityManagerServiceExt.TAG, "Freezer supported: " + z);
            return z;
        }
        Slog.d(IActivityManagerServiceExt.TAG, "Checking binder freezer ioctl");
        getBinderFreezeInfo(Process.myPid());
        Slog.d(IActivityManagerServiceExt.TAG, "Checking freezer profiles");
        z = isFreezerProfileValid();
        if (fileReader != null) {
        }
        Slog.d(IActivityManagerServiceExt.TAG, "Freezer supported: " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateUseFreezer() {
        String string = Settings.Global.getString(this.mAm.mContext.getContentResolver(), "cached_apps_freezer");
        if ("disabled".equals(string)) {
            this.mUseFreezer = false;
        } else if (("enabled".equals(string) || DeviceConfig.getBoolean("activity_manager_native_boot", KEY_USE_FREEZER, true)) && this.mWrapper.isExtUseFreezeEnable()) {
            this.mUseFreezer = isFreezerSupported();
            updateFreezerDebounceTimeout();
            updateFreezerExemptInstPkg();
        } else {
            this.mUseFreezer = false;
        }
        final boolean z = this.mUseFreezer;
        this.mAm.mHandler.post(new Runnable() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CachedAppOptimizer.this.lambda$updateUseFreezer$1(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUseFreezer$1(boolean z) {
        if (z) {
            Slog.d(IActivityManagerServiceExt.TAG, "Freezer enabled");
            enableFreezer(true);
            if (!this.mCachedAppOptimizerThread.isAlive()) {
                this.mCachedAppOptimizerThread.start();
            }
            if (this.mFreezeHandler == null) {
                this.mFreezeHandler = new FreezeHandler();
            }
            Process.setThreadGroupAndCpuset(this.mCachedAppOptimizerThread.getThreadId(), 2);
            return;
        }
        Slog.d(IActivityManagerServiceExt.TAG, "Freezer disabled");
        enableFreezer(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateCompactionThrottles() {
        String property = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_1);
        String property2 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_2);
        String property3 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_3);
        String property4 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_4);
        String property5 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_5);
        String property6 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_6);
        String property7 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_MIN_OOM_ADJ);
        String property8 = DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_MAX_OOM_ADJ);
        boolean z = true;
        if (!TextUtils.isEmpty(property) && !TextUtils.isEmpty(property2) && !TextUtils.isEmpty(property3) && !TextUtils.isEmpty(property4) && !TextUtils.isEmpty(property5) && !TextUtils.isEmpty(property6) && !TextUtils.isEmpty(property7) && !TextUtils.isEmpty(property8)) {
            try {
                this.mCompactThrottleSomeSome = Integer.parseInt(property);
                this.mCompactThrottleSomeFull = Integer.parseInt(property2);
                this.mCompactThrottleFullSome = Integer.parseInt(property3);
                this.mCompactThrottleFullFull = Integer.parseInt(property4);
                this.mCompactThrottleMinOomAdj = Long.parseLong(property7);
                this.mCompactThrottleMaxOomAdj = Long.parseLong(property8);
                z = false;
            } catch (NumberFormatException unused) {
            }
        }
        if (z) {
            this.mCompactThrottleSomeSome = DEFAULT_COMPACT_THROTTLE_1;
            this.mCompactThrottleSomeFull = IDeviceIdleControllerExt.ADVANCE_TIME;
            this.mCompactThrottleFullSome = DEFAULT_COMPACT_THROTTLE_3;
            this.mCompactThrottleFullFull = IDeviceIdleControllerExt.ADVANCE_TIME;
            this.mCompactThrottleMinOomAdj = DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ;
            this.mCompactThrottleMaxOomAdj = DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateCompactStatsdSampleRate() {
        this.mCompactStatsdSampleRate = DeviceConfig.getFloat("activity_manager", KEY_COMPACT_STATSD_SAMPLE_RATE, DEFAULT_STATSD_SAMPLE_RATE);
        this.mCompactStatsdSampleRate = Math.min(1.0f, Math.max(0.0f, this.mCompactStatsdSampleRate));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateFreezerStatsdSampleRate() {
        this.mFreezerStatsdSampleRate = DeviceConfig.getFloat("activity_manager", KEY_FREEZER_STATSD_SAMPLE_RATE, DEFAULT_STATSD_SAMPLE_RATE);
        this.mFreezerStatsdSampleRate = Math.min(1.0f, Math.max(0.0f, this.mFreezerStatsdSampleRate));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateFullRssThrottle() {
        this.mFullAnonRssThrottleKb = DeviceConfig.getLong("activity_manager", KEY_COMPACT_FULL_RSS_THROTTLE_KB, DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB);
        if (this.mFullAnonRssThrottleKb < 0) {
            this.mFullAnonRssThrottleKb = DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateFullDeltaRssThrottle() {
        this.mFullDeltaRssThrottleKb = DeviceConfig.getLong("activity_manager", KEY_COMPACT_FULL_DELTA_RSS_THROTTLE_KB, DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB);
        if (this.mFullDeltaRssThrottleKb < 0) {
            this.mFullDeltaRssThrottleKb = DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateProcStateThrottle() {
        String str = DEFAULT_COMPACT_PROC_STATE_THROTTLE;
        String string = DeviceConfig.getString("activity_manager", KEY_COMPACT_PROC_STATE_THROTTLE, str);
        if (parseProcStateThrottle(string)) {
            return;
        }
        Slog.w(IActivityManagerServiceExt.TAG, "Unable to parse app compact proc state throttle \"" + string + "\" falling back to default.");
        if (parseProcStateThrottle(str)) {
            return;
        }
        Slog.wtf(IActivityManagerServiceExt.TAG, "Unable to parse default app compact proc state throttle " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateMinOomAdjThrottle() {
        this.mCompactThrottleMinOomAdj = DeviceConfig.getLong("activity_manager", KEY_COMPACT_THROTTLE_MIN_OOM_ADJ, DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ);
        if (this.mCompactThrottleMinOomAdj < DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ) {
            this.mCompactThrottleMinOomAdj = DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateMaxOomAdjThrottle() {
        this.mCompactThrottleMaxOomAdj = DeviceConfig.getLong("activity_manager", KEY_COMPACT_THROTTLE_MAX_OOM_ADJ, DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ);
        if (this.mCompactThrottleMaxOomAdj > DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ) {
            this.mCompactThrottleMaxOomAdj = DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateFreezerDebounceTimeout() {
        this.mFreezerDebounceTimeout = DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_DEBOUNCE_TIMEOUT, IDeviceIdleControllerExt.ADVANCE_TIME);
        if (this.mFreezerDebounceTimeout < 0) {
            this.mFreezerDebounceTimeout = IDeviceIdleControllerExt.ADVANCE_TIME;
        }
        Slog.d(IActivityManagerServiceExt.TAG, "Freezer timeout set to " + this.mFreezerDebounceTimeout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateFreezerExemptInstPkg() {
        this.mFreezerExemptInstPkg = DeviceConfig.getBoolean("activity_manager_native_boot", KEY_FREEZER_EXEMPT_INST_PKG, true);
        Slog.d(IActivityManagerServiceExt.TAG, "Freezer exemption set to " + this.mFreezerExemptInstPkg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPhenotypeFlagLock"})
    public void updateFreezerBinderState() {
        this.mFreezerBinderEnabled = DeviceConfig.getBoolean("activity_manager_native_boot", KEY_FREEZER_BINDER_ENABLED, true);
        this.mFreezerBinderDivisor = DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_BINDER_DIVISOR, DEFAULT_FREEZER_BINDER_DIVISOR);
        this.mFreezerBinderOffset = DeviceConfig.getInt("activity_manager_native_boot", KEY_FREEZER_BINDER_OFFSET, 500);
        this.mFreezerBinderThreshold = DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_BINDER_THRESHOLD, DEFAULT_FREEZER_BINDER_THRESHOLD);
        Slog.d(IActivityManagerServiceExt.TAG, "Freezer binder state set to enabled=" + this.mFreezerBinderEnabled + ", divisor=" + this.mFreezerBinderDivisor + ", offset=" + this.mFreezerBinderOffset + ", threshold=" + this.mFreezerBinderThreshold);
    }

    private boolean parseProcStateThrottle(String str) {
        String[] split = TextUtils.split(str, ",");
        this.mProcStateThrottle.clear();
        for (String str2 : split) {
            try {
                this.mProcStateThrottle.add(Integer.valueOf(Integer.parseInt(str2)));
            } catch (NumberFormatException unused) {
                Slog.e(IActivityManagerServiceExt.TAG, "Failed to parse default app compaction proc state: " + str2);
                return false;
            }
        }
        return true;
    }

    @GuardedBy({"mProcLock"})
    private long updateEarliestFreezableTime(ProcessRecord processRecord, long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
        processCachedOptimizerRecord.setEarliestFreezableTime(Math.max(processCachedOptimizerRecord.getEarliestFreezableTime(), j + uptimeMillis));
        return processRecord.mOptRecord.getEarliestFreezableTime() - uptimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public void unfreezeTemporarily(ProcessRecord processRecord, int i) {
        unfreezeTemporarily(processRecord, i, this.mFreezerDebounceTimeout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public void unfreezeTemporarily(ProcessRecord processRecord, int i, long j) {
        if (this.mUseFreezer) {
            ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    long updateEarliestFreezableTime = updateEarliestFreezableTime(processRecord, j);
                    if (processRecord.mOptRecord.isFrozen() || processRecord.mOptRecord.isPendingFreeze()) {
                        unfreezeAppLSP(processRecord, i);
                        freezeAppAsyncLSP(processRecord, updateEarliestFreezableTime);
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm", "mProcLock"})
    public void freezeAppAsyncLSP(ProcessRecord processRecord) {
        freezeAppAsyncLSP(processRecord, updateEarliestFreezableTime(processRecord, this.mFreezerDebounceTimeout));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mAm", "mProcLock"})
    public void freezeAppAsyncLSP(ProcessRecord processRecord, long j) {
        freezeAppAsyncInternalLSP(processRecord, j, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm", "mProcLock"})
    public void freezeAppAsyncInternalLSP(ProcessRecord processRecord, long j, boolean z) {
        IApplicationThread thread;
        ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
        if (processCachedOptimizerRecord.isPendingFreeze()) {
            return;
        }
        if (processCachedOptimizerRecord.isFreezeSticky() && !z) {
            if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                Slog.d(IActivityManagerServiceExt.TAG, "Skip freezing because unfrozen state is sticky pid=" + processRecord.getPid() + " " + processRecord.processName);
                return;
            }
            return;
        }
        if (this.mAm.mConstants.USE_MODERN_TRIM && processRecord.mState.getSetAdj() >= 900 && (thread = processRecord.getThread()) != null) {
            try {
                thread.scheduleTrimMemory(40);
            } catch (RemoteException unused) {
            }
        }
        reportProcessFreezableChangedLocked(processRecord);
        ICachedAppOptimizerExt iCachedAppOptimizerExt = this.mCachedAppOptimizerExt;
        if (iCachedAppOptimizerExt == null || iCachedAppOptimizerExt.checkFreezeProc(processRecord)) {
            processRecord.mOptRecord.setLastUsedTimeout(j);
            Handler handler = this.mFreezeHandler;
            handler.sendMessageDelayed(handler.obtainMessage(3, 1, 0, processRecord), j);
            processCachedOptimizerRecord.setPendingFreeze(true);
            if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                Slog.d(IActivityManagerServiceExt.TAG, "Async freezing " + processRecord.getPid() + " " + processRecord.processName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm", "mProcLock", "mFreezerLock"})
    public void unfreezeAppInternalLSP(ProcessRecord processRecord, int i, boolean z) {
        boolean z2;
        int pid = processRecord.getPid();
        ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
        if (processCachedOptimizerRecord.isFreezeSticky() && !z) {
            if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                Slog.d(IActivityManagerServiceExt.TAG, "Skip unfreezing because frozen state is sticky pid=" + pid + " " + processRecord.processName);
                return;
            }
            return;
        }
        ICachedAppOptimizerExt iCachedAppOptimizerExt = this.mCachedAppOptimizerExt;
        if (iCachedAppOptimizerExt != null && !iCachedAppOptimizerExt.checkUnfreezeProc(processRecord)) {
            reportProcessFreezableChangedLocked(processRecord);
            return;
        }
        if (processCachedOptimizerRecord.isPendingFreeze()) {
            this.mFreezeHandler.removeMessages(3, processRecord);
            processCachedOptimizerRecord.setPendingFreeze(false);
            if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                Slog.d(IActivityManagerServiceExt.TAG, "Cancel freezing " + pid + " " + processRecord.processName);
            }
        }
        UidRecord uidRecord = processRecord.getUidRecord();
        if (uidRecord != null && uidRecord.isFrozen()) {
            uidRecord.setFrozen(false);
            postUidFrozenMessage(uidRecord.getUid(), false);
        }
        reportProcessFreezableChangedLocked(processRecord);
        processCachedOptimizerRecord.setFreezerOverride(false);
        if (pid == 0 || !processCachedOptimizerRecord.isFrozen()) {
            return;
        }
        try {
            int binderFreezeInfo = getBinderFreezeInfo(pid);
            if ((binderFreezeInfo & 1) != 0) {
                Slog.d(IActivityManagerServiceExt.TAG, "pid " + pid + " " + processRecord.processName + " received sync transactions while frozen, killing");
                processRecord.killLocked("Sync transaction while in frozen state", 14, 20, true);
                z2 = true;
            } else {
                z2 = false;
            }
            if ((binderFreezeInfo & 2) != 0 && ActivityManagerDebugConfig.DEBUG_FREEZER) {
                Slog.d(IActivityManagerServiceExt.TAG, "pid " + pid + " " + processRecord.processName + " received async transactions while frozen");
            }
        } catch (Exception e) {
            Slog.d(IActivityManagerServiceExt.TAG, "Unable to query binder frozen info for pid " + pid + " " + processRecord.processName + ". Killing it. Exception: " + e);
            processRecord.killLocked("Unable to query binder frozen stats", 14, 19, true);
            z2 = true;
        }
        if (z2) {
            return;
        }
        long freezeUnfreezeTime = processCachedOptimizerRecord.getFreezeUnfreezeTime();
        try {
            freezeBinder(pid, false, 100);
            try {
                traceAppFreeze(processRecord.processName, pid, i);
                Process.setProcessFrozen(pid, processRecord.uid, false);
                processCachedOptimizerRecord.setFreezeUnfreezeTime(SystemClock.uptimeMillis());
                processCachedOptimizerRecord.setFrozen(false);
                this.mFrozenProcesses.delete(pid);
            } catch (Exception unused) {
                Slog.e(IActivityManagerServiceExt.TAG, "Unable to unfreeze " + pid + " " + processRecord.processName + ". This might cause inconsistency or UI hangs.");
            }
            if (processCachedOptimizerRecord.isFrozen()) {
                return;
            }
            Slog.d(IActivityManagerServiceExt.TAG, "sync unfroze " + pid + " " + processRecord.processName + " for " + i);
            Handler handler = this.mFreezeHandler;
            handler.sendMessage(handler.obtainMessage(4, pid, (int) Math.min(processCachedOptimizerRecord.getFreezeUnfreezeTime() - freezeUnfreezeTime, 2147483647L), new Pair(processRecord.processName, Integer.valueOf(i))));
        } catch (RuntimeException unused2) {
            Slog.e(IActivityManagerServiceExt.TAG, "Unable to unfreeze binder for " + pid + " " + processRecord.processName + ". Killing it");
            processRecord.killLocked("Unable to unfreeze", 14, 19, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm", "mProcLock"})
    public void unfreezeAppLSP(ProcessRecord processRecord, int i) {
        synchronized (this.mFreezerLock) {
            unfreezeAppInternalLSP(processRecord, i, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unfreezeProcess(int i, int i2) {
        synchronized (this.mFreezerLock) {
            ProcessRecord processRecord = this.mFrozenProcesses.get(i);
            if (processRecord == null) {
                return;
            }
            Slog.d(IActivityManagerServiceExt.TAG, "quick sync unfreeze " + i + " for " + i2);
            try {
                freezeBinder(i, false, 100);
                try {
                    traceAppFreeze(processRecord.processName, i, i2);
                    Process.setProcessFrozen(i, processRecord.uid, false);
                } catch (Exception unused) {
                    Slog.e(IActivityManagerServiceExt.TAG, "Unable to quick unfreeze " + i);
                }
            } catch (RuntimeException unused2) {
                Slog.e(IActivityManagerServiceExt.TAG, "Unable to quick unfreeze binder for " + i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void traceAppFreeze(String str, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(i2 < 0 ? "Freeze " : "Unfreeze ");
        sb.append(str);
        sb.append(":");
        sb.append(i);
        sb.append(" ");
        sb.append(i2);
        Trace.instantForTrack(64L, ATRACE_FREEZER_TRACK, sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm", "mProcLock"})
    public void onCleanupApplicationRecordLocked(ProcessRecord processRecord) {
        if (this.mUseFreezer) {
            ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
            boolean z = false;
            if (processCachedOptimizerRecord.isPendingFreeze()) {
                this.mFreezeHandler.removeMessages(3, processRecord);
                processCachedOptimizerRecord.setPendingFreeze(false);
            }
            UidRecord uidRecord = processRecord.getUidRecord();
            if (uidRecord != null) {
                if (uidRecord.getNumOfProcs() > 1 && uidRecord.areAllProcessesFrozen(processRecord)) {
                    z = true;
                }
                if (z != uidRecord.isFrozen()) {
                    uidRecord.setFrozen(z);
                    postUidFrozenMessage(uidRecord.getUid(), z);
                }
            }
            this.mFrozenProcesses.delete(processRecord.getPid());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWakefulnessChanged(int i) {
        if (i == 1) {
            Slog.e(IActivityManagerServiceExt.TAG, "Cancel pending or running compactions as system is awake");
            cancelAllCompactions(CancelCompactReason.SCREEN_ON);
        }
    }

    void cancelAllCompactions(CancelCompactReason cancelCompactReason) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            while (!this.mPendingCompactionProcesses.isEmpty()) {
                try {
                    cancelCompactionForProcess(this.mPendingCompactionProcesses.get(0), cancelCompactReason);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            this.mPendingCompactionProcesses.clear();
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    @GuardedBy({"mProcLock"})
    void cancelCompactionForProcess(ProcessRecord processRecord, CancelCompactReason cancelCompactReason) {
        String str;
        boolean z = false;
        if (this.mPendingCompactionProcesses.contains(processRecord)) {
            processRecord.mOptRecord.setHasPendingCompact(false);
            this.mPendingCompactionProcesses.remove(processRecord);
            z = true;
        }
        if (DefaultProcessDependencies.mPidCompacting == processRecord.mPid) {
            cancelCompaction();
            z = true;
        }
        if (z) {
            if (this.mTotalCompactionsCancelled.containsKey(cancelCompactReason)) {
                this.mTotalCompactionsCancelled.put((EnumMap<CancelCompactReason, Integer>) cancelCompactReason, (CancelCompactReason) Integer.valueOf(this.mTotalCompactionsCancelled.get(cancelCompactReason).intValue() + 1));
            } else {
                this.mTotalCompactionsCancelled.put((EnumMap<CancelCompactReason, Integer>) cancelCompactReason, (CancelCompactReason) 1);
            }
            if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                if (("Cancelled pending or running compactions for process: " + processRecord.processName) != null) {
                    str = processRecord.processName;
                } else {
                    str = " reason: " + cancelCompactReason.name();
                }
                Slog.d(IActivityManagerServiceExt.TAG, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService", "mProcLock"})
    public void onOomAdjustChanged(int i, int i2, ProcessRecord processRecord) {
        if (!useCompaction() || i2 >= i || i2 >= 900) {
            return;
        }
        cancelCompactionForProcess(processRecord, CancelCompactReason.OOM_IMPROVEMENT);
    }

    void onProcessFrozen(ProcessRecord processRecord) {
        if (useCompaction()) {
            ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    compactApp(processRecord, CompactProfile.FULL, CompactSource.APP, false);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    CompactProfile resolveCompactionProfile(CompactProfile compactProfile) {
        CompactProfile compactProfile2 = CompactProfile.FULL;
        if (compactProfile == compactProfile2) {
            double freeSwapPercent = getFreeSwapPercent();
            if (freeSwapPercent < COMPACT_DOWNGRADE_FREE_SWAP_THRESHOLD) {
                compactProfile = CompactProfile.SOME;
                this.mTotalCompactionDowngrades++;
                if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    Slog.d(IActivityManagerServiceExt.TAG, "Downgraded compaction to " + compactProfile + " due to low swap. Swap Free% " + freeSwapPercent);
                }
            }
        }
        if (compactProfile == CompactProfile.SOME) {
            compactProfile = CompactProfile.NONE;
        } else if (compactProfile == compactProfile2) {
            compactProfile = CompactProfile.ANON;
        }
        if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
            Slog.d(IActivityManagerServiceExt.TAG, "Final compaction profile " + compactProfile + " due to file compact disabled");
        }
        return compactProfile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isProcessFrozen(int i) {
        boolean contains;
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                contains = this.mFrozenProcesses.contains(i);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        return contains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SingleCompactionStats {
        private static final float STATSD_SAMPLE_RATE = 0.1f;
        private static final Random mRandom = new Random();
        public long mAnonMemFreedKBs;
        public float mCpuTimeMillis;
        public long mDeltaAnonRssKBs;
        public int mOomAdj;
        public int mOomAdjReason;
        public long mOrigAnonRss;
        public int mProcState;
        public String mProcessName;
        private final long[] mRssAfterCompaction;
        public CompactSource mSourceType;
        public final int mUid;
        public long mZramConsumedKBs;

        SingleCompactionStats(long[] jArr, CompactSource compactSource, String str, long j, long j2, long j3, long j4, long j5, int i, int i2, int i3, int i4) {
            this.mRssAfterCompaction = jArr;
            this.mSourceType = compactSource;
            this.mProcessName = str;
            this.mUid = i4;
            this.mDeltaAnonRssKBs = j;
            this.mZramConsumedKBs = j2;
            this.mAnonMemFreedKBs = j3;
            this.mCpuTimeMillis = (float) j5;
            this.mOrigAnonRss = j4;
            this.mProcState = i;
            this.mOomAdj = i2;
            this.mOomAdjReason = i3;
        }

        double getCompactEfficiency() {
            return this.mAnonMemFreedKBs / this.mOrigAnonRss;
        }

        double getCompactCost() {
            return (this.mCpuTimeMillis / this.mAnonMemFreedKBs) * 1024.0d;
        }

        long[] getRssAfterCompaction() {
            return this.mRssAfterCompaction;
        }

        void dump(PrintWriter printWriter) {
            printWriter.println("    (" + this.mProcessName + "," + this.mSourceType.name() + "," + this.mDeltaAnonRssKBs + "," + this.mZramConsumedKBs + "," + this.mAnonMemFreedKBs + "," + getCompactEfficiency() + "," + getCompactCost() + "," + this.mProcState + "," + this.mOomAdj + "," + OomAdjuster.oomAdjReasonToString(this.mOomAdjReason) + ")");
        }

        void sendStat() {
            if (mRandom.nextFloat() < STATSD_SAMPLE_RATE) {
                FrameworkStatsLog.write(FrameworkStatsLog.APP_COMPACTED_V2, this.mUid, this.mProcState, this.mOomAdj, this.mDeltaAnonRssKBs, this.mZramConsumedKBs, this.mCpuTimeMillis, this.mOrigAnonRss, this.mOomAdjReason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class MemCompactionHandler extends Handler {
        private MemCompactionHandler() {
            super(CachedAppOptimizer.this.mCachedAppOptimizerThread.getLooper());
        }

        private boolean shouldOomAdjThrottleCompaction(ProcessRecord processRecord) {
            String str = processRecord.processName;
            if (processRecord.mState.getSetAdj() > 200) {
                return false;
            }
            if (!ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                return true;
            }
            Slog.d(IActivityManagerServiceExt.TAG, "Skipping compaction as process " + str + " is now perceptible.");
            return true;
        }

        private boolean shouldTimeThrottleCompaction(ProcessRecord processRecord, long j, CompactProfile compactProfile, CompactSource compactSource) {
            ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
            String str = processRecord.processName;
            CompactProfile lastCompactProfile = processCachedOptimizerRecord.getLastCompactProfile();
            long lastCompactTime = processCachedOptimizerRecord.getLastCompactTime();
            if (lastCompactTime == 0 || compactSource != CompactSource.APP) {
                return false;
            }
            CompactProfile compactProfile2 = CompactProfile.SOME;
            if (compactProfile == compactProfile2) {
                if ((lastCompactProfile != compactProfile2 || j - lastCompactTime >= CachedAppOptimizer.this.mCompactThrottleSomeSome) && (lastCompactProfile != CompactProfile.FULL || j - lastCompactTime >= CachedAppOptimizer.this.mCompactThrottleSomeFull)) {
                    return false;
                }
                if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    Slog.d(IActivityManagerServiceExt.TAG, "Skipping some compaction for " + str + ": too soon. throttle=" + CachedAppOptimizer.this.mCompactThrottleSomeSome + "/" + CachedAppOptimizer.this.mCompactThrottleSomeFull + " last=" + (j - lastCompactTime) + "ms ago");
                }
                return true;
            }
            CompactProfile compactProfile3 = CompactProfile.FULL;
            if (compactProfile != compactProfile3) {
                return false;
            }
            if ((lastCompactProfile != compactProfile2 || j - lastCompactTime >= CachedAppOptimizer.this.mCompactThrottleFullSome) && (lastCompactProfile != compactProfile3 || j - lastCompactTime >= CachedAppOptimizer.this.mCompactThrottleFullFull)) {
                return false;
            }
            if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                Slog.d(IActivityManagerServiceExt.TAG, "Skipping full compaction for " + str + ": too soon. throttle=" + CachedAppOptimizer.this.mCompactThrottleFullSome + "/" + CachedAppOptimizer.this.mCompactThrottleFullFull + " last=" + (j - lastCompactTime) + "ms ago");
            }
            return true;
        }

        private boolean shouldThrottleMiscCompaction(ProcessRecord processRecord, int i) {
            if (!CachedAppOptimizer.this.mProcStateThrottle.contains(Integer.valueOf(i))) {
                return false;
            }
            if (!ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                return true;
            }
            Slog.d(IActivityManagerServiceExt.TAG, "Skipping full compaction for process " + processRecord.processName + "; proc state is " + i);
            return true;
        }

        private boolean shouldRssThrottleCompaction(CompactProfile compactProfile, int i, String str, long[] jArr) {
            long j = jArr[2];
            SingleCompactionStats singleCompactionStats = CachedAppOptimizer.this.mLastCompactionStats.get(Integer.valueOf(i));
            if (jArr[0] == 0 && jArr[1] == 0 && jArr[2] == 0 && jArr[3] == 0) {
                if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    Slog.d(IActivityManagerServiceExt.TAG, "Skipping compaction forprocess " + i + " with no memory usage. Dead?");
                }
                return true;
            }
            if (compactProfile != CompactProfile.FULL) {
                return false;
            }
            if (CachedAppOptimizer.this.mFullAnonRssThrottleKb > 0 && j < CachedAppOptimizer.this.mFullAnonRssThrottleKb) {
                if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    Slog.d(IActivityManagerServiceExt.TAG, "Skipping full compaction for process " + str + "; anon RSS is too small: " + j + "KB.");
                }
                return true;
            }
            if (singleCompactionStats != null && CachedAppOptimizer.this.mFullDeltaRssThrottleKb > 0) {
                long[] rssAfterCompaction = singleCompactionStats.getRssAfterCompaction();
                long abs = Math.abs(jArr[1] - rssAfterCompaction[1]) + Math.abs(jArr[2] - rssAfterCompaction[2]) + Math.abs(jArr[3] - rssAfterCompaction[3]);
                if (abs <= CachedAppOptimizer.this.mFullDeltaRssThrottleKb) {
                    if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                        Slog.d(IActivityManagerServiceExt.TAG, "Skipping full compaction for process " + str + "; abs delta is too small: " + abs + "KB.");
                    }
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Finally extract failed */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r7v2 */
        /* JADX WARN: Type inference failed for: r7v4 */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AggregatedProcessCompactionStats aggregatedProcessCompactionStats;
            ProcessCachedOptimizerRecord processCachedOptimizerRecord;
            char c;
            ProcessRecord processRecord;
            long j;
            int i;
            int i2;
            long[] rss;
            long j2;
            String str;
            long j3;
            String str2;
            int i3 = message.what;
            if (i3 != 1) {
                if (i3 == 2) {
                    CachedAppOptimizer.this.mSystemCompactionsPerformed++;
                    Trace.traceBegin(64L, "compactSystem");
                    long m1431$$Nest$smgetMemoryFreedCompaction = CachedAppOptimizer.m1431$$Nest$smgetMemoryFreedCompaction();
                    CachedAppOptimizer.this.compactSystem();
                    long m1431$$Nest$smgetMemoryFreedCompaction2 = CachedAppOptimizer.m1431$$Nest$smgetMemoryFreedCompaction();
                    CachedAppOptimizer.this.mSystemTotalMemFreed += m1431$$Nest$smgetMemoryFreedCompaction2 - m1431$$Nest$smgetMemoryFreedCompaction;
                    Trace.traceEnd(64L);
                    return;
                }
                if (i3 != 5) {
                    return;
                }
                int i4 = message.arg1;
                CompactProfile compactProfile = CompactProfile.values()[message.arg2];
                Slog.d(IActivityManagerServiceExt.TAG, "Performing native compaction for pid=" + i4 + " type=" + compactProfile.name());
                Trace.traceBegin(64L, "compactSystem");
                try {
                    CachedAppOptimizer.this.mProcessDependencies.performCompaction(compactProfile, i4);
                } catch (Exception unused) {
                    Slog.d(IActivityManagerServiceExt.TAG, "Failed compacting native pid= " + i4);
                }
                Trace.traceEnd(64L);
                return;
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            int i5 = message.arg1;
            int i6 = message.arg2;
            ActivityManagerGlobalLock activityManagerGlobalLock = CachedAppOptimizer.this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    if (CachedAppOptimizer.this.mPendingCompactionProcesses.isEmpty()) {
                        if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                            Slog.d(IActivityManagerServiceExt.TAG, "No processes pending compaction, bail out");
                        }
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                        return;
                    }
                    ProcessRecord processRecord2 = (ProcessRecord) CachedAppOptimizer.this.mPendingCompactionProcesses.remove(0);
                    ProcessCachedOptimizerRecord processCachedOptimizerRecord2 = processRecord2.mOptRecord;
                    boolean isForceCompact = processCachedOptimizerRecord2.isForceCompact();
                    processCachedOptimizerRecord2.setForceCompact(false);
                    int pid = processRecord2.getPid();
                    String str3 = processRecord2.processName;
                    processCachedOptimizerRecord2.setHasPendingCompact(false);
                    CompactSource reqCompactSource = processCachedOptimizerRecord2.getReqCompactSource();
                    CompactProfile reqCompactProfile = processCachedOptimizerRecord2.getReqCompactProfile();
                    CompactProfile lastCompactProfile = processCachedOptimizerRecord2.getLastCompactProfile();
                    long lastCompactTime = processCachedOptimizerRecord2.getLastCompactTime();
                    int lastOomAdjChangeReason = processCachedOptimizerRecord2.getLastOomAdjChangeReason();
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                    AggregatedSourceCompactionStats perSourceAggregatedCompactStat = CachedAppOptimizer.this.getPerSourceAggregatedCompactStat(processCachedOptimizerRecord2.getReqCompactSource());
                    AggregatedProcessCompactionStats perProcessAggregatedCompactStat = CachedAppOptimizer.this.getPerProcessAggregatedCompactStat(str3);
                    if (pid == 0) {
                        if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                            Slog.d(IActivityManagerServiceExt.TAG, "Compaction failed, pid is 0");
                        }
                        perSourceAggregatedCompactStat.mProcCompactionsNoPidThrottled++;
                        perProcessAggregatedCompactStat.mProcCompactionsNoPidThrottled++;
                        return;
                    }
                    if (!isForceCompact) {
                        if (shouldOomAdjThrottleCompaction(processRecord2)) {
                            perProcessAggregatedCompactStat.mProcCompactionsOomAdjThrottled++;
                            perSourceAggregatedCompactStat.mProcCompactionsOomAdjThrottled++;
                            return;
                        }
                        aggregatedProcessCompactionStats = perProcessAggregatedCompactStat;
                        c = 0;
                        processCachedOptimizerRecord = processCachedOptimizerRecord2;
                        processRecord = processRecord2;
                        j = uptimeMillis;
                        i = i6;
                        i2 = i5;
                        if (shouldTimeThrottleCompaction(processRecord2, uptimeMillis, reqCompactProfile, reqCompactSource)) {
                            aggregatedProcessCompactionStats.mProcCompactionsTimeThrottled++;
                            perSourceAggregatedCompactStat.mProcCompactionsTimeThrottled++;
                            return;
                        } else if (shouldThrottleMiscCompaction(processRecord, i)) {
                            aggregatedProcessCompactionStats.mProcCompactionsMiscThrottled++;
                            perSourceAggregatedCompactStat.mProcCompactionsMiscThrottled++;
                            return;
                        } else {
                            rss = CachedAppOptimizer.this.mProcessDependencies.getRss(pid);
                            if (shouldRssThrottleCompaction(reqCompactProfile, pid, str3, rss)) {
                                aggregatedProcessCompactionStats.mProcCompactionsRSSThrottled++;
                                perSourceAggregatedCompactStat.mProcCompactionsRSSThrottled++;
                                return;
                            }
                        }
                    } else {
                        aggregatedProcessCompactionStats = perProcessAggregatedCompactStat;
                        processCachedOptimizerRecord = processCachedOptimizerRecord2;
                        c = 0;
                        processRecord = processRecord2;
                        j = uptimeMillis;
                        i = i6;
                        i2 = i5;
                        rss = CachedAppOptimizer.this.mProcessDependencies.getRss(pid);
                        if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                            Slog.d(IActivityManagerServiceExt.TAG, "Forcing compaction for " + str3);
                        }
                    }
                    CompactProfile resolveCompactionProfile = CachedAppOptimizer.this.resolveCompactionProfile(reqCompactProfile);
                    if (resolveCompactionProfile == CompactProfile.NONE) {
                        if (ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                            Slog.d(IActivityManagerServiceExt.TAG, "Resolved no compaction for " + str3 + " requested profile=" + reqCompactProfile);
                            return;
                        }
                        return;
                    }
                    try {
                        try {
                        } catch (Throwable th) {
                            th = th;
                            j2 = 64;
                        }
                    } catch (Exception e) {
                        e = e;
                        str = str3;
                    }
                    try {
                        Trace.traceBegin(64L, "Compact " + resolveCompactionProfile.name() + ": " + str3 + " lastOomAdjReason: " + lastOomAdjChangeReason + " source: " + reqCompactSource.name());
                        long m1432$$Nest$smgetUsedZramMemory = CachedAppOptimizer.m1432$$Nest$smgetUsedZramMemory();
                        long m1433$$Nest$smthreadCpuTimeNs = CachedAppOptimizer.m1433$$Nest$smthreadCpuTimeNs();
                        CachedAppOptimizer.this.mProcessDependencies.performCompaction(resolveCompactionProfile, pid);
                        long m1433$$Nest$smthreadCpuTimeNs2 = CachedAppOptimizer.m1433$$Nest$smthreadCpuTimeNs();
                        long[] rss2 = CachedAppOptimizer.this.mProcessDependencies.getRss(pid);
                        int i7 = i;
                        int i8 = i2;
                        long uptimeMillis2 = SystemClock.uptimeMillis();
                        long j4 = uptimeMillis2 - j;
                        long j5 = m1433$$Nest$smthreadCpuTimeNs2 - m1433$$Nest$smthreadCpuTimeNs;
                        long m1432$$Nest$smgetUsedZramMemory2 = CachedAppOptimizer.m1432$$Nest$smgetUsedZramMemory();
                        long j6 = rss2[c] - rss[c];
                        long j7 = rss2[1] - rss[1];
                        long j8 = rss2[2] - rss[2];
                        long j9 = rss2[3] - rss[3];
                        int i9 = AnonymousClass5.$SwitchMap$com$android$server$am$CachedAppOptimizer$CompactProfile[processCachedOptimizerRecord.getReqCompactProfile().ordinal()];
                        str = 1;
                        try {
                            if (i9 == 1) {
                                str2 = str3;
                                perSourceAggregatedCompactStat.mSomeCompactPerformed++;
                                aggregatedProcessCompactionStats.mSomeCompactPerformed++;
                            } else if (i9 == 2) {
                                str2 = str3;
                                perSourceAggregatedCompactStat.mFullCompactPerformed++;
                                aggregatedProcessCompactionStats.mFullCompactPerformed++;
                                long j10 = -j8;
                                long j11 = m1432$$Nest$smgetUsedZramMemory2 - m1432$$Nest$smgetUsedZramMemory;
                                long j12 = j10 - j11;
                                long j13 = j5 / 1000000;
                                long j14 = rss[2];
                                if (j10 <= 0) {
                                    j10 = 0;
                                }
                                long j15 = j11 > 0 ? j11 : 0L;
                                long j16 = j12 > 0 ? j12 : 0L;
                                aggregatedProcessCompactionStats.addMemStats(j10, j15, j16, j14, j13);
                                perSourceAggregatedCompactStat.addMemStats(j10, j15, j16, j14, j13);
                                SingleCompactionStats singleCompactionStats = new SingleCompactionStats(rss2, reqCompactSource, str2, j10, j15, j16, j14, j13, i7, i8, lastOomAdjChangeReason, processRecord.uid);
                                CachedAppOptimizer.this.mLastCompactionStats.remove(Integer.valueOf(pid));
                                CachedAppOptimizer.this.mLastCompactionStats.put(Integer.valueOf(pid), singleCompactionStats);
                                CachedAppOptimizer.this.mCompactionStatsHistory.add(singleCompactionStats);
                                if (!isForceCompact) {
                                    singleCompactionStats.sendStat();
                                }
                            } else {
                                Slog.wtf(IActivityManagerServiceExt.TAG, "Compaction: Unknown requested action");
                                Trace.traceEnd(64L);
                                return;
                            }
                            Object[] objArr = new Object[18];
                            objArr[c] = Integer.valueOf(pid);
                            objArr[1] = str2;
                            objArr[2] = resolveCompactionProfile.name();
                            objArr[3] = Long.valueOf(rss[c]);
                            objArr[4] = Long.valueOf(rss[1]);
                            objArr[5] = Long.valueOf(rss[2]);
                            objArr[6] = Long.valueOf(rss[3]);
                            objArr[7] = Long.valueOf(j6);
                            objArr[8] = Long.valueOf(j7);
                            objArr[9] = Long.valueOf(j8);
                            objArr[10] = Long.valueOf(j9);
                            objArr[11] = Long.valueOf(j4);
                            objArr[12] = lastCompactProfile.name();
                            objArr[13] = Long.valueOf(lastCompactTime);
                            objArr[14] = Integer.valueOf(i8);
                            objArr[15] = Integer.valueOf(i7);
                            objArr[16] = Long.valueOf(m1432$$Nest$smgetUsedZramMemory);
                            objArr[17] = Long.valueOf(m1432$$Nest$smgetUsedZramMemory - m1432$$Nest$smgetUsedZramMemory2);
                            EventLog.writeEvent(EventLogTags.AM_COMPACT, objArr);
                            ActivityManagerGlobalLock activityManagerGlobalLock2 = CachedAppOptimizer.this.mProcLock;
                            ActivityManagerService.boostPriorityForProcLockedSection();
                            synchronized (activityManagerGlobalLock2) {
                                ProcessCachedOptimizerRecord processCachedOptimizerRecord3 = processCachedOptimizerRecord;
                                try {
                                    processCachedOptimizerRecord3.setLastCompactTime(uptimeMillis2);
                                    processCachedOptimizerRecord3.setLastCompactProfile(reqCompactProfile);
                                } catch (Throwable th2) {
                                    throw th2;
                                }
                            }
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            j3 = 64;
                        } catch (Exception e2) {
                            e = e2;
                            Slog.d(IActivityManagerServiceExt.TAG, "Exception occurred while compacting pid: " + str + ". Exception:" + e.getMessage());
                            j3 = 64;
                            Trace.traceEnd(j3);
                        }
                        Trace.traceEnd(j3);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = 64;
                        Trace.traceEnd(j2);
                        throw th;
                    }
                } finally {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportOneUidFrozenStateChanged(int i, boolean z) {
        int[] iArr = new int[1];
        int[] iArr2 = {i};
        iArr[0] = z ? 1 : 2;
        if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
            Slog.d(IActivityManagerServiceExt.TAG, "reportOneUidFrozenStateChanged uid " + i + " frozen = " + z);
        }
        this.mAm.reportUidFrozenStateChanged(iArr2, iArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postUidFrozenMessage(int i, boolean z) {
        Integer valueOf = Integer.valueOf(i);
        this.mFreezeHandler.removeEqualMessages(6, valueOf);
        Handler handler = this.mFreezeHandler;
        handler.sendMessage(handler.obtainMessage(6, z ? 1 : 0, 0, valueOf));
    }

    @GuardedBy({"mAm"})
    private void reportProcessFreezableChangedLocked(ProcessRecord processRecord) {
        this.mAm.onProcessFreezableChangedLocked(processRecord);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class FreezeHandler extends Handler implements ProcLocksReader.ProcLocksReaderCallback {
        private FreezeHandler() {
            super(CachedAppOptimizer.this.mCachedAppOptimizerThread.getLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                ProcessRecord processRecord = (ProcessRecord) message.obj;
                ActivityManagerService activityManagerService = CachedAppOptimizer.this.mAm;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        freezeProcess(processRecord);
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                if (processRecord.mOptRecord.isFrozen()) {
                    CachedAppOptimizer.this.onProcessFrozen(processRecord);
                    removeMessages(7);
                    sendEmptyMessageDelayed(7, CachedAppOptimizer.DEFAULT_FREEZER_BINDER_THRESHOLD);
                    return;
                }
                return;
            }
            if (i == 4) {
                int i2 = message.arg1;
                int i3 = message.arg2;
                Pair pair = (Pair) message.obj;
                reportUnfreeze(i2, i3, (String) pair.first, ((Integer) pair.second).intValue());
                return;
            }
            if (i == 6) {
                CachedAppOptimizer.this.reportOneUidFrozenStateChanged(((Integer) message.obj).intValue(), message.arg1 == 1);
            } else {
                if (i != 7) {
                    return;
                }
                try {
                    if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                        Slog.d(IActivityManagerServiceExt.TAG, "Freezer deadlock watchdog");
                    }
                    CachedAppOptimizer.this.mProcLocksReader.handleBlockingFileLocks(this);
                } catch (IOException unused) {
                    Slog.w(IActivityManagerServiceExt.TAG, "Unable to check file locks");
                }
            }
        }

        @GuardedBy({"mAm", "mProcLock"})
        private void handleBinderFreezerFailure(final ProcessRecord processRecord, String str) {
            if (!CachedAppOptimizer.this.mFreezerBinderEnabled) {
                CachedAppOptimizer.this.unfreezeAppLSP(processRecord, 18);
                CachedAppOptimizer.this.freezeAppAsyncLSP(processRecord);
                return;
            }
            if (processRecord.mOptRecord.getLastUsedTimeout() <= CachedAppOptimizer.this.mFreezerBinderThreshold) {
                Slog.d(IActivityManagerServiceExt.TAG, "Kill app due to repeated failure to freeze binder: " + processRecord.getPid() + " " + processRecord.processName);
                CachedAppOptimizer.this.mAm.mHandler.post(new Runnable() { // from class: com.android.server.am.CachedAppOptimizer$FreezeHandler$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        CachedAppOptimizer.FreezeHandler.this.lambda$handleBinderFreezerFailure$0(processRecord);
                    }
                });
                return;
            }
            long max = Math.max((processRecord.mOptRecord.getLastUsedTimeout() / CachedAppOptimizer.this.mFreezerBinderDivisor) + (CachedAppOptimizer.this.mRandom.nextInt(CachedAppOptimizer.this.mFreezerBinderOffset * 2) - CachedAppOptimizer.this.mFreezerBinderOffset), CachedAppOptimizer.this.mFreezerBinderThreshold);
            Slog.d(IActivityManagerServiceExt.TAG, "Reschedule freeze for process " + processRecord.getPid() + " " + processRecord.processName + " (" + str + "), timeout=" + max);
            Trace.instantForTrack(64L, CachedAppOptimizer.ATRACE_FREEZER_TRACK, "Reschedule freeze " + processRecord.processName + ":" + processRecord.getPid() + " timeout=" + max + ", reason=" + str);
            CachedAppOptimizer.this.unfreezeAppLSP(processRecord, 18);
            CachedAppOptimizer.this.freezeAppAsyncLSP(processRecord, max);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleBinderFreezerFailure$0(ProcessRecord processRecord) {
            ActivityManagerService activityManagerService = CachedAppOptimizer.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (processRecord.getThread() == null) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                    } else {
                        processRecord.killLocked("excessive binder traffic during cached", 9, 7, true);
                        ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @GuardedBy({"mAm"})
        private void freezeProcess(final ProcessRecord processRecord) {
            processRecord.getPid();
            String str = processRecord.processName;
            ProcessCachedOptimizerRecord processCachedOptimizerRecord = processRecord.mOptRecord;
            ActivityManagerGlobalLock activityManagerGlobalLock = CachedAppOptimizer.this.mProcLock;
            ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    if (processCachedOptimizerRecord.isPendingFreeze()) {
                        processCachedOptimizerRecord.setPendingFreeze(false);
                        int pid = processRecord.getPid();
                        if (CachedAppOptimizer.this.mFreezerOverride) {
                            processCachedOptimizerRecord.setFreezerOverride(true);
                            Slog.d(IActivityManagerServiceExt.TAG, "Skipping freeze for process " + pid + " " + str + " curAdj = " + processRecord.mState.getCurAdj() + "(override)");
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            return;
                        }
                        if (pid != 0 && !processCachedOptimizerRecord.isFrozen()) {
                            Slog.d(IActivityManagerServiceExt.TAG, "freezing " + pid + " " + str);
                            try {
                                if (CachedAppOptimizer.freezeBinder(pid, true, 100) != 0) {
                                    handleBinderFreezerFailure(processRecord, "outstanding txns");
                                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                                    return;
                                }
                            } catch (RuntimeException unused) {
                                Slog.e(IActivityManagerServiceExt.TAG, "Unable to freeze binder for " + pid + " " + str);
                                CachedAppOptimizer.this.mFreezeHandler.post(new Runnable() { // from class: com.android.server.am.CachedAppOptimizer$FreezeHandler$$ExternalSyntheticLambda0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        CachedAppOptimizer.FreezeHandler.this.lambda$freezeProcess$1(processRecord);
                                    }
                                });
                            }
                            long freezeUnfreezeTime = processCachedOptimizerRecord.getFreezeUnfreezeTime();
                            try {
                                CachedAppOptimizer.traceAppFreeze(processRecord.processName, pid, -1);
                                Process.setProcessFrozen(pid, processRecord.uid, true);
                                processCachedOptimizerRecord.setFreezeUnfreezeTime(SystemClock.uptimeMillis());
                                processCachedOptimizerRecord.setFrozen(true);
                                processCachedOptimizerRecord.setHasCollectedFrozenPSS(false);
                                CachedAppOptimizer.this.mFrozenProcesses.put(pid, processRecord);
                            } catch (Exception unused2) {
                                Slog.w(IActivityManagerServiceExt.TAG, "Unable to freeze " + pid + " " + str);
                            }
                            long freezeUnfreezeTime2 = processCachedOptimizerRecord.getFreezeUnfreezeTime() - freezeUnfreezeTime;
                            boolean isFrozen = processCachedOptimizerRecord.isFrozen();
                            UidRecord uidRecord = processRecord.getUidRecord();
                            if (isFrozen && uidRecord != null && uidRecord.areAllProcessesFrozen()) {
                                uidRecord.setFrozen(true);
                                CachedAppOptimizer.this.postUidFrozenMessage(uidRecord.getUid(), true);
                            }
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            if (isFrozen) {
                                EventLog.writeEvent(EventLogTags.AM_FREEZE, Integer.valueOf(pid), str);
                                if (CachedAppOptimizer.this.mRandom.nextFloat() < CachedAppOptimizer.this.mFreezerStatsdSampleRate) {
                                    FrameworkStatsLog.write(FrameworkStatsLog.APP_FREEZE_CHANGED, 1, pid, str, freezeUnfreezeTime2, 0, 0);
                                }
                                try {
                                    if ((CachedAppOptimizer.getBinderFreezeInfo(pid) & 4) != 0) {
                                        ActivityManagerGlobalLock activityManagerGlobalLock2 = CachedAppOptimizer.this.mProcLock;
                                        ActivityManagerService.boostPriorityForProcLockedSection();
                                        synchronized (activityManagerGlobalLock2) {
                                            try {
                                                handleBinderFreezerFailure(processRecord, "new pending txns");
                                            } finally {
                                            }
                                        }
                                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                                        return;
                                    }
                                    return;
                                } catch (RuntimeException unused3) {
                                    Slog.e(IActivityManagerServiceExt.TAG, "Unable to freeze binder for " + pid + " " + str);
                                    CachedAppOptimizer.this.mFreezeHandler.post(new Runnable() { // from class: com.android.server.am.CachedAppOptimizer$FreezeHandler$$ExternalSyntheticLambda1
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            CachedAppOptimizer.FreezeHandler.this.lambda$freezeProcess$2(processRecord);
                                        }
                                    });
                                    return;
                                }
                            }
                            return;
                        }
                        if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                            Slog.d(IActivityManagerServiceExt.TAG, "Skipping freeze for process " + pid + " " + str + ". Already frozen or not a real process");
                        }
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                    }
                } finally {
                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$freezeProcess$1(ProcessRecord processRecord) {
            ActivityManagerService activityManagerService = CachedAppOptimizer.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    processRecord.killLocked("Unable to freeze binder interface", 14, 19, true);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$freezeProcess$2(ProcessRecord processRecord) {
            ActivityManagerService activityManagerService = CachedAppOptimizer.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    processRecord.killLocked("Unable to freeze binder interface", 14, 19, true);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }

        private void reportUnfreeze(int i, int i2, String str, int i3) {
            EventLog.writeEvent(EventLogTags.AM_UNFREEZE, Integer.valueOf(i), str, Integer.valueOf(i3));
            if (CachedAppOptimizer.this.mRandom.nextFloat() < CachedAppOptimizer.this.mFreezerStatsdSampleRate) {
                FrameworkStatsLog.write(FrameworkStatsLog.APP_FREEZE_CHANGED, 2, i, str, i2, 0, i3);
            }
        }

        @GuardedBy({"mAm"})
        public void onBlockingFileLock(IntArray intArray) {
            ProcessRecord processRecord;
            if (ActivityManagerDebugConfig.DEBUG_FREEZER) {
                Slog.d(IActivityManagerServiceExt.TAG, "Blocking file lock found: " + intArray);
            }
            ActivityManagerService activityManagerService = CachedAppOptimizer.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    ActivityManagerGlobalLock activityManagerGlobalLock = CachedAppOptimizer.this.mProcLock;
                    ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            int i = intArray.get(0);
                            ProcessRecord processRecord2 = (ProcessRecord) CachedAppOptimizer.this.mFrozenProcesses.get(i);
                            if (processRecord2 != null) {
                                int i2 = 1;
                                while (true) {
                                    if (i2 >= intArray.size()) {
                                        break;
                                    }
                                    int i3 = intArray.get(i2);
                                    synchronized (CachedAppOptimizer.this.mAm.mPidsSelfLocked) {
                                        processRecord = CachedAppOptimizer.this.mAm.mPidsSelfLocked.get(i3);
                                    }
                                    if (processRecord != null && processRecord.mState.getCurAdj() < 900) {
                                        Slog.d(IActivityManagerServiceExt.TAG, processRecord2.processName + " (" + i + ") blocks " + processRecord.processName + " (" + i3 + ")");
                                        CachedAppOptimizer.this.unfreezeAppLSP(processRecord2, 16);
                                        break;
                                    }
                                    i2++;
                                }
                            }
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class DefaultProcessDependencies implements ProcessDependencies {
        public static volatile int mPidCompacting = -1;

        private DefaultProcessDependencies() {
        }

        @Override // com.android.server.am.CachedAppOptimizer.ProcessDependencies
        public long[] getRss(int i) {
            return Process.getRss(i);
        }

        @Override // com.android.server.am.CachedAppOptimizer.ProcessDependencies
        public void performCompaction(CompactProfile compactProfile, int i) throws IOException {
            mPidCompacting = i;
            if (compactProfile == CompactProfile.FULL) {
                CachedAppOptimizer.compactProcess(i, 3);
            } else if (compactProfile == CompactProfile.SOME) {
                CachedAppOptimizer.compactProcess(i, 1);
            } else if (compactProfile == CompactProfile.ANON) {
                CachedAppOptimizer.compactProcess(i, 2);
            }
            mPidCompacting = -1;
        }
    }

    public ICachedAppOptimizerWrapper getWrapper() {
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class CachedAppOptimizerWrapper implements ICachedAppOptimizerWrapper {
        private boolean mExtUseFreezerEnable;

        private CachedAppOptimizerWrapper() {
            this.mExtUseFreezerEnable = true;
        }

        @Override // com.android.server.am.ICachedAppOptimizerWrapper
        public void updateExtUseFreezerEnable(boolean z) {
            Slog.d(IActivityManagerServiceExt.TAG, "updateExtUseFreezerEnable: " + z);
            synchronized (CachedAppOptimizer.this.mPhenotypeFlagLock) {
                if (this.mExtUseFreezerEnable != z) {
                    this.mExtUseFreezerEnable = z;
                    CachedAppOptimizer.this.updateUseFreezer();
                }
            }
        }

        public boolean isExtUseFreezeEnable() {
            return this.mExtUseFreezerEnable;
        }
    }
}
