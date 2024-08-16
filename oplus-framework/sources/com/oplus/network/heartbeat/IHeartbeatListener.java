package com.oplus.network.heartbeat;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IHeartbeatListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.heartbeat.IHeartbeatListener";

    void onHeartbeatStateUpdate(int i, int i2, int i3, int[] iArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IHeartbeatListener {
        @Override // com.oplus.network.heartbeat.IHeartbeatListener
        public void onHeartbeatStateUpdate(int event, int err, int destroy, int[] args) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IHeartbeatListener {
        static final int TRANSACTION_onHeartbeatStateUpdate = 1;

        public Stub() {
            attachInterface(this, IHeartbeatListener.DESCRIPTOR);
        }

        public static IHeartbeatListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IHeartbeatListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IHeartbeatListener)) {
                return (IHeartbeatListener) iin;
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
                    return "onHeartbeatStateUpdate";
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
                data.enforceInterface(IHeartbeatListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IHeartbeatListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            int[] _arg3 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onHeartbeatStateUpdate(_arg0, _arg1, _arg2, _arg3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IHeartbeatListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IHeartbeatListener.DESCRIPTOR;
            }

            @Override // com.oplus.network.heartbeat.IHeartbeatListener
            public void onHeartbeatStateUpdate(int event, int err, int destroy, int[] args) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IHeartbeatListener.DESCRIPTOR);
                    _data.writeInt(event);
                    _data.writeInt(err);
                    _data.writeInt(destroy);
                    _data.writeIntArray(args);
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
