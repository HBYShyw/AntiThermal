package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusBluetoothManagerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IOplusBluetoothManagerCallback";

    void onBluetoothManagerMonitor(String str, Map map) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBluetoothManagerCallback {
        @Override // android.bluetooth.IOplusBluetoothManagerCallback
        public void onBluetoothManagerMonitor(String monitorEvent, Map monitResult) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBluetoothManagerCallback {
        static final int TRANSACTION_onBluetoothManagerMonitor = 1;

        public Stub() {
            attachInterface(this, IOplusBluetoothManagerCallback.DESCRIPTOR);
        }

        public static IOplusBluetoothManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBluetoothManagerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBluetoothManagerCallback)) {
                return (IOplusBluetoothManagerCallback) iin;
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
                    return "onBluetoothManagerMonitor";
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
                data.enforceInterface(IOplusBluetoothManagerCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBluetoothManagerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg1 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            onBluetoothManagerMonitor(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBluetoothManagerCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBluetoothManagerCallback.DESCRIPTOR;
            }

            @Override // android.bluetooth.IOplusBluetoothManagerCallback
            public void onBluetoothManagerMonitor(String monitorEvent, Map monitResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBluetoothManagerCallback.DESCRIPTOR);
                    _data.writeString(monitorEvent);
                    _data.writeMap(monitResult);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
