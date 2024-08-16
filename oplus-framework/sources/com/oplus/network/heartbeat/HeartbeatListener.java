package com.oplus.network.heartbeat;

import android.os.Binder;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import com.android.internal.util.FunctionalUtils;
import com.oplus.network.heartbeat.HeartbeatListener;
import com.oplus.network.heartbeat.IHeartbeatListener;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class HeartbeatListener {
    public final IHeartbeatListener mCallback;

    public HeartbeatListener(Looper looper) {
        this((Executor) new HandlerExecutor(new Handler(looper)));
    }

    private HeartbeatListener(Executor e) {
        if (e == null) {
            throw new IllegalArgumentException("HeartbeatListener executor must be non-null");
        }
        this.mCallback = new IHeartbeatListenerStub(this, e);
    }

    public void onHeartbeatStateUpdate(int event, int err, int destroy, int[] args) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class IHeartbeatListenerStub extends IHeartbeatListener.Stub {
        private Executor mExecutor;
        private WeakReference<HeartbeatListener> mHeartbeatListenerWeakRef;

        IHeartbeatListenerStub(HeartbeatListener heartBeatListener, Executor executor) {
            this.mHeartbeatListenerWeakRef = new WeakReference<>(heartBeatListener);
            this.mExecutor = executor;
        }

        @Override // com.oplus.network.heartbeat.IHeartbeatListener
        public void onHeartbeatStateUpdate(final int event, final int err, final int destroy, final int[] args) {
            final HeartbeatListener csl = this.mHeartbeatListenerWeakRef.get();
            if (csl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.oplus.network.heartbeat.HeartbeatListener$IHeartbeatListenerStub$$ExternalSyntheticLambda1
                public final void runOrThrow() {
                    HeartbeatListener.IHeartbeatListenerStub.this.lambda$onHeartbeatStateUpdate$1(csl, event, err, destroy, args);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHeartbeatStateUpdate$1(final HeartbeatListener csl, final int event, final int err, final int destroy, final int[] args) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.oplus.network.heartbeat.HeartbeatListener$IHeartbeatListenerStub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HeartbeatListener.this.onHeartbeatStateUpdate(event, err, destroy, args);
                }
            });
        }
    }
}
