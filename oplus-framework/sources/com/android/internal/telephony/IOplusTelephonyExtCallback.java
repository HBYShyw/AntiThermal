package com.android.internal.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusTelephonyExtCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IOplusTelephonyExtCallback";

    void onTelephonyEventReport(int i, int i2, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTelephonyExtCallback {
        @Override // com.android.internal.telephony.IOplusTelephonyExtCallback
        public void onTelephonyEventReport(int slotId, int eventId, Bundle data) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTelephonyExtCallback {
        static final int TRANSACTION_onTelephonyEventReport = 8;

        public Stub() {
            attachInterface(this, IOplusTelephonyExtCallback.DESCRIPTOR);
        }

        public static IOplusTelephonyExtCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTelephonyExtCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTelephonyExtCallback)) {
                return (IOplusTelephonyExtCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 8:
                    return "onTelephonyEventReport";
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
                data.enforceInterface(IOplusTelephonyExtCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTelephonyExtCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 8:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onTelephonyEventReport(_arg0, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTelephonyExtCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTelephonyExtCallback.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IOplusTelephonyExtCallback
            public void onTelephonyEventReport(int slotId, int eventId, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTelephonyExtCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(eventId);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 7;
        }
    }
}
