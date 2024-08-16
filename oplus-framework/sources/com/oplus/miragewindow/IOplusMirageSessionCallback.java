package com.oplus.miragewindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusMirageSessionCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.miragewindow.IOplusMirageSessionCallback";

    /* loaded from: classes.dex */
    public static class Default implements IOplusMirageSessionCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusMirageSessionCallback {
        public Stub() {
            attachInterface(this, IOplusMirageSessionCallback.DESCRIPTOR);
        }

        public static IOplusMirageSessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusMirageSessionCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusMirageSessionCallback)) {
                return (IOplusMirageSessionCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            return null;
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusMirageSessionCallback.DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusMirageSessionCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusMirageSessionCallback.DESCRIPTOR;
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
