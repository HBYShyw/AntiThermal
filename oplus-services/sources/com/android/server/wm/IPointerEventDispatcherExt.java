package com.android.server.wm;

import android.os.Looper;
import android.view.MotionEvent;
import android.view.WindowManagerPolicyConstants;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IPointerEventDispatcherExt {
    default void debugInputEventDuration(MotionEvent motionEvent, WindowManagerPolicyConstants.PointerEventListener pointerEventListener, long j) {
    }

    default Looper getOptLooper(Looper looper) {
        return looper;
    }
}
