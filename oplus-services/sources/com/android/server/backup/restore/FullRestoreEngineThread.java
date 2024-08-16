package com.android.server.backup.restore;

import android.os.ParcelFileDescriptor;
import java.io.InputStream;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class FullRestoreEngineThread implements Runnable {
    FullRestoreEngine mEngine;
    InputStream mEngineStream;
    private final boolean mMustKillAgent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FullRestoreEngineThread(FullRestoreEngine fullRestoreEngine, ParcelFileDescriptor parcelFileDescriptor) {
        this.mEngine = fullRestoreEngine;
        fullRestoreEngine.setRunning(true);
        this.mEngineStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
        this.mMustKillAgent = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FullRestoreEngineThread(FullRestoreEngine fullRestoreEngine, InputStream inputStream) {
        this.mEngine = fullRestoreEngine;
        fullRestoreEngine.setRunning(true);
        this.mEngineStream = inputStream;
        this.mMustKillAgent = true;
    }

    public boolean isRunning() {
        return this.mEngine.isRunning();
    }

    public int waitForResult() {
        return this.mEngine.waitForResult();
    }

    @Override // java.lang.Runnable
    public void run() {
        while (this.mEngine.isRunning()) {
            try {
                FullRestoreEngine fullRestoreEngine = this.mEngine;
                fullRestoreEngine.restoreOneFile(this.mEngineStream, this.mMustKillAgent, fullRestoreEngine.mBuffer, fullRestoreEngine.mOnlyPackage, fullRestoreEngine.mAllowApks, fullRestoreEngine.mEphemeralOpToken, fullRestoreEngine.mMonitor);
            } finally {
                IoUtils.closeQuietly(this.mEngineStream);
            }
        }
    }

    public void handleTimeout() {
        IoUtils.closeQuietly(this.mEngineStream);
        this.mEngine.handleTimeout();
    }
}
