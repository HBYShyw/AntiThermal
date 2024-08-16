package com.android.server.backup.transport;

import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.backup.ITransportStatusCallback;
import com.android.server.backup.BackupAndRestoreFeatureFlags;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TransportStatusCallback extends ITransportStatusCallback.Stub {
    private static final int OPERATION_STATUS_DEFAULT = 0;
    private static final String TAG = "TransportStatusCallback";

    @GuardedBy({"this"})
    private boolean mHasCompletedOperation;

    @GuardedBy({"this"})
    private int mOperationStatus;
    private final long mOperationTimeout;

    public TransportStatusCallback() {
        this.mOperationStatus = 0;
        this.mHasCompletedOperation = false;
        this.mOperationTimeout = BackupAndRestoreFeatureFlags.getBackupTransportCallbackTimeoutMillis();
    }

    @VisibleForTesting
    TransportStatusCallback(int i) {
        this.mOperationStatus = 0;
        this.mHasCompletedOperation = false;
        this.mOperationTimeout = i;
    }

    public synchronized void onOperationCompleteWithStatus(int i) throws RemoteException {
        this.mHasCompletedOperation = true;
        this.mOperationStatus = i;
        notifyAll();
    }

    public synchronized void onOperationComplete() throws RemoteException {
        onOperationCompleteWithStatus(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getOperationStatus() {
        if (this.mHasCompletedOperation) {
            return this.mOperationStatus;
        }
        long j = this.mOperationTimeout;
        while (!this.mHasCompletedOperation && j > 0) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                wait(j);
                if (this.mHasCompletedOperation) {
                    return this.mOperationStatus;
                }
                j -= System.currentTimeMillis() - currentTimeMillis;
            } catch (InterruptedException e) {
                Slog.w(TAG, "Couldn't get operation status from transport: ", e);
            }
        }
        Slog.w(TAG, "Couldn't get operation status from transport");
        return -1000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void reset() {
        this.mHasCompletedOperation = false;
        this.mOperationStatus = 0;
    }
}
