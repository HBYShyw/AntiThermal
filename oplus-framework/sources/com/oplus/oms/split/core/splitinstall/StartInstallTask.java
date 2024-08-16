package com.oplus.oms.split.core.splitinstall;

import android.os.Bundle;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.remote.RemoteTask;
import com.oplus.oms.split.core.tasks.OplusTaskWrapper;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;
import java.util.List;

/* loaded from: classes.dex */
final class StartInstallTask extends RemoteTask {
    private static final String TAG = "StartInstallTask";
    private final List<String> mModuleNames;
    private final OplusTaskWrapper<Integer> mTask;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartInstallTask(OplusTaskWrapper<Integer> task, List<String> moduleNames) {
        super(task);
        this.mModuleNames = moduleNames;
        this.mTask = task;
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask
    protected void execute(SplitInstallSupervisor supervisor) {
        supervisor.startInstall(this.mModuleNames, this);
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask, com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onStartInstall(int sessionId, Bundle data) {
        SplitLog.i(TAG, "onStartInstall sessionId " + sessionId, new Object[0]);
        this.mTask.setResult(Integer.valueOf(sessionId));
    }
}
