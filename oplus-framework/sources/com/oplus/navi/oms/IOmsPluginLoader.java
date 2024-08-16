package com.oplus.navi.oms;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOmsPluginLoader extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.navi.oms.IOmsPluginLoader";

    void onQueryCompleted(Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOmsPluginLoader {
        @Override // com.oplus.navi.oms.IOmsPluginLoader
        public void onQueryCompleted(Bundle bundle) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOmsPluginLoader {
        static final int TRANSACTION_onQueryCompleted = 1;

        public Stub() {
            attachInterface(this, IOmsPluginLoader.DESCRIPTOR);
        }

        public static IOmsPluginLoader asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOmsPluginLoader.DESCRIPTOR);
            if (iin != null && (iin instanceof IOmsPluginLoader)) {
                return (IOmsPluginLoader) iin;
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
                    return "onQueryCompleted";
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
                data.enforceInterface(IOmsPluginLoader.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOmsPluginLoader.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Bundle _arg0 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onQueryCompleted(_arg0);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOmsPluginLoader {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOmsPluginLoader.DESCRIPTOR;
            }

            @Override // com.oplus.navi.oms.IOmsPluginLoader
            public void onQueryCompleted(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOmsPluginLoader.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
