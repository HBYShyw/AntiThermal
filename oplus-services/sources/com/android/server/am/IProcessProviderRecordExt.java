package com.android.server.am;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessProviderRecordExt {
    default boolean checkIfAlwaysCleanupAppInLaunchingProviders(Context context, ProcessRecord processRecord, boolean z) {
        return false;
    }

    default boolean checkIfAlwaysCleanupAppInLaunchingProviders(boolean z) {
        return false;
    }
}
