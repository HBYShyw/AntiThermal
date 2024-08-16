package android.bluetooth;

import android.bluetooth.IOplusBluetoothManagerCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusBluetoothManager extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IOplusBluetoothManager";

    boolean registerBtMonitStateCallback(String str, List<String> list, IOplusBluetoothManagerCallback iOplusBluetoothManagerCallback) throws RemoteException;

    boolean unregisterBtMonitStateCallback(String str, List<String> list, IOplusBluetoothManagerCallback iOplusBluetoothManagerCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBluetoothManager {
        @Override // android.bluetooth.IOplusBluetoothManager
        public boolean unregisterBtMonitStateCallback(String callAppName, List<String> monitorEvents, IOplusBluetoothManagerCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IOplusBluetoothManager
        public boolean registerBtMonitStateCallback(String callAppName, List<String> monitorEvents, IOplusBluetoothManagerCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBluetoothManager {
        static final int TRANSACTION_registerBtMonitStateCallback = 2;
        static final int TRANSACTION_unregisterBtMonitStateCallback = 1;

        public Stub() {
            attachInterface(this, IOplusBluetoothManager.DESCRIPTOR);
        }

        public static IOplusBluetoothManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBluetoothManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBluetoothManager)) {
                return (IOplusBluetoothManager) iin;
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
                    return "unregisterBtMonitStateCallback";
                case 2:
                    return "registerBtMonitStateCallback";
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
                data.enforceInterface(IOplusBluetoothManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBluetoothManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            List<String> _arg1 = data.createStringArrayList();
                            IOplusBluetoothManagerCallback _arg2 = IOplusBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = unregisterBtMonitStateCallback(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            List<String> _arg12 = data.createStringArrayList();
                            IOplusBluetoothManagerCallback _arg22 = IOplusBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = registerBtMonitStateCallback(_arg02, _arg12, _arg22);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBluetoothManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBluetoothManager.DESCRIPTOR;
            }

            @Override // android.bluetooth.IOplusBluetoothManager
            public boolean unregisterBtMonitStateCallback(String callAppName, List<String> monitorEvents, IOplusBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothManager.DESCRIPTOR);
                    _data.writeString(callAppName);
                    _data.writeStringList(monitorEvents);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IOplusBluetoothManager
            public boolean registerBtMonitStateCallback(String callAppName, List<String> monitorEvents, IOplusBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothManager.DESCRIPTOR);
                    _data.writeString(callAppName);
                    _data.writeStringList(monitorEvents);
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
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
