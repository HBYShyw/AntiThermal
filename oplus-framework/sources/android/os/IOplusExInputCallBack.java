package android.os;

import android.view.InputEvent;

/* loaded from: classes.dex */
public interface IOplusExInputCallBack extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusExInputCallBack";

    void onInputEvent(InputEvent inputEvent) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusExInputCallBack {
        @Override // android.os.IOplusExInputCallBack
        public void onInputEvent(InputEvent event) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusExInputCallBack {
        static final int TRANSACTION_onInputEvent = 1;

        public Stub() {
            attachInterface(this, IOplusExInputCallBack.DESCRIPTOR);
        }

        public static IOplusExInputCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusExInputCallBack.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusExInputCallBack)) {
                return (IOplusExInputCallBack) iin;
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
                    return "onInputEvent";
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
                data.enforceInterface(IOplusExInputCallBack.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusExInputCallBack.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            InputEvent _arg0 = (InputEvent) data.readTypedObject(InputEvent.CREATOR);
                            data.enforceNoDataAvail();
                            onInputEvent(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusExInputCallBack {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusExInputCallBack.DESCRIPTOR;
            }

            @Override // android.os.IOplusExInputCallBack
            public void onInputEvent(InputEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusExInputCallBack.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
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
