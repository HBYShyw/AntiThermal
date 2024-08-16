package com.oplus.favorite;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public class OplusFavoriteHandler extends Handler {
    private static final boolean DBG = false;
    private final HandlerThread mThread;

    public OplusFavoriteHandler(HandlerThread thread, String tag) {
        super(getLooper(thread));
        this.mThread = thread;
        OplusLog.i(false, IOplusFavoriteConstans.TAG_UNIFY, "<init>() : " + thread.getName() + " @ " + tag);
    }

    public boolean quit() {
        return this.mThread.quit();
    }

    private static Looper getLooper(HandlerThread thread) {
        Looper looper = thread.getLooper();
        if (looper != null) {
            return looper;
        }
        throw new IllegalThreadStateException("Looper is null of " + thread.getName() + "[" + thread.getThreadId() + "]");
    }
}
