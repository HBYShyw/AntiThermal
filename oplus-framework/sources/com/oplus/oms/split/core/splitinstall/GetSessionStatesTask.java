package com.oplus.oms.split.core.splitinstall;

import android.os.Bundle;
import com.oplus.oms.split.core.remote.RemoteTask;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;
import com.oplus.oms.split.core.tasks.OplusTaskWrapper;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
final class GetSessionStatesTask<S extends OplusSplitInstallSessionState> extends RemoteTask {
    private final OplusSplitInstallSessionStateFactory<S> mFactory;
    private final OplusTaskWrapper<List<S>> mTask;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GetSessionStatesTask(OplusSplitInstallSessionStateFactory<S> factory, OplusTaskWrapper<List<S>> task) {
        super(task);
        this.mFactory = factory;
        this.mTask = task;
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask
    protected void execute(SplitInstallSupervisor supervisor) throws Exception {
        supervisor.getSessionStates(this);
    }

    @Override // com.oplus.oms.split.core.remote.RemoteTask, com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onGetSessionStates(List<Bundle> data) {
        super.onGetSessionStates(data);
        List<S> sessionStates = new ArrayList<>(data.size());
        for (Bundle bundle : data) {
            sessionStates.add(this.mFactory.create(bundle));
        }
        this.mTask.setResult(sessionStates);
    }
}
