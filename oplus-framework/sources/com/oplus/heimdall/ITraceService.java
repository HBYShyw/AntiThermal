package com.oplus.heimdall;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.heimdall.ITraceListener;

/* loaded from: classes.dex */
public interface ITraceService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.heimdall.ITraceService";

    boolean addListener(String str, ITraceListener iTraceListener) throws RemoteException;

    boolean isTraceOn() throws RemoteException;

    boolean removeListener(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITraceService {
        @Override // com.oplus.heimdall.ITraceService
        public boolean addListener(String callerPackageName, ITraceListener listener) throws RemoteException {
            return false;
        }

        @Override // com.oplus.heimdall.ITraceService
        public boolean removeListener(String callerPackageName) throws RemoteException {
            return false;
        }

        @Override // com.oplus.heimdall.ITraceService
        public boolean isTraceOn() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITraceService {
        static final int TRANSACTION_addListener = 1;
        static final int TRANSACTION_isTraceOn = 3;
        static final int TRANSACTION_removeListener = 2;

        public Stub() {
            attachInterface(this, ITraceService.DESCRIPTOR);
        }

        public static ITraceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ITraceService.DESCRIPTOR);
            if (iin != null && (iin instanceof ITraceService)) {
                return (ITraceService) iin;
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
                    return "addListener";
                case 2:
                    return "removeListener";
                case 3:
                    return "isTraceOn";
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
                data.enforceInterface(ITraceService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ITraceService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            ITraceListener _arg1 = ITraceListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = addListener(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = removeListener(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            boolean _result3 = isTraceOn();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ITraceService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ITraceService.DESCRIPTOR;
            }

            @Override // com.oplus.heimdall.ITraceService
            public boolean addListener(String callerPackageName, ITraceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITraceService.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.heimdall.ITraceService
            public boolean removeListener(String callerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITraceService.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.heimdall.ITraceService
            public boolean isTraceOn() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ITraceService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
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
            return 2;
        }
    }
}
