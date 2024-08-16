package com.oplus.atlas;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusAtlasServiceCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.atlas.IOplusAtlasServiceCallback";

    void notifyEvent(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusAtlasServiceCallback {
        @Override // com.oplus.atlas.IOplusAtlasServiceCallback
        public void notifyEvent(int event, String value) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusAtlasServiceCallback {
        static final int TRANSACTION_notifyEvent = 1;

        public Stub() {
            attachInterface(this, IOplusAtlasServiceCallback.DESCRIPTOR);
        }

        public static IOplusAtlasServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusAtlasServiceCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusAtlasServiceCallback)) {
                return (IOplusAtlasServiceCallback) iin;
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
                    return "notifyEvent";
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
                data.enforceInterface(IOplusAtlasServiceCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusAtlasServiceCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            notifyEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusAtlasServiceCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusAtlasServiceCallback.DESCRIPTOR;
            }

            @Override // com.oplus.atlas.IOplusAtlasServiceCallback
            public void notifyEvent(int event, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAtlasServiceCallback.DESCRIPTOR);
                    _data.writeInt(event);
                    _data.writeString(value);
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
