package com.android.server.wm;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.window.ScreenCapture;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISurfaceFreezerExt {
    default ScreenCapture.ScreenshotHardwareBuffer createFlexibleTaskSnapshotBuffer(SurfaceControl surfaceControl) {
        return null;
    }

    default void resetFlexibleTaskInfo() {
    }

    default void setFlexibleTaskInfo(Rect rect, float f) {
    }
}
