package com.oplus.heimdall;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.heimdall.IRootCallback;

/* loaded from: classes.dex */
public interface IRootService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.heimdall.IRootService";

    boolean isRoot(String str) throws RemoteException;

    void isRootForceUpdate(String str, IRootCallback iRootCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IRootService {
        @Override // com.oplus.heimdall.IRootService
        public boolean isRoot(String callerPackageName) throws RemoteException {
            return false;
        }

        @Override // com.oplus.heimdall.IRootService
        public void isRootForceUpdate(String callerPackageName, IRootCallback callBack) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IRootService {
        static final int TRANSACTION_isRoot = 1;
        static final int TRANSACTION_isRootForceUpdate = 2;

        public Stub() {
            attachInterface(this, IRootService.DESCRIPTOR);
        }

        public static IRootService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IRootService.DESCRIPTOR);
            if (iin != null && (iin instanceof IRootService)) {
                return (IRootService) iin;
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
                    return "isRoot";
                case 2:
                    return "isRootForceUpdate";
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
                data.enforceInterface(IRootService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IRootService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result = isRoot(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            IRootCallback _arg1 = IRootCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            isRootForceUpdate(_arg02, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRootService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IRootService.DESCRIPTOR;
            }

            @Override // com.oplus.heimdall.IRootService
            public boolean isRoot(String callerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRootService.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.heimdall.IRootService
            public void isRootForceUpdate(String callerPackageName, IRootCallback callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRootService.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
