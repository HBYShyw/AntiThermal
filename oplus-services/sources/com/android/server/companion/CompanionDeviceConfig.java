package com.android.server.companion;

import android.os.Binder;
import android.provider.DeviceConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CompanionDeviceConfig {
    public static final String ENABLE_CONTEXT_SYNC_TELECOM = "enable_context_sync_telecom";
    private static final String NAMESPACE_COMPANION = "companion";

    public static boolean isEnabled(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return DeviceConfig.getBoolean(NAMESPACE_COMPANION, str, false);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public static boolean isEnabled(String str, boolean z) {
        return DeviceConfig.getBoolean(NAMESPACE_COMPANION, str, z);
    }
}
