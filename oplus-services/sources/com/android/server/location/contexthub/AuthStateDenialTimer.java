package com.android.server.location.contexthub;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AuthStateDenialTimer {
    private static final int MSG = 1;
    private static final long TIMEOUT_MS = TimeUnit.SECONDS.toMillis(60);
    private boolean mCancelled = false;
    private final ContextHubClientBroker mClient;
    private final Handler mHandler;
    private final long mNanoAppId;
    private long mStopTimeInFuture;

    public AuthStateDenialTimer(ContextHubClientBroker contextHubClientBroker, long j, Looper looper) {
        this.mClient = contextHubClientBroker;
        this.mNanoAppId = j;
        this.mHandler = new CountDownHandler(looper);
    }

    public synchronized void cancel() {
        this.mCancelled = true;
        this.mHandler.removeMessages(1);
    }

    public synchronized void start() {
        this.mCancelled = false;
        this.mStopTimeInFuture = SystemClock.elapsedRealtime() + TIMEOUT_MS;
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(1));
    }

    public void onFinish() {
        this.mClient.handleAuthStateTimerExpiry(this.mNanoAppId);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class CountDownHandler extends Handler {
        CountDownHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (AuthStateDenialTimer.this) {
                if (AuthStateDenialTimer.this.mCancelled) {
                    return;
                }
                long elapsedRealtime = AuthStateDenialTimer.this.mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (elapsedRealtime <= 0) {
                    AuthStateDenialTimer.this.onFinish();
                } else {
                    sendMessageDelayed(obtainMessage(1), elapsedRealtime);
                }
            }
        }
    }
}
