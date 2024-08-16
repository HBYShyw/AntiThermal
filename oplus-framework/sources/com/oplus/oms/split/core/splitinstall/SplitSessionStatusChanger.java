package com.oplus.oms.split.core.splitinstall;

import android.os.IBinder;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;
import com.oplus.oms.split.splitload.LoadListener;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SplitSessionStatusChanger<S extends OplusSplitInstallSessionState> {
    private static final String TAG = "SplitSessionStatusChanger";
    private static final long WAIT_TIME_OUT = 3000;
    private IBinder mBinder;
    final OplusSplitInstallSessionStateFactory<S> mFactory;
    final SplitInstallListenerRegistry<S> mRegistry;
    final S mSessionState;
    private List<String> mActions = new ArrayList();
    private boolean mHasCallBack = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitSessionStatusChanger(SplitInstallListenerRegistry<S> registry, OplusSplitInstallSessionStateFactory<S> factory, S sessionState) {
        this.mRegistry = registry;
        this.mFactory = factory;
        this.mSessionState = sessionState;
    }

    public void changeStatus(int status) {
        this.mRegistry.mMainHandler.post(new ChangeSessionStatusWorker(this, this.mFactory, status));
    }

    public void changeStatus(int status, int errorCode) {
        this.mRegistry.mMainHandler.post(new ChangeSessionStatusWorker(this, this.mFactory, status, errorCode));
    }

    public S getSessionState() {
        return this.mSessionState;
    }

    public void postTimeOutMessage(final OplusSplitInstallSessionState state) {
        this.mRegistry.mMainHandler.postDelayed(new Runnable() { // from class: com.oplus.oms.split.core.splitinstall.SplitSessionStatusChanger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SplitSessionStatusChanger.this.lambda$postTimeOutMessage$0(state);
            }
        }, WAIT_TIME_OUT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postTimeOutMessage$0(OplusSplitInstallSessionState state) {
        if (!this.mHasCallBack) {
            SplitLog.w(TAG, "session load timeout, sessionId: %d", Integer.valueOf(state.sessionId()));
            changeStatus(6, -100);
            this.mHasCallBack = true;
        }
    }

    public IBinder getLoaderListenerBinder() {
        if (this.mBinder == null) {
            this.mBinder = new OmsLoadListener().asBinder();
        }
        return this.mBinder;
    }

    public void putAction(String action) {
        if (!this.mActions.contains(action)) {
            this.mActions.add(action);
        }
    }

    /* loaded from: classes.dex */
    private class OmsLoadListener extends LoadListener.Stub {
        private OmsLoadListener() {
        }

        @Override // com.oplus.oms.split.splitload.LoadListener
        public void loadStatus(String action, boolean isSuccess, int errorCode) {
            SplitLog.i(SplitSessionStatusChanger.TAG, "loadStatus action: %s, success: %b, errorCode: %d", action, Boolean.valueOf(isSuccess), Integer.valueOf(errorCode));
            if (!isSuccess) {
                if (!SplitSessionStatusChanger.this.mHasCallBack) {
                    SplitSessionStatusChanger.this.changeStatus(6, errorCode);
                    SplitSessionStatusChanger.this.mHasCallBack = true;
                    return;
                }
                return;
            }
            SplitSessionStatusChanger.this.mActions.remove(action);
            if (SplitSessionStatusChanger.this.mActions.isEmpty()) {
                SplitSessionStatusChanger.this.changeStatus(5);
                SplitSessionStatusChanger.this.mHasCallBack = true;
            }
        }
    }
}
