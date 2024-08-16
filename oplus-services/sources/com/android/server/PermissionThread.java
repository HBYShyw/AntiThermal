package com.android.server;

import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import com.android.internal.annotations.GuardedBy;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PermissionThread extends ServiceThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;

    @GuardedBy({"sLock"})
    private static PermissionThread sInstance;
    private static final Object sLock = new Object();

    private PermissionThread() {
        super("android.perm", 0, true);
    }

    @GuardedBy({"sLock"})
    private static void ensureThreadLocked() {
        if (sInstance != null) {
            return;
        }
        PermissionThread permissionThread = new PermissionThread();
        sInstance = permissionThread;
        permissionThread.start();
        Looper looper = sInstance.getLooper();
        looper.setTraceTag(524288L);
        looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
        sHandler = new Handler(sInstance.getLooper());
        sHandlerExecutor = new HandlerExecutor(sHandler);
    }

    public static PermissionThread get() {
        PermissionThread permissionThread;
        synchronized (sLock) {
            ensureThreadLocked();
            permissionThread = sInstance;
        }
        return permissionThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (sLock) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (sLock) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
