package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusClickTopCallback extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusClickTopCallback";

    void onClickTopCallback() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusClickTopCallback {
        @Override // android.app.IOplusClickTopCallback
        public void onClickTopCallback() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusClickTopCallback {
        static final int TRANSACTION_onClickTopCallback = 1;

        public Stub() {
            attachInterface(this, IOplusClickTopCallback.DESCRIPTOR);
        }

        public static IOplusClickTopCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusClickTopCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusClickTopCallback)) {
                return (IOplusClickTopCallback) iin;
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
                    return "onClickTopCallback";
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
                data.enforceInterface(IOplusClickTopCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusClickTopCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            onClickTopCallback();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusClickTopCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusClickTopCallback.DESCRIPTOR;
            }

            @Override // android.app.IOplusClickTopCallback
            public void onClickTopCallback() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusClickTopCallback.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
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
