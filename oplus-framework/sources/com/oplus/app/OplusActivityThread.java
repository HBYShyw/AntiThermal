package com.oplus.app;

import android.app.ActivityThread;
import android.app.IActivityThreadWrapper;
import android.app.Instrumentation;

/* loaded from: classes.dex */
public class OplusActivityThread {
    public static void scheduleTrimMemory(int level) {
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        IActivityThreadWrapper activityThreadWrapper = activityThread.getWrapper();
        activityThreadWrapper.scheduleTrimMemory(level);
    }

    public static void setInstrumentation(Instrumentation instrumentation) {
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        IActivityThreadWrapper activityThreadWrapper = activityThread.getWrapper();
        activityThreadWrapper.setInstrumentation(instrumentation);
    }
}
