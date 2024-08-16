package com.oplus.putt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPuttObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.putt.IPuttObserver";

    void onPuttEnter(OplusPuttEnterInfo oplusPuttEnterInfo) throws RemoteException;

    void onPuttExit(OplusPuttExitInfo oplusPuttExitInfo) throws RemoteException;

    void onPuttReplace(OplusPuttEnterInfo oplusPuttEnterInfo, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPuttObserver {
        @Override // com.oplus.putt.IPuttObserver
        public void onPuttEnter(OplusPuttEnterInfo info) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttObserver
        public void onPuttExit(OplusPuttExitInfo info) throws RemoteException {
        }

        @Override // com.oplus.putt.IPuttObserver
        public void onPuttReplace(OplusPuttEnterInfo info, int outTaskId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPuttObserver {
        static final int TRANSACTION_onPuttEnter = 1;
        static final int TRANSACTION_onPuttExit = 2;
        static final int TRANSACTION_onPuttReplace = 3;

        public Stub() {
            attachInterface(this, IPuttObserver.DESCRIPTOR);
        }

        public static IPuttObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPuttObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IPuttObserver)) {
                return (IPuttObserver) iin;
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
                    return "onPuttEnter";
                case 2:
                    return "onPuttExit";
                case 3:
                    return "onPuttReplace";
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
                data.enforceInterface(IPuttObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPuttObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusPuttEnterInfo _arg0 = (OplusPuttEnterInfo) data.readTypedObject(OplusPuttEnterInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onPuttEnter(_arg0);
                            return true;
                        case 2:
                            OplusPuttExitInfo _arg02 = (OplusPuttExitInfo) data.readTypedObject(OplusPuttExitInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onPuttExit(_arg02);
                            return true;
                        case 3:
                            OplusPuttEnterInfo _arg03 = (OplusPuttEnterInfo) data.readTypedObject(OplusPuttEnterInfo.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onPuttReplace(_arg03, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPuttObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPuttObserver.DESCRIPTOR;
            }

            @Override // com.oplus.putt.IPuttObserver
            public void onPuttEnter(OplusPuttEnterInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttObserver
            public void onPuttExit(OplusPuttExitInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttObserver
            public void onPuttReplace(OplusPuttEnterInfo info, int outTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(outTaskId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
