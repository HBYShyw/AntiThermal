package com.android.server.wm;

import android.view.InsetsState;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayFramesExt {
    default int getDisplayId() {
        return -1;
    }

    default void removeSecondaryDisplaySource(InsetsState insetsState, int i, int i2) {
    }

    default void setDisplayId(int i) {
    }
}
