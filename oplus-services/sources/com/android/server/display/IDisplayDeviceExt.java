package com.android.server.display;

import android.view.Surface;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayDeviceExt {
    default void cacheSurfaceForDisplay(DisplayDevice displayDevice, Surface surface) {
    }

    default int getLayerStack() {
        return -1;
    }

    default boolean getMirageSetSurfaceNull() {
        return false;
    }

    default boolean isPowerModeChanged() {
        return false;
    }

    default void setLayerStack(int i) {
    }

    default void setMirageSetSurfaceNull(boolean z) {
    }

    default boolean shouldSetDisplayDeviceSurface(DisplayDevice displayDevice) {
        return true;
    }

    default void updatePowerModeChanged(boolean z) {
    }
}
