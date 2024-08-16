package com.oplus.eap;

import android.app.ApplicationExitInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SharedMemory;

/* loaded from: classes.dex */
public interface IOplusEapDataCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.eap.IOplusEapDataCallback";

    void onAppCrashed(SharedMemory sharedMemory) throws RemoteException;

    void onExitInfoRecordAdded(ApplicationExitInfo applicationExitInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusEapDataCallback {
        @Override // com.oplus.eap.IOplusEapDataCallback
        public void onAppCrashed(SharedMemory data) throws RemoteException {
        }

        @Override // com.oplus.eap.IOplusEapDataCallback
        public void onExitInfoRecordAdded(ApplicationExitInfo exitInfo) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusEapDataCallback {
        static final int TRANSACTION_onAppCrashed = 1;
        static final int TRANSACTION_onExitInfoRecordAdded = 2;

        public Stub() {
            attachInterface(this, IOplusEapDataCallback.DESCRIPTOR);
        }

        public static IOplusEapDataCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusEapDataCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusEapDataCallback)) {
                return (IOplusEapDataCallback) iin;
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
                    return "onAppCrashed";
                case 2:
                    return "onExitInfoRecordAdded";
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
                data.enforceInterface(IOplusEapDataCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusEapDataCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            SharedMemory _arg0 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            data.enforceNoDataAvail();
                            onAppCrashed(_arg0);
                            return true;
                        case 2:
                            ApplicationExitInfo _arg02 = (ApplicationExitInfo) data.readTypedObject(ApplicationExitInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onExitInfoRecordAdded(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusEapDataCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusEapDataCallback.DESCRIPTOR;
            }

            @Override // com.oplus.eap.IOplusEapDataCallback
            public void onAppCrashed(SharedMemory data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusEapDataCallback.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.eap.IOplusEapDataCallback
            public void onExitInfoRecordAdded(ApplicationExitInfo exitInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusEapDataCallback.DESCRIPTOR);
                    _data.writeTypedObject(exitInfo, 0);
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
