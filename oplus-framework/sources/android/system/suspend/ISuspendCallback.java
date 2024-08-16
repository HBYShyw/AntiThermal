package android.system.suspend;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ISuspendCallback extends IInterface {
    public static final String DESCRIPTOR = "android.system.suspend.ISuspendCallback";

    void notifyWakeup(boolean z, String[] strArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISuspendCallback {
        @Override // android.system.suspend.ISuspendCallback
        public void notifyWakeup(boolean success, String[] wakeupReasons) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISuspendCallback {
        static final int TRANSACTION_notifyWakeup = 1;

        public Stub() {
            attachInterface(this, ISuspendCallback.DESCRIPTOR);
        }

        public static ISuspendCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISuspendCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof ISuspendCallback)) {
                return (ISuspendCallback) iin;
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
                    return "notifyWakeup";
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
                data.enforceInterface(ISuspendCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ISuspendCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            String[] _arg1 = data.createStringArray();
                            data.enforceNoDataAvail();
                            notifyWakeup(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISuspendCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISuspendCallback.DESCRIPTOR;
            }

            @Override // android.system.suspend.ISuspendCallback
            public void notifyWakeup(boolean success, String[] wakeupReasons) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISuspendCallback.DESCRIPTOR);
                    _data.writeBoolean(success);
                    _data.writeStringArray(wakeupReasons);
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
