package com.oplus.wrapper.app;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Handler;

/* loaded from: classes.dex */
public class ActivityThread {
    private final android.app.ActivityThread mActivityThread;

    private ActivityThread(android.app.ActivityThread activityThread) {
        this.mActivityThread = activityThread;
    }

    public static ActivityThread currentActivityThread() {
        return new ActivityThread(android.app.ActivityThread.currentActivityThread());
    }

    public Instrumentation getInstrumentation() {
        return this.mActivityThread.getInstrumentation();
    }

    public Handler getHandler() {
        return this.mActivityThread.getHandler();
    }

    public static Application currentApplication() {
        return android.app.ActivityThread.currentApplication();
    }
}
