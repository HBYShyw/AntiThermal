package android.bluetooth;

import android.bluetooth.le.IOplusRssiDetectCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusBluetoothGatt extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IOplusBluetoothGatt";

    void clientSetConnectMode(String str, int i) throws RemoteException;

    boolean registerBluetoothRssiDetectCallback(String str, IOplusRssiDetectCallback iOplusRssiDetectCallback) throws RemoteException;

    boolean unregisterBluetoothRssiDetectCallback(String str, IOplusRssiDetectCallback iOplusRssiDetectCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBluetoothGatt {
        @Override // android.bluetooth.IOplusBluetoothGatt
        public void clientSetConnectMode(String address, int mode) throws RemoteException {
        }

        @Override // android.bluetooth.IOplusBluetoothGatt
        public boolean unregisterBluetoothRssiDetectCallback(String callAppName, IOplusRssiDetectCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetoothGatt
        public boolean registerBluetoothRssiDetectCallback(String callAppName, IOplusRssiDetectCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBluetoothGatt {
        static final int TRANSACTION_clientSetConnectMode = 1;
        static final int TRANSACTION_registerBluetoothRssiDetectCallback = 3;
        static final int TRANSACTION_unregisterBluetoothRssiDetectCallback = 2;

        public Stub() {
            attachInterface(this, IOplusBluetoothGatt.DESCRIPTOR);
        }

        public static IOplusBluetoothGatt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBluetoothGatt.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBluetoothGatt)) {
                return (IOplusBluetoothGatt) iin;
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
                    return "clientSetConnectMode";
                case 2:
                    return "unregisterBluetoothRssiDetectCallback";
                case 3:
                    return "registerBluetoothRssiDetectCallback";
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
                data.enforceInterface(IOplusBluetoothGatt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBluetoothGatt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            clientSetConnectMode(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            IOplusRssiDetectCallback _arg12 = IOplusRssiDetectCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = unregisterBluetoothRssiDetectCallback(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            IOplusRssiDetectCallback _arg13 = IOplusRssiDetectCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = registerBluetoothRssiDetectCallback(_arg03, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBluetoothGatt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBluetoothGatt.DESCRIPTOR;
            }

            @Override // android.bluetooth.IOplusBluetoothGatt
            public void clientSetConnectMode(String address, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothGatt.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(mode);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetoothGatt
            public boolean unregisterBluetoothRssiDetectCallback(String callAppName, IOplusRssiDetectCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothGatt.DESCRIPTOR);
                    _data.writeString(callAppName);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetoothGatt
            public boolean registerBluetoothRssiDetectCallback(String callAppName, IOplusRssiDetectCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothGatt.DESCRIPTOR);
                    _data.writeString(callAppName);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
