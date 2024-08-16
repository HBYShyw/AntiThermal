package android.engineer;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IEngineerCommandCallback extends IInterface {
    public static final String DESCRIPTOR = "android.engineer.IEngineerCommandCallback";

    void onCommandHandled(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IEngineerCommandCallback {
        @Override // android.engineer.IEngineerCommandCallback
        public void onCommandHandled(String message) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEngineerCommandCallback {
        static final int TRANSACTION_onCommandHandled = 1;

        public Stub() {
            attachInterface(this, IEngineerCommandCallback.DESCRIPTOR);
        }

        public static IEngineerCommandCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IEngineerCommandCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IEngineerCommandCallback)) {
                return (IEngineerCommandCallback) iin;
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
                    return "onCommandHandled";
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
                data.enforceInterface(IEngineerCommandCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IEngineerCommandCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            onCommandHandled(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IEngineerCommandCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IEngineerCommandCallback.DESCRIPTOR;
            }

            @Override // android.engineer.IEngineerCommandCallback
            public void onCommandHandled(String message) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEngineerCommandCallback.DESCRIPTOR);
                    _data.writeString(message);
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
