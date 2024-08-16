package android.os.customize;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusPreciseCallStateChangedInnerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusPreciseCallStateChangedInnerCallback";

    void OnCustPreciseCallStateChanged(Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusPreciseCallStateChangedInnerCallback {
        @Override // android.os.customize.IOplusPreciseCallStateChangedInnerCallback
        public void OnCustPreciseCallStateChanged(Bundle bundle) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusPreciseCallStateChangedInnerCallback {
        static final int TRANSACTION_OnCustPreciseCallStateChanged = 1;

        public Stub() {
            attachInterface(this, IOplusPreciseCallStateChangedInnerCallback.DESCRIPTOR);
        }

        public static IOplusPreciseCallStateChangedInnerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusPreciseCallStateChangedInnerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusPreciseCallStateChangedInnerCallback)) {
                return (IOplusPreciseCallStateChangedInnerCallback) iin;
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
                    return "OnCustPreciseCallStateChanged";
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
                data.enforceInterface(IOplusPreciseCallStateChangedInnerCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusPreciseCallStateChangedInnerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Bundle _arg0 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            OnCustPreciseCallStateChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusPreciseCallStateChangedInnerCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusPreciseCallStateChangedInnerCallback.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusPreciseCallStateChangedInnerCallback
            public void OnCustPreciseCallStateChanged(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPreciseCallStateChangedInnerCallback.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
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
