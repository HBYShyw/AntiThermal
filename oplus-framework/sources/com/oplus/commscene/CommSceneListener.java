package com.oplus.commscene;

import android.os.Binder;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.util.Log;
import com.android.internal.util.FunctionalUtils;
import com.oplus.commscene.CommSceneListener;
import com.oplus.commscene.ICommSceneListener;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class CommSceneListener {
    public static final int SCENE_ALIGN_TICK = 1006;
    public static final int SCENE_CHARGING = 1011;
    public static final int SCENE_DEEP_DOZE = 1003;
    public static final int SCENE_DEEP_SLEEP = 1004;
    public static final int SCENE_ENTER = 1;
    public static final int SCENE_EXIT = 0;
    public static final int SCENE_GAME_HIGH_TEMPER = 1009;
    public static final int SCENE_LIGHT_DOZE = 1002;
    public static final int SCENE_MAX = 1099;
    public static final int SCENE_MIN = 1000;
    public static final int SCENE_MODEM_STATIONARY = 1005;
    public static final int SCENE_NONE = -1;
    public static final int SCENE_NO_GAME_HIGH_TEMPER = 1010;
    public static final int SCENE_SCREEN_OFF = 1001;
    public static final int SCENE_WEAK_WIFI = 1008;
    public static final int SCENE_WIFI_ON = 1007;
    public static final String TAG = "CommSceneListener";
    public final ICommSceneListener mCallback;
    private CommSceneManager mCommSceneManager;
    private final Handler mHandler;

    public CommSceneListener(Handler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("CommSceneListener handler must be non-null");
        }
        this.mHandler = handler;
        this.mCallback = new ICommSceneListenerStub(this, new HandlerExecutor(handler));
    }

    public void listenSceneState(Set<Integer> scenes, boolean addFlag) {
        if (this.mCommSceneManager == null) {
            this.mCommSceneManager = CommSceneManager.getInstance();
        }
        CommSceneManager commSceneManager = this.mCommSceneManager;
        if (commSceneManager != null) {
            commSceneManager.listenSceneState(this.mCallback, scenes, addFlag);
        } else {
            Log.e(TAG, "failed to call listen, comm scene manager is null!");
        }
    }

    public int inquireSceneState(int scene, int phoneId) {
        if (this.mCommSceneManager == null) {
            this.mCommSceneManager = CommSceneManager.getInstance();
        }
        CommSceneManager commSceneManager = this.mCommSceneManager;
        if (commSceneManager != null) {
            return commSceneManager.inquireSceneState(scene, phoneId);
        }
        Log.e(TAG, "failed to call inquireSceneState, comm scene manager is null!");
        return -1;
    }

    public void onSceneStateChanged(int scene, int state, int phoneId) {
        Log.i(TAG, "on scene=" + scene + " state=" + state + " pId=" + phoneId);
        Handler handler = this.mHandler;
        if (handler != null) {
            Message message = handler.obtainMessage(scene);
            message.arg1 = state;
            message.arg2 = phoneId;
            this.mHandler.sendMessage(message);
            return;
        }
        Log.e(TAG, "failed to send scene message, handler is null!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ICommSceneListenerStub extends ICommSceneListener.Stub {
        private WeakReference<CommSceneListener> mCommSceneListenerWeakRef;
        private Executor mExecutor;

        ICommSceneListenerStub(CommSceneListener CommSceneListener, Executor executor) {
            this.mCommSceneListenerWeakRef = new WeakReference<>(CommSceneListener);
            this.mExecutor = executor;
        }

        @Override // com.oplus.commscene.ICommSceneListener
        public void onSceneStateChanged(final int scene, final int state, final int phoneId) {
            final CommSceneListener csl = this.mCommSceneListenerWeakRef.get();
            if (csl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.oplus.commscene.CommSceneListener$ICommSceneListenerStub$$ExternalSyntheticLambda1
                public final void runOrThrow() {
                    CommSceneListener.ICommSceneListenerStub.this.lambda$onSceneStateChanged$1(csl, scene, state, phoneId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSceneStateChanged$1(final CommSceneListener csl, final int scene, final int state, final int phoneId) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.oplus.commscene.CommSceneListener$ICommSceneListenerStub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CommSceneListener.this.onSceneStateChanged(scene, state, phoneId);
                }
            });
        }
    }
}
