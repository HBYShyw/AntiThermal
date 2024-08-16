package android.os;

import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class OplusAppBackgroundThread extends HandlerThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;
    private static OplusAppBackgroundThread sInstance;

    private OplusAppBackgroundThread() {
        super("oplus.app.bg", 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            OplusAppBackgroundThread oplusAppBackgroundThread = new OplusAppBackgroundThread();
            sInstance = oplusAppBackgroundThread;
            oplusAppBackgroundThread.start();
            Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            sHandler = new Handler(sInstance.getLooper());
            sHandlerExecutor = new HandlerExecutor(sHandler);
        }
    }

    public static OplusAppBackgroundThread get() {
        OplusAppBackgroundThread oplusAppBackgroundThread;
        synchronized (OplusAppBackgroundThread.class) {
            ensureThreadLocked();
            oplusAppBackgroundThread = sInstance;
        }
        return oplusAppBackgroundThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (OplusAppBackgroundThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (OplusAppBackgroundThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
