package com.android.server.wm;

import com.android.server.wm.ActivityRecord;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityTaskSupervisorSocExt {
    default void acquireAppLaunchPerfLock(ActivityRecord activityRecord, ActivityTaskManagerService activityTaskManagerService) {
    }

    default void notifyServiceTracker(ActivityRecord.State state, boolean z, ActivityRecord activityRecord, long j) {
    }

    default void reportActivityLaunchedPerfHint(ActivityRecord activityRecord) {
    }

    default void startPreferredApps(String str, ActivityTaskManagerService activityTaskManagerService) {
    }

    default void startSpecificActivityPerfHint(String str, ActivityRecord activityRecord, int i) {
    }
}
