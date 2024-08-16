package com.oplus.oms.split.core.splitinstall;

import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ChangeSessionStatusWorker<S extends OplusSplitInstallSessionState> implements Runnable {
    private final SplitSessionStatusChanger<S> mChanger;
    private final int mErrorCode;
    private final OplusSplitInstallSessionStateFactory<S> mFactory;
    private final int mStatus;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChangeSessionStatusWorker(SplitSessionStatusChanger<S> changer, OplusSplitInstallSessionStateFactory<S> factory, int status) {
        this(changer, factory, status, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChangeSessionStatusWorker(SplitSessionStatusChanger<S> changer, OplusSplitInstallSessionStateFactory<S> factory, int status, int errorCode) {
        this.mChanger = changer;
        this.mFactory = factory;
        this.mStatus = status;
        this.mErrorCode = errorCode;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mErrorCode != 0) {
            this.mChanger.mRegistry.notifyListeners(this.mFactory.newState(this.mChanger.mSessionState, this.mStatus, this.mErrorCode));
        } else {
            this.mChanger.mRegistry.notifyListeners(this.mFactory.newState(this.mChanger.mSessionState, this.mStatus));
        }
    }
}
