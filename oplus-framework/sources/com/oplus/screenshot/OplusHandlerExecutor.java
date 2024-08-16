package com.oplus.screenshot;

import android.os.Handler;
import android.os.Looper;
import com.oplus.util.OplusLog;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class OplusHandlerExecutor implements Executor {
    private static final String TAG = "OplusHandlerExecutor";
    private final long mDelayMillis;
    private final Handler mHandler;

    public OplusHandlerExecutor() {
        this(new Handler(Looper.getMainLooper()), 0L);
    }

    public OplusHandlerExecutor(long delayMillis) {
        this(new Handler(Looper.getMainLooper()), delayMillis);
    }

    public OplusHandlerExecutor(Handler handler, long delayMillis) {
        this.mHandler = handler;
        this.mDelayMillis = delayMillis;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        if (!this.mHandler.postDelayed(command, this.mDelayMillis)) {
            OplusLog.e(TAG, "post command to HandlerExecutor failed!");
        }
    }
}
