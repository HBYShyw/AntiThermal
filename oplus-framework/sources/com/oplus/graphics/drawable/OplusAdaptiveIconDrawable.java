package com.oplus.graphics.drawable;

import android.graphics.drawable.AdaptiveIconDrawable;

/* loaded from: classes.dex */
public class OplusAdaptiveIconDrawable {
    private OplusAdaptiveIconDrawable() {
    }

    public float getForegroundScalePercent(AdaptiveIconDrawable adaptiveIconDrawable) {
        return adaptiveIconDrawable.mIconDrawableExt.getForegroundScalePercent();
    }
}
