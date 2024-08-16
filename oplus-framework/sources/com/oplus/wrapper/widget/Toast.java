package com.oplus.wrapper.widget;

import android.view.WindowManager;

/* loaded from: classes.dex */
public class Toast {
    private final android.widget.Toast mToast;

    public Toast(android.widget.Toast toast) {
        this.mToast = toast;
    }

    public WindowManager.LayoutParams getWindowParams() {
        return this.mToast.getWindowParams();
    }
}
