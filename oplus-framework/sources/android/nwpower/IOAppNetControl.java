package android.nwpower;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOAppNetControl extends IInterface {
    public static final String DESCRIPTOR = "android.nwpower.IOAppNetControl";

    void destroySocket(int i) throws RemoteException;

    void destroySocketForProc(int i, int i2) throws RemoteException;

    void legacyDestroySocket() throws RemoteException;

    int networkDisableWhiteList(List<String> list, int i) throws RemoteException;

    void notifyUnFreezeForProc(int i, int i2) throws RemoteException;

    long[] requestAppFireWallHistoryStamp(int i) throws RemoteException;

    void setFirewall(int i, boolean z) throws RemoteException;

    void setFirewallWithArgs(int i, boolean z, int i2, int i3) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOAppNetControl {
        @Override // android.nwpower.IOAppNetControl
        public void setFirewall(int uid, boolean allow) throws RemoteException {
        }

        @Override // android.nwpower.IOAppNetControl
        public void setFirewallWithArgs(int uid, boolean allow, int netrestore, int scenes) throws RemoteException {
        }

        @Override // android.nwpower.IOAppNetControl
        public void destroySocket(int uid) throws RemoteException {
        }

        @Override // android.nwpower.IOAppNetControl
        public void legacyDestroySocket() throws RemoteException {
        }

        @Override // android.nwpower.IOAppNetControl
        public long[] requestAppFireWallHistoryStamp(int uid) throws RemoteException {
            return null;
        }

        @Override // android.nwpower.IOAppNetControl
        public int networkDisableWhiteList(List<String> appConfigUids, int enable) throws RemoteException {
            return 0;
        }

        @Override // android.nwpower.IOAppNetControl
        public void destroySocketForProc(int uid, int pid) throws RemoteException {
        }

        @Override // android.nwpower.IOAppNetControl
        public void notifyUnFreezeForProc(int uid, int pid) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOAppNetControl {
        static final int TRANSACTION_destroySocket = 3;
        static final int TRANSACTION_destroySocketForProc = 7;
        static final int TRANSACTION_legacyDestroySocket = 4;
        static final int TRANSACTION_networkDisableWhiteList = 6;
        static final int TRANSACTION_notifyUnFreezeForProc = 8;
        static final int TRANSACTION_requestAppFireWallHistoryStamp = 5;
        static final int TRANSACTION_setFirewall = 1;
        static final int TRANSACTION_setFirewallWithArgs = 2;

        public Stub() {
            attachInterface(this, IOAppNetControl.DESCRIPTOR);
        }

        public static IOAppNetControl asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOAppNetControl.DESCRIPTOR);
            if (iin != null && (iin instanceof IOAppNetControl)) {
                return (IOAppNetControl) iin;
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
                    return "setFirewall";
                case 2:
                    return "setFirewallWithArgs";
                case 3:
                    return "destroySocket";
                case 4:
                    return "legacyDestroySocket";
                case 5:
                    return "requestAppFireWallHistoryStamp";
                case 6:
                    return "networkDisableWhiteList";
                case 7:
                    return "destroySocketForProc";
                case 8:
                    return "notifyUnFreezeForProc";
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
                data.enforceInterface(IOAppNetControl.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOAppNetControl.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setFirewall(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            setFirewallWithArgs(_arg02, _arg12, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            destroySocket(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            legacyDestroySocket();
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            long[] _result = requestAppFireWallHistoryStamp(_arg04);
                            reply.writeNoException();
                            reply.writeLongArray(_result);
                            return true;
                        case 6:
                            List<String> _arg05 = data.createStringArrayList();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result2 = networkDisableWhiteList(_arg05, _arg13);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 7:
                            int _arg06 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            destroySocketForProc(_arg06, _arg14);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg07 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyUnFreezeForProc(_arg07, _arg15);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOAppNetControl {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOAppNetControl.DESCRIPTOR;
            }

            @Override // android.nwpower.IOAppNetControl
            public void setFirewall(int uid, boolean allow) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeBoolean(allow);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public void setFirewallWithArgs(int uid, boolean allow, int netrestore, int scenes) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeBoolean(allow);
                    _data.writeInt(netrestore);
                    _data.writeInt(scenes);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public void destroySocket(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public void legacyDestroySocket() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public long[] requestAppFireWallHistoryStamp(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public int networkDisableWhiteList(List<String> appConfigUids, int enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeStringList(appConfigUids);
                    _data.writeInt(enable);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public void destroySocketForProc(int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nwpower.IOAppNetControl
            public void notifyUnFreezeForProc(int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOAppNetControl.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 7;
        }
    }
}
