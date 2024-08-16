package android.os.oplusdevicepolicy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusDevicePolicyObserver extends IInterface {
    public static final String DESCRIPTOR = "android.os.oplusdevicepolicy.IOplusDevicePolicyObserver";

    void onOplusDevicePolicyListUpdate(String str, List<String> list) throws RemoteException;

    void onOplusDevicePolicyValueUpdate(String str, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusDevicePolicyObserver {
        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyObserver
        public void onOplusDevicePolicyListUpdate(String name, List<String> list) throws RemoteException {
        }

        @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyObserver
        public void onOplusDevicePolicyValueUpdate(String name, String value) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusDevicePolicyObserver {
        static final int TRANSACTION_onOplusDevicePolicyListUpdate = 1;
        static final int TRANSACTION_onOplusDevicePolicyValueUpdate = 2;

        public Stub() {
            attachInterface(this, IOplusDevicePolicyObserver.DESCRIPTOR);
        }

        public static IOplusDevicePolicyObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusDevicePolicyObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusDevicePolicyObserver)) {
                return (IOplusDevicePolicyObserver) iin;
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
                    return "onOplusDevicePolicyListUpdate";
                case 2:
                    return "onOplusDevicePolicyValueUpdate";
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
                data.enforceInterface(IOplusDevicePolicyObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusDevicePolicyObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            List<String> _arg1 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            onOplusDevicePolicyListUpdate(_arg0, _arg1);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            onOplusDevicePolicyValueUpdate(_arg02, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusDevicePolicyObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusDevicePolicyObserver.DESCRIPTOR;
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyObserver
            public void onOplusDevicePolicyListUpdate(String name, List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyObserver.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStringList(list);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.oplusdevicepolicy.IOplusDevicePolicyObserver
            public void onOplusDevicePolicyValueUpdate(String name, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusDevicePolicyObserver.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(value);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
