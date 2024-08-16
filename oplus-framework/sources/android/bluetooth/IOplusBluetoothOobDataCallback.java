package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusBluetoothOobDataCallback extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IOplusBluetoothOobDataCallback";

    void onError(int i) throws RemoteException;

    void onOobData(int i, OplusBluetoothOobData oplusBluetoothOobData) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBluetoothOobDataCallback {
        @Override // android.bluetooth.IOplusBluetoothOobDataCallback
        public void onOobData(int transport, OplusBluetoothOobData oplusoobData) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetoothOobDataCallback
        public void onError(int errorCode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBluetoothOobDataCallback {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onOobData = 1;

        public Stub() {
            attachInterface(this, IOplusBluetoothOobDataCallback.DESCRIPTOR);
        }

        public static IOplusBluetoothOobDataCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBluetoothOobDataCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBluetoothOobDataCallback)) {
                return (IOplusBluetoothOobDataCallback) iin;
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
                    return "onOobData";
                case 2:
                    return "onError";
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
                data.enforceInterface(IOplusBluetoothOobDataCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBluetoothOobDataCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            OplusBluetoothOobData _arg1 = (OplusBluetoothOobData) data.readTypedObject(OplusBluetoothOobData.CREATOR);
                            data.enforceNoDataAvail();
                            onOobData(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            onError(_arg02);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBluetoothOobDataCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBluetoothOobDataCallback.DESCRIPTOR;
            }

            @Override // android.bluetooth.IOplusBluetoothOobDataCallback
            public void onOobData(int transport, OplusBluetoothOobData oplusoobData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothOobDataCallback.DESCRIPTOR);
                    _data.writeInt(transport);
                    _data.writeTypedObject(oplusoobData, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetoothOobDataCallback
            public void onError(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothOobDataCallback.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
