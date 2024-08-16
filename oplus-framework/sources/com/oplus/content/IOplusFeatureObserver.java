package com.oplus.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusFeatureObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.content.IOplusFeatureObserver";

    void onFeatureUpdate(List<String> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFeatureObserver {
        @Override // com.oplus.content.IOplusFeatureObserver
        public void onFeatureUpdate(List<String> features) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFeatureObserver {
        static final int TRANSACTION_onFeatureUpdate = 1;

        public Stub() {
            attachInterface(this, IOplusFeatureObserver.DESCRIPTOR);
        }

        public static IOplusFeatureObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFeatureObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFeatureObserver)) {
                return (IOplusFeatureObserver) iin;
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
                    return "onFeatureUpdate";
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
                data.enforceInterface(IOplusFeatureObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFeatureObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<String> _arg0 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            onFeatureUpdate(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFeatureObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFeatureObserver.DESCRIPTOR;
            }

            @Override // com.oplus.content.IOplusFeatureObserver
            public void onFeatureUpdate(List<String> features) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFeatureObserver.DESCRIPTOR);
                    _data.writeStringList(features);
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
