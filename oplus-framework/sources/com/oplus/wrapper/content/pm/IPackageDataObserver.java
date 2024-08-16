package com.oplus.wrapper.content.pm;

import android.content.pm.IPackageDataObserver;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPackageDataObserver {
    void onRemoveCompleted(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IPackageDataObserver {
        private final android.content.pm.IPackageDataObserver mTarget = new IPackageDataObserver.Stub() { // from class: com.oplus.wrapper.content.pm.IPackageDataObserver.Stub.1
            public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                Stub.this.onRemoveCompleted(packageName, succeeded);
            }
        };

        public static IPackageDataObserver asInterface(IBinder obj) {
            return new Proxy(IPackageDataObserver.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPackageDataObserver {
            private final android.content.pm.IPackageDataObserver mTarget;

            Proxy(android.content.pm.IPackageDataObserver target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.content.pm.IPackageDataObserver
            public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                this.mTarget.onRemoveCompleted(packageName, succeeded);
            }
        }
    }
}
