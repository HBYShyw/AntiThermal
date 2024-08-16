package com.oplus.wrapper.window;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.SurfaceControl;
import android.window.IRemoteTransitionFinishedCallback;

/* loaded from: classes.dex */
public interface IRemoteTransitionFinishedCallback {
    void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRemoteTransitionFinishedCallback {
        private final android.window.IRemoteTransitionFinishedCallback mTarget = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.oplus.wrapper.window.IRemoteTransitionFinishedCallback.Stub.1
            public void onTransitionFinished(android.window.WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction sct) throws RemoteException {
                Stub.this.onTransitionFinished(new WindowContainerTransaction(windowContainerTransaction), sct);
            }
        };

        public static IRemoteTransitionFinishedCallback asInterface(IBinder obj) {
            return new Proxy(IRemoteTransitionFinishedCallback.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRemoteTransitionFinishedCallback {
            private final android.window.IRemoteTransitionFinishedCallback mTarget;

            Proxy(android.window.IRemoteTransitionFinishedCallback target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.window.IRemoteTransitionFinishedCallback
            public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction sct) throws RemoteException {
                this.mTarget.onTransitionFinished(windowContainerTransaction.get(), sct);
            }
        }
    }
}
