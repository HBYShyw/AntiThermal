package com.android.server.display;

import android.content.Context;
import android.os.Handler;
import com.android.server.display.DisplayManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IMirageDisplayManagerExt {
    default int getBondDisplayIdLocked(int i, DisplayDeviceInfo displayDeviceInfo, boolean z) {
        return -1;
    }

    default int getLastAssignedDisplayId() {
        return -1;
    }

    default int getMirageDisplayCastMode(int i) {
        return -1;
    }

    default void init(OverlayDisplayAdapter overlayDisplayAdapter, DisplayManagerService.SyncRoot syncRoot, Handler handler, Context context, Handler handler2) {
    }

    default boolean isMirageBackgroundStreamMode(int i) {
        return false;
    }

    default boolean isMirageCarMode(int i) {
        return false;
    }

    default boolean isMirageDisplay(int i) {
        return false;
    }

    default boolean isMirageDisplayEnabled() {
        return false;
    }

    default boolean isMiragePcMode(int i) {
        return false;
    }

    default boolean isMirageTvMode(int i) {
        return false;
    }

    default void recordDisplayIdForDisplay(DisplayDevice displayDevice, int i) {
    }
}
