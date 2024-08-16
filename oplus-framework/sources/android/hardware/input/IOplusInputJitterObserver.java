package android.hardware.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusInputJitterObserver extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.input.IOplusInputJitterObserver";

    void notifyGameInputJitter(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusInputJitterObserver {
        @Override // android.hardware.input.IOplusInputJitterObserver
        public void notifyGameInputJitter(String info) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusInputJitterObserver {
        static final int TRANSACTION_notifyGameInputJitter = 1;

        public Stub() {
            attachInterface(this, IOplusInputJitterObserver.DESCRIPTOR);
        }

        public static IOplusInputJitterObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusInputJitterObserver.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusInputJitterObserver)) {
                return (IOplusInputJitterObserver) iin;
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
                    return "notifyGameInputJitter";
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
                data.enforceInterface(IOplusInputJitterObserver.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusInputJitterObserver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            notifyGameInputJitter(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusInputJitterObserver {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusInputJitterObserver.DESCRIPTOR;
            }

            @Override // android.hardware.input.IOplusInputJitterObserver
            public void notifyGameInputJitter(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusInputJitterObserver.DESCRIPTOR);
                    _data.writeString(info);
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
