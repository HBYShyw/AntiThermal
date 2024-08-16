package com.oplus.exfunction;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusExFunction extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.exfunction.IOplusExFunction";

    int endRepairMode() throws RemoteException;

    int startRepairMode() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusExFunction {
        @Override // com.oplus.exfunction.IOplusExFunction
        public int startRepairMode() throws RemoteException {
            return 0;
        }

        @Override // com.oplus.exfunction.IOplusExFunction
        public int endRepairMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusExFunction {
        static final int TRANSACTION_endRepairMode = 2;
        static final int TRANSACTION_startRepairMode = 1;

        public Stub() {
            attachInterface(this, IOplusExFunction.DESCRIPTOR);
        }

        public static IOplusExFunction asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusExFunction.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusExFunction)) {
                return (IOplusExFunction) iin;
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
                    return "startRepairMode";
                case 2:
                    return "endRepairMode";
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
                data.enforceInterface(IOplusExFunction.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusExFunction.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _result = startRepairMode();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            int _result2 = endRepairMode();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusExFunction {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusExFunction.DESCRIPTOR;
            }

            @Override // com.oplus.exfunction.IOplusExFunction
            public int startRepairMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExFunction.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.exfunction.IOplusExFunction
            public int endRepairMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusExFunction.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
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
            return 1;
        }
    }
}
