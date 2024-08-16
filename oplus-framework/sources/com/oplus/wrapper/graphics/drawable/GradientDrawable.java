package com.oplus.wrapper.graphics.drawable;

import android.graphics.Xfermode;

/* loaded from: classes.dex */
public class GradientDrawable {
    private final android.graphics.drawable.GradientDrawable mGradientDrawable;

    public GradientDrawable(android.graphics.drawable.GradientDrawable gradientDrawable) {
        this.mGradientDrawable = gradientDrawable;
    }

    public void setXfermode(Xfermode mode) {
        this.mGradientDrawable.setXfermode(mode);
    }
}
