package com.oplus.oms.split.core.remote;

import android.os.Bundle;
import android.provider.oplus.Telephony;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallException;
import com.oplus.oms.split.core.tasks.OplusTaskWrapper;
import com.oplus.oms.split.splitinstall.SplitInstallSupervisorImpl;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;
import java.util.List;

/* loaded from: classes.dex */
public abstract class RemoteTask implements Runnable, SplitInstallSupervisor.Callback {
    private static final String TAG = "RemoteTask";
    private final SplitInstallSupervisor mInstallSupervisor = SplitInstallSupervisorImpl.getSplitInstallSupervisor();
    protected final OplusTaskWrapper<?> mTaskWrapper;

    protected abstract void execute(SplitInstallSupervisor splitInstallSupervisor) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public RemoteTask(OplusTaskWrapper<?> task) {
        this.mTaskWrapper = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            SplitInstallSupervisor splitInstallSupervisor = this.mInstallSupervisor;
            if (splitInstallSupervisor != null) {
                execute(splitInstallSupervisor);
            } else {
                onError(SplitInstallSupervisor.bundleErrorCode(-100));
                SplitLog.w(TAG, "Have you call SplitInstallSupervisorImpl#install method?", new Object[0]);
            }
        } catch (Exception e) {
            SplitLog.e(TAG, "execute error, msg: %s", e.getMessage());
            OplusTaskWrapper<?> oplusTaskWrapper = this.mTaskWrapper;
            if (oplusTaskWrapper != null) {
                oplusTaskWrapper.setException(e);
            }
        }
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onError(Bundle data) {
        SplitLog.i(TAG, "onError data " + data, new Object[0]);
        this.mTaskWrapper.setException(new OplusSplitInstallException(data.getInt(Telephony.TextBasedSmsColumns.ERROR_CODE)));
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onStartInstall(int sessionId, Bundle data) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onCancelInstall(int sessionId, Bundle data) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onGetSession(int sessionId, Bundle data) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onDeferredUninstall(Bundle data) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onDeferredInstall(Bundle data) {
    }

    @Override // com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor.Callback
    public void onGetSessionStates(List<Bundle> data) {
    }
}
