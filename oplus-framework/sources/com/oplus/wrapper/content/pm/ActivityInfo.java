package com.oplus.wrapper.content.pm;

import android.content.res.TypedArray;

/* loaded from: classes.dex */
public class ActivityInfo {
    public static final int FLAG_ALLOW_EMBEDDED = getFlagAllowEmbedded();

    private static int getFlagAllowEmbedded() {
        return Integer.MIN_VALUE;
    }

    public static boolean isTranslucentOrFloating(TypedArray attributes) {
        return android.content.pm.ActivityInfo.isTranslucentOrFloating(attributes);
    }
}
