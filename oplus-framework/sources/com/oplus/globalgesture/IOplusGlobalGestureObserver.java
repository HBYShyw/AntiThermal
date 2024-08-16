package com.oplus.globalgesture;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusGlobalGestureObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.globalgesture.IOplusGlobalGestureObserver";

    void onGestureDetected(int i, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusGlobalGestureObserver {
        @Override // com.oplus.globalgesture.IOplusGlobalGestureObserver
        public void onGestureDetected(int type, Bundle extras) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusGlobalGestureObserver {
        static final int TRANSACTION_onGestureDetected = 1;

        public Stub() {
            attachInterface(this, IOplusGlobalGestureObserver.DESCRIPTOR);
        }

        public static IOplusGlobalGestureObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusGlobalGestureObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusGlobalGestureObserver)) {
                return (IOplusGlobalGestureObserver) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onGestureDetected";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusGlobalGestureObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusGlobalGestureObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onGestureDetected(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusGlobalGestureObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusGlobalGestureObserver.DESCRIPTOR;
            }

            @Override // com.oplus.globalgesture.IOplusGlobalGestureObserver
            public void onGestureDetected(int type, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGlobalGestureObserver.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
