package com.android.server.wm;

import android.animation.ValueAnimator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDragStateWrapper {
    default ValueAnimator createCancelAnimationLocked() {
        return null;
    }

    default ValueAnimator createReturnAnimationLocked() {
        return null;
    }
}
