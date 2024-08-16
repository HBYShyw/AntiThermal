package com.oplus.wrapper.window;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.SurfaceControl;
import android.window.IWindowContainerTransactionCallback;

/* loaded from: classes.dex */
public interface IWindowContainerTransactionCallback {
    void onTransactionReady(int i, SurfaceControl.Transaction transaction) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IWindowContainerTransactionCallback {
        private final android.window.IWindowContainerTransactionCallback mTarget = new IWindowContainerTransactionCallback.Stub() { // from class: com.oplus.wrapper.window.IWindowContainerTransactionCallback.Stub.1
            public void onTransactionReady(int id, SurfaceControl.Transaction t) throws RemoteException {
                Stub.this.onTransactionReady(id, t);
            }
        };

        public static IWindowContainerTransactionCallback asInterface(IBinder obj) {
            return new Proxy(IWindowContainerTransactionCallback.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IWindowContainerTransactionCallback {
            private final android.window.IWindowContainerTransactionCallback mTarget;

            Proxy(android.window.IWindowContainerTransactionCallback target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.window.IWindowContainerTransactionCallback
            public void onTransactionReady(int id, SurfaceControl.Transaction t) throws RemoteException {
                this.mTarget.onTransactionReady(id, t);
            }
        }
    }
}
