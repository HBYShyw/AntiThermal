package com.oplus.heimdall;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ITraceListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.heimdall.ITraceListener";

    void onNotify(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITraceListener {
        @Override // com.oplus.heimdall.ITraceListener
        public void onNotify(boolean isTraceOn) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITraceListener {
        static final int TRANSACTION_onNotify = 1;

        public Stub() {
            attachInterface(this, ITraceListener.DESCRIPTOR);
        }

        public static ITraceListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ITraceListener.DESCRIPTOR);
            if (iin != null && (iin instanceof ITraceListener)) {
                return (ITraceListener) iin;
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
                    return "onNotify";
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
                data.enforceInterface(ITraceListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ITraceListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onNotify(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ITraceListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ITraceListener.DESCRIPTOR;
            }

            @Override // com.oplus.heimdall.ITraceListener
            public void onNotify(boolean isTraceOn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITraceListener.DESCRIPTOR);
                    _data.writeBoolean(isTraceOn);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
