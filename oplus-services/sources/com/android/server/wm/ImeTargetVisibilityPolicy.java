package com.android.server.wm;

import android.os.IBinder;
import android.view.WindowManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public abstract class ImeTargetVisibilityPolicy {
    public abstract boolean removeImeScreenshot(int i);

    public abstract boolean showImeScreenshot(IBinder iBinder, int i);

    public static boolean canComputeImeParent(WindowState windowState, InputTarget inputTarget) {
        boolean z = false;
        if (windowState == null) {
            return false;
        }
        if (shouldComputeImeParentForEmbeddedActivity(windowState, inputTarget)) {
            return true;
        }
        boolean z2 = WindowManager.LayoutParams.mayUseInputMethod(windowState.mAttrs.flags) || windowState.mAttrs.type == 3;
        boolean z3 = inputTarget == null || windowState.mActivityRecord != inputTarget.getActivityRecord();
        if (z2 && z3) {
            z = true;
        }
        return !z;
    }

    private static boolean shouldComputeImeParentForEmbeddedActivity(WindowState windowState, InputTarget inputTarget) {
        WindowState windowState2;
        if (inputTarget == null || windowState == null || (windowState2 = inputTarget.getWindowState()) == null || !windowState.isAttached() || !windowState2.isAttached()) {
            return false;
        }
        ActivityRecord activityRecord = inputTarget.getActivityRecord();
        ActivityRecord activityRecord2 = windowState.getActivityRecord();
        return activityRecord != null && activityRecord2 != null && activityRecord != activityRecord2 && activityRecord.getTask() == activityRecord2.getTask() && activityRecord.isEmbedded() && activityRecord2.isEmbedded() && windowState.compareTo((WindowContainer) windowState2) > 0;
    }
}
