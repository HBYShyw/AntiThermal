package com.oplus.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusFeatureActionObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.content.IOplusFeatureActionObserver";

    void onFeaturesActionUpdate(String str, String str2, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFeatureActionObserver {
        @Override // com.oplus.content.IOplusFeatureActionObserver
        public void onFeaturesActionUpdate(String action, String actionValue, int featureID) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFeatureActionObserver {
        static final int TRANSACTION_onFeaturesActionUpdate = 1;

        public Stub() {
            attachInterface(this, IOplusFeatureActionObserver.DESCRIPTOR);
        }

        public static IOplusFeatureActionObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFeatureActionObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFeatureActionObserver)) {
                return (IOplusFeatureActionObserver) iin;
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
                    return "onFeaturesActionUpdate";
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
                data.enforceInterface(IOplusFeatureActionObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFeatureActionObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            String _arg1 = data.readString();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            onFeaturesActionUpdate(_arg0, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFeatureActionObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFeatureActionObserver.DESCRIPTOR;
            }

            @Override // com.oplus.content.IOplusFeatureActionObserver
            public void onFeaturesActionUpdate(String action, String actionValue, int featureID) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFeatureActionObserver.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(actionValue);
                    _data.writeInt(featureID);
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
