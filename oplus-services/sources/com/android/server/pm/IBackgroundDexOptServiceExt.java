package com.android.server.pm;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBackgroundDexOptServiceExt {
    default int adjustDexoptFlagsInOptimizePackage(int i, String str) {
        return i;
    }

    default void afterOptInIdleOptimization() {
    }

    default void beforeOptInIdleOptimization() {
    }

    default void beforeScheduleJob(Context context, PackageManagerService packageManagerService) {
    }

    default int breakAndReturnInOptimizePackages(boolean z) {
        return 0;
    }

    default boolean breakAndReturnInPostBootUpdate() {
        return false;
    }

    default boolean isEnableFastIdle() {
        return false;
    }

    default boolean needSkipIdleOptimization() {
        return false;
    }

    default void notifyTriggerFastIdle() {
    }

    default boolean parseResultAfterIdleOptimization(int i) {
        return false;
    }

    default boolean skipOnStartJob() {
        return false;
    }

    default void updateIdleOptimizeRecord(String str, String str2) {
    }

    default ArrayList<String> adjustPkgOrderInOptimizePackages(List<String> list) {
        return new ArrayList<>(list);
    }
}
