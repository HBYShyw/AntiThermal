package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface INetworkDiagnosisCb extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.INetworkDiagnosisCb";

    void onFinish(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements INetworkDiagnosisCb {
        @Override // com.oplus.network.INetworkDiagnosisCb
        public void onFinish(int result, String extra) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INetworkDiagnosisCb {
        static final int TRANSACTION_onFinish = 1;

        public Stub() {
            attachInterface(this, INetworkDiagnosisCb.DESCRIPTOR);
        }

        public static INetworkDiagnosisCb asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INetworkDiagnosisCb.DESCRIPTOR);
            if (iin != null && (iin instanceof INetworkDiagnosisCb)) {
                return (INetworkDiagnosisCb) iin;
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
                    return "onFinish";
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
                data.enforceInterface(INetworkDiagnosisCb.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(INetworkDiagnosisCb.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            onFinish(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INetworkDiagnosisCb {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INetworkDiagnosisCb.DESCRIPTOR;
            }

            @Override // com.oplus.network.INetworkDiagnosisCb
            public void onFinish(int result, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(INetworkDiagnosisCb.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeString(extra);
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
