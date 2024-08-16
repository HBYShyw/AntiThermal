package com.oplus.wrapper.view;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.IRotationWatcher;

/* loaded from: classes.dex */
public interface IRotationWatcher {
    void onRotationChanged(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IRotationWatcher {
        private final android.view.IRotationWatcher mRotationWatcher = new IRotationWatcher.Stub() { // from class: com.oplus.wrapper.view.IRotationWatcher.Stub.1
            public void onRotationChanged(int rotation) throws RemoteException {
                Stub.this.onRotationChanged(rotation);
            }
        };

        public static IRotationWatcher asInterface(IBinder obj) {
            return new Proxy(IRotationWatcher.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mRotationWatcher.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRotationWatcher {
            private final android.view.IRotationWatcher mRotationWatcher;

            Proxy(android.view.IRotationWatcher rotationWatcher) {
                this.mRotationWatcher = rotationWatcher;
            }

            @Override // com.oplus.wrapper.view.IRotationWatcher
            public void onRotationChanged(int rotation) throws RemoteException {
                this.mRotationWatcher.onRotationChanged(rotation);
            }
        }
    }
}
