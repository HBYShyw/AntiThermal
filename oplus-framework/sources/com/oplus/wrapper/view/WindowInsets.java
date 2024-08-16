package com.oplus.wrapper.view;

import android.graphics.Rect;

/* loaded from: classes.dex */
public class WindowInsets {
    private final android.view.WindowInsets mWindowInsets;

    public WindowInsets(Rect systemWindowInsets) {
        this.mWindowInsets = new android.view.WindowInsets(systemWindowInsets);
    }

    public android.view.WindowInsets getWindowInsets() {
        return this.mWindowInsets;
    }
}
