package com.android.server.am;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.IndentingPrintWriter;
import android.util.KeyValueListParser;
import android.util.Slog;
import android.util.TimeUtils;
import dalvik.annotation.optimization.NeverCompile;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BroadcastConstants {
    private static final long DEFAULT_ALLOW_BG_ACTIVITY_START_TIMEOUT;
    private static final boolean DEFAULT_CORE_DEFER_UNTIL_ACTIVE = true;
    private static final long DEFAULT_DEFERRAL;
    private static final float DEFAULT_DEFERRAL_DECAY_FACTOR = 0.75f;
    private static final long DEFAULT_DEFERRAL_FLOOR = 0;
    private static final long DEFAULT_DELAY_CACHED_MILLIS = 120000;
    private static final long DEFAULT_DELAY_FOREGROUND_PROC_MILLIS = -120000;
    private static final long DEFAULT_DELAY_NORMAL_MILLIS = 500;
    private static final long DEFAULT_DELAY_PERSISTENT_PROC_MILLIS = -120000;
    private static final long DEFAULT_DELAY_URGENT_MILLIS = -120000;
    private static final int DEFAULT_EXTRA_RUNNING_URGENT_PROCESS_QUEUES = 1;
    private static final int DEFAULT_MAX_CONSECUTIVE_NORMAL_DISPATCHES = 10;
    private static final int DEFAULT_MAX_CONSECUTIVE_URGENT_DISPATCHES = 3;
    private static final int DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS;
    private static final int DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS;
    private static final int DEFAULT_MAX_HISTORY_COMPLETE_SIZE;
    private static final int DEFAULT_MAX_HISTORY_SUMMARY_SIZE;
    private static final int DEFAULT_MAX_PENDING_BROADCASTS;
    private static final int DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS;
    private static final int DEFAULT_MAX_RUNNING_PROCESS_QUEUES;
    private static final boolean DEFAULT_MODERN_QUEUE_ENABLED = true;
    private static final long DEFAULT_PENDING_COLD_START_CHECK_INTERVAL_MILLIS = 30000;
    private static final long DEFAULT_SLOW_TIME;
    private static final long DEFAULT_TIMEOUT;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_ALL = 1;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_BACKGROUND_RESTRICTED_ONLY = 2;
    static final long DEFER_BOOT_COMPLETED_BROADCAST_CHANGE_ID = 203704822;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_NONE = 0;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_TARGET_T_ONLY = 4;
    static final String KEY_ALLOW_BG_ACTIVITY_START_TIMEOUT = "bcast_allow_bg_activity_start_timeout";
    private static final String KEY_CORE_DEFER_UNTIL_ACTIVE = "bcast_core_defer_until_active";
    private static final String KEY_CORE_MAX_RUNNING_BLOCKING_BROADCASTS = "bcast_max_core_running_blocking_broadcasts";
    private static final String KEY_CORE_MAX_RUNNING_NON_BLOCKING_BROADCASTS = "bcast_max_core_running_non_blocking_broadcasts";
    static final String KEY_DEFERRAL = "bcast_deferral";
    static final String KEY_DEFERRAL_DECAY_FACTOR = "bcast_deferral_decay_factor";
    static final String KEY_DEFERRAL_FLOOR = "bcast_deferral_floor";
    private static final String KEY_DELAY_CACHED_MILLIS = "bcast_delay_cached_millis";
    private static final String KEY_DELAY_FOREGROUND_PROC_MILLIS = "bcast_delay_foreground_proc_millis";
    private static final String KEY_DELAY_NORMAL_MILLIS = "bcast_delay_normal_millis";
    private static final String KEY_DELAY_PERSISTENT_PROC_MILLIS = "bcast_delay_persistent_proc_millis";
    private static final String KEY_DELAY_URGENT_MILLIS = "bcast_delay_urgent_millis";
    private static final String KEY_EXTRA_RUNNING_URGENT_PROCESS_QUEUES = "bcast_extra_running_urgent_process_queues";
    private static final String KEY_MAX_CONSECUTIVE_NORMAL_DISPATCHES = "bcast_max_consecutive_normal_dispatches";
    private static final String KEY_MAX_CONSECUTIVE_URGENT_DISPATCHES = "bcast_max_consecutive_urgent_dispatches";
    private static final String KEY_MAX_HISTORY_COMPLETE_SIZE = "bcast_max_history_complete_size";
    private static final String KEY_MAX_HISTORY_SUMMARY_SIZE = "bcast_max_history_summary_size";
    private static final String KEY_MAX_PENDING_BROADCASTS = "bcast_max_pending_broadcasts";
    private static final String KEY_MAX_RUNNING_ACTIVE_BROADCASTS = "bcast_max_running_active_broadcasts";
    private static final String KEY_MAX_RUNNING_PROCESS_QUEUES = "bcast_max_running_process_queues";
    private static final String KEY_MODERN_QUEUE_ENABLED = "modern_queue_enabled";
    private static final String KEY_PENDING_COLD_START_CHECK_INTERVAL_MILLIS = "pending_cold_start_check_interval_millis";
    static final String KEY_SLOW_TIME = "bcast_slow_time";
    static final String KEY_TIMEOUT = "bcast_timeout";
    private static final String TAG = "BroadcastConstants";
    private ContentResolver mResolver;
    private String mSettingsKey;
    private SettingsObserver mSettingsObserver;
    public long TIMEOUT = DEFAULT_TIMEOUT;
    public long SLOW_TIME = DEFAULT_SLOW_TIME;
    public long DEFERRAL = DEFAULT_DEFERRAL;
    public float DEFERRAL_DECAY_FACTOR = DEFAULT_DEFERRAL_DECAY_FACTOR;
    public long DEFERRAL_FLOOR = 0;
    public long ALLOW_BG_ACTIVITY_START_TIMEOUT = DEFAULT_ALLOW_BG_ACTIVITY_START_TIMEOUT;
    public boolean MODERN_QUEUE_ENABLED = true;
    public int MAX_RUNNING_PROCESS_QUEUES = DEFAULT_MAX_RUNNING_PROCESS_QUEUES;
    public int EXTRA_RUNNING_URGENT_PROCESS_QUEUES = 1;
    public int MAX_CONSECUTIVE_URGENT_DISPATCHES = 3;
    public int MAX_CONSECUTIVE_NORMAL_DISPATCHES = 10;
    public int MAX_RUNNING_ACTIVE_BROADCASTS = DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS;
    public int MAX_CORE_RUNNING_BLOCKING_BROADCASTS = DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS;
    public int MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS = DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS;
    public int MAX_PENDING_BROADCASTS = DEFAULT_MAX_PENDING_BROADCASTS;
    public long DELAY_NORMAL_MILLIS = DEFAULT_DELAY_NORMAL_MILLIS;
    public long DELAY_CACHED_MILLIS = DEFAULT_DELAY_CACHED_MILLIS;
    public long DELAY_URGENT_MILLIS = -120000;
    public long DELAY_FOREGROUND_PROC_MILLIS = -120000;
    public long DELAY_PERSISTENT_PROC_MILLIS = -120000;
    public int MAX_HISTORY_COMPLETE_SIZE = DEFAULT_MAX_HISTORY_COMPLETE_SIZE;
    public int MAX_HISTORY_SUMMARY_SIZE = DEFAULT_MAX_HISTORY_SUMMARY_SIZE;
    public boolean CORE_DEFER_UNTIL_ACTIVE = true;
    public long PENDING_COLD_START_CHECK_INTERVAL_MILLIS = 30000;
    private final KeyValueListParser mParser = new KeyValueListParser(',');

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface DeferBootCompletedBroadcastType {
    }

    static {
        int i = Build.HW_TIMEOUT_MULTIPLIER;
        DEFAULT_TIMEOUT = i * 10000;
        DEFAULT_SLOW_TIME = i * 5000;
        DEFAULT_DEFERRAL = i * 5000;
        DEFAULT_ALLOW_BG_ACTIVITY_START_TIMEOUT = i * 10000;
        DEFAULT_MAX_RUNNING_PROCESS_QUEUES = ActivityManager.isLowRamDeviceStatic() ? 2 : 4;
        DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS = ActivityManager.isLowRamDeviceStatic() ? 8 : 16;
        DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS = ActivityManager.isLowRamDeviceStatic() ? 8 : 16;
        DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS = ActivityManager.isLowRamDeviceStatic() ? 32 : 64;
        DEFAULT_MAX_PENDING_BROADCASTS = ActivityManager.isLowRamDeviceStatic() ? 128 : 256;
        DEFAULT_MAX_HISTORY_COMPLETE_SIZE = ActivityManager.isLowRamDeviceStatic() ? 64 : 256;
        DEFAULT_MAX_HISTORY_SUMMARY_SIZE = ActivityManager.isLowRamDeviceStatic() ? 256 : 1024;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            BroadcastConstants.this.updateSettingsConstants();
        }
    }

    public BroadcastConstants(String str) {
        this.mSettingsKey = str;
        updateDeviceConfigConstants();
    }

    public void startObserving(Handler handler, ContentResolver contentResolver) {
        this.mResolver = contentResolver;
        this.mSettingsObserver = new SettingsObserver(handler);
        this.mResolver.registerContentObserver(Settings.Global.getUriFor(this.mSettingsKey), false, this.mSettingsObserver);
        updateSettingsConstants();
        DeviceConfig.addOnPropertiesChangedListener("activity_manager_native_boot", new HandlerExecutor(handler), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.BroadcastConstants$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                BroadcastConstants.this.updateDeviceConfigConstants(properties);
            }
        });
        updateDeviceConfigConstants();
    }

    public int getMaxRunningQueues() {
        return this.MAX_RUNNING_PROCESS_QUEUES + this.EXTRA_RUNNING_URGENT_PROCESS_QUEUES;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettingsConstants() {
        synchronized (this) {
            try {
                try {
                    this.mParser.setString(Settings.Global.getString(this.mResolver, this.mSettingsKey));
                    this.TIMEOUT = this.mParser.getLong(KEY_TIMEOUT, this.TIMEOUT);
                    this.SLOW_TIME = this.mParser.getLong(KEY_SLOW_TIME, this.SLOW_TIME);
                    this.DEFERRAL = this.mParser.getLong(KEY_DEFERRAL, this.DEFERRAL);
                    this.DEFERRAL_DECAY_FACTOR = this.mParser.getFloat(KEY_DEFERRAL_DECAY_FACTOR, this.DEFERRAL_DECAY_FACTOR);
                    this.DEFERRAL_FLOOR = this.mParser.getLong(KEY_DEFERRAL_FLOOR, this.DEFERRAL_FLOOR);
                    this.ALLOW_BG_ACTIVITY_START_TIMEOUT = this.mParser.getLong(KEY_ALLOW_BG_ACTIVITY_START_TIMEOUT, this.ALLOW_BG_ACTIVITY_START_TIMEOUT);
                } catch (IllegalArgumentException e) {
                    Slog.e(TAG, "Bad broadcast settings in key '" + this.mSettingsKey + "'", e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private String propertyFor(String str) {
        return "persist.device_config.activity_manager_native_boot." + str;
    }

    private String propertyOverrideFor(String str) {
        return "persist.sys.activity_manager_native_boot." + str;
    }

    private boolean getDeviceConfigBoolean(String str, boolean z) {
        return SystemProperties.getBoolean(propertyOverrideFor(str), SystemProperties.getBoolean(propertyFor(str), z));
    }

    private int getDeviceConfigInt(String str, int i) {
        return SystemProperties.getInt(propertyOverrideFor(str), SystemProperties.getInt(propertyFor(str), i));
    }

    private long getDeviceConfigLong(String str, long j) {
        return SystemProperties.getLong(propertyOverrideFor(str), SystemProperties.getLong(propertyFor(str), j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceConfigConstants(DeviceConfig.Properties properties) {
        updateDeviceConfigConstants();
    }

    private void updateDeviceConfigConstants() {
        synchronized (this) {
            this.MODERN_QUEUE_ENABLED = getDeviceConfigBoolean(KEY_MODERN_QUEUE_ENABLED, true);
            this.MAX_RUNNING_PROCESS_QUEUES = getDeviceConfigInt(KEY_MAX_RUNNING_PROCESS_QUEUES, DEFAULT_MAX_RUNNING_PROCESS_QUEUES);
            this.EXTRA_RUNNING_URGENT_PROCESS_QUEUES = getDeviceConfigInt(KEY_EXTRA_RUNNING_URGENT_PROCESS_QUEUES, 1);
            this.MAX_CONSECUTIVE_URGENT_DISPATCHES = getDeviceConfigInt(KEY_MAX_CONSECUTIVE_URGENT_DISPATCHES, 3);
            this.MAX_CONSECUTIVE_NORMAL_DISPATCHES = getDeviceConfigInt(KEY_MAX_CONSECUTIVE_NORMAL_DISPATCHES, 10);
            this.MAX_RUNNING_ACTIVE_BROADCASTS = getDeviceConfigInt(KEY_MAX_RUNNING_ACTIVE_BROADCASTS, DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS);
            this.MAX_CORE_RUNNING_BLOCKING_BROADCASTS = getDeviceConfigInt(KEY_CORE_MAX_RUNNING_BLOCKING_BROADCASTS, DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS);
            this.MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS = getDeviceConfigInt(KEY_CORE_MAX_RUNNING_NON_BLOCKING_BROADCASTS, DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS);
            this.MAX_PENDING_BROADCASTS = getDeviceConfigInt(KEY_MAX_PENDING_BROADCASTS, DEFAULT_MAX_PENDING_BROADCASTS);
            this.DELAY_NORMAL_MILLIS = getDeviceConfigLong(KEY_DELAY_NORMAL_MILLIS, DEFAULT_DELAY_NORMAL_MILLIS);
            this.DELAY_CACHED_MILLIS = getDeviceConfigLong(KEY_DELAY_CACHED_MILLIS, DEFAULT_DELAY_CACHED_MILLIS);
            this.DELAY_URGENT_MILLIS = getDeviceConfigLong(KEY_DELAY_URGENT_MILLIS, -120000L);
            this.DELAY_FOREGROUND_PROC_MILLIS = getDeviceConfigLong(KEY_DELAY_FOREGROUND_PROC_MILLIS, -120000L);
            this.DELAY_PERSISTENT_PROC_MILLIS = getDeviceConfigLong(KEY_DELAY_PERSISTENT_PROC_MILLIS, -120000L);
            this.MAX_HISTORY_COMPLETE_SIZE = getDeviceConfigInt(KEY_MAX_HISTORY_COMPLETE_SIZE, DEFAULT_MAX_HISTORY_COMPLETE_SIZE);
            this.MAX_HISTORY_SUMMARY_SIZE = getDeviceConfigInt(KEY_MAX_HISTORY_SUMMARY_SIZE, DEFAULT_MAX_HISTORY_SUMMARY_SIZE);
            this.CORE_DEFER_UNTIL_ACTIVE = getDeviceConfigBoolean(KEY_CORE_DEFER_UNTIL_ACTIVE, true);
            this.PENDING_COLD_START_CHECK_INTERVAL_MILLIS = getDeviceConfigLong(KEY_PENDING_COLD_START_CHECK_INTERVAL_MILLIS, 30000L);
        }
        BroadcastRecord.CORE_DEFER_UNTIL_ACTIVE = this.CORE_DEFER_UNTIL_ACTIVE;
    }

    @NeverCompile
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this) {
            indentingPrintWriter.print("Broadcast parameters (key=");
            indentingPrintWriter.print(this.mSettingsKey);
            indentingPrintWriter.print(", observing=");
            indentingPrintWriter.print(this.mSettingsObserver != null);
            indentingPrintWriter.println("):");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_TIMEOUT, TimeUtils.formatDuration(this.TIMEOUT)).println();
            indentingPrintWriter.print(KEY_SLOW_TIME, TimeUtils.formatDuration(this.SLOW_TIME)).println();
            indentingPrintWriter.print(KEY_DEFERRAL, TimeUtils.formatDuration(this.DEFERRAL)).println();
            indentingPrintWriter.print(KEY_DEFERRAL_DECAY_FACTOR, Float.valueOf(this.DEFERRAL_DECAY_FACTOR)).println();
            indentingPrintWriter.print(KEY_DEFERRAL_FLOOR, Long.valueOf(this.DEFERRAL_FLOOR)).println();
            indentingPrintWriter.print(KEY_ALLOW_BG_ACTIVITY_START_TIMEOUT, TimeUtils.formatDuration(this.ALLOW_BG_ACTIVITY_START_TIMEOUT)).println();
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.print("Broadcast parameters (namespace=");
            indentingPrintWriter.print("activity_manager_native_boot");
            indentingPrintWriter.println("):");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(KEY_MODERN_QUEUE_ENABLED, Boolean.valueOf(this.MODERN_QUEUE_ENABLED)).println();
            indentingPrintWriter.print(KEY_MAX_RUNNING_PROCESS_QUEUES, Integer.valueOf(this.MAX_RUNNING_PROCESS_QUEUES)).println();
            indentingPrintWriter.print(KEY_MAX_RUNNING_ACTIVE_BROADCASTS, Integer.valueOf(this.MAX_RUNNING_ACTIVE_BROADCASTS)).println();
            indentingPrintWriter.print(KEY_CORE_MAX_RUNNING_BLOCKING_BROADCASTS, Integer.valueOf(this.MAX_CORE_RUNNING_BLOCKING_BROADCASTS)).println();
            indentingPrintWriter.print(KEY_CORE_MAX_RUNNING_NON_BLOCKING_BROADCASTS, Integer.valueOf(this.MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS)).println();
            indentingPrintWriter.print(KEY_MAX_PENDING_BROADCASTS, Integer.valueOf(this.MAX_PENDING_BROADCASTS)).println();
            indentingPrintWriter.print(KEY_DELAY_NORMAL_MILLIS, TimeUtils.formatDuration(this.DELAY_NORMAL_MILLIS)).println();
            indentingPrintWriter.print(KEY_DELAY_CACHED_MILLIS, TimeUtils.formatDuration(this.DELAY_CACHED_MILLIS)).println();
            indentingPrintWriter.print(KEY_DELAY_URGENT_MILLIS, TimeUtils.formatDuration(this.DELAY_URGENT_MILLIS)).println();
            indentingPrintWriter.print(KEY_DELAY_FOREGROUND_PROC_MILLIS, TimeUtils.formatDuration(this.DELAY_FOREGROUND_PROC_MILLIS)).println();
            indentingPrintWriter.print(KEY_DELAY_PERSISTENT_PROC_MILLIS, TimeUtils.formatDuration(this.DELAY_PERSISTENT_PROC_MILLIS)).println();
            indentingPrintWriter.print(KEY_MAX_HISTORY_COMPLETE_SIZE, Integer.valueOf(this.MAX_HISTORY_COMPLETE_SIZE)).println();
            indentingPrintWriter.print(KEY_MAX_HISTORY_SUMMARY_SIZE, Integer.valueOf(this.MAX_HISTORY_SUMMARY_SIZE)).println();
            indentingPrintWriter.print(KEY_MAX_CONSECUTIVE_URGENT_DISPATCHES, Integer.valueOf(this.MAX_CONSECUTIVE_URGENT_DISPATCHES)).println();
            indentingPrintWriter.print(KEY_MAX_CONSECUTIVE_NORMAL_DISPATCHES, Integer.valueOf(this.MAX_CONSECUTIVE_NORMAL_DISPATCHES)).println();
            indentingPrintWriter.print(KEY_CORE_DEFER_UNTIL_ACTIVE, Boolean.valueOf(this.CORE_DEFER_UNTIL_ACTIVE)).println();
            indentingPrintWriter.print(KEY_PENDING_COLD_START_CHECK_INTERVAL_MILLIS, Long.valueOf(this.PENDING_COLD_START_CHECK_INTERVAL_MILLIS)).println();
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
        }
    }
}
