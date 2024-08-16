package com.oplus.miragewindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusMirageWindowObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.miragewindow.IOplusMirageWindowObserver";

    void onMirageDisplayTopActivityUidChanged(int i) throws RemoteException;

    void onMirageWindConfigChanged(OplusMirageWindowInfo oplusMirageWindowInfo) throws RemoteException;

    void onMirageWindowDied(String str) throws RemoteException;

    void onMirageWindowExit(OplusMirageWindowInfo oplusMirageWindowInfo) throws RemoteException;

    void onMirageWindowShow(OplusMirageWindowInfo oplusMirageWindowInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusMirageWindowObserver {
        @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
        public void onMirageWindowShow(OplusMirageWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
        public void onMirageWindowExit(OplusMirageWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
        public void onMirageWindowDied(String cpnName) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
        public void onMirageWindConfigChanged(OplusMirageWindowInfo info) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
        public void onMirageDisplayTopActivityUidChanged(int uid) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMirageWindowObserver {
        static final int TRANSACTION_onMirageDisplayTopActivityUidChanged = 5;
        static final int TRANSACTION_onMirageWindConfigChanged = 4;
        static final int TRANSACTION_onMirageWindowDied = 3;
        static final int TRANSACTION_onMirageWindowExit = 2;
        static final int TRANSACTION_onMirageWindowShow = 1;

        public Stub() {
            attachInterface(this, IOplusMirageWindowObserver.DESCRIPTOR);
        }

        public static IOplusMirageWindowObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMirageWindowObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMirageWindowObserver)) {
                return (IOplusMirageWindowObserver) iin;
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
                    return "onMirageWindowShow";
                case 2:
                    return "onMirageWindowExit";
                case 3:
                    return "onMirageWindowDied";
                case 4:
                    return "onMirageWindConfigChanged";
                case 5:
                    return "onMirageDisplayTopActivityUidChanged";
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
                data.enforceInterface(IOplusMirageWindowObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMirageWindowObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusMirageWindowInfo _arg0 = (OplusMirageWindowInfo) data.readTypedObject(OplusMirageWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onMirageWindowShow(_arg0);
                            return true;
                        case 2:
                            OplusMirageWindowInfo _arg02 = (OplusMirageWindowInfo) data.readTypedObject(OplusMirageWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onMirageWindowExit(_arg02);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            onMirageWindowDied(_arg03);
                            return true;
                        case 4:
                            OplusMirageWindowInfo _arg04 = (OplusMirageWindowInfo) data.readTypedObject(OplusMirageWindowInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onMirageWindConfigChanged(_arg04);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            onMirageDisplayTopActivityUidChanged(_arg05);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMirageWindowObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMirageWindowObserver.DESCRIPTOR;
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
            public void onMirageWindowShow(OplusMirageWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
            public void onMirageWindowExit(OplusMirageWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
            public void onMirageWindowDied(String cpnName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowObserver.DESCRIPTOR);
                    _data.writeString(cpnName);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
            public void onMirageWindConfigChanged(OplusMirageWindowInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageWindowObserver
            public void onMirageDisplayTopActivityUidChanged(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageWindowObserver.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 4;
        }
    }
}
