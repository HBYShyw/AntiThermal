package com.oplus.performance;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusPerfService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.performance.IOplusPerfService";

    boolean isScrollOptimEnable(String str, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusPerfService {
        @Override // com.oplus.performance.IOplusPerfService
        public boolean isScrollOptimEnable(String pkgName, Bundle output) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusPerfService {
        static final int TRANSACTION_isScrollOptimEnable = 1;

        public Stub() {
            attachInterface(this, IOplusPerfService.DESCRIPTOR);
        }

        public static IOplusPerfService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusPerfService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusPerfService)) {
                return (IOplusPerfService) iin;
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
                    return "isScrollOptimEnable";
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
                data.enforceInterface(IOplusPerfService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusPerfService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            Bundle _arg1 = new Bundle();
                            data.enforceNoDataAvail();
                            boolean _result = isScrollOptimEnable(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            reply.writeTypedObject(_arg1, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusPerfService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusPerfService.DESCRIPTOR;
            }

            @Override // com.oplus.performance.IOplusPerfService
            public boolean isScrollOptimEnable(String pkgName, Bundle output) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPerfService.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    if (_reply.readInt() != 0) {
                        output.readFromParcel(_reply);
                    }
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
