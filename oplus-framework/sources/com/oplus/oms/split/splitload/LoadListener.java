package com.oplus.oms.split.splitload;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface LoadListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.oms.split.splitload.LoadListener";

    void loadStatus(String str, boolean z, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements LoadListener {
        @Override // com.oplus.oms.split.splitload.LoadListener
        public void loadStatus(String action, boolean isSuccess, int errorCode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements LoadListener {
        static final int TRANSACTION_loadStatus = 1;

        public Stub() {
            attachInterface(this, LoadListener.DESCRIPTOR);
        }

        public static LoadListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(LoadListener.DESCRIPTOR);
            if (iin != null && (iin instanceof LoadListener)) {
                return (LoadListener) iin;
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
                    return "loadStatus";
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
                data.enforceInterface(LoadListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(LoadListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            boolean _arg1 = data.readBoolean();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            loadStatus(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements LoadListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return LoadListener.DESCRIPTOR;
            }

            @Override // com.oplus.oms.split.splitload.LoadListener
            public void loadStatus(String action, boolean isSuccess, int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(LoadListener.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeBoolean(isSuccess);
                    _data.writeInt(errorCode);
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
