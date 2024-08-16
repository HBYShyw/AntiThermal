package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusFingerprintServiceReceiver extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.fingerprint.IOplusFingerprintServiceReceiver";

    void onError(int i, int i2) throws RemoteException;

    void onFingerprintEngineeringInfoUpdated(EngineeringInfo engineeringInfo) throws RemoteException;

    void onTouchDown(int i) throws RemoteException;

    void onTouchUp(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFingerprintServiceReceiver {
        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onFingerprintEngineeringInfoUpdated(EngineeringInfo info) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onError(int error, int vendorCode) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onTouchDown(int sensorId) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
        public void onTouchUp(int sensorId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFingerprintServiceReceiver {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onFingerprintEngineeringInfoUpdated = 1;
        static final int TRANSACTION_onTouchDown = 3;
        static final int TRANSACTION_onTouchUp = 4;

        public Stub() {
            attachInterface(this, IOplusFingerprintServiceReceiver.DESCRIPTOR);
        }

        public static IOplusFingerprintServiceReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFingerprintServiceReceiver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFingerprintServiceReceiver)) {
                return (IOplusFingerprintServiceReceiver) iin;
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
                    return "onFingerprintEngineeringInfoUpdated";
                case 2:
                    return "onError";
                case 3:
                    return "onTouchDown";
                case 4:
                    return "onTouchUp";
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
                data.enforceInterface(IOplusFingerprintServiceReceiver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFingerprintServiceReceiver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            EngineeringInfo _arg0 = (EngineeringInfo) data.readTypedObject(EngineeringInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onFingerprintEngineeringInfoUpdated(_arg0);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onError(_arg02, _arg1);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            onTouchDown(_arg03);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            onTouchUp(_arg04);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFingerprintServiceReceiver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFingerprintServiceReceiver.DESCRIPTOR;
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
            public void onFingerprintEngineeringInfoUpdated(EngineeringInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFingerprintServiceReceiver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
            public void onError(int error, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFingerprintServiceReceiver.DESCRIPTOR);
                    _data.writeInt(error);
                    _data.writeInt(vendorCode);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
            public void onTouchDown(int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFingerprintServiceReceiver.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IOplusFingerprintServiceReceiver
            public void onTouchUp(int sensorId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFingerprintServiceReceiver.DESCRIPTOR);
                    _data.writeInt(sensorId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
