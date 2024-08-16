package com.android.internal.telecom;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ITelecomServiceExt extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telecom.ITelecomServiceExt";

    void cancelMissedCallsNotification(String str, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITelecomServiceExt {
        @Override // com.android.internal.telecom.ITelecomServiceExt
        public void cancelMissedCallsNotification(String callingPackage, Bundle extras) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITelecomServiceExt {
        static final int TRANSACTION_cancelMissedCallsNotification = 1;

        public Stub() {
            attachInterface(this, ITelecomServiceExt.DESCRIPTOR);
        }

        public static ITelecomServiceExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ITelecomServiceExt.DESCRIPTOR);
            if (iin != null && (iin instanceof ITelecomServiceExt)) {
                return (ITelecomServiceExt) iin;
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
                    return "cancelMissedCallsNotification";
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
                data.enforceInterface(ITelecomServiceExt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ITelecomServiceExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            cancelMissedCallsNotification(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ITelecomServiceExt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ITelecomServiceExt.DESCRIPTOR;
            }

            @Override // com.android.internal.telecom.ITelecomServiceExt
            public void cancelMissedCallsNotification(String callingPackage, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ITelecomServiceExt.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeTypedObject(extras, 0);
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
