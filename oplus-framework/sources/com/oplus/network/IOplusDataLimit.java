package com.oplus.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.IDataLimitEventCb;

/* loaded from: classes.dex */
public interface IOplusDataLimit extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOplusDataLimit";

    boolean clearGlobalDataLimit(int i) throws RemoteException;

    boolean clearGlobalDataLimitWithModule(int i, String str) throws RemoteException;

    boolean clearThermalDataLimit(int i) throws RemoteException;

    int getDataLimitState(int i) throws RemoteException;

    void registerDataLimitEvent(IDataLimitEventCb iDataLimitEventCb) throws RemoteException;

    boolean setGlobalDataLimit(int i, long j, long j2) throws RemoteException;

    boolean setGlobalDataLimitWithModule(int i, long j, long j2, String str) throws RemoteException;

    boolean setThermalDataLimit(int i, long j, long j2) throws RemoteException;

    void unregisterDataLimitEvent(IDataLimitEventCb iDataLimitEventCb) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusDataLimit {
        @Override // com.oplus.network.IOplusDataLimit
        public int getDataLimitState(int netId) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOplusDataLimit
        public void registerDataLimitEvent(IDataLimitEventCb cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusDataLimit
        public void unregisterDataLimitEvent(IDataLimitEventCb cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOplusDataLimit
        public boolean setThermalDataLimit(int netId, long rxSpeed, long txSpeed) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusDataLimit
        public boolean clearThermalDataLimit(int netId) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusDataLimit
        public boolean setGlobalDataLimit(int netId, long rxSpeed, long txSpeed) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusDataLimit
        public boolean clearGlobalDataLimit(int netId) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusDataLimit
        public boolean setGlobalDataLimitWithModule(int netId, long rxSpeed, long txSpeed, String moduleName) throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.IOplusDataLimit
        public boolean clearGlobalDataLimitWithModule(int netId, String moduleName) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusDataLimit {
        static final int TRANSACTION_clearGlobalDataLimit = 7;
        static final int TRANSACTION_clearGlobalDataLimitWithModule = 9;
        static final int TRANSACTION_clearThermalDataLimit = 5;
        static final int TRANSACTION_getDataLimitState = 1;
        static final int TRANSACTION_registerDataLimitEvent = 2;
        static final int TRANSACTION_setGlobalDataLimit = 6;
        static final int TRANSACTION_setGlobalDataLimitWithModule = 8;
        static final int TRANSACTION_setThermalDataLimit = 4;
        static final int TRANSACTION_unregisterDataLimitEvent = 3;

        public Stub() {
            attachInterface(this, IOplusDataLimit.DESCRIPTOR);
        }

        public static IOplusDataLimit asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusDataLimit.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusDataLimit)) {
                return (IOplusDataLimit) iin;
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
                    return "getDataLimitState";
                case 2:
                    return "registerDataLimitEvent";
                case 3:
                    return "unregisterDataLimitEvent";
                case 4:
                    return "setThermalDataLimit";
                case 5:
                    return "clearThermalDataLimit";
                case 6:
                    return "setGlobalDataLimit";
                case 7:
                    return "clearGlobalDataLimit";
                case 8:
                    return "setGlobalDataLimitWithModule";
                case 9:
                    return "clearGlobalDataLimitWithModule";
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
                data.enforceInterface(IOplusDataLimit.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusDataLimit.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = getDataLimitState(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            IDataLimitEventCb _arg02 = IDataLimitEventCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerDataLimitEvent(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            IDataLimitEventCb _arg03 = IDataLimitEventCb.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterDataLimitEvent(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            long _arg1 = data.readLong();
                            long _arg2 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result2 = setThermalDataLimit(_arg04, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = clearThermalDataLimit(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            long _arg12 = data.readLong();
                            long _arg22 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result4 = setGlobalDataLimit(_arg06, _arg12, _arg22);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = clearGlobalDataLimit(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            long _arg13 = data.readLong();
                            long _arg23 = data.readLong();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result6 = setGlobalDataLimitWithModule(_arg08, _arg13, _arg23, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result7 = clearGlobalDataLimitWithModule(_arg09, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusDataLimit {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusDataLimit.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOplusDataLimit
            public int getDataLimitState(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public void registerDataLimitEvent(IDataLimitEventCb cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public void unregisterDataLimitEvent(IDataLimitEventCb cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public boolean setThermalDataLimit(int netId, long rxSpeed, long txSpeed) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeLong(rxSpeed);
                    _data.writeLong(txSpeed);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public boolean clearThermalDataLimit(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public boolean setGlobalDataLimit(int netId, long rxSpeed, long txSpeed) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeLong(rxSpeed);
                    _data.writeLong(txSpeed);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public boolean clearGlobalDataLimit(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public boolean setGlobalDataLimitWithModule(int netId, long rxSpeed, long txSpeed, String moduleName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeLong(rxSpeed);
                    _data.writeLong(txSpeed);
                    _data.writeString(moduleName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOplusDataLimit
            public boolean clearGlobalDataLimitWithModule(int netId, String moduleName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDataLimit.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(moduleName);
                    this.mRemote.transact(9, _data, _reply, 0);
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
            return 8;
        }
    }
}
