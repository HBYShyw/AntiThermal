package com.android.server.job.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.controllers.PrefetchController;
import com.android.server.pm.DumpState;
import com.android.server.utils.AlarmQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class FlexibilityController extends StateController {
    private static final boolean DEBUG;
    private static final int FLEXIBLE_CONSTRAINTS = 268435463;
    private static final int JOB_SPECIFIC_FLEXIBLE_CONSTRAINTS = 268435456;
    private static final int MSG_UPDATE_JOBS = 0;
    private static final long NO_LIFECYCLE_END = Long.MAX_VALUE;
    static final int NUM_FLEXIBLE_CONSTRAINTS;
    private static final int NUM_JOB_SPECIFIC_FLEXIBLE_CONSTRAINTS;
    static final int NUM_OPTIONAL_FLEXIBLE_CONSTRAINTS;
    static final int NUM_SYSTEM_WIDE_FLEXIBLE_CONSTRAINTS;
    static final int OPTIONAL_FLEXIBLE_CONSTRAINTS = 7;
    static final int SYSTEM_WIDE_FLEXIBLE_CONSTRAINTS = 7;
    private static final String TAG = "JobScheduler.Flex";
    private long mDeadlineProximityLimitMs;

    @VisibleForTesting
    boolean mDeviceSupportsFlexConstraints;
    private long mFallbackFlexibilityDeadlineMs;

    @VisibleForTesting
    final FcConfig mFcConfig;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    final FlexibilityAlarmQueue mFlexibilityAlarmQueue;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    boolean mFlexibilityEnabled;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    final FlexibilityTracker mFlexibilityTracker;
    private final FcHandler mHandler;
    private long mMaxRescheduledDeadline;
    private long mMinTimeBetweenFlexibilityAlarmsMs;
    private int[] mPercentToDropConstraints;

    @VisibleForTesting
    final PrefetchController.PrefetchChangedListener mPrefetchChangedListener;

    @VisibleForTesting
    final PrefetchController mPrefetchController;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    final SparseArrayMap<String, Long> mPrefetchLifeCycleStart;
    private long mRescheduledJobDeadline;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    int mSatisfiedFlexibleConstraints;

    static {
        DEBUG = JobSchedulerService.DEBUG || Log.isLoggable(TAG, 3);
        NUM_JOB_SPECIFIC_FLEXIBLE_CONSTRAINTS = Integer.bitCount(268435456);
        NUM_OPTIONAL_FLEXIBLE_CONSTRAINTS = Integer.bitCount(7);
        NUM_SYSTEM_WIDE_FLEXIBLE_CONSTRAINTS = Integer.bitCount(7);
        NUM_FLEXIBLE_CONSTRAINTS = Integer.bitCount(FLEXIBLE_CONSTRAINTS);
    }

    public FlexibilityController(JobSchedulerService jobSchedulerService, PrefetchController prefetchController) {
        super(jobSchedulerService);
        this.mFallbackFlexibilityDeadlineMs = 259200000L;
        this.mRescheduledJobDeadline = 3600000L;
        this.mMaxRescheduledDeadline = 432000000L;
        this.mFlexibilityEnabled = false;
        this.mMinTimeBetweenFlexibilityAlarmsMs = 60000L;
        this.mDeadlineProximityLimitMs = 900000L;
        this.mPrefetchLifeCycleStart = new SparseArrayMap<>();
        PrefetchController.PrefetchChangedListener prefetchChangedListener = new PrefetchController.PrefetchChangedListener() { // from class: com.android.server.job.controllers.FlexibilityController.1
            @Override // com.android.server.job.controllers.PrefetchController.PrefetchChangedListener
            public void onPrefetchCacheUpdated(ArraySet<JobStatus> arraySet, int i, String str, long j, long j2, long j3) {
                synchronized (FlexibilityController.this.mLock) {
                    long launchTimeThresholdMs = FlexibilityController.this.mPrefetchController.getLaunchTimeThresholdMs();
                    boolean z = true;
                    boolean z2 = j - launchTimeThresholdMs < j3;
                    if (j2 - launchTimeThresholdMs >= j3) {
                        z = false;
                    }
                    if (z != z2) {
                        SparseArrayMap<String, Long> sparseArrayMap = FlexibilityController.this.mPrefetchLifeCycleStart;
                        sparseArrayMap.add(i, str, Long.valueOf(Math.max(j3, ((Long) sparseArrayMap.getOrDefault(i, str, 0L)).longValue())));
                    }
                    for (int i2 = 0; i2 < arraySet.size(); i2++) {
                        JobStatus valueAt = arraySet.valueAt(i2);
                        if (valueAt.hasFlexibilityConstraint()) {
                            FlexibilityController.this.mFlexibilityTracker.resetJobNumDroppedConstraints(valueAt, j3);
                            FlexibilityController.this.mFlexibilityAlarmQueue.scheduleDropNumConstraintsAlarm(valueAt, j3);
                        }
                    }
                }
            }
        };
        this.mPrefetchChangedListener = prefetchChangedListener;
        this.mHandler = new FcHandler(AppSchedulingModuleThread.get().getLooper());
        boolean z = !this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
        this.mDeviceSupportsFlexConstraints = z;
        this.mFlexibilityEnabled = z & this.mFlexibilityEnabled;
        this.mFlexibilityTracker = new FlexibilityTracker(NUM_FLEXIBLE_CONSTRAINTS);
        FcConfig fcConfig = new FcConfig();
        this.mFcConfig = fcConfig;
        this.mFlexibilityAlarmQueue = new FlexibilityAlarmQueue(this.mContext, AppSchedulingModuleThread.get().getLooper());
        this.mPercentToDropConstraints = fcConfig.DEFAULT_PERCENT_TO_DROP_FLEXIBLE_CONSTRAINTS;
        this.mPrefetchController = prefetchController;
        if (this.mFlexibilityEnabled) {
            prefetchController.registerPrefetchChangedListener(prefetchChangedListener);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void maybeStartTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if (jobStatus.hasFlexibilityConstraint()) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            if (!this.mDeviceSupportsFlexConstraints) {
                jobStatus.setFlexibilityConstraintSatisfied(millis, true);
                return;
            }
            jobStatus.setFlexibilityConstraintSatisfied(millis, isFlexibilitySatisfiedLocked(jobStatus));
            this.mFlexibilityTracker.add(jobStatus);
            jobStatus.setTrackingController(128);
            this.mFlexibilityAlarmQueue.scheduleDropNumConstraintsAlarm(jobStatus, millis);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void maybeStopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if (jobStatus.clearTrackingController(128)) {
            this.mFlexibilityAlarmQueue.removeAlarmForKey(jobStatus);
            this.mFlexibilityTracker.remove(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onAppRemovedLocked(String str, int i) {
        this.mPrefetchLifeCycleStart.delete(UserHandle.getUserId(i), str);
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onUserRemovedLocked(int i) {
        this.mPrefetchLifeCycleStart.delete(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isFlexibilitySatisfiedLocked(JobStatus jobStatus) {
        return !this.mFlexibilityEnabled || this.mService.getUidBias(jobStatus.getSourceUid()) == 40 || getNumSatisfiedFlexibleConstraintsLocked(jobStatus) >= jobStatus.getNumRequiredFlexibleConstraints() || this.mService.isCurrentlyRunningLocked(jobStatus);
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    int getNumSatisfiedFlexibleConstraintsLocked(JobStatus jobStatus) {
        return Integer.bitCount(this.mSatisfiedFlexibleConstraints & jobStatus.getPreferredConstraintFlags()) + (jobStatus.getHasAccessToUnmetered() ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setConstraintSatisfied(int i, boolean z, long j) {
        synchronized (this.mLock) {
            if (((this.mSatisfiedFlexibleConstraints & i) != 0) == z) {
                return;
            }
            if (DEBUG) {
                Slog.d(TAG, "setConstraintSatisfied:  constraint: " + i + " state: " + z);
            }
            int i2 = this.mSatisfiedFlexibleConstraints & (~i);
            if (!z) {
                i = 0;
            }
            this.mSatisfiedFlexibleConstraints = i | i2;
            this.mHandler.obtainMessage(0).sendToTarget();
        }
    }

    @VisibleForTesting
    boolean isConstraintSatisfied(int i) {
        return (this.mSatisfiedFlexibleConstraints & i) != 0;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    long getLifeCycleBeginningElapsedLocked(JobStatus jobStatus) {
        if (!jobStatus.getJob().isPrefetch()) {
            return jobStatus.getEarliestRunTime() == 0 ? jobStatus.enqueueTime : jobStatus.getEarliestRunTime();
        }
        long max = Math.max(jobStatus.enqueueTime, jobStatus.getEarliestRunTime());
        long nextEstimatedLaunchTimeLocked = this.mPrefetchController.getNextEstimatedLaunchTimeLocked(jobStatus);
        long longValue = ((Long) this.mPrefetchLifeCycleStart.getOrDefault(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), 0L)).longValue();
        if (nextEstimatedLaunchTimeLocked != Long.MAX_VALUE) {
            longValue = Math.max(longValue, nextEstimatedLaunchTimeLocked - this.mPrefetchController.getLaunchTimeThresholdMs());
        }
        return Math.max(longValue, max);
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    long getLifeCycleEndElapsedLocked(JobStatus jobStatus, long j) {
        if (jobStatus.getJob().isPrefetch()) {
            long nextEstimatedLaunchTimeLocked = this.mPrefetchController.getNextEstimatedLaunchTimeLocked(jobStatus);
            if (jobStatus.getLatestRunTimeElapsed() != Long.MAX_VALUE) {
                return Math.min(nextEstimatedLaunchTimeLocked - this.mConstants.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS, jobStatus.getLatestRunTimeElapsed());
            }
            if (nextEstimatedLaunchTimeLocked != Long.MAX_VALUE) {
                return nextEstimatedLaunchTimeLocked - this.mConstants.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS;
            }
            return Long.MAX_VALUE;
        }
        if (jobStatus.getNumPreviousAttempts() > 1) {
            return j + Math.min(Math.scalb((float) this.mRescheduledJobDeadline, jobStatus.getNumPreviousAttempts() - 2), this.mMaxRescheduledDeadline);
        }
        return jobStatus.getLatestRunTimeElapsed() == Long.MAX_VALUE ? j + this.mFallbackFlexibilityDeadlineMs : jobStatus.getLatestRunTimeElapsed();
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    int getCurPercentOfLifecycleLocked(JobStatus jobStatus, long j) {
        long lifeCycleBeginningElapsedLocked = getLifeCycleBeginningElapsedLocked(jobStatus);
        long lifeCycleEndElapsedLocked = getLifeCycleEndElapsedLocked(jobStatus, lifeCycleBeginningElapsedLocked);
        if (lifeCycleEndElapsedLocked == Long.MAX_VALUE || lifeCycleBeginningElapsedLocked >= j) {
            return 0;
        }
        if (j > lifeCycleEndElapsedLocked || lifeCycleEndElapsedLocked == lifeCycleBeginningElapsedLocked) {
            return 100;
        }
        return (int) (((j - lifeCycleBeginningElapsedLocked) * 100) / (lifeCycleEndElapsedLocked - lifeCycleBeginningElapsedLocked));
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    long getNextConstraintDropTimeElapsedLocked(JobStatus jobStatus) {
        long lifeCycleBeginningElapsedLocked = getLifeCycleBeginningElapsedLocked(jobStatus);
        return getNextConstraintDropTimeElapsedLocked(jobStatus, lifeCycleBeginningElapsedLocked, getLifeCycleEndElapsedLocked(jobStatus, lifeCycleBeginningElapsedLocked));
    }

    @GuardedBy({"mLock"})
    long getNextConstraintDropTimeElapsedLocked(JobStatus jobStatus, long j, long j2) {
        if (j2 != Long.MAX_VALUE) {
            if (jobStatus.getNumDroppedFlexibleConstraints() != this.mPercentToDropConstraints.length) {
                return j + (((j2 - j) * r4[jobStatus.getNumDroppedFlexibleConstraints()]) / 100);
            }
        }
        return Long.MAX_VALUE;
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onUidBiasChangedLocked(int i, int i2, int i3) {
        if (i2 == 40 || i3 == 40) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            ArraySet<JobStatus> jobsBySourceUid = this.mService.getJobStore().getJobsBySourceUid(i);
            boolean z = false;
            for (int i4 = 0; i4 < jobsBySourceUid.size(); i4++) {
                JobStatus valueAt = jobsBySourceUid.valueAt(i4);
                if (valueAt.hasFlexibilityConstraint()) {
                    valueAt.setFlexibilityConstraintSatisfied(millis, isFlexibilitySatisfiedLocked(valueAt));
                    z |= valueAt.getJob().isPrefetch();
                }
            }
            if (z && i2 == 40) {
                int userId = UserHandle.getUserId(i);
                ArraySet<String> packagesForUidLocked = this.mService.getPackagesForUidLocked(i);
                if (packagesForUidLocked == null) {
                    return;
                }
                for (int i5 = 0; i5 < packagesForUidLocked.size(); i5++) {
                    String valueAt2 = packagesForUidLocked.valueAt(i5);
                    SparseArrayMap<String, Long> sparseArrayMap = this.mPrefetchLifeCycleStart;
                    sparseArrayMap.add(userId, valueAt2, Long.valueOf(Math.max(((Long) sparseArrayMap.getOrDefault(userId, valueAt2, 0L)).longValue(), millis)));
                }
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onConstantsUpdatedLocked() {
        if (this.mFcConfig.mShouldReevaluateConstraints) {
            AppSchedulingModuleThread.getHandler().post(new Runnable() { // from class: com.android.server.job.controllers.FlexibilityController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibilityController.this.lambda$onConstantsUpdatedLocked$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$0() {
        ArraySet<JobStatus> arraySet = new ArraySet<>();
        synchronized (this.mLock) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            for (int i = 0; i < this.mFlexibilityTracker.size(); i++) {
                ArraySet<JobStatus> jobsByNumRequiredConstraints = this.mFlexibilityTracker.getJobsByNumRequiredConstraints(i);
                for (int i2 = 0; i2 < jobsByNumRequiredConstraints.size(); i2++) {
                    JobStatus valueAt = jobsByNumRequiredConstraints.valueAt(i2);
                    this.mFlexibilityTracker.resetJobNumDroppedConstraints(valueAt, millis);
                    this.mFlexibilityAlarmQueue.scheduleDropNumConstraintsAlarm(valueAt, millis);
                    if (valueAt.setFlexibilityConstraintSatisfied(millis, isFlexibilitySatisfiedLocked(valueAt))) {
                        arraySet.add(valueAt);
                    }
                }
            }
        }
        if (arraySet.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(arraySet);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void prepareForUpdatedConstantsLocked() {
        this.mFcConfig.mShouldReevaluateConstraints = false;
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void processConstantLocked(DeviceConfig.Properties properties, String str) {
        this.mFcConfig.processConstantLocked(properties, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class FlexibilityTracker {
        final ArrayList<ArraySet<JobStatus>> mTrackedJobs = new ArrayList<>();

        FlexibilityTracker(int i) {
            for (int i2 = 0; i2 <= i; i2++) {
                this.mTrackedJobs.add(new ArraySet<>());
            }
        }

        public ArraySet<JobStatus> getJobsByNumRequiredConstraints(int i) {
            if (i > this.mTrackedJobs.size()) {
                Slog.wtfStack(FlexibilityController.TAG, "Asked for a larger number of constraints than exists.");
                return null;
            }
            return this.mTrackedJobs.get(i);
        }

        public void add(JobStatus jobStatus) {
            if (jobStatus.getNumRequiredFlexibleConstraints() < 0) {
                return;
            }
            this.mTrackedJobs.get(jobStatus.getNumRequiredFlexibleConstraints()).add(jobStatus);
        }

        public void remove(JobStatus jobStatus) {
            this.mTrackedJobs.get(jobStatus.getNumRequiredFlexibleConstraints()).remove(jobStatus);
        }

        public void resetJobNumDroppedConstraints(JobStatus jobStatus, long j) {
            int curPercentOfLifecycleLocked = FlexibilityController.this.getCurPercentOfLifecycleLocked(jobStatus, j);
            int i = FlexibilityController.NUM_SYSTEM_WIDE_FLEXIBLE_CONSTRAINTS + (jobStatus.getPreferUnmetered() ? 1 : 0);
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                if (curPercentOfLifecycleLocked >= FlexibilityController.this.mPercentToDropConstraints[i3]) {
                    i2++;
                }
            }
            adjustJobsRequiredConstraints(jobStatus, jobStatus.getNumDroppedFlexibleConstraints() - i2, j);
        }

        public ArrayList<ArraySet<JobStatus>> getArrayList() {
            return this.mTrackedJobs;
        }

        public boolean adjustJobsRequiredConstraints(JobStatus jobStatus, int i, long j) {
            if (i != 0) {
                remove(jobStatus);
                jobStatus.adjustNumRequiredFlexibleConstraints(i);
                jobStatus.setFlexibilityConstraintSatisfied(j, FlexibilityController.this.isFlexibilitySatisfiedLocked(jobStatus));
                add(jobStatus);
            }
            return jobStatus.getNumRequiredFlexibleConstraints() > 0;
        }

        public int size() {
            return this.mTrackedJobs.size();
        }

        public void dump(IndentingPrintWriter indentingPrintWriter, Predicate<JobStatus> predicate) {
            for (int i = 0; i < this.mTrackedJobs.size(); i++) {
                ArraySet<JobStatus> arraySet = this.mTrackedJobs.get(i);
                for (int i2 = 0; i2 < arraySet.size(); i2++) {
                    JobStatus valueAt = arraySet.valueAt(i2);
                    if (predicate.test(valueAt)) {
                        indentingPrintWriter.print("#");
                        valueAt.printUniqueId(indentingPrintWriter);
                        indentingPrintWriter.print(" from ");
                        UserHandle.formatUid(indentingPrintWriter, valueAt.getSourceUid());
                        indentingPrintWriter.print(" Num Required Constraints: ");
                        indentingPrintWriter.print(valueAt.getNumRequiredFlexibleConstraints());
                        indentingPrintWriter.println();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class FlexibilityAlarmQueue extends AlarmQueue<JobStatus> {
        private FlexibilityAlarmQueue(Context context, Looper looper) {
            super(context, looper, "*job.flexibility_check*", "Flexible Constraint Check", true, FlexibilityController.this.mMinTimeBetweenFlexibilityAlarmsMs);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(JobStatus jobStatus, int i) {
            return jobStatus.getSourceUserId() == i;
        }

        public void scheduleDropNumConstraintsAlarm(JobStatus jobStatus, long j) {
            synchronized (FlexibilityController.this.mLock) {
                long lifeCycleBeginningElapsedLocked = FlexibilityController.this.getLifeCycleBeginningElapsedLocked(jobStatus);
                long lifeCycleEndElapsedLocked = FlexibilityController.this.getLifeCycleEndElapsedLocked(jobStatus, lifeCycleBeginningElapsedLocked);
                long nextConstraintDropTimeElapsedLocked = FlexibilityController.this.getNextConstraintDropTimeElapsedLocked(jobStatus, lifeCycleBeginningElapsedLocked, lifeCycleEndElapsedLocked);
                if (FlexibilityController.DEBUG) {
                    Slog.d(FlexibilityController.TAG, "scheduleDropNumConstraintsAlarm: " + jobStatus.getSourcePackageName() + " " + jobStatus.getSourceUserId() + " numRequired: " + jobStatus.getNumRequiredFlexibleConstraints() + " numSatisfied: " + Integer.bitCount(FlexibilityController.this.mSatisfiedFlexibleConstraints) + " curTime: " + j + " earliest: " + lifeCycleBeginningElapsedLocked + " latest: " + lifeCycleEndElapsedLocked + " nextTime: " + nextConstraintDropTimeElapsedLocked);
                }
                if (lifeCycleEndElapsedLocked - j < FlexibilityController.this.mDeadlineProximityLimitMs) {
                    if (FlexibilityController.DEBUG) {
                        Slog.d(FlexibilityController.TAG, "deadline proximity met: " + jobStatus);
                    }
                    FlexibilityController.this.mFlexibilityTracker.adjustJobsRequiredConstraints(jobStatus, -jobStatus.getNumRequiredFlexibleConstraints(), j);
                    return;
                }
                if (nextConstraintDropTimeElapsedLocked == Long.MAX_VALUE) {
                    removeAlarmForKey(jobStatus);
                    return;
                }
                if (lifeCycleEndElapsedLocked - nextConstraintDropTimeElapsedLocked <= FlexibilityController.this.mDeadlineProximityLimitMs) {
                    if (FlexibilityController.DEBUG) {
                        Slog.d(FlexibilityController.TAG, "last alarm set: " + jobStatus);
                    }
                    addAlarm(jobStatus, lifeCycleEndElapsedLocked - FlexibilityController.this.mDeadlineProximityLimitMs);
                    return;
                }
                addAlarm(jobStatus, nextConstraintDropTimeElapsedLocked);
            }
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(ArraySet<JobStatus> arraySet) {
            synchronized (FlexibilityController.this.mLock) {
                ArraySet<JobStatus> arraySet2 = new ArraySet<>();
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                for (int i = 0; i < arraySet.size(); i++) {
                    JobStatus valueAt = arraySet.valueAt(i);
                    boolean isConstraintSatisfied = valueAt.isConstraintSatisfied(DumpState.DUMP_COMPILER_STATS);
                    if (FlexibilityController.this.mFlexibilityTracker.adjustJobsRequiredConstraints(valueAt, -1, millis)) {
                        scheduleDropNumConstraintsAlarm(valueAt, millis);
                    }
                    if (isConstraintSatisfied != valueAt.isConstraintSatisfied(DumpState.DUMP_COMPILER_STATS)) {
                        arraySet2.add(valueAt);
                    }
                }
                FlexibilityController.this.mStateChangedListener.onControllerStateChanged(arraySet2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class FcHandler extends Handler {
        FcHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            removeMessages(0);
            synchronized (FlexibilityController.this.mLock) {
                long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
                ArraySet<JobStatus> arraySet = new ArraySet<>();
                for (int i = 0; i <= FlexibilityController.NUM_OPTIONAL_FLEXIBLE_CONSTRAINTS; i++) {
                    ArraySet<JobStatus> jobsByNumRequiredConstraints = FlexibilityController.this.mFlexibilityTracker.getJobsByNumRequiredConstraints(i);
                    if (jobsByNumRequiredConstraints != null) {
                        for (int i2 = 0; i2 < jobsByNumRequiredConstraints.size(); i2++) {
                            JobStatus valueAt = jobsByNumRequiredConstraints.valueAt(i2);
                            if (valueAt.setFlexibilityConstraintSatisfied(millis, FlexibilityController.this.isFlexibilitySatisfiedLocked(valueAt))) {
                                arraySet.add(valueAt);
                            }
                        }
                    }
                }
                if (arraySet.size() > 0) {
                    FlexibilityController.this.mStateChangedListener.onControllerStateChanged(arraySet);
                }
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class FcConfig {

        @VisibleForTesting
        static final long DEFAULT_DEADLINE_PROXIMITY_LIMIT_MS = 900000;

        @VisibleForTesting
        static final long DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_MS = 259200000;
        private static final boolean DEFAULT_FLEXIBILITY_ENABLED = false;
        private static final long DEFAULT_MAX_RESCHEDULED_DEADLINE_MS = 432000000;
        private static final long DEFAULT_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = 60000;
        private static final long DEFAULT_RESCHEDULED_JOB_DEADLINE_MS = 3600000;
        private static final String FC_CONFIG_PREFIX = "fc_";
        static final String KEY_DEADLINE_PROXIMITY_LIMIT = "fc_flexibility_deadline_proximity_limit_ms";
        static final String KEY_FALLBACK_FLEXIBILITY_DEADLINE = "fc_fallback_flexibility_deadline_ms";
        static final String KEY_FLEXIBILITY_ENABLED = "fc_enable_flexibility";
        static final String KEY_MAX_RESCHEDULED_DEADLINE_MS = "fc_max_rescheduled_deadline_ms";
        static final String KEY_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = "fc_min_time_between_flexibility_alarms_ms";
        static final String KEY_PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS = "fc_percents_to_drop_num_flexible_constraints";
        static final String KEY_RESCHEDULED_JOB_DEADLINE_MS = "fc_rescheduled_job_deadline_ms";

        @VisibleForTesting
        final int[] DEFAULT_PERCENT_TO_DROP_FLEXIBLE_CONSTRAINTS;
        public int[] PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS;
        private boolean mShouldReevaluateConstraints = false;
        public boolean FLEXIBILITY_ENABLED = false;
        public long DEADLINE_PROXIMITY_LIMIT_MS = DEFAULT_DEADLINE_PROXIMITY_LIMIT_MS;
        public long FALLBACK_FLEXIBILITY_DEADLINE_MS = DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_MS;
        public long MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = DEFAULT_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS;
        public long RESCHEDULED_JOB_DEADLINE_MS = 3600000;
        public long MAX_RESCHEDULED_DEADLINE_MS = DEFAULT_MAX_RESCHEDULED_DEADLINE_MS;

        FcConfig() {
            int[] iArr = {50, 60, 70, 80};
            this.DEFAULT_PERCENT_TO_DROP_FLEXIBLE_CONSTRAINTS = iArr;
            this.PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS = iArr;
        }

        @GuardedBy({"mLock"})
        public void processConstantLocked(DeviceConfig.Properties properties, String str) {
            str.hashCode();
            boolean z = false;
            char c = 65535;
            switch (str.hashCode()) {
                case -2004789501:
                    if (str.equals(KEY_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1573718613:
                    if (str.equals(KEY_MAX_RESCHEDULED_DEADLINE_MS)) {
                        c = 1;
                        break;
                    }
                    break;
                case -540379004:
                    if (str.equals(KEY_RESCHEDULED_JOB_DEADLINE_MS)) {
                        c = 2;
                        break;
                    }
                    break;
                case 174123958:
                    if (str.equals(KEY_PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS)) {
                        c = 3;
                        break;
                    }
                    break;
                case 581236233:
                    if (str.equals(KEY_DEADLINE_PROXIMITY_LIMIT)) {
                        c = 4;
                        break;
                    }
                    break;
                case 806071071:
                    if (str.equals(KEY_FLEXIBILITY_ENABLED)) {
                        c = 5;
                        break;
                    }
                    break;
                case 1906562988:
                    if (str.equals(KEY_FALLBACK_FLEXIBILITY_DEADLINE)) {
                        c = 6;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = properties.getLong(str, DEFAULT_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS);
                    long j = FlexibilityController.this.mMinTimeBetweenFlexibilityAlarmsMs;
                    long j2 = this.MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS;
                    if (j != j2) {
                        FlexibilityController.this.mMinTimeBetweenFlexibilityAlarmsMs = j2;
                        FlexibilityController.this.mFlexibilityAlarmQueue.setMinTimeBetweenAlarmsMs(this.MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS);
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 1:
                    this.MAX_RESCHEDULED_DEADLINE_MS = properties.getLong(str, DEFAULT_MAX_RESCHEDULED_DEADLINE_MS);
                    long j3 = FlexibilityController.this.mMaxRescheduledDeadline;
                    long j4 = this.MAX_RESCHEDULED_DEADLINE_MS;
                    if (j3 != j4) {
                        FlexibilityController.this.mMaxRescheduledDeadline = j4;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 2:
                    this.RESCHEDULED_JOB_DEADLINE_MS = properties.getLong(str, 3600000L);
                    long j5 = FlexibilityController.this.mRescheduledJobDeadline;
                    long j6 = this.RESCHEDULED_JOB_DEADLINE_MS;
                    if (j5 != j6) {
                        FlexibilityController.this.mRescheduledJobDeadline = j6;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 3:
                    int[] parsePercentToDropString = parsePercentToDropString(properties.getString(str, ""));
                    this.PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS = parsePercentToDropString;
                    if (parsePercentToDropString == null || Arrays.equals(FlexibilityController.this.mPercentToDropConstraints, this.PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS)) {
                        return;
                    }
                    FlexibilityController.this.mPercentToDropConstraints = this.PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS;
                    this.mShouldReevaluateConstraints = true;
                    return;
                case 4:
                    this.DEADLINE_PROXIMITY_LIMIT_MS = properties.getLong(str, DEFAULT_DEADLINE_PROXIMITY_LIMIT_MS);
                    long j7 = FlexibilityController.this.mDeadlineProximityLimitMs;
                    long j8 = this.DEADLINE_PROXIMITY_LIMIT_MS;
                    if (j7 != j8) {
                        FlexibilityController.this.mDeadlineProximityLimitMs = j8;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                case 5:
                    if (properties.getBoolean(str, false) && FlexibilityController.this.mDeviceSupportsFlexConstraints) {
                        z = true;
                    }
                    this.FLEXIBILITY_ENABLED = z;
                    FlexibilityController flexibilityController = FlexibilityController.this;
                    if (flexibilityController.mFlexibilityEnabled != z) {
                        flexibilityController.mFlexibilityEnabled = z;
                        this.mShouldReevaluateConstraints = true;
                        if (z) {
                            flexibilityController.mPrefetchController.registerPrefetchChangedListener(flexibilityController.mPrefetchChangedListener);
                            return;
                        } else {
                            flexibilityController.mPrefetchController.unRegisterPrefetchChangedListener(flexibilityController.mPrefetchChangedListener);
                            return;
                        }
                    }
                    return;
                case 6:
                    this.FALLBACK_FLEXIBILITY_DEADLINE_MS = properties.getLong(str, DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_MS);
                    long j9 = FlexibilityController.this.mFallbackFlexibilityDeadlineMs;
                    long j10 = this.FALLBACK_FLEXIBILITY_DEADLINE_MS;
                    if (j9 != j10) {
                        FlexibilityController.this.mFallbackFlexibilityDeadlineMs = j10;
                        this.mShouldReevaluateConstraints = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private int[] parsePercentToDropString(String str) {
            String[] split = str.split(",");
            int i = FlexibilityController.NUM_FLEXIBLE_CONSTRAINTS;
            int[] iArr = new int[i];
            if (i != split.length) {
                return this.DEFAULT_PERCENT_TO_DROP_FLEXIBLE_CONSTRAINTS;
            }
            int i2 = 0;
            int i3 = 0;
            while (i2 < split.length) {
                try {
                    int parseInt = Integer.parseInt(split[i2]);
                    iArr[i2] = parseInt;
                    if (parseInt < i3) {
                        Slog.wtf(FlexibilityController.TAG, "Percents to drop constraints were not in increasing order.");
                        return this.DEFAULT_PERCENT_TO_DROP_FLEXIBLE_CONSTRAINTS;
                    }
                    i2++;
                    i3 = parseInt;
                } catch (NumberFormatException e) {
                    Slog.e(FlexibilityController.TAG, "Provided string was improperly formatted.", e);
                    return this.DEFAULT_PERCENT_TO_DROP_FLEXIBLE_CONSTRAINTS;
                }
            }
            return iArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println();
            indentingPrintWriter.print(FlexibilityController.class.getSimpleName());
            indentingPrintWriter.println(":");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_FLEXIBILITY_ENABLED, Boolean.valueOf(this.FLEXIBILITY_ENABLED)).println();
            indentingPrintWriter.print(KEY_DEADLINE_PROXIMITY_LIMIT, Long.valueOf(this.DEADLINE_PROXIMITY_LIMIT_MS)).println();
            indentingPrintWriter.print(KEY_FALLBACK_FLEXIBILITY_DEADLINE, Long.valueOf(this.FALLBACK_FLEXIBILITY_DEADLINE_MS)).println();
            indentingPrintWriter.print(KEY_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS, Long.valueOf(this.MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS)).println();
            indentingPrintWriter.print(KEY_PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS, this.PERCENTS_TO_DROP_NUM_FLEXIBLE_CONSTRAINTS).println();
            indentingPrintWriter.print(KEY_RESCHEDULED_JOB_DEADLINE_MS, Long.valueOf(this.RESCHEDULED_JOB_DEADLINE_MS)).println();
            indentingPrintWriter.print(KEY_MAX_RESCHEDULED_DEADLINE_MS, Long.valueOf(this.MAX_RESCHEDULED_DEADLINE_MS)).println();
            indentingPrintWriter.decreaseIndent();
        }
    }

    @VisibleForTesting
    FcConfig getFcConfig() {
        return this.mFcConfig;
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void dumpConstants(IndentingPrintWriter indentingPrintWriter) {
        this.mFcConfig.dump(indentingPrintWriter);
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void dumpControllerStateLocked(IndentingPrintWriter indentingPrintWriter, Predicate<JobStatus> predicate) {
        indentingPrintWriter.println("# Constraints Satisfied: " + Integer.bitCount(this.mSatisfiedFlexibleConstraints));
        indentingPrintWriter.print("Satisfied Flexible Constraints: ");
        JobStatus.dumpConstraints(indentingPrintWriter, this.mSatisfiedFlexibleConstraints);
        indentingPrintWriter.println();
        indentingPrintWriter.println();
        this.mFlexibilityTracker.dump(indentingPrintWriter, predicate);
        indentingPrintWriter.println();
        this.mFlexibilityAlarmQueue.dump(indentingPrintWriter);
    }
}
