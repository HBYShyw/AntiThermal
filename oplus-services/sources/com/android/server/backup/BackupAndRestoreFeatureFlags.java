package com.android.server.backup;

import android.annotation.RequiresPermission;
import android.provider.DeviceConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BackupAndRestoreFeatureFlags {
    private static final String NAMESPACE = "backup_and_restore";

    private BackupAndRestoreFeatureFlags() {
    }

    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public static long getBackupTransportFutureTimeoutMillis() {
        return DeviceConfig.getLong(NAMESPACE, "backup_transport_future_timeout_millis", BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_FUZZ_MILLISECONDS);
    }

    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public static long getBackupTransportCallbackTimeoutMillis() {
        return DeviceConfig.getLong(NAMESPACE, "backup_transport_callback_timeout_millis", 300000L);
    }

    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public static int getFullBackupWriteToTransportBufferSizeBytes() {
        return DeviceConfig.getInt(NAMESPACE, "full_backup_write_to_transport_buffer_size_bytes", 8192);
    }

    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public static int getFullBackupUtilsRouteBufferSizeBytes() {
        return DeviceConfig.getInt(NAMESPACE, "full_backup_utils_route_buffer_size_bytes", 32768);
    }

    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public static boolean getUnifiedRestoreContinueAfterTransportFailureInKvRestore() {
        return DeviceConfig.getBoolean(NAMESPACE, "unified_restore_continue_after_transport_failure_in_kv_restore", true);
    }
}
