package com.android.server;

import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OplusFgThread extends ServiceThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;
    private static OplusFgThread sInstance;

    private OplusFgThread() {
        super("oplus.fg", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            OplusFgThread oplusFgThread = new OplusFgThread();
            sInstance = oplusFgThread;
            oplusFgThread.start();
            Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            sHandler = ServiceThread.makeSharedHandler(sInstance.getLooper());
            sHandlerExecutor = new HandlerExecutor(sHandler);
        }
    }

    public static OplusFgThread get() {
        OplusFgThread oplusFgThread;
        synchronized (OplusFgThread.class) {
            ensureThreadLocked();
            oplusFgThread = sInstance;
        }
        return oplusFgThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (OplusFgThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (OplusFgThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
