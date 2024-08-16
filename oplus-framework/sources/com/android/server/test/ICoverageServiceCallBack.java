package com.android.server.test;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICoverageServiceCallBack extends IInterface {
    public static final String DESCRIPTOR = "com.android.server.test.ICoverageServiceCallBack";

    void callToDump(String str) throws RemoteException;

    void callToReset(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICoverageServiceCallBack {
        @Override // com.android.server.test.ICoverageServiceCallBack
        public void callToDump(String caseId) throws RemoteException {
        }

        @Override // com.android.server.test.ICoverageServiceCallBack
        public void callToReset(String caseId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICoverageServiceCallBack {
        static final int TRANSACTION_callToDump = 1;
        static final int TRANSACTION_callToReset = 2;

        public Stub() {
            attachInterface(this, ICoverageServiceCallBack.DESCRIPTOR);
        }

        public static ICoverageServiceCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICoverageServiceCallBack.DESCRIPTOR);
            if (iin != null && (iin instanceof ICoverageServiceCallBack)) {
                return (ICoverageServiceCallBack) iin;
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
                    return "callToDump";
                case 2:
                    return "callToReset";
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
                data.enforceInterface(ICoverageServiceCallBack.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICoverageServiceCallBack.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            callToDump(_arg0);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            callToReset(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICoverageServiceCallBack {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICoverageServiceCallBack.DESCRIPTOR;
            }

            @Override // com.android.server.test.ICoverageServiceCallBack
            public void callToDump(String caseId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICoverageServiceCallBack.DESCRIPTOR);
                    _data.writeString(caseId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.server.test.ICoverageServiceCallBack
            public void callToReset(String caseId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICoverageServiceCallBack.DESCRIPTOR);
                    _data.writeString(caseId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
