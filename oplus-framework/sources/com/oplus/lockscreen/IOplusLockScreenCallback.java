package com.oplus.lockscreen;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusLockScreenCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.lockscreen.IOplusLockScreenCallback";

    void showDialogForIntercpet(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusLockScreenCallback {
        @Override // com.oplus.lockscreen.IOplusLockScreenCallback
        public void showDialogForIntercpet(String packageName) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusLockScreenCallback {
        static final int TRANSACTION_showDialogForIntercpet = 1;

        public Stub() {
            attachInterface(this, IOplusLockScreenCallback.DESCRIPTOR);
        }

        public static IOplusLockScreenCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusLockScreenCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusLockScreenCallback)) {
                return (IOplusLockScreenCallback) iin;
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
                    return "showDialogForIntercpet";
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
                data.enforceInterface(IOplusLockScreenCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusLockScreenCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            showDialogForIntercpet(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusLockScreenCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusLockScreenCallback.DESCRIPTOR;
            }

            @Override // com.oplus.lockscreen.IOplusLockScreenCallback
            public void showDialogForIntercpet(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLockScreenCallback.DESCRIPTOR);
                    _data.writeString(packageName);
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
