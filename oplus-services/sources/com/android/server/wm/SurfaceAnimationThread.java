package com.android.server.wm;

import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.ServiceThread;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class SurfaceAnimationThread extends ServiceThread {
    private static Handler sHandler;
    private static SurfaceAnimationThread sInstance;

    private SurfaceAnimationThread() {
        super("android.anim.lf", -4, false);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [android.os.HandlerThread, com.android.server.wm.SurfaceAnimationThread] */
    private static void ensureThreadLocked() {
        if (sInstance == null) {
            ?? surfaceAnimationThread = new SurfaceAnimationThread();
            sInstance = surfaceAnimationThread;
            surfaceAnimationThread.start();
            sInstance.getLooper().setTraceTag(32L);
            sHandler = ServiceThread.makeSharedHandler(sInstance.getLooper());
        }
    }

    public static SurfaceAnimationThread get() {
        SurfaceAnimationThread surfaceAnimationThread;
        synchronized (SurfaceAnimationThread.class) {
            ensureThreadLocked();
            surfaceAnimationThread = sInstance;
        }
        return surfaceAnimationThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (SurfaceAnimationThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    @VisibleForTesting
    public static void dispose() {
        synchronized (SurfaceAnimationThread.class) {
            if (sInstance == null) {
                return;
            }
            getHandler().runWithScissors(new Runnable() { // from class: com.android.server.wm.SurfaceAnimationThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SurfaceAnimationThread.lambda$dispose$0();
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
