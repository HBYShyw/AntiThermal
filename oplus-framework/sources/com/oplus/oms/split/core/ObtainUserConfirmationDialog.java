package com.oplus.oms.split.core;

import android.app.Activity;
import android.os.Bundle;
import com.oplus.oms.split.splitinstall.SplitInstallSupervisorImpl;
import com.oplus.oms.split.splitinstall.remote.SplitInstallSupervisor;
import java.util.List;

/* loaded from: classes.dex */
public abstract class ObtainUserConfirmationDialog extends Activity {
    private SplitInstallSupervisor mInstallService;
    private List<String> mModuleNames;
    private long mRealTotalBytesNeedToDownload;
    private int mSessionId;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSessionId = getIntent().getIntExtra("sessionId", 0);
        this.mRealTotalBytesNeedToDownload = getIntent().getLongExtra("realTotalBytesNeedToDownload", 0L);
        this.mModuleNames = getIntent().getStringArrayListExtra("moduleNames");
        this.mInstallService = SplitInstallSupervisorImpl.getSplitInstallSupervisor();
    }

    protected void onUserConfirm() {
        SplitInstallSupervisor splitInstallSupervisor = this.mInstallService;
        if (splitInstallSupervisor != null) {
            if (splitInstallSupervisor.continueInstallWithUserConfirmation(this.mSessionId)) {
                setResult(-1);
            }
            finish();
        }
    }

    protected void onUserCancel() {
        SplitInstallSupervisor splitInstallSupervisor = this.mInstallService;
        if (splitInstallSupervisor != null) {
            if (splitInstallSupervisor.cancelInstallWithoutUserConfirmation(this.mSessionId)) {
                setResult(0);
            }
            finish();
        }
    }

    protected List<String> getModuleNames() {
        return this.mModuleNames;
    }

    protected long getRealTotalBytesNeedToDownload() {
        return this.mRealTotalBytesNeedToDownload;
    }

    protected boolean checkInternParametersIllegal() {
        List<String> list;
        return this.mSessionId == 0 || this.mRealTotalBytesNeedToDownload <= 0 || (list = this.mModuleNames) == null || list.isEmpty();
    }
}
