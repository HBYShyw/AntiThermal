package com.oplus.compactwindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCompactWindowObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.compactwindow.IOplusCompactWindowObserver";

    void onCompactWindowDied(String str) throws RemoteException;

    void onCompactWindowExit(OplusCompactWindowInfo oplusCompactWindowInfo) throws RemoteException;

    void onCompactWindowInfoChanged(OplusCompactWindowInfo oplusCompactWindowInfo) throws RemoteException;

    void onCompactWindowStart(OplusCompactWindowInfo oplusCompactWindowInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCompactWindowObserver {
        @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
        public void onCompactWindowStart(OplusCompactWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
        public void onCompactWindowExit(OplusCompactWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
        public void onCompactWindowDied(String appName) throws RemoteException {
        }

        @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
        public void onCompactWindowInfoChanged(OplusCompactWindowInfo info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCompactWindowObserver {
        static final int TRANSACTION_onCompactWindowDied = 3;
        static final int TRANSACTION_onCompactWindowExit = 2;
        static final int TRANSACTION_onCompactWindowInfoChanged = 4;
        static final int TRANSACTION_onCompactWindowStart = 1;

        public Stub() {
            attachInterface(this, IOplusCompactWindowObserver.DESCRIPTOR);
        }

        public static IOplusCompactWindowObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCompactWindowObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCompactWindowObserver)) {
                return (IOplusCompactWindowObserver) iin;
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
                    return "onCompactWindowStart";
                case 2:
                    return "onCompactWindowExit";
                case 3:
                    return "onCompactWindowDied";
                case 4:
                    return "onCompactWindowInfoChanged";
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
                data.enforceInterface(IOplusCompactWindowObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCompactWindowObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusCompactWindowInfo _arg0 = (OplusCompactWindowInfo) data.readTypedObject(OplusCompactWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onCompactWindowStart(_arg0);
                            return true;
                        case 2:
                            OplusCompactWindowInfo _arg02 = (OplusCompactWindowInfo) data.readTypedObject(OplusCompactWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onCompactWindowExit(_arg02);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            onCompactWindowDied(_arg03);
                            return true;
                        case 4:
                            OplusCompactWindowInfo _arg04 = (OplusCompactWindowInfo) data.readTypedObject(OplusCompactWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onCompactWindowInfoChanged(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCompactWindowObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCompactWindowObserver.DESCRIPTOR;
            }

            @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
            public void onCompactWindowStart(OplusCompactWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCompactWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
            public void onCompactWindowExit(OplusCompactWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCompactWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
            public void onCompactWindowDied(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCompactWindowObserver.DESCRIPTOR);
                    _data.writeString(appName);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.compactwindow.IOplusCompactWindowObserver
            public void onCompactWindowInfoChanged(OplusCompactWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusCompactWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
