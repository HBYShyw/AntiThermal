package android.payjoy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPayJoyAccessService extends IInterface {
    public static final String DESCRIPTOR = "android.payjoy.IPayJoyAccessService";

    void dumpPayJoyDataBlock() throws RemoteException;

    void enableDebuggingFeature() throws RemoteException;

    void erasePayJoyDataBlock() throws RemoteException;

    String[] getAppOpPermissionPackages(String str) throws RemoteException;

    int getPayJoyControlState() throws RemoteException;

    int getPayJoyDataLocation() throws RemoteException;

    int getPayJoyDataVersion() throws RemoteException;

    String getPersistedImei(int i) throws RemoteException;

    String getVersion() throws RemoteException;

    boolean setPayJoyAppAsDeviceOwner(String str, String str2, String str3) throws RemoteException;

    int setPayJoyControlState(int i) throws RemoteException;

    void switchPayjoyLockStatus(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPayJoyAccessService {
        @Override // android.payjoy.IPayJoyAccessService
        public String getVersion() throws RemoteException {
            return null;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public int getPayJoyControlState() throws RemoteException {
            return 0;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public int setPayJoyControlState(int state) throws RemoteException {
            return 0;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public String getPersistedImei(int slot) throws RemoteException {
            return null;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public boolean setPayJoyAppAsDeviceOwner(String packageName, String className, String ownerName) throws RemoteException {
            return false;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
            return null;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public void enableDebuggingFeature() throws RemoteException {
        }

        @Override // android.payjoy.IPayJoyAccessService
        public void dumpPayJoyDataBlock() throws RemoteException {
        }

        @Override // android.payjoy.IPayJoyAccessService
        public void erasePayJoyDataBlock() throws RemoteException {
        }

        @Override // android.payjoy.IPayJoyAccessService
        public int getPayJoyDataVersion() throws RemoteException {
            return 0;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public int getPayJoyDataLocation() throws RemoteException {
            return 0;
        }

        @Override // android.payjoy.IPayJoyAccessService
        public void switchPayjoyLockStatus(int status) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPayJoyAccessService {
        static final int TRANSACTION_dumpPayJoyDataBlock = 8;
        static final int TRANSACTION_enableDebuggingFeature = 7;
        static final int TRANSACTION_erasePayJoyDataBlock = 9;
        static final int TRANSACTION_getAppOpPermissionPackages = 6;
        static final int TRANSACTION_getPayJoyControlState = 2;
        static final int TRANSACTION_getPayJoyDataLocation = 11;
        static final int TRANSACTION_getPayJoyDataVersion = 10;
        static final int TRANSACTION_getPersistedImei = 4;
        static final int TRANSACTION_getVersion = 1;
        static final int TRANSACTION_setPayJoyAppAsDeviceOwner = 5;
        static final int TRANSACTION_setPayJoyControlState = 3;
        static final int TRANSACTION_switchPayjoyLockStatus = 12;

        public Stub() {
            attachInterface(this, IPayJoyAccessService.DESCRIPTOR);
        }

        public static IPayJoyAccessService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPayJoyAccessService.DESCRIPTOR);
            if (iin != null && (iin instanceof IPayJoyAccessService)) {
                return (IPayJoyAccessService) iin;
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
                    return "getVersion";
                case 2:
                    return "getPayJoyControlState";
                case 3:
                    return "setPayJoyControlState";
                case 4:
                    return "getPersistedImei";
                case 5:
                    return "setPayJoyAppAsDeviceOwner";
                case 6:
                    return "getAppOpPermissionPackages";
                case 7:
                    return "enableDebuggingFeature";
                case 8:
                    return "dumpPayJoyDataBlock";
                case 9:
                    return "erasePayJoyDataBlock";
                case 10:
                    return "getPayJoyDataVersion";
                case 11:
                    return "getPayJoyDataLocation";
                case 12:
                    return "switchPayjoyLockStatus";
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
                data.enforceInterface(IPayJoyAccessService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPayJoyAccessService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _result = getVersion();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 2:
                            int _result2 = getPayJoyControlState();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result3 = setPayJoyControlState(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result4 = getPersistedImei(_arg02);
                            reply.writeNoException();
                            reply.writeString(_result4);
                            return true;
                        case 5:
                            String _arg03 = data.readString();
                            String _arg1 = data.readString();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result5 = setPayJoyAppAsDeviceOwner(_arg03, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            String[] _result6 = getAppOpPermissionPackages(_arg04);
                            reply.writeNoException();
                            reply.writeStringArray(_result6);
                            return true;
                        case 7:
                            enableDebuggingFeature();
                            reply.writeNoException();
                            return true;
                        case 8:
                            dumpPayJoyDataBlock();
                            reply.writeNoException();
                            return true;
                        case 9:
                            erasePayJoyDataBlock();
                            reply.writeNoException();
                            return true;
                        case 10:
                            int _result7 = getPayJoyDataVersion();
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 11:
                            int _result8 = getPayJoyDataLocation();
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 12:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            switchPayjoyLockStatus(_arg05);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPayJoyAccessService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPayJoyAccessService.DESCRIPTOR;
            }

            @Override // android.payjoy.IPayJoyAccessService
            public String getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public int getPayJoyControlState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public int setPayJoyControlState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public String getPersistedImei(int slot) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    _data.writeInt(slot);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public boolean setPayJoyAppAsDeviceOwner(String packageName, String className, String ownerName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(className);
                    _data.writeString(ownerName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    _data.writeString(permissionName);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public void enableDebuggingFeature() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public void dumpPayJoyDataBlock() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public void erasePayJoyDataBlock() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public int getPayJoyDataVersion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public int getPayJoyDataLocation() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.payjoy.IPayJoyAccessService
            public void switchPayjoyLockStatus(int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPayJoyAccessService.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 11;
        }
    }
}
