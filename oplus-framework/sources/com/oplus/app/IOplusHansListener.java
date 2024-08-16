package com.oplus.app;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusHansListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusHansListener";

    void notifyRecordData(Bundle bundle, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusHansListener {
        @Override // com.oplus.app.IOplusHansListener
        public void notifyRecordData(Bundle data, String configName) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusHansListener {
        static final int TRANSACTION_notifyRecordData = 1;

        public Stub() {
            attachInterface(this, IOplusHansListener.DESCRIPTOR);
        }

        public static IOplusHansListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusHansListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusHansListener)) {
                return (IOplusHansListener) iin;
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
                    return "notifyRecordData";
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
                data.enforceInterface(IOplusHansListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusHansListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Bundle _arg0 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            notifyRecordData(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusHansListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusHansListener.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusHansListener
            public void notifyRecordData(Bundle data, String configName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusHansListener.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    _data.writeString(configName);
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
