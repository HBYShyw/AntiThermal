package com.android.server;

import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AnimationThread extends ServiceThread {
    private static Handler sHandler;
    private static AnimationThread sInstance;

    private AnimationThread() {
        super("android.anim", -4, false);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            AnimationThread animationThread = new AnimationThread();
            sInstance = animationThread;
            animationThread.start();
            sInstance.getLooper().setTraceTag(32L);
            sHandler = ServiceThread.makeSharedHandler(sInstance.getLooper());
        }
    }

    public static AnimationThread get() {
        AnimationThread animationThread;
        synchronized (AnimationThread.class) {
            ensureThreadLocked();
            animationThread = sInstance;
        }
        return animationThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (AnimationThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    @VisibleForTesting
    public static void dispose() {
        synchronized (AnimationThread.class) {
            if (sInstance == null) {
                return;
            }
            getHandler().runWithScissors(new Runnable() { // from class: com.android.server.AnimationThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimationThread.lambda$dispose$0();
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
