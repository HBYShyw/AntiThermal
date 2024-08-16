package com.oplus.nas.cybersense.sdk;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICyberSenseCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.nas.cybersense.sdk.ICyberSenseCallback";

    void onEventStateChanged(int i, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICyberSenseCallback {
        @Override // com.oplus.nas.cybersense.sdk.ICyberSenseCallback
        public void onEventStateChanged(int type, Bundle result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICyberSenseCallback {
        static final int TRANSACTION_onEventStateChanged = 1;

        public Stub() {
            attachInterface(this, ICyberSenseCallback.DESCRIPTOR);
        }

        public static ICyberSenseCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICyberSenseCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof ICyberSenseCallback)) {
                return (ICyberSenseCallback) iin;
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
                    return "onEventStateChanged";
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
                data.enforceInterface(ICyberSenseCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICyberSenseCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onEventStateChanged(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICyberSenseCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICyberSenseCallback.DESCRIPTOR;
            }

            @Override // com.oplus.nas.cybersense.sdk.ICyberSenseCallback
            public void onEventStateChanged(int type, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICyberSenseCallback.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(result, 0);
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
