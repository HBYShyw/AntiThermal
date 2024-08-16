package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityMetricsLoggerExt {
    default int getCompatWindowMode() {
        return 0;
    }

    default int getZoomToFullWindowMode() {
        return 0;
    }

    default int getZoomWindowMode() {
        return 0;
    }

    default int getZoomWindowState() {
        return 0;
    }

    default boolean isFlexibleWindowTaskLaunched(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean isMultiSearchTaskLaunched(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default void notifyActivityStarted(String str, String str2) {
    }
}
