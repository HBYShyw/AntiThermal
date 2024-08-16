package com.oplus.wrapper.location;

import android.location.Address;
import android.location.IGeocodeListener;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IGeocodeListener {
    void onResults(String str, List<Address> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IGeocodeListener {
        private final android.location.IGeocodeListener mTarget = new IGeocodeListener.Stub() { // from class: com.oplus.wrapper.location.IGeocodeListener.Stub.1
            public void onResults(String error, List<Address> results) throws RemoteException {
                Stub.this.onResults(error, results);
            }
        };

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        public static IGeocodeListener asInterface(IBinder obj) {
            return new Proxy(IGeocodeListener.Stub.asInterface(obj));
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGeocodeListener {
            private final android.location.IGeocodeListener mTarget;

            Proxy(android.location.IGeocodeListener target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.location.IGeocodeListener
            public void onResults(String error, List<Address> results) throws RemoteException {
                this.mTarget.onResults(error, results);
            }
        }
    }
}
