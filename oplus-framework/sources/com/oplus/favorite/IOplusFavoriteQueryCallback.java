package com.oplus.favorite;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusFavoriteQueryCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.favorite.IOplusFavoriteQueryCallback";

    void onQueryResult(OplusFavoriteQueryResult oplusFavoriteQueryResult) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFavoriteQueryCallback {
        @Override // com.oplus.favorite.IOplusFavoriteQueryCallback
        public void onQueryResult(OplusFavoriteQueryResult result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFavoriteQueryCallback {
        static final int TRANSACTION_onQueryResult = 1;

        public Stub() {
            attachInterface(this, IOplusFavoriteQueryCallback.DESCRIPTOR);
        }

        public static IOplusFavoriteQueryCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFavoriteQueryCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFavoriteQueryCallback)) {
                return (IOplusFavoriteQueryCallback) iin;
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
                    return "onQueryResult";
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
                data.enforceInterface(IOplusFavoriteQueryCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFavoriteQueryCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusFavoriteQueryResult _arg0 = (OplusFavoriteQueryResult) data.readTypedObject(OplusFavoriteQueryResult.CREATOR);
                            data.enforceNoDataAvail();
                            onQueryResult(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFavoriteQueryCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFavoriteQueryCallback.DESCRIPTOR;
            }

            @Override // com.oplus.favorite.IOplusFavoriteQueryCallback
            public void onQueryResult(OplusFavoriteQueryResult result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFavoriteQueryCallback.DESCRIPTOR);
                    _data.writeTypedObject(result, 0);
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
