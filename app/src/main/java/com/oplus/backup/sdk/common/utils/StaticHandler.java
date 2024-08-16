package com.oplus.backup.sdk.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public abstract class StaticHandler<T> extends Handler {
    private static final String TAG = "StaticHandler";
    protected WeakReference<T> ref;

    public StaticHandler(T t7) {
        this.ref = new WeakReference<>(t7);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        T t7 = this.ref.get();
        if (t7 == null) {
            BRLog.w(TAG, "ref.get is null.");
        } else {
            handleMessage(message, t7);
            super.handleMessage(message);
        }
    }

    protected abstract void handleMessage(Message message, T t7);

    public StaticHandler(T t7, Looper looper) {
        super(looper);
        this.ref = new WeakReference<>(t7);
    }
}
