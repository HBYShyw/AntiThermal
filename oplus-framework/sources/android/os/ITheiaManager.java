package android.os;

/* loaded from: classes.dex */
public interface ITheiaManager extends IInterface {
    public static final String DESCRIPTOR = "android.os.ITheiaManager";

    void dumpAnrTrace(int i) throws RemoteException;

    void dumpMeminfo(int i) throws RemoteException;

    void dumpSf(int i) throws RemoteException;

    void dumpSystrace() throws RemoteException;

    void sendEvent(int i) throws RemoteException;

    void theiaRecoveryKillApp(int i) throws RemoteException;

    void theiaRecoveryRestartApp(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITheiaManager {
        @Override // android.os.ITheiaManager
        public void sendEvent(int dummy) throws RemoteException {
        }

        @Override // android.os.ITheiaManager
        public void dumpAnrTrace(int pid) throws RemoteException {
        }

        @Override // android.os.ITheiaManager
        public void dumpSf(int fd) throws RemoteException {
        }

        @Override // android.os.ITheiaManager
        public void dumpMeminfo(int fd) throws RemoteException {
        }

        @Override // android.os.ITheiaManager
        public void theiaRecoveryKillApp(int pid) throws RemoteException {
        }

        @Override // android.os.ITheiaManager
        public void theiaRecoveryRestartApp(int pid) throws RemoteException {
        }

        @Override // android.os.ITheiaManager
        public void dumpSystrace() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITheiaManager {
        static final int TRANSACTION_dumpAnrTrace = 2;
        static final int TRANSACTION_dumpMeminfo = 4;
        static final int TRANSACTION_dumpSf = 3;
        static final int TRANSACTION_dumpSystrace = 7;
        static final int TRANSACTION_sendEvent = 1;
        static final int TRANSACTION_theiaRecoveryKillApp = 5;
        static final int TRANSACTION_theiaRecoveryRestartApp = 6;

        public Stub() {
            attachInterface(this, ITheiaManager.DESCRIPTOR);
        }

        public static ITheiaManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ITheiaManager.DESCRIPTOR);
            if (iin != null && (iin instanceof ITheiaManager)) {
                return (ITheiaManager) iin;
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
                    return "sendEvent";
                case 2:
                    return "dumpAnrTrace";
                case 3:
                    return "dumpSf";
                case 4:
                    return "dumpMeminfo";
                case 5:
                    return "theiaRecoveryKillApp";
                case 6:
                    return "theiaRecoveryRestartApp";
                case 7:
                    return "dumpSystrace";
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
                data.enforceInterface(ITheiaManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ITheiaManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            sendEvent(_arg0);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            dumpAnrTrace(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            dumpSf(_arg03);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            dumpMeminfo(_arg04);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            theiaRecoveryKillApp(_arg05);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            theiaRecoveryRestartApp(_arg06);
                            return true;
                        case 7:
                            dumpSystrace();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ITheiaManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ITheiaManager.DESCRIPTOR;
            }

            @Override // android.os.ITheiaManager
            public void sendEvent(int dummy) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    _data.writeInt(dummy);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.ITheiaManager
            public void dumpAnrTrace(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.ITheiaManager
            public void dumpSf(int fd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    _data.writeInt(fd);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.ITheiaManager
            public void dumpMeminfo(int fd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    _data.writeInt(fd);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.ITheiaManager
            public void theiaRecoveryKillApp(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.ITheiaManager
            public void theiaRecoveryRestartApp(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.ITheiaManager
            public void dumpSystrace() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITheiaManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 6;
        }
    }
}
