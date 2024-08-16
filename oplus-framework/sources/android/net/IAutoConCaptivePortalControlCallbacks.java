package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IAutoConCaptivePortalControlCallbacks extends IInterface {
    public static final String DESCRIPTOR = "android.net.IAutoConCaptivePortalControlCallbacks";

    void notifyNetworkTested(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAutoConCaptivePortalControlCallbacks {
        @Override // android.net.IAutoConCaptivePortalControlCallbacks
        public void notifyNetworkTested(int testResult) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAutoConCaptivePortalControlCallbacks {
        static final int TRANSACTION_notifyNetworkTested = 1;

        public Stub() {
            attachInterface(this, IAutoConCaptivePortalControlCallbacks.DESCRIPTOR);
        }

        public static IAutoConCaptivePortalControlCallbacks asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAutoConCaptivePortalControlCallbacks.DESCRIPTOR);
            if (iin != null && (iin instanceof IAutoConCaptivePortalControlCallbacks)) {
                return (IAutoConCaptivePortalControlCallbacks) iin;
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
                    return "notifyNetworkTested";
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
                data.enforceInterface(IAutoConCaptivePortalControlCallbacks.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAutoConCaptivePortalControlCallbacks.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyNetworkTested(_arg0);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAutoConCaptivePortalControlCallbacks {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAutoConCaptivePortalControlCallbacks.DESCRIPTOR;
            }

            @Override // android.net.IAutoConCaptivePortalControlCallbacks
            public void notifyNetworkTested(int testResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAutoConCaptivePortalControlCallbacks.DESCRIPTOR);
                    _data.writeInt(testResult);
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
