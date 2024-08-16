package com.android.server.wm;

import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISurfaceAnimatorWrapper {
    public static final int ANIMATION_TYPE_TRANSWINDOW = 128;

    default void reset(SurfaceControl.Transaction transaction, boolean z) {
    }

    default ISurfaceAnimatorExt getExtImpl() {
        return new ISurfaceAnimatorExt() { // from class: com.android.server.wm.ISurfaceAnimatorWrapper.1
        };
    }
}
