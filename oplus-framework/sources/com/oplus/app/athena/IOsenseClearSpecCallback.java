package com.oplus.app.athena;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOsenseClearSpecCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.athena.IOsenseClearSpecCallback";

    void onResult(OsenseExternalClearResult osenseExternalClearResult) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOsenseClearSpecCallback {
        @Override // com.oplus.app.athena.IOsenseClearSpecCallback
        public void onResult(OsenseExternalClearResult clearResult) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOsenseClearSpecCallback {
        static final int TRANSACTION_onResult = 1;

        public Stub() {
            attachInterface(this, IOsenseClearSpecCallback.DESCRIPTOR);
        }

        public static IOsenseClearSpecCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOsenseClearSpecCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOsenseClearSpecCallback)) {
                return (IOsenseClearSpecCallback) iin;
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
                    return "onResult";
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
                data.enforceInterface(IOsenseClearSpecCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOsenseClearSpecCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OsenseExternalClearResult _arg0 = (OsenseExternalClearResult) data.readTypedObject(OsenseExternalClearResult.CREATOR);
                            data.enforceNoDataAvail();
                            onResult(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOsenseClearSpecCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOsenseClearSpecCallback.DESCRIPTOR;
            }

            @Override // com.oplus.app.athena.IOsenseClearSpecCallback
            public void onResult(OsenseExternalClearResult clearResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseClearSpecCallback.DESCRIPTOR);
                    _data.writeTypedObject(clearResult, 0);
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
