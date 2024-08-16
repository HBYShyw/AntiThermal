package com.oplus.oms.split.core.splitinstall;

import android.os.Bundle;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.remote.RemoteTask;
import com.oplus.oms.split.core.tasks.OplusTaskWrapper;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;

/* loaded from: classes.dex */
final class CancelInstallTask extends RemoteTask {
    private static final String TAG = "CancelInstallTask";
    private final int mSessionId;
    private final OplusTaskWrapper<Void> mTask;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CancelInstallTask(OplusTaskWrapper<Void> task, int sessionId) {
        super(task);
        this.mTask = task;
        this.mSessionId = sessionId;
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask
    protected void execute(SplitInstallSupervisor supervisor) throws Exception {
        supervisor.cancelInstall(this.mSessionId, this);
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask, com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onCancelInstall(int sessionId, Bundle data) {
        super.onCancelInstall(sessionId, data);
        SplitLog.i(TAG, "onCancelInstall session: %d", Integer.valueOf(sessionId));
        this.mTask.setResult(null);
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask, com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onError(Bundle data) {
        super.onError(data);
        SplitLog.i(TAG, "onCancelInstall error session: %d", Integer.valueOf(this.mSessionId));
    }
}
