package com.android.server.job.controllers;

import android.app.usage.UsageStatsManagerInternal;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.pm.UserPackage;
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
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.LocalServices;
import com.android.server.job.JobSchedulerService;
import com.android.server.usage.AppStandbyController;
import com.android.server.utils.AlarmQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PrefetchController extends StateController {
    private static final boolean DEBUG;
    private static final int MSG_PROCESS_TOP_STATE_CHANGE = 2;
    private static final int MSG_PROCESS_UPDATED_ESTIMATED_LAUNCH_TIME = 1;
    private static final int MSG_RETRIEVE_ESTIMATED_LAUNCH_TIME = 0;
    private static final String TAG = "JobScheduler.Prefetch";
    private AppWidgetManager mAppWidgetManager;
    private final UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener mEstimatedLaunchTimeChangedListener;

    @GuardedBy({"mLock"})
    private final SparseArrayMap<String, Long> mEstimatedLaunchTimes;
    private final PcHandler mHandler;

    @GuardedBy({"mLock"})
    private long mLaunchTimeAllowanceMs;

    @GuardedBy({"mLock"})
    private long mLaunchTimeThresholdMs;
    private final PcConstants mPcConstants;

    @GuardedBy({"mLock"})
    private final ArraySet<PrefetchChangedListener> mPrefetchChangedListeners;
    private final ThresholdAlarmListener mThresholdAlarmListener;

    @GuardedBy({"mLock"})
    private final SparseArrayMap<String, ArraySet<JobStatus>> mTrackedJobs;
    private final UsageStatsManagerInternal mUsageStatsManagerInternal;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PrefetchChangedListener {
        void onPrefetchCacheUpdated(ArraySet<JobStatus> arraySet, int i, String str, long j, long j2, long j3);
    }

    static {
        DEBUG = JobSchedulerService.DEBUG || Log.isLoggable(TAG, 3);
    }

    public PrefetchController(JobSchedulerService jobSchedulerService) {
        super(jobSchedulerService);
        this.mTrackedJobs = new SparseArrayMap<>();
        this.mEstimatedLaunchTimes = new SparseArrayMap<>();
        this.mPrefetchChangedListeners = new ArraySet<>();
        this.mLaunchTimeThresholdMs = 25200000L;
        this.mLaunchTimeAllowanceMs = 1200000L;
        UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener = new UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener() { // from class: com.android.server.job.controllers.PrefetchController.1
            public void onEstimatedLaunchTimeChanged(int i, String str, long j) {
                SomeArgs obtain = SomeArgs.obtain();
                obtain.arg1 = str;
                obtain.argi1 = i;
                obtain.argl1 = j;
                PrefetchController.this.mHandler.obtainMessage(1, obtain).sendToTarget();
            }
        };
        this.mEstimatedLaunchTimeChangedListener = estimatedLaunchTimeChangedListener;
        this.mPcConstants = new PcConstants();
        this.mHandler = new PcHandler(AppSchedulingModuleThread.get().getLooper());
        this.mThresholdAlarmListener = new ThresholdAlarmListener(this.mContext, AppSchedulingModuleThread.get().getLooper());
        UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        this.mUsageStatsManagerInternal = usageStatsManagerInternal;
        usageStatsManagerInternal.registerLaunchTimeChangedListener(estimatedLaunchTimeChangedListener);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onSystemServicesReady() {
        this.mAppWidgetManager = (AppWidgetManager) this.mContext.getSystemService(AppWidgetManager.class);
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void maybeStartTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if (jobStatus.getJob().isPrefetch()) {
            int sourceUserId = jobStatus.getSourceUserId();
            String sourcePackageName = jobStatus.getSourcePackageName();
            ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(sourceUserId, sourcePackageName);
            if (arraySet == null) {
                arraySet = new ArraySet();
                this.mTrackedJobs.add(sourceUserId, sourcePackageName, arraySet);
            }
            long millis = JobSchedulerService.sSystemClock.millis();
            long millis2 = JobSchedulerService.sElapsedRealtimeClock.millis();
            if (arraySet.add(jobStatus) && arraySet.size() == 1 && !willBeLaunchedSoonLocked(sourceUserId, sourcePackageName, millis)) {
                updateThresholdAlarmLocked(sourceUserId, sourcePackageName, millis, millis2);
            }
            updateConstraintLocked(jobStatus, millis, millis2);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void maybeStopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        int sourceUserId = jobStatus.getSourceUserId();
        String sourcePackageName = jobStatus.getSourcePackageName();
        ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(sourceUserId, sourcePackageName);
        if (arraySet != null && arraySet.remove(jobStatus) && arraySet.size() == 0) {
            this.mThresholdAlarmListener.removeAlarmForKey(UserPackage.of(sourceUserId, sourcePackageName));
        }
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onAppRemovedLocked(String str, int i) {
        if (str == null) {
            Slog.wtf(TAG, "Told app removed but given null package name.");
            return;
        }
        int userId = UserHandle.getUserId(i);
        this.mTrackedJobs.delete(userId, str);
        this.mEstimatedLaunchTimes.delete(userId, str);
        this.mThresholdAlarmListener.removeAlarmForKey(UserPackage.of(userId, str));
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onUserRemovedLocked(int i) {
        this.mTrackedJobs.delete(i);
        this.mEstimatedLaunchTimes.delete(i);
        this.mThresholdAlarmListener.removeAlarmsForUserId(i);
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onUidBiasChangedLocked(int i, int i2, int i3) {
        if ((i3 == 40) != (i2 == 40)) {
            this.mHandler.obtainMessage(2, i, 0).sendToTarget();
        }
    }

    @GuardedBy({"mLock"})
    public long getNextEstimatedLaunchTimeLocked(JobStatus jobStatus) {
        return getNextEstimatedLaunchTimeLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), JobSchedulerService.sSystemClock.millis());
    }

    @GuardedBy({"mLock"})
    private long getNextEstimatedLaunchTimeLocked(int i, String str, long j) {
        Long l = (Long) this.mEstimatedLaunchTimes.get(i, str);
        if (l == null || l.longValue() < j - this.mLaunchTimeAllowanceMs) {
            this.mHandler.obtainMessage(0, i, 0, str).sendToTarget();
            this.mEstimatedLaunchTimes.add(i, str, Long.valueOf(JobStatus.NO_LATEST_RUNTIME));
            return JobStatus.NO_LATEST_RUNTIME;
        }
        return l.longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean maybeUpdateConstraintForPkgLocked(long j, long j2, int i, String str) {
        ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(i, str);
        if (arraySet == null) {
            return false;
        }
        boolean z = false;
        for (int i2 = 0; i2 < arraySet.size(); i2++) {
            z |= updateConstraintLocked((JobStatus) arraySet.valueAt(i2), j, j2);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeUpdateConstraintForUid(int i) {
        synchronized (this.mLock) {
            ArraySet<String> packagesForUidLocked = this.mService.getPackagesForUidLocked(i);
            if (packagesForUidLocked == null) {
                return;
            }
            int userId = UserHandle.getUserId(i);
            ArraySet<JobStatus> arraySet = new ArraySet<>();
            long millis = JobSchedulerService.sSystemClock.millis();
            long millis2 = JobSchedulerService.sElapsedRealtimeClock.millis();
            int size = packagesForUidLocked.size() - 1;
            while (size >= 0) {
                ArraySet arraySet2 = (ArraySet) this.mTrackedJobs.get(userId, packagesForUidLocked.valueAt(size));
                if (arraySet2 != null) {
                    int i2 = 0;
                    while (i2 < arraySet2.size()) {
                        JobStatus jobStatus = (JobStatus) arraySet2.valueAt(i2);
                        ArraySet<String> arraySet3 = packagesForUidLocked;
                        ArraySet arraySet4 = arraySet2;
                        int i3 = i2;
                        if (updateConstraintLocked(jobStatus, millis, millis2)) {
                            arraySet.add(jobStatus);
                        }
                        i2 = i3 + 1;
                        packagesForUidLocked = arraySet3;
                        arraySet2 = arraySet4;
                    }
                }
                size--;
                packagesForUidLocked = packagesForUidLocked;
            }
            if (arraySet.size() > 0) {
                this.mStateChangedListener.onControllerStateChanged(arraySet);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processUpdatedEstimatedLaunchTime(int i, String str, long j) {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "Estimated launch time for " + StateController.packageToString(i, str) + " changed to " + j + " (" + TimeUtils.formatDuration(j - JobSchedulerService.sSystemClock.millis()) + " from now)");
        }
        synchronized (this.mLock) {
            ArraySet<JobStatus> arraySet = (ArraySet) this.mTrackedJobs.get(i, str);
            if (arraySet != null) {
                long longValue = ((Long) this.mEstimatedLaunchTimes.get(i, str)).longValue();
                this.mEstimatedLaunchTimes.add(i, str, Long.valueOf(j));
                if (!arraySet.isEmpty()) {
                    long millis = JobSchedulerService.sSystemClock.millis();
                    long millis2 = JobSchedulerService.sElapsedRealtimeClock.millis();
                    updateThresholdAlarmLocked(i, str, millis, millis2);
                    int i2 = 0;
                    while (i2 < this.mPrefetchChangedListeners.size()) {
                        this.mPrefetchChangedListeners.valueAt(i2).onPrefetchCacheUpdated(arraySet, i, str, longValue, j, millis2);
                        i2++;
                        arraySet = arraySet;
                    }
                    ArraySet<JobStatus> arraySet2 = arraySet;
                    if (maybeUpdateConstraintForPkgLocked(millis, millis2, i, str)) {
                        this.mStateChangedListener.onControllerStateChanged(arraySet2);
                    }
                }
            } else if (z) {
                Slog.i(TAG, "Not caching launch time since we haven't seen any prefetch jobs for " + StateController.packageToString(i, str));
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean updateConstraintLocked(JobStatus jobStatus, long j, long j2) {
        AppWidgetManager appWidgetManager;
        boolean z = true;
        if (!(this.mService.getUidBias(jobStatus.getSourceUid()) == 40)) {
            int sourceUserId = jobStatus.getSourceUserId();
            String sourcePackageName = jobStatus.getSourcePackageName();
            if (!willBeLaunchedSoonLocked(sourceUserId, sourcePackageName, j) && ((appWidgetManager = this.mAppWidgetManager) == null || !appWidgetManager.isBoundWidgetPackage(sourcePackageName, sourceUserId))) {
                z = false;
            }
        } else {
            z = this.mService.isCurrentlyRunningLocked(jobStatus);
        }
        return jobStatus.setPrefetchConstraintSatisfied(j2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void updateThresholdAlarmLocked(int i, String str, long j, long j2) {
        ArraySet arraySet = (ArraySet) this.mTrackedJobs.get(i, str);
        if (arraySet == null || arraySet.size() == 0) {
            this.mThresholdAlarmListener.removeAlarmForKey(UserPackage.of(i, str));
            return;
        }
        long nextEstimatedLaunchTimeLocked = getNextEstimatedLaunchTimeLocked(i, str, j);
        if (nextEstimatedLaunchTimeLocked != JobStatus.NO_LATEST_RUNTIME) {
            long j3 = nextEstimatedLaunchTimeLocked - j;
            long j4 = this.mLaunchTimeThresholdMs;
            if (j3 > j4) {
                this.mThresholdAlarmListener.addAlarm(UserPackage.of(i, str), j2 + (nextEstimatedLaunchTimeLocked - (j + j4)));
                return;
            }
        }
        this.mThresholdAlarmListener.removeAlarmForKey(UserPackage.of(i, str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean willBeLaunchedSoonLocked(int i, String str, long j) {
        return getNextEstimatedLaunchTimeLocked(i, str, j) <= (j + this.mLaunchTimeThresholdMs) - this.mLaunchTimeAllowanceMs;
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void prepareForUpdatedConstantsLocked() {
        this.mPcConstants.mShouldReevaluateConstraints = false;
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void processConstantLocked(DeviceConfig.Properties properties, String str) {
        this.mPcConstants.processConstantLocked(properties, str);
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void onConstantsUpdatedLocked() {
        if (this.mPcConstants.mShouldReevaluateConstraints) {
            AppSchedulingModuleThread.getHandler().post(new Runnable() { // from class: com.android.server.job.controllers.PrefetchController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PrefetchController.this.lambda$onConstantsUpdatedLocked$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$0() {
        ArraySet<JobStatus> arraySet = new ArraySet<>();
        synchronized (this.mLock) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            long millis2 = JobSchedulerService.sSystemClock.millis();
            for (int i = 0; i < this.mTrackedJobs.numMaps(); i++) {
                int keyAt = this.mTrackedJobs.keyAt(i);
                int i2 = 0;
                while (i2 < this.mTrackedJobs.numElementsForKey(keyAt)) {
                    String str = (String) this.mTrackedJobs.keyAt(i, i2);
                    int i3 = i2;
                    long j = millis;
                    int i4 = keyAt;
                    if (maybeUpdateConstraintForPkgLocked(millis2, millis, keyAt, str)) {
                        arraySet.addAll((ArraySet<? extends JobStatus>) this.mTrackedJobs.valueAt(i, i3));
                    }
                    if (!willBeLaunchedSoonLocked(i4, str, millis2)) {
                        updateThresholdAlarmLocked(i4, str, millis2, j);
                    }
                    i2 = i3 + 1;
                    keyAt = i4;
                    millis = j;
                }
            }
        }
        if (arraySet.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(arraySet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ThresholdAlarmListener extends AlarmQueue<UserPackage> {
        private ThresholdAlarmListener(Context context, Looper looper) {
            super(context, looper, "*job.prefetch*", "Prefetch threshold", false, 2520000L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(UserPackage userPackage, int i) {
            return userPackage.userId == i;
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(ArraySet<UserPackage> arraySet) {
            long j;
            ArraySet<JobStatus> arraySet2 = new ArraySet<>();
            synchronized (PrefetchController.this.mLock) {
                long millis = JobSchedulerService.sSystemClock.millis();
                long millis2 = JobSchedulerService.sElapsedRealtimeClock.millis();
                int i = 0;
                while (i < arraySet.size()) {
                    UserPackage valueAt = arraySet.valueAt(i);
                    if (!PrefetchController.this.willBeLaunchedSoonLocked(valueAt.userId, valueAt.packageName, millis)) {
                        Slog.e(PrefetchController.TAG, "Alarm expired for " + StateController.packageToString(valueAt.userId, valueAt.packageName) + " at the wrong time");
                        PrefetchController.this.updateThresholdAlarmLocked(valueAt.userId, valueAt.packageName, millis, millis2);
                        j = millis;
                    } else {
                        j = millis;
                        if (PrefetchController.this.maybeUpdateConstraintForPkgLocked(millis, millis2, valueAt.userId, valueAt.packageName)) {
                            arraySet2.addAll((ArraySet<? extends JobStatus>) PrefetchController.this.mTrackedJobs.get(valueAt.userId, valueAt.packageName));
                        }
                    }
                    i++;
                    millis = j;
                }
            }
            if (arraySet2.size() > 0) {
                PrefetchController.this.mStateChangedListener.onControllerStateChanged(arraySet2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerPrefetchChangedListener(PrefetchChangedListener prefetchChangedListener) {
        synchronized (this.mLock) {
            this.mPrefetchChangedListeners.add(prefetchChangedListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unRegisterPrefetchChangedListener(PrefetchChangedListener prefetchChangedListener) {
        synchronized (this.mLock) {
            this.mPrefetchChangedListeners.remove(prefetchChangedListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class PcHandler extends Handler {
        PcHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i == 1) {
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    PrefetchController.this.processUpdatedEstimatedLaunchTime(someArgs.argi1, (String) someArgs.arg1, someArgs.argl1);
                    someArgs.recycle();
                    return;
                } else {
                    if (i != 2) {
                        return;
                    }
                    PrefetchController.this.maybeUpdateConstraintForUid(message.arg1);
                    return;
                }
            }
            int i2 = message.arg1;
            String str = (String) message.obj;
            long estimatedPackageLaunchTime = PrefetchController.this.mUsageStatsManagerInternal.getEstimatedPackageLaunchTime(str, i2);
            if (PrefetchController.DEBUG) {
                Slog.d(PrefetchController.TAG, "Retrieved launch time for " + StateController.packageToString(i2, str) + " of " + estimatedPackageLaunchTime + " (" + TimeUtils.formatDuration(estimatedPackageLaunchTime - JobSchedulerService.sSystemClock.millis()) + " from now)");
            }
            synchronized (PrefetchController.this.mLock) {
                Long l = (Long) PrefetchController.this.mEstimatedLaunchTimes.get(i2, str);
                if (l == null || estimatedPackageLaunchTime != l.longValue()) {
                    PrefetchController.this.processUpdatedEstimatedLaunchTime(i2, str, estimatedPackageLaunchTime);
                }
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class PcConstants {
        private static final long DEFAULT_LAUNCH_TIME_ALLOWANCE_MS = 1200000;
        private static final long DEFAULT_LAUNCH_TIME_THRESHOLD_MS = 25200000;

        @VisibleForTesting
        static final String KEY_LAUNCH_TIME_ALLOWANCE_MS = "pc_launch_time_allowance_ms";

        @VisibleForTesting
        static final String KEY_LAUNCH_TIME_THRESHOLD_MS = "pc_launch_time_threshold_ms";
        private static final String PC_CONSTANT_PREFIX = "pc_";
        private boolean mShouldReevaluateConstraints = false;
        public long LAUNCH_TIME_THRESHOLD_MS = DEFAULT_LAUNCH_TIME_THRESHOLD_MS;
        public long LAUNCH_TIME_ALLOWANCE_MS = DEFAULT_LAUNCH_TIME_ALLOWANCE_MS;

        PcConstants() {
        }

        @GuardedBy({"mLock"})
        public void processConstantLocked(DeviceConfig.Properties properties, String str) {
            str.hashCode();
            if (str.equals(KEY_LAUNCH_TIME_ALLOWANCE_MS)) {
                long j = properties.getLong(str, DEFAULT_LAUNCH_TIME_ALLOWANCE_MS);
                this.LAUNCH_TIME_ALLOWANCE_MS = j;
                long min = Math.min(AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT, Math.max(0L, j));
                if (PrefetchController.this.mLaunchTimeAllowanceMs != min) {
                    PrefetchController.this.mLaunchTimeAllowanceMs = min;
                    this.mShouldReevaluateConstraints = true;
                    return;
                }
                return;
            }
            if (str.equals(KEY_LAUNCH_TIME_THRESHOLD_MS)) {
                long j2 = properties.getLong(str, DEFAULT_LAUNCH_TIME_THRESHOLD_MS);
                this.LAUNCH_TIME_THRESHOLD_MS = j2;
                long min2 = Math.min(86400000L, Math.max(3600000L, j2));
                if (PrefetchController.this.mLaunchTimeThresholdMs != min2) {
                    PrefetchController.this.mLaunchTimeThresholdMs = min2;
                    this.mShouldReevaluateConstraints = true;
                    PrefetchController.this.mThresholdAlarmListener.setMinTimeBetweenAlarmsMs(PrefetchController.this.mLaunchTimeThresholdMs / 10);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println();
            indentingPrintWriter.print(PrefetchController.class.getSimpleName());
            indentingPrintWriter.println(":");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_LAUNCH_TIME_THRESHOLD_MS, Long.valueOf(this.LAUNCH_TIME_THRESHOLD_MS)).println();
            indentingPrintWriter.print(KEY_LAUNCH_TIME_ALLOWANCE_MS, Long.valueOf(this.LAUNCH_TIME_ALLOWANCE_MS)).println();
            indentingPrintWriter.decreaseIndent();
        }
    }

    @VisibleForTesting
    long getLaunchTimeAllowanceMs() {
        return this.mLaunchTimeAllowanceMs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public long getLaunchTimeThresholdMs() {
        return this.mLaunchTimeThresholdMs;
    }

    @VisibleForTesting
    PcConstants getPcConstants() {
        return this.mPcConstants;
    }

    @Override // com.android.server.job.controllers.StateController
    @GuardedBy({"mLock"})
    public void dumpControllerStateLocked(final IndentingPrintWriter indentingPrintWriter, final Predicate<JobStatus> predicate) {
        long millis = JobSchedulerService.sSystemClock.millis();
        indentingPrintWriter.println("Cached launch times:");
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < this.mEstimatedLaunchTimes.numMaps(); i++) {
            int keyAt = this.mEstimatedLaunchTimes.keyAt(i);
            for (int i2 = 0; i2 < this.mEstimatedLaunchTimes.numElementsForKey(keyAt); i2++) {
                String str = (String) this.mEstimatedLaunchTimes.keyAt(i, i2);
                long longValue = ((Long) this.mEstimatedLaunchTimes.valueAt(i, i2)).longValue();
                indentingPrintWriter.print(StateController.packageToString(keyAt, str));
                indentingPrintWriter.print(": ");
                indentingPrintWriter.print(longValue);
                indentingPrintWriter.print(" (");
                TimeUtils.formatDuration(longValue - millis, indentingPrintWriter, 19);
                indentingPrintWriter.println(" from now)");
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        this.mTrackedJobs.forEach(new Consumer() { // from class: com.android.server.job.controllers.PrefetchController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PrefetchController.lambda$dumpControllerStateLocked$1(predicate, indentingPrintWriter, (ArraySet) obj);
            }
        });
        indentingPrintWriter.println();
        this.mThresholdAlarmListener.dump(indentingPrintWriter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpControllerStateLocked$1(Predicate predicate, IndentingPrintWriter indentingPrintWriter, ArraySet arraySet) {
        for (int i = 0; i < arraySet.size(); i++) {
            JobStatus jobStatus = (JobStatus) arraySet.valueAt(i);
            if (predicate.test(jobStatus)) {
                indentingPrintWriter.print("#");
                jobStatus.printUniqueId(indentingPrintWriter);
                indentingPrintWriter.print(" from ");
                UserHandle.formatUid(indentingPrintWriter, jobStatus.getSourceUid());
                indentingPrintWriter.println();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(IndentingPrintWriter indentingPrintWriter) {
        this.mPcConstants.dump(indentingPrintWriter);
    }
}
