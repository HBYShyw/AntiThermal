package android.bluetooth.le;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusRssiDetectCallback extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.le.IOplusRssiDetectCallback";

    void onRssiDetectResultCallback(ScanResult scanResult, float f) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusRssiDetectCallback {
        @Override // android.bluetooth.le.IOplusRssiDetectCallback
        public void onRssiDetectResultCallback(ScanResult result, float modifRssi) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusRssiDetectCallback {
        static final int TRANSACTION_onRssiDetectResultCallback = 1;

        public Stub() {
            attachInterface(this, IOplusRssiDetectCallback.DESCRIPTOR);
        }

        public static IOplusRssiDetectCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusRssiDetectCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusRssiDetectCallback)) {
                return (IOplusRssiDetectCallback) iin;
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
                    return "onRssiDetectResultCallback";
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
                data.enforceInterface(IOplusRssiDetectCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusRssiDetectCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ScanResult _arg0 = (ScanResult) data.readTypedObject(ScanResult.CREATOR);
                            float _arg1 = data.readFloat();
                            data.enforceNoDataAvail();
                            onRssiDetectResultCallback(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusRssiDetectCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusRssiDetectCallback.DESCRIPTOR;
            }

            @Override // android.bluetooth.le.IOplusRssiDetectCallback
            public void onRssiDetectResultCallback(ScanResult result, float modifRssi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusRssiDetectCallback.DESCRIPTOR);
                    _data.writeTypedObject(result, 0);
                    _data.writeFloat(modifRssi);
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
