package com.android.server.wm;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IRecentsAnimationExt {
    default void disableSensorScreenShot(Context context) {
    }

    default void finishAnimation() {
    }

    default TaskDisplayArea getDefaultTaskDisplayArea(Intent intent, ActivityTaskManagerService activityTaskManagerService) {
        return null;
    }

    default boolean hasGestureAnimationController() {
        return false;
    }

    default void needHideInputMethod(ActivityRecord activityRecord) {
    }

    default void notifyCompactWindowState(Task task, boolean z) {
    }

    default void onRecentAnimationEnd() {
    }

    default void onRecentAnimationStart() {
    }

    default boolean startRecentsWhenKeyguardLocked(ActivityRecord activityRecord, WindowManagerService windowManagerService) {
        return false;
    }

    default void startSecondHomeActivityInBackground(Intent intent, ActivityOptions activityOptions) {
    }
}
