package com.oplus.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusLongshotCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusLongshotCallback";

    void stop() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusLongshotCallback {
        @Override // com.oplus.screenshot.IOplusLongshotCallback
        public void stop() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusLongshotCallback {
        static final int TRANSACTION_stop = 1;

        public Stub() {
            attachInterface(this, IOplusLongshotCallback.DESCRIPTOR);
        }

        public static IOplusLongshotCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusLongshotCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusLongshotCallback)) {
                return (IOplusLongshotCallback) iin;
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
                    return "stop";
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
                data.enforceInterface(IOplusLongshotCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusLongshotCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            stop();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusLongshotCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusLongshotCallback.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusLongshotCallback
            public void stop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLongshotCallback.DESCRIPTOR);
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
