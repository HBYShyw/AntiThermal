package com.oplus.miragewindow;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusMirageDisplayObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.miragewindow.IOplusMirageDisplayObserver";

    void onMirageDisplayCastFailed(int i) throws RemoteException;

    void onMirageDisplayCastSuccess(OplusMirageDisplayCastInfo oplusMirageDisplayCastInfo, int i) throws RemoteException;

    void onMirageDisplayConfigChanged(OplusMirageDisplayCastInfo oplusMirageDisplayCastInfo, int i) throws RemoteException;

    void onMirageDisplayExit(int i) throws RemoteException;

    void onMirageDisplayToastEvent(int i, int i2, Bundle bundle) throws RemoteException;

    void onMirageDisplayTopActivityUidChanged(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusMirageDisplayObserver {
        @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
        public void onMirageDisplayCastSuccess(OplusMirageDisplayCastInfo info, int sessionId) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
        public void onMirageDisplayCastFailed(int sessionId) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
        public void onMirageDisplayExit(int sessionId) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
        public void onMirageDisplayConfigChanged(OplusMirageDisplayCastInfo info, int sessionId) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
        public void onMirageDisplayTopActivityUidChanged(int uid, int sessionId) throws RemoteException {
        }

        @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
        public void onMirageDisplayToastEvent(int eventId, int displayId, Bundle extention) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMirageDisplayObserver {
        static final int TRANSACTION_onMirageDisplayCastFailed = 2;
        static final int TRANSACTION_onMirageDisplayCastSuccess = 1;
        static final int TRANSACTION_onMirageDisplayConfigChanged = 4;
        static final int TRANSACTION_onMirageDisplayExit = 3;
        static final int TRANSACTION_onMirageDisplayToastEvent = 6;
        static final int TRANSACTION_onMirageDisplayTopActivityUidChanged = 5;

        public Stub() {
            attachInterface(this, IOplusMirageDisplayObserver.DESCRIPTOR);
        }

        public static IOplusMirageDisplayObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMirageDisplayObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMirageDisplayObserver)) {
                return (IOplusMirageDisplayObserver) iin;
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
                    return "onMirageDisplayCastSuccess";
                case 2:
                    return "onMirageDisplayCastFailed";
                case 3:
                    return "onMirageDisplayExit";
                case 4:
                    return "onMirageDisplayConfigChanged";
                case 5:
                    return "onMirageDisplayTopActivityUidChanged";
                case 6:
                    return "onMirageDisplayToastEvent";
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
                data.enforceInterface(IOplusMirageDisplayObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMirageDisplayObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusMirageDisplayCastInfo _arg0 = (OplusMirageDisplayCastInfo) data.readTypedObject(OplusMirageDisplayCastInfo.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onMirageDisplayCastSuccess(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            onMirageDisplayCastFailed(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            onMirageDisplayExit(_arg03);
                            return true;
                        case 4:
                            OplusMirageDisplayCastInfo _arg04 = (OplusMirageDisplayCastInfo) data.readTypedObject(OplusMirageDisplayCastInfo.CREATOR);
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            onMirageDisplayConfigChanged(_arg04, _arg12);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            onMirageDisplayTopActivityUidChanged(_arg05, _arg13);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg14 = data.readInt();
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onMirageDisplayToastEvent(_arg06, _arg14, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMirageDisplayObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMirageDisplayObserver.DESCRIPTOR;
            }

            @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
            public void onMirageDisplayCastSuccess(OplusMirageDisplayCastInfo info, int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageDisplayObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
            public void onMirageDisplayCastFailed(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageDisplayObserver.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
            public void onMirageDisplayExit(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageDisplayObserver.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
            public void onMirageDisplayConfigChanged(OplusMirageDisplayCastInfo info, int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageDisplayObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
            public void onMirageDisplayTopActivityUidChanged(int uid, int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageDisplayObserver.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.miragewindow.IOplusMirageDisplayObserver
            public void onMirageDisplayToastEvent(int eventId, int displayId, Bundle extention) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMirageDisplayObserver.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeInt(displayId);
                    _data.writeTypedObject(extention, 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 5;
        }
    }
}
