package com.oplus.network;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.IOlkEventChange;
import com.oplus.network.IOlkInternalCallback;

/* loaded from: classes.dex */
public interface IOlk extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.IOlk";

    void addAuthResultInfo(int i, int i2, int i3, String str) throws RemoteException;

    String getVersion() throws RemoteException;

    int registerEventChange(int i, IOlkEventChange iOlkEventChange) throws RemoteException;

    void registerInternalCallback(IOlkInternalCallback iOlkInternalCallback) throws RemoteException;

    Bundle request(Bundle bundle) throws RemoteException;

    void unregisterEventChange(IOlkEventChange iOlkEventChange) throws RemoteException;

    void unregisterInternalCallback(IOlkInternalCallback iOlkInternalCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOlk {
        @Override // com.oplus.network.IOlk
        public int registerEventChange(int event, IOlkEventChange change) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.network.IOlk
        public void unregisterEventChange(IOlkEventChange change) throws RemoteException {
        }

        @Override // com.oplus.network.IOlk
        public Bundle request(Bundle bundle) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOlk
        public String getVersion() throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.IOlk
        public void registerInternalCallback(IOlkInternalCallback cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOlk
        public void unregisterInternalCallback(IOlkInternalCallback cb) throws RemoteException {
        }

        @Override // com.oplus.network.IOlk
        public void addAuthResultInfo(int uid, int pid, int permBits, String packageName) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOlk {
        static final int TRANSACTION_addAuthResultInfo = 7;
        static final int TRANSACTION_getVersion = 4;
        static final int TRANSACTION_registerEventChange = 1;
        static final int TRANSACTION_registerInternalCallback = 5;
        static final int TRANSACTION_request = 3;
        static final int TRANSACTION_unregisterEventChange = 2;
        static final int TRANSACTION_unregisterInternalCallback = 6;

        public Stub() {
            attachInterface(this, IOlk.DESCRIPTOR);
        }

        public static IOlk asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOlk.DESCRIPTOR);
            if (iin != null && (iin instanceof IOlk)) {
                return (IOlk) iin;
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
                    return "registerEventChange";
                case 2:
                    return "unregisterEventChange";
                case 3:
                    return "request";
                case 4:
                    return "getVersion";
                case 5:
                    return "registerInternalCallback";
                case 6:
                    return "unregisterInternalCallback";
                case 7:
                    return "addAuthResultInfo";
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
                data.enforceInterface(IOlk.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOlk.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            IOlkEventChange _arg1 = IOlkEventChange.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result = registerEventChange(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            IOlkEventChange _arg02 = IOlkEventChange.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterEventChange(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            Bundle _arg03 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result2 = request(_arg03);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 4:
                            String _result3 = getVersion();
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 5:
                            IOlkInternalCallback _arg04 = IOlkInternalCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerInternalCallback(_arg04);
                            reply.writeNoException();
                            return true;
                        case 6:
                            IOlkInternalCallback _arg05 = IOlkInternalCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterInternalCallback(_arg05);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg06 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            addAuthResultInfo(_arg06, _arg12, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOlk {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOlk.DESCRIPTOR;
            }

            @Override // com.oplus.network.IOlk
            public int registerEventChange(int event, IOlkEventChange change) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    _data.writeInt(event);
                    _data.writeStrongInterface(change);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlk
            public void unregisterEventChange(IOlkEventChange change) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    _data.writeStrongInterface(change);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlk
            public Bundle request(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlk
            public String getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlk
            public void registerInternalCallback(IOlkInternalCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlk
            public void unregisterInternalCallback(IOlkInternalCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.IOlk
            public void addAuthResultInfo(int uid, int pid, int permBits, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOlk.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeInt(permBits);
                    _data.writeString(packageName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 6;
        }
    }
}
