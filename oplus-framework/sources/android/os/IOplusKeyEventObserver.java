package android.os;

import android.view.KeyEvent;

/* loaded from: classes.dex */
public interface IOplusKeyEventObserver extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusKeyEventObserver";

    void onKeyEvent(KeyEvent keyEvent) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusKeyEventObserver {
        @Override // android.os.IOplusKeyEventObserver
        public void onKeyEvent(KeyEvent info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusKeyEventObserver {
        static final int TRANSACTION_onKeyEvent = 1;

        public Stub() {
            attachInterface(this, IOplusKeyEventObserver.DESCRIPTOR);
        }

        public static IOplusKeyEventObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusKeyEventObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusKeyEventObserver)) {
                return (IOplusKeyEventObserver) iin;
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
                    return "onKeyEvent";
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
                data.enforceInterface(IOplusKeyEventObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusKeyEventObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            KeyEvent _arg0 = (KeyEvent) data.readTypedObject(KeyEvent.CREATOR);
                            data.enforceNoDataAvail();
                            onKeyEvent(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusKeyEventObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusKeyEventObserver.DESCRIPTOR;
            }

            @Override // android.os.IOplusKeyEventObserver
            public void onKeyEvent(KeyEvent info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusKeyEventObserver.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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
