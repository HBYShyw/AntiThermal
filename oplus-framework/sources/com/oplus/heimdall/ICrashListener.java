package com.oplus.heimdall;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICrashListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.heimdall.ICrashListener";

    void onNotify(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICrashListener {
        @Override // com.oplus.heimdall.ICrashListener
        public void onNotify(String packageName) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICrashListener {
        static final int TRANSACTION_onNotify = 1;

        public Stub() {
            attachInterface(this, ICrashListener.DESCRIPTOR);
        }

        public static ICrashListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICrashListener.DESCRIPTOR);
            if (iin != null && (iin instanceof ICrashListener)) {
                return (ICrashListener) iin;
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
                data.enforceInterface(ICrashListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICrashListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            onNotify(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICrashListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICrashListener.DESCRIPTOR;
            }

            @Override // com.oplus.heimdall.ICrashListener
            public void onNotify(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICrashListener.DESCRIPTOR);
                    _data.writeString(packageName);
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
