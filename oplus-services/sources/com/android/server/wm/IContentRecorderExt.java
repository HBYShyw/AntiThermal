package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IContentRecorderExt {
    default boolean ifNeedRotateSurfaceForOplus(DisplayContent displayContent) {
        return false;
    }

    default boolean isLinkToWindowsMode(DisplayContent displayContent) {
        return false;
    }

    default boolean isSurfaceSizeChanged() {
        return false;
    }

    default void rotateSurface(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f, Rect rect, Point point, int i) {
    }

    default void setSurfaceSize(Point point) {
    }

    default boolean synchronizeMirrorTransactionIfNeeded() {
        return false;
    }

    default boolean updateMirroringIfSurfaceSizeChanged() {
        return false;
    }
}
