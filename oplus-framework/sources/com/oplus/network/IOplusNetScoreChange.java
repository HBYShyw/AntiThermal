package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusNetScoreChange extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusNetScoreChange";

    void networkKPIChange(OplusNetworkKPI oplusNetworkKPI) throws RemoteException;

    void networkScoreChange(int i, int i2, int i3, boolean z, int i4, int i5) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNetScoreChange {
        @Override // com.oplus.network.IOplusNetScoreChange
        public void networkScoreChange(int networkId, int oldScore, int newScore, boolean better, int dnsScore, int tcpScore) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusNetScoreChange
        public void networkKPIChange(OplusNetworkKPI networkKPI) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNetScoreChange {
        static final int TRANSACTION_networkKPIChange = 2;
        static final int TRANSACTION_networkScoreChange = 1;

        public Stub() {
            attachInterface(this, IOplusNetScoreChange.DESCRIPTOR);
        }

        public static IOplusNetScoreChange asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNetScoreChange.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNetScoreChange)) {
                return (IOplusNetScoreChange) iin;
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
                    return "networkScoreChange";
                case 2:
                    return "networkKPIChange";
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
                data.enforceInterface(IOplusNetScoreChange.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNetScoreChange.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            boolean _arg3 = data.readBoolean();
                            int _arg4 = data.readInt();
                            int _arg5 = data.readInt();
                            data.enforceNoDataAvail();
                            networkScoreChange(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                            return true;
                        case 2:
                            OplusNetworkKPI _arg02 = (OplusNetworkKPI) data.readTypedObject(OplusNetworkKPI.CREATOR);
                            data.enforceNoDataAvail();
                            networkKPIChange(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNetScoreChange {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNetScoreChange.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusNetScoreChange
            public void networkScoreChange(int networkId, int oldScore, int newScore, boolean better, int dnsScore, int tcpScore) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNetScoreChange.DESCRIPTOR);
                    _data.writeInt(networkId);
                    _data.writeInt(oldScore);
                    _data.writeInt(newScore);
                    _data.writeBoolean(better);
                    _data.writeInt(dnsScore);
                    _data.writeInt(tcpScore);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusNetScoreChange
            public void networkKPIChange(OplusNetworkKPI networkKPI) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusNetScoreChange.DESCRIPTOR);
                    _data.writeTypedObject(networkKPI, 0);
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
