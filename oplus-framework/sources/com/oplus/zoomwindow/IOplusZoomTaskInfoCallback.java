package com.oplus.zoomwindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusZoomTaskInfoCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.zoomwindow.IOplusZoomTaskInfoCallback";

    void onZoomInfoCallback(OplusZoomTaskInfo oplusZoomTaskInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusZoomTaskInfoCallback {
        @Override // com.oplus.zoomwindow.IOplusZoomTaskInfoCallback
        public void onZoomInfoCallback(OplusZoomTaskInfo extention) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusZoomTaskInfoCallback {
        static final int TRANSACTION_onZoomInfoCallback = 1;

        public Stub() {
            attachInterface(this, IOplusZoomTaskInfoCallback.DESCRIPTOR);
        }

        public static IOplusZoomTaskInfoCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusZoomTaskInfoCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusZoomTaskInfoCallback)) {
                return (IOplusZoomTaskInfoCallback) iin;
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
                    return "onZoomInfoCallback";
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
                data.enforceInterface(IOplusZoomTaskInfoCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusZoomTaskInfoCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            OplusZoomTaskInfo _arg0 = (OplusZoomTaskInfo) data.readTypedObject(OplusZoomTaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onZoomInfoCallback(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusZoomTaskInfoCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusZoomTaskInfoCallback.DESCRIPTOR;
            }

            @Override // com.oplus.zoomwindow.IOplusZoomTaskInfoCallback
            public void onZoomInfoCallback(OplusZoomTaskInfo extention) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusZoomTaskInfoCallback.DESCRIPTOR);
                    _data.writeTypedObject(extention, 0);
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
