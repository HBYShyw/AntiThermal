package com.android.server.pm;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.content.pm.LauncherApps;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IShortcutServiceExt {
    default boolean adjustPackageEnabledForIsInstalled(boolean z, ApplicationInfo applicationInfo, IApplicationInfoExt iApplicationInfoExt) {
        return false;
    }

    default void afterGetUserShortcutsOnUnlockUser(boolean z, int i) {
    }

    default boolean beforeGetUserShortcutsOnUnlockUser(int i) {
        return false;
    }

    default String hookGetLauncherPkgNameInLoadFromXml(String str) {
        return str;
    }

    default void startRequestConfirmActivity(LauncherApps.PinItemRequest pinItemRequest, Intent intent) {
    }
}
