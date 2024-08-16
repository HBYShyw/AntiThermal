package android.view.performance;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Slog;
import android.view.Choreographer;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class OplusChoreographerPerfInjector {
    private Object mCallbackQueues;
    private Choreographer mChoreographer;
    private Handler mHandler;
    private final Object mLock;
    private static int MSG_DO_FRAME = 0;
    private static int CALLBACK_LAST = 4;
    private static String TAG = "OplusChoreographerPerfInjector";
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public OplusChoreographerPerfInjector(Choreographer choreographer) {
        this.mChoreographer = choreographer;
        this.mHandler = choreographer.getWrapper().getHandler();
        this.mCallbackQueues = this.mChoreographer.getWrapper().getCallbackQueues();
        this.mLock = choreographer.getWrapper().getLock();
        CALLBACK_LAST = choreographer.getWrapper().getCallbackLastConst();
        MSG_DO_FRAME = choreographer.getWrapper().getMsgDoFrameConst();
    }

    public void doFrameImmediately() {
        synchronized (this.mLock) {
            Message msg = this.mHandler.obtainMessage(MSG_DO_FRAME);
            msg.setAsynchronous(true);
            this.mHandler.sendMessageAtFrontOfQueue(msg);
            if (DEBUG) {
                Slog.d(TAG, "doFrameImmediately");
            }
        }
    }

    public void postCallbackImmediately(int callbackType, Object action, Object token, long delayMillis) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        if (callbackType < 0 || callbackType > CALLBACK_LAST) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        synchronized (this.mLock) {
            long now = SystemClock.uptimeMillis();
            long dueTime = now + delayMillis;
            Object callBackQueue = Array.get(this.mCallbackQueues, callbackType);
            this.mChoreographer.getWrapper().addCallbackLockedForCallbackQueue(callBackQueue, dueTime, action, token);
            if (DEBUG) {
                Slog.d(TAG, "CallbackQueue addCallbackLocked");
            }
        }
    }
}
