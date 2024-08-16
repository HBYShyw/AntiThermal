package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusScrollCaptureCallbacksInner extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusScrollCaptureCallbacksInner";

    void onCaptureEnded() throws RemoteException;

    void onCaptureStarted() throws RemoteException;

    void onImageRequestCompleted(int i, Rect rect, Rect rect2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScrollCaptureCallbacksInner {
        @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
        public void onCaptureStarted() throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
        public void onImageRequestCompleted(int flags, Rect capturedArea, Rect screenArea) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
        public void onCaptureEnded() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScrollCaptureCallbacksInner {
        static final int TRANSACTION_onCaptureEnded = 3;
        static final int TRANSACTION_onCaptureStarted = 1;
        static final int TRANSACTION_onImageRequestCompleted = 2;

        public Stub() {
            attachInterface(this, IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
        }

        public static IOplusScrollCaptureCallbacksInner asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScrollCaptureCallbacksInner)) {
                return (IOplusScrollCaptureCallbacksInner) iin;
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
                    return "onCaptureStarted";
                case 2:
                    return "onImageRequestCompleted";
                case 3:
                    return "onCaptureEnded";
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
                data.enforceInterface(IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            onCaptureStarted();
                            return true;
                        case 2:
                            int _arg0 = data.readInt();
                            Rect _arg1 = (Rect) data.readTypedObject(Rect.CREATOR);
                            Rect _arg2 = (Rect) data.readTypedObject(Rect.CREATOR);
                            data.enforceNoDataAvail();
                            onImageRequestCompleted(_arg0, _arg1, _arg2);
                            return true;
                        case 3:
                            onCaptureEnded();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScrollCaptureCallbacksInner {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScrollCaptureCallbacksInner.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
            public void onCaptureStarted() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
            public void onImageRequestCompleted(int flags, Rect capturedArea, Rect screenArea) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeTypedObject(capturedArea, 0);
                    _data.writeTypedObject(screenArea, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureCallbacksInner
            public void onCaptureEnded() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureCallbacksInner.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
