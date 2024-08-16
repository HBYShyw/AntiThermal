package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IFingerprintCommandCallback extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintCommandCallback";

    void onFingerprintCmd(int i, byte[] bArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IFingerprintCommandCallback {
        @Override // android.hardware.fingerprint.IFingerprintCommandCallback
        public void onFingerprintCmd(int cmdId, byte[] result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFingerprintCommandCallback {
        static final int TRANSACTION_onFingerprintCmd = 1;

        public Stub() {
            attachInterface(this, IFingerprintCommandCallback.DESCRIPTOR);
        }

        public static IFingerprintCommandCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFingerprintCommandCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IFingerprintCommandCallback)) {
                return (IFingerprintCommandCallback) iin;
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
                    return "onFingerprintCmd";
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
                data.enforceInterface(IFingerprintCommandCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IFingerprintCommandCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            byte[] _arg1 = data.createByteArray();
                            data.enforceNoDataAvail();
                            onFingerprintCmd(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IFingerprintCommandCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFingerprintCommandCallback.DESCRIPTOR;
            }

            @Override // android.hardware.fingerprint.IFingerprintCommandCallback
            public void onFingerprintCmd(int cmdId, byte[] result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IFingerprintCommandCallback.DESCRIPTOR);
                    _data.writeInt(cmdId);
                    _data.writeByteArray(result);
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
