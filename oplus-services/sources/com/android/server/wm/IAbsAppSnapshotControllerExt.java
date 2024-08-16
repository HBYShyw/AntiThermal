package com.android.server.wm;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.window.ScreenCapture;
import com.android.server.policy.WindowManagerPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAbsAppSnapshotControllerExt {
    default ScreenCapture.ScreenshotHardwareBuffer createTaskSnapshot(Task task, ActivityRecord activityRecord, Rect rect, float f, int i, SurfaceControl[] surfaceControlArr) {
        return null;
    }

    default int drawAppThemeSnapshot(int i, Task task) {
        return i;
    }

    default boolean isActivityTypeMultiSearch(int i) {
        return false;
    }

    default boolean isSecondScreenOn(WindowManagerPolicy windowManagerPolicy) {
        return false;
    }

    default void prepareTaskSnapshot(Rect rect, ActivityRecord activityRecord, WindowState windowState) {
    }

    default boolean snapshotForScreenOffActPreload(Task task) {
        return false;
    }

    default void snapshotTask(Task task, ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
    }
}
