package android.os;

/* loaded from: classes.dex */
public interface IOplusGestureCallBack extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusGestureCallBack";

    void onDealGesture(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusGestureCallBack {
        @Override // android.os.IOplusGestureCallBack
        public void onDealGesture(int nGesture) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusGestureCallBack {
        static final int TRANSACTION_onDealGesture = 1;

        public Stub() {
            attachInterface(this, IOplusGestureCallBack.DESCRIPTOR);
        }

        public static IOplusGestureCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusGestureCallBack.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusGestureCallBack)) {
                return (IOplusGestureCallBack) iin;
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
                    return "onDealGesture";
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
                data.enforceInterface(IOplusGestureCallBack.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusGestureCallBack.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            onDealGesture(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusGestureCallBack {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusGestureCallBack.DESCRIPTOR;
            }

            @Override // android.os.IOplusGestureCallBack
            public void onDealGesture(int nGesture) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusGestureCallBack.DESCRIPTOR);
                    _data.writeInt(nGesture);
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
