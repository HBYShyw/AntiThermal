package com.android.server.am;

import android.R;
import android.app.ActivityThread;
import android.app.ForegroundServiceTypePolicy;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.KeyValueListParser;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.am.ActivityManagerService;
import com.android.server.backup.BackupManagerConstants;
import dalvik.annotation.optimization.NeverCompile;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ActivityManagerConstants extends ContentObserver {
    private static final Uri ACTIVITY_MANAGER_CONSTANTS_URI;
    private static final Uri ACTIVITY_STARTS_LOGGING_ENABLED_URI;
    public static int BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = 0;
    public static boolean BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = false;
    public static float BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD = 0.0f;
    public static int BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = 0;
    public static boolean BINDER_HEAVY_HITTER_WATCHER_ENABLED = false;
    public static float BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = 0.0f;
    static final long DEFAULT_BACKGROUND_SETTLE_TIME = 60000;
    private static final long DEFAULT_BG_START_TIMEOUT = 15000;
    private static final int DEFAULT_BOOT_TIME_TEMP_ALLOWLIST_DURATION = 20000;
    private static final int DEFAULT_BOUND_SERVICE_CRASH_MAX_RETRY = 16;
    private static final long DEFAULT_BOUND_SERVICE_CRASH_RESTART_DURATION = 1800000;
    private static final String DEFAULT_COMPONENT_ALIAS_OVERRIDES = "";
    private static final long DEFAULT_CONTENT_PROVIDER_RETAIN_TIME = 20000;
    private static final int DEFAULT_DEFER_BOOT_COMPLETED_BROADCAST = 6;
    private static final boolean DEFAULT_ENABLE_COMPONENT_ALIAS = false;
    private static final boolean DEFAULT_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = true;
    private static final boolean DEFAULT_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION = false;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_CRITICAL_MEM = 30000;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_LOW_MEM = 20000;
    private static final long[] DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MODERATE_MEM = 10000;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_NORMAL_MEM = 0;
    private static final long DEFAULT_FGSERVICE_MIN_REPORT_TIME = 3000;
    private static final long DEFAULT_FGSERVICE_MIN_SHOWN_TIME = 2000;
    private static final long DEFAULT_FGSERVICE_SCREEN_ON_AFTER_TIME = 5000;
    private static final long DEFAULT_FGSERVICE_SCREEN_ON_BEFORE_TIME = 1000;
    private static final boolean DEFAULT_FGS_ALLOW_OPT_OUT = false;
    private static final float DEFAULT_FGS_ATOM_SAMPLE_RATE = 1.0f;
    private static final float DEFAULT_FGS_START_ALLOWED_LOG_SAMPLE_RATE = 0.25f;
    private static final float DEFAULT_FGS_START_DENIED_LOG_SAMPLE_RATE = 1.0f;
    private static final int DEFAULT_FGS_START_FOREGROUND_TIMEOUT_MS = 10000;
    private static final long DEFAULT_FG_TO_BG_FGS_GRACE_DURATION = 5000;
    private static final boolean DEFAULT_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = true;
    private static final long DEFAULT_FULL_PSS_LOWERED_INTERVAL = 300000;
    private static final long DEFAULT_FULL_PSS_MIN_INTERVAL = 1200000;
    private static final long DEFAULT_GC_MIN_INTERVAL = 60000;
    private static final long DEFAULT_GC_TIMEOUT = 5000;
    static final boolean DEFAULT_KILL_BG_RESTRICTED_CACHED_IDLE = true;
    static final long DEFAULT_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME_MS = 60000;
    private static final float DEFAULT_LOW_SWAP_THRESHOLD_PERCENT = 0.1f;
    private static final int DEFAULT_MAX_CACHED_PROCESSES = 1024;
    private static final long DEFAULT_MAX_EMPTY_TIME_MILLIS = 3600000000L;
    private static final int DEFAULT_MAX_PHANTOM_PROCESSES = 32;
    private static final long DEFAULT_MAX_PREVIOUS_TIME = 60000;
    static final int DEFAULT_MAX_SERVICE_CONNECTIONS_PER_PROCESS = 3000;
    private static final long DEFAULT_MAX_SERVICE_INACTIVITY = 1800000;
    private static final long DEFAULT_MEMORY_INFO_THROTTLE_TIME = 300000;
    private static final long DEFAULT_MIN_ASSOC_LOG_DURATION = 300000;
    private static final int DEFAULT_MIN_CRASH_INTERVAL = 120000;
    private static final long DEFAULT_NETWORK_ACCESS_TIMEOUT_MS = 200;
    private static final long DEFAULT_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS = 600000;
    private static final boolean DEFAULT_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED = true;
    private static final int DEFAULT_OOMADJ_UPDATE_POLICY = 1;
    private static final int DEFAULT_PENDINGINTENT_WARNING_THRESHOLD = 2000;
    private static final long DEFAULT_POWER_CHECK_INTERVAL;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_1 = 25;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_2 = 25;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_3 = 10;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_4 = 2;
    private static final boolean DEFAULT_PRIORITIZE_ALARM_BROADCASTS = true;
    private static final boolean DEFAULT_PROACTIVE_KILLS_ENABLED = false;
    private static final int DEFAULT_PROCESS_CRASH_COUNT_LIMIT = 12;
    private static final int DEFAULT_PROCESS_CRASH_COUNT_RESET_INTERVAL = 43200000;
    private static final long DEFAULT_PROCESS_KILL_TIMEOUT_MS = 10000;
    private static final boolean DEFAULT_PROCESS_START_ASYNC = true;
    private static final int DEFAULT_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR = 1;
    private static final long DEFAULT_SERVICE_BACKGROUND_TIMEOUT;
    private static final long DEFAULT_SERVICE_BG_ACTIVITY_START_TIMEOUT = 10000;
    private static final long DEFAULT_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS = 15000;
    private static final long DEFAULT_SERVICE_MIN_RESTART_TIME_BETWEEN = 10000;
    private static final long DEFAULT_SERVICE_RESET_RUN_DURATION = 60000;
    private static final long DEFAULT_SERVICE_RESTART_DURATION = 1000;
    private static final int DEFAULT_SERVICE_RESTART_DURATION_FACTOR = 4;
    private static final long DEFAULT_SERVICE_RESTART_DURATION_LOW = 30000;
    private static final int DEFAULT_SERVICE_START_FOREGROUND_ANR_DELAY_MS = 10000;
    private static final int DEFAULT_SERVICE_START_FOREGROUND_TIMEOUT_MS = 30000;
    private static final long DEFAULT_SERVICE_TIMEOUT;
    private static final long DEFAULT_SERVICE_USAGE_INTERACTION_TIME_POST_S = 60000;
    private static final long DEFAULT_SERVICE_USAGE_INTERACTION_TIME_PRE_S = 1800000;
    static final long DEFAULT_SHORT_FGS_ANR_EXTRA_WAIT_DURATION = 10000;
    static final long DEFAULT_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION = 5000;
    static final long DEFAULT_SHORT_FGS_TIMEOUT_DURATION = 180000;
    private static final boolean DEFAULT_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED = true;
    private static final long DEFAULT_TIERED_CACHED_ADJ_DECAY_TIME = 60000;
    private static final long DEFAULT_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = 15000;
    private static final long DEFAULT_TOP_TO_FGS_GRACE_DURATION = 15000;
    private static final long DEFAULT_USAGE_STATS_INTERACTION_INTERVAL_POST_S = 600000;
    private static final long DEFAULT_USAGE_STATS_INTERACTION_INTERVAL_PRE_S = 7200000;
    private static final boolean DEFAULT_USE_MODERN_TRIM = true;
    private static final boolean DEFAULT_USE_TIERED_CACHED_ADJ = false;
    private static final long DEFAULT_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION = 5000;
    private static final Uri ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI;
    private static final Uri FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI;
    static final String KEY_BACKGROUND_SETTLE_TIME = "background_settle_time";
    static final String KEY_BG_START_TIMEOUT = "service_bg_start_timeout";
    private static final String KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = "binder_heavy_hitter_auto_sampler_batchsize";
    private static final String KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = "binder_heavy_hitter_auto_sampler_enabled";
    private static final String KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD = "binder_heavy_hitter_auto_sampler_threshold";
    private static final String KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = "binder_heavy_hitter_watcher_batchsize";
    private static final String KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED = "binder_heavy_hitter_watcher_enabled";
    private static final String KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = "binder_heavy_hitter_watcher_threshold";
    static final String KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION = "boot_time_temp_allowlist_duration";
    static final String KEY_BOUND_SERVICE_CRASH_MAX_RETRY = "service_crash_max_retry";
    static final String KEY_BOUND_SERVICE_CRASH_RESTART_DURATION = "service_crash_restart_duration";
    static final String KEY_COMPONENT_ALIAS_OVERRIDES = "component_alias_overrides";
    private static final String KEY_CONTENT_PROVIDER_RETAIN_TIME = "content_provider_retain_time";
    private static final String KEY_DEFAULT_APPLICATION_START_INFO_ENABLED = "enable_app_start_info";
    private static final String KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED = "default_background_activity_starts_enabled";
    private static final String KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED = "default_background_fgs_starts_restriction_enabled";
    private static final String KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK = "default_fgs_starts_restriction_check_caller_target_sdk";
    private static final String KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED = "default_fgs_starts_restriction_enabled";
    private static final String KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED = "default_fgs_starts_restriction_notification_enabled";
    private static final String KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED = "deferred_fgs_notifications_api_gated";
    private static final String KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED = "deferred_fgs_notifications_enabled";
    private static final String KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME = "deferred_fgs_notification_exclusion_time";
    private static final String KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT = "deferred_fgs_notification_exclusion_time_for_short";
    private static final String KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL = "deferred_fgs_notification_interval";
    private static final String KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT = "deferred_fgs_notification_interval_for_short";
    private static final String KEY_DEFER_BOOT_COMPLETED_BROADCAST = "defer_boot_completed_broadcast";
    static final String KEY_ENABLE_COMPONENT_ALIAS = "enable_experimental_component_alias";
    static final String KEY_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = "enable_extra_delay_svc_restart_mem_pressure";
    private static final String KEY_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION = "enable_wait_for_finish_attach_application";
    static final String KEY_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = "extra_delay_svc_restart_mem_pressure";
    private static final String KEY_FGSERVICE_MIN_REPORT_TIME = "fgservice_min_report_time";
    private static final String KEY_FGSERVICE_MIN_SHOWN_TIME = "fgservice_min_shown_time";
    private static final String KEY_FGSERVICE_SCREEN_ON_AFTER_TIME = "fgservice_screen_on_after_time";
    private static final String KEY_FGSERVICE_SCREEN_ON_BEFORE_TIME = "fgservice_screen_on_before_time";
    static final String KEY_FGS_ALLOW_OPT_OUT = "fgs_allow_opt_out";
    static final String KEY_FGS_ATOM_SAMPLE_RATE = "fgs_atom_sample_rate";
    static final String KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE = "fgs_start_allowed_log_sample_rate";
    static final String KEY_FGS_START_DENIED_LOG_SAMPLE_RATE = "fgs_start_denied_log_sample_rate";
    static final String KEY_FGS_START_FOREGROUND_TIMEOUT = "fgs_start_foreground_timeout";
    static final String KEY_FG_TO_BG_FGS_GRACE_DURATION = "fg_to_bg_fgs_grace_duration";
    private static final String KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = "force_bg_check_on_restricted";
    private static final String KEY_FULL_PSS_LOWERED_INTERVAL = "full_pss_lowered_interval";
    private static final String KEY_FULL_PSS_MIN_INTERVAL = "full_pss_min_interval";
    private static final String KEY_GC_MIN_INTERVAL = "gc_min_interval";
    private static final String KEY_GC_TIMEOUT = "gc_timeout";
    private static final String KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES = "imperceptible_kill_exempt_packages";
    private static final String KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES = "imperceptible_kill_exempt_proc_states";
    static final String KEY_KILL_BG_RESTRICTED_CACHED_IDLE = "kill_bg_restricted_cached_idle";
    static final String KEY_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME = "kill_bg_restricted_cached_idle_settle_time";
    private static final String KEY_LOW_SWAP_THRESHOLD_PERCENT = "low_swap_threshold_percent";
    private static final String KEY_MAX_CACHED_PROCESSES = "max_cached_processes";
    private static final String KEY_MAX_EMPTY_TIME_MILLIS = "max_empty_time_millis";
    private static final String KEY_MAX_PHANTOM_PROCESSES = "max_phantom_processes";
    static final String KEY_MAX_PREVIOUS_TIME = "max_previous_time";
    private static final String KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS = "max_service_connections_per_process";
    static final String KEY_MAX_SERVICE_INACTIVITY = "service_max_inactivity";
    static final String KEY_MEMORY_INFO_THROTTLE_TIME = "memory_info_throttle_time";
    private static final String KEY_MIN_ASSOC_LOG_DURATION = "min_assoc_log_duration";
    static final String KEY_MIN_CRASH_INTERVAL = "min_crash_interval";
    static final String KEY_NETWORK_ACCESS_TIMEOUT_MS = "network_access_timeout_ms";
    private static final String KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS = "no_kill_cached_processes_post_boot_completed_duration_millis";
    private static final String KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED = "no_kill_cached_processes_until_boot_completed";
    private static final String KEY_OOMADJ_UPDATE_POLICY = "oomadj_update_policy";
    static final String KEY_PENDINGINTENT_WARNING_THRESHOLD = "pendingintent_warning_threshold";
    private static final String KEY_POWER_CHECK_INTERVAL = "power_check_interval";
    private static final String KEY_POWER_CHECK_MAX_CPU_1 = "power_check_max_cpu_1";
    private static final String KEY_POWER_CHECK_MAX_CPU_2 = "power_check_max_cpu_2";
    private static final String KEY_POWER_CHECK_MAX_CPU_3 = "power_check_max_cpu_3";
    private static final String KEY_POWER_CHECK_MAX_CPU_4 = "power_check_max_cpu_4";
    private static final String KEY_PRIORITIZE_ALARM_BROADCASTS = "prioritize_alarm_broadcasts";
    private static final String KEY_PROACTIVE_KILLS_ENABLED = "proactive_kills_enabled";
    static final String KEY_PROCESS_CRASH_COUNT_LIMIT = "process_crash_count_limit";
    static final String KEY_PROCESS_CRASH_COUNT_RESET_INTERVAL = "process_crash_count_reset_interval";
    private static final String KEY_PROCESS_KILL_TIMEOUT = "process_kill_timeout";
    static final String KEY_PROCESS_START_ASYNC = "process_start_async";
    private static final String KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR = "push_messaging_over_quota_behavior";
    static final String KEY_SERVICE_BG_ACTIVITY_START_TIMEOUT = "service_bg_activity_start_timeout";
    private static final String KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS = "service_bind_almost_perceptible_timeout_ms";
    static final String KEY_SERVICE_MIN_RESTART_TIME_BETWEEN = "service_min_restart_time_between";
    static final String KEY_SERVICE_RESET_RUN_DURATION = "service_reset_run_duration";
    static final String KEY_SERVICE_RESTART_DURATION = "service_restart_duration";
    static final String KEY_SERVICE_RESTART_DURATION_FACTOR = "service_restart_duration_factor";
    private static final String KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS = "service_start_foreground_anr_delay_ms";
    private static final String KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS = "service_start_foreground_timeout_ms";
    private static final String KEY_SERVICE_USAGE_INTERACTION_TIME_POST_S = "service_usage_interaction_time_post_s";
    private static final String KEY_SERVICE_USAGE_INTERACTION_TIME_PRE_S = "service_usage_interaction_time";
    private static final String KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION = "short_fgs_anr_extra_wait_duration";
    private static final String KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION = "short_fgs_proc_state_extra_wait_duration";
    private static final String KEY_SHORT_FGS_TIMEOUT_DURATION = "short_fgs_timeout_duration";
    private static final String KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED = "system_exempt_power_restrictions_enabled";
    static final String KEY_TIERED_CACHED_ADJ_DECAY_TIME = "tiered_cached_adj_decay_time";
    static final String KEY_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = "top_to_almost_perceptible_grace_duration";
    static final String KEY_TOP_TO_FGS_GRACE_DURATION = "top_to_fgs_grace_duration";
    private static final String KEY_USAGE_STATS_INTERACTION_INTERVAL_POST_S = "usage_stats_interaction_interval_post_s";
    private static final String KEY_USAGE_STATS_INTERACTION_INTERVAL_PRE_S = "usage_stats_interaction_interval";
    static final String KEY_USE_MODERN_TRIM = "use_modern_trim";
    static final String KEY_USE_TIERED_CACHED_ADJ = "use_tiered_cached_adj";
    static final String KEY_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION = "vis_to_invis_uij_schedule_grace_duration";
    public static float LOW_SWAP_THRESHOLD_PERCENT = 0.0f;
    public static long MAX_PREVIOUS_TIME = 0;
    public static long MIN_ASSOC_LOG_DURATION = 0;
    private static final long MIN_AUTOMATIC_HEAP_DUMP_PSS_THRESHOLD_BYTES = 102400;
    public static int MIN_CRASH_INTERVAL = 0;
    private static final int OOMADJ_UPDATE_POLICY_QUICK = 1;
    private static final int OOMADJ_UPDATE_POLICY_SLOW = 0;
    public static boolean PROACTIVE_KILLS_ENABLED = false;
    static int PROCESS_CRASH_COUNT_LIMIT = 0;
    static long PROCESS_CRASH_COUNT_RESET_INTERVAL = 0;
    private static final String TAG = "ActivityManagerConstants";
    public long BACKGROUND_SETTLE_TIME;
    public long BG_START_TIMEOUT;
    public long BOUND_SERVICE_CRASH_RESTART_DURATION;
    public long BOUND_SERVICE_MAX_CRASH_RETRY;
    long CONTENT_PROVIDER_RETAIN_TIME;
    public int CUR_MAX_CACHED_PROCESSES;
    public int CUR_MAX_EMPTY_PROCESSES;
    public int CUR_TRIM_CACHED_PROCESSES;
    public int CUR_TRIM_EMPTY_PROCESSES;
    public long FGSERVICE_MIN_REPORT_TIME;
    public long FGSERVICE_MIN_SHOWN_TIME;
    public long FGSERVICE_SCREEN_ON_AFTER_TIME;
    public long FGSERVICE_SCREEN_ON_BEFORE_TIME;
    public boolean FLAG_PROCESS_START_ASYNC;
    boolean FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS;
    long FULL_PSS_LOWERED_INTERVAL;
    long FULL_PSS_MIN_INTERVAL;
    long GC_MIN_INTERVAL;
    long GC_TIMEOUT;
    public ArraySet<String> IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES;
    public ArraySet<Integer> IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES;
    public final ArraySet<ComponentName> KEEP_WARMING_SERVICES;
    public int MAX_CACHED_PROCESSES;
    public int MAX_PHANTOM_PROCESSES;
    public long MAX_SERVICE_INACTIVITY;
    public long MEMORY_INFO_THROTTLE_TIME;
    public boolean OOMADJ_UPDATE_QUICK;
    public int PENDINGINTENT_WARNING_THRESHOLD;
    long POWER_CHECK_INTERVAL;
    int POWER_CHECK_MAX_CPU_1;
    int POWER_CHECK_MAX_CPU_2;
    int POWER_CHECK_MAX_CPU_3;
    int POWER_CHECK_MAX_CPU_4;
    long SERVICE_BACKGROUND_TIMEOUT;
    public long SERVICE_BG_ACTIVITY_START_TIMEOUT;
    public long SERVICE_MIN_RESTART_TIME_BETWEEN;
    public long SERVICE_RESET_RUN_DURATION;
    public long SERVICE_RESTART_DURATION;
    public int SERVICE_RESTART_DURATION_FACTOR;
    public long SERVICE_RESTART_DURATION_LOW;
    long SERVICE_TIMEOUT;
    long SERVICE_USAGE_INTERACTION_TIME_POST_S;
    long SERVICE_USAGE_INTERACTION_TIME_PRE_S;
    public long TIERED_CACHED_ADJ_DECAY_TIME;
    public long TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION;
    public volatile long TOP_TO_FGS_GRACE_DURATION;
    long USAGE_STATS_INTERACTION_INTERVAL_POST_S;
    long USAGE_STATS_INTERACTION_INTERVAL_PRE_S;
    public boolean USE_MODERN_TRIM;
    public boolean USE_TIERED_CACHED_ADJ;
    volatile long mBootTimeTempAllowlistDuration;
    volatile String mComponentAliasOverrides;
    private final int mCustomizedMaxCachedProcesses;
    private final int mDefaultBinderHeavyHitterAutoSamplerBatchSize;
    private final boolean mDefaultBinderHeavyHitterAutoSamplerEnabled;
    private final float mDefaultBinderHeavyHitterAutoSamplerThreshold;
    private final int mDefaultBinderHeavyHitterWatcherBatchSize;
    private final boolean mDefaultBinderHeavyHitterWatcherEnabled;
    private final float mDefaultBinderHeavyHitterWatcherThreshold;
    private List<String> mDefaultImperceptibleKillExemptPackages;
    private List<Integer> mDefaultImperceptibleKillExemptProcStates;

    @GuardedBy({"mService"})
    volatile int mDeferBootCompletedBroadcast;
    volatile boolean mEnableComponentAlias;

    @GuardedBy({"mService"})
    boolean mEnableExtraServiceRestartDelayOnMemPressure;
    public volatile boolean mEnableWaitForFinishAttachApplication;

    @GuardedBy({"mService"})
    long[] mExtraServiceRestartDelayOnMemPressure;
    volatile long mFgToBgFgsGraceDuration;
    volatile boolean mFgsAllowOptOut;
    volatile float mFgsAtomSampleRate;
    volatile long mFgsNotificationDeferralExclusionTime;
    volatile long mFgsNotificationDeferralExclusionTimeForShort;
    volatile long mFgsNotificationDeferralInterval;
    volatile long mFgsNotificationDeferralIntervalForShort;
    volatile float mFgsStartAllowedLogSampleRate;
    volatile float mFgsStartDeniedLogSampleRate;
    volatile long mFgsStartForegroundTimeoutMs;
    volatile boolean mFgsStartRestrictionCheckCallerTargetSdk;
    volatile boolean mFgsStartRestrictionNotificationEnabled;
    volatile boolean mFlagActivityStartsLoggingEnabled;
    volatile boolean mFlagApplicationStartInfoEnabled;
    volatile boolean mFlagBackgroundActivityStartsEnabled;
    volatile boolean mFlagBackgroundFgsStartRestrictionEnabled;
    volatile boolean mFlagFgsNotificationDeferralApiGated;
    volatile boolean mFlagFgsNotificationDeferralEnabled;
    volatile boolean mFlagFgsStartRestrictionEnabled;
    volatile boolean mFlagForegroundServiceStartsLoggingEnabled;
    volatile boolean mFlagSystemExemptPowerRestrictionsEnabled;
    volatile boolean mKillBgRestrictedAndCachedIdle;
    volatile long mKillBgRestrictedAndCachedIdleSettleTimeMs;
    volatile long mMaxEmptyTimeMillis;
    volatile int mMaxServiceConnectionsPerProcess;
    volatile long mNetworkAccessTimeoutMs;
    volatile long mNoKillCachedProcessesPostBootCompletedDurationMillis;
    volatile boolean mNoKillCachedProcessesUntilBootCompleted;
    private final DeviceConfig.OnPropertiesChangedListener mOnDeviceConfigChangedForComponentAliasListener;
    private final DeviceConfig.OnPropertiesChangedListener mOnDeviceConfigChangedListener;
    private int mOverrideMaxCachedProcesses;
    private final KeyValueListParser mParser;
    volatile boolean mPrioritizeAlarmBroadcasts;
    volatile long mProcessKillTimeoutMs;
    volatile int mPushMessagingOverQuotaBehavior;
    private ContentResolver mResolver;
    private final ActivityManagerService mService;
    volatile long mServiceBindAlmostPerceptibleTimeoutMs;
    volatile int mServiceStartForegroundAnrDelayMs;
    volatile int mServiceStartForegroundTimeoutMs;
    public volatile long mShortFgsAnrExtraWaitDuration;
    public volatile long mShortFgsProcStateExtraWaitDuration;
    public volatile long mShortFgsTimeoutDuration;
    private final boolean mSystemServerAutomaticHeapDumpEnabled;
    private final String mSystemServerAutomaticHeapDumpPackageName;
    private long mSystemServerAutomaticHeapDumpPssThresholdBytes;
    volatile long mVisibleToInvisibleUijScheduleGraceDurationMs;

    static {
        DEFAULT_POWER_CHECK_INTERVAL = (ActivityManagerDebugConfig.DEBUG_POWER_QUICK ? 1 : 5) * 60 * 1000;
        DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = new long[]{0, IDeviceIdleControllerExt.ADVANCE_TIME, 20000, 30000};
        long j = Build.HW_TIMEOUT_MULTIPLIER * DEFAULT_BOOT_TIME_TEMP_ALLOWLIST_DURATION;
        DEFAULT_SERVICE_TIMEOUT = j;
        DEFAULT_SERVICE_BACKGROUND_TIMEOUT = j * 10;
        MAX_PREVIOUS_TIME = 60000L;
        MIN_CRASH_INTERVAL = 120000;
        PROCESS_CRASH_COUNT_RESET_INTERVAL = 43200000L;
        PROCESS_CRASH_COUNT_LIMIT = 12;
        ACTIVITY_MANAGER_CONSTANTS_URI = Settings.Global.getUriFor("activity_manager_constants");
        ACTIVITY_STARTS_LOGGING_ENABLED_URI = Settings.Global.getUriFor("activity_starts_logging_enabled");
        FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI = Settings.Global.getUriFor("foreground_service_starts_logging_enabled");
        ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI = Settings.Global.getUriFor("enable_automatic_system_server_heap_dumps");
        MIN_ASSOC_LOG_DURATION = 300000L;
        PROACTIVE_KILLS_ENABLED = false;
        LOW_SWAP_THRESHOLD_PERCENT = DEFAULT_LOW_SWAP_THRESHOLD_PERCENT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManagerConstants(Context context, ActivityManagerService activityManagerService, Handler handler) {
        super(handler);
        this.MAX_CACHED_PROCESSES = 1024;
        this.BACKGROUND_SETTLE_TIME = 60000L;
        this.FGSERVICE_MIN_SHOWN_TIME = DEFAULT_FGSERVICE_MIN_SHOWN_TIME;
        this.FGSERVICE_MIN_REPORT_TIME = 3000L;
        this.FGSERVICE_SCREEN_ON_BEFORE_TIME = 1000L;
        this.FGSERVICE_SCREEN_ON_AFTER_TIME = 5000L;
        this.CONTENT_PROVIDER_RETAIN_TIME = 20000L;
        this.GC_TIMEOUT = 5000L;
        this.GC_MIN_INTERVAL = 60000L;
        this.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = true;
        this.FULL_PSS_MIN_INTERVAL = DEFAULT_FULL_PSS_MIN_INTERVAL;
        this.FULL_PSS_LOWERED_INTERVAL = 300000L;
        this.POWER_CHECK_INTERVAL = DEFAULT_POWER_CHECK_INTERVAL;
        this.POWER_CHECK_MAX_CPU_1 = 25;
        this.POWER_CHECK_MAX_CPU_2 = 25;
        this.POWER_CHECK_MAX_CPU_3 = 10;
        this.POWER_CHECK_MAX_CPU_4 = 2;
        this.SERVICE_USAGE_INTERACTION_TIME_PRE_S = 1800000L;
        this.SERVICE_USAGE_INTERACTION_TIME_POST_S = 60000L;
        this.USAGE_STATS_INTERACTION_INTERVAL_PRE_S = DEFAULT_USAGE_STATS_INTERACTION_INTERVAL_PRE_S;
        this.USAGE_STATS_INTERACTION_INTERVAL_POST_S = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS;
        this.SERVICE_RESTART_DURATION = 1000L;
        this.SERVICE_RESTART_DURATION_LOW = 30000L;
        this.SERVICE_RESET_RUN_DURATION = 60000L;
        this.SERVICE_RESTART_DURATION_FACTOR = 4;
        this.SERVICE_MIN_RESTART_TIME_BETWEEN = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.SERVICE_TIMEOUT = DEFAULT_SERVICE_TIMEOUT;
        this.SERVICE_BACKGROUND_TIMEOUT = DEFAULT_SERVICE_BACKGROUND_TIMEOUT;
        this.MAX_SERVICE_INACTIVITY = 1800000L;
        this.BG_START_TIMEOUT = 15000L;
        this.SERVICE_BG_ACTIVITY_START_TIMEOUT = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.BOUND_SERVICE_CRASH_RESTART_DURATION = 1800000L;
        this.BOUND_SERVICE_MAX_CRASH_RETRY = 16L;
        this.FLAG_PROCESS_START_ASYNC = true;
        this.MEMORY_INFO_THROTTLE_TIME = 300000L;
        this.TOP_TO_FGS_GRACE_DURATION = 15000L;
        this.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = 15000L;
        this.mFlagBackgroundFgsStartRestrictionEnabled = true;
        this.mFlagFgsStartRestrictionEnabled = true;
        this.mFgsStartRestrictionNotificationEnabled = false;
        this.mFgsStartRestrictionCheckCallerTargetSdk = true;
        this.mFlagFgsNotificationDeferralEnabled = true;
        this.mFlagFgsNotificationDeferralApiGated = false;
        this.mFgsNotificationDeferralInterval = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.mFgsNotificationDeferralIntervalForShort = this.mFgsNotificationDeferralInterval;
        this.mFgsNotificationDeferralExclusionTime = 120000L;
        this.mFgsNotificationDeferralExclusionTimeForShort = this.mFgsNotificationDeferralExclusionTime;
        this.mFlagSystemExemptPowerRestrictionsEnabled = true;
        this.mPushMessagingOverQuotaBehavior = 1;
        this.mBootTimeTempAllowlistDuration = 20000L;
        this.mFgToBgFgsGraceDuration = 5000L;
        this.mVisibleToInvisibleUijScheduleGraceDurationMs = 5000L;
        this.mFgsStartForegroundTimeoutMs = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.mFgsAtomSampleRate = 1.0f;
        this.mFgsStartAllowedLogSampleRate = DEFAULT_FGS_START_ALLOWED_LOG_SAMPLE_RATE;
        this.mFgsStartDeniedLogSampleRate = 1.0f;
        this.mKillBgRestrictedAndCachedIdle = true;
        this.mKillBgRestrictedAndCachedIdleSettleTimeMs = 60000L;
        this.mProcessKillTimeoutMs = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.mFgsAllowOptOut = false;
        this.mExtraServiceRestartDelayOnMemPressure = DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE;
        this.mEnableExtraServiceRestartDelayOnMemPressure = true;
        this.mEnableComponentAlias = false;
        this.mDeferBootCompletedBroadcast = 6;
        this.mPrioritizeAlarmBroadcasts = true;
        this.mServiceStartForegroundTimeoutMs = 30000;
        this.mServiceStartForegroundAnrDelayMs = 10000;
        this.mServiceBindAlmostPerceptibleTimeoutMs = 15000L;
        this.mComponentAliasOverrides = "";
        this.mMaxServiceConnectionsPerProcess = 3000;
        this.mParser = new KeyValueListParser(',');
        this.mOverrideMaxCachedProcesses = -1;
        this.mNoKillCachedProcessesUntilBootCompleted = true;
        this.mNoKillCachedProcessesPostBootCompletedDurationMillis = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS;
        this.CUR_TRIM_EMPTY_PROCESSES = computeEmptyProcessLimit(this.MAX_CACHED_PROCESSES) / 2;
        int i = this.MAX_CACHED_PROCESSES;
        this.CUR_TRIM_CACHED_PROCESSES = (i - computeEmptyProcessLimit(i)) / 3;
        this.mMaxEmptyTimeMillis = DEFAULT_MAX_EMPTY_TIME_MILLIS;
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES = new ArraySet<>();
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES = new ArraySet<>();
        this.PENDINGINTENT_WARNING_THRESHOLD = 2000;
        ArraySet<ComponentName> arraySet = new ArraySet<>();
        this.KEEP_WARMING_SERVICES = arraySet;
        this.MAX_PHANTOM_PROCESSES = 32;
        this.mNetworkAccessTimeoutMs = DEFAULT_NETWORK_ACCESS_TIMEOUT_MS;
        this.OOMADJ_UPDATE_QUICK = true;
        this.mShortFgsTimeoutDuration = 180000L;
        this.mShortFgsProcStateExtraWaitDuration = 5000L;
        this.mEnableWaitForFinishAttachApplication = false;
        this.mShortFgsAnrExtraWaitDuration = IDeviceIdleControllerExt.ADVANCE_TIME;
        this.USE_TIERED_CACHED_ADJ = false;
        this.TIERED_CACHED_ADJ_DECAY_TIME = 60000L;
        this.USE_MODERN_TRIM = true;
        this.mOnDeviceConfigChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.ActivityManagerConstants.1
            /* JADX WARN: Failed to find 'out' block for switch in B:7:0x001c. Please report as an issue. */
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                String str;
                Iterator it = properties.getKeyset().iterator();
                while (it.hasNext() && (str = (String) it.next()) != null) {
                    char c = 65535;
                    switch (str.hashCode()) {
                        case -2074391906:
                            if (str.equals(ActivityManagerConstants.KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES)) {
                                c = 0;
                                break;
                            }
                            break;
                        case -2038720731:
                            if (str.equals(ActivityManagerConstants.KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION)) {
                                c = 1;
                                break;
                            }
                            break;
                        case -1996097272:
                            if (str.equals(ActivityManagerConstants.KEY_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME)) {
                                c = 2;
                                break;
                            }
                            break;
                        case -1905817813:
                            if (str.equals(ActivityManagerConstants.KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED)) {
                                c = 3;
                                break;
                            }
                            break;
                        case -1903697007:
                            if (str.equals(ActivityManagerConstants.KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS)) {
                                c = 4;
                                break;
                            }
                            break;
                        case -1830853932:
                            if (str.equals(ActivityManagerConstants.KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE)) {
                                c = 5;
                                break;
                            }
                            break;
                        case -1782036688:
                            if (str.equals(ActivityManagerConstants.KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED)) {
                                c = 6;
                                break;
                            }
                            break;
                        case -1660341473:
                            if (str.equals(ActivityManagerConstants.KEY_FGS_ALLOW_OPT_OUT)) {
                                c = 7;
                                break;
                            }
                            break;
                        case -1640024320:
                            if (str.equals(ActivityManagerConstants.KEY_FGS_START_DENIED_LOG_SAMPLE_RATE)) {
                                c = '\b';
                                break;
                            }
                            break;
                        case -1600089364:
                            if (str.equals(ActivityManagerConstants.KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT)) {
                                c = '\t';
                                break;
                            }
                            break;
                        case -1483050242:
                            if (str.equals(ActivityManagerConstants.KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS)) {
                                c = '\n';
                                break;
                            }
                            break;
                        case -1453165935:
                            if (str.equals(ActivityManagerConstants.KEY_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION)) {
                                c = 11;
                                break;
                            }
                            break;
                        case -1406935837:
                            if (str.equals(ActivityManagerConstants.KEY_OOMADJ_UPDATE_POLICY)) {
                                c = '\f';
                                break;
                            }
                            break;
                        case -1327576198:
                            if (str.equals(ActivityManagerConstants.KEY_MAX_PREVIOUS_TIME)) {
                                c = '\r';
                                break;
                            }
                            break;
                        case -1303617396:
                            if (str.equals(ActivityManagerConstants.KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL)) {
                                c = 14;
                                break;
                            }
                            break;
                        case -1220759920:
                            if (str.equals(ActivityManagerConstants.KEY_MAX_PHANTOM_PROCESSES)) {
                                c = 15;
                                break;
                            }
                            break;
                        case -1213341854:
                            if (str.equals(ActivityManagerConstants.KEY_SHORT_FGS_TIMEOUT_DURATION)) {
                                c = 16;
                                break;
                            }
                            break;
                        case -1198352864:
                            if (str.equals(ActivityManagerConstants.KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED)) {
                                c = 17;
                                break;
                            }
                            break;
                        case -1191409506:
                            if (str.equals(ActivityManagerConstants.KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS)) {
                                c = 18;
                                break;
                            }
                            break;
                        case -1092962821:
                            if (str.equals(ActivityManagerConstants.KEY_MAX_CACHED_PROCESSES)) {
                                c = 19;
                                break;
                            }
                            break;
                        case -1055864341:
                            if (str.equals(ActivityManagerConstants.KEY_MAX_EMPTY_TIME_MILLIS)) {
                                c = 20;
                                break;
                            }
                            break;
                        case -964261074:
                            if (str.equals(ActivityManagerConstants.KEY_NETWORK_ACCESS_TIMEOUT_MS)) {
                                c = 21;
                                break;
                            }
                            break;
                        case -815375578:
                            if (str.equals(ActivityManagerConstants.KEY_MIN_ASSOC_LOG_DURATION)) {
                                c = 22;
                                break;
                            }
                            break;
                        case -769365680:
                            if (str.equals(ActivityManagerConstants.KEY_PROCESS_KILL_TIMEOUT)) {
                                c = 23;
                                break;
                            }
                            break;
                        case -728096000:
                            if (str.equals(ActivityManagerConstants.KEY_PRIORITIZE_ALARM_BROADCASTS)) {
                                c = 24;
                                break;
                            }
                            break;
                        case -682752716:
                            if (str.equals(ActivityManagerConstants.KEY_FGS_ATOM_SAMPLE_RATE)) {
                                c = 25;
                                break;
                            }
                            break;
                        case -577670375:
                            if (str.equals(ActivityManagerConstants.KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS)) {
                                c = 26;
                                break;
                            }
                            break;
                        case -449032007:
                            if (str.equals(ActivityManagerConstants.KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE)) {
                                c = 27;
                                break;
                            }
                            break;
                        case -329920445:
                            if (str.equals(ActivityManagerConstants.KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED)) {
                                c = 28;
                                break;
                            }
                            break;
                        case -292047334:
                            if (str.equals(ActivityManagerConstants.KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED)) {
                                c = 29;
                                break;
                            }
                            break;
                        case -253203740:
                            if (str.equals(ActivityManagerConstants.KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR)) {
                                c = 30;
                                break;
                            }
                            break;
                        case -224039213:
                            if (str.equals(ActivityManagerConstants.KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS)) {
                                c = 31;
                                break;
                            }
                            break;
                        case -216971728:
                            if (str.equals(ActivityManagerConstants.KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED)) {
                                c = ' ';
                                break;
                            }
                            break;
                        case -84078814:
                            if (str.equals(ActivityManagerConstants.KEY_TOP_TO_FGS_GRACE_DURATION)) {
                                c = '!';
                                break;
                            }
                            break;
                        case -48740806:
                            if (str.equals(ActivityManagerConstants.KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES)) {
                                c = '\"';
                                break;
                            }
                            break;
                        case 21817133:
                            if (str.equals(ActivityManagerConstants.KEY_DEFER_BOOT_COMPLETED_BROADCAST)) {
                                c = '#';
                                break;
                            }
                            break;
                        case 66973322:
                            if (str.equals(ActivityManagerConstants.KEY_USE_MODERN_TRIM)) {
                                c = '$';
                                break;
                            }
                            break;
                        case 74597321:
                            if (str.equals(ActivityManagerConstants.KEY_TIERED_CACHED_ADJ_DECAY_TIME)) {
                                c = '%';
                                break;
                            }
                            break;
                        case 102688395:
                            if (str.equals(ActivityManagerConstants.KEY_PROACTIVE_KILLS_ENABLED)) {
                                c = '&';
                                break;
                            }
                            break;
                        case 174194291:
                            if (str.equals(ActivityManagerConstants.KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED)) {
                                c = '\'';
                                break;
                            }
                            break;
                        case 273690789:
                            if (str.equals(ActivityManagerConstants.KEY_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE)) {
                                c = '(';
                                break;
                            }
                            break;
                        case 628754725:
                            if (str.equals(ActivityManagerConstants.KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME)) {
                                c = ')';
                                break;
                            }
                            break;
                        case 886770227:
                            if (str.equals(ActivityManagerConstants.KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK)) {
                                c = '*';
                                break;
                            }
                            break;
                        case 889934779:
                            if (str.equals(ActivityManagerConstants.KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED)) {
                                c = '+';
                                break;
                            }
                            break;
                        case 969545596:
                            if (str.equals(ActivityManagerConstants.KEY_FG_TO_BG_FGS_GRACE_DURATION)) {
                                c = ',';
                                break;
                            }
                            break;
                        case 991356293:
                            if (str.equals(ActivityManagerConstants.KEY_DEFAULT_APPLICATION_START_INFO_ENABLED)) {
                                c = '-';
                                break;
                            }
                            break;
                        case 1113517584:
                            if (str.equals(ActivityManagerConstants.KEY_LOW_SWAP_THRESHOLD_PERCENT)) {
                                c = '.';
                                break;
                            }
                            break;
                        case 1163990130:
                            if (str.equals(ActivityManagerConstants.KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION)) {
                                c = '/';
                                break;
                            }
                            break;
                        case 1199252102:
                            if (str.equals(ActivityManagerConstants.KEY_KILL_BG_RESTRICTED_CACHED_IDLE)) {
                                c = '0';
                                break;
                            }
                            break;
                        case 1239401352:
                            if (str.equals(ActivityManagerConstants.KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION)) {
                                c = '1';
                                break;
                            }
                            break;
                        case 1351914345:
                            if (str.equals(ActivityManagerConstants.KEY_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE)) {
                                c = '2';
                                break;
                            }
                            break;
                        case 1380211165:
                            if (str.equals(ActivityManagerConstants.KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED)) {
                                c = '3';
                                break;
                            }
                            break;
                        case 1444000894:
                            if (str.equals(ActivityManagerConstants.KEY_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION)) {
                                c = '4';
                                break;
                            }
                            break;
                        case 1509836936:
                            if (str.equals(ActivityManagerConstants.KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD)) {
                                c = '5';
                                break;
                            }
                            break;
                        case 1577406544:
                            if (str.equals(ActivityManagerConstants.KEY_USE_TIERED_CACHED_ADJ)) {
                                c = '6';
                                break;
                            }
                            break;
                        case 1598050974:
                            if (str.equals(ActivityManagerConstants.KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED)) {
                                c = '7';
                                break;
                            }
                            break;
                        case 1626346799:
                            if (str.equals(ActivityManagerConstants.KEY_FGS_START_FOREGROUND_TIMEOUT)) {
                                c = '8';
                                break;
                            }
                            break;
                        case 1874204051:
                            if (str.equals(ActivityManagerConstants.KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT)) {
                                c = '9';
                                break;
                            }
                            break;
                        case 1896529156:
                            if (str.equals(ActivityManagerConstants.KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD)) {
                                c = ':';
                                break;
                            }
                            break;
                        case 2013655783:
                            if (str.equals(ActivityManagerConstants.KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS)) {
                                c = ';';
                                break;
                            }
                            break;
                        case 2077421144:
                            if (str.equals(ActivityManagerConstants.KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE)) {
                                c = '<';
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                        case '\"':
                            ActivityManagerConstants.this.updateImperceptibleKillExemptions();
                            break;
                        case 1:
                            ActivityManagerConstants.this.updateShortFgsAnrExtraWaitDuration();
                            break;
                        case 2:
                            ActivityManagerConstants.this.updateKillBgRestrictedCachedIdleSettleTime();
                            break;
                        case 3:
                            ActivityManagerConstants.this.updateFgsStartsRestriction();
                            break;
                        case 4:
                            ActivityManagerConstants.this.updateServiceStartForegroundAnrDealyMs();
                            break;
                        case 5:
                        case 29:
                        case '5':
                        case '7':
                        case ':':
                        case '<':
                            ActivityManagerConstants.this.updateBinderHeavyHitterWatcher();
                            break;
                        case 6:
                            ActivityManagerConstants.this.updateBackgroundActivityStarts();
                            break;
                        case 7:
                            ActivityManagerConstants.this.updateFgsAllowOptOut();
                            break;
                        case '\b':
                            ActivityManagerConstants.this.updateFgsStartDeniedLogSamplePercent();
                            break;
                        case '\t':
                            ActivityManagerConstants.this.updateFgsNotificationDeferralExclusionTimeForShort();
                            break;
                        case '\n':
                            ActivityManagerConstants.this.updateMaxServiceConnectionsPerProcess();
                            break;
                        case 11:
                            ActivityManagerConstants.this.updateFgToBgFgsGraceDuration();
                            break;
                        case '\f':
                            ActivityManagerConstants.this.updateOomAdjUpdatePolicy();
                            break;
                        case '\r':
                            ActivityManagerConstants.this.updateMaxPreviousTime();
                            break;
                        case 14:
                            ActivityManagerConstants.this.updateFgsNotificationDeferralInterval();
                            break;
                        case 15:
                            ActivityManagerConstants.this.updateMaxPhantomProcesses();
                            break;
                        case 16:
                            ActivityManagerConstants.this.updateShortFgsTimeoutDuration();
                            break;
                        case 17:
                            ActivityManagerConstants.this.updateBackgroundFgsStartsRestriction();
                            break;
                        case 18:
                            ActivityManagerConstants.this.updateForceRestrictedBackgroundCheck();
                            break;
                        case 19:
                            ActivityManagerConstants.this.updateMaxCachedProcesses();
                            break;
                        case 20:
                            ActivityManagerConstants.this.updateMaxEmptyTimeMillis();
                            break;
                        case 21:
                            ActivityManagerConstants.this.updateNetworkAccessTimeoutMs();
                            break;
                        case 22:
                            ActivityManagerConstants.this.updateMinAssocLogDuration();
                            break;
                        case 23:
                            ActivityManagerConstants.this.updateProcessKillTimeout();
                            break;
                        case 24:
                            ActivityManagerConstants.this.updatePrioritizeAlarmBroadcasts();
                            break;
                        case 25:
                            ActivityManagerConstants.this.updateFgsAtomSamplePercent();
                            break;
                        case 26:
                            ActivityManagerConstants.this.updateServiceStartForegroundTimeoutMs();
                            break;
                        case 27:
                            ActivityManagerConstants.this.updateFgsStartAllowedLogSamplePercent();
                            break;
                        case 28:
                            ActivityManagerConstants.this.updateFgsStartsRestrictionNotification();
                            break;
                        case 30:
                            ActivityManagerConstants.this.updatePushMessagingOverQuotaBehavior();
                            break;
                        case 31:
                            ActivityManagerConstants.this.updateNoKillCachedProcessesPostBootCompletedDurationMillis();
                            break;
                        case ' ':
                            ActivityManagerConstants.this.updateFgsNotificationDeferralApiGated();
                            break;
                        case '!':
                            ActivityManagerConstants.this.updateTopToFgsGraceDuration();
                            break;
                        case '#':
                            ActivityManagerConstants.this.updateDeferBootCompletedBroadcast();
                            break;
                        case '$':
                            ActivityManagerConstants.this.updateUseModernTrim();
                            break;
                        case '%':
                        case '6':
                            ActivityManagerConstants.this.updateUseTieredCachedAdj();
                            break;
                        case '&':
                            ActivityManagerConstants.this.updateProactiveKillsEnabled();
                            break;
                        case '\'':
                            ActivityManagerConstants.this.updateSystemExemptPowerRestrictionsEnabled();
                            break;
                        case '(':
                            ActivityManagerConstants.this.updateEnableExtraServiceRestartDelayOnMemPressure();
                            break;
                        case ')':
                            ActivityManagerConstants.this.updateFgsNotificationDeferralExclusionTime();
                            break;
                        case '*':
                            ActivityManagerConstants.this.updateFgsStartsRestrictionCheckCallerTargetSdk();
                            break;
                        case '+':
                            ActivityManagerConstants.this.updateNoKillCachedProcessesUntilBootCompleted();
                            break;
                        case ',':
                            ActivityManagerConstants.this.updateFgToBgFgsGraceDuration();
                            break;
                        case '-':
                            ActivityManagerConstants.this.updateApplicationStartInfoEnabled();
                            break;
                        case '.':
                            ActivityManagerConstants.this.updateLowSwapThresholdPercent();
                            break;
                        case '/':
                            ActivityManagerConstants.this.updateBootTimeTempAllowListDuration();
                            break;
                        case '0':
                            ActivityManagerConstants.this.updateKillBgRestrictedCachedIdle();
                            break;
                        case '1':
                            ActivityManagerConstants.this.updateShortFgsProcStateExtraWaitDuration();
                            break;
                        case '2':
                            ActivityManagerConstants.this.updateExtraServiceRestartDelayOnMemPressure();
                            break;
                        case '3':
                            ActivityManagerConstants.this.updateFgsNotificationDeferralEnable();
                            break;
                        case '4':
                            ActivityManagerConstants.this.updateEnableWaitForFinishAttachApplication();
                            break;
                        case '8':
                            ActivityManagerConstants.this.updateFgsStartForegroundTimeout();
                            break;
                        case '9':
                            ActivityManagerConstants.this.updateFgsNotificationDeferralIntervalForShort();
                            break;
                        case ';':
                            ActivityManagerConstants.this.updateServiceBindAlmostPerceptibleTimeoutMs();
                            break;
                        default:
                            ActivityManagerConstants.this.updateFGSPermissionEnforcementFlagsIfNecessary(str);
                            break;
                    }
                }
            }
        };
        this.mOnDeviceConfigChangedForComponentAliasListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.ActivityManagerConstants.2
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                String str;
                Iterator it = properties.getKeyset().iterator();
                while (it.hasNext() && (str = (String) it.next()) != null) {
                    if (str.equals(ActivityManagerConstants.KEY_ENABLE_COMPONENT_ALIAS) || str.equals(ActivityManagerConstants.KEY_COMPONENT_ALIAS_OVERRIDES)) {
                        ActivityManagerConstants.this.updateComponentAliases();
                    }
                }
            }
        };
        this.mService = activityManagerService;
        this.mSystemServerAutomaticHeapDumpEnabled = Build.IS_DEBUGGABLE && context.getResources().getBoolean(R.bool.config_wirelessConsentRequired);
        this.mSystemServerAutomaticHeapDumpPackageName = context.getPackageName();
        this.mSystemServerAutomaticHeapDumpPssThresholdBytes = Math.max(MIN_AUTOMATIC_HEAP_DUMP_PSS_THRESHOLD_BYTES, context.getResources().getInteger(R.integer.config_deskDockKeepsScreenOn));
        this.mDefaultImperceptibleKillExemptPackages = Arrays.asList(context.getResources().getStringArray(R.array.config_displayWhiteBalanceDisplayColorTemperatures));
        this.mDefaultImperceptibleKillExemptProcStates = (List) Arrays.stream(context.getResources().getIntArray(R.array.config_displayWhiteBalanceDisplayNominalWhite)).boxed().collect(Collectors.toList());
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.addAll(this.mDefaultImperceptibleKillExemptPackages);
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.addAll(this.mDefaultImperceptibleKillExemptProcStates);
        boolean z = context.getResources().getBoolean(R.bool.imsServiceAllowTurnOff);
        this.mDefaultBinderHeavyHitterWatcherEnabled = z;
        int integer = context.getResources().getInteger(R.integer.config_displayWhiteBalanceColorTemperatureDefault);
        this.mDefaultBinderHeavyHitterWatcherBatchSize = integer;
        float f = context.getResources().getFloat(R.dimen.config_progressBarCornerRadius);
        this.mDefaultBinderHeavyHitterWatcherThreshold = f;
        boolean z2 = context.getResources().getBoolean(R.bool.editable_voicemailnumber);
        this.mDefaultBinderHeavyHitterAutoSamplerEnabled = z2;
        int integer2 = context.getResources().getInteger(R.integer.config_displayWhiteBalanceBrightnessSensorRate);
        this.mDefaultBinderHeavyHitterAutoSamplerBatchSize = integer2;
        float f2 = context.getResources().getFloat(R.dimen.config_preferredHyphenationFrequency);
        this.mDefaultBinderHeavyHitterAutoSamplerThreshold = f2;
        BINDER_HEAVY_HITTER_WATCHER_ENABLED = z;
        BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = integer;
        BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = f;
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = z2;
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = integer2;
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD = f2;
        activityManagerService.scheduleUpdateBinderHeavyHitterWatcherConfig();
        arraySet.addAll((Collection<? extends ComponentName>) Arrays.stream(context.getResources().getStringArray(R.array.config_tether_wifi_p2p_regexs)).map(new Function() { // from class: com.android.server.am.ActivityManagerConstants$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ComponentName.unflattenFromString((String) obj);
            }
        }).collect(Collectors.toSet()));
        int integer3 = context.getResources().getInteger(R.integer.config_defaultRefreshRate);
        this.mCustomizedMaxCachedProcesses = integer3;
        this.CUR_MAX_CACHED_PROCESSES = integer3;
        this.CUR_MAX_EMPTY_PROCESSES = computeEmptyProcessLimit(integer3);
        int computeEmptyProcessLimit = computeEmptyProcessLimit(Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES));
        this.CUR_TRIM_EMPTY_PROCESSES = computeEmptyProcessLimit / 2;
        this.CUR_TRIM_CACHED_PROCESSES = (Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES) - computeEmptyProcessLimit) / 3;
    }

    public void start(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
        contentResolver.registerContentObserver(ACTIVITY_MANAGER_CONSTANTS_URI, false, this);
        this.mResolver.registerContentObserver(ACTIVITY_STARTS_LOGGING_ENABLED_URI, false, this);
        this.mResolver.registerContentObserver(FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI, false, this);
        if (this.mSystemServerAutomaticHeapDumpEnabled) {
            this.mResolver.registerContentObserver(ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI, false, this);
        }
        updateConstants();
        if (this.mSystemServerAutomaticHeapDumpEnabled) {
            updateEnableAutomaticSystemServerHeapDumps();
        }
        DeviceConfig.addOnPropertiesChangedListener("activity_manager", ActivityThread.currentApplication().getMainExecutor(), this.mOnDeviceConfigChangedListener);
        DeviceConfig.addOnPropertiesChangedListener("activity_manager_ca", ActivityThread.currentApplication().getMainExecutor(), this.mOnDeviceConfigChangedForComponentAliasListener);
        loadDeviceConfigConstants();
        updateActivityStartsLoggingEnabled();
        updateForegroundServiceStartsLoggingEnabled();
    }

    private void loadDeviceConfigConstants() {
        this.mOnDeviceConfigChangedListener.onPropertiesChanged(DeviceConfig.getProperties("activity_manager", new String[0]));
        this.mOnDeviceConfigChangedForComponentAliasListener.onPropertiesChanged(DeviceConfig.getProperties("activity_manager_ca", new String[0]));
    }

    public void setOverrideMaxCachedProcesses(int i) {
        this.mOverrideMaxCachedProcesses = i;
        updateMaxCachedProcesses();
    }

    public int getOverrideMaxCachedProcesses() {
        return this.mOverrideMaxCachedProcesses;
    }

    public static int computeEmptyProcessLimit(int i) {
        return i / 2;
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z, Uri uri) {
        if (uri == null) {
            return;
        }
        if (ACTIVITY_MANAGER_CONSTANTS_URI.equals(uri)) {
            updateConstants();
            return;
        }
        if (ACTIVITY_STARTS_LOGGING_ENABLED_URI.equals(uri)) {
            updateActivityStartsLoggingEnabled();
        } else if (FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI.equals(uri)) {
            updateForegroundServiceStartsLoggingEnabled();
        } else if (ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI.equals(uri)) {
            updateEnableAutomaticSystemServerHeapDumps();
        }
    }

    private void updateConstants() {
        String string = Settings.Global.getString(this.mResolver, "activity_manager_constants");
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                try {
                    this.mParser.setString(string);
                } catch (IllegalArgumentException e) {
                    Slog.e(TAG, "Bad activity manager config settings", e);
                }
                long j = this.POWER_CHECK_INTERVAL;
                this.BACKGROUND_SETTLE_TIME = this.mParser.getLong(KEY_BACKGROUND_SETTLE_TIME, 60000L);
                this.FGSERVICE_MIN_SHOWN_TIME = this.mParser.getLong(KEY_FGSERVICE_MIN_SHOWN_TIME, DEFAULT_FGSERVICE_MIN_SHOWN_TIME);
                this.FGSERVICE_MIN_REPORT_TIME = this.mParser.getLong(KEY_FGSERVICE_MIN_REPORT_TIME, 3000L);
                this.FGSERVICE_SCREEN_ON_BEFORE_TIME = this.mParser.getLong(KEY_FGSERVICE_SCREEN_ON_BEFORE_TIME, 1000L);
                this.FGSERVICE_SCREEN_ON_AFTER_TIME = this.mParser.getLong(KEY_FGSERVICE_SCREEN_ON_AFTER_TIME, 5000L);
                this.CONTENT_PROVIDER_RETAIN_TIME = this.mParser.getLong(KEY_CONTENT_PROVIDER_RETAIN_TIME, 20000L);
                this.GC_TIMEOUT = this.mParser.getLong(KEY_GC_TIMEOUT, 5000L);
                this.GC_MIN_INTERVAL = this.mParser.getLong(KEY_GC_MIN_INTERVAL, 60000L);
                this.FULL_PSS_MIN_INTERVAL = this.mParser.getLong(KEY_FULL_PSS_MIN_INTERVAL, DEFAULT_FULL_PSS_MIN_INTERVAL);
                this.FULL_PSS_LOWERED_INTERVAL = this.mParser.getLong(KEY_FULL_PSS_LOWERED_INTERVAL, 300000L);
                this.POWER_CHECK_INTERVAL = this.mParser.getLong(KEY_POWER_CHECK_INTERVAL, DEFAULT_POWER_CHECK_INTERVAL);
                this.POWER_CHECK_MAX_CPU_1 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_1, 25);
                this.POWER_CHECK_MAX_CPU_2 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_2, 25);
                this.POWER_CHECK_MAX_CPU_3 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_3, 10);
                this.POWER_CHECK_MAX_CPU_4 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_4, 2);
                this.SERVICE_USAGE_INTERACTION_TIME_PRE_S = this.mParser.getLong(KEY_SERVICE_USAGE_INTERACTION_TIME_PRE_S, 1800000L);
                this.SERVICE_USAGE_INTERACTION_TIME_POST_S = this.mParser.getLong(KEY_SERVICE_USAGE_INTERACTION_TIME_POST_S, 60000L);
                this.USAGE_STATS_INTERACTION_INTERVAL_PRE_S = this.mParser.getLong(KEY_USAGE_STATS_INTERACTION_INTERVAL_PRE_S, DEFAULT_USAGE_STATS_INTERACTION_INTERVAL_PRE_S);
                this.USAGE_STATS_INTERACTION_INTERVAL_POST_S = this.mParser.getLong(KEY_USAGE_STATS_INTERACTION_INTERVAL_POST_S, BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS);
                this.SERVICE_RESTART_DURATION = this.mParser.getLong(KEY_SERVICE_RESTART_DURATION, 1000L);
                this.SERVICE_RESET_RUN_DURATION = this.mParser.getLong(KEY_SERVICE_RESET_RUN_DURATION, 60000L);
                this.SERVICE_RESTART_DURATION_FACTOR = this.mParser.getInt(KEY_SERVICE_RESTART_DURATION_FACTOR, 4);
                this.SERVICE_MIN_RESTART_TIME_BETWEEN = this.mParser.getLong(KEY_SERVICE_MIN_RESTART_TIME_BETWEEN, IDeviceIdleControllerExt.ADVANCE_TIME);
                this.MAX_SERVICE_INACTIVITY = this.mParser.getLong(KEY_MAX_SERVICE_INACTIVITY, 1800000L);
                this.BG_START_TIMEOUT = this.mParser.getLong(KEY_BG_START_TIMEOUT, 15000L);
                this.SERVICE_BG_ACTIVITY_START_TIMEOUT = this.mParser.getLong(KEY_SERVICE_BG_ACTIVITY_START_TIMEOUT, IDeviceIdleControllerExt.ADVANCE_TIME);
                this.BOUND_SERVICE_CRASH_RESTART_DURATION = this.mParser.getLong(KEY_BOUND_SERVICE_CRASH_RESTART_DURATION, 1800000L);
                this.BOUND_SERVICE_MAX_CRASH_RETRY = this.mParser.getInt(KEY_BOUND_SERVICE_CRASH_MAX_RETRY, 16);
                this.FLAG_PROCESS_START_ASYNC = this.mParser.getBoolean(KEY_PROCESS_START_ASYNC, true);
                this.MEMORY_INFO_THROTTLE_TIME = this.mParser.getLong(KEY_MEMORY_INFO_THROTTLE_TIME, 300000L);
                this.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = this.mParser.getDurationMillis(KEY_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION, 15000L);
                MIN_CRASH_INTERVAL = this.mParser.getInt(KEY_MIN_CRASH_INTERVAL, 120000);
                this.PENDINGINTENT_WARNING_THRESHOLD = this.mParser.getInt(KEY_PENDINGINTENT_WARNING_THRESHOLD, 2000);
                PROCESS_CRASH_COUNT_RESET_INTERVAL = this.mParser.getInt(KEY_PROCESS_CRASH_COUNT_RESET_INTERVAL, DEFAULT_PROCESS_CRASH_COUNT_RESET_INTERVAL);
                PROCESS_CRASH_COUNT_LIMIT = this.mParser.getInt(KEY_PROCESS_CRASH_COUNT_LIMIT, 12);
                if (this.POWER_CHECK_INTERVAL != j) {
                    this.mService.mHandler.removeMessages(27);
                    this.mService.mHandler.sendMessageDelayed(this.mService.mHandler.obtainMessage(27), this.POWER_CHECK_INTERVAL);
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void updateActivityStartsLoggingEnabled() {
        this.mFlagActivityStartsLoggingEnabled = Settings.Global.getInt(this.mResolver, "activity_starts_logging_enabled", 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateApplicationStartInfoEnabled() {
        this.mFlagApplicationStartInfoEnabled = DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_APPLICATION_START_INFO_ENABLED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBackgroundActivityStarts() {
        this.mFlagBackgroundActivityStartsEnabled = DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED, false);
    }

    private void updateForegroundServiceStartsLoggingEnabled() {
        this.mFlagForegroundServiceStartsLoggingEnabled = Settings.Global.getInt(this.mResolver, "foreground_service_starts_logging_enabled", 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBackgroundFgsStartsRestriction() {
        this.mFlagBackgroundFgsStartRestrictionEnabled = DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartsRestriction() {
        this.mFlagFgsStartRestrictionEnabled = DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartsRestrictionNotification() {
        this.mFgsStartRestrictionNotificationEnabled = DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartsRestrictionCheckCallerTargetSdk() {
        this.mFgsStartRestrictionCheckCallerTargetSdk = DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralEnable() {
        this.mFlagFgsNotificationDeferralEnabled = DeviceConfig.getBoolean("activity_manager", KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralApiGated() {
        this.mFlagFgsNotificationDeferralApiGated = DeviceConfig.getBoolean("activity_manager", KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralInterval() {
        this.mFgsNotificationDeferralInterval = DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL, IDeviceIdleControllerExt.ADVANCE_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralIntervalForShort() {
        this.mFgsNotificationDeferralIntervalForShort = DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT, IDeviceIdleControllerExt.ADVANCE_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralExclusionTime() {
        this.mFgsNotificationDeferralExclusionTime = DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME, 120000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralExclusionTimeForShort() {
        this.mFgsNotificationDeferralExclusionTimeForShort = DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT, 120000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemExemptPowerRestrictionsEnabled() {
        this.mFlagSystemExemptPowerRestrictionsEnabled = DeviceConfig.getBoolean("activity_manager", KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePushMessagingOverQuotaBehavior() {
        this.mPushMessagingOverQuotaBehavior = DeviceConfig.getInt("activity_manager", KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR, 1);
        if (this.mPushMessagingOverQuotaBehavior < -1 || this.mPushMessagingOverQuotaBehavior > 1) {
            this.mPushMessagingOverQuotaBehavior = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOomAdjUpdatePolicy() {
        this.OOMADJ_UPDATE_QUICK = DeviceConfig.getInt("activity_manager", KEY_OOMADJ_UPDATE_POLICY, 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForceRestrictedBackgroundCheck() {
        this.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = DeviceConfig.getBoolean("activity_manager", KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBootTimeTempAllowListDuration() {
        this.mBootTimeTempAllowlistDuration = DeviceConfig.getLong("activity_manager", KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION, 20000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgToBgFgsGraceDuration() {
        this.mFgToBgFgsGraceDuration = DeviceConfig.getLong("activity_manager", KEY_FG_TO_BG_FGS_GRACE_DURATION, 5000L);
    }

    private void updateVisibleToInvisibleUijScheduleGraceDuration() {
        this.mVisibleToInvisibleUijScheduleGraceDurationMs = DeviceConfig.getLong("activity_manager", KEY_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartForegroundTimeout() {
        this.mFgsStartForegroundTimeoutMs = DeviceConfig.getLong("activity_manager", KEY_FGS_START_FOREGROUND_TIMEOUT, IDeviceIdleControllerExt.ADVANCE_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsAtomSamplePercent() {
        this.mFgsAtomSampleRate = DeviceConfig.getFloat("activity_manager", KEY_FGS_ATOM_SAMPLE_RATE, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartAllowedLogSamplePercent() {
        this.mFgsStartAllowedLogSampleRate = DeviceConfig.getFloat("activity_manager", KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE, DEFAULT_FGS_START_ALLOWED_LOG_SAMPLE_RATE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartDeniedLogSamplePercent() {
        this.mFgsStartDeniedLogSampleRate = DeviceConfig.getFloat("activity_manager", KEY_FGS_START_DENIED_LOG_SAMPLE_RATE, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKillBgRestrictedCachedIdle() {
        this.mKillBgRestrictedAndCachedIdle = DeviceConfig.getBoolean("activity_manager", KEY_KILL_BG_RESTRICTED_CACHED_IDLE, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKillBgRestrictedCachedIdleSettleTime() {
        long j = this.mKillBgRestrictedAndCachedIdleSettleTimeMs;
        this.mKillBgRestrictedAndCachedIdleSettleTimeMs = DeviceConfig.getLong("activity_manager", KEY_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME, 60000L);
        if (this.mKillBgRestrictedAndCachedIdleSettleTimeMs < j) {
            this.mService.mHandler.sendEmptyMessageDelayed(58, this.mKillBgRestrictedAndCachedIdleSettleTimeMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsAllowOptOut() {
        this.mFgsAllowOptOut = DeviceConfig.getBoolean("activity_manager", KEY_FGS_ALLOW_OPT_OUT, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExtraServiceRestartDelayOnMemPressure() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                int lastMemoryLevelLocked = this.mService.mAppProfiler.getLastMemoryLevelLocked();
                long[] jArr = this.mExtraServiceRestartDelayOnMemPressure;
                long[] parseLongArray = parseLongArray(KEY_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE, DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE);
                this.mExtraServiceRestartDelayOnMemPressure = parseLongArray;
                this.mService.mServices.performRescheduleServiceRestartOnMemoryPressureLocked(parseLongArray[lastMemoryLevelLocked], jArr[lastMemoryLevelLocked], "config", SystemClock.uptimeMillis());
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnableExtraServiceRestartDelayOnMemPressure() {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                boolean z = this.mEnableExtraServiceRestartDelayOnMemPressure;
                boolean z2 = DeviceConfig.getBoolean("activity_manager", KEY_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE, true);
                this.mEnableExtraServiceRestartDelayOnMemPressure = z2;
                this.mService.mServices.rescheduleServiceRestartOnMemoryPressureIfNeededLocked(z, z2, SystemClock.uptimeMillis());
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePrioritizeAlarmBroadcasts() {
        String string = DeviceConfig.getString("activity_manager", KEY_PRIORITIZE_ALARM_BROADCASTS, "");
        this.mPrioritizeAlarmBroadcasts = TextUtils.isEmpty(string) ? true : Boolean.parseBoolean(string);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeferBootCompletedBroadcast() {
        this.mDeferBootCompletedBroadcast = DeviceConfig.getInt("activity_manager", KEY_DEFER_BOOT_COMPLETED_BROADCAST, 6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNoKillCachedProcessesUntilBootCompleted() {
        this.mNoKillCachedProcessesUntilBootCompleted = DeviceConfig.getBoolean("activity_manager", KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNoKillCachedProcessesPostBootCompletedDurationMillis() {
        this.mNoKillCachedProcessesPostBootCompletedDurationMillis = DeviceConfig.getLong("activity_manager", KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS, BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxEmptyTimeMillis() {
        this.mMaxEmptyTimeMillis = DeviceConfig.getLong("activity_manager", KEY_MAX_EMPTY_TIME_MILLIS, DEFAULT_MAX_EMPTY_TIME_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkAccessTimeoutMs() {
        this.mNetworkAccessTimeoutMs = DeviceConfig.getLong("activity_manager", KEY_NETWORK_ACCESS_TIMEOUT_MS, DEFAULT_NETWORK_ACCESS_TIMEOUT_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceStartForegroundTimeoutMs() {
        this.mServiceStartForegroundTimeoutMs = DeviceConfig.getInt("activity_manager", KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS, 30000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceStartForegroundAnrDealyMs() {
        this.mServiceStartForegroundAnrDelayMs = DeviceConfig.getInt("activity_manager", KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS, 10000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceBindAlmostPerceptibleTimeoutMs() {
        this.mServiceBindAlmostPerceptibleTimeoutMs = DeviceConfig.getLong("activity_manager", KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS, 15000L);
    }

    private long[] parseLongArray(String str, long[] jArr) {
        String string = DeviceConfig.getString("activity_manager", str, (String) null);
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split(",");
            if (split.length == jArr.length) {
                long[] jArr2 = new long[split.length];
                for (int i = 0; i < split.length; i++) {
                    try {
                        jArr2[i] = Long.parseLong(split[i]);
                    } catch (NumberFormatException unused) {
                    }
                }
                return jArr2;
            }
        }
        return jArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateComponentAliases() {
        this.mEnableComponentAlias = DeviceConfig.getBoolean("activity_manager_ca", KEY_ENABLE_COMPONENT_ALIAS, false);
        this.mComponentAliasOverrides = DeviceConfig.getString("activity_manager_ca", KEY_COMPONENT_ALIAS_OVERRIDES, "");
        this.mService.mComponentAliasResolver.update(this.mEnableComponentAlias, this.mComponentAliasOverrides);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProcessKillTimeout() {
        this.mProcessKillTimeoutMs = DeviceConfig.getLong("activity_manager", KEY_PROCESS_KILL_TIMEOUT, IDeviceIdleControllerExt.ADVANCE_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImperceptibleKillExemptions() {
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.clear();
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.addAll(this.mDefaultImperceptibleKillExemptPackages);
        String string = DeviceConfig.getString("activity_manager", KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES, (String) null);
        if (!TextUtils.isEmpty(string)) {
            this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.addAll(Arrays.asList(string.split(",")));
        }
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.clear();
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.addAll(this.mDefaultImperceptibleKillExemptProcStates);
        String string2 = DeviceConfig.getString("activity_manager", KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES, (String) null);
        if (TextUtils.isEmpty(string2)) {
            return;
        }
        Arrays.asList(string2.split(",")).stream().forEach(new Consumer() { // from class: com.android.server.am.ActivityManagerConstants$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityManagerConstants.this.lambda$updateImperceptibleKillExemptions$0((String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateImperceptibleKillExemptions$0(String str) {
        try {
            this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.add(Integer.valueOf(Integer.parseInt(str)));
        } catch (NumberFormatException unused) {
        }
    }

    private void updateEnableAutomaticSystemServerHeapDumps() {
        if (!this.mSystemServerAutomaticHeapDumpEnabled) {
            Slog.wtf(TAG, "updateEnableAutomaticSystemServerHeapDumps called when leak detection disabled");
        } else {
            this.mService.setDumpHeapDebugLimit(null, 0, Settings.Global.getInt(this.mResolver, "enable_automatic_system_server_heap_dumps", 1) == 1 ? this.mSystemServerAutomaticHeapDumpPssThresholdBytes : 0L, this.mSystemServerAutomaticHeapDumpPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxCachedProcesses() {
        String property = DeviceConfig.getProperty("activity_manager", KEY_MAX_CACHED_PROCESSES);
        try {
            int i = this.mOverrideMaxCachedProcesses;
            if (i < 0) {
                i = TextUtils.isEmpty(property) ? this.mCustomizedMaxCachedProcesses : Integer.parseInt(property);
            }
            this.CUR_MAX_CACHED_PROCESSES = i;
        } catch (NumberFormatException e) {
            Slog.e(TAG, "Unable to parse flag for max_cached_processes: " + property, e);
            this.CUR_MAX_CACHED_PROCESSES = this.mCustomizedMaxCachedProcesses;
        }
        this.CUR_MAX_EMPTY_PROCESSES = computeEmptyProcessLimit(this.CUR_MAX_CACHED_PROCESSES);
        int computeEmptyProcessLimit = computeEmptyProcessLimit(Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES));
        this.CUR_TRIM_EMPTY_PROCESSES = computeEmptyProcessLimit / 2;
        this.CUR_TRIM_CACHED_PROCESSES = (Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES) - computeEmptyProcessLimit) / 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProactiveKillsEnabled() {
        PROACTIVE_KILLS_ENABLED = DeviceConfig.getBoolean("activity_manager", KEY_PROACTIVE_KILLS_ENABLED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLowSwapThresholdPercent() {
        LOW_SWAP_THRESHOLD_PERCENT = DeviceConfig.getFloat("activity_manager", KEY_LOW_SWAP_THRESHOLD_PERCENT, DEFAULT_LOW_SWAP_THRESHOLD_PERCENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTopToFgsGraceDuration() {
        this.TOP_TO_FGS_GRACE_DURATION = DeviceConfig.getLong("activity_manager", KEY_TOP_TO_FGS_GRACE_DURATION, 15000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxPreviousTime() {
        MAX_PREVIOUS_TIME = DeviceConfig.getLong("activity_manager", KEY_MAX_PREVIOUS_TIME, 60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMinAssocLogDuration() {
        MIN_ASSOC_LOG_DURATION = DeviceConfig.getLong("activity_manager", KEY_MIN_ASSOC_LOG_DURATION, 300000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBinderHeavyHitterWatcher() {
        BINDER_HEAVY_HITTER_WATCHER_ENABLED = DeviceConfig.getBoolean("activity_manager", KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED, this.mDefaultBinderHeavyHitterWatcherEnabled);
        BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = DeviceConfig.getInt("activity_manager", KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE, this.mDefaultBinderHeavyHitterWatcherBatchSize);
        BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = DeviceConfig.getFloat("activity_manager", KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD, this.mDefaultBinderHeavyHitterWatcherThreshold);
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = DeviceConfig.getBoolean("activity_manager", KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED, this.mDefaultBinderHeavyHitterAutoSamplerEnabled);
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = DeviceConfig.getInt("activity_manager", KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE, this.mDefaultBinderHeavyHitterAutoSamplerBatchSize);
        BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = DeviceConfig.getFloat("activity_manager", KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD, this.mDefaultBinderHeavyHitterAutoSamplerThreshold);
        this.mService.scheduleUpdateBinderHeavyHitterWatcherConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxPhantomProcesses() {
        int i = this.MAX_PHANTOM_PROCESSES;
        int i2 = DeviceConfig.getInt("activity_manager", KEY_MAX_PHANTOM_PROCESSES, 32);
        this.MAX_PHANTOM_PROCESSES = i2;
        if (i > i2) {
            ActivityManagerService activityManagerService = this.mService;
            ActivityManagerService.MainHandler mainHandler = activityManagerService.mHandler;
            PhantomProcessList phantomProcessList = activityManagerService.mPhantomProcessList;
            Objects.requireNonNull(phantomProcessList);
            mainHandler.post(new ActivityManagerConstants$$ExternalSyntheticLambda2(phantomProcessList));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxServiceConnectionsPerProcess() {
        this.mMaxServiceConnectionsPerProcess = DeviceConfig.getInt("activity_manager", KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS, 3000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShortFgsTimeoutDuration() {
        this.mShortFgsTimeoutDuration = DeviceConfig.getLong("activity_manager", KEY_SHORT_FGS_TIMEOUT_DURATION, 180000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShortFgsProcStateExtraWaitDuration() {
        this.mShortFgsProcStateExtraWaitDuration = DeviceConfig.getLong("activity_manager", KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShortFgsAnrExtraWaitDuration() {
        this.mShortFgsAnrExtraWaitDuration = DeviceConfig.getLong("activity_manager", KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION, IDeviceIdleControllerExt.ADVANCE_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnableWaitForFinishAttachApplication() {
        this.mEnableWaitForFinishAttachApplication = DeviceConfig.getBoolean("activity_manager", KEY_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseTieredCachedAdj() {
        this.USE_TIERED_CACHED_ADJ = DeviceConfig.getBoolean("activity_manager", KEY_USE_TIERED_CACHED_ADJ, false);
        this.TIERED_CACHED_ADJ_DECAY_TIME = DeviceConfig.getLong("activity_manager", KEY_TIERED_CACHED_ADJ_DECAY_TIME, 60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseModernTrim() {
        this.USE_MODERN_TRIM = DeviceConfig.getBoolean("activity_manager", KEY_USE_MODERN_TRIM, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFGSPermissionEnforcementFlagsIfNecessary(String str) {
        ForegroundServiceTypePolicy.getDefaultPolicy().updatePermissionEnforcementFlagIfNecessary(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NeverCompile
    public void dump(PrintWriter printWriter) {
        printWriter.println("ACTIVITY MANAGER SETTINGS (dumpsys activity settings) activity_manager_constants:");
        printWriter.print("  ");
        printWriter.print(KEY_MAX_CACHED_PROCESSES);
        printWriter.print("=");
        printWriter.println(this.MAX_CACHED_PROCESSES);
        printWriter.print("  ");
        printWriter.print(KEY_BACKGROUND_SETTLE_TIME);
        printWriter.print("=");
        printWriter.println(this.BACKGROUND_SETTLE_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_FGSERVICE_MIN_SHOWN_TIME);
        printWriter.print("=");
        printWriter.println(this.FGSERVICE_MIN_SHOWN_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_FGSERVICE_MIN_REPORT_TIME);
        printWriter.print("=");
        printWriter.println(this.FGSERVICE_MIN_REPORT_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_FGSERVICE_SCREEN_ON_BEFORE_TIME);
        printWriter.print("=");
        printWriter.println(this.FGSERVICE_SCREEN_ON_BEFORE_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_FGSERVICE_SCREEN_ON_AFTER_TIME);
        printWriter.print("=");
        printWriter.println(this.FGSERVICE_SCREEN_ON_AFTER_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_CONTENT_PROVIDER_RETAIN_TIME);
        printWriter.print("=");
        printWriter.println(this.CONTENT_PROVIDER_RETAIN_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_GC_TIMEOUT);
        printWriter.print("=");
        printWriter.println(this.GC_TIMEOUT);
        printWriter.print("  ");
        printWriter.print(KEY_GC_MIN_INTERVAL);
        printWriter.print("=");
        printWriter.println(this.GC_MIN_INTERVAL);
        printWriter.print("  ");
        printWriter.print(KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS);
        printWriter.print("=");
        printWriter.println(this.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS);
        printWriter.print("  ");
        printWriter.print(KEY_FULL_PSS_MIN_INTERVAL);
        printWriter.print("=");
        printWriter.println(this.FULL_PSS_MIN_INTERVAL);
        printWriter.print("  ");
        printWriter.print(KEY_FULL_PSS_LOWERED_INTERVAL);
        printWriter.print("=");
        printWriter.println(this.FULL_PSS_LOWERED_INTERVAL);
        printWriter.print("  ");
        printWriter.print(KEY_POWER_CHECK_INTERVAL);
        printWriter.print("=");
        printWriter.println(this.POWER_CHECK_INTERVAL);
        printWriter.print("  ");
        printWriter.print(KEY_POWER_CHECK_MAX_CPU_1);
        printWriter.print("=");
        printWriter.println(this.POWER_CHECK_MAX_CPU_1);
        printWriter.print("  ");
        printWriter.print(KEY_POWER_CHECK_MAX_CPU_2);
        printWriter.print("=");
        printWriter.println(this.POWER_CHECK_MAX_CPU_2);
        printWriter.print("  ");
        printWriter.print(KEY_POWER_CHECK_MAX_CPU_3);
        printWriter.print("=");
        printWriter.println(this.POWER_CHECK_MAX_CPU_3);
        printWriter.print("  ");
        printWriter.print(KEY_POWER_CHECK_MAX_CPU_4);
        printWriter.print("=");
        printWriter.println(this.POWER_CHECK_MAX_CPU_4);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_USAGE_INTERACTION_TIME_PRE_S);
        printWriter.print("=");
        printWriter.println(this.SERVICE_USAGE_INTERACTION_TIME_PRE_S);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_USAGE_INTERACTION_TIME_POST_S);
        printWriter.print("=");
        printWriter.println(this.SERVICE_USAGE_INTERACTION_TIME_POST_S);
        printWriter.print("  ");
        printWriter.print(KEY_USAGE_STATS_INTERACTION_INTERVAL_PRE_S);
        printWriter.print("=");
        printWriter.println(this.USAGE_STATS_INTERACTION_INTERVAL_PRE_S);
        printWriter.print("  ");
        printWriter.print(KEY_USAGE_STATS_INTERACTION_INTERVAL_POST_S);
        printWriter.print("=");
        printWriter.println(this.USAGE_STATS_INTERACTION_INTERVAL_POST_S);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_RESTART_DURATION);
        printWriter.print("=");
        printWriter.println(this.SERVICE_RESTART_DURATION);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_RESET_RUN_DURATION);
        printWriter.print("=");
        printWriter.println(this.SERVICE_RESET_RUN_DURATION);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_RESTART_DURATION_FACTOR);
        printWriter.print("=");
        printWriter.println(this.SERVICE_RESTART_DURATION_FACTOR);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_MIN_RESTART_TIME_BETWEEN);
        printWriter.print("=");
        printWriter.println(this.SERVICE_MIN_RESTART_TIME_BETWEEN);
        printWriter.print("  ");
        printWriter.print(KEY_MAX_SERVICE_INACTIVITY);
        printWriter.print("=");
        printWriter.println(this.MAX_SERVICE_INACTIVITY);
        printWriter.print("  ");
        printWriter.print(KEY_BG_START_TIMEOUT);
        printWriter.print("=");
        printWriter.println(this.BG_START_TIMEOUT);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_BG_ACTIVITY_START_TIMEOUT);
        printWriter.print("=");
        printWriter.println(this.SERVICE_BG_ACTIVITY_START_TIMEOUT);
        printWriter.print("  ");
        printWriter.print(KEY_BOUND_SERVICE_CRASH_RESTART_DURATION);
        printWriter.print("=");
        printWriter.println(this.BOUND_SERVICE_CRASH_RESTART_DURATION);
        printWriter.print("  ");
        printWriter.print(KEY_BOUND_SERVICE_CRASH_MAX_RETRY);
        printWriter.print("=");
        printWriter.println(this.BOUND_SERVICE_MAX_CRASH_RETRY);
        printWriter.print("  ");
        printWriter.print(KEY_PROCESS_START_ASYNC);
        printWriter.print("=");
        printWriter.println(this.FLAG_PROCESS_START_ASYNC);
        printWriter.print("  ");
        printWriter.print(KEY_MEMORY_INFO_THROTTLE_TIME);
        printWriter.print("=");
        printWriter.println(this.MEMORY_INFO_THROTTLE_TIME);
        printWriter.print("  ");
        printWriter.print(KEY_TOP_TO_FGS_GRACE_DURATION);
        printWriter.print("=");
        printWriter.println(this.TOP_TO_FGS_GRACE_DURATION);
        printWriter.print("  ");
        printWriter.print(KEY_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION);
        printWriter.print("=");
        printWriter.println(this.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION);
        printWriter.print("  ");
        printWriter.print(KEY_MIN_CRASH_INTERVAL);
        printWriter.print("=");
        printWriter.println(MIN_CRASH_INTERVAL);
        printWriter.print("  ");
        printWriter.print(KEY_PROCESS_CRASH_COUNT_RESET_INTERVAL);
        printWriter.print("=");
        printWriter.println(PROCESS_CRASH_COUNT_RESET_INTERVAL);
        printWriter.print("  ");
        printWriter.print(KEY_PROCESS_CRASH_COUNT_LIMIT);
        printWriter.print("=");
        printWriter.println(PROCESS_CRASH_COUNT_LIMIT);
        printWriter.print("  ");
        printWriter.print(KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES);
        printWriter.print("=");
        printWriter.println(Arrays.toString(this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.toArray()));
        printWriter.print("  ");
        printWriter.print(KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES);
        printWriter.print("=");
        printWriter.println(Arrays.toString(this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.toArray()));
        printWriter.print("  ");
        printWriter.print(KEY_MIN_ASSOC_LOG_DURATION);
        printWriter.print("=");
        printWriter.println(MIN_ASSOC_LOG_DURATION);
        printWriter.print("  ");
        printWriter.print(KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED);
        printWriter.print("=");
        printWriter.println(BINDER_HEAVY_HITTER_WATCHER_ENABLED);
        printWriter.print("  ");
        printWriter.print(KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE);
        printWriter.print("=");
        printWriter.println(BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE);
        printWriter.print("  ");
        printWriter.print(KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD);
        printWriter.print("=");
        printWriter.println(BINDER_HEAVY_HITTER_WATCHER_THRESHOLD);
        printWriter.print("  ");
        printWriter.print(KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED);
        printWriter.print("=");
        printWriter.println(BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED);
        printWriter.print("  ");
        printWriter.print(KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE);
        printWriter.print("=");
        printWriter.println(BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE);
        printWriter.print("  ");
        printWriter.print(KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD);
        printWriter.print("=");
        printWriter.println(BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD);
        printWriter.print("  ");
        printWriter.print(KEY_MAX_PHANTOM_PROCESSES);
        printWriter.print("=");
        printWriter.println(this.MAX_PHANTOM_PROCESSES);
        printWriter.print("  ");
        printWriter.print(KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION);
        printWriter.print("=");
        printWriter.println(this.mBootTimeTempAllowlistDuration);
        printWriter.print("  ");
        printWriter.print(KEY_FG_TO_BG_FGS_GRACE_DURATION);
        printWriter.print("=");
        printWriter.println(this.mFgToBgFgsGraceDuration);
        printWriter.print("  ");
        printWriter.print(KEY_FGS_START_FOREGROUND_TIMEOUT);
        printWriter.print("=");
        printWriter.println(this.mFgsStartForegroundTimeoutMs);
        printWriter.print("  ");
        printWriter.print(KEY_DEFAULT_APPLICATION_START_INFO_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFlagApplicationStartInfoEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFlagBackgroundActivityStartsEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFlagBackgroundFgsStartRestrictionEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFlagFgsStartRestrictionEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFgsStartRestrictionNotificationEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK);
        printWriter.print("=");
        printWriter.println(this.mFgsStartRestrictionCheckCallerTargetSdk);
        printWriter.print("  ");
        printWriter.print(KEY_FGS_ATOM_SAMPLE_RATE);
        printWriter.print("=");
        printWriter.println(this.mFgsAtomSampleRate);
        printWriter.print("  ");
        printWriter.print(KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE);
        printWriter.print("=");
        printWriter.println(this.mFgsStartAllowedLogSampleRate);
        printWriter.print("  ");
        printWriter.print(KEY_FGS_START_DENIED_LOG_SAMPLE_RATE);
        printWriter.print("=");
        printWriter.println(this.mFgsStartDeniedLogSampleRate);
        printWriter.print("  ");
        printWriter.print(KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR);
        printWriter.print("=");
        printWriter.println(this.mPushMessagingOverQuotaBehavior);
        printWriter.print("  ");
        printWriter.print(KEY_FGS_ALLOW_OPT_OUT);
        printWriter.print("=");
        printWriter.println(this.mFgsAllowOptOut);
        printWriter.print("  ");
        printWriter.print(KEY_ENABLE_COMPONENT_ALIAS);
        printWriter.print("=");
        printWriter.println(this.mEnableComponentAlias);
        printWriter.print("  ");
        printWriter.print(KEY_COMPONENT_ALIAS_OVERRIDES);
        printWriter.print("=");
        printWriter.println(this.mComponentAliasOverrides);
        printWriter.print("  ");
        printWriter.print(KEY_DEFER_BOOT_COMPLETED_BROADCAST);
        printWriter.print("=");
        printWriter.println(this.mDeferBootCompletedBroadcast);
        printWriter.print("  ");
        printWriter.print(KEY_PRIORITIZE_ALARM_BROADCASTS);
        printWriter.print("=");
        printWriter.println(this.mPrioritizeAlarmBroadcasts);
        printWriter.print("  ");
        printWriter.print(KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED);
        printWriter.print("=");
        printWriter.println(this.mNoKillCachedProcessesUntilBootCompleted);
        printWriter.print("  ");
        printWriter.print(KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS);
        printWriter.print("=");
        printWriter.println(this.mNoKillCachedProcessesPostBootCompletedDurationMillis);
        printWriter.print("  ");
        printWriter.print(KEY_MAX_EMPTY_TIME_MILLIS);
        printWriter.print("=");
        printWriter.println(this.mMaxEmptyTimeMillis);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS);
        printWriter.print("=");
        printWriter.println(this.mServiceStartForegroundTimeoutMs);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS);
        printWriter.print("=");
        printWriter.println(this.mServiceStartForegroundAnrDelayMs);
        printWriter.print("  ");
        printWriter.print(KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS);
        printWriter.print("=");
        printWriter.println(this.mServiceBindAlmostPerceptibleTimeoutMs);
        printWriter.print("  ");
        printWriter.print(KEY_NETWORK_ACCESS_TIMEOUT_MS);
        printWriter.print("=");
        printWriter.println(this.mNetworkAccessTimeoutMs);
        printWriter.print("  ");
        printWriter.print(KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS);
        printWriter.print("=");
        printWriter.println(this.mMaxServiceConnectionsPerProcess);
        printWriter.print("  ");
        printWriter.print(KEY_PROACTIVE_KILLS_ENABLED);
        printWriter.print("=");
        printWriter.println(PROACTIVE_KILLS_ENABLED);
        printWriter.print("  ");
        printWriter.print(KEY_LOW_SWAP_THRESHOLD_PERCENT);
        printWriter.print("=");
        printWriter.println(LOW_SWAP_THRESHOLD_PERCENT);
        printWriter.print("  ");
        printWriter.print(KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFlagFgsNotificationDeferralEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED);
        printWriter.print("=");
        printWriter.println(this.mFlagFgsNotificationDeferralApiGated);
        printWriter.print("  ");
        printWriter.print(KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL);
        printWriter.print("=");
        printWriter.println(this.mFgsNotificationDeferralInterval);
        printWriter.print("  ");
        printWriter.print(KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT);
        printWriter.print("=");
        printWriter.println(this.mFgsNotificationDeferralIntervalForShort);
        printWriter.print("  ");
        printWriter.print(KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME);
        printWriter.print("=");
        printWriter.println(this.mFgsNotificationDeferralExclusionTime);
        printWriter.print("  ");
        printWriter.print(KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT);
        printWriter.print("=");
        printWriter.println(this.mFgsNotificationDeferralExclusionTimeForShort);
        printWriter.print("  ");
        printWriter.print(KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED);
        printWriter.print("=");
        printWriter.println(this.mFlagSystemExemptPowerRestrictionsEnabled);
        printWriter.print("  ");
        printWriter.print(KEY_SHORT_FGS_TIMEOUT_DURATION);
        printWriter.print("=");
        printWriter.println(this.mShortFgsTimeoutDuration);
        printWriter.print("  ");
        printWriter.print(KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION);
        printWriter.print("=");
        printWriter.println(this.mShortFgsProcStateExtraWaitDuration);
        printWriter.print("  ");
        printWriter.print(KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION);
        printWriter.print("=");
        printWriter.println(this.mShortFgsAnrExtraWaitDuration);
        printWriter.print("  ");
        printWriter.print(KEY_USE_TIERED_CACHED_ADJ);
        printWriter.print("=");
        printWriter.println(this.USE_TIERED_CACHED_ADJ);
        printWriter.print("  ");
        printWriter.print(KEY_TIERED_CACHED_ADJ_DECAY_TIME);
        printWriter.print("=");
        printWriter.println(this.TIERED_CACHED_ADJ_DECAY_TIME);
        printWriter.println();
        if (this.mOverrideMaxCachedProcesses >= 0) {
            printWriter.print("  mOverrideMaxCachedProcesses=");
            printWriter.println(this.mOverrideMaxCachedProcesses);
        }
        printWriter.print("  mCustomizedMaxCachedProcesses=");
        printWriter.println(this.mCustomizedMaxCachedProcesses);
        printWriter.print("  CUR_MAX_CACHED_PROCESSES=");
        printWriter.println(this.CUR_MAX_CACHED_PROCESSES);
        printWriter.print("  CUR_MAX_EMPTY_PROCESSES=");
        printWriter.println(this.CUR_MAX_EMPTY_PROCESSES);
        printWriter.print("  CUR_TRIM_EMPTY_PROCESSES=");
        printWriter.println(this.CUR_TRIM_EMPTY_PROCESSES);
        printWriter.print("  CUR_TRIM_CACHED_PROCESSES=");
        printWriter.println(this.CUR_TRIM_CACHED_PROCESSES);
        printWriter.print("  OOMADJ_UPDATE_QUICK=");
        printWriter.println(this.OOMADJ_UPDATE_QUICK);
        printWriter.print("  ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION=");
        printWriter.println(this.mEnableWaitForFinishAttachApplication);
    }
}
