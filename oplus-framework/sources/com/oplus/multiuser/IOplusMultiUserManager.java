package com.oplus.multiuser;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusMultiUserManager extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.multiuser.IOplusMultiUserManager";

    int getMultiSystemUserIdNoCheck() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusMultiUserManager {
        @Override // com.oplus.multiuser.IOplusMultiUserManager
        public int getMultiSystemUserIdNoCheck() throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMultiUserManager {
        static final int TRANSACTION_getMultiSystemUserIdNoCheck = 1;

        public Stub() {
            attachInterface(this, IOplusMultiUserManager.DESCRIPTOR);
        }

        public static IOplusMultiUserManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMultiUserManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMultiUserManager)) {
                return (IOplusMultiUserManager) iin;
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
                    return "getMultiSystemUserIdNoCheck";
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
                data.enforceInterface(IOplusMultiUserManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMultiUserManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _result = getMultiSystemUserIdNoCheck();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMultiUserManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMultiUserManager.DESCRIPTOR;
            }

            @Override // com.oplus.multiuser.IOplusMultiUserManager
            public int getMultiSystemUserIdNoCheck() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusMultiUserManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
