package com.oplus.wrapper.app;

import android.app.Application;

/* loaded from: classes.dex */
public class AppGlobals {
    private AppGlobals() {
    }

    public static Application getInitialApplication() {
        return android.app.AppGlobals.getInitialApplication();
    }
}
