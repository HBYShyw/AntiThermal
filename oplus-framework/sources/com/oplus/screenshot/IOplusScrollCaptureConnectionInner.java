package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;
import com.oplus.screenshot.IOplusScrollCaptureCallbacksInner;

/* loaded from: classes.dex */
public interface IOplusScrollCaptureConnectionInner extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusScrollCaptureConnectionInner";

    void close() throws RemoteException;

    ICancellationSignal endCapture() throws RemoteException;

    ICancellationSignal requestImage(Rect rect) throws RemoteException;

    ICancellationSignal startCapture(Surface surface, IOplusScrollCaptureCallbacksInner iOplusScrollCaptureCallbacksInner) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScrollCaptureConnectionInner {
        @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
        public ICancellationSignal startCapture(Surface surface, IOplusScrollCaptureCallbacksInner callbacks) throws RemoteException {
            return null;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
        public ICancellationSignal requestImage(Rect captureArea) throws RemoteException {
            return null;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
        public ICancellationSignal endCapture() throws RemoteException {
            return null;
        }

        @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
        public void close() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScrollCaptureConnectionInner {
        static final int TRANSACTION_close = 4;
        static final int TRANSACTION_endCapture = 3;
        static final int TRANSACTION_requestImage = 2;
        static final int TRANSACTION_startCapture = 1;

        public Stub() {
            attachInterface(this, IOplusScrollCaptureConnectionInner.DESCRIPTOR);
        }

        public static IOplusScrollCaptureConnectionInner asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScrollCaptureConnectionInner)) {
                return (IOplusScrollCaptureConnectionInner) iin;
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
                    return "startCapture";
                case 2:
                    return "requestImage";
                case 3:
                    return "endCapture";
                case 4:
                    return "close";
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
                data.enforceInterface(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            Surface _arg0 = (Surface) data.readTypedObject(Surface.CREATOR);
                            IOplusScrollCaptureCallbacksInner _arg1 = IOplusScrollCaptureCallbacksInner.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            ICancellationSignal _result = startCapture(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeStrongInterface(_result);
                            return true;
                        case 2:
                            Rect _arg02 = (Rect) data.readTypedObject(Rect.CREATOR);
                            data.enforceNoDataAvail();
                            ICancellationSignal _result2 = requestImage(_arg02);
                            reply.writeNoException();
                            reply.writeStrongInterface(_result2);
                            return true;
                        case 3:
                            ICancellationSignal _result3 = endCapture();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result3);
                            return true;
                        case 4:
                            close();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScrollCaptureConnectionInner {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScrollCaptureConnectionInner.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
            public ICancellationSignal startCapture(Surface surface, IOplusScrollCaptureCallbacksInner callbacks) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
                    _data.writeTypedObject(surface, 0);
                    _data.writeStrongInterface(callbacks);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    ICancellationSignal _result = ICancellationSignal.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
            public ICancellationSignal requestImage(Rect captureArea) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
                    _data.writeTypedObject(captureArea, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    ICancellationSignal _result = ICancellationSignal.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
            public ICancellationSignal endCapture() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    ICancellationSignal _result = ICancellationSignal.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScrollCaptureConnectionInner
            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScrollCaptureConnectionInner.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
