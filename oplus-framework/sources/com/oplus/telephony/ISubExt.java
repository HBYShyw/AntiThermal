package com.oplus.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ISubExt extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.telephony.ISubExt";

    String getIsimImpiForSubscriber(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISubExt {
        @Override // com.oplus.telephony.ISubExt
        public String getIsimImpiForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISubExt {
        static final int TRANSACTION_getIsimImpiForSubscriber = 1;

        public Stub() {
            attachInterface(this, ISubExt.DESCRIPTOR);
        }

        public static ISubExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISubExt.DESCRIPTOR);
            if (iin != null && (iin instanceof ISubExt)) {
                return (ISubExt) iin;
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
                    return "getIsimImpiForSubscriber";
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
                data.enforceInterface(ISubExt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ISubExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result = getIsimImpiForSubscriber(_arg0);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISubExt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISubExt.DESCRIPTOR;
            }

            @Override // com.oplus.telephony.ISubExt
            public String getIsimImpiForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISubExt.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
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
