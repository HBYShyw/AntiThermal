package com.android.server;

import android.os.Handler;
import android.os.HandlerExecutor;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OplusIoThread extends ServiceThread {
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;
    private static OplusIoThread sInstance;

    private OplusIoThread() {
        super("oplus.io", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            OplusIoThread oplusIoThread = new OplusIoThread();
            sInstance = oplusIoThread;
            oplusIoThread.start();
            sInstance.getLooper().setTraceTag(524288L);
            sHandler = new Handler(sInstance.getLooper());
            sHandlerExecutor = new HandlerExecutor(sHandler);
        }
    }

    public static OplusIoThread get() {
        OplusIoThread oplusIoThread;
        synchronized (OplusIoThread.class) {
            ensureThreadLocked();
            oplusIoThread = sInstance;
        }
        return oplusIoThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (OplusIoThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (OplusIoThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
