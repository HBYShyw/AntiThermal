package android.os;

import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusFilterListener extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusFilterListener";

    void onFilterChanged(int i, Map map) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusFilterListener {
        @Override // android.os.IOplusFilterListener
        public void onFilterChanged(int hashCode, Map filter) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusFilterListener {
        static final int TRANSACTION_onFilterChanged = 1;

        public Stub() {
            attachInterface(this, IOplusFilterListener.DESCRIPTOR);
        }

        public static IOplusFilterListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusFilterListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusFilterListener)) {
                return (IOplusFilterListener) iin;
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
                    return "onFilterChanged";
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
                data.enforceInterface(IOplusFilterListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusFilterListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg1 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            onFilterChanged(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusFilterListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusFilterListener.DESCRIPTOR;
            }

            @Override // android.os.IOplusFilterListener
            public void onFilterChanged(int hashCode, Map filter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusFilterListener.DESCRIPTOR);
                    _data.writeInt(hashCode);
                    _data.writeMap(filter);
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
