package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IDataLimitEventCb extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IDataLimitEventCb";

    void dataLimitEvent(int i, int i2) throws RemoteException;

    void dataLimitStateChange(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IDataLimitEventCb {
        @Override // com.oplus.network.IDataLimitEventCb
        public void dataLimitEvent(int netId, int event) throws RemoteException {
        }

        @Override // com.oplus.network.IDataLimitEventCb
        public void dataLimitStateChange(int netId, int state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDataLimitEventCb {
        static final int TRANSACTION_dataLimitEvent = 1;
        static final int TRANSACTION_dataLimitStateChange = 2;

        public Stub() {
            attachInterface(this, IDataLimitEventCb.DESCRIPTOR);
        }

        public static IDataLimitEventCb asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IDataLimitEventCb.DESCRIPTOR);
            if (iin != null && (iin instanceof IDataLimitEventCb)) {
                return (IDataLimitEventCb) iin;
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
                    return "dataLimitEvent";
                case 2:
                    return "dataLimitStateChange";
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
                data.enforceInterface(IDataLimitEventCb.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IDataLimitEventCb.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            dataLimitEvent(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            dataLimitStateChange(_arg02, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IDataLimitEventCb {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IDataLimitEventCb.DESCRIPTOR;
            }

            @Override // com.oplus.network.IDataLimitEventCb
            public void dataLimitEvent(int netId, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDataLimitEventCb.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(event);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IDataLimitEventCb
            public void dataLimitStateChange(int netId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IDataLimitEventCb.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(state);
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
