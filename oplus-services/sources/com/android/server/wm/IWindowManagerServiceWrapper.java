package com.android.server.wm;

import android.os.IBinder;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowManagerServiceWrapper {
    default boolean dumpWindows(PrintWriter printWriter, String str, boolean z) {
        return false;
    }

    default IWindowManagerServiceExt getExtImpl() {
        return null;
    }

    default WindowState getFocusedWindow() {
        return null;
    }

    default int getForcedDisplayDensityForUserLocked(int i) {
        return 0;
    }

    default void setTransitionAnimationScaleSetting(float f) {
    }

    default void setWindowAnimationScaleSetting(float f) {
    }

    default void transferTouchFocus(IBinder iBinder, IBinder iBinder2) {
    }

    default void updateAppOpsState() {
    }
}
