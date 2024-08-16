package com.android.server.job;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.BackgroundStartPrivileges;
import android.app.UserSwitchObserver;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.DataUnit;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import android.util.Pools;
import android.util.Slog;
import android.util.SparseArrayMap;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.util.MemInfoReader;
import com.android.internal.util.StatLogger;
import com.android.modules.expresslog.Histogram;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.LocalServices;
import com.android.server.job.JobConcurrencyManager;
import com.android.server.job.controllers.JobStatus;
import com.android.server.job.controllers.StateController;
import com.android.server.job.restrictions.JobRestriction;
import com.android.server.pm.UserManagerInternal;
import com.android.server.slice.SliceClientPermissions;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class JobConcurrencyManager {
    private static final int ALL_WORK_TYPES = 127;
    static final String CONFIG_KEY_PREFIX_CONCURRENCY = "concurrency_";
    private static final WorkConfigLimitsPerMemoryTrimLevel CONFIG_LIMITS_SCREEN_OFF;
    private static final WorkConfigLimitsPerMemoryTrimLevel CONFIG_LIMITS_SCREEN_ON;
    private static final boolean DEBUG = JobSchedulerService.DEBUG;

    @VisibleForTesting
    static final int DEFAULT_CONCURRENCY_LIMIT;
    private static final boolean DEFAULT_ENABLE_MAX_WAIT_TIME_BYPASS = true;

    @VisibleForTesting
    static final long DEFAULT_MAX_WAIT_EJ_MS = 300000;

    @VisibleForTesting
    static final long DEFAULT_MAX_WAIT_REGULAR_MS = 1800000;

    @VisibleForTesting
    static final long DEFAULT_MAX_WAIT_UI_MS = 300000;
    private static final int DEFAULT_PKG_CONCURRENCY_LIMIT_EJ = 3;
    private static final int DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR;
    private static final long DEFAULT_SCREEN_OFF_ADJUSTMENT_DELAY_MS = 30000;
    private static final String KEY_CONCURRENCY_LIMIT = "concurrency_limit";

    @VisibleForTesting
    static final String KEY_ENABLE_MAX_WAIT_TIME_BYPASS = "concurrency_enable_max_wait_time_bypass";
    private static final String KEY_MAX_WAIT_EJ_MS = "concurrency_max_wait_ej_ms";
    private static final String KEY_MAX_WAIT_REGULAR_MS = "concurrency_max_wait_regular_ms";

    @VisibleForTesting
    static final String KEY_MAX_WAIT_UI_MS = "concurrency_max_wait_ui_ms";

    @VisibleForTesting
    static final String KEY_PKG_CONCURRENCY_LIMIT_EJ = "concurrency_pkg_concurrency_limit_ej";

    @VisibleForTesting
    static final String KEY_PKG_CONCURRENCY_LIMIT_REGULAR = "concurrency_pkg_concurrency_limit_regular";
    private static final String KEY_SCREEN_OFF_ADJUSTMENT_DELAY_MS = "concurrency_screen_off_adjustment_delay_ms";

    @VisibleForTesting
    static final int MAX_CONCURRENCY_LIMIT = 64;
    private static final int MAX_RETAINED_OBJECTS = 96;

    @VisibleForTesting
    static final int NUM_WORK_TYPES = 7;
    private static final int PRIVILEGED_STATE_BAL = 2;
    private static final int PRIVILEGED_STATE_NONE = 1;
    private static final int PRIVILEGED_STATE_TOP = 3;
    private static final int PRIVILEGED_STATE_UNDEFINED = 0;
    private static final int SYSTEM_STATE_REFRESH_MIN_INTERVAL = 1000;
    private static final String TAG = "JobScheduler.Concurrency";
    static final int WORK_TYPE_BG = 16;
    static final int WORK_TYPE_BGUSER = 64;
    static final int WORK_TYPE_BGUSER_IMPORTANT = 32;
    static final int WORK_TYPE_EJ = 8;
    static final int WORK_TYPE_FGS = 2;
    static final int WORK_TYPE_NONE = 0;
    static final int WORK_TYPE_TOP = 1;
    static final int WORK_TYPE_UI = 4;
    private static final Histogram sConcurrencyHistogramLogger;
    private static final Comparator<ContextAssignment> sDeterminationComparator;
    private final SparseArrayMap<String, PackageStats> mActivePkgStats;
    final List<JobServiceContext> mActiveServices;
    private final Context mContext;
    private final Pools.Pool<ContextAssignment> mContextAssignmentPool;
    private boolean mCurrentInteractiveState;
    private boolean mEffectiveInteractiveState;

    @VisibleForTesting
    GracePeriodObserver mGracePeriodObserver;
    private final Handler mHandler;
    private final ArraySet<JobServiceContext> mIdleContexts;
    private final Injector mInjector;
    public IJobConcurrencyManagerExt mJobConcurrencyManagerExt;
    private int mLastMemoryTrimLevel;
    private long mLastScreenOffRealtime;
    private long mLastScreenOnRealtime;
    private final Object mLock;
    private long mMaxWaitEjMs;
    private long mMaxWaitRegularMs;
    private boolean mMaxWaitTimeBypassEnabled;
    private long mMaxWaitUIMs;
    private long mNextSystemStateRefreshTime;
    private final JobNotificationCoordinator mNotificationCoordinator;
    private int mNumDroppedContexts;
    private final Consumer<PackageStats> mPackageStatsStagingCountClearer;
    private int mPkgConcurrencyLimitEj;
    private int mPkgConcurrencyLimitRegular;
    private final Pools.Pool<PackageStats> mPkgStatsPool;
    private PowerManager mPowerManager;
    private final Runnable mRampUpForScreenOff;
    private final BroadcastReceiver mReceiver;
    private final AssignmentInfo mRecycledAssignmentInfo;
    private final ArraySet<ContextAssignment> mRecycledChanged;
    private final ArraySet<ContextAssignment> mRecycledIdle;
    private final ArrayList<ContextAssignment> mRecycledPreferredUidOnly;
    private final SparseIntArray mRecycledPrivilegedState;
    private final ArrayList<ContextAssignment> mRecycledStoppable;
    private final ArraySet<JobStatus> mRunningJobs;
    private long mScreenOffAdjustmentDelayMs;
    private final JobSchedulerService mService;

    @VisibleForTesting
    boolean mShouldRestrictBgUser;
    private final StatLogger mStatLogger;
    private int mSteadyStateConcurrencyLimit;
    private final WorkCountTracker mWorkCountTracker;
    private WorkTypeConfig mWorkTypeConfig;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface Stats {
        public static final int ASSIGN_JOBS_TO_CONTEXTS = 0;
        public static final int COUNT = 2;
        public static final int REFRESH_SYSTEM_STATE = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface WorkType {
    }

    static {
        if (ActivityManager.isLowRamDeviceStatic()) {
            DEFAULT_CONCURRENCY_LIMIT = 8;
        } else {
            long totalSize = new MemInfoReader().getTotalSize();
            if (totalSize <= DataUnit.GIGABYTES.toBytes(6L)) {
                DEFAULT_CONCURRENCY_LIMIT = 16;
            } else if (totalSize <= DataUnit.GIGABYTES.toBytes(8L)) {
                DEFAULT_CONCURRENCY_LIMIT = 20;
            } else if (totalSize <= DataUnit.GIGABYTES.toBytes(12L)) {
                DEFAULT_CONCURRENCY_LIMIT = 32;
            } else {
                DEFAULT_CONCURRENCY_LIMIT = 40;
            }
        }
        int i = DEFAULT_CONCURRENCY_LIMIT;
        DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR = i / 2;
        CONFIG_LIMITS_SCREEN_ON = new WorkConfigLimitsPerMemoryTrimLevel(new WorkTypeConfig("screen_on_normal", i, (i * 3) / 4, List.of(Pair.create(1, Float.valueOf(0.4f)), Pair.create(2, Float.valueOf(0.2f)), Pair.create(4, Float.valueOf(0.1f)), Pair.create(8, Float.valueOf(0.1f)), Pair.create(16, Float.valueOf(0.05f)), Pair.create(32, Float.valueOf(0.05f))), List.of(Pair.create(16, Float.valueOf(0.5f)), Pair.create(32, Float.valueOf(0.25f)), Pair.create(64, Float.valueOf(0.2f)))), new WorkTypeConfig("screen_on_moderate", i, i / 2, List.of(Pair.create(1, Float.valueOf(0.4f)), Pair.create(2, Float.valueOf(0.1f)), Pair.create(4, Float.valueOf(0.1f)), Pair.create(8, Float.valueOf(0.1f)), Pair.create(16, Float.valueOf(0.1f)), Pair.create(32, Float.valueOf(0.1f))), List.of(Pair.create(16, Float.valueOf(0.4f)), Pair.create(32, Float.valueOf(0.1f)), Pair.create(64, Float.valueOf(0.1f)))), new WorkTypeConfig("screen_on_low", i, (i * 4) / 10, List.of(Pair.create(1, Float.valueOf(0.6f)), Pair.create(2, Float.valueOf(0.1f)), Pair.create(4, Float.valueOf(0.1f)), Pair.create(8, Float.valueOf(0.1f))), List.of(Pair.create(16, Float.valueOf(0.33333334f)), Pair.create(32, Float.valueOf(0.16666667f)), Pair.create(64, Float.valueOf(0.16666667f)))), new WorkTypeConfig("screen_on_critical", i, (i * 4) / 10, List.of(Pair.create(1, Float.valueOf(0.7f)), Pair.create(2, Float.valueOf(0.1f)), Pair.create(4, Float.valueOf(0.1f)), Pair.create(8, Float.valueOf(0.05f))), List.of(Pair.create(16, Float.valueOf(0.16666667f)), Pair.create(32, Float.valueOf(0.16666667f)), Pair.create(64, Float.valueOf(0.16666667f)))));
        CONFIG_LIMITS_SCREEN_OFF = new WorkConfigLimitsPerMemoryTrimLevel(new WorkTypeConfig("screen_off_normal", i, i, List.of(Pair.create(1, Float.valueOf(0.3f)), Pair.create(2, Float.valueOf(0.2f)), Pair.create(4, Float.valueOf(0.2f)), Pair.create(8, Float.valueOf(0.15f)), Pair.create(16, Float.valueOf(0.1f)), Pair.create(32, Float.valueOf(0.05f))), List.of(Pair.create(16, Float.valueOf(0.6f)), Pair.create(32, Float.valueOf(0.2f)), Pair.create(64, Float.valueOf(0.2f)))), new WorkTypeConfig("screen_off_moderate", i, (i * 9) / 10, List.of(Pair.create(1, Float.valueOf(0.3f)), Pair.create(2, Float.valueOf(0.2f)), Pair.create(4, Float.valueOf(0.2f)), Pair.create(8, Float.valueOf(0.15f)), Pair.create(16, Float.valueOf(0.1f)), Pair.create(32, Float.valueOf(0.05f))), List.of(Pair.create(16, Float.valueOf(0.5f)), Pair.create(32, Float.valueOf(0.1f)), Pair.create(64, Float.valueOf(0.1f)))), new WorkTypeConfig("screen_off_low", i, (i * 6) / 10, List.of(Pair.create(1, Float.valueOf(0.3f)), Pair.create(2, Float.valueOf(0.15f)), Pair.create(4, Float.valueOf(0.15f)), Pair.create(8, Float.valueOf(0.1f)), Pair.create(16, Float.valueOf(0.05f)), Pair.create(32, Float.valueOf(0.05f))), List.of(Pair.create(16, Float.valueOf(0.25f)), Pair.create(32, Float.valueOf(0.1f)), Pair.create(64, Float.valueOf(0.1f)))), new WorkTypeConfig("screen_off_critical", i, (i * 4) / 10, List.of(Pair.create(1, Float.valueOf(0.3f)), Pair.create(2, Float.valueOf(0.1f)), Pair.create(4, Float.valueOf(0.1f)), Pair.create(8, Float.valueOf(0.05f))), List.of(Pair.create(16, Float.valueOf(0.1f)), Pair.create(32, Float.valueOf(0.1f)), Pair.create(64, Float.valueOf(0.1f)))));
        sDeterminationComparator = new Comparator() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$static$0;
                lambda$static$0 = JobConcurrencyManager.lambda$static$0((JobConcurrencyManager.ContextAssignment) obj, (JobConcurrencyManager.ContextAssignment) obj2);
                return lambda$static$0;
            }
        };
        sConcurrencyHistogramLogger = new Histogram("job_scheduler.value_hist_job_concurrency", new Histogram.UniformOptions(100, 0.0f, 99.0f));
    }

    @VisibleForTesting
    static String workTypeToString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "TOP";
        }
        if (i == 2) {
            return "FGS";
        }
        if (i == 4) {
            return "UI";
        }
        if (i == 8) {
            return "EJ";
        }
        if (i == 16) {
            return "BG";
        }
        if (i == 32) {
            return "BGUSER_IMPORTANT";
        }
        if (i == 64) {
            return "BGUSER";
        }
        return "WORK(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(ContextAssignment contextAssignment, ContextAssignment contextAssignment2) {
        if (contextAssignment == contextAssignment2) {
            return 0;
        }
        JobStatus runningJobLocked = contextAssignment.context.getRunningJobLocked();
        JobStatus runningJobLocked2 = contextAssignment2.context.getRunningJobLocked();
        if (runningJobLocked == null) {
            return runningJobLocked2 == null ? 0 : 1;
        }
        if (runningJobLocked2 == null) {
            return -1;
        }
        if (runningJobLocked.lastEvaluatedBias == 40) {
            if (runningJobLocked2.lastEvaluatedBias != 40) {
                return -1;
            }
        } else if (runningJobLocked2.lastEvaluatedBias == 40) {
            return 1;
        }
        return Long.compare(contextAssignment2.context.getExecutionStartTimeElapsed(), contextAssignment.context.getExecutionStartTimeElapsed());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JobConcurrencyManager(JobSchedulerService jobSchedulerService) {
        this(jobSchedulerService, new Injector());
    }

    @VisibleForTesting
    JobConcurrencyManager(JobSchedulerService jobSchedulerService, Injector injector) {
        this.mRecycledChanged = new ArraySet<>();
        this.mRecycledIdle = new ArraySet<>();
        this.mRecycledPreferredUidOnly = new ArrayList<>();
        this.mRecycledStoppable = new ArrayList<>();
        this.mRecycledAssignmentInfo = new AssignmentInfo();
        this.mRecycledPrivilegedState = new SparseIntArray();
        this.mContextAssignmentPool = new Pools.SimplePool(96);
        this.mActiveServices = new ArrayList();
        this.mIdleContexts = new ArraySet<>();
        this.mNumDroppedContexts = 0;
        this.mRunningJobs = new ArraySet<>();
        this.mWorkCountTracker = new WorkCountTracker();
        this.mPkgStatsPool = new Pools.SimplePool(96);
        this.mActivePkgStats = new SparseArrayMap<>();
        this.mWorkTypeConfig = CONFIG_LIMITS_SCREEN_OFF.normal;
        this.mScreenOffAdjustmentDelayMs = DEFAULT_SCREEN_OFF_ADJUSTMENT_DELAY_MS;
        this.mSteadyStateConcurrencyLimit = DEFAULT_CONCURRENCY_LIMIT;
        this.mPkgConcurrencyLimitEj = 3;
        this.mPkgConcurrencyLimitRegular = DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR;
        this.mMaxWaitTimeBypassEnabled = true;
        this.mMaxWaitUIMs = 300000L;
        this.mMaxWaitEjMs = 300000L;
        this.mMaxWaitRegularMs = 1800000L;
        this.mPackageStatsStagingCountClearer = new Consumer() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((JobConcurrencyManager.PackageStats) obj).resetStagedCount();
            }
        };
        this.mStatLogger = new StatLogger(new String[]{"assignJobsToContexts", "refreshSystemState"});
        this.mJobConcurrencyManagerExt = (IJobConcurrencyManagerExt) ExtLoader.type(IJobConcurrencyManagerExt.class).base(this).create();
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.server.job.JobConcurrencyManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                char c = 65535;
                switch (action.hashCode()) {
                    case -2128145023:
                        if (action.equals("android.intent.action.SCREEN_OFF")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1454123155:
                        if (action.equals("android.intent.action.SCREEN_ON")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 870701415:
                        if (action.equals("android.os.action.DEVICE_IDLE_MODE_CHANGED")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1779291251:
                        if (action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        JobConcurrencyManager.this.onInteractiveStateChanged(false);
                        return;
                    case 1:
                        JobConcurrencyManager.this.onInteractiveStateChanged(true);
                        return;
                    case 2:
                        if (JobConcurrencyManager.this.mPowerManager == null || !JobConcurrencyManager.this.mPowerManager.isDeviceIdleMode()) {
                            return;
                        }
                        synchronized (JobConcurrencyManager.this.mLock) {
                            JobConcurrencyManager.this.stopUnexemptedJobsForDoze();
                            JobConcurrencyManager.this.stopOvertimeJobsLocked("deep doze");
                        }
                        return;
                    case 3:
                        if (JobConcurrencyManager.this.mPowerManager == null || !JobConcurrencyManager.this.mPowerManager.isPowerSaveMode()) {
                            return;
                        }
                        synchronized (JobConcurrencyManager.this.mLock) {
                            JobConcurrencyManager.this.stopOvertimeJobsLocked("battery saver");
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mRampUpForScreenOff = new Runnable() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                JobConcurrencyManager.this.rampUpForScreenOff();
            }
        };
        this.mService = jobSchedulerService;
        this.mLock = jobSchedulerService.getLock();
        Context testableContext = jobSchedulerService.getTestableContext();
        this.mContext = testableContext;
        this.mInjector = injector;
        this.mNotificationCoordinator = new JobNotificationCoordinator();
        this.mHandler = AppSchedulingModuleThread.getHandler();
        this.mGracePeriodObserver = new GracePeriodObserver(testableContext);
        this.mShouldRestrictBgUser = testableContext.getResources().getBoolean(17891728);
    }

    public void onSystemReady() {
        this.mPowerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        try {
            ActivityManager.getService().registerUserSwitchObserver(this.mGracePeriodObserver, TAG);
        } catch (RemoteException unused) {
        }
        onInteractiveStateChanged(this.mPowerManager.isInteractive());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onThirdPartyAppsCanStart() {
        IBatteryStats asInterface = IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
        for (int i = 0; i < this.mSteadyStateConcurrencyLimit; i++) {
            ArraySet<JobServiceContext> arraySet = this.mIdleContexts;
            Injector injector = this.mInjector;
            JobSchedulerService jobSchedulerService = this.mService;
            arraySet.add(injector.createJobServiceContext(jobSchedulerService, this, this.mNotificationCoordinator, asInterface, jobSchedulerService.mJobPackageTracker, AppSchedulingModuleThread.get().getLooper()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onAppRemovedLocked(String str, int i) {
        PackageStats packageStats = (PackageStats) this.mActivePkgStats.get(UserHandle.getUserId(i), str);
        if (packageStats != null) {
            if (packageStats.numRunningEj > 0 || packageStats.numRunningRegular > 0) {
                Slog.w(TAG, str + "(" + i + ") marked as removed before jobs stopped running");
                return;
            }
            this.mActivePkgStats.delete(UserHandle.getUserId(i), str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserRemoved(int i) {
        this.mGracePeriodObserver.onUserRemoved(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInteractiveStateChanged(boolean z) {
        synchronized (this.mLock) {
            if (this.mCurrentInteractiveState == z) {
                return;
            }
            this.mCurrentInteractiveState = z;
            if (DEBUG) {
                Slog.d(TAG, "Interactive: " + z);
            }
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            if (z) {
                this.mLastScreenOnRealtime = millis;
                this.mEffectiveInteractiveState = true;
                this.mHandler.removeCallbacks(this.mRampUpForScreenOff);
            } else {
                this.mLastScreenOffRealtime = millis;
                this.mHandler.postDelayed(this.mRampUpForScreenOff, this.mScreenOffAdjustmentDelayMs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rampUpForScreenOff() {
        synchronized (this.mLock) {
            if (this.mEffectiveInteractiveState) {
                if (this.mLastScreenOnRealtime > this.mLastScreenOffRealtime) {
                    return;
                }
                if (this.mLastScreenOffRealtime + this.mScreenOffAdjustmentDelayMs > JobSchedulerService.sElapsedRealtimeClock.millis()) {
                    return;
                }
                this.mEffectiveInteractiveState = false;
                if (DEBUG) {
                    Slog.d(TAG, "Ramping up concurrency");
                }
                this.mService.maybeRunPendingJobsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public ArraySet<JobStatus> getRunningJobsLocked() {
        return this.mRunningJobs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isJobRunningLocked(JobStatus jobStatus) {
        return this.mRunningJobs.contains(jobStatus);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isJobInOvertimeLocked(JobStatus jobStatus) {
        if (!this.mRunningJobs.contains(jobStatus)) {
            return false;
        }
        for (int size = this.mActiveServices.size() - 1; size >= 0; size--) {
            if (this.mActiveServices.get(size).getRunningJobLocked() == jobStatus) {
                return !r2.isWithinExecutionGuaranteeTime();
            }
        }
        Slog.wtf(TAG, "Couldn't find long running job on a context");
        this.mRunningJobs.remove(jobStatus);
        return false;
    }

    @GuardedBy({"mLock"})
    private boolean isSimilarJobRunningLocked(JobStatus jobStatus) {
        for (int size = this.mRunningJobs.size() - 1; size >= 0; size--) {
            JobStatus valueAt = this.mRunningJobs.valueAt(size);
            if (jobStatus.matches(valueAt.getUid(), valueAt.getNamespace(), valueAt.getJobId())) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private boolean refreshSystemStateLocked() {
        long millis = JobSchedulerService.sUptimeMillisClock.millis();
        if (millis < this.mNextSystemStateRefreshTime) {
            return false;
        }
        long time = this.mStatLogger.getTime();
        this.mNextSystemStateRefreshTime = millis + 1000;
        this.mLastMemoryTrimLevel = 0;
        try {
            this.mLastMemoryTrimLevel = ActivityManager.getService().getMemoryTrimLevel();
        } catch (RemoteException unused) {
        }
        this.mStatLogger.logDurationStat(1, time);
        return true;
    }

    @GuardedBy({"mLock"})
    private void updateCounterConfigLocked() {
        if (refreshSystemStateLocked()) {
            WorkConfigLimitsPerMemoryTrimLevel workConfigLimitsPerMemoryTrimLevel = this.mEffectiveInteractiveState ? CONFIG_LIMITS_SCREEN_ON : CONFIG_LIMITS_SCREEN_OFF;
            int i = this.mLastMemoryTrimLevel;
            if (i == 1) {
                this.mWorkTypeConfig = workConfigLimitsPerMemoryTrimLevel.moderate;
            } else if (i == 2) {
                this.mWorkTypeConfig = workConfigLimitsPerMemoryTrimLevel.low;
            } else if (i == 3) {
                this.mWorkTypeConfig = workConfigLimitsPerMemoryTrimLevel.critical;
            } else {
                this.mWorkTypeConfig = workConfigLimitsPerMemoryTrimLevel.normal;
            }
            this.mWorkCountTracker.setConfig(this.mWorkTypeConfig);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void assignJobsToContextsLocked() {
        long time = this.mStatLogger.getTime();
        assignJobsToContextsInternalLocked();
        this.mStatLogger.logDurationStat(0, time);
    }

    @GuardedBy({"mLock"})
    private void assignJobsToContextsInternalLocked() {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, printPendingQueueLocked());
        }
        if (this.mService.getPendingJobQueue().size() == 0) {
            return;
        }
        prepareForAssignmentDeterminationLocked(this.mRecycledIdle, this.mRecycledPreferredUidOnly, this.mRecycledStoppable, this.mRecycledAssignmentInfo);
        if (z) {
            Slog.d(TAG, printAssignments("running jobs initial", this.mRecycledStoppable, this.mRecycledPreferredUidOnly));
        }
        determineAssignmentsLocked(this.mRecycledChanged, this.mRecycledIdle, this.mRecycledPreferredUidOnly, this.mRecycledStoppable, this.mRecycledAssignmentInfo);
        if (z) {
            Slog.d(TAG, printAssignments("running jobs final", this.mRecycledStoppable, this.mRecycledPreferredUidOnly, this.mRecycledChanged));
            Slog.d(TAG, "work count results: " + this.mWorkCountTracker);
        }
        carryOutAssignmentChangesLocked(this.mRecycledChanged);
        cleanUpAfterAssignmentChangesLocked(this.mRecycledChanged, this.mRecycledIdle, this.mRecycledPreferredUidOnly, this.mRecycledStoppable, this.mRecycledAssignmentInfo, this.mRecycledPrivilegedState);
        noteConcurrency(true);
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void prepareForAssignmentDeterminationLocked(ArraySet<ContextAssignment> arraySet, List<ContextAssignment> list, List<ContextAssignment> list2, AssignmentInfo assignmentInfo) {
        JobServiceContext createNewJobServiceContext;
        int i;
        PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        List<JobServiceContext> list3 = this.mActiveServices;
        updateCounterConfigLocked();
        this.mWorkCountTracker.resetCounts();
        int i2 = 1;
        updateNonRunningPrioritiesLocked(pendingJobQueue, true);
        int size = list3.size();
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        int i3 = 0;
        long j = JobStatus.NO_LATEST_RUNTIME;
        while (i3 < size) {
            JobServiceContext jobServiceContext = list3.get(i3);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            ContextAssignment contextAssignment = (ContextAssignment) this.mContextAssignmentPool.acquire();
            if (contextAssignment == null) {
                contextAssignment = new ContextAssignment();
            }
            contextAssignment.context = jobServiceContext;
            if (runningJobLocked != null) {
                this.mWorkCountTracker.incrementRunningJobCount(jobServiceContext.getRunningJobWorkType());
                contextAssignment.workType = jobServiceContext.getRunningJobWorkType();
                if (runningJobLocked.startedWithImmediacyPrivilege) {
                    i = 1;
                    assignmentInfo.numRunningImmediacyPrivileged++;
                } else {
                    i = 1;
                }
                if (runningJobLocked.shouldTreatAsUserInitiatedJob()) {
                    assignmentInfo.numRunningUi += i;
                } else if (runningJobLocked.startedAsExpeditedJob) {
                    assignmentInfo.numRunningEj += i;
                } else {
                    assignmentInfo.numRunningReg += i;
                }
            } else {
                i = i2;
            }
            contextAssignment.preferredUid = jobServiceContext.getPreferredUid();
            String shouldStopRunningJobLocked = shouldStopRunningJobLocked(jobServiceContext);
            contextAssignment.shouldStopJobReason = shouldStopRunningJobLocked;
            if (shouldStopRunningJobLocked != null) {
                list2.add(contextAssignment);
            } else {
                long remainingGuaranteedTimeMs = jobServiceContext.getRemainingGuaranteedTimeMs(millis);
                contextAssignment.timeUntilStoppableMs = remainingGuaranteedTimeMs;
                j = Math.min(j, remainingGuaranteedTimeMs);
                list.add(contextAssignment);
            }
            i3++;
            i2 = i;
        }
        Comparator<ContextAssignment> comparator = sDeterminationComparator;
        list.sort(comparator);
        list2.sort(comparator);
        while (size < this.mSteadyStateConcurrencyLimit) {
            int size2 = this.mIdleContexts.size();
            if (size2 > 0) {
                createNewJobServiceContext = this.mIdleContexts.removeAt(size2 - 1);
            } else {
                Slog.w(TAG, "Had fewer than " + this.mSteadyStateConcurrencyLimit + " in existence");
                createNewJobServiceContext = createNewJobServiceContext();
            }
            ContextAssignment contextAssignment2 = (ContextAssignment) this.mContextAssignmentPool.acquire();
            if (contextAssignment2 == null) {
                contextAssignment2 = new ContextAssignment();
            }
            contextAssignment2.context = createNewJobServiceContext;
            arraySet.add(contextAssignment2);
            size++;
        }
        this.mWorkCountTracker.onCountDone();
        if (j == JobStatus.NO_LATEST_RUNTIME) {
            j = 0;
        }
        assignmentInfo.minPreferredUidOnlyWaitingTimeMs = j;
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x0152, code lost:
    
        if (r11 >= r30.mMaxWaitUIMs) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0154, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0216  */
    @GuardedBy({"mLock"})
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void determineAssignmentsLocked(ArraySet<ContextAssignment> arraySet, ArraySet<ContextAssignment> arraySet2, List<ContextAssignment> list, List<ContextAssignment> list2, AssignmentInfo assignmentInfo) {
        boolean z;
        PendingJobQueue pendingJobQueue;
        int i;
        String str;
        boolean z2;
        ContextAssignment contextAssignment;
        boolean z3;
        ContextAssignment contextAssignment2;
        boolean z4;
        long j;
        ArraySet<ContextAssignment> arraySet3;
        int i2;
        boolean z5;
        JobServiceContext createNewJobServiceContext;
        long j2;
        ContextAssignment contextAssignment3;
        JobServiceContext createNewJobServiceContext2;
        int i3;
        int size;
        ContextAssignment contextAssignment4;
        boolean z6;
        int canJobStart;
        boolean z7;
        ArraySet<ContextAssignment> arraySet4 = arraySet2;
        List<ContextAssignment> list3 = list2;
        AssignmentInfo assignmentInfo2 = assignmentInfo;
        PendingJobQueue pendingJobQueue2 = this.mService.getPendingJobQueue();
        List<JobServiceContext> list4 = this.mActiveServices;
        pendingJobQueue2.resetIterator();
        int size2 = list4.size();
        boolean z8 = assignmentInfo2.numRunningUi == 0;
        boolean z9 = assignmentInfo2.numRunningEj == 0;
        boolean z10 = assignmentInfo2.numRunningReg == 0;
        long j3 = JobStatus.NO_LATEST_RUNTIME;
        while (true) {
            JobStatus next = pendingJobQueue2.next();
            if (next == null) {
                return;
            }
            if (this.mRunningJobs.contains(next)) {
                Slog.wtf(TAG, "Pending queue contained a running job");
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    z = z10;
                    sb.append("Pending+running job: ");
                    sb.append(next);
                    Slog.e(TAG, sb.toString());
                } else {
                    z = z10;
                }
                pendingJobQueue2.remove(next);
                z10 = z;
            } else {
                boolean z11 = z10;
                boolean hasImmediacyPrivilegeLocked = hasImmediacyPrivilegeLocked(next, this.mRecycledPrivilegedState);
                if (DEBUG && isSimilarJobRunningLocked(next)) {
                    StringBuilder sb2 = new StringBuilder();
                    pendingJobQueue = pendingJobQueue2;
                    sb2.append("Already running similar job to: ");
                    sb2.append(next);
                    Slog.w(TAG, sb2.toString());
                } else {
                    pendingJobQueue = pendingJobQueue2;
                }
                boolean z12 = z9;
                long min = Math.min(assignmentInfo2.minPreferredUidOnlyWaitingTimeMs, j3);
                long j4 = j3;
                int jobWorkTypes = getJobWorkTypes(next);
                boolean z13 = !isPkgConcurrencyLimitedLocked(next);
                boolean z14 = size2 > this.mSteadyStateConcurrencyLimit;
                boolean z15 = z8;
                if (arraySet2.size() > 0) {
                    int size3 = arraySet2.size() - 1;
                    ContextAssignment valueAt = arraySet4.valueAt(size3);
                    str = TAG;
                    contextAssignment = valueAt;
                    i = size2;
                    boolean z16 = contextAssignment.preferredUid == next.getUid() || contextAssignment.preferredUid == -1;
                    int canJobStart2 = this.mWorkCountTracker.canJobStart(jobWorkTypes);
                    if (z16 && z13 && canJobStart2 != 0) {
                        arraySet4.removeAt(size3);
                        contextAssignment.newJob = next;
                        contextAssignment.newWorkType = canJobStart2;
                        z2 = true;
                        if (contextAssignment == null && list2.size() > 0) {
                            size = list2.size() - 1;
                            while (size >= 0) {
                                ContextAssignment contextAssignment5 = list3.get(size);
                                JobStatus runningJobLocked = contextAssignment5.context.getRunningJobLocked();
                                if (hasImmediacyPrivilegeLocked || z14) {
                                    contextAssignment4 = contextAssignment;
                                    z6 = hasImmediacyPrivilegeLocked;
                                } else {
                                    contextAssignment4 = contextAssignment;
                                    z6 = runningJobLocked.lastEvaluatedBias < 40 || this.mService.evaluateJobBiasLocked(runningJobLocked) < 40 || assignmentInfo2.numRunningImmediacyPrivileged > this.mWorkTypeConfig.getMaxTotal() / 2;
                                }
                                if (z6 || !this.mMaxWaitTimeBypassEnabled) {
                                    z3 = z2;
                                } else {
                                    if (next.shouldTreatAsUserInitiatedJob()) {
                                        z3 = z2;
                                    } else {
                                        z3 = z2;
                                        z7 = !next.shouldTreatAsExpeditedJob() ? false : false;
                                    }
                                    z6 = z7;
                                }
                                if (z6 && (canJobStart = this.mWorkCountTracker.canJobStart(jobWorkTypes, contextAssignment5.context.getRunningJobWorkType())) != 0) {
                                    contextAssignment5.preemptReason = contextAssignment5.shouldStopJobReason;
                                    contextAssignment5.preemptReasonCode = 4;
                                    list3.remove(size);
                                    contextAssignment5.newJob = next;
                                    contextAssignment5.newWorkType = canJobStart;
                                    contextAssignment2 = contextAssignment5;
                                    break;
                                }
                                size--;
                                z2 = z3;
                                contextAssignment = contextAssignment4;
                            }
                        }
                        z3 = z2;
                        contextAssignment2 = contextAssignment;
                        if (contextAssignment2 == null || (z14 && !hasImmediacyPrivilegeLocked)) {
                            z4 = z3;
                            j = min;
                        } else {
                            int size4 = list.size() - 1;
                            int i4 = Integer.MAX_VALUE;
                            boolean z17 = z3;
                            long j5 = JobStatus.NO_LATEST_RUNTIME;
                            while (size4 >= 0) {
                                ContextAssignment contextAssignment6 = list.get(size4);
                                JobStatus runningJobLocked2 = contextAssignment6.context.getRunningJobLocked();
                                boolean z18 = z17;
                                long j6 = min;
                                if (runningJobLocked2.getUid() == next.getUid() && (i3 = this.mService.evaluateJobBiasLocked(runningJobLocked2)) < next.lastEvaluatedBias) {
                                    if (contextAssignment2 == null || i4 > i3) {
                                        if (contextAssignment2 != null) {
                                            j5 = Math.min(j5, contextAssignment2.timeUntilStoppableMs);
                                        }
                                        contextAssignment6.preemptReason = "higher bias job found";
                                        contextAssignment6.preemptReasonCode = 2;
                                        contextAssignment2 = contextAssignment6;
                                        size4--;
                                        i4 = i3;
                                        z17 = z18;
                                        min = j6;
                                    } else {
                                        j5 = Math.min(j5, contextAssignment6.timeUntilStoppableMs);
                                    }
                                }
                                i3 = i4;
                                size4--;
                                i4 = i3;
                                z17 = z18;
                                min = j6;
                            }
                            z4 = z17;
                            j = min;
                            if (contextAssignment2 != null) {
                                contextAssignment2.newJob = next;
                                list.remove(contextAssignment2);
                                assignmentInfo2.minPreferredUidOnlyWaitingTimeMs = j5;
                            }
                        }
                        if (!hasImmediacyPrivilegeLocked) {
                            if (contextAssignment2 == null || contextAssignment2.context.getRunningJobLocked() == null) {
                                arraySet3 = arraySet;
                                contextAssignment3 = contextAssignment2;
                                i2 = i;
                            } else {
                                arraySet3 = arraySet;
                                arraySet3.add(contextAssignment2);
                                i2 = i - 1;
                                contextAssignment2.newJob = null;
                                contextAssignment2.newWorkType = 0;
                                contextAssignment3 = null;
                            }
                            if (contextAssignment3 == null) {
                                if (DEBUG) {
                                    Slog.d(str, "Allowing additional context because EJ would wait too long");
                                }
                                ContextAssignment contextAssignment7 = (ContextAssignment) this.mContextAssignmentPool.acquire();
                                if (contextAssignment7 == null) {
                                    contextAssignment7 = new ContextAssignment();
                                }
                                contextAssignment2 = contextAssignment7;
                                if (this.mIdleContexts.size() > 0) {
                                    ArraySet<JobServiceContext> arraySet5 = this.mIdleContexts;
                                    createNewJobServiceContext2 = arraySet5.removeAt(arraySet5.size() - 1);
                                } else {
                                    createNewJobServiceContext2 = createNewJobServiceContext();
                                }
                                contextAssignment2.context = createNewJobServiceContext2;
                                contextAssignment2.newJob = next;
                                int canJobStart3 = this.mWorkCountTracker.canJobStart(jobWorkTypes);
                                if (canJobStart3 == 0) {
                                    canJobStart3 = 1;
                                }
                                contextAssignment2.newWorkType = canJobStart3;
                            } else {
                                contextAssignment2 = contextAssignment3;
                            }
                            z10 = z11;
                            z9 = z12;
                        } else {
                            arraySet3 = arraySet;
                            String str2 = str;
                            if (contextAssignment2 == null && this.mMaxWaitTimeBypassEnabled) {
                                if (next.shouldTreatAsUserInitiatedJob() && z15) {
                                    z5 = j >= this.mMaxWaitUIMs;
                                    z8 = !z5;
                                    z10 = z11;
                                    z9 = z12;
                                } else {
                                    if (next.shouldTreatAsExpeditedJob() && z12) {
                                        z5 = j >= this.mMaxWaitEjMs;
                                        z9 = !z5;
                                        z10 = z11;
                                    } else {
                                        if (z11) {
                                            z5 = j >= this.mMaxWaitRegularMs;
                                            z10 = !z5;
                                        } else {
                                            z5 = false;
                                            z10 = z11;
                                        }
                                        z9 = z12;
                                    }
                                    z8 = z15;
                                }
                                if (z5) {
                                    if (DEBUG) {
                                        Slog.d(str2, "Allowing additional context because job would wait too long");
                                    }
                                    ContextAssignment contextAssignment8 = (ContextAssignment) this.mContextAssignmentPool.acquire();
                                    if (contextAssignment8 == null) {
                                        contextAssignment8 = new ContextAssignment();
                                    }
                                    contextAssignment2 = contextAssignment8;
                                    if (this.mIdleContexts.size() > 0) {
                                        ArraySet<JobServiceContext> arraySet6 = this.mIdleContexts;
                                        createNewJobServiceContext = arraySet6.removeAt(arraySet6.size() - 1);
                                    } else {
                                        createNewJobServiceContext = createNewJobServiceContext();
                                    }
                                    contextAssignment2.context = createNewJobServiceContext;
                                    contextAssignment2.newJob = next;
                                    int canJobStart4 = this.mWorkCountTracker.canJobStart(jobWorkTypes);
                                    if (canJobStart4 != 0) {
                                        contextAssignment2.newWorkType = canJobStart4;
                                    } else {
                                        int i5 = 1;
                                        while (true) {
                                            if (i5 > 127) {
                                                break;
                                            }
                                            if ((i5 & jobWorkTypes) != 0) {
                                                contextAssignment2.newWorkType = i5;
                                                break;
                                            }
                                            i5 <<= 1;
                                        }
                                    }
                                }
                                i2 = i;
                                PackageStats pkgStatsLocked = getPkgStatsLocked(next.getSourceUserId(), next.getSourcePackageName());
                                if (contextAssignment2 != null) {
                                    arraySet3.add(contextAssignment2);
                                    if (contextAssignment2.context.getRunningJobLocked() != null) {
                                        i2--;
                                    }
                                    JobStatus jobStatus = contextAssignment2.newJob;
                                    if (jobStatus != null) {
                                        jobStatus.startedWithImmediacyPrivilege = hasImmediacyPrivilegeLocked;
                                        j2 = Math.min(j4, this.mService.getMinJobExecutionGuaranteeMs(jobStatus));
                                        size2 = i2 + 1;
                                    } else {
                                        size2 = i2;
                                        j2 = j4;
                                    }
                                    pkgStatsLocked.adjustStagedCount(true, next.shouldTreatAsExpeditedJob());
                                } else {
                                    j2 = j4;
                                    size2 = i2;
                                }
                                if (z4) {
                                    this.mWorkCountTracker.stageJob(contextAssignment2.newWorkType, jobWorkTypes);
                                    this.mActivePkgStats.add(next.getSourceUserId(), next.getSourcePackageName(), pkgStatsLocked);
                                }
                                arraySet4 = arraySet2;
                                j3 = j2;
                                pendingJobQueue2 = pendingJobQueue;
                                list3 = list2;
                                assignmentInfo2 = assignmentInfo;
                            } else {
                                z10 = z11;
                                z9 = z12;
                                i2 = i;
                            }
                        }
                        z8 = z15;
                        PackageStats pkgStatsLocked2 = getPkgStatsLocked(next.getSourceUserId(), next.getSourcePackageName());
                        if (contextAssignment2 != null) {
                        }
                        if (z4) {
                        }
                        arraySet4 = arraySet2;
                        j3 = j2;
                        pendingJobQueue2 = pendingJobQueue;
                        list3 = list2;
                        assignmentInfo2 = assignmentInfo;
                    }
                } else {
                    i = size2;
                    str = TAG;
                }
                z2 = false;
                contextAssignment = null;
                if (contextAssignment == null) {
                    size = list2.size() - 1;
                    while (size >= 0) {
                    }
                }
                z3 = z2;
                contextAssignment2 = contextAssignment;
                if (contextAssignment2 == null) {
                }
                z4 = z3;
                j = min;
                if (!hasImmediacyPrivilegeLocked) {
                }
                z8 = z15;
                PackageStats pkgStatsLocked22 = getPkgStatsLocked(next.getSourceUserId(), next.getSourcePackageName());
                if (contextAssignment2 != null) {
                }
                if (z4) {
                }
                arraySet4 = arraySet2;
                j3 = j2;
                pendingJobQueue2 = pendingJobQueue;
                list3 = list2;
                assignmentInfo2 = assignmentInfo;
            }
        }
    }

    @GuardedBy({"mLock"})
    private void carryOutAssignmentChangesLocked(ArraySet<ContextAssignment> arraySet) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            ContextAssignment valueAt = arraySet.valueAt(size);
            JobStatus runningJobLocked = valueAt.context.getRunningJobLocked();
            if (runningJobLocked != null) {
                if (DEBUG) {
                    Slog.d(TAG, "preempting job: " + runningJobLocked);
                }
                valueAt.context.cancelExecutingJobLocked(valueAt.preemptReasonCode, 2, valueAt.preemptReason);
            } else {
                JobStatus jobStatus = valueAt.newJob;
                if (DEBUG) {
                    Slog.d(TAG, "About to run job on context " + valueAt.context.getId() + ", job: " + jobStatus);
                }
                startJobLocked(valueAt.context, jobStatus, valueAt.newWorkType);
            }
            valueAt.clear();
            this.mContextAssignmentPool.release(valueAt);
        }
    }

    @GuardedBy({"mLock"})
    private void cleanUpAfterAssignmentChangesLocked(ArraySet<ContextAssignment> arraySet, ArraySet<ContextAssignment> arraySet2, List<ContextAssignment> list, List<ContextAssignment> list2, AssignmentInfo assignmentInfo, SparseIntArray sparseIntArray) {
        for (int size = list2.size() - 1; size >= 0; size--) {
            ContextAssignment contextAssignment = list2.get(size);
            contextAssignment.clear();
            this.mContextAssignmentPool.release(contextAssignment);
        }
        for (int size2 = list.size() - 1; size2 >= 0; size2--) {
            ContextAssignment contextAssignment2 = list.get(size2);
            contextAssignment2.clear();
            this.mContextAssignmentPool.release(contextAssignment2);
        }
        for (int size3 = arraySet2.size() - 1; size3 >= 0; size3--) {
            ContextAssignment valueAt = arraySet2.valueAt(size3);
            this.mIdleContexts.add(valueAt.context);
            valueAt.clear();
            this.mContextAssignmentPool.release(valueAt);
        }
        arraySet.clear();
        arraySet2.clear();
        list2.clear();
        list.clear();
        assignmentInfo.clear();
        sparseIntArray.clear();
        this.mWorkCountTracker.resetStagingCount();
        this.mActivePkgStats.forEach(this.mPackageStatsStagingCountClearer);
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    boolean hasImmediacyPrivilegeLocked(JobStatus jobStatus, SparseIntArray sparseIntArray) {
        if (!jobStatus.shouldTreatAsExpeditedJob() && !jobStatus.shouldTreatAsUserInitiatedJob()) {
            return false;
        }
        if (jobStatus.lastEvaluatedBias == 40) {
            return true;
        }
        int sourceUid = jobStatus.getSourceUid();
        int i = sparseIntArray.get(sourceUid, 0);
        if (i == 1) {
            return false;
        }
        if (i == 2) {
            return jobStatus.shouldTreatAsUserInitiatedJob();
        }
        if (i == 3) {
            return true;
        }
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        if (activityManagerInternal.getUidProcessState(sourceUid) == 2) {
            sparseIntArray.put(sourceUid, 3);
            return true;
        }
        if (jobStatus.shouldTreatAsExpeditedJob()) {
            return false;
        }
        BackgroundStartPrivileges backgroundStartPrivileges = activityManagerInternal.getBackgroundStartPrivileges(sourceUid);
        if (DEBUG) {
            Slog.d(TAG, "Job " + jobStatus.toShortString() + " bsp state: " + backgroundStartPrivileges);
        }
        boolean allowsBackgroundActivityStarts = backgroundStartPrivileges.allowsBackgroundActivityStarts();
        sparseIntArray.put(sourceUid, allowsBackgroundActivityStarts ? 2 : 1);
        return allowsBackgroundActivityStarts;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onUidBiasChangedLocked(int i, int i2) {
        if ((i == 40 || i2 == 40) && this.mService.getPendingJobQueue().size() != 0) {
            assignJobsToContextsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public JobServiceContext getRunningJobServiceContextLocked(JobStatus jobStatus) {
        if (!this.mRunningJobs.contains(jobStatus)) {
            return null;
        }
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i);
            if (jobServiceContext.getRunningJobLocked() == jobStatus) {
                return jobServiceContext;
            }
        }
        Slog.wtf(TAG, "Couldn't find running job on a context");
        this.mRunningJobs.remove(jobStatus);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean stopJobOnServiceContextLocked(JobStatus jobStatus, int i, int i2, String str) {
        if (!this.mRunningJobs.contains(jobStatus)) {
            return false;
        }
        for (int i3 = 0; i3 < this.mActiveServices.size(); i3++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i3);
            if (jobServiceContext.getRunningJobLocked() == jobStatus) {
                jobServiceContext.cancelExecutingJobLocked(i, i2, str);
                return true;
            }
        }
        Slog.wtf(TAG, "Couldn't find running job on a context");
        this.mRunningJobs.remove(jobStatus);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void stopUnexemptedJobsForDoze() {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked != null && !runningJobLocked.canRunInDoze()) {
                jobServiceContext.cancelExecutingJobLocked(4, 4, "cancelled due to doze");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void stopOvertimeJobsLocked(String str) {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i);
            if (jobServiceContext.getRunningJobLocked() != null && !jobServiceContext.isWithinExecutionGuaranteeTime()) {
                jobServiceContext.cancelExecutingJobLocked(4, 3, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void maybeStopOvertimeJobsLocked(JobRestriction jobRestriction) {
        for (int size = this.mActiveServices.size() - 1; size >= 0; size--) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(size);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked != null && !jobServiceContext.isWithinExecutionGuaranteeTime() && jobRestriction.isJobRestricted(runningJobLocked)) {
                jobServiceContext.cancelExecutingJobLocked(jobRestriction.getStopReason(), jobRestriction.getInternalReason(), JobParameters.getInternalReasonCodeDescription(jobRestriction.getInternalReason()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void markJobsForUserStopLocked(int i, String str, String str2) {
        for (int size = this.mActiveServices.size() - 1; size >= 0; size--) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(size);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked != null && i == runningJobLocked.getUserId() && runningJobLocked.getServiceComponent().getPackageName().equals(str)) {
                jobServiceContext.markForProcessDeathLocked(13, 11, str2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void stopNonReadyActiveJobsLocked() {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked != null) {
                if (!runningJobLocked.isReady()) {
                    if (runningJobLocked.getEffectiveStandbyBucket() == 5 && runningJobLocked.getStopReason() == 12) {
                        jobServiceContext.cancelExecutingJobLocked(runningJobLocked.getStopReason(), 6, "cancelled due to restricted bucket");
                    } else {
                        jobServiceContext.cancelExecutingJobLocked(runningJobLocked.getStopReason(), 1, "cancelled due to unsatisfied constraints");
                    }
                } else {
                    JobRestriction checkIfRestricted = this.mService.checkIfRestricted(runningJobLocked);
                    if (checkIfRestricted != null) {
                        int internalReason = checkIfRestricted.getInternalReason();
                        jobServiceContext.cancelExecutingJobLocked(checkIfRestricted.getStopReason(), internalReason, "restricted due to " + JobParameters.getInternalReasonCodeDescription(internalReason));
                    }
                }
            }
        }
    }

    private void noteConcurrency(boolean z) {
        this.mService.mJobPackageTracker.noteConcurrency(this.mRunningJobs.size(), this.mWorkCountTracker.getRunningJobCount(1));
        if (z) {
            sConcurrencyHistogramLogger.logSample(this.mActiveServices.size());
        }
    }

    @GuardedBy({"mLock"})
    private void updateNonRunningPrioritiesLocked(PendingJobQueue pendingJobQueue, boolean z) {
        pendingJobQueue.resetIterator();
        while (true) {
            JobStatus next = pendingJobQueue.next();
            if (next == null) {
                return;
            }
            if (!this.mRunningJobs.contains(next)) {
                next.lastEvaluatedBias = this.mService.evaluateJobBiasLocked(next);
                if (z) {
                    this.mWorkCountTracker.incrementPendingJobCount(getJobWorkTypes(next));
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private PackageStats getPkgStatsLocked(int i, String str) {
        PackageStats packageStats = (PackageStats) this.mActivePkgStats.get(i, str);
        if (packageStats != null) {
            return packageStats;
        }
        PackageStats packageStats2 = (PackageStats) this.mPkgStatsPool.acquire();
        if (packageStats2 == null) {
            packageStats2 = new PackageStats();
        }
        PackageStats packageStats3 = packageStats2;
        packageStats3.setPackage(i, str);
        return packageStats3;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    boolean isPkgConcurrencyLimitedLocked(JobStatus jobStatus) {
        PackageStats packageStats;
        if (jobStatus.lastEvaluatedBias < 40 && this.mService.getPendingJobQueue().size() + this.mRunningJobs.size() >= this.mWorkTypeConfig.getMaxTotal() && (packageStats = (PackageStats) this.mActivePkgStats.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName())) != null) {
            return jobStatus.shouldTreatAsExpeditedJob() ? packageStats.numRunningEj + packageStats.numStagedEj >= this.mPkgConcurrencyLimitEj : packageStats.numRunningRegular + packageStats.numStagedRegular >= this.mPkgConcurrencyLimitRegular;
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private void startJobLocked(JobServiceContext jobServiceContext, JobStatus jobStatus, int i) {
        List<StateController> list = this.mService.mControllers;
        int size = list.size();
        PowerManager.WakeLock newWakeLock = this.mPowerManager.newWakeLock(1, jobStatus.getTag());
        newWakeLock.setWorkSource(this.mService.deriveWorkSource(jobStatus.getSourceUid(), jobStatus.getSourcePackageName()));
        newWakeLock.setReferenceCounted(false);
        newWakeLock.acquire();
        for (int i2 = 0; i2 < size; i2++) {
            try {
                list.get(i2).prepareForExecutionLocked(jobStatus);
            } finally {
                newWakeLock.release();
            }
        }
        PackageStats pkgStatsLocked = getPkgStatsLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        pkgStatsLocked.adjustStagedCount(false, jobStatus.shouldTreatAsExpeditedJob());
        if (!jobServiceContext.executeRunnableJob(jobStatus, i)) {
            Slog.e(TAG, "Error executing " + jobStatus);
            this.mJobConcurrencyManagerExt.hookStartJobErrorExecute(jobStatus, i);
            this.mWorkCountTracker.onStagedJobFailed(i);
            for (int i3 = 0; i3 < size; i3++) {
                list.get(i3).unprepareFromExecutionLocked(jobStatus);
            }
        } else {
            this.mRunningJobs.add(jobStatus);
            this.mActiveServices.add(jobServiceContext);
            this.mIdleContexts.remove(jobServiceContext);
            this.mWorkCountTracker.onJobStarted(i);
            pkgStatsLocked.adjustRunningCount(true, jobStatus.shouldTreatAsExpeditedJob());
            this.mActivePkgStats.add(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), pkgStatsLocked);
            this.mService.resetPendingJobReasonCache(jobStatus);
        }
        if (this.mService.getPendingJobQueue().remove(jobStatus)) {
            this.mService.mJobPackageTracker.noteNonpending(jobStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onJobCompletedLocked(JobServiceContext jobServiceContext, JobStatus jobStatus, int i) {
        String str;
        int jobWorkTypes;
        int canJobStart;
        String str2;
        int jobWorkTypes2;
        int canJobStart2;
        this.mWorkCountTracker.onJobFinished(i);
        this.mRunningJobs.remove(jobStatus);
        this.mActiveServices.remove(jobServiceContext);
        boolean z = true;
        if (this.mIdleContexts.size() < 96) {
            this.mIdleContexts.add(jobServiceContext);
        } else {
            this.mNumDroppedContexts++;
        }
        PackageStats packageStats = (PackageStats) this.mActivePkgStats.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        if (packageStats == null) {
            Slog.wtf(TAG, "Running job didn't have an active PackageStats object");
        } else {
            packageStats.adjustRunningCount(false, jobStatus.startedAsExpeditedJob);
            if (packageStats.numRunningEj <= 0 && packageStats.numRunningRegular <= 0) {
                this.mActivePkgStats.delete(packageStats.userId, packageStats.packageName);
                this.mPkgStatsPool.release(packageStats);
            }
        }
        PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        if (pendingJobQueue.size() == 0) {
            jobServiceContext.clearPreferredUid();
            noteConcurrency(false);
            return;
        }
        if (this.mActiveServices.size() >= this.mSteadyStateConcurrencyLimit) {
            if (this.mMaxWaitTimeBypassEnabled) {
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                long j = JobStatus.NO_LATEST_RUNTIME;
                for (int size = this.mActiveServices.size() - 1; size >= 0; size--) {
                    j = Math.min(j, this.mActiveServices.get(size).getRemainingGuaranteedTimeMs(millis));
                }
                z = true ^ (this.mWorkCountTracker.getPendingJobCount(4) <= 0 ? !(this.mWorkCountTracker.getPendingJobCount(8) <= 0 ? j < this.mMaxWaitRegularMs : j < this.mMaxWaitEjMs) : j >= this.mMaxWaitUIMs);
            }
            if (z) {
                jobServiceContext.clearPreferredUid();
                noteConcurrency(false);
                return;
            }
        }
        String str3 = "Already running similar job to: ";
        JobStatus jobStatus2 = null;
        if (jobServiceContext.getPreferredUid() != -1) {
            updateCounterConfigLocked();
            updateNonRunningPrioritiesLocked(pendingJobQueue, false);
            pendingJobQueue.resetIterator();
            int i2 = i;
            int i3 = i2;
            int i4 = 0;
            int i5 = 0;
            JobStatus jobStatus3 = null;
            while (true) {
                JobStatus next = pendingJobQueue.next();
                if (next == null) {
                    break;
                }
                if (this.mRunningJobs.contains(next)) {
                    Slog.wtf(TAG, "Pending queue contained a running job");
                    if (DEBUG) {
                        Slog.e(TAG, "Pending+running job: " + next);
                    }
                    pendingJobQueue.remove(next);
                    str2 = str3;
                } else {
                    if (DEBUG && isSimilarJobRunningLocked(next)) {
                        Slog.w(TAG, str3 + next);
                    }
                    str2 = str3;
                    if (jobServiceContext.getPreferredUid() != next.getUid()) {
                        if (jobStatus3 == null && !isPkgConcurrencyLimitedLocked(next) && (canJobStart2 = this.mWorkCountTracker.canJobStart((jobWorkTypes2 = getJobWorkTypes(next)))) != 0) {
                            i5 = jobWorkTypes2;
                            jobStatus3 = next;
                            i4 = canJobStart2;
                        }
                    } else if ((next.lastEvaluatedBias > jobStatus.lastEvaluatedBias || !isPkgConcurrencyLimitedLocked(next)) && (jobStatus2 == null || jobStatus2.lastEvaluatedBias < next.lastEvaluatedBias)) {
                        i3 = getJobWorkTypes(next);
                        int canJobStart3 = this.mWorkCountTracker.canJobStart(i3);
                        i2 = canJobStart3 == 0 ? i : canJobStart3;
                        jobStatus2 = next;
                        str3 = str2;
                    }
                }
                str3 = str2;
            }
            if (jobStatus2 != null) {
                if (DEBUG) {
                    Slog.d(TAG, "Running job " + jobStatus2 + " as preemption");
                }
                this.mWorkCountTracker.stageJob(i2, i3);
                startJobLocked(jobServiceContext, jobStatus2, i2);
            } else {
                boolean z2 = DEBUG;
                if (z2) {
                    Slog.d(TAG, "Couldn't find preemption job for uid " + jobServiceContext.getPreferredUid());
                }
                jobServiceContext.clearPreferredUid();
                if (jobStatus3 != null) {
                    if (z2) {
                        Slog.d(TAG, "Running job " + jobStatus3 + " instead");
                    }
                    this.mWorkCountTracker.stageJob(i4, i5);
                    startJobLocked(jobServiceContext, jobStatus3, i4);
                }
            }
        } else {
            String str4 = "Already running similar job to: ";
            if (pendingJobQueue.size() > 0) {
                updateCounterConfigLocked();
                updateNonRunningPrioritiesLocked(pendingJobQueue, false);
                pendingJobQueue.resetIterator();
                int i6 = i;
                int i7 = i6;
                while (true) {
                    JobStatus next2 = pendingJobQueue.next();
                    if (next2 == null) {
                        break;
                    }
                    if (this.mRunningJobs.contains(next2)) {
                        Slog.wtf(TAG, "Pending queue contained a running job");
                        if (DEBUG) {
                            Slog.e(TAG, "Pending+running job: " + next2);
                        }
                        pendingJobQueue.remove(next2);
                        str = str4;
                    } else {
                        if (DEBUG && isSimilarJobRunningLocked(next2)) {
                            StringBuilder sb = new StringBuilder();
                            str = str4;
                            sb.append(str);
                            sb.append(next2);
                            Slog.w(TAG, sb.toString());
                        } else {
                            str = str4;
                        }
                        if (!isPkgConcurrencyLimitedLocked(next2) && (canJobStart = this.mWorkCountTracker.canJobStart((jobWorkTypes = getJobWorkTypes(next2)))) != 0 && (jobStatus2 == null || jobStatus2.lastEvaluatedBias < next2.lastEvaluatedBias)) {
                            jobStatus2 = next2;
                            i7 = jobWorkTypes;
                            i6 = canJobStart;
                        }
                    }
                    str4 = str;
                }
                if (jobStatus2 != null) {
                    if (DEBUG) {
                        Slog.d(TAG, "About to run job: " + jobStatus2);
                    }
                    this.mWorkCountTracker.stageJob(i6, i7);
                    startJobLocked(jobServiceContext, jobStatus2, i6);
                }
            }
        }
        noteConcurrency(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public String shouldStopRunningJobLocked(JobServiceContext jobServiceContext) {
        JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
        if (runningJobLocked == null || jobServiceContext.isWithinExecutionGuaranteeTime()) {
            return null;
        }
        if (this.mPowerManager.isPowerSaveMode()) {
            return "battery saver";
        }
        if (this.mPowerManager.isDeviceIdleMode()) {
            return "deep doze";
        }
        JobRestriction checkIfRestricted = this.mService.checkIfRestricted(runningJobLocked);
        if (checkIfRestricted != null) {
            return "restriction:" + JobParameters.getInternalReasonCodeDescription(checkIfRestricted.getInternalReason());
        }
        updateCounterConfigLocked();
        int runningJobWorkType = jobServiceContext.getRunningJobWorkType();
        if (this.mRunningJobs.size() > this.mWorkTypeConfig.getMaxTotal() || this.mWorkCountTracker.isOverTypeLimit(runningJobWorkType)) {
            return "too many jobs running";
        }
        PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        if (pendingJobQueue.size() == 0) {
            return null;
        }
        if (!runningJobLocked.shouldTreatAsExpeditedJob() && !runningJobLocked.startedAsExpeditedJob) {
            if (this.mWorkCountTracker.getPendingJobCount(runningJobWorkType) > 0) {
                return "blocking " + workTypeToString(runningJobWorkType) + " queue";
            }
            pendingJobQueue.resetIterator();
            int i = 127;
            do {
                JobStatus next = pendingJobQueue.next();
                if (next == null) {
                    break;
                }
                int jobWorkTypes = getJobWorkTypes(next);
                if ((jobWorkTypes & i) > 0 && this.mWorkCountTracker.canJobStart(jobWorkTypes, runningJobWorkType) != 0) {
                    return "blocking other pending jobs";
                }
                i &= ~jobWorkTypes;
            } while (i != 0);
            return null;
        }
        if (runningJobWorkType == 32 || runningJobWorkType == 64) {
            if (this.mWorkCountTracker.getPendingJobCount(32) > 0) {
                return "blocking " + workTypeToString(32) + " queue";
            }
            if (this.mWorkCountTracker.getPendingJobCount(8) > 0 && this.mWorkCountTracker.canJobStart(8, runningJobWorkType) != 0) {
                return "blocking " + workTypeToString(8) + " queue";
            }
        } else {
            if (this.mWorkCountTracker.getPendingJobCount(8) > 0) {
                return "blocking " + workTypeToString(8) + " queue";
            }
            if (runningJobLocked.startedWithImmediacyPrivilege) {
                int i2 = 0;
                for (int size = this.mRunningJobs.size() - 1; size >= 0; size--) {
                    if (this.mRunningJobs.valueAt(size).startedWithImmediacyPrivilege) {
                        i2++;
                    }
                }
                if (i2 > this.mWorkTypeConfig.getMaxTotal() / 2) {
                    return "prevent immediacy privilege dominance";
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean executeStopCommandLocked(PrintWriter printWriter, String str, int i, String str2, boolean z, int i2, int i3, int i4) {
        boolean z2 = false;
        for (int i5 = 0; i5 < this.mActiveServices.size(); i5++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i5);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (jobServiceContext.stopIfExecutingLocked(str, i, str2, z, i2, i3, i4)) {
                printWriter.print("Stopping job: ");
                runningJobLocked.printUniqueId(printWriter);
                printWriter.print(" ");
                printWriter.println(runningJobLocked.getServiceComponent().flattenToShortString());
                z2 = true;
            }
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public Pair<Long, Long> getEstimatedNetworkBytesLocked(String str, int i, String str2, int i2) {
        for (int i3 = 0; i3 < this.mActiveServices.size(); i3++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i3);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked != null && runningJobLocked.matches(i, str2, i2) && runningJobLocked.getSourcePackageName().equals(str)) {
                return jobServiceContext.getEstimatedNetworkBytes();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public Pair<Long, Long> getTransferredNetworkBytesLocked(String str, int i, String str2, int i2) {
        for (int i3 = 0; i3 < this.mActiveServices.size(); i3++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i3);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked != null && runningJobLocked.matches(i, str2, i2) && runningJobLocked.getSourcePackageName().equals(str)) {
                return jobServiceContext.getTransferredNetworkBytes();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNotificationAssociatedWithAnyUserInitiatedJobs(int i, int i2, String str) {
        return this.mNotificationCoordinator.isNotificationAssociatedWithAnyUserInitiatedJobs(i, i2, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNotificationChannelAssociatedWithAnyUserInitiatedJobs(String str, int i, String str2) {
        return this.mNotificationCoordinator.isNotificationChannelAssociatedWithAnyUserInitiatedJobs(str, i, str2);
    }

    private JobServiceContext createNewJobServiceContext() {
        return this.mInjector.createJobServiceContext(this.mService, this, this.mNotificationCoordinator, IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats")), this.mService.mJobPackageTracker, AppSchedulingModuleThread.get().getLooper());
    }

    @GuardedBy({"mLock"})
    private String printPendingQueueLocked() {
        StringBuilder sb = new StringBuilder("Pending queue: ");
        PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        pendingJobQueue.resetIterator();
        while (true) {
            JobStatus next = pendingJobQueue.next();
            if (next != null) {
                sb.append("(");
                sb.append("{");
                sb.append(next.getNamespace());
                sb.append("} ");
                sb.append(next.getJob().getId());
                sb.append(", ");
                sb.append(next.getUid());
                sb.append(") ");
            } else {
                return sb.toString();
            }
        }
    }

    private static String printAssignments(String str, Collection<ContextAssignment>... collectionArr) {
        StringBuilder sb = new StringBuilder(str + ": ");
        for (int i = 0; i < collectionArr.length; i++) {
            int i2 = 0;
            for (ContextAssignment contextAssignment : collectionArr[i]) {
                JobStatus jobStatus = contextAssignment.newJob;
                if (jobStatus == null) {
                    jobStatus = contextAssignment.context.getRunningJobLocked();
                }
                if (i > 0 || i2 > 0) {
                    sb.append(" ");
                }
                sb.append("(");
                sb.append(contextAssignment.context.getId());
                sb.append("=");
                if (jobStatus == null) {
                    sb.append("nothing");
                } else {
                    if (jobStatus.getNamespace() != null) {
                        sb.append(jobStatus.getNamespace());
                        sb.append(":");
                    }
                    sb.append(jobStatus.getJobId());
                    sb.append(SliceClientPermissions.SliceAuthority.DELIMITER);
                    sb.append(jobStatus.getUid());
                }
                sb.append(")");
                i2++;
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void updateConfigLocked() {
        DeviceConfig.Properties properties = DeviceConfig.getProperties("jobscheduler", new String[0]);
        this.mSteadyStateConcurrencyLimit = Math.max(8, Math.min(64, properties.getInt(KEY_CONCURRENCY_LIMIT, DEFAULT_CONCURRENCY_LIMIT)));
        this.mScreenOffAdjustmentDelayMs = properties.getLong(KEY_SCREEN_OFF_ADJUSTMENT_DELAY_MS, DEFAULT_SCREEN_OFF_ADJUSTMENT_DELAY_MS);
        WorkConfigLimitsPerMemoryTrimLevel workConfigLimitsPerMemoryTrimLevel = CONFIG_LIMITS_SCREEN_ON;
        workConfigLimitsPerMemoryTrimLevel.normal.update(properties, this.mSteadyStateConcurrencyLimit);
        workConfigLimitsPerMemoryTrimLevel.moderate.update(properties, this.mSteadyStateConcurrencyLimit);
        workConfigLimitsPerMemoryTrimLevel.low.update(properties, this.mSteadyStateConcurrencyLimit);
        workConfigLimitsPerMemoryTrimLevel.critical.update(properties, this.mSteadyStateConcurrencyLimit);
        WorkConfigLimitsPerMemoryTrimLevel workConfigLimitsPerMemoryTrimLevel2 = CONFIG_LIMITS_SCREEN_OFF;
        workConfigLimitsPerMemoryTrimLevel2.normal.update(properties, this.mSteadyStateConcurrencyLimit);
        workConfigLimitsPerMemoryTrimLevel2.moderate.update(properties, this.mSteadyStateConcurrencyLimit);
        workConfigLimitsPerMemoryTrimLevel2.low.update(properties, this.mSteadyStateConcurrencyLimit);
        workConfigLimitsPerMemoryTrimLevel2.critical.update(properties, this.mSteadyStateConcurrencyLimit);
        this.mPkgConcurrencyLimitEj = Math.max(1, Math.min(this.mSteadyStateConcurrencyLimit, properties.getInt(KEY_PKG_CONCURRENCY_LIMIT_EJ, 3)));
        this.mPkgConcurrencyLimitRegular = Math.max(1, Math.min(this.mSteadyStateConcurrencyLimit, properties.getInt(KEY_PKG_CONCURRENCY_LIMIT_REGULAR, DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR)));
        this.mMaxWaitTimeBypassEnabled = properties.getBoolean(KEY_ENABLE_MAX_WAIT_TIME_BYPASS, true);
        long max = Math.max(0L, properties.getLong(KEY_MAX_WAIT_UI_MS, 300000L));
        this.mMaxWaitUIMs = max;
        long max2 = Math.max(max, properties.getLong(KEY_MAX_WAIT_EJ_MS, 300000L));
        this.mMaxWaitEjMs = max2;
        this.mMaxWaitRegularMs = Math.max(max2, properties.getLong(KEY_MAX_WAIT_REGULAR_MS, 1800000L));
    }

    @GuardedBy({"mLock"})
    public void dumpLocked(final IndentingPrintWriter indentingPrintWriter, long j, long j2) {
        indentingPrintWriter.println("Concurrency:");
        indentingPrintWriter.increaseIndent();
        try {
            indentingPrintWriter.println("Configuration:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_CONCURRENCY_LIMIT, Integer.valueOf(this.mSteadyStateConcurrencyLimit)).println();
            indentingPrintWriter.print(KEY_SCREEN_OFF_ADJUSTMENT_DELAY_MS, Long.valueOf(this.mScreenOffAdjustmentDelayMs)).println();
            indentingPrintWriter.print(KEY_PKG_CONCURRENCY_LIMIT_EJ, Integer.valueOf(this.mPkgConcurrencyLimitEj)).println();
            indentingPrintWriter.print(KEY_PKG_CONCURRENCY_LIMIT_REGULAR, Integer.valueOf(this.mPkgConcurrencyLimitRegular)).println();
            indentingPrintWriter.print(KEY_ENABLE_MAX_WAIT_TIME_BYPASS, Boolean.valueOf(this.mMaxWaitTimeBypassEnabled)).println();
            indentingPrintWriter.print(KEY_MAX_WAIT_UI_MS, Long.valueOf(this.mMaxWaitUIMs)).println();
            indentingPrintWriter.print(KEY_MAX_WAIT_EJ_MS, Long.valueOf(this.mMaxWaitEjMs)).println();
            indentingPrintWriter.print(KEY_MAX_WAIT_REGULAR_MS, Long.valueOf(this.mMaxWaitRegularMs)).println();
            indentingPrintWriter.println();
            WorkConfigLimitsPerMemoryTrimLevel workConfigLimitsPerMemoryTrimLevel = CONFIG_LIMITS_SCREEN_ON;
            workConfigLimitsPerMemoryTrimLevel.normal.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            workConfigLimitsPerMemoryTrimLevel.moderate.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            workConfigLimitsPerMemoryTrimLevel.low.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            workConfigLimitsPerMemoryTrimLevel.critical.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            WorkConfigLimitsPerMemoryTrimLevel workConfigLimitsPerMemoryTrimLevel2 = CONFIG_LIMITS_SCREEN_OFF;
            workConfigLimitsPerMemoryTrimLevel2.normal.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            workConfigLimitsPerMemoryTrimLevel2.moderate.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            workConfigLimitsPerMemoryTrimLevel2.low.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            workConfigLimitsPerMemoryTrimLevel2.critical.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.print("Screen state: current ");
            String str = "ON";
            indentingPrintWriter.print(this.mCurrentInteractiveState ? "ON" : "OFF");
            indentingPrintWriter.print("  effective ");
            if (!this.mEffectiveInteractiveState) {
                str = "OFF";
            }
            indentingPrintWriter.print(str);
            indentingPrintWriter.println();
            indentingPrintWriter.print("Last screen ON: ");
            long j3 = j - j2;
            TimeUtils.dumpTimeWithDelta(indentingPrintWriter, this.mLastScreenOnRealtime + j3, j);
            indentingPrintWriter.println();
            indentingPrintWriter.print("Last screen OFF: ");
            TimeUtils.dumpTimeWithDelta(indentingPrintWriter, j3 + this.mLastScreenOffRealtime, j);
            indentingPrintWriter.println();
            indentingPrintWriter.println();
            indentingPrintWriter.print("Current work counts: ");
            indentingPrintWriter.println(this.mWorkCountTracker);
            indentingPrintWriter.println();
            indentingPrintWriter.print("mLastMemoryTrimLevel: ");
            indentingPrintWriter.println(this.mLastMemoryTrimLevel);
            indentingPrintWriter.println();
            indentingPrintWriter.println("Active Package stats:");
            indentingPrintWriter.increaseIndent();
            this.mActivePkgStats.forEach(new Consumer() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    JobConcurrencyManager.PackageStats.m437$$Nest$mdumpLocked((JobConcurrencyManager.PackageStats) obj, indentingPrintWriter);
                }
            });
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.print("User Grace Period: ");
            indentingPrintWriter.println(this.mGracePeriodObserver.mGracePeriodExpiration);
            indentingPrintWriter.println();
            this.mStatLogger.dump(indentingPrintWriter);
        } finally {
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void dumpContextInfoLocked(IndentingPrintWriter indentingPrintWriter, Predicate<JobStatus> predicate, long j, long j2) {
        indentingPrintWriter.println("Active jobs:");
        indentingPrintWriter.increaseIndent();
        if (this.mActiveServices.size() == 0) {
            indentingPrintWriter.println("N/A");
        }
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            JobServiceContext jobServiceContext = this.mActiveServices.get(i);
            JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
            if (runningJobLocked == null || predicate.test(runningJobLocked)) {
                indentingPrintWriter.print("Slot #");
                indentingPrintWriter.print(i);
                indentingPrintWriter.print("(ID=");
                indentingPrintWriter.print(jobServiceContext.getId());
                indentingPrintWriter.print("): ");
                jobServiceContext.dumpLocked(indentingPrintWriter, j);
                if (runningJobLocked != null) {
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.increaseIndent();
                    runningJobLocked.dump(indentingPrintWriter, false, j);
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.print("Evaluated bias: ");
                    indentingPrintWriter.println(JobInfo.getBiasString(runningJobLocked.lastEvaluatedBias));
                    indentingPrintWriter.print("Active at ");
                    TimeUtils.formatDuration(runningJobLocked.madeActive - j2, indentingPrintWriter);
                    indentingPrintWriter.print(", pending for ");
                    TimeUtils.formatDuration(runningJobLocked.madeActive - runningJobLocked.madePending, indentingPrintWriter);
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.println();
                }
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.print("Idle contexts (");
        indentingPrintWriter.print(this.mIdleContexts.size());
        indentingPrintWriter.println("):");
        indentingPrintWriter.increaseIndent();
        for (int i2 = 0; i2 < this.mIdleContexts.size(); i2++) {
            JobServiceContext valueAt = this.mIdleContexts.valueAt(i2);
            indentingPrintWriter.print("ID=");
            indentingPrintWriter.print(valueAt.getId());
            indentingPrintWriter.print(": ");
            valueAt.dumpLocked(indentingPrintWriter, j);
        }
        indentingPrintWriter.decreaseIndent();
        if (this.mNumDroppedContexts > 0) {
            indentingPrintWriter.println();
            indentingPrintWriter.print("Dropped ");
            indentingPrintWriter.print(this.mNumDroppedContexts);
            indentingPrintWriter.println(" contexts");
        }
    }

    public void dumpProtoLocked(ProtoOutputStream protoOutputStream, long j, long j2, long j3) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1133871366145L, this.mCurrentInteractiveState);
        protoOutputStream.write(1133871366146L, this.mEffectiveInteractiveState);
        protoOutputStream.write(1112396529667L, j3 - this.mLastScreenOnRealtime);
        protoOutputStream.write(1112396529668L, j3 - this.mLastScreenOffRealtime);
        protoOutputStream.write(1120986464262L, this.mLastMemoryTrimLevel);
        this.mStatLogger.dumpProto(protoOutputStream, 1146756268039L);
        protoOutputStream.end(start);
    }

    @VisibleForTesting
    boolean shouldRunAsFgUserJob(JobStatus jobStatus) {
        if (!this.mShouldRestrictBgUser) {
            return true;
        }
        int sourceUserId = jobStatus.getSourceUserId();
        UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        UserInfo userInfo = userManagerInternal.getUserInfo(sourceUserId);
        int i = userInfo.profileGroupId;
        if (i != -10000 && i != sourceUserId) {
            userInfo = userManagerInternal.getUserInfo(i);
            sourceUserId = i;
        }
        return ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCurrentUserId() == sourceUserId || userInfo.isPrimary() || this.mGracePeriodObserver.isWithinGracePeriodForUser(sourceUserId);
    }

    int getJobWorkTypes(JobStatus jobStatus) {
        if (!shouldRunAsFgUserJob(jobStatus)) {
            return ((jobStatus.lastEvaluatedBias >= 35 || jobStatus.shouldTreatAsExpeditedJob() || jobStatus.shouldTreatAsUserInitiatedJob()) ? 32 : 0) | 64;
        }
        int i = jobStatus.lastEvaluatedBias;
        int i2 = i >= 40 ? 1 : i >= 35 ? 2 : 16;
        return jobStatus.shouldTreatAsExpeditedJob() ? i2 | 8 : jobStatus.shouldTreatAsUserInitiatedJob() ? i2 | 4 : i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class WorkTypeConfig {
        private static final String KEY_PREFIX_MAX = "concurrency_max_";

        @VisibleForTesting
        static final String KEY_PREFIX_MAX_RATIO = "concurrency_max_ratio_";
        private static final String KEY_PREFIX_MAX_RATIO_BG = "concurrency_max_ratio_bg_";
        private static final String KEY_PREFIX_MAX_RATIO_BGUSER = "concurrency_max_ratio_bguser_";
        private static final String KEY_PREFIX_MAX_RATIO_BGUSER_IMPORTANT = "concurrency_max_ratio_bguser_important_";
        private static final String KEY_PREFIX_MAX_RATIO_EJ = "concurrency_max_ratio_ej_";
        private static final String KEY_PREFIX_MAX_RATIO_FGS = "concurrency_max_ratio_fgs_";
        private static final String KEY_PREFIX_MAX_RATIO_TOP = "concurrency_max_ratio_top_";
        private static final String KEY_PREFIX_MAX_RATIO_UI = "concurrency_max_ratio_ui_";

        @VisibleForTesting
        static final String KEY_PREFIX_MAX_TOTAL = "concurrency_max_total_";
        private static final String KEY_PREFIX_MIN = "concurrency_min_";

        @VisibleForTesting
        static final String KEY_PREFIX_MIN_RATIO = "concurrency_min_ratio_";
        private static final String KEY_PREFIX_MIN_RATIO_BG = "concurrency_min_ratio_bg_";
        private static final String KEY_PREFIX_MIN_RATIO_BGUSER = "concurrency_min_ratio_bguser_";
        private static final String KEY_PREFIX_MIN_RATIO_BGUSER_IMPORTANT = "concurrency_min_ratio_bguser_important_";
        private static final String KEY_PREFIX_MIN_RATIO_EJ = "concurrency_min_ratio_ej_";
        private static final String KEY_PREFIX_MIN_RATIO_FGS = "concurrency_min_ratio_fgs_";
        private static final String KEY_PREFIX_MIN_RATIO_TOP = "concurrency_min_ratio_top_";
        private static final String KEY_PREFIX_MIN_RATIO_UI = "concurrency_min_ratio_ui_";
        private final String mConfigIdentifier;
        private final int mDefaultMaxTotal;
        private int mMaxTotal;
        private final SparseIntArray mMinReservedSlots = new SparseIntArray(7);
        private final SparseIntArray mMaxAllowedSlots = new SparseIntArray(7);
        private final SparseIntArray mDefaultMinReservedSlotsRatio = new SparseIntArray(7);
        private final SparseIntArray mDefaultMaxAllowedSlotsRatio = new SparseIntArray(7);

        WorkTypeConfig(String str, int i, int i2, List<Pair<Integer, Float>> list, List<Pair<Integer, Float>> list2) {
            this.mConfigIdentifier = str;
            int min = Math.min(i2, i);
            this.mMaxTotal = min;
            this.mDefaultMaxTotal = min;
            int i3 = 0;
            for (int size = list.size() - 1; size >= 0; size--) {
                float floatValue = ((Float) list.get(size).second).floatValue();
                int intValue = ((Integer) list.get(size).first).intValue();
                if (floatValue < 0.0f || 1.0f <= floatValue) {
                    throw new IllegalArgumentException("Invalid default min ratio: wt=" + intValue + " minRatio=" + floatValue);
                }
                this.mDefaultMinReservedSlotsRatio.put(intValue, Float.floatToRawIntBits(floatValue));
                i3 = (int) (i3 + (this.mMaxTotal * floatValue));
            }
            int i4 = this.mDefaultMaxTotal;
            if (i4 < 0 || i3 > i4) {
                throw new IllegalArgumentException("Invalid default config: t=" + i2 + " min=" + list + " max=" + list2);
            }
            for (int size2 = list2.size() - 1; size2 >= 0; size2--) {
                float floatValue2 = ((Float) list2.get(size2).second).floatValue();
                int intValue2 = ((Integer) list2.get(size2).first).intValue();
                if (floatValue2 < Float.intBitsToFloat(this.mDefaultMinReservedSlotsRatio.get(intValue2, 0)) || floatValue2 <= 0.0f) {
                    throw new IllegalArgumentException("Invalid default config: t=" + i2 + " min=" + list + " max=" + list2);
                }
                this.mDefaultMaxAllowedSlotsRatio.put(intValue2, Float.floatToRawIntBits(floatValue2));
            }
            update(new DeviceConfig.Properties.Builder("jobscheduler").build(), i);
        }

        void update(DeviceConfig.Properties properties, int i) {
            this.mMaxTotal = Math.max(1, Math.min(i, properties.getInt(KEY_PREFIX_MAX_TOTAL + this.mConfigIdentifier, this.mDefaultMaxTotal)));
            int floatToIntBits = Float.floatToIntBits(1.0f);
            this.mMaxAllowedSlots.clear();
            int maxValue = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_TOP + this.mConfigIdentifier, 1, floatToIntBits);
            this.mMaxAllowedSlots.put(1, maxValue);
            int maxValue2 = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_FGS + this.mConfigIdentifier, 2, floatToIntBits);
            this.mMaxAllowedSlots.put(2, maxValue2);
            int maxValue3 = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_UI + this.mConfigIdentifier, 4, floatToIntBits);
            this.mMaxAllowedSlots.put(4, maxValue3);
            int maxValue4 = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_EJ + this.mConfigIdentifier, 8, floatToIntBits);
            this.mMaxAllowedSlots.put(8, maxValue4);
            int maxValue5 = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_BG + this.mConfigIdentifier, 16, floatToIntBits);
            this.mMaxAllowedSlots.put(16, maxValue5);
            int maxValue6 = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_BGUSER_IMPORTANT + this.mConfigIdentifier, 32, floatToIntBits);
            this.mMaxAllowedSlots.put(32, maxValue6);
            int maxValue7 = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_BGUSER + this.mConfigIdentifier, 64, floatToIntBits);
            this.mMaxAllowedSlots.put(64, maxValue7);
            int i2 = this.mMaxTotal;
            this.mMinReservedSlots.clear();
            int minValue = getMinValue(properties, KEY_PREFIX_MIN_RATIO_TOP + this.mConfigIdentifier, 1, 1, Math.min(maxValue, this.mMaxTotal));
            this.mMinReservedSlots.put(1, minValue);
            int i3 = i2 - minValue;
            int minValue2 = getMinValue(properties, KEY_PREFIX_MIN_RATIO_FGS + this.mConfigIdentifier, 2, 0, Math.min(maxValue2, i3));
            this.mMinReservedSlots.put(2, minValue2);
            int i4 = i3 - minValue2;
            int minValue3 = getMinValue(properties, KEY_PREFIX_MIN_RATIO_UI + this.mConfigIdentifier, 4, 0, Math.min(maxValue3, i4));
            this.mMinReservedSlots.put(4, minValue3);
            int i5 = i4 - minValue3;
            int minValue4 = getMinValue(properties, KEY_PREFIX_MIN_RATIO_EJ + this.mConfigIdentifier, 8, 0, Math.min(maxValue4, i5));
            this.mMinReservedSlots.put(8, minValue4);
            int i6 = i5 - minValue4;
            int minValue5 = getMinValue(properties, KEY_PREFIX_MIN_RATIO_BG + this.mConfigIdentifier, 16, 0, Math.min(maxValue5, i6));
            this.mMinReservedSlots.put(16, minValue5);
            int i7 = i6 - minValue5;
            int minValue6 = getMinValue(properties, KEY_PREFIX_MIN_RATIO_BGUSER_IMPORTANT + this.mConfigIdentifier, 32, 0, Math.min(maxValue6, i7));
            this.mMinReservedSlots.put(32, minValue6);
            this.mMinReservedSlots.put(64, getMinValue(properties, KEY_PREFIX_MIN_RATIO_BGUSER + this.mConfigIdentifier, 64, 0, Math.min(maxValue7, i7 - minValue6)));
        }

        private int getMaxValue(DeviceConfig.Properties properties, String str, int i, int i2) {
            return Math.max(1, (int) (this.mMaxTotal * Math.min(1.0f, properties.getFloat(str, Float.intBitsToFloat(this.mDefaultMaxAllowedSlotsRatio.get(i, i2))))));
        }

        private int getMinValue(DeviceConfig.Properties properties, String str, int i, int i2, int i3) {
            return Math.max(i2, Math.min(i3, (int) (this.mMaxTotal * Math.min(1.0f, properties.getFloat(str, Float.intBitsToFloat(this.mDefaultMinReservedSlotsRatio.get(i)))))));
        }

        int getMaxTotal() {
            return this.mMaxTotal;
        }

        int getMax(int i) {
            return this.mMaxAllowedSlots.get(i, this.mMaxTotal);
        }

        int getMinReserved(int i) {
            return this.mMinReservedSlots.get(i);
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.print(KEY_PREFIX_MAX_TOTAL + this.mConfigIdentifier, Integer.valueOf(this.mMaxTotal)).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_TOP + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(1))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_TOP + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(1))).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_FGS + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(2))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_FGS + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(2))).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_UI + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(4))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_UI + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(4))).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_EJ + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(8))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_EJ + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(8))).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_BG + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(16))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_BG + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(16))).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_BGUSER + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(32))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_BGUSER + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(32))).println();
            indentingPrintWriter.print(KEY_PREFIX_MIN_RATIO_BGUSER + this.mConfigIdentifier, Integer.valueOf(this.mMinReservedSlots.get(64))).println();
            indentingPrintWriter.print(KEY_PREFIX_MAX_RATIO_BGUSER + this.mConfigIdentifier, Integer.valueOf(this.mMaxAllowedSlots.get(64))).println();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class WorkConfigLimitsPerMemoryTrimLevel {
        public final WorkTypeConfig critical;
        public final WorkTypeConfig low;
        public final WorkTypeConfig moderate;
        public final WorkTypeConfig normal;

        WorkConfigLimitsPerMemoryTrimLevel(WorkTypeConfig workTypeConfig, WorkTypeConfig workTypeConfig2, WorkTypeConfig workTypeConfig3, WorkTypeConfig workTypeConfig4) {
            this.normal = workTypeConfig;
            this.moderate = workTypeConfig2;
            this.low = workTypeConfig3;
            this.critical = workTypeConfig4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class GracePeriodObserver extends UserSwitchObserver {

        @VisibleForTesting
        int mGracePeriod;

        @VisibleForTesting
        final SparseLongArray mGracePeriodExpiration = new SparseLongArray();
        final Object mLock = new Object();
        private int mCurrentUserId = ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCurrentUserId();
        private final UserManagerInternal mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);

        GracePeriodObserver(Context context) {
            this.mGracePeriod = Math.max(0, context.getResources().getInteger(R.integer.config_networkTransitionTimeout));
        }

        public void onUserSwitchComplete(int i) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis() + this.mGracePeriod;
            synchronized (this.mLock) {
                int i2 = this.mCurrentUserId;
                if (i2 != -10000 && this.mUserManagerInternal.exists(i2)) {
                    this.mGracePeriodExpiration.append(this.mCurrentUserId, millis);
                }
                this.mGracePeriodExpiration.delete(i);
                this.mCurrentUserId = i;
            }
        }

        void onUserRemoved(int i) {
            synchronized (this.mLock) {
                this.mGracePeriodExpiration.delete(i);
            }
        }

        @VisibleForTesting
        public boolean isWithinGracePeriodForUser(int i) {
            boolean z;
            synchronized (this.mLock) {
                z = i == this.mCurrentUserId || JobSchedulerService.sElapsedRealtimeClock.millis() < this.mGracePeriodExpiration.get(i, JobStatus.NO_LATEST_RUNTIME);
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class WorkCountTracker {
        private int mConfigMaxTotal;
        private final SparseIntArray mConfigNumReservedSlots = new SparseIntArray(7);
        private final SparseIntArray mConfigAbsoluteMaxSlots = new SparseIntArray(7);
        private final SparseIntArray mRecycledReserved = new SparseIntArray(7);
        private final SparseIntArray mNumActuallyReservedSlots = new SparseIntArray(7);
        private final SparseIntArray mNumPendingJobs = new SparseIntArray(7);
        private final SparseIntArray mNumRunningJobs = new SparseIntArray(7);
        private final SparseIntArray mNumStartingJobs = new SparseIntArray(7);
        private int mNumUnspecializedRemaining = 0;

        WorkCountTracker() {
        }

        void setConfig(WorkTypeConfig workTypeConfig) {
            this.mConfigMaxTotal = workTypeConfig.getMaxTotal();
            for (int i = 1; i < 127; i <<= 1) {
                this.mConfigNumReservedSlots.put(i, workTypeConfig.getMinReserved(i));
                this.mConfigAbsoluteMaxSlots.put(i, workTypeConfig.getMax(i));
            }
            this.mNumUnspecializedRemaining = this.mConfigMaxTotal;
            for (int size = this.mNumRunningJobs.size() - 1; size >= 0; size--) {
                this.mNumUnspecializedRemaining -= Math.max(this.mNumRunningJobs.valueAt(size), this.mConfigNumReservedSlots.get(this.mNumRunningJobs.keyAt(size)));
            }
        }

        void resetCounts() {
            this.mNumActuallyReservedSlots.clear();
            this.mNumPendingJobs.clear();
            this.mNumRunningJobs.clear();
            resetStagingCount();
        }

        void resetStagingCount() {
            this.mNumStartingJobs.clear();
        }

        void incrementRunningJobCount(int i) {
            SparseIntArray sparseIntArray = this.mNumRunningJobs;
            sparseIntArray.put(i, sparseIntArray.get(i) + 1);
        }

        void incrementPendingJobCount(int i) {
            adjustPendingJobCount(i, true);
        }

        void decrementPendingJobCount(int i) {
            if (adjustPendingJobCount(i, false) > 1) {
                for (int i2 = 1; i2 <= i; i2 <<= 1) {
                    if ((i2 & i) == i2) {
                        maybeAdjustReservations(i2);
                    }
                }
            }
        }

        private int adjustPendingJobCount(int i, boolean z) {
            int i2 = z ? 1 : -1;
            int i3 = 0;
            for (int i4 = 1; i4 <= i; i4 <<= 1) {
                if ((i & i4) == i4) {
                    SparseIntArray sparseIntArray = this.mNumPendingJobs;
                    sparseIntArray.put(i4, sparseIntArray.get(i4) + i2);
                    i3++;
                }
            }
            return i3;
        }

        void stageJob(int i, int i2) {
            int i3 = this.mNumStartingJobs.get(i) + 1;
            this.mNumStartingJobs.put(i, i3);
            decrementPendingJobCount(i2);
            if (i3 + this.mNumRunningJobs.get(i) > this.mNumActuallyReservedSlots.get(i)) {
                this.mNumUnspecializedRemaining--;
            }
        }

        void onStagedJobFailed(int i) {
            int i2 = this.mNumStartingJobs.get(i);
            if (i2 == 0) {
                Slog.e(JobConcurrencyManager.TAG, "# staged jobs for " + i + " went negative.");
                return;
            }
            this.mNumStartingJobs.put(i, i2 - 1);
            maybeAdjustReservations(i);
        }

        private void maybeAdjustReservations(int i) {
            int max = Math.max(this.mConfigNumReservedSlots.get(i), this.mNumRunningJobs.get(i) + this.mNumStartingJobs.get(i) + this.mNumPendingJobs.get(i));
            if (max < this.mNumActuallyReservedSlots.get(i)) {
                this.mNumActuallyReservedSlots.put(i, max);
                int i2 = 0;
                for (int i3 = 0; i3 < this.mNumActuallyReservedSlots.size(); i3++) {
                    int keyAt = this.mNumActuallyReservedSlots.keyAt(i3);
                    if (i2 == 0 || keyAt < i2) {
                        int i4 = this.mNumRunningJobs.get(keyAt) + this.mNumStartingJobs.get(keyAt) + this.mNumPendingJobs.get(keyAt);
                        if (this.mNumActuallyReservedSlots.valueAt(i3) < this.mConfigAbsoluteMaxSlots.get(keyAt) && i4 > this.mNumActuallyReservedSlots.valueAt(i3)) {
                            i2 = keyAt;
                        }
                    }
                }
                if (i2 != 0) {
                    SparseIntArray sparseIntArray = this.mNumActuallyReservedSlots;
                    sparseIntArray.put(i2, sparseIntArray.get(i2) + 1);
                } else {
                    this.mNumUnspecializedRemaining++;
                }
            }
        }

        void onJobStarted(int i) {
            SparseIntArray sparseIntArray = this.mNumRunningJobs;
            sparseIntArray.put(i, sparseIntArray.get(i) + 1);
            int i2 = this.mNumStartingJobs.get(i);
            if (i2 == 0) {
                Slog.e(JobConcurrencyManager.TAG, "# stated jobs for " + i + " went negative.");
                return;
            }
            this.mNumStartingJobs.put(i, i2 - 1);
        }

        void onJobFinished(int i) {
            int i2 = this.mNumRunningJobs.get(i) - 1;
            if (i2 < 0) {
                Slog.e(JobConcurrencyManager.TAG, "# running jobs for " + i + " went negative.");
                return;
            }
            this.mNumRunningJobs.put(i, i2);
            maybeAdjustReservations(i);
        }

        void onCountDone() {
            this.mNumUnspecializedRemaining = this.mConfigMaxTotal;
            for (int i = 1; i < 127; i <<= 1) {
                int i2 = this.mNumRunningJobs.get(i);
                this.mRecycledReserved.put(i, i2);
                this.mNumUnspecializedRemaining -= i2;
            }
            for (int i3 = 1; i3 < 127; i3 <<= 1) {
                int i4 = this.mNumRunningJobs.get(i3) + this.mNumPendingJobs.get(i3);
                int i5 = this.mRecycledReserved.get(i3);
                int max = Math.max(0, Math.min(this.mNumUnspecializedRemaining, Math.min(i4, this.mConfigNumReservedSlots.get(i3) - i5)));
                this.mRecycledReserved.put(i3, i5 + max);
                this.mNumUnspecializedRemaining -= max;
            }
            for (int i6 = 1; i6 < 127; i6 <<= 1) {
                int i7 = this.mNumRunningJobs.get(i6) + this.mNumPendingJobs.get(i6);
                int i8 = this.mRecycledReserved.get(i6);
                int max2 = Math.max(0, Math.min(this.mNumUnspecializedRemaining, Math.min(this.mConfigAbsoluteMaxSlots.get(i6), i7) - i8));
                this.mNumActuallyReservedSlots.put(i6, i8 + max2);
                this.mNumUnspecializedRemaining -= max2;
            }
        }

        int canJobStart(int i) {
            for (int i2 = 1; i2 <= i; i2 <<= 1) {
                if ((i & i2) == i2) {
                    if (this.mNumRunningJobs.get(i2) + this.mNumStartingJobs.get(i2) < Math.min(this.mConfigAbsoluteMaxSlots.get(i2), this.mNumActuallyReservedSlots.get(i2) + this.mNumUnspecializedRemaining)) {
                        return i2;
                    }
                }
            }
            return 0;
        }

        int canJobStart(int i, int i2) {
            boolean z;
            int i3 = this.mNumRunningJobs.get(i2);
            if (i2 == 0 || i3 <= 0) {
                z = false;
            } else {
                this.mNumRunningJobs.put(i2, i3 - 1);
                this.mNumUnspecializedRemaining++;
                z = true;
            }
            int canJobStart = canJobStart(i);
            if (z) {
                this.mNumRunningJobs.put(i2, i3);
                this.mNumUnspecializedRemaining--;
            }
            return canJobStart;
        }

        int getPendingJobCount(int i) {
            return this.mNumPendingJobs.get(i, 0);
        }

        int getRunningJobCount(int i) {
            return this.mNumRunningJobs.get(i, 0);
        }

        boolean isOverTypeLimit(int i) {
            return getRunningJobCount(i) > this.mConfigAbsoluteMaxSlots.get(i);
        }

        public String toString() {
            return "Config={tot=" + this.mConfigMaxTotal + " mins=" + this.mConfigNumReservedSlots + " maxs=" + this.mConfigAbsoluteMaxSlots + "}, act res=" + this.mNumActuallyReservedSlots + ", Pending=" + this.mNumPendingJobs + ", Running=" + this.mNumRunningJobs + ", Staged=" + this.mNumStartingJobs + ", # unspecialized remaining=" + this.mNumUnspecializedRemaining;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PackageStats {
        public int numRunningEj;
        public int numRunningRegular;
        public int numStagedEj;
        public int numStagedRegular;
        public String packageName;
        public int userId;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: -$$Nest$mdumpLocked, reason: not valid java name */
        public static /* bridge */ /* synthetic */ void m437$$Nest$mdumpLocked(PackageStats packageStats, IndentingPrintWriter indentingPrintWriter) {
            packageStats.dumpLocked(indentingPrintWriter);
        }

        PackageStats() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPackage(int i, String str) {
            this.userId = i;
            this.packageName = str;
            this.numRunningRegular = 0;
            this.numRunningEj = 0;
            resetStagedCount();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetStagedCount() {
            this.numStagedRegular = 0;
            this.numStagedEj = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void adjustRunningCount(boolean z, boolean z2) {
            if (z2) {
                this.numRunningEj = Math.max(0, this.numRunningEj + (z ? 1 : -1));
            } else {
                this.numRunningRegular = Math.max(0, this.numRunningRegular + (z ? 1 : -1));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void adjustStagedCount(boolean z, boolean z2) {
            if (z2) {
                this.numStagedEj = Math.max(0, this.numStagedEj + (z ? 1 : -1));
            } else {
                this.numStagedRegular = Math.max(0, this.numStagedRegular + (z ? 1 : -1));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        @GuardedBy({"mLock"})
        public void dumpLocked(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.print("PackageStats{");
            indentingPrintWriter.print(this.userId);
            indentingPrintWriter.print("-");
            indentingPrintWriter.print(this.packageName);
            indentingPrintWriter.print("#runEJ", Integer.valueOf(this.numRunningEj));
            indentingPrintWriter.print("#runReg", Integer.valueOf(this.numRunningRegular));
            indentingPrintWriter.print("#stagedEJ", Integer.valueOf(this.numStagedEj));
            indentingPrintWriter.print("#stagedReg", Integer.valueOf(this.numStagedRegular));
            indentingPrintWriter.println("}");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ContextAssignment {
        public JobServiceContext context;
        public JobStatus newJob;
        public String preemptReason;
        public String shouldStopJobReason;
        public long timeUntilStoppableMs;
        public int preferredUid = -1;
        public int workType = 0;
        public int preemptReasonCode = 0;
        public int newWorkType = 0;

        ContextAssignment() {
        }

        void clear() {
            this.context = null;
            this.preferredUid = -1;
            this.workType = 0;
            this.preemptReason = null;
            this.preemptReasonCode = 0;
            this.timeUntilStoppableMs = 0L;
            this.shouldStopJobReason = null;
            this.newJob = null;
            this.newWorkType = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class AssignmentInfo {
        public long minPreferredUidOnlyWaitingTimeMs;
        public int numRunningEj;
        public int numRunningImmediacyPrivileged;
        public int numRunningReg;
        public int numRunningUi;

        AssignmentInfo() {
        }

        void clear() {
            this.minPreferredUidOnlyWaitingTimeMs = 0L;
            this.numRunningImmediacyPrivileged = 0;
            this.numRunningUi = 0;
            this.numRunningEj = 0;
            this.numRunningReg = 0;
        }
    }

    @VisibleForTesting
    void addRunningJobForTesting(JobStatus jobStatus) {
        JobServiceContext createNewJobServiceContext;
        this.mRunningJobs.add(jobStatus);
        getPackageStatsForTesting(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()).adjustRunningCount(true, jobStatus.shouldTreatAsExpeditedJob());
        if (this.mIdleContexts.size() > 0) {
            ArraySet<JobServiceContext> arraySet = this.mIdleContexts;
            createNewJobServiceContext = arraySet.removeAt(arraySet.size() - 1);
        } else {
            createNewJobServiceContext = createNewJobServiceContext();
        }
        createNewJobServiceContext.executeRunnableJob(jobStatus, this.mWorkCountTracker.canJobStart(getJobWorkTypes(jobStatus)));
        this.mActiveServices.add(createNewJobServiceContext);
    }

    @VisibleForTesting
    int getPackageConcurrencyLimitEj() {
        return this.mPkgConcurrencyLimitEj;
    }

    int getPackageConcurrencyLimitRegular() {
        return this.mPkgConcurrencyLimitRegular;
    }

    @VisibleForTesting
    PackageStats getPackageStatsForTesting(int i, String str) {
        PackageStats pkgStatsLocked = getPkgStatsLocked(i, str);
        this.mActivePkgStats.add(i, str, pkgStatsLocked);
        return pkgStatsLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        Injector() {
        }

        JobServiceContext createJobServiceContext(JobSchedulerService jobSchedulerService, JobConcurrencyManager jobConcurrencyManager, JobNotificationCoordinator jobNotificationCoordinator, IBatteryStats iBatteryStats, JobPackageTracker jobPackageTracker, Looper looper) {
            return new JobServiceContext(jobSchedulerService, jobConcurrencyManager, jobNotificationCoordinator, iBatteryStats, jobPackageTracker, looper);
        }
    }
}
