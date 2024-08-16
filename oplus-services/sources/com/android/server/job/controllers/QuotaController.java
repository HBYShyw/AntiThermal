package com.android.server.job.controllers;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.UidObserver;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManagerInternal;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.UserPackage;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseArrayMap;
import android.util.SparseBooleanArray;
import android.util.SparseLongArray;
import android.util.SparseSetArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.LocalServices;
import com.android.server.PowerAllowlistInternal;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.StateChangedListener;
import com.android.server.job.controllers.QuotaController;
import com.android.server.net.NetworkPolicyManagerService;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.usage.AppStandbyController;
import com.android.server.usage.AppStandbyInternal;
import com.android.server.usage.UnixCalendar;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.utils.AlarmQueue;
import dalvik.annotation.optimization.NeverCompile;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class QuotaController extends StateController {
    private static final String ALARM_TAG_CLEANUP = "*job.cleanup*";
    private static final String ALARM_TAG_QUOTA_CHECK = "*job.quota_check*";
    private static final boolean DEBUG;
    private static final long MAX_PERIOD_MS = 86400000;
    private static final int MSG_CHECK_PACKAGE = 2;
    private static final int MSG_CLEAN_UP_SESSIONS = 1;

    @VisibleForTesting
    static final int MSG_END_GRACE_PERIOD = 6;
    private static final int MSG_PROCESS_USAGE_EVENT = 5;

    @VisibleForTesting
    static final int MSG_REACHED_EJ_QUOTA = 4;

    @VisibleForTesting
    static final int MSG_REACHED_QUOTA = 0;
    private static final int MSG_UID_PROCESS_STATE_CHANGED = 3;
    private static final int SYSTEM_APP_CHECK_FLAGS = 4993024;
    private static final String TAG = "JobScheduler.Quota";
    private final AlarmManager mAlarmManager;
    private final long[] mAllowedTimePerPeriodMs;
    private final BackgroundJobsController mBackgroundJobsController;
    private final long[] mBucketPeriodsMs;
    private final ConnectivityController mConnectivityController;
    private final Consumer<List<TimedEvent>> mDeleteOldEventsFunctor;
    private long mEJGracePeriodTempAllowlistMs;
    private long mEJGracePeriodTopAppMs;
    private long mEJLimitWindowSizeMs;
    private final long[] mEJLimitsMs;
    private final SparseArrayMap<String, Timer> mEJPkgTimers;
    private long mEJRewardInteractionMs;
    private long mEJRewardNotificationSeenMs;
    private long mEJRewardTopAppMs;
    private final SparseArrayMap<String, ShrinkableDebits> mEJStats;
    private final SparseArrayMap<String, List<TimedEvent>> mEJTimingSessions;
    private long mEJTopAppTimeChunkSizeMs;
    private final EarliestEndTimeFunctor mEarliestEndTimeFunctor;
    private long mEjLimitAdditionInstallerMs;
    private long mEjLimitAdditionSpecialMs;
    private final SparseArrayMap<String, ExecutionStats[]> mExecutionStatsCache;
    private final SparseBooleanArray mForegroundUids;
    private final QcHandler mHandler;

    @GuardedBy({"mLock"})
    private final InQuotaAlarmQueue mInQuotaAlarmQueue;

    @GuardedBy({"mLock"})
    private boolean mIsEnabled;
    private final int[] mMaxBucketJobCounts;
    private final int[] mMaxBucketSessionCounts;
    private long mMaxExecutionTimeIntoQuotaMs;
    private long mMaxExecutionTimeMs;
    private int mMaxJobCountPerRateLimitingWindow;
    private int mMaxSessionCountPerRateLimitingWindow;
    private long mNextCleanupTimeElapsed;
    private final SparseArrayMap<String, Timer> mPkgTimers;
    private final QcConstants mQcConstants;
    private long mQuotaBufferMs;
    private long mQuotaBumpAdditionalDurationMs;
    private int mQuotaBumpAdditionalJobCount;
    private int mQuotaBumpAdditionalSessionCount;
    private int mQuotaBumpLimit;
    private long mQuotaBumpWindowSizeMs;
    private long mRateLimitingWindowMs;
    private final AlarmManager.OnAlarmListener mSessionCleanupAlarmListener;
    private final SparseSetArray<String> mSystemInstallers;
    private final SparseBooleanArray mTempAllowlistCache;
    private final SparseLongArray mTempAllowlistGraceCache;
    private final TimedEventTooOldPredicate mTimedEventTooOld;
    private final TimerChargingUpdateFunctor mTimerChargingUpdateFunctor;
    private final SparseArrayMap<String, List<TimedEvent>> mTimingEvents;
    private long mTimingSessionCoalescingDurationMs;
    private final SparseBooleanArray mTopAppCache;
    private final SparseLongArray mTopAppGraceCache;
    private final SparseArrayMap<String, TopAppTimer> mTopAppTrackers;
    private final ArraySet<JobStatus> mTopStartedJobs;
    private final SparseArrayMap<String, ArraySet<JobStatus>> mTrackedJobs;
    private final UidConstraintUpdater mUpdateUidConstraints;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface TimedEvent {
        void dump(IndentingPrintWriter indentingPrintWriter);

        long getEndTimeElapsed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashLong(long j) {
        return (int) (j ^ (j >>> 32));
    }

    static {
        DEBUG = JobSchedulerService.DEBUG || Log.isLoggable(TAG, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ExecutionStats {
        public long allowedTimePerPeriodMs;
        public int bgJobCountInMaxPeriod;
        public int bgJobCountInWindow;
        public long executionTimeInMaxPeriodMs;
        public long executionTimeInWindowMs;
        public long expirationTimeElapsed;
        public long inQuotaTimeElapsed;
        public int jobCountInRateLimitingWindow;
        public int jobCountLimit;
        public long jobRateLimitExpirationTimeElapsed;
        public int sessionCountInRateLimitingWindow;
        public int sessionCountInWindow;
        public int sessionCountLimit;
        public long sessionRateLimitExpirationTimeElapsed;
        public long windowSizeMs;

        ExecutionStats() {
        }

        public String toString() {
            return "expirationTime=" + this.expirationTimeElapsed + ", allowedTimePerPeriodMs=" + this.allowedTimePerPeriodMs + ", windowSizeMs=" + this.windowSizeMs + ", jobCountLimit=" + this.jobCountLimit + ", sessionCountLimit=" + this.sessionCountLimit + ", executionTimeInWindow=" + this.executionTimeInWindowMs + ", bgJobCountInWindow=" + this.bgJobCountInWindow + ", executionTimeInMaxPeriod=" + this.executionTimeInMaxPeriodMs + ", bgJobCountInMaxPeriod=" + this.bgJobCountInMaxPeriod + ", sessionCountInWindow=" + this.sessionCountInWindow + ", inQuotaTime=" + this.inQuotaTimeElapsed + ", rateLimitJobCountExpirationTime=" + this.jobRateLimitExpirationTimeElapsed + ", rateLimitJobCountWindow=" + this.jobCountInRateLimitingWindow + ", rateLimitSessionCountExpirationTime=" + this.sessionRateLimitExpirationTimeElapsed + ", rateLimitSessionCountWindow=" + this.sessionCountInRateLimitingWindow;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ExecutionStats)) {
                return false;
            }
            ExecutionStats executionStats = (ExecutionStats) obj;
            return this.expirationTimeElapsed == executionStats.expirationTimeElapsed && this.allowedTimePerPeriodMs == executionStats.allowedTimePerPeriodMs && this.windowSizeMs == executionStats.windowSizeMs && this.jobCountLimit == executionStats.jobCountLimit && this.sessionCountLimit == executionStats.sessionCountLimit && this.executionTimeInWindowMs == executionStats.executionTimeInWindowMs && this.bgJobCountInWindow == executionStats.bgJobCountInWindow && this.executionTimeInMaxPeriodMs == executionStats.executionTimeInMaxPeriodMs && this.sessionCountInWindow == executionStats.sessionCountInWindow && this.bgJobCountInMaxPeriod == executionStats.bgJobCountInMaxPeriod && this.inQuotaTimeElapsed == executionStats.inQuotaTimeElapsed && this.jobRateLimitExpirationTimeElapsed == executionStats.jobRateLimitExpirationTimeElapsed && this.jobCountInRateLimitingWindow == executionStats.jobCountInRateLimitingWindow && this.sessionRateLimitExpirationTimeElapsed == executionStats.sessionRateLimitExpirationTimeElapsed && this.sessionCountInRateLimitingWindow == executionStats.sessionCountInRateLimitingWindow;
        }

        public int hashCode() {
            return ((((((((((((((((((((((((((((0 + QuotaController.hashLong(this.expirationTimeElapsed)) * 31) + QuotaController.hashLong(this.allowedTimePerPeriodMs)) * 31) + QuotaController.hashLong(this.windowSizeMs)) * 31) + QuotaController.hashLong(this.jobCountLimit)) * 31) + QuotaController.hashLong(this.sessionCountLimit)) * 31) + QuotaController.hashLong(this.executionTimeInWindowMs)) * 31) + this.bgJobCountInWindow) * 31) + QuotaController.hashLong(this.executionTimeInMaxPeriodMs)) * 31) + this.bgJobCountInMaxPeriod) * 31) + this.sessionCountInWindow) * 31) + QuotaController.hashLong(this.inQuotaTimeElapsed)) * 31) + QuotaController.hashLong(this.jobRateLimitExpirationTimeElapsed)) * 31) + this.jobCountInRateLimitingWindow) * 31) + QuotaController.hashLong(this.sessionRateLimitExpirationTimeElapsed)) * 31) + this.sessionCountInRateLimitingWindow;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class QcUidObserver extends UidObserver {
        private QcUidObserver() {
        }

        public void onUidStateChanged(int i, int i2, long j, int i3) {
            QuotaController.this.mHandler.obtainMessage(3, i, i2).sendToTarget();
        }
    }

    public QuotaController(JobSchedulerService jobSchedulerService, BackgroundJobsController backgroundJobsController, ConnectivityController connectivityController) {
        super(jobSchedulerService);
        this.mTrackedJobs = new SparseArrayMap<>();
        this.mPkgTimers = new SparseArrayMap<>();
        this.mEJPkgTimers = new SparseArrayMap<>();
        this.mTimingEvents = new SparseArrayMap<>();
        this.mEJTimingSessions = new SparseArrayMap<>();
        this.mExecutionStatsCache = new SparseArrayMap<>();
        this.mEJStats = new SparseArrayMap<>();
        this.mTopAppTrackers = new SparseArrayMap<>();
        this.mForegroundUids = new SparseBooleanArray();
        this.mTopStartedJobs = new ArraySet<>();
        this.mTempAllowlistCache = new SparseBooleanArray();
        this.mTempAllowlistGraceCache = new SparseLongArray();
        this.mTopAppCache = new SparseBooleanArray();
        this.mTopAppGraceCache = new SparseLongArray();
        this.mAllowedTimePerPeriodMs = new long[]{600000, 600000, 600000, 600000, 0, 600000, 600000};
        this.mMaxExecutionTimeMs = 14400000L;
        this.mQuotaBufferMs = 30000L;
        this.mMaxExecutionTimeIntoQuotaMs = 14400000 - 30000;
        this.mRateLimitingWindowMs = 60000L;
        this.mMaxJobCountPerRateLimitingWindow = 20;
        this.mMaxSessionCountPerRateLimitingWindow = 20;
        this.mNextCleanupTimeElapsed = 0L;
        this.mSessionCleanupAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.job.controllers.QuotaController.1
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                QuotaController.this.mHandler.obtainMessage(1).sendToTarget();
            }
        };
        this.mBucketPeriodsMs = new long[]{600000, AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT, 28800000, 86400000, 0, 86400000, 600000};
        this.mMaxBucketJobCounts = new int[]{75, 120, UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS, 48, 0, 10, 75};
        this.mMaxBucketSessionCounts = new int[]{75, 10, 8, 3, 0, 1, 75};
        this.mTimingSessionCoalescingDurationMs = 5000L;
        this.mEJLimitsMs = new long[]{1800000, 1800000, 600000, 600000, 0, 300000, 3600000};
        this.mEjLimitAdditionInstallerMs = 1800000L;
        this.mEjLimitAdditionSpecialMs = 900000L;
        this.mEJLimitWindowSizeMs = 86400000L;
        this.mEJTopAppTimeChunkSizeMs = 30000L;
        this.mEJRewardTopAppMs = JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY;
        this.mEJRewardInteractionMs = 15000L;
        this.mEJRewardNotificationSeenMs = 0L;
        this.mEJGracePeriodTempAllowlistMs = JobSchedulerService.Constants.DEFAULT_RUNTIME_MIN_EJ_GUARANTEE_MS;
        this.mEJGracePeriodTopAppMs = 60000L;
        this.mQuotaBumpAdditionalDurationMs = 60000L;
        this.mQuotaBumpAdditionalJobCount = 2;
        this.mQuotaBumpAdditionalSessionCount = 1;
        this.mQuotaBumpWindowSizeMs = 28800000L;
        this.mQuotaBumpLimit = 8;
        this.mSystemInstallers = new SparseSetArray<>();
        byte b = 0;
        byte b2 = 0;
        this.mEarliestEndTimeFunctor = new EarliestEndTimeFunctor();
        this.mTimerChargingUpdateFunctor = new TimerChargingUpdateFunctor();
        this.mUpdateUidConstraints = new UidConstraintUpdater();
        this.mTimedEventTooOld = new TimedEventTooOldPredicate();
        this.mDeleteOldEventsFunctor = new Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QuotaController.this.lambda$new$2((List) obj);
            }
        };
        this.mHandler = new QcHandler(AppSchedulingModuleThread.get().getLooper());
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(AlarmManager.class);
        this.mQcConstants = new QcConstants();
        this.mBackgroundJobsController = backgroundJobsController;
        this.mConnectivityController = connectivityController;
        this.mIsEnabled = !this.mConstants.USE_TARE_POLICY;
        this.mInQuotaAlarmQueue = new InQuotaAlarmQueue(this.mContext, AppSchedulingModuleThread.get().getLooper());
        ((AppStandbyInternal) LocalServices.getService(AppStandbyInternal.class)).addListener(new StandbyTracker());
        ((UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class)).registerListener(new UsageEventTracker());
        ((PowerAllowlistInternal) LocalServices.getService(PowerAllowlistInternal.class)).registerTempAllowlistChangeListener(new TempAllowlistTracker());
        try {
            ActivityManager.getService().registerUidObserver(new QcUidObserver(), 1, 4, (String) null);
            ActivityManager.getService().registerUidObserver(new QcUidObserver(), 1, 2, (String) null);
        } catch (RemoteException unused) {
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onSystemServicesReady() {
        synchronized (this.mLock) {
            cacheInstallerPackagesLocked(0);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void maybeStartTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        int sourceUserId = jobStatus.getSourceUserId();
        String sourcePackageName = jobStatus.getSourcePackageName();
        ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(sourceUserId, sourcePackageName);
        if (arraySet == null) {
            arraySet = new ArraySet();
            this.mTrackedJobs.add(sourceUserId, sourcePackageName, arraySet);
        }
        arraySet.add(jobStatus);
        jobStatus.setTrackingController(64);
        boolean isWithinQuotaLocked = isWithinQuotaLocked(jobStatus);
        boolean z = false;
        boolean z2 = jobStatus.isRequestedExpeditedJob() && isWithinEJQuotaLocked(jobStatus);
        setConstraintSatisfied(jobStatus, millis, isWithinQuotaLocked, z2);
        if (jobStatus.isRequestedExpeditedJob()) {
            setExpeditedQuotaApproved(jobStatus, millis, z2);
            z = !z2;
        }
        if (!isWithinQuotaLocked || z) {
            maybeScheduleStartAlarmLocked(sourceUserId, sourcePackageName, jobStatus.getEffectiveStandbyBucket());
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ConstructorVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r9v0 ??, still in use, count: 1, list:
          (r9v0 ?? I:java.lang.Object) from 0x007b: INVOKE (r2v4 ?? I:android.util.SparseArrayMap), (r0v2 ?? I:int), (r1v1 ?? I:java.lang.Object), (r9v0 ?? I:java.lang.Object) VIRTUAL call: android.util.SparseArrayMap.add(int, java.lang.Object, java.lang.Object):java.lang.Object (LINE:637)
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
        	at jadx.core.utils.InsnRemover.perform(InsnRemover.java:72)
        	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:54)
        	at jadx.core.dex.visitors.ConstructorVisitor.visit(ConstructorVisitor.java:34)
        */
    @Override // com.android.server.job.controllers.StateController
    @com.android.internal.annotations.GuardedBy({"mLock"})
    public void prepareForExecutionLocked(
    /*  JADX ERROR: JadxRuntimeException in pass: ConstructorVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r9v0 ??, still in use, count: 1, list:
          (r9v0 ?? I:java.lang.Object) from 0x007b: INVOKE (r2v4 ?? I:android.util.SparseArrayMap), (r0v2 ?? I:int), (r1v1 ?? I:java.lang.Object), (r9v0 ?? I:java.lang.Object) VIRTUAL call: android.util.SparseArrayMap.add(int, java.lang.Object, java.lang.Object):java.lang.Object (LINE:637)
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
        	at jadx.core.utils.InsnRemover.perform(InsnRemover.java:72)
        	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:54)
        */
    /*  JADX ERROR: Method generation error
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r11v0 ??
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:237)
        	at jadx.core.codegen.MethodGen.addMethodArguments(MethodGen.java:223)
        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:168)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:401)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:335)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:301)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void unprepareFromExecutionLocked(JobStatus jobStatus) {
        Timer timer;
        Timer timer2 = (Timer) this.mPkgTimers.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        if (timer2 != null) {
            timer2.stopTrackingJob(jobStatus);
        }
        if (jobStatus.isRequestedExpeditedJob() && (timer = (Timer) this.mEJPkgTimers.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName())) != null) {
            timer.stopTrackingJob(jobStatus);
        }
        this.mTopStartedJobs.remove(jobStatus);
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void maybeStopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if (jobStatus.clearTrackingController(64)) {
            unprepareFromExecutionLocked(jobStatus);
            int sourceUserId = jobStatus.getSourceUserId();
            String sourcePackageName = jobStatus.getSourcePackageName();
            ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(sourceUserId, sourcePackageName);
            if (arraySet != null && arraySet.remove(jobStatus) && arraySet.size() == 0) {
                this.mInQuotaAlarmQueue.removeAlarmForKey(UserPackage.of(sourceUserId, sourcePackageName));
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(String str, int i) {
        if (str == null) {
            Slog.wtf(TAG, "Told app removed but given null package name.");
            return;
        }
        clearAppStatsLocked(UserHandle.getUserId(i), str);
        if (this.mService.getPackagesForUidLocked(i) == null) {
            this.mForegroundUids.delete(i);
            this.mTempAllowlistCache.delete(i);
            this.mTempAllowlistGraceCache.delete(i);
            this.mTopAppCache.delete(i);
            this.mTopAppGraceCache.delete(i);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserAddedLocked(int i) {
        cacheInstallerPackagesLocked(i);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int i) {
        this.mTrackedJobs.delete(i);
        this.mPkgTimers.delete(i);
        this.mEJPkgTimers.delete(i);
        this.mTimingEvents.delete(i);
        this.mEJTimingSessions.delete(i);
        this.mInQuotaAlarmQueue.removeAlarmsForUserId(i);
        this.mExecutionStatsCache.delete(i);
        this.mEJStats.delete(i);
        this.mSystemInstallers.remove(i);
        this.mTopAppTrackers.delete(i);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onBatteryStateChangedLocked() {
        handleNewChargingStateLocked();
    }

    public void clearAppStatsLocked(int i, String str) {
        this.mTrackedJobs.delete(i, str);
        Timer timer = (Timer) this.mPkgTimers.delete(i, str);
        if (timer != null && timer.isActive()) {
            Slog.e(TAG, "clearAppStats called before Timer turned off.");
            timer.dropEverythingLocked();
        }
        Timer timer2 = (Timer) this.mEJPkgTimers.delete(i, str);
        if (timer2 != null && timer2.isActive()) {
            Slog.e(TAG, "clearAppStats called before EJ Timer turned off.");
            timer2.dropEverythingLocked();
        }
        this.mTimingEvents.delete(i, str);
        this.mEJTimingSessions.delete(i, str);
        this.mInQuotaAlarmQueue.removeAlarmForKey(UserPackage.of(i, str));
        this.mExecutionStatsCache.delete(i, str);
        this.mEJStats.delete(i, str);
        this.mTopAppTrackers.delete(i, str);
    }

    private void cacheInstallerPackagesLocked(int i) {
        List installedPackagesAsUser = this.mContext.getPackageManager().getInstalledPackagesAsUser(SYSTEM_APP_CHECK_FLAGS, i);
        for (int size = installedPackagesAsUser.size() - 1; size >= 0; size--) {
            PackageInfo packageInfo = (PackageInfo) installedPackagesAsUser.get(size);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (ArrayUtils.indexOf(packageInfo.requestedPermissions, "android.permission.INSTALL_PACKAGES") >= 0 && applicationInfo != null && this.mContext.checkPermission("android.permission.INSTALL_PACKAGES", -1, applicationInfo.uid) == 0) {
                this.mSystemInstallers.add(UserHandle.getUserId(applicationInfo.uid), packageInfo.packageName);
            }
        }
    }

    private boolean isUidInForeground(int i) {
        boolean z;
        if (UserHandle.isCore(i)) {
            return true;
        }
        synchronized (this.mLock) {
            z = this.mForegroundUids.get(i);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTopStartedJobLocked(JobStatus jobStatus) {
        return this.mTopStartedJobs.contains(jobStatus);
    }

    @GuardedBy({"mLock"})
    public long getMaxJobExecutionTimeMsLocked(JobStatus jobStatus) {
        if (!jobStatus.shouldTreatAsExpeditedJob()) {
            if (this.mService.isBatteryCharging() || this.mTopAppCache.get(jobStatus.getSourceUid()) || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid())) {
                return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            }
            return getTimeUntilQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        }
        if (this.mService.isBatteryCharging()) {
            return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
        }
        if (jobStatus.getEffectiveStandbyBucket() == 6) {
            return Math.max(this.mEJLimitsMs[6] / 2, getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
        }
        if (this.mTopAppCache.get(jobStatus.getSourceUid()) || isTopStartedJobLocked(jobStatus)) {
            return Math.max(this.mEJLimitsMs[0] / 2, getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
        }
        if (isUidInForeground(jobStatus.getSourceUid())) {
            return Math.max(this.mEJLimitsMs[1] / 2, getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
        }
        return getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasTempAllowlistExemptionLocked(int i, int i2, long j) {
        if (i2 == 5 || i2 == 4) {
            return false;
        }
        return this.mTempAllowlistCache.get(i) || j < this.mTempAllowlistGraceCache.get(i);
    }

    @GuardedBy({"mLock"})
    public boolean isWithinEJQuotaLocked(JobStatus jobStatus) {
        if (!this.mIsEnabled || isQuotaFreeLocked(jobStatus.getEffectiveStandbyBucket()) || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid())) {
            return true;
        }
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        if (hasTempAllowlistExemptionLocked(jobStatus.getSourceUid(), jobStatus.getEffectiveStandbyBucket(), millis)) {
            return true;
        }
        return (this.mTopAppCache.get(jobStatus.getSourceUid()) || (millis > this.mTopAppGraceCache.get(jobStatus.getSourceUid()) ? 1 : (millis == this.mTopAppGraceCache.get(jobStatus.getSourceUid()) ? 0 : -1)) < 0) || 0 < getRemainingEJExecutionTimeLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
    }

    @VisibleForTesting
    ShrinkableDebits getEJDebitsLocked(int i, String str) {
        ShrinkableDebits shrinkableDebits = (ShrinkableDebits) this.mEJStats.get(i, str);
        if (shrinkableDebits != null) {
            return shrinkableDebits;
        }
        ShrinkableDebits shrinkableDebits2 = new ShrinkableDebits(JobSchedulerService.standbyBucketForPackage(str, i, JobSchedulerService.sElapsedRealtimeClock.millis()));
        this.mEJStats.add(i, str, shrinkableDebits2);
        return shrinkableDebits2;
    }

    @VisibleForTesting
    boolean isWithinQuotaLocked(JobStatus jobStatus) {
        if (this.mIsEnabled) {
            return jobStatus.shouldTreatAsUserInitiatedJob() || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid()) || isWithinQuotaLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), jobStatus.getEffectiveStandbyBucket());
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean isQuotaFreeLocked(int i) {
        return this.mService.isBatteryCharging() && i != 5;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    boolean isWithinQuotaLocked(int i, String str, int i2) {
        if (!this.mIsEnabled) {
            return true;
        }
        if (i2 == 4) {
            return false;
        }
        if (isQuotaFreeLocked(i2)) {
            return true;
        }
        ExecutionStats executionStatsLocked = getExecutionStatsLocked(i, str, i2);
        return getRemainingExecutionTimeLocked(executionStatsLocked) > 0 && isUnderJobCountQuotaLocked(executionStatsLocked, i2) && isUnderSessionCountQuotaLocked(executionStatsLocked, i2);
    }

    private boolean isUnderJobCountQuotaLocked(ExecutionStats executionStats, int i) {
        return ((executionStats.jobRateLimitExpirationTimeElapsed > JobSchedulerService.sElapsedRealtimeClock.millis() ? 1 : (executionStats.jobRateLimitExpirationTimeElapsed == JobSchedulerService.sElapsedRealtimeClock.millis() ? 0 : -1)) <= 0 || executionStats.jobCountInRateLimitingWindow < this.mMaxJobCountPerRateLimitingWindow) && executionStats.bgJobCountInWindow < executionStats.jobCountLimit;
    }

    private boolean isUnderSessionCountQuotaLocked(ExecutionStats executionStats, int i) {
        return ((executionStats.sessionRateLimitExpirationTimeElapsed > JobSchedulerService.sElapsedRealtimeClock.millis() ? 1 : (executionStats.sessionRateLimitExpirationTimeElapsed == JobSchedulerService.sElapsedRealtimeClock.millis() ? 0 : -1)) <= 0 || executionStats.sessionCountInRateLimitingWindow < this.mMaxSessionCountPerRateLimitingWindow) && executionStats.sessionCountInWindow < executionStats.sessionCountLimit;
    }

    @VisibleForTesting
    long getRemainingExecutionTimeLocked(JobStatus jobStatus) {
        return getRemainingExecutionTimeLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), jobStatus.getEffectiveStandbyBucket());
    }

    @VisibleForTesting
    long getRemainingExecutionTimeLocked(int i, String str) {
        return getRemainingExecutionTimeLocked(i, str, JobSchedulerService.standbyBucketForPackage(str, i, JobSchedulerService.sElapsedRealtimeClock.millis()));
    }

    private long getRemainingExecutionTimeLocked(int i, String str, int i2) {
        if (i2 == 4) {
            return 0L;
        }
        return getRemainingExecutionTimeLocked(getExecutionStatsLocked(i, str, i2));
    }

    private long getRemainingExecutionTimeLocked(ExecutionStats executionStats) {
        return Math.min(executionStats.allowedTimePerPeriodMs - executionStats.executionTimeInWindowMs, this.mMaxExecutionTimeMs - executionStats.executionTimeInMaxPeriodMs);
    }

    @VisibleForTesting
    long getRemainingEJExecutionTimeLocked(int i, String str) {
        ShrinkableDebits eJDebitsLocked = getEJDebitsLocked(i, str);
        if (eJDebitsLocked.getStandbyBucketLocked() == 4) {
            return 0L;
        }
        long eJLimitMsLocked = getEJLimitMsLocked(i, str, eJDebitsLocked.getStandbyBucketLocked()) - eJDebitsLocked.getTallyLocked();
        List list = (List) this.mEJTimingSessions.get(i, str);
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        long j = millis - this.mEJLimitWindowSizeMs;
        if (list != null) {
            while (true) {
                if (list.size() <= 0) {
                    break;
                }
                TimingSession timingSession = (TimingSession) list.get(0);
                long j2 = timingSession.endTimeElapsed;
                if (j2 < j) {
                    long j3 = j2 - timingSession.startTimeElapsed;
                    eJLimitMsLocked += j3;
                    eJDebitsLocked.transactLocked(-j3);
                    list.remove(0);
                } else {
                    long j4 = timingSession.startTimeElapsed;
                    if (j4 < j) {
                        eJLimitMsLocked += j - j4;
                    }
                }
            }
        }
        TopAppTimer topAppTimer = (TopAppTimer) this.mTopAppTrackers.get(i, str);
        if (topAppTimer != null && topAppTimer.isActive()) {
            eJLimitMsLocked += topAppTimer.getPendingReward(millis);
        }
        Timer timer = (Timer) this.mEJPkgTimers.get(i, str);
        return timer == null ? eJLimitMsLocked : eJLimitMsLocked - timer.getCurrentDuration(JobSchedulerService.sElapsedRealtimeClock.millis());
    }

    private long getEJLimitMsLocked(int i, String str, int i2) {
        long j = this.mEJLimitsMs[i2];
        return this.mSystemInstallers.contains(i, str) ? j + this.mEjLimitAdditionInstallerMs : j;
    }

    @VisibleForTesting
    long getTimeUntilQuotaConsumedLocked(int i, String str) {
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        int standbyBucketForPackage = JobSchedulerService.standbyBucketForPackage(str, i, millis);
        if (standbyBucketForPackage == 4) {
            return 0L;
        }
        List<TimedEvent> list = (List) this.mTimingEvents.get(i, str);
        ExecutionStats executionStatsLocked = getExecutionStatsLocked(i, str, standbyBucketForPackage);
        if (list == null || list.size() == 0) {
            long j = executionStatsLocked.windowSizeMs;
            long j2 = this.mAllowedTimePerPeriodMs[standbyBucketForPackage];
            return j == j2 ? this.mMaxExecutionTimeMs : j2;
        }
        long j3 = executionStatsLocked.windowSizeMs;
        long j4 = millis - j3;
        long j5 = millis - 86400000;
        long j6 = this.mAllowedTimePerPeriodMs[standbyBucketForPackage];
        long j7 = j6 - executionStatsLocked.executionTimeInWindowMs;
        long j8 = this.mMaxExecutionTimeMs - executionStatsLocked.executionTimeInMaxPeriodMs;
        if (j3 == j6) {
            return calculateTimeUntilQuotaConsumedLocked(list, j5, j8, false);
        }
        return Math.min(calculateTimeUntilQuotaConsumedLocked(list, j5, j8, false), calculateTimeUntilQuotaConsumedLocked(list, j4, j7, true));
    }

    private long calculateTimeUntilQuotaConsumedLocked(List<TimedEvent> list, long j, long j2, boolean z) {
        long j3;
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis() - this.mQuotaBumpWindowSizeMs;
        int size = list.size();
        if (z) {
            int i = 0;
            j3 = j2;
            for (int i2 = size - 1; i2 >= 0; i2--) {
                TimedEvent timedEvent = list.get(i2);
                if (timedEvent instanceof QuotaBump) {
                    if (timedEvent.getEndTimeElapsed() < millis) {
                        break;
                    }
                    int i3 = i + 1;
                    if (i >= this.mQuotaBumpLimit) {
                        break;
                    }
                    j3 += this.mQuotaBumpAdditionalDurationMs;
                    i = i3;
                }
            }
        } else {
            j3 = j2;
        }
        long j4 = 0;
        long j5 = j;
        for (int i4 = 0; i4 < size; i4++) {
            TimedEvent timedEvent2 = list.get(i4);
            if (!(timedEvent2 instanceof QuotaBump)) {
                TimingSession timingSession = (TimingSession) timedEvent2;
                long j6 = timingSession.endTimeElapsed;
                if (j6 < j) {
                    continue;
                } else {
                    long j7 = timingSession.startTimeElapsed;
                    if (j7 <= j) {
                        j4 += j6 - j;
                    } else {
                        long j8 = j7 - j5;
                        if (j8 > j3) {
                            break;
                        }
                        j4 += (j6 - j7) + j8;
                        j3 -= j8;
                    }
                    j5 = j6;
                }
            }
        }
        long j9 = j4 + j3;
        if (j9 > this.mMaxExecutionTimeMs) {
            Slog.wtf(TAG, "Calculated quota consumed time too high: " + j9);
        }
        return j9;
    }

    @VisibleForTesting
    long getTimeUntilEJQuotaConsumedLocked(int i, String str) {
        long j;
        List list;
        long j2;
        long j3;
        long remainingEJExecutionTimeLocked = getRemainingEJExecutionTimeLocked(i, str);
        List list2 = (List) this.mEJTimingSessions.get(i, str);
        if (list2 == null || list2.size() == 0) {
            return remainingEJExecutionTimeLocked;
        }
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        long eJLimitMsLocked = getEJLimitMsLocked(i, str, getEJDebitsLocked(i, str).getStandbyBucketLocked());
        long max = Math.max(0L, millis - this.mEJLimitWindowSizeMs);
        int i2 = 0;
        long j4 = 0;
        long j5 = 0;
        while (true) {
            if (i2 >= list2.size()) {
                j = eJLimitMsLocked;
                break;
            }
            TimingSession timingSession = (TimingSession) list2.get(i2);
            long j6 = timingSession.endTimeElapsed;
            if (j6 < max) {
                remainingEJExecutionTimeLocked += j6 - timingSession.startTimeElapsed;
                list2.remove(i2);
                i2--;
                j = eJLimitMsLocked;
            } else {
                j = eJLimitMsLocked;
                long j7 = timingSession.startTimeElapsed;
                if (j7 < max) {
                    j5 = j6 - max;
                } else {
                    long endTimeElapsed = j7 - (i2 == 0 ? max : ((TimedEvent) list2.get(i2 - 1)).getEndTimeElapsed());
                    long min = Math.min(remainingEJExecutionTimeLocked, endTimeElapsed);
                    j4 += min;
                    if (min == endTimeElapsed) {
                        list = list2;
                        j2 = max;
                        j5 += timingSession.endTimeElapsed - timingSession.startTimeElapsed;
                    } else {
                        list = list2;
                        j2 = max;
                    }
                    remainingEJExecutionTimeLocked -= min;
                    j3 = 0;
                    if (remainingEJExecutionTimeLocked <= 0) {
                        break;
                    }
                    i2++;
                    eJLimitMsLocked = j;
                    list2 = list;
                    max = j2;
                }
            }
            list = list2;
            j2 = max;
            j3 = 0;
            i2++;
            eJLimitMsLocked = j;
            list2 = list;
            max = j2;
        }
        return Math.min(j, j4 + j5 + remainingEJExecutionTimeLocked);
    }

    @VisibleForTesting
    ExecutionStats getExecutionStatsLocked(int i, String str, int i2) {
        return getExecutionStatsLocked(i, str, i2, true);
    }

    private ExecutionStats getExecutionStatsLocked(int i, String str, int i2, boolean z) {
        if (i2 == 4) {
            Slog.wtf(TAG, "getExecutionStatsLocked called for a NEVER app.");
            return new ExecutionStats();
        }
        ExecutionStats[] executionStatsArr = (ExecutionStats[]) this.mExecutionStatsCache.get(i, str);
        if (executionStatsArr == null) {
            executionStatsArr = new ExecutionStats[this.mBucketPeriodsMs.length];
            this.mExecutionStatsCache.add(i, str, executionStatsArr);
        }
        ExecutionStats executionStats = executionStatsArr[i2];
        if (executionStats == null) {
            executionStats = new ExecutionStats();
            executionStatsArr[i2] = executionStats;
        }
        if (z) {
            long j = this.mAllowedTimePerPeriodMs[i2];
            long j2 = this.mBucketPeriodsMs[i2];
            int i3 = this.mMaxBucketJobCounts[i2];
            int i4 = this.mMaxBucketSessionCounts[i2];
            Timer timer = (Timer) this.mPkgTimers.get(i, str);
            if ((timer != null && timer.isActive()) || executionStats.expirationTimeElapsed <= JobSchedulerService.sElapsedRealtimeClock.millis() || executionStats.allowedTimePerPeriodMs != j || executionStats.windowSizeMs != j2 || executionStats.jobCountLimit != i3 || executionStats.sessionCountLimit != i4) {
                executionStats.allowedTimePerPeriodMs = j;
                executionStats.windowSizeMs = j2;
                executionStats.jobCountLimit = i3;
                executionStats.sessionCountLimit = i4;
                updateExecutionStatsLocked(i, str, executionStats);
            }
        }
        return executionStats;
    }

    @VisibleForTesting
    void updateExecutionStatsLocked(int i, String str, ExecutionStats executionStats) {
        long j;
        long j2;
        long j3;
        long j4;
        long j5;
        executionStats.executionTimeInWindowMs = 0L;
        executionStats.bgJobCountInWindow = 0;
        executionStats.executionTimeInMaxPeriodMs = 0L;
        executionStats.bgJobCountInMaxPeriod = 0;
        executionStats.sessionCountInWindow = 0;
        if (executionStats.jobCountLimit == 0 || executionStats.sessionCountLimit == 0) {
            executionStats.inQuotaTimeElapsed = JobStatus.NO_LATEST_RUNTIME;
        } else {
            executionStats.inQuotaTimeElapsed = 0L;
        }
        long j6 = executionStats.allowedTimePerPeriodMs - this.mQuotaBufferMs;
        Timer timer = (Timer) this.mPkgTimers.get(i, str);
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        executionStats.expirationTimeElapsed = millis + 86400000;
        if (timer != null && timer.isActive()) {
            long currentDuration = timer.getCurrentDuration(millis);
            executionStats.executionTimeInMaxPeriodMs = currentDuration;
            executionStats.executionTimeInWindowMs = currentDuration;
            int bgJobCount = timer.getBgJobCount();
            executionStats.bgJobCountInMaxPeriod = bgJobCount;
            executionStats.bgJobCountInWindow = bgJobCount;
            executionStats.expirationTimeElapsed = millis;
            if (executionStats.executionTimeInWindowMs >= j6) {
                executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, (millis - j6) + executionStats.windowSizeMs);
            }
            long j7 = executionStats.executionTimeInMaxPeriodMs;
            long j8 = this.mMaxExecutionTimeIntoQuotaMs;
            if (j7 >= j8) {
                executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, (millis - j8) + 86400000);
            }
            if (executionStats.bgJobCountInWindow >= executionStats.jobCountLimit) {
                executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, executionStats.windowSizeMs + millis);
            }
        }
        List list = (List) this.mTimingEvents.get(i, str);
        if (list == null || list.size() == 0) {
            return;
        }
        long j9 = millis - executionStats.windowSizeMs;
        long j10 = millis - 86400000;
        long j11 = millis - this.mQuotaBumpWindowSizeMs;
        int size = list.size() - 1;
        int i2 = 0;
        long j12 = JobStatus.NO_LATEST_RUNTIME;
        while (size >= 0) {
            TimedEvent timedEvent = (TimedEvent) list.get(size);
            if (timedEvent.getEndTimeElapsed() < j11) {
                break;
            }
            j = j10;
            if (i2 >= this.mQuotaBumpLimit) {
                break;
            }
            if (timedEvent instanceof QuotaBump) {
                j5 = j6;
                executionStats.allowedTimePerPeriodMs += this.mQuotaBumpAdditionalDurationMs;
                executionStats.jobCountLimit += this.mQuotaBumpAdditionalJobCount;
                executionStats.sessionCountLimit += this.mQuotaBumpAdditionalSessionCount;
                i2++;
                j12 = Math.min(j12, timedEvent.getEndTimeElapsed() - j11);
            } else {
                j5 = j6;
            }
            size--;
            j10 = j;
            j6 = j5;
        }
        j = j10;
        long j13 = j6;
        TimingSession timingSession = null;
        int i3 = 0;
        int i4 = size;
        while (i4 >= 0) {
            TimedEvent timedEvent2 = (TimedEvent) list.get(i4);
            if (timedEvent2 instanceof QuotaBump) {
                j2 = j9;
            } else {
                TimingSession timingSession2 = (TimingSession) timedEvent2;
                if (j9 < timingSession2.endTimeElapsed) {
                    long j14 = timingSession2.startTimeElapsed;
                    if (j9 < j14) {
                        j4 = Math.min(j12, j14 - j9);
                    } else {
                        j14 = j9;
                        j4 = 0;
                    }
                    j2 = j9;
                    long j15 = executionStats.executionTimeInWindowMs + (timingSession2.endTimeElapsed - j14);
                    executionStats.executionTimeInWindowMs = j15;
                    executionStats.bgJobCountInWindow += timingSession2.bgJobCount;
                    if (j15 >= j13) {
                        executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, ((j14 + j15) - j13) + executionStats.windowSizeMs);
                    }
                    if (executionStats.bgJobCountInWindow >= executionStats.jobCountLimit) {
                        executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, timingSession2.endTimeElapsed + executionStats.windowSizeMs);
                    }
                    if (!(timingSession != null && timingSession.startTimeElapsed - timingSession2.endTimeElapsed <= this.mTimingSessionCoalescingDurationMs) && (i3 = i3 + 1) >= executionStats.sessionCountLimit) {
                        executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, timingSession2.endTimeElapsed + executionStats.windowSizeMs);
                    }
                    j12 = j4;
                } else {
                    j2 = j9;
                }
                long j16 = timingSession2.startTimeElapsed;
                if (j < j16) {
                    executionStats.executionTimeInMaxPeriodMs += timingSession2.endTimeElapsed - j16;
                    executionStats.bgJobCountInMaxPeriod += timingSession2.bgJobCount;
                    long min = Math.min(j12, j16 - j);
                    long j17 = executionStats.executionTimeInMaxPeriodMs;
                    long j18 = this.mMaxExecutionTimeIntoQuotaMs;
                    if (j17 >= j18) {
                        j3 = min;
                        executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, ((timingSession2.startTimeElapsed + j17) - j18) + 86400000);
                    } else {
                        j3 = min;
                    }
                } else {
                    long j19 = timingSession2.endTimeElapsed;
                    if (j >= j19) {
                        break;
                    }
                    long j20 = executionStats.executionTimeInMaxPeriodMs + (j19 - j);
                    executionStats.executionTimeInMaxPeriodMs = j20;
                    executionStats.bgJobCountInMaxPeriod += timingSession2.bgJobCount;
                    long j21 = this.mMaxExecutionTimeIntoQuotaMs;
                    if (j20 >= j21) {
                        executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, ((j + j20) - j21) + 86400000);
                    }
                    j3 = 0;
                }
                timingSession = timingSession2;
                j12 = j3;
            }
            i4--;
            j9 = j2;
        }
        executionStats.expirationTimeElapsed = millis + j12;
        executionStats.sessionCountInWindow = i3;
    }

    @VisibleForTesting
    void invalidateAllExecutionStatsLocked() {
        final long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        this.mExecutionStatsCache.forEach(new Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QuotaController.lambda$invalidateAllExecutionStatsLocked$0(millis, (QuotaController.ExecutionStats[]) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$invalidateAllExecutionStatsLocked$0(long j, ExecutionStats[] executionStatsArr) {
        if (executionStatsArr != null) {
            for (ExecutionStats executionStats : executionStatsArr) {
                if (executionStats != null) {
                    executionStats.expirationTimeElapsed = j;
                }
            }
        }
    }

    @VisibleForTesting
    void invalidateAllExecutionStatsLocked(int i, String str) {
        ExecutionStats[] executionStatsArr = (ExecutionStats[]) this.mExecutionStatsCache.get(i, str);
        if (executionStatsArr != null) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            for (ExecutionStats executionStats : executionStatsArr) {
                if (executionStats != null) {
                    executionStats.expirationTimeElapsed = millis;
                }
            }
        }
    }

    @VisibleForTesting
    void incrementJobCountLocked(int i, String str, int i2) {
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        ExecutionStats[] executionStatsArr = (ExecutionStats[]) this.mExecutionStatsCache.get(i, str);
        if (executionStatsArr == null) {
            executionStatsArr = new ExecutionStats[this.mBucketPeriodsMs.length];
            this.mExecutionStatsCache.add(i, str, executionStatsArr);
        }
        for (int i3 = 0; i3 < executionStatsArr.length; i3++) {
            ExecutionStats executionStats = executionStatsArr[i3];
            if (executionStats == null) {
                executionStats = new ExecutionStats();
                executionStatsArr[i3] = executionStats;
            }
            if (executionStats.jobRateLimitExpirationTimeElapsed <= millis) {
                executionStats.jobRateLimitExpirationTimeElapsed = this.mRateLimitingWindowMs + millis;
                executionStats.jobCountInRateLimitingWindow = 0;
            }
            executionStats.jobCountInRateLimitingWindow += i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void incrementTimingSessionCountLocked(int i, String str) {
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        ExecutionStats[] executionStatsArr = (ExecutionStats[]) this.mExecutionStatsCache.get(i, str);
        if (executionStatsArr == null) {
            executionStatsArr = new ExecutionStats[this.mBucketPeriodsMs.length];
            this.mExecutionStatsCache.add(i, str, executionStatsArr);
        }
        for (int i2 = 0; i2 < executionStatsArr.length; i2++) {
            ExecutionStats executionStats = executionStatsArr[i2];
            if (executionStats == null) {
                executionStats = new ExecutionStats();
                executionStatsArr[i2] = executionStats;
            }
            if (executionStats.sessionRateLimitExpirationTimeElapsed <= millis) {
                executionStats.sessionRateLimitExpirationTimeElapsed = this.mRateLimitingWindowMs + millis;
                executionStats.sessionCountInRateLimitingWindow = 0;
            }
            executionStats.sessionCountInRateLimitingWindow++;
        }
    }

    @VisibleForTesting
    void saveTimingSession(int i, String str, TimingSession timingSession, boolean z) {
        saveTimingSession(i, str, timingSession, z, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveTimingSession(int i, String str, TimingSession timingSession, boolean z, long j) {
        synchronized (this.mLock) {
            SparseArrayMap<String, List<TimedEvent>> sparseArrayMap = z ? this.mEJTimingSessions : this.mTimingEvents;
            List list = (List) sparseArrayMap.get(i, str);
            if (list == null) {
                list = new ArrayList();
                sparseArrayMap.add(i, str, list);
            }
            list.add(timingSession);
            if (z) {
                getEJDebitsLocked(i, str).transactLocked((timingSession.endTimeElapsed - timingSession.startTimeElapsed) + j);
            } else {
                invalidateAllExecutionStatsLocked(i, str);
                maybeScheduleCleanupAlarmLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantRewardForInstantEvent(int i, String str, long j) {
        if (j == 0) {
            return;
        }
        synchronized (this.mLock) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            if (transactQuotaLocked(i, str, millis, getEJDebitsLocked(i, str), j)) {
                this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForPkgLocked(millis, i, str));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean transactQuotaLocked(int i, String str, long j, ShrinkableDebits shrinkableDebits, long j2) {
        Timer timer;
        long tallyLocked = shrinkableDebits.getTallyLocked();
        long transactLocked = shrinkableDebits.transactLocked(-j2);
        if (DEBUG) {
            Slog.d(TAG, "debits overflowed by " + transactLocked);
        }
        boolean z = tallyLocked != shrinkableDebits.getTallyLocked();
        if (transactLocked == 0 || (timer = (Timer) this.mEJPkgTimers.get(i, str)) == null || !timer.isActive()) {
            return z;
        }
        timer.updateDebitAdjustment(j, transactLocked);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class EarliestEndTimeFunctor implements Consumer<List<TimedEvent>> {
        public long earliestEndElapsed;

        private EarliestEndTimeFunctor() {
            this.earliestEndElapsed = JobStatus.NO_LATEST_RUNTIME;
        }

        @Override // java.util.function.Consumer
        public void accept(List<TimedEvent> list) {
            if (list == null || list.size() <= 0) {
                return;
            }
            this.earliestEndElapsed = Math.min(this.earliestEndElapsed, list.get(0).getEndTimeElapsed());
        }

        void reset() {
            this.earliestEndElapsed = JobStatus.NO_LATEST_RUNTIME;
        }
    }

    @VisibleForTesting
    void maybeScheduleCleanupAlarmLocked() {
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        if (this.mNextCleanupTimeElapsed > millis) {
            if (DEBUG) {
                Slog.v(TAG, "Not scheduling cleanup since there's already one at " + this.mNextCleanupTimeElapsed + " (in " + (this.mNextCleanupTimeElapsed - millis) + "ms)");
                return;
            }
            return;
        }
        this.mEarliestEndTimeFunctor.reset();
        this.mTimingEvents.forEach(this.mEarliestEndTimeFunctor);
        this.mEJTimingSessions.forEach(this.mEarliestEndTimeFunctor);
        long j = this.mEarliestEndTimeFunctor.earliestEndElapsed;
        if (j == JobStatus.NO_LATEST_RUNTIME) {
            if (DEBUG) {
                Slog.d(TAG, "Didn't find a time to schedule cleanup");
                return;
            }
            return;
        }
        long j2 = j + 86400000;
        long j3 = this.mNextCleanupTimeElapsed;
        if (j2 - j3 <= 600000) {
            j2 = j3 + 600000;
        }
        long j4 = j2;
        this.mNextCleanupTimeElapsed = j4;
        this.mAlarmManager.set(3, j4, ALARM_TAG_CLEANUP, this.mSessionCleanupAlarmListener, this.mHandler);
        if (DEBUG) {
            Slog.d(TAG, "Scheduled next cleanup for " + this.mNextCleanupTimeElapsed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class TimerChargingUpdateFunctor implements Consumer<Timer> {
        private boolean mIsCharging;
        private long mNowElapsed;

        private TimerChargingUpdateFunctor() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setStatus(long j, boolean z) {
            this.mNowElapsed = j;
            this.mIsCharging = z;
        }

        @Override // java.util.function.Consumer
        public void accept(Timer timer) {
            if (JobSchedulerService.standbyBucketForPackage(timer.mPkg.packageName, timer.mPkg.userId, this.mNowElapsed) != 5) {
                timer.onStateChangedLocked(this.mNowElapsed, this.mIsCharging);
            }
        }
    }

    private void handleNewChargingStateLocked() {
        this.mTimerChargingUpdateFunctor.setStatus(JobSchedulerService.sElapsedRealtimeClock.millis(), this.mService.isBatteryCharging());
        if (DEBUG) {
            Slog.d(TAG, "handleNewChargingStateLocked: " + this.mService.isBatteryCharging());
        }
        this.mEJPkgTimers.forEach(this.mTimerChargingUpdateFunctor);
        this.mPkgTimers.forEach(this.mTimerChargingUpdateFunctor);
        AppSchedulingModuleThread.getHandler().post(new Runnable() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                QuotaController.this.lambda$handleNewChargingStateLocked$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleNewChargingStateLocked$1() {
        synchronized (this.mLock) {
            maybeUpdateAllConstraintsLocked();
        }
    }

    private void maybeUpdateAllConstraintsLocked() {
        ArraySet<JobStatus> arraySet = new ArraySet<>();
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        for (int i = 0; i < this.mTrackedJobs.numMaps(); i++) {
            int keyAt = this.mTrackedJobs.keyAt(i);
            for (int i2 = 0; i2 < this.mTrackedJobs.numElementsForKey(keyAt); i2++) {
                arraySet.addAll((ArraySet<? extends JobStatus>) maybeUpdateConstraintForPkgLocked(millis, keyAt, (String) this.mTrackedJobs.keyAt(i, i2)));
            }
        }
        if (arraySet.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(arraySet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArraySet<JobStatus> maybeUpdateConstraintForPkgLocked(long j, int i, String str) {
        JobStatus jobStatus;
        boolean z;
        int i2;
        boolean z2;
        ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(i, str);
        ArraySet<JobStatus> arraySet2 = new ArraySet<>();
        if (arraySet != null && arraySet.size() != 0) {
            boolean z3 = false;
            int standbyBucket = ((JobStatus) arraySet.valueAt(0)).getStandbyBucket();
            boolean isWithinQuotaLocked = isWithinQuotaLocked(i, str, standbyBucket);
            boolean z4 = true;
            int size = arraySet.size() - 1;
            boolean z5 = false;
            while (size >= 0) {
                JobStatus jobStatus2 = (JobStatus) arraySet.valueAt(size);
                boolean z6 = (jobStatus2.isRequestedExpeditedJob() && isWithinEJQuotaLocked(jobStatus2)) ? z4 : z3;
                if (isTopStartedJobLocked(jobStatus2)) {
                    if (jobStatus2.setQuotaConstraintSatisfied(j, z4)) {
                        arraySet2.add(jobStatus2);
                    }
                    jobStatus = jobStatus2;
                    z = z6;
                    i2 = size;
                    z2 = z4;
                } else if (standbyBucket != 6 && standbyBucket != 0 && standbyBucket == jobStatus2.getEffectiveStandbyBucket()) {
                    jobStatus = jobStatus2;
                    z = z6;
                    i2 = size;
                    z2 = z4;
                    if (setConstraintSatisfied(jobStatus2, j, isWithinQuotaLocked, z)) {
                        arraySet2.add(jobStatus);
                    }
                } else {
                    jobStatus = jobStatus2;
                    z = z6;
                    i2 = size;
                    z2 = z4;
                    if (setConstraintSatisfied(jobStatus, j, isWithinQuotaLocked(jobStatus), z)) {
                        arraySet2.add(jobStatus);
                    }
                }
                if (jobStatus.isRequestedExpeditedJob()) {
                    boolean z7 = z;
                    if (setExpeditedQuotaApproved(jobStatus, j, z7)) {
                        arraySet2.add(jobStatus);
                    }
                    z5 |= !z7;
                }
                size = i2 - 1;
                z4 = z2;
                z3 = false;
            }
            if (!isWithinQuotaLocked || z5) {
                maybeScheduleStartAlarmLocked(i, str, standbyBucket);
            } else {
                this.mInQuotaAlarmQueue.removeAlarmForKey(UserPackage.of(i, str));
            }
        }
        return arraySet2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class UidConstraintUpdater implements Consumer<JobStatus> {
        public final ArraySet<JobStatus> changedJobs;
        private final SparseArrayMap<String, Integer> mToScheduleStartAlarms;
        long mUpdateTimeElapsed;

        private UidConstraintUpdater() {
            this.mToScheduleStartAlarms = new SparseArrayMap<>();
            this.changedJobs = new ArraySet<>();
            this.mUpdateTimeElapsed = 0L;
        }

        void prepare() {
            this.mUpdateTimeElapsed = JobSchedulerService.sElapsedRealtimeClock.millis();
            this.changedJobs.clear();
        }

        @Override // java.util.function.Consumer
        public void accept(JobStatus jobStatus) {
            boolean isWithinEJQuotaLocked = jobStatus.isRequestedExpeditedJob() ? QuotaController.this.isWithinEJQuotaLocked(jobStatus) : false;
            QuotaController quotaController = QuotaController.this;
            if (quotaController.setConstraintSatisfied(jobStatus, this.mUpdateTimeElapsed, quotaController.isWithinQuotaLocked(jobStatus), isWithinEJQuotaLocked)) {
                this.changedJobs.add(jobStatus);
            }
            if (QuotaController.this.setExpeditedQuotaApproved(jobStatus, this.mUpdateTimeElapsed, isWithinEJQuotaLocked)) {
                this.changedJobs.add(jobStatus);
            }
            int sourceUserId = jobStatus.getSourceUserId();
            String sourcePackageName = jobStatus.getSourcePackageName();
            int standbyBucket = jobStatus.getStandbyBucket();
            if (isWithinEJQuotaLocked && QuotaController.this.isWithinQuotaLocked(sourceUserId, sourcePackageName, standbyBucket)) {
                QuotaController.this.mInQuotaAlarmQueue.removeAlarmForKey(UserPackage.of(sourceUserId, sourcePackageName));
            } else {
                this.mToScheduleStartAlarms.add(sourceUserId, sourcePackageName, Integer.valueOf(standbyBucket));
            }
        }

        void postProcess() {
            for (int i = 0; i < this.mToScheduleStartAlarms.numMaps(); i++) {
                int keyAt = this.mToScheduleStartAlarms.keyAt(i);
                for (int i2 = 0; i2 < this.mToScheduleStartAlarms.numElementsForKey(keyAt); i2++) {
                    String str = (String) this.mToScheduleStartAlarms.keyAt(i, i2);
                    QuotaController.this.maybeScheduleStartAlarmLocked(keyAt, str, ((Integer) this.mToScheduleStartAlarms.get(keyAt, str)).intValue());
                }
            }
        }

        void reset() {
            this.mToScheduleStartAlarms.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public ArraySet<JobStatus> maybeUpdateConstraintForUidLocked(int i) {
        this.mUpdateUidConstraints.prepare();
        this.mService.getJobStore().forEachJobForSourceUid(i, this.mUpdateUidConstraints);
        this.mUpdateUidConstraints.postProcess();
        this.mUpdateUidConstraints.reset();
        return this.mUpdateUidConstraints.changedJobs;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x012d  */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void maybeScheduleStartAlarmLocked(int i, String str, int i2) {
        long j;
        long j2;
        long j3;
        long j4;
        List list;
        if (i2 == 4) {
            return;
        }
        ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(i, str);
        if (arraySet == null || arraySet.size() == 0) {
            Slog.e(TAG, "maybeScheduleStartAlarmLocked called for " + StateController.packageToString(i, str) + " that has no jobs");
            this.mInQuotaAlarmQueue.removeAlarmForKey(UserPackage.of(i, str));
            return;
        }
        ExecutionStats executionStatsLocked = getExecutionStatsLocked(i, str, i2);
        boolean isUnderJobCountQuotaLocked = isUnderJobCountQuotaLocked(executionStatsLocked, i2);
        boolean isUnderSessionCountQuotaLocked = isUnderSessionCountQuotaLocked(executionStatsLocked, i2);
        long remainingEJExecutionTimeLocked = getRemainingEJExecutionTimeLocked(i, str);
        boolean z = executionStatsLocked.executionTimeInWindowMs < this.mAllowedTimePerPeriodMs[i2] && executionStatsLocked.executionTimeInMaxPeriodMs < this.mMaxExecutionTimeMs && isUnderJobCountQuotaLocked && isUnderSessionCountQuotaLocked;
        if (z && remainingEJExecutionTimeLocked > 0) {
            if (DEBUG) {
                Slog.e(TAG, "maybeScheduleStartAlarmLocked called for " + StateController.packageToString(i, str) + " even though it already has " + getRemainingExecutionTimeLocked(i, str, i2) + "ms in its quota.");
            }
            this.mInQuotaAlarmQueue.removeAlarmForKey(UserPackage.of(i, str));
            this.mHandler.obtainMessage(2, i, 0, str).sendToTarget();
            return;
        }
        long j5 = JobStatus.NO_LATEST_RUNTIME;
        boolean z2 = z;
        if (z) {
            j = 0;
            j2 = Long.MAX_VALUE;
        } else {
            j2 = executionStatsLocked.inQuotaTimeElapsed;
            if (!isUnderJobCountQuotaLocked && executionStatsLocked.bgJobCountInWindow < executionStatsLocked.jobCountLimit) {
                j2 = Math.max(j2, executionStatsLocked.jobRateLimitExpirationTimeElapsed);
            }
            if (!isUnderSessionCountQuotaLocked && executionStatsLocked.sessionCountInWindow < executionStatsLocked.sessionCountLimit) {
                j2 = Math.max(j2, executionStatsLocked.sessionRateLimitExpirationTimeElapsed);
            }
            j = 0;
        }
        if (remainingEJExecutionTimeLocked <= j) {
            long eJLimitMsLocked = getEJLimitMsLocked(i, str, i2) - this.mQuotaBufferMs;
            Timer timer = (Timer) this.mEJPkgTimers.get(i, str);
            if (timer == null || !timer.isActive()) {
                j3 = 0;
            } else {
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                j4 = timer.getCurrentDuration(millis) + 0;
                if (j4 >= eJLimitMsLocked) {
                    j3 = j4;
                    j5 = (millis - eJLimitMsLocked) + this.mEJLimitWindowSizeMs;
                } else {
                    list = (List) this.mEJTimingSessions.get(i, str);
                    if (list == null) {
                        int size = list.size() - 1;
                        while (true) {
                            if (size < 0) {
                                break;
                            }
                            TimingSession timingSession = (TimingSession) list.get(size);
                            long j6 = timingSession.endTimeElapsed;
                            long j7 = timingSession.startTimeElapsed;
                            j4 += j6 - j7;
                            if (j4 >= eJLimitMsLocked) {
                                j5 = j7 + (j4 - eJLimitMsLocked) + this.mEJLimitWindowSizeMs;
                                break;
                            }
                            size--;
                        }
                    } else if ((timer == null || !timer.isActive()) && z2) {
                        Slog.wtf(TAG, StateController.packageToString(i, str) + " has 0 EJ quota without running anything");
                        return;
                    }
                }
            }
            j4 = j3;
            list = (List) this.mEJTimingSessions.get(i, str);
            if (list == null) {
            }
        }
        long min = Math.min(j2, j5);
        if (min <= JobSchedulerService.sElapsedRealtimeClock.millis()) {
            long millis2 = JobSchedulerService.sElapsedRealtimeClock.millis();
            Slog.wtf(TAG, "In quota time is " + (millis2 - min) + "ms old. Now=" + millis2 + ", inQuotaTime=" + min + ": " + executionStatsLocked);
            min = 300000 + millis2;
        }
        this.mInQuotaAlarmQueue.addAlarm(UserPackage.of(i, str), min);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setConstraintSatisfied(JobStatus jobStatus, long j, boolean z, boolean z2) {
        if (jobStatus.startedAsExpeditedJob) {
            z = z2;
        } else if (!this.mService.isCurrentlyRunningLocked(jobStatus)) {
            z = z2 || z;
        }
        if (!z && jobStatus.getWhenStandbyDeferred() == 0) {
            jobStatus.setWhenStandbyDeferred(j);
        }
        return jobStatus.setQuotaConstraintSatisfied(j, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setExpeditedQuotaApproved(JobStatus jobStatus, long j, boolean z) {
        if (!jobStatus.setExpeditedJobQuotaApproved(j, z)) {
            return false;
        }
        this.mBackgroundJobsController.evaluateStateLocked(jobStatus);
        this.mConnectivityController.evaluateStateLocked(jobStatus);
        if (!z || !jobStatus.isReady()) {
            return true;
        }
        this.mStateChangedListener.onRunJobNow(jobStatus);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class TimingSession implements TimedEvent {
        public final int bgJobCount;
        public final long endTimeElapsed;
        private final int mHashCode;
        public final long startTimeElapsed;

        TimingSession(long j, long j2, int i) {
            this.startTimeElapsed = j;
            this.endTimeElapsed = j2;
            this.bgJobCount = i;
            this.mHashCode = ((((0 + QuotaController.hashLong(j)) * 31) + QuotaController.hashLong(j2)) * 31) + i;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public long getEndTimeElapsed() {
            return this.endTimeElapsed;
        }

        public String toString() {
            return "TimingSession{" + this.startTimeElapsed + "->" + this.endTimeElapsed + ", " + this.bgJobCount + "}";
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TimingSession)) {
                return false;
            }
            TimingSession timingSession = (TimingSession) obj;
            return this.startTimeElapsed == timingSession.startTimeElapsed && this.endTimeElapsed == timingSession.endTimeElapsed && this.bgJobCount == timingSession.bgJobCount;
        }

        public int hashCode() {
            return this.mHashCode;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.print(this.startTimeElapsed);
            indentingPrintWriter.print(" -> ");
            indentingPrintWriter.print(this.endTimeElapsed);
            indentingPrintWriter.print(" (");
            indentingPrintWriter.print(this.endTimeElapsed - this.startTimeElapsed);
            indentingPrintWriter.print("), ");
            indentingPrintWriter.print(this.bgJobCount);
            indentingPrintWriter.print(" bg jobs.");
            indentingPrintWriter.println();
        }

        public void dump(ProtoOutputStream protoOutputStream, long j) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1112396529665L, this.startTimeElapsed);
            protoOutputStream.write(1112396529666L, this.endTimeElapsed);
            protoOutputStream.write(1120986464259L, this.bgJobCount);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class QuotaBump implements TimedEvent {
        public final long eventTimeElapsed;

        QuotaBump(long j) {
            this.eventTimeElapsed = j;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public long getEndTimeElapsed() {
            return this.eventTimeElapsed;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.print("Quota bump @ ");
            indentingPrintWriter.print(this.eventTimeElapsed);
            indentingPrintWriter.println();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ShrinkableDebits {
        private long mDebitTally = 0;
        private int mStandbyBucket;

        ShrinkableDebits(int i) {
            this.mStandbyBucket = i;
        }

        long getTallyLocked() {
            return this.mDebitTally;
        }

        long transactLocked(long j) {
            long j2;
            if (j < 0) {
                long abs = Math.abs(j);
                long j3 = this.mDebitTally;
                if (abs > j3) {
                    j2 = j3 + j;
                    this.mDebitTally = Math.max(0L, this.mDebitTally + j);
                    return j2;
                }
            }
            j2 = 0;
            this.mDebitTally = Math.max(0L, this.mDebitTally + j);
            return j2;
        }

        void setStandbyBucketLocked(int i) {
            this.mStandbyBucket = i;
        }

        int getStandbyBucketLocked() {
            return this.mStandbyBucket;
        }

        public String toString() {
            return "ShrinkableDebits { debit tally: " + this.mDebitTally + ", bucket: " + this.mStandbyBucket + " }";
        }

        void dumpLocked(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println(toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class Timer {
        private int mBgJobCount;
        private long mDebitAdjustment;
        private final UserPackage mPkg;
        private final boolean mRegularJobTimer;
        private final ArraySet<JobStatus> mRunningBgJobs = new ArraySet<>();
        private long mStartTimeElapsed;
        private final int mUid;

        Timer(int i, int i2, String str, boolean z) {
            this.mPkg = UserPackage.of(i2, str);
            this.mUid = i;
            this.mRegularJobTimer = z;
        }

        void startTrackingJobLocked(JobStatus jobStatus) {
            if (jobStatus.shouldTreatAsUserInitiatedJob()) {
                if (QuotaController.DEBUG) {
                    Slog.v(QuotaController.TAG, "Timer ignoring " + jobStatus.toShortString() + " because it's user-initiated");
                    return;
                }
                return;
            }
            if (QuotaController.this.isTopStartedJobLocked(jobStatus)) {
                if (QuotaController.DEBUG) {
                    Slog.v(QuotaController.TAG, "Timer ignoring " + jobStatus.toShortString() + " because isTop");
                    return;
                }
                return;
            }
            if (QuotaController.DEBUG) {
                Slog.v(QuotaController.TAG, "Starting to track " + jobStatus.toShortString());
            }
            if (this.mRunningBgJobs.add(jobStatus) && shouldTrackLocked()) {
                this.mBgJobCount++;
                if (this.mRegularJobTimer) {
                    QuotaController quotaController = QuotaController.this;
                    UserPackage userPackage = this.mPkg;
                    quotaController.incrementJobCountLocked(userPackage.userId, userPackage.packageName, 1);
                }
                if (this.mRunningBgJobs.size() == 1) {
                    this.mStartTimeElapsed = JobSchedulerService.sElapsedRealtimeClock.millis();
                    this.mDebitAdjustment = 0L;
                    if (this.mRegularJobTimer) {
                        QuotaController quotaController2 = QuotaController.this;
                        UserPackage userPackage2 = this.mPkg;
                        quotaController2.invalidateAllExecutionStatsLocked(userPackage2.userId, userPackage2.packageName);
                    }
                    scheduleCutoff();
                }
            }
        }

        void stopTrackingJob(JobStatus jobStatus) {
            if (QuotaController.DEBUG) {
                Slog.v(QuotaController.TAG, "Stopping tracking of " + jobStatus.toShortString());
            }
            synchronized (QuotaController.this.mLock) {
                if (this.mRunningBgJobs.size() == 0) {
                    if (QuotaController.DEBUG) {
                        Slog.d(QuotaController.TAG, "Timer isn't tracking any jobs but still told to stop");
                    }
                    return;
                }
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                UserPackage userPackage = this.mPkg;
                int standbyBucketForPackage = JobSchedulerService.standbyBucketForPackage(userPackage.packageName, userPackage.userId, millis);
                if (this.mRunningBgJobs.remove(jobStatus) && this.mRunningBgJobs.size() == 0 && !QuotaController.this.isQuotaFreeLocked(standbyBucketForPackage)) {
                    emitSessionLocked(millis);
                    cancelCutoff();
                }
            }
        }

        void updateDebitAdjustment(long j, long j2) {
            this.mDebitAdjustment = Math.max(this.mDebitAdjustment + j2, this.mStartTimeElapsed - j);
        }

        void dropEverythingLocked() {
            this.mRunningBgJobs.clear();
            cancelCutoff();
        }

        @GuardedBy({"mLock"})
        private void emitSessionLocked(long j) {
            int i = this.mBgJobCount;
            if (i <= 0) {
                return;
            }
            TimingSession timingSession = new TimingSession(this.mStartTimeElapsed, j, i);
            QuotaController quotaController = QuotaController.this;
            UserPackage userPackage = this.mPkg;
            quotaController.saveTimingSession(userPackage.userId, userPackage.packageName, timingSession, !this.mRegularJobTimer, this.mDebitAdjustment);
            this.mBgJobCount = 0;
            cancelCutoff();
            if (this.mRegularJobTimer) {
                QuotaController quotaController2 = QuotaController.this;
                UserPackage userPackage2 = this.mPkg;
                quotaController2.incrementTimingSessionCountLocked(userPackage2.userId, userPackage2.packageName);
            }
        }

        public boolean isActive() {
            boolean z;
            synchronized (QuotaController.this.mLock) {
                z = this.mBgJobCount > 0;
            }
            return z;
        }

        boolean isRunning(JobStatus jobStatus) {
            return this.mRunningBgJobs.contains(jobStatus);
        }

        long getCurrentDuration(long j) {
            long j2;
            synchronized (QuotaController.this.mLock) {
                j2 = !isActive() ? 0L : (j - this.mStartTimeElapsed) + this.mDebitAdjustment;
            }
            return j2;
        }

        int getBgJobCount() {
            int i;
            synchronized (QuotaController.this.mLock) {
                i = this.mBgJobCount;
            }
            return i;
        }

        @GuardedBy({"mLock"})
        private boolean shouldTrackLocked() {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            UserPackage userPackage = this.mPkg;
            int standbyBucketForPackage = JobSchedulerService.standbyBucketForPackage(userPackage.packageName, userPackage.userId, millis);
            boolean z = !this.mRegularJobTimer && QuotaController.this.hasTempAllowlistExemptionLocked(this.mUid, standbyBucketForPackage, millis);
            boolean z2 = !this.mRegularJobTimer && (QuotaController.this.mTopAppCache.get(this.mUid) || millis < QuotaController.this.mTopAppGraceCache.get(this.mUid));
            if (QuotaController.DEBUG) {
                Slog.d(QuotaController.TAG, "quotaFree=" + QuotaController.this.isQuotaFreeLocked(standbyBucketForPackage) + " isFG=" + QuotaController.this.mForegroundUids.get(this.mUid) + " tempEx=" + z + " topEx=" + z2);
            }
            return (QuotaController.this.isQuotaFreeLocked(standbyBucketForPackage) || QuotaController.this.mForegroundUids.get(this.mUid) || z || z2) ? false : true;
        }

        void onStateChangedLocked(long j, boolean z) {
            if (z) {
                emitSessionLocked(j);
                return;
            }
            if (isActive() || !shouldTrackLocked() || this.mRunningBgJobs.size() <= 0) {
                return;
            }
            this.mStartTimeElapsed = j;
            this.mDebitAdjustment = 0L;
            int size = this.mRunningBgJobs.size();
            this.mBgJobCount = size;
            if (this.mRegularJobTimer) {
                QuotaController quotaController = QuotaController.this;
                UserPackage userPackage = this.mPkg;
                quotaController.incrementJobCountLocked(userPackage.userId, userPackage.packageName, size);
                QuotaController quotaController2 = QuotaController.this;
                UserPackage userPackage2 = this.mPkg;
                quotaController2.invalidateAllExecutionStatsLocked(userPackage2.userId, userPackage2.packageName);
            }
            scheduleCutoff();
        }

        void rescheduleCutoff() {
            cancelCutoff();
            scheduleCutoff();
        }

        private void scheduleCutoff() {
            long timeUntilEJQuotaConsumedLocked;
            synchronized (QuotaController.this.mLock) {
                if (isActive()) {
                    Message obtainMessage = QuotaController.this.mHandler.obtainMessage(this.mRegularJobTimer ? 0 : 4, this.mPkg);
                    if (this.mRegularJobTimer) {
                        QuotaController quotaController = QuotaController.this;
                        UserPackage userPackage = this.mPkg;
                        timeUntilEJQuotaConsumedLocked = quotaController.getTimeUntilQuotaConsumedLocked(userPackage.userId, userPackage.packageName);
                    } else {
                        QuotaController quotaController2 = QuotaController.this;
                        UserPackage userPackage2 = this.mPkg;
                        timeUntilEJQuotaConsumedLocked = quotaController2.getTimeUntilEJQuotaConsumedLocked(userPackage2.userId, userPackage2.packageName);
                    }
                    if (QuotaController.DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.mRegularJobTimer ? "Regular job" : "EJ");
                        sb.append(" for ");
                        sb.append(this.mPkg);
                        sb.append(" has ");
                        sb.append(timeUntilEJQuotaConsumedLocked);
                        sb.append("ms left.");
                        Slog.i(QuotaController.TAG, sb.toString());
                    }
                    QuotaController.this.mHandler.sendMessageDelayed(obtainMessage, timeUntilEJQuotaConsumedLocked);
                }
            }
        }

        private void cancelCutoff() {
            QuotaController.this.mHandler.removeMessages(this.mRegularJobTimer ? 0 : 4, this.mPkg);
        }

        public void dump(IndentingPrintWriter indentingPrintWriter, Predicate<JobStatus> predicate) {
            indentingPrintWriter.print("Timer<");
            indentingPrintWriter.print(this.mRegularJobTimer ? "REG" : "EJ");
            indentingPrintWriter.print(">{");
            indentingPrintWriter.print(this.mPkg);
            indentingPrintWriter.print("} ");
            if (isActive()) {
                indentingPrintWriter.print("started at ");
                indentingPrintWriter.print(this.mStartTimeElapsed);
                indentingPrintWriter.print(" (");
                indentingPrintWriter.print(JobSchedulerService.sElapsedRealtimeClock.millis() - this.mStartTimeElapsed);
                indentingPrintWriter.print("ms ago)");
            } else {
                indentingPrintWriter.print("NOT active");
            }
            indentingPrintWriter.print(", ");
            indentingPrintWriter.print(this.mBgJobCount);
            indentingPrintWriter.print(" running bg jobs");
            if (!this.mRegularJobTimer) {
                indentingPrintWriter.print(" (debit adj=");
                indentingPrintWriter.print(this.mDebitAdjustment);
                indentingPrintWriter.print(")");
            }
            indentingPrintWriter.println();
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mRunningBgJobs.size(); i++) {
                JobStatus valueAt = this.mRunningBgJobs.valueAt(i);
                if (predicate.test(valueAt)) {
                    indentingPrintWriter.println(valueAt.toShortString());
                }
            }
            indentingPrintWriter.decreaseIndent();
        }

        public void dump(ProtoOutputStream protoOutputStream, long j, Predicate<JobStatus> predicate) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1133871366146L, isActive());
            protoOutputStream.write(1112396529667L, this.mStartTimeElapsed);
            protoOutputStream.write(1120986464260L, this.mBgJobCount);
            for (int i = 0; i < this.mRunningBgJobs.size(); i++) {
                JobStatus valueAt = this.mRunningBgJobs.valueAt(i);
                if (predicate.test(valueAt)) {
                    valueAt.writeToShortProto(protoOutputStream, 2246267895813L);
                }
            }
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class TopAppTimer {
        private final SparseArray<UsageEvents.Event> mActivities = new SparseArray<>();
        private final UserPackage mPkg;
        private long mStartTimeElapsed;

        TopAppTimer(int i, String str) {
            this.mPkg = UserPackage.of(i, str);
        }

        private int calculateTimeChunks(long j) {
            long j2 = j - this.mStartTimeElapsed;
            int i = (int) (j2 / QuotaController.this.mEJTopAppTimeChunkSizeMs);
            return j2 % QuotaController.this.mEJTopAppTimeChunkSizeMs >= 1000 ? i + 1 : i;
        }

        long getPendingReward(long j) {
            return QuotaController.this.mEJRewardTopAppMs * calculateTimeChunks(j);
        }

        void processEventLocked(UsageEvents.Event event) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            int eventType = event.getEventType();
            if (eventType == 1) {
                if (this.mActivities.size() == 0) {
                    this.mStartTimeElapsed = millis;
                }
                this.mActivities.put(event.mInstanceId, event);
                return;
            }
            if ((eventType == 2 || eventType == 23 || eventType == 24) && ((UsageEvents.Event) this.mActivities.removeReturnOld(event.mInstanceId)) != null && this.mActivities.size() == 0) {
                long pendingReward = getPendingReward(millis);
                if (QuotaController.DEBUG) {
                    Slog.d(QuotaController.TAG, "Crediting " + this.mPkg + " " + pendingReward + "ms for " + calculateTimeChunks(millis) + " time chunks");
                }
                QuotaController quotaController = QuotaController.this;
                UserPackage userPackage = this.mPkg;
                ShrinkableDebits eJDebitsLocked = quotaController.getEJDebitsLocked(userPackage.userId, userPackage.packageName);
                QuotaController quotaController2 = QuotaController.this;
                UserPackage userPackage2 = this.mPkg;
                if (quotaController2.transactQuotaLocked(userPackage2.userId, userPackage2.packageName, millis, eJDebitsLocked, pendingReward)) {
                    QuotaController quotaController3 = QuotaController.this;
                    StateChangedListener stateChangedListener = quotaController3.mStateChangedListener;
                    UserPackage userPackage3 = this.mPkg;
                    stateChangedListener.onControllerStateChanged(quotaController3.maybeUpdateConstraintForPkgLocked(millis, userPackage3.userId, userPackage3.packageName));
                }
            }
        }

        boolean isActive() {
            boolean z;
            synchronized (QuotaController.this.mLock) {
                z = this.mActivities.size() > 0;
            }
            return z;
        }

        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.print("TopAppTimer{");
            indentingPrintWriter.print(this.mPkg);
            indentingPrintWriter.print("} ");
            if (isActive()) {
                indentingPrintWriter.print("started at ");
                indentingPrintWriter.print(this.mStartTimeElapsed);
                indentingPrintWriter.print(" (");
                indentingPrintWriter.print(JobSchedulerService.sElapsedRealtimeClock.millis() - this.mStartTimeElapsed);
                indentingPrintWriter.print("ms ago)");
            } else {
                indentingPrintWriter.print("NOT active");
            }
            indentingPrintWriter.println();
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mActivities.size(); i++) {
                indentingPrintWriter.println(this.mActivities.valueAt(i).getClassName());
            }
            indentingPrintWriter.decreaseIndent();
        }

        public void dump(ProtoOutputStream protoOutputStream, long j) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1133871366146L, isActive());
            protoOutputStream.write(1112396529667L, this.mStartTimeElapsed);
            protoOutputStream.write(1120986464260L, this.mActivities.size());
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class StandbyTracker extends AppStandbyInternal.AppIdleStateChangeListener {
        StandbyTracker() {
        }

        public void onAppIdleStateChanged(final String str, final int i, boolean z, final int i2, int i3) {
            AppSchedulingModuleThread.getHandler().post(new Runnable() { // from class: com.android.server.job.controllers.QuotaController$StandbyTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    QuotaController.StandbyTracker.this.lambda$onAppIdleStateChanged$0(i2, i, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAppIdleStateChanged$0(int i, int i2, String str) {
            QuotaController.this.updateStandbyBucket(i2, str, JobSchedulerService.standbyBucketToBucketIndex(i));
        }

        public void triggerTemporaryQuotaBump(String str, int i) {
            synchronized (QuotaController.this.mLock) {
                List list = (List) QuotaController.this.mTimingEvents.get(i, str);
                if (list != null && list.size() != 0) {
                    list.add(new QuotaBump(JobSchedulerService.sElapsedRealtimeClock.millis()));
                    QuotaController.this.invalidateAllExecutionStatsLocked(i, str);
                    QuotaController.this.mHandler.obtainMessage(2, i, 0, str).sendToTarget();
                }
            }
        }
    }

    @VisibleForTesting
    void updateStandbyBucket(int i, String str, int i2) {
        if (DEBUG) {
            Slog.i(TAG, "Moving pkg " + StateController.packageToString(i, str) + " to bucketIndex " + i2);
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            ShrinkableDebits shrinkableDebits = (ShrinkableDebits) this.mEJStats.get(i, str);
            if (shrinkableDebits != null) {
                shrinkableDebits.setStandbyBucketLocked(i2);
            }
            ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(i, str);
            if (arraySet != null && arraySet.size() != 0) {
                for (int size = arraySet.size() - 1; size >= 0; size--) {
                    JobStatus jobStatus = (JobStatus) arraySet.valueAt(size);
                    if ((i2 == 5 || jobStatus.getStandbyBucket() == 5) && i2 != jobStatus.getStandbyBucket()) {
                        arrayList.add(jobStatus);
                    }
                    jobStatus.setStandbyBucket(i2);
                }
                Timer timer = (Timer) this.mPkgTimers.get(i, str);
                if (timer != null && timer.isActive()) {
                    timer.rescheduleCutoff();
                }
                Timer timer2 = (Timer) this.mEJPkgTimers.get(i, str);
                if (timer2 != null && timer2.isActive()) {
                    timer2.rescheduleCutoff();
                }
                this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForPkgLocked(JobSchedulerService.sElapsedRealtimeClock.millis(), i, str));
                if (arrayList.size() > 0) {
                    this.mStateChangedListener.onRestrictedBucketChanged(arrayList);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class UsageEventTracker implements UsageStatsManagerInternal.UsageEventListener {
        UsageEventTracker() {
        }

        public void onUsageEvent(int i, UsageEvents.Event event) {
            QuotaController.this.mHandler.obtainMessage(5, i, 0, event).sendToTarget();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class TempAllowlistTracker implements PowerAllowlistInternal.TempAllowlistChangeListener {
        TempAllowlistTracker() {
        }

        public void onAppAdded(int i) {
            synchronized (QuotaController.this.mLock) {
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                QuotaController.this.mTempAllowlistCache.put(i, true);
                ArraySet<String> packagesForUidLocked = QuotaController.this.mService.getPackagesForUidLocked(i);
                if (packagesForUidLocked != null) {
                    int userId = UserHandle.getUserId(i);
                    for (int size = packagesForUidLocked.size() - 1; size >= 0; size--) {
                        Timer timer = (Timer) QuotaController.this.mEJPkgTimers.get(userId, packagesForUidLocked.valueAt(size));
                        if (timer != null) {
                            timer.onStateChangedLocked(millis, true);
                        }
                    }
                    ArraySet<JobStatus> maybeUpdateConstraintForUidLocked = QuotaController.this.maybeUpdateConstraintForUidLocked(i);
                    if (maybeUpdateConstraintForUidLocked.size() > 0) {
                        QuotaController.this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForUidLocked);
                    }
                }
            }
        }

        public void onAppRemoved(int i) {
            synchronized (QuotaController.this.mLock) {
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis() + QuotaController.this.mEJGracePeriodTempAllowlistMs;
                QuotaController.this.mTempAllowlistCache.delete(i);
                QuotaController.this.mTempAllowlistGraceCache.put(i, millis);
                QuotaController.this.mHandler.sendMessageDelayed(QuotaController.this.mHandler.obtainMessage(6, i, 0), QuotaController.this.mEJGracePeriodTempAllowlistMs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class TimedEventTooOldPredicate implements Predicate<TimedEvent> {
        private long mNowElapsed;

        private TimedEventTooOldPredicate() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateNow() {
            this.mNowElapsed = JobSchedulerService.sElapsedRealtimeClock.millis();
        }

        @Override // java.util.function.Predicate
        public boolean test(TimedEvent timedEvent) {
            return timedEvent.getEndTimeElapsed() <= this.mNowElapsed - 86400000;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(List list) {
        if (list != null) {
            list.removeIf(this.mTimedEventTooOld);
        }
    }

    @VisibleForTesting
    void deleteObsoleteSessionsLocked() {
        this.mTimedEventTooOld.updateNow();
        this.mTimingEvents.forEach(this.mDeleteOldEventsFunctor);
        for (int i = 0; i < this.mEJTimingSessions.numMaps(); i++) {
            int keyAt = this.mEJTimingSessions.keyAt(i);
            for (int i2 = 0; i2 < this.mEJTimingSessions.numElementsForKey(keyAt); i2++) {
                String str = (String) this.mEJTimingSessions.keyAt(i, i2);
                ShrinkableDebits eJDebitsLocked = getEJDebitsLocked(keyAt, str);
                List list = (List) this.mEJTimingSessions.get(keyAt, str);
                if (list != null) {
                    while (list.size() > 0) {
                        TimingSession timingSession = (TimingSession) list.get(0);
                        if (this.mTimedEventTooOld.test((TimedEvent) timingSession)) {
                            eJDebitsLocked.transactLocked(-(timingSession.endTimeElapsed - timingSession.startTimeElapsed));
                            list.remove(0);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class QcHandler extends Handler {
        QcHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:5:0x000b. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:117:0x034a A[Catch: all -> 0x0282, TryCatch #0 {all -> 0x0282, blocks: (B:106:0x0256, B:108:0x0274, B:110:0x0277, B:111:0x02e4, B:113:0x02f0, B:115:0x033e, B:117:0x034a, B:118:0x0351, B:120:0x02fc, B:122:0x0306, B:124:0x030d, B:126:0x0321, B:127:0x0324, B:129:0x0338, B:131:0x033b, B:135:0x0287, B:138:0x0296, B:139:0x02ac, B:141:0x02b8, B:143:0x02e1, B:146:0x02a1), top: B:104:0x0254, outer: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:124:0x030d A[Catch: all -> 0x0282, TryCatch #0 {all -> 0x0282, blocks: (B:106:0x0256, B:108:0x0274, B:110:0x0277, B:111:0x02e4, B:113:0x02f0, B:115:0x033e, B:117:0x034a, B:118:0x0351, B:120:0x02fc, B:122:0x0306, B:124:0x030d, B:126:0x0321, B:127:0x0324, B:129:0x0338, B:131:0x033b, B:135:0x0287, B:138:0x0296, B:139:0x02ac, B:141:0x02b8, B:143:0x02e1, B:146:0x02a1), top: B:104:0x0254, outer: #2 }] */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void handleMessage(Message message) {
            boolean z;
            boolean z2;
            ArraySet<String> packagesForUidLocked;
            int size;
            ArraySet<JobStatus> maybeUpdateConstraintForUidLocked;
            synchronized (QuotaController.this.mLock) {
                switch (message.what) {
                    case 0:
                        UserPackage userPackage = (UserPackage) message.obj;
                        if (QuotaController.DEBUG) {
                            Slog.d(QuotaController.TAG, "Checking if " + userPackage + " has reached its quota.");
                        }
                        if (QuotaController.this.getRemainingExecutionTimeLocked(userPackage.userId, userPackage.packageName) <= 50) {
                            if (QuotaController.DEBUG) {
                                Slog.d(QuotaController.TAG, userPackage + " has reached its quota.");
                            }
                            QuotaController quotaController = QuotaController.this;
                            quotaController.mStateChangedListener.onControllerStateChanged(quotaController.maybeUpdateConstraintForPkgLocked(JobSchedulerService.sElapsedRealtimeClock.millis(), userPackage.userId, userPackage.packageName));
                        } else {
                            Message obtainMessage = obtainMessage(0, userPackage);
                            long timeUntilQuotaConsumedLocked = QuotaController.this.getTimeUntilQuotaConsumedLocked(userPackage.userId, userPackage.packageName);
                            if (QuotaController.DEBUG) {
                                Slog.d(QuotaController.TAG, userPackage + " has " + timeUntilQuotaConsumedLocked + "ms left.");
                            }
                            sendMessageDelayed(obtainMessage, timeUntilQuotaConsumedLocked);
                        }
                        break;
                    case 1:
                        if (QuotaController.DEBUG) {
                            Slog.d(QuotaController.TAG, "Cleaning up timing sessions.");
                        }
                        QuotaController.this.deleteObsoleteSessionsLocked();
                        QuotaController.this.maybeScheduleCleanupAlarmLocked();
                        break;
                    case 2:
                        String str = (String) message.obj;
                        int i = message.arg1;
                        if (QuotaController.DEBUG) {
                            Slog.d(QuotaController.TAG, "Checking pkg " + StateController.packageToString(i, str));
                        }
                        QuotaController quotaController2 = QuotaController.this;
                        quotaController2.mStateChangedListener.onControllerStateChanged(quotaController2.maybeUpdateConstraintForPkgLocked(JobSchedulerService.sElapsedRealtimeClock.millis(), i, str));
                        break;
                    case 3:
                        int i2 = message.arg1;
                        int i3 = message.arg2;
                        int userId = UserHandle.getUserId(i2);
                        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                        synchronized (QuotaController.this.mLock) {
                            try {
                                if (i3 <= 2) {
                                    QuotaController.this.mTopAppCache.put(i2, true);
                                    QuotaController.this.mTopAppGraceCache.delete(i2);
                                    if (!QuotaController.this.mForegroundUids.get(i2)) {
                                        QuotaController.this.mForegroundUids.put(i2, true);
                                        z = true;
                                        if ((QuotaController.this.mPkgTimers.indexOfKey(userId) < 0 || QuotaController.this.mEJPkgTimers.indexOfKey(userId) >= 0) && (packagesForUidLocked = QuotaController.this.mService.getPackagesForUidLocked(i2)) != null) {
                                            for (size = packagesForUidLocked.size() - 1; size >= 0; size--) {
                                                Timer timer = (Timer) QuotaController.this.mEJPkgTimers.get(userId, packagesForUidLocked.valueAt(size));
                                                if (timer != null) {
                                                    timer.onStateChangedLocked(millis, z);
                                                }
                                                Timer timer2 = (Timer) QuotaController.this.mPkgTimers.get(userId, packagesForUidLocked.valueAt(size));
                                                if (timer2 != null) {
                                                    timer2.onStateChangedLocked(millis, z);
                                                }
                                            }
                                        }
                                        maybeUpdateConstraintForUidLocked = QuotaController.this.maybeUpdateConstraintForUidLocked(i2);
                                        if (maybeUpdateConstraintForUidLocked.size() > 0) {
                                            QuotaController.this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForUidLocked);
                                        }
                                    }
                                } else {
                                    if (i3 <= 4) {
                                        z2 = !QuotaController.this.mForegroundUids.get(i2);
                                        QuotaController.this.mForegroundUids.put(i2, true);
                                        z = true;
                                    } else {
                                        QuotaController.this.mForegroundUids.delete(i2);
                                        z = false;
                                        z2 = true;
                                    }
                                    if (QuotaController.this.mTopAppCache.get(i2)) {
                                        long j = QuotaController.this.mEJGracePeriodTopAppMs + millis;
                                        QuotaController.this.mTopAppCache.delete(i2);
                                        QuotaController.this.mTopAppGraceCache.put(i2, j);
                                        sendMessageDelayed(obtainMessage(6, i2, 0), QuotaController.this.mEJGracePeriodTopAppMs);
                                    }
                                    if (!z2) {
                                    }
                                    if (QuotaController.this.mPkgTimers.indexOfKey(userId) < 0) {
                                    }
                                    while (size >= 0) {
                                    }
                                    maybeUpdateConstraintForUidLocked = QuotaController.this.maybeUpdateConstraintForUidLocked(i2);
                                    if (maybeUpdateConstraintForUidLocked.size() > 0) {
                                    }
                                }
                            } finally {
                            }
                        }
                        break;
                    case 4:
                        UserPackage userPackage2 = (UserPackage) message.obj;
                        if (QuotaController.DEBUG) {
                            Slog.d(QuotaController.TAG, "Checking if " + userPackage2 + " has reached its EJ quota.");
                        }
                        if (QuotaController.this.getRemainingEJExecutionTimeLocked(userPackage2.userId, userPackage2.packageName) <= 0) {
                            if (QuotaController.DEBUG) {
                                Slog.d(QuotaController.TAG, userPackage2 + " has reached its EJ quota.");
                            }
                            QuotaController quotaController3 = QuotaController.this;
                            quotaController3.mStateChangedListener.onControllerStateChanged(quotaController3.maybeUpdateConstraintForPkgLocked(JobSchedulerService.sElapsedRealtimeClock.millis(), userPackage2.userId, userPackage2.packageName));
                        } else {
                            Message obtainMessage2 = obtainMessage(4, userPackage2);
                            long timeUntilEJQuotaConsumedLocked = QuotaController.this.getTimeUntilEJQuotaConsumedLocked(userPackage2.userId, userPackage2.packageName);
                            if (QuotaController.DEBUG) {
                                Slog.d(QuotaController.TAG, userPackage2 + " has " + timeUntilEJQuotaConsumedLocked + "ms left for EJ");
                            }
                            sendMessageDelayed(obtainMessage2, timeUntilEJQuotaConsumedLocked);
                        }
                        break;
                    case 5:
                        int i4 = message.arg1;
                        UsageEvents.Event event = (UsageEvents.Event) message.obj;
                        String packageName = event.getPackageName();
                        if (QuotaController.DEBUG) {
                            Slog.d(QuotaController.TAG, "Processing event " + event.getEventType() + " for " + StateController.packageToString(i4, packageName));
                        }
                        int eventType = event.getEventType();
                        if (eventType != 1 && eventType != 2) {
                            if (eventType == 7 || eventType == 12 || eventType == 9) {
                                QuotaController quotaController4 = QuotaController.this;
                                quotaController4.grantRewardForInstantEvent(i4, packageName, quotaController4.mEJRewardInteractionMs);
                            } else if (eventType == 10) {
                                QuotaController quotaController5 = QuotaController.this;
                                quotaController5.grantRewardForInstantEvent(i4, packageName, quotaController5.mEJRewardNotificationSeenMs);
                            } else if (eventType != 23 && eventType != 24) {
                            }
                            break;
                        }
                        synchronized (QuotaController.this.mLock) {
                            TopAppTimer topAppTimer = (TopAppTimer) QuotaController.this.mTopAppTrackers.get(i4, packageName);
                            if (topAppTimer == null) {
                                topAppTimer = new TopAppTimer(i4, packageName);
                                QuotaController.this.mTopAppTrackers.add(i4, packageName, topAppTimer);
                            }
                            topAppTimer.processEventLocked(event);
                        }
                        break;
                    case 6:
                        int i5 = message.arg1;
                        synchronized (QuotaController.this.mLock) {
                            if (!QuotaController.this.mTempAllowlistCache.get(i5) && !QuotaController.this.mTopAppCache.get(i5)) {
                                long millis2 = JobSchedulerService.sElapsedRealtimeClock.millis();
                                if (millis2 >= QuotaController.this.mTempAllowlistGraceCache.get(i5) && millis2 >= QuotaController.this.mTopAppGraceCache.get(i5)) {
                                    if (QuotaController.DEBUG) {
                                        Slog.d(QuotaController.TAG, i5 + " is now out of grace period");
                                    }
                                    QuotaController.this.mTempAllowlistGraceCache.delete(i5);
                                    QuotaController.this.mTopAppGraceCache.delete(i5);
                                    ArraySet<String> packagesForUidLocked2 = QuotaController.this.mService.getPackagesForUidLocked(i5);
                                    if (packagesForUidLocked2 != null) {
                                        int userId2 = UserHandle.getUserId(i5);
                                        for (int size2 = packagesForUidLocked2.size() - 1; size2 >= 0; size2--) {
                                            Timer timer3 = (Timer) QuotaController.this.mEJPkgTimers.get(userId2, packagesForUidLocked2.valueAt(size2));
                                            if (timer3 != null) {
                                                timer3.onStateChangedLocked(millis2, false);
                                            }
                                        }
                                        ArraySet<JobStatus> maybeUpdateConstraintForUidLocked2 = QuotaController.this.maybeUpdateConstraintForUidLocked(i5);
                                        if (maybeUpdateConstraintForUidLocked2.size() > 0) {
                                            QuotaController.this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForUidLocked2);
                                        }
                                    }
                                }
                                if (QuotaController.DEBUG) {
                                    Slog.d(QuotaController.TAG, i5 + " is still in grace period");
                                }
                            }
                            if (QuotaController.DEBUG) {
                                Slog.d(QuotaController.TAG, i5 + " is still allowed");
                            }
                        }
                        break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class InQuotaAlarmQueue extends AlarmQueue<UserPackage> {
        private InQuotaAlarmQueue(Context context, Looper looper) {
            super(context, looper, QuotaController.ALARM_TAG_QUOTA_CHECK, "In quota", false, 60000L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(UserPackage userPackage, int i) {
            return userPackage.userId == i;
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(ArraySet<UserPackage> arraySet) {
            for (int i = 0; i < arraySet.size(); i++) {
                UserPackage valueAt = arraySet.valueAt(i);
                QuotaController.this.mHandler.obtainMessage(2, valueAt.userId, 0, valueAt.packageName).sendToTarget();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForUpdatedConstantsLocked() {
        this.mQcConstants.mShouldReevaluateConstraints = false;
        this.mQcConstants.mRateLimitingConstantsUpdated = false;
        this.mQcConstants.mExecutionPeriodConstantsUpdated = false;
        this.mQcConstants.mEJLimitConstantsUpdated = false;
        this.mQcConstants.mQuotaBumpConstantsUpdated = false;
    }

    @Override // com.android.server.job.controllers.StateController
    public void processConstantLocked(DeviceConfig.Properties properties, String str) {
        this.mQcConstants.processConstantLocked(properties, str);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onConstantsUpdatedLocked() {
        if (this.mQcConstants.mShouldReevaluateConstraints || this.mIsEnabled == this.mConstants.USE_TARE_POLICY) {
            this.mIsEnabled = !this.mConstants.USE_TARE_POLICY;
            AppSchedulingModuleThread.getHandler().post(new Runnable() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    QuotaController.this.lambda$onConstantsUpdatedLocked$3();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$3() {
        synchronized (this.mLock) {
            invalidateAllExecutionStatsLocked();
            maybeUpdateAllConstraintsLocked();
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class QcConstants {
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_RARE_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_WORKING_MS = 600000;
        private static final long DEFAULT_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = 180000;
        private static final long DEFAULT_EJ_GRACE_PERIOD_TOP_APP_MS = 60000;
        private static final long DEFAULT_EJ_LIMIT_ACTIVE_MS = 1800000;
        private static final long DEFAULT_EJ_LIMIT_ADDITION_INSTALLER_MS = 1800000;
        private static final long DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS = 900000;
        private static final long DEFAULT_EJ_LIMIT_EXEMPTED_MS = 3600000;
        private static final long DEFAULT_EJ_LIMIT_FREQUENT_MS = 600000;
        private static final long DEFAULT_EJ_LIMIT_RARE_MS = 600000;
        private static final long DEFAULT_EJ_LIMIT_RESTRICTED_MS = 300000;
        private static final long DEFAULT_EJ_LIMIT_WORKING_MS = 1800000;
        private static final long DEFAULT_EJ_REWARD_INTERACTION_MS = 15000;
        private static final long DEFAULT_EJ_REWARD_NOTIFICATION_SEEN_MS = 0;
        private static final long DEFAULT_EJ_REWARD_TOP_APP_MS = 10000;
        private static final long DEFAULT_EJ_TOP_APP_TIME_CHUNK_SIZE_MS = 30000;
        private static final long DEFAULT_EJ_WINDOW_SIZE_MS = 86400000;
        private static final long DEFAULT_IN_QUOTA_BUFFER_MS = 30000;
        private static final long DEFAULT_MAX_EXECUTION_TIME_MS = 14400000;
        private static final int DEFAULT_MAX_JOB_COUNT_ACTIVE = 75;
        private static final int DEFAULT_MAX_JOB_COUNT_EXEMPTED = 75;
        private static final int DEFAULT_MAX_JOB_COUNT_FREQUENT = 200;
        private static final int DEFAULT_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        private static final int DEFAULT_MAX_JOB_COUNT_RARE = 48;
        private static final int DEFAULT_MAX_JOB_COUNT_RESTRICTED = 10;
        private static final int DEFAULT_MAX_JOB_COUNT_WORKING = 120;
        private static final int DEFAULT_MAX_SESSION_COUNT_ACTIVE = 75;
        private static final int DEFAULT_MAX_SESSION_COUNT_EXEMPTED = 75;
        private static final int DEFAULT_MAX_SESSION_COUNT_FREQUENT = 8;
        private static final int DEFAULT_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        private static final int DEFAULT_MAX_SESSION_COUNT_RARE = 3;
        private static final int DEFAULT_MAX_SESSION_COUNT_RESTRICTED = 1;
        private static final int DEFAULT_MAX_SESSION_COUNT_WORKING = 10;
        private static final long DEFAULT_MIN_QUOTA_CHECK_DELAY_MS = 60000;
        private static final long DEFAULT_QUOTA_BUMP_ADDITIONAL_DURATION_MS = 60000;
        private static final int DEFAULT_QUOTA_BUMP_ADDITIONAL_JOB_COUNT = 2;
        private static final int DEFAULT_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = 1;
        private static final int DEFAULT_QUOTA_BUMP_LIMIT = 8;
        private static final long DEFAULT_QUOTA_BUMP_WINDOW_SIZE_MS = 28800000;
        private static final long DEFAULT_RATE_LIMITING_WINDOW_MS = 60000;
        private static final long DEFAULT_TIMING_SESSION_COALESCING_DURATION_MS = 5000;
        private static final long DEFAULT_WINDOW_SIZE_ACTIVE_MS = 600000;
        private static final long DEFAULT_WINDOW_SIZE_EXEMPTED_MS = 600000;
        private static final long DEFAULT_WINDOW_SIZE_FREQUENT_MS = 28800000;
        private static final long DEFAULT_WINDOW_SIZE_RARE_MS = 86400000;
        private static final long DEFAULT_WINDOW_SIZE_RESTRICTED_MS = 86400000;
        private static final long DEFAULT_WINDOW_SIZE_WORKING_MS = 7200000;

        @VisibleForTesting
        static final String KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = "qc_allowed_time_per_period_active_ms";

        @VisibleForTesting
        static final String KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = "qc_allowed_time_per_period_exempted_ms";

        @VisibleForTesting
        static final String KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = "qc_allowed_time_per_period_frequent_ms";

        @VisibleForTesting
        static final String KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS = "qc_allowed_time_per_period_rare_ms";

        @VisibleForTesting
        static final String KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = "qc_allowed_time_per_period_restricted_ms";

        @VisibleForTesting
        static final String KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS = "qc_allowed_time_per_period_working_ms";

        @VisibleForTesting
        static final String KEY_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = "qc_ej_grace_period_temp_allowlist_ms";

        @VisibleForTesting
        static final String KEY_EJ_GRACE_PERIOD_TOP_APP_MS = "qc_ej_grace_period_top_app_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_ACTIVE_MS = "qc_ej_limit_active_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_ADDITION_INSTALLER_MS = "qc_ej_limit_addition_installer_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_ADDITION_SPECIAL_MS = "qc_ej_limit_addition_special_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_EXEMPTED_MS = "qc_ej_limit_exempted_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_FREQUENT_MS = "qc_ej_limit_frequent_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_RARE_MS = "qc_ej_limit_rare_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_RESTRICTED_MS = "qc_ej_limit_restricted_ms";

        @VisibleForTesting
        static final String KEY_EJ_LIMIT_WORKING_MS = "qc_ej_limit_working_ms";

        @VisibleForTesting
        static final String KEY_EJ_REWARD_INTERACTION_MS = "qc_ej_reward_interaction_ms";

        @VisibleForTesting
        static final String KEY_EJ_REWARD_NOTIFICATION_SEEN_MS = "qc_ej_reward_notification_seen_ms";

        @VisibleForTesting
        static final String KEY_EJ_REWARD_TOP_APP_MS = "qc_ej_reward_top_app_ms";

        @VisibleForTesting
        static final String KEY_EJ_TOP_APP_TIME_CHUNK_SIZE_MS = "qc_ej_top_app_time_chunk_size_ms";

        @VisibleForTesting
        static final String KEY_EJ_WINDOW_SIZE_MS = "qc_ej_window_size_ms";

        @VisibleForTesting
        static final String KEY_IN_QUOTA_BUFFER_MS = "qc_in_quota_buffer_ms";

        @VisibleForTesting
        static final String KEY_MAX_EXECUTION_TIME_MS = "qc_max_execution_time_ms";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_ACTIVE = "qc_max_job_count_active";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_EXEMPTED = "qc_max_job_count_exempted";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_FREQUENT = "qc_max_job_count_frequent";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = "qc_max_job_count_per_rate_limiting_window";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_RARE = "qc_max_job_count_rare";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_RESTRICTED = "qc_max_job_count_restricted";

        @VisibleForTesting
        static final String KEY_MAX_JOB_COUNT_WORKING = "qc_max_job_count_working";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_ACTIVE = "qc_max_session_count_active";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_EXEMPTED = "qc_max_session_count_exempted";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_FREQUENT = "qc_max_session_count_frequent";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = "qc_max_session_count_per_rate_limiting_window";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_RARE = "qc_max_session_count_rare";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_RESTRICTED = "qc_max_session_count_restricted";

        @VisibleForTesting
        static final String KEY_MAX_SESSION_COUNT_WORKING = "qc_max_session_count_working";

        @VisibleForTesting
        static final String KEY_MIN_QUOTA_CHECK_DELAY_MS = "qc_min_quota_check_delay_ms";

        @VisibleForTesting
        static final String KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS = "qc_quota_bump_additional_duration_ms";

        @VisibleForTesting
        static final String KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT = "qc_quota_bump_additional_job_count";

        @VisibleForTesting
        static final String KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = "qc_quota_bump_additional_session_count";

        @VisibleForTesting
        static final String KEY_QUOTA_BUMP_LIMIT = "qc_quota_bump_limit";

        @VisibleForTesting
        static final String KEY_QUOTA_BUMP_WINDOW_SIZE_MS = "qc_quota_bump_window_size_ms";

        @VisibleForTesting
        static final String KEY_RATE_LIMITING_WINDOW_MS = "qc_rate_limiting_window_ms";

        @VisibleForTesting
        static final String KEY_TIMING_SESSION_COALESCING_DURATION_MS = "qc_timing_session_coalescing_duration_ms";

        @VisibleForTesting
        static final String KEY_WINDOW_SIZE_ACTIVE_MS = "qc_window_size_active_ms";

        @VisibleForTesting
        static final String KEY_WINDOW_SIZE_EXEMPTED_MS = "qc_window_size_exempted_ms";

        @VisibleForTesting
        static final String KEY_WINDOW_SIZE_FREQUENT_MS = "qc_window_size_frequent_ms";

        @VisibleForTesting
        static final String KEY_WINDOW_SIZE_RARE_MS = "qc_window_size_rare_ms";

        @VisibleForTesting
        static final String KEY_WINDOW_SIZE_RESTRICTED_MS = "qc_window_size_restricted_ms";

        @VisibleForTesting
        static final String KEY_WINDOW_SIZE_WORKING_MS = "qc_window_size_working_ms";
        private static final int MIN_BUCKET_JOB_COUNT = 10;
        private static final int MIN_BUCKET_SESSION_COUNT = 1;
        private static final long MIN_MAX_EXECUTION_TIME_MS = 3600000;
        private static final int MIN_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = 10;
        private static final int MIN_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = 10;
        private static final long MIN_RATE_LIMITING_WINDOW_MS = 30000;
        private static final String QC_CONSTANT_PREFIX = "qc_";
        private boolean mShouldReevaluateConstraints = false;
        private boolean mRateLimitingConstantsUpdated = false;
        private boolean mExecutionPeriodConstantsUpdated = false;
        private boolean mEJLimitConstantsUpdated = false;
        private boolean mQuotaBumpConstantsUpdated = false;
        public long ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_WORKING_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_RARE_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = 600000;
        public long IN_QUOTA_BUFFER_MS = 30000;
        public long WINDOW_SIZE_EXEMPTED_MS = 600000;
        public long WINDOW_SIZE_ACTIVE_MS = 600000;
        public long WINDOW_SIZE_WORKING_MS = 7200000;
        public long WINDOW_SIZE_FREQUENT_MS = 28800000;
        public long WINDOW_SIZE_RARE_MS = 86400000;
        public long WINDOW_SIZE_RESTRICTED_MS = 86400000;
        public long MAX_EXECUTION_TIME_MS = 14400000;
        public int MAX_JOB_COUNT_EXEMPTED = 75;
        public int MAX_JOB_COUNT_ACTIVE = 75;
        public int MAX_JOB_COUNT_WORKING = DEFAULT_MAX_JOB_COUNT_WORKING;
        public int MAX_JOB_COUNT_FREQUENT = 200;
        public int MAX_JOB_COUNT_RARE = 48;
        public int MAX_JOB_COUNT_RESTRICTED = 10;
        public long RATE_LIMITING_WINDOW_MS = 60000;
        public int MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        public int MAX_SESSION_COUNT_EXEMPTED = 75;
        public int MAX_SESSION_COUNT_ACTIVE = 75;
        public int MAX_SESSION_COUNT_WORKING = 10;
        public int MAX_SESSION_COUNT_FREQUENT = 8;
        public int MAX_SESSION_COUNT_RARE = 3;
        public int MAX_SESSION_COUNT_RESTRICTED = 1;
        public int MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        public long TIMING_SESSION_COALESCING_DURATION_MS = DEFAULT_TIMING_SESSION_COALESCING_DURATION_MS;
        public long MIN_QUOTA_CHECK_DELAY_MS = 60000;
        public long EJ_LIMIT_EXEMPTED_MS = 3600000;
        public long EJ_LIMIT_ACTIVE_MS = 1800000;
        public long EJ_LIMIT_WORKING_MS = 1800000;
        public long EJ_LIMIT_FREQUENT_MS = 600000;
        public long EJ_LIMIT_RARE_MS = 600000;
        public long EJ_LIMIT_RESTRICTED_MS = DEFAULT_EJ_LIMIT_RESTRICTED_MS;
        public long EJ_LIMIT_ADDITION_SPECIAL_MS = DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS;
        public long EJ_LIMIT_ADDITION_INSTALLER_MS = 1800000;
        public long EJ_WINDOW_SIZE_MS = 86400000;
        public long EJ_TOP_APP_TIME_CHUNK_SIZE_MS = 30000;
        public long EJ_REWARD_TOP_APP_MS = 10000;
        public long EJ_REWARD_INTERACTION_MS = DEFAULT_EJ_REWARD_INTERACTION_MS;
        public long EJ_REWARD_NOTIFICATION_SEEN_MS = 0;
        public long EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = 180000;
        public long EJ_GRACE_PERIOD_TOP_APP_MS = 60000;
        public long QUOTA_BUMP_ADDITIONAL_DURATION_MS = 60000;
        public int QUOTA_BUMP_ADDITIONAL_JOB_COUNT = 2;
        public int QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = 1;
        public long QUOTA_BUMP_WINDOW_SIZE_MS = 28800000;
        public int QUOTA_BUMP_LIMIT = 8;

        QcConstants() {
        }

        public void processConstantLocked(DeviceConfig.Properties properties, String str) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1952749138:
                    if (str.equals(KEY_EJ_LIMIT_ACTIVE_MS)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1719823663:
                    if (str.equals(KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1683576133:
                    if (str.equals(KEY_WINDOW_SIZE_FREQUENT_MS)) {
                        c = 2;
                        break;
                    }
                    break;
                case -1525098678:
                    if (str.equals(KEY_QUOTA_BUMP_WINDOW_SIZE_MS)) {
                        c = 3;
                        break;
                    }
                    break;
                case -1515776932:
                    if (str.equals(KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS)) {
                        c = 4;
                        break;
                    }
                    break;
                case -1507602138:
                    if (str.equals(KEY_EJ_LIMIT_FREQUENT_MS)) {
                        c = 5;
                        break;
                    }
                    break;
                case -1495638658:
                    if (str.equals(KEY_EJ_LIMIT_ADDITION_SPECIAL_MS)) {
                        c = 6;
                        break;
                    }
                    break;
                case -1436524327:
                    if (str.equals(KEY_EJ_REWARD_NOTIFICATION_SEEN_MS)) {
                        c = 7;
                        break;
                    }
                    break;
                case -1412574464:
                    if (str.equals(KEY_MAX_JOB_COUNT_ACTIVE)) {
                        c = '\b';
                        break;
                    }
                    break;
                case -1409079211:
                    if (str.equals(KEY_EJ_TOP_APP_TIME_CHUNK_SIZE_MS)) {
                        c = '\t';
                        break;
                    }
                    break;
                case -1301522660:
                    if (str.equals(KEY_MAX_JOB_COUNT_RARE)) {
                        c = '\n';
                        break;
                    }
                    break;
                case -1253638898:
                    if (str.equals(KEY_WINDOW_SIZE_RESTRICTED_MS)) {
                        c = 11;
                        break;
                    }
                    break;
                case -1004520055:
                    if (str.equals(KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS)) {
                        c = '\f';
                        break;
                    }
                    break;
                case -947372170:
                    if (str.equals(KEY_EJ_REWARD_INTERACTION_MS)) {
                        c = '\r';
                        break;
                    }
                    break;
                case -947005713:
                    if (str.equals(KEY_RATE_LIMITING_WINDOW_MS)) {
                        c = 14;
                        break;
                    }
                    break;
                case -911626004:
                    if (str.equals(KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW)) {
                        c = 15;
                        break;
                    }
                    break;
                case -861283784:
                    if (str.equals(KEY_MAX_JOB_COUNT_EXEMPTED)) {
                        c = 16;
                        break;
                    }
                    break;
                case -743649451:
                    if (str.equals(KEY_MAX_JOB_COUNT_RESTRICTED)) {
                        c = 17;
                        break;
                    }
                    break;
                case -615999962:
                    if (str.equals(KEY_QUOTA_BUMP_LIMIT)) {
                        c = 18;
                        break;
                    }
                    break;
                case -473591193:
                    if (str.equals(KEY_WINDOW_SIZE_RARE_MS)) {
                        c = 19;
                        break;
                    }
                    break;
                case -144699320:
                    if (str.equals(KEY_MAX_JOB_COUNT_FREQUENT)) {
                        c = 20;
                        break;
                    }
                    break;
                case 202838626:
                    if (str.equals(KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS)) {
                        c = 21;
                        break;
                    }
                    break;
                case 224532750:
                    if (str.equals(KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS)) {
                        c = 22;
                        break;
                    }
                    break;
                case 319829733:
                    if (str.equals(KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW)) {
                        c = 23;
                        break;
                    }
                    break;
                case 353645753:
                    if (str.equals(KEY_EJ_LIMIT_RESTRICTED_MS)) {
                        c = 24;
                        break;
                    }
                    break;
                case 353674834:
                    if (str.equals(KEY_EJ_LIMIT_RARE_MS)) {
                        c = 25;
                        break;
                    }
                    break;
                case 515924943:
                    if (str.equals(KEY_EJ_LIMIT_ADDITION_INSTALLER_MS)) {
                        c = 26;
                        break;
                    }
                    break;
                case 542719401:
                    if (str.equals(KEY_MAX_EXECUTION_TIME_MS)) {
                        c = 27;
                        break;
                    }
                    break;
                case 659682264:
                    if (str.equals(KEY_EJ_GRACE_PERIOD_TOP_APP_MS)) {
                        c = 28;
                        break;
                    }
                    break;
                case 1012217584:
                    if (str.equals(KEY_WINDOW_SIZE_WORKING_MS)) {
                        c = 29;
                        break;
                    }
                    break;
                case 1029123626:
                    if (str.equals(KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT)) {
                        c = 30;
                        break;
                    }
                    break;
                case 1070239943:
                    if (str.equals(KEY_MAX_SESSION_COUNT_ACTIVE)) {
                        c = 31;
                        break;
                    }
                    break;
                case 1072854979:
                    if (str.equals(KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT)) {
                        c = ' ';
                        break;
                    }
                    break;
                case 1185201205:
                    if (str.equals(KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS)) {
                        c = '!';
                        break;
                    }
                    break;
                case 1211719583:
                    if (str.equals(KEY_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS)) {
                        c = '\"';
                        break;
                    }
                    break;
                case 1232643386:
                    if (str.equals(KEY_MIN_QUOTA_CHECK_DELAY_MS)) {
                        c = '#';
                        break;
                    }
                    break;
                case 1415707953:
                    if (str.equals(KEY_IN_QUOTA_BUFFER_MS)) {
                        c = '$';
                        break;
                    }
                    break;
                case 1416512063:
                    if (str.equals(KEY_MAX_SESSION_COUNT_EXEMPTED)) {
                        c = '%';
                        break;
                    }
                    break;
                case 1504661904:
                    if (str.equals(KEY_MAX_SESSION_COUNT_WORKING)) {
                        c = '&';
                        break;
                    }
                    break;
                case 1510141337:
                    if (str.equals(KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS)) {
                        c = '\'';
                        break;
                    }
                    break;
                case 1572083493:
                    if (str.equals(KEY_EJ_LIMIT_WORKING_MS)) {
                        c = '(';
                        break;
                    }
                    break;
                case 1737007281:
                    if (str.equals(KEY_EJ_REWARD_TOP_APP_MS)) {
                        c = ')';
                        break;
                    }
                    break;
                case 1846826615:
                    if (str.equals(KEY_MAX_JOB_COUNT_WORKING)) {
                        c = '*';
                        break;
                    }
                    break;
                case 1908515971:
                    if (str.equals(KEY_WINDOW_SIZE_ACTIVE_MS)) {
                        c = '+';
                        break;
                    }
                    break;
                case 1921715463:
                    if (str.equals(KEY_TIMING_SESSION_COALESCING_DURATION_MS)) {
                        c = ',';
                        break;
                    }
                    break;
                case 1988481858:
                    if (str.equals(KEY_EJ_WINDOW_SIZE_MS)) {
                        c = '-';
                        break;
                    }
                    break;
                case 2079805852:
                    if (str.equals(KEY_MAX_SESSION_COUNT_RESTRICTED)) {
                        c = '.';
                        break;
                    }
                    break;
                case 2084297379:
                    if (str.equals(KEY_MAX_SESSION_COUNT_RARE)) {
                        c = '/';
                        break;
                    }
                    break;
                case 2133096527:
                    if (str.equals(KEY_MAX_SESSION_COUNT_FREQUENT)) {
                        c = '0';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 5:
                case 6:
                case 24:
                case 25:
                case 26:
                case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_8 /* 40 */:
                case NetworkPolicyManagerService.TYPE_RAPID /* 45 */:
                    updateEJLimitConstantsLocked();
                    return;
                case 1:
                case 2:
                case 4:
                case 11:
                case '\f':
                case 19:
                case 21:
                case 27:
                case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_1 /* 33 */:
                case '$':
                case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_7 /* 39 */:
                case HdmiCecKeycode.CEC_KEYCODE_ENTER /* 43 */:
                    updateExecutionPeriodConstantsLocked();
                    return;
                case 3:
                case 18:
                case 22:
                case HdmiCecKeycode.CEC_KEYCODE_NUMBER_11 /* 30 */:
                case ' ':
                    updateQuotaBumpConstantsLocked();
                    return;
                case 7:
                    long j = properties.getLong(str, 0L);
                    this.EJ_REWARD_NOTIFICATION_SEEN_MS = j;
                    QuotaController.this.mEJRewardNotificationSeenMs = Math.min(DEFAULT_EJ_LIMIT_RESTRICTED_MS, Math.max(0L, j));
                    return;
                case '\b':
                    int i = properties.getInt(str, 75);
                    this.MAX_JOB_COUNT_ACTIVE = i;
                    int max = Math.max(10, i);
                    if (QuotaController.this.mMaxBucketJobCounts[0] != max) {
                        QuotaController.this.mMaxBucketJobCounts[0] = max;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case '\t':
                    long j2 = properties.getLong(str, 30000L);
                    this.EJ_TOP_APP_TIME_CHUNK_SIZE_MS = j2;
                    long min = Math.min(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.max(1L, j2));
                    if (QuotaController.this.mEJTopAppTimeChunkSizeMs != min) {
                        QuotaController.this.mEJTopAppTimeChunkSizeMs = min;
                        if (QuotaController.this.mEJTopAppTimeChunkSizeMs < QuotaController.this.mEJRewardTopAppMs) {
                            Slog.w(QuotaController.TAG, "EJ top app time chunk less than reward: " + QuotaController.this.mEJTopAppTimeChunkSizeMs + " vs " + QuotaController.this.mEJRewardTopAppMs);
                            return;
                        }
                        return;
                    }
                    return;
                case '\n':
                    int i2 = properties.getInt(str, 48);
                    this.MAX_JOB_COUNT_RARE = i2;
                    int max2 = Math.max(10, i2);
                    if (QuotaController.this.mMaxBucketJobCounts[3] != max2) {
                        QuotaController.this.mMaxBucketJobCounts[3] = max2;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case '\r':
                    long j3 = properties.getLong(str, DEFAULT_EJ_REWARD_INTERACTION_MS);
                    this.EJ_REWARD_INTERACTION_MS = j3;
                    QuotaController.this.mEJRewardInteractionMs = Math.min(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.max(DEFAULT_TIMING_SESSION_COALESCING_DURATION_MS, j3));
                    return;
                case 14:
                case 15:
                case PackageManagerService.MIN_INSTALLABLE_TARGET_SDK /* 23 */:
                    updateRateLimitingConstantsLocked();
                    return;
                case 16:
                    int i3 = properties.getInt(str, 75);
                    this.MAX_JOB_COUNT_EXEMPTED = i3;
                    int max3 = Math.max(10, i3);
                    if (QuotaController.this.mMaxBucketJobCounts[6] != max3) {
                        QuotaController.this.mMaxBucketJobCounts[6] = max3;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 17:
                    int i4 = properties.getInt(str, 10);
                    this.MAX_JOB_COUNT_RESTRICTED = i4;
                    int max4 = Math.max(10, i4);
                    if (QuotaController.this.mMaxBucketJobCounts[5] != max4) {
                        QuotaController.this.mMaxBucketJobCounts[5] = max4;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 20:
                    int i5 = properties.getInt(str, 200);
                    this.MAX_JOB_COUNT_FREQUENT = i5;
                    int max5 = Math.max(10, i5);
                    if (QuotaController.this.mMaxBucketJobCounts[2] != max5) {
                        QuotaController.this.mMaxBucketJobCounts[2] = max5;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 28:
                    long j4 = properties.getLong(str, 60000L);
                    this.EJ_GRACE_PERIOD_TOP_APP_MS = j4;
                    QuotaController.this.mEJGracePeriodTopAppMs = Math.min(3600000L, Math.max(0L, j4));
                    return;
                case HdmiCecKeycode.CEC_KEYCODE_NUMBER_12 /* 31 */:
                    int i6 = properties.getInt(str, 75);
                    this.MAX_SESSION_COUNT_ACTIVE = i6;
                    int max6 = Math.max(1, i6);
                    if (QuotaController.this.mMaxBucketSessionCounts[0] != max6) {
                        QuotaController.this.mMaxBucketSessionCounts[0] = max6;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case '\"':
                    long j5 = properties.getLong(str, 180000L);
                    this.EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = j5;
                    QuotaController.this.mEJGracePeriodTempAllowlistMs = Math.min(3600000L, Math.max(0L, j5));
                    return;
                case '#':
                    this.MIN_QUOTA_CHECK_DELAY_MS = properties.getLong(str, 60000L);
                    QuotaController.this.mInQuotaAlarmQueue.setMinTimeBetweenAlarmsMs(Math.min(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.max(0L, this.MIN_QUOTA_CHECK_DELAY_MS)));
                    return;
                case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_5 /* 37 */:
                    int i7 = properties.getInt(str, 75);
                    this.MAX_SESSION_COUNT_EXEMPTED = i7;
                    int max7 = Math.max(1, i7);
                    if (QuotaController.this.mMaxBucketSessionCounts[6] != max7) {
                        QuotaController.this.mMaxBucketSessionCounts[6] = max7;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case '&':
                    int i8 = properties.getInt(str, 10);
                    this.MAX_SESSION_COUNT_WORKING = i8;
                    int max8 = Math.max(1, i8);
                    if (QuotaController.this.mMaxBucketSessionCounts[1] != max8) {
                        QuotaController.this.mMaxBucketSessionCounts[1] = max8;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_9 /* 41 */:
                    long j6 = properties.getLong(str, 10000L);
                    this.EJ_REWARD_TOP_APP_MS = j6;
                    long min2 = Math.min(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.max(10000L, j6));
                    if (QuotaController.this.mEJRewardTopAppMs != min2) {
                        QuotaController.this.mEJRewardTopAppMs = min2;
                        if (QuotaController.this.mEJTopAppTimeChunkSizeMs < QuotaController.this.mEJRewardTopAppMs) {
                            Slog.w(QuotaController.TAG, "EJ top app time chunk less than reward: " + QuotaController.this.mEJTopAppTimeChunkSizeMs + " vs " + QuotaController.this.mEJRewardTopAppMs);
                            return;
                        }
                        return;
                    }
                    return;
                case HdmiCecKeycode.CEC_KEYCODE_DOT /* 42 */:
                    int i9 = properties.getInt(str, DEFAULT_MAX_JOB_COUNT_WORKING);
                    this.MAX_JOB_COUNT_WORKING = i9;
                    int max9 = Math.max(10, i9);
                    if (QuotaController.this.mMaxBucketJobCounts[1] != max9) {
                        QuotaController.this.mMaxBucketJobCounts[1] = max9;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case HdmiCecKeycode.CEC_KEYCODE_CLEAR /* 44 */:
                    long j7 = properties.getLong(str, DEFAULT_TIMING_SESSION_COALESCING_DURATION_MS);
                    this.TIMING_SESSION_COALESCING_DURATION_MS = j7;
                    long min3 = Math.min(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.max(0L, j7));
                    if (QuotaController.this.mTimingSessionCoalescingDurationMs != min3) {
                        QuotaController.this.mTimingSessionCoalescingDurationMs = min3;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case '.':
                    int i10 = properties.getInt(str, 1);
                    this.MAX_SESSION_COUNT_RESTRICTED = i10;
                    int max10 = Math.max(0, i10);
                    if (QuotaController.this.mMaxBucketSessionCounts[5] != max10) {
                        QuotaController.this.mMaxBucketSessionCounts[5] = max10;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case HdmiCecKeycode.CEC_KEYCODE_NEXT_FAVORITE /* 47 */:
                    int i11 = properties.getInt(str, 3);
                    this.MAX_SESSION_COUNT_RARE = i11;
                    int max11 = Math.max(1, i11);
                    if (QuotaController.this.mMaxBucketSessionCounts[3] != max11) {
                        QuotaController.this.mMaxBucketSessionCounts[3] = max11;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case '0':
                    int i12 = properties.getInt(str, 8);
                    this.MAX_SESSION_COUNT_FREQUENT = i12;
                    int max12 = Math.max(1, i12);
                    if (QuotaController.this.mMaxBucketSessionCounts[2] != max12) {
                        QuotaController.this.mMaxBucketSessionCounts[2] = max12;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void updateExecutionPeriodConstantsLocked() {
            if (this.mExecutionPeriodConstantsUpdated) {
                return;
            }
            this.mExecutionPeriodConstantsUpdated = true;
            DeviceConfig.Properties properties = DeviceConfig.getProperties("jobscheduler", new String[]{KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS, KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS, KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS, KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS, KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS, KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS, KEY_IN_QUOTA_BUFFER_MS, KEY_MAX_EXECUTION_TIME_MS, KEY_WINDOW_SIZE_EXEMPTED_MS, KEY_WINDOW_SIZE_ACTIVE_MS, KEY_WINDOW_SIZE_WORKING_MS, KEY_WINDOW_SIZE_FREQUENT_MS, KEY_WINDOW_SIZE_RARE_MS, KEY_WINDOW_SIZE_RESTRICTED_MS});
            this.ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS, 600000L);
            this.ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS, 600000L);
            this.ALLOWED_TIME_PER_PERIOD_WORKING_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS, 600000L);
            this.ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS, 600000L);
            this.ALLOWED_TIME_PER_PERIOD_RARE_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS, 600000L);
            this.ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS, 600000L);
            this.IN_QUOTA_BUFFER_MS = properties.getLong(KEY_IN_QUOTA_BUFFER_MS, 30000L);
            this.MAX_EXECUTION_TIME_MS = properties.getLong(KEY_MAX_EXECUTION_TIME_MS, 14400000L);
            this.WINDOW_SIZE_EXEMPTED_MS = properties.getLong(KEY_WINDOW_SIZE_EXEMPTED_MS, 600000L);
            this.WINDOW_SIZE_ACTIVE_MS = properties.getLong(KEY_WINDOW_SIZE_ACTIVE_MS, 600000L);
            this.WINDOW_SIZE_WORKING_MS = properties.getLong(KEY_WINDOW_SIZE_WORKING_MS, 7200000L);
            this.WINDOW_SIZE_FREQUENT_MS = properties.getLong(KEY_WINDOW_SIZE_FREQUENT_MS, 28800000L);
            this.WINDOW_SIZE_RARE_MS = properties.getLong(KEY_WINDOW_SIZE_RARE_MS, 86400000L);
            this.WINDOW_SIZE_RESTRICTED_MS = properties.getLong(KEY_WINDOW_SIZE_RESTRICTED_MS, 86400000L);
            long max = Math.max(3600000L, Math.min(86400000L, this.MAX_EXECUTION_TIME_MS));
            if (QuotaController.this.mMaxExecutionTimeMs != max) {
                QuotaController.this.mMaxExecutionTimeMs = max;
                QuotaController quotaController = QuotaController.this;
                quotaController.mMaxExecutionTimeIntoQuotaMs = quotaController.mMaxExecutionTimeMs - QuotaController.this.mQuotaBufferMs;
                this.mShouldReevaluateConstraints = true;
            }
            long min = Math.min(QuotaController.this.mMaxExecutionTimeMs, Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS));
            long min2 = Math.min(JobStatus.NO_LATEST_RUNTIME, min);
            if (QuotaController.this.mAllowedTimePerPeriodMs[6] != min) {
                QuotaController.this.mAllowedTimePerPeriodMs[6] = min;
                this.mShouldReevaluateConstraints = true;
            }
            long min3 = Math.min(QuotaController.this.mMaxExecutionTimeMs, Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_ACTIVE_MS));
            long min4 = Math.min(min2, min3);
            if (QuotaController.this.mAllowedTimePerPeriodMs[0] != min3) {
                QuotaController.this.mAllowedTimePerPeriodMs[0] = min3;
                this.mShouldReevaluateConstraints = true;
            }
            long min5 = Math.min(QuotaController.this.mMaxExecutionTimeMs, Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_WORKING_MS));
            long min6 = Math.min(min4, min5);
            if (QuotaController.this.mAllowedTimePerPeriodMs[1] != min5) {
                QuotaController.this.mAllowedTimePerPeriodMs[1] = min5;
                this.mShouldReevaluateConstraints = true;
            }
            long min7 = Math.min(QuotaController.this.mMaxExecutionTimeMs, Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_FREQUENT_MS));
            long min8 = Math.min(min6, min7);
            if (QuotaController.this.mAllowedTimePerPeriodMs[2] != min7) {
                QuotaController.this.mAllowedTimePerPeriodMs[2] = min7;
                this.mShouldReevaluateConstraints = true;
            }
            long min9 = Math.min(QuotaController.this.mMaxExecutionTimeMs, Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_RARE_MS));
            long min10 = Math.min(min8, min9);
            if (QuotaController.this.mAllowedTimePerPeriodMs[3] != min9) {
                QuotaController.this.mAllowedTimePerPeriodMs[3] = min9;
                this.mShouldReevaluateConstraints = true;
            }
            long min11 = Math.min(QuotaController.this.mMaxExecutionTimeMs, Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS));
            long min12 = Math.min(min10, min11);
            if (QuotaController.this.mAllowedTimePerPeriodMs[5] != min11) {
                QuotaController.this.mAllowedTimePerPeriodMs[5] = min11;
                this.mShouldReevaluateConstraints = true;
            }
            long max2 = Math.max(0L, Math.min(min12, Math.min(DEFAULT_EJ_LIMIT_RESTRICTED_MS, this.IN_QUOTA_BUFFER_MS)));
            if (QuotaController.this.mQuotaBufferMs != max2) {
                QuotaController.this.mQuotaBufferMs = max2;
                QuotaController quotaController2 = QuotaController.this;
                quotaController2.mMaxExecutionTimeIntoQuotaMs = quotaController2.mMaxExecutionTimeMs - QuotaController.this.mQuotaBufferMs;
                this.mShouldReevaluateConstraints = true;
            }
            long max3 = Math.max(QuotaController.this.mAllowedTimePerPeriodMs[6], Math.min(86400000L, this.WINDOW_SIZE_EXEMPTED_MS));
            if (QuotaController.this.mBucketPeriodsMs[6] != max3) {
                QuotaController.this.mBucketPeriodsMs[6] = max3;
                this.mShouldReevaluateConstraints = true;
            }
            long max4 = Math.max(QuotaController.this.mAllowedTimePerPeriodMs[0], Math.min(86400000L, this.WINDOW_SIZE_ACTIVE_MS));
            if (QuotaController.this.mBucketPeriodsMs[0] != max4) {
                QuotaController.this.mBucketPeriodsMs[0] = max4;
                this.mShouldReevaluateConstraints = true;
            }
            long max5 = Math.max(QuotaController.this.mAllowedTimePerPeriodMs[1], Math.min(86400000L, this.WINDOW_SIZE_WORKING_MS));
            if (QuotaController.this.mBucketPeriodsMs[1] != max5) {
                QuotaController.this.mBucketPeriodsMs[1] = max5;
                this.mShouldReevaluateConstraints = true;
            }
            long max6 = Math.max(QuotaController.this.mAllowedTimePerPeriodMs[2], Math.min(86400000L, this.WINDOW_SIZE_FREQUENT_MS));
            if (QuotaController.this.mBucketPeriodsMs[2] != max6) {
                QuotaController.this.mBucketPeriodsMs[2] = max6;
                this.mShouldReevaluateConstraints = true;
            }
            long max7 = Math.max(QuotaController.this.mAllowedTimePerPeriodMs[3], Math.min(86400000L, this.WINDOW_SIZE_RARE_MS));
            if (QuotaController.this.mBucketPeriodsMs[3] != max7) {
                QuotaController.this.mBucketPeriodsMs[3] = max7;
                this.mShouldReevaluateConstraints = true;
            }
            long max8 = Math.max(QuotaController.this.mAllowedTimePerPeriodMs[5], Math.min(UnixCalendar.WEEK_IN_MILLIS, this.WINDOW_SIZE_RESTRICTED_MS));
            if (QuotaController.this.mBucketPeriodsMs[5] != max8) {
                QuotaController.this.mBucketPeriodsMs[5] = max8;
                this.mShouldReevaluateConstraints = true;
            }
        }

        private void updateRateLimitingConstantsLocked() {
            if (this.mRateLimitingConstantsUpdated) {
                return;
            }
            this.mRateLimitingConstantsUpdated = true;
            DeviceConfig.Properties properties = DeviceConfig.getProperties("jobscheduler", new String[]{KEY_RATE_LIMITING_WINDOW_MS, KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW, KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW});
            this.RATE_LIMITING_WINDOW_MS = properties.getLong(KEY_RATE_LIMITING_WINDOW_MS, 60000L);
            this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = properties.getInt(KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW, 20);
            this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = properties.getInt(KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW, 20);
            long min = Math.min(86400000L, Math.max(30000L, this.RATE_LIMITING_WINDOW_MS));
            if (QuotaController.this.mRateLimitingWindowMs != min) {
                QuotaController.this.mRateLimitingWindowMs = min;
                this.mShouldReevaluateConstraints = true;
            }
            int max = Math.max(10, this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW);
            if (QuotaController.this.mMaxJobCountPerRateLimitingWindow != max) {
                QuotaController.this.mMaxJobCountPerRateLimitingWindow = max;
                this.mShouldReevaluateConstraints = true;
            }
            int max2 = Math.max(10, this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW);
            if (QuotaController.this.mMaxSessionCountPerRateLimitingWindow != max2) {
                QuotaController.this.mMaxSessionCountPerRateLimitingWindow = max2;
                this.mShouldReevaluateConstraints = true;
            }
        }

        private void updateEJLimitConstantsLocked() {
            if (this.mEJLimitConstantsUpdated) {
                return;
            }
            this.mEJLimitConstantsUpdated = true;
            DeviceConfig.Properties properties = DeviceConfig.getProperties("jobscheduler", new String[]{KEY_EJ_LIMIT_EXEMPTED_MS, KEY_EJ_LIMIT_ACTIVE_MS, KEY_EJ_LIMIT_WORKING_MS, KEY_EJ_LIMIT_FREQUENT_MS, KEY_EJ_LIMIT_RARE_MS, KEY_EJ_LIMIT_RESTRICTED_MS, KEY_EJ_LIMIT_ADDITION_SPECIAL_MS, KEY_EJ_LIMIT_ADDITION_INSTALLER_MS, KEY_EJ_WINDOW_SIZE_MS});
            this.EJ_LIMIT_EXEMPTED_MS = properties.getLong(KEY_EJ_LIMIT_EXEMPTED_MS, 3600000L);
            this.EJ_LIMIT_ACTIVE_MS = properties.getLong(KEY_EJ_LIMIT_ACTIVE_MS, 1800000L);
            this.EJ_LIMIT_WORKING_MS = properties.getLong(KEY_EJ_LIMIT_WORKING_MS, 1800000L);
            this.EJ_LIMIT_FREQUENT_MS = properties.getLong(KEY_EJ_LIMIT_FREQUENT_MS, 600000L);
            this.EJ_LIMIT_RARE_MS = properties.getLong(KEY_EJ_LIMIT_RARE_MS, 600000L);
            this.EJ_LIMIT_RESTRICTED_MS = properties.getLong(KEY_EJ_LIMIT_RESTRICTED_MS, DEFAULT_EJ_LIMIT_RESTRICTED_MS);
            this.EJ_LIMIT_ADDITION_INSTALLER_MS = properties.getLong(KEY_EJ_LIMIT_ADDITION_INSTALLER_MS, 1800000L);
            this.EJ_LIMIT_ADDITION_SPECIAL_MS = properties.getLong(KEY_EJ_LIMIT_ADDITION_SPECIAL_MS, DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS);
            long j = properties.getLong(KEY_EJ_WINDOW_SIZE_MS, 86400000L);
            this.EJ_WINDOW_SIZE_MS = j;
            long max = Math.max(3600000L, Math.min(86400000L, j));
            if (QuotaController.this.mEJLimitWindowSizeMs != max) {
                QuotaController.this.mEJLimitWindowSizeMs = max;
                this.mShouldReevaluateConstraints = true;
            }
            long max2 = Math.max(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.min(max, this.EJ_LIMIT_EXEMPTED_MS));
            if (QuotaController.this.mEJLimitsMs[6] != max2) {
                QuotaController.this.mEJLimitsMs[6] = max2;
                this.mShouldReevaluateConstraints = true;
            }
            long max3 = Math.max(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.min(max2, this.EJ_LIMIT_ACTIVE_MS));
            if (QuotaController.this.mEJLimitsMs[0] != max3) {
                QuotaController.this.mEJLimitsMs[0] = max3;
                this.mShouldReevaluateConstraints = true;
            }
            long max4 = Math.max(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, Math.min(max3, this.EJ_LIMIT_WORKING_MS));
            if (QuotaController.this.mEJLimitsMs[1] != max4) {
                QuotaController.this.mEJLimitsMs[1] = max4;
                this.mShouldReevaluateConstraints = true;
            }
            long max5 = Math.max(600000L, Math.min(max4, this.EJ_LIMIT_FREQUENT_MS));
            if (QuotaController.this.mEJLimitsMs[2] != max5) {
                QuotaController.this.mEJLimitsMs[2] = max5;
                this.mShouldReevaluateConstraints = true;
            }
            long max6 = Math.max(600000L, Math.min(max5, this.EJ_LIMIT_RARE_MS));
            if (QuotaController.this.mEJLimitsMs[3] != max6) {
                QuotaController.this.mEJLimitsMs[3] = max6;
                this.mShouldReevaluateConstraints = true;
            }
            long max7 = Math.max(DEFAULT_EJ_LIMIT_RESTRICTED_MS, Math.min(max6, this.EJ_LIMIT_RESTRICTED_MS));
            if (QuotaController.this.mEJLimitsMs[5] != max7) {
                QuotaController.this.mEJLimitsMs[5] = max7;
                this.mShouldReevaluateConstraints = true;
            }
            long j2 = max - max3;
            long max8 = Math.max(0L, Math.min(j2, this.EJ_LIMIT_ADDITION_INSTALLER_MS));
            if (QuotaController.this.mEjLimitAdditionInstallerMs != max8) {
                QuotaController.this.mEjLimitAdditionInstallerMs = max8;
                this.mShouldReevaluateConstraints = true;
            }
            long max9 = Math.max(0L, Math.min(j2, this.EJ_LIMIT_ADDITION_SPECIAL_MS));
            if (QuotaController.this.mEjLimitAdditionSpecialMs != max9) {
                QuotaController.this.mEjLimitAdditionSpecialMs = max9;
                this.mShouldReevaluateConstraints = true;
            }
        }

        private void updateQuotaBumpConstantsLocked() {
            if (this.mQuotaBumpConstantsUpdated) {
                return;
            }
            this.mQuotaBumpConstantsUpdated = true;
            DeviceConfig.Properties properties = DeviceConfig.getProperties("jobscheduler", new String[]{KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS, KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT, KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT, KEY_QUOTA_BUMP_WINDOW_SIZE_MS, KEY_QUOTA_BUMP_LIMIT});
            this.QUOTA_BUMP_ADDITIONAL_DURATION_MS = properties.getLong(KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS, 60000L);
            this.QUOTA_BUMP_ADDITIONAL_JOB_COUNT = properties.getInt(KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT, 2);
            this.QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = properties.getInt(KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT, 1);
            this.QUOTA_BUMP_WINDOW_SIZE_MS = properties.getLong(KEY_QUOTA_BUMP_WINDOW_SIZE_MS, 28800000L);
            this.QUOTA_BUMP_LIMIT = properties.getInt(KEY_QUOTA_BUMP_LIMIT, 8);
            long max = Math.max(3600000L, Math.min(86400000L, this.QUOTA_BUMP_WINDOW_SIZE_MS));
            if (QuotaController.this.mQuotaBumpWindowSizeMs != max) {
                QuotaController.this.mQuotaBumpWindowSizeMs = max;
                this.mShouldReevaluateConstraints = true;
            }
            int max2 = Math.max(0, this.QUOTA_BUMP_LIMIT);
            if (QuotaController.this.mQuotaBumpLimit != max2) {
                QuotaController.this.mQuotaBumpLimit = max2;
                this.mShouldReevaluateConstraints = true;
            }
            int max3 = Math.max(0, this.QUOTA_BUMP_ADDITIONAL_JOB_COUNT);
            if (QuotaController.this.mQuotaBumpAdditionalJobCount != max3) {
                QuotaController.this.mQuotaBumpAdditionalJobCount = max3;
                this.mShouldReevaluateConstraints = true;
            }
            int max4 = Math.max(0, this.QUOTA_BUMP_ADDITIONAL_SESSION_COUNT);
            if (QuotaController.this.mQuotaBumpAdditionalSessionCount != max4) {
                QuotaController.this.mQuotaBumpAdditionalSessionCount = max4;
                this.mShouldReevaluateConstraints = true;
            }
            long max5 = Math.max(0L, Math.min(600000L, this.QUOTA_BUMP_ADDITIONAL_DURATION_MS));
            if (QuotaController.this.mQuotaBumpAdditionalDurationMs != max5) {
                QuotaController.this.mQuotaBumpAdditionalDurationMs = max5;
                this.mShouldReevaluateConstraints = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println();
            indentingPrintWriter.println("QuotaController:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS, Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS)).println();
            indentingPrintWriter.print(KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS, Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_ACTIVE_MS)).println();
            indentingPrintWriter.print(KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS, Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_WORKING_MS)).println();
            indentingPrintWriter.print(KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS, Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_FREQUENT_MS)).println();
            indentingPrintWriter.print(KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS, Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_RARE_MS)).println();
            indentingPrintWriter.print(KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS, Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS)).println();
            indentingPrintWriter.print(KEY_IN_QUOTA_BUFFER_MS, Long.valueOf(this.IN_QUOTA_BUFFER_MS)).println();
            indentingPrintWriter.print(KEY_WINDOW_SIZE_EXEMPTED_MS, Long.valueOf(this.WINDOW_SIZE_EXEMPTED_MS)).println();
            indentingPrintWriter.print(KEY_WINDOW_SIZE_ACTIVE_MS, Long.valueOf(this.WINDOW_SIZE_ACTIVE_MS)).println();
            indentingPrintWriter.print(KEY_WINDOW_SIZE_WORKING_MS, Long.valueOf(this.WINDOW_SIZE_WORKING_MS)).println();
            indentingPrintWriter.print(KEY_WINDOW_SIZE_FREQUENT_MS, Long.valueOf(this.WINDOW_SIZE_FREQUENT_MS)).println();
            indentingPrintWriter.print(KEY_WINDOW_SIZE_RARE_MS, Long.valueOf(this.WINDOW_SIZE_RARE_MS)).println();
            indentingPrintWriter.print(KEY_WINDOW_SIZE_RESTRICTED_MS, Long.valueOf(this.WINDOW_SIZE_RESTRICTED_MS)).println();
            indentingPrintWriter.print(KEY_MAX_EXECUTION_TIME_MS, Long.valueOf(this.MAX_EXECUTION_TIME_MS)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_EXEMPTED, Integer.valueOf(this.MAX_JOB_COUNT_EXEMPTED)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_ACTIVE, Integer.valueOf(this.MAX_JOB_COUNT_ACTIVE)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_WORKING, Integer.valueOf(this.MAX_JOB_COUNT_WORKING)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_FREQUENT, Integer.valueOf(this.MAX_JOB_COUNT_FREQUENT)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_RARE, Integer.valueOf(this.MAX_JOB_COUNT_RARE)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_RESTRICTED, Integer.valueOf(this.MAX_JOB_COUNT_RESTRICTED)).println();
            indentingPrintWriter.print(KEY_RATE_LIMITING_WINDOW_MS, Long.valueOf(this.RATE_LIMITING_WINDOW_MS)).println();
            indentingPrintWriter.print(KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW, Integer.valueOf(this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_EXEMPTED, Integer.valueOf(this.MAX_SESSION_COUNT_EXEMPTED)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_ACTIVE, Integer.valueOf(this.MAX_SESSION_COUNT_ACTIVE)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_WORKING, Integer.valueOf(this.MAX_SESSION_COUNT_WORKING)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_FREQUENT, Integer.valueOf(this.MAX_SESSION_COUNT_FREQUENT)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_RARE, Integer.valueOf(this.MAX_SESSION_COUNT_RARE)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_RESTRICTED, Integer.valueOf(this.MAX_SESSION_COUNT_RESTRICTED)).println();
            indentingPrintWriter.print(KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW, Integer.valueOf(this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW)).println();
            indentingPrintWriter.print(KEY_TIMING_SESSION_COALESCING_DURATION_MS, Long.valueOf(this.TIMING_SESSION_COALESCING_DURATION_MS)).println();
            indentingPrintWriter.print(KEY_MIN_QUOTA_CHECK_DELAY_MS, Long.valueOf(this.MIN_QUOTA_CHECK_DELAY_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_EXEMPTED_MS, Long.valueOf(this.EJ_LIMIT_EXEMPTED_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_ACTIVE_MS, Long.valueOf(this.EJ_LIMIT_ACTIVE_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_WORKING_MS, Long.valueOf(this.EJ_LIMIT_WORKING_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_FREQUENT_MS, Long.valueOf(this.EJ_LIMIT_FREQUENT_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_RARE_MS, Long.valueOf(this.EJ_LIMIT_RARE_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_RESTRICTED_MS, Long.valueOf(this.EJ_LIMIT_RESTRICTED_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_ADDITION_INSTALLER_MS, Long.valueOf(this.EJ_LIMIT_ADDITION_INSTALLER_MS)).println();
            indentingPrintWriter.print(KEY_EJ_LIMIT_ADDITION_SPECIAL_MS, Long.valueOf(this.EJ_LIMIT_ADDITION_SPECIAL_MS)).println();
            indentingPrintWriter.print(KEY_EJ_WINDOW_SIZE_MS, Long.valueOf(this.EJ_WINDOW_SIZE_MS)).println();
            indentingPrintWriter.print(KEY_EJ_TOP_APP_TIME_CHUNK_SIZE_MS, Long.valueOf(this.EJ_TOP_APP_TIME_CHUNK_SIZE_MS)).println();
            indentingPrintWriter.print(KEY_EJ_REWARD_TOP_APP_MS, Long.valueOf(this.EJ_REWARD_TOP_APP_MS)).println();
            indentingPrintWriter.print(KEY_EJ_REWARD_INTERACTION_MS, Long.valueOf(this.EJ_REWARD_INTERACTION_MS)).println();
            indentingPrintWriter.print(KEY_EJ_REWARD_NOTIFICATION_SEEN_MS, Long.valueOf(this.EJ_REWARD_NOTIFICATION_SEEN_MS)).println();
            indentingPrintWriter.print(KEY_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS, Long.valueOf(this.EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS)).println();
            indentingPrintWriter.print(KEY_EJ_GRACE_PERIOD_TOP_APP_MS, Long.valueOf(this.EJ_GRACE_PERIOD_TOP_APP_MS)).println();
            indentingPrintWriter.print(KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS, Long.valueOf(this.QUOTA_BUMP_ADDITIONAL_DURATION_MS)).println();
            indentingPrintWriter.print(KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT, Integer.valueOf(this.QUOTA_BUMP_ADDITIONAL_JOB_COUNT)).println();
            indentingPrintWriter.print(KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT, Integer.valueOf(this.QUOTA_BUMP_ADDITIONAL_SESSION_COUNT)).println();
            indentingPrintWriter.print(KEY_QUOTA_BUMP_WINDOW_SIZE_MS, Long.valueOf(this.QUOTA_BUMP_WINDOW_SIZE_MS)).println();
            indentingPrintWriter.print(KEY_QUOTA_BUMP_LIMIT, Integer.valueOf(this.QUOTA_BUMP_LIMIT)).println();
            indentingPrintWriter.decreaseIndent();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(ProtoOutputStream protoOutputStream) {
            long start = protoOutputStream.start(1146756268056L);
            protoOutputStream.write(1112396529666L, this.IN_QUOTA_BUFFER_MS);
            protoOutputStream.write(1112396529667L, this.WINDOW_SIZE_ACTIVE_MS);
            protoOutputStream.write(1112396529668L, this.WINDOW_SIZE_WORKING_MS);
            protoOutputStream.write(1112396529669L, this.WINDOW_SIZE_FREQUENT_MS);
            protoOutputStream.write(1112396529670L, this.WINDOW_SIZE_RARE_MS);
            protoOutputStream.write(1112396529684L, this.WINDOW_SIZE_RESTRICTED_MS);
            protoOutputStream.write(1112396529671L, this.MAX_EXECUTION_TIME_MS);
            protoOutputStream.write(1120986464264L, this.MAX_JOB_COUNT_ACTIVE);
            protoOutputStream.write(1120986464265L, this.MAX_JOB_COUNT_WORKING);
            protoOutputStream.write(1120986464266L, this.MAX_JOB_COUNT_FREQUENT);
            protoOutputStream.write(1120986464267L, this.MAX_JOB_COUNT_RARE);
            protoOutputStream.write(1120986464277L, this.MAX_JOB_COUNT_RESTRICTED);
            protoOutputStream.write(1120986464275L, this.RATE_LIMITING_WINDOW_MS);
            protoOutputStream.write(1120986464268L, this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW);
            protoOutputStream.write(1120986464269L, this.MAX_SESSION_COUNT_ACTIVE);
            protoOutputStream.write(1120986464270L, this.MAX_SESSION_COUNT_WORKING);
            protoOutputStream.write(1120986464271L, this.MAX_SESSION_COUNT_FREQUENT);
            protoOutputStream.write(1120986464272L, this.MAX_SESSION_COUNT_RARE);
            protoOutputStream.write(1120986464278L, this.MAX_SESSION_COUNT_RESTRICTED);
            protoOutputStream.write(1120986464273L, this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW);
            protoOutputStream.write(1112396529682L, this.TIMING_SESSION_COALESCING_DURATION_MS);
            protoOutputStream.write(1112396529687L, this.MIN_QUOTA_CHECK_DELAY_MS);
            protoOutputStream.write(1112396529688L, this.EJ_LIMIT_ACTIVE_MS);
            protoOutputStream.write(1112396529689L, this.EJ_LIMIT_WORKING_MS);
            protoOutputStream.write(1112396529690L, this.EJ_LIMIT_FREQUENT_MS);
            protoOutputStream.write(1112396529691L, this.EJ_LIMIT_RARE_MS);
            protoOutputStream.write(1112396529692L, this.EJ_LIMIT_RESTRICTED_MS);
            protoOutputStream.write(1112396529693L, this.EJ_WINDOW_SIZE_MS);
            protoOutputStream.write(1112396529694L, this.EJ_TOP_APP_TIME_CHUNK_SIZE_MS);
            protoOutputStream.write(1112396529695L, this.EJ_REWARD_TOP_APP_MS);
            protoOutputStream.write(1112396529696L, this.EJ_REWARD_INTERACTION_MS);
            protoOutputStream.write(1112396529697L, this.EJ_REWARD_NOTIFICATION_SEEN_MS);
            protoOutputStream.end(start);
        }
    }

    @VisibleForTesting
    long[] getAllowedTimePerPeriodMs() {
        return this.mAllowedTimePerPeriodMs;
    }

    @VisibleForTesting
    int[] getBucketMaxJobCounts() {
        return this.mMaxBucketJobCounts;
    }

    @VisibleForTesting
    int[] getBucketMaxSessionCounts() {
        return this.mMaxBucketSessionCounts;
    }

    @VisibleForTesting
    long[] getBucketWindowSizes() {
        return this.mBucketPeriodsMs;
    }

    @VisibleForTesting
    SparseBooleanArray getForegroundUids() {
        return this.mForegroundUids;
    }

    @VisibleForTesting
    Handler getHandler() {
        return this.mHandler;
    }

    @VisibleForTesting
    long getEJGracePeriodTempAllowlistMs() {
        return this.mEJGracePeriodTempAllowlistMs;
    }

    @VisibleForTesting
    long getEJGracePeriodTopAppMs() {
        return this.mEJGracePeriodTopAppMs;
    }

    @VisibleForTesting
    long[] getEJLimitsMs() {
        return this.mEJLimitsMs;
    }

    @VisibleForTesting
    long getEjLimitAdditionInstallerMs() {
        return this.mEjLimitAdditionInstallerMs;
    }

    @VisibleForTesting
    long getEjLimitAdditionSpecialMs() {
        return this.mEjLimitAdditionSpecialMs;
    }

    @VisibleForTesting
    long getEJLimitWindowSizeMs() {
        return this.mEJLimitWindowSizeMs;
    }

    @VisibleForTesting
    long getEJRewardInteractionMs() {
        return this.mEJRewardInteractionMs;
    }

    @VisibleForTesting
    long getEJRewardNotificationSeenMs() {
        return this.mEJRewardNotificationSeenMs;
    }

    @VisibleForTesting
    long getEJRewardTopAppMs() {
        return this.mEJRewardTopAppMs;
    }

    @VisibleForTesting
    List<TimedEvent> getEJTimingSessions(int i, String str) {
        return (List) this.mEJTimingSessions.get(i, str);
    }

    @VisibleForTesting
    long getEJTopAppTimeChunkSizeMs() {
        return this.mEJTopAppTimeChunkSizeMs;
    }

    @VisibleForTesting
    long getInQuotaBufferMs() {
        return this.mQuotaBufferMs;
    }

    @VisibleForTesting
    long getMaxExecutionTimeMs() {
        return this.mMaxExecutionTimeMs;
    }

    @VisibleForTesting
    int getMaxJobCountPerRateLimitingWindow() {
        return this.mMaxJobCountPerRateLimitingWindow;
    }

    @VisibleForTesting
    int getMaxSessionCountPerRateLimitingWindow() {
        return this.mMaxSessionCountPerRateLimitingWindow;
    }

    @VisibleForTesting
    long getMinQuotaCheckDelayMs() {
        return this.mInQuotaAlarmQueue.getMinTimeBetweenAlarmsMs();
    }

    @VisibleForTesting
    long getRateLimitingWindowMs() {
        return this.mRateLimitingWindowMs;
    }

    @VisibleForTesting
    long getTimingSessionCoalescingDurationMs() {
        return this.mTimingSessionCoalescingDurationMs;
    }

    @VisibleForTesting
    List<TimedEvent> getTimingSessions(int i, String str) {
        return (List) this.mTimingEvents.get(i, str);
    }

    @VisibleForTesting
    QcConstants getQcConstants() {
        return this.mQcConstants;
    }

    @VisibleForTesting
    long getQuotaBumpAdditionDurationMs() {
        return this.mQuotaBumpAdditionalDurationMs;
    }

    @VisibleForTesting
    int getQuotaBumpAdditionJobCount() {
        return this.mQuotaBumpAdditionalJobCount;
    }

    @VisibleForTesting
    int getQuotaBumpAdditionSessionCount() {
        return this.mQuotaBumpAdditionalSessionCount;
    }

    @VisibleForTesting
    int getQuotaBumpLimit() {
        return this.mQuotaBumpLimit;
    }

    @VisibleForTesting
    long getQuotaBumpWindowSizeMs() {
        return this.mQuotaBumpWindowSizeMs;
    }

    @Override // com.android.server.job.controllers.StateController
    @NeverCompile
    public void dumpControllerStateLocked(final IndentingPrintWriter indentingPrintWriter, final Predicate<JobStatus> predicate) {
        indentingPrintWriter.println("Is enabled: " + this.mIsEnabled);
        indentingPrintWriter.println("Current elapsed time: " + JobSchedulerService.sElapsedRealtimeClock.millis());
        indentingPrintWriter.println();
        indentingPrintWriter.print("Foreground UIDs: ");
        indentingPrintWriter.println(this.mForegroundUids.toString());
        indentingPrintWriter.println();
        indentingPrintWriter.print("Cached top apps: ");
        indentingPrintWriter.println(this.mTopAppCache.toString());
        indentingPrintWriter.print("Cached top app grace period: ");
        indentingPrintWriter.println(this.mTopAppGraceCache.toString());
        indentingPrintWriter.print("Cached temp allowlist: ");
        indentingPrintWriter.println(this.mTempAllowlistCache.toString());
        indentingPrintWriter.print("Cached temp allowlist grace period: ");
        indentingPrintWriter.println(this.mTempAllowlistGraceCache.toString());
        indentingPrintWriter.println();
        indentingPrintWriter.println("Special apps:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("System installers={");
        for (int i = 0; i < this.mSystemInstallers.size(); i++) {
            if (i > 0) {
                indentingPrintWriter.print(", ");
            }
            indentingPrintWriter.print(this.mSystemInstallers.keyAt(i));
            indentingPrintWriter.print("->");
            indentingPrintWriter.print(this.mSystemInstallers.get(i));
        }
        indentingPrintWriter.println("}");
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        this.mTrackedJobs.forEach(new Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QuotaController.this.lambda$dumpControllerStateLocked$4(predicate, indentingPrintWriter, (ArraySet) obj);
            }
        });
        indentingPrintWriter.println();
        for (int i2 = 0; i2 < this.mPkgTimers.numMaps(); i2++) {
            int keyAt = this.mPkgTimers.keyAt(i2);
            for (int i3 = 0; i3 < this.mPkgTimers.numElementsForKey(keyAt); i3++) {
                String str = (String) this.mPkgTimers.keyAt(i2, i3);
                ((Timer) this.mPkgTimers.valueAt(i2, i3)).dump(indentingPrintWriter, predicate);
                indentingPrintWriter.println();
                List list = (List) this.mTimingEvents.get(keyAt, str);
                if (list != null) {
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println("Saved events:");
                    indentingPrintWriter.increaseIndent();
                    for (int size = list.size() - 1; size >= 0; size--) {
                        ((TimedEvent) list.get(size)).dump(indentingPrintWriter);
                    }
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.println();
                }
            }
        }
        indentingPrintWriter.println();
        for (int i4 = 0; i4 < this.mEJPkgTimers.numMaps(); i4++) {
            int keyAt2 = this.mEJPkgTimers.keyAt(i4);
            for (int i5 = 0; i5 < this.mEJPkgTimers.numElementsForKey(keyAt2); i5++) {
                String str2 = (String) this.mEJPkgTimers.keyAt(i4, i5);
                ((Timer) this.mEJPkgTimers.valueAt(i4, i5)).dump(indentingPrintWriter, predicate);
                indentingPrintWriter.println();
                List list2 = (List) this.mEJTimingSessions.get(keyAt2, str2);
                if (list2 != null) {
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println("Saved sessions:");
                    indentingPrintWriter.increaseIndent();
                    for (int size2 = list2.size() - 1; size2 >= 0; size2--) {
                        ((TimedEvent) list2.get(size2)).dump(indentingPrintWriter);
                    }
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.println();
                }
            }
        }
        indentingPrintWriter.println();
        this.mTopAppTrackers.forEach(new Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((QuotaController.TopAppTimer) obj).dump(indentingPrintWriter);
            }
        });
        indentingPrintWriter.println();
        indentingPrintWriter.println("Cached execution stats:");
        indentingPrintWriter.increaseIndent();
        for (int i6 = 0; i6 < this.mExecutionStatsCache.numMaps(); i6++) {
            int keyAt3 = this.mExecutionStatsCache.keyAt(i6);
            for (int i7 = 0; i7 < this.mExecutionStatsCache.numElementsForKey(keyAt3); i7++) {
                String str3 = (String) this.mExecutionStatsCache.keyAt(i6, i7);
                ExecutionStats[] executionStatsArr = (ExecutionStats[]) this.mExecutionStatsCache.valueAt(i6, i7);
                indentingPrintWriter.println(StateController.packageToString(keyAt3, str3));
                indentingPrintWriter.increaseIndent();
                for (int i8 = 0; i8 < executionStatsArr.length; i8++) {
                    ExecutionStats executionStats = executionStatsArr[i8];
                    if (executionStats != null) {
                        indentingPrintWriter.print(JobStatus.bucketName(i8));
                        indentingPrintWriter.print(": ");
                        indentingPrintWriter.println(executionStats);
                    }
                }
                indentingPrintWriter.decreaseIndent();
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("EJ debits:");
        indentingPrintWriter.increaseIndent();
        for (int i9 = 0; i9 < this.mEJStats.numMaps(); i9++) {
            int keyAt4 = this.mEJStats.keyAt(i9);
            for (int i10 = 0; i10 < this.mEJStats.numElementsForKey(keyAt4); i10++) {
                String str4 = (String) this.mEJStats.keyAt(i9, i10);
                ShrinkableDebits shrinkableDebits = (ShrinkableDebits) this.mEJStats.valueAt(i9, i10);
                indentingPrintWriter.print(StateController.packageToString(keyAt4, str4));
                indentingPrintWriter.print(": ");
                shrinkableDebits.dumpLocked(indentingPrintWriter);
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        this.mInQuotaAlarmQueue.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$4(Predicate predicate, IndentingPrintWriter indentingPrintWriter, ArraySet arraySet) {
        for (int i = 0; i < arraySet.size(); i++) {
            JobStatus jobStatus = (JobStatus) arraySet.valueAt(i);
            if (predicate.test(jobStatus)) {
                indentingPrintWriter.print("#");
                jobStatus.printUniqueId(indentingPrintWriter);
                indentingPrintWriter.print(" from ");
                UserHandle.formatUid(indentingPrintWriter, jobStatus.getSourceUid());
                if (this.mTopStartedJobs.contains(jobStatus)) {
                    indentingPrintWriter.print(" (TOP)");
                }
                indentingPrintWriter.println();
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.print(JobStatus.bucketName(jobStatus.getEffectiveStandbyBucket()));
                indentingPrintWriter.print(", ");
                if (jobStatus.shouldTreatAsExpeditedJob()) {
                    indentingPrintWriter.print("within EJ quota");
                } else if (jobStatus.startedAsExpeditedJob) {
                    indentingPrintWriter.print("out of EJ quota");
                } else if (jobStatus.isConstraintSatisfied(DumpState.DUMP_SERVICE_PERMISSIONS)) {
                    indentingPrintWriter.print("within regular quota");
                } else {
                    indentingPrintWriter.print("not within quota");
                }
                indentingPrintWriter.print(", ");
                if (jobStatus.shouldTreatAsExpeditedJob()) {
                    indentingPrintWriter.print(getRemainingEJExecutionTimeLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
                    indentingPrintWriter.print("ms remaining in EJ quota");
                } else if (jobStatus.startedAsExpeditedJob) {
                    indentingPrintWriter.print("should be stopped after min execution time");
                } else {
                    indentingPrintWriter.print(getRemainingExecutionTimeLocked(jobStatus));
                    indentingPrintWriter.print("ms remaining in quota");
                }
                indentingPrintWriter.println();
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final ProtoOutputStream protoOutputStream, long j, Predicate<JobStatus> predicate) {
        long j2;
        int i;
        long j3;
        final Predicate<JobStatus> predicate2 = predicate;
        long start = protoOutputStream.start(j);
        long start2 = protoOutputStream.start(1146756268041L);
        protoOutputStream.write(1133871366145L, this.mService.isBatteryCharging());
        protoOutputStream.write(1112396529670L, JobSchedulerService.sElapsedRealtimeClock.millis());
        for (int i2 = 0; i2 < this.mForegroundUids.size(); i2++) {
            protoOutputStream.write(2220498092035L, this.mForegroundUids.keyAt(i2));
        }
        this.mTrackedJobs.forEach(new Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QuotaController.this.lambda$dumpControllerStateLocked$6(predicate2, protoOutputStream, (ArraySet) obj);
            }
        });
        int i3 = 0;
        while (i3 < this.mPkgTimers.numMaps()) {
            int keyAt = this.mPkgTimers.keyAt(i3);
            int i4 = 0;
            while (i4 < this.mPkgTimers.numElementsForKey(keyAt)) {
                String str = (String) this.mPkgTimers.keyAt(i3, i4);
                long start3 = protoOutputStream.start(2246267895813L);
                ((Timer) this.mPkgTimers.valueAt(i3, i4)).dump(protoOutputStream, 1146756268034L, predicate2);
                Timer timer = (Timer) this.mEJPkgTimers.get(keyAt, str);
                if (timer != null) {
                    timer.dump(protoOutputStream, 1146756268038L, predicate2);
                }
                List list = (List) this.mTimingEvents.get(keyAt, str);
                if (list != null) {
                    int size = list.size() - 1;
                    while (size >= 0) {
                        TimedEvent timedEvent = (TimedEvent) list.get(size);
                        if (timedEvent instanceof TimingSession) {
                            j3 = start;
                            ((TimingSession) timedEvent).dump(protoOutputStream, 2246267895811L);
                        } else {
                            j3 = start;
                        }
                        size--;
                        start = j3;
                    }
                }
                long j4 = start;
                ExecutionStats[] executionStatsArr = (ExecutionStats[]) this.mExecutionStatsCache.get(keyAt, str);
                if (executionStatsArr != null) {
                    int i5 = 0;
                    while (i5 < executionStatsArr.length) {
                        ExecutionStats executionStats = executionStatsArr[i5];
                        if (executionStats == null) {
                            j2 = start2;
                            i = i3;
                        } else {
                            long start4 = protoOutputStream.start(2246267895812L);
                            j2 = start2;
                            protoOutputStream.write(1159641169921L, i5);
                            i = i3;
                            protoOutputStream.write(1112396529666L, executionStats.expirationTimeElapsed);
                            protoOutputStream.write(1112396529667L, executionStats.windowSizeMs);
                            protoOutputStream.write(1120986464270L, executionStats.jobCountLimit);
                            protoOutputStream.write(1120986464271L, executionStats.sessionCountLimit);
                            protoOutputStream.write(1112396529668L, executionStats.executionTimeInWindowMs);
                            protoOutputStream.write(1120986464261L, executionStats.bgJobCountInWindow);
                            protoOutputStream.write(1112396529670L, executionStats.executionTimeInMaxPeriodMs);
                            protoOutputStream.write(1120986464263L, executionStats.bgJobCountInMaxPeriod);
                            protoOutputStream.write(1120986464267L, executionStats.sessionCountInWindow);
                            protoOutputStream.write(1112396529672L, executionStats.inQuotaTimeElapsed);
                            protoOutputStream.write(1112396529673L, executionStats.jobRateLimitExpirationTimeElapsed);
                            protoOutputStream.write(1120986464266L, executionStats.jobCountInRateLimitingWindow);
                            protoOutputStream.write(1112396529676L, executionStats.sessionRateLimitExpirationTimeElapsed);
                            protoOutputStream.write(1120986464269L, executionStats.sessionCountInRateLimitingWindow);
                            protoOutputStream.end(start4);
                        }
                        i5++;
                        i3 = i;
                        start2 = j2;
                    }
                }
                protoOutputStream.end(start3);
                i4++;
                predicate2 = predicate;
                i3 = i3;
                start = j4;
                start2 = start2;
            }
            i3++;
            predicate2 = predicate;
        }
        protoOutputStream.end(start2);
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$6(Predicate predicate, ProtoOutputStream protoOutputStream, ArraySet arraySet) {
        for (int i = 0; i < arraySet.size(); i++) {
            JobStatus jobStatus = (JobStatus) arraySet.valueAt(i);
            if (predicate.test(jobStatus)) {
                long start = protoOutputStream.start(2246267895812L);
                jobStatus.writeToShortProto(protoOutputStream, 1146756268033L);
                protoOutputStream.write(1120986464258L, jobStatus.getSourceUid());
                protoOutputStream.write(1159641169923L, jobStatus.getEffectiveStandbyBucket());
                protoOutputStream.write(1133871366148L, this.mTopStartedJobs.contains(jobStatus));
                protoOutputStream.write(1133871366149L, jobStatus.isConstraintSatisfied(DumpState.DUMP_SERVICE_PERMISSIONS));
                protoOutputStream.write(1112396529670L, getRemainingExecutionTimeLocked(jobStatus));
                protoOutputStream.write(1133871366151L, jobStatus.isRequestedExpeditedJob());
                protoOutputStream.write(1133871366152L, jobStatus.isExpeditedQuotaApproved());
                protoOutputStream.end(start);
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(IndentingPrintWriter indentingPrintWriter) {
        this.mQcConstants.dump(indentingPrintWriter);
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(ProtoOutputStream protoOutputStream) {
        this.mQcConstants.dump(protoOutputStream);
    }
}
