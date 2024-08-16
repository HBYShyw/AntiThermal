package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface INeworkSelectionOptCb extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.INeworkSelectionOptCb";

    void notifyAvaliableBss(String[] strArr) throws RemoteException;

    void notifyNetworkSelectionStatus(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements INeworkSelectionOptCb {
        @Override // android.net.wifi.INeworkSelectionOptCb
        public void notifyNetworkSelectionStatus(int status, String bssId) throws RemoteException {
        }

        @Override // android.net.wifi.INeworkSelectionOptCb
        public void notifyAvaliableBss(String[] bssIdList) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INeworkSelectionOptCb {
        static final int TRANSACTION_notifyAvaliableBss = 2;
        static final int TRANSACTION_notifyNetworkSelectionStatus = 1;

        public Stub() {
            attachInterface(this, INeworkSelectionOptCb.DESCRIPTOR);
        }

        public static INeworkSelectionOptCb asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INeworkSelectionOptCb.DESCRIPTOR);
            if (iin != null && (iin instanceof INeworkSelectionOptCb)) {
                return (INeworkSelectionOptCb) iin;
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
                    return "notifyNetworkSelectionStatus";
                case 2:
                    return "notifyAvaliableBss";
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
                data.enforceInterface(INeworkSelectionOptCb.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(INeworkSelectionOptCb.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            notifyNetworkSelectionStatus(_arg0, _arg1);
                            return true;
                        case 2:
                            String[] _arg02 = data.createStringArray();
                            data.enforceNoDataAvail();
                            notifyAvaliableBss(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INeworkSelectionOptCb {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INeworkSelectionOptCb.DESCRIPTOR;
            }

            @Override // android.net.wifi.INeworkSelectionOptCb
            public void notifyNetworkSelectionStatus(int status, String bssId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(INeworkSelectionOptCb.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(bssId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.INeworkSelectionOptCb
            public void notifyAvaliableBss(String[] bssIdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(INeworkSelectionOptCb.DESCRIPTOR);
                    _data.writeStringArray(bssIdList);
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
