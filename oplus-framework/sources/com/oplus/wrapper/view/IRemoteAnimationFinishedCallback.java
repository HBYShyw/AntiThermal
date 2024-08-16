package com.oplus.wrapper.view;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;

/* loaded from: classes.dex */
public interface IRemoteAnimationFinishedCallback {
    void onAnimationFinished() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRemoteAnimationFinishedCallback {
        private final android.view.IRemoteAnimationFinishedCallback mTarget = new IRemoteAnimationFinishedCallback.Stub() { // from class: com.oplus.wrapper.view.IRemoteAnimationFinishedCallback.Stub.1
            public void onAnimationFinished() throws RemoteException {
                Stub.this.onAnimationFinished();
            }
        };

        public static IRemoteAnimationFinishedCallback asInterface(IBinder obj) {
            return new Proxy(IRemoteAnimationFinishedCallback.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRemoteAnimationFinishedCallback {
            private final android.view.IRemoteAnimationFinishedCallback mTarget;

            Proxy(android.view.IRemoteAnimationFinishedCallback target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.view.IRemoteAnimationFinishedCallback
            public void onAnimationFinished() throws RemoteException {
                this.mTarget.onAnimationFinished();
            }
        }
    }
}
