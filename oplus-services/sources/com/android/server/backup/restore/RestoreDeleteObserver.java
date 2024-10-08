package com.android.server.backup.restore;

import android.content.pm.IPackageDeleteObserver;
import android.os.RemoteException;
import com.android.internal.annotations.GuardedBy;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RestoreDeleteObserver extends IPackageDeleteObserver.Stub {

    @GuardedBy({"mDone"})
    private final AtomicBoolean mDone = new AtomicBoolean();

    public void reset() {
        synchronized (this.mDone) {
            this.mDone.set(false);
        }
    }

    public void waitForCompletion() {
        synchronized (this.mDone) {
            while (!this.mDone.get()) {
                try {
                    this.mDone.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    public void packageDeleted(String str, int i) throws RemoteException {
        synchronized (this.mDone) {
            this.mDone.set(true);
            this.mDone.notifyAll();
        }
    }
}
