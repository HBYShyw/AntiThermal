package com.android.server;

import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OplusBackgroundThread extends HandlerThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;
    private static OplusBackgroundThread sInstance;

    private OplusBackgroundThread() {
        super("oplus.bg", 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            OplusBackgroundThread oplusBackgroundThread = new OplusBackgroundThread();
            sInstance = oplusBackgroundThread;
            oplusBackgroundThread.start();
            Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(10000L, 30000L);
            sHandler = new Handler(sInstance.getLooper());
            sHandlerExecutor = new HandlerExecutor(sHandler);
        }
    }

    public static OplusBackgroundThread get() {
        OplusBackgroundThread oplusBackgroundThread;
        synchronized (OplusBackgroundThread.class) {
            ensureThreadLocked();
            oplusBackgroundThread = sInstance;
        }
        return oplusBackgroundThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (OplusBackgroundThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (OplusBackgroundThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
