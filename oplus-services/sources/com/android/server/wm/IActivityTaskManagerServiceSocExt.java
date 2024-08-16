package com.android.server.wm;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityTaskManagerServiceSocExt {
    default void onActivityStateChanged(ActivityRecord activityRecord, boolean z) {
    }

    default void onAfterActivityResumed(ActivityRecord activityRecord) {
    }

    default void onBeforeActivitySwitch(ActivityRecord activityRecord, boolean z, int i, boolean z2) {
    }

    default void onBeforeActivitySwitch(Task task, ActivityRecord activityRecord) {
    }

    default void onEndOfActivityIdle(Context context, ActivityRecord activityRecord) {
    }

    default void setLastResumedBeforeActivitySwitch(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }
}
