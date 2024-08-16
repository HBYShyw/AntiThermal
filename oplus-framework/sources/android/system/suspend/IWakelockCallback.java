package android.system.suspend;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IWakelockCallback extends IInterface {
    public static final String DESCRIPTOR = "android.system.suspend.IWakelockCallback";

    void notifyAcquired() throws RemoteException;

    void notifyReleased() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IWakelockCallback {
        @Override // android.system.suspend.IWakelockCallback
        public void notifyAcquired() throws RemoteException {
        }

        @Override // android.system.suspend.IWakelockCallback
        public void notifyReleased() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IWakelockCallback {
        static final int TRANSACTION_notifyAcquired = 1;
        static final int TRANSACTION_notifyReleased = 2;

        public Stub() {
            attachInterface(this, IWakelockCallback.DESCRIPTOR);
        }

        public static IWakelockCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IWakelockCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IWakelockCallback)) {
                return (IWakelockCallback) iin;
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
                    return "notifyAcquired";
                case 2:
                    return "notifyReleased";
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
                data.enforceInterface(IWakelockCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IWakelockCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            notifyAcquired();
                            return true;
                        case 2:
                            notifyReleased();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IWakelockCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IWakelockCallback.DESCRIPTOR;
            }

            @Override // android.system.suspend.IWakelockCallback
            public void notifyAcquired() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWakelockCallback.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.system.suspend.IWakelockCallback
            public void notifyReleased() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWakelockCallback.DESCRIPTOR);
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
