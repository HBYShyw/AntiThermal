package com.oplus.wrapper.policy;

import android.content.Context;
import android.content.res.Resources;

/* loaded from: classes.dex */
public class ScreenDecorationsUtils {
    private ScreenDecorationsUtils() {
    }

    public static float getWindowCornerRadius(Context context) {
        return com.android.internal.policy.ScreenDecorationsUtils.getWindowCornerRadius(context);
    }

    public static boolean supportsRoundedCornersOnWindows(Resources resources) {
        return com.android.internal.policy.ScreenDecorationsUtils.supportsRoundedCornersOnWindows(resources);
    }
}
