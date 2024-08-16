package com.android.server.backup.remote;

import android.app.backup.IBackupCallback;
import android.os.RemoteException;
import java.util.concurrent.CompletableFuture;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FutureBackupCallback extends IBackupCallback.Stub {
    private final CompletableFuture<RemoteResult> mFuture;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FutureBackupCallback(CompletableFuture<RemoteResult> completableFuture) {
        this.mFuture = completableFuture;
    }

    public void operationComplete(long j) throws RemoteException {
        this.mFuture.complete(RemoteResult.of(j));
    }
}
