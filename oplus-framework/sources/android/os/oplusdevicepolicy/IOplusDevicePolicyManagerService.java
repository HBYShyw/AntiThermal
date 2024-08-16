package android.os.oplusdevicepolicy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.oplusdevicepolicy.IOplusDevicePolicyObserver;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusDevicePolicyManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService";

    boolean addList(String str, List list, int i) throws RemoteException;

    boolean clearData(int i) throws RemoteException;

    boolean clearList(int i) throws RemoteException;

    String getData(String str, int i) throws RemoteException;

    List<String> getList(String str, int i) throws RemoteException;

    boolean registerOplusDevicePolicyObserver(String str, IOplusDevicePolicyObserver iOplusDevicePolicyObserver) throws RemoteException;

    boolean removeData(String str, int i) throws RemoteException;

    boolean removeList(String str, int i) throws RemoteException;

    boolean removePartListData(String str, List list, int i) throws RemoteException;

    boolean setData(String str, String str2, int i) throws RemoteException;

    boolean setList(String str, List list, int i) throws RemoteException;

    boolean unregisterOplusDevicePolicyObserver(IOplusDevicePolicyObserver iOplusDevicePolicyObserver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusDevicePolicyManagerService {
        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean setData(String name, String value, int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean setList(String name, List list, int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean addList(String name, List list, int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public String getData(String name, int data_type) throws RemoteException {
            return null;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public List<String> getList(String name, int data_type) throws RemoteException {
            return null;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean removeData(String name, int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean removeList(String name, int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean removePartListData(String name, List list, int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean clearData(int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean clearList(int data_type) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean registerOplusDevicePolicyObserver(String name, IOplusDevicePolicyObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
        public boolean unregisterOplusDevicePolicyObserver(IOplusDevicePolicyObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusDevicePolicyManagerService {
        static final int TRANSACTION_addList = 3;
        static final int TRANSACTION_clearData = 9;
        static final int TRANSACTION_clearList = 10;
        static final int TRANSACTION_getData = 4;
        static final int TRANSACTION_getList = 5;
        static final int TRANSACTION_registerOplusDevicePolicyObserver = 11;
        static final int TRANSACTION_removeData = 6;
        static final int TRANSACTION_removeList = 7;
        static final int TRANSACTION_removePartListData = 8;
        static final int TRANSACTION_setData = 1;
        static final int TRANSACTION_setList = 2;
        static final int TRANSACTION_unregisterOplusDevicePolicyObserver = 12;

        public Stub() {
            attachInterface(this, IOplusDevicePolicyManagerService.DESCRIPTOR);
        }

        public static IOplusDevicePolicyManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusDevicePolicyManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusDevicePolicyManagerService)) {
                return (IOplusDevicePolicyManagerService) iin;
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
                    return "setData";
                case 2:
                    return "setList";
                case 3:
                    return "addList";
                case 4:
                    return "getData";
                case 5:
                    return "getList";
                case 6:
                    return "removeData";
                case 7:
                    return "removeList";
                case 8:
                    return "removePartListData";
                case 9:
                    return "clearData";
                case 10:
                    return "clearList";
                case 11:
                    return "registerOplusDevicePolicyObserver";
                case 12:
                    return "unregisterOplusDevicePolicyObserver";
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
                data.enforceInterface(IOplusDevicePolicyManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            String _arg1 = data.readString();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = setData(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            ClassLoader cl = getClass().getClassLoader();
                            List _arg12 = data.readArrayList(cl);
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result2 = setList(_arg02, _arg12, _arg22);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            ClassLoader cl2 = getClass().getClassLoader();
                            List _arg13 = data.readArrayList(cl2);
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = addList(_arg03, _arg13, _arg23);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result4 = getData(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeString(_result4);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result5 = getList(_arg05, _arg15);
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result6 = removeData(_arg06, _arg16);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = removeList(_arg07, _arg17);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            ClassLoader cl3 = getClass().getClassLoader();
                            List _arg18 = data.readArrayList(cl3);
                            int _arg24 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result8 = removePartListData(_arg08, _arg18, _arg24);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = clearData(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result10 = clearList(_arg010);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 11:
                            String _arg011 = data.readString();
                            IOplusDevicePolicyObserver _arg19 = IOplusDevicePolicyObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result11 = registerOplusDevicePolicyObserver(_arg011, _arg19);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 12:
                            IOplusDevicePolicyObserver _arg012 = IOplusDevicePolicyObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result12 = unregisterOplusDevicePolicyObserver(_arg012);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusDevicePolicyManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusDevicePolicyManagerService.DESCRIPTOR;
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean setData(String name, String value, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(value);
                    _data.writeInt(data_type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean setList(String name, List list, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeList(list);
                    _data.writeInt(data_type);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean addList(String name, List list, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeList(list);
                    _data.writeInt(data_type);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public String getData(String name, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(data_type);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public List<String> getList(String name, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(data_type);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean removeData(String name, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(data_type);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean removeList(String name, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(data_type);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean removePartListData(String name, List list, int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeList(list);
                    _data.writeInt(data_type);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean clearData(int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeInt(data_type);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean clearList(int data_type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeInt(data_type);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean registerOplusDevicePolicyObserver(String name, IOplusDevicePolicyObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyManagerService
            public boolean unregisterOplusDevicePolicyObserver(IOplusDevicePolicyObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyManagerService.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
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
