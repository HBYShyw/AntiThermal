package com.oplus.direct;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusDirectFindCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.direct.IOplusDirectFindCallback";

    void onDirectInfoFound(OplusDirectFindResult oplusDirectFindResult) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusDirectFindCallback {
        @Override // com.oplus.direct.IOplusDirectFindCallback
        public void onDirectInfoFound(OplusDirectFindResult result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusDirectFindCallback {
        static final int TRANSACTION_onDirectInfoFound = 1;

        public Stub() {
            attachInterface(this, IOplusDirectFindCallback.DESCRIPTOR);
        }

        public static IOplusDirectFindCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusDirectFindCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusDirectFindCallback)) {
                return (IOplusDirectFindCallback) iin;
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
                    return "onDirectInfoFound";
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
                data.enforceInterface(IOplusDirectFindCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusDirectFindCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusDirectFindResult _arg0 = (OplusDirectFindResult) data.readTypedObject(OplusDirectFindResult.CREATOR);
                            data.enforceNoDataAvail();
                            onDirectInfoFound(_arg0);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusDirectFindCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusDirectFindCallback.DESCRIPTOR;
            }

            @Override // com.oplus.direct.IOplusDirectFindCallback
            public void onDirectInfoFound(OplusDirectFindResult result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDirectFindCallback.DESCRIPTOR);
                    _data.writeTypedObject(result, 0);
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
