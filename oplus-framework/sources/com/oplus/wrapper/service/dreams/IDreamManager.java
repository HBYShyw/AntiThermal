package com.oplus.wrapper.service.dreams;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.service.dreams.IDreamManager;

/* loaded from: classes.dex */
public interface IDreamManager {
    boolean isDreaming() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IDreamManager {
        private final android.service.dreams.IDreamManager mIDreamManager = new IDreamManager.Stub() { // from class: com.oplus.wrapper.service.dreams.IDreamManager.Stub.1
            public void dream() throws RemoteException {
            }

            public void awaken() throws RemoteException {
            }

            public void setDreamComponents(ComponentName[] componentNames) throws RemoteException {
            }

            public ComponentName[] getDreamComponents() throws RemoteException {
                return new ComponentName[0];
            }

            public ComponentName getDefaultDreamComponentForUser(int i) throws RemoteException {
                return null;
            }

            public void testDream(int i, ComponentName componentName) throws RemoteException {
            }

            public boolean isDreaming() throws RemoteException {
                return Stub.this.isDreaming();
            }

            public boolean isDreamingOrInPreview() throws RemoteException {
                return false;
            }

            public void finishSelf(IBinder iBinder, boolean b) throws RemoteException {
            }

            public void startDozing(IBinder iBinder, int i, int i1) throws RemoteException {
            }

            public void stopDozing(IBinder iBinder) throws RemoteException {
            }

            public void forceAmbientDisplayEnabled(boolean b) throws RemoteException {
            }

            public ComponentName[] getDreamComponentsForUser(int i) throws RemoteException {
                return new ComponentName[0];
            }

            public void setDreamComponentsForUser(int i, ComponentName[] componentNames) throws RemoteException {
            }

            public void setSystemDreamComponent(ComponentName componentName) throws RemoteException {
            }

            public void registerDreamOverlayService(ComponentName componentName) throws RemoteException {
            }
        };

        public static IDreamManager asInterface(IBinder obj) {
            return new Proxy(IDreamManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mIDreamManager.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IDreamManager {
            private final android.service.dreams.IDreamManager mIDreamManager;

            Proxy(android.service.dreams.IDreamManager target) {
                this.mIDreamManager = target;
            }

            @Override // com.oplus.wrapper.service.dreams.IDreamManager
            public boolean isDreaming() throws RemoteException {
                return this.mIDreamManager.isDreaming();
            }
        }
    }
}
