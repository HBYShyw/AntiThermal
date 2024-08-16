package com.android.server.job;

import android.annotation.EnforcePermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.app.IUidObserver;
import android.app.UidObserver;
import android.app.compat.CompatChanges;
import android.app.job.IJobScheduler;
import android.app.job.IUserVisibleJobObserver;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobSnapshot;
import android.app.job.JobWorkItem;
import android.app.job.UserVisibleJobSummary;
import android.app.usage.UsageStatsManagerInternal;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.PermissionChecker;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.net.Network;
import android.net.Uri;
import android.os.BatteryManagerInternal;
import android.os.BatteryStatsInternal;
import android.os.Binder;
import android.os.Handler;
import android.os.LimitExceededException;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.os.storage.StorageManagerInternal;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseArrayMap;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseSetArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.modules.expresslog.Counter;
import com.android.modules.expresslog.Histogram;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.AppStateTracker;
import com.android.server.AppStateTrackerImpl;
import com.android.server.DeviceIdleInternal;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.am.BatteryStatsService;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.job.JobSchedulerInternal;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.controllers.BackgroundJobsController;
import com.android.server.job.controllers.BatteryController;
import com.android.server.job.controllers.ComponentController;
import com.android.server.job.controllers.ConnectivityController;
import com.android.server.job.controllers.ContentObserverController;
import com.android.server.job.controllers.DeviceIdleJobsController;
import com.android.server.job.controllers.FlexibilityController;
import com.android.server.job.controllers.IdleController;
import com.android.server.job.controllers.JobStatus;
import com.android.server.job.controllers.PrefetchController;
import com.android.server.job.controllers.QuotaController;
import com.android.server.job.controllers.RestrictingController;
import com.android.server.job.controllers.StateController;
import com.android.server.job.controllers.StorageController;
import com.android.server.job.controllers.TareController;
import com.android.server.job.controllers.TimeController;
import com.android.server.job.restrictions.JobRestriction;
import com.android.server.job.restrictions.ThermalStatusRestriction;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import com.android.server.pm.verify.domain.DomainVerificationPersistence;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.storage.DeviceStorageMonitorService;
import com.android.server.tare.AlarmManagerEconomicPolicy;
import com.android.server.tare.EconomyManagerInternal;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.usage.AppStandbyInternal;
import com.android.server.utils.quota.Categorizer;
import com.android.server.utils.quota.Category;
import com.android.server.utils.quota.CountQuotaTracker;
import dalvik.annotation.optimization.NeverCompile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Predicate;
import libcore.util.EmptyArray;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class JobSchedulerService extends SystemService implements StateChangedListener, JobCompletedListener {
    public static final int ACTIVE_INDEX = 0;
    public static boolean DEBUG = false;
    public static final boolean DEBUG_STANDBY;
    private static boolean DEBUG_USAGE = false;
    public static final int EXEMPTED_INDEX = 6;
    public static final int FREQUENT_INDEX = 2;
    public static final long MAX_ALLOWED_PERIOD_MS = 31536000000L;
    private static final int MAX_JOBS_PER_APP = 150;
    static final int MSG_CHECK_CHANGED_JOB_LIST = 8;
    static final int MSG_CHECK_INDIVIDUAL_JOB = 0;
    static final int MSG_CHECK_JOB = 1;
    static final int MSG_CHECK_JOB_GREEDY = 3;
    static final int MSG_CHECK_MEDIA_EXEMPTION = 9;
    static final int MSG_INFORM_OBSERVERS_OF_USER_VISIBLE_JOB_CHANGE = 11;
    static final int MSG_INFORM_OBSERVER_OF_ALL_USER_VISIBLE_JOBS = 10;
    static final int MSG_STOP_JOB = 2;
    static final int MSG_UID_ACTIVE = 6;
    static final int MSG_UID_GONE = 5;
    static final int MSG_UID_IDLE = 7;
    static final int MSG_UID_STATE_CHANGED = 4;
    public static final int NEVER_INDEX = 4;
    private static final int NUM_COMPLETED_JOB_HISTORY = 20;
    private static final long PERIODIC_JOB_WINDOW_BUFFER = 1800000;
    private static final String QUOTA_TRACKER_ANR_TAG = "anr";
    private static final Category QUOTA_TRACKER_CATEGORY_ANR;
    private static final Category QUOTA_TRACKER_CATEGORY_DISABLED;
    private static final Category QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED;
    private static final Category QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED;
    private static final Category QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ;
    private static final Category QUOTA_TRACKER_CATEGORY_TIMEOUT_REG;
    private static final Category QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL;
    private static final Category QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ;
    private static final String QUOTA_TRACKER_SCHEDULE_LOGGED = ".schedulePersisted out-of-quota logged";
    private static final String QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG = ".schedulePersisted()";
    private static final String QUOTA_TRACKER_TIMEOUT_EJ_TAG = "timeout-ej";
    private static final String QUOTA_TRACKER_TIMEOUT_REG_TAG = "timeout-reg";
    private static final String QUOTA_TRACKER_TIMEOUT_TOTAL_TAG = "timeout-total";
    private static final String QUOTA_TRACKER_TIMEOUT_UIJ_TAG = "timeout-uij";
    public static final int RARE_INDEX = 3;
    private static final long REQUIRE_NETWORK_CONSTRAINT_FOR_NETWORK_JOB_WORK_ITEMS = 241104082;
    static final long REQUIRE_NETWORK_PERMISSIONS_FOR_CONNECTIVITY_JOBS = 271850009;
    public static final int RESTRICTED_INDEX = 5;
    public static final String TAG = "JobScheduler";
    public static final int WORKING_INDEX = 1;
    public static Clock sElapsedRealtimeClock;
    private static final Histogram sEnqueuedJwiHighWaterMarkLogger;
    private static final Histogram sInitialJobEstimatedNetworkDownloadKBLogger;
    private static final Histogram sInitialJobEstimatedNetworkUploadKBLogger;
    private static final Histogram sInitialJwiEstimatedNetworkDownloadKBLogger;
    private static final Histogram sInitialJwiEstimatedNetworkUploadKBLogger;
    private static final Histogram sJobMinimumChunkKBLogger;
    private static final Histogram sJwiMinimumChunkKBLogger;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static Clock sSystemClock;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static Clock sUptimeMillisClock;
    ActivityManagerInternal mActivityManagerInternal;
    private final AppStandbyInternal mAppStandbyInternal;

    @VisibleForTesting
    AppStateTrackerImpl mAppStateTracker;
    private final SparseBooleanArray mBackingUpUids;

    @GuardedBy({"mLock"})
    private final BatteryStateTracker mBatteryStateTracker;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Consumer<JobStatus> mCancelJobDueToUserRemovalConsumer;

    @GuardedBy({"mLock"})
    private final ArraySet<JobStatus> mChangedJobList;

    @GuardedBy({"mLock"})
    private final SparseArray<String> mCloudMediaProviderPackages;
    final JobConcurrencyManager mConcurrencyManager;
    private final ConnectivityController mConnectivityController;
    final Constants mConstants;
    final ConstantsObserver mConstantsObserver;
    final List<StateController> mControllers;
    final ArrayMap<String, Boolean> mDebuggableApps;
    private final DeviceIdleJobsController mDeviceIdleJobsController;
    final JobHandler mHandler;
    private final Predicate<Integer> mIsUidActivePredicate;
    final JobPackageTracker mJobPackageTracker;
    private final List<JobRestriction> mJobRestrictions;
    private IJobSchedulerServiceExt mJobSchedulerServiceExt;
    private JobSchedulerServiceWrapper mJobSchedulerServiceWrapper;
    final JobSchedulerStub mJobSchedulerStub;
    private final CountDownLatch mJobStoreLoadedLatch;
    private final Runnable mJobTimeUpdater;
    final JobStore mJobs;
    private int mLastCancelledJobIndex;
    private final long[] mLastCancelledJobTimeElapsed;
    private final JobStatus[] mLastCancelledJobs;
    private int mLastCompletedJobIndex;
    private final long[] mLastCompletedJobTimeElapsed;
    private final JobStatus[] mLastCompletedJobs;
    DeviceIdleInternal mLocalDeviceIdleController;
    PackageManagerInternal mLocalPM;
    final Object mLock;
    private final MaybeReadyJobQueueFunctor mMaybeQueueFunctor;
    private final PendingJobQueue mPendingJobQueue;

    @GuardedBy({"mPendingJobReasonCache"})
    private final SparseArrayMap<String, SparseIntArray> mPendingJobReasonCache;

    @GuardedBy({"mPermissionCache"})
    private final SparseArray<SparseArrayMap<String, Boolean>> mPermissionCache;
    private final PrefetchController mPrefetchController;
    private final QuotaController mQuotaController;
    private final CountQuotaTracker mQuotaTracker;
    private final ReadyJobQueueFunctor mReadyQueueFunctor;
    boolean mReadyToRock;
    boolean mReportedActive;
    private final List<RestrictingController> mRestrictiveControllers;
    final StandbyTracker mStandbyTracker;
    int[] mStartedUsers;
    private final StorageController mStorageController;
    private final TareController mTareController;
    private final BroadcastReceiver mTimeSetReceiver;
    final SparseIntArray mUidBiasOverride;

    @GuardedBy({"mLock"})
    private final SparseIntArray mUidCapabilities;
    private final IUidObserver mUidObserver;

    @GuardedBy({"mLock"})
    private final SparseIntArray mUidProcStates;
    private final SparseSetArray<String> mUidToPackageCache;
    final UsageStatsManagerInternal mUsageStats;
    private final RemoteCallbackList<IUserVisibleJobObserver> mUserVisibleJobObservers;

    public static int standbyBucketToBucketIndex(int i) {
        if (i == 50) {
            return 4;
        }
        if (i > 40) {
            return 5;
        }
        if (i > 30) {
            return 3;
        }
        if (i > 20) {
            return 2;
        }
        if (i > 10) {
            return 1;
        }
        return i > 5 ? 0 : 6;
    }

    void reportAppUsage(String str, int i) {
    }

    static {
        boolean isLoggable = Log.isLoggable(TAG, 3);
        DEBUG = isLoggable;
        DEBUG_STANDBY = isLoggable;
        DEBUG_USAGE = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        sSystemClock = Clock.systemUTC();
        sUptimeMillisClock = new MySimpleClock(ZoneOffset.UTC) { // from class: com.android.server.job.JobSchedulerService.1
            @Override // com.android.server.job.JobSchedulerService.MySimpleClock, java.time.Clock, java.time.InstantSource
            public long millis() {
                return SystemClock.uptimeMillis();
            }
        };
        sElapsedRealtimeClock = new MySimpleClock(ZoneOffset.UTC) { // from class: com.android.server.job.JobSchedulerService.2
            @Override // com.android.server.job.JobSchedulerService.MySimpleClock, java.time.Clock, java.time.InstantSource
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED = new Category(QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG);
        QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED = new Category(QUOTA_TRACKER_SCHEDULE_LOGGED);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ = new Category(QUOTA_TRACKER_TIMEOUT_UIJ_TAG);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ = new Category(QUOTA_TRACKER_TIMEOUT_EJ_TAG);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_REG = new Category(QUOTA_TRACKER_TIMEOUT_REG_TAG);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL = new Category(QUOTA_TRACKER_TIMEOUT_TOTAL_TAG);
        QUOTA_TRACKER_CATEGORY_ANR = new Category(QUOTA_TRACKER_ANR_TAG);
        QUOTA_TRACKER_CATEGORY_DISABLED = new Category(ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        sEnqueuedJwiHighWaterMarkLogger = new Histogram("job_scheduler.value_hist_w_uid_enqueued_work_items_high_water_mark", new Histogram.ScaledRangeOptions(25, 0, 5.0f, 1.4f));
        sInitialJobEstimatedNetworkDownloadKBLogger = new Histogram("job_scheduler.value_hist_initial_job_estimated_network_download_kilobytes", new Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sInitialJwiEstimatedNetworkDownloadKBLogger = new Histogram("job_scheduler.value_hist_initial_jwi_estimated_network_download_kilobytes", new Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sInitialJobEstimatedNetworkUploadKBLogger = new Histogram("job_scheduler.value_hist_initial_job_estimated_network_upload_kilobytes", new Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sInitialJwiEstimatedNetworkUploadKBLogger = new Histogram("job_scheduler.value_hist_initial_jwi_estimated_network_upload_kilobytes", new Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sJobMinimumChunkKBLogger = new Histogram("job_scheduler.value_hist_w_uid_job_minimum_chunk_kilobytes", new Histogram.ScaledRangeOptions(25, 0, 5.0f, 1.76f));
        sJwiMinimumChunkKBLogger = new Histogram("job_scheduler.value_hist_w_uid_jwi_minimum_chunk_kilobytes", new Histogram.ScaledRangeOptions(25, 0, 5.0f, 1.76f));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static abstract class MySimpleClock extends Clock {
        private final ZoneId mZoneId;

        @Override // java.time.Clock, java.time.InstantSource
        public abstract long millis();

        MySimpleClock(ZoneId zoneId) {
            this.mZoneId = zoneId;
        }

        @Override // java.time.Clock
        public ZoneId getZone() {
            return this.mZoneId;
        }

        @Override // java.time.Clock, java.time.InstantSource
        public Clock withZone(ZoneId zoneId) {
            return new MySimpleClock(zoneId) { // from class: com.android.server.job.JobSchedulerService.MySimpleClock.1
                @Override // com.android.server.job.JobSchedulerService.MySimpleClock, java.time.Clock, java.time.InstantSource
                public long millis() {
                    return MySimpleClock.this.millis();
                }
            };
        }

        @Override // java.time.Clock, java.time.InstantSource
        public Instant instant() {
            return Instant.ofEpochMilli(millis());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class ConstantsObserver implements DeviceConfig.OnPropertiesChangedListener, EconomyManagerInternal.TareStateChangeListener {
        private ConstantsObserver() {
        }

        public void start() {
            DeviceConfig.addOnPropertiesChangedListener("jobscheduler", AppSchedulingModuleThread.getExecutor(), this);
            EconomyManagerInternal economyManagerInternal = (EconomyManagerInternal) LocalServices.getService(EconomyManagerInternal.class);
            economyManagerInternal.registerTareStateChangeListener(this, 536870912);
            synchronized (JobSchedulerService.this.mLock) {
                JobSchedulerService.this.mConstants.updateTareSettingsLocked(economyManagerInternal.getEnabledMode(536870912));
            }
            onPropertiesChanged(DeviceConfig.getProperties("jobscheduler", new String[0]));
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            char c;
            for (int i = 0; i < JobSchedulerService.this.mControllers.size(); i++) {
                JobSchedulerService.this.mControllers.get(i).prepareForUpdatedConstantsLocked();
            }
            synchronized (JobSchedulerService.this.mLock) {
                boolean z = false;
                boolean z2 = false;
                boolean z3 = false;
                boolean z4 = false;
                for (String str : properties.getKeyset()) {
                    if (str != null) {
                        switch (str.hashCode()) {
                            case -1847335205:
                                if (str.equals("es_u_timeout_total_count")) {
                                    c = '\t';
                                    break;
                                }
                                break;
                            case -1787939498:
                                if (str.equals("aq_schedule_count")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case -1644162308:
                                if (str.equals("enable_api_quotas")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -1470844605:
                                if (str.equals("runtime_min_ej_guarantee_ms")) {
                                    c = 28;
                                    break;
                                }
                                break;
                            case -1313417082:
                                if (str.equals("conn_use_cell_signal_strength")) {
                                    c = 23;
                                    break;
                                }
                                break;
                            case -1272362358:
                                if (str.equals("prefetch_force_batch_relax_threshold_ms")) {
                                    c = 25;
                                    break;
                                }
                                break;
                            case -1215861621:
                                if (str.equals("conn_update_all_jobs_min_interval_ms")) {
                                    c = 24;
                                    break;
                                }
                                break;
                            case -1194025741:
                                if (str.equals("enable_execution_safeguards_udc")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case -1062323940:
                                if (str.equals("aq_schedule_window_ms")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case -941023983:
                                if (str.equals("runtime_min_guarantee_ms")) {
                                    c = 27;
                                    break;
                                }
                                break;
                            case -803030002:
                                if (str.equals("runtime_ui_limit_ms")) {
                                    c = 30;
                                    break;
                                }
                                break;
                            case -722508861:
                                if (str.equals("moderate_use_factor")) {
                                    c = 16;
                                    break;
                                }
                                break;
                            case -687279910:
                                if (str.equals("es_u_anr_count")) {
                                    c = 11;
                                    break;
                                }
                                break;
                            case -544478093:
                                if (str.equals("runtime_min_ui_data_transfer_guarantee_ms")) {
                                    c = ' ';
                                    break;
                                }
                                break;
                            case -492250078:
                                if (str.equals("conn_low_signal_strength_relax_frac")) {
                                    c = 22;
                                    break;
                                }
                                break;
                            case -376691020:
                                if (str.equals("max_num_persisted_job_work_items")) {
                                    c = '#';
                                    break;
                                }
                                break;
                            case -138964320:
                                if (str.equals("es_u_anr_window_ms")) {
                                    c = '\f';
                                    break;
                                }
                                break;
                            case -109453036:
                                if (str.equals("aq_schedule_return_failure")) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case -57293457:
                                if (str.equals("conn_congestion_delay_frac")) {
                                    c = 20;
                                    break;
                                }
                                break;
                            case -45782187:
                                if (str.equals("max_non_active_job_batch_delay_ms")) {
                                    c = 14;
                                    break;
                                }
                                break;
                            case 213091160:
                                if (str.equals("runtime_use_data_estimates_for_limits")) {
                                    c = '\"';
                                    break;
                                }
                                break;
                            case 232730669:
                                if (str.equals("es_u_timeout_uij_count")) {
                                    c = 6;
                                    break;
                                }
                                break;
                            case 263198386:
                                if (str.equals("min_exp_backoff_time_ms")) {
                                    c = 18;
                                    break;
                                }
                                break;
                            case 289418623:
                                if (str.equals("heavy_use_factor")) {
                                    c = 15;
                                    break;
                                }
                                break;
                            case 316308971:
                                if (str.equals("es_u_timeout_reg_count")) {
                                    c = '\b';
                                    break;
                                }
                                break;
                            case 322281628:
                                if (str.equals("es_u_timeout_window_ms")) {
                                    c = '\n';
                                    break;
                                }
                                break;
                            case 408648654:
                                if (str.equals("es_u_timeout_ej_count")) {
                                    c = 7;
                                    break;
                                }
                                break;
                            case 709194164:
                                if (str.equals("min_linear_backoff_time_ms")) {
                                    c = 17;
                                    break;
                                }
                                break;
                            case 811737328:
                                if (str.equals("runtime_cumulative_ui_limit_ms")) {
                                    c = '!';
                                    break;
                                }
                                break;
                            case 1004645316:
                                if (str.equals("min_ready_non_active_jobs_count")) {
                                    c = '\r';
                                    break;
                                }
                                break;
                            case 1185412831:
                                if (str.equals("system_stop_to_failure_ratio")) {
                                    c = 19;
                                    break;
                                }
                                break;
                            case 1185743293:
                                if (str.equals("aq_schedule_throw_exception")) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case 1302735555:
                                if (str.equals("persist_in_split_files")) {
                                    c = '$';
                                    break;
                                }
                                break;
                            case 1396959553:
                                if (str.equals("runtime_min_ui_data_transfer_guarantee_buffer_factor")) {
                                    c = 31;
                                    break;
                                }
                                break;
                            case 1470808280:
                                if (str.equals("runtime_free_quota_max_limit_ms")) {
                                    c = 26;
                                    break;
                                }
                                break;
                            case 1680706484:
                                if (str.equals("runtime_min_ui_guarantee_ms")) {
                                    c = 29;
                                    break;
                                }
                                break;
                            case 1692637170:
                                if (str.equals("conn_prefetch_relax_frac")) {
                                    c = 21;
                                    break;
                                }
                                break;
                        }
                        c = 65535;
                        switch (c) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case '\b':
                            case '\t':
                            case '\n':
                            case 11:
                            case '\f':
                                if (z) {
                                    break;
                                } else {
                                    JobSchedulerService.this.mConstants.updateApiQuotaConstantsLocked();
                                    JobSchedulerService.this.updateQuotaTracker();
                                    z = true;
                                    break;
                                }
                            case '\r':
                            case 14:
                                JobSchedulerService.this.mConstants.updateBatchingConstantsLocked();
                                break;
                            case 15:
                            case 16:
                                JobSchedulerService.this.mConstants.updateUseFactorConstantsLocked();
                                break;
                            case 17:
                            case 18:
                            case 19:
                                JobSchedulerService.this.mConstants.updateBackoffConstantsLocked();
                                break;
                            case 20:
                            case 21:
                            case 22:
                            case PackageManagerService.MIN_INSTALLABLE_TARGET_SDK /* 23 */:
                            case 24:
                                JobSchedulerService.this.mConstants.updateConnectivityConstantsLocked();
                                break;
                            case 25:
                                JobSchedulerService.this.mConstants.updatePrefetchConstantsLocked();
                                break;
                            case 26:
                            case 27:
                            case 28:
                            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_11 /* 30 */:
                            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_12 /* 31 */:
                            case ' ':
                            case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_1 /* 33 */:
                            case '\"':
                                if (z3) {
                                    break;
                                } else {
                                    JobSchedulerService.this.mConstants.updateRuntimeConstantsLocked();
                                    z3 = true;
                                    break;
                                }
                            case '#':
                            case '$':
                                if (z2) {
                                    break;
                                } else {
                                    JobSchedulerService.this.mConstants.updatePersistingConstantsLocked();
                                    JobSchedulerService jobSchedulerService = JobSchedulerService.this;
                                    jobSchedulerService.mJobs.setUseSplitFiles(jobSchedulerService.mConstants.PERSIST_IN_SPLIT_FILES);
                                    z2 = true;
                                    break;
                                }
                            default:
                                if (str.startsWith("concurrency_") && !z4) {
                                    JobSchedulerService.this.mConcurrencyManager.updateConfigLocked();
                                    z4 = true;
                                    break;
                                } else {
                                    for (int i2 = 0; i2 < JobSchedulerService.this.mControllers.size(); i2++) {
                                        JobSchedulerService.this.mControllers.get(i2).processConstantLocked(properties, str);
                                    }
                                    break;
                                }
                        }
                    }
                }
                for (int i3 = 0; i3 < JobSchedulerService.this.mControllers.size(); i3++) {
                    JobSchedulerService.this.mControllers.get(i3).onConstantsUpdatedLocked();
                }
            }
        }

        @Override // com.android.server.tare.EconomyManagerInternal.TareStateChangeListener
        public void onTareEnabledModeChanged(int i) {
            if (JobSchedulerService.this.mConstants.updateTareSettingsLocked(i)) {
                for (int i2 = 0; i2 < JobSchedulerService.this.mControllers.size(); i2++) {
                    JobSchedulerService.this.mControllers.get(i2).onConstantsUpdatedLocked();
                }
                JobSchedulerService.this.onControllerStateChanged(null);
            }
        }
    }

    @VisibleForTesting
    void updateQuotaTracker() {
        CountQuotaTracker countQuotaTracker = this.mQuotaTracker;
        Constants constants = this.mConstants;
        countQuotaTracker.setEnabled(constants.ENABLE_API_QUOTAS || constants.ENABLE_EXECUTION_SAFEGUARDS_UDC);
        CountQuotaTracker countQuotaTracker2 = this.mQuotaTracker;
        Category category = QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED;
        Constants constants2 = this.mConstants;
        countQuotaTracker2.setCountLimit(category, constants2.API_QUOTA_SCHEDULE_COUNT, constants2.API_QUOTA_SCHEDULE_WINDOW_MS);
        CountQuotaTracker countQuotaTracker3 = this.mQuotaTracker;
        Category category2 = QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ;
        Constants constants3 = this.mConstants;
        countQuotaTracker3.setCountLimit(category2, constants3.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, constants3.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        CountQuotaTracker countQuotaTracker4 = this.mQuotaTracker;
        Category category3 = QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ;
        Constants constants4 = this.mConstants;
        countQuotaTracker4.setCountLimit(category3, constants4.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, constants4.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        CountQuotaTracker countQuotaTracker5 = this.mQuotaTracker;
        Category category4 = QUOTA_TRACKER_CATEGORY_TIMEOUT_REG;
        Constants constants5 = this.mConstants;
        countQuotaTracker5.setCountLimit(category4, constants5.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT, constants5.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        CountQuotaTracker countQuotaTracker6 = this.mQuotaTracker;
        Category category5 = QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL;
        Constants constants6 = this.mConstants;
        countQuotaTracker6.setCountLimit(category5, constants6.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT, constants6.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        CountQuotaTracker countQuotaTracker7 = this.mQuotaTracker;
        Category category6 = QUOTA_TRACKER_CATEGORY_ANR;
        Constants constants7 = this.mConstants;
        countQuotaTracker7.setCountLimit(category6, constants7.EXECUTION_SAFEGUARDS_UDC_ANR_COUNT, constants7.EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Constants {
        private static final int DEFAULT_API_QUOTA_SCHEDULE_COUNT = 250;
        private static final boolean DEFAULT_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = false;
        private static final boolean DEFAULT_API_QUOTA_SCHEDULE_THROW_EXCEPTION = true;
        private static final long DEFAULT_API_QUOTA_SCHEDULE_WINDOW_MS = 60000;
        private static final float DEFAULT_CONN_CONGESTION_DELAY_FRAC = 0.5f;
        private static final float DEFAULT_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = 0.5f;
        private static final float DEFAULT_CONN_PREFETCH_RELAX_FRAC = 0.5f;
        private static final long DEFAULT_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = 60000;
        private static final boolean DEFAULT_CONN_USE_CELL_SIGNAL_STRENGTH = true;
        private static final boolean DEFAULT_ENABLE_API_QUOTAS = true;
        private static final boolean DEFAULT_ENABLE_EXECUTION_SAFEGUARDS_UDC = true;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = 3;
        private static final long DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = 21600000;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = 5;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = 3;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = 10;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = 2;
        private static final long DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = 86400000;
        private static final float DEFAULT_HEAVY_USE_FACTOR = 0.9f;
        private static final long DEFAULT_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = 1860000;
        static final int DEFAULT_MAX_NUM_PERSISTED_JOB_WORK_ITEMS = 100000;
        private static final long DEFAULT_MIN_EXP_BACKOFF_TIME_MS = 10000;
        private static final long DEFAULT_MIN_LINEAR_BACKOFF_TIME_MS = 10000;
        private static final int DEFAULT_MIN_READY_NON_ACTIVE_JOBS_COUNT = 5;
        private static final float DEFAULT_MODERATE_USE_FACTOR = 0.5f;
        static final boolean DEFAULT_PERSIST_IN_SPLIT_FILES = true;
        private static final long DEFAULT_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = 3600000;
        public static final long DEFAULT_RUNTIME_CUMULATIVE_UI_LIMIT_MS = 86400000;

        @VisibleForTesting
        public static final long DEFAULT_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = 1800000;

        @VisibleForTesting
        public static final long DEFAULT_RUNTIME_MIN_EJ_GUARANTEE_MS = 180000;

        @VisibleForTesting
        public static final long DEFAULT_RUNTIME_MIN_GUARANTEE_MS = 600000;
        public static final float DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = 1.35f;
        public static final long DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS;
        public static final long DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS;
        public static final long DEFAULT_RUNTIME_UI_LIMIT_MS;
        public static final boolean DEFAULT_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = false;
        private static final int DEFAULT_SYSTEM_STOP_TO_FAILURE_RATIO = 3;
        private static final String KEY_API_QUOTA_SCHEDULE_COUNT = "aq_schedule_count";
        private static final String KEY_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = "aq_schedule_return_failure";
        private static final String KEY_API_QUOTA_SCHEDULE_THROW_EXCEPTION = "aq_schedule_throw_exception";
        private static final String KEY_API_QUOTA_SCHEDULE_WINDOW_MS = "aq_schedule_window_ms";
        private static final String KEY_CONN_CONGESTION_DELAY_FRAC = "conn_congestion_delay_frac";
        private static final String KEY_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = "conn_low_signal_strength_relax_frac";
        private static final String KEY_CONN_PREFETCH_RELAX_FRAC = "conn_prefetch_relax_frac";
        private static final String KEY_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = "conn_update_all_jobs_min_interval_ms";
        private static final String KEY_CONN_USE_CELL_SIGNAL_STRENGTH = "conn_use_cell_signal_strength";
        private static final String KEY_ENABLE_API_QUOTAS = "enable_api_quotas";
        private static final String KEY_ENABLE_EXECUTION_SAFEGUARDS_UDC = "enable_execution_safeguards_udc";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = "es_u_anr_count";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = "es_u_anr_window_ms";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = "es_u_timeout_ej_count";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = "es_u_timeout_reg_count";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = "es_u_timeout_total_count";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = "es_u_timeout_uij_count";
        private static final String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = "es_u_timeout_window_ms";
        private static final String KEY_HEAVY_USE_FACTOR = "heavy_use_factor";
        private static final String KEY_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = "max_non_active_job_batch_delay_ms";
        private static final String KEY_MAX_NUM_PERSISTED_JOB_WORK_ITEMS = "max_num_persisted_job_work_items";
        private static final String KEY_MIN_EXP_BACKOFF_TIME_MS = "min_exp_backoff_time_ms";
        private static final String KEY_MIN_LINEAR_BACKOFF_TIME_MS = "min_linear_backoff_time_ms";
        private static final String KEY_MIN_READY_NON_ACTIVE_JOBS_COUNT = "min_ready_non_active_jobs_count";
        private static final String KEY_MODERATE_USE_FACTOR = "moderate_use_factor";
        private static final String KEY_PERSIST_IN_SPLIT_FILES = "persist_in_split_files";
        private static final String KEY_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = "prefetch_force_batch_relax_threshold_ms";
        private static final String KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS = "runtime_cumulative_ui_limit_ms";
        private static final String KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = "runtime_free_quota_max_limit_ms";
        private static final String KEY_RUNTIME_MIN_EJ_GUARANTEE_MS = "runtime_min_ej_guarantee_ms";
        private static final String KEY_RUNTIME_MIN_GUARANTEE_MS = "runtime_min_guarantee_ms";
        private static final String KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = "runtime_min_ui_data_transfer_guarantee_buffer_factor";
        private static final String KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = "runtime_min_ui_data_transfer_guarantee_ms";
        private static final String KEY_RUNTIME_MIN_UI_GUARANTEE_MS = "runtime_min_ui_guarantee_ms";
        private static final String KEY_RUNTIME_UI_LIMIT_MS = "runtime_ui_limit_ms";
        private static final String KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = "runtime_use_data_estimates_for_limits";
        private static final String KEY_SYSTEM_STOP_TO_FAILURE_RATIO = "system_stop_to_failure_ratio";
        int MIN_READY_NON_ACTIVE_JOBS_COUNT = 5;
        long MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = DEFAULT_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS;
        float HEAVY_USE_FACTOR = DEFAULT_HEAVY_USE_FACTOR;
        float MODERATE_USE_FACTOR = 0.5f;
        long MIN_LINEAR_BACKOFF_TIME_MS = JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY;
        long MIN_EXP_BACKOFF_TIME_MS = JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY;
        int SYSTEM_STOP_TO_FAILURE_RATIO = 3;
        public float CONN_CONGESTION_DELAY_FRAC = 0.5f;
        public float CONN_PREFETCH_RELAX_FRAC = 0.5f;
        public boolean CONN_USE_CELL_SIGNAL_STRENGTH = true;
        public long CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = 60000;
        public float CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = 0.5f;
        public long PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = 3600000;
        public boolean ENABLE_API_QUOTAS = true;
        public int API_QUOTA_SCHEDULE_COUNT = DEFAULT_API_QUOTA_SCHEDULE_COUNT;
        public long API_QUOTA_SCHEDULE_WINDOW_MS = 60000;
        public boolean API_QUOTA_SCHEDULE_THROW_EXCEPTION = true;
        public boolean API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = false;
        public boolean ENABLE_EXECUTION_SAFEGUARDS_UDC = true;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = 2;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = 5;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = 3;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = 10;
        public long EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = 86400000;
        public int EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = 3;
        public long EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS;
        public long RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = 1800000;
        public long RUNTIME_MIN_GUARANTEE_MS = 600000;
        public long RUNTIME_MIN_EJ_GUARANTEE_MS = DEFAULT_RUNTIME_MIN_EJ_GUARANTEE_MS;
        public long RUNTIME_MIN_UI_GUARANTEE_MS = DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS;
        public long RUNTIME_UI_LIMIT_MS = DEFAULT_RUNTIME_UI_LIMIT_MS;
        public float RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = 1.35f;
        public long RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS;
        public long RUNTIME_CUMULATIVE_UI_LIMIT_MS = 86400000;
        public boolean RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = false;
        public boolean PERSIST_IN_SPLIT_FILES = true;
        public int MAX_NUM_PERSISTED_JOB_WORK_ITEMS = 100000;
        public boolean USE_TARE_POLICY = false;

        static {
            long max = Math.max(DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS, 600000L);
            DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS = max;
            DEFAULT_RUNTIME_UI_LIMIT_MS = Math.max(43200000L, 1800000L);
            DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = Math.max(600000L, max);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateBatchingConstantsLocked() {
            this.MIN_READY_NON_ACTIVE_JOBS_COUNT = DeviceConfig.getInt("jobscheduler", KEY_MIN_READY_NON_ACTIVE_JOBS_COUNT, 5);
            this.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = DeviceConfig.getLong("jobscheduler", KEY_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS, DEFAULT_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateUseFactorConstantsLocked() {
            this.HEAVY_USE_FACTOR = DeviceConfig.getFloat("jobscheduler", KEY_HEAVY_USE_FACTOR, DEFAULT_HEAVY_USE_FACTOR);
            this.MODERATE_USE_FACTOR = DeviceConfig.getFloat("jobscheduler", KEY_MODERATE_USE_FACTOR, 0.5f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateBackoffConstantsLocked() {
            this.MIN_LINEAR_BACKOFF_TIME_MS = DeviceConfig.getLong("jobscheduler", KEY_MIN_LINEAR_BACKOFF_TIME_MS, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
            this.MIN_EXP_BACKOFF_TIME_MS = DeviceConfig.getLong("jobscheduler", KEY_MIN_EXP_BACKOFF_TIME_MS, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
            this.SYSTEM_STOP_TO_FAILURE_RATIO = DeviceConfig.getInt("jobscheduler", KEY_SYSTEM_STOP_TO_FAILURE_RATIO, 3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateConnectivityConstantsLocked() {
            this.CONN_CONGESTION_DELAY_FRAC = DeviceConfig.getFloat("jobscheduler", KEY_CONN_CONGESTION_DELAY_FRAC, 0.5f);
            this.CONN_PREFETCH_RELAX_FRAC = DeviceConfig.getFloat("jobscheduler", KEY_CONN_PREFETCH_RELAX_FRAC, 0.5f);
            this.CONN_USE_CELL_SIGNAL_STRENGTH = DeviceConfig.getBoolean("jobscheduler", KEY_CONN_USE_CELL_SIGNAL_STRENGTH, true);
            this.CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = DeviceConfig.getLong("jobscheduler", KEY_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS, 60000L);
            this.CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = DeviceConfig.getFloat("jobscheduler", KEY_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC, 0.5f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePersistingConstantsLocked() {
            this.PERSIST_IN_SPLIT_FILES = DeviceConfig.getBoolean("jobscheduler", KEY_PERSIST_IN_SPLIT_FILES, true);
            this.MAX_NUM_PERSISTED_JOB_WORK_ITEMS = DeviceConfig.getInt("jobscheduler", KEY_MAX_NUM_PERSISTED_JOB_WORK_ITEMS, 100000);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePrefetchConstantsLocked() {
            this.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = DeviceConfig.getLong("jobscheduler", KEY_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS, 3600000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateApiQuotaConstantsLocked() {
            this.ENABLE_API_QUOTAS = DeviceConfig.getBoolean("jobscheduler", KEY_ENABLE_API_QUOTAS, true);
            this.ENABLE_EXECUTION_SAFEGUARDS_UDC = DeviceConfig.getBoolean("jobscheduler", KEY_ENABLE_EXECUTION_SAFEGUARDS_UDC, true);
            this.API_QUOTA_SCHEDULE_COUNT = Math.max(DEFAULT_API_QUOTA_SCHEDULE_COUNT, DeviceConfig.getInt("jobscheduler", KEY_API_QUOTA_SCHEDULE_COUNT, DEFAULT_API_QUOTA_SCHEDULE_COUNT));
            this.API_QUOTA_SCHEDULE_WINDOW_MS = DeviceConfig.getLong("jobscheduler", KEY_API_QUOTA_SCHEDULE_WINDOW_MS, 60000L);
            this.API_QUOTA_SCHEDULE_THROW_EXCEPTION = DeviceConfig.getBoolean("jobscheduler", KEY_API_QUOTA_SCHEDULE_THROW_EXCEPTION, true);
            this.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = DeviceConfig.getBoolean("jobscheduler", KEY_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT, false);
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = Math.max(2, DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, 2));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = Math.max(2, DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, 5));
            int max = Math.max(2, DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT, 3));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = max;
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = Math.max(Math.max(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, Math.max(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, max)), DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT, 10));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = DeviceConfig.getLong("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS, 86400000L);
            this.EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = Math.max(1, DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT, 3));
            this.EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = DeviceConfig.getLong("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS, DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRuntimeConstantsLocked() {
            DeviceConfig.Properties properties = DeviceConfig.getProperties("jobscheduler", new String[]{KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, KEY_RUNTIME_MIN_GUARANTEE_MS, KEY_RUNTIME_MIN_EJ_GUARANTEE_MS, KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, KEY_RUNTIME_MIN_UI_GUARANTEE_MS, KEY_RUNTIME_UI_LIMIT_MS, KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS, KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS, KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS});
            this.RUNTIME_MIN_GUARANTEE_MS = Math.max(600000L, properties.getLong(KEY_RUNTIME_MIN_GUARANTEE_MS, 600000L));
            this.RUNTIME_MIN_EJ_GUARANTEE_MS = Math.max(60000L, properties.getLong(KEY_RUNTIME_MIN_EJ_GUARANTEE_MS, DEFAULT_RUNTIME_MIN_EJ_GUARANTEE_MS));
            this.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = Math.max(this.RUNTIME_MIN_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, 1800000L));
            long max = Math.max(this.RUNTIME_MIN_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_MIN_UI_GUARANTEE_MS, DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS));
            this.RUNTIME_MIN_UI_GUARANTEE_MS = max;
            this.RUNTIME_UI_LIMIT_MS = Math.max(this.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, Math.max(max, properties.getLong(KEY_RUNTIME_UI_LIMIT_MS, DEFAULT_RUNTIME_UI_LIMIT_MS)));
            this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = Math.max(1.0f, properties.getFloat(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, 1.35f));
            this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = Math.max(this.RUNTIME_MIN_UI_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS, DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS));
            this.RUNTIME_CUMULATIVE_UI_LIMIT_MS = Math.max(this.RUNTIME_UI_LIMIT_MS, properties.getLong(KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS, 86400000L));
            this.RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = properties.getBoolean(KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean updateTareSettingsLocked(int i) {
            boolean z = i == 1;
            if (this.USE_TARE_POLICY == z) {
                return false;
            }
            this.USE_TARE_POLICY = z;
            return true;
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("Settings:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_MIN_READY_NON_ACTIVE_JOBS_COUNT, Integer.valueOf(this.MIN_READY_NON_ACTIVE_JOBS_COUNT)).println();
            indentingPrintWriter.print(KEY_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS, Long.valueOf(this.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS)).println();
            indentingPrintWriter.print(KEY_HEAVY_USE_FACTOR, Float.valueOf(this.HEAVY_USE_FACTOR)).println();
            indentingPrintWriter.print(KEY_MODERATE_USE_FACTOR, Float.valueOf(this.MODERATE_USE_FACTOR)).println();
            indentingPrintWriter.print(KEY_MIN_LINEAR_BACKOFF_TIME_MS, Long.valueOf(this.MIN_LINEAR_BACKOFF_TIME_MS)).println();
            indentingPrintWriter.print(KEY_MIN_EXP_BACKOFF_TIME_MS, Long.valueOf(this.MIN_EXP_BACKOFF_TIME_MS)).println();
            indentingPrintWriter.print(KEY_SYSTEM_STOP_TO_FAILURE_RATIO, Integer.valueOf(this.SYSTEM_STOP_TO_FAILURE_RATIO)).println();
            indentingPrintWriter.print(KEY_CONN_CONGESTION_DELAY_FRAC, Float.valueOf(this.CONN_CONGESTION_DELAY_FRAC)).println();
            indentingPrintWriter.print(KEY_CONN_PREFETCH_RELAX_FRAC, Float.valueOf(this.CONN_PREFETCH_RELAX_FRAC)).println();
            indentingPrintWriter.print(KEY_CONN_USE_CELL_SIGNAL_STRENGTH, Boolean.valueOf(this.CONN_USE_CELL_SIGNAL_STRENGTH)).println();
            indentingPrintWriter.print(KEY_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS, Long.valueOf(this.CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS)).println();
            indentingPrintWriter.print(KEY_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC, Float.valueOf(this.CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC)).println();
            indentingPrintWriter.print(KEY_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS, Long.valueOf(this.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS)).println();
            indentingPrintWriter.print(KEY_ENABLE_API_QUOTAS, Boolean.valueOf(this.ENABLE_API_QUOTAS)).println();
            indentingPrintWriter.print(KEY_API_QUOTA_SCHEDULE_COUNT, Integer.valueOf(this.API_QUOTA_SCHEDULE_COUNT)).println();
            indentingPrintWriter.print(KEY_API_QUOTA_SCHEDULE_WINDOW_MS, Long.valueOf(this.API_QUOTA_SCHEDULE_WINDOW_MS)).println();
            indentingPrintWriter.print(KEY_API_QUOTA_SCHEDULE_THROW_EXCEPTION, Boolean.valueOf(this.API_QUOTA_SCHEDULE_THROW_EXCEPTION)).println();
            indentingPrintWriter.print(KEY_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT, Boolean.valueOf(this.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT)).println();
            indentingPrintWriter.print(KEY_ENABLE_EXECUTION_SAFEGUARDS_UDC, Boolean.valueOf(this.ENABLE_EXECUTION_SAFEGUARDS_UDC)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT, Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT, Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS, Long.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT, Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_ANR_COUNT)).println();
            indentingPrintWriter.print(KEY_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS, Long.valueOf(this.EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_MIN_GUARANTEE_MS, Long.valueOf(this.RUNTIME_MIN_GUARANTEE_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_MIN_EJ_GUARANTEE_MS, Long.valueOf(this.RUNTIME_MIN_EJ_GUARANTEE_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, Long.valueOf(this.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_MIN_UI_GUARANTEE_MS, Long.valueOf(this.RUNTIME_MIN_UI_GUARANTEE_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_UI_LIMIT_MS, Long.valueOf(this.RUNTIME_UI_LIMIT_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, Float.valueOf(this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR)).println();
            indentingPrintWriter.print(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS, Long.valueOf(this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS, Long.valueOf(this.RUNTIME_CUMULATIVE_UI_LIMIT_MS)).println();
            indentingPrintWriter.print(KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS, Boolean.valueOf(this.RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS)).println();
            indentingPrintWriter.print(KEY_PERSIST_IN_SPLIT_FILES, Boolean.valueOf(this.PERSIST_IN_SPLIT_FILES)).println();
            indentingPrintWriter.print(KEY_MAX_NUM_PERSISTED_JOB_WORK_ITEMS, Integer.valueOf(this.MAX_NUM_PERSISTED_JOB_WORK_ITEMS)).println();
            indentingPrintWriter.print("enable_tare", Boolean.valueOf(this.USE_TARE_POLICY)).println();
            indentingPrintWriter.decreaseIndent();
        }

        void dump(ProtoOutputStream protoOutputStream) {
            protoOutputStream.write(1120986464285L, this.MIN_READY_NON_ACTIVE_JOBS_COUNT);
            protoOutputStream.write(1112396529694L, this.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS);
            protoOutputStream.write(1103806595080L, this.HEAVY_USE_FACTOR);
            protoOutputStream.write(1103806595081L, this.MODERATE_USE_FACTOR);
            protoOutputStream.write(1112396529681L, this.MIN_LINEAR_BACKOFF_TIME_MS);
            protoOutputStream.write(1112396529682L, this.MIN_EXP_BACKOFF_TIME_MS);
            protoOutputStream.write(1103806595093L, this.CONN_CONGESTION_DELAY_FRAC);
            protoOutputStream.write(1103806595094L, this.CONN_PREFETCH_RELAX_FRAC);
            protoOutputStream.write(1133871366175L, this.ENABLE_API_QUOTAS);
            protoOutputStream.write(1120986464288L, this.API_QUOTA_SCHEDULE_COUNT);
            protoOutputStream.write(1112396529697L, this.API_QUOTA_SCHEDULE_WINDOW_MS);
            protoOutputStream.write(1133871366178L, this.API_QUOTA_SCHEDULE_THROW_EXCEPTION);
            protoOutputStream.write(1133871366179L, this.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPackageName(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            return data.getSchemeSpecificPart();
        }
        return null;
    }

    public Context getTestableContext() {
        return getContext();
    }

    public Object getLock() {
        return this.mLock;
    }

    public JobStore getJobStore() {
        return this.mJobs;
    }

    public Constants getConstants() {
        return this.mConstants;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingJobQueue getPendingJobQueue() {
        return this.mPendingJobQueue;
    }

    public WorkSource deriveWorkSource(int i, String str) {
        if (!WorkSource.isChainedBatteryAttributionEnabled(getContext())) {
            return str == null ? new WorkSource(i) : new WorkSource(i, str);
        }
        WorkSource workSource = new WorkSource();
        workSource.createWorkChain().addNode(i, str).addNode(1000, TAG);
        return workSource;
    }

    @GuardedBy({"mLock"})
    public ArraySet<String> getPackagesForUidLocked(int i) {
        ArraySet<String> arraySet = this.mUidToPackageCache.get(i);
        if (arraySet != null) {
            return arraySet;
        }
        try {
            String[] packagesForUid = AppGlobals.getPackageManager().getPackagesForUid(i);
            if (packagesForUid == null) {
                return arraySet;
            }
            for (String str : packagesForUid) {
                this.mUidToPackageCache.add(i, str);
            }
            return this.mUidToPackageCache.get(i);
        } catch (RemoteException unused) {
            return arraySet;
        }
    }

    public void onUserStarting(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            this.mStartedUsers = ArrayUtils.appendInt(this.mStartedUsers, targetUser.getUserIdentifier());
        }
    }

    public void onUserCompletedEvent(SystemService.TargetUser targetUser, SystemService.UserCompletedEventType userCompletedEventType) {
        if (userCompletedEventType.includesOnUserStarting() || userCompletedEventType.includesOnUserUnlocked()) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    public void onUserStopping(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            this.mStartedUsers = ArrayUtils.removeInt(this.mStartedUsers, targetUser.getUserIdentifier());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUidActive(int i) {
        return this.mAppStateTracker.isUidActiveSynced(i);
    }

    public int scheduleAsPackage(JobInfo jobInfo, JobWorkItem jobWorkItem, int i, String str, int i2, String str2, String str3) {
        String packageName = jobInfo.getService().getPackageName();
        if (jobInfo.isPersisted() && (str == null || str.equals(packageName))) {
            String str4 = str == null ? packageName : str;
            if (!this.mQuotaTracker.isWithinQuota(i2, str4, QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG)) {
                if (this.mQuotaTracker.isWithinQuota(i2, str4, QUOTA_TRACKER_SCHEDULE_LOGGED)) {
                    Slog.wtf(TAG, i2 + "-" + str4 + " has called schedule() too many times");
                    this.mQuotaTracker.noteEvent(i2, str4, QUOTA_TRACKER_SCHEDULE_LOGGED);
                }
                this.mAppStandbyInternal.restrictApp(str4, i2, 4);
                if (this.mConstants.API_QUOTA_SCHEDULE_THROW_EXCEPTION) {
                    synchronized (this.mLock) {
                        if (!this.mDebuggableApps.containsKey(str)) {
                            try {
                                ApplicationInfo applicationInfo = AppGlobals.getPackageManager().getApplicationInfo(str4, 0L, i2);
                                if (applicationInfo == null) {
                                    return 0;
                                }
                                this.mDebuggableApps.put(str, Boolean.valueOf((applicationInfo.flags & 2) != 0));
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        boolean booleanValue = this.mDebuggableApps.get(str).booleanValue();
                        if (booleanValue) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("schedule()/enqueue() called more than ");
                            CountQuotaTracker countQuotaTracker = this.mQuotaTracker;
                            Category category = QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED;
                            sb.append(countQuotaTracker.getLimit(category));
                            sb.append(" times in the past ");
                            sb.append(this.mQuotaTracker.getWindowSizeMs(category));
                            sb.append("ms. See the documentation for more information.");
                            throw new LimitExceededException(sb.toString());
                        }
                    }
                }
                if (this.mConstants.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT) {
                    return 0;
                }
            }
            this.mQuotaTracker.noteEvent(i2, str4, QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG);
        }
        if (this.mActivityManagerInternal.isAppStartModeDisabled(i, packageName)) {
            Slog.w(TAG, "Not scheduling job " + i + ":" + jobInfo.toString() + " -- package not allowed to start");
            Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_app_start_mode_disabled", i);
            return 0;
        }
        this.mJobSchedulerServiceExt.scheduleAsPackage(getContext(), jobInfo, i);
        if (jobInfo.getRequiredNetwork() != null) {
            sInitialJobEstimatedNetworkDownloadKBLogger.logSample(safelyScaleBytesToKBForHistogram(jobInfo.getEstimatedNetworkDownloadBytes()));
            sInitialJobEstimatedNetworkUploadKBLogger.logSample(safelyScaleBytesToKBForHistogram(jobInfo.getEstimatedNetworkUploadBytes()));
            sJobMinimumChunkKBLogger.logSampleWithUid(i, safelyScaleBytesToKBForHistogram(jobInfo.getMinimumNetworkChunkBytes()));
            if (jobWorkItem != null) {
                sInitialJwiEstimatedNetworkDownloadKBLogger.logSample(safelyScaleBytesToKBForHistogram(jobWorkItem.getEstimatedNetworkDownloadBytes()));
                sInitialJwiEstimatedNetworkUploadKBLogger.logSample(safelyScaleBytesToKBForHistogram(jobWorkItem.getEstimatedNetworkUploadBytes()));
                sJwiMinimumChunkKBLogger.logSampleWithUid(i, safelyScaleBytesToKBForHistogram(jobWorkItem.getMinimumNetworkChunkBytes()));
            }
        }
        if (jobWorkItem != null) {
            Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_job_work_items_enqueued", i);
        }
        synchronized (this.mLock) {
            try {
                try {
                    JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(i, str2, jobInfo.getId());
                    if (jobWorkItem != null && jobByUidAndJobId != null && jobByUidAndJobId.getJob().equals(jobInfo)) {
                        if (jobByUidAndJobId.getWorkCount() >= this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS && jobByUidAndJobId.isPersisted()) {
                            Slog.w(TAG, "Too many JWIs for uid " + i);
                            throw new IllegalStateException("Apps may not persist more than " + this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS + " JobWorkItems per job");
                        }
                        jobByUidAndJobId.enqueueWorkLocked(jobWorkItem);
                        if (jobByUidAndJobId.getJob().isUserInitiated()) {
                            jobByUidAndJobId.removeInternalFlags(6);
                        }
                        this.mJobs.touchJob(jobByUidAndJobId);
                        sEnqueuedJwiHighWaterMarkLogger.logSampleWithUid(i, jobByUidAndJobId.getWorkCount());
                        jobByUidAndJobId.maybeAddForegroundExemption(this.mIsUidActivePredicate);
                        return 1;
                    }
                    JobStatus createFromJobInfo = JobStatus.createFromJobInfo(jobInfo, i, str, i2, str2, str3);
                    if (createFromJobInfo.isRequestedExpeditedJob() && ((this.mConstants.USE_TARE_POLICY && !this.mTareController.canScheduleEJ(createFromJobInfo)) || (!this.mConstants.USE_TARE_POLICY && !this.mQuotaController.isWithinEJQuotaLocked(createFromJobInfo)))) {
                        Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_ej_out_of_quota", i);
                        return 0;
                    }
                    createFromJobInfo.maybeAddForegroundExemption(this.mIsUidActivePredicate);
                    if (DEBUG) {
                        Slog.d(TAG, "SCHEDULE: " + createFromJobInfo.toShortString());
                    }
                    if (str == null && this.mJobs.countJobsForUid(i) > MAX_JOBS_PER_APP) {
                        Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_max_scheduling_limit_hit", i);
                        this.mJobSchedulerServiceExt.onHookRedundantJob(getContext(), this.mJobs, i, MAX_JOBS_PER_APP);
                    }
                    createFromJobInfo.prepareLocked();
                    if (jobByUidAndJobId != null) {
                        if (jobWorkItem != null && jobByUidAndJobId.isPersisted() && jobByUidAndJobId.getWorkCount() >= this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS) {
                            Slog.w(TAG, "Too many JWIs for uid " + i);
                            throw new IllegalStateException("Apps may not persist more than " + this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS + " JobWorkItems per job");
                        }
                        cancelJobImplLocked(jobByUidAndJobId, createFromJobInfo, 1, 0, "job rescheduled by app");
                    } else {
                        startTrackingJobLocked(createFromJobInfo, null);
                    }
                    if (jobWorkItem != null) {
                        createFromJobInfo.enqueueWorkLocked(jobWorkItem);
                        sEnqueuedJwiHighWaterMarkLogger.logSampleWithUid(i, createFromJobInfo.getWorkCount());
                    }
                    FrameworkStatsLog.write_non_chained(8, i, (String) null, createFromJobInfo.getBatteryName(), 2, -1, createFromJobInfo.getStandbyBucket(), createFromJobInfo.getLoggingJobId(), createFromJobInfo.hasChargingConstraint(), createFromJobInfo.hasBatteryNotLowConstraint(), createFromJobInfo.hasStorageNotLowConstraint(), createFromJobInfo.hasTimingDelayConstraint(), createFromJobInfo.hasDeadlineConstraint(), createFromJobInfo.hasIdleConstraint(), createFromJobInfo.hasConnectivityConstraint(), createFromJobInfo.hasContentTriggerConstraint(), createFromJobInfo.isRequestedExpeditedJob(), false, 0, createFromJobInfo.getJob().isPrefetch(), createFromJobInfo.getJob().getPriority(), createFromJobInfo.getEffectivePriority(), createFromJobInfo.getNumPreviousAttempts(), createFromJobInfo.getJob().getMaxExecutionDelayMillis(), false, false, false, false, false, false, false, false, 0L, createFromJobInfo.getJob().isUserInitiated(), false, createFromJobInfo.getJob().isPeriodic(), createFromJobInfo.getJob().getMinLatencyMillis(), createFromJobInfo.getEstimatedNetworkDownloadBytes(), createFromJobInfo.getEstimatedNetworkUploadBytes(), createFromJobInfo.getWorkCount(), ActivityManager.processStateAmToProto(this.mUidProcStates.get(createFromJobInfo.getUid())), createFromJobInfo.getNamespaceHash());
                    if (isReadyToBeExecutedLocked(createFromJobInfo)) {
                        this.mJobPackageTracker.notePending(createFromJobInfo);
                        this.mPendingJobQueue.add(createFromJobInfo);
                        maybeRunPendingJobsLocked();
                    }
                    return 1;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayMap<String, List<JobInfo>> getPendingJobs(int i) {
        ArrayMap<String, List<JobInfo>> arrayMap = new ArrayMap<>();
        synchronized (this.mLock) {
            ArraySet<JobStatus> jobsByUid = this.mJobs.getJobsByUid(i);
            for (int size = jobsByUid.size() - 1; size >= 0; size--) {
                JobStatus valueAt = jobsByUid.valueAt(size);
                List<JobInfo> list = arrayMap.get(valueAt.getNamespace());
                if (list == null) {
                    list = new ArrayList<>();
                    arrayMap.put(valueAt.getNamespace(), list);
                }
                list.add(valueAt.getJob());
            }
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<JobInfo> getPendingJobsInNamespace(int i, String str) {
        ArrayList arrayList;
        synchronized (this.mLock) {
            ArraySet<JobStatus> jobsByUid = this.mJobs.getJobsByUid(i);
            arrayList = new ArrayList();
            for (int size = jobsByUid.size() - 1; size >= 0; size--) {
                JobStatus valueAt = jobsByUid.valueAt(size);
                if (Objects.equals(str, valueAt.getNamespace())) {
                    arrayList.add(valueAt.getJob());
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPendingJobReason(int i, String str, int i2) {
        int pendingJobReasonLocked;
        int i3;
        synchronized (this.mPendingJobReasonCache) {
            SparseIntArray sparseIntArray = (SparseIntArray) this.mPendingJobReasonCache.get(i, str);
            if (sparseIntArray != null && (i3 = sparseIntArray.get(i2, 0)) != 0) {
                return i3;
            }
            synchronized (this.mLock) {
                pendingJobReasonLocked = getPendingJobReasonLocked(i, str, i2);
                if (DEBUG) {
                    Slog.v(TAG, "getPendingJobReason(" + i + "," + str + "," + i2 + ")=" + pendingJobReasonLocked);
                }
            }
            synchronized (this.mPendingJobReasonCache) {
                SparseIntArray sparseIntArray2 = (SparseIntArray) this.mPendingJobReasonCache.get(i, str);
                if (sparseIntArray2 == null) {
                    sparseIntArray2 = new SparseIntArray();
                    this.mPendingJobReasonCache.add(i, str, sparseIntArray2);
                }
                sparseIntArray2.put(i2, pendingJobReasonLocked);
            }
            return pendingJobReasonLocked;
        }
    }

    @VisibleForTesting
    int getPendingJobReason(JobStatus jobStatus) {
        return getPendingJobReason(jobStatus.getUid(), jobStatus.getNamespace(), jobStatus.getJobId());
    }

    @GuardedBy({"mLock"})
    private int getPendingJobReasonLocked(int i, String str, int i2) {
        JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(i, str, i2);
        if (jobByUidAndJobId == null) {
            return -2;
        }
        if (isCurrentlyRunningLocked(jobByUidAndJobId)) {
            return -1;
        }
        boolean isReady = jobByUidAndJobId.isReady();
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " ready=" + isReady);
        }
        if (!isReady) {
            return jobByUidAndJobId.getPendingJobReason();
        }
        boolean areUsersStartedLocked = areUsersStartedLocked(jobByUidAndJobId);
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " userStarted=" + areUsersStartedLocked);
        }
        if (!areUsersStartedLocked) {
            return 15;
        }
        boolean z = this.mBackingUpUids.get(jobByUidAndJobId.getSourceUid());
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " backingUp=" + z);
        }
        if (z) {
            return 1;
        }
        JobRestriction checkIfRestricted = checkIfRestricted(jobByUidAndJobId);
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " restriction=" + checkIfRestricted);
        }
        if (checkIfRestricted != null) {
            return checkIfRestricted.getPendingReason();
        }
        boolean contains = this.mPendingJobQueue.contains(jobByUidAndJobId);
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " pending=" + contains);
        }
        if (contains) {
            return 12;
        }
        boolean isJobRunningLocked = this.mConcurrencyManager.isJobRunningLocked(jobByUidAndJobId);
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " active=" + isJobRunningLocked);
        }
        if (isJobRunningLocked) {
            return 0;
        }
        boolean isComponentUsable = isComponentUsable(jobByUidAndJobId);
        if (DEBUG) {
            Slog.v(TAG, "getPendingJobReasonLocked: " + jobByUidAndJobId.toShortString() + " componentUsable=" + isComponentUsable);
        }
        return !isComponentUsable ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JobInfo getPendingJob(int i, String str, int i2) {
        synchronized (this.mLock) {
            ArraySet<JobStatus> jobsByUid = this.mJobs.getJobsByUid(i);
            for (int size = jobsByUid.size() - 1; size >= 0; size--) {
                JobStatus valueAt = jobsByUid.valueAt(size);
                if (valueAt.getJobId() == i2 && Objects.equals(str, valueAt.getNamespace())) {
                    return valueAt.getJob();
                }
            }
            return null;
        }
    }

    @VisibleForTesting
    void notePendingUserRequestedAppStopInternal(String str, int i, String str2) {
        int packageUid = this.mLocalPM.getPackageUid(str, 0L, i);
        if (packageUid < 0) {
            Slog.wtf(TAG, "Asked to stop jobs of an unknown package");
            return;
        }
        synchronized (this.mLock) {
            this.mConcurrencyManager.markJobsForUserStopLocked(i, str, str2);
            ArraySet<JobStatus> jobsByUid = this.mJobs.getJobsByUid(packageUid);
            for (int size = jobsByUid.size() - 1; size >= 0; size--) {
                JobStatus valueAt = jobsByUid.valueAt(size);
                valueAt.addInternalFlags(2);
                if (this.mPendingJobQueue.remove(valueAt)) {
                    synchronized (this.mPendingJobReasonCache) {
                        SparseIntArray sparseIntArray = (SparseIntArray) this.mPendingJobReasonCache.get(valueAt.getUid(), valueAt.getNamespace());
                        if (sparseIntArray == null) {
                            sparseIntArray = new SparseIntArray();
                            this.mPendingJobReasonCache.add(valueAt.getUid(), valueAt.getNamespace(), sparseIntArray);
                        }
                        sparseIntArray.put(valueAt.getJobId(), 15);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(JobStatus jobStatus) {
        cancelJobImplLocked(jobStatus, null, 13, 7, "user removed");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelJobsForUserLocked(final int i) {
        this.mJobs.forEachJob(new Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$cancelJobsForUserLocked$1;
                lambda$cancelJobsForUserLocked$1 = JobSchedulerService.lambda$cancelJobsForUserLocked$1(i, (JobStatus) obj);
                return lambda$cancelJobsForUserLocked$1;
            }
        }, this.mCancelJobDueToUserRemovalConsumer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$cancelJobsForUserLocked$1(int i, JobStatus jobStatus) {
        return jobStatus.getUserId() == i || jobStatus.getSourceUserId() == i;
    }

    private void cancelJobsForNonExistentUsers() {
        UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        synchronized (this.mLock) {
            this.mJobs.removeJobsOfUnlistedUsers(userManagerInternal.getUserIds());
        }
        synchronized (this.mPendingJobReasonCache) {
            this.mPendingJobReasonCache.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelJobsForPackageAndUidLocked(String str, int i, boolean z, boolean z2, int i2, int i3, String str2) {
        boolean z3;
        if (z || z2) {
            z3 = z2;
        } else {
            Slog.wtfStack(TAG, "Didn't indicate whether to cancel jobs for scheduling and/or source app");
            z3 = true;
        }
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str)) {
            Slog.wtfStack(TAG, "Can't cancel all jobs for system package");
            return;
        }
        ArraySet arraySet = new ArraySet();
        if (z) {
            this.mJobs.getJobsByUid(i, arraySet);
        }
        if (z3) {
            this.mJobs.getJobsBySourceUid(i, arraySet);
        }
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            JobStatus jobStatus = (JobStatus) arraySet.valueAt(size);
            if ((z && jobStatus.getServiceComponent().getPackageName().equals(str)) || (z3 && jobStatus.getSourcePackageName().equals(str))) {
                cancelJobImplLocked(jobStatus, null, i2, i3, str2);
            }
        }
    }

    public boolean cancelJobsForUid(int i, boolean z, int i2, int i3, String str) {
        return cancelJobsForUid(i, z, false, null, i2, i3, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean cancelJobsForUid(int i, boolean z, boolean z2, String str, int i2, int i3, String str2) {
        boolean z3 = false;
        if (i == 1000 && (!z2 || str == null)) {
            Slog.wtfStack(TAG, "Can't cancel all jobs for system uid");
            return false;
        }
        synchronized (this.mLock) {
            ArraySet arraySet = new ArraySet();
            this.mJobs.getJobsByUid(i, arraySet);
            if (z) {
                this.mJobs.getJobsBySourceUid(i, arraySet);
            }
            for (int i4 = 0; i4 < arraySet.size(); i4++) {
                JobStatus jobStatus = (JobStatus) arraySet.valueAt(i4);
                if (!z2 || Objects.equals(str, jobStatus.getNamespace())) {
                    cancelJobImplLocked(jobStatus, null, i2, i3, str2);
                    z3 = true;
                }
            }
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean cancelJob(int i, String str, int i2, int i3, int i4) {
        boolean z;
        synchronized (this.mLock) {
            JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(i, str, i2);
            if (jobByUidAndJobId != null) {
                cancelJobImplLocked(jobByUidAndJobId, null, i4, 0, "cancel() called by app, callingUid=" + i3 + " uid=" + i + " jobId=" + i2);
            }
            z = jobByUidAndJobId != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void cancelJobImplLocked(JobStatus jobStatus, JobStatus jobStatus2, int i, int i2, String str) {
        String str2;
        if (DEBUG) {
            Slog.d(TAG, "CANCEL: " + jobStatus.toShortString());
        }
        if (DEBUG_USAGE) {
            Slog.d(TAG, "CANCEL: " + jobStatus.getJobId() + " " + jobStatus.getSourcePackageName());
        }
        jobStatus.unprepareLocked();
        stopTrackingJobLocked(jobStatus, jobStatus2, true);
        if (this.mPendingJobQueue.remove(jobStatus)) {
            this.mJobPackageTracker.noteNonpending(jobStatus);
        }
        this.mChangedJobList.remove(jobStatus);
        if (this.mConcurrencyManager.stopJobOnServiceContextLocked(jobStatus, i, i2, str)) {
            str2 = TAG;
        } else {
            int sourceUid = jobStatus.getSourceUid();
            String batteryName = jobStatus.getBatteryName();
            int standbyBucket = jobStatus.getStandbyBucket();
            long loggingJobId = jobStatus.getLoggingJobId();
            boolean hasChargingConstraint = jobStatus.hasChargingConstraint();
            boolean hasBatteryNotLowConstraint = jobStatus.hasBatteryNotLowConstraint();
            boolean hasStorageNotLowConstraint = jobStatus.hasStorageNotLowConstraint();
            boolean hasTimingDelayConstraint = jobStatus.hasTimingDelayConstraint();
            str2 = TAG;
            FrameworkStatsLog.write_non_chained(8, sourceUid, (String) null, batteryName, 3, i2, standbyBucket, loggingJobId, hasChargingConstraint, hasBatteryNotLowConstraint, hasStorageNotLowConstraint, hasTimingDelayConstraint, jobStatus.hasDeadlineConstraint(), jobStatus.hasIdleConstraint(), jobStatus.hasConnectivityConstraint(), jobStatus.hasContentTriggerConstraint(), jobStatus.isRequestedExpeditedJob(), false, i, jobStatus.getJob().isPrefetch(), jobStatus.getJob().getPriority(), jobStatus.getEffectivePriority(), jobStatus.getNumPreviousAttempts(), jobStatus.getJob().getMaxExecutionDelayMillis(), jobStatus.isConstraintSatisfied(1073741824), jobStatus.isConstraintSatisfied(1), jobStatus.isConstraintSatisfied(2), jobStatus.isConstraintSatisfied(8), jobStatus.isConstraintSatisfied(Integer.MIN_VALUE), jobStatus.isConstraintSatisfied(4), jobStatus.isConstraintSatisfied(268435456), jobStatus.isConstraintSatisfied(67108864), 0L, jobStatus.getJob().isUserInitiated(), false, jobStatus.getJob().isPeriodic(), jobStatus.getJob().getMinLatencyMillis(), jobStatus.getEstimatedNetworkDownloadBytes(), jobStatus.getEstimatedNetworkUploadBytes(), jobStatus.getWorkCount(), ActivityManager.processStateAmToProto(this.mUidProcStates.get(jobStatus.getUid())), jobStatus.getNamespaceHash());
        }
        if (jobStatus2 != null) {
            if (DEBUG) {
                Slog.i(str2, "Tracking replacement job " + jobStatus2.toShortString());
            }
            startTrackingJobLocked(jobStatus2, jobStatus);
        }
        reportActiveLocked();
        JobStatus[] jobStatusArr = this.mLastCancelledJobs;
        if (jobStatusArr.length <= 0 || i2 != 0) {
            return;
        }
        int i3 = this.mLastCancelledJobIndex;
        jobStatusArr[i3] = jobStatus;
        this.mLastCancelledJobTimeElapsed[i3] = sElapsedRealtimeClock.millis();
        this.mLastCancelledJobIndex = (this.mLastCancelledJobIndex + 1) % this.mLastCancelledJobs.length;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0081 A[Catch: all -> 0x00c8, TryCatch #0 {, blocks: (B:7:0x0035, B:9:0x0044, B:14:0x006e, B:15:0x0079, B:17:0x0081, B:19:0x0085, B:21:0x00ab, B:23:0x00b3, B:25:0x00c1, B:26:0x00c6, B:30:0x0074, B:33:0x004f, B:36:0x005a, B:37:0x0062), top: B:6:0x0035 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void updateUidState(int i, int i2, int i3) {
        int i4;
        if (DEBUG) {
            Slog.d(TAG, "UID " + i + " proc state changed to " + ActivityManager.procStateToString(i2) + " with capabilities=" + ActivityManager.getCapabilitiesSummary(i3));
        }
        synchronized (this.mLock) {
            this.mUidProcStates.put(i, i2);
            int i5 = this.mUidBiasOverride.get(i, 0);
            if (i2 == 2) {
                this.mUidBiasOverride.put(i, 40);
            } else if (i2 <= 4) {
                this.mUidBiasOverride.put(i, 35);
            } else if (i2 <= 5) {
                this.mUidBiasOverride.put(i, 30);
            } else {
                this.mUidBiasOverride.delete(i);
            }
            if (i3 != 0 && i2 != 20) {
                this.mUidCapabilities.put(i, i3);
                i4 = this.mUidBiasOverride.get(i, 0);
                if (i5 != i4) {
                    if (DEBUG) {
                        Slog.d(TAG, "UID " + i + " bias changed from " + i5 + " to " + i4);
                    }
                    for (int i6 = 0; i6 < this.mControllers.size(); i6++) {
                        this.mControllers.get(i6).onUidBiasChangedLocked(i, i5, i4);
                    }
                    this.mConcurrencyManager.onUidBiasChangedLocked(i5, i4);
                }
            }
            this.mUidCapabilities.delete(i);
            i4 = this.mUidBiasOverride.get(i, 0);
            if (i5 != i4) {
            }
        }
    }

    public int getUidBias(int i) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mUidBiasOverride.get(i, 0);
        }
        return i2;
    }

    public int getUidCapabilities(int i) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mUidCapabilities.get(i, 0);
        }
        return i2;
    }

    public int getUidProcState(int i) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mUidProcStates.get(i, -1);
        }
        return i2;
    }

    @Override // com.android.server.job.StateChangedListener
    public void onDeviceIdleStateChanged(boolean z) {
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.d(TAG, "Doze state changed: " + z);
            }
            if (!z && this.mReadyToRock) {
                DeviceIdleInternal deviceIdleInternal = this.mLocalDeviceIdleController;
                if (deviceIdleInternal != null && !this.mReportedActive) {
                    this.mReportedActive = true;
                    deviceIdleInternal.setJobsActive(true);
                }
                this.mHandler.obtainMessage(1).sendToTarget();
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onNetworkChanged(JobStatus jobStatus, Network network) {
        synchronized (this.mLock) {
            JobServiceContext runningJobServiceContextLocked = this.mConcurrencyManager.getRunningJobServiceContextLocked(jobStatus);
            if (runningJobServiceContextLocked != null) {
                runningJobServiceContextLocked.informOfNetworkChangeLocked(network);
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onRestrictedBucketChanged(List<JobStatus> list) {
        int size = list.size();
        if (size == 0) {
            Slog.wtf(TAG, "onRestrictedBucketChanged called with no jobs");
            return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < size; i++) {
                JobStatus jobStatus = list.get(i);
                for (int size2 = this.mRestrictiveControllers.size() - 1; size2 >= 0; size2--) {
                    if (jobStatus.getStandbyBucket() == 5) {
                        this.mRestrictiveControllers.get(size2).startTrackingRestrictedJobLocked(jobStatus);
                    } else {
                        this.mRestrictiveControllers.get(size2).stopTrackingRestrictedJobLocked(jobStatus);
                    }
                }
            }
        }
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    void reportActiveLocked() {
        boolean z = true;
        boolean z2 = this.mPendingJobQueue.size() > 0;
        if (!z2) {
            ArraySet<JobStatus> runningJobsLocked = this.mConcurrencyManager.getRunningJobsLocked();
            for (int size = runningJobsLocked.size() - 1; size >= 0; size--) {
                if (!runningJobsLocked.valueAt(size).canRunInDoze()) {
                    break;
                }
            }
        }
        z = z2;
        if (this.mReportedActive != z) {
            this.mReportedActive = z;
            DeviceIdleInternal deviceIdleInternal = this.mLocalDeviceIdleController;
            if (deviceIdleInternal != null) {
                deviceIdleInternal.setJobsActive(z);
            }
        }
    }

    public JobSchedulerService(Context context) {
        super(context);
        byte b = 0;
        this.mJobSchedulerServiceWrapper = new JobSchedulerServiceWrapper();
        this.mJobSchedulerServiceExt = (IJobSchedulerServiceExt) ExtLoader.type(IJobSchedulerServiceExt.class).create();
        this.mLock = new Object();
        this.mJobPackageTracker = new JobPackageTracker();
        this.mCloudMediaProviderPackages = new SparseArray<>();
        this.mUserVisibleJobObservers = new RemoteCallbackList<>();
        this.mPermissionCache = new SparseArray<>();
        this.mPendingJobQueue = new PendingJobQueue();
        this.mStartedUsers = EmptyArray.INT;
        this.mLastCompletedJobIndex = 0;
        this.mLastCompletedJobs = new JobStatus[20];
        this.mLastCompletedJobTimeElapsed = new long[20];
        this.mLastCancelledJobIndex = 0;
        boolean z = DEBUG;
        this.mLastCancelledJobs = new JobStatus[z ? 20 : 0];
        this.mLastCancelledJobTimeElapsed = new long[z ? 20 : 0];
        this.mUidBiasOverride = new SparseIntArray();
        this.mUidCapabilities = new SparseIntArray();
        this.mUidProcStates = new SparseIntArray();
        this.mBackingUpUids = new SparseBooleanArray();
        this.mDebuggableApps = new ArrayMap<>();
        this.mUidToPackageCache = new SparseSetArray<>();
        this.mChangedJobList = new ArraySet<>();
        this.mPendingJobReasonCache = new SparseArrayMap<>();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.job.JobSchedulerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ArraySet<JobStatus> jobsByUid;
                String action = intent.getAction();
                if (JobSchedulerService.DEBUG) {
                    Slog.d(JobSchedulerService.TAG, "Receieved: " + action);
                }
                String packageName = JobSchedulerService.this.getPackageName(intent);
                int intExtra = intent.getIntExtra("android.intent.extra.UID", -1);
                int i = 0;
                if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
                    synchronized (JobSchedulerService.this.mPermissionCache) {
                        JobSchedulerService.this.mPermissionCache.remove(intExtra);
                    }
                    if (packageName != null && intExtra != -1) {
                        String[] stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
                        if (stringArrayExtra != null) {
                            int length = stringArrayExtra.length;
                            while (true) {
                                if (i >= length) {
                                    break;
                                }
                                if (stringArrayExtra[i].equals(packageName)) {
                                    if (JobSchedulerService.DEBUG) {
                                        Slog.d(JobSchedulerService.TAG, "Package state change: " + packageName);
                                    }
                                    try {
                                        int userId = UserHandle.getUserId(intExtra);
                                        int applicationEnabledSetting = AppGlobals.getPackageManager().getApplicationEnabledSetting(packageName, userId);
                                        if (applicationEnabledSetting == 2 || applicationEnabledSetting == 3) {
                                            if (JobSchedulerService.DEBUG) {
                                                Slog.d(JobSchedulerService.TAG, "Removing jobs for package " + packageName + " in user " + userId);
                                            }
                                            synchronized (JobSchedulerService.this.mLock) {
                                                JobSchedulerService.this.cancelJobsForPackageAndUidLocked(packageName, intExtra, true, true, 13, 7, "app disabled");
                                            }
                                        }
                                    } catch (RemoteException | IllegalArgumentException unused) {
                                    }
                                } else {
                                    i++;
                                }
                            }
                            if (JobSchedulerService.DEBUG) {
                                Slog.d(JobSchedulerService.TAG, "Something in " + packageName + " changed. Reevaluating controller states.");
                            }
                            synchronized (JobSchedulerService.this.mLock) {
                                for (int size = JobSchedulerService.this.mControllers.size() - 1; size >= 0; size--) {
                                    JobSchedulerService.this.mControllers.get(size).reevaluateStateLocked(intExtra);
                                }
                            }
                            return;
                        }
                        return;
                    }
                    Slog.w(JobSchedulerService.TAG, "PACKAGE_CHANGED for " + packageName + " / uid " + intExtra);
                    return;
                }
                if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                    synchronized (JobSchedulerService.this.mPermissionCache) {
                        JobSchedulerService.this.mPermissionCache.remove(intExtra);
                    }
                    if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        return;
                    }
                    synchronized (JobSchedulerService.this.mLock) {
                        JobSchedulerService.this.mUidToPackageCache.remove(intExtra);
                    }
                    return;
                }
                if ("android.intent.action.PACKAGE_FULLY_REMOVED".equals(action)) {
                    synchronized (JobSchedulerService.this.mPermissionCache) {
                        JobSchedulerService.this.mPermissionCache.remove(intExtra);
                    }
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Removing jobs for " + packageName + " (uid=" + intExtra + ")");
                    }
                    synchronized (JobSchedulerService.this.mLock) {
                        JobSchedulerService.this.mUidToPackageCache.remove(intExtra);
                        JobSchedulerService.this.cancelJobsForPackageAndUidLocked(packageName, intExtra, true, true, 13, 7, "app uninstalled");
                        while (i < JobSchedulerService.this.mControllers.size()) {
                            JobSchedulerService.this.mControllers.get(i).onAppRemovedLocked(packageName, intExtra);
                            i++;
                        }
                        JobSchedulerService.this.mDebuggableApps.remove(packageName);
                        JobSchedulerService.this.mConcurrencyManager.onAppRemovedLocked(packageName, intExtra);
                        JobSchedulerService.this.mJobSchedulerServiceExt.hookReceivePackageRemove(JobSchedulerService.this, intExtra, packageName);
                    }
                    return;
                }
                if ("android.intent.action.UID_REMOVED".equals(action)) {
                    if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        return;
                    }
                    synchronized (JobSchedulerService.this.mLock) {
                        JobSchedulerService.this.mUidBiasOverride.delete(intExtra);
                        JobSchedulerService.this.mUidCapabilities.delete(intExtra);
                        JobSchedulerService.this.mUidProcStates.delete(intExtra);
                    }
                    return;
                }
                if ("android.intent.action.USER_ADDED".equals(action)) {
                    int intExtra2 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    synchronized (JobSchedulerService.this.mLock) {
                        while (i < JobSchedulerService.this.mControllers.size()) {
                            JobSchedulerService.this.mControllers.get(i).onUserAddedLocked(intExtra2);
                            i++;
                        }
                    }
                    return;
                }
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    int intExtra3 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Removing jobs for user: " + intExtra3);
                    }
                    synchronized (JobSchedulerService.this.mLock) {
                        JobSchedulerService.this.mUidToPackageCache.clear();
                        JobSchedulerService.this.cancelJobsForUserLocked(intExtra3);
                        while (i < JobSchedulerService.this.mControllers.size()) {
                            JobSchedulerService.this.mControllers.get(i).onUserRemovedLocked(intExtra3);
                            i++;
                        }
                    }
                    JobSchedulerService.this.mConcurrencyManager.onUserRemoved(intExtra3);
                    synchronized (JobSchedulerService.this.mPermissionCache) {
                        for (int size2 = JobSchedulerService.this.mPermissionCache.size() - 1; size2 >= 0; size2--) {
                            if (intExtra3 == UserHandle.getUserId(JobSchedulerService.this.mPermissionCache.keyAt(size2))) {
                                JobSchedulerService.this.mPermissionCache.removeAt(size2);
                            }
                        }
                    }
                    return;
                }
                if (!"android.intent.action.QUERY_PACKAGE_RESTART".equals(action)) {
                    if (!"android.intent.action.PACKAGE_RESTARTED".equals(action) || intExtra == -1) {
                        return;
                    }
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Removing jobs for pkg " + packageName + " at uid " + intExtra);
                    }
                    if (JobSchedulerService.this.mJobSchedulerServiceExt.ignoreJobRemoved(JobSchedulerService.this, packageName, intExtra)) {
                        synchronized (JobSchedulerService.this.mLock) {
                            JobSchedulerService.this.cancelJobsForPackageAndUidLocked(packageName, intExtra, true, false, 13, 0, "app force stopped");
                        }
                        JobSchedulerService.this.mJobSchedulerServiceExt.hookReceivePackageRestarted(JobSchedulerService.this, intExtra, packageName);
                        return;
                    }
                    return;
                }
                if (intExtra != -1) {
                    synchronized (JobSchedulerService.this.mLock) {
                        jobsByUid = JobSchedulerService.this.mJobs.getJobsByUid(intExtra);
                    }
                    for (int size3 = jobsByUid.size() - 1; size3 >= 0; size3--) {
                        if (jobsByUid.valueAt(size3).getSourcePackageName().equals(packageName)) {
                            if (JobSchedulerService.DEBUG) {
                                Slog.d(JobSchedulerService.TAG, "Restart query: package " + packageName + " at uid " + intExtra + " has jobs");
                            }
                            setResultCode(-1);
                            return;
                        }
                    }
                }
            }
        };
        this.mUidObserver = new UidObserver() { // from class: com.android.server.job.JobSchedulerService.4
            public void onUidStateChanged(int i, int i2, long j, int i3) {
                SomeArgs obtain = SomeArgs.obtain();
                obtain.argi1 = i;
                obtain.argi2 = i2;
                obtain.argi3 = i3;
                JobSchedulerService.this.mHandler.obtainMessage(4, obtain).sendToTarget();
            }

            public void onUidGone(int i, boolean z2) {
                JobSchedulerService.this.mHandler.obtainMessage(5, i, z2 ? 1 : 0).sendToTarget();
            }

            public void onUidActive(int i) {
                JobSchedulerService.this.mHandler.obtainMessage(6, i, 0).sendToTarget();
            }

            public void onUidIdle(int i, boolean z2) {
                JobSchedulerService.this.mHandler.obtainMessage(7, i, z2 ? 1 : 0).sendToTarget();
            }
        };
        this.mIsUidActivePredicate = new Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isUidActive;
                isUidActive = JobSchedulerService.this.isUidActive(((Integer) obj).intValue());
                return isUidActive;
            }
        };
        this.mCancelJobDueToUserRemovalConsumer = new Consumer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                JobSchedulerService.this.lambda$new$0((JobStatus) obj);
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.job.JobSchedulerService.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.TIME_SET".equals(intent.getAction()) && JobSchedulerService.this.mJobs.clockNowValidToInflate(JobSchedulerService.sSystemClock.millis())) {
                    Slog.i(JobSchedulerService.TAG, "RTC now valid; recalculating persisted job windows");
                    context2.unregisterReceiver(this);
                    JobSchedulerService jobSchedulerService = JobSchedulerService.this;
                    jobSchedulerService.mJobs.runWorkAsync(jobSchedulerService.mJobTimeUpdater);
                }
            }
        };
        this.mTimeSetReceiver = broadcastReceiver;
        this.mJobTimeUpdater = new Runnable() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                JobSchedulerService.this.lambda$new$3();
            }
        };
        this.mReadyQueueFunctor = new ReadyJobQueueFunctor();
        this.mMaybeQueueFunctor = new MaybeReadyJobQueueFunctor();
        this.mLocalPM = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        Objects.requireNonNull(activityManagerInternal);
        this.mActivityManagerInternal = activityManagerInternal;
        JobHandler jobHandler = new JobHandler(AppSchedulingModuleThread.get().getLooper());
        this.mHandler = jobHandler;
        this.mConstants = new Constants();
        this.mConstantsObserver = new ConstantsObserver();
        this.mJobSchedulerStub = new JobSchedulerStub();
        this.mConcurrencyManager = new JobConcurrencyManager(this);
        StandbyTracker standbyTracker = new StandbyTracker();
        this.mStandbyTracker = standbyTracker;
        this.mUsageStats = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        CountQuotaTracker countQuotaTracker = new CountQuotaTracker(context, new Categorizer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda5
            @Override // com.android.server.utils.quota.Categorizer
            public final Category getCategory(int i, String str, String str2) {
                Category lambda$new$2;
                lambda$new$2 = JobSchedulerService.this.lambda$new$2(i, str, str2);
                return lambda$new$2;
            }
        });
        this.mQuotaTracker = countQuotaTracker;
        updateQuotaTracker();
        countQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED, 1, 60000L);
        countQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_DISABLED, Integer.MAX_VALUE, 60000L);
        AppStandbyInternal appStandbyInternal = (AppStandbyInternal) LocalServices.getService(AppStandbyInternal.class);
        this.mAppStandbyInternal = appStandbyInternal;
        appStandbyInternal.addListener(standbyTracker);
        publishLocalService(JobSchedulerInternal.class, new LocalService());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.mJobStoreLoadedLatch = countDownLatch;
        JobStore jobStore = JobStore.get(this);
        this.mJobs = jobStore;
        jobStore.initAsync(countDownLatch);
        this.mJobSchedulerServiceExt.onHookPreInit(this, jobHandler, context);
        BatteryStateTracker batteryStateTracker = new BatteryStateTracker();
        this.mBatteryStateTracker = batteryStateTracker;
        batteryStateTracker.startTracking();
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        PrefetchController prefetchController = new PrefetchController(this);
        this.mPrefetchController = prefetchController;
        arrayList.add(prefetchController);
        FlexibilityController flexibilityController = new FlexibilityController(this, prefetchController);
        arrayList.add(flexibilityController);
        ConnectivityController connectivityController = new ConnectivityController(this, flexibilityController);
        this.mConnectivityController = connectivityController;
        arrayList.add(connectivityController);
        arrayList.add(new TimeController(this));
        IdleController idleController = new IdleController(this, flexibilityController);
        arrayList.add(idleController);
        BatteryController batteryController = new BatteryController(this, flexibilityController);
        arrayList.add(batteryController);
        StorageController storageController = new StorageController(this);
        this.mStorageController = storageController;
        arrayList.add(storageController);
        BackgroundJobsController backgroundJobsController = new BackgroundJobsController(this);
        arrayList.add(backgroundJobsController);
        arrayList.add(new ContentObserverController(this));
        DeviceIdleJobsController deviceIdleJobsController = new DeviceIdleJobsController(this);
        this.mDeviceIdleJobsController = deviceIdleJobsController;
        arrayList.add(deviceIdleJobsController);
        QuotaController quotaController = new QuotaController(this, backgroundJobsController, connectivityController);
        this.mQuotaController = quotaController;
        arrayList.add(quotaController);
        arrayList.add(new ComponentController(this));
        TareController tareController = new TareController(this, backgroundJobsController, connectivityController);
        this.mTareController = tareController;
        arrayList.add(tareController);
        ArrayList arrayList2 = new ArrayList();
        this.mRestrictiveControllers = arrayList2;
        arrayList2.add(batteryController);
        arrayList2.add(connectivityController);
        arrayList2.add(idleController);
        ArrayList arrayList3 = new ArrayList();
        this.mJobRestrictions = arrayList3;
        arrayList3.add(new ThermalStatusRestriction(this));
        this.mJobSchedulerServiceExt.onHookEndInit(this, arrayList, arrayList3);
        if (jobStore.jobTimesInflatedValid()) {
            return;
        }
        Slog.w(TAG, "!!! RTC not yet good; tracking time updates for job scheduling");
        context.registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.TIME_SET"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Category lambda$new$2(int i, String str, String str2) {
        if (QUOTA_TRACKER_TIMEOUT_UIJ_TAG.equals(str2)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_TIMEOUT_EJ_TAG.equals(str2)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_TIMEOUT_REG_TAG.equals(str2)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_REG;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_TIMEOUT_TOTAL_TAG.equals(str2)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_ANR_TAG.equals(str2)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_ANR;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG.equals(str2)) {
            if (this.mConstants.ENABLE_API_QUOTAS) {
                return QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_SCHEDULE_LOGGED.equals(str2)) {
            if (this.mConstants.ENABLE_API_QUOTAS) {
                return QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        Slog.wtf(TAG, "Unexpected category tag: " + str2);
        return QUOTA_TRACKER_CATEGORY_DISABLED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        Process.setThreadPriority(-2);
        ArrayList<JobStatus> arrayList = new ArrayList<>();
        ArrayList<JobStatus> arrayList2 = new ArrayList<>();
        synchronized (this.mLock) {
            getJobStore().getRtcCorrectedJobsLocked(arrayList2, arrayList);
            int size = arrayList2.size();
            for (int i = 0; i < size; i++) {
                JobStatus jobStatus = arrayList.get(i);
                JobStatus jobStatus2 = arrayList2.get(i);
                if (DEBUG) {
                    Slog.v(TAG, "  replacing " + jobStatus + " with " + jobStatus2);
                }
                cancelJobImplLocked(jobStatus, jobStatus2, 14, 9, "deferred rtc calculation");
            }
        }
    }

    public void onStart() {
        publishBinderService("jobscheduler", this.mJobSchedulerStub);
    }

    public void onBootPhase(int i) {
        if (480 == i) {
            try {
                this.mJobStoreLoadedLatch.await();
                return;
            } catch (InterruptedException unused) {
                Slog.e(TAG, "Couldn't wait on job store loading latch");
                return;
            }
        }
        if (500 != i) {
            if (i == 600) {
                synchronized (this.mLock) {
                    this.mReadyToRock = true;
                    this.mLocalDeviceIdleController = (DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class);
                    this.mConcurrencyManager.onThirdPartyAppsCanStart();
                    this.mJobs.forEachJob(new Consumer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda6
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            JobSchedulerService.this.lambda$onBootPhase$4((JobStatus) obj);
                        }
                    });
                    this.mHandler.obtainMessage(1).sendToTarget();
                }
                return;
            }
            return;
        }
        this.mJobSchedulerServiceExt.onHookSystemReady();
        this.mConstantsObserver.start();
        Iterator<StateController> it = this.mControllers.iterator();
        while (it.hasNext()) {
            it.next().onSystemServicesReady();
        }
        AppStateTrackerImpl appStateTrackerImpl = (AppStateTracker) LocalServices.getService(AppStateTracker.class);
        Objects.requireNonNull(appStateTrackerImpl);
        this.mAppStateTracker = appStateTrackerImpl;
        ((StorageManagerInternal) LocalServices.getService(StorageManagerInternal.class)).registerCloudProviderChangeListener(new CloudProviderChangeListener());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
        intentFilter.addDataScheme("package");
        getContext().registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter, null, null);
        getContext().registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, new IntentFilter("android.intent.action.UID_REMOVED"), null, null);
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.USER_REMOVED");
        intentFilter2.addAction("android.intent.action.USER_ADDED");
        getContext().registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter2, null, null);
        try {
            ActivityManager.getService().registerUidObserver(this.mUidObserver, 15, -1, (String) null);
        } catch (RemoteException unused2) {
        }
        this.mConcurrencyManager.onSystemReady();
        cancelJobsForNonExistentUsers();
        for (int size = this.mJobRestrictions.size() - 1; size >= 0; size--) {
            this.mJobRestrictions.get(size).onSystemServicesReady();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$4(JobStatus jobStatus) {
        for (int i = 0; i < this.mControllers.size(); i++) {
            this.mControllers.get(i).maybeStartTrackingJobLocked(jobStatus, null);
        }
    }

    private void startTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2) {
        if (!jobStatus.isPreparedLocked()) {
            Slog.wtf(TAG, "Not yet prepared when started tracking: " + jobStatus);
        }
        jobStatus.enqueueTime = sElapsedRealtimeClock.millis();
        boolean z = jobStatus2 != null;
        this.mJobs.add(jobStatus);
        resetPendingJobReasonCache(jobStatus);
        if (this.mReadyToRock) {
            for (int i = 0; i < this.mControllers.size(); i++) {
                StateController stateController = this.mControllers.get(i);
                if (z) {
                    stateController.maybeStopTrackingJobLocked(jobStatus, null);
                }
                stateController.maybeStartTrackingJobLocked(jobStatus, jobStatus2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean stopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2, boolean z) {
        jobStatus.stopTrackingJobLocked(jobStatus2);
        synchronized (this.mPendingJobReasonCache) {
            SparseIntArray sparseIntArray = (SparseIntArray) this.mPendingJobReasonCache.get(jobStatus.getUid(), jobStatus.getNamespace());
            if (sparseIntArray != null) {
                sparseIntArray.delete(jobStatus.getJobId());
            }
        }
        boolean remove = this.mJobs.remove(jobStatus, z);
        if (!remove) {
            Slog.w(TAG, "Job didn't exist in JobStore: #" + jobStatus.getSourcePackageName() + SliceClientPermissions.SliceAuthority.DELIMITER + jobStatus.getJobId());
        }
        if (this.mReadyToRock) {
            for (int i = 0; i < this.mControllers.size(); i++) {
                this.mControllers.get(i).maybeStopTrackingJobLocked(jobStatus, jobStatus2);
            }
        }
        return remove;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetPendingJobReasonCache(JobStatus jobStatus) {
        synchronized (this.mPendingJobReasonCache) {
            SparseIntArray sparseIntArray = (SparseIntArray) this.mPendingJobReasonCache.get(jobStatus.getUid(), jobStatus.getNamespace());
            if (sparseIntArray != null) {
                sparseIntArray.delete(jobStatus.getJobId());
            }
        }
    }

    @GuardedBy({"mLock"})
    public boolean isCurrentlyRunningLocked(JobStatus jobStatus) {
        return this.mConcurrencyManager.isJobRunningLocked(jobStatus);
    }

    @GuardedBy({"mLock"})
    public boolean isJobInOvertimeLocked(JobStatus jobStatus) {
        return this.mConcurrencyManager.isJobInOvertimeLocked(jobStatus);
    }

    private void noteJobPending(JobStatus jobStatus) {
        this.mJobPackageTracker.notePending(jobStatus);
    }

    void noteJobsPending(List<JobStatus> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            noteJobPending(list.get(size));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void noteJobNonPending(JobStatus jobStatus) {
        this.mJobPackageTracker.noteNonpending(jobStatus);
    }

    private void clearPendingJobQueue() {
        this.mPendingJobQueue.resetIterator();
        while (true) {
            JobStatus next = this.mPendingJobQueue.next();
            if (next != null) {
                noteJobNonPending(next);
            } else {
                this.mPendingJobQueue.clear();
                return;
            }
        }
    }

    @VisibleForTesting
    JobStatus getRescheduleJobForFailureLocked(JobStatus jobStatus, int i, int i2) {
        long j;
        long translateDelayTime;
        if (i2 == 11 && jobStatus.isUserVisibleJob()) {
            Slog.i(TAG, "Dropping " + jobStatus.toShortString() + " because of user stop");
            return null;
        }
        long millis = sElapsedRealtimeClock.millis();
        JobInfo job = jobStatus.getJob();
        long initialBackoffMillis = job.getInitialBackoffMillis();
        int numFailures = jobStatus.getNumFailures();
        int numSystemStops = jobStatus.getNumSystemStops();
        if (i2 == 10 || i2 == 3 || i2 == 12 || i == 13) {
            numFailures++;
        } else {
            numSystemStops++;
        }
        int i3 = numFailures;
        int i4 = numSystemStops;
        int i5 = i3 + (i4 / this.mConstants.SYSTEM_STOP_TO_FAILURE_RATIO);
        if (i5 == 0) {
            translateDelayTime = 0;
        } else {
            int backoffPolicy = job.getBackoffPolicy();
            if (backoffPolicy == 0) {
                long j2 = this.mConstants.MIN_LINEAR_BACKOFF_TIME_MS;
                if (initialBackoffMillis < j2) {
                    initialBackoffMillis = j2;
                }
                j = initialBackoffMillis * i5;
            } else {
                if (backoffPolicy != 1 && DEBUG) {
                    Slog.v(TAG, "Unrecognised back-off policy, defaulting to exponential.");
                }
                long j3 = this.mConstants.MIN_EXP_BACKOFF_TIME_MS;
                if (initialBackoffMillis < j3) {
                    initialBackoffMillis = j3;
                }
                j = Math.scalb((float) initialBackoffMillis, i5 - 1);
            }
            translateDelayTime = millis + this.mJobSchedulerServiceExt.translateDelayTime(job, Math.min(j, 18000000L));
        }
        JobStatus jobStatus2 = new JobStatus(jobStatus, translateDelayTime, JobStatus.NO_LATEST_RUNTIME, i3, i4, jobStatus.getLastSuccessfulRunTime(), sSystemClock.millis(), jobStatus.getCumulativeExecutionTimeMs());
        if (i == 13) {
            jobStatus2.addInternalFlags(2);
        }
        if (jobStatus2.getCumulativeExecutionTimeMs() >= this.mConstants.RUNTIME_CUMULATIVE_UI_LIMIT_MS && jobStatus2.shouldTreatAsUserInitiatedJob()) {
            jobStatus2.addInternalFlags(4);
        }
        if (job.isPeriodic()) {
            jobStatus2.setOriginalLatestRunTimeElapsed(jobStatus.getOriginalLatestRunTimeElapsed());
        }
        for (int i6 = 0; i6 < this.mControllers.size(); i6++) {
            this.mControllers.get(i6).rescheduleForFailureLocked(jobStatus2, jobStatus);
        }
        return jobStatus2;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00f4  */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    JobStatus getRescheduleJobForPeriodic(JobStatus jobStatus) {
        long j;
        long min;
        long millis = sElapsedRealtimeClock.millis();
        long max = Math.max(JobInfo.getMinPeriodMillis(), Math.min(31536000000L, jobStatus.getJob().getIntervalMillis()));
        long max2 = Math.max(JobInfo.getMinFlexMillis(), Math.min(max, jobStatus.getJob().getFlexMillis()));
        long originalLatestRunTimeElapsed = jobStatus.getOriginalLatestRunTimeElapsed();
        if (originalLatestRunTimeElapsed < 0 || originalLatestRunTimeElapsed == JobStatus.NO_LATEST_RUNTIME) {
            Slog.wtf(TAG, "Invalid periodic job original latest run time: " + originalLatestRunTimeElapsed);
            originalLatestRunTimeElapsed = millis;
        }
        long abs = Math.abs(millis - originalLatestRunTimeElapsed);
        if (millis > originalLatestRunTimeElapsed) {
            if (DEBUG) {
                Slog.i(TAG, "Periodic job ran after its intended window by " + abs + " ms");
            }
            long j2 = (abs / max) + 1;
            if (max != max2 && (max - max2) - (abs % max) <= max2 / 6) {
                if (DEBUG) {
                    Slog.d(TAG, "Custom flex job ran too close to next window.");
                }
                j2++;
            }
            j = originalLatestRunTimeElapsed + (j2 * max);
        } else {
            j = originalLatestRunTimeElapsed + max;
            if (abs < 1800000) {
                long j3 = max / 6;
                if (abs < j3) {
                    min = Math.min(1800000L, j3 - abs);
                    if (j >= millis) {
                        Slog.wtf(TAG, "Rescheduling calculated latest runtime in the past: " + j);
                        long j4 = millis + max;
                        return new JobStatus(jobStatus, j4 - max2, j4, 0, 0, sSystemClock.millis(), jobStatus.getLastFailedRunTime(), 0L);
                    }
                    long min2 = j - Math.min(max2, max - min);
                    if (DEBUG) {
                        Slog.v(TAG, "Rescheduling executed periodic. New execution window [" + (min2 / 1000) + ", " + (j / 1000) + "]s");
                    }
                    return new JobStatus(jobStatus, min2, j, 0, 0, sSystemClock.millis(), jobStatus.getLastFailedRunTime(), 0L);
                }
            }
        }
        min = 0;
        if (j >= millis) {
        }
    }

    @VisibleForTesting
    void maybeProcessBuggyJob(JobStatus jobStatus, int i) {
        String str;
        boolean z = true;
        boolean z2 = i == 3;
        if (!z2 && jobStatus.madeActive > 0) {
            long millis = sUptimeMillisClock.millis() - jobStatus.madeActive;
            if (!jobStatus.startedAsUserInitiatedJob ? !jobStatus.startedAsExpeditedJob ? millis < this.mConstants.RUNTIME_MIN_GUARANTEE_MS : millis < this.mConstants.RUNTIME_MIN_EJ_GUARANTEE_MS : millis < this.mConstants.RUNTIME_MIN_UI_GUARANTEE_MS) {
                z = false;
            }
            z2 = z;
        }
        if (z2) {
            int timeoutBlameUserId = jobStatus.getTimeoutBlameUserId();
            String timeoutBlamePackageName = jobStatus.getTimeoutBlamePackageName();
            CountQuotaTracker countQuotaTracker = this.mQuotaTracker;
            if (jobStatus.startedAsUserInitiatedJob) {
                str = QUOTA_TRACKER_TIMEOUT_UIJ_TAG;
            } else {
                str = jobStatus.startedAsExpeditedJob ? QUOTA_TRACKER_TIMEOUT_EJ_TAG : QUOTA_TRACKER_TIMEOUT_REG_TAG;
            }
            countQuotaTracker.noteEvent(timeoutBlameUserId, timeoutBlamePackageName, str);
            if (!this.mQuotaTracker.noteEvent(timeoutBlameUserId, timeoutBlamePackageName, QUOTA_TRACKER_TIMEOUT_TOTAL_TAG)) {
                this.mAppStandbyInternal.restrictApp(timeoutBlamePackageName, timeoutBlameUserId, 4);
            }
        }
        if (i == 12) {
            int userId = jobStatus.getUserId();
            String packageName = jobStatus.getServiceComponent().getPackageName();
            if (this.mQuotaTracker.noteEvent(userId, packageName, QUOTA_TRACKER_ANR_TAG)) {
                return;
            }
            this.mAppStandbyInternal.restrictApp(packageName, userId, 4);
        }
    }

    @Override // com.android.server.job.JobCompletedListener
    public void onJobCompletedLocked(JobStatus jobStatus, int i, int i2, boolean z) {
        if (DEBUG) {
            Slog.d(TAG, "Completed " + jobStatus + ", reason=" + i2 + ", reschedule=" + z);
        }
        JobStatus[] jobStatusArr = this.mLastCompletedJobs;
        int i3 = this.mLastCompletedJobIndex;
        jobStatusArr[i3] = jobStatus;
        this.mLastCompletedJobTimeElapsed[i3] = sElapsedRealtimeClock.millis();
        this.mLastCompletedJobIndex = (this.mLastCompletedJobIndex + 1) % 20;
        maybeProcessBuggyJob(jobStatus, i2);
        if (i2 == 7 || i2 == 8) {
            jobStatus.unprepareLocked();
            reportActiveLocked();
            return;
        }
        JobStatus rescheduleJobForFailureLocked = z ? getRescheduleJobForFailureLocked(jobStatus, i, i2) : null;
        if (rescheduleJobForFailureLocked != null && !rescheduleJobForFailureLocked.shouldTreatAsUserInitiatedJob() && (i2 == 3 || i2 == 2)) {
            rescheduleJobForFailureLocked.disallowRunInBatterySaverAndDoze();
        }
        if (!stopTrackingJobLocked(jobStatus, rescheduleJobForFailureLocked, !jobStatus.getJob().isPeriodic())) {
            if (DEBUG) {
                Slog.d(TAG, "Could not find job to remove. Was job removed while executing?");
            }
            JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(jobStatus.getUid(), jobStatus.getNamespace(), jobStatus.getJobId());
            if (jobByUidAndJobId != null) {
                this.mHandler.obtainMessage(0, jobByUidAndJobId).sendToTarget();
                return;
            }
            return;
        }
        if (rescheduleJobForFailureLocked != null) {
            try {
                rescheduleJobForFailureLocked.prepareLocked();
            } catch (SecurityException unused) {
                Slog.w(TAG, "Unable to regrant job permissions for " + rescheduleJobForFailureLocked);
            }
            startTrackingJobLocked(rescheduleJobForFailureLocked, jobStatus);
        } else if (jobStatus.getJob().isPeriodic()) {
            JobStatus rescheduleJobForPeriodic = getRescheduleJobForPeriodic(jobStatus);
            try {
                rescheduleJobForPeriodic.prepareLocked();
            } catch (SecurityException unused2) {
                Slog.w(TAG, "Unable to regrant job permissions for " + rescheduleJobForPeriodic);
            }
            startTrackingJobLocked(rescheduleJobForPeriodic, jobStatus);
        }
        jobStatus.unprepareLocked();
        reportActiveLocked();
    }

    @Override // com.android.server.job.StateChangedListener
    public void onControllerStateChanged(ArraySet<JobStatus> arraySet) {
        if (arraySet == null) {
            this.mHandler.obtainMessage(1).sendToTarget();
            synchronized (this.mPendingJobReasonCache) {
                this.mPendingJobReasonCache.clear();
            }
            return;
        }
        if (arraySet.size() > 0) {
            synchronized (this.mLock) {
                this.mChangedJobList.addAll((ArraySet<? extends JobStatus>) arraySet);
            }
            this.mHandler.obtainMessage(8).sendToTarget();
            synchronized (this.mPendingJobReasonCache) {
                for (int size = arraySet.size() - 1; size >= 0; size--) {
                    resetPendingJobReasonCache(arraySet.valueAt(size));
                }
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onRestrictionStateChanged(JobRestriction jobRestriction, boolean z) {
        this.mHandler.obtainMessage(1).sendToTarget();
        if (z) {
            synchronized (this.mLock) {
                this.mConcurrencyManager.maybeStopOvertimeJobsLocked(jobRestriction);
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onRunJobNow(JobStatus jobStatus) {
        if (jobStatus == null) {
            this.mHandler.obtainMessage(3).sendToTarget();
        } else {
            this.mHandler.obtainMessage(0, jobStatus).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class JobHandler extends Handler {
        public JobHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0011. Please report as an issue. */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (JobSchedulerService.this.mLock) {
                JobSchedulerService jobSchedulerService = JobSchedulerService.this;
                if (jobSchedulerService.mReadyToRock) {
                    boolean z = true;
                    switch (message.what) {
                        case 0:
                            JobStatus jobStatus = (JobStatus) message.obj;
                            if (jobStatus != null) {
                                if (jobSchedulerService.isReadyToBeExecutedLocked(jobStatus)) {
                                    JobSchedulerService.this.mJobPackageTracker.notePending(jobStatus);
                                    JobSchedulerService.this.mPendingJobQueue.add(jobStatus);
                                }
                                JobSchedulerService.this.mChangedJobList.remove(jobStatus);
                            } else {
                                Slog.e(JobSchedulerService.TAG, "Given null job to check individually");
                            }
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 1:
                            if (JobSchedulerService.DEBUG) {
                                Slog.d(JobSchedulerService.TAG, "MSG_CHECK_JOB");
                            }
                            JobSchedulerService jobSchedulerService2 = JobSchedulerService.this;
                            if (jobSchedulerService2.mReportedActive) {
                                jobSchedulerService2.queueReadyJobsForExecutionLocked();
                            } else {
                                jobSchedulerService2.maybeQueueReadyJobsForExecutionLocked();
                            }
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 2:
                            jobSchedulerService.cancelJobImplLocked((JobStatus) message.obj, null, message.arg1, 1, "app no longer allowed to run");
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 3:
                            if (JobSchedulerService.DEBUG) {
                                Slog.d(JobSchedulerService.TAG, "MSG_CHECK_JOB_GREEDY");
                            }
                            JobSchedulerService.this.queueReadyJobsForExecutionLocked();
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 4:
                            SomeArgs someArgs = (SomeArgs) message.obj;
                            jobSchedulerService.updateUidState(someArgs.argi1, someArgs.argi2, someArgs.argi3);
                            someArgs.recycle();
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 5:
                            int i = message.arg1;
                            if (message.arg2 == 0) {
                                z = false;
                            }
                            jobSchedulerService.updateUidState(i, 19, 0);
                            if (z) {
                                JobSchedulerService.this.cancelJobsForUid(i, true, 11, 1, "uid gone");
                            }
                            synchronized (JobSchedulerService.this.mLock) {
                                JobSchedulerService.this.mDeviceIdleJobsController.setUidActiveLocked(i, false);
                            }
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 6:
                            int i2 = message.arg1;
                            synchronized (jobSchedulerService.mLock) {
                                JobSchedulerService.this.mDeviceIdleJobsController.setUidActiveLocked(i2, true);
                            }
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 7:
                            int i3 = message.arg1;
                            if (message.arg2 == 0) {
                                z = false;
                            }
                            if (z) {
                                jobSchedulerService.cancelJobsForUid(i3, true, 11, 1, "app uid idle");
                            }
                            synchronized (JobSchedulerService.this.mLock) {
                                JobSchedulerService.this.mDeviceIdleJobsController.setUidActiveLocked(i3, false);
                            }
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 8:
                            if (JobSchedulerService.DEBUG) {
                                Slog.d(JobSchedulerService.TAG, "MSG_CHECK_CHANGED_JOB_LIST");
                            }
                            JobSchedulerService.this.checkChangedJobListLocked();
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 9:
                            SomeArgs someArgs2 = (SomeArgs) message.obj;
                            synchronized (jobSchedulerService.mLock) {
                                JobSchedulerService.this.updateMediaBackupExemptionLocked(someArgs2.argi1, (String) someArgs2.arg1, (String) someArgs2.arg2);
                            }
                            someArgs2.recycle();
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 10:
                            IUserVisibleJobObserver iUserVisibleJobObserver = (IUserVisibleJobObserver) message.obj;
                            synchronized (jobSchedulerService.mLock) {
                                for (int size = JobSchedulerService.this.mConcurrencyManager.mActiveServices.size() - 1; size >= 0; size--) {
                                    JobStatus runningJobLocked = JobSchedulerService.this.mConcurrencyManager.mActiveServices.get(size).getRunningJobLocked();
                                    if (runningJobLocked != null && runningJobLocked.isUserVisibleJob()) {
                                        try {
                                            iUserVisibleJobObserver.onUserVisibleJobStateChanged(runningJobLocked.getUserVisibleJobSummary(), true);
                                        } catch (RemoteException unused) {
                                        }
                                    }
                                }
                            }
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 11:
                            SomeArgs someArgs3 = (SomeArgs) message.obj;
                            UserVisibleJobSummary userVisibleJobSummary = ((JobStatus) someArgs3.arg2).getUserVisibleJobSummary();
                            boolean z2 = someArgs3.argi1 == 1;
                            for (int beginBroadcast = JobSchedulerService.this.mUserVisibleJobObservers.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                                try {
                                    JobSchedulerService.this.mUserVisibleJobObservers.getBroadcastItem(beginBroadcast).onUserVisibleJobStateChanged(userVisibleJobSummary, z2);
                                } catch (RemoteException unused2) {
                                }
                            }
                            JobSchedulerService.this.mUserVisibleJobObservers.finishBroadcast();
                            someArgs3.recycle();
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        default:
                            JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public JobRestriction checkIfRestricted(JobStatus jobStatus) {
        if (evaluateJobBiasLocked(jobStatus) >= 35) {
            return null;
        }
        for (int size = this.mJobRestrictions.size() - 1; size >= 0; size--) {
            JobRestriction jobRestriction = this.mJobRestrictions.get(size);
            if (jobRestriction.isJobRestricted(jobStatus)) {
                return jobRestriction;
            }
        }
        return null;
    }

    @GuardedBy({"mLock"})
    private void stopNonReadyActiveJobsLocked() {
        this.mConcurrencyManager.stopNonReadyActiveJobsLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void queueReadyJobsForExecutionLocked() {
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(8);
        this.mChangedJobList.clear();
        if (DEBUG) {
            Slog.d(TAG, "queuing all ready jobs for execution:");
        }
        clearPendingJobQueue();
        stopNonReadyActiveJobsLocked();
        this.mJobs.forEachJob(this.mReadyQueueFunctor);
        this.mReadyQueueFunctor.postProcessLocked();
        if (DEBUG) {
            int size = this.mPendingJobQueue.size();
            if (size == 0) {
                Slog.d(TAG, "No jobs pending.");
                return;
            }
            Slog.d(TAG, size + " jobs queued.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ReadyJobQueueFunctor implements Consumer<JobStatus> {
        final ArrayList<JobStatus> newReadyJobs = new ArrayList<>();

        ReadyJobQueueFunctor() {
        }

        @Override // java.util.function.Consumer
        public void accept(JobStatus jobStatus) {
            if (JobSchedulerService.this.isReadyToBeExecutedLocked(jobStatus)) {
                if (JobSchedulerService.DEBUG) {
                    Slog.d(JobSchedulerService.TAG, "    queued " + jobStatus.toShortString());
                }
                this.newReadyJobs.add(jobStatus);
                return;
            }
            JobSchedulerService.this.mJobSchedulerServiceExt.jobQueueFunctorNotAccept(jobStatus);
        }

        /* JADX INFO: Access modifiers changed from: private */
        @GuardedBy({"mLock"})
        public void postProcessLocked() {
            JobSchedulerService.this.noteJobsPending(this.newReadyJobs);
            JobSchedulerService.this.mPendingJobQueue.addAll(this.newReadyJobs);
            this.newReadyJobs.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class MaybeReadyJobQueueFunctor implements Consumer<JobStatus> {
        int forceBatchedCount;
        final List<JobStatus> runnableJobs = new ArrayList();
        int unbatchedCount;

        public MaybeReadyJobQueueFunctor() {
            reset();
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x00b7, code lost:
        
            if (r1.mPrefetchController.getNextEstimatedLaunchTimeLocked(r13) > (r3 + r1.mConstants.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS)) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00f5, code lost:
        
            if (r1 == false) goto L20;
         */
        @Override // java.util.function.Consumer
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void accept(JobStatus jobStatus) {
            String str;
            boolean isCurrentlyRunningLocked = JobSchedulerService.this.isCurrentlyRunningLocked(jobStatus);
            boolean z = false;
            int i = 6;
            if (JobSchedulerService.this.isReadyToBeExecutedLocked(jobStatus, false)) {
                if (JobSchedulerService.this.mActivityManagerInternal.isAppStartModeDisabled(jobStatus.getUid(), jobStatus.getJob().getService().getPackageName())) {
                    Slog.w(JobSchedulerService.TAG, "Aborting job " + jobStatus.getUid() + ":" + jobStatus.getJob().toString() + " -- package not allowed to start");
                    if (isCurrentlyRunningLocked) {
                        JobSchedulerService.this.mHandler.obtainMessage(2, 11, 0, jobStatus).sendToTarget();
                        return;
                    } else {
                        if (JobSchedulerService.this.mPendingJobQueue.remove(jobStatus)) {
                            JobSchedulerService.this.noteJobNonPending(jobStatus);
                            return;
                        }
                        return;
                    }
                }
                if (!jobStatus.shouldTreatAsExpeditedJob() && !jobStatus.shouldTreatAsUserInitiatedJob()) {
                    if (jobStatus.getEffectiveStandbyBucket() != 5) {
                        if (jobStatus.getJob().isPrefetch()) {
                            long millis = JobSchedulerService.sSystemClock.millis();
                            JobSchedulerService jobSchedulerService = JobSchedulerService.this;
                        } else if (jobStatus.getNumPreviousAttempts() <= 0) {
                            boolean z2 = jobStatus.getFirstForceBatchedTimeElapsed() > 0 && JobSchedulerService.sElapsedRealtimeClock.millis() - jobStatus.getFirstForceBatchedTimeElapsed() >= JobSchedulerService.this.mConstants.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS;
                            if (JobSchedulerService.this.mConstants.MIN_READY_NON_ACTIVE_JOBS_COUNT > 1) {
                                if (jobStatus.getEffectiveStandbyBucket() != 0) {
                                    if (jobStatus.getEffectiveStandbyBucket() != 6) {
                                    }
                                }
                            }
                        }
                    }
                    z = true;
                }
                if (z) {
                    this.forceBatchedCount++;
                    if (jobStatus.getFirstForceBatchedTimeElapsed() == 0) {
                        jobStatus.setFirstForceBatchedTimeElapsed(JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                } else {
                    this.unbatchedCount++;
                    JobSchedulerService.this.mJobSchedulerServiceExt.acceptForMaybeReadyJobQueueFunctor(jobStatus);
                }
                if (isCurrentlyRunningLocked) {
                    return;
                }
                this.runnableJobs.add(jobStatus);
                return;
            }
            if (isCurrentlyRunningLocked) {
                if (!jobStatus.isReady()) {
                    if (jobStatus.getEffectiveStandbyBucket() == 5 && jobStatus.getStopReason() == 12) {
                        str = "cancelled due to restricted bucket";
                    } else {
                        str = "cancelled due to unsatisfied constraints";
                        i = 1;
                    }
                } else {
                    JobRestriction checkIfRestricted = JobSchedulerService.this.checkIfRestricted(jobStatus);
                    if (checkIfRestricted != null) {
                        i = checkIfRestricted.getInternalReason();
                        str = "restricted due to " + JobParameters.getInternalReasonCodeDescription(i);
                    } else {
                        i = -1;
                        str = "couldn't figure out why the job should stop running";
                    }
                }
                JobSchedulerService.this.mConcurrencyManager.stopJobOnServiceContextLocked(jobStatus, jobStatus.getStopReason(), i, str);
            } else if (JobSchedulerService.this.mPendingJobQueue.remove(jobStatus)) {
                JobSchedulerService.this.noteJobNonPending(jobStatus);
            }
            JobSchedulerService.this.mJobSchedulerServiceExt.jobQueueFunctorNotAccept(jobStatus);
        }

        @GuardedBy({"mLock"})
        @VisibleForTesting
        void postProcessLocked() {
            if (this.unbatchedCount > 0 || JobSchedulerService.this.mJobSchedulerServiceExt.readyForPostProcess() || this.forceBatchedCount >= JobSchedulerService.this.mConstants.MIN_READY_NON_ACTIVE_JOBS_COUNT) {
                if (JobSchedulerService.DEBUG) {
                    Slog.d(JobSchedulerService.TAG, "maybeQueueReadyJobsForExecutionLocked: Running jobs.");
                }
                List<JobStatus> list = this.runnableJobs;
                if (list != null) {
                    JobSchedulerService.this.noteJobsPending(list);
                    JobSchedulerService.this.mPendingJobQueue.addAll(this.runnableJobs);
                }
            } else {
                if (JobSchedulerService.DEBUG) {
                    Slog.d(JobSchedulerService.TAG, "maybeQueueReadyJobsForExecutionLocked: Not running anything.");
                }
                int size = this.runnableJobs.size();
                if (size > 0) {
                    synchronized (JobSchedulerService.this.mPendingJobReasonCache) {
                        for (int i = 0; i < size; i++) {
                            JobStatus jobStatus = this.runnableJobs.get(i);
                            SparseIntArray sparseIntArray = (SparseIntArray) JobSchedulerService.this.mPendingJobReasonCache.get(jobStatus.getUid(), jobStatus.getNamespace());
                            if (sparseIntArray == null) {
                                sparseIntArray = new SparseIntArray();
                                JobSchedulerService.this.mPendingJobReasonCache.add(jobStatus.getUid(), jobStatus.getNamespace(), sparseIntArray);
                            }
                            sparseIntArray.put(jobStatus.getJobId(), 13);
                        }
                    }
                }
            }
            reset();
        }

        @VisibleForTesting
        void reset() {
            this.forceBatchedCount = 0;
            this.unbatchedCount = 0;
            JobSchedulerService.this.mJobSchedulerServiceExt.resetForMaybeReadyJobQueueFunctor();
            this.runnableJobs.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void maybeQueueReadyJobsForExecutionLocked() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(8);
        this.mChangedJobList.clear();
        if (DEBUG) {
            Slog.d(TAG, "Maybe queuing ready jobs...");
        }
        clearPendingJobQueue();
        stopNonReadyActiveJobsLocked();
        this.mJobs.forEachJob(this.mMaybeQueueFunctor);
        this.mMaybeQueueFunctor.postProcessLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void checkChangedJobListLocked() {
        this.mHandler.removeMessages(8);
        if (DEBUG) {
            Slog.d(TAG, "Check changed jobs...");
        }
        if (this.mChangedJobList.size() == 0) {
            return;
        }
        this.mChangedJobList.forEach(this.mMaybeQueueFunctor);
        this.mMaybeQueueFunctor.postProcessLocked();
        this.mChangedJobList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void updateMediaBackupExemptionLocked(final int i, final String str, final String str2) {
        this.mJobs.forEachJob(new Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateMediaBackupExemptionLocked$5;
                lambda$updateMediaBackupExemptionLocked$5 = JobSchedulerService.lambda$updateMediaBackupExemptionLocked$5(i, str, str2, (JobStatus) obj);
                return lambda$updateMediaBackupExemptionLocked$5;
            }
        }, new Consumer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                JobSchedulerService.this.lambda$updateMediaBackupExemptionLocked$6((JobStatus) obj);
            }
        });
        this.mHandler.sendEmptyMessage(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateMediaBackupExemptionLocked$5(int i, String str, String str2, JobStatus jobStatus) {
        return jobStatus.getSourceUserId() == i && (jobStatus.getSourcePackageName().equals(str) || jobStatus.getSourcePackageName().equals(str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMediaBackupExemptionLocked$6(JobStatus jobStatus) {
        if (jobStatus.updateMediaBackupExemptionStatus()) {
            this.mChangedJobList.add(jobStatus);
        }
    }

    @GuardedBy({"mLock"})
    public boolean areUsersStartedLocked(JobStatus jobStatus) {
        boolean contains = ArrayUtils.contains(this.mStartedUsers, jobStatus.getSourceUserId());
        if (this.mJobSchedulerServiceExt.checkIdleJobNotUserStatus(jobStatus)) {
            return true;
        }
        return jobStatus.getUserId() == jobStatus.getSourceUserId() ? contains : contains && ArrayUtils.contains(this.mStartedUsers, jobStatus.getUserId());
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    boolean isReadyToBeExecutedLocked(JobStatus jobStatus) {
        return isReadyToBeExecutedLocked(jobStatus, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isReadyToBeExecutedLocked(JobStatus jobStatus, boolean z) {
        boolean z2 = jobStatus.isReady() || evaluateControllerStatesLocked(jobStatus);
        if (DEBUG) {
            Slog.v(TAG, "isReadyToBeExecutedLocked: " + jobStatus.toShortString() + " ready=" + z2);
        }
        if (!z2) {
            if (jobStatus.getSourcePackageName().equals("android.jobscheduler.cts.jobtestapp")) {
                Slog.v(TAG, "    NOT READY: " + jobStatus);
            }
            return false;
        }
        boolean containsJob = this.mJobs.containsJob(jobStatus);
        boolean areUsersStartedLocked = areUsersStartedLocked(jobStatus);
        boolean z3 = this.mBackingUpUids.get(jobStatus.getSourceUid());
        if (DEBUG) {
            Slog.v(TAG, "isReadyToBeExecutedLocked: " + jobStatus.toShortString() + " exists=" + containsJob + " userStarted=" + areUsersStartedLocked + " backingUp=" + z3);
        }
        if (!containsJob || !areUsersStartedLocked || z3 || checkIfRestricted(jobStatus) != null) {
            return false;
        }
        boolean contains = this.mPendingJobQueue.contains(jobStatus);
        boolean z4 = z && this.mConcurrencyManager.isJobRunningLocked(jobStatus);
        if (DEBUG) {
            Slog.v(TAG, "isReadyToBeExecutedLocked: " + jobStatus.toShortString() + " pending=" + contains + " active=" + z4);
        }
        if (contains || z4 || !this.mJobSchedulerServiceExt.isReadyToBeExecuted(jobStatus)) {
            return false;
        }
        return this.mJobSchedulerServiceExt.isComponentUsable(jobStatus, isComponentUsable(jobStatus));
    }

    private boolean isComponentUsable(JobStatus jobStatus) {
        String str = jobStatus.serviceProcessName;
        if (str == null) {
            if (!DEBUG) {
                return false;
            }
            Slog.v(TAG, "isComponentUsable: " + jobStatus.toShortString() + " component not present");
            return false;
        }
        boolean isAppBad = this.mActivityManagerInternal.isAppBad(str, jobStatus.getUid());
        if (DEBUG && isAppBad) {
            Slog.i(TAG, "App is bad for " + jobStatus.toShortString() + " so not runnable");
        }
        return !isAppBad;
    }

    @VisibleForTesting
    boolean evaluateControllerStatesLocked(JobStatus jobStatus) {
        for (int size = this.mControllers.size() - 1; size >= 0; size--) {
            this.mControllers.get(size).evaluateStateLocked(jobStatus);
        }
        return jobStatus.isReady();
    }

    public boolean areComponentsInPlaceLocked(JobStatus jobStatus) {
        boolean containsJob = this.mJobs.containsJob(jobStatus);
        boolean areUsersStartedLocked = areUsersStartedLocked(jobStatus);
        boolean z = this.mBackingUpUids.get(jobStatus.getSourceUid());
        if (DEBUG) {
            Slog.v(TAG, "areComponentsInPlaceLocked: " + jobStatus.toShortString() + " exists=" + containsJob + " userStarted=" + areUsersStartedLocked + " backingUp=" + z);
        }
        if (!containsJob || !areUsersStartedLocked || z) {
            return false;
        }
        JobRestriction checkIfRestricted = checkIfRestricted(jobStatus);
        if (checkIfRestricted != null) {
            if (DEBUG) {
                Slog.v(TAG, "areComponentsInPlaceLocked: " + jobStatus.toShortString() + " restricted due to " + checkIfRestricted.getInternalReason());
            }
            return false;
        }
        return isComponentUsable(jobStatus);
    }

    public long getMinJobExecutionGuaranteeMs(JobStatus jobStatus) {
        long min;
        long j;
        synchronized (this.mLock) {
            if (jobStatus.shouldTreatAsUserInitiatedJob() && checkRunUserInitiatedJobsPermission(jobStatus.getSourceUid(), jobStatus.getSourcePackageName())) {
                if (this.mQuotaTracker.isWithinQuota(jobStatus.getTimeoutBlameUserId(), jobStatus.getTimeoutBlamePackageName(), QUOTA_TRACKER_TIMEOUT_UIJ_TAG)) {
                    j = this.mConstants.RUNTIME_UI_LIMIT_MS;
                } else {
                    j = this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
                }
                if (jobStatus.getJob().getRequiredNetwork() != null) {
                    Constants constants = this.mConstants;
                    if (constants.RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS) {
                        if (this.mConnectivityController.getEstimatedTransferTimeMs(jobStatus) == -1) {
                            return Math.min(j, this.mConstants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS);
                        }
                        return Math.min(j, Math.max(((float) r3) * r7.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, this.mConstants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS));
                    }
                    return Math.min(j, Math.max(constants.RUNTIME_MIN_UI_GUARANTEE_MS, constants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS));
                }
                return Math.min(j, this.mConstants.RUNTIME_MIN_UI_GUARANTEE_MS);
            }
            if (jobStatus.shouldTreatAsExpeditedJob()) {
                if (jobStatus.getEffectiveStandbyBucket() != 5) {
                    min = this.mConstants.RUNTIME_MIN_EJ_GUARANTEE_MS;
                } else {
                    min = Math.min(this.mConstants.RUNTIME_MIN_EJ_GUARANTEE_MS, 300000L);
                }
                return min;
            }
            return this.mConstants.RUNTIME_MIN_GUARANTEE_MS;
        }
    }

    public long getMaxJobExecutionTimeMs(JobStatus jobStatus) {
        long j;
        long maxJobExecutionTimeMsLocked;
        synchronized (this.mLock) {
            if (jobStatus.shouldTreatAsUserInitiatedJob() && checkRunUserInitiatedJobsPermission(jobStatus.getSourceUid(), jobStatus.getSourcePackageName()) && this.mQuotaTracker.isWithinQuota(jobStatus.getTimeoutBlameUserId(), jobStatus.getTimeoutBlamePackageName(), QUOTA_TRACKER_TIMEOUT_UIJ_TAG)) {
                return this.mConstants.RUNTIME_UI_LIMIT_MS;
            }
            if (jobStatus.shouldTreatAsUserInitiatedJob()) {
                return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            }
            if (this.mQuotaTracker.isWithinQuota(jobStatus.getTimeoutBlameUserId(), jobStatus.getTimeoutBlamePackageName(), jobStatus.shouldTreatAsExpeditedJob() ? QUOTA_TRACKER_TIMEOUT_EJ_TAG : QUOTA_TRACKER_TIMEOUT_REG_TAG)) {
                j = this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            } else {
                j = this.mConstants.RUNTIME_MIN_GUARANTEE_MS;
            }
            if (this.mConstants.USE_TARE_POLICY) {
                maxJobExecutionTimeMsLocked = this.mTareController.getMaxJobExecutionTimeMsLocked(jobStatus);
            } else {
                maxJobExecutionTimeMsLocked = this.mQuotaController.getMaxJobExecutionTimeMsLocked(jobStatus);
            }
            return Math.min(j, maxJobExecutionTimeMsLocked);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void maybeRunPendingJobsLocked() {
        if (DEBUG) {
            Slog.d(TAG, "pending queue: " + this.mPendingJobQueue.size() + " jobs.");
        }
        this.mConcurrencyManager.assignJobsToContextsLocked();
        reportActiveLocked();
    }

    private int adjustJobBias(int i, JobStatus jobStatus) {
        if (i >= 40) {
            return i;
        }
        float loadFactor = this.mJobPackageTracker.getLoadFactor(jobStatus);
        Constants constants = this.mConstants;
        return loadFactor >= constants.HEAVY_USE_FACTOR ? i - 80 : loadFactor >= constants.MODERATE_USE_FACTOR ? i - 40 : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int evaluateJobBiasLocked(JobStatus jobStatus) {
        int bias = jobStatus.getBias();
        if (bias >= 30) {
            return adjustJobBias(bias, jobStatus);
        }
        int i = this.mUidBiasOverride.get(jobStatus.getSourceUid(), 0);
        if (i != 0) {
            return adjustJobBias(i, jobStatus);
        }
        return adjustJobBias(bias, jobStatus);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void informObserversOfUserVisibleJobChange(JobServiceContext jobServiceContext, JobStatus jobStatus, boolean z) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = jobServiceContext;
        obtain.arg2 = jobStatus;
        obtain.argi1 = z ? 1 : 0;
        this.mHandler.obtainMessage(11, obtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class BatteryStateTracker extends BroadcastReceiver {
        private boolean mBatteryNotLow;
        private boolean mCharging;
        private int mLastBatterySeq = -1;
        private BroadcastReceiver mMonitor;

        BatteryStateTracker() {
        }

        public void startTracking() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.BATTERY_LOW");
            intentFilter.addAction("android.intent.action.BATTERY_OKAY");
            intentFilter.addAction("android.os.action.CHARGING");
            intentFilter.addAction("android.os.action.DISCHARGING");
            JobSchedulerService.this.getTestableContext().registerReceiver(this, intentFilter);
            BatteryManagerInternal batteryManagerInternal = (BatteryManagerInternal) LocalServices.getService(BatteryManagerInternal.class);
            this.mBatteryNotLow = !batteryManagerInternal.getBatteryLevelLow();
            try {
                this.mCharging = BatteryStatsService.getService().isCharging();
            } catch (RemoteException unused) {
                Slog.e(JobSchedulerService.TAG, "mBatteryStats.isCharging() error occurred");
                this.mCharging = batteryManagerInternal.isPowered(15);
            }
        }

        public void setMonitorBatteryLocked(boolean z) {
            if (z) {
                if (this.mMonitor == null) {
                    this.mMonitor = new BroadcastReceiver() { // from class: com.android.server.job.JobSchedulerService.BatteryStateTracker.1
                        @Override // android.content.BroadcastReceiver
                        public void onReceive(Context context, Intent intent) {
                            BatteryStateTracker.this.onReceiveInternal(intent);
                        }
                    };
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
                    JobSchedulerService.this.getTestableContext().registerReceiver(this.mMonitor, intentFilter);
                    return;
                }
                return;
            }
            if (this.mMonitor != null) {
                JobSchedulerService.this.getTestableContext().unregisterReceiver(this.mMonitor);
                this.mMonitor = null;
            }
        }

        public boolean isCharging() {
            return this.mCharging;
        }

        public boolean isBatteryNotLow() {
            return this.mBatteryNotLow;
        }

        public boolean isMonitoring() {
            return this.mMonitor != null;
        }

        public int getSeq() {
            return this.mLastBatterySeq;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            onReceiveInternal(intent);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00d7 A[Catch: all -> 0x00f4, TryCatch #0 {, blocks: (B:4:0x0005, B:6:0x0013, B:8:0x0017, B:9:0x0033, B:11:0x0037, B:13:0x00ca, B:15:0x00d7, B:17:0x00e2, B:19:0x00f2, B:23:0x003c, B:25:0x0044, B:27:0x0048, B:28:0x0064, B:30:0x0068, B:31:0x006b, B:33:0x0073, B:35:0x0077, B:36:0x0093, B:38:0x0097, B:39:0x009a, B:41:0x00a2, B:43:0x00a6, B:44:0x00c2, B:46:0x00c6), top: B:3:0x0005 }] */
        @VisibleForTesting
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onReceiveInternal(Intent intent) {
            synchronized (JobSchedulerService.this.mLock) {
                String action = intent.getAction();
                boolean z = false;
                if ("android.intent.action.BATTERY_LOW".equals(action)) {
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Battery life too low @ " + JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (this.mBatteryNotLow) {
                        this.mBatteryNotLow = false;
                        z = true;
                    }
                    this.mLastBatterySeq = intent.getIntExtra(DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mLastBatterySeq);
                    if (z) {
                        for (int size = JobSchedulerService.this.mControllers.size() - 1; size >= 0; size--) {
                            JobSchedulerService.this.mControllers.get(size).onBatteryStateChangedLocked();
                        }
                    }
                } else if ("android.intent.action.BATTERY_OKAY".equals(action)) {
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Battery high enough @ " + JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (!this.mBatteryNotLow) {
                        this.mBatteryNotLow = true;
                        z = true;
                    }
                    this.mLastBatterySeq = intent.getIntExtra(DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mLastBatterySeq);
                    if (z) {
                    }
                } else if ("android.os.action.CHARGING".equals(action)) {
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Battery charging @ " + JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (!this.mCharging) {
                        this.mCharging = true;
                        z = true;
                    }
                    this.mLastBatterySeq = intent.getIntExtra(DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mLastBatterySeq);
                    if (z) {
                    }
                } else {
                    if ("android.os.action.DISCHARGING".equals(action)) {
                        if (JobSchedulerService.DEBUG) {
                            Slog.d(JobSchedulerService.TAG, "Battery discharging @ " + JobSchedulerService.sElapsedRealtimeClock.millis());
                        }
                        if (this.mCharging) {
                            this.mCharging = false;
                            z = true;
                        }
                    }
                    this.mLastBatterySeq = intent.getIntExtra(DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mLastBatterySeq);
                    if (z) {
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class LocalService implements JobSchedulerInternal {
        LocalService() {
        }

        public List<JobInfo> getSystemScheduledOwnJobs(final String str) {
            final ArrayList arrayList;
            synchronized (JobSchedulerService.this.mLock) {
                arrayList = new ArrayList();
                JobSchedulerService.this.mJobs.forEachJob(1000, new Consumer() { // from class: com.android.server.job.JobSchedulerService$LocalService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        JobSchedulerService.LocalService.lambda$getSystemScheduledOwnJobs$0(str, arrayList, (JobStatus) obj);
                    }
                });
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getSystemScheduledOwnJobs$0(String str, List list, JobStatus jobStatus) {
            if (jobStatus.getSourceUid() == 1000 && Objects.equals(jobStatus.getNamespace(), str) && PackageManagerService.PLATFORM_PACKAGE_NAME.equals(jobStatus.getSourcePackageName())) {
                list.add(jobStatus.getJob());
            }
        }

        public void cancelJobsForUid(int i, boolean z, int i2, int i3, String str) {
            JobSchedulerService.this.cancelJobsForUid(i, z, i2, i3, str);
        }

        public void addBackingUpUid(int i) {
            synchronized (JobSchedulerService.this.mLock) {
                JobSchedulerService.this.mBackingUpUids.put(i, true);
            }
        }

        public void removeBackingUpUid(int i) {
            synchronized (JobSchedulerService.this.mLock) {
                JobSchedulerService.this.mBackingUpUids.delete(i);
                if (JobSchedulerService.this.mJobs.countJobsForUid(i) > 0) {
                    JobSchedulerService.this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }

        public void clearAllBackingUpUids() {
            synchronized (JobSchedulerService.this.mLock) {
                if (JobSchedulerService.this.mBackingUpUids.size() > 0) {
                    JobSchedulerService.this.mBackingUpUids.clear();
                    JobSchedulerService.this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }

        public String getCloudMediaProviderPackage(int i) {
            return (String) JobSchedulerService.this.mCloudMediaProviderPackages.get(i);
        }

        public void reportAppUsage(String str, int i) {
            JobSchedulerService.this.reportAppUsage(str, i);
        }

        public boolean isAppConsideredBuggy(int i, String str, int i2, String str2) {
            return (JobSchedulerService.this.mQuotaTracker.isWithinQuota(i, str, JobSchedulerService.QUOTA_TRACKER_ANR_TAG) && JobSchedulerService.this.mQuotaTracker.isWithinQuota(i, str, JobSchedulerService.QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG) && JobSchedulerService.this.mQuotaTracker.isWithinQuota(i2, str2, JobSchedulerService.QUOTA_TRACKER_TIMEOUT_TOTAL_TAG)) ? false : true;
        }

        public boolean isNotificationAssociatedWithAnyUserInitiatedJobs(int i, int i2, String str) {
            if (str == null) {
                return false;
            }
            return JobSchedulerService.this.mConcurrencyManager.isNotificationAssociatedWithAnyUserInitiatedJobs(i, i2, str);
        }

        public boolean isNotificationChannelAssociatedWithAnyUserInitiatedJobs(String str, int i, String str2) {
            if (str2 == null || str == null) {
                return false;
            }
            return JobSchedulerService.this.mConcurrencyManager.isNotificationChannelAssociatedWithAnyUserInitiatedJobs(str, i, str2);
        }

        public JobSchedulerInternal.JobStorePersistStats getPersistStats() {
            JobSchedulerInternal.JobStorePersistStats jobStorePersistStats;
            synchronized (JobSchedulerService.this.mLock) {
                jobStorePersistStats = new JobSchedulerInternal.JobStorePersistStats(JobSchedulerService.this.mJobs.getPersistStats());
            }
            return jobStorePersistStats;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class StandbyTracker extends AppStandbyInternal.AppIdleStateChangeListener {
        public void onAppIdleStateChanged(String str, int i, boolean z, int i2, int i3) {
        }

        StandbyTracker() {
        }

        public void onUserInteractionStarted(String str, int i) {
            int packageUid = JobSchedulerService.this.mLocalPM.getPackageUid(str, 8192L, i);
            if (packageUid < 0) {
                return;
            }
            long timeSinceLastJobRun = JobSchedulerService.this.mUsageStats.getTimeSinceLastJobRun(str, i);
            long j = timeSinceLastJobRun > 172800000 ? 0L : timeSinceLastJobRun;
            DeferredJobCounter deferredJobCounter = new DeferredJobCounter();
            synchronized (JobSchedulerService.this.mLock) {
                JobSchedulerService.this.mJobs.forEachJobForSourceUid(packageUid, deferredJobCounter);
            }
            if (deferredJobCounter.numDeferred() > 0 || j > 0) {
                ((BatteryStatsInternal) LocalServices.getService(BatteryStatsInternal.class)).noteJobsDeferred(packageUid, deferredJobCounter.numDeferred(), j);
                FrameworkStatsLog.write_non_chained(85, packageUid, (String) null, deferredJobCounter.numDeferred(), j);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class DeferredJobCounter implements Consumer<JobStatus> {
        private int mDeferred = 0;

        DeferredJobCounter() {
        }

        public int numDeferred() {
            return this.mDeferred;
        }

        @Override // java.util.function.Consumer
        public void accept(JobStatus jobStatus) {
            if (jobStatus.getWhenStandbyDeferred() > 0) {
                this.mDeferred++;
            }
        }
    }

    public static int standbyBucketForPackage(String str, int i, long j) {
        UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        int standbyBucketToBucketIndex = standbyBucketToBucketIndex(usageStatsManagerInternal != null ? usageStatsManagerInternal.getAppStandbyBucket(str, i, j) : 0);
        if (DEBUG_STANDBY) {
            Slog.v(TAG, str + SliceClientPermissions.SliceAuthority.DELIMITER + i + " standby bucket index: " + standbyBucketToBucketIndex);
        }
        return standbyBucketToBucketIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int safelyScaleBytesToKBForHistogram(long j) {
        long j2 = j / 1000;
        if (j2 > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j2 < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class CloudProviderChangeListener implements StorageManagerInternal.CloudProviderChangeListener {
        private CloudProviderChangeListener() {
        }

        public void onCloudProviderChanged(int i, String str) {
            ProviderInfo resolveContentProvider = JobSchedulerService.this.getContext().createContextAsUser(UserHandle.of(i), 0).getPackageManager().resolveContentProvider(str, PackageManager.ComponentInfoFlags.of(0L));
            String str2 = resolveContentProvider == null ? null : resolveContentProvider.packageName;
            synchronized (JobSchedulerService.this.mLock) {
                String str3 = (String) JobSchedulerService.this.mCloudMediaProviderPackages.get(i);
                if (!Objects.equals(str3, str2)) {
                    if (JobSchedulerService.DEBUG) {
                        Slog.d(JobSchedulerService.TAG, "Cloud provider of user " + i + " changed from " + str3 + " to " + str2);
                    }
                    JobSchedulerService.this.mCloudMediaProviderPackages.put(i, str2);
                    SomeArgs obtain = SomeArgs.obtain();
                    obtain.argi1 = i;
                    obtain.arg1 = str3;
                    obtain.arg2 = str2;
                    JobSchedulerService.this.mHandler.obtainMessage(9, obtain).sendToTarget();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasPermission(int i, int i2, String str) {
        synchronized (this.mPermissionCache) {
            SparseArrayMap<String, Boolean> sparseArrayMap = this.mPermissionCache.get(i);
            if (sparseArrayMap == null) {
                sparseArrayMap = new SparseArrayMap<>();
                this.mPermissionCache.put(i, sparseArrayMap);
            }
            Boolean bool = (Boolean) sparseArrayMap.get(i2, str);
            if (bool != null) {
                return bool.booleanValue();
            }
            boolean z = getContext().checkPermission(str, i2, i) == 0;
            sparseArrayMap.add(i2, str, Boolean.valueOf(z));
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class JobSchedulerStub extends IJobScheduler.Stub {
        JobSchedulerStub() {
        }

        private void enforceValidJobRequest(int i, int i2, JobInfo jobInfo) {
            PackageManager packageManager = JobSchedulerService.this.getContext().createContextAsUser(UserHandle.getUserHandleForUid(i), 0).getPackageManager();
            ComponentName service = jobInfo.getService();
            try {
                ServiceInfo serviceInfo = packageManager.getServiceInfo(service, 786432);
                if (serviceInfo == null) {
                    throw new IllegalArgumentException("No such service " + service);
                }
                if (serviceInfo.applicationInfo.uid != i) {
                    throw new IllegalArgumentException("uid " + i + " cannot schedule job in " + service.getPackageName());
                }
                if (!"android.permission.BIND_JOB_SERVICE".equals(serviceInfo.permission)) {
                    throw new IllegalArgumentException("Scheduled service " + service + " does not require android.permission.BIND_JOB_SERVICE permission");
                }
                if (jobInfo.isPersisted() && !canPersistJobs(i2, i)) {
                    throw new IllegalArgumentException("Requested job cannot be persisted without holding android.permission.RECEIVE_BOOT_COMPLETED permission");
                }
                if (jobInfo.getRequiredNetwork() != null && CompatChanges.isChangeEnabled(JobSchedulerService.REQUIRE_NETWORK_PERMISSIONS_FOR_CONNECTIVITY_JOBS, i) && !JobSchedulerService.this.hasPermission(i, i2, "android.permission.ACCESS_NETWORK_STATE")) {
                    throw new SecurityException("android.permission.ACCESS_NETWORK_STATE required for jobs with a connectivity constraint");
                }
            } catch (PackageManager.NameNotFoundException unused) {
                throw new IllegalArgumentException("Tried to schedule job for non-existent component: " + service);
            }
        }

        private boolean canPersistJobs(int i, int i2) {
            return JobSchedulerService.this.hasPermission(i2, i, "android.permission.RECEIVE_BOOT_COMPLETED");
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0077  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x009b A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x009c  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x008c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private int validateJob(JobInfo jobInfo, int i, int i2, int i3, String str, JobWorkItem jobWorkItem) {
            int i4;
            String packageName;
            boolean z;
            int validateRunUserInitiatedJobsPermission;
            boolean isInStateToScheduleUserInitiatedJobs;
            boolean isChangeEnabled = CompatChanges.isChangeEnabled(253665015L, i);
            jobInfo.enforceValidity(CompatChanges.isChangeEnabled(194532703L, i), isChangeEnabled);
            if ((jobInfo.getFlags() & 1) != 0) {
                JobSchedulerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONNECTIVITY_INTERNAL", JobSchedulerService.TAG);
            }
            if ((jobInfo.getFlags() & 8) != 0) {
                if (i != 1000) {
                    throw new SecurityException("Job has invalid flags");
                }
                if (jobInfo.isPeriodic()) {
                    Slog.wtf(JobSchedulerService.TAG, "Periodic jobs mustn't have FLAG_EXEMPT_FROM_APP_STANDBY. Job=" + jobInfo);
                }
            }
            if (jobInfo.isUserInitiated()) {
                int i5 = -1;
                if (i3 != -1 && str != null) {
                    try {
                        i4 = AppGlobals.getPackageManager().getPackageUid(str, 0L, i3);
                    } catch (RemoteException unused) {
                    }
                    packageName = jobInfo.getService().getPackageName();
                    if (i4 == -1) {
                        int validateRunUserInitiatedJobsPermission2 = validateRunUserInitiatedJobsPermission(i4, str);
                        if (validateRunUserInitiatedJobsPermission2 != 1) {
                            return validateRunUserInitiatedJobsPermission2;
                        }
                        if (i == i4 && packageName.equals(str)) {
                            i5 = i2;
                        }
                        z = isInStateToScheduleUserInitiatedJobs(i4, i5, str);
                    } else {
                        z = false;
                    }
                    if (i == i4 || !packageName.equals(str)) {
                        validateRunUserInitiatedJobsPermission = validateRunUserInitiatedJobsPermission(i, packageName);
                        if (validateRunUserInitiatedJobsPermission == 1) {
                            return validateRunUserInitiatedJobsPermission;
                        }
                        if (!z) {
                            isInStateToScheduleUserInitiatedJobs = isInStateToScheduleUserInitiatedJobs(i, i2, packageName);
                            if (!z && !isInStateToScheduleUserInitiatedJobs) {
                                Slog.e(JobSchedulerService.TAG, "Uid(s) " + i4 + SliceClientPermissions.SliceAuthority.DELIMITER + i + " not in a state to schedule user-initiated jobs");
                                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_invalid_state", i);
                                return 0;
                            }
                        }
                    }
                    isInStateToScheduleUserInitiatedJobs = false;
                    if (!z) {
                        Slog.e(JobSchedulerService.TAG, "Uid(s) " + i4 + SliceClientPermissions.SliceAuthority.DELIMITER + i + " not in a state to schedule user-initiated jobs");
                        Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_invalid_state", i);
                        return 0;
                    }
                }
                i4 = -1;
                packageName = jobInfo.getService().getPackageName();
                if (i4 == -1) {
                }
                if (i == i4) {
                }
                validateRunUserInitiatedJobsPermission = validateRunUserInitiatedJobsPermission(i, packageName);
                if (validateRunUserInitiatedJobsPermission == 1) {
                }
            }
            if (jobWorkItem != null) {
                jobWorkItem.enforceValidity(isChangeEnabled);
                if ((jobWorkItem.getEstimatedNetworkDownloadBytes() != -1 || jobWorkItem.getEstimatedNetworkUploadBytes() != -1 || jobWorkItem.getMinimumNetworkChunkBytes() != -1) && jobInfo.getRequiredNetwork() == null) {
                    if (CompatChanges.isChangeEnabled(JobSchedulerService.REQUIRE_NETWORK_CONSTRAINT_FOR_NETWORK_JOB_WORK_ITEMS, i)) {
                        throw new IllegalArgumentException("JobWorkItem implies network usage but job doesn't specify a network constraint");
                    }
                    Slog.e(JobSchedulerService.TAG, "JobWorkItem implies network usage but job doesn't specify a network constraint");
                }
                if (jobInfo.isPersisted() && jobWorkItem.getIntent() != null) {
                    throw new IllegalArgumentException("Cannot persist JobWorkItems with Intents");
                }
            }
            JobSchedulerService.this.mJobSchedulerServiceExt.checkOplusPermission(jobInfo, Binder.getCallingPid(), i);
            return 1;
        }

        private String validateNamespace(String str) {
            String sanitizeNamespace = JobScheduler.sanitizeNamespace(str);
            if (sanitizeNamespace == null) {
                return sanitizeNamespace;
            }
            if (sanitizeNamespace.isEmpty()) {
                throw new IllegalArgumentException("namespace cannot be empty");
            }
            if (sanitizeNamespace.length() > 1000) {
                throw new IllegalArgumentException("namespace cannot be more than 1000 characters");
            }
            return sanitizeNamespace.intern();
        }

        private int validateRunUserInitiatedJobsPermission(int i, String str) {
            int runUserInitiatedJobsPermissionState = JobSchedulerService.this.getRunUserInitiatedJobsPermissionState(i, str);
            if (runUserInitiatedJobsPermissionState == 2) {
                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_no_permission", i);
                throw new SecurityException("android.permission.RUN_USER_INITIATED_JOBS required to schedule user-initiated jobs.");
            }
            if (runUserInitiatedJobsPermissionState != 1) {
                return 1;
            }
            Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_no_permission", i);
            return 0;
        }

        private boolean isInStateToScheduleUserInitiatedJobs(int i, int i2, String str) {
            int uidProcessState = JobSchedulerService.this.mActivityManagerInternal.getUidProcessState(i);
            if (JobSchedulerService.DEBUG) {
                Slog.d(JobSchedulerService.TAG, "Uid " + i + " proc state=" + ActivityManager.procStateToString(uidProcessState));
            }
            if (uidProcessState == 2) {
                return true;
            }
            boolean canScheduleUserInitiatedJobs = JobSchedulerService.this.mActivityManagerInternal.canScheduleUserInitiatedJobs(i, i2, str);
            if (JobSchedulerService.DEBUG) {
                Slog.d(JobSchedulerService.TAG, "Uid " + i + " AM.canScheduleUserInitiatedJobs= " + canScheduleUserInitiatedJobs);
            }
            return canScheduleUserInitiatedJobs;
        }

        public int schedule(String str, JobInfo jobInfo) throws RemoteException {
            if (JobSchedulerService.DEBUG) {
                Slog.d(JobSchedulerService.TAG, "Scheduling job: " + jobInfo.toString());
            }
            if (JobSchedulerService.DEBUG_USAGE) {
                Slog.d(JobSchedulerService.TAG, "Scheduling job: " + jobInfo.toString2());
            }
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            int userId = UserHandle.getUserId(callingUid);
            enforceValidJobRequest(callingUid, callingPid, jobInfo);
            int validateJob = validateJob(jobInfo, callingUid, callingPid, -1, null, null);
            if (validateJob != 1) {
                return validateJob;
            }
            String validateNamespace = validateNamespace(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return JobSchedulerService.this.scheduleAsPackage(jobInfo, null, callingUid, null, userId, validateNamespace, null);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int enqueue(String str, JobInfo jobInfo, JobWorkItem jobWorkItem) throws RemoteException {
            if (JobSchedulerService.DEBUG) {
                Slog.d(JobSchedulerService.TAG, "Enqueueing job: " + jobInfo.toString() + " work: " + jobWorkItem);
            }
            if (JobSchedulerService.DEBUG_USAGE) {
                Slog.d(JobSchedulerService.TAG, "Enqueueing job: " + jobInfo.toString2() + " work: " + jobWorkItem);
            }
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            int userId = UserHandle.getUserId(callingUid);
            enforceValidJobRequest(callingUid, callingPid, jobInfo);
            if (jobWorkItem == null) {
                throw new NullPointerException("work is null");
            }
            int validateJob = validateJob(jobInfo, callingUid, callingPid, -1, null, jobWorkItem);
            if (validateJob != 1) {
                return validateJob;
            }
            String validateNamespace = validateNamespace(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return JobSchedulerService.this.scheduleAsPackage(jobInfo, jobWorkItem, callingUid, null, userId, validateNamespace, null);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int scheduleAsPackage(String str, JobInfo jobInfo, String str2, int i, String str3) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            if (JobSchedulerService.DEBUG) {
                Slog.d(JobSchedulerService.TAG, "Caller uid " + callingUid + " scheduling job: " + jobInfo.toString() + " on behalf of " + str2 + SliceClientPermissions.SliceAuthority.DELIMITER);
            }
            if (str2 == null) {
                throw new NullPointerException("Must specify a package for scheduleAsPackage()");
            }
            if (JobSchedulerService.this.getContext().checkCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS") != 0) {
                throw new SecurityException("Caller uid " + callingUid + " not permitted to schedule jobs for other apps");
            }
            enforceValidJobRequest(callingUid, callingPid, jobInfo);
            int validateJob = validateJob(jobInfo, callingUid, callingPid, i, str2, null);
            if (validateJob != 1) {
                return validateJob;
            }
            String validateNamespace = validateNamespace(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return JobSchedulerService.this.scheduleAsPackage(jobInfo, null, callingUid, str2, i, validateNamespace, str3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public Map<String, ParceledListSlice<JobInfo>> getAllPendingJobs() throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ArrayMap pendingJobs = JobSchedulerService.this.getPendingJobs(callingUid);
                ArrayMap arrayMap = new ArrayMap();
                for (int i = 0; i < pendingJobs.size(); i++) {
                    arrayMap.put((String) pendingJobs.keyAt(i), new ParceledListSlice((List) pendingJobs.valueAt(i)));
                }
                return arrayMap;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public ParceledListSlice<JobInfo> getAllPendingJobsInNamespace(String str) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return new ParceledListSlice<>(JobSchedulerService.this.getPendingJobsInNamespace(callingUid, validateNamespace(str)));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public JobInfo getPendingJob(String str, int i) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return JobSchedulerService.this.getPendingJob(callingUid, validateNamespace(str), i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getPendingJobReason(String str, int i) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return JobSchedulerService.this.getPendingJobReason(callingUid, validateNamespace(str), i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancelAll() throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                JobSchedulerService.this.cancelJobsForUid(callingUid, false, 1, 0, "cancelAll() called by app, callingUid=" + callingUid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancelAllInNamespace(String str) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                JobSchedulerService.this.cancelJobsForUid(callingUid, false, true, validateNamespace(str), 1, 0, "cancelAllInNamespace() called by app, callingUid=" + callingUid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancel(String str, int i) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                JobSchedulerService.this.cancelJob(callingUid, validateNamespace(str), i, callingUid, 1);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean canRunUserInitiatedJobs(String str) {
            int callingUid = Binder.getCallingUid();
            int packageUid = JobSchedulerService.this.mLocalPM.getPackageUid(str, 0L, UserHandle.getUserId(callingUid));
            if (callingUid != packageUid) {
                throw new SecurityException("Uid " + callingUid + " cannot query canRunUserInitiatedJobs for package " + str);
            }
            return JobSchedulerService.this.checkRunUserInitiatedJobsPermission(packageUid, str);
        }

        public boolean hasRunUserInitiatedJobsPermission(String str, int i) {
            int packageUid = JobSchedulerService.this.mLocalPM.getPackageUid(str, 0L, i);
            int callingUid = Binder.getCallingUid();
            if (callingUid != packageUid && !UserHandle.isCore(callingUid)) {
                throw new SecurityException("Uid " + callingUid + " cannot query hasRunUserInitiatedJobsPermission for package " + str);
            }
            return JobSchedulerService.this.checkRunUserInitiatedJobsPermission(packageUid, str);
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpAndUsageStatsPermission(JobSchedulerService.this.getContext(), JobSchedulerService.TAG, printWriter)) {
                boolean z = false;
                int i = -1;
                if (!ArrayUtils.isEmpty(strArr)) {
                    int i2 = 0;
                    boolean z2 = false;
                    while (true) {
                        if (i2 >= strArr.length) {
                            break;
                        }
                        String str = strArr[i2];
                        if ("-h".equals(str)) {
                            JobSchedulerService.dumpHelp(printWriter);
                            return;
                        }
                        if (!"-a".equals(str)) {
                            if ("--proto".equals(str)) {
                                z2 = true;
                            } else if (str.length() > 0 && str.charAt(0) == '-') {
                                printWriter.println("Unknown option: " + str);
                                return;
                            }
                        }
                        i2++;
                    }
                    if (i2 < strArr.length) {
                        String str2 = strArr[i2];
                        try {
                            i = JobSchedulerService.this.getContext().getPackageManager().getPackageUid(str2, DumpState.DUMP_CHANGES);
                        } catch (PackageManager.NameNotFoundException unused) {
                            printWriter.println("Invalid package: " + str2);
                            return;
                        }
                    }
                    z = z2;
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (z) {
                        JobSchedulerService.this.dumpInternalProto(fileDescriptor, i);
                    } else {
                        JobSchedulerService.this.dumpInternal(new IndentingPrintWriter(printWriter, "  "), i);
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, String[] strArr) {
            return new JobSchedulerShellCommand(JobSchedulerService.this).exec(this, parcelFileDescriptor.getFileDescriptor(), parcelFileDescriptor2.getFileDescriptor(), parcelFileDescriptor3.getFileDescriptor(), strArr);
        }

        public List<JobInfo> getStartedJobs() {
            ArrayList arrayList;
            if (Binder.getCallingUid() != 1000) {
                throw new SecurityException("getStartedJobs() is system internal use only.");
            }
            synchronized (JobSchedulerService.this.mLock) {
                ArraySet<JobStatus> runningJobsLocked = JobSchedulerService.this.mConcurrencyManager.getRunningJobsLocked();
                arrayList = new ArrayList(runningJobsLocked.size());
                for (int size = runningJobsLocked.size() - 1; size >= 0; size--) {
                    JobStatus valueAt = runningJobsLocked.valueAt(size);
                    if (valueAt != null) {
                        arrayList.add(valueAt.getJob());
                    }
                }
            }
            return arrayList;
        }

        public ParceledListSlice<JobSnapshot> getAllJobSnapshots() {
            ParceledListSlice<JobSnapshot> parceledListSlice;
            if (Binder.getCallingUid() != 1000) {
                throw new SecurityException("getAllJobSnapshots() is system internal use only.");
            }
            synchronized (JobSchedulerService.this.mLock) {
                final ArrayList arrayList = new ArrayList(JobSchedulerService.this.mJobs.size());
                JobSchedulerService.this.mJobs.forEachJob(new Consumer() { // from class: com.android.server.job.JobSchedulerService$JobSchedulerStub$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        JobSchedulerService.JobSchedulerStub.this.lambda$getAllJobSnapshots$0(arrayList, (JobStatus) obj);
                    }
                });
                parceledListSlice = new ParceledListSlice<>(arrayList);
            }
            return parceledListSlice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getAllJobSnapshots$0(ArrayList arrayList, JobStatus jobStatus) {
            arrayList.add(new JobSnapshot(jobStatus.getJob(), jobStatus.getSatisfiedConstraintFlags(), JobSchedulerService.this.isReadyToBeExecutedLocked(jobStatus)));
        }

        @EnforcePermission(allOf = {"android.permission.MANAGE_ACTIVITY_TASKS", "android.permission.INTERACT_ACROSS_USERS_FULL"})
        public void registerUserVisibleJobObserver(IUserVisibleJobObserver iUserVisibleJobObserver) {
            super.registerUserVisibleJobObserver_enforcePermission();
            if (iUserVisibleJobObserver == null) {
                throw new NullPointerException("observer");
            }
            JobSchedulerService.this.mUserVisibleJobObservers.register(iUserVisibleJobObserver);
            JobSchedulerService.this.mHandler.obtainMessage(10, iUserVisibleJobObserver).sendToTarget();
        }

        @EnforcePermission(allOf = {"android.permission.MANAGE_ACTIVITY_TASKS", "android.permission.INTERACT_ACROSS_USERS_FULL"})
        public void unregisterUserVisibleJobObserver(IUserVisibleJobObserver iUserVisibleJobObserver) {
            super.unregisterUserVisibleJobObserver_enforcePermission();
            if (iUserVisibleJobObserver == null) {
                throw new NullPointerException("observer");
            }
            JobSchedulerService.this.mUserVisibleJobObservers.unregister(iUserVisibleJobObserver);
        }

        @EnforcePermission(allOf = {"android.permission.MANAGE_ACTIVITY_TASKS", "android.permission.INTERACT_ACROSS_USERS_FULL"})
        public void notePendingUserRequestedAppStop(String str, int i, String str2) {
            super.notePendingUserRequestedAppStop_enforcePermission();
            if (str == null) {
                throw new NullPointerException(DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            }
            JobSchedulerService.this.notePendingUserRequestedAppStopInternal(str, i, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int executeRunCommand(String str, int i, String str2, int i2, boolean z, boolean z2) {
        int packageUid;
        Slog.d(TAG, "executeRunCommand(): " + str + SliceClientPermissions.SliceAuthority.DELIMITER + str2 + SliceClientPermissions.SliceAuthority.DELIMITER + i + " " + i2 + " s=" + z + " f=" + z2);
        try {
            IPackageManager packageManager = AppGlobals.getPackageManager();
            if (i == -1) {
                i = 0;
            }
            packageUid = packageManager.getPackageUid(str, 0L, i);
        } catch (RemoteException unused) {
        }
        if (packageUid < 0) {
            return JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
        }
        synchronized (this.mLock) {
            JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(packageUid, str2, i2);
            if (jobByUidAndJobId == null) {
                return JobSchedulerShellCommand.CMD_ERR_NO_JOB;
            }
            jobByUidAndJobId.overrideState = z2 ? 3 : z ? 1 : 2;
            for (int size = this.mControllers.size() - 1; size >= 0; size--) {
                this.mControllers.get(size).reevaluateStateLocked(packageUid);
            }
            if (!jobByUidAndJobId.isConstraintsSatisfied()) {
                jobByUidAndJobId.overrideState = 0;
                return JobSchedulerShellCommand.CMD_ERR_CONSTRAINTS;
            }
            queueReadyJobsForExecutionLocked();
            maybeRunPendingJobsLocked();
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int executeStopCommand(PrintWriter printWriter, String str, int i, String str2, boolean z, int i2, int i3, int i4) {
        if (DEBUG) {
            Slog.v(TAG, "executeStopJobCommand(): " + str + SliceClientPermissions.SliceAuthority.DELIMITER + i + " " + i2 + ": " + i3 + "(" + JobParameters.getInternalReasonCodeDescription(i4) + ")");
        }
        synchronized (this.mLock) {
            if (!this.mConcurrencyManager.executeStopCommandLocked(printWriter, str, i, str2, z, i2, i3, i4)) {
                printWriter.println("No matching executing jobs found.");
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int executeCancelCommand(PrintWriter printWriter, String str, int i, String str2, boolean z, int i2) {
        int i3;
        if (DEBUG) {
            Slog.v(TAG, "executeCancelCommand(): " + str + SliceClientPermissions.SliceAuthority.DELIMITER + i + " " + i2);
        }
        try {
            i3 = AppGlobals.getPackageManager().getPackageUid(str, 0L, i);
        } catch (RemoteException unused) {
            i3 = -1;
        }
        int i4 = i3;
        if (i4 < 0) {
            printWriter.println("Package " + str + " not found.");
            return JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
        }
        if (!z) {
            printWriter.println("Canceling all jobs for " + str + " in user " + i);
            if (cancelJobsForUid(i4, false, 13, 0, "cancel shell command for package")) {
                return 0;
            }
            printWriter.println("No matching jobs found.");
            return 0;
        }
        printWriter.println("Canceling job " + str + "/#" + i2 + " in user " + i);
        if (cancelJob(i4, str2, i2, 2000, 13)) {
            return 0;
        }
        printWriter.println("No matching job found.");
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMonitorBattery(boolean z) {
        synchronized (this.mLock) {
            this.mBatteryStateTracker.setMonitorBatteryLocked(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBatterySeq() {
        int seq;
        synchronized (this.mLock) {
            seq = this.mBatteryStateTracker.getSeq();
        }
        return seq;
    }

    public boolean isBatteryCharging() {
        boolean isCharging;
        synchronized (this.mLock) {
            isCharging = this.mBatteryStateTracker.isCharging();
        }
        return isCharging;
    }

    public boolean isBatteryNotLow() {
        boolean isBatteryNotLow;
        synchronized (this.mLock) {
            isBatteryNotLow = this.mBatteryStateTracker.isBatteryNotLow();
        }
        return isBatteryNotLow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStorageSeq() {
        int seq;
        synchronized (this.mLock) {
            seq = this.mStorageController.getTracker().getSeq();
        }
        return seq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getStorageNotLow() {
        boolean isStorageNotLow;
        synchronized (this.mLock) {
            isStorageNotLow = this.mStorageController.getTracker().isStorageNotLow();
        }
        return isStorageNotLow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getEstimatedNetworkBytes(PrintWriter printWriter, String str, int i, String str2, int i2, int i3) {
        int packageUid;
        long longValue;
        long longValue2;
        try {
            IPackageManager packageManager = AppGlobals.getPackageManager();
            if (i == -1) {
                i = 0;
            }
            packageUid = packageManager.getPackageUid(str, 0L, i);
        } catch (RemoteException unused) {
        }
        if (packageUid < 0) {
            printWriter.print("unknown(");
            printWriter.print(str);
            printWriter.println(")");
            return JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
        }
        synchronized (this.mLock) {
            JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(packageUid, str2, i2);
            if (DEBUG) {
                Slog.d(TAG, "get-estimated-network-bytes " + packageUid + SliceClientPermissions.SliceAuthority.DELIMITER + str2 + SliceClientPermissions.SliceAuthority.DELIMITER + i2 + ": " + jobByUidAndJobId);
            }
            if (jobByUidAndJobId == null) {
                printWriter.print("unknown(");
                UserHandle.formatUid(printWriter, packageUid);
                printWriter.print("/jid");
                printWriter.print(i2);
                printWriter.println(")");
                return JobSchedulerShellCommand.CMD_ERR_NO_JOB;
            }
            Pair<Long, Long> estimatedNetworkBytesLocked = this.mConcurrencyManager.getEstimatedNetworkBytesLocked(str, packageUid, str2, i2);
            if (estimatedNetworkBytesLocked == null) {
                longValue = jobByUidAndJobId.getEstimatedNetworkDownloadBytes();
                longValue2 = jobByUidAndJobId.getEstimatedNetworkUploadBytes();
            } else {
                longValue = ((Long) estimatedNetworkBytesLocked.first).longValue();
                longValue2 = ((Long) estimatedNetworkBytesLocked.second).longValue();
            }
            if (i3 == 0) {
                printWriter.println(longValue);
            } else {
                printWriter.println(longValue2);
            }
            printWriter.println();
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTransferredNetworkBytes(PrintWriter printWriter, String str, int i, String str2, int i2, int i3) {
        long j;
        int packageUid;
        long longValue;
        try {
            IPackageManager packageManager = AppGlobals.getPackageManager();
            int i4 = i;
            if (i4 == -1) {
                i4 = 0;
            }
            j = 0;
            packageUid = packageManager.getPackageUid(str, 0L, i4);
        } catch (RemoteException unused) {
        }
        if (packageUid < 0) {
            printWriter.print("unknown(");
            printWriter.print(str);
            printWriter.println(")");
            return JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
        }
        synchronized (this.mLock) {
            JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(packageUid, str2, i2);
            if (DEBUG) {
                Slog.d(TAG, "get-transferred-network-bytes " + packageUid + str2 + "//" + i2 + ": " + jobByUidAndJobId);
            }
            if (jobByUidAndJobId == null) {
                printWriter.print("unknown(");
                UserHandle.formatUid(printWriter, packageUid);
                printWriter.print("/jid");
                printWriter.print(i2);
                printWriter.println(")");
                return JobSchedulerShellCommand.CMD_ERR_NO_JOB;
            }
            Pair<Long, Long> transferredNetworkBytesLocked = this.mConcurrencyManager.getTransferredNetworkBytesLocked(str, packageUid, str2, i2);
            if (transferredNetworkBytesLocked == null) {
                longValue = 0;
            } else {
                longValue = ((Long) transferredNetworkBytesLocked.first).longValue();
                j = ((Long) transferredNetworkBytesLocked.second).longValue();
            }
            if (i3 == 0) {
                printWriter.println(longValue);
            } else {
                printWriter.println(j);
            }
            printWriter.println();
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkRunUserInitiatedJobsPermission(int i, String str) {
        return getRunUserInitiatedJobsPermissionState(i, str) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRunUserInitiatedJobsPermissionState(int i, String str) {
        return PermissionChecker.checkPermissionForPreflight(getTestableContext(), "android.permission.RUN_USER_INITIATED_JOBS", -1, i, str);
    }

    @VisibleForTesting
    protected ConnectivityController getConnectivityController() {
        return this.mConnectivityController;
    }

    @VisibleForTesting
    protected QuotaController getQuotaController() {
        return this.mQuotaController;
    }

    @VisibleForTesting
    protected TareController getTareController() {
        return this.mTareController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x012d A[Catch: all -> 0x0138, TryCatch #0 {, blocks: (B:14:0x0026, B:16:0x0030, B:18:0x0060, B:19:0x0076, B:21:0x007a, B:23:0x0083, B:24:0x008c, B:27:0x0096, B:28:0x009b, B:29:0x00a1, B:32:0x00af, B:33:0x00b4, B:34:0x00bb, B:37:0x00c9, B:38:0x00ce, B:39:0x00d5, B:42:0x00e3, B:43:0x00e8, B:46:0x00ee, B:51:0x010a, B:52:0x010f, B:53:0x0116, B:56:0x011e, B:57:0x0123, B:59:0x012d, B:60:0x0133, B:61:0x0136), top: B:13:0x0026, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x012a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getJobState(PrintWriter printWriter, String str, int i, String str2, int i2) {
        int packageUid;
        boolean z;
        boolean z2;
        try {
            IPackageManager packageManager = AppGlobals.getPackageManager();
            if (i == -1) {
                i = 0;
            }
            packageUid = packageManager.getPackageUid(str, 0L, i);
        } catch (RemoteException unused) {
        }
        if (packageUid < 0) {
            printWriter.print("unknown(");
            printWriter.print(str);
            printWriter.println(")");
            return JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
        }
        synchronized (this.mLock) {
            JobStatus jobByUidAndJobId = this.mJobs.getJobByUidAndJobId(packageUid, str2, i2);
            if (DEBUG) {
                Slog.d(TAG, "get-job-state " + str2 + SliceClientPermissions.SliceAuthority.DELIMITER + packageUid + SliceClientPermissions.SliceAuthority.DELIMITER + i2 + ": " + jobByUidAndJobId);
            }
            if (jobByUidAndJobId == null) {
                printWriter.print("unknown(");
                UserHandle.formatUid(printWriter, packageUid);
                printWriter.print("/jid");
                printWriter.print(i2);
                printWriter.println(")");
                return JobSchedulerShellCommand.CMD_ERR_NO_JOB;
            }
            boolean z3 = true;
            if (this.mPendingJobQueue.contains(jobByUidAndJobId)) {
                printWriter.print("pending");
                z = true;
            } else {
                z = false;
            }
            if (this.mConcurrencyManager.isJobRunningLocked(jobByUidAndJobId)) {
                if (z) {
                    printWriter.print(" ");
                }
                printWriter.println(DomainVerificationPersistence.TAG_ACTIVE);
                z = true;
            }
            if (!ArrayUtils.contains(this.mStartedUsers, jobByUidAndJobId.getUserId())) {
                if (z) {
                    printWriter.print(" ");
                }
                printWriter.println("user-stopped");
                z = true;
            }
            if (!ArrayUtils.contains(this.mStartedUsers, jobByUidAndJobId.getSourceUserId())) {
                if (z) {
                    printWriter.print(" ");
                }
                printWriter.println("source-user-stopped");
                z = true;
            }
            if (this.mBackingUpUids.get(jobByUidAndJobId.getSourceUid())) {
                if (z) {
                    printWriter.print(" ");
                }
                printWriter.println("backing-up");
                z = true;
            }
            if (AppGlobals.getPackageManager().getServiceInfo(jobByUidAndJobId.getServiceComponent(), 268435456L, jobByUidAndJobId.getUserId()) != null) {
                z2 = true;
                if (!z2) {
                    if (z) {
                        printWriter.print(" ");
                    }
                    printWriter.println("no-component");
                    z = true;
                }
                if (jobByUidAndJobId.isReady()) {
                    z3 = z;
                } else {
                    if (z) {
                        printWriter.print(" ");
                    }
                    printWriter.println("ready");
                }
                if (!z3) {
                    printWriter.print("waiting");
                }
                printWriter.println();
                return 0;
            }
            z2 = false;
            if (!z2) {
            }
            if (jobByUidAndJobId.isReady()) {
            }
            if (!z3) {
            }
            printWriter.println();
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetExecutionQuota(String str, int i) {
        synchronized (this.mLock) {
            this.mQuotaController.clearAppStatsLocked(i, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetScheduleQuota() {
        this.mQuotaTracker.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void triggerDockState(boolean z) {
        Intent intent;
        if (z) {
            intent = new Intent("android.intent.action.DOCK_IDLE");
        } else {
            intent = new Intent("android.intent.action.DOCK_ACTIVE");
        }
        intent.setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME);
        intent.addFlags(AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE);
        getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    static void dumpHelp(PrintWriter printWriter) {
        printWriter.println("Job Scheduler (jobscheduler) dump options:");
        printWriter.println("  [-h] [package] ...");
        printWriter.println("    -h: print this help");
        printWriter.println("  [package] is an optional package name to limit the output to.");
    }

    private static void sortJobs(List<JobStatus> list) {
        Collections.sort(list, new Comparator<JobStatus>() { // from class: com.android.server.job.JobSchedulerService.6
            @Override // java.util.Comparator
            public int compare(JobStatus jobStatus, JobStatus jobStatus2) {
                int uid = jobStatus.getUid();
                int uid2 = jobStatus2.getUid();
                int jobId = jobStatus.getJobId();
                int jobId2 = jobStatus2.getJobId();
                if (uid != uid2) {
                    return uid < uid2 ? -1 : 1;
                }
                if (jobId < jobId2) {
                    return -1;
                }
                return jobId > jobId2 ? 1 : 0;
            }
        });
    }

    @NeverCompile
    void dumpInternal(IndentingPrintWriter indentingPrintWriter, int i) {
        long j;
        boolean z;
        Iterator<JobStatus> it;
        long j2;
        long j3;
        final int appId = UserHandle.getAppId(i);
        long millis = sSystemClock.millis();
        long millis2 = sElapsedRealtimeClock.millis();
        long millis3 = sUptimeMillisClock.millis();
        Predicate<JobStatus> predicate = new Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$dumpInternal$7;
                lambda$dumpInternal$7 = JobSchedulerService.lambda$dumpInternal$7(appId, (JobStatus) obj);
                return lambda$dumpInternal$7;
            }
        };
        synchronized (this.mLock) {
            this.mConstants.dump(indentingPrintWriter);
            for (StateController stateController : this.mControllers) {
                indentingPrintWriter.increaseIndent();
                stateController.dumpConstants(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.println();
            boolean z2 = true;
            for (int size = this.mJobRestrictions.size() - 1; size >= 0; size--) {
                this.mJobRestrictions.get(size).dumpConstants(indentingPrintWriter);
            }
            indentingPrintWriter.println();
            this.mQuotaTracker.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            indentingPrintWriter.print("Battery charging: ");
            indentingPrintWriter.println(this.mBatteryStateTracker.isCharging());
            indentingPrintWriter.print("Battery not low: ");
            indentingPrintWriter.println(this.mBatteryStateTracker.isBatteryNotLow());
            if (this.mBatteryStateTracker.isMonitoring()) {
                indentingPrintWriter.print("MONITORING: seq=");
                indentingPrintWriter.println(this.mBatteryStateTracker.getSeq());
            }
            indentingPrintWriter.println();
            indentingPrintWriter.println("Started users: " + Arrays.toString(this.mStartedUsers));
            indentingPrintWriter.println();
            indentingPrintWriter.print("Media Cloud Providers: ");
            indentingPrintWriter.println(this.mCloudMediaProviderPackages);
            indentingPrintWriter.println();
            indentingPrintWriter.print("Registered ");
            indentingPrintWriter.print(this.mJobs.size());
            indentingPrintWriter.println(" jobs:");
            indentingPrintWriter.increaseIndent();
            if (this.mJobs.size() > 0) {
                List<JobStatus> allJobs = this.mJobs.mJobSet.getAllJobs();
                sortJobs(allJobs);
                Iterator<JobStatus> it2 = allJobs.iterator();
                z = false;
                while (it2.hasNext()) {
                    JobStatus next = it2.next();
                    if (predicate.test(next)) {
                        indentingPrintWriter.print("JOB ");
                        next.printUniqueId(indentingPrintWriter);
                        indentingPrintWriter.print(": ");
                        indentingPrintWriter.println(next.toShortStringExceptUniqueId());
                        indentingPrintWriter.increaseIndent();
                        next.dump(indentingPrintWriter, z2, millis2);
                        indentingPrintWriter.print("Restricted due to:");
                        boolean z3 = checkIfRestricted(next) != null ? z2 : false;
                        if (z3) {
                            int size2 = this.mJobRestrictions.size() - 1;
                            while (size2 >= 0) {
                                Iterator<JobStatus> it3 = it2;
                                JobRestriction jobRestriction = this.mJobRestrictions.get(size2);
                                if (jobRestriction.isJobRestricted(next)) {
                                    int internalReason = jobRestriction.getInternalReason();
                                    j3 = millis;
                                    indentingPrintWriter.print(" ");
                                    indentingPrintWriter.print(JobParameters.getInternalReasonCodeDescription(internalReason));
                                } else {
                                    j3 = millis;
                                }
                                size2--;
                                it2 = it3;
                                millis = j3;
                            }
                            it = it2;
                            j2 = millis;
                        } else {
                            it = it2;
                            j2 = millis;
                            indentingPrintWriter.print(" none");
                        }
                        indentingPrintWriter.println(".");
                        indentingPrintWriter.print("Ready: ");
                        indentingPrintWriter.print(isReadyToBeExecutedLocked(next));
                        indentingPrintWriter.print(" (job=");
                        indentingPrintWriter.print(next.isReady());
                        indentingPrintWriter.print(" user=");
                        indentingPrintWriter.print(areUsersStartedLocked(next));
                        indentingPrintWriter.print(" !restricted=");
                        indentingPrintWriter.print(!z3);
                        indentingPrintWriter.print(" !pending=");
                        indentingPrintWriter.print(!this.mPendingJobQueue.contains(next));
                        indentingPrintWriter.print(" !active=");
                        indentingPrintWriter.print(!this.mConcurrencyManager.isJobRunningLocked(next));
                        indentingPrintWriter.print(" !backingup=");
                        indentingPrintWriter.print(!this.mBackingUpUids.get(next.getSourceUid()));
                        indentingPrintWriter.print(" comp=");
                        indentingPrintWriter.print(isComponentUsable(next));
                        indentingPrintWriter.println(")");
                        indentingPrintWriter.decreaseIndent();
                        it2 = it;
                        millis = j2;
                        z = true;
                        z2 = true;
                    }
                }
                j = millis;
            } else {
                j = millis;
                z = false;
            }
            if (!z) {
                indentingPrintWriter.println("None.");
            }
            indentingPrintWriter.decreaseIndent();
            for (int i2 = 0; i2 < this.mControllers.size(); i2++) {
                indentingPrintWriter.println();
                indentingPrintWriter.println(this.mControllers.get(i2).getClass().getSimpleName() + ":");
                indentingPrintWriter.increaseIndent();
                this.mControllers.get(i2).dumpControllerStateLocked(indentingPrintWriter, predicate);
                indentingPrintWriter.decreaseIndent();
            }
            boolean z4 = false;
            for (int i3 = 0; i3 < this.mUidProcStates.size(); i3++) {
                int keyAt = this.mUidProcStates.keyAt(i3);
                if (appId == -1 || appId == UserHandle.getAppId(keyAt)) {
                    if (!z4) {
                        indentingPrintWriter.println();
                        indentingPrintWriter.println("Uid proc states:");
                        indentingPrintWriter.increaseIndent();
                        z4 = true;
                    }
                    indentingPrintWriter.print(UserHandle.formatUid(keyAt));
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.println(ActivityManager.procStateToString(this.mUidProcStates.valueAt(i3)));
                }
            }
            if (z4) {
                indentingPrintWriter.decreaseIndent();
            }
            boolean z5 = false;
            for (int i4 = 0; i4 < this.mUidBiasOverride.size(); i4++) {
                int keyAt2 = this.mUidBiasOverride.keyAt(i4);
                if (appId == -1 || appId == UserHandle.getAppId(keyAt2)) {
                    if (!z5) {
                        indentingPrintWriter.println();
                        indentingPrintWriter.println("Uid bias overrides:");
                        indentingPrintWriter.increaseIndent();
                        z5 = true;
                    }
                    indentingPrintWriter.print(UserHandle.formatUid(keyAt2));
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.println(this.mUidBiasOverride.valueAt(i4));
                }
            }
            if (z5) {
                indentingPrintWriter.decreaseIndent();
            }
            boolean z6 = false;
            for (int i5 = 0; i5 < this.mUidCapabilities.size(); i5++) {
                int keyAt3 = this.mUidCapabilities.keyAt(i5);
                if (appId == -1 || appId == UserHandle.getAppId(keyAt3)) {
                    if (!z6) {
                        indentingPrintWriter.println();
                        indentingPrintWriter.println("Uid capabilities:");
                        indentingPrintWriter.increaseIndent();
                        z6 = true;
                    }
                    indentingPrintWriter.print(UserHandle.formatUid(keyAt3));
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.println(ActivityManager.getCapabilitiesSummary(this.mUidCapabilities.valueAt(i5)));
                }
            }
            if (z6) {
                indentingPrintWriter.decreaseIndent();
            }
            boolean z7 = false;
            for (int i6 = 0; i6 < this.mUidToPackageCache.size(); i6++) {
                int keyAt4 = this.mUidToPackageCache.keyAt(i6);
                if (i == -1 || i == keyAt4) {
                    if (!z7) {
                        indentingPrintWriter.println();
                        indentingPrintWriter.println("Cached UID->package map:");
                        indentingPrintWriter.increaseIndent();
                        z7 = true;
                    }
                    indentingPrintWriter.print(keyAt4);
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.println(this.mUidToPackageCache.get(keyAt4));
                }
            }
            if (z7) {
                indentingPrintWriter.decreaseIndent();
            }
            boolean z8 = false;
            for (int i7 = 0; i7 < this.mBackingUpUids.size(); i7++) {
                int keyAt5 = this.mBackingUpUids.keyAt(i7);
                if (appId == -1 || appId == UserHandle.getAppId(keyAt5)) {
                    if (!z8) {
                        indentingPrintWriter.println();
                        indentingPrintWriter.println("Backing up uids:");
                        indentingPrintWriter.increaseIndent();
                        z8 = true;
                    } else {
                        indentingPrintWriter.print(", ");
                    }
                    indentingPrintWriter.print(UserHandle.formatUid(keyAt5));
                }
            }
            if (z8) {
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println();
            }
            indentingPrintWriter.println();
            this.mJobPackageTracker.dump(indentingPrintWriter, appId);
            indentingPrintWriter.println();
            if (this.mJobPackageTracker.dumpHistory(indentingPrintWriter, appId)) {
                indentingPrintWriter.println();
            }
            indentingPrintWriter.println("Pending queue:");
            indentingPrintWriter.increaseIndent();
            this.mPendingJobQueue.resetIterator();
            boolean z9 = false;
            int i8 = 0;
            while (true) {
                JobStatus next2 = this.mPendingJobQueue.next();
                if (next2 == null) {
                    break;
                }
                i8++;
                if (predicate.test(next2)) {
                    if (!z9) {
                        z9 = true;
                    }
                    indentingPrintWriter.print("Pending #");
                    indentingPrintWriter.print(i8);
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.println(next2.toShortString());
                    indentingPrintWriter.increaseIndent();
                    next2.dump(indentingPrintWriter, false, millis2);
                    int evaluateJobBiasLocked = evaluateJobBiasLocked(next2);
                    indentingPrintWriter.print("Evaluated bias: ");
                    indentingPrintWriter.println(JobInfo.getBiasString(evaluateJobBiasLocked));
                    indentingPrintWriter.print("Tag: ");
                    indentingPrintWriter.println(next2.getTag());
                    indentingPrintWriter.print("Enq: ");
                    TimeUtils.formatDuration(next2.madePending - millis3, indentingPrintWriter);
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.println();
                }
            }
            if (!z9) {
                indentingPrintWriter.println("None");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            boolean z10 = false;
            this.mConcurrencyManager.dumpContextInfoLocked(indentingPrintWriter, predicate, millis2, millis3);
            indentingPrintWriter.println();
            indentingPrintWriter.println("Recently completed jobs:");
            indentingPrintWriter.increaseIndent();
            boolean z11 = false;
            for (int i9 = 1; i9 <= 20; i9++) {
                int i10 = ((this.mLastCompletedJobIndex + 20) - i9) % 20;
                JobStatus jobStatus = this.mLastCompletedJobs[i10];
                if (jobStatus != null && predicate.test(jobStatus)) {
                    TimeUtils.formatDuration(this.mLastCompletedJobTimeElapsed[i10], millis2, indentingPrintWriter);
                    indentingPrintWriter.println();
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println(jobStatus.toShortString());
                    jobStatus.dump(indentingPrintWriter, true, millis2);
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.decreaseIndent();
                    z11 = true;
                }
            }
            if (!z11) {
                indentingPrintWriter.println("None");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            int i11 = 1;
            while (true) {
                JobStatus[] jobStatusArr = this.mLastCancelledJobs;
                if (i11 > jobStatusArr.length) {
                    break;
                }
                int length = ((this.mLastCancelledJobIndex + jobStatusArr.length) - i11) % jobStatusArr.length;
                JobStatus jobStatus2 = jobStatusArr[length];
                if (jobStatus2 != null && predicate.test(jobStatus2)) {
                    if (!z10) {
                        indentingPrintWriter.println();
                        indentingPrintWriter.println("Recently cancelled jobs:");
                        indentingPrintWriter.increaseIndent();
                        z10 = true;
                    }
                    TimeUtils.formatDuration(this.mLastCancelledJobTimeElapsed[length], millis2, indentingPrintWriter);
                    indentingPrintWriter.println();
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println(jobStatus2.toShortString());
                    jobStatus2.dump(indentingPrintWriter, true, millis2);
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.decreaseIndent();
                }
                i11++;
            }
            if (!z10) {
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println();
            }
            if (i == -1) {
                indentingPrintWriter.println();
                indentingPrintWriter.print("mReadyToRock=");
                indentingPrintWriter.println(this.mReadyToRock);
                indentingPrintWriter.print("mReportedActive=");
                indentingPrintWriter.println(this.mReportedActive);
            }
            indentingPrintWriter.println();
            this.mConcurrencyManager.dumpLocked(indentingPrintWriter, j, millis2);
            indentingPrintWriter.println();
            indentingPrintWriter.print("PersistStats: ");
            indentingPrintWriter.println(this.mJobs.getPersistStats());
        }
        this.mJobSchedulerServiceExt.dumpProxyJob(indentingPrintWriter);
        indentingPrintWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dumpInternal$7(int i, JobStatus jobStatus) {
        return i == -1 || UserHandle.getAppId(jobStatus.getUid()) == i || UserHandle.getAppId(jobStatus.getSourceUid()) == i;
    }

    void dumpInternalProto(FileDescriptor fileDescriptor, int i) {
        int i2;
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        final int appId = UserHandle.getAppId(i);
        long millis = sSystemClock.millis();
        long millis2 = sElapsedRealtimeClock.millis();
        long millis3 = sUptimeMillisClock.millis();
        Predicate<JobStatus> predicate = new Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$dumpInternalProto$8;
                lambda$dumpInternalProto$8 = JobSchedulerService.lambda$dumpInternalProto$8(appId, (JobStatus) obj);
                return lambda$dumpInternalProto$8;
            }
        };
        Object obj = this.mLock;
        synchronized (obj) {
            try {
                try {
                    long start = protoOutputStream.start(1146756268033L);
                    this.mConstants.dump(protoOutputStream);
                    Iterator<StateController> it = this.mControllers.iterator();
                    while (it.hasNext()) {
                        it.next().dumpConstants(protoOutputStream);
                    }
                    protoOutputStream.end(start);
                    for (int size = this.mJobRestrictions.size() - 1; size >= 0; size--) {
                        this.mJobRestrictions.get(size).dumpConstants(protoOutputStream);
                    }
                    int[] iArr = this.mStartedUsers;
                    int length = iArr.length;
                    int i3 = 0;
                    while (i3 < length) {
                        protoOutputStream.write(2220498092034L, iArr[i3]);
                        i3++;
                        length = length;
                        iArr = iArr;
                    }
                    this.mQuotaTracker.dump(protoOutputStream, 1146756268054L);
                    if (this.mJobs.size() > 0) {
                        List<JobStatus> allJobs = this.mJobs.mJobSet.getAllJobs();
                        sortJobs(allJobs);
                        for (JobStatus jobStatus : allJobs) {
                            long start2 = protoOutputStream.start(2246267895811L);
                            jobStatus.writeToShortProto(protoOutputStream, 1146756268033L);
                            if (predicate.test(jobStatus)) {
                                long j = millis;
                                Predicate<JobStatus> predicate2 = predicate;
                                Object obj2 = obj;
                                jobStatus.dump(protoOutputStream, 1146756268034L, true, millis2);
                                protoOutputStream.write(1133871366154L, isReadyToBeExecutedLocked(jobStatus));
                                protoOutputStream.write(1133871366147L, jobStatus.isReady());
                                protoOutputStream.write(1133871366148L, areUsersStartedLocked(jobStatus));
                                protoOutputStream.write(1133871366155L, checkIfRestricted(jobStatus) != null);
                                protoOutputStream.write(1133871366149L, this.mPendingJobQueue.contains(jobStatus));
                                protoOutputStream.write(1133871366150L, this.mConcurrencyManager.isJobRunningLocked(jobStatus));
                                protoOutputStream.write(1133871366151L, this.mBackingUpUids.get(jobStatus.getSourceUid()));
                                protoOutputStream.write(1133871366152L, isComponentUsable(jobStatus));
                                for (JobRestriction jobRestriction : this.mJobRestrictions) {
                                    long start3 = protoOutputStream.start(2246267895820L);
                                    protoOutputStream.write(1159641169921L, jobRestriction.getInternalReason());
                                    protoOutputStream.write(1133871366146L, jobRestriction.isJobRestricted(jobStatus));
                                    protoOutputStream.end(start3);
                                }
                                protoOutputStream.end(start2);
                                predicate = predicate2;
                                obj = obj2;
                                millis = j;
                            }
                        }
                    }
                    Object obj3 = obj;
                    long j2 = millis;
                    Predicate<JobStatus> predicate3 = predicate;
                    Iterator<StateController> it2 = this.mControllers.iterator();
                    while (it2.hasNext()) {
                        it2.next().dumpControllerStateLocked(protoOutputStream, 2246267895812L, predicate3);
                    }
                    int i4 = 0;
                    while (true) {
                        i2 = -1;
                        if (i4 >= this.mUidBiasOverride.size()) {
                            break;
                        }
                        int keyAt = this.mUidBiasOverride.keyAt(i4);
                        if (appId == -1 || appId == UserHandle.getAppId(keyAt)) {
                            long start4 = protoOutputStream.start(2246267895813L);
                            protoOutputStream.write(1120986464257L, keyAt);
                            protoOutputStream.write(1172526071810L, this.mUidBiasOverride.valueAt(i4));
                            protoOutputStream.end(start4);
                        }
                        i4++;
                    }
                    for (int i5 = 0; i5 < this.mBackingUpUids.size(); i5++) {
                        int keyAt2 = this.mBackingUpUids.keyAt(i5);
                        if (appId == -1 || appId == UserHandle.getAppId(keyAt2)) {
                            protoOutputStream.write(2220498092038L, keyAt2);
                        }
                    }
                    this.mJobPackageTracker.dump(protoOutputStream, 1146756268040L, appId);
                    this.mJobPackageTracker.dumpHistory(protoOutputStream, 1146756268039L, appId);
                    this.mPendingJobQueue.resetIterator();
                    while (true) {
                        JobStatus next = this.mPendingJobQueue.next();
                        if (next == null) {
                            break;
                        }
                        long start5 = protoOutputStream.start(2246267895817L);
                        next.writeToShortProto(protoOutputStream, 1146756268033L);
                        next.dump(protoOutputStream, 1146756268034L, false, millis2);
                        protoOutputStream.write(1172526071811L, evaluateJobBiasLocked(next));
                        protoOutputStream.write(1112396529668L, millis3 - next.madePending);
                        protoOutputStream.end(start5);
                        i2 = -1;
                    }
                    if (i == i2) {
                        protoOutputStream.write(1133871366155L, this.mReadyToRock);
                        protoOutputStream.write(1133871366156L, this.mReportedActive);
                    }
                    this.mConcurrencyManager.dumpProtoLocked(protoOutputStream, 1146756268052L, j2, millis2);
                    this.mJobs.getPersistStats().dumpDebug(protoOutputStream, 1146756268053L);
                    protoOutputStream.flush();
                } catch (Throwable th) {
                    th = th;
                    Object obj4 = obj;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dumpInternalProto$8(int i, JobStatus jobStatus) {
        return i == -1 || UserHandle.getAppId(jobStatus.getUid()) == i || UserHandle.getAppId(jobStatus.getSourceUid()) == i;
    }

    public IJobSchedulerServiceWrapper getWrapper() {
        return this.mJobSchedulerServiceWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class JobSchedulerServiceWrapper implements IJobSchedulerServiceWrapper {
        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public int getMAX_JOBS_PER_APP() {
            return JobSchedulerService.MAX_JOBS_PER_APP;
        }

        private JobSchedulerServiceWrapper() {
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public IJobSchedulerServiceExt getExtImpl() {
            return JobSchedulerService.this.mJobSchedulerServiceExt;
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public void cancelJobImplLocked(JobStatus jobStatus, JobStatus jobStatus2, int i, int i2, String str) {
            JobSchedulerService.this.cancelJobImplLocked(jobStatus, jobStatus2, i, i2, str);
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public boolean stopTrackingJobLocked(JobStatus jobStatus, JobStatus jobStatus2, boolean z) {
            return JobSchedulerService.this.stopTrackingJobLocked(jobStatus, jobStatus2, z);
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public void cancelJobsForPackageAndUidLocked(String str, int i, int i2, int i3, String str2) {
            JobSchedulerService.this.cancelJobsForPackageAndUidLocked(str, i, true, true, i2, i3, str2);
        }
    }
}
