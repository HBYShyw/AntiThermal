package com.oplus.wrapper.app;

import android.app.IProcessObserver;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IProcessObserver {
    void onForegroundActivitiesChanged(int i, int i2, boolean z) throws RemoteException;

    void onForegroundServicesChanged(int i, int i2, int i3) throws RemoteException;

    void onProcessDied(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IProcessObserver {
        private final android.app.IProcessObserver mTarget = new IProcessObserver.Stub() { // from class: com.oplus.wrapper.app.IProcessObserver.Stub.1
            public void onForegroundActivitiesChanged(int i, int i1, boolean b) throws RemoteException {
                Stub.this.onForegroundActivitiesChanged(i, i1, b);
            }

            public void onForegroundServicesChanged(int i, int i1, int i2) throws RemoteException {
                Stub.this.onForegroundServicesChanged(i, i1, i2);
            }

            public void onProcessDied(int i, int i1) throws RemoteException {
                Stub.this.onProcessDied(i, i1);
            }
        };

        public static IProcessObserver asInterface(IBinder obj) {
            return new Proxy(IProcessObserver.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IProcessObserver {
            private final android.app.IProcessObserver mTarget;

            Proxy(android.app.IProcessObserver target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.IProcessObserver
            public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
                this.mTarget.onForegroundActivitiesChanged(pid, uid, foregroundActivities);
            }

            @Override // com.oplus.wrapper.app.IProcessObserver
            public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {
                this.mTarget.onForegroundServicesChanged(pid, uid, serviceTypes);
            }

            @Override // com.oplus.wrapper.app.IProcessObserver
            public void onProcessDied(int pid, int uid) throws RemoteException {
                this.mTarget.onProcessDied(pid, uid);
            }
        }
    }
}
