package android.view;

import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class OplusViewBackgroundThread extends HandlerThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;
    private static OplusViewBackgroundThread sInstance;

    private OplusViewBackgroundThread() {
        super("oplus.view.bg", 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            OplusViewBackgroundThread oplusViewBackgroundThread = new OplusViewBackgroundThread();
            sInstance = oplusViewBackgroundThread;
            oplusViewBackgroundThread.start();
            Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            sHandler = new Handler(sInstance.getLooper());
            sHandlerExecutor = new HandlerExecutor(sHandler);
        }
    }

    public static OplusViewBackgroundThread get() {
        OplusViewBackgroundThread oplusViewBackgroundThread;
        synchronized (OplusViewBackgroundThread.class) {
            ensureThreadLocked();
            oplusViewBackgroundThread = sInstance;
        }
        return oplusViewBackgroundThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (OplusViewBackgroundThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (OplusViewBackgroundThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
