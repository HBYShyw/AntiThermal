package com.oplus.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.oplus.Telephony;
import com.oplus.screenshot.IOplusScreenshotCallback;

/* loaded from: classes.dex */
public interface IOplusScreenshot extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.screenshot.IOplusScreenshot";

    boolean isEdit() throws RemoteException;

    void start(IOplusScreenshotCallback iOplusScreenshotCallback) throws RemoteException;

    void stop() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusScreenshot {
        @Override // com.oplus.screenshot.IOplusScreenshot
        public void start(IOplusScreenshotCallback callback) throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshot
        public void stop() throws RemoteException {
        }

        @Override // com.oplus.screenshot.IOplusScreenshot
        public boolean isEdit() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusScreenshot {
        static final int TRANSACTION_isEdit = 3;
        static final int TRANSACTION_start = 1;
        static final int TRANSACTION_stop = 2;

        public Stub() {
            attachInterface(this, IOplusScreenshot.DESCRIPTOR);
        }

        public static IOplusScreenshot asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusScreenshot.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusScreenshot)) {
                return (IOplusScreenshot) iin;
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
                    return Telephony.BaseMmsColumns.START;
                case 2:
                    return "stop";
                case 3:
                    return "isEdit";
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
                data.enforceInterface(IOplusScreenshot.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusScreenshot.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusScreenshotCallback _arg0 = IOplusScreenshotCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            start(_arg0);
                            return true;
                        case 2:
                            stop();
                            return true;
                        case 3:
                            boolean _result = isEdit();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusScreenshot {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusScreenshot.DESCRIPTOR;
            }

            @Override // com.oplus.screenshot.IOplusScreenshot
            public void start(IOplusScreenshotCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshot.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshot
            public void stop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusScreenshot.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.screenshot.IOplusScreenshot
            public boolean isEdit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusScreenshot.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
