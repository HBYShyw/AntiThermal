package com.android.server.wm;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.IRecentsAnimationController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IZoomWindowManagerExt {
    public static final int TRANSIT_FLOAT_OPEN_TO_ZOOM = 102;
    public static final int TRANSIT_ZOOM_CLOSE = 101;
    public static final int TRANSIT_ZOOM_CLOSE_TO_FLOAT = 103;
    public static final int TRANSIT_ZOOM_OPEN = 100;
    public static final int TYPE_FULL_ANIMATION = 105;
    public static final int TYPE_SCALE_ANIMATION = 104;
    public static final int UPDATE_MODE_APP_ORIENTATION_CHANGE = 1;

    default void adjustInputWindowHandle(InputMonitor inputMonitor, WindowState windowState, InputWindowHandleWrapper inputWindowHandleWrapper) {
    }

    default boolean checkInSideGestureHotZone(float f, float f2) {
        return false;
    }

    default void gestureSwipeFromBottom() {
    }

    default boolean recentAnimationFinished(int i, int i2, Rect rect, int i3, Bundle bundle, IRecentsAnimationController iRecentsAnimationController, boolean z, boolean z2) {
        return false;
    }
}
