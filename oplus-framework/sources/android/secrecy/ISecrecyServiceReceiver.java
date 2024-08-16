package android.secrecy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

/* loaded from: classes.dex */
public interface ISecrecyServiceReceiver extends IInterface {
    public static final String DESCRIPTOR = "android.secrecy.ISecrecyServiceReceiver";

    void onSecrecyStateChanged(Map map) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISecrecyServiceReceiver {
        @Override // android.secrecy.ISecrecyServiceReceiver
        public void onSecrecyStateChanged(Map map) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISecrecyServiceReceiver {
        static final int TRANSACTION_onSecrecyStateChanged = 1;

        public Stub() {
            attachInterface(this, ISecrecyServiceReceiver.DESCRIPTOR);
        }

        public static ISecrecyServiceReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISecrecyServiceReceiver.DESCRIPTOR);
            if (iin != null && (iin instanceof ISecrecyServiceReceiver)) {
                return (ISecrecyServiceReceiver) iin;
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
                    return "onSecrecyStateChanged";
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
                data.enforceInterface(ISecrecyServiceReceiver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ISecrecyServiceReceiver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg0 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            onSecrecyStateChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISecrecyServiceReceiver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISecrecyServiceReceiver.DESCRIPTOR;
            }

            @Override // android.secrecy.ISecrecyServiceReceiver
            public void onSecrecyStateChanged(Map map) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ISecrecyServiceReceiver.DESCRIPTOR);
                    _data.writeMap(map);
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
