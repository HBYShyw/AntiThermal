package com.android.server.wm;

import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IInsetsSourceProviderExt {
    default boolean getInputShowStatus() {
        return false;
    }

    default boolean hasFlexibleActivityInfo(WindowState windowState) {
        return false;
    }

    default void showImeLeashInCarDisplayIfNeed(InsetsSourceProvider insetsSourceProvider, InsetsControlTarget insetsControlTarget, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }
}
