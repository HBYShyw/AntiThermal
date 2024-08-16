package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusNetworkingControlManager extends IInterface {
    public static final String DESCRIPTOR = "android.net.IOplusNetworkingControlManager";

    void factoryReset() throws RemoteException;

    Map getPolicyList() throws RemoteException;

    int getUidPolicy(int i) throws RemoteException;

    int[] getUidsWithPolicy(int i) throws RemoteException;

    void setUidPolicy(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNetworkingControlManager {
        @Override // android.net.IOplusNetworkingControlManager
        public void setUidPolicy(int uid, int policy) throws RemoteException {
        }

        @Override // android.net.IOplusNetworkingControlManager
        public int getUidPolicy(int uid) throws RemoteException {
            return 0;
        }

        @Override // android.net.IOplusNetworkingControlManager
        public int[] getUidsWithPolicy(int policy) throws RemoteException {
            return null;
        }

        @Override // android.net.IOplusNetworkingControlManager
        public void factoryReset() throws RemoteException {
        }

        @Override // android.net.IOplusNetworkingControlManager
        public Map getPolicyList() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNetworkingControlManager {
        static final int TRANSACTION_factoryReset = 4;
        static final int TRANSACTION_getPolicyList = 5;
        static final int TRANSACTION_getUidPolicy = 2;
        static final int TRANSACTION_getUidsWithPolicy = 3;
        static final int TRANSACTION_setUidPolicy = 1;

        public Stub() {
            attachInterface(this, IOplusNetworkingControlManager.DESCRIPTOR);
        }

        public static IOplusNetworkingControlManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNetworkingControlManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNetworkingControlManager)) {
                return (IOplusNetworkingControlManager) iin;
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
                    return "setUidPolicy";
                case 2:
                    return "getUidPolicy";
                case 3:
                    return "getUidsWithPolicy";
                case 4:
                    return "factoryReset";
                case 5:
                    return "getPolicyList";
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
                data.enforceInterface(IOplusNetworkingControlManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNetworkingControlManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            setUidPolicy(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = getUidPolicy(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            int[] _result2 = getUidsWithPolicy(_arg03);
                            reply.writeNoException();
                            reply.writeIntArray(_result2);
                            return true;
                        case 4:
                            factoryReset();
                            reply.writeNoException();
                            return true;
                        case 5:
                            Map _result3 = getPolicyList();
                            reply.writeNoException();
                            reply.writeMap(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNetworkingControlManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNetworkingControlManager.DESCRIPTOR;
            }

            @Override // android.net.IOplusNetworkingControlManager
            public void setUidPolicy(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkingControlManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IOplusNetworkingControlManager
            public int getUidPolicy(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkingControlManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IOplusNetworkingControlManager
            public int[] getUidsWithPolicy(int policy) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkingControlManager.DESCRIPTOR);
                    _data.writeInt(policy);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IOplusNetworkingControlManager
            public void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkingControlManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IOplusNetworkingControlManager
            public Map getPolicyList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNetworkingControlManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 4;
        }
    }
}
