package com.oplus.oiface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOIfaceCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.oiface.IOIfaceCallback";

    void onEngineBoostINfo(String str, int i, int i2) throws RemoteException;

    String onFBNotification(int i) throws RemoteException;

    String onGPANotification(String str) throws RemoteException;

    void onGameJitter(String str, int i) throws RemoteException;

    void onGameStatusChanged(String str, String str2) throws RemoteException;

    void onHyperBoostInfo(String str, int i, int i2) throws RemoteException;

    void onNetworkChanged(String str, int i) throws RemoteException;

    void onOifaceGeneralInfo(String str, int i, int i2, int i3) throws RemoteException;

    void onSystemNotify(String str) throws RemoteException;

    void onTGPAInfo(String str, int i, int i2) throws RemoteException;

    void onThermalStatusChanged(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOIfaceCallback {
        @Override // com.oplus.oiface.IOIfaceCallback
        public String onFBNotification(int status) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public String onGPANotification(String info) throws RemoteException {
            return null;
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onTGPAInfo(String info, int uid, int pid) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onHyperBoostInfo(String info, int uid, int pid) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onEngineBoostINfo(String info, int uid, int pid) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onOifaceGeneralInfo(String info, int type, int uid, int pid) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onGameStatusChanged(String packageName, String gamestat) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onNetworkChanged(String packageName, int latency) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onSystemNotify(String result) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onThermalStatusChanged(String status) throws RemoteException {
        }

        @Override // com.oplus.oiface.IOIfaceCallback
        public void onGameJitter(String packageName, int fps) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOIfaceCallback {
        static final int TRANSACTION_onEngineBoostINfo = 5;
        static final int TRANSACTION_onFBNotification = 1;
        static final int TRANSACTION_onGPANotification = 2;
        static final int TRANSACTION_onGameJitter = 11;
        static final int TRANSACTION_onGameStatusChanged = 7;
        static final int TRANSACTION_onHyperBoostInfo = 4;
        static final int TRANSACTION_onNetworkChanged = 8;
        static final int TRANSACTION_onOifaceGeneralInfo = 6;
        static final int TRANSACTION_onSystemNotify = 9;
        static final int TRANSACTION_onTGPAInfo = 3;
        static final int TRANSACTION_onThermalStatusChanged = 10;

        public Stub() {
            attachInterface(this, IOIfaceCallback.DESCRIPTOR);
        }

        public static IOIfaceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOIfaceCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOIfaceCallback)) {
                return (IOIfaceCallback) iin;
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
                    return "onFBNotification";
                case 2:
                    return "onGPANotification";
                case 3:
                    return "onTGPAInfo";
                case 4:
                    return "onHyperBoostInfo";
                case 5:
                    return "onEngineBoostINfo";
                case 6:
                    return "onOifaceGeneralInfo";
                case 7:
                    return "onGameStatusChanged";
                case 8:
                    return "onNetworkChanged";
                case 9:
                    return "onSystemNotify";
                case 10:
                    return "onThermalStatusChanged";
                case 11:
                    return "onGameJitter";
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
                data.enforceInterface(IOIfaceCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOIfaceCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result = onFBNotification(_arg0);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            String _result2 = onGPANotification(_arg02);
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            onTGPAInfo(_arg03, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            int _arg12 = data.readInt();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            onHyperBoostInfo(_arg04, _arg12, _arg22);
                            reply.writeNoException();
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            int _arg13 = data.readInt();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            onEngineBoostINfo(_arg05, _arg13, _arg23);
                            reply.writeNoException();
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            int _arg14 = data.readInt();
                            int _arg24 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            onOifaceGeneralInfo(_arg06, _arg14, _arg24, _arg3);
                            reply.writeNoException();
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            onGameStatusChanged(_arg07, _arg15);
                            reply.writeNoException();
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            onNetworkChanged(_arg08, _arg16);
                            reply.writeNoException();
                            return true;
                        case 9:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            onSystemNotify(_arg09);
                            reply.writeNoException();
                            return true;
                        case 10:
                            String _arg010 = data.readString();
                            data.enforceNoDataAvail();
                            onThermalStatusChanged(_arg010);
                            reply.writeNoException();
                            return true;
                        case 11:
                            String _arg011 = data.readString();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            onGameJitter(_arg011, _arg17);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOIfaceCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOIfaceCallback.DESCRIPTOR;
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public String onFBNotification(int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public String onGPANotification(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(info);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onTGPAInfo(String info, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(info);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onHyperBoostInfo(String info, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(info);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onEngineBoostINfo(String info, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(info);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onOifaceGeneralInfo(String info, int type, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(info);
                    _data.writeInt(type);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onGameStatusChanged(String packageName, String gamestat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(gamestat);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onNetworkChanged(String packageName, int latency) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(latency);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onSystemNotify(String result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(result);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onThermalStatusChanged(String status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(status);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oiface.IOIfaceCallback
            public void onGameJitter(String packageName, int fps) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOIfaceCallback.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(fps);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 10;
        }
    }
}
