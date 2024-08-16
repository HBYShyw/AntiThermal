package com.android.server.pm;

import android.os.Build;
import android.os.SystemProperties;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SharedUidMigration {
    public static final int BEST_EFFORT = 2;
    private static final int DEFAULT = 1;
    public static final int LIVE_TRANSITION = 4;
    public static final int NEW_INSTALL_ONLY = 1;
    public static final String PROPERTY_KEY = "persist.debug.pm.shared_uid_migration_strategy";
    public static final int TRANSITION_AT_BOOT = 3;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface Strategy {
    }

    public static boolean isDisabled() {
        return false;
    }

    public static int getCurrentStrategy() {
        int i;
        if (Build.IS_USERDEBUG && (i = SystemProperties.getInt(PROPERTY_KEY, 1)) <= 2 && i >= 1) {
            return i;
        }
        return 1;
    }

    public static boolean applyStrategy(int i) {
        return !isDisabled() && getCurrentStrategy() >= i;
    }

    private SharedUidMigration() {
    }
}
