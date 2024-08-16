package com.android.server.wm;

import com.android.server.policy.WindowManagerPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskSnapshotControllerExt {
    default void getClosingTasks(ActivityRecord activityRecord) {
    }

    default boolean isSecondScreenOn(WindowManagerPolicy windowManagerPolicy) {
        return false;
    }

    default boolean shouldDisableSnapshots() {
        return false;
    }
}
