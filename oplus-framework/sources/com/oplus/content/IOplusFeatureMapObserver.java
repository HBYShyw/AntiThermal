package com.oplus.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusFeatureMapObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.content.IOplusFeatureMapObserver";

    void onFeatureUpdate(List<String> list, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFeatureMapObserver {
        @Override // com.oplus.content.IOplusFeatureMapObserver
        public void onFeatureUpdate(List<String> featureList, int featureID) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFeatureMapObserver {
        static final int TRANSACTION_onFeatureUpdate = 1;

        public Stub() {
            attachInterface(this, IOplusFeatureMapObserver.DESCRIPTOR);
        }

        public static IOplusFeatureMapObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFeatureMapObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFeatureMapObserver)) {
                return (IOplusFeatureMapObserver) iin;
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
                data.enforceInterface(IOplusFeatureMapObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFeatureMapObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            List<String> _arg0 = data.createStringArrayList();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onFeatureUpdate(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFeatureMapObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFeatureMapObserver.DESCRIPTOR;
            }

            @Override // com.oplus.content.IOplusFeatureMapObserver
            public void onFeatureUpdate(List<String> featureList, int featureID) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFeatureMapObserver.DESCRIPTOR);
                    _data.writeStringList(featureList);
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
