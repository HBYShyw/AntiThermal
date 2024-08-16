package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusUidNwChange extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusUidNwChange";

    void appFreezeDataNotify(String str) throws RemoteException;

    void uidNetworkChange(int i, boolean z, boolean z2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusUidNwChange {
        @Override // com.oplus.network.IOplusUidNwChange
        public void uidNetworkChange(int uid, boolean networkFail, boolean isForegroundApp) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusUidNwChange
        public void appFreezeDataNotify(String data) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusUidNwChange {
        static final int TRANSACTION_appFreezeDataNotify = 2;
        static final int TRANSACTION_uidNetworkChange = 1;

        public Stub() {
            attachInterface(this, IOplusUidNwChange.DESCRIPTOR);
        }

        public static IOplusUidNwChange asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusUidNwChange.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusUidNwChange)) {
                return (IOplusUidNwChange) iin;
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
                    return "uidNetworkChange";
                case 2:
                    return "appFreezeDataNotify";
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
                data.enforceInterface(IOplusUidNwChange.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusUidNwChange.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            boolean _arg1 = data.readBoolean();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            uidNetworkChange(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            appFreezeDataNotify(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusUidNwChange {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusUidNwChange.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusUidNwChange
            public void uidNetworkChange(int uid, boolean networkFail, boolean isForegroundApp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusUidNwChange.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeBoolean(networkFail);
                    _data.writeBoolean(isForegroundApp);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusUidNwChange
            public void appFreezeDataNotify(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusUidNwChange.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
