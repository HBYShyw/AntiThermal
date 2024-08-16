package com.oplus.putt;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;

/* loaded from: classes.dex */
public interface IPuttServerCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.putt.IPuttServerCallback";

    int attachDisplayToSurface(Surface surface, Bundle bundle) throws RemoteException;

    void removePuttTask(int i, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPuttServerCallback {
        @Override // com.oplus.putt.IPuttServerCallback
        public int attachDisplayToSurface(Surface surface, Bundle extras) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.putt.IPuttServerCallback
        public void removePuttTask(int exitAction, Bundle extras) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPuttServerCallback {
        static final int TRANSACTION_attachDisplayToSurface = 1;
        static final int TRANSACTION_removePuttTask = 2;

        public Stub() {
            attachInterface(this, IPuttServerCallback.DESCRIPTOR);
        }

        public static IPuttServerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPuttServerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IPuttServerCallback)) {
                return (IPuttServerCallback) iin;
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
                    return "attachDisplayToSurface";
                case 2:
                    return "removePuttTask";
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
                data.enforceInterface(IPuttServerCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPuttServerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Surface _arg0 = (Surface) data.readTypedObject(Surface.CREATOR);
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            int _result = attachDisplayToSurface(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            Bundle _arg12 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            removePuttTask(_arg02, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPuttServerCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPuttServerCallback.DESCRIPTOR;
            }

            @Override // com.oplus.putt.IPuttServerCallback
            public int attachDisplayToSurface(Surface surface, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPuttServerCallback.DESCRIPTOR);
                    _data.writeTypedObject(surface, 0);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.putt.IPuttServerCallback
            public void removePuttTask(int exitAction, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IPuttServerCallback.DESCRIPTOR);
                    _data.writeInt(exitAction);
                    _data.writeTypedObject(extras, 0);
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
