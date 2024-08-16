package com.oplus.osense.complexscene;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IComplexSceneObserver extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.osense.complexscene.IComplexSceneObserver";

    void onChanged(int i, List<Bundle> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IComplexSceneObserver {
        @Override // com.oplus.osense.complexscene.IComplexSceneObserver
        public void onChanged(int eventId, List<Bundle> bundleList) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IComplexSceneObserver {
        static final int TRANSACTION_onChanged = 1;

        public Stub() {
            attachInterface(this, IComplexSceneObserver.DESCRIPTOR);
        }

        public static IComplexSceneObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IComplexSceneObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IComplexSceneObserver)) {
                return (IComplexSceneObserver) iin;
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
                    return "onChanged";
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
                data.enforceInterface(IComplexSceneObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IComplexSceneObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            List<Bundle> _arg1 = data.createTypedArrayList(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onChanged(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IComplexSceneObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IComplexSceneObserver.DESCRIPTOR;
            }

            @Override // com.oplus.osense.complexscene.IComplexSceneObserver
            public void onChanged(int eventId, List<Bundle> bundleList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IComplexSceneObserver.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeTypedList(bundleList, 0);
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
