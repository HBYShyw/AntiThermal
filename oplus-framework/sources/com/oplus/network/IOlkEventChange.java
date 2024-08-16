package com.oplus.network;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOlkEventChange extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOlkEventChange";

    void onChanged(Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOlkEventChange {
        @Override // com.oplus.network.IOlkEventChange
        public void onChanged(Bundle data) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOlkEventChange {
        static final int TRANSACTION_onChanged = 1;

        public Stub() {
            attachInterface(this, IOlkEventChange.DESCRIPTOR);
        }

        public static IOlkEventChange asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOlkEventChange.DESCRIPTOR);
            if (iin != null && (iin instanceof IOlkEventChange)) {
                return (IOlkEventChange) iin;
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
                    return "onChanged";
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
                data.enforceInterface(IOlkEventChange.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOlkEventChange.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Bundle _arg0 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOlkEventChange {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOlkEventChange.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOlkEventChange
            public void onChanged(Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOlkEventChange.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
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
