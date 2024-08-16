package com.oplus.eventhub.sdk.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IEventCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.eventhub.sdk.aidl.IEventCallback";

    void onEventStateChanged(DeviceEventResult deviceEventResult) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IEventCallback {
        @Override // com.oplus.eventhub.sdk.aidl.IEventCallback
        public void onEventStateChanged(DeviceEventResult deviceEventResult) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEventCallback {
        static final int TRANSACTION_onEventStateChanged = 1;

        public Stub() {
            attachInterface(this, IEventCallback.DESCRIPTOR);
        }

        public static IEventCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IEventCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IEventCallback)) {
                return (IEventCallback) iin;
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
                    return "onEventStateChanged";
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
                data.enforceInterface(IEventCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IEventCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            DeviceEventResult _arg0 = (DeviceEventResult) data.readTypedObject(DeviceEventResult.CREATOR);
                            data.enforceNoDataAvail();
                            onEventStateChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IEventCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IEventCallback.DESCRIPTOR;
            }

            @Override // com.oplus.eventhub.sdk.aidl.IEventCallback
            public void onEventStateChanged(DeviceEventResult deviceEventResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEventCallback.DESCRIPTOR);
                    _data.writeTypedObject(deviceEventResult, 0);
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
