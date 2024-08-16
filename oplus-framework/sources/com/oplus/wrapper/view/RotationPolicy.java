package com.oplus.wrapper.view;

import android.content.Context;

/* loaded from: classes.dex */
public class RotationPolicy {
    public static boolean isRotationSupported(Context context) {
        return com.android.internal.view.RotationPolicy.isRotationSupported(context);
    }

    public static void setRotationLock(Context context, boolean enabled) {
        com.android.internal.view.RotationPolicy.setRotationLock(context, enabled);
    }

    public static boolean isRotationLocked(Context context) {
        return com.android.internal.view.RotationPolicy.isRotationLocked(context);
    }

    public static void setRotationLockAtAngle(Context context, boolean enabled, int rotation) {
        com.android.internal.view.RotationPolicy.setRotationLockAtAngle(context, enabled, rotation);
    }
}
