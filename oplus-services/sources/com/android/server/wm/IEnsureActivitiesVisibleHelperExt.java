package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IEnsureActivitiesVisibleHelperExt {
    default boolean isScreenOffPlay(Task task) {
        return false;
    }

    default void makeVisibleAndRestartIfNeeded(Task task, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }
}
