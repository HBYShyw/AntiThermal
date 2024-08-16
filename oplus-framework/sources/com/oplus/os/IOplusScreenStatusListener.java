package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusScreenStatusListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IOplusScreenStatusListener";

    void onScreenOff() throws RemoteException;

    void onScreenOn() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScreenStatusListener {
        @Override // com.oplus.os.IOplusScreenStatusListener
        public void onScreenOff() throws RemoteException {
        }

        @Override // com.oplus.os.IOplusScreenStatusListener
        public void onScreenOn() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScreenStatusListener {
        static final int TRANSACTION_onScreenOff = 1;
        static final int TRANSACTION_onScreenOn = 2;

        public Stub() {
            attachInterface(this, IOplusScreenStatusListener.DESCRIPTOR);
        }

        public static IOplusScreenStatusListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScreenStatusListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScreenStatusListener)) {
                return (IOplusScreenStatusListener) iin;
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
                    return "onScreenOff";
                case 2:
                    return "onScreenOn";
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
                data.enforceInterface(IOplusScreenStatusListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScreenStatusListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            onScreenOff();
                            return true;
                        case 2:
                            onScreenOn();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScreenStatusListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScreenStatusListener.DESCRIPTOR;
            }

            @Override // com.oplus.os.IOplusScreenStatusListener
            public void onScreenOff() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenStatusListener.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusScreenStatusListener
            public void onScreenOn() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenStatusListener.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
