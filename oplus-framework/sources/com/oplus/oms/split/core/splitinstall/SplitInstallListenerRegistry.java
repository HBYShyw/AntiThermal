package com.oplus.oms.split.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.listener.StateUpdateListenerRegister;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;

/* loaded from: classes.dex */
public final class SplitInstallListenerRegistry<S extends OplusSplitInstallSessionState> extends StateUpdateListenerRegister<S> {
    private static final String TAG = "SplitInstallListenerRegistry";
    private final SplitSessionLoader mLoader;
    final Handler mMainHandler;
    private final OplusSplitInstallSessionStateFactory<S> mStateFactory;

    public SplitInstallListenerRegistry(Context context, OplusSplitInstallSessionStateFactory<S> stateFactory) {
        this(context, SplitSessionLoaderSingleton.get(), stateFactory);
    }

    private SplitInstallListenerRegistry(Context context, SplitSessionLoader loader, OplusSplitInstallSessionStateFactory<S> stateFactory) {
        super(new IntentFilter("com.oplus.oms.play.core.receiver.SplitInstallUpdateIntentService"), context);
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mLoader = loader;
        this.mStateFactory = stateFactory;
    }

    public void changeStatus(Intent intent) {
        SplitSessionLoader splitSessionLoader;
        Bundle data = intent.getBundleExtra("session_state");
        if (data == null) {
            SplitLog.e(TAG, "receive null session_state", new Object[0]);
            return;
        }
        S sessionState = this.mStateFactory.create(data);
        SplitLog.i(TAG, "changeStatus onReceive: sessionId: %d, status: %d, errorCode: %d", Integer.valueOf(sessionState.sessionId()), Integer.valueOf(sessionState.status()), Integer.valueOf(sessionState.errorCode()));
        if (sessionState.status() == 10 && (splitSessionLoader = this.mLoader) != null) {
            splitSessionLoader.load(this.mContext, sessionState.mSplitFileIntents, new SplitSessionStatusChanger<>(this, this.mStateFactory, sessionState));
        } else {
            notifyListeners(sessionState);
        }
    }
}
