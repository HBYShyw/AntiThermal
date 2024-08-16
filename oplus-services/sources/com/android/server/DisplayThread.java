package com.android.server;

import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayThread extends ServiceThread {
    private static Handler sHandler;
    private static DisplayThread sInstance;

    private DisplayThread() {
        super("android.display", -3, false);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            DisplayThread displayThread = new DisplayThread();
            sInstance = displayThread;
            displayThread.start();
            sInstance.getLooper().setTraceTag(524288L);
            sHandler = ServiceThread.makeSharedHandler(sInstance.getLooper());
        }
    }

    public static DisplayThread get() {
        DisplayThread displayThread;
        synchronized (DisplayThread.class) {
            ensureThreadLocked();
            displayThread = sInstance;
        }
        return displayThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (DisplayThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    @VisibleForTesting
    public static void dispose() {
        synchronized (DisplayThread.class) {
            if (sInstance == null) {
                return;
            }
            getHandler().runWithScissors(new Runnable() { // from class: com.android.server.DisplayThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayThread.lambda$dispose$0();
                }
            }, 0L);
            sInstance = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dispose$0() {
        sInstance.quit();
    }
}
