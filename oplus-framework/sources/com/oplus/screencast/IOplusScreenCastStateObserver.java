package com.oplus.screencast;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusScreenCastStateObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screencast.IOplusScreenCastStateObserver";

    void onContentConfigChanged(OplusScreenCastInfo oplusScreenCastInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScreenCastStateObserver {
        @Override // com.oplus.screencast.IOplusScreenCastStateObserver
        public void onContentConfigChanged(OplusScreenCastInfo info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScreenCastStateObserver {
        static final int TRANSACTION_onContentConfigChanged = 1;

        public Stub() {
            attachInterface(this, IOplusScreenCastStateObserver.DESCRIPTOR);
        }

        public static IOplusScreenCastStateObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScreenCastStateObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScreenCastStateObserver)) {
                return (IOplusScreenCastStateObserver) iin;
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
                    return "onContentConfigChanged";
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
                data.enforceInterface(IOplusScreenCastStateObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScreenCastStateObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusScreenCastInfo _arg0 = (OplusScreenCastInfo) data.readTypedObject(OplusScreenCastInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onContentConfigChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScreenCastStateObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScreenCastStateObserver.DESCRIPTOR;
            }

            @Override // com.oplus.screencast.IOplusScreenCastStateObserver
            public void onContentConfigChanged(OplusScreenCastInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenCastStateObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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
