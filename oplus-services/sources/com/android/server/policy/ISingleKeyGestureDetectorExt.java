package com.android.server.policy;

import android.view.KeyEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISingleKeyGestureDetectorExt {
    default void endHookInterceptKeyUp() {
    }

    default long modifyPressTimeout(int i, long j, KeyEvent keyEvent) {
        return j;
    }
}
