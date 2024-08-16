package com.oplus.wrapper.util;

/* loaded from: classes.dex */
public class DisplayMetrics {
    private final android.util.DisplayMetrics mDisplayMetrics;

    public DisplayMetrics(android.util.DisplayMetrics displayMetrics) {
        this.mDisplayMetrics = displayMetrics;
    }

    public int getNoncompatDensityDpi() {
        return this.mDisplayMetrics.noncompatDensityDpi;
    }
}
