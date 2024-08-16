package com.oplus.oms.split.core.splitinstall;

import android.os.Bundle;
import com.oplus.oms.split.core.remote.RemoteTask;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;
import com.oplus.oms.split.core.tasks.OplusTaskWrapper;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;

/* loaded from: classes.dex */
final class GetSessionStateTask<S extends OplusSplitInstallSessionState> extends RemoteTask {
    private final OplusSplitInstallSessionStateFactory<S> mFactory;
    private final int mSessionId;
    private final OplusTaskWrapper<S> mTask;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GetSessionStateTask(OplusSplitInstallSessionStateFactory<S> factory, OplusTaskWrapper<S> task, int sessionId) {
        super(task);
        this.mFactory = factory;
        this.mTask = task;
        this.mSessionId = sessionId;
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask
    protected void execute(SplitInstallSupervisor supervisor) throws Exception {
        supervisor.getSessionState(this.mSessionId, this);
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask, com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onGetSession(int i, Bundle bundle) {
        super.onGetSession(i, bundle);
        this.mTask.setResult(this.mFactory.create(bundle));
    }
}
