package com.oplus.oshare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusOshareInitListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.oshare.IOplusOshareInitListener";

    void onShareInit() throws RemoteException;

    void onShareUninit() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusOshareInitListener {
        @Override // com.oplus.oshare.IOplusOshareInitListener
        public void onShareInit() throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareInitListener
        public void onShareUninit() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusOshareInitListener {
        static final int TRANSACTION_onShareInit = 1;
        static final int TRANSACTION_onShareUninit = 2;

        public Stub() {
            attachInterface(this, IOplusOshareInitListener.DESCRIPTOR);
        }

        public static IOplusOshareInitListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusOshareInitListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusOshareInitListener)) {
                return (IOplusOshareInitListener) iin;
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
                    return "onShareInit";
                case 2:
                    return "onShareUninit";
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
                data.enforceInterface(IOplusOshareInitListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusOshareInitListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            onShareInit();
                            reply.writeNoException();
                            return true;
                        case 2:
                            onShareUninit();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusOshareInitListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusOshareInitListener.DESCRIPTOR;
            }

            @Override // com.oplus.oshare.IOplusOshareInitListener
            public void onShareInit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareInitListener.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareInitListener
            public void onShareUninit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareInitListener.DESCRIPTOR);
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
