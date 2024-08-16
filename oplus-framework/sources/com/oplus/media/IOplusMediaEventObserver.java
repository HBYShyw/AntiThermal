package com.oplus.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusMediaEventObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.media.IOplusMediaEventObserver";

    void onEvent(OplusMediaEvent oplusMediaEvent) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusMediaEventObserver {
        @Override // com.oplus.media.IOplusMediaEventObserver
        public void onEvent(OplusMediaEvent event) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMediaEventObserver {
        static final int TRANSACTION_onEvent = 1;

        public Stub() {
            attachInterface(this, IOplusMediaEventObserver.DESCRIPTOR);
        }

        public static IOplusMediaEventObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMediaEventObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMediaEventObserver)) {
                return (IOplusMediaEventObserver) iin;
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
                    return "onEvent";
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
                data.enforceInterface(IOplusMediaEventObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMediaEventObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusMediaEvent _arg0 = (OplusMediaEvent) data.readTypedObject(OplusMediaEvent.CREATOR);
                            data.enforceNoDataAvail();
                            onEvent(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMediaEventObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMediaEventObserver.DESCRIPTOR;
            }

            @Override // com.oplus.media.IOplusMediaEventObserver
            public void onEvent(OplusMediaEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusMediaEventObserver.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
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
